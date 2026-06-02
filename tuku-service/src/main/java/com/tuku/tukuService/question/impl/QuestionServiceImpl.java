package com.tuku.tukuService.question.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.es.document.QuestionEsDoc;
import com.tuku.es.query.QueEsDto;
import com.tuku.tukuMapper.QuestionMapper;
import com.tuku.tukuModel.dto.question.*;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.question.QuePageVo;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.question.IQuestionBankQuestionService;
import com.tuku.tukuService.question.IQuestionBankService;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.SearchPoints;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Resource
    private IUserService userService;

    @Resource
    private IQuestionBankService questionBankService;

    @Resource
    private IQuestionBankQuestionService questionBankQuestionService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private QdrantClient qdrantClient;

    private static final String QDRANT_COLLECTION_NAME = "question_collection";

    @Override
    public Page<QuePageVo> listQuestionByPage(QueQueryDto queQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queQueryDto == null, ErrorCode.PARAMS_ERROR);

        String title = queQueryDto.getTitle();
        String content = queQueryDto.getContent();
        Long userId = queQueryDto.getUserId();
        String searchText = queQueryDto.getSearchText();
        int pageNum = queQueryDto.getPageNum();
        int pageSize = queQueryDto.getPageSize();

        Page<Question> questionPage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(title), Question::getTitle, title)
                .like(StrUtil.isNotBlank(content), Question::getContent, content)
                .eq(userId != null, Question::getUserId, userId)
                .and(StrUtil.isNotBlank(searchText), wrapper -> wrapper
                        .like(Question::getTitle, searchText)
                        .or()
                        .like(Question::getContent, searchText))
                .page(new Page<>(pageNum, pageSize));

        Page<QuePageVo> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(questionPage.getTotal());
        pageVO.setSize(questionPage.getSize());
        pageVO.setCurrent(questionPage.getCurrent());
        List<QuePageVo> quePageVoList = questionPage.getRecords().stream()
                .map(this::entityToPageVo)
                .collect(Collectors.toList());
        pageVO.setRecords(quePageVoList);

        return pageVO;
    }

    @Override
    public QuePageVo getQuestionDetail(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        QuePageVo quePageVo = entityToPageVo(question);
        return quePageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addQuestion(QueAddDto queAddDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queAddDto == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = new Question();
        BeanUtil.copyProperties(queAddDto, question);
        question.setUserId(loginUser.getId());
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        question.setEditTime(new Date());

        this.save(question);
        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestion(QueUpdDto queUpdDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdDto == null || queUpdDto.getId() == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(queUpdDto.getId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        Question question = new Question();
        BeanUtil.copyProperties(queUpdDto, question);
        question.setUpdateTime(new Date());
        question.setEditTime(new Date());

        return this.updateById(question);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        return this.removeById(id);
    }

    @Override
    public Page<QuePageVo> listQuestionByBankId(QueByBankQueryDto queByBankQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queByBankQueryDto == null, ErrorCode.PARAMS_ERROR);

        Long questionBankId = queByBankQueryDto.getQuestionBankId();
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0, ErrorCode.PARAMS_ERROR, "题库id不能为空");

        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        String searchText = queByBankQueryDto.getSearchText();
        int pageNum = queByBankQueryDto.getPageNum();
        int pageSize = queByBankQueryDto.getPageSize();

        LambdaQueryWrapper<QuestionBankQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionBankQuestion::getQuestionBankId, questionBankId);

        Page<QuestionBankQuestion> questionBankQuestionPage = questionBankQuestionService
                .page(new Page<>(pageNum, pageSize), queryWrapper);

        List<Long> questionIdList = questionBankQuestionPage.getRecords().stream()
                .map(QuestionBankQuestion::getQuestionId)
                .collect(Collectors.toList());

        Page<QuePageVo> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(questionBankQuestionPage.getTotal());
        pageVO.setSize(questionBankQuestionPage.getSize());
        pageVO.setCurrent(questionBankQuestionPage.getCurrent());

        if (questionIdList.isEmpty()) {
            pageVO.setRecords(List.of());
            return pageVO;
        }

        List<Question> questionList = this.lambdaQuery()
                .in(Question::getId, questionIdList)
                .and(StrUtil.isNotBlank(searchText), wrapper -> wrapper
                        .like(Question::getTitle, searchText)
                        .or()
                        .like(Question::getContent, searchText))
                .list();

        List<QuePageVo> quePageVoList = questionList.stream()
                .map(this::entityToPageVo)
                .collect(Collectors.toList());
        pageVO.setRecords(quePageVoList);

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestionBank(QueUpdateBankDto queUpdateBankDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdateBankDto == null || queUpdateBankDto.getQuestionId() == null,
                ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Long questionId = queUpdateBankDto.getQuestionId();
        Question question = this.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        List<Long> questionBankIds = queUpdateBankDto.getQuestionBankIds();

        questionBankQuestionService.remove(new LambdaQueryWrapper<QuestionBankQuestion>()
                .eq(QuestionBankQuestion::getQuestionId, questionId));

        if (questionBankIds != null && !questionBankIds.isEmpty()) {
            List<QuestionBankQuestion> questionBankQuestionList = questionBankIds.stream()
                    .map(questionBankId -> getQuesBankQueById(questionBankId, questionId, loginUser))
                    .collect(Collectors.toList());

            questionBankQuestionService.saveBatch(questionBankQuestionList);
        }

        return true;
    }

    @Override
    public List<Question> queryQueByHybridSearch(QueEsDto queEsDto) {
        if (queEsDto == null || StrUtil.isBlank(queEsDto.getQuestionSearchText())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "搜索文本不能为空");
        }
        String searchText = queEsDto.getQuestionSearchText();

        // 1. 从 Elasticsearch 进行关键词检索
        List<Long> esIds = searchFromEs(searchText);

        // 2. 从 Qdrant 进行向量检索
        List<Long> qdrantIds = searchFromQdrant(searchText);

        // 3. 使用 RRF (Reciprocal Rank Fusion) 合并结果
        List<Long> mergedIds = rrfMerge(esIds, qdrantIds);

        if (CollUtil.isEmpty(mergedIds)) {
            return new ArrayList<>();
        }

        // 4. 根据合并后的 ID 列表从数据库查询完整的题目信息，并保持顺序
        Map<Long, Question> questionMap = this.listByIds(mergedIds).stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        return mergedIds.stream()
                .map(questionMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 从 Elasticsearch 检索 ID 列表
     */
    private List<Long> searchFromEs(String searchText) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText).boost(2.0f));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
        boolQueryBuilder.minimumShouldMatch(1);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 50))
                .build();

        SearchHits<QuestionEsDoc> searchHits = elasticsearchRestTemplate.search(searchQuery, QuestionEsDoc.class);
        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent().getId())
                .collect(Collectors.toList());
    }

    /**
     * 从 Qdrant 检索 ID 列表
     */
    private List<Long> searchFromQdrant(String searchText) {
//        try {
//            // 获取文本向量（此处应替换为真实的 Embedding 模型调用）
//            List<Float> vector = getEmbedding(searchText);
//
//            SearchPoints searchPoints = SearchPoints.newBuilder()
//                    .setCollectionName(QDRANT_COLLECTION_NAME)
//                    .addAllVector(vector)
//                    .setLimit(50)
//                    .setWithPayload(newBuilder().setEnable(true).build())
//                    .build();
//
//            List<Points.ScoredPoint> scoredPoints = qdrantClient.searchAsync(searchPoints).get();
//            return scoredPoints.stream()
//                    .map(point -> point.getId().getNum())
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error("Qdrant 搜索失败", e);
//            return new ArrayList<>();
//        }
        return null;
    }

    /**
     * RRF 合并算法
     */
    private List<Long> rrfMerge(List<Long> esIds, List<Long> qdrantIds) {
        Map<Long, Double> scoreMap = new HashMap<>();
        int k = 60; // RRF 常数

        // 计算 ES 排名得分
        for (int i = 0; i < esIds.size(); i++) {
            Long id = esIds.get(i);
            scoreMap.put(id, scoreMap.getOrDefault(id, 0.0) + 1.0 / (k + i + 1));
        }

        // 计算 Qdrant 排名得分
        for (int i = 0; i < qdrantIds.size(); i++) {
            Long id = qdrantIds.get(i);
            scoreMap.put(id, scoreMap.getOrDefault(id, 0.0) + 1.0 / (k + i + 1));
        }

        // 按得分降序排序
        return scoreMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取 Embedding 向量（模拟实现）
     * 实际生产中应调用如 OpenAI, HuggingFace 或本地部署的向量化模型
     */
    private List<Float> getEmbedding(String text) {
        // 模拟生成 384 维向量（常用小模型维度）
        List<Float> vector = new ArrayList<>(384);
        Random random = new Random(text.hashCode());
        for (int i = 0; i < 384; i++) {
            vector.add(random.nextFloat());
        }
        return vector;
    }

    private QuePageVo entityToPageVo(Question question) {
        if (question == null) {
            return null;
        }
        QuePageVo vo = new QuePageVo();
        BeanUtil.copyProperties(question, vo);
        return vo;
    }

    private QuestionBankQuestion getQuesBankQueById(Long questionBankId, Long questionId, LoginUserVo loginUser) {
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        questionBankQuestion.setQuestionBankId(questionBankId);
        questionBankQuestion.setQuestionId(questionId);
        questionBankQuestion.setUserId(loginUser.getId());
        questionBankQuestion.setCreateTime(new Date());
        questionBankQuestion.setUpdateTime(new Date());
        return questionBankQuestion;
    }

    private List<Question> queryEsByQueEsDto(QueQueryDto queQueryDto) {
        return null;
    }

    // TODO 从Qdrant中查询数据的
}
