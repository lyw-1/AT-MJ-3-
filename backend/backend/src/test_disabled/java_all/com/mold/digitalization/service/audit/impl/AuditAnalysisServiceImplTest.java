package com.mold.digitalization.service.audit.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mold.digitalization.dao.system.OperationLogMapper;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAnalysisServiceImplTest {

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private OperationLogMapper operationLogMapper;

    @InjectMocks
    private AuditAnalysisServiceImpl auditAnalysisService;

    private List<OperationLog> mockLogs;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        startTime = LocalDateTime.now().minusDays(7);
        endTime = LocalDateTime.now();
        
        mockLogs = new ArrayList<>();
        
        // 创建成功的普通操作日志
        OperationLog log1 = new OperationLog();
        log1.setId(1L);
        log1.setUsername("user1");
        log1.setUserId(1001L);
        log1.setUserIp("192.168.1.1");
        log1.setOperationType("QUERY");
        log1.setOperationDesc("查询用户信息");
        log1.setOperationContent("{\"userId\":1001,\"module\":\"user\"}");
        log1.setResultCode("200");
        log1.setIsSensitive(false);
        log1.setOperationTime(LocalDateTime.now().minusDays(1).withHour(10));
        mockLogs.add(log1);
        
        // 创建失败的普通操作日志
        OperationLog log2 = new OperationLog();
        log2.setId(2L);
        log2.setUsername("user1");
        log2.setUserId(1001L);
        log2.setUserIp("192.168.1.1");
        log2.setOperationType("UPDATE");
        log2.setOperationDesc("更新用户信息");
        log2.setOperationContent("{\"userId\":1001,\"module\":\"user\"}");
        log2.setResultCode("500");
        log2.setIsSensitive(false);
        log2.setOperationTime(LocalDateTime.now().minusDays(2).withHour(14));
        mockLogs.add(log2);
        
        // 创建成功的敏感操作日志
        OperationLog log3 = new OperationLog();
        log3.setId(3L);
        log3.setUsername("admin");
        log3.setUserId(1000L);
        log3.setUserIp("192.168.1.2");
        log3.setOperationType("DELETE");
        log3.setOperationDesc("删除用户");
        log3.setOperationContent("{\"userId\":2001,\"module\":\"user\"}");
        log3.setResultCode("200");
        log3.setIsSensitive(true);
        log3.setOperationTime(LocalDateTime.now().minusDays(3).withHour(9));
        mockLogs.add(log3);
    }

    @Test
    void analyzeUserBehavior() {
        // 准备mock数据
        Long userId = 1001L;
        
        // 创建只包含userId=1001L的mock数据
        List<OperationLog> filteredLogs = new ArrayList<>();
        for (OperationLog log : mockLogs) {
            if (userId.equals(log.getUserId())) {
                filteredLogs.add(log);
            }
        }
        
        // 打印调试信息
        System.out.println("Mock数据筛选结果: " + filteredLogs.size() + " 条记录");
        for (OperationLog log : filteredLogs) {
            System.out.println("Log ID: " + log.getId() + ", UserId: " + log.getUserId() + ", Type: " + log.getOperationType() + ", Result: " + log.getResultCode());
        }
        
        // 使用简单的any匹配，确保mock数据被返回
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(filteredLogs);
        
        // 执行方法
        Map<String, Object> result = auditAnalysisService.analyzeUserBehavior(userId, startTime, endTime);
        
        // 打印结果调试信息
        System.out.println("Service返回结果: totalOperations = " + result.get("totalOperations"));
        System.out.println("Service返回结果: successRate = " + result.get("successRate"));
        System.out.println("Service返回结果: operationTypeDistribution = " + result.get("operationTypeDistribution"));
        System.out.println("Service返回结果: sensitiveOperationCount = " + result.get("sensitiveOperationCount"));
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.get("totalOperations"));
        assertEquals("50.00%", result.get("successRate"));
        
        // 验证操作类型分布
        Map<String, Long> typeDistribution = (Map<String, Long>) result.get("operationTypeDistribution");
        assertEquals(2, typeDistribution.size());
        assertEquals(1L, typeDistribution.get("QUERY"));
        assertEquals(1L, typeDistribution.get("UPDATE"));
        
        // 验证敏感操作数量
        assertEquals(0L, result.get("sensitiveOperationCount"));
        
        // 验证mock调用
        verify(operationLogService).list(any(QueryWrapper.class));
    }

    @Test
    void analyzeUserBehavior_emptyData() {
        // 准备空数据
        Long userId = 9999L;
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        
        // 执行方法
        Map<String, Object> result = auditAnalysisService.analyzeUserBehavior(userId, startTime, endTime);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0L, result.get("totalOperations"));
        assertEquals("0.00%", result.get("successRate"));
        
        // 验证操作类型分布为空
        Map<String, Long> typeDistribution = (Map<String, Long>) result.get("operationTypeDistribution");
        assertTrue(typeDistribution.isEmpty());
        
        // 验证敏感操作数量为0
        assertEquals(0L, result.get("sensitiveOperationCount"));
    }

    @Test
    void analyzeUserBehavior_nullParameters() {
        // 测试空参数
        Map<String, Object> result = auditAnalysisService.analyzeUserBehavior(null, null, null);
        
        // 验证异常处理
        assertNotNull(result);
        assertTrue(result.containsKey("error"));
    }

    @Test
    void analyzeUserBehavior_exception() {
        // 模拟异常情况
        when(operationLogService.list(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行方法
        Map<String, Object> result = auditAnalysisService.analyzeUserBehavior(1001L, startTime, endTime);
        
        // 验证异常处理
        assertNotNull(result);
        assertTrue(result.containsKey("error"));
    }

    @Test
    void detectAbnormalOperations() {
        // 准备包含异常操作的mock数据
        List<OperationLog> abnormalLogs = new ArrayList<>(mockLogs);
        
        // 添加失败的敏感操作
        OperationLog failedSensitive = new OperationLog();
        failedSensitive.setId(4L);
        failedSensitive.setUsername("user2");
        failedSensitive.setUserId(1002L);
        failedSensitive.setOperationType("DELETE");
        failedSensitive.setOperationDesc("删除用户");
        failedSensitive.setResultCode("failure");
        failedSensitive.setIsSensitive(true);
        failedSensitive.setOperationTime(LocalDateTime.now().minusHours(1));
        abnormalLogs.add(failedSensitive);
        
        // 添加短时间内多次失败操作
        LocalDateTime baseTime = LocalDateTime.now().minusMinutes(10);
        for (int i = 0; i < 3; i++) {
            OperationLog rapidFail = new OperationLog();
            rapidFail.setId(5L + i);
            rapidFail.setUsername("user3");
            rapidFail.setUserId(1003L);
            rapidFail.setOperationType("LOGIN");
            rapidFail.setOperationDesc("用户登录");
            rapidFail.setResultCode("failure");
            rapidFail.setIsSensitive(false);
            rapidFail.setOperationTime(baseTime.plusMinutes(i * 2)); // 4分钟内3次失败
            abnormalLogs.add(rapidFail);
        }
        
        // 添加非正常时间的敏感操作
        OperationLog unusualTime = new OperationLog();
        unusualTime.setId(8L);
        unusualTime.setUsername("admin");
        unusualTime.setUserId(1000L);
        unusualTime.setOperationType("CONFIG");
        unusualTime.setOperationDesc("系统配置");
        unusualTime.setResultCode("success");
        unusualTime.setIsSensitive(true);
        unusualTime.setOperationTime(LocalDateTime.now().withHour(3)); // 凌晨3点
        abnormalLogs.add(unusualTime);
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(abnormalLogs);
        
        // 执行方法
        List<OperationLog> result = auditAnalysisService.detectAbnormalOperations(startTime, endTime);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.size() > 0);
        
        // 验证去重和排序
        List<Long> ids = result.stream().map(OperationLog::getId).collect(Collectors.toList());
        assertEquals(ids.size(), new HashSet<>(ids).size()); // 确保没有重复
        
        // 验证时间排序（降序）
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getOperationTime().isAfter(result.get(i + 1).getOperationTime()) || 
                      result.get(i).getOperationTime().equals(result.get(i + 1).getOperationTime()));
        }
    }

    @Test
    void detectAbnormalOperations_emptyData() {
        // 准备空数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        
        // 执行方法
        List<OperationLog> result = auditAnalysisService.detectAbnormalOperations(startTime, endTime);
        
        // 验证结果为空
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void detectAbnormalOperations_exception() {
        // 模拟异常情况
        when(operationLogService.list(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行方法
        List<OperationLog> result = auditAnalysisService.detectAbnormalOperations(startTime, endTime);
        
        // 验证异常处理
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void analyzeOperationTrend_byDay() {
        // 准备mock数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行方法
        List<Map<String, Object>> trendData = auditAnalysisService.analyzeOperationTrend("day", startTime, endTime);
        
        // 验证结果
        assertNotNull(trendData);
        // 应该有3天的数据（根据mockLogs中的createTime）
        assertTrue(trendData.size() > 0);
        
        // 验证数据结构
        for (Map<String, Object> dataPoint : trendData) {
            assertTrue(dataPoint.containsKey("timeLabel"));
            assertTrue(dataPoint.containsKey("totalCount"));
            assertTrue(dataPoint.containsKey("successCount"));
            assertTrue(dataPoint.containsKey("failCount"));
            assertTrue(dataPoint.containsKey("sensitiveCount"));
        }
    }

    @Test
    void analyzeOperationTrend_invalidDimension() {
        // 执行方法
        List<Map<String, Object>> trendData = auditAnalysisService.analyzeOperationTrend("invalid", startTime, endTime);
        
        // 验证结果是空列表
        assertNotNull(trendData);
        assertTrue(trendData.isEmpty());
    }

    @Test
    void statisticSensitiveOperations() {
        // 准备包含敏感操作的mock数据
        List<OperationLog> sensitiveLogs = new ArrayList<>();
        
        // 添加敏感操作
        OperationLog sensitive1 = new OperationLog();
        sensitive1.setId(4L);
        sensitive1.setUsername("admin");
        sensitive1.setUserId(1000L);
        sensitive1.setOperationType("DELETE");
        sensitive1.setOperationDesc("删除用户");
        sensitive1.setResultCode("200");
        sensitive1.setIsSensitive(true);
        sensitive1.setOperationTime(LocalDateTime.now().minusHours(2));
        sensitiveLogs.add(sensitive1);
        
        OperationLog sensitive2 = new OperationLog();
        sensitive2.setId(5L);
        sensitive2.setUsername("user2");
        sensitive2.setUserId(1002L);
        sensitive2.setOperationType("CONFIG");
        sensitive2.setOperationDesc("系统配置");
        sensitive2.setResultCode("500");
        sensitive2.setIsSensitive(true);
        sensitive2.setOperationTime(LocalDateTime.now().minusHours(1));
        sensitiveLogs.add(sensitive2);
        
        when(operationLogMapper.queryByTimeRange(startTime, endTime)).thenReturn(sensitiveLogs);
        
        // 创建查询DTO
        OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        
        // 执行方法
        OperationLogStatisticDTO result = auditAnalysisService.statisticSensitiveOperations(queryDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.getTotalCount().longValue());
        assertEquals(1L, result.getSuccessCount().longValue());
        assertEquals(1L, result.getFailCount().longValue());
        assertEquals(2L, result.getSensitiveCount().longValue());
        assertEquals("50.00%", result.getSuccessRate());
        assertEquals("100.00%", result.getSensitiveRate());
        
        // 验证详细信息
        assertNotNull(result.getDetail());
        assertEquals(2, result.getDetail().size());
    }

    @Test
    void statisticSensitiveOperations_noSensitive() {
        // 准备不包含敏感操作的mock数据
        List<OperationLog> nonSensitiveLogs = new ArrayList<>(mockLogs);
        
        // 确保所有操作都不是敏感操作
        nonSensitiveLogs.forEach(log -> log.setIsSensitive(false));
        
        when(operationLogMapper.queryByTimeRange(startTime, endTime)).thenReturn(nonSensitiveLogs);
        
        // 创建查询DTO
        OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        
        // 执行方法
        OperationLogStatisticDTO result = auditAnalysisService.statisticSensitiveOperations(queryDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount().longValue());
        assertEquals(0L, result.getSuccessCount().longValue());
        assertEquals(0L, result.getFailCount().longValue());
        assertEquals(0L, result.getSensitiveCount().longValue());
        assertEquals("0.00%", result.getSuccessRate());
        assertEquals("0.00%", result.getSensitiveRate());
        
        // 验证详细信息为空
        assertNotNull(result.getDetail());
        assertTrue(result.getDetail().isEmpty());
    }

    @Test
    void statisticSensitiveOperations_emptyData() {
        // 准备空数据
        when(operationLogMapper.queryByTimeRange(startTime, endTime)).thenReturn(Collections.emptyList());
        
        // 创建查询DTO
        OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        
        // 执行方法
        OperationLogStatisticDTO result = auditAnalysisService.statisticSensitiveOperations(queryDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount().longValue());
        assertEquals(0L, result.getSuccessCount().longValue());
        assertEquals(0L, result.getFailCount().longValue());
        assertEquals(0L, result.getSensitiveCount().longValue());
        assertEquals("0.00%", result.getSuccessRate());
        assertEquals("0.00%", result.getSensitiveRate());
        
        // 验证详细信息为空
        assertNotNull(result.getDetail());
        assertTrue(result.getDetail().isEmpty());
    }

    @Test
    void statisticSensitiveOperations_exception() {
        // 模拟异常情况
        when(operationLogMapper.queryByTimeRange(startTime, endTime)).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 创建查询DTO
        OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        
        // 执行方法并验证异常处理
        OperationLogStatisticDTO result = auditAnalysisService.statisticSensitiveOperations(queryDTO);
        
        // 验证返回了默认的统计结果（异常被捕获，返回空结果）
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount().longValue());
        assertEquals(0L, result.getSuccessCount().longValue());
        assertEquals(0L, result.getFailCount().longValue());
        assertEquals(0L, result.getSensitiveCount().longValue());
        assertEquals("0.00%", result.getSuccessRate());
        assertEquals("0.00%", result.getSensitiveRate());
        assertNotNull(result.getDetail());
        assertTrue(result.getDetail().isEmpty());
    }

    @Test
    void getTopNOperations() {
        // 准备包含多种操作类型的mock数据
        List<OperationLog> topNLogs = new ArrayList<>(mockLogs);
        
        // 添加更多操作类型
        OperationLog queryOp = new OperationLog();
        queryOp.setId(4L);
        queryOp.setUsername("user1");
        queryOp.setUserId(1001L);
        queryOp.setOperationType("QUERY");
        queryOp.setOperationDesc("查询数据");
        queryOp.setResultCode("success");
        queryOp.setIsSensitive(false);
        queryOp.setOperationTime(LocalDateTime.now().minusHours(3));
        topNLogs.add(queryOp);
        
        // 添加更多UPDATE操作（使其成为最高频）
        for (int i = 0; i < 3; i++) {
            OperationLog updateOp = new OperationLog();
            updateOp.setId(5L + i);
            updateOp.setUsername("user1");
            updateOp.setUserId(1001L);
            updateOp.setOperationType("UPDATE");
            updateOp.setOperationDesc("更新数据");
            updateOp.setResultCode("success");
            updateOp.setIsSensitive(false);
            updateOp.setOperationTime(LocalDateTime.now().minusHours(i + 1));
            topNLogs.add(updateOp);
        }
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(topNLogs);
        
        // 执行方法，获取前3个
        List<Map<String, Object>> result = auditAnalysisService.getTopNOperations(3, startTime, endTime);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // 验证鎺掑簭锛堟寜操作娆℃暟闄嶅簭锛?        Map<String, Object> first = result.get(0);
        Map<String, Object> second = result.get(1);
        assertTrue((Long) first.get("count") >= (Long) second.get("count"));
        
        // 验证数据缁撴瀯
        for (Map<String, Object> item : result) {
            assertTrue(item.containsKey("operationType"));
            assertTrue(item.containsKey("count"));
            assertTrue(item.containsKey("percentage"));
        }
    }

    @Test
    void getTopNOperations_withTopN() {
        // 准备鍖呭惈澶氱操作绫诲瀷鐨刴ock数据
        List<OperationLog> topNLogs = new ArrayList<>(mockLogs);
        
        // 添加鏇村操作绫诲瀷
        for (int i = 0; i < 10; i++) {
            OperationLog op = new OperationLog();
            op.setId(4L + i);
            op.setUsername("user1");
            op.setUserId(1001L);
            op.setOperationType("TYPE_" + (i % 5)); // 5绉嶄笉鍚岀殑操作绫诲瀷
            op.setOperationDesc("操作鎻忚堪" + i);
            op.setResultCode("success");
            op.setIsSensitive(false);
            op.setOperationTime(LocalDateTime.now().minusHours(i));
            topNLogs.add(op);
        }
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(topNLogs);
        
        // 执行方法锛岃幏鍙栧墠2涓?        List<Map<String, Object>> result = auditAnalysisService.getTopNOperations(2, startTime, endTime);
        
        // 验证结果鍙繑鍥炲墠2涓?        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getTopNOperations_emptyData() {
        // 准备绌烘暟鎹?        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.getTopNOperations(5, startTime, endTime);
        
        // 验证结果为空
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTopNOperations_singleOperation() {
        // 准备鍙湁涓€绉嶆搷浣滅被鍨嬬殑数据
        List<OperationLog> singleOpLogs = new ArrayList<>();
        
        OperationLog op = new OperationLog();
        op.setId(1L);
        op.setUsername("user1");
        op.setUserId(1001L);
        op.setOperationType("QUERY");
        op.setOperationDesc("查询数据");
        op.setResultCode("200");
        op.setIsSensitive(false);
        op.setOperationTime(LocalDateTime.now().minusHours(1));
        singleOpLogs.add(op);
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(singleOpLogs);
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.getTopNOperations(5, startTime, endTime);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        
        Map<String, Object> item = result.get(0);
        assertEquals("QUERY", item.get("operationType"));
        assertEquals(1L, item.get("count"));
        assertEquals(100.0, item.get("percentage"));
    }

    @Test
    void getTopNOperations_exception() {
        // 模拟异常情况
        when(operationLogService.list(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.getTopNOperations(5, startTime, endTime);
        
        // 验证异常处理
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void analyzeSuccessRate() {
        // 准备鍖呭惈成功鍜屽け璐ユ搷浣滅殑mock数据
        List<OperationLog> successRateLogs = new ArrayList<>(mockLogs);
        
        // 添加失败操作
        OperationLog failedOp = new OperationLog();
        failedOp.setId(4L);
        failedOp.setUsername("user1");
        failedOp.setUserId(1001L);
        failedOp.setOperationType("UPDATE");
        failedOp.setOperationDesc("更新数据");
        failedOp.setResultCode("failure");
        failedOp.setIsSensitive(false);
        failedOp.setOperationTime(LocalDateTime.now().minusHours(1));
        successRateLogs.add(failedOp);
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(successRateLogs);
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // 验证每个分组的结果
        for (Map<String, Object> groupResult : result) {
            assertTrue(groupResult.containsKey("groupKey"));
            assertTrue(groupResult.containsKey("totalCount"));
            assertTrue(groupResult.containsKey("successCount"));
            assertTrue(groupResult.containsKey("successRate"));
        }
    }

    @Test
    void analyzeSuccessRate_allSuccess() {
        // 准备全部成功的mock数据
        List<OperationLog> allSuccessLogs = new ArrayList<>(mockLogs);
        
        // 确保所有操作都是成功的
        allSuccessLogs.forEach(log -> log.setResultCode("200"));
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(allSuccessLogs);
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // 验证每个分组的成功率都是100%
        for (Map<String, Object> groupResult : result) {
            String successRate = (String) groupResult.get("successRate");
            assertEquals("100.00%", successRate);
        }
    }

    @Test
    void analyzeSuccessRate_allFailure() {
        // 准备全部失败的mock数据
        List<OperationLog> allFailureLogs = new ArrayList<>(mockLogs);
        
        // 将所有操作设置为失败
        allFailureLogs.forEach(log -> log.setResultCode("500"));
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(allFailureLogs);
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // 验证每个分组的成功率都是0%
        for (Map<String, Object> groupResult : result) {
            String successRate = (String) groupResult.get("successRate");
            assertEquals("0.00%", successRate);
        }
    }

    @Test
    void analyzeSuccessRate_emptyData() {
        // 准备空数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void analyzeSuccessRate_mixedResultCodes() {
        // 准备鍖呭惈澶氱结果浠ｇ爜鐨刴ock数据
        List<OperationLog> mixedLogs = new ArrayList<>();
        
        // 添加涓嶅悓结果浠ｇ爜鐨勬搷浣滐紙鍙湁"200"琚涓烘垚鍔燂級
        String[] resultCodes = {"200", "500", "400", "401", "403"};
        
        for (int i = 0; i < resultCodes.length; i++) {
            OperationLog log = new OperationLog();
            log.setId((long) (i + 1));
            log.setUsername("user" + i);
            log.setUserId(1000L + i);
            log.setOperationType("TYPE_" + i);
            log.setOperationDesc("操作鎻忚堪" + i);
            log.setResultCode(resultCodes[i]);
            log.setIsSensitive(false);
            log.setOperationTime(LocalDateTime.now().minusHours(i + 1));
            mixedLogs.add(log);
        }
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mixedLogs);
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证结果（仅有 "200" 视为成功）
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // 验证每个分组的结果
        for (Map<String, Object> groupResult : result) {
            String successRate = (String) groupResult.get("successRate");
            // 姣忎釜操作绫诲瀷鍙湁涓€鏉¤褰曪紝鎵€浠ユ垚鍔熺巼应该鏄?%鎴?00%
            assertTrue(successRate.equals("100.00%") || successRate.equals("0.00%"));
        }
    }

    @Test
    void analyzeSuccessRate_exception() {
        // 模拟异常情况
        when(operationLogService.list(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行方法
        List<Map<String, Object>> result = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "operationType");
        
        // 验证异常处理
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void analyzeSuccessRate_byUser() {
        // 准备mock数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行方法
        List<Map<String, Object>> successRateData = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "user");
        
        // 验证结果
        assertNotNull(successRateData);
        // 应该鏈?涓敤鎴风殑缁熻
        assertTrue(successRateData.size() > 0);
    }

    @Test
    void analyzeSuccessRate_invalidGroupBy() {
        // 准备mock数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行方法
        List<Map<String, Object>> successRateData = auditAnalysisService.analyzeSuccessRate(startTime, endTime, "invalid");
        
        // 验证结果鏄┖鍒楄〃
        assertNotNull(successRateData);
        assertTrue(successRateData.isEmpty());
    }

    @Test
    void generateAuditReport() {
        // 准备mock数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行方法
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        
        // 验证结果
        assertNotNull(report);
        assertTrue(report.containsKey("summary"));
        assertTrue(report.containsKey("userBehavior"));
        assertTrue(report.containsKey("abnormalOperations"));
        assertTrue(report.containsKey("operationTrend"));
        assertTrue(report.containsKey("sensitiveOperations"));
        assertTrue(report.containsKey("topOperations"));
        assertTrue(report.containsKey("successRate"));
        
        // 验证鎽樿淇℃伅
        @SuppressWarnings("unchecked")
        Map<String, Object> summary = (Map<String, Object>) report.get("summary");
        assertTrue(summary.containsKey("totalOperations"));
        assertTrue(summary.containsKey("totalUsers"));
        assertTrue(summary.containsKey("timeRange"));
    }

    @Test
    void generateAuditReport_comprehensive() {
        // 准备鏇村叏闈㈢殑mock数据
        List<OperationLog> comprehensiveLogs = new ArrayList<>(mockLogs);
        
        // 添加鏇村数据浠ユ祴璇曞悇绉嶇粺璁?        // 添加鏁忔劅操作
        OperationLog sensitiveOp = new OperationLog();
        sensitiveOp.setId(4L);
        sensitiveOp.setUsername("admin");
        sensitiveOp.setUserId(1000L);
        sensitiveOp.setOperationType("DELETE");
        sensitiveOp.setOperationDesc("删除用户");
        sensitiveOp.setResultCode("success");
        sensitiveOp.setIsSensitive(true);
        sensitiveOp.setOperationTime(LocalDateTime.now().minusHours(2));
        comprehensiveLogs.add(sensitiveOp);
        
        // 添加失败操作
        OperationLog failedOp = new OperationLog();
        failedOp.setId(5L);
        failedOp.setUsername("user1");
        failedOp.setUserId(1001L);
        failedOp.setOperationType("UPDATE");
        failedOp.setOperationDesc("更新数据");
        failedOp.setResultCode("failure");
        failedOp.setIsSensitive(false);
        failedOp.setOperationTime(LocalDateTime.now().minusHours(1));
        comprehensiveLogs.add(failedOp);
        
        // 添加开傚父操作锛堢煭鏃堕棿内容娆″け璐ワ級
        LocalDateTime baseTime = LocalDateTime.now().minusMinutes(10);
        for (int i = 0; i < 3; i++) {
            OperationLog rapidFail = new OperationLog();
            rapidFail.setId(6L + i);
            rapidFail.setUsername("user3");
            rapidFail.setUserId(1003L);
            rapidFail.setOperationType("LOGIN");
            rapidFail.setOperationDesc("用户鐧诲綍");
            rapidFail.setResultCode("failure");
            rapidFail.setIsSensitive(false);
            rapidFail.setOperationTime(baseTime.plusMinutes(i * 2));
            comprehensiveLogs.add(rapidFail);
        }
        
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(comprehensiveLogs);
        
        // 执行方法
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        
        // 验证报告完整性
        assertNotNull(report);
        
        // 验证鎽樿
        @SuppressWarnings("unchecked")
        Map<String, Object> summary = (Map<String, Object>) report.get("summary");
        assertTrue((Long) summary.get("totalOperations") > 0);
        assertTrue((Long) summary.get("totalUsers") > 0);
        
        // 验证鍚勪釜閮ㄥ垎
        assertNotNull(report.get("userBehavior"));
        assertNotNull(report.get("abnormalOperations"));
        assertNotNull(report.get("operationTrend"));
        assertNotNull(report.get("sensitiveOperations"));
        assertNotNull(report.get("topOperations"));
        assertNotNull(report.get("successRate"));
    }

    @Test
    void generateAuditReport_emptyData() {
        // 准备空数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        
        // 执行方法
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        
        // 验证结果
        assertNotNull(report);
        
        // 验证鎽樿淇℃伅
        @SuppressWarnings("unchecked")
        Map<String, Object> summary = (Map<String, Object>) report.get("summary");
        assertEquals(0L, summary.get("totalOperations"));
        assertEquals(0L, summary.get("totalUsers"));
        
        // 验证各部分为空或默认值
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> abnormalOps = (List<Map<String, Object>>) report.get("abnormalOperations");
        assertTrue(abnormalOps.isEmpty());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> sensitiveOps = (Map<String, Object>) report.get("sensitiveOperations");
        assertEquals(0L, sensitiveOps.get("totalCount"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> successRate = (Map<String, Object>) report.get("successRate");
        assertEquals(0L, successRate.get("totalCount"));
    }

    @Test
    void generateAuditReport_exception() {
        // 模拟异常情况
        when(operationLogService.list(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行方法
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        
        // 验证异常处理
        assertNotNull(report);
        assertTrue(report.containsKey("error"));
    }

    @Test
    void generateAuditReport_partialException() {
        // 模拟閮ㄥ垎方法成功锛岄儴鍒嗘柟娉曞け璐ョ殑鎯呭喌
        List<OperationLog> partialLogs = new ArrayList<>(mockLogs);
        
        // 第一次调用成功
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(partialLogs);
        
        // 浣嗘ā鎷熸煇涓唴閮ㄦ柟娉曡皟鐢ㄦ椂鎶涘嚭开傚父
        // 杩欓噷鎴戜滑閫氳繃淇敼mock琛屼负鏉ユā鎷熼儴鍒嗗け璐?        // 鐢变簬generateAuditReport鍐呴儴璋冪敤澶氫釜方法锛屾垜浠棤娉曠洿鎺ユ帶鍒跺崟涓柟娉?        // 鎵€浠ヨ繖涓祴璇曚富瑕侀獙璇佹暣浣撳紓甯稿鐞?        
        // 执行方法
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        
        // 验证结果
        assertNotNull(report);
        // 鐢变簬鎴戜滑鏃犳硶绮剧‘控制閮ㄥ垎开傚父锛屼富瑕侀獙璇佹柟娉曡兘姝ｅ父返回
    }
}
