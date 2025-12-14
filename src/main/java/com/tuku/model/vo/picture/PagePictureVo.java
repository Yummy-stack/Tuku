package com.tuku.model.vo.picture;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PagePictureVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6299439940581190105L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图片名称
     */
    private String picName;

    /**
     * 图片URL路径
     */
    private String picUrl;

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
