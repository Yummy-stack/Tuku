package com.tuku.model.entity.picture;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Blob;
import java.io.Serializable;

/**
 * <p>
 * 图片表
 * </p>
 *
 * @author zhangyuxi
 * @since 2025-12-10
 */
@TableName("picture")
@Data
public class Picture implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * 图片创建人id
     */
    private Long createUser;

    /**
     * 图片修改人
     */
    private Long updateUser;

    /**
     * 图片创建时间
     */
    private LocalDateTime createTime;

    /**
     * 图片更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 图片编辑时间
     */
    private LocalDateTime editTime;
}
