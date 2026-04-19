package com.tuku.tukuModel.dto.pictureSpace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PicSpaAddPicOneDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7471166284285762153L;

    @ApiModelProperty(value = "图片空间id")
    private Long id;

    @ApiModelProperty(value = "图片id")
    private Long pictureId;
}
