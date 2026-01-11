package com.mold.digitalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);
        testRole.setRoleName("管理员");
        testRole.setRoleCode("ADMIN");
        testRole.setDescription("系统管理员角色");
        testRole.setStatus(1); // 1表示启用
    }

    @Test
    void testGetRoleById_Success() throws Exception {
        // 模拟行为
        when(roleService.getRoleById(1L)).thenReturn(testRole);

        // 执行请求
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roleName").value("管理员"))
                .andExpect(jsonPath("$.roleCode").value("ADMIN"));

        // 验证调用
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void testGetRoleById_NotFound() throws Exception {
        // 模拟行为
        when(roleService.getRoleById(999L)).thenReturn(null);

        // 执行请求
        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("角色不存在")));

        // 验证调用
        verify(roleService, times(1)).getRoleById(999L);
    }

    @Test
    void testGetAllRoles() throws Exception {
        // 准备测试数据
        List<Role> roleList = Arrays.asList(testRole);

        // 模拟行为
        when(roleService.getAllRoles()).thenReturn(roleList);

        // 执行请求
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roleName").value("管理员"));

        // 验证调用
        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void testGetRoleList() throws Exception {
        // 准备测试数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", 1);
        resultMap.put("rows", Arrays.asList(testRole));

        // 模拟行为
        when(roleService.getRoleList(1, 10, null)).thenReturn(resultMap);

        // 执行请求
        mockMvc.perform(get("/api/roles/page")
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.rows.length()").value(1))
                .andExpect(jsonPath("$.rows[0].roleCode").value("ADMIN"));

        // 验证调用
        verify(roleService, times(1)).getRoleList(1, 10, null);
    }

    @Test
    void testCreateRole_Success() throws Exception {
        // 模拟行为
        when(roleService.createRole(any(Role.class))).thenReturn(true);
        when(roleService.isRoleCodeExists("ADMIN", null)).thenReturn(false);

        // 执行请求
        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功响应体为空字符串

        // 验证调用
        verify(roleService, times(1)).isRoleCodeExists("ADMIN", null);
        verify(roleService, times(1)).createRole(any(Role.class));
    }

    @Test
    void testCreateRole_RoleCodeExists() throws Exception {
        // 模拟行为
        when(roleService.isRoleCodeExists("ADMIN", null)).thenReturn(true);

        // 执行请求
        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("角色代码已存在")));

        // 验证调用
        verify(roleService, times(1)).isRoleCodeExists("ADMIN", null);
        verify(roleService, times(0)).createRole(any(Role.class));
    }

    @Test
    void testUpdateRole_Success() throws Exception {
        // 模拟行为
        when(roleService.isRoleCodeExists("ADMIN_UPDATED", 1L)).thenReturn(false);
        when(roleService.updateRole(any(Role.class))).thenReturn(true);

        // 修改角色信息
        Role updatedRole = new Role();
        updatedRole.setId(1L);
        updatedRole.setRoleName("超级管理员");
        updatedRole.setRoleCode("ADMIN_UPDATED");

        // 执行请求
        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功响应体为空字符串

        // 验证调用
        verify(roleService, times(1)).isRoleCodeExists("ADMIN_UPDATED", 1L);
        verify(roleService, times(1)).updateRole(any(Role.class));
    }

    @Test
    void testUpdateRole_RoleCodeExists() throws Exception {
        // 模拟行为
        when(roleService.isRoleCodeExists("ADMIN", 1L)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("瑙掕壊浠ｇ爜宸茶鍏朵粬瑙掕壊浣跨敤")));

        // 验证璋冪敤
        verify(roleService, times(1)).isRoleCodeExists("ADMIN", 1L);
        verify(roleService, times(0)).updateRole(any(Role.class));
    }

    @Test
    void testUpdateRole_NotFound() throws Exception {
        // 模拟行为
        when(roleService.isRoleCodeExists("ADMIN_UPDATED", 999L)).thenReturn(false);
        when(roleService.updateRole(any(Role.class))).thenReturn(false);

        // 执行璇锋眰
        mockMvc.perform(put("/api/roles/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("瑙掕壊涓嶅瓨鍦ㄦ垨更新失败")));

        // 验证璋冪敤
        verify(roleService, times(1)).isRoleCodeExists("ADMIN_UPDATED", 999L);
        verify(roleService, times(1)).updateRole(any(Role.class));
    }

    @Test
    void testDeleteRole_Success() throws Exception {
        // 模拟行为
        when(roleService.deleteRole(1L)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功鍝嶅簲浣撲负绌哄瓧绗︿覆

        // 验证璋冪敤
        verify(roleService, times(1)).deleteRole(1L);
    }

    @Test
    void testDeleteRole_NotFound() throws Exception {
        // 模拟行为
        when(roleService.deleteRole(999L)).thenReturn(false);

        // 执行璇锋眰
        mockMvc.perform(delete("/api/roles/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("瑙掕壊涓嶅瓨鍦ㄦ垨删除失败")));

        // 验证璋冪敤
        verify(roleService, times(1)).deleteRole(999L);
    }

    @Test
    void testUpdateRoleStatus_Success() throws Exception {
        // 模拟行为
        when(roleService.updateRoleStatus(1L, 0)).thenReturn(true);

        // 执行璇锋眰
        mockMvc.perform(put("/api/roles/1/status")
                .param("status", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // 成功鍝嶅簲浣撲负绌哄瓧绗︿覆

        // 验证璋冪敤
        verify(roleService, times(1)).updateRoleStatus(1L, 0);
    }

    @Test
    void testUpdateRoleStatus_NotFound() throws Exception {
        // 模拟行为
        when(roleService.updateRoleStatus(999L, 0)).thenReturn(false);

        // 执行璇锋眰
        mockMvc.perform(put("/api/roles/999/status")
                .param("status", "0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("角色不存在或状态更新失败")));

        // 验证璋冪敤
        verify(roleService, times(1)).updateRoleStatus(999L, 0);
    }
}