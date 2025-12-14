package com.tuku.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class AddPictureDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1615405800246583491L;

    /**
     * 图片简介
     */
    private String picIntroduction;

    /**
     * 图片种类
     */
    private String picCategory;

    /**
     * 图片标签
     */
    private String picTags;

    /**
     * 图片体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

}
