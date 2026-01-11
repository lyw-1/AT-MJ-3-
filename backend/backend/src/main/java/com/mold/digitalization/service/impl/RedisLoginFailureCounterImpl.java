package com.mold.digitalization.service.impl;

import com.mold.digitalization.config.UserLockProperties;
import com.mold.digitalization.service.LoginFailureCounter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis实现鐨勭櫥褰曞け璐ヨ鏁板櫒
 * 浣跨敤Redis瀛樺偍用户鐧诲綍失败娆℃暟锛屽苟设置杩囨湡鏃堕棿
 */
@Service
@ConditionalOnProperty(name = "app.redis.use", havingValue = "true")
public class RedisLoginFailureCounterImpl implements LoginFailureCounter {
    
    private final RedisTemplate<String, Integer> redisTemplate;
    private final UserLockProperties userLockProperties;
    
    /**
     * 鏋勯€犲嚱鏁?
     * @param redisTemplate Redis妯℃澘锛屼娇鐢ˊQualifier鎸囧畾浣跨敤loginFailureCountRedisTemplate
     * @param userLockProperties 用户閿佸畾配置
     */
    public RedisLoginFailureCounterImpl(@Qualifier("loginFailureCountRedisTemplate") RedisTemplate<String, Integer> redisTemplate, 
                                       UserLockProperties userLockProperties) {
        this.redisTemplate = redisTemplate;
        this.userLockProperties = userLockProperties;
    }
    
    /**
     * 获取用户鐧诲綍失败璁℃暟鐨凴edis閿?
     * @param username 用户名?
     * @return Redis閿?
     */
    private String getFailureCountKey(String username) {
        return userLockProperties.getRedisKeyPrefix() + "failure:" + username;
    }
    
    @Override
    public int incrementFailureCount(String username) {
        try {
            String key = getFailureCountKey(username);
            
            // 澧炲姞璁℃暟锛屽鏋滈敭涓嶅瓨鍦ㄥ垯鍒濆鍖栦负1
            int newCount = redisTemplate.opsForValue().increment(key, 1).intValue();
            
            // 濡傛灉鏄涓€娆″鍔狅紝设置杩囨湡鏃堕棿
            if (newCount == 1) {
                redisTemplate.expire(key, userLockProperties.getFailureCountExpireMinutes(), TimeUnit.MINUTES);
            }
            
            return newCount;
        } catch (Exception e) {
            // Redis涓嶅彲鐢ㄦ椂锛岃繑鍥為粯璁ゅ€硷紙琛ㄧず鏈変竴娆″け璐ワ級
            // 实际项目涓簲璇ヨ褰曟棩蹇?
            System.err.println("Redis操作失败: " + e.getMessage());
            return 1;
        }
    }
    
    @Override
    public void resetFailureCount(String username) {
        try {
            redisTemplate.delete(getFailureCountKey(username));
        } catch (Exception e) {
            // Redis涓嶅彲鐢ㄦ椂锛屽拷鐣ラ敊璇?
            // 实际项目涓簲璇ヨ褰曟棩蹇?
            System.err.println("Redis操作失败: " + e.getMessage());
        }
    }
    
    @Override
    public int getFailureCount(String username) {
        try {
            Integer count = redisTemplate.opsForValue().get(getFailureCountKey(username));
            return count != null ? count : 0;
        } catch (Exception e) {
            // Redis涓嶅彲鐢ㄦ椂锛岃繑鍥?
            // 实际项目涓簲璇ヨ褰曟棩蹇?
            System.err.println("Redis操作失败: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean isThresholdReached(String username, int threshold) {
        return getFailureCount(username) >= threshold;
    }
}
