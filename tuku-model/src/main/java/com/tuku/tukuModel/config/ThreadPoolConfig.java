package com.tuku.tukuModel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync  // 开启异步支持，让 @Async 生效
public class ThreadPoolConfig {

    /**
     * 自定义线程池，Bean 名称为 "customExecutor"
     */
    @Bean(name = "CustomExecutor")
    public ThreadPoolTaskExecutor customExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(20);
        // 队列容量
        executor.setQueueCapacity(200);
        // 线程存活时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名前缀
        executor.setThreadNamePrefix("custom-task-");

        // 拒绝策略：由调用线程处理（提交任务的线程执行）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 是否等待所有任务完成再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间（秒），超时后强制销毁
        executor.setAwaitTerminationSeconds(60);

        // 初始化线程池
        executor.initialize();
        return executor;
    }
}