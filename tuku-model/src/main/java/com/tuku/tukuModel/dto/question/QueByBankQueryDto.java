package com.tuku.tukuModel.dto.question;

import com.tuku.tukuModel.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "根据题库id查询题目参数")
@Data
public class QueByBankQueryDto extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    private Long questionBankId;

    @ApiModelProperty(value = "搜索词")
    private String searchText;
}
