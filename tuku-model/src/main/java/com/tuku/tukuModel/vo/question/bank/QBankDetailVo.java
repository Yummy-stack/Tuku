package com.tuku.tukuModel.vo.question.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tuku.tukuModel.entity.question.QuestionBank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "题库详情返回值")
@Data
public class QBankDetailVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String picture;

    @ApiModelProperty(value = "创建用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "题目总数")
    private Integer questionCount;

    public static QBankDetailVo entityToVo(QuestionBank questionBank) {
        if (questionBank == null) {
            return null;
        }
        QBankDetailVo vo = new QBankDetailVo();
        vo.setId(questionBank.getId());
        vo.setTitle(questionBank.getTitle());
        vo.setDescription(questionBank.getDescription());
        vo.setPicture(questionBank.getPicture());
        vo.setUserId(questionBank.getUserId());
        vo.setEditTime(questionBank.getEditTime());
        vo.setCreateTime(questionBank.getCreateTime());
        vo.setUpdateTime(questionBank.getUpdateTime());
        return vo;
    }
}
