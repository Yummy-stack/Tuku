package com.tuku.tukuService.picture.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;

import com.tuku.es.document.PictureEsDTO;
import com.tuku.es.repository.PictureEsRepository;
import com.tuku.manager.CacheManager;
import com.tuku.tukuMapper.PictureMapper;
import com.tuku.tukuMapper.TaskinfoLogsMapper;
import com.tuku.tukuMapper.TaskinfoMapper;
import com.tuku.tukuModel.bo.result.picture.UploadLocalResult;
import com.tuku.tukuModel.bo.task.TaskPicBo;
import com.tuku.tukuModel.dto.picture.PictureReviewRequest;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.entity.task.Taskinfo;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.enums.task.TaskTypeEnum;
import com.tuku.tukuModel.request.picture.PictureEditRequest;
import com.tuku.tukuModel.request.picture.PictureQueryRequest;
import com.tuku.tukuModel.request.picture.PictureUploadByBatchRequest;
import com.tuku.tukuModel.request.picture.PictureUploadRequest;
import com.tuku.tukuModel.vo.picture.PagePictureVo;
import com.tuku.tukuModel.vo.picture.PictureVO;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.picture.IPictureService;
import com.tuku.tukuService.task.ITaskinfoLogsService;
import com.tuku.tukuService.task.ITaskinfoService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.manager.FileManager;
import com.tuku.tukucommon.utils.ProtostuffUtil;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cn.hutool.core.collection.CollUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.tuku.tukucommon.constant.redis.KeyConstant.PICTURE_DETAIL;

