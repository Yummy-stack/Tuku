package com.tuku.tukuModel.dto.question.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "题库增加参数")
@Data
public class QBankAddDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6676023489027783266L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String picture;
}
