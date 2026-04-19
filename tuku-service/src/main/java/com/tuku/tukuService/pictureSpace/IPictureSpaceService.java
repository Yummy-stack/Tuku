package com.tuku.tukuService.pictureSpace;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.tukuModel.dto.pictureSpace.*;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.entity.pictureSpace.PictureSpace;
import com.tuku.tukuModel.vo.pictureSpace.PicSpaDetailVO;

public interface IPictureSpaceService extends IService<PictureSpace> {

    Page<PictureSpace> listPictureSpaceByPage(PicSpaPageListDto picSpaPageListDto);

    boolean deletePictureSpace(PicSpaceDeleteDto picSpaceDeleteDto);

    PicSpaDetailVO detailPictureSpace(PicSpaDetailDto picSpaDetailDto);

    Picture addPictureSpaceOne(PicSpaAddPicOneDto picSpaAddPicOneDto);

    boolean deletePictureSpaceOne(PicSpaDeletePicOneDto picSpaDeletePicOneDto);

    PictureSpace createPictureSpace(PicSpaCreateDto picSpaCreateDto);
}
