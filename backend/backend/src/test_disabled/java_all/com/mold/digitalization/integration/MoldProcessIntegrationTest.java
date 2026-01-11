package com.mold.digitalization.integration;

import com.mold.digitalization.Application;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.entity.ProgressTracking;
import com.mold.digitalization.enums.ProcessStatus;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.mapper.ProgressTrackingMapper;
import com.mold.digitalization.service.MoldProcessService;
import com.mold.digitalization.service.ProgressTrackingService;
import com.mold.digitalization.service.StateMachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 妯″叿鍔犲伐娴佺▼闆嗘垚测试
 * 测试鏁翠釜娴佺▼鐨勭鍒扮功能
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class MoldProcessIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MoldProcessService moldProcessService;

    @Autowired
    private ProgressTrackingService progressTrackingService;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private MoldProcessMapper moldProcessMapper;

    @Autowired
    private ProcessStatusHistoryMapper statusHistoryMapper;

    @Autowired
    private ProgressTrackingMapper progressTrackingMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MoldProcess testProcess;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // 鍒濆鍖栨祴璇曟暟鎹?        testProcess = new MoldProcess();
        testProcess.setMoldId("M001");
        testProcess.setProcessType("CNC");
        testProcess.setEquipmentId("EQ001");
        testProcess.setOperatorId("OP001");
        testProcess.setPriority(1);
        testProcess.setEstimatedDuration(120);
        testProcess.setNotes("闆嗘垚测试娴佺▼");
    }

    @Test
    void testCompleteProcessFlow() throws Exception {
        // 1. 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moldId", is("M001")))
                .andExpect(jsonPath("$.processType", is("CNC")))
                .andExpect(jsonPath("$.currentStatus", is("CREATED")))
                .andReturn();
        
        // 获取创建鐨勬祦绋婭D
        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();
        
        // 验证流程已创建
        assertNotNull(processId);
        
        // 2. 创建杩涘害璺熻釜记录
        ProgressTracking progress = new ProgressTracking();
        progress.setProcessId(processId);
        progress.setCurrentStep(1);
        progress.setTotalSteps(10);
        progress.setProgressPercentage(10.0);
        progress.setCurrentOperation("瑁呭す");
        progress.setNextOperation("绮楀姞宸?");
        
        String progressJson = objectMapper.writeValueAsString(progress);
        
        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(progressJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                .andExpect(jsonPath("$.currentStep", is(1)))
                .andExpect(jsonPath("$.progressPercentage", is(10.0)));
        
        // 3. 开始流程
        mockMvc.perform(post("/api/processes/{id}/start", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证娴佺▼鐘舵€佸凡更新
        MoldProcess startedProcess = moldProcessService.getProcessById(processId);
        assertEquals(ProcessStatus.IN_PROGRESS, startedProcess.getCurrentStatus());
        assertNotNull(startedProcess.getStartTime());
        
        // 4. 更新杩涘害
        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "30.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证杩涘害宸叉洿鏂?        ProgressTracking updatedProgress = progressTrackingService.getProgressByProcessId(processId);
        assertEquals(3, updatedProgress.getCurrentStep());
        assertEquals(30.0, updatedProgress.getProgressPercentage());
        
        // 5. 鏆傚仠娴佺▼
        mockMvc.perform(post("/api/processes/{id}/pause", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证娴佺▼鐘舵€佸凡更新
        MoldProcess pausedProcess = moldProcessService.getProcessById(processId);
        assertEquals(ProcessStatus.PAUSED, pausedProcess.getCurrentStatus());
        
        // 6. 鎭㈠娴佺▼
        mockMvc.perform(post("/api/processes/{id}/resume", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证娴佺▼鐘舵€佸凡更新
        MoldProcess resumedProcess = moldProcessService.getProcessById(processId);
        assertEquals(ProcessStatus.IN_PROGRESS, resumedProcess.getCurrentStatus());
        
        // 7. 瀹屾垚娴佺▼
        mockMvc.perform(post("/api/processes/{id}/complete", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证娴佺▼鐘舵€佸凡更新
        MoldProcess completedProcess = moldProcessService.getProcessById(processId);
        assertEquals(ProcessStatus.COMPLETED, completedProcess.getCurrentStatus());
        assertNotNull(completedProcess.getEndTime());
        assertTrue(completedProcess.getActualDuration() > 0);
        
        // 8. 验证鐘舵€佸巻鍙茶褰?        List<ProcessStatusHistory> history = moldProcessService.getProcessHistory(processId);
        assertTrue(history.size() >= 3); // CREATED -> IN_PROGRESS -> PAUSED -> IN_PROGRESS -> COMPLETED
        
        // 验证鐘舵€佽浆鎹㈣褰?        boolean hasCreatedToInProgress = false;
        boolean hasInProgressToPaused = false;
        boolean hasPausedToInProgress = false;
        boolean hasInProgressToCompleted = false;
        
        for (ProcessStatusHistory record : history) {
            if (record.getFromStatus() == ProcessStatus.CREATED && 
                record.getToStatus() == ProcessStatus.IN_PROGRESS) {
                hasCreatedToInProgress = true;
            } else if (record.getFromStatus() == ProcessStatus.IN_PROGRESS && 
                       record.getToStatus() == ProcessStatus.PAUSED) {
                hasInProgressToPaused = true;
            } else if (record.getFromStatus() == ProcessStatus.PAUSED && 
                       record.getToStatus() == ProcessStatus.IN_PROGRESS) {
                hasPausedToInProgress = true;
            } else if (record.getFromStatus() == ProcessStatus.IN_PROGRESS && 
                       record.getToStatus() == ProcessStatus.COMPLETED) {
                hasInProgressToCompleted = true;
            }
        }
        
        assertTrue(hasCreatedToInProgress);
        assertTrue(hasInProgressToPaused);
        assertTrue(hasPausedToInProgress);
        assertTrue(hasInProgressToCompleted);
    }

    @Test
    void testCancelProcessFlow() throws Exception {
        // 1. 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andReturn();
        
        // 获取创建鐨勬祦绋婭D
        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();
        
        // 2. 开始流程
        mockMvc.perform(post("/api/processes/{id}/start", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 3. 鍙栨秷娴佺▼
        mockMvc.perform(post("/api/processes/{id}/cancel", processId)
                .param("operatorId", "OP001")
                .param("reason", "瀹㈡埛鍙栨秷璁㈠崟"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证娴佺▼鐘舵€佸凡更新
        MoldProcess cancelledProcess = moldProcessService.getProcessById(processId);
        assertEquals(ProcessStatus.CANCELLED, cancelledProcess.getCurrentStatus());
        
        // 验证鐘舵€佸巻鍙茶褰?        List<ProcessStatusHistory> history = moldProcessService.getProcessHistory(processId);
        assertTrue(history.size() >= 2); // CREATED -> IN_PROGRESS -> CANCELLED
        
        // 验证鐘舵€佽浆鎹㈣褰?        boolean hasCreatedToInProgress = false;
        boolean hasInProgressToCancelled = false;
        
        for (ProcessStatusHistory record : history) {
            if (record.getFromStatus() == ProcessStatus.CREATED && 
                record.getToStatus() == ProcessStatus.IN_PROGRESS) {
                hasCreatedToInProgress = true;
            } else if (record.getFromStatus() == ProcessStatus.IN_PROGRESS && 
                       record.getToStatus() == ProcessStatus.CANCELLED) {
                hasInProgressToCancelled = true;
            }
        }
        
        assertTrue(hasCreatedToInProgress);
        assertTrue(hasInProgressToCancelled);
    }

    @Test
    void testProcessQuery() throws Exception {
        // 1. 创建澶氫釜娴佺▼
        for (int i = 0; i < 5; i++) {
            MoldProcess process = new MoldProcess();
            process.setMoldId("M00" + i);
            process.setProcessType("CNC");
            process.setEquipmentId("EQ00" + i);
            process.setOperatorId("OP00" + i);
            process.setPriority(i + 1);
            process.setEstimatedDuration(120 + i * 10);
            process.setNotes("测试娴佺▼ " + i);
            
            String processJson = objectMapper.writeValueAsString(process);
            
            mockMvc.perform(post("/api/processes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(processJson))
                    .andExpect(status().isOk());
        }
        
        // 2. 查询所有流程
        mockMvc.perform(get("/api/processes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$.list", hasSize(5)));
        
        // 3. 鍒嗛〉查询
        mockMvc.perform(get("/api/processes")
                .param("page", "1")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$.list", hasSize(2)));
        
        // 4. 按状态查询
        mockMvc.perform(get("/api/processes")
                .param("status", "CREATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$.list", hasSize(5)));
        
        // 5. 鎸夋ā鍏稩D查询
        mockMvc.perform(get("/api/processes")
                .param("moldId", "M001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.list[0].moldId", is("M001")));
        
        // 6. 鎸夎澶嘔D查询
        mockMvc.perform(get("/api/processes")
                .param("equipmentId", "EQ001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.list[0].equipmentId", is("EQ001")));
    }

    @Test
    void testProcessUpdate() throws Exception {
        // 1. 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andReturn();
        
        // 获取创建鐨勬祦绋婭D
        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();
        
        // 2. 更新娴佺▼
        MoldProcess updateProcess = new MoldProcess();
        updateProcess.setId(processId);
        updateProcess.setMoldId("M001");
        updateProcess.setProcessType("EDM");
        updateProcess.setEquipmentId("EQ002");
        updateProcess.setOperatorId("OP002");
        updateProcess.setPriority(2);
        updateProcess.setEstimatedDuration(150);
        updateProcess.setNotes("更新鍚庣殑娴佺▼");
        
        String updateJson = objectMapper.writeValueAsString(updateProcess);
        
        mockMvc.perform(put("/api/processes/{id}", processId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(processId.intValue())))
                .andExpect(jsonPath("$.processType", is("EDM")))
                .andExpect(jsonPath("$.equipmentId", is("EQ002")))
                .andExpect(jsonPath("$.operatorId", is("OP002")))
                .andExpect(jsonPath("$.priority", is(2)))
                .andExpect(jsonPath("$.estimatedDuration", is(150)))
                .andExpect(jsonPath("$.notes", is("更新鍚庣殑娴佺▼")));
    }

    @Test
    void testProcessDeletion() throws Exception {
        // 1. 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andReturn();
        
        // 获取创建鐨勬祦绋婭D
        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();
        
        // 2. 删除娴佺▼
        mockMvc.perform(delete("/api/processes/{id}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 3. 验证流程已删除
        mockMvc.perform(get("/api/processes/{id}", processId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testProcessStatistics() throws Exception {
        // 1. 创建澶氫釜涓嶅悓鐘舵€佺殑娴佺▼
        for (int i = 0; i < 5; i++) {
            MoldProcess process = new MoldProcess();
            process.setMoldId("M00" + i);
            process.setProcessType("CNC");
            process.setEquipmentId("EQ00" + i);
            process.setOperatorId("OP00" + i);
            process.setPriority(i + 1);
            process.setEstimatedDuration(120 + i * 10);
            process.setNotes("测试娴佺▼ " + i);
            
            String processJson = objectMapper.writeValueAsString(process);
            
            MvcResult result = mockMvc.perform(post("/api/processes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(processJson))
                    .andExpect(status().isOk())
                    .andReturn();
            
            String responseContent = result.getResponse().getContentAsString();
            MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
            Long processId = createdProcess.getId();
            
            // 根据索引设置不同的状态
            if (i == 1) {
                mockMvc.perform(post("/api/processes/{id}/start", processId)
                        .param("operatorId", "OP00" + i));
            } else if (i == 2) {
                mockMvc.perform(post("/api/processes/{id}/start", processId)
                        .param("operatorId", "OP00" + i));
                mockMvc.perform(post("/api/processes/{id}/pause", processId)
                        .param("operatorId", "OP00" + i));
            } else if (i == 3) {
                mockMvc.perform(post("/api/processes/{id}/start", processId)
                        .param("operatorId", "OP00" + i));
                mockMvc.perform(post("/api/processes/{id}/complete", processId)
                        .param("operatorId", "OP00" + i));
            } else if (i == 4) {
                mockMvc.perform(post("/api/processes/{id}/start", processId)
                        .param("operatorId", "OP00" + i));
                mockMvc.perform(post("/api/processes/{id}/cancel", processId)
                        .param("operatorId", "OP00" + i)
                        .param("reason", "测试鍙栨秷"));
            }
        }
        
        // 2. 获取缁熻淇℃伅
        mockMvc.perform(get("/api/processes/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.created", is(1)))
                .andExpect(jsonPath("$.inProgress", is(1)))
                .andExpect(jsonPath("$.paused", is(1)))
                .andExpect(jsonPath("$.completed", is(1)))
                .andExpect(jsonPath("$.cancelled", is(1)))
                .andExpect(jsonPath("$.total", is(5)));
    }
}