@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {
    @Resource
    private IUserService userService;

    @Resource
    private FileManager fileManager;

    @Resource
    private TaskinfoMapper taskinfoMapper;

    @Resource
    private TaskinfoLogsMapper taskinfoLogsMapper;

    @Resource
    private ITaskinfoService taskinfoService;

    @Resource
    private ITaskinfoLogsService taskinfoLogsService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private PictureEsRepository pictureEsRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public static final Cache<Object, Object> pictureCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PictureVO upLoadPicture(PictureUploadRequest pictureUploadRequest, MultipartFile multipartFile,
                                   Long userId) {
        // 参数校验
        ThrowUtils.throwIf(multipartFile.isEmpty(), ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(userId == null, "用户未登录");
        validPicture(multipartFile);
        // 主体逻辑
        Picture picture = new Picture();
        if (pictureUploadRequest != null && pictureUploadRequest.getId() != null) {
            boolean isExist = this.lambdaQuery()
                    .eq(Picture::getId, pictureUploadRequest.getId())
                    .exists();
            ThrowUtils.throwIf(!isExist, "更新失败，图片不存在");
            picture.setId(pictureUploadRequest.getId());
            picture.setEditTime(new Date());
        }

        UploadLocalResult uploadLocalResult = fileManager.uploadPictureToLocal(multipartFile, userId);
        BeanUtil.copyProperties(pictureUploadRequest, picture);
        BeanUtil.copyProperties(uploadLocalResult, picture);
        setCreateTimeAndUser(picture, userId);
        picture.setReviewStatus(0);
        this.saveOrUpdate(picture);

        // 开启延迟任务 - 超时自动审核通过
        Taskinfo taskinfo = new Taskinfo();
        long taskTime = new Date().getTime() + 30 * 60 * 1000L;
        taskinfo.setExecuteTime(new Date(taskTime));

        taskinfo.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        taskinfo.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());

        taskinfo.setCreateTime(new Date());
        taskinfo.setUpdateTime(new Date());
        taskinfo.setCreateUser(userId);

        TaskPicBo taskPicBo = new TaskPicBo();
        // taskPicBo.(picture.getId());
        // taskPicBo.setExecuteTime(new Date(taskTime));
        byte[] taskPicBoSerialize = ProtostuffUtil.serialize(taskPicBo);
        taskinfo.setParameters(taskPicBoSerialize);
        taskinfoService.save(taskinfo);

        PictureVO pictureVO = PictureVO.entityToVo(picture);

        // 删除对应的缓存，保证缓存一致性
        try {
            String cacheKey = PICTURE_DETAIL + picture.getId();
            cacheManager.delete(cacheKey);
        } catch (Exception e) {
            log.error("缓存删除失败: {}", e.getMessage());
        }

        return pictureVO;
    }

    @Override
    public Picture getPictureById(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Picture picture = this.lambdaQuery()
                .eq(Picture::getId, id)
                .one();
        return picture;
    }

    @Override
    public PictureVO getPictureVOById(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Picture picture = this.lambdaQuery()
                .eq(Picture::getId, id)
                .one();
        PictureVO pictureVO = PictureVO.entityToVo(picture);
        return pictureVO;
    }

    @Override
    public Page<Picture> listPictureByPage(PictureQueryRequest pictureQueryRequest) {
        ThrowUtils.throwIf(pictureQueryRequest == null, ErrorCode.PARAMS_ERROR);

        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        String searchText = pictureQueryRequest.getSearchText();
        int pageNum = pictureQueryRequest.getPageNum();
        int pageSize = pictureQueryRequest.getPageSize();
        Page<Picture> picturePage = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(category), Picture::getPicCategory, category)
                .in(tags != null && !tags.isEmpty(), Picture::getPicTags, tags)
                .and(StrUtil.isNotBlank(searchText),
                        i -> i.like(StrUtil.isNotBlank(category), Picture::getPicName, searchText)
                                .or().like(StrUtil.isNotBlank(category), Picture::getPicIntroduction, searchText))
                .page(new Page<>(pageNum, pageSize));

        return picturePage;
    }

    @Override
    public Page<PictureVO> listPictureVOByPage(PictureQueryRequest pictureQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, ErrorCode.PARAMS_ERROR);

        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        String searchText = pictureQueryRequest.getSearchText();
        Date startEditTime = pictureQueryRequest.getStartEditTime();
        Date endEditTime = pictureQueryRequest.getEndEditTime();
        int pageNum = pictureQueryRequest.getPageNum();
        int pageSize = pictureQueryRequest.getPageSize();
        Page<Picture> picturePage = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(category), Picture::getPicCategory, category)
                .in(tags != null && !tags.isEmpty(), Picture::getPicTags, tags)
                .and(StrUtil.isNotBlank(searchText),
                        w -> w.like(StrUtil.isNotBlank(searchText), Picture::getPicName, searchText)
                                .or().like(StrUtil.isNotBlank(searchText), Picture::getPicIntroduction, searchText))
                .gt(startEditTime != null, Picture::getEditTime, startEditTime)
                .lt(endEditTime != null, Picture::getEditTime, endEditTime)
                .page(new Page<>(pageNum, pageSize));

        Page<PictureVO> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(picturePage.getTotal());
        pageVO.setSize(picturePage.getSize());
        pageVO.setCurrent(picturePage.getCurrent());
        List<PictureVO> pictureVOList = picturePage.getRecords().stream()
                .map(PictureVO::entityToVo)
                .collect(Collectors.toList());
        pageVO.setRecords(pictureVOList);

        return pageVO;
    }

    @Override
    public Page<PictureVO> listPictureVOByPageV2(PictureQueryRequest pictureQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, ErrorCode.PARAMS_ERROR);

        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        String searchText = pictureQueryRequest.getSearchText();
        int pageNum = pictureQueryRequest.getPageNum();
        int pageSize = pictureQueryRequest.getPageSize();
        Page<Picture> picturePage = this.lambdaQuery()
                .eq(Picture::getReviewStatus, 1)
                .eq(StrUtil.isNotBlank(category), Picture::getPicCategory, category)
                .and(StrUtil.isNotBlank(searchText),
                        w -> w.like(StrUtil.isNotBlank(searchText), Picture::getPicName, searchText)
                                .or().like(StrUtil.isNotBlank(searchText), Picture::getPicIntroduction, searchText))
                .page(new Page<>(pageNum, pageSize));

        // 根据标签来进行过滤
        if (tags != null && !tags.isEmpty()) {
            List<Long> pictureIdList = picturePage.getRecords().stream()
                    .filter(picture -> filterPictureByTag(picture, tags))
                    .map(Picture::getId)
                    .collect(Collectors.toList());
            if (pictureIdList.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            } else {
                picturePage = this.lambdaQuery()
                        .in(Picture::getId, pictureIdList)
                        .page(new Page<>(pageNum, pageSize));
            }
        }

        Page<PictureVO> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(picturePage.getTotal());
        pageVO.setSize(picturePage.getSize());
        pageVO.setCurrent(picturePage.getCurrent());
        List<PictureVO> pictureVOList = picturePage.getRecords().stream()
                .map(PictureVO::entityToVo)
                .collect(Collectors.toList());
        pageVO.setRecords(pictureVOList);

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PictureVO upLoadPictureByUrl(PictureUploadRequest pictureUploadRequest, String uploadUrl, Long userId) {
        ThrowUtils.throwIf(uploadUrl == null, "上传的图片Url为空");
        ThrowUtils.throwIf(userId == null, "用户未登录");

        Picture picture = new Picture();
        if (pictureUploadRequest != null && pictureUploadRequest.getId() != null) {
            boolean isExist = this.lambdaQuery()
                    .eq(Picture::getId, pictureUploadRequest.getId())
                    .exists();
            ThrowUtils.throwIf(!isExist, "更新失败，图片不存在");
            picture.setId(pictureUploadRequest.getId());
            picture.setEditTime(new Date());
        }
        UploadLocalResult uploadLocalResult = fileManager.uploadPictureToLocalByUrl(uploadUrl, userId);
        BeanUtil.copyProperties(pictureUploadRequest, picture);
        BeanUtil.copyProperties(uploadLocalResult, picture);
        setCreateTimeAndUser(picture, userId);
        picture.setReviewStatus(0);

        this.saveOrUpdate(picture);

        PictureVO pictureVO = PictureVO.entityToVo(picture);

        // 删除对应的缓存，保证缓存一致性
        try {
            String cacheKey = PICTURE_DETAIL + picture.getId();
            cacheManager.delete(cacheKey);
        } catch (Exception e) {
            log.error("缓存删除失败: {}", e.getMessage());
        }

        return pictureVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewPicture(PictureReviewRequest pictureReviewRequest, Long userId) {
        ThrowUtils.throwIf(pictureReviewRequest == null, "请传入审核参数Dto");
        ThrowUtils.throwIf(userId == null, "用户未登录不能审核");

        Picture picture = this.lambdaQuery()
                .eq(Picture::getId, pictureReviewRequest.getId())
                .one();
        ThrowUtils.throwIf(picture == null, "审查的图片不存在");

        Integer reviewStatus = pictureReviewRequest.getReviewStatus();
        ThrowUtils.throwIf(reviewStatus == null, "审核参数为空");
        if (reviewStatus != 0 && reviewStatus.equals(picture.getReviewStatus())) {
            throw new RuntimeException("审核参数错误");
        }

        boolean updateResult = this.lambdaUpdate()
                .eq(Picture::getId, pictureReviewRequest.getId())
                .set(Picture::getReviewStatus, pictureReviewRequest.getReviewStatus())
                .set(Picture::getReviewMessage, pictureReviewRequest.getReviewMessage())
                .set(Picture::getReviewUser, userId)
                .set(Picture::getReviewTime, LocalDateTime.now())
                .update();

        return updateResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upLoadPicturesBySearch(PictureUploadByBatchRequest pictureUploadByBatchRequest,
                                      LoginUserVo loginUserVo) {
        String searchText = pictureUploadByBatchRequest.getSearchText();
        // 格式化数量
        Integer count = pictureUploadByBatchRequest.getCount();
        ThrowUtils.throwIf(count > 30, ErrorCode.PARAMS_ERROR, "最多 30 条");
        // 要抓取的地址
        String fetchUrl = String.format("https://cn.bing.com/images/async?q=%s&mmasync=1", searchText);
        Document document;
        try {
            document = Jsoup.connect(fetchUrl).get();
        } catch (IOException e) {
            log.error("获取页面失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取页面失败");
        }
        Element div = document.getElementsByClass("dgControl").first();
        if (ObjUtil.isNull(div)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取元素失败");
        }
        Elements imgElementList = div.select("img.mimg");
        int uploadCount = 0;
        for (Element imgElement : imgElementList) {
            String fileUrl = imgElement.attr("src");
            if (StrUtil.isBlank(fileUrl)) {
                log.info("当前链接为空，已跳过: {}", fileUrl);
                continue;
            }
            // 处理图片上传地址，防止出现转义问题
            int questionMarkIndex = fileUrl.indexOf("?");
            if (questionMarkIndex > -1) {
                fileUrl = fileUrl.substring(0, questionMarkIndex);
            }
            // 上传图片
            PictureUploadRequest pictureUploadRequest = new PictureUploadRequest();
            try {
                PictureVO pictureVO = this.upLoadPictureByUrl(pictureUploadRequest, fileUrl, loginUserVo.getId());
                log.info("图片上传成功, id = {}", pictureVO.getId());
                uploadCount++;
            } catch (Exception e) {
                log.error("图片上传失败", e);
                continue;
            }
            if (uploadCount >= count) {
                break;
            }
        }
        return uploadCount;
    }

    @Override
    public boolean editPicture(PictureEditRequest pictureEditRequest, LoginUserVo loginUserVo) {
        ThrowUtils.throwIf(loginUserVo == null, "用户未登录");

        Long id = pictureEditRequest.getId();
        ThrowUtils.throwIf(id == null, "编辑的图片id未空");

        String name = pictureEditRequest.getName();
        String introduction = pictureEditRequest.getIntroduction();
        String category = pictureEditRequest.getCategory();
        List<String> tags = pictureEditRequest.getTags();
        String stringTags = StrUtil.join(",", tags);

        boolean editResult = this.lambdaUpdate()
                .eq(Picture::getId, id)
                .set(Picture::getPicName, name)
                .set(Picture::getPicIntroduction, introduction)
                .set(Picture::getPicCategory, category)
                .set(Picture::getPicTags, stringTags)
                .set(Picture::getEditTime, LocalDateTime.now())
                .update();

        // 删除对应的缓存，保证缓存一致性
        if (editResult) {
            try {
                String cacheKey = PICTURE_DETAIL + id;
                cacheManager.delete(cacheKey);
            } catch (Exception e) {
                log.error("缓存删除失败: {}", e.getMessage());
            }
        }

        return editResult;
    }

    @Override
    public Page<PictureVO> listPictureVOByPageV2WithCache(PictureQueryRequest pictureQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null || request == null, "参数为null");

        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        String searchText = pictureQueryRequest.getSearchText();
        int pageNum = pictureQueryRequest.getPageNum();
        int pageSize = pictureQueryRequest.getPageSize();

        // 生成缓存键
        String cacheKey = "Tuku:picture:page:" + category + ":" + searchText + ":" + pageNum + ":" + pageSize;
        if (tags != null && !tags.isEmpty()) {
            cacheKey += ":" + StrUtil.join(",", tags);
        }

        // 尝试从缓存获取
        Page<PictureVO> cachedPage = (Page<PictureVO>) cacheManager.get(cacheKey);
        if (cachedPage != null) {
            return cachedPage;
        }

        // 缓存未命中，从数据库获取
        Page<Picture> picturePage = this.lambdaQuery()
                .eq(Picture::getReviewStatus, 1)
                .eq(StrUtil.isNotBlank(category), Picture::getPicCategory, category)
                .and(StrUtil.isNotBlank(searchText), w -> w.like(StrUtil.isNotBlank(searchText), Picture::getPicName, searchText)
                        .or().like(StrUtil.isNotBlank(searchText), Picture::getPicIntroduction, searchText))
                .page(new Page<>(pageNum, pageSize));

        //根据标签来进行过滤
        if (tags != null && !tags.isEmpty()) {
            List<Long> pictureIdList = picturePage.getRecords().stream()
                    .filter(picture -> filterPictureByTag(picture, tags))
                    .map(Picture::getId)
                    .toList();
            if (pictureIdList.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            } else {
                picturePage = this.lambdaQuery()
                        .in(Picture::getId, pictureIdList)
                        .page(new Page<>(pageNum, pageSize));
            }
        }

        Page<PictureVO> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(picturePage.getTotal());
        pageVO.setSize(picturePage.getSize());
        pageVO.setCurrent(picturePage.getCurrent());
        List<PictureVO> pictureVOList = picturePage.getRecords().stream()
                .map(PictureVO::entityToVo)
                .collect(Collectors.toList());
        pageVO.setRecords(pictureVOList);

        // 设置缓存，使用随机过期时间防止缓存雪崩
        try {
            int expireTime = cacheManager.getRandomExpireTime(30, 10);
            cacheManager.set(cacheKey, pageVO, expireTime);
        } catch (Exception e) {
            log.error("------ 缓存设置失败: {} ------", e.getMessage());
        }

        return pageVO;
    }

    @Override
    public PictureVO getPictureVOByIdWithCache(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);

        String cacheKey = PICTURE_DETAIL + id;

        // 1. 检查布隆过滤器，防止缓存穿透
        if (!cacheManager.mightContain(cacheKey)) {
            Picture picture = this.lambdaQuery()
                    .eq(Picture::getId, id)
                    .one();
            if (picture == null) {
                // 将不存在的ID加入布隆过滤器，防止缓存穿透
                cacheManager.set(cacheKey, null, 5);
                return null;
            }
        }

        // 2. 使用CacheManager获取缓存
        PictureVO pictureVO = (PictureVO) cacheManager.get(cacheKey);
        if (pictureVO != null) {
            return pictureVO;
        }

        // 3. 缓存未命中，从数据库获取
        Picture picture = this.lambdaQuery()
                .eq(Picture::getId, id)
                .one();
        if (picture == null) {
            // 将不存在的ID加入布隆过滤器，防止缓存穿透
            cacheManager.set(cacheKey, null, 5);
            return null;
        }

        pictureVO = PictureVO.entityToVo(picture);

        try {
            // 4. 设置缓存，使用随机过期时间防止缓存雪崩
            int expireTime = cacheManager.getRandomExpireTime(200, 100);
            cacheManager.set(cacheKey, pictureVO, expireTime);
        } catch (Exception e) {
            log.error("--------- 缓存设置失败: {} --------", e.getMessage());
        }

        return pictureVO;
    }

    @Override
    public PictureVO upLoadPictureV2(PictureUploadRequest pictureUploadRequest, MultipartFile multipartFile, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(multipartFile.isEmpty(), ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(userId == null, "用户未登录");
        validPicture(multipartFile);
        // 主体逻辑
        Picture picture = new Picture();
        if (pictureUploadRequest != null && pictureUploadRequest.getId() != null) {
            boolean isExist = this.lambdaQuery()
                    .eq(Picture::getId, pictureUploadRequest.getId())
                    .exists();
            ThrowUtils.throwIf(!isExist, "更新失败，图片不存在");
            picture.setId(pictureUploadRequest.getId());
            picture.setEditTime(new Date());
        }

        UploadLocalResult uploadLocalResult = fileManager.uploadPictureToCOS(multipartFile, userId);
        BeanUtil.copyProperties(pictureUploadRequest, picture);
        BeanUtil.copyProperties(uploadLocalResult, picture);
        setCreateTimeAndUser(picture, userId);
        picture.setReviewStatus(0);
        this.saveOrUpdate(picture);

        PictureVO pictureVO = PictureVO.entityToVo(picture);

        // 删除对应的缓存，保证缓存一致性
        try {
            String cacheKey = PICTURE_DETAIL + picture.getId();
            cacheManager.delete(cacheKey);
        } catch (Exception e) {
            log.error("------- 缓存删除失败: {} -------", e.getMessage());
        }

        return pictureVO;
    }

    @Override
    public void downloadPictureV2(String filePath, HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, ErrorCode.PARAMS_ERROR);
        Picture picture = this.lambdaQuery()
                .eq(Picture::getPicUrl, filePath)
                .one();
        fileManager.downloadPictureFromCOS(filePath, picture, response);
    }

    @Override
    public Page<PictureVO> searchFromEs(PictureQueryRequest pictureQueryRequest) {
        ThrowUtils.throwIf(pictureQueryRequest == null, ErrorCode.PARAMS_ERROR);

        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        String searchText = pictureQueryRequest.getSearchText();
        Long userId = pictureQueryRequest.getUserId();
        Date startEditTime = pictureQueryRequest.getStartEditTime();
        Date endEditTime = pictureQueryRequest.getEndEditTime();
        int current = pictureQueryRequest.getPageNum();
        int pageSize = pictureQueryRequest.getPageSize();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("createUser", userId));
        }
        if (StrUtil.isNotBlank(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("picCategory", category));
        }
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("picTags", tag));
            }
        }
        if (startEditTime != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("editTime").gt(startEditTime.getTime()));
        }
        if (endEditTime != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("editTime").lt(endEditTime.getTime()));
        }
        // 审核状态必须为通过
        boolQueryBuilder.filter(QueryBuilders.termQuery("reviewStatus", 1));

        // 搜索
        if (StrUtil.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("picName", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("picIntroduction", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        if (StrUtil.isNotBlank(name)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("picName", name));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        if (StrUtil.isNotBlank(introduction)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("picIntroduction", introduction));
            boolQueryBuilder.minimumShouldMatch(1);
        }

        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StrUtil.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order("ascend".equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }

        // 分页
        PageRequest pageRequest = PageRequest.of(current - 1, pageSize);

        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSorts(sortBuilder)
                .build();

        SearchHits<PictureEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PictureEsDTO.class);

        Page<PictureVO> page = new Page<>(current, pageSize, searchHits.getTotalHits());
        List<PictureVO> resourceList = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            List<SearchHit<PictureEsDTO>> searchHitList = searchHits.getSearchHits();
            for (SearchHit<PictureEsDTO> searchHit : searchHitList) {
                resourceList.add(PictureVO.entityToVo(PictureEsDTO.dtoToObj(searchHit.getContent())));
            }
        }
        page.setRecords(resourceList);
        return page;
    }

    @Override
    public void downloadPicture(String filePath, HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, ErrorCode.PARAMS_ERROR);
        fileManager.downloadPictureFromLocal(filePath, response);

    }

    private PagePictureVo toPictureVo(Picture picture) {
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR, "参数为空");
        PagePictureVo pagePictureVo = BeanUtil.copyProperties(picture, PagePictureVo.class);
        return pagePictureVo;
    }

    private void setCreateTimeAndUser(Picture picture, Long userId) {
        picture.setCreateTime(new Date());
        picture.setUpdateTime(new Date());
        if (picture.getEditTime() == null) {
            picture.setEditTime(new Date());
        }
        picture.setCreateUser(userId);
        picture.setUpdateUser(userId);
    }

    private void validPicture(MultipartFile multipartFile) {
        // 校验参数
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.NOT_FOUND_ERROR);
        // 逻辑实现
        Long size = multipartFile.getSize();
        Long MaxSize = 1024 * 1024L * 2;
        ThrowUtils.throwIf(MaxSize.compareTo(size) < 0, ErrorCode.NOT_FOUND_ERROR, "文件不能超过2M");

        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        List<String> pictureSuffixes = Arrays.asList("png", "jpg", "jpeg", "gif", "bmp", "webp");
        ThrowUtils.throwIf(!pictureSuffixes.contains(suffix), "不支持该图片类型");
    }

    private boolean filterPictureByTag(Picture picture, List<String> tags) {
        String picTags = picture.getPicTags();
        List<String> tagList = Arrays.asList(picTags.split(","));
        for (String tag : tags) {
            if (!tagList.contains(tag)) {
                return false;
            }
        }
        return true;
    }
}
