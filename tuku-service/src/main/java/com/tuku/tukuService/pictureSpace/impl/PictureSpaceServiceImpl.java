package com.tuku.tukuService.pictureSpace.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tuku.tukuMapper.PictureSpaceMapper;
import com.tuku.tukuModel.bo.pictureSpace.AddOnePicToSpaceBO;
import com.tuku.tukuModel.dto.pictureSpace.*;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.entity.pictureAndPictureSpace.PictureAndPictureSpace;
import com.tuku.tukuModel.entity.pictureSpace.PictureSpace;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.enums.picture.PictureReviewStatusEnum;
import com.tuku.tukuModel.enums.pictureSpace.PictureSpaceLevelEnum;
import com.tuku.tukuModel.vo.picture.PictureVO;
import com.tuku.tukuModel.vo.pictureSpace.PicSpaDetailVO;
import com.tuku.tukuService.picture.IPictureService;
import com.tuku.tukuService.pictureAndPictureSpace.IPictureAndPictureSpaceService;
import com.tuku.tukuService.pictureSpace.IPictureSpaceService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.NumberUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import com.tuku.tukucommon.utils.user.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.tuku.tukucommon.utils.NumberUtils.ifNullToZero;


@Service
@Slf4j
public class PictureSpaceServiceImpl extends ServiceImpl<PictureSpaceMapper, PictureSpace> implements IPictureSpaceService {
    @Resource
    private IPictureAndPictureSpaceService pictureAndPictureSpaceService;

    @Resource
    private IPictureService pictureService;

    @Resource
    private IUserService userService;

    @Resource
    private PictureSpaceMapper pictureSpaceMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RedisTemplate<String, Objects> redisTemplate;

    @Override
    public Page<PictureSpace> listPictureSpaceByPage(PicSpaPageListDto picSpaPageListDto) {
        ThrowUtils.throwIf(picSpaPageListDto == null, ErrorCode.PARAMS_ERROR);

        String spaceName = picSpaPageListDto.getSpaceName();
        Date leftCreateTime = picSpaPageListDto.getLeftCreateTime();
        Date rightCreateTime = picSpaPageListDto.getRightCreateTime();
        int pageNum = picSpaPageListDto.getPageNum();
        int pageSize = picSpaPageListDto.getPageSize();
        Page<PictureSpace> pictureSpacePage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(spaceName), PictureSpace::getSpaceName, spaceName)
                .ge(leftCreateTime != null, PictureSpace::getCreateTime, leftCreateTime)
                .lt(rightCreateTime != null, PictureSpace::getCreateTime, rightCreateTime)
                .page(new Page<>(pageNum, pageSize));

