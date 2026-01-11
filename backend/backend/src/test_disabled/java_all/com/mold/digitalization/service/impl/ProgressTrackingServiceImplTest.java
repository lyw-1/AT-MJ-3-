package com.mold.digitalization.service.impl;

import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.service.ProgressTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@ExtendWith(MockitoExtension.class)
class ProgressTrackingServiceImplTest {

    @Mock
    private MoldProcessMapper moldProcessMapper;

    @Mock
    private ProcessStatusHistoryMapper processStatusHistoryMapper;

    @InjectMocks
    private ProgressTrackingServiceImpl progressTrackingService;

    private MoldProcess testMoldProcess;
    private ProcessStatusHistory testStatusHistory;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?        testMoldProcess = new MoldProcess();
        testMoldProcess.setId(1L);
        testMoldProcess.setProcessCode("PROC001");
        testMoldProcess.setMoldId(1L);
        testMoldProcess.setCurrentProgress(50);
        testMoldProcess.setCurrentStatus(1);
        testMoldProcess.setActualStartTime(LocalDateTime.now());
        testMoldProcess.setPlannedEndTime(LocalDateTime.now().plusHours(2));
        testMoldProcess.setActualEndTime(null);

        testStatusHistory = new ProcessStatusHistory();
        testStatusHistory.setId(1L);
        testStatusHistory.setProcessId(1L);
        testStatusHistory.setFromStatus(0);
        testStatusHistory.setToStatus(50);
        testStatusHistory.setOperatorId(1L);
        testStatusHistory.setDescription("杩涘害更新");
        testStatusHistory.setCreateTime(LocalDateTime.now());

