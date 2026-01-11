package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.dto.PasswordResetRequest;
import com.mold.digitalization.dto.UserLockRequest;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.service.UserLockService;
import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import com.mold.digitalization.config.TestConfig;
import org.springframework.test.web.servlet.MockMvc;
import com.mold.digitalization.aspect.OperationLogAspect;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 管理员认证控制器测试类
 */
@WebMvcTest(value = AdminAuthController.class)
@Import(TestConfig.class)
public class AdminAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserLockService userLockService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        // email字段已按要求移除
        testUser.setStatus(1); // 启用状态
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_Success() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(authService.resetPassword(eq(1L), eq("newPassword123"))).thenReturn(true);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("密码重置成功"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(authService, times(1)).resetPassword(eq(1L), eq("newPassword123"));
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_SuccessWithDefaultPassword() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(authService.resetPassword(eq(1L), isNull())).thenReturn(true);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        // 不设置newPassword，使用默认密码

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("密码重置成功"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(authService, times(1)).resetPassword(eq(1L), isNull());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(null);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("用户不存在"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(authService, never()).resetPassword(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_PasswordMismatch() throws Exception {
        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("differentPassword");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("新密码与确认密码不匹配"));

        // 验证调用
        verify(userService, never()).getById(anyLong());
        verify(authService, never()).resetPassword(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_InvalidPassword() throws Exception {
        // 创建请求对象 - 密码过短
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("short");
        request.setConfirmPassword("short");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // 验证调用
        verify(userService, never()).getById(anyLong());
        verify(authService, never()).resetPassword(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testResetPassword_ServiceFailure() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(authService.resetPassword(eq(1L), eq("newPassword123"))).thenReturn(false);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("密码重置失败"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(authService, times(1)).resetPassword(eq(1L), eq("newPassword123"));
    }

    @Test
    @WithMockUser(roles = { "USER" }) // 闈炵鐞嗗憳瑙掕壊
    void testResetPassword_AccessDenied() throws Exception {
        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        // 执行请求 - 没有管理员权限
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        // 验证调用
        verify(userService, never()).getById(anyLong());
        verify(authService, never()).resetPassword(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testLockUser_Success() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(userLockService.lockUser(eq(1L), eq("测试锁定"))).thenReturn(true);

        // 创建请求对象
        UserLockRequest request = new UserLockRequest();
        request.setUserId(1L);
        request.setOperationType("lock");
        request.setLockReason("测试锁定");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/lock-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("用户锁定成功"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(userLockService, times(1)).lockUser(eq(1L), eq("测试锁定"));
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testLockUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(null);

        // 创建璇锋眰瀵硅薄
        UserLockRequest request = new UserLockRequest();
        request.setUserId(1L);
        request.setOperationType("lock");
        request.setLockReason("测试閿佸畾");

        // 执行璇锋眰
        mockMvc.perform(post("/api/v1/admin/auth/lock-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("用户不存在"));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(userLockService, never()).lockUser(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testUnlockUser_Success() throws Exception {
        // 模拟行为
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userLockService.unlockUser(1L)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(post("/api/v1/admin/auth/users/1/unlock"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("用户瑙ｉ攣成功"));

        // 验证璋冪敤
        verify(userService, times(1)).getUserById(1L);
        verify(userLockService, times(1)).unlockUser(1L);
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testUnlockUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getUserById(999L)).thenReturn(null);

        // 执行璇锋眰
        mockMvc.perform(post("/api/v1/admin/auth/users/999/unlock"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("用户不存在"));

        // 验证璋冪敤
        verify(userService, times(1)).getUserById(999L);
        verify(userLockService, times(0)).unlockUser(anyLong());
    }
}
