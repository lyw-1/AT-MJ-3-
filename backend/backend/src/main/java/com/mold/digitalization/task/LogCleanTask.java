package com.mold.digitalization.task;

import com.mold.digitalization.service.system.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 鏃ュ織娓呯悊瀹氭椂浠诲姟
 * 瀹氭湡娓呯悊杩囨湡鐨勬搷浣滄棩蹇楋紝闃叉数据搴撹繃搴﹁啫鑳€
 */
@Component
@RequiredArgsConstructor
public class LogCleanTask {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogCleanTask.class);

    private final OperationLogService operationLogService;

    /**
     * 鏃ュ織淇濈暀澶╂暟锛岄粯璁?0澶?
     */
    @Value("${log.retention.days:90}")
    private int logRetentionDays;

    /**
     * 鏄惁鍚敤鏃ュ織娓呯悊功能
     */
    @Value("${log.clean.enabled:true}")
    private boolean logCleanEnabled;

    /**
     * 姣忓ぉ鍑屾櫒2鐐规墽琛屾棩蹇楁竻鐞嗕换鍔?
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanExpiredLogs() {
        if (!logCleanEnabled) {
            log.info("Log clean feature is disabled");
            return;
        }

        try {
            log.info("Starting expired log cleanup, retention days: {}", logRetentionDays);
            
            // 璁＄畻杩囨湡鏃堕棿鐐?
            LocalDateTime expireTime = LocalDateTime.now().minusDays(logRetentionDays);
            
            // 娓呯悊杩囨湡鏃ュ織
            int deletedCount = operationLogService.cleanOldLogs(expireTime);
            
            log.info("Log clean task finished, deleted {} records", deletedCount);
            
        } catch (Exception e) {
            log.error("鏃ュ織娓呯悊浠诲姟执行失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 手动触发日志清理（用于测试和管理员手动清理）
     */
    public void manualCleanExpiredLogs() {
        if (!logCleanEnabled) {
            log.warn("Log clean feature is disabled, cannot run manual clean");
            return;
        }

        try {
            log.info("Manually triggered log clean task, retention days: {}", logRetentionDays);
            
            LocalDateTime expireTime = LocalDateTime.now().minusDays(logRetentionDays);
            int deletedCount = operationLogService.cleanOldLogs(expireTime);
            
            log.info("Manual log clean finished, deleted {} records", deletedCount);
            
        } catch (Exception e) {
            log.error("Manual log clean failed: {}", e.getMessage(), e);
            throw new RuntimeException("Manual log clean failed", e);
        }
    }

    /**
     * 获取褰撳墠配置鐨勬棩蹇椾繚鐣欏ぉ鏁?
     */
    public int getLogRetentionDays() {
        return logRetentionDays;
    }

    /**
     * 妫€鏌ユ棩蹇楁竻鐞嗗姛鑳芥槸鍚﹀惎鐢?
     */
    public boolean isLogCleanEnabled() {
        return logCleanEnabled;
    }
}
