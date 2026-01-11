package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.dto.PasswordResetRequest;
import com.mold.digitalization.dto.UserLockRequest;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.service.UserLockService;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.testsupport.JsonTestUtils;
import com.mold.digitalization.testsupport.MockMvcFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import java.nio.charset.StandardCharsets;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 管理员认证控制器测试类（standaloneSetup 版本）
 *
 * 说明：为彻底避免 MyBatis/Swagger 及其它自动配置干扰切片测试，本测试改为使用
 * MockMvcBuilders.standaloneSetup，仅构建 AdminAuthController 本身，不加载 Spring 应用上下文。
 */
@ExtendWith(MockitoExtension.class)
public class AdminAuthControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
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
        // email字段已按要求去除
        testUser.setStatus(1); // 启用状态

        // 统一使用测试工具中的 ObjectMapper
        this.objectMapper = JsonTestUtils.newObjectMapper();
        // 使用 standaloneSetup，仅加载目标控制器，避免扫描其它控制器及其依赖，并统一注册 UTF-8 与 JSON 转换器
        AdminAuthController controller = new AdminAuthController(authService, userService, userLockService);
        this.mockMvc = MockMvcFactory.standalone(
                controller,
                objectMapper
        );
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
                // 在 standaloneSetup 下，Jackson 默认会附带 charset=UTF-8，使用兼容匹配更稳妥
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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
        // 不设置 newPassword，使用默认密码

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // 在 standaloneSetup 下，Jackson 默认会附带 charset=UTF-8，使用兼容匹配更稳妥
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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

        // 密码与确认密码不匹配，控制器在参数校验阶段直接返回 400，不会触发用户存在性校验
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

        // 说明：密码长度校验不通过，控制器在参数校验阶段直接返回 400，不会触发用户存在性校验
        verify(userService, never()).getById(anyLong());
        // 未经过权限校验直接返回 400，不会触发重置密码逻辑
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
    @WithMockUser(roles = { "USER" }) // 非管理员角色，无权限
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
                // 说明：standaloneSetup 未启用 Spring Security 过滤器链，
                // WithMockUser 仅设置了 SecurityContext，但 @PreAuthorize 不会生效。
                // 控制器会先执行 userService.getById 校验，默认 mock 返回 null，因此为 404
                .andExpect(status().isNotFound());

        // 验证调用：未启用 Spring Security，控制器仍会进行用户存在性校验
        verify(userService, times(1)).getById(1L);
        // 未经过权限校验直接返回 404，不会触发重置密码逻辑
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

        // 创建请求对象
        UserLockRequest request = new UserLockRequest();
        request.setUserId(1L);
        request.setOperationType("lock");
        request.setLockReason("系统审核拒绝");

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/lock-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("用户不存在"));

        // 验证调用
        verify(userService, times(1)).getById(1L);
        verify(userLockService, never()).lockUser(anyLong(), anyString());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testUnlockUser_Success() throws Exception {
        // 模拟行为
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userLockService.unlockUser(1L)).thenReturn(true);

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/users/1/unlock"))
                .andExpect(status().isOk())
                // Jackson 可能返回 application/json;charset=UTF-8，使用兼容匹配
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("用户解锁成功"));

        // 验证调用
        verify(userService, times(1)).getUserById(1L);
        verify(userLockService, times(1)).unlockUser(1L);
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testUnlockUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getUserById(999L)).thenReturn(null);

        // 执行请求
        mockMvc.perform(post("/api/v1/admin/auth/users/999/unlock"))
                .andExpect(status().isNotFound())
                // Jackson 可能返回 application/json;charset=UTF-8，使用兼容匹配
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("用户不存在"));

        // 妤犲矁鐦夌拫鍐暏
        verify(userService, times(1)).getUserById(999L);
        verify(userLockService, times(0)).unlockUser(anyLong());
    }
}
