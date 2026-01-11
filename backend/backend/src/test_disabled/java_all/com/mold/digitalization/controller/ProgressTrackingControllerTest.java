package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.service.ProgressTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProgressTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressTrackingService progressTrackingService;

    @Autowired
    private ObjectMapper objectMapper;

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

        testStatusHistory = new ProcessStatusHistory();
        testStatusHistory.setId(1L);
        testStatusHistory.setProcessId(1L);
        testStatusHistory.setFromStatus(0);
        testStatusHistory.setToStatus(50);
        testStatusHistory.setOperatorId(1L);
        testStatusHistory.setDescription("杩涘害更新");
        testStatusHistory.setCreateTime(LocalDateTime.now());
    }

    @Test
    void getCurrentProgress_ShouldReturnProgress_WhenProcessExists() throws Exception {
        // Arrange
        Long processId = 1L;
        when(progressTrackingService.getCurrentProgress(processId)).thenReturn(50);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/current-progress", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(50))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getCurrentProgress_ShouldReturnZero_WhenProcessNotExists() throws Exception {
        // Arrange
        Long processId = 999L;
        when(progressTrackingService.getCurrentProgress(processId)).thenReturn(0);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/current-progress", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(0))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void updateProgress_ShouldReturnSuccess_WhenValidInput() throws Exception {
        // Arrange
        Long processId = 1L;
        Integer newProgress = 75;
        Long operatorId = 1L;
        
        when(progressTrackingService.updateProgress(processId, newProgress, operatorId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("progress", newProgress.toString())
                        .param("operatorId", operatorId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void updateProgress_ShouldReturnFalse_WhenProcessNotExists() throws Exception {
        // Arrange
        Long processId = 999L;
        Integer newProgress = 75;
        Long operatorId = 1L;
        
        when(progressTrackingService.updateProgress(processId, newProgress, operatorId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("progress", newProgress.toString())
                        .param("operatorId", operatorId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressHistory_ShouldReturnHistoryList_WhenProcessExists() throws Exception {
        // Arrange
        Long processId = 1L;
        List<ProcessStatusHistory> expectedHistory = Arrays.asList(testStatusHistory);
        when(progressTrackingService.getProgressHistory(processId)).thenReturn(expectedHistory);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/history", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processId").value(1))
                .andExpect(jsonPath("$.data[0].fromStatus").value(0))
                .andExpect(jsonPath("$.data[0].toStatus").value(50))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressHistory_ShouldReturnEmptyList_WhenNoHistoryExists() throws Exception {
        // Arrange
        Long processId = 999L;
        when(progressTrackingService.getProgressHistory(processId)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/history", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getEstimatedCompletionTime_ShouldReturnTime_WhenProcessExists() throws Exception {
        // Arrange
        Long processId = 1L;
        LocalDateTime expectedTime = LocalDateTime.now().plusHours(2);
        when(progressTrackingService.getEstimatedCompletionTime(processId)).thenReturn(expectedTime);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/estimated-completion-time", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getEstimatedCompletionTime_ShouldReturnNull_WhenProcessNotExists() throws Exception {
        // Arrange
        Long processId = 999L;
        when(progressTrackingService.getEstimatedCompletionTime(processId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/estimated-completion-time", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void calculateActualDuration_ShouldReturnDuration_WhenProcessCompleted() throws Exception {
        // Arrange
        Long processId = 1L;
        Long expectedDuration = 3600L;
        when(progressTrackingService.calculateActualDuration(processId)).thenReturn(expectedDuration);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/actual-duration", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(3600))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void calculateActualDuration_ShouldReturnZero_WhenProcessNotCompleted() throws Exception {
        // Arrange
        Long processId = 1L;
        when(progressTrackingService.calculateActualDuration(processId)).thenReturn(0L);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/actual-duration", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(0))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void isProcessTimeout_ShouldReturnTrue_WhenProcessTimeout() throws Exception {
        // Arrange
        Long processId = 1L;
        when(progressTrackingService.isProcessTimeout(processId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/timeout", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void isProcessTimeout_ShouldReturnFalse_WhenProcessNotTimeout() throws Exception {
        // Arrange
        Long processId = 1L;
        when(progressTrackingService.isProcessTimeout(processId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/timeout", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getTimeoutProcesses_ShouldReturnTimeoutProcesses() throws Exception {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(progressTrackingService.getTimeoutProcesses()).thenReturn(expectedProcesses);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/timeout-processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getDelayedProcesses_ShouldReturnDelayedProcesses() throws Exception {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(progressTrackingService.getDelayedProcesses()).thenReturn(expectedProcesses);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/delayed-processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getNormalProgressProcesses_ShouldReturnNormalProcesses() throws Exception {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(progressTrackingService.getNormalProgressProcesses()).thenReturn(expectedProcesses);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/normal-progress-processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getAbnormalProgressProcesses_ShouldReturnAbnormalProcesses() throws Exception {
        // Arrange
        List<MoldProcess> expectedProcesses = Arrays.asList(testMoldProcess);
        when(progressTrackingService.getAbnormalProgressProcesses()).thenReturn(expectedProcesses);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/abnormal-progress-processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressSummary_ShouldReturnSummaryList() throws Exception {
        // Arrange
        ProgressTrackingService.ProgressSummary summary = new ProgressTrackingService.ProgressSummary();
        summary.setProcessId(1L);
        summary.setProcessCode("PROC001");
        summary.setCurrentProgress(50);
        summary.setStatus(1);
        summary.setStatusName("进行中");
        
        List<ProgressTrackingService.ProgressSummary> expectedSummaries = Arrays.asList(summary);
        when(progressTrackingService.getProgressSummary()).thenReturn(expectedSummaries);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/progress-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].processId").value(1))
                .andExpect(jsonPath("$.data[0].processCode").value("PROC001"))
                .andExpect(jsonPath("$.data[0].currentProgress").value(50))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressTrend_ShouldReturnTrendData() throws Exception {
        // Arrange
        Long processId = 1L;
        Date startTime = new Date();
        Date endTime = new Date(System.currentTimeMillis() + 3600000);
        
        ProgressTrackingService.ProgressTrend expectedTrend = new ProgressTrackingService.ProgressTrend();
        expectedTrend.setProgress(50);
        expectedTrend.setStatus("进行中");
        expectedTrend.setOperator("操作员");
        
        when(progressTrackingService.getProgressTrend(processId, startTime, endTime)).thenReturn(expectedTrend);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/progress-trend", processId)
                        .param("startTime", String.valueOf(startTime.getTime()))
                        .param("endTime", String.valueOf(endTime.getTime())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.progress").value(50))
                .andExpect(jsonPath("$.data.status").value("进行中"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressPrediction_ShouldReturnPrediction() throws Exception {
        // Arrange
        Long processId = 1L;
        ProgressTrackingService.ProgressSummary expectedPrediction = new ProgressTrackingService.ProgressSummary();
        expectedPrediction.setProcessId(processId);
        expectedPrediction.setProcessCode("PROC001");
        expectedPrediction.setCurrentProgress(50);
        
        when(progressTrackingService.getProgressPrediction(processId)).thenReturn(expectedPrediction);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/progress-prediction", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.processId").value(1))
                .andExpect(jsonPath("$.data.processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void getProgressAnalysis_ShouldReturnAnalysis() throws Exception {
        // Arrange
        Long processId = 1L;
        ProgressTrackingService.ProgressSummary expectedAnalysis = new ProgressTrackingService.ProgressSummary();
        expectedAnalysis.setProcessId(processId);
        expectedAnalysis.setProcessCode("PROC001");
        expectedAnalysis.setCurrentProgress(50);
        
        when(progressTrackingService.getProgressAnalysis(processId)).thenReturn(expectedAnalysis);

        // Act & Assert
        mockMvc.perform(get("/api/progress-tracking/{processId}/progress-analysis", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.processId").value(1))
                .andExpect(jsonPath("$.data.processCode").value("PROC001"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void updateProgress_ShouldReturnBadRequest_WhenInvalidProgress() throws Exception {
        // Arrange
        Long processId = 1L;
        Integer invalidProgress = 150; // 瓒呰繃100鐨勬棤鏁堣繘搴?        Long operatorId = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("progress", invalidProgress.toString())
                        .param("operatorId", operatorId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProgress_ShouldReturnBadRequest_WhenNegativeProgress() throws Exception {
        // Arrange
        Long processId = 1L;
        Integer invalidProgress = -10; // 负数的无效进度
        Long operatorId = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("progress", invalidProgress.toString())
                        .param("operatorId", operatorId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProgress_ShouldReturnBadRequest_WhenMissingRequiredParameters() throws Exception {
        // Arrange
        Long processId = 1L;

        // Act & Assert - 缺少progress参数
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("operatorId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Act & Assert - 缺少operatorId参数
        mockMvc.perform(put("/api/progress-tracking/{processId}/progress", processId)
                        .param("progress", "50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}