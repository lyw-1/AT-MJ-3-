package com.mold.digitalization.scheduler;

import com.mold.digitalization.config.LogCleanConfig;
import com.mold.digitalization.service.system.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 鏃ュ織娓呯悊瀹氭椂浠诲姟
 */
@Component
@RequiredArgsConstructor
public class LogCleanScheduler {
    
    private static final Logger log = LoggerFactory.getLogger(LogCleanScheduler.class);
    
    private final OperationLogService operationLogService;
    private final LogCleanConfig logCleanConfig;
    
    /**
     * 瀹氭椂娓呯悊杩囨湡鏃ュ織
     * 浣跨敤配置鐨刢ron琛ㄨ揪开忔墽琛?
     */
    @Scheduled(cron = "${log.clean.cron:0 0 2 * * ?}")
    public void cleanExpiredLogs() {
        if (!logCleanConfig.isEnabled()) {
            log.info("Log cleaning is disabled in configuration");
            return;
        }
        
        try {
            log.info("Start scheduled log cleaning task");
            
            // 璁＄畻鏅€氭棩蹇楃殑杩囨湡鏃堕棿
            LocalDateTime normalExpireTime = LocalDateTime.now().minus(logCleanConfig.getRetentionDays(), ChronoUnit.DAYS);
            // 璁＄畻鍏抽敭鏃ュ織鐨勮繃鏈熸椂闂?
            LocalDateTime criticalExpireTime = LocalDateTime.now().minus(logCleanConfig.getCriticalRetentionDays(), ChronoUnit.DAYS);
            log.info("Retention days: normal={} days, critical={} days", logCleanConfig.getRetentionDays(), logCleanConfig.getCriticalRetentionDays());
            log.info("Expire thresholds: normal={}, critical={}", normalExpireTime, criticalExpireTime);
            
            // Execute cleaning: use different levels to clean logs
            int deletedCount = operationLogService.cleanOldLogsByLevel(normalExpireTime, criticalExpireTime);
            log.info("Log cleaning finished, total deleted={}", deletedCount);
            
        } catch (Exception e) {
            log.error("Scheduled log cleaning task failed: {}", e.getMessage(), e);
        }
    }
}
