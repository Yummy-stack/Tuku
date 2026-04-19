package com.tuku.tukuModel.request.picture;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;  

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String introduction;  

    @ApiModelProperty(value = "分类")
    private String category;  

    @ApiModelProperty(value = "标签")
    private List<String> tags;
}
