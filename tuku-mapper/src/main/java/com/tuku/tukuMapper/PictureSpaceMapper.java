package com.tuku.tukuMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuku.tukuModel.bo.pictureSpace.AddOnePicToSpaceBO;
import com.tuku.tukuModel.entity.pictureSpace.PictureSpace;

public interface PictureSpaceMapper extends BaseMapper<PictureSpace> {

    void updateByOnePicToSpa(AddOnePicToSpaceBO picToSpaceBO);

    void updateByOnePicAwaySpa(AddOnePicToSpaceBO picToSpaceBO);
}
