package com.tuku.controller.api.picture;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.tukuModel.dto.picture.PictureReviewRequest;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.request.DeleteRequest;
import com.tuku.tukuModel.request.picture.*;
import com.tuku.tukuModel.vo.picture.PictureTagCategory;
import com.tuku.tukuModel.vo.picture.PictureVO;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.picture.IPictureService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.annotation.AuthCheck;
import com.tuku.tukucommon.constant.user.UserRoleConstant;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Api(tags = "PictureApi")
@RestController
@RequestMapping("/picture")
@CrossOrigin
@Slf4j
public class PictureController {
    @Resource
    private IUserService userService;

    @Resource
    private IPictureService pictureService;

    @ApiOperation(value = "图片上传 v1")
    @PostMapping("/upload/v1")
    BaseResponse<PictureVO> uploadPicture(
            @ApiParam(value = "图片信息", required = true) @RequestPart("file") MultipartFile multipartFile,
            @ApiParam(value = "请求", required = true) HttpServletRequest request,
            @ApiParam(value = "图片上传的参数", required = false) PictureUploadRequest pictureUploadRequest) {
        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, "用户未登录");
        PictureVO result = pictureService.upLoadPicture(pictureUploadRequest, multipartFile, loginUser.getId());
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "图片上传 v1 v2")
    @PostMapping("/upload/v1/v2")
    BaseResponse<PictureVO> uploadPictureV2(
            @ApiParam(value = "图片信息", required = true) @RequestPart("file") MultipartFile multipartFile,
            @ApiParam(value = "请求", required = true) HttpServletRequest request,
            @ApiParam(value = "图片上传的参数", required = false) PictureUploadRequest pictureUploadRequest) {
        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, "用户未登录");
        PictureVO result = pictureService.upLoadPictureV2(pictureUploadRequest, multipartFile, loginUser.getId());
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "图片上传（Url）v2")
    @PostMapping("/upload/v2")
    BaseResponse<PictureVO> uploadPictureByUrl(
            @ApiParam(value = "请求", required = true) HttpServletRequest request,
            @ApiParam(value = "图片上传的参数", required = true) @RequestBody PictureUploadRequest pictureUploadRequest) {
        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, "控制层参数为null");
        String uploadUrl = pictureUploadRequest.getFileUrl();
        PictureVO result = pictureService.upLoadPictureByUrl(pictureUploadRequest, uploadUrl, loginUser.getId());
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "图片下载")
    @GetMapping("/download")
    void downloadPicture(
            @ApiParam(value = "下载的文件名", required = true) @RequestParam("filePath") String filePath,
            @ApiParam(value = "servlet参数") HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, "控制层参数为空");
        pictureService.downloadPicture(filePath, response);
    }

    @ApiOperation(value = "图片下载 v2")
    @GetMapping("/download/v2")
    void downloadPictureV2(
            @ApiParam(value = "下载的文件名", required = true) @RequestParam("filePath") String filePath,
            @ApiParam(value = "servlet参数") HttpServletResponse response) {
        ThrowUtils.throwIf(filePath == null || response == null, "控制层参数为空");
        pictureService.downloadPictureV2(filePath, response);
    }

    @ApiOperation(value = "根据 id 获取图片（仅管理员可用）")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserRoleConstant.ADMIN_ROLE)
    public BaseResponse<Picture> getPictureById(
            @ApiParam(value = "图片ID", required = true) @RequestParam(value = "id") Long id,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0 || request == null, "控制层参数为空");
        Picture picture = pictureService.getPictureById(id, request);
        return ResultUtils.success(picture);
    }

    @ApiOperation(value = " 根据 id 获取图片（封装类）")
    @GetMapping("/get/vo")
    public BaseResponse<PictureVO> getPictureVOById(
            @ApiParam(value = "图片ID", required = true) @RequestParam(value = "id") Long id,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0 || request == null, "控制层参数为空");
        PictureVO pictureVO = pictureService.getPictureVOById(id, request);
        return ResultUtils.success(pictureVO);
    }

    @ApiOperation(value = " 根据 id 获取图片（封装类） -- 缓存版本")
    @GetMapping("/get/vo/with/cache")
    public BaseResponse<PictureVO> getPictureVOByIdWithCache(
            @ApiParam(value = "图片ID", required = true) @RequestParam(value = "id") Long id,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0 || request == null, "控制层参数为空");
        PictureVO pictureVO = pictureService.getPictureVOByIdWithCache(id, request);
        return ResultUtils.success(pictureVO);
    }

    @ApiOperation(value = "分页获取图片列表（仅管理员可用）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserRoleConstant.ADMIN_ROLE)
    public BaseResponse<Page<Picture>> listPictureByPage(
            @ApiParam(value = "查询参数", required = false) @RequestBody PictureQueryRequest pictureQueryRequest) {
        ThrowUtils.throwIf(pictureQueryRequest == null, "控制层参数为空");
        Page<Picture> picturePage = pictureService.listPictureByPage(pictureQueryRequest);
        return ResultUtils.success(picturePage);
    }

    @ApiOperation(value = "分页获取图片列表（封装类）")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PictureVO>> listPictureVOByPage(
            @ApiParam(value = "查询参数", required = false) @RequestBody PictureQueryRequest pictureQueryRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, "控制层参数为空");
        Page<PictureVO> pictureVOPage = pictureService.listPictureVOByPage(pictureQueryRequest, request);
        return ResultUtils.success(pictureVOPage);
    }

    @ApiOperation(value = "从 ES 分页获取图片列表")
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<PictureVO>> searchPictureVOByPage(
            @ApiParam(value = "查询参数", required = false) @RequestBody PictureQueryRequest pictureQueryRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, "控制层参数为空");
        Page<PictureVO> pictureVOPage = pictureService.searchFromEs(pictureQueryRequest);
        return ResultUtils.success(pictureVOPage);
    }

    @ApiOperation(value = "分页获取审核通过的图片列表（封装类）")
    @PostMapping("/list/page/vo/v2")
    public BaseResponse<Page<PictureVO>> listPictureVOByPageV2(
            @ApiParam(value = "查询参数", required = false) @RequestBody PictureQueryRequest pictureQueryRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, "控制层参数为空");
        Page<PictureVO> pictureVOPage = pictureService.listPictureVOByPageV2(pictureQueryRequest, request);
        return ResultUtils.success(pictureVOPage);
    }

    @ApiOperation(value = "分页获取审核通过的图片列表（封装类）-- 缓存优化版本")
    @PostMapping("/list/page/vo/v2/with/cache")
    public BaseResponse<Page<PictureVO>> listPictureVOByPageV2WithCache(
            @ApiParam(value = "查询参数", required = false) @RequestBody PictureQueryRequest pictureQueryRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureQueryRequest == null, "控制层参数为空");
        Page<PictureVO> pictureVOPage = pictureService.listPictureVOByPageV2WithCache(pictureQueryRequest, request);
        return ResultUtils.success(pictureVOPage);
    }

    @ApiOperation(value = "删除图片")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePicture(
            @ApiParam(value = "删除图片的参数", required = true) @RequestBody DeleteRequest deleteRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null, "控制层参数为空");

        return null;

    }

    @ApiOperation(value = "更新图片（仅管理员可用）")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRoleConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePicture(
            @ApiParam(value = "更新参数", required = false) @RequestBody PictureUpdateRequest pictureUpdateRequest) {
        return null;

    }

    @ApiOperation(value = "编辑图片（给用户使用）")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editPicture(
            @ApiParam(value = "编辑参数", required = false) @RequestBody PictureEditRequest pictureEditRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureEditRequest == null, "控制层参数为空");
        LoginUserVo loginUserVo = userService.getLoginUser(request);
        boolean editResult = pictureService.editPicture(pictureEditRequest, loginUserVo);
        return ResultUtils.success(editResult);
    }

    @ApiOperation(value = "图片种类和标签")
    @GetMapping("/tag_category")
    public BaseResponse<PictureTagCategory> listPictureTagCategory() {
        PictureTagCategory pictureTagCategory = new PictureTagCategory();
        List<String> tagList = Arrays.asList("热门", "搞笑", "生活", "高清", "艺术", "校园", "背景", "简历", "创意");
        List<String> categoryList = Arrays.asList("模板", "电商", "表情包", "素材", "海报");
        pictureTagCategory.setTagList(tagList);
        pictureTagCategory.setCategoryList(categoryList);
        return ResultUtils.success(pictureTagCategory);
    }

    @ApiOperation(value = "图片审核")
    @PostMapping("/review")
    @AuthCheck(mustRole = UserRoleConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> reviewPicture(
            @ApiParam(value = "图片审核Dto", required = true) PictureReviewRequest pictureReviewRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureReviewRequest == null || request == null, "控制层参数为空");
        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, "控制层参数为null");
        boolean reviewResult = pictureService.reviewPicture(pictureReviewRequest, loginUser.getId());
        return ResultUtils.success(reviewResult);
    }

    @ApiOperation(value = "批量抓取图片")
    @PostMapping("/upload/batch")
    @AuthCheck(mustRole = UserRoleConstant.ADMIN_ROLE)
    public BaseResponse<Integer> uploadPictureByBatch(
            @ApiParam(value = "批量抓取的Dto参数") @RequestBody PictureUploadByBatchRequest pictureUploadByBatchRequest,
            @ApiParam(value = "servlet参数") HttpServletRequest request) {
        ThrowUtils.throwIf(pictureUploadByBatchRequest == null, ErrorCode.PARAMS_ERROR);
        LoginUserVo loginUser = userService.getLoginUser(request);
        int uploadCount = pictureService.upLoadPicturesBySearch(pictureUploadByBatchRequest, loginUser);
        return ResultUtils.success(uploadCount);
    }

}
