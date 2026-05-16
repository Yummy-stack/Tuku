package com.tuku.es.document;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tuku.tukuModel.entity.picture.Picture;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图片 ES DTO
 */
@Document(indexName = "picture")
@Data
public class PictureEsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 图片名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String picName;

    /**
     * 图片URL路径
     */
    @Field(type = FieldType.Keyword)
    private String picUrl;

    /**
     * 图片简介
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String picIntroduction;

    /**
     * 图片种类
     */
    @Field(type = FieldType.Keyword)
    private String picCategory;

    /**
     * 图片标签
     */
    @Field(type = FieldType.Keyword)
    private List<String> picTags;

    /**
     * 图片体积
     */
    @Field(type = FieldType.Long)
    private Long picSize;

    /**
     * 图片宽度
     */
    @Field(type = FieldType.Integer)
    private Integer picWidth;

    /**
     * 图片高度
     */
    @Field(type = FieldType.Integer)
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    @Field(type = FieldType.Double)
    private Double picScale;

    /**
     * 图片格式
     */
    @Field(type = FieldType.Keyword)
    private String picFormat;

    /**
     * 图片创建人id
     */
    @Field(type = FieldType.Long)
    private Long createUser;

    /**
     * 图片修改人
     */
    @Field(type = FieldType.Long)
    private Long updateUser;

    /**
     * 图片创建时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 图片更新时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否删除
     */
    @Field(type = FieldType.Integer)
    private Integer isDelete;

    /**
     * 图片编辑时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date editTime;

    /**
     * 审核状态 0待审核 1通过 2不通过
     */
    @Field(type = FieldType.Integer)
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @Field(type = FieldType.Text)
    private String reviewMessage;

    /**
     * 审核人
     */
    @Field(type = FieldType.Long)
    private Long reviewUser;

    /**
     * 审核时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /**
     * 图片主调颜色
     */
    @Field(type = FieldType.Keyword)
    private String picColor;

    /**
     * 对象转包装类
     *
     * @param picture
     * @return
     */
    public static PictureEsDTO objToDto(Picture picture) {
        if (picture == null) {
            return null;
        }
        PictureEsDTO pictureEsDTO = new PictureEsDTO();
        BeanUtil.copyProperties(picture, pictureEsDTO);
        String picTags = picture.getPicTags();
        if (StrUtil.isNotBlank(picTags)) {
            pictureEsDTO.setPicTags(JSONUtil.toList(picTags, String.class));
        }
        return pictureEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param pictureEsDTO
     * @return
     */
    public static Picture dtoToObj(PictureEsDTO pictureEsDTO) {
        if (pictureEsDTO == null) {
            return null;
        }
        Picture picture = new Picture();
        BeanUtil.copyProperties(pictureEsDTO, picture);
        List<String> tagList = pictureEsDTO.getPicTags();
        if (CollUtil.isNotEmpty(tagList)) {
            picture.setPicTags(JSONUtil.toJsonStr(tagList));
        }
        return picture;
    }
}
