package com.tuku.es.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "题目搜索引擎传递参数")
@Data
public class QueEsDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -206065019795917528L;

    @ApiModelProperty(value = "题目名称或者题目内容")
    private String questionSearchText;
}
