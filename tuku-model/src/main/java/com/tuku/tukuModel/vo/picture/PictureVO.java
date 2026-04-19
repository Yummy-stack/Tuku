package com.tuku.tukuModel.vo.picture;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.xiaoymin.knife4j.core.util.StrUtil;

import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.enums.picture.PictureReviewStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
public class PictureVO implements Serializable {
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
    private List<String> picTags;

    @ApiModelProperty(value = "图片体积")
    @JsonSerialize(using = ToStringSerializer.class)
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

//    @ApiModelProperty(value = "创建人的详细脱敏信息")
//    private UserVO userVO;

    @ApiModelProperty(value = "是否删除 0存在 1删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "图片编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;

    @ApiModelProperty(value = "待审核 通过 不通过")
    private String reviewStatusText;

    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;

    @ApiModelProperty(value = "审核人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long reviewUser;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    @ApiModelProperty(value = "图片主调颜色")
    private String picColor;

    public static Picture voToEntity(PictureVO pictureVo) {
//        ThrowUtils.throwIf(pictureVo == null, "参数为null");
        if (pictureVo == null) {
            throw new RuntimeException("参数为null");
        }
        Picture picture = new Picture();
        BeanUtil.copyProperties(pictureVo, picture);
        if (picture.getPicTags() != null && !picture.getPicTags().isEmpty()) {
            picture.setPicTags(JSONUtil.toJsonStr(pictureVo.getPicTags()));
        }
        return picture;
    }

    public static PictureVO entityToVo(Picture picture) {
//        ThrowUtils.throwIf(picture == null, "参数为null");
        if (picture == null) {
            throw new RuntimeException("参数为null");
        }
        PictureVO pictureVo = new PictureVO();
        BeanUtil.copyProperties(picture, pictureVo);
        String textByValue = PictureReviewStatusEnum.getTextByValue(picture.getReviewStatus());
        pictureVo.setReviewStatusText(textByValue);
        String tags = picture.getPicTags();
        if (StrUtil.isNotBlank(tags)) {
            List<String> tagList = Arrays.asList(tags.split(","));
            pictureVo.setPicTags(tagList);
        }

        return pictureVo;
    }
}
