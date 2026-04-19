package com.tuku.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

@SpringBootTest(classes = TukuWebApplication.class)
@Slf4j
public class ElasticSearchTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void test1() {

    }
}
