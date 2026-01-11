package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.enums.ProcessStatus;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.service.state.ProcessStateMachineService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 妯″叿鍔犲伐娴佺▼服务实现绫诲崟鍏冩祴璇? * 测试娴佺▼绠＄悊鐨勬牳蹇冧笟鍔￠€昏緫
 */
@ExtendWith(MockitoExtension.class)
class MoldProcessServiceImplTest {

    @Mock
    private MoldProcessMapper moldProcessMapper;
    
    @Mock
    private ProcessStatusHistoryMapper statusHistoryMapper;
    
    @Mock
    private ProcessStateMachineService stateMachineService;
    
    @InjectMocks
    private MoldProcessServiceImpl moldProcessService;

    private MoldProcess testProcess;
    private ProcessStatusHistory testHistory;
    private Page<MoldProcess> testPage;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?        testProcess = new MoldProcess();
        testProcess.setId(1L);
        testProcess.setMoldId("M001");
        testProcess.setProcessType("CNC");
        testProcess.setEquipmentId("EQ001");
        testProcess.setOperatorId("OP001");
        testProcess.setCurrentStatus(ProcessStatus.CREATED);
        testProcess.setPriority(1);
        testProcess.setEstimatedDuration(120);
        testProcess.setActualDuration(0);
        testProcess.setNotes("测试娴佺▼");
        testProcess.setCreateTime(LocalDateTime.now());
        testProcess.setUpdateTime(LocalDateTime.now());

        testHistory = new ProcessStatusHistory();
        testHistory.setId(1L);
        testHistory.setProcessId(1L);
        testHistory.setFromStatus(ProcessStatus.CREATED);
        testHistory.setToStatus(ProcessStatus.IN_PROGRESS);
        testHistory.setChangeReason("开始加工");
        testHistory.setOperatorId("OP001");
        testHistory.setChangeTime(LocalDateTime.now());

