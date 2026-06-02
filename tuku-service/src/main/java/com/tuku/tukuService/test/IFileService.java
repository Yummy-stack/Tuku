package com.tuku.tukuService.test;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IFileService {
    /**
     * 上传文件
     */
    String uploadFile(MultipartFile multipartFile, String prefix);

    /**
     * 下载文件
     */
    void downloadFile(String objectName, HttpServletResponse response);

    /**
     * 删除文件
     */
    void deleteFile(String objectName);

    /**
     * 获取文件外链
     */
    String getFileUrl(String objectName);
}
