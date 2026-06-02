package com.tuku.tukuService.test.impl;

import cn.hutool.core.io.IoUtil;
import com.tuku.manager.MinioManager;
import com.tuku.tukuService.test.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Resource
    private MinioManager minioManager;

    @Override
    public String uploadFile(MultipartFile multipartFile, String prefix) {
        return minioManager.uploadFile(multipartFile, prefix);
    }

    @Override
    public void downloadFile(String objectName, HttpServletResponse response) {
        try (InputStream inputStream = minioManager.downloadFile(objectName)) {
            String fileName = URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败");
        }
    }

    @Override
    public void deleteFile(String objectName) {
        minioManager.deleteFile(objectName);
    }

    @Override
    public String getFileUrl(String objectName) {
        return minioManager.getFileUrl(objectName);
    }
}
