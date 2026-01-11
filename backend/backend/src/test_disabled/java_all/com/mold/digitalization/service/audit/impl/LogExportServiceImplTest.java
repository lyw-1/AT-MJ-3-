package com.mold.digitalization.service.audit.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogExportServiceImplTest {

    @Mock
    private OperationLogService operationLogService;

    @InjectMocks
    private LogExportServiceImpl logExportService;

    private List<OperationLog> mockLogs;
    private OperationLogQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        mockLogs = new ArrayList<>();
        
        OperationLog log1 = new OperationLog();
        log1.setId(1L);
        log1.setUserId(1001L);
        log1.setUsername("张三");
        log1.setModule("用户管理");
        log1.setOperationType("ADD");
        log1.setOperationDesc("添加新用户");
        log1.setUserIp("192.168.1.100");
        log1.setOperationContent("{\"username\":\"test\",\"password\":\"******\"}");
        log1.setResultCode("200");
        log1.setIsSensitive(false);
        log1.setOperationTime(LocalDateTime.now().minusDays(1));
        mockLogs.add(log1);
        
        OperationLog log2 = new OperationLog();
        log2.setId(2L);
        log2.setUserId(1002L);
        log2.setUsername("鏉庡洓");
        log2.setModule("系统设置");
        log2.setOperationType("UPDATE");
        log2.setOperationDesc("更新系统配置");
        log2.setUserIp("192.168.1.101");
        log2.setOperationContent("{\"key\":\"system.version\",\"value\":\"v2.0\"}");
        log2.setResultCode("200");
        log2.setIsSensitive(true);
        log2.setSensitiveLevel("high");
        log2.setOperationTime(LocalDateTime.now().minusHours(12));
        mockLogs.add(log2);
        
        // 准备查询鏉′欢
        queryDTO = new OperationLogQueryDTO();
        queryDTO.setUsername("张三");
        queryDTO.setStartTime(LocalDateTime.now().minusDays(7));
        queryDTO.setEndTime(LocalDateTime.now());
    }

    @Test
    void exportToExcel() throws Exception {
        // 模拟数据查询
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行瀵煎嚭方法
        ResponseEntity<byte[]> response = logExportService.exportToExcel(queryDTO);
        
        // 验证鍝嶅簲
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证鍝嶅簲澶?        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getContentType());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertNotNull(headers.getContentDisposition());
        assertTrue(headers.getContentDisposition().getFilename().contains(".xlsx"));
        
        // 验证Excel内容
        byte[] body = response.getBody();
        assertNotNull(body);
        
        // 尝试读取Excel文件，验证其有效性
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertNotNull(workbook.getSheetAt(0));
            // 验证Excel文件至少包含表头行
            assertNotNull(workbook.getSheetAt(0).getRow(0));
        }
        
        // 验证mock璋冪敤
        verify(operationLogService).list(any(QueryWrapper.class));
    }

    @Test
    void exportToCsv() throws Exception {
        // 模拟数据查询
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(mockLogs);
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportToCsv(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证响应头
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getContentType());
        assertEquals(new MediaType("text", "csv", StandardCharsets.UTF_8), headers.getContentType());
        assertNotNull(headers.getContentDisposition());
        assertTrue(headers.getContentDisposition().getFilename().contains(".csv"));
        
        // 验证CSV内容
        byte[] body = response.getBody();
        assertNotNull(body);
        
        // 读取CSV文件并验证其内容
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(new ByteArrayInputStream(body), StandardCharsets.UTF_8))) {
            // 验证表头（第一行就是表头，没有BOM字符）
            String headerLine = reader.readLine();
            assertNotNull(headerLine);
            assertTrue(headerLine.contains("ID"));
            assertTrue(headerLine.contains("用户ID"));
            assertTrue(headerLine.contains("用户名"));
            
            // 验证数据行数量
            int dataLineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                dataLineCount++;
            }
            // 验证数据行数量等于mock数据数量
            assertEquals(mockLogs.size(), dataLineCount);
        }
        
        // 验证mock璋冪敤
        verify(operationLogService).list(any(QueryWrapper.class));
    }

    @Test
    void exportAuditReportToPdf() throws Exception {
        // 模拟数据查询 - 使用不同的返回值来区分不同的count调用
        when(operationLogService.count(any(QueryWrapper.class))).thenReturn(2L, 2L, 1L);
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportAuditReportToPdf(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证响应头
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getContentType());
        assertEquals(MediaType.TEXT_PLAIN, headers.getContentType());
        assertNotNull(headers.getContentDisposition());
        assertTrue(headers.getContentDisposition().getFilename().contains(".txt"));
        
        // 验证报告内容
        byte[] body = response.getBody();
        assertNotNull(body);
        String content = new String(body, StandardCharsets.UTF_8);
        assertTrue(content.contains("===== 操作日志审计报告 ====="));
        assertTrue(content.contains("总操作数: 2"));
        assertTrue(content.contains("成功操作数: 2"));
        assertTrue(content.contains("敏感操作数: 1"));
        assertTrue(content.contains("成功率: 100.00%"));
        
        // 验证mock璋冪敤
        verify(operationLogService, times(3)).count(any(QueryWrapper.class));
    }

    @Test
    void exportSensitiveLogsToExcel() throws Exception {
        // 模拟敏感数据查询
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(
                mockLogs.stream().filter(OperationLog::getIsSensitive).toList());
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportSensitiveLogsToExcel(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证响应头
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getContentType());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertNotNull(headers.getContentDisposition());
        assertTrue(headers.getContentDisposition().getFilename().contains(".xlsx"));
        
        // 验证Excel内容
        byte[] body = response.getBody();
        assertNotNull(body);
        
        // 尝试读取Excel文件，验证其有效性
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertNotNull(workbook.getSheetAt(0));
            // 验证Excel文件至少包含表头行
            assertNotNull(workbook.getSheetAt(0).getRow(0));
        }
        
        // 验证mock璋冪敤
        verify(operationLogService).list(any(QueryWrapper.class));
    }

    @Test
    void exportToExcel_emptyData() throws Exception {
        // 模拟空数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(new ArrayList<>());
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportToExcel(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证Excel仍然可以正常创建
        byte[] body = response.getBody();
        assertNotNull(body);
        
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            assertEquals(1, workbook.getNumberOfSheets());
        }
    }

    @Test
    void exportToCsv_emptyData() throws Exception {
        // 模拟空数据
        when(operationLogService.list(any(QueryWrapper.class))).thenReturn(new ArrayList<>());
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportToCsv(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证CSV内容
        byte[] body = response.getBody();
        assertNotNull(body);
        
        // 读取CSV文件并验证其内容（空数据时应该只有表头）
        String csvContent = new String(body, StandardCharsets.UTF_8);
        String[] lines = csvContent.split("\n");
        
        // 跳过BOM字符（如果有）
        int startIndex = 0;
        if (lines.length > 0 && lines[0].startsWith("\uFEFF")) {
            lines[0] = lines[0].substring(1);
        }
        
        // 验证表头
        assertTrue(lines.length >= 1);
        String headerLine = lines[0];
        assertNotNull(headerLine);
        assertTrue(headerLine.contains("ID"));
        assertTrue(headerLine.contains("用户ID"));
        assertTrue(headerLine.contains("用户名"));
        
        // 验证没有数据行（空数据时只有表头）
        assertEquals(1, lines.length);
        
        // 验证mock璋冪敤
        verify(operationLogService).list(any(QueryWrapper.class));
    }

    @Test
    void exportAuditReportToPdf_zeroCount() throws Exception {
        // 模拟零数据 - 使用不同的返回值来区分不同的count调用
        when(operationLogService.count(any(QueryWrapper.class))).thenReturn(0L, 0L, 0L);
        
        // 执行导出方法
        ResponseEntity<byte[]> response = logExportService.exportAuditReportToPdf(queryDTO);
        
        // 验证响应
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        // 验证报告内容包含零统计
        byte[] body = response.getBody();
        assertNotNull(body);
        String content = new String(body, StandardCharsets.UTF_8);
        assertTrue(content.contains("总操作数: 0"));
        assertTrue(content.contains("成功率: 0%"));
        
        // 验证mock璋冪敤
        verify(operationLogService, times(3)).count(any(QueryWrapper.class));
    }
}
