package com.tuku.tukuModel.bo.result.picture;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UploadLocalResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片名称")
    private String picName;

    @ApiModelProperty(value = "图片URL路径")
    private String picUrl;

    @ApiModelProperty(value = "图片体积")
    private Long picSize;

    @ApiModelProperty(value = "图片宽度")
    private Integer picWidth;

    @ApiModelProperty(value = "图片高度")
    private Integer picHeight;

    @ApiModelProperty(value = "图片宽高比例")
    private Double picScale;

    @ApiModelProperty(value = "图片格式")
    private String picFormat;

    @ApiModelProperty(value = "图片主调颜色")
    private String picColor;
}
