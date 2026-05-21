package com.tuku.tukuModel.dto.question.bank;

import com.tuku.tukuModel.dto.PageQueryCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题库分页查询参数")
@Data
public class QBankPageDto extends PageQueryCondition implements Serializable {
    @Serial
    private static final long serialVersionUID = -6676023489027783266L;

}
