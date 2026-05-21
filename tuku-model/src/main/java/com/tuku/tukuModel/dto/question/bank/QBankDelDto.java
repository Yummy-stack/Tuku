package com.tuku.tukuModel.dto.question.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "题库删除参数")
@Data
public class QBankDelDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6676023489027783266L;

    @ApiModelProperty(value = "题库id")
    private List<Long> ids;
}
