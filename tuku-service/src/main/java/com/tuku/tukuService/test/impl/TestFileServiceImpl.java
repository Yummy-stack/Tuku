package com.tuku.tukuService.test.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuService.test.TestFileService;
import com.tuku.tukucommon.constant.file.FileConstant;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@Slf4j
public class TestFileServiceImpl implements TestFileService {
    @Override
    public String uploadFile(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null || multipartFile.isEmpty(), ErrorCode.NOT_FOUND_ERROR, "上传文件为空");
        String originalFilename = multipartFile.getOriginalFilename();
        ThrowUtils.throwIf(StrUtil.isBlank(originalFilename), ErrorCode.NOT_FOUND_ERROR, "原文件名为空");
        try {
            // 确保上传目录存在
            FileUtil.mkdir(FileConstant.UPLOAD_PATH);

            String ext = FileUtil.extName(originalFilename);
            String uuid = IdUtil.fastSimpleUUID();
            String saveName = StrUtil.isBlank(ext) ? uuid : uuid + "." + ext;

            File dest = new File(FileConstant.UPLOAD_PATH + saveName);
            multipartFile.transferTo(dest);

            log.info("文件上传成功, 原文件名: {}, 保存文件名: {}, 路径: {}", originalFilename, saveName, dest.getAbsolutePath());
            return saveName;
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean downloadFile(String fileName, HttpServletResponse response) {
        ThrowUtils.throwIf(StrUtil.isBlank(fileName), ErrorCode.NOT_FOUND_ERROR, "文件名为空");
        File file = new File(FileConstant.UPLOAD_PATH + fileName);
        ThrowUtils.throwIf(!file.exists(), ErrorCode.NOT_FOUND_ERROR, "文件不存在");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
            IoUtil.copy(fis, os, 1024);
            os.flush();
            return true;
        } catch (IOException e) {
            log.error("文件下载失败: {}", e.getMessage());
            return false;
        }
    }
}