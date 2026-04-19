package com.tuku.tukuModel.bo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "图片审核-延迟任务 的任务实体类")
public class TaskPicBo implements Serializable {
    @Serial
    private static final long serialVersionUID = 8309573236065745525L;

    @ApiModelProperty(value = "图片id")
    private Long picId;
}
