package com.tuku.job.delay;

import com.tuku.tukuModel.entity.task.Taskinfo;
import com.tuku.tukuService.task.ITaskinfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.tuku.tukucommon.constant.task.ScheduleConstants.FUTURE;
import static com.tuku.tukucommon.constant.task.ScheduleConstants.TOPIC;

@Component
@Slf4j
public class DelayMQSystem {
    @Resource
    private ITaskinfoService taskinfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

//    @Scheduled(fixedRate = 5000)
//    public void DbToRedis() {
//        RLock lock = redissonClient.getLock("DbToRedis");
//        try {
//            if (lock.tryLock(0, 30, TimeUnit.SECONDS)) {
//                int fiveTime = 5 * 60 * 1000;
//                Date date = new Date();
//                long fiveTimeDate = date.getTime() + fiveTime;
//
//                List<Taskinfo> taskinfos = taskinfoService.lambdaQuery()
//                        .gt(Taskinfo::getExecuteTime, fiveTimeDate)
//                        .list();
//
//                for (Taskinfo taskinfo : taskinfos) {
//                    Date executeTime = taskinfo.getExecuteTime();
//                    if (date.compareTo(executeTime) >= 0) {
//                        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
//                        String listKey = "pic:" + FUTURE + "taskinfo";
//                        listOperations.leftPush(listKey, taskinfo);
//                    } else {
//                        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
//                        Date executeTime1 = taskinfo.getExecuteTime();
//                        if (executeTime1 != null) {
//                            long executeTime1Time = executeTime1.getTime();
//                            double scoreTime = executeTime1Time * 1.0;
//                            String ZSetKey = "pic:" + TOPIC + "taskinfo";
//                            zSetOperations.add(ZSetKey, taskinfo, scoreTime);
//                        } else {
//                            throw new RuntimeException("任务的执行时间参数为空");
//                        }
//                        zSetOperations.add("taskinfo", taskinfo, 1.0);
//                    }
//                }
//            } else {
//                log.error(" ------------ 获取锁失败，其它节点已经获取到锁 --------------- ");
//            }
//        } catch (Exception e) {
//            log.error(" ---------------- DbToRedis 同步逻辑 出现异常 -------------------- ");
//            throw new RuntimeException(e.getMessage());
//        } finally {
//            if (lock != null) {
//                if (lock.isHeldByCurrentThread()) {
//                    lock.unlock();
//                }
//            }
//        }
//    }
//
//    @Scheduled(fixedRate = 5000)
//    public void ZSetToList() {
//        RLock lock = redissonClient.getLock("ZSetToList");
//        try {
//            if (lock.tryLock(0, 30, TimeUnit.SECONDS)) {
//                ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
//                String ZSetKey = "pic:" + TOPIC + "taskinfo";
//                Set<String> futureTaskSet = redisTemplate.keys(ZSetKey);
//
//            }
//        } catch (Exception e) {
//            log.error(" ------------------ ZSetToList 同步任务执行异常 ------------------ ");
//            throw new RuntimeException(e);
//        } finally {
//            if (lock != null) {
//                if (lock.isHeldByCurrentThread()) {
//                    lock.unlock();
//                }
//            }
//        }
//    }
}
