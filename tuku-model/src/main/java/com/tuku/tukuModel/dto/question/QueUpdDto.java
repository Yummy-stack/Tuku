package com.tuku.tukuModel.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(value = "题目更新参数")
@Data
public class QueUpdDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2790538666780950458L;

    @ApiModelProperty(value = "题目id")
    @TableId(value = "id", type = IdType.AUTO)
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
