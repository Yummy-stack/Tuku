package com.tuku.tukuModel.bo.pictureSpace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AddOnePicToSpaceBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3224684175233051256L;

    @ApiModelProperty(value = "空间id")
    private Long id;

    @ApiModelProperty(value = "增加的图片大小")
    private Long addSize;

    @ApiModelProperty(value = "修改人的id")
    private Long updateUser;
}
