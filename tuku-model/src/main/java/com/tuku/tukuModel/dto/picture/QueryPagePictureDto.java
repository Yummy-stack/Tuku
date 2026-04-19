package com.tuku.tukuModel.dto.picture;

import com.tuku.tukuModel.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPagePictureDto extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6654557062410328165L;


    /**
     * 图片名称
     */
    private String picName;
    // TODO 之后再使用ElasticSearch进行扩展
//    /**
//     * 图片简介
//     */
//    private String picIntroduction;

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
     * 最早创建时间
     */
    private LocalDateTime minCreateTime;

    /**
     * 最晚创建时间
     */
    private LocalDateTime maxCreateTime;
//    /**
//     * 图片更新时间
//     */
//    private LocalDateTime updateTime;
//
//    /**
//     * 是否删除
//     */
//    private Integer isDelete;
//
//    /**
//     * 图片编辑时间
//     */
//    private LocalDateTime editTime;
}
