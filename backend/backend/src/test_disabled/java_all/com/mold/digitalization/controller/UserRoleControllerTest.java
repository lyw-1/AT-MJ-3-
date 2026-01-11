package com.mold.digitalization.controller;

import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.service.UserRoleService;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import com.mold.digitalization.aspect.OperationLogAspect;
import com.mold.digitalization.config.TestConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(value = UserRoleController.class)
@Import(TestConfig.class)
public class UserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private User testUser;
    private Role testRole;
    private UserRole testUserRole;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRealName("测试用户");

        // 初始化测试角色
        testRole = new Role();
        testRole.setId(1L);
        testRole.setRoleName("管理员");
        testRole.setRoleCode("ADMIN");
        testRole.setStatus(1); // 启用状态
        // 初始化用户角色关系
        testUserRole = new UserRole();
        testUserRole.setUserId(1L);
        testUserRole.setRoleId(1L);
    }

    @Test
    void testGetUserRolesByUserId_Success() throws Exception {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);

        // 模拟行为
        when(userRoleService.getUserRolesByUserId(1L)).thenReturn(userRoles);

        // 执行璇锋眰
        mockMvc.perform(get("/api/user-roles/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].roleId").value(1L));

        // 验证璋冪敤
        verify(userRoleService, times(1)).getUserRolesByUserId(1L);
    }

    @Test
    void testGetUserRolesByRoleId_Success() throws Exception {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);

        // 模拟行为
        when(userRoleService.getUserRolesByRoleId(1L)).thenReturn(userRoles);

        // 执行璇锋眰
        mockMvc.perform(get("/api/user-roles/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].roleId").value(1L));

        // 验证璋冪敤
        verify(userRoleService, times(1)).getUserRolesByRoleId(1L);
    }

    @Test
    void testAssignRoleToUser_Success() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(roleService.getRoleById(1L)).thenReturn(testRole);
        when(userRoleService.assignRoleToUser(1L, 1L)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/assign")
                .param("userId", "1")
                .param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("瑙掕壊鍒嗛厤成功")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(roleService, times(1)).getRoleById(1L);
        verify(userRoleService, times(1)).assignRoleToUser(1L, 1L);
    }

    @Test
    void testAssignRoleToUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(999L)).thenReturn(null);

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/assign")
                .param("userId", "999")
                .param("roleId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("用户不存在")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(999L);
        verify(roleService, times(0)).getRoleById(anyLong());
        verify(userRoleService, times(0)).assignRoleToUser(anyLong(), anyLong());
    }

    @Test
    void testAssignRoleToUser_RoleNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(roleService.getRoleById(999L)).thenReturn(null);

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/assign")
                .param("userId", "1")
                .param("roleId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("角色不存在")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(roleService, times(1)).getRoleById(999L);
        verify(userRoleService, times(0)).assignRoleToUser(anyLong(), anyLong());
    }

    @Test
    void testAssignRoleToUser_AssignmentFailed() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(roleService.getRoleById(1L)).thenReturn(testRole);
        when(userRoleService.assignRoleToUser(1L, 1L)).thenReturn(false);

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/assign")
                .param("userId", "1")
                .param("roleId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("瑙掕壊鍒嗛厤失败")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(roleService, times(1)).getRoleById(1L);
        verify(userRoleService, times(1)).assignRoleToUser(1L, 1L);
    }

    @Test
    void testAssignRoleToUser_Exception() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(roleService.getRoleById(1L)).thenReturn(testRole);
        when(userRoleService.assignRoleToUser(1L, 1L)).thenThrow(new RuntimeException("Test exception"));

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/assign")
                .param("userId", "1")
                .param("roleId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("角色分配过程中发生异常")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(roleService, times(1)).getRoleById(1L);
        verify(userRoleService, times(1)).assignRoleToUser(1L, 1L);
    }

    @Test
    void testRemoveRoleFromUser_Success() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(userRoleService.removeRoleFromUser(1L, 1L)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(delete("/api/user-roles/remove")
                .param("userId", "1")
                .param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("瑙掕壊绉婚櫎成功")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(userRoleService, times(1)).removeRoleFromUser(1L, 1L);
    }

    @Test
    void testRemoveRoleFromUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(999L)).thenReturn(null);

        // 执行璇锋眰
        mockMvc.perform(delete("/api/user-roles/remove")
                .param("userId", "999")
                .param("roleId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("用户不存在")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(999L);
        verify(userRoleService, times(0)).removeRoleFromUser(anyLong(), anyLong());
    }

    @Test
    void testRemoveRoleFromUser_RoleNotAssigned() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(userRoleService.removeRoleFromUser(1L, 2L)).thenReturn(false); // 绉婚櫎鏈垎閰嶇殑瑙掕壊

        // 执行璇锋眰
        mockMvc.perform(delete("/api/user-roles/remove")
                .param("userId", "1")
                .param("roleId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("用户鏈嫢鏈夎瑙掕壊")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(userRoleService, times(1)).removeRoleFromUser(1L, 2L);
    }

    @Test
    void testRemoveRoleFromUser_Exception() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(userRoleService.removeRoleFromUser(1L, 1L)).thenThrow(new RuntimeException("Test exception"));

        // 执行璇锋眰
        mockMvc.perform(delete("/api/user-roles/remove")
                .param("userId", "1")
                .param("roleId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("角色移除过程中发生异常")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(userRoleService, times(1)).removeRoleFromUser(1L, 1L);
    }

    @Test
    void testBatchAssignRolesToUser_Success() throws Exception {
        // 模拟行为
        when(userService.getById(1L)).thenReturn(testUser);
        when(userRoleService.batchAssignRolesToUser(1L, Arrays.asList(1L, 2L, 3L))).thenReturn(true);

        // 执行请求 - 这里使用POST请求并将角色ID列表作为请求体
        mockMvc.perform(post("/api/user-roles/batch-assign")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2,3]"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("瑙掕壊鎵归噺鍒嗛厤成功")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(1L);
        verify(userRoleService, times(1)).batchAssignRolesToUser(1L, Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void testBatchAssignRolesToUser_UserNotFound() throws Exception {
        // 模拟行为
        when(userService.getById(999L)).thenReturn(null);

        // 执行璇锋眰
        mockMvc.perform(post("/api/user-roles/batch-assign")
                .param("userId", "999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2]")
                .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("用户不存在")));

        // 验证璋冪敤
        verify(userService, times(1)).getById(999L);
        verify(userRoleService, times(0)).batchAssignRolesToUser(anyLong(), anyList());
    }
}
