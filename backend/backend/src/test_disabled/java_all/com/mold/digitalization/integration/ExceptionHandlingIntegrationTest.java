package com.mold.digitalization.integration;

import com.mold.digitalization.Application;
import com.mold.digitalization.entity.ExceptionRecord;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.enums.ExceptionLevel;
import com.mold.digitalization.enums.ExceptionStatus;
import com.mold.digitalization.enums.ExceptionType;
import com.mold.digitalization.mapper.ExceptionRecordMapper;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.service.ExceptionHandlingService;
import com.mold.digitalization.service.MoldProcessService;
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
 * 开傚父澶勭悊闆嗘垚测试
 * 测试开傚父澶勭悊鐨勭鍒扮功能
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class ExceptionHandlingIntegrationTest {

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private MoldProcessService moldProcessService;

        @Autowired
        private ExceptionHandlingService exceptionHandlingService;

        @Autowired
        private MoldProcessMapper moldProcessMapper;

        @Autowired
        private ExceptionRecordMapper exceptionRecordMapper;

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
                testProcess.setNotes("开傚父澶勭悊闆嗘垚测试娴佺▼");
        }

        @Test
        void testCompleteExceptionHandlingFlow() throws Exception {
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

                // 3. 创建开傚父
                String exceptionJson = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                + "\"exceptionLevel\":\"HIGH\","
                                + "\"title\":\"璁惧鏁呴殰\","
                                + "\"description\":\"涓昏酱开傚父闇囧姩\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                MvcResult exceptionResult = mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(exceptionJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                                .andExpect(jsonPath("$.exceptionType", is("EQUIPMENT_FAILURE")))
                                .andExpect(jsonPath("$.exceptionLevel", is("HIGH")))
                                .andExpect(jsonPath("$.title", is("璁惧鏁呴殰")))
                                .andExpect(jsonPath("$.description", is("涓昏酱开傚父闇囧姩")))
                                .andExpect(jsonPath("$.reportedBy", is("OP001")))
                                .andExpect(jsonPath("$.exceptionStatus", is("OPEN")))
                                .andReturn();

                // 获取开傚父ID
                String exceptionResponse = exceptionResult.getResponse().getContentAsString();
                ExceptionRecord createdException = objectMapper.readValue(exceptionResponse, ExceptionRecord.class);
                Long exceptionId = createdException.getId();

                // 4. 获取开傚父璇︽儏
                mockMvc.perform(get("/api/exceptions/{id}", exceptionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(exceptionId.intValue())))
                                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                                .andExpect(jsonPath("$.exceptionType", is("EQUIPMENT_FAILURE")))
                                .andExpect(jsonPath("$.exceptionStatus", is("OPEN")));

                // 5. 更新异常状态为处理中
                mockMvc.perform(put("/api/exceptions/{id}/status", exceptionId)
                                .param("status", "IN_PROGRESS")
                                .param("updatedBy", "TECH001"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", is(true)));

                // 6. 验证开傚父鐘舵€佸凡更新
                mockMvc.perform(get("/api/exceptions/{id}", exceptionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.exceptionStatus", is("IN_PROGRESS")));

                // 7. 澶勭悊开傚父
                mockMvc.perform(put("/api/exceptions/{id}/handle", exceptionId)
                                .param("resolution", "鏇存崲涓昏酱杞存壙")
                                .param("resolvedBy", "TECH001"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", is(true)));

                // 8. 验证异常已解决
                mockMvc.perform(get("/api/exceptions/{id}", exceptionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.exceptionStatus", is("RESOLVED")))
                                .andExpect(jsonPath("$.resolution", is("鏇存崲涓昏酱杞存壙")))
                                .andExpect(jsonPath("$.resolvedBy", is("TECH001")))
                                .andExpect(jsonPath("$.resolvedAt", notNullValue()));

                // 9. 验证流程状态
                mockMvc.perform(get("/api/processes/{id}", processId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(processId.intValue())));

                // 10. 获取流程的所有异常
                mockMvc.perform(get("/api/exceptions/process/{processId}", processId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id", is(exceptionId.intValue())))
                                .andExpect(jsonPath("$[0].processId", is(processId.intValue())));
        }

        @Test
        void testExceptionHandlingWithMultipleExceptions() throws Exception {
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

                // 3. 创建第一个异常
                String exceptionJson1 = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                + "\"exceptionLevel\":\"HIGH\","
                                + "\"title\":\"璁惧鏁呴殰\","
                                + "\"description\":\"涓昏酱开傚父闇囧姩\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(exceptionJson1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                                .andExpect(jsonPath("$.exceptionType", is("EQUIPMENT_FAILURE")))
                                .andExpect(jsonPath("$.exceptionLevel", is("HIGH")))
                                .andExpect(jsonPath("$.title", is("璁惧鏁呴殰")))
                                .andExpect(jsonPath("$.description", is("涓昏酱开傚父闇囧姩")))
                                .andExpect(jsonPath("$.reportedBy", is("OP001")))
                                .andExpect(jsonPath("$.exceptionStatus", is("OPEN")));

                // 4. 创建第二个异常
                String exceptionJson2 = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"MATERIAL_ISSUE\","
                                + "\"exceptionLevel\":\"MEDIUM\","
                                + "\"title\":\"鏉愭枡闂\","
                                + "\"description\":\"材料精度不符合要求\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(exceptionJson2))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.processId", is(processId.intValue())))
                                .andExpect(jsonPath("$.exceptionType", is("MATERIAL_ISSUE")))
                                .andExpect(jsonPath("$.exceptionLevel", is("MEDIUM")))
                                .andExpect(jsonPath("$.title", is("鏉愭枡闂")))
                                .andExpect(jsonPath("$.description", is("材料精度不符合要求")))
                                .andExpect(jsonPath("$.reportedBy", is("OP001")))
                                .andExpect(jsonPath("$.exceptionStatus", is("OPEN")));

                // 5. 获取流程的所有异常
                mockMvc.perform(get("/api/exceptions/process/{processId}", processId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)));

                // 6. 处理第一个异常
                List<ExceptionRecord> exceptions = exceptionHandlingService.getExceptionsByProcessId(processId);
                Long firstExceptionId = exceptions.get(0).getId();

                mockMvc.perform(put("/api/exceptions/{id}/handle", firstExceptionId)
                                .param("resolution", "鏇存崲涓昏酱杞存壙")
                                .param("resolvedBy", "TECH001"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", is(true)));

                // 7. 验证绗竴涓紓甯稿凡瑙ｅ喅
                mockMvc.perform(get("/api/exceptions/{id}", firstExceptionId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.exceptionStatus", is("RESOLVED")))
                                .andExpect(jsonPath("$.resolution", is("鏇存崲涓昏酱杞存壙")))
                                .andExpect(jsonPath("$.resolvedBy", is("TECH001")))
                                .andExpect(jsonPath("$.resolvedAt", notNullValue()));

                // 8. 验证流程状态
                mockMvc.perform(get("/api/processes/{id}", processId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(processId.intValue())));
        }

        @Test
        void testExceptionHandlingWithInvalidData() throws Exception {
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

                // 3. 创建开傚父 - 缂哄皯蹇呭～瀛楁
                String invalidExceptionJson = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                + "\"exceptionLevel\":\"HIGH\""
                                + "}";

                mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidExceptionJson))
                                .andExpect(status().isBadRequest());

                // 4. 创建异常 - 无效的异常类型
                String invalidTypeExceptionJson = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"INVALID_TYPE\","
                                + "\"exceptionLevel\":\"HIGH\","
                                + "\"title\":\"璁惧鏁呴殰\","
                                + "\"description\":\"涓昏酱开傚父闇囧姩\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidTypeExceptionJson))
                                .andExpect(status().isBadRequest());

                // 5. 创建异常 - 无效的异常等级
                String invalidLevelExceptionJson = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                + "\"exceptionLevel\":\"INVALID_LEVEL\","
                                + "\"title\":\"璁惧鏁呴殰\","
                                + "\"description\":\"涓昏酱开傚父闇囧姩\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidLevelExceptionJson))
                                .andExpect(status().isBadRequest());

                // 6. 更新不存在的异常状态
                mockMvc.perform(put("/api/exceptions/{id}/status", 99999L)
                                .param("status", "IN_PROGRESS")
                                .param("updatedBy", "TECH001"))
                                .andExpect(status().isNotFound());

                // 7. 澶勭悊涓嶅瓨鍦ㄧ殑开傚父
                mockMvc.perform(put("/api/exceptions/{id}/handle", 99999L)
                                .param("resolution", "鏇存崲涓昏酱杞存壙")
                                .param("resolvedBy", "TECH001"))
                                .andExpect(status().isNotFound());

                // 8. 获取涓嶅瓨鍦ㄧ殑开傚父璇︽儏
                mockMvc.perform(get("/api/exceptions/{id}", 99999L))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testExceptionHandlingWithUnauthorizedAccess() throws Exception {
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

                // 3. 创建开傚父
                String exceptionJson = "{"
                                + "\"processId\":" + processId + ","
                                + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                + "\"exceptionLevel\":\"HIGH\","
                                + "\"title\":\"璁惧鏁呴殰\","
                                + "\"description\":\"涓昏酱开傚父闇囧姩\","
                                + "\"reportedBy\":\"OP001\""
                                + "}";

                MvcResult exceptionResult = mockMvc.perform(post("/api/exceptions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(exceptionJson))
                                .andExpect(status().isOk())
                                .andReturn();

                // 获取开傚父ID
                String exceptionResponse = exceptionResult.getResponse().getContentAsString();
                ExceptionRecord createdException = objectMapper.readValue(exceptionResponse, ExceptionRecord.class);
                Long exceptionId = createdException.getId();

                // 4. 尝试使用未授权用户更新异常状态
                mockMvc.perform(put("/api/exceptions/{id}/status", exceptionId)
                                .param("status", "IN_PROGRESS")
                                .param("updatedBy", "UNAUTHORIZED_USER"))
                                .andExpect(status().isForbidden());

                // 5. 灏濊瘯鐢ㄦ湭鎺堟潈用户澶勭悊开傚父
                mockMvc.perform(put("/api/exceptions/{id}/handle", exceptionId)
                                .param("resolution", "鏇存崲涓昏酱杞存壙")
                                .param("resolvedBy", "UNAUTHORIZED_USER"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testExceptionStatistics() throws Exception {
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

                // 3. 创建澶氫釜开傚父
                for (int i = 0; i < 5; i++) {
                        String exceptionJson = "{"
                                        + "\"processId\":" + processId + ","
                                        + "\"exceptionType\":\"EQUIPMENT_FAILURE\","
                                        + "\"exceptionLevel\":\"HIGH\","
                                        + "\"title\":\"璁惧鏁呴殰" + i + "\","
                                        + "\"description\":\"涓昏酱开傚父闇囧姩" + i + "\","
                                        + "\"reportedBy\":\"OP001\""
                                        + "}";

                        mockMvc.perform(post("/api/exceptions")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(exceptionJson))
                                        .andExpect(status().isOk());
                }

                // 4. 获取开傚父缁熻淇℃伅
                mockMvc.perform(get("/api/exceptions/statistics"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.total", is(5)))
                                .andExpect(jsonPath("$.open", is(5)))
                                .andExpect(jsonPath("$.inProgress", is(0)))
                                .andExpect(jsonPath("$.resolved", is(0)))
                                .andExpect(jsonPath("$.closed", is(0)));

                // 5. 澶勭悊閮ㄥ垎开傚父
                List<ExceptionRecord> exceptions = exceptionHandlingService.getExceptionsByProcessId(processId);
                for (int i = 0; i < 3; i++) {
                        Long exceptionId = exceptions.get(i).getId();
                        mockMvc.perform(put("/api/exceptions/{id}/handle", exceptionId)
                                        .param("resolution", "鏇存崲涓昏酱杞存壙" + i)
                                        .param("resolvedBy", "TECH001"))
                                        .andExpect(status().isOk());
                }

                // 6. 鍐嶆获取开傚父缁熻淇℃伅
                mockMvc.perform(get("/api/exceptions/statistics"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.total", is(5)))
                                .andExpect(jsonPath("$.open", is(2)))
                                .andExpect(jsonPath("$.inProgress", is(0)))
                                .andExpect(jsonPath("$.resolved", is(3)))
                                .andExpect(jsonPath("$.closed", is(0)));
        }
}
