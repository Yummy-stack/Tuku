package com.tuku.tukuModel.entity.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Blob;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("taskinfo")
@ApiModel(value = "任务表")
public class Taskinfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long taskId;

    @ApiModelProperty(value = "执行时间")
    private Date executeTime;

    @ApiModelProperty(value = "参数")
    private byte[] parameters;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "任务类型")
    private Integer taskType;

    @ApiModelProperty(value = "创建用户id")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
