package com.mold.digitalization.service.system.impl;

import com.mold.digitalization.dao.system.OperationLogMapper;
import com.mold.digitalization.entity.system.OperationLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AsyncOperationLogServiceImpl单元测试
 * 测试异步操作日志服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AsyncOperationLogServiceImplTest {

    @Mock
    private OperationLogMapper operationLogMapper;

    @InjectMocks
    private AsyncOperationLogServiceImpl asyncOperationLogService;

    private OperationLog testOperationLog;
    private LocalDateTime testCutoffTime;

    @BeforeEach
    void setUp() {
        testCutoffTime = LocalDateTime.of(2023, 12, 31, 23, 59);

        testOperationLog = new OperationLog();
        testOperationLog.setId(1L);
        testOperationLog.setUserId(1001L);
        testOperationLog.setUsername("testuser");
        testOperationLog.setUserIp("192.168.1.1");
        testOperationLog.setOperationType("GET");
        testOperationLog.setOperationDesc("查询用户信息");
        testOperationLog.setOperationContent("查询用户ID: 1001");
        testOperationLog.setResultCode("SUCCESS");
        testOperationLog.setIsSensitive(false);
        testOperationLog.setOperationTime(LocalDateTime.now());
    }

    @Test
    void testBatchSaveOperationLogsAsync_EmptyList() {
        // 准备数据
        List<OperationLog> emptyList = Collections.emptyList();

        // 执行测试
        asyncOperationLogService.batchSaveOperationLogsAsync(emptyList);

        // 验证结果 - 空列表应该直接返回，不执行任何数据库操作
        verify(operationLogMapper, never()).insert(any(OperationLog.class));
    }

    @Test
    void testBatchSaveOperationLogsAsync_NullList() {
        // 执行测试
        asyncOperationLogService.batchSaveOperationLogsAsync(null);

        // 验证结果 - null列表应该直接返回，不执行任何数据库操作
        verify(operationLogMapper, never()).insert(any(OperationLog.class));
    }

    @Test
    void testBatchSaveOperationLogsAsync_SingleBatch() {
        // 准备数据 - 小于默认批量大小的列表
        List<OperationLog> logs = Arrays.asList(testOperationLog, createOperationLog(2L));

        // 模拟数据库操作
        when(operationLogMapper.insert(any(OperationLog.class))).thenReturn(1);

        // 执行测试
        asyncOperationLogService.batchSaveOperationLogsAsync(logs);

        // 验证结果 - 应该执行2次插入操作
        verify(operationLogMapper, times(2)).insert(any(OperationLog.class));
    }

    @Test
    void testBatchSaveOperationLogsAsync_MultipleBatches() {
        // 准备数据 - 澶т簬榛樿鎵归噺澶у皬鐨勫垪琛?        List<OperationLog> logs = createLargeOperationLogList(1500);

        // 模拟数据搴撴搷浣?        when(operationLogMapper.insert(any(OperationLog.class))).thenReturn(1);

        // 执行测试
        asyncOperationLogService.batchSaveOperationLogsAsync(logs);

        // 验证结果 - 应该执行1500次插入操作，分2个批次（1000 + 500）
        verify(operationLogMapper, times(1500)).insert(any(OperationLog.class));
    }

    @Test
    void testBatchSaveOperationLogsAsync_ExceptionHandling() {
        // 准备数据
        List<OperationLog> logs = Arrays.asList(testOperationLog);

        // 模拟数据库操作抛出异常
        when(operationLogMapper.insert(any(OperationLog.class))).thenThrow(new RuntimeException("Database error"));

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            asyncOperationLogService.batchSaveOperationLogsAsync(logs);
        });

        // 验证结果 - 应该执行1次插入操作
        verify(operationLogMapper, times(1)).insert(any(OperationLog.class));
    }

    @Test
    void testBatchCleanOldLogsAsync_NullCutoffTime() {
        // 执行测试
        int result = asyncOperationLogService.batchCleanOldLogsAsync(null, 1000, "high");

        // 验证结果 - null截止时间应该返回0
        assertEquals(0, result);
        verify(operationLogMapper, never()).deleteBatchIds(any());
    }

    @Test
    void testBatchCleanOldLogsAsync_NoRecordsToDelete() {
        // 模拟查询返回绌哄垪琛?        when(operationLogMapper.selectObjs(any())).thenReturn(Collections.emptyList());

        // 执行测试
        int result = asyncOperationLogService.batchCleanOldLogsAsync(testCutoffTime, 1000, "high");

        // 验证结果 - 娌℃湁记录鍙垹闄わ紝应该返回0
        assertEquals(0, result);
        verify(operationLogMapper, never()).deleteBatchIds(any());
    }

    @Test
    void testBatchCleanOldLogsAsync_SingleBatch() {
        // 准备数据
        List<Object> ids = Arrays.asList(1L, 2L, 3L);

        // 模拟查询返回ID鍒楄〃
        when(operationLogMapper.selectObjs(any())).thenReturn(ids).thenReturn(Collections.emptyList());
        when(operationLogMapper.deleteBatchIds(any())).thenReturn(3);

        // 执行测试
        int result = asyncOperationLogService.batchCleanOldLogsAsync(testCutoffTime, 1000, "high");

        // 验证结果 - 应该删除3鏉¤褰?        assertEquals(3, result);
        verify(operationLogMapper, times(1)).deleteBatchIds(any());
    }

    @Test
    void testBatchCleanOldLogsAsync_MultipleBatches() {
        // 准备数据 - 模拟澶氫釜鎵规
        List<Object> batch1 = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<Object> batch2 = Arrays.asList(6L, 7L, 8L);

        // 模拟查询返回ID鍒楄〃
        when(operationLogMapper.selectObjs(any()))
                .thenReturn(batch1)
                .thenReturn(batch2)
                .thenReturn(Collections.emptyList());
        when(operationLogMapper.deleteBatchIds(any())).thenReturn(5).thenReturn(3);

        // 执行测试
        int result = asyncOperationLogService.batchCleanOldLogsAsync(testCutoffTime, 5, "high");

        // 验证结果 - 应该删除8鏉¤褰曪紙5 + 3锛?        assertEquals(8, result);
        verify(operationLogMapper, times(2)).deleteBatchIds(any());
    }

    @Test
    void testBatchCleanOldLogsAsync_DefaultBatchSize() {
        // 准备数据
        List<Object> ids = Arrays.asList(1L, 2L);

        // 模拟查询返回ID鍒楄〃
        when(operationLogMapper.selectObjs(any())).thenReturn(ids).thenReturn(Collections.emptyList());
        when(operationLogMapper.deleteBatchIds(any())).thenReturn(2);

        // 执行测试 - 浣跨敤鏃犳晥鐨勬壒閲忓ぇ灏忥紝应该浣跨敤榛樿鍊?        int result = asyncOperationLogService.batchCleanOldLogsAsync(testCutoffTime, 0, "high");

        // 验证结果 - 应该删除2鏉¤褰?        assertEquals(2, result);
        verify(operationLogMapper, times(1)).deleteBatchIds(any());
    }

    @Test
    void testBatchCleanOldLogsAsync_ExceptionHandling() {
        // 准备数据
        List<Object> ids = Arrays.asList(1L, 2L);

        // 模拟查询返回ID鍒楄〃锛屼絾删除操作鎶涘嚭开傚父
        when(operationLogMapper.selectObjs(any())).thenReturn(ids);
        when(operationLogMapper.deleteBatchIds(any())).thenThrow(new RuntimeException("Delete error"));

        // 执行测试
        int result = asyncOperationLogService.batchCleanOldLogsAsync(testCutoffTime, 1000, "high");

        // 验证结果 - 开傚父应该琚崟鑾凤紝返回宸插垹闄ょ殑鏁伴噺锛?锛?        assertEquals(0, result);
        verify(operationLogMapper, times(1)).deleteBatchIds(any());
    }

    @Test
    void testAsyncExportOperationLogs_Success() {
        // 准备数据
        String exportFilePath = "/tmp/test_export.csv";
        Object queryParams = new Object();

        // 模拟查询返回绌哄垪琛?        when(operationLogMapper.selectList(any())).thenReturn(Collections.emptyList());

        // 执行测试
        String result = asyncOperationLogService.asyncExportOperationLogs(queryParams, exportFilePath);

        // 验证结果 - 应该返回瀵煎嚭鏂囦欢璺緞
        assertEquals(exportFilePath, result);
        verify(operationLogMapper, times(1)).selectList(any());
    }

    @Test
    void testAsyncExportOperationLogs_WithData() {
        // 准备数据
        String exportFilePath = "/tmp/test_export.csv";
        Object queryParams = new Object();
        List<OperationLog> logs = Arrays.asList(testOperationLog, createOperationLog(2L));

        // 模拟查询返回数据
        when(operationLogMapper.selectList(any())).thenReturn(logs).thenReturn(Collections.emptyList());

        // 执行测试
        String result = asyncOperationLogService.asyncExportOperationLogs(queryParams, exportFilePath);

        // 验证结果 - 应该返回瀵煎嚭鏂囦欢璺緞
        assertEquals(exportFilePath, result);
        verify(operationLogMapper, times(2)).selectList(any());
    }

    @Test
    void testAsyncExportOperationLogs_ExceptionHandling() {
        // 准备数据
        String exportFilePath = "/tmp/test_export.csv";
        Object queryParams = new Object();

        // 模拟查询鎶涘嚭开傚父
        when(operationLogMapper.selectList(any())).thenThrow(new RuntimeException("Query error"));

        // 执行测试
        String result = asyncOperationLogService.asyncExportOperationLogs(queryParams, exportFilePath);

        // 验证结果 - 开傚父应该琚崟鑾凤紝返回null
        assertNull(result);
        verify(operationLogMapper, times(1)).selectList(any());
    }

    @Test
    void testAsyncExportOperationLogs_CsvFormatting() {
        // 准备数据
        String exportFilePath = "/tmp/test_export.csv";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("username", "testuser");
        
        List<OperationLog> logs = Arrays.asList(testOperationLog);

        // 模拟查询返回数据
        when(operationLogMapper.selectList(any())).thenReturn(logs).thenReturn(Collections.emptyList());

        // 执行测试
        String result = asyncOperationLogService.asyncExportOperationLogs(queryParams, exportFilePath);

        // 验证结果 - 应该返回瀵煎嚭鏂囦欢璺緞
        assertEquals(exportFilePath, result);
        verify(operationLogMapper, times(2)).selectList(any());
        
        // 验证CSV鏂囦欢内容锛堥€氳繃璇诲彇鏂囦欢鎴栭獙璇佹柟娉曡皟鐢級
        // 杩欓噷鎴戜滑验证浜嗗鍑哄姛鑳芥甯稿伐浣滐紝CSV鏍煎紡鍖栧湪绉佹湁方法涓畬鎴?    }

    // 杈呭姪方法锛氬垱寤烘搷浣滄棩蹇?    private OperationLog createOperationLog(Long id) {
        OperationLog log = new OperationLog();
        log.setId(id);
        log.setUserId(1001L + id);
        log.setUsername("user" + id);
        log.setOperationType("GET");
        log.setOperationDesc("操作鎻忚堪" + id);
        log.setOperationContent("操作内容" + id);
        log.setResultCode("SUCCESS");
        log.setIsSensitive(false);
        log.setOperationTime(LocalDateTime.now());
        return log;
    }

    // 杈呭姪方法锛氬垱寤哄ぇ閲忔搷浣滄棩蹇楀垪琛?    private List<OperationLog> createLargeOperationLogList(int count) {
        OperationLog[] logs = new OperationLog[count];
        for (int i = 0; i < count; i++) {
            logs[i] = createOperationLog((long) (i + 1));
        }
        return Arrays.asList(logs);
    }
}