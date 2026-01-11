package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.testsupport.JsonTestUtils;
import com.mold.digitalization.testsupport.MockMvcFactory;
import com.mold.digitalization.dto.PasswordResetRequest;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.handler.GlobalExceptionHandler;
import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 统一使用测试工具中的 ObjectMapper 与 MockMvcFactory
        objectMapper = JsonTestUtils.newObjectMapper();
        mockMvc = MockMvcFactory.standalone(userController, objectMapper);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        testUser.setStatus(1); // 1表示启用
    }

    @Test
    void testResetUserPassword_Success_DefaultPassword() throws Exception {
        // 模拟行为 - 默认密码场景
        when(userService.getUserById(1L)).thenReturn(testUser);
        // 默认密码场景：控制器已改为使用 PasswordUtil.generateDefaultPassword() 动态生成一次性默认密码
        // 因无法预知具体值，这里使用 anyString() 进行 stub，使重置调用返回成功
        when(userService.resetPassword(eq(1L), anyString())).thenReturn(true);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        // 不设置newPassword，使用默认密码
        // 执行请求
        mockMvc.perform(post("/api/users/1/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.defaultPassword").value(true));

        // 验证调用
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).resetPassword(eq(1L), anyString());
    }

    @Test
    void testResetUserPassword_Success_CustomPassword() throws Exception {
        // 模拟行为 - 自定义密码场景
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userService.resetPassword(1L, "custom123")).thenReturn(true);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNewPassword("custom123");

        // 执行请求
        mockMvc.perform(post("/api/users/1/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.defaultPassword").value(false));

        // 验证调用
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).resetPassword(1L, "custom123");
    }

    @Test
    void testResetUserPassword_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getUserById(999L)).thenReturn(null);

        // 创建请求对象
        PasswordResetRequest request = new PasswordResetRequest();

        // 执行请求
        mockMvc.perform(post("/api/users/999/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("用户不存在")));

        // 验证调用
        verify(userService, times(1)).getUserById(999L);
        verify(userService, times(0)).resetPassword(anyLong(), anyString());
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // 模拟行为
        when(userService.getUserById(1L)).thenReturn(testUser);

        // 执行请求
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));

        // 验证璋冪敤
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        // 模拟行为
        when(userService.getUserById(999L)).thenReturn(null);

        // 执行请求
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("用户不存在")));

        // 验证调用
        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    void testGetAllUsers() throws Exception {
        // 准备测试数据
        List<User> userList = Arrays.asList(testUser);

        // 模拟行为
        when(userService.getAllUsers()).thenReturn(userList);

        // 执行请求
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));

        // 验证调用
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testCreateUser_Success() throws Exception {
        // 模拟行为
        when(userService.createUser(any(User.class))).thenReturn(true);

        // 使用 DTO 作为入参
        com.mold.digitalization.dto.UserCreateRequest req = new com.mold.digitalization.dto.UserCreateRequest();
        req.setUsername("testuser");
        req.setPassword("password123");
        req.setRealName("测试用户");
        req.setPhone("13800138000");
        req.setStatus(1);

        // 执行请求
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testuser"));

        // 验证调用
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        // 模拟行为
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userService.updateUser(any(User.class))).thenReturn(true);

        // 使用 DTO 更新用户信息
        com.mold.digitalization.dto.UserUpdateRequest updatedReq = new com.mold.digitalization.dto.UserUpdateRequest();
        updatedReq.setRealName("更新后的用户");
        updatedReq.setPhone("13800138001");
        updatedReq.setStatus(1);

        // 执行请求
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReq)))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功响应体为空字符串

        // 验证调用
        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        // 模拟行为
        when(userService.getUserById(999L)).thenReturn(null);

        // 执行请求
        com.mold.digitalization.dto.UserUpdateRequest badReq = new com.mold.digitalization.dto.UserUpdateRequest();
        badReq.setRealName("测试用户");
        mockMvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badReq)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(userService, times(0)).updateUser(any(User.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        // 模拟行为
        when(userService.deleteUser(1L)).thenReturn(true);

        // 执行请求
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功响应体为空字符串

        // 验证调用
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        // 模拟行为
        when(userService.deleteUser(999L)).thenReturn(false);

        // 执行请求
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        // 验证调用
        verify(userService, times(1)).deleteUser(999L);
    }

    @Test
    void testLogin_Success() throws Exception {
        // 模拟行为
        when(userService.validateUser("testuser", "password123")).thenReturn(true);

        // 执行请求
        mockMvc.perform(post("/api/users/login")
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login successful")));

        // 验证调用
        verify(userService, times(1)).validateUser("testuser", "password123");
    }

    @Test
    void testLogin_Failure() throws Exception {
        // 模拟行为
        when(userService.validateUser("testuser", "wrongpassword")).thenReturn(false);

        // 执行请求
        mockMvc.perform(post("/api/users/login")
                .param("username", "testuser")
                .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid username or password")));

        // 验证调用
        verify(userService, times(1)).validateUser("testuser", "wrongpassword");
    }
}
