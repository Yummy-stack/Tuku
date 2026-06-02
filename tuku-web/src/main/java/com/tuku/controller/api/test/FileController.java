package com.tuku.controller.api.test;

import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuService.test.IFileService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api(value = "文件管理接口")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IFileService fileService;

    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam String prefix) {
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        String path = fileService.uploadFile(file, prefix);
        return ResultUtils.success(path);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("/download")
    public void downloadFile(@RequestParam String objectName, HttpServletResponse response) {
        fileService.downloadFile(objectName, response);
    }

    @ApiOperation(value = "删除文件")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFile(@RequestParam String objectName) {
        fileService.deleteFile(objectName);
        return ResultUtils.success(true);
    }

    @ApiOperation(value = "获取文件外链")
    @GetMapping("/url")
    public BaseResponse<String> getFileUrl(@RequestParam String objectName) {
        String url = fileService.getFileUrl(objectName);
        return ResultUtils.success(url);
    }
}
