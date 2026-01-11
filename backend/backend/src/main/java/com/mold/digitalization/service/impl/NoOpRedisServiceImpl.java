package com.mold.digitalization.service.impl;

import com.mold.digitalization.service.RedisService;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * No-Op RedisService 实现
 * 当 app.redis.use=false 时启用，避免在未启动 Redis 的环境下发生连接错误。
 */
@Service
@Primary
@ConditionalOnProperty(name = "app.redis.use", havingValue = "false", matchIfMissing = true)
public class NoOpRedisServiceImpl implements RedisService {

    @Override
    public void set(String key, String value, long expireTime) {
        // no-op
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {
        // no-op
    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void set(String key, String value) {
        // no-op
    }

    @Override
    public boolean expire(String key, long expireTime) {
        return true;
    }

    @Override
    public Long sAdd(String key, String... values) {
        return 0L;
    }

    @Override
    public Set<String> keys(String pattern) {
        return Collections.emptySet();
    }
}

