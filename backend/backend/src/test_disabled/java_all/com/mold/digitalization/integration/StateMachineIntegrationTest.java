package com.mold.digitalization.integration;

import com.mold.digitalization.Application;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.enums.ProcessStatus;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.service.MoldProcessService;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 鐘舵€佹満闆嗘垚测试
 * 测试鐘舵€佹満鐨勭鍒扮功能
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class StateMachineIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MoldProcessService moldProcessService;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private MoldProcessMapper moldProcessMapper;

    @Autowired
    private ProcessStatusHistoryMapper statusHistoryMapper;

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
        testProcess.setNotes("鐘舵€佹満闆嗘垚测试娴佺▼");
    }

    @Test
    void testCompleteStateTransitionFlow() throws Exception {
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
        
        // 2. 测试鍒濆状态        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("CREATED")));
        
        // 3. 测试鍙兘鐨勭姸鎬佽浆鎹?        mockMvc.perform(get("/api/state-machine/{processId}/possible-transitions", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // IN_PROGRESS, CANCELLED
                .andExpect(jsonPath("$", hasItem("IN_PROGRESS")))
                .andExpect(jsonPath("$", hasItem("CANCELLED")));
        
        // 4. 测试鐘舵€佽浆鎹㈡湁鏁堟€?        mockMvc.perform(get("/api/state-machine/{processId}/can-transition", processId)
                .param("targetStatus", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        mockMvc.perform(get("/api/state-machine/{processId}/can-transition", processId)
                .param("targetStatus", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
        
        // 5. 执行鐘舵€佽浆鎹細CREATED -> IN_PROGRESS
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "IN_PROGRESS")
                .param("reason", "开€濮嬪姞宸?)"
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证鐘舵€佸凡更新
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("IN_PROGRESS")));
        
        // 6. 测试鏂扮姸鎬佺殑鍙兘杞崲
        mockMvc.perform(get("/api/state-machine/{processId}/possible-transitions", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))) // PAUSED, COMPLETED, CANCELLED
                .andExpect(jsonPath("$", hasItem("PAUSED")))
                .andExpect(jsonPath("$", hasItem("COMPLETED")))
                .andExpect(jsonPath("$", hasItem("CANCELLED")));
        
        // 7. 执行鐘舵€佽浆鎹細IN_PROGRESS -> PAUSED
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "PAUSED")
                .param("reason", "璁惧缁存姢")
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证鐘舵€佸凡更新
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("PAUSED")));
        
        // 8. 测试鏂扮姸鎬佺殑鍙兘杞崲
        mockMvc.perform(get("/api/state-machine/{processId}/possible-transitions", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // IN_PROGRESS, CANCELLED
                .andExpect(jsonPath("$", hasItem("IN_PROGRESS")))
                .andExpect(jsonPath("$", hasItem("CANCELLED")));
        
        // 9. 执行鐘舵€佽浆鎹細PAUSED -> IN_PROGRESS
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "IN_PROGRESS")
                .param("reason", "缁存姢瀹屾垚锛岀户缁姞宸?)"
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证鐘舵€佸凡更新
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("IN_PROGRESS")));
        
        // 10. 执行鐘舵€佽浆鎹細IN_PROGRESS -> COMPLETED
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "COMPLETED")
                .param("reason", "鍔犲伐瀹屾垚")
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证鐘舵€佸凡更新
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("COMPLETED")));
        
        // 11. 测试鏈€缁堢姸鎬佺殑鍙兘杞崲锛堝簲璇ヤ负绌猴級
        mockMvc.perform(get("/api/state-machine/{processId}/possible-transitions", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        // 12. 获取鐘舵€佸巻鍙?        mockMvc.perform(get("/api/state-machine/{processId}/history", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4))) // CREATED -> IN_PROGRESS -> PAUSED -> IN_PROGRESS -> COMPLETED
                .andExpect(jsonPath("$[0].fromStatus", is("CREATED")))
                .andExpect(jsonPath("$[0].toStatus", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[1].fromStatus", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[1].toStatus", is("PAUSED")))
                .andExpect(jsonPath("$[2].fromStatus", is("PAUSED")))
                .andExpect(jsonPath("$[2].toStatus", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[3].fromStatus", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[3].toStatus", is("COMPLETED")));
    }

    @Test
    void testInvalidStateTransition() throws Exception {
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
        
        // 2. 灏濊瘯鏃犳晥鐨勭姸鎬佽浆鎹細CREATED -> COMPLETED
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "COMPLETED")
                .param("reason", "灏濊瘯鐩存帴瀹屾垚")
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
        
        // 3. 验证鐘舵€佹湭鏀瑰彉
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("CREATED")));
    }

    @Test
    void testCancelStateTransition() throws Exception {
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
        
        // 2. 执行鐘舵€佽浆鎹細CREATED -> CANCELLED
        mockMvc.perform(post("/api/state-machine/{processId}/transition", processId)
                .param("targetStatus", "CANCELLED")
                .param("reason", "瀹㈡埛鍙栨秷璁㈠崟")
                .param("operatorId", "OP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
        
        // 验证鐘舵€佸凡更新
        mockMvc.perform(get("/api/state-machine/{processId}/current-state", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("CANCELLED")));
        
        // 3. 测试鍙栨秷鐘舵€佺殑鍙兘杞崲锛堝簲璇ヤ负绌猴級
        mockMvc.perform(get("/api/state-machine/{processId}/possible-transitions", processId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testStateMachineServiceIntegration() {
        // 1. 创建娴佺▼
        MoldProcess createdProcess = moldProcessService.createProcess(testProcess);
        Long processId = createdProcess.getId();
        
        // 2. 测试鍒濆状态        String currentState = stateMachineService.getCurrentState(processId);
        assertEquals("CREATED", currentState);
        
        // 3. 测试鍙兘鐨勭姸鎬佽浆鎹?        List<String> possibleTransitions = stateMachineService.getPossibleTransitions(processId);
        assertEquals(2, possibleTransitions.size());
        assertTrue(possibleTransitions.contains("IN_PROGRESS"));
        assertTrue(possibleTransitions.contains("CANCELLED"));
        
        // 4. 测试鐘舵€佽浆鎹㈡湁鏁堟€?        assertTrue(stateMachineService.canTransition(processId, "IN_PROGRESS"));
        assertFalse(stateMachineService.canTransition(processId, "COMPLETED"));
        
        // 5. 执行鐘舵€佽浆鎹細CREATED -> IN_PROGRESS
        assertTrue(stateMachineService.transitionState(processId, "IN_PROGRESS", 1L));
        
        // 验证鐘舵€佸凡更新
        assertEquals("IN_PROGRESS", stateMachineService.getCurrentState(processId));
        
        // 6. 获取鐘舵€佸巻鍙?        List<ProcessStatusHistory> history = stateMachineService.getStateHistory(processId);
        assertEquals(1, history.size());
        assertEquals("CREATED", history.get(0).getFromStatus());
        assertEquals("IN_PROGRESS", history.get(0).getToStatus());
    }
}