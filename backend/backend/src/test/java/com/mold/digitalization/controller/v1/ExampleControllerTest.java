package com.mold.digitalization.controller.v1;

import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.controller.v1.ExampleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 示例Controller测试
 * 展示如何正确测试使用API版本控制的Controller
 */
@ExtendWith(MockitoExtension.class)
class ExampleControllerTest {
    
    private MockMvc mockMvc;
    
    @InjectMocks
    private ExampleController exampleController;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exampleController).build();
    }
    
    @Test
    @DisplayName("获取示例列表 - 成功")
    void getExamples_Success() throws Exception {
        mockMvc.perform(get("/api/v1/examples")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("success"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.version").value("v1"))
            .andExpect(jsonPath("$.data.message").isNotEmpty());
    }
    
    @Test
    @DisplayName("获取单个示例 - 成功")
    void getExample_Success() throws Exception {
        Long exampleId = 1L;
        
        mockMvc.perform(get("/api/v1/examples/{id}", exampleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.id").value(exampleId))
            .andExpect(jsonPath("$.data.name").isNotEmpty())
            .andExpect(jsonPath("$.data.description").isNotEmpty());
    }
    
    @Test
    @DisplayName("创建示例 - 成功")
    void createExample_Success() throws Exception {
        String requestBody = """
            {
                "name": "测试示例",
                "description": "这是一个测试示例"
            }
            """;
        
        mockMvc.perform(post("/api/v1/examples")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.created").value(true))
            .andExpect(jsonPath("$.message").value("创建成功"));
    }
    
    @Test
    @DisplayName("更新示例 - 成功")
    void updateExample_Success() throws Exception {
        Long exampleId = 1L;
        String requestBody = """
            {
                "name": "更新后的名称",
                "description": "更新后的描述"
            }
            """;
        
        mockMvc.perform(put("/api/v1/examples/{id}", exampleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.id").value(exampleId))
            .andExpect(jsonPath("$.data.updated").value(true))
            .andExpect(jsonPath("$.message").value("更新成功"));
    }
    
    @Test
    @DisplayName("删除示例 - 成功")
    void deleteExample_Success() throws Exception {
        Long exampleId = 1L;
        
        mockMvc.perform(delete("/api/v1/examples/{id}", exampleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("删除成功"));
    }
    
    @Test
    @DisplayName("API路径验证 - 确保使用v1版本")
    void apiPathValidation() {
        // 验证路径前缀正确
        ResponseDTO<Map<String, Object>> response = ExampleController.getExamples();
        
        Map<String, Object> data = response.getData();
        String version = (String) data.get("version");
        
        org.junit.jupiter.api.Assertions.assertEquals("v1", version, 
            "API版本应该为v1");
    }
}
