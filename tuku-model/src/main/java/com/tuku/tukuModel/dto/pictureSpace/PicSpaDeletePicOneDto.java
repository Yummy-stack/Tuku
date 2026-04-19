package com.tuku.tukuModel.dto.pictureSpace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PicSpaDeletePicOneDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6656678500664199234L;

    @ApiModelProperty(value = "图片空间id")
    private Long id;

    @ApiModelProperty(value = "图片id")
    private Long pictureId;
}
