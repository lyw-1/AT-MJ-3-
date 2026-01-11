package com.mold.digitalization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志清理配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "log.clean")
public class LogCleanupProperties {
    
    /**
     * 是否启用日志清理功能
     */
    private boolean enabled = true;
    
    /**
     * 普通日志保留天数
     */
    private int retentionDays = 90;
    
    /**
     * 敏感日志保留天数
     */
    private int criticalRetentionDays = 365;
    
    /**
     * 清理任务执行时间表达式
     */
    private String cron = "0 0 2 * * ?";

    public boolean isEnabled() { return enabled; }
    public int getRetentionDays() { return retentionDays; }
    public int getCriticalRetentionDays() { return criticalRetentionDays; }
}
