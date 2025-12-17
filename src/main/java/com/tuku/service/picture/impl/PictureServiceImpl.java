package com.tuku.service.picture.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.constant.file.FileConstant;
import com.tuku.mapper.PictureMapper;
import com.tuku.model.dto.picture.AddPictureDto;
import com.tuku.model.dto.picture.QueryPagePictureDto;
import com.tuku.model.dto.picture.UpdatePictureDto;
import com.tuku.model.entity.picture.Picture;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.model.enums.error.ErrorCode;
import com.tuku.model.vo.picture.PagePictureVo;
import com.tuku.service.picture.IPictureService;
import com.tuku.service.user.IUserService;
import com.tuku.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author zhangyuxi
 * @since 2025-12-10
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {
    @Resource
    private IUserService userService;

    @Override
    public boolean addPicture(AddPictureDto addPictureDto,MultipartFile multipartFile) {
        //参数校验
        ThrowUtils.throwIf(multipartFile.isEmpty(),ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(addPictureDto == null,ErrorCode.NOT_FOUND_ERROR);
        //主体逻辑
        try {
            //先存文件到本地
            String originalFilename = multipartFile.getOriginalFilename();
            String uploadFileName = String.format("%s-%s", LocalDateTime.now(), originalFilename);
            File file = new File(FileConstant.UPLOAD_PATH + uploadFileName);
            // 确保目录存在
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            // TODO 校验图片是否合法
            multipartFile.transferTo(file);
            //在数据库中存图片信息
            Picture insertPicture = createInsertPicture(multipartFile, addPictureDto);
            boolean save = this.save(insertPicture);
            return save;
        } catch (IOException e) {
            log.error("文件上传失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PagePictureVo> queryPagePicture(QueryPagePictureDto queryPagePictureDto) {
        //参数校验
        ThrowUtils.throwIf(queryPagePictureDto == null,ErrorCode.NOT_FOUND_ERROR);
        //主体逻辑
        String picTags = queryPagePictureDto.getPicTags();
        List<String> picTagsList = Arrays.asList(picTags.split(","));
        Page<Picture> pagePicture = this.lambdaQuery()
                .eq(queryPagePictureDto.getPicName() != null, Picture::getPicName, queryPagePictureDto.getPicName())
                .eq(queryPagePictureDto.getPicCategory() != null, Picture::getPicCategory, queryPagePictureDto.getPicCategory())
                .page(new Page<>(queryPagePictureDto.getPageNum(), queryPagePictureDto.getPicSize()));

        List<Picture> pictureList = pagePicture.getRecords();
        List<PagePictureVo> pagePictureVoList = pictureList.stream()
                .map(this::toPictureVo)
                .toList();

        return pagePictureVoList;
    }

    @Override
    public PagePictureVo queryOnePicture(Long id) {
        Picture picture = this.getById(id);
        ThrowUtils.throwIf(picture == null,ErrorCode.NOT_FOUND_ERROR,"图片不存在");
        PagePictureVo pictureVo = toPictureVo(picture);
        return pictureVo;
    }

    @Override
    public boolean updatePicture(UpdatePictureDto updatePictureDto, MultipartFile multipartFile) {
        //参数校验
        ThrowUtils.throwIf(updatePictureDto == null,ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(multipartFile.isEmpty(),ErrorCode.NOT_FOUND_ERROR);
        //主体逻辑
        //TODO 删除文件系统的文件
        boolean removeResult = this.lambdaUpdate()
                .eq(Picture::getId, updatePictureDto.getId())
                .remove();
        try {
            //先存文件到本地
            String originalFilename = multipartFile.getOriginalFilename();
            String uploadFileName = String.format("%s-%s", LocalDateTime.now(), originalFilename);
            File file = new File(FileConstant.UPLOAD_PATH + uploadFileName);
            // 确保目录存在
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            // TODO 校验图片是否合法
            multipartFile.transferTo(file);
            AddPictureDto addPictureDto = BeanUtil.copyProperties(updatePictureDto, AddPictureDto.class);
            //在数据库中存图片信息
            Picture insertPicture = createInsertPicture(multipartFile, addPictureDto);
            boolean save = this.save(insertPicture);
            return save;
        } catch (IOException e) {
            log.error("文件更新失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deletePicture(Long id) {
        //校验参数
        ThrowUtils.throwIf(id == null,ErrorCode.NOT_FOUND_ERROR);
        //删除文件
        //TODO 删除文件系统的文件
        boolean removeResult = this.lambdaUpdate()
                .eq(Picture::getId, id)
                .remove();
        //返回值
        return removeResult;
    }

    private PagePictureVo toPictureVo(Picture picture) {
        ThrowUtils.throwIf(picture == null,ErrorCode.NOT_FOUND_ERROR,"参数为空");
        PagePictureVo pagePictureVo = BeanUtil.copyProperties(picture, PagePictureVo.class);
        return pagePictureVo;
    }

    private Picture createInsertPicture(MultipartFile multipartFile, AddPictureDto addPictureDto) {
        //参数校验
        ThrowUtils.throwIf(multipartFile.isEmpty(),ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(addPictureDto == null,ErrorCode.NOT_FOUND_ERROR);
        //主体逻辑
        Picture picture = new Picture();
        picture.setPicName(multipartFile.getOriginalFilename());
        String uploadFileName = String.format("%s-%s", LocalDateTime.now(), multipartFile.getOriginalFilename());
        picture.setPicUrl(FileConstant.UPLOAD_PATH + uploadFileName);
        BeanUtil.copyProperties(addPictureDto,picture);
        picture.setCreateTime(LocalDateTime.now());
        picture.setUpdateTime(LocalDateTime.now());
        picture.setEditTime(LocalDateTime.now());
        //TODO 先暂时写死create_user和update_user
        picture.setCreateUser(1L);
        picture.setUpdateUser(1L);
        //返回值
        return picture;
    }

    private boolean validPicture(Picture picture) {
        return true;
    }
}
