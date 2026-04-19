package com.tuku.job.schedule;

import com.tuku.manager.CacheManager;
import com.tuku.tukuMapper.PictureMapper;
import com.tuku.tukuModel.entity.picture.Picture;
import com.tuku.tukuModel.vo.picture.PictureVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tuku.tukucommon.constant.redis.KeyConstant.PICTURE_DETAIL;

@Component
@Slf4j
public class ReCacheJob {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 每天凌晨刷新缓存，保证数据库和缓存一致性
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void refreshCache() {
        log.info("\n----------- refreshCache定时任务开始执行：  每日凌晨刷新缓存 ------------");
        RLock rLock = redissonClient.getLock("refreshCache");

        try {
            if (rLock.tryLock(200, 0, TimeUnit.MILLISECONDS)) {
                refreshPictureCache();
                cacheManager.clearHotKeyCounter();
                log.info("\n------------ refreshCache执行成功：缓存刷新完成 ------------");
            } else {
                log.info("------ 执行失败，其它的集群服务实例的任务获取到了锁 --------");
                return;
            }
        } catch (Exception e) {
            log.error("refreshCache执行失败：{}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 刷新图片缓存
     */
    private void refreshPictureCache() {
        log.info("开始刷新图片缓存...");

        // 获取所有已审核通过的图片
        List<Picture> pictures = pictureMapper.selectAllReviewedPictures();
        if (pictures == null || pictures.isEmpty()) {
            log.info("没有需要刷新缓存的图片");
            return;
        }

        int refreshCount = 0;
        for (Picture picture : pictures) {
            try {
                // 创建缓存键
                String cacheKey = PICTURE_DETAIL + picture.getId();

                // 删除旧缓存
                cacheManager.delete(cacheKey);

                // 创建新的PictureVO
                PictureVO pictureVO = PictureVO.entityToVo(picture);

                // 重新设置缓存
                int expireTime = cacheManager.getRandomExpireTime(200, 100);
                cacheManager.set(cacheKey, pictureVO, expireTime);

                refreshCount++;
            } catch (Exception e) {
                log.error("刷新图片缓存失败，图片ID：{}，错误：{}", picture.getId(), e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }

        log.info("图片缓存刷新完成，共刷新 {} 张图片", refreshCount);
    }
}