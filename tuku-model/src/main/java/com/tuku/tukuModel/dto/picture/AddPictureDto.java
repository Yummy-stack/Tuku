package com.tuku.tukuModel.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class AddPictureDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1615405800246583491L;

    @ApiModelProperty(value = "图片简介")
    private String picIntroduction;

    @ApiModelProperty(value = "图片种类")
    private String picCategory;

    @ApiModelProperty(value = "图片标签")
    private String picTags;

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

}
