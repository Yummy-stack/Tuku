package com.tuku.tukuModel.vo.question.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "用户端题库分页查询返回值")
@Data
public class QuePageBankVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 4551250049069759155L;

    @ApiModelProperty(value = "题库id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String picture;
}
