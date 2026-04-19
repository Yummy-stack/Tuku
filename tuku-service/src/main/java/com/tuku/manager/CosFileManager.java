package com.tuku.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class CosFileManager {
    @Resource
    private COSClient cosClient;

    public PutObjectResult putFile(String bucketName, File file, String cosFilePath) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, cosFilePath, file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return putObjectResult;
        } catch (CosClientException cce) {
            throw new RuntimeException("COS文件上传失败");
        }
    }

    public InputStream getFileInputStream(String bucketName,String cosFiletPath) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, cosFiletPath);
        InputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            return cosObjectInput;
        } catch (CosClientException e) {
            throw new RuntimeException("COS文件的读流失败");
        }
    }

    public void deleteOneFile(String bucketName,String cosFilePath) {
        try {
            cosClient.deleteObject(bucketName, cosFilePath);
        } catch (CosClientException e) {
            throw new RuntimeException("COS文件删除失败");
        }
    }
}
