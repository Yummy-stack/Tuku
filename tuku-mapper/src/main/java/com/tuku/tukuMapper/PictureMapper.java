package com.tuku.tukuMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuku.tukuModel.entity.picture.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PictureMapper extends BaseMapper<Picture> {
    /**
     * 查询删除状态为number的Pic.Url，然后映射到Picture实体类中
     * @param number 是否删除
     * @return 符合条件的Pic
     */
    List<Picture> selectNoneExistent(@Param("number") Integer number);

    /**
     * 查询所有已审核通过的图片
     * @return 已审核通过的图片列表
     */
    List<Picture> selectAllReviewedPictures();
}
