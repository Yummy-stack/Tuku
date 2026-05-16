package com.tuku.tukuService.picture;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.tuku.tukuModel.dto.picture.PictureReviewRequest;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.request.picture.PictureEditRequest;
import com.tuku.tukuModel.request.picture.PictureQueryRequest;
import com.tuku.tukuModel.request.picture.PictureUploadByBatchRequest;
import com.tuku.tukuModel.request.picture.PictureUploadRequest;
import com.tuku.tukuModel.vo.picture.PictureVO;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IPictureService extends IService<Picture> {
    PictureVO upLoadPicture(PictureUploadRequest pictureUploadRequest, MultipartFile multipartFile, Long userId);

    Picture getPictureById(Long id, HttpServletRequest request);

    PictureVO getPictureVOById(Long id, HttpServletRequest request);

    Page<Picture> listPictureByPage(PictureQueryRequest pictureQueryRequest);

    Page<PictureVO> listPictureVOByPage(PictureQueryRequest pictureQueryRequest, HttpServletRequest request);

    Page<PictureVO> listPictureVOByPageV2(PictureQueryRequest pictureQueryRequest, HttpServletRequest request);

    void downloadPicture(String filePath, HttpServletResponse response);

    PictureVO upLoadPictureByUrl(PictureUploadRequest pictureUploadRequest, String uploadUrl, Long userId);

    boolean reviewPicture(PictureReviewRequest pictureReviewRequest, Long userId);

    int upLoadPicturesBySearch(PictureUploadByBatchRequest pictureUploadByBatchRequest, LoginUserVo loginUserVo);

    boolean editPicture(PictureEditRequest pictureEditRequest, LoginUserVo loginUserVo);

    Page<PictureVO> listPictureVOByPageV2WithCache(PictureQueryRequest pictureQueryRequest, HttpServletRequest request);

    PictureVO getPictureVOByIdWithCache(Long id, HttpServletRequest request);

    PictureVO upLoadPictureV2(PictureUploadRequest pictureUploadRequest, MultipartFile multipartFile, Long userId);

    void downloadPictureV2(String filePath, HttpServletResponse response);

    Page<PictureVO> searchFromEs(PictureQueryRequest pictureQueryRequest);
}
