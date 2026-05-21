package com.tuku.tukuModel.dto.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "题目增加参数")
@Data
public class QueAddDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2790538666780950458L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签列表（json 数组）")
    private String tags;

    @ApiModelProperty(value = "推荐答案")
    private String answer;
}
