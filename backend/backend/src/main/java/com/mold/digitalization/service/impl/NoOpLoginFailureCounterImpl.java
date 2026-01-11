package com.mold.digitalization.service.impl;

import com.mold.digitalization.service.LoginFailureCounter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 无 Redis 环境下的登录失败计数器实现（内存版）
 * 当 app.redis.use=false 时启用，避免依赖 Redis。
 */
@Service
@Primary
@ConditionalOnProperty(name = "app.redis.use", havingValue = "false", matchIfMissing = true)
public class NoOpLoginFailureCounterImpl implements LoginFailureCounter {

    private final ConcurrentMap<String, Integer> counters = new ConcurrentHashMap<>();

    @Override
    public int incrementFailureCount(String username) {
        return counters.merge(username, 1, Integer::sum);
    }

    @Override
    public void resetFailureCount(String username) {
        counters.remove(username);
    }

    @Override
    public int getFailureCount(String username) {
        return counters.getOrDefault(username, 0);
    }

    @Override
    public boolean isThresholdReached(String username, int threshold) {
        return getFailureCount(username) >= threshold;
    }
}

