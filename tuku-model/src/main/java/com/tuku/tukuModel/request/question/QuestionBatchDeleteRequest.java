package com.tuku.tukuModel.request.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "批量删除题库的参数")
@Data
public class QuestionBatchDeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id列表")
    private List<Long> questionIdList;
}