        // 初始化分页测试数据
        testPage = new Page<>();
        testPage.add(testProcess);
        testPage.setTotal(1);
    }

    @Test
    void testCreateProcess() {
        // 准备测试数据
        MoldProcess newProcess = new MoldProcess();
        newProcess.setMoldId("M002");
        newProcess.setProcessType("EDM");
        newProcess.setEquipmentId("EQ002");
        newProcess.setOperatorId("OP002");
        newProcess.setPriority(2);
        newProcess.setEstimatedDuration(90);
        newProcess.setNotes("新测试流程");

        // 模拟Mapper琛屼负
        when(moldProcessMapper.insert(any(MoldProcess.class))).thenReturn(1);
        when(moldProcessMapper.selectById(anyLong())).thenReturn(newProcess);

        // 执行测试
        MoldProcess result = moldProcessService.createProcess(newProcess);

        // 验证结果
        assertNotNull(result);
        assertEquals("M002", result.getMoldId());
        assertEquals("EDM", result.getProcessType());
        assertEquals(ProcessStatus.CREATED, result.getCurrentStatus());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).insert(newProcess);
        verify(stateMachineService).createStateMachine(anyLong(), eq(ProcessStatus.CREATED));
    }

    @Test
    void testGetProcessById() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);

        // 执行测试
        MoldProcess result = moldProcessService.getProcessById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("M001", result.getMoldId());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
    }

    @Test
    void testUpdateProcess() {
        // 准备更新数据
        testProcess.setNotes("更新鍚庣殑娴佺▼");
        testProcess.setActualDuration(60);

        // 模拟Mapper琛屼负
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);

        // 执行测试
        MoldProcess result = moldProcessService.updateProcess(testProcess);

        // 验证结果
        assertNotNull(result);
        assertEquals("更新鍚庣殑娴佺▼", result.getNotes());
        assertEquals(60, result.getActualDuration());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testDeleteProcess() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.deleteById(1L)).thenReturn(1);
        when(statusHistoryMapper.deleteByProcessId(1L)).thenReturn(1);

        // 执行测试
        boolean result = moldProcessService.deleteProcess(1L);

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).deleteById(1L);
        verify(statusHistoryMapper).deleteByProcessId(1L);
        verify(stateMachineService).removeStateMachine(1L);
    }

    @Test
    void testGetProcessesByPage() {
        // 模拟鍒嗛〉查询
        try (var ignored = PageHelper.startPage(1, 10)) {
            when(moldProcessMapper.selectAll()).thenReturn(Arrays.asList(testProcess));
        }

        // 执行测试
        PageInfo<MoldProcess> result = moldProcessService.getProcessesByPage(1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("M001", result.getList().get(0).getMoldId());
    }

    @Test
    void testGetProcessesByMoldId() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByMoldId("M001")).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByMoldId("M001");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("M001", result.get(0).getMoldId());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByMoldId("M001");
    }

    @Test
    void testGetProcessesByEquipmentId() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByEquipmentId("EQ001")).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByEquipmentId("EQ001");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EQ001", result.get(0).getEquipmentId());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByEquipmentId("EQ001");
    }

    @Test
    void testGetProcessesByOperatorId() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByOperatorId("OP001")).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByOperatorId("OP001");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("OP001", result.get(0).getOperatorId());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByOperatorId("OP001");
    }

    @Test
    void testGetProcessStatusStatistics() {
        // 准备测试数据
        List<MoldProcess> processes = Arrays.asList(testProcess);
        
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectAll()).thenReturn(processes);

        // 执行测试
        Map<String, Long> result = moldProcessService.getProcessStatusStatistics();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.get("CREATED"));
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectAll();
    }

    @Test
    void testStartProcess() {
        // 模拟Mapper鍜孲tateMachine琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(stateMachineService.canTransition(1L, ProcessStatus.IN_PROGRESS)).thenReturn(true);
        when(stateMachineService.transitionState(1L, ProcessStatus.IN_PROGRESS, "开始加工", "OP001")).thenReturn(true);

        // 执行测试
        boolean result = moldProcessService.startProcess(1L, "OP001");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
        verify(stateMachineService).canTransition(1L, ProcessStatus.IN_PROGRESS);
        verify(stateMachineService).transitionState(1L, ProcessStatus.IN_PROGRESS, "开始加工", "OP001");
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testPauseProcess() {
        // 设置流程状态为进行中
        testProcess.setCurrentStatus(ProcessStatus.IN_PROGRESS);
        
        // 模拟Mapper鍜孲tateMachine琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(stateMachineService.canTransition(1L, ProcessStatus.PAUSED)).thenReturn(true);
        when(stateMachineService.transitionState(1L, ProcessStatus.PAUSED, "鏆傚仠鍔犲伐", "OP001")).thenReturn(true);

        // 执行测试
        boolean result = moldProcessService.pauseProcess(1L, "OP001");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
        verify(stateMachineService).canTransition(1L, ProcessStatus.PAUSED);
        verify(stateMachineService).transitionState(1L, ProcessStatus.PAUSED, "鏆傚仠鍔犲伐", "OP001");
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testResumeProcess() {
        // 设置娴佺▼鐘舵€佷负鏆傚仠
        testProcess.setCurrentStatus(ProcessStatus.PAUSED);
        
        // 模拟Mapper鍜孲tateMachine琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(stateMachineService.canTransition(1L, ProcessStatus.IN_PROGRESS)).thenReturn(true);
        when(stateMachineService.transitionState(1L, ProcessStatus.IN_PROGRESS, "鎭㈠鍔犲伐", "OP001")).thenReturn(true);

        // 执行测试
        boolean result = moldProcessService.resumeProcess(1L, "OP001");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
        verify(stateMachineService).canTransition(1L, ProcessStatus.IN_PROGRESS);
        verify(stateMachineService).transitionState(1L, ProcessStatus.IN_PROGRESS, "鎭㈠鍔犲伐", "OP001");
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testCompleteProcess() {
        // 设置娴佺▼鐘舵€佷负杩涜涓?        testProcess.setCurrentStatus(ProcessStatus.IN_PROGRESS);
        
        // 模拟Mapper鍜孲tateMachine琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(stateMachineService.canTransition(1L, ProcessStatus.COMPLETED)).thenReturn(true);
        when(stateMachineService.transitionState(1L, ProcessStatus.COMPLETED, "瀹屾垚鍔犲伐", "OP001")).thenReturn(true);

        // 执行测试
        boolean result = moldProcessService.completeProcess(1L, "OP001");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
        verify(stateMachineService).canTransition(1L, ProcessStatus.COMPLETED);
        verify(stateMachineService).transitionState(1L, ProcessStatus.COMPLETED, "瀹屾垚鍔犲伐", "OP001");
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testCancelProcess() {
        // 模拟Mapper鍜孲tateMachine琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(stateMachineService.canTransition(1L, ProcessStatus.CANCELLED)).thenReturn(true);
        when(stateMachineService.transitionState(1L, ProcessStatus.CANCELLED, "鍙栨秷鍔犲伐", "OP001")).thenReturn(true);

        // 执行测试
        boolean result = moldProcessService.cancelProcess(1L, "OP001");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
        verify(stateMachineService).canTransition(1L, ProcessStatus.CANCELLED);
        verify(stateMachineService).transitionState(1L, ProcessStatus.CANCELLED, "鍙栨秷鍔犲伐", "OP001");
        verify(moldProcessMapper).updateById(testProcess);
    }

    @Test
    void testGetProcessesByStatus() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByStatus(ProcessStatus.CREATED)).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByStatus(ProcessStatus.CREATED);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ProcessStatus.CREATED, result.get(0).getCurrentStatus());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByStatus(ProcessStatus.CREATED);
    }

    @Test
    void testGetProcessesByTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByTimeRange(startTime, endTime)).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByTimeRange(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByTimeRange(startTime, endTime);
    }

    @Test
    void testGetProcessHistory() {
        // 模拟Mapper琛屼负
        when(statusHistoryMapper.selectByProcessId(1L)).thenReturn(Arrays.asList(testHistory));

        // 执行测试
        List<ProcessStatusHistory> result = moldProcessService.getProcessHistory(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProcessId());
        
        // 验证方法璋冪敤
        verify(statusHistoryMapper).selectByProcessId(1L);
    }

    @Test
    void testGetProcessDuration() {
        // 设置娴佺▼鏃堕棿
        testProcess.setStartTime(LocalDateTime.now().minusHours(2));
        testProcess.setEndTime(LocalDateTime.now());
        
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectById(1L)).thenReturn(testProcess);

        // 执行测试
        Long result = moldProcessService.getProcessDuration(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result > 0);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectById(1L);
    }

    @Test
    void testGetOverdueProcesses() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectOverdueProcesses()).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getOverdueProcesses();

        // 验证结果
        assertNotNull(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectOverdueProcesses();
    }

    @Test
    void testUpdateProcessPriority() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.updatePriority(1L, 3)).thenReturn(1);

        // 执行测试
        boolean result = moldProcessService.updateProcessPriority(1L, 3);

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).updatePriority(1L, 3);
    }

    @Test
    void testAssignOperator() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.updateOperator(1L, "OP002")).thenReturn(1);

        // 执行测试
        boolean result = moldProcessService.assignOperator(1L, "OP002");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).updateOperator(1L, "OP002");
    }

    @Test
    void testUpdateProcessNotes() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.updateNotes(1L, "更新澶囨敞")).thenReturn(1);

        // 执行测试
        boolean result = moldProcessService.updateProcessNotes(1L, "更新澶囨敞");

        // 验证结果
        assertTrue(result);
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).updateNotes(1L, "更新澶囨敞");
    }

    @Test
    void testGetProcessStatistics() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByStatus(ProcessStatus.CREATED)).thenReturn(Arrays.asList(testProcess));
        when(moldProcessMapper.selectByStatus(ProcessStatus.IN_PROGRESS)).thenReturn(Arrays.asList());
        when(moldProcessMapper.selectByStatus(ProcessStatus.COMPLETED)).thenReturn(Arrays.asList());
        when(moldProcessMapper.selectByStatus(ProcessStatus.CANCELLED)).thenReturn(Arrays.asList());
        when(moldProcessMapper.selectAll()).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        Map<String, Object> result = moldProcessService.getProcessStatistics();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("total"));
        assertTrue(result.containsKey("byStatus"));
        assertTrue(result.containsKey("completionRate"));
        
        // 验证方法璋冪敤
        verify(moldProcessMapper, atLeastOnce()).selectByStatus(any(ProcessStatus.class));
        verify(moldProcessMapper).selectAll();
    }

    @Test
    void testGetProcessesByPriority() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByPriority(1)).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByPriority(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPriority());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByPriority(1);
    }

    @Test
    void testGetProcessesByType() {
        // 模拟Mapper琛屼负
        when(moldProcessMapper.selectByProcessType("CNC")).thenReturn(Arrays.asList(testProcess));

        // 执行测试
        List<MoldProcess> result = moldProcessService.getProcessesByType("CNC");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CNC", result.get(0).getProcessType());
        
        // 验证方法璋冪敤
        verify(moldProcessMapper).selectByProcessType("CNC");
    }
}
