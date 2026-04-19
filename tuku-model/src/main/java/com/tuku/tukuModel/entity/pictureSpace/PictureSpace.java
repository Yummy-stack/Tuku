package com.tuku.tukuModel.entity.pictureSpace;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("picture_space")
public class PictureSpace implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "空间级别： 0 普通版 1专业版 2旗舰版")
    private Integer spaceLevel;

    @ApiModelProperty(value = "空间的大小")
    private Long spaceMaxSize;

    @ApiModelProperty(value = "空间的图片容量")
    private Long spaceMaxNumber;

    @ApiModelProperty(value = "当前空间下图片的总大小")
    private Long currentSize;

    @ApiModelProperty(value = "当前空间下图片的总数量")
    private Long currentCount;

    @ApiModelProperty(value = "创建用户id")
    private Long createUser;

    @ApiModelProperty(value = "修改用户id")
    private Long updateUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;
}
