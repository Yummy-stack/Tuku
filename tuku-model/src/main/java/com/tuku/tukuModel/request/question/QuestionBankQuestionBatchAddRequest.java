package com.tuku.tukuModel.request.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "题库批量增加题目参数")
@Data
public class QuestionBankQuestionBatchAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    private Long questionBankId;

    @ApiModelProperty(value = "题目id列表")
    private List<Long> questionIdList;
}
