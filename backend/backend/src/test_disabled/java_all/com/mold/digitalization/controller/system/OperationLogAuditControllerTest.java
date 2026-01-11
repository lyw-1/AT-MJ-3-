package com.mold.digitalization.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.process.common.Result;
import com.mold.digitalization.entity.dto.OperationLogDTO;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.audit.AuditAnalysisService;
import com.mold.digitalization.service.audit.LogExportService;
import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * OperationLogAuditController鍗曞厓测试
 */
class OperationLogAuditControllerTest {

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private AuditAnalysisService auditAnalysisService;

    @Mock
    private LogExportService logExportService;

    @InjectMocks
    private OperationLogAuditController operationLogAuditController;

    private OperationLog testOperationLog;
    private OperationLogQueryDTO testQueryDTO;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 鍒濆鍖栨祴璇曟暟鎹?
        testStartTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        testEndTime = LocalDateTime.of(2024, 12, 31, 23, 59);
        
        testOperationLog = new OperationLog();
        testOperationLog.setId(1L);
        testOperationLog.setUserId(1001L);
        testOperationLog.setUsername("testuser");
        testOperationLog.setUserIp("192.168.1.1");
        testOperationLog.setOperationType("QUERY");
        testOperationLog.setOperationDesc("查询操作");
        testOperationLog.setOperationContent("查询用户鍒楄〃");
        testOperationLog.setResultCode("200");
        testOperationLog.setIsSensitive(false);
        testOperationLog.setOperationTime(LocalDateTime.now());
        
