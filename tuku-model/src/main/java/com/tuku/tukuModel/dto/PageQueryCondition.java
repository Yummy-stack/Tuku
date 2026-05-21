package com.tuku.tukuModel.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "分页参数")
@Data
public class PageQueryCondition implements Serializable {
    @Serial
    private static final long serialVersionUID = 7711740652482977777L;

    @ApiModelProperty(value = "当前页号")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序顺序（默认降序）")
    private String sortOrder = "descend";

}
