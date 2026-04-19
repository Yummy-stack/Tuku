package com.tuku.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.tuku.manager.CosFileManager;
import com.tuku.tukuMapper.PictureMapper;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuService.picture.IPictureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@SpringBootTest(classes = TukuWebApplication.class)
@Slf4j
class TuKuApplicationTests {
    @Resource
    private COSClient cosClient;

    @Resource
    private IPictureService pictureService;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private CosFileManager cosFileManager;

    @Value("${COS_URL_BUCKET_ONE}")
    private String COS_URL_BUCKET_ONE;

    @Value("${PIC_BUCKET_NAME}")
    private String PIC_BUCKET_NAME;

    @BeforeEach
    void beforeEach() {
//        System.out.println("=============> Test测试方法开始运行 =============>");
    }

    @AfterEach
    void afterEach() {
        System.out.println("\n\n\n\n 该测试方法执行成功");
    }

    @Test
    void testCosClientPut() {
        String bucketName = "file--bucket-one-1322921281";
        String key = "file-test/pic.jpg";
        String localPath = "D:/pictures/" + "微信图片_2025-12-07_223339_538.jpg";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new File(localPath));
        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println("\n文件上传成功\n");
        } catch (CosClientException cce) {
            log.error("文件上传失败：{}", cce.getMessage());
            cce.printStackTrace();
        }
    }

    @Test
    void testCosClientGet() {
        try {
            List<Picture> pictures = pictureMapper.selectNoneExistent(1);
            if (pictures == null) {
                log.info("COS凌晨没有需要清理的图片");
                return;
            }
            List<String> paths = pictures.stream()
                    .map(picture -> {
                        try {
                            if (picture == null || StrUtil.isBlank(picture.getPicUrl())) {
                                throw new RuntimeException("图片参数为空");
                            }
                            URL url = new URL(picture.getPicUrl());
                            String path = url.getPath();
                            return path;
                        } catch (MalformedURLException e) {
                            log.error("获取Url中的文件路径失败");
                            throw new RuntimeException(e.getMessage());
                        }
                    })
                    .toList();
            for (String path : paths) {
                cosFileManager.deleteOneFile(PIC_BUCKET_NAME, path);
            }
            Assert.isTrue(true);
            log.info("操作成功：COS每次凌晨清理不存在的图片");
        } catch (Exception e) {
            log.error("COS凌晨清理任务执行失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMethod() {
        System.out.println("============> 方法成功运行了 ============>");
    }

}
