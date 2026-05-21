package com.tuku.tukuModel.vo.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "用户端题目分页查询返回值")
@Data
public class QuePageVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1270899731330376210L;

    @ApiModelProperty(value = "题目id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签列表（json 数组）")
    private String tags;

    @ApiModelProperty(value = "推荐答案")
    private String answer;
}
