-- KEYS[1]: ZSet 的 key (future_key)
-- KEYS[2]: List 的 key (topic_key)
-- ARGV[1]: 当前时间戳 (currentTime)
-- ------------------- 将ZSet中的任务同步到List中去 -------------------
-- 1. 获取所有分值小于等于当前时间的任务
local tasks = redis.call('ZRANGEBYSCORE', KEYS[1], 0, ARGV[1])

if #tasks > 0 then
    -- 2. 从 ZSet 中移除这些任务
    redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, ARGV[1])
    -- 3. 将任务逐个压入 List 的左侧
    for i, task in ipairs(tasks) do
        redis.call('LPUSH', KEYS[2], task)
    end
end

return #tasks