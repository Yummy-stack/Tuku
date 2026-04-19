package com.tuku.tukuService.test;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface TestFileService {
    /**
     * 上传文件，返回保存后的文件名
     */
    String uploadFile(MultipartFile multipartFile);

    /**
     * 下载文件，将文件内容写入响应
     */
    boolean downloadFile(String fileName, HttpServletResponse response);
}