package com.tuku.service.picture;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.model.dto.picture.AddPictureDto;
import com.tuku.model.dto.picture.QueryPagePictureDto;
import com.tuku.model.dto.picture.UpdatePictureDto;
import com.tuku.model.entity.picture.Picture;
import com.tuku.model.vo.picture.PagePictureVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图片表 服务类
 * </p>
 *
 * @author zhangyuxi
 * @since 2025-12-10
 */
public interface IPictureService extends IService<Picture> {
    //新增图片
    boolean addPicture(AddPictureDto addPictureDto,MultipartFile multipartFile);
    //分页查询图片
    List<PagePictureVo> queryPagePicture(QueryPagePictureDto queryPagePictureDto);
    //查询单个图片（修改回显）
    PagePictureVo queryOnePicture(Long id);
    //修改图片
    boolean updatePicture(UpdatePictureDto updatePictureDto,MultipartFile multipartFile);
    //删除图片
    boolean deletePicture(Long id);
}
