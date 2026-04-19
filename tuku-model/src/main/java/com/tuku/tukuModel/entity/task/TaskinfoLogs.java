package com.tuku.tukuModel.entity.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.sql.Date;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Blob;
import java.io.Serializable;

@Data
@TableName("taskinfo_logs")
@ApiModel(value = "任务日志表")
public class TaskinfoLogs implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    @TableId(value = "task_id", type = IdType.INPUT)
    private Long taskId;

    @ApiModelProperty(value = "执行时间")
    private Date executeTime;

    @ApiModelProperty(value = "参数")
    private byte[] parameters;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "任务类型")
    private Integer taskType;

    @ApiModelProperty(value = "版本号,用乐观锁")
    private Integer version;

    @ApiModelProperty(value = "创建用户id")
    private Long consumeUser;

    @ApiModelProperty(value = "任务执行失败的异常信息")
    private String errorMessage;

    @ApiModelProperty(value = " 创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态 0=初始化状态 1=EXECUTED 2=CANCELLED")
    private Integer status;

}
