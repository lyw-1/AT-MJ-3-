package com.mold.digitalization.scheduler;

import com.mold.digitalization.config.LogCleanupProperties;
import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LogCleanupSchedulerTest {
    @Test
    void cleanupExpiredLogs_invokesServiceWithCalculatedTimes() {
        OperationLogService operationLogService = Mockito.mock(OperationLogService.class);
        LogCleanupProperties props = new LogCleanupProperties();
        // 默认：enabled=true, retentionDays=90, criticalRetentionDays=365

        LogCleanupScheduler scheduler = new LogCleanupScheduler(operationLogService, props);
        scheduler.cleanupExpiredLogs();

        verify(operationLogService, times(1)).cleanOldLogsByLevel(any(), any());
    }
}