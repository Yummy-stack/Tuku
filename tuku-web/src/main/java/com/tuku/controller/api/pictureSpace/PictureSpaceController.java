package com.tuku.controller.api.pictureSpace;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.tukuModel.dto.pictureSpace.*;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.entity.pictureSpace.PictureSpace;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.enums.pictureSpace.PictureSpaceLevelEnum;
import com.tuku.tukuModel.vo.pictureSpace.PicSpaDetailVO;
import com.tuku.tukuModel.vo.pictureSpace.PicSpaLevelVO;
import com.tuku.tukuService.pictureSpace.IPictureSpaceService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;


@Api(tags = "pictureSpaceApi")
@RestController
@RequestMapping("/picture-space")
public class PictureSpaceController {
    @Resource
    private IPictureSpaceService pictureSpaceService;

    @ApiOperation(value = "空间管理 - 查询空间列表")
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Page<PictureSpace>> listPictureSpaceByPage(
            @ApiParam(value = "图片空间查询参数") @RequestBody PicSpaPageListDto picSpaPageListDto
    ) {
        ThrowUtils.throwIf(picSpaPageListDto == null, ErrorCode.PARAMS_ERROR);
        Page<PictureSpace> pictureSpaceList = pictureSpaceService.listPictureSpaceByPage(picSpaPageListDto);
        return ResultUtils.success(pictureSpaceList);
    }

    @ApiOperation(value = "空间管理 - 空间删除")
    @PostMapping("/delete")
//    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> deletePictureSpace(
            @ApiParam(value = "删除的空间空间id") @RequestBody PicSpaceDeleteDto picSpaceDeleteDto
    ) {
        ThrowUtils.throwIf(picSpaceDeleteDto == null, ErrorCode.PARAMS_ERROR);
        boolean deleteResult = pictureSpaceService.deletePictureSpace(picSpaceDeleteDto);
        return ResultUtils.success(deleteResult);
    }

    @ApiOperation(value = "空间管理 - 空间编辑")
    @PostMapping("/edit")
//    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> editPictureSpace(
    ) {
        return ResultUtils.success(true);
    }

    @ApiOperation(value = "个人空间或空间管理 - 创建空间")
    @PostMapping("/create")
    BaseResponse<PictureSpace> createPictureSpace(
            @ApiParam(value = "创建空间的参数") @RequestBody PicSpaCreateDto picSpaCreateDto
    ) {
        ThrowUtils.throwIf(picSpaCreateDto == null, ErrorCode.PARAMS_ERROR);
        PictureSpace pictureSpace = pictureSpaceService.createPictureSpace(picSpaCreateDto);
        return ResultUtils.success(pictureSpace);
    }

    @ApiOperation(value = "个人空间或空间管理 - 查看空间详情")
    @PostMapping("/detail")
    BaseResponse<PicSpaDetailVO> detailPictureSpace(
            @ApiParam(value = "空间ID") @RequestBody PicSpaDetailDto picSpaDetailDto
    ) {
        ThrowUtils.throwIf(picSpaDetailDto == null, ErrorCode.PARAMS_ERROR);
        PicSpaDetailVO picSpaDetailVo = pictureSpaceService.detailPictureSpace(picSpaDetailDto);
        return ResultUtils.success(picSpaDetailVo);
    }

    @ApiOperation(value = "个人空间或空间管理 - 添加图片")
    @PostMapping("/add-picture/one")
    BaseResponse<Picture> addPictureSpaceOne(
            @ApiParam(value = "图片ID") @RequestBody PicSpaAddPicOneDto picSpaAddPicOneDto
    ) {
        ThrowUtils.throwIf(picSpaAddPicOneDto == null, ErrorCode.PARAMS_ERROR);
        Picture picture = pictureSpaceService.addPictureSpaceOne(picSpaAddPicOneDto);
        return ResultUtils.success(picture);
    }

    @ApiOperation(value = "个人空间或空间管理 - 删除图片")
    @PostMapping("/delete-picture/one")
    BaseResponse<Boolean> deletePictureSpaceOne(
            @ApiParam(value = "图片ID") @RequestBody PicSpaDeletePicOneDto picSpaDeletePicOneDto
    ) {
        ThrowUtils.throwIf(picSpaDeletePicOneDto == null, ErrorCode.PARAMS_ERROR);
        boolean deleteOneResult = pictureSpaceService.deletePictureSpaceOne(picSpaDeletePicOneDto);
        return ResultUtils.success(deleteOneResult);
    }

    @ApiOperation(value = "个人空间或空间管理 - 获取所有空间级别")
    @PostMapping("/picture-space/levels")
    BaseResponse<List<PicSpaLevelVO>> pictureSpaceLevels(
    ) {
        PictureSpaceLevelEnum[] pictureSpaceLevelEnums = PictureSpaceLevelEnum.values();
        List<PicSpaLevelVO> pisSpaLevelList = Arrays.stream(pictureSpaceLevelEnums)
                .map(pictureSpaceLevelEnum -> {
                    PicSpaLevelVO picSpaLevelVO = new PicSpaLevelVO();
                    picSpaLevelVO.setValue(pictureSpaceLevelEnum.getValue());
                    picSpaLevelVO.setText(pictureSpaceLevelEnum.getText());
                    picSpaLevelVO.setTotal(pictureSpaceLevelEnum.getTotal());
                    picSpaLevelVO.setSize(pictureSpaceLevelEnum.getSize());
                    return picSpaLevelVO;
                })
                .toList();
        return ResultUtils.success(pisSpaLevelList);
    }
}
