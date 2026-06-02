package com.tuku.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class ThreadPoolServerConfig {

    @Bean(name = "syncThreadPoolExecutor")
    public ThreadPoolExecutor syncThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                10, // 核心线程数
                20, // 最大线程数
                60, // 闲置时间
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), // 队列容量
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者运行
        );
    }
}
