package com.mold.digitalization.integration;

import com.mold.digitalization.Application;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProgressTracking;
import com.mold.digitalization.enums.ProcessStatus;
import com.mold.digitalization.mapper.MoldProcessMapper;
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
 * 杩涘害璺熻釜闆嗘垚测试
 * 测试杩涘害璺熻釜鐨勭鍒扮功能
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class ProgressTrackingIntegrationTest {

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
        private ProgressTrackingMapper progressTrackingMapper;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;
        private MoldProcess testProcess;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
                objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();

                // 鍒濆鍖栨祴璇曟暟鎹? testProcess = new MoldProcess();
                testProcess.setMoldId("M001");
                testProcess.setProcessType("CNC");
                testProcess.setEquipmentId("EQ001");
                testProcess.setOperatorId("OP001");
                testProcess.setPriority(1);
                testProcess.setEstimatedDuration(120);
                testProcess.setNotes("杩涘害璺熻釜闆嗘垚测试娴佺▼");
        }

    @Test
    void testCompleteProgressTrackingFlow() throws Exception {
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

        // 2. 创建杩涘害璺熻釜记录
        ProgressTracking progress = new ProgressTracking();
        progress.setProcessId(processId);
        progress.setCurrentStep(1);
        progress.setTotalSteps(10);
        progress.setProgressPercentage(10.0);
        progress.setCurrentOperation("瑁呭す");
        progress.setNextOperation("绮楀姞宸?);"
        progress.setEstimatedTimeRemaining(108);
        progress.setActualTimeUsed(12);

        String progressJson = objectMapper.writeValueAsString(progress);

        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(progressJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                .andExpect(jsonPath("$.currentStep", is(1)))
                .andExpect(jsonPath("$.totalSteps", is(10)))
                .andExpect(jsonPath("$.progressPercentage", is(10.0)))
                .andExpect(jsonPath("$.currentOperation", is("瑁呭す")))
                .andExpect(jsonPath("$.nextOperation", is("焊接作业")));

        // 3. 开€濮嬫祦绋?        mockMvc.perform(post("/api/processes/{id}/start", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 4. 更新杩涘害姝ラ
        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证杩涘害宸叉洿鏂?        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                .andExpect(jsonPath("$.currentStep", is(3)));

        // 5. 更新杩涘害鐧惧垎姣?        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "30.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证杩涘害宸叉洿鏂?        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progressPercentage", is(30.0)));

        // 6. 更新褰撳墠操作
        mockMvc.perform(put("/api/progress/{processId}/operation", processId)
                .param("currentOperation", "绮楀姞宸?)"
                .param("nextOperation", "鍗婄簿鍔犲伐"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证操作宸叉洿鏂?        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentOperation", is("绮楀姞宸?)))"
                .andExpect(jsonPath("$.nextOperation", is("鍗婄簿鍔犲伐")));

        // 7. 更新鏃堕棿淇℃伅
        mockMvc.perform(put("/api/progress/{processId}/time", processId)
                .param("estimatedTimeRemaining", "80")
                .param("actualTimeUsed", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证鏃堕棿淇℃伅宸叉洿鏂?        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estimatedTimeRemaining", is(80)))
                .andExpect(jsonPath("$.actualTimeUsed", is(40)));

        // 8. 更新杩涘害鍒?0%
        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "80.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        mockMvc.perform(put("/api/progress/{processId}/operation", processId)
                .param("currentOperation", "绮惧姞宸?)"
                .param("nextOperation", "鎶涘厜"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 9. 瀹屾垚娴佺▼
        mockMvc.perform(post("/api/processes/{id}/complete", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证娴佺▼宸插畬鎴?        mockMvc.perform(get("/api/processes/{id}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStatus", is("COMPLETED")));

        // 验证杩涘害璺熻釜记录宸叉洿鏂?        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progressPercentage", is(100.0)))
                .andExpect(jsonPath("$.currentStep", is(10)))
                .andExpect(jsonPath("$.currentOperation", is("已完成")));
    }

    @Test
    void testProgressTrackingWithMultipleProcesses() throws Exception {
        // 创建澶氫釜娴佺▼
        MoldProcess process1 = new MoldProcess();
        process1.setMoldId("M001");
        process1.setProcessType("CNC");
        process1.setEquipmentId("EQ001");
        process1.setOperatorId("OP001");
        process1.setPriority(1);
        process1.setEstimatedDuration(120);
        process1.setNotes("澶氭祦绋嬫祴璇?娴佺▼1");

        MoldProcess process2 = new MoldProcess();
        process2.setMoldId("M002");
        process2.setProcessType("EDM");
        process2.setEquipmentId("EQ002");
        process2.setOperatorId("OP002");
        process2.setPriority(2);
        process2.setEstimatedDuration(90);
        process2.setNotes("澶氭祦绋嬫祴璇?娴佺▼2");

        // 创建娴佺▼1
        String process1Json = objectMapper.writeValueAsString(process1);
        MvcResult result1 = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(process1Json))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent1 = result1.getResponse().getContentAsString();
        MoldProcess createdProcess1 = objectMapper.readValue(responseContent1, MoldProcess.class);
        Long processId1 = createdProcess1.getId();

        // 创建娴佺▼2
        String process2Json = objectMapper.writeValueAsString(process2);
        MvcResult result2 = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(process2Json))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent2 = result2.getResponse().getContentAsString();
        MoldProcess createdProcess2 = objectMapper.readValue(responseContent2, MoldProcess.class);
        Long processId2 = createdProcess2.getId();

        // 涓烘祦绋?创建杩涘害璺熻釜
        ProgressTracking progress1 = new ProgressTracking();
        progress1.setProcessId(processId1);
        progress1.setCurrentStep(2);
        progress1.setTotalSteps(8);
        progress1.setProgressPercentage(25.0);
        progress1.setCurrentOperation("绮楀姞宸?);"
        progress1.setNextOperation("鍗婄簿鍔犲伐");
        progress1.setEstimatedTimeRemaining(90);
        progress1.setActualTimeUsed(30);

        String progress1Json = objectMapper.writeValueAsString(progress1);
        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(progress1Json))
                .andExpect(status().isOk());

        // 涓烘祦绋?创建杩涘害璺熻釜
        ProgressTracking progress2 = new ProgressTracking();
        progress2.setProcessId(processId2);
        progress2.setCurrentStep(1);
        progress2.setTotalSteps(6);
        progress2.setProgressPercentage(16.7);
        progress2.setCurrentOperation("鐢垫瀬鍒朵綔");
        progress2.setNextOperation("鏀剧數鍔犲伐");
        progress2.setEstimatedTimeRemaining(75);
        progress2.setActualTimeUsed(15);

        String progress2Json = objectMapper.writeValueAsString(progress2);
        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(progress2Json))
                .andExpect(status().isOk());

        // 开€濮嬫祦绋?
        mockMvc.perform(post("/api/processes/{id}/start", processId1)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk());

        // 开€濮嬫祦绋?
        mockMvc.perform(post("/api/processes/{id}/start", processId2)
                .param("operatorId", "OP002"))
                .andExpect(status().isOk());

        // 更新娴佺▼1杩涘害
        mockMvc.perform(put("/api/progress/{processId}/step", processId1)
                .param("step", "4"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/progress/{processId}/percentage", processId1)
                .param("percentage", "50.0"))
                .andExpect(status().isOk());

        // 更新娴佺▼2杩涘害
        mockMvc.perform(put("/api/progress/{processId}/step", processId2)
                .param("step", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/progress/{processId}/percentage", processId2)
                .param("percentage", "33.3"))
                .andExpect(status().isOk());

        // 验证涓や釜娴佺▼鐨勮繘搴﹁窡韪褰?        mockMvc.perform(get("/api/progress/process/{processId}", processId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processId", is(processId1.intValue())))
                .andExpect(jsonPath("$.currentStep", is(4)))
                .andExpect(jsonPath("$.progressPercentage", is(50.0)));

        mockMvc.perform(get("/api/progress/process/{processId}", processId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processId", is(processId2.intValue())))
                .andExpect(jsonPath("$.currentStep", is(2)))
                .andExpect(jsonPath("$.progressPercentage", is(33.3)));

        // 获取鎵€鏈夎繘琛屼腑鐨勮繘搴﹁窡韪褰?        mockMvc.perform(get("/api/progress/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].processId", hasItems(processId1.intValue(), processId2.intValue())));
    }

    @Test
    void testProgressTrackingWithExceptions() throws Exception {
        // 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();

        // 创建杩涘害璺熻釜记录
        ProgressTracking progress = new ProgressTracking();
        progress.setProcessId(processId);
        progress.setCurrentStep(3);
        progress.setTotalSteps(10);
        progress.setProgressPercentage(30.0);
        progress.setCurrentOperation("绮楀姞宸?);"
        progress.setNextOperation("鍗婄簿鍔犲伐");
        progress.setEstimatedTimeRemaining(84);
        progress.setActualTimeUsed(36);

        String progressJson = objectMapper.writeValueAsString(progress);
        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(progressJson))
                .andExpect(status().isOk());

        // 开€濮嬫祦绋?        mockMvc.perform(post("/api/processes/{id}/start", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk());

        // 创建开傚父记录
        mockMvc.perform(post("/api/exceptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"processId\":" + processId + ",\"exceptionType\":\"EQUIPMENT_FAILURE\",\"exceptionLevel\":\"HIGH\",\"title\":\"璁惧鏁呴殰\",\"description\":\"涓昏酱开傚父\",\"reportedBy\":\"OP001\"}"))
                .andExpect(status().isOk());

        // 鏆傚仠娴佺▼
        mockMvc.perform(post("/api/processes/{id}/pause", processId)
                .param("operatorId", "OP001")
                .param("reason", "璁惧鏁呴殰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证娴佺▼状态        mockMvc.perform(get("/api/processes/{id}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStatus", is("PAUSED")));

        // 瑙ｅ喅开傚父
        mockMvc.perform(put("/api/exceptions/{id}/resolve", 1)
                .param("resolution", "鏇存崲涓昏酱杞存壙")
                .param("resolvedBy", "TECH001"))
                .andExpect(status().isOk());

        // 鎭㈠娴佺▼
        mockMvc.perform(post("/api/processes/{id}/resume", processId)
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 验证娴佺▼状态        mockMvc.perform(get("/api/processes/{id}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStatus", is("IN_PROGRESS")));

        // 更新杩涘害
        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "5"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "50.0"))
                .andExpect(status().isOk());

        // 验证杩涘害璺熻釜记录
        mockMvc.perform(get("/api/progress/process/{processId}", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStep", is(5)))
                .andExpect(jsonPath("$.progressPercentage", is(50.0)));
    }

    @Test
    void testProgressTrackingStatistics() throws Exception {
        // 创建澶氫釜娴佺▼骞惰缃笉鍚岃繘搴?        for (int i = 1; i <= 5; i++) {
            MoldProcess process = new MoldProcess();
            process.setMoldId("M00" + i);
            process.setProcessType("CNC");
            process.setEquipmentId("EQ00" + i);
            process.setOperatorId("OP00" + i);
            process.setPriority(i);
            process.setEstimatedDuration(120);
            process.setNotes("缁熻测试娴佺▼" + i);

            String processJson = objectMapper.writeValueAsString(process);
            MvcResult result = mockMvc.perform(post("/api/processes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(processJson))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
            Long processId = createdProcess.getId();

            // 创建杩涘害璺熻釜记录
            ProgressTracking progress = new ProgressTracking();
            progress.setProcessId(processId);
            progress.setCurrentStep(i);
            progress.setTotalSteps(10);
            progress.setProgressPercentage(i * 10.0);
            progress.setCurrentOperation("测试操作" + i);
            progress.setNextOperation("涓嬩竴操作" + i);
            progress.setEstimatedTimeRemaining(120 - i * 12);
            progress.setActualTimeUsed(i * 12);

            String progressJson = objectMapper.writeValueAsString(progress);
            mockMvc.perform(post("/api/progress")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(progressJson))
                    .andExpect(status().isOk());

            // 开€濮嬫祦绋?            mockMvc.perform(post("/api/processes/{id}/start", processId)
                    .param("operatorId", "OP00" + i))
                    .andExpect(status().isOk());
        }

        // 获取杩涘害缁熻淇℃伅
        mockMvc.perform(get("/api/progress/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProcesses", is(5)))
                .andExpect(jsonPath("$.averageProgress", closeTo(30.0, 0.1)))
                .andExpect(jsonPath("$.completedProcesses", is(0)))
                .andExpect(jsonPath("$.inProgressProcesses", is(5)));
    }

    @Test
    void testProgressTrackingValidation() throws Exception {
        // 创建娴佺▼
        String processJson = objectMapper.writeValueAsString(testProcess);
        MvcResult result = mockMvc.perform(post("/api/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MoldProcess createdProcess = objectMapper.readValue(responseContent, MoldProcess.class);
        Long processId = createdProcess.getId();

        // 测试鏃犳晥鐨勮繘搴︾櫨鍒嗘瘮
        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "150.0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/progress/{processId}/percentage", processId)
                .param("percentage", "-10.0"))
                .andExpect(status().isBadRequest());

        // 测试鏃犳晥鐨勬楠?        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/progress/{processId}/step", processId)
                .param("step", "-5"))
                .andExpect(status().isBadRequest());

        // 测试鏃犳晥鐨勬椂闂?        mockMvc.perform(put("/api/progress/{processId}/time", processId)
                .param("estimatedTimeRemaining", "-10")
                .param("actualTimeUsed", "20"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/progress/{processId}/time", processId)
                .param("estimatedTimeRemaining", "80")
                .param("actualTimeUsed", "-5"))
                .andExpect(status().isBadRequest());
    }
}
