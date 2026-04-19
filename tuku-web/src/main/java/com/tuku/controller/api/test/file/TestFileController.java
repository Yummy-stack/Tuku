package com.tuku.controller.api.test.file;

import cn.hutool.core.util.StrUtil;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuService.test.TestFileService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.Delimiter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test/file")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "TestUploadDownloadApi")
public class TestFileController {
    private final TestFileService testFileService;

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/upload")
    public BaseResponse<String> uploadFile(MultipartFile multipartFile) {
        boolean isNull = multipartFile == null || multipartFile.isEmpty();
        ThrowUtils.throwIf(isNull, ErrorCode.NOT_FOUND_ERROR);
        String savedFileName = testFileService.uploadFile(multipartFile);
        return ResultUtils.success(savedFileName);
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public BaseResponse<Boolean> downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        ThrowUtils.throwIf(StrUtil.isBlank(fileName), ErrorCode.NOT_FOUND_ERROR);
        boolean result = testFileService.downloadFile(fileName, response);
        return ResultUtils.success(result);
    }
}