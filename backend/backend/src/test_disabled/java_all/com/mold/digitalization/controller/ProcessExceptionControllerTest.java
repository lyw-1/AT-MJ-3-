package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.service.ProcessExceptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessExceptionService processExceptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProcessException testException;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?        testException = new ProcessException();
        testException.setId(1L);
        testException.setProcessId(1001L);
        testException.setExceptionType(1); // 璁惧鏁呴殰
        testException.setSeverityLevel(2);
        testException.setExceptionDescription("璁惧绐佺劧鍋滄満");
        testException.setHandlingStatus(0); // 鏈鐞?        testException.setCreateTime(LocalDateTime.now());
        testException.setUpdateTime(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionById_ShouldReturnException_WhenExists() throws Exception {
        // Arrange
        Long exceptionId = 1L;
        when(processExceptionService.getById(exceptionId)).thenReturn(testException);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/{id}", exceptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.processId").value(1001))
                .andExpect(jsonPath("$.data.exceptionType").value(1))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Arrange
        Long exceptionId = 999L;
        when(processExceptionService.getById(exceptionId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/{id}", exceptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("异常记录不存在"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionsByProcessId_ShouldReturnExceptionList() throws Exception {
        // Arrange
        Long processId = 1L;
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getExceptionsByProcessId(processId)).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].processId").value(1001))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionsByType_ShouldReturnExceptionList() throws Exception {
        // Arrange
        Integer exceptionType = 1; // 璁惧鏁呴殰
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getExceptionsByExceptionType(exceptionType)).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/type/{type}", exceptionType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].exceptionType").value(1))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionsBySeverity_ShouldReturnExceptionList() throws Exception {
        // Arrange
        Integer severity = 2;
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getExceptionsBySeverity(severity)).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/severity/{severity}", severity))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].severityLevel").value(2))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionsByStatus_ShouldReturnExceptionList() throws Exception {
        // Arrange
        Integer status = 0;
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getExceptionsByHandlingStatus(status)).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].handlingStatus").value(0))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void createException_ShouldReturnSuccess_WhenValidInput() throws Exception {
        // Arrange
        when(processExceptionService.createProcessException(any(ProcessException.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/process-exceptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testException)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void updateException_ShouldReturnSuccess_WhenValidInput() throws Exception {
        // Arrange
        Long exceptionId = 1L;
        when(processExceptionService.updateProcessException(any(ProcessException.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/process-exceptions/{id}", exceptionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testException)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deleteException_ShouldReturnSuccess_WhenExists() throws Exception {
        // Arrange
        Long exceptionId = 1L;
        when(processExceptionService.deleteProcessException(exceptionId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/process-exceptions/{id}", exceptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deleteException_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Arrange
        Long exceptionId = 999L;
        when(processExceptionService.deleteProcessException(exceptionId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/process-exceptions/{id}", exceptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("异常记录不存在"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void handleException_ShouldReturnSuccess_WhenValidInput() throws Exception {
        // Arrange
        Long exceptionId = 1L;
        String handlingResult = "閲嶅惎璁惧";
        Long handlerId = 1L;
        
        when(processExceptionService.handleException(exceptionId, handlerId, handlingResult)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/process-exceptions/{id}/handle", exceptionId)
                        .param("handlingResult", handlingResult)
                        .param("handlerId", handlerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void handleException_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Arrange
        Long exceptionId = 999L;
        String handlingResult = "閲嶅惎璁惧";
        Long handlerId = 1L;
        
        when(processExceptionService.handleException(exceptionId, handlerId, handlingResult)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/process-exceptions/{id}/handle", exceptionId)
                        .param("handlingResult", handlingResult)
                        .param("handlerId", handlerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("告警记录不存在"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getHighSeverityExceptions_ShouldReturnExceptionList() throws Exception {
        // Arrange
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getHighSeverityExceptions()).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/high-severity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].severityLevel").value(2))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getUnhandledExceptions_ShouldReturnExceptionList() throws Exception {
        // Arrange
        List<ProcessException> expectedExceptions = Arrays.asList(testException);
        when(processExceptionService.getUnhandledExceptions()).thenReturn(expectedExceptions);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/unhandled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].handlingStatus").value(0))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getExceptionStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        com.mold.digitalization.entity.ExceptionStatistics statistics = new com.mold.digitalization.entity.ExceptionStatistics();
        statistics.setTotalExceptions(10);
        statistics.setUnhandledCount(3);
        statistics.setSeriousCount(2);
        
        when(processExceptionService.getExceptionStatistics()).thenReturn(statistics);

        // Act & Assert
        mockMvc.perform(get("/api/process-exceptions/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalExceptions").value(10))
                .andExpect(jsonPath("$.data.unhandledCount").value(3))
                .andExpect(jsonPath("$.data.seriousCount").value(2))
                .andExpect(jsonPath("$.message").value("success"));
    }



    @Test
    @WithMockUser(roles = {"USER"})
    void createException_ShouldReturnBadRequest_WhenMissingRequiredFields() throws Exception {
        // Arrange
        ProcessException invalidException = new ProcessException();
        invalidException.setId(1L);
        // 缂哄皯processId绛夊繀濉瓧娈?
        // Act & Assert
        mockMvc.perform(post("/api/process-exceptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidException)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("创建异常记录失败"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void updateException_ShouldReturnBadRequest_WhenIdMismatch() throws Exception {
        // Arrange
        Long exceptionId = 2L; // URL涓殑ID涓庤姹備綋涓殑ID涓嶅尮閰?        testException.setId(1L);

        // Act & Assert
        mockMvc.perform(put("/api/process-exceptions/{id}", exceptionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testException)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("异常记录不存在"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void handleException_ShouldReturnBadRequest_WhenMissingRequiredParameters() throws Exception {
        // Arrange
        Long exceptionId = 1L;

        // Act & Assert - 缂哄皯handlingResult鍙傛暟
        mockMvc.perform(put("/api/process-exceptions/{id}/handle", exceptionId)
                        .param("handlerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));

        // Act & Assert - 缂哄皯handlerId鍙傛暟
        mockMvc.perform(put("/api/process-exceptions/{id}/handle", exceptionId)
                        .param("handlingResult", "閲嶅惎璁惧"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }


}