        // 设置鐖剁被Mapper
        try {
            Field mapperField = progressTrackingService.getClass().getSuperclass().getDeclaredField("baseMapper");
            mapperField.setAccessible(true);
            mapperField.set(progressTrackingService, moldProcessMapper);
        } catch (Exception e) {
            fail("Failed to set baseMapper field: " + e.getMessage());
        }
    }

    @Test
    void getCurrentProgress_ShouldReturnProgress_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        Integer result = progressTrackingService.getCurrentProgress(processId);

        // Assert
        assertNotNull(result);
        assertEquals(50, result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void getCurrentProgress_ShouldReturnZero_WhenProcessNotExists() {
        // Arrange
        Long processId = 999L;
        when(moldProcessMapper.selectById(processId)).thenReturn(null);

        // Act
        Integer result = progressTrackingService.getCurrentProgress(processId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void updateProgress_ShouldUpdateProgress_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        Integer newProgress = 75;
        Long operatorId = 1L;
        
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);
        when(moldProcessMapper.updateById(any(MoldProcess.class))).thenReturn(1);
        when(processStatusHistoryMapper.insert(any(ProcessStatusHistory.class))).thenReturn(1);

        // Act
        boolean result = progressTrackingService.updateProgress(processId, newProgress, operatorId);

        // Assert
        assertTrue(result);
        verify(moldProcessMapper, times(1)).selectById(processId);
        verify(moldProcessMapper, times(1)).updateById(any(MoldProcess.class));
        verify(processStatusHistoryMapper, times(1)).insert(any(ProcessStatusHistory.class));
    }

    @Test
    void updateProgress_ShouldReturnFalse_WhenProcessNotExists() {
        // Arrange
        Long processId = 999L;
        Integer newProgress = 75;
        Long operatorId = 1L;
        
        when(moldProcessMapper.selectById(processId)).thenReturn(null);

        // Act
        boolean result = progressTrackingService.updateProgress(processId, newProgress, operatorId);

        // Assert
        assertFalse(result);
        verify(moldProcessMapper, times(1)).selectById(processId);
        verify(moldProcessMapper, never()).updateById(any(MoldProcess.class));
        verify(processStatusHistoryMapper, never()).insert(any(ProcessStatusHistory.class));
    }

    @Test
    void getProgressHistory_ShouldReturnHistoryList_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        List<ProcessStatusHistory> expectedHistory = Arrays.asList(testStatusHistory);
        when(processStatusHistoryMapper.selectList(any(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper.class))).thenReturn(expectedHistory);

        // Act
        List<ProcessStatusHistory> result = progressTrackingService.getProgressHistory(processId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(processId, result.get(0).getProcessId());
        verify(processStatusHistoryMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void getProgressHistory_ShouldReturnEmptyList_WhenNoHistoryExists() {
        // Arrange
        Long processId = 999L;
        when(processStatusHistoryMapper.selectList(any(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper.class))).thenReturn(Arrays.asList());

        // Act
        List<ProcessStatusHistory> result = progressTrackingService.getProgressHistory(processId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(processStatusHistoryMapper, times(1)).selectList(any(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper.class));
    }

    @Test
    void getEstimatedCompletionTime_ShouldReturnTime_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        LocalDateTime result = progressTrackingService.getEstimatedCompletionTime(processId);

        // Assert
        assertNotNull(result);
        assertEquals(testMoldProcess.getPlannedEndTime(), result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void getEstimatedCompletionTime_ShouldReturnNull_WhenProcessNotExists() {
        // Arrange
        Long processId = 999L;
        when(moldProcessMapper.selectById(processId)).thenReturn(null);

        // Act
        LocalDateTime result = progressTrackingService.getEstimatedCompletionTime(processId);

        // Assert
        assertNull(result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void calculateActualDuration_ShouldReturnDuration_WhenProcessCompleted() {
        // Arrange
        Long processId = 1L;
        testMoldProcess.setActualEndTime(LocalDateTime.now().plusHours(1));
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        Long result = progressTrackingService.calculateActualDuration(processId);

        // Assert
        assertNotNull(result);
        assertTrue(result > 0);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void calculateActualDuration_ShouldReturnZero_WhenProcessNotCompleted() {
        // Arrange
        Long processId = 1L;
        testMoldProcess.setActualEndTime(null);
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        Long result = progressTrackingService.calculateActualDuration(processId);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void isProcessTimeout_ShouldReturnTrue_WhenProcessTimeout() {
        // Arrange
        Long processId = 1L;
        testMoldProcess.setPlannedEndTime(LocalDateTime.now().minusHours(1));
        testMoldProcess.setActualEndTime(null);
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        boolean result = progressTrackingService.isProcessTimeout(processId);

        // Assert
        assertTrue(result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void isProcessTimeout_ShouldReturnFalse_WhenProcessNotTimeout() {
        // Arrange
        Long processId = 1L;
        testMoldProcess.setPlannedEndTime(LocalDateTime.now().plusHours(1));
        testMoldProcess.setActualEndTime(null);
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        boolean result = progressTrackingService.isProcessTimeout(processId);

        // Assert
        assertFalse(result);
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void getTimeoutProcesses_ShouldReturnTimeoutProcesses() {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(moldProcessMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedProcesses);

        // Act
        List<MoldProcess> result = progressTrackingService.getTimeoutProcesses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldProcessMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void recordProgressChange_ShouldRecordHistory_WhenValidInput() {
        // Arrange
        Long processId = 1L;
        Integer fromProgress = 50;
        Integer toProgress = 75;
        Long operatorId = 1L;
        String description = "杩涘害更新测试";
        
        when(processStatusHistoryMapper.insert(any(ProcessStatusHistory.class))).thenReturn(1);

        // Act
        boolean result = progressTrackingService.recordProgressChange(processId, fromProgress, toProgress, operatorId, description);

        // Assert
        assertTrue(result);
        verify(processStatusHistoryMapper, times(1)).insert(any(ProcessStatusHistory.class));
    }

    @Test
    void recordProgressChange_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        Long processId = 1L;
        Integer fromProgress = 50;
        Integer toProgress = 75;
        Long operatorId = 1L;
        String description = "杩涘害更新测试";
        
        when(processStatusHistoryMapper.insert(any(ProcessStatusHistory.class))).thenReturn(0);

        // Act
        boolean result = progressTrackingService.recordProgressChange(processId, fromProgress, toProgress, operatorId, description);

        // Assert
        assertFalse(result);
        verify(processStatusHistoryMapper, times(1)).insert(any(ProcessStatusHistory.class));
    }

    @Test
    void getProgressTrend_ShouldReturnTrendData_WhenValidInput() {
        // Arrange
        Long processId = 1L;
        Date startTime = new Date();
        Date endTime = new Date(System.currentTimeMillis() + 3600000);
        
        List<ProcessStatusHistory> histories = Arrays.asList(testStatusHistory);
        when(processStatusHistoryMapper.selectList(any(QueryWrapper.class))).thenReturn(histories);

        // Act
        ProgressTrackingService.ProgressTrend result = progressTrackingService.getProgressTrend(processId, startTime, endTime);

        // Assert
        assertNotNull(result);
        assertEquals(50, result.getProgress());
        verify(processStatusHistoryMapper, times(1)).selectList(any(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper.class));
    }

    @Test
    void getProgressSummary_ShouldReturnSummaryList() {
        // Arrange
        List<MoldProcess> processes = Arrays.asList(testMoldProcess);
        when(moldProcessMapper.selectList(any(QueryWrapper.class))).thenReturn(processes);

        // Act
        List<ProgressTrackingService.ProgressSummary> result = progressTrackingService.getProgressSummary();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldProcessMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void getDelayedProcesses_ShouldReturnDelayedProcesses() {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(moldProcessMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedProcesses);

        // Act
        List<MoldProcess> result = progressTrackingService.getDelayedProcesses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldProcessMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void getNormalProgressProcesses_ShouldReturnNormalProcesses() {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(moldProcessMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedProcesses);

        // Act
        List<MoldProcess> result = progressTrackingService.getNormalProgressProcesses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldProcessMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void getAbnormalProgressProcesses_ShouldReturnAbnormalProcesses() {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(moldProcessMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedProcesses);

        // Act
        List<MoldProcess> result = progressTrackingService.getAbnormalProgressProcesses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldProcessMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void getProgressPrediction_ShouldReturnPrediction_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        ProgressTrackingService.ProgressSummary expectedPrediction = new ProgressTrackingService.ProgressSummary();
        expectedPrediction.setProcessId(processId);
        expectedPrediction.setProcessCode("PROC001");
        expectedPrediction.setCurrentProgress(50);
        
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        ProgressTrackingService.ProgressSummary result = progressTrackingService.getProgressPrediction(processId);

        // Assert
        assertNotNull(result);
        assertEquals(processId, result.getProcessId());
        verify(moldProcessMapper, times(1)).selectById(processId);
    }

    @Test
    void getProgressAnalysis_ShouldReturnAnalysis_WhenProcessExists() {
        // Arrange
        Long processId = 1L;
        ProgressTrackingService.ProgressSummary expectedAnalysis = new ProgressTrackingService.ProgressSummary();
        expectedAnalysis.setProcessId(processId);
        expectedAnalysis.setProcessCode("PROC001");
        expectedAnalysis.setCurrentProgress(50);
        
        when(moldProcessMapper.selectById(processId)).thenReturn(testMoldProcess);

        // Act
        ProgressTrackingService.ProgressSummary result = progressTrackingService.getProgressAnalysis(processId);

        // Assert
        assertNotNull(result);
        assertEquals(processId, result.getProcessId());
        verify(moldProcessMapper, times(1)).selectById(processId);
    }
}