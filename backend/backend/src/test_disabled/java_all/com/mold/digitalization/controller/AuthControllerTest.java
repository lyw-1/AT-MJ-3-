package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.dto.LoginRequest;
import com.mold.digitalization.dto.LoginResponse;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        testUser.setStatus(1);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new com.mold.digitalization.handler.GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setMessageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter()
                )
                .build();
    }

    @Test
    void testLogin_Success() throws Exception {
        // 准备登录请求对象
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // 准备登录响应
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("test-access-token");
        loginResponse.setRefreshToken("test-refresh-token");
        loginResponse.setUsername("testuser");
        loginResponse.setUserId(1L);
        loginResponse.setRoles("ROLE_USER");

        // 模拟行为
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // 执行请求
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.username").exists());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // 准备登录请求对象
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        // 模拟认证失败 - 密码错误时抛出BadCredentialsException
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("用户名或密码错误"));

        // 执行请求
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testLogin_UserNotExist() throws Exception {
        // 准备登录请求对象
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("password123");

        // 模拟认证失败 - 用户不存在时抛出BadCredentialsException
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("用户名或密码错误"));

        // 执行请求
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testLogin_NullUsername() throws Exception {
        // 准备登录请求对象
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");

        // 执行请求 - 验证请求参数校验
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // 验证没有调用认证服务
        verify(authService, times(0)).login(any());
    }

    @Test
    void testLogin_EmptyPassword() throws Exception {
        // 准备登录请求对象
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("");

        // 执行请求 - 验证请求参数校验
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // 验证没有调用认证服务
        verify(authService, times(0)).login(any());
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        // 准备请求参数
        String refreshToken = "old-jwt-token";

        // 模拟刷新令牌成功
        when(authService.refreshToken(refreshToken)).thenReturn("new-access-token");

        // 执行请求
        mockMvc.perform(post("/v1/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").value("new-access-token"));
    }

    @Test
    void testRefreshToken_InvalidToken() throws Exception {
        // 准备过期或无效的令牌
        String invalidToken = "invalid-jwt-token";

        // 模拟刷新令牌失败
        when(authService.refreshToken(invalidToken))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("无效的刷新令牌"));

        // 执行请求
        mockMvc.perform(post("/v1/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\": \"" + invalidToken + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testRefreshToken_NullToken() throws Exception {
        // 执行请求 - 缺少refreshToken参数
        mockMvc.perform(post("/v1/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserInfo_Success() throws Exception {
        // 模拟获取当前用户信息
        when(authService.getCurrentUserInfo()).thenReturn(testUser);

        // 执行请求
        mockMvc.perform(get("/v1/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.realName").value("测试用户"))
                .andExpect(jsonPath("$.data.phone").value("13800138000"));
        // 验证调用
        verify(authService, times(1)).getCurrentUserInfo();
    }

    @Test
    void testGetUserInfo_AccessDenied() throws Exception {
        // 模拟权限拒绝
        when(authService.getCurrentUserInfo()).thenThrow(new org.springframework.security.access.AccessDeniedException("拒绝访问"));

        mockMvc.perform(get("/v1/api/auth/me"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("拒绝访问"));
        verify(authService, times(1)).getCurrentUserInfo();
    }

    @Test
    void testLogout_Success() throws Exception {
        // 模拟登出无异常
        doNothing().when(authService).logout(anyString());

        mockMvc.perform(post("/v1/api/auth/logout").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("登出成功"));
        verify(authService, times(1)).logout("test-token");
    }
}
