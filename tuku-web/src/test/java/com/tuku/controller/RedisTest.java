package com.tuku.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = TukuWebApplication.class)
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testOne() {
        try {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.add("testOne", 1, new Date().getTime());
            Thread.sleep(500);
            zSetOperations.add("testOne", 2, new Date().getTime());
            Thread.sleep(500);
            zSetOperations.add("testOne", 3, new Date().getTime());
            Thread.sleep(500);
            zSetOperations.add("testOne", 4, new Date().getTime());

            redisTemplate.expire("testOne", 10000, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("---------------- 系统发生异常 ----------------");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTwo() {
        try {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOperations.rangeByScoreWithScores("testOne", 1771056292491.0, 1771056293027.0);
            if (typedTuples != null) {
                for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                    System.out.println(typedTuple.getValue());
                    System.out.println(typedTuple.getScore());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThree() {
        try {
            ListOperations<String, Object> listOperations = redisTemplate.opsForList();
            listOperations.leftPush("testList",1);

            List<Object> list = listOperations.rightPop("testList", 1);
            Assertions.assertNotNull(list);
            Object first = list.getFirst();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
