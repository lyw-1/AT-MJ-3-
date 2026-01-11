package com.mold.digitalization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 密码重置缓存配置
 * 管理密码重置相关的缓存参数和常量
 */
@Configuration
public class PasswordResetCacheConfig {
    
    // 密码重置验证码缓存键前开
    public static final String RESET_CODE_KEY_PREFIX = "reset_code:";
    
    // 密码重置验证码过期时间（默认15分钟，单位：秒）
    @Value("${password.reset.code.expire-time:900}")
    private long resetCodeExpireTime;
    
    // 同一用户一天内最大验证码发送次数（默认5次）
    @Value("${password.reset.code.max-send-times:5}")
    private int maxSendTimesPerDay;
    
    // 发送间隔限制（默认60秒）
    @Value("${password.reset.code.min-interval:60}")
    private int minSendInterval;
    
    /**
     * 获取重置验证码缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public String getResetCodeKey(Long userId) {
        return RESET_CODE_KEY_PREFIX + userId;
    }
    
    /**
     * 获取验证码发送计数缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public String getSendCountKey(Long userId) {
        return "reset_code_count:" + userId;
    }
    
    /**
     * 获取上次发送时间缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public String getLastSendTimeKey(Long userId) {
        return "reset_code_last_time:" + userId;
    }
    
    /**
     * 获取重置验证码过期时间
     * @return 过期时间（秒）
     */
    public long getResetCodeExpireTime() {
        return resetCodeExpireTime;
    }
    
    /**
     * 获取一天内最大发送次数
     * @return 最大发送次数
     */
    public int getMaxSendTimesPerDay() {
        return maxSendTimesPerDay;
    }
    
    /**
     * 获取最小发送间隔（秒）
     * @return 最小发送间隔
     */
    public int getMinSendInterval() {
        return minSendInterval;
    }
}
