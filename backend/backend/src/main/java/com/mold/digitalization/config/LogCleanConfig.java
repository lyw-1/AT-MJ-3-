package com.mold.digitalization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 鏃ュ織娓呯悊绛栫暐配置绫? */
@Configuration
@ConfigurationProperties(prefix = "log.clean")
@Data
public class LogCleanConfig {
    
    /**
     * 鏄惁鍚敤鏃ュ織娓呯悊
     */
    private boolean enabled = true;
    
    /**
     * 鏃ュ織淇濈暀澶╂暟
     */
    private int retentionDays = 90;
    
    /**
     * 鍏抽敭鏃ュ織淇濈暀澶╂暟
     */
    private int criticalRetentionDays = 365;
    
    /**
     * 瀹氭椂娓呯悊浠诲姟cron琛ㄨ揪开?     */
    private String cron = "0 0 2 * * ?";
    
    // 鎵嬪姩添加getter方法
    public boolean isEnabled() {
        return enabled;
    }
    
    public int getRetentionDays() {
        return retentionDays;
    }
    
    public int getCriticalRetentionDays() {
        return criticalRetentionDays;
    }
    
    public String getCron() {
        return cron;
    }
}
