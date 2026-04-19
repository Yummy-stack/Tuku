package com.tuku.tukuModel.entity.picture;


import java.io.Serial;
import java.time.LocalDateTime;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Blob;
import java.io.Serializable;
import java.util.Date;


@TableName("picture")
@Data
public class Picture implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "图片名称")
    private String picName;

    @ApiModelProperty(value = "图片URL路径")
    private String picUrl;

    @ApiModelProperty(value = "图片简介")
    private String picIntroduction;

    @ApiModelProperty(value = "图片种类")
    private String picCategory;

    @ApiModelProperty(value = "图片标签")
    private String picTags;

    @ApiModelProperty(value = "图片体积")
    private Long picSize;

    @ApiModelProperty(value = "图片宽度")
    private Integer picWidth;

    @ApiModelProperty(value = "图片高度")
    private Integer picHeight;

    @ApiModelProperty(value = "图片宽高比例")
    private Double picScale;

    @ApiModelProperty(value = "图片格式")
    private String picFormat;

    @ApiModelProperty(value = "图片创建人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUser;

    @ApiModelProperty(value = "图片修改人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUser;

    @ApiModelProperty(value = "图片创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "图片更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "图片编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;

    @ApiModelProperty(value = "审核状态 0待审核 1通过 2不通过")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;

    @ApiModelProperty(value = "审核人")
    private Long reviewUser;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    @ApiModelProperty(value = "图片主调颜色")
    private String picColor;
}
