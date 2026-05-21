package com.tuku.tukuModel.dto.question;


import com.tuku.tukuModel.dto.PageQueryCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题目分页查询参数")
@Data
public class QuePageDto extends PageQueryCondition implements Serializable {
    @Serial
    private static final long serialVersionUID = -7920830070778816634L;


}
