package com.tuku.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class RedissonConfig {

    private final String redisPro = "redis://redis:6379";

    private final String redisLocal = "redis://127.0.0.1:6379";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisLocal)
                .setDatabase(3);
        return Redisson.create(config);
    }

}
