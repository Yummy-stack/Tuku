package com.tuku.tukuModel.entity.question;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "题目和题库的关联关系")
@TableName(value = "question_bank_question")
@Data
public class QuestionBankQuestion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目和题库的关联关系id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题库 id")
    private Long questionBankId;

    @ApiModelProperty(value = "题目 id")
    private Long questionId;

    @ApiModelProperty(value = "创建用户 id")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
