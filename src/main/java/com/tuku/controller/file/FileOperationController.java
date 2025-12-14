package com.tuku.controller.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.tuku.constant.file.FileConstant;
import com.tuku.model.enums.error.ErrorCode;
import com.tuku.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.tuku.constant.file.FileConstant.UPLOAD_PATH;

@RestController
@RequestMapping("/file")
@CrossOrigin
@Slf4j
@Api(tags = "文件上传和下载的接口")
public class FileOperationController {
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    boolean uploadPicture(MultipartFile multipartFile) {
        //参数判空
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(multipartFile.isEmpty(),ErrorCode.NOT_FOUND_ERROR);
        //逻辑实现
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            ThrowUtils.throwIf(originalFilename == null || originalFilename.isEmpty(),ErrorCode.NOT_FOUND_ERROR);

            File file = new File(UPLOAD_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }

            File uploadFile = new File(UPLOAD_PATH + originalFilename + multipartFile);

            multipartFile.transferTo(uploadFile);
            //返回值
            log.info("文件上传成功: {}",originalFilename);
            return true;
        } catch (IOException e) {
            log.error("文件上传失败；{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    boolean downloadPicture(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        //参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(fileName),ErrorCode.NOT_FOUND_ERROR);
        // 逻辑实现
        try {
            // 构造文件路径
            File file = new File(FileConstant.UPLOAD_PATH + fileName);

            // 检查文件是否存在
            ThrowUtils.throwIf(!file.exists(), ErrorCode.NOT_FOUND_ERROR, "文件不存在");

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 使用try-with-resources自动管理资源
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }

                os.flush();
            }

            // 返回值
            return true;
        } catch (IOException e) {
            log.error("文件下载失败: {}", e.getMessage());
            return false;
        }
    }

    @ApiOperation(value = "文件删除")
    @PostMapping("/delete")
    boolean deletePicture() {
        return true;
    }
}
