package com.tuku.manager;

import cn.hutool.core.util.RandomUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tuku.tukucommon.constant.redis.KeyConstant.*;

@Component
@Slf4j
public class CacheManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    // Caffeine本地缓存
    private final Cache<String, Object> localCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    // 布隆过滤器
    private RBloomFilter<String> bloomFilter;

    /**
     * 初始化布隆过滤器
     */
    @PostConstruct
    public void init() {
        try {
            bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
            if (!bloomFilter.isExists()) {
                bloomFilter.tryInit(1000000L, 0.01);
                log.info("布隆过滤器初始化成功，预期容量：1000000，误判率：0.01");
            } else {
                log.info("布隆过滤器已存在");
            }
        } catch (Exception e) {
            log.error("布隆过滤器初始化失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public Object get(String key) {
        // 热Key探测
        incrementKeyCount(key);

        // 1. 先从本地缓存获取
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        // 2. 从Redis获取
        try {
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                // 同步到本地缓存
                localCache.put(key, value);
            }
        } catch (Exception e) {
            log.error("Redis get error: {}", e.getMessage());
        }

        return value;
    }

    /**
     * 获取哈希缓存
     *
     * @param key 缓存键
     * @return 哈希值
     */
    public Map<Object, Object> getHash(String key) {
        // 热Key探测
        incrementKeyCount(key);

        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Redis getHash error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 设置缓存
     *
     * @param key        缓存键
     * @param value      缓存值
     * @param expireTime 过期时间（分钟）
     */
    public void set(String key, Object value, int expireTime) {
        // 1. 设置本地缓存
        localCache.put(key, value);

        // 2. 设置Redis缓存
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MINUTES);
            // 添加到布隆过滤器
            bloomFilter.add(key);
        } catch (Exception e) {
            log.error("----------- Redis set error: {} ---------------", e.getMessage());
        }
    }

    /**
     * 设置哈希缓存
     *
     * @param key        缓存键
     * @param hashKey    哈希键
     * @param value      缓存值
     * @param expireTime 过期时间（分钟）
     */
    public void setHash(String key, String hashKey, Object value, int expireTime) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            // 添加到布隆过滤器
            bloomFilter.add(key);
        } catch (Exception e) {
            log.error("Redis setHash error: {}", e.getMessage());
        }
    }

    /**
     * 设置哈希缓存（批量）
     *
     * @param key        缓存键
     * @param map        哈希值映射
     * @param expireTime 过期时间（分钟）
     */
    public void setHashAll(String key, Map<String, Object> map, int expireTime) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            // 添加到布隆过滤器
            bloomFilter.add(key);
        } catch (Exception e) {
            log.error("Redis setHashAll error: {}", e.getMessage());
        }
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    public void delete(String key) {
        // 1. 删除本地缓存
        localCache.invalidate(key);

        // 2. 删除Redis缓存
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete error: {}", e.getMessage());
        }
    }

    /**
     * 检查键是否存在于布隆过滤器
     *
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean mightContain(String key) {
        try {
            return bloomFilter.contains(key);
        } catch (Exception e) {
            log.error("布隆过滤器检查失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 增加热Key计数（用于热Key探测）
     *
     * @param key 缓存键
     */
    private void incrementKeyCount(String key) {
        try {
            String counterKey = HOT_KEY_COUNTER_PREFIX + key;
            Long count = redisTemplate.opsForValue().increment(counterKey);

            // 设置过期时间为24小时
            if (count == 1) {
                redisTemplate.expire(counterKey, 24, TimeUnit.HOURS);
            }

            // 检查是否达到热Key阈值
            if (count != null && count == HOT_KEY_THRESHOLD) {
                log.info("检测到热Key：{}，访问次数：{}", key, count);
                // 可以在这里添加热Key处理逻辑，例如增加过期时间、备份等
            }
        } catch (Exception e) {
            log.error("热Key计数失败：{}", e.getMessage());
        }
    }

    /**
     * 获取随机过期时间（防止缓存雪崩）
     *
     * @param baseTime 基础过期时间
     * @param range    随机范围
     * @return 随机过期时间
     */
    public int getRandomExpireTime(int baseTime, int range) {
        return baseTime + RandomUtil.randomInt(range);
    }

    /**
     * 清空热Key计数器
     */
    public void clearHotKeyCounter() {
        try {
            RKeys keys = redissonClient.getKeys();
            Iterable<String> matchedKeys = keys.getKeysByPattern(HOT_KEY_COUNTER_PREFIX + "*");
            for (String key : matchedKeys) {
                redisTemplate.delete(key);
            }
            log.info("热Key计数器清空完成");
        } catch (Exception e) {
            log.error("清空热Key计数器失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取热Key列表
     *
     * @param limit 返回数量限制
     * @return 热Key列表
     */
    public Map<String, Object> getHotKeys(int limit) {
        Map<String, Object> hotKeys = new HashMap<>();
        try {
            RKeys keys = redissonClient.getKeys();
            Iterable<String> matchedKeys = keys.getKeysByPattern(HOT_KEY_COUNTER_PREFIX + "*");
            int count = 0;
            for (String key : matchedKeys) {
                if (count >= limit) {
                    break;
                }
                Object value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    String realKey = key.substring(HOT_KEY_COUNTER_PREFIX.length());
                    hotKeys.put(realKey, value);
                    count++;
                }
            }
        } catch (Exception e) {
            log.error("获取热Key列表失败：{}", e.getMessage(), e);
        }
        return hotKeys;
    }
}
