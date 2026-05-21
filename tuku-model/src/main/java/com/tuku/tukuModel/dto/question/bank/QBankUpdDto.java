package com.tuku.tukuModel.dto.question.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tuku.tukuModel.dto.PageQueryCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题库更新参数")
@Data
public class QBankUpdDto extends PageQueryCondition implements Serializable {
    @Serial
    private static final long serialVersionUID = -6676023489027783266L;

    @ApiModelProperty(value = "题库id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String picture;

}
