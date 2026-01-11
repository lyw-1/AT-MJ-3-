package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.entity.InspectionResult;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.service.ProcessStateMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 鐘舵€佹満服务实现绫诲崟鍏冩祴璇? * 
 * @author System
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
class ProcessStateMachineServiceImplTest {
    
    @Mock
    private MoldProcessMapper moldProcessMapper;
    
    @InjectMocks
    private ProcessStateMachineServiceImpl stateMachineService;
    
    private MoldProcess testProcess;
    
    @BeforeEach
    void setUp() {
        // 设置测试数据
        testProcess = new MoldProcess();
        testProcess.setId(1L);
        testProcess.setProcessName("测试娴佺▼");
        testProcess.setCurrentStatus(1); // PENDING
    }
    
    @Test
    void testTriggerStateTransition() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        
        // 执行测试
        boolean result = stateMachineService.triggerStateTransition(1L, "START", 100L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
        verify(moldProcessMapper).updateById(any(MoldProcess.class));
    }
    
    @Test
    void testTriggerStateTransitionWithParameters() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        
        // 执行测试
        boolean result = stateMachineService.triggerStateTransition(1L, "START", 100L, "鍙傛暟");
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
        verify(moldProcessMapper).updateById(any(MoldProcess.class));
    }
    
    @Test
    void testValidateTransitionConditions() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.validateTransitionConditions(1L, "START");
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testGetAllowedEvents() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        List<String> result = stateMachineService.getAllowedEvents(1L);
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testGetStateTransitionHistory() {
        // 执行测试
        List<ProcessStatusHistory> result = stateMachineService.getStateTransitionHistory(1L);
        
        // 验证结果
        assertNotNull(result);
        // 在实际实现中，该方法会查询数据库获取变更记录
    }
    
    @Test
    void testRecordStateTransition() {
        // 执行测试
        boolean result = stateMachineService.recordStateTransition(1L, 1, 2, "START", 100L, "状态转换");
        
        // 验证结果
        assertTrue(result);
        // 在实际实现中，该方法会向数据库插入记录
    }
    
    @Test
    void testHandleProcessException() {
        // 执行测试
        boolean result = stateMachineService.handleProcessException(1L, "E001", "开傚父淇℃伅", 100L);
        
        // 验证结果
        assertTrue(result);
        // 在实际实现中，该方法会处理异常并记录
    }
    
    @Test
    void testGetProcessExceptions() {
        // 执行测试
        List<ProcessException> result = stateMachineService.getProcessExceptions(1L);
        
        // 验证结果
        assertNotNull(result);
        // 在实际实现中，该方法会查询数据库获取异常记录
    }
    
    @Test
    void testRecordInspectionResult() {
        // 创建测试数据
        InspectionResult inspectionResult = new InspectionResult();
        inspectionResult.setId(1L);
        inspectionResult.setProcessId(1L);
        inspectionResult.setResult("PASS");
        
        // 执行测试
        boolean result = stateMachineService.recordInspectionResult(1L, inspectionResult, 100L);
        
        // 验证结果
        assertTrue(result);
        // 在实际实现中，该方法会记录检验结果
    }
    
    @Test
    void testGetInspectionResults() {
        // 执行测试
        List<InspectionResult> result = stateMachineService.getInspectionResults(1L);
        
        // 验证结果
        assertNotNull(result);
        // 在实际实现中，该方法会查询数据库获取检验结果
    }
    
    @Test
    void testCanStartProcess() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.canStartProcess(1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanPauseProcess() {
        // 设置模拟数据
        testProcess.setCurrentStatus(2); // IN_PROGRESS
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.canPauseProcess(1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanResumeProcess() {
        // 设置模拟数据
        testProcess.setCurrentStatus(4); // PAUSED
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.canResumeProcess(1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanCompleteProcess() {
        // 设置模拟数据
        testProcess.setCurrentStatus(2); // IN_PROGRESS
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.canCompleteProcess(1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanCancelProcess() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        boolean result = stateMachineService.canCancelProcess(1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testGetCurrentProcessStatus() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        ProcessStateMachineService.ProcessStatusInfo result = stateMachineService.getCurrentProcessStatus(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCurrentState());
        verify(moldProcessMapper).selectById(1L);
    }
}
