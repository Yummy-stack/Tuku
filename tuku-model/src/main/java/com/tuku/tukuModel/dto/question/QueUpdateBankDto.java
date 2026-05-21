package com.tuku.tukuModel.dto.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "修改题目所属题库参数")
@Data
public class QueUpdateBankDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目id")
    private Long questionId;

    @ApiModelProperty(value = "题库id列表")
    private List<Long> questionBankIds;
}
