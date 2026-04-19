package com.tuku.tukuModel.vo.pictureSpace;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.tuku.tukuModel.vo.picture.PictureVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PicSpaDetailVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3847776425812731644L;

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

    @ApiModelProperty(value = "空间里的图片信息")
    private List<PictureVO> pictureVOList;

}
