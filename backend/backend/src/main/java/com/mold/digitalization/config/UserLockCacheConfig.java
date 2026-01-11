package com.mold.digitalization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 用户锁定缓存配置类
 * 管理用户锁定相关的Redis缓存配置
 */
@Configuration
public class UserLockCacheConfig {

    /**
     * 用户锁定信息缓存键前缀
     */
    public static final String USER_LOCK_KEY_PREFIX = "user:lock:";

    /**
     * 登录失败次数缓存键前缀
     */
    public static final String LOGIN_FAILED_KEY_PREFIX = "user:login:failed:";

    /**
     * 锁定信息缓存默认过期时间（秒）
     * 默认2小时
     */
    public static final int DEFAULT_CACHE_EXPIRE_SECONDS = 7200;

    /**
     * 登录失败临时缓存过期时间（秒）
     * 默认10分钟
     */
    public static final int LOGIN_FAILED_CACHE_EXPIRE_SECONDS = 600;

    @Autowired
    private UserLockProperties userLockProperties;

    /**
     * 获取用户锁定信息缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public String getUserLockKey(Long userId) {
        return USER_LOCK_KEY_PREFIX + userId;
    }

    /**
     * 获取登录失败次数缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public String getLoginFailedKey(Long userId) {
        return LOGIN_FAILED_KEY_PREFIX + userId;
    }

    /**
     * 获取缓存过期时间（秒）
     * @return 缓存过期时间
     */
    public int getCacheExpireSeconds() {
        if (userLockProperties.getCacheExpireMinutes() != null) {
            return userLockProperties.getCacheExpireMinutes() * 60;
        }
        return DEFAULT_CACHE_EXPIRE_SECONDS;
    }

    /**
     * 是否启用缓存
     * @return 是否启用缓存
     */
    public boolean isCacheEnabled() {
        return userLockProperties.getCacheEnabled() != null && userLockProperties.getCacheEnabled();
    }
}
