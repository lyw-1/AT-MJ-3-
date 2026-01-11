package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.dto.LoginRequest;
import com.mold.digitalization.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 鐧诲綍功能测试绫? * 测试用户鐧诲綍接口鐨勫姛鑳芥槸鍚︽甯? */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 测试管理员用户登录
     */
    @Test
    public void testAdminLogin() throws Exception {
        // 设置MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 创建登录请求
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("123456");

        // 发送登录请求
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.user.username").value("admin"))
                .andExpect(jsonPath("$.data.user.realName").value("系统管理员"))
                .andReturn();

        // 验证响应结果
        String responseContent = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(responseContent, LoginResponse.class);
        
        assertNotNull(loginResponse.getData().getToken());
        assertEquals("admin", loginResponse.getData().getUser().getUsername());
        assertEquals("系统管理员", loginResponse.getData().getUser().getRealName());
    }

    /**
     * 测试操作鍛樼敤鎴风櫥褰?     */
    @Test
    public void testOperatorLogin() throws Exception {
        // 设置MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 创建鐧诲綍璇锋眰
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("operator1");
        loginRequest.setPassword("123456");

        // 鍙戦€佺櫥褰曡姹?        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("鐧诲綍成功"))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.user.username").value("operator1"))
                .andExpect(jsonPath("$.data.user.realName").value("操作鍛?"))
                .andReturn();

        // 验证鍝嶅簲结果
        String responseContent = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(responseContent, LoginResponse.class);
        
        assertNotNull(loginResponse.getData().getToken());
        assertEquals("operator1", loginResponse.getData().getUser().getUsername());
        assertEquals("操作鍛?", loginResponse.getData().getUser().getRealName());
    }

    /**
     * 测试错误鐨勭敤鎴峰悕
     */
    @Test
    public void testInvalidUsername() throws Exception {
        // 设置MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 创建鐧诲綍璇锋眰
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("123456");

        // 鍙戦€佺櫥褰曡姹?        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户名嶆垨密码错误"));
    }

    /**
     * 测试错误鐨勫瘑鐮?     */
    @Test
    public void testInvalidPassword() throws Exception {
        // 设置MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 创建鐧诲綍璇锋眰
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongpassword");

        // 鍙戦€佺櫥褰曡姹?        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户名嶆垨密码错误"));
    }
}