package com.mold.digitalization.scheduler;

import com.mold.digitalization.config.LogCleanupProperties;
import com.mold.digitalization.service.system.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 日志定时清理任务调度器
 * 根据配置定期清理过期操作日志
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "log.clean.enabled", havingValue = "true")
public class LogCleanupScheduler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogCleanupScheduler.class);
    
    private final OperationLogService operationLogService;
    private final LogCleanupProperties logCleanupProperties;
    
    /**
     * 定时清理过期操作日志
     * 默认每天凌晨2点执行
     */
    @Scheduled(cron = "${log.clean.cron:0 0 2 * * ?}")
    public void cleanupExpiredLogs() {
        try {
            if (!logCleanupProperties.isEnabled()) {
                log.debug("日志清理功能已禁用");
                return;
            }
            
            log.info("开始执行操作日志定时清理任务...");
            
            // 清理普通操作日志（保留指定天数）
            int normalRetentionDays = logCleanupProperties.getRetentionDays();
            LocalDateTime normalExpireTime = LocalDateTime.now().minusDays(normalRetentionDays);
            
            // 清理敏感操作日志（保留更长时间）
            int criticalRetentionDays = logCleanupProperties.getCriticalRetentionDays();
            LocalDateTime criticalExpireTime = LocalDateTime.now().minusDays(criticalRetentionDays);
            
            int deleted = operationLogService.cleanOldLogsByLevel(normalExpireTime, criticalExpireTime);
            log.info("操作日志定时清理完成，删除数量: {}", deleted);
            
            log.info("操作日志定时清理任务执行完成");
            
        } catch (Exception e) {
            log.error("操作日志定时清理任务执行失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 清理普通操作日志
     */
    private void cleanupNormalLogs(LocalDateTime expireTime) {
        try {
            // 这里需要调用Service层的方法进行清理
            // 暂时使用模拟实现
            log.info("清理普通操作日志，过期时间: {}", expireTime);
            
            // TODO: 实现具体的清理逻辑
            // operationLogService.cleanupNormalLogsBefore(expireTime);
            
        } catch (Exception e) {
            log.error("清理普通操作日志失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 清理敏感操作日志
     */
    private void cleanupSensitiveLogs(LocalDateTime expireTime) {
        try {
            log.info("清理敏感操作日志，过期时间: {}", expireTime);
            
            // TODO: 实现具体的清理逻辑
            // operationLogService.cleanupSensitiveLogsBefore(expireTime);
            
        } catch (Exception e) {
            log.error("清理敏感操作日志失败: {}", e.getMessage(), e);
        }
    }
}
