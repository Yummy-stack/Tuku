package com.tuku.tukuModel.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "题目删除参数")
@Data
public class QueDeleteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}
