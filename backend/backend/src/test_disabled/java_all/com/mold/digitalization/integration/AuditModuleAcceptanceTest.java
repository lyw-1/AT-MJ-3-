package com.mold.digitalization.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.audit.AuditAnalysisService;
import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import com.mold.digitalization.config.TestConfig;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 审计模块验收测试
 * 全面测试操作日志审计功能的核心特性
 */
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AuditModuleAcceptanceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private AuditAnalysisService auditAnalysisService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        // 初始化MockMvc和会话对象
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        session.setAttribute("username", "test-admin");
        session.setAttribute("userId", 1000L);
        
        request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.100");
        request.setSession(session);
    }

    /**
     * 测试用例1: 验证密码修改操作的敏感内容脱敏
     * 验收标准: 敏感字段(password)应该被正确脱敏为******
     */
    @Test
    void testSensitiveContentMasking() throws Exception {
        // 准备测试数据 - 包含敏感信息
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("oldPassword", "Admin@123");
        requestBody.put("newPassword", "NewPass@456");
        requestBody.put("confirmPassword", "NewPass@456");
        
        // 执行密码修改操作
        mockMvc.perform(post("/api/v1/user/update-password")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
        
        // 查询最近的操作日志 - 使用queryByTimeRange替代getRecentLogs
        List<OperationLog> recentLogs = operationLogService.queryByTimeRange(
            java.time.LocalDateTime.now().minusHours(1), 
            java.time.LocalDateTime.now());
        assertFalse(recentLogs.isEmpty(), "应该生成操作日志记录");
        
        // 查找密码修改相关日志
        OperationLog passwordLog = recentLogs.stream()
                .filter(log -> log.getOperationType().equals("update-password") || 
                        log.getOperationDesc().contains("密码"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(passwordLog, "应该找到密码修改操作的日志");
        assertTrue(passwordLog.getIsSensitive(), "密码修改应该被标记为敏感操作");
        
        // 验证敏感内容脱敏
        String operationContent = passwordLog.getOperationContent();
        assertFalse(operationContent.contains("Admin@123"), "原密码不应明文显示");
        assertFalse(operationContent.contains("NewPass@456"), "新密码不应明文显示");
        assertTrue(operationContent.contains("\"password\":\"******\""), "密码字段应该被脱敏");
        
        System.out.println("敏感内容脱敏测试通过");
    }

    /**
     * 测试用例2: 验证AOP切面能够正确捕获和记录异常操作
     * 验收标准: 异常操作应被记录，结果标记为失败，包含错误信息
     */
    @Test
    void testExceptionOperationLogging() throws Exception {
        // 准备错误测试数据 - 故意提供不完整的请求参数
        Map<String, String> invalidRequestBody = new HashMap<>();
        invalidRequestBody.put("username", "test-user");
        // 缺少必要字段
        
        // 执行会产生异常的操作
        mockMvc.perform(post("/api/v1/user/update-profile")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody)));
        
        // 查询最近的操作日志 - 使用queryByTimeRange替代getRecentLogs
        List<OperationLog> recentLogs = operationLogService.queryByTimeRange(
            java.time.LocalDateTime.now().minusHours(1), 
            java.time.LocalDateTime.now());
        
        // 查找异常操作日志
        OperationLog errorLog = recentLogs.stream()
                .filter(log -> log.getResultCode().contains("失败") || 
                        log.getResultCode().contains("error") ||
                        log.getResultCode().contains("exception"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(errorLog, "应该找到异常操作的日志记录");
        assertFalse(errorLog.getResultCode().equals("success"), "异常操作结果不应为success");
        assertTrue(errorLog.getResultCode().length() > 10, "异常日志应包含详细错误信息");
        
        System.out.println("异常操作日志记录测试通过");
    }

    /**
     * 测试用例3: 验证审计分析服务的异常操作检测功能
     * 验收标准: 能够正确检测和分析短时间内的重复失败操作
     */
    @Test
    void testAbnormalOperationDetection() throws Exception {
        // 模拟多次失败操作
        for (int i = 0; i < 3; i++) {
            // 创建敏感操作日志
        OperationLog log = new OperationLog();
        log.setUsername("test-hacker");
        log.setUserId(999L);
        log.setUserIp("192.168.1.200");
        log.setOperationType("login");
        log.setOperationDesc("用户登录");
        log.setOperationContent("{\"username\":\"admin\",\"password\":\"******\"}");
        log.setResultCode("failure: 密码错误");
        log.setIsSensitive(true);
        log.setOperationTime(LocalDateTime.now());
        // log.setStatus(0); // OperationLog实体类没有status字段，注释掉
            
            operationLogService.saveOperationLog(log);
        }
        
        // 执行异常操作检测
        List<OperationLog> abnormalOperations = 
                auditAnalysisService.detectAbnormalOperations(null, null);
        
        assertNotNull(abnormalOperations, "异常检测结果不应为空");
        assertFalse(abnormalOperations.isEmpty(), "应该检测到异常操作模式");
        
        // 验证检测结果包含测试数据
        boolean found = false;
        for (OperationLog operation : abnormalOperations) {
            if (operation.getUsername() != null && 
                operation.getUsername().equals("test-hacker") &&
                operation.getOperationType() != null &&
                operation.getOperationType().equals("login")) {
                found = true;
                break;
            }
        }
        
        assertTrue(found, "应该检测到我们模拟的异常登录操作");
        
        System.out.println("异常操作检测功能测试通过");
    }

    /**
     * 测试用例4: 验证审计统计分析功能
     * 验收标准: 能够正确统计操作类型分布、成功率等指标
     */
    @Test
    void testAuditStatisticsAnalysis() {
        // 准备测试数据 - 不同类型的操作日志
        OperationLog loginSuccess = createTestLog("login", "success", false, 1);
        OperationLog dataQuery = createTestLog("query", "success", false, 1);
        OperationLog dataUpdate = createTestLog("update", "success", false, 1);
        OperationLog dataDelete = createTestLog("delete", "failure", true, 0);
        OperationLog configChange = createTestLog("config", "success", true, 1);
        
        // 保存测试数据
        operationLogService.saveOperationLog(loginSuccess);
        operationLogService.saveOperationLog(dataQuery);
        operationLogService.saveOperationLog(dataUpdate);
        operationLogService.saveOperationLog(dataDelete);
        operationLogService.saveOperationLog(configChange);
        
        // 创建查询条件
        OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
        queryDTO.setStartTime(LocalDateTime.now().minusHours(1));
        queryDTO.setEndTime(LocalDateTime.now().plusHours(1));
        
        // 执行统计分析
        OperationLogStatisticDTO statistics = 
                auditAnalysisService.statisticSensitiveOperations(queryDTO);
        
        // 验证统计结果
        assertNotNull(statistics, "统计结果不应为空");
        assertEquals(5L, statistics.getTotalCount(), "总操作数应该为5");
        assertEquals(4L, statistics.getSuccessCount(), "成功操作数应该为4");
        assertEquals(1L, statistics.getFailCount(), "失败操作数应该为1");
        assertEquals(2L, statistics.getSensitiveCount(), "敏感操作数应该为2");
        
        // 验证操作趋势分析
        List<Map<String, Object>> trendData = 
                auditAnalysisService.analyzeOperationTrend("day", null, null);
        assertNotNull(trendData, "趋势分析结果不应为空");
        assertFalse(trendData.isEmpty(), "趋势分析应包含数据");
        
        // 验证操作类型分布
        Map<String, Object> userBehavior = 
                auditAnalysisService.analyzeUserBehavior(null, null, null);
        assertNotNull(userBehavior, "用户行为分析结果不应为空");
        assertTrue(userBehavior.containsKey("operationTypeDistribution"), 
                "应包含操作类型分布");
        
        System.out.println("审计统计分析功能测试通过");
    }

    /**
     * 测试用例5: 验证审计报告生成功能
     * 验收标准: 能够生成包含完整统计信息的审计报告
     */
    @Test
    void testAuditReportGeneration() {
        // 生成一些测试操作日志
        for (int i = 0; i < 3; i++) {
            OperationLog log = createTestLog("test-operation-" + i, "success", i % 2 == 0, 1);
            operationLogService.saveOperationLog(log);
        }
        
        // 生成审计报告
        Map<String, Object> auditReport = 
                auditAnalysisService.generateAuditReport(null, null);
        
        // 验证报告结构和内容
        assertNotNull(auditReport, "审计报告不应为空");
        
        // 验证必要字段
        assertTrue(auditReport.containsKey("statistics"), "报告应包含统计信息");
        assertTrue(auditReport.containsKey("topOperations"), "报告应包含高频操作");
        assertTrue(auditReport.containsKey("abnormalOperations"), "报告应包含异常操作");
        assertTrue(auditReport.containsKey("dailyTrend"), "报告应包含趋势分析");
        assertTrue(auditReport.containsKey("topActiveUsers"), "报告应包含活跃用户");
        assertTrue(auditReport.containsKey("generatedTime"), "报告应包含生成时间");
        
        // 验证统计信息内容
        Map<String, Object> statistics = (Map<String, Object>) auditReport.get("statistics");
        assertNotNull(statistics);
        assertTrue(statistics.containsKey("totalOperations"), "统计应包含总操作数");
        assertTrue(statistics.containsKey("successOperations"), "统计应包含成功操作数");
        assertTrue(statistics.containsKey("failedOperations"), "统计应包含失败操作数");
        assertTrue(statistics.containsKey("sensitiveOperations"), "统计应包含敏感操作数");
        
        System.out.println("审计报告生成功能测试通过");
    }

    /**
     * 创建测试用操作日志
     */
    private OperationLog createTestLog(String operationType, String result, 
                                      boolean isSensitive, Integer status) {
        OperationLog log = new OperationLog();
        log.setUsername("test-user");
        log.setUserId(1001L);
        log.setUserIp("192.168.1.101");
        log.setOperationType(operationType);
        log.setOperationDesc("Test operation: " + operationType);
        log.setOperationContent("{\"test\":\"data\"}");
        log.setResultCode(result);
        log.setIsSensitive(isSensitive);
        log.setOperationTime(LocalDateTime.now());
        // log.setStatus(status); // OperationLog实体绫绘病鏈塻tatus瀛楁锛屾敞閲婃帀
        return log;
    }
}