package com.mold.digitalization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户閿佸畾配置灞炴€х被
 * 鐢ㄤ簬绠＄悊用户閿佸畾鏈哄埗鐨勭浉鍏抽厤缃弬鏁? */
@Data
@Component
@ConfigurationProperties(prefix = "user.lock")
public class UserLockProperties {

    /**
     * 鏈€澶х櫥褰曞け璐ユ鏁?     * 褰撶敤鎴风櫥褰曞け璐ヨ揪鍒版娆℃暟鏃讹紝璐﹀彿灏嗚閿佸畾
     */
    private Integer maxFailedAttempts = 5;
    
    /**
     * 閿佸畾鏃堕暱锛堝垎閽燂級
     * 用户璐﹀彿琚攣瀹氱殑鎸佺画鏃堕棿
     */
    private Integer durationMinutes = 30;
    
    /**
     * 鐧诲綍失败璁℃暟鏈夋晥鏈燂紙鍒嗛挓锛?     * 瓒呰繃姝ゆ椂闂存湭鐧诲綍成功鍒欓噸缃鏁?     */
    private Integer failureCountExpireMinutes = 10;
    
    /**
     * 鑷姩瑙ｉ攣瀹氭椂浠诲姟执行闂撮殧锛堝垎閽燂級
     */
    private Integer unlockTaskIntervalMinutes = 5;
    
    /**
     * Redis閿墠缂€
     */
    private String redisKeyPrefix = "mold:user:lock:";

    /**
     * 鏄惁鍚敤缂撳瓨
     * 控制鏄惁浣跨敤Redis缂撳瓨用户閿佸畾淇℃伅
     */
    private Boolean cacheEnabled = true;

    /**
     * 缂撳瓨杩囨湡鏃堕棿锛堝垎閽燂級
     * Redis涓敤鎴烽攣瀹氫俊鎭殑缂撳瓨杩囨湡鏃堕棿
     */
    private Integer cacheExpireMinutes = 60;
    
    // 鍏煎鎬etter方法
    public Integer getMaxLoginFailures() {
        return maxFailedAttempts;
    }
    public Integer getMaxFailedAttempts() { return maxFailedAttempts; }
    
    public Integer getLockDurationMinutes() {
        return durationMinutes;
    }
    public Integer getDurationMinutes() { return durationMinutes; }
    
    public String getRedisKeyPrefix() {
        return redisKeyPrefix;
    }
    public Integer getFailureCountExpireMinutes() { return failureCountExpireMinutes; }
    public Boolean getCacheEnabled() { return cacheEnabled; }
    public Integer getCacheExpireMinutes() { return cacheExpireMinutes; }
}
