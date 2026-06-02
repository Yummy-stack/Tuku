package com.tuku.job.schedule.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tuku.es.document.QuestionEsDoc;
import com.tuku.es.repository.QuestionEsRepository;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukucommon.constant.redis.KeyConstant;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
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
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 全量同步题目数据到 ES
     */
//    @XxlJob("fullSyncQuestionToEs")
    @Scheduled(cron = "0 0 0 * * *")
    public void fullSyncQuestionToEs() {
        log.info("开始全量同步题目数据到 ES");
        // 获取所有题目
        List<Question> questionList = questionService.list();
        Long questionAllCount = questionService.lambdaQuery()
                .count();
        if (questionAllCount == 0) {
            log.info("题目列表为空，无需同步");
            return;
        }

        int pageSize = 500;
        long lastId = 0L;

        AtomicInteger successCount = new AtomicInteger();
        List<CompletableFuture<Void>> taskList = new ArrayList<>();

        while (true) {
            List<Question> QueList = questionService.lambdaQuery()
                    .gt(Question::getId, lastId)
                    .orderByAsc(Question::getId)
                    .last("limit" + pageSize)
                    .list();

            if (QueList == null || QueList.isEmpty()) {
                break;
            }
            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                try {
                    List<QuestionEsDoc> esDocList = QueList.stream()
                            .map(this::convertToEsDoc)
                            .collect(Collectors.toList());
                    questionEsRepository.saveAll(esDocList);
                    log.info("批量同步题目到 ES 成功，数量：{}", esDocList.size());
                    successCount.addAndGet(esDocList.size());
                } catch (Exception e) {
                    log.error("批量同步题目到 ES 失败，记录失败 ID 到 Redis", e);
                    Set<String> failedIds = QueList.stream()
                            .map(item -> String.valueOf(item.getId()))
                            .collect(Collectors.toSet());
                    stringRedisTemplate.opsForSet().add(KeyConstant.QUESTION_SYNC_FAILED_KEY,
                            failedIds.toArray(new String[0]));
                }
            }, threadPoolExecutor);
            taskList.add(task);

        }
        CompletableFuture.allOf(taskList.toArray(new CompletableFuture[0])).join();
        log.info("全量同步题目数据任务提交完成，数量：{}", successCount.get());
    }

    /**
     * 增量同步题目数据到 ES（每 5 分钟同步一次）
     */

    @Scheduled(cron = "0 */5 * * * *")
    @XxlJob("incSyncQuestionToEs")
    public void incSyncQuestionToEs() {
        log.info("开始增量同步题目数据到 ES");
        // 查询最近 5 分钟内修改的数据
        long fiveMinutesAgo = System.currentTimeMillis() - 5 * 60 * 1000L;
        List<Question> questionList = questionService.lambdaQuery()
                .gt(Question::getUpdateTime, new Date(fiveMinutesAgo))
                .list();

        if (questionList == null || questionList.isEmpty()) {
            log.info("最近 5 分钟无更新题目，无需同步");
            return;
        }

        // 异步同步
        CompletableFuture.runAsync(() -> {
            try {
                List<QuestionEsDoc> esDocList = questionList.stream()
                        .map(this::convertToEsDoc)
                        .collect(Collectors.toList());
                questionEsRepository.saveAll(esDocList);
                log.info("增量同步题目到 ES 成功，数量：{}", esDocList.size());
            } catch (Exception e) {
                log.error("增量同步题目到 ES 失败，记录失败 ID 到 Redis", e);
                Set<String> failedIds = questionList.stream()
                        .map(item -> String.valueOf(item.getId()))
                        .collect(Collectors.toSet());
                stringRedisTemplate.opsForSet().add(KeyConstant.QUESTION_SYNC_FAILED_KEY,
                        failedIds.toArray(new String[0]));
            }
        }, threadPoolExecutor);
    }

    /**
     * 补偿同步：定时从 Redis 读取失败的 ID 并重试
     */
//    @XxlJob("compensateSyncQuestionToEs")
    @Scheduled(cron = "0 0 0 * * *")
    public void compensateSyncQuestionToEs() {
        log.info("开始执行题目同步补偿任务");
        // 从 Redis 获取所有失败的 ID
        Set<String> failedIds = stringRedisTemplate.opsForSet().members(KeyConstant.QUESTION_SYNC_FAILED_KEY);
        if (failedIds == null || failedIds.isEmpty()) {
            log.info("当前无失败的题目 ID，无需补偿");
            return;
        }

        log.info("发现 {} 个失败的题目 ID，准备重试同步", failedIds.size());
        List<Long> idList = failedIds.stream().map(Long::parseLong).collect(Collectors.toList());

        // 分批重试，避免一次性查询过多
        int batchSize = 100;
        for (int i = 0; i < idList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, idList.size());
            List<Long> subIdList = idList.subList(i, end);

            List<Question> questions = questionService.listByIds(subIdList);
            if (questions != null && !questions.isEmpty()) {
                try {
                    List<QuestionEsDoc> esDocList = questions.stream()
                            .map(this::convertToEsDoc)
                            .collect(Collectors.toList());
                    questionEsRepository.saveAll(esDocList);
                    log.info("补偿同步成功，数量：{}", esDocList.size());

                    // 同步成功后，从 Redis 中移除已成功的 ID
                    String[] successIds = questions.stream()
                            .map(q -> String.valueOf(q.getId()))
                            .toArray(String[]::new);
                    stringRedisTemplate.opsForSet().remove(KeyConstant.QUESTION_SYNC_FAILED_KEY, (Object[]) successIds);
                } catch (Exception e) {
                    log.error("补偿同步批次失败", e);
                }
            }
        }
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
