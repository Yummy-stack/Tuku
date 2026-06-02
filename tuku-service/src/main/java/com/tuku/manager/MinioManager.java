package com.tuku.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.tuku.tukucommon.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MinioManager {
    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param prefix        目录前缀
     * @return 文件路径
     */
    public String uploadFile(MultipartFile multipartFile, String prefix) {
        String filename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(filename);
        String objectName = prefix + "/" + DateUtil.today() + "/" + IdUtil.simpleUUID() + "." + suffix;
        try {
            createBucketIfNotExists();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            return objectName;
        } catch (Exception e) {
            log.error("MinIO upload error", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 下载文件
     *
     * @param objectName 对象名称
     * @return 输入流
     */
    public InputStream downloadFile(String objectName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error("MinIO download error", e);
            throw new RuntimeException("文件下载失败");
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     */
    public void deleteFile(String objectName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("MinIO delete error", e);
            throw new RuntimeException("文件删除失败");
        }
    }

    /**
     * 获取文件外链（有效期 7 天）
     *
     * @param objectName 对象名称
     * @return 外链地址
     */
    public String getFileUrl(String objectName) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .expiry(7, TimeUnit.DAYS)
                    .build();
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            log.error("MinIO get url error", e);
            throw new RuntimeException("获取文件链接失败");
        }
    }

    /**
     * 检查并创建存储桶
     */
    private void createBucketIfNotExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
        }
    }
}
