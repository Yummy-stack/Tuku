package com.tuku.controller;

import com.tuku.tukuModel.entity.picture.Picture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class TuKuApplicationTestsTwo {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void TestRedis() {
        Picture picture = new Picture();
        picture.setId(1L);
        picture.setReviewStatus(0);
//        stringRedisTemplate.opsForValue().set("key", "value");
//        stringRedisTemplate.opsForValue().set("test1", "test1",300, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set("test2", "test2",300, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("test3", picture,300, TimeUnit.MINUTES);


//
//        String test1 = stringRedisTemplate.opsForValue().get("test1");
//        String test2 = stringRedisTemplate.opsForValue().get("test2");

//        System.out.printf("===========> %s =================> %s%n",test1,test2);
    }

}
