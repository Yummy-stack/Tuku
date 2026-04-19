package com.tuku.controller.api.file.cos;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tuku.tukucommon.constant.file.FileConstant.UPLOAD_PATH;


@RestController
@RequestMapping("/file/cos")
@CrossOrigin
@Slf4j
@Api(tags = "COS - 文件上传和下载的接口")
public class COSFileController {
    @ApiOperation(value = "文件上传至COS")
    @PostMapping("/upload")
    boolean upload(@RequestParam("file") MultipartFile multipartFile) {
        //参数判空
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(multipartFile.isEmpty(), ErrorCode.NOT_FOUND_ERROR);
        //逻辑实现
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            ThrowUtils.throwIf(originalFilename == null || originalFilename.isEmpty(), ErrorCode.NOT_FOUND_ERROR);

            File dir = new File(UPLOAD_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String suffix = FileUtil.getSuffix(originalFilename);
            String saveName = IdUtil.fastSimpleUUID() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (suffix.isEmpty() ? "" : "." + suffix);
            File uploadFile = new File(dir, saveName);

            multipartFile.transferTo(uploadFile);
            //返回值
            log.info("文件上传成功: {} -> {}", originalFilename, saveName);
            return true;
        } catch (IOException e) {
            log.error("文件上传失败；{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @ApiOperation(value = "COS下载文件")
    @GetMapping("/download")
    boolean download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        //参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(fileName), ErrorCode.NOT_FOUND_ERROR);
        // 逻辑实现
        try {
            // 构造文件路径
            File file = new File(UPLOAD_PATH, fileName);

            // 检查文件是否存在
            ThrowUtils.throwIf(!file.exists(), ErrorCode.NOT_FOUND_ERROR, "文件不存在");

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

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

    @ApiOperation(value = "COS删除文件")
    @PostMapping("/delete")
    boolean delete() {
        return true;
    }

    @ApiOperation(value = "COS查询文件")
    @PostMapping("/is-exist")
    boolean isExist() {
        return true;
    }
}
