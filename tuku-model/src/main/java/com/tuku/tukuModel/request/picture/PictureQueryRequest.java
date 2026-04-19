package com.tuku.tukuModel.request.picture;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuku.tukuModel.request.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {
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

    @ApiModelProperty(value = "文件体积")
    private Long picSize;  

    @ApiModelProperty(value = "图片宽度")
    private Integer picWidth;  

    @ApiModelProperty(value = "图片高度")
    private Integer picHeight;  

    @ApiModelProperty(value = "图片比例")
    private Double picScale;  

    @ApiModelProperty(value = "图片格式")
    private String picFormat;  

    @ApiModelProperty(value = "搜索词（同时搜名称、简介等）")
    private String searchText;  

    @ApiModelProperty(value = "用户 id")
    private Long userId;

    @ApiModelProperty(value = "开始编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startEditTime;

    @ApiModelProperty(value = "结束编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endEditTime;
}
