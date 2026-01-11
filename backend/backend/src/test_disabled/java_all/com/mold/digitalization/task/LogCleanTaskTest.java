package com.mold.digitalization.task;

import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * LogCleanTask瀹氭椂浠诲姟绫荤殑鍗曞厓测试
 */
public class LogCleanTaskTest {

    @Mock
    private OperationLogService operationLogService;

    @InjectMocks
    private LogCleanTask logCleanTask;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // 设置榛樿配置鍊? ReflectionTestUtils.setField(logCleanTask, "logRetentionDays",
        // 90);
        ReflectionTestUtils.setField(logCleanTask, "logCleanEnabled", true);
    }

    @Test
    public void testCleanExpiredLogs_WhenEnabled() {
        // 模拟娓呯悊操作返回删除记录鏁?
        // when(operationLogService.cleanOldLogs(any(LocalDateTime.class))).thenReturn(50);

        // 执行瀹氭椂娓呯悊浠诲姟
        logCleanTask.cleanExpiredLogs();

        // 验证璋冪敤浜嗘竻鐞嗘柟娉? verify(operationLogService,
        // times(1)).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testCleanExpiredLogs_WhenDisabled() {
        // 绂佺敤鏃ュ織娓呯悊功能
        ReflectionTestUtils.setField(logCleanTask, "logCleanEnabled", false);

        // 执行瀹氭椂娓呯悊浠诲姟
        logCleanTask.cleanExpiredLogs();

        // 验证娌℃湁璋冪敤娓呯悊方法
        verify(operationLogService, never()).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testManualCleanExpiredLogs_WhenEnabled() {
        // 模拟娓呯悊操作返回删除记录鏁?
        // when(operationLogService.cleanOldLogs(any(LocalDateTime.class))).thenReturn(100);

        // 执行鎵嬪姩娓呯悊浠诲姟
        logCleanTask.manualCleanExpiredLogs();

        // 验证璋冪敤浜嗘竻鐞嗘柟娉? verify(operationLogService,
        // times(1)).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testManualCleanExpiredLogs_WhenDisabled() {
        // 绂佺敤鏃ュ織娓呯悊功能
        ReflectionTestUtils.setField(logCleanTask, "logCleanEnabled", false);

        // 执行鎵嬪姩娓呯悊浠诲姟
        logCleanTask.manualCleanExpiredLogs();

        // 验证娌℃湁璋冪敤娓呯悊方法
        verify(operationLogService, never()).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testManualCleanExpiredLogs_WhenServiceThrowsException() {
        // 模拟服务鎶涘嚭开傚父
        when(operationLogService.cleanOldLogs(any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // 验证运行时抛出异常
        assertThrows(RuntimeException.class, () -> {
            logCleanTask.manualCleanExpiredLogs();
        });

        // 验证调用了清理方法
        verify(operationLogService, times(1)).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testGetLogRetentionDays() {
        // 设置淇濈暀澶╂暟
        ReflectionTestUtils.setField(logCleanTask, "logRetentionDays", 30);

        // 验证获取配置鍊? assertEquals(30, logCleanTask.getLogRetentionDays());
    }

    @Test
    public void testIsLogCleanEnabled() {
        // 设置鍚敤状态 ReflectionTestUtils.setField(logCleanTask, "logCleanEnabled",
        // false);

        // 验证获取鍚敤状态 assertFalse(logCleanTask.isLogCleanEnabled());
    }

    @Test
    public void testCleanExpiredLogs_WithCustomRetentionDays() {
        // 设置鑷畾涔変繚鐣欏ぉ鏁? ReflectionTestUtils.setField(logCleanTask, "logRetentionDays",
        // 7);

        // 模拟娓呯悊操作
        when(operationLogService.cleanOldLogs(any(LocalDateTime.class))).thenReturn(25);

        // 执行瀹氭椂娓呯悊浠诲姟
        logCleanTask.cleanExpiredLogs();

        // 验证璋冪敤浜嗘竻鐞嗘柟娉? verify(operationLogService,
        // times(1)).cleanOldLogs(any(LocalDateTime.class));
    }

    @Test
    public void testCleanExpiredLogs_WithZeroDeletedRecords() {
        // 模拟娓呯悊操作返回0鏉¤褰?
        // when(operationLogService.cleanOldLogs(any(LocalDateTime.class))).thenReturn(0);

        // 执行瀹氭椂娓呯悊浠诲姟
        logCleanTask.cleanExpiredLogs();

        // 验证璋冪敤浜嗘竻鐞嗘柟娉? verify(operationLogService,
        // times(1)).cleanOldLogs(any(LocalDateTime.class));
    }
}
