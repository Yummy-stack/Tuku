package com.tuku.job.schedule.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tuku.es.document.QuestionEsDoc;
import com.tuku.es.repository.QuestionEsRepository;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukucommon.constant.redis.KeyConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 题目数据同步到 ES 任务
 */
@Component
@Slf4j
public class QuestionSyncJob {

    @Resource
    private IQuestionService questionService;

    @Resource
    private QuestionEsRepository questionEsRepository;

    @Resource(name = "syncThreadPoolExecutor")
    private ThreadPoolExecutor syncThreadPoolExecutor;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 全量同步题目数据到 ES
     */
    @XxlJob("fullSyncQuestionToEs")
    public void fullSyncQuestionToEs() {
        log.info("开始全量同步题目数据到 ES");
        XxlJobHelper.log("开始全量同步题目数据到 ES");

        // 获取分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.info("当前分片: {}/{}", shardIndex, shardTotal);

        // 查询符合当前分片条件的题目总数
        Long questionAllCount = questionService.lambdaQuery()
                .apply(shardTotal > 1, "id % " + shardTotal + " = " + shardIndex)
                .count();
        if (questionAllCount == 0) {
            log.info("分片 {} 题目列表为空，无需同步", shardIndex);
            XxlJobHelper.log("分片 {} 题目列表为空，无需同步", shardIndex);
            return;
        }
        log.info("分片 {} 预计同步题目数量: {}", shardIndex, questionAllCount);

        int pageSize = 500;
        long lastId = 0L;
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // 并发控制：最多允许同时执行 5 个异步任务（可根据环境调整）
        final int MAX_CONCURRENT = 900;
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT);
        List<CompletableFuture<Void>> taskList = new ArrayList<>();

        // 主循环：游标分页读取数据
        while (true) {
            // 申请许可，防止瞬间提交过多任务
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("获取信号量时被中断", e);
                break;
            }

            List<Question> questionList;
            try {
                questionList = questionService.lambdaQuery()
                        .gt(Question::getId, lastId)
                        // 分片过滤，确保每个分片只处理自己负责的数据
                        .apply(shardTotal > 1, "id % " + shardTotal + " = " + shardIndex)
                        .orderByAsc(Question::getId)
                        .last(" LIMIT " + pageSize)  // 修正 SQL 拼接
                        .list();
            } catch (Exception e) {
                semaphore.release(); // 查询异常时释放许可
                log.error("分片 {} 查询数据库失败", shardIndex, e);
                XxlJobHelper.log("分片 {} 查询数据库失败: {}", shardIndex, e.getMessage());
                break;
            }

            if (questionList == null || questionList.isEmpty()) {
                semaphore.release(); // 无数据，释放刚才申请的许可
                break;
            }

            // 更新游标
            lastId = questionList.get(questionList.size() - 1).getId();

            // 提交异步同步任务
            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                try {
                    List<QuestionEsDoc> esDocList = questionList.stream()
                            .map(this::convertToEsDoc)
                            .collect(Collectors.toList());

                    // 批量保存，并捕获部分失败
                    questionEsRepository.saveAll(esDocList); // 方法本身不返回详细状态，需视实际情况增强
                    // 注意：如果 saveAll 内部是 bulk 操作，建议改用更底层 API 以获取逐条结果。
                    // 此处假设 saveAll 整体成功即为全部成功，若抛异常则记录整批。
                    int size = esDocList.size();
                    successCount.addAndGet(size);
                    log.info("分片 {} 批量同步题目到 ES 成功，数量：{}", shardIndex, size);
                    XxlJobHelper.log("分片 {} 批量同步题目到 ES 成功，数量：{}", shardIndex, size);

                } catch (Exception e) {
                    // 批量保存整体失败，整批记入失败集合
                    log.error("分片 {} 批量同步题目到 ES 失败，数量：{}", shardIndex, questionList.size(), e);
                    XxlJobHelper.log("分片 {} 批量同步题目到 ES 失败，数量：{}，原因：{}", shardIndex, questionList.size(), e.getMessage());
                    Set<String> failedIds = questionList.stream()
                            .map(item -> String.valueOf(item.getId()))
                            .collect(Collectors.toSet());
                    try {
                        stringRedisTemplate.opsForSet()
                                .add(KeyConstant.QUESTION_SYNC_FAILED_KEY, failedIds.toArray(new String[0]));
                        // 设置过期时间（例如 7 天），避免 Key 持续膨胀
                        stringRedisTemplate.expire(KeyConstant.QUESTION_SYNC_FAILED_KEY, 7, TimeUnit.DAYS);
                    } catch (Exception redisEx) {
                        log.error("记录失败 ID 到 Redis 时出错", redisEx);
                    }
                    failCount.addAndGet(questionList.size());

                } finally {
                    // 无论成功与否，释放许可
                    semaphore.release();
                }
            }, syncThreadPoolExecutor).exceptionally(ex -> {
                // 以防 runAsync 内部未捕获的异常（如 convertToEsDoc 抛出 RuntimeException）
                log.error("分片 {} 异步任务发生未知异常", shardIndex, ex);
                XxlJobHelper.log("分片 {} 异步任务发生未知异常: {}", shardIndex, ex.getMessage());
                failCount.addAndGet(questionList.size());
                semaphore.release(); // 确保释放，虽然 finally 已处理，但双保险
                return null;
            });

            taskList.add(task);
        }

        // 等待所有已提交的任务完成
        try {
            CompletableFuture.allOf(taskList.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("分片 {} 等待异步任务完成时发生异常", shardIndex, e);
            XxlJobHelper.log("分片 {} 等待任务完成异常: {}", shardIndex, e.getMessage());
            // 不中断，继续输出统计
        }

        // 等待期间可能还有任务在执行，这里再尝试获取剩余许可，确保真正全部结束（可选）
        // 由于 each task 都会 release，理论上 allOf 完成即所有任务结束，无需额外处理

        log.info("分片 {} 全量同步题目数据完成，成功数量：{}，失败数量：{}", shardIndex, successCount.get(), failCount.get());
        XxlJobHelper.log("分片 {} 全量同步题目数据完成，成功数量：{}，失败数量：{}", shardIndex, successCount.get(), failCount.get());
    }

    /**
     * 增量同步题目数据到 ES（每 5 分钟同步一次）
     */


    /**
     * 使用 Redis 原子操作记录分片完成标记，当所有分片都完成时更新 lastSyncTime
     */
    private void updateLastSyncTimeIfAllShardsDone(int shardIndex, int shardTotal, LocalDateTime newSyncTime) {
        String completeKey = KeyConstant.QUESTION_SYNC_FAILED_KEY + ":" + newSyncTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 记录当前分片已完成
        stringRedisTemplate.opsForSet().add(completeKey, String.valueOf(shardIndex));
        stringRedisTemplate.expire(completeKey, 10, TimeUnit.MINUTES);

        // 检查是否所有分片都完成了
        Long doneCount = stringRedisTemplate.opsForSet().size(completeKey);
        if (doneCount != null && doneCount >= shardTotal) {
            // 所有分片完成，更新全局水位
            stringRedisTemplate.opsForValue().set(KeyConstant.QUESTION_SYNC_FAILED_KEY, newSyncTime.toString());
            log.info("所有分片已完成，更新 lastSyncTime 为 {}", newSyncTime);
            // 清理完成标记
            stringRedisTemplate.delete(completeKey);
        }
    }

    @XxlJob("compensateSyncQuestionToEs")
    public void compensateSyncQuestionToEs() {
        String failedKey = KeyConstant.QUESTION_SYNC_FAILED_KEY;
        Map<Object, Object> failedMap = stringRedisTemplate.opsForHash().entries(failedKey);
        if (failedMap.isEmpty()) {
            return;
        }

        int maxRetry = 3;
        long retryInterval = TimeUnit.MINUTES.toMillis(5); // 5 分钟间隔
        List<Long> successIds = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : failedMap.entrySet()) {
            String idStr = (String) entry.getKey();
            Long id = Long.valueOf(idStr);
            String[] parts = ((String) entry.getValue()).split(":");
            int retryCount = Integer.parseInt(parts[0]);
            long lastFailTime = Long.parseLong(parts[1]);

            if (retryCount >= maxRetry) {
                // 移到死信队列
                stringRedisTemplate.opsForSet().add(KeyConstant.QUESTION_SYNC_FAILED_KEY, idStr);
                stringRedisTemplate.opsForHash().delete(failedKey, idStr);
                log.warn("同步失败重试次数耗尽，ID: {}", id);
                continue;
            }
            if (System.currentTimeMillis() - lastFailTime < retryInterval) {
                // 未到重试间隔，跳过
                continue;
            }

            // 重新同步
            try {
                Question question = questionService.getById(id);
                if (question == null) {
                    // 原数据已不存在，直接移除失败记录
                    stringRedisTemplate.opsForHash().delete(failedKey, idStr);
                    continue;
                }
                QuestionEsDoc doc = convertToEsDoc(question);
                questionEsRepository.save(doc); // 单条保存
                successIds.add(id);
            } catch (Exception e) {
                // 更新重试次数
                int newRetry = retryCount + 1;
                stringRedisTemplate.opsForHash().put(failedKey, idStr, newRetry + ":" + System.currentTimeMillis());
                log.error("补偿同步失败，ID: {}, 重试次数: {}", id, newRetry, e);
            }
        }

        // 批量删除成功的 ID
        if (!successIds.isEmpty()) {
            stringRedisTemplate.opsForHash().delete(failedKey, successIds.stream().map(String::valueOf).toArray());
        }
        log.info("补偿任务完成，成功: {} 条", successIds.size());
    }

    /**
     * 将 Question 转换为 QuestionEsDoc
     */
    private QuestionEsDoc convertToEsDoc(Question question) {
        QuestionEsDoc esDoc = BeanUtil.copyProperties(question, QuestionEsDoc.class, "tags");
        String tagsStr = question.getTags();
        if (StrUtil.isNotBlank(tagsStr)) {
            try {
                esDoc.setTags(JSONUtil.toList(tagsStr, String.class));
            } catch (Exception e) {
                log.error("解析题目标签 JSON 失败，id: {}, tags: {}", question.getId(), tagsStr);
            }
        }
        return esDoc;
    }
}