        testQueryDTO = new OperationLogQueryDTO();
        testQueryDTO.setUsername("testuser");
        testQueryDTO.setStartTime(testStartTime);
        testQueryDTO.setEndTime(testEndTime);
    }

    @Test
    void testQueryLogPage_Success() {
        // 准备
        Page<OperationLog> mockPage = new Page<>(1, 10, 100);
        mockPage.setRecords(Arrays.asList(testOperationLog));
        when(operationLogService.queryByCondition(any(OperationLogQueryDTO.class))).thenReturn(mockPage);
        
        // 执行
        Result<Page<OperationLog>> result = operationLogAuditController.queryLogPage(testQueryDTO);
        
        // 校验
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());
        assertEquals(testOperationLog, result.getData().getRecords().get(0));
    }

    @Test
    void testGetLogDetail_Success() {
        // 准备
        when(operationLogService.getById(1L)).thenReturn(testOperationLog);
        
        // 执行
        Result<OperationLogDTO> result = operationLogAuditController.getLogDetail(1L);
        
        // 校验
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(testOperationLog.getId(), result.getData().getId());
        assertEquals(testOperationLog.getUsername(), result.getData().getUsername());
        assertEquals(testOperationLog.getUserIp(), result.getData().getIp());
        assertEquals(testOperationLog.getOperationType(), result.getData().getOperationType());
    }

    @Test
    void testGetLogDetail_NotFound() {
        // 准备
        when(operationLogService.getById(999L)).thenReturn(null);
        
        // 执行
        Result<OperationLogDTO> result = operationLogAuditController.getLogDetail(999L);
        
        // 验证
        assertNotNull(result);
        assertEquals("日志不存在", result.getMessage());
        assertEquals(1004, result.getCode());
        assertNull(result.getData());
    }

    @Test
    void testQuerySensitiveLogs_Success() {
        // 准备
        Page<OperationLog> mockPage = new Page<>(1, 10, 50);
        mockPage.setRecords(Arrays.asList(testOperationLog));
        when(operationLogService.queryByCondition(any(OperationLogQueryDTO.class))).thenReturn(mockPage);
        
        // 执行
        Result<Page<OperationLog>> result = operationLogAuditController.querySensitiveLogs(testQueryDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());
    }

    @Test
    void testDeleteBatchLogs_Success() {
        // 准备
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(operationLogService.deleteBatch(any(Long[].class))).thenReturn(true);
        
        // 执行
        Result<Boolean> result = operationLogAuditController.deleteBatchLogs(ids);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertTrue(result.getData());
    }

    @Test
    void testCleanOldLogs_Success() {
        // 准备
        LocalDateTime beforeTime = LocalDateTime.of(2023, 12, 31, 23, 59);
        when(operationLogService.cleanOldLogs(beforeTime)).thenReturn(100);
        
        // 执行
        Result<Integer> result = operationLogAuditController.cleanOldLogs(beforeTime);
        
        // 校验
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertEquals(100, result.getData());
    }

    @Test
    void testStatisticByOperationType_Success() {
        // 准备
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("QUERY");
        stat1.setCount(50L);
        OperationLogStatisticDTO stat2 = new OperationLogStatisticDTO();
        stat2.setName("UPDATE");
        stat2.setCount(30L);
        List<OperationLogStatisticDTO> statistics = Arrays.asList(stat1, stat2);
        when(operationLogService.statisticByOperationType(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(statistics);
        
        // 执行
        Result<List<OperationLogStatisticDTO>> result = operationLogAuditController.statisticByOperationType(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("QUERY", result.getData().get(0).getName());
        assertEquals(50L, result.getData().get(0).getCount());
    }

    @Test
    void testStatisticByUser_Success() {
        // 准备
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("user1");
        stat1.setCount(20L);
        OperationLogStatisticDTO stat2 = new OperationLogStatisticDTO();
        stat2.setName("user2");
        stat2.setCount(15L);
        List<OperationLogStatisticDTO> statistics = Arrays.asList(stat1, stat2);
        when(operationLogService.statisticByUser(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(statistics);
        
        // 执行
        Result<List<OperationLogStatisticDTO>> result = operationLogAuditController.statisticByUser(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("user1", result.getData().get(0).getName());
        assertEquals(20L, result.getData().get(0).getCount());
    }

    @Test
    void testStatisticByModule_Success() {
        // 准备
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("用户绠＄悊");
        stat1.setCount(40L);
        OperationLogStatisticDTO stat2 = new OperationLogStatisticDTO();
        stat2.setName("鏉冮檺绠＄悊");
        stat2.setCount(25L);
        List<OperationLogStatisticDTO> statistics = Arrays.asList(stat1, stat2);
        when(operationLogService.statisticByModule(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(statistics);
        
        // 执行
        Result<List<OperationLogStatisticDTO>> result = operationLogAuditController.statisticByModule(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("用户绠＄悊", result.getData().get(0).getName());
        assertEquals(40L, result.getData().get(0).getCount());
    }

    @Test
    void testAnalyzeUserBehavior_Success() {
        // 准备
        Map<String, Object> analysisResult = new HashMap<>();
        analysisResult.put("userId", 1001L);
        analysisResult.put("operationCount", 150);
        analysisResult.put("successRate", 95.5);
        analysisResult.put("favoriteModule", "用户绠＄悊");
        when(auditAnalysisService.analyzeUserBehavior(1001L, testStartTime, testEndTime)).thenReturn(analysisResult);
        
        // 执行
        Result<Map<String, Object>> result = operationLogAuditController.analyzeUserBehavior(1001L, testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1001L, result.getData().get("userId"));
        assertEquals(150, result.getData().get("operationCount"));
    }

    @Test
    void testDetectAbnormalOperations_Success() {
        // 准备
        OperationLog abnormalLog = new OperationLog();
        abnormalLog.setId(2L);
        abnormalLog.setUsername("suspicious_user");
        abnormalLog.setOperationType("DELETE");
        abnormalLog.setOperationDesc("开傚父删除操作");
        List<OperationLog> abnormalOperations = Arrays.asList(abnormalLog);
        when(auditAnalysisService.detectAbnormalOperations(testStartTime, testEndTime)).thenReturn(abnormalOperations);
        
        // 执行
        Result<List<OperationLog>> result = operationLogAuditController.detectAbnormalOperations(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals("suspicious_user", result.getData().get(0).getUsername());
    }

    @Test
    void testAnalyzeOperationTrend_Success() {
        // 准备
        Map<String, Object> trend1 = new HashMap<>();
        trend1.put("date", "2024-01-01");
        trend1.put("count", 10);
        Map<String, Object> trend2 = new HashMap<>();
        trend2.put("date", "2024-01-02");
        trend2.put("count", 15);
        List<Map<String, Object>> trendData = Arrays.asList(trend1, trend2);
        when(auditAnalysisService.analyzeOperationTrend("day", testStartTime, testEndTime)).thenReturn(trendData);
        
        // 执行
        Result<List<Map<String, Object>>> result = operationLogAuditController.analyzeOperationTrend("day", testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("2024-01-01", result.getData().get(0).get("date"));
    }

    @Test
    void testStatisticSensitiveOperations_Success() {
        // 准备
        OperationLogStatisticDTO statisticDTO = new OperationLogStatisticDTO();
        statisticDTO.setOperationType("鏁忔劅操作");
        statisticDTO.setCount(25L);
        when(auditAnalysisService.statisticSensitiveOperations(any(OperationLogQueryDTO.class))).thenReturn(statisticDTO);
        
        // 执行
        Result<OperationLogStatisticDTO> result = operationLogAuditController.statisticSensitiveOperations(testQueryDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("鏁忔劅操作", result.getData().getOperationType());
        assertEquals(25L, result.getData().getCount());
    }

    @Test
    void testGetTopNOperations_Success() {
        // 准备
        Map<String, Object> top1 = new HashMap<>();
        top1.put("operationType", "QUERY");
        top1.put("count", 100L);
        Map<String, Object> top2 = new HashMap<>();
        top2.put("operationType", "UPDATE");
        top2.put("count", 75L);
        List<Map<String, Object>> topOperations = Arrays.asList(top1, top2);
        when(auditAnalysisService.getTopNOperations(5, testStartTime, testEndTime)).thenReturn(topOperations);
        
        // 执行
        Result<List<Map<String, Object>>> result = operationLogAuditController.getTopNOperations(5, testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("QUERY", result.getData().get(0).get("operationType"));
    }

    @Test
    void testAnalyzeSuccessRate_Success() {
        // 准备
        Map<String, Object> rate1 = new HashMap<>();
        rate1.put("operationType", "QUERY");
        rate1.put("successRate", 98.5);
        Map<String, Object> rate2 = new HashMap<>();
        rate2.put("operationType", "UPDATE");
        rate2.put("successRate", 92.0);
        List<Map<String, Object>> successRateData = Arrays.asList(rate1, rate2);
        when(auditAnalysisService.analyzeSuccessRate(testStartTime, testEndTime, "operationType")).thenReturn(successRateData);
        
        // 执行
        Result<List<Map<String, Object>>> result = operationLogAuditController.analyzeSuccessRate(testStartTime, testEndTime, "operationType");
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals(98.5, result.getData().get(0).get("successRate"));
    }

    @Test
    void testGenerateAuditReport_Success() {
        // 准备
        Map<String, Object> report = new HashMap<>();
        report.put("totalOperations", 1000L);
        report.put("successRate", 96.5);
        report.put("sensitiveOperations", 25L);
        report.put("abnormalOperations", 3L);
        when(auditAnalysisService.generateAuditReport(testStartTime, testEndTime)).thenReturn(report);
        
        // 执行
        Result<Map<String, Object>> result = operationLogAuditController.generateAuditReport(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals("Operation successful", result.getMessage());
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1000L, result.getData().get("totalOperations"));
        assertEquals(96.5, result.getData().get("successRate"));
    }

    @Test
    void testExportLogsToExcel_Success() throws Exception {
        // 准备
        byte[] excelData = "Mock Excel Data".getBytes();
        ResponseEntity<byte[]> mockResponse = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
        when(logExportService.exportToExcel(any(OperationLogQueryDTO.class))).thenReturn(mockResponse);
        
        // 执行
        ResponseEntity<InputStreamResource> result = operationLogAuditController.exportLogsToExcel(testQueryDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getHeaders().getContentDisposition());
        assertTrue(result.getHeaders().getContentDisposition().getFilename().contains("operation_logs_"));
        assertTrue(result.getHeaders().getContentDisposition().getFilename().endsWith(".xlsx"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result.getHeaders().getContentType());
    }

    @Test
    void testExportLogsToCsv_Success() throws Exception {
        // 准备
        byte[] csvData = "Mock CSV Data".getBytes();
        ResponseEntity<byte[]> mockResponse = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(csvData);
        when(logExportService.exportToCsv(any(OperationLogQueryDTO.class))).thenReturn(mockResponse);
        
        // 执行
        ResponseEntity<InputStreamResource> result = operationLogAuditController.exportLogsToCsv(testQueryDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getHeaders().getContentDisposition());
        assertTrue(result.getHeaders().getContentDisposition().getFilename().contains("operation_logs_"));
        assertTrue(result.getHeaders().getContentDisposition().getFilename().endsWith(".csv"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result.getHeaders().getContentType());
    }

    @Test
    void testExportAuditReportToPdf_Success() throws Exception {
        // 准备
        byte[] pdfData = "Mock PDF Data".getBytes();
        ResponseEntity<byte[]> mockResponse = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
        when(logExportService.exportAuditReportToPdf(any(OperationLogQueryDTO.class))).thenReturn(mockResponse);
        
        // 执行
        ResponseEntity<InputStreamResource> result = operationLogAuditController.exportAuditReportToPdf(testStartTime, testEndTime);
        
        // 验证
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getHeaders().getContentDisposition());
        assertTrue(result.getHeaders().getContentDisposition().getFilename().contains("audit_report_"));
        assertTrue(result.getHeaders().getContentDisposition().getFilename().endsWith(".pdf"));
        assertEquals(MediaType.APPLICATION_PDF, result.getHeaders().getContentType());
    }

    @Test
    void testExportSensitiveLogs_Success() throws Exception {
        // 准备
        byte[] excelData = "Mock Sensitive Excel Data".getBytes();
        ResponseEntity<byte[]> mockResponse = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=sensitive_test.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
        when(logExportService.exportSensitiveLogsToExcel(any(OperationLogQueryDTO.class))).thenReturn(mockResponse);
        
        // 执行
        ResponseEntity<InputStreamResource> result = operationLogAuditController.exportSensitiveLogs(testQueryDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getHeaders().getContentDisposition());
        assertTrue(result.getHeaders().getContentDisposition().getFilename().contains("sensitive_operation_logs_"));
        assertTrue(result.getHeaders().getContentDisposition().getFilename().endsWith(".xlsx"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result.getHeaders().getContentType());
    }
}
