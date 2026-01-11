package com.mold.digitalization.service.state.impl;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.service.state.StateTransition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 鐘舵€佹満实现绫诲崟鍏冩祴璇?
 * 
 * @author System
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
class StateMachineImplTest {
    
    @Mock
    private MoldProcessMapper moldProcessMapper;
    
    @Mock
    private ProcessStatusHistoryMapper processStatusHistoryMapper;
    
    @Mock
    private List<StateTransition> stateTransitions;
    
    @InjectMocks
    private StateMachineImpl stateMachine;
    
    private MoldProcess testProcess;
    private ProcessStatusHistory testHistory;
    private StateTransition testTransition;
    
    @BeforeEach
    void setUp() {
        // 创建测试鐢ㄧ殑娴佺▼瀵硅薄
        testProcess = new MoldProcess();
        testProcess.setId(1L);
        testProcess.setMoldId("MOLD001");
        testProcess.setCurrentStatus("PENDING");
        testProcess.setCreatedAt(LocalDateTime.now());
        
        // 创建测试鐢ㄧ殑鐘舵€佸巻鍙茶褰?
        testHistory = new ProcessStatusHistory();
        testHistory.setId(1L);
        testHistory.setProcessId(1L);
        testHistory.setFromStatus("PENDING");
        testHistory.setToStatus("PREPARING");
        testHistory.setChangedAt(LocalDateTime.now());
        testHistory.setChangedBy(1L);
        
        // 创建用于测试的状态转换
        testTransition = new StateTransitionImpl("PENDING", "PREPARING", "准备中");
    }
    
    @Test
    void testGetCurrentState() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        
        // 执行测试
        String currentState = stateMachine.getCurrentState(1L);
        
        // 验证结果
        assertEquals("PENDING", currentState);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testGetCurrentStateNotFound() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(null);
        
        // 执行测试
        String currentState = stateMachine.getCurrentState(1L);
        
        // 验证结果
        assertNull(currentState);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testGetPossibleTransitions() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(stateTransitions.stream()).thenReturn(Arrays.asList(testTransition).stream());
        
        // 执行测试
        List<String> transitions = stateMachine.getPossibleTransitions(1L);
        
        // 验证结果
        assertNotNull(transitions);
        assertEquals(1, transitions.size());
        assertEquals("PREPARING", transitions.get(0));
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanTransition() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(stateTransitions.stream()).thenReturn(Arrays.asList(testTransition).stream());
        
        // 执行测试
        boolean canTransition = stateMachine.canTransition(1L, "PREPARING");
        
        // 验证结果
        assertTrue(canTransition);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testCanTransitionNotFound() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(null);
        
        // 执行测试
        boolean canTransition = stateMachine.canTransition(1L, "PREPARING");
        
        // 验证结果
        assertFalse(canTransition);
        verify(moldProcessMapper).selectById(1L);
    }
    
    @Test
    void testTransitionState() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(stateTransitions.stream()).thenReturn(Arrays.asList(testTransition).stream());
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(processStatusHistoryMapper.insert(any(ProcessStatusHistory.class))).thenReturn(1);
        
        // 执行测试
        boolean result = stateMachine.transitionState(1L, "PREPARING", 1L);
        
        // 验证结果
        assertTrue(result);
        verify(moldProcessMapper).selectById(1L);
        verify(moldProcessMapper).updateById(any(MoldProcess.class));
        verify(processStatusHistoryMapper).insert(any(ProcessStatusHistory.class));
    }
    
    @Test
    void testTransitionStateNotFound() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(null);
        
        // 执行测试
        boolean result = stateMachine.transitionState(1L, "PREPARING", 1L);
        
        // 验证结果
        assertFalse(result);
        verify(moldProcessMapper).selectById(1L);
        verify(moldProcessMapper, never()).updateById(any(MoldProcess.class));
        verify(processStatusHistoryMapper, never()).insert(any(ProcessStatusHistory.class));
    }
    
    @Test
    void testTransitionStateInvalidTransition() {
        // 设置模拟数据
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(stateTransitions.stream()).thenReturn(Arrays.asList(testTransition).stream());
        
        // 执行测试
        boolean result = stateMachine.transitionState(1L, "COMPLETED", 1L);
        
        // 验证结果
        assertFalse(result);
        verify(moldProcessMapper).selectById(1L);
        verify(moldProcessMapper, never()).updateById(any(MoldProcess.class));
        verify(processStatusHistoryMapper, never()).insert(any(ProcessStatusHistory.class));
    }
    
    @Test
    void testGetStateHistory() {
        // 设置模拟数据
        List<ProcessStatusHistory> historyList = Arrays.asList(testHistory);
        when(processStatusHistoryMapper.selectByProcessId(1L)).thenReturn(historyList);
        
        // 执行测试
        List<ProcessStatusHistory> result = stateMachine.getStateHistory(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testHistory, result.get(0));
        verify(processStatusHistoryMapper).selectByProcessId(1L);
    }
}