        return pictureSpacePage;
    }

    @Override
    public boolean deletePictureSpace(PicSpaceDeleteDto picSpaceDeleteDto) {
        ThrowUtils.throwIf(picSpaceDeleteDto == null, ErrorCode.PARAMS_ERROR);

        boolean removeResult = this.lambdaUpdate()
                .eq(PictureSpace::getId, picSpaceDeleteDto.getId())
                .remove();

        return removeResult;
    }

    @Override
    public PicSpaDetailVO detailPictureSpace(PicSpaDetailDto picSpaDetailDto) {
        ThrowUtils.throwIf(picSpaDetailDto == null, ErrorCode.PARAMS_ERROR);

        PictureSpace pictureSpace = this.lambdaQuery()
                .eq(PictureSpace::getId, picSpaDetailDto.getId())
                .one();
        PicSpaDetailVO picSpaDetailVo = new PicSpaDetailVO();
        BeanUtil.copyProperties(pictureSpace, picSpaDetailVo);

        List<PictureAndPictureSpace> pictureAndPictureSpaces = pictureAndPictureSpaceService.lambdaQuery()
                .eq(PictureAndPictureSpace::getPictureSpaceId, picSpaDetailDto.getId())
                .list();
        List<Long> picIds = pictureAndPictureSpaces.stream()
                .map(PictureAndPictureSpace::getPictureId)
                .toList();
        List<Picture> pictureList = pictureService.lambdaQuery()
                .in(Picture::getId, picIds)
                .list();
        List<PictureVO> pictureVOList = pictureList.stream().map(PictureVO::entityToVo).toList();
        picSpaDetailVo.setPictureVOList(pictureVOList);

        return picSpaDetailVo;
    }

    @Override
    public PictureSpace createPictureSpace(PicSpaCreateDto picSpaCreateDto) {
        ThrowUtils.throwIf(picSpaCreateDto == null, ErrorCode.PARAMS_ERROR);

        PictureSpace pictureSpace = BeanUtil.copyProperties(picSpaCreateDto, PictureSpace.class);
        //todo 先写死为1L
        setCreateTimeAndUser(pictureSpace, 1L);
        PictureSpaceLevelEnum pictureSpaceLevelEnum = PictureSpaceLevelEnum.getEnumByValue(picSpaCreateDto.getSpaceLevel());
        ThrowUtils.throwIf(pictureSpaceLevelEnum == null, ErrorCode.PARAMS_ERROR);

        Long size = pictureSpaceLevelEnum.getSize();
        Long total = pictureSpaceLevelEnum.getTotal();
        pictureSpace.setSpaceMaxSize(size);
        pictureSpace.setSpaceMaxNumber(total);
        boolean saveResult = this.save(pictureSpace);

        return pictureSpace;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Picture addPictureSpaceOne(PicSpaAddPicOneDto picSpaAddPicOneDto) {
        ThrowUtils.throwIf(picSpaAddPicOneDto == null, ErrorCode.PARAMS_ERROR);

        boolean isExisted = pictureAndPictureSpaceService.lambdaQuery()
                .eq(PictureAndPictureSpace::getPictureSpaceId, picSpaAddPicOneDto.getId())
                .eq(PictureAndPictureSpace::getPictureId, picSpaAddPicOneDto.getPictureId())
                .exists();
        ThrowUtils.throwIf(isExisted, "图片空间中已经存在该图片");

        Picture pictureReview = pictureService.lambdaQuery()
                .eq(Picture::getId, picSpaAddPicOneDto.getPictureId())
                .one();
        ThrowUtils.throwIf(pictureReview == null, ErrorCode.PARAMS_ERROR);
        Integer reviewStatus = pictureReview.getReviewStatus();
        if (!PictureReviewStatusEnum.PASS.getValue().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片审核还未通过");
        }

        Picture responsePicture = null;
//        synchronized (this) {
//            responsePicture = transactionTemplate.execute(status -> {
        PictureSpace pictureSpace = this.lambdaQuery()
                .eq(PictureSpace::getId, picSpaAddPicOneDto.getId())
                .one();
        Integer spaceLevel = pictureSpace.getSpaceLevel();
        PictureSpaceLevelEnum pictureSpaceLevelEnum = PictureSpaceLevelEnum.getEnumByValue(spaceLevel);
        ThrowUtils.throwIf(pictureSpaceLevelEnum == null, ErrorCode.PARAMS_ERROR);

        Long maxSize = pictureSpaceLevelEnum.getSize();
        Long maxNumber = pictureSpaceLevelEnum.getTotal();
        Long currentSize = Optional.ofNullable(pictureSpace.getCurrentSize())
                .orElse(0L);

        Picture picture = pictureService.getById(picSpaAddPicOneDto.getPictureId());
        Long picSize = Optional.ofNullable(picture.getPicSize())
                .orElse(0L);
        Long needSize = picSize + currentSize;

        if (needSize.compareTo(maxSize) > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片空间不足");
        }
        if (maxNumber.equals(pictureSpace.getCurrentCount())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片数量已满");
        }

        AddOnePicToSpaceBO picToSpaceBO = new AddOnePicToSpaceBO();
        picToSpaceBO.setAddSize(picSize);
        picToSpaceBO.setId(picSpaAddPicOneDto.getId());
        //todo 先写死为1L
        picToSpaceBO.setUpdateUser(1L);
        pictureSpaceMapper.updateByOnePicToSpa(picToSpaceBO);

        PictureAndPictureSpace pictureAndPictureSpace = new PictureAndPictureSpace();
        pictureAndPictureSpace.setPictureId(picSpaAddPicOneDto.getPictureId());
        pictureAndPictureSpace.setPictureSpaceId(picSpaAddPicOneDto.getId());
        setCreateTimeAndUser(pictureAndPictureSpace, 1L);
        boolean saveOnePicResult = pictureAndPictureSpaceService.save(pictureAndPictureSpace);

        responsePicture = picture;
//            });
//        }

        return responsePicture;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePictureSpaceOne(PicSpaDeletePicOneDto picSpaDeletePicOneDto) {
        ThrowUtils.throwIf(picSpaDeletePicOneDto == null, ErrorCode.PARAMS_ERROR);

        boolean deleteResult = false;
        ReentrantLock lock = new ReentrantLock();
        try {
//            boolean tryLock = lock.tryLock();
//            if (tryLock) {
            PictureSpace pictureSpace = this.lambdaQuery()
                    .eq(PictureSpace::getId, picSpaDeletePicOneDto.getId())
                    .one();
            Integer spaceLevel = pictureSpace.getSpaceLevel();
            PictureSpaceLevelEnum pictureSpaceLevelEnum = PictureSpaceLevelEnum.getEnumByValue(spaceLevel);
            ThrowUtils.throwIf(pictureSpaceLevelEnum == null, ErrorCode.PARAMS_ERROR);

            Long maxSize = ifNullToZero(pictureSpaceLevelEnum.getSize());
            Long currentSize = Optional.ofNullable(pictureSpace.getCurrentSize())
                    .orElse(0L);

            Picture picture = pictureService.getById(picSpaDeletePicOneDto.getPictureId());
            Long picSize = ifNullToZero(picture.getPicSize());

            if (currentSize.compareTo(maxSize) > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "不合法的删除操作");
            }
            if (picSize.compareTo(maxSize) > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "不合法的删除操作");
            }

            AddOnePicToSpaceBO picToSpaceBO = new AddOnePicToSpaceBO();
            picToSpaceBO.setAddSize(picSize);
            picToSpaceBO.setId(picSpaDeletePicOneDto.getId());
            //todo 先写死为1L
            picToSpaceBO.setUpdateUser(1L);
            pictureSpaceMapper.updateByOnePicAwaySpa(picToSpaceBO);

            List<PictureAndPictureSpace> pictureAndPictureSpaces = pictureAndPictureSpaceService.lambdaQuery()
                    .select(PictureAndPictureSpace::getId)
                    .eq(PictureAndPictureSpace::getPictureSpaceId, picSpaDeletePicOneDto.getId())
                    .eq(PictureAndPictureSpace::getPictureId, picSpaDeletePicOneDto.getPictureId())
                    .list();
            List<Long> pApsIds = pictureAndPictureSpaces.stream().map(PictureAndPictureSpace::getId).toList();
            boolean removeResult = pictureAndPictureSpaceService.lambdaUpdate()
                    .in(PictureAndPictureSpace::getId, pApsIds)
                    .remove();

            return removeResult;
//            } else {
//                return deleteResult;
//            }
        } catch (BusinessException e) {
            log.error("在空间中删除图片发生了异常：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
//            lock.unlock();
        }

    }

    private void setCreateTimeAndUser(PictureSpace pictureSpace, Long userId) {
        pictureSpace.setCreateTime(new Date());
        pictureSpace.setUpdateTime(new Date());
        if (pictureSpace.getEditTime() == null) {
            pictureSpace.setEditTime(new Date());
        }
        pictureSpace.setCreateUser(userId);
        pictureSpace.setUpdateUser(userId);
    }


    private void setCreateTimeAndUser(PictureAndPictureSpace pictureAndPictureSpace, Long userId) {
        pictureAndPictureSpace.setCreateTime(new Date());
        pictureAndPictureSpace.setUpdateTime(new Date());
        if (pictureAndPictureSpace.getEditTime() == null) {
            pictureAndPictureSpace.setEditTime(new Date());
        }
        pictureAndPictureSpace.setCreateUser(userId);
        pictureAndPictureSpace.setUpdateUser(userId);
    }
}
