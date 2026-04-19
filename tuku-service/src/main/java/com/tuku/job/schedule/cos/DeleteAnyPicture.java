package com.tuku.job.schedule.cos;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.exception.CosClientException;
import com.tuku.manager.CosFileManager;
import com.tuku.tukuMapper.PictureMapper;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuService.picture.IPictureService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tuku.tukucommon.constant.date.DateConstants.THIRTY_DAYS_IN_MILLIS;

@Component
@Slf4j
public class DeleteAnyPicture {
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

    /**
     * COS每次凌晨清理不存在的图片
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAnyPicture() {
        log.info("\n----------- deleteAnyPicture定时任务开始执行：  COS每日凌晨清理不存在的图片 ------------");

        List<Picture> pictures = pictureMapper.selectNoneExistent(1);
        if (pictures == null || pictures.isEmpty()) {
            log.info("-------- COS凌晨没有需要清理的图片 --------");
            return;
        }
        List<String> paths = pictures.stream()
                .map(this::getCosUrlPath)
                .toList();
        if (paths.isEmpty()) {
            return;
        }
        ArrayList<String> successPaths = new ArrayList<>();
        ArrayList<String> failedPaths = new ArrayList<>();
        for (String path : paths) {
            try {
                cosFileManager.deleteOneFile(PIC_BUCKET_NAME, path);
                successPaths.add(path);
            } catch (CosClientException e) {
                log.error("--------- 参数错误，文件删除失败 -----------");
                failedPaths.add(path);
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("--------- 系统错误 -----------");
                failedPaths.add(path);
                throw new RuntimeException(e);
            }
        }

        log.info("\n------------ deleteAnyPicture执行成功：本次成功清理：{}，-------------", successPaths.size());

    }

    private String getCosUrlPath(Picture picture) {
        try {
            if (picture == null || StrUtil.isBlank(picture.getPicUrl())) {
                throw new RuntimeException("图片参数为空");
            }
            URL url = new URL(picture.getPicUrl());
            String path = url.getPath();
            if (path.startsWith("/")) {
                return path.substring(1);
            }
            return path;
        } catch (MalformedURLException e) {
            log.error("获取Url中的文件路径失败");
            throw new RuntimeException(e.getMessage());
        }
    }
}
