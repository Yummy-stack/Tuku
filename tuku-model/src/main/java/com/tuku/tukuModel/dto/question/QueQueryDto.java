package com.tuku.tukuModel.dto.question;

import com.tuku.tukuModel.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题目分页查询参数")
@Data
public class QueQueryDto extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签列表（json 数组）")
    private List<String> tags;

    @ApiModelProperty(value = "创建用户id")
    private Long userId;

    @ApiModelProperty(value = "搜索词（同时搜标题、内容等）")
    private String searchText;
}
