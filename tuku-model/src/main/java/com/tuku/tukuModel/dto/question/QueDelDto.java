package com.tuku.tukuModel.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "题目删除参数")
@Data
public class QueDelDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2790538666780950458L;

    @ApiModelProperty(value = "题目id")
    private List<Long> ids;

}
