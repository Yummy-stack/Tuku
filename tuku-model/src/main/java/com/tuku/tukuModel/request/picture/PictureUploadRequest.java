package com.tuku.tukuModel.request.picture;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PictureUploadRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片 id（用于修改）")
    private Long id;

    @ApiModelProperty(value = "图片的Url")
    private String FileUrl;
}
