package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.digitalization.mapper.RoleMapper;
import com.mold.digitalization.mapper.UserRoleMapper;
import com.mold.digitalization.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void testGetRoleById() {
        // 准备测试数据
        Long roleId = 1L;
        Role expectedRole = new Role();
        expectedRole.setId(roleId);
        expectedRole.setRoleName("管理员");
        expectedRole.setRoleCode("ADMIN");

        // 模拟行为
        when(roleService.getById(roleId)).thenReturn(expectedRole);

        // 执行测试
        Role actualRole = roleService.getRoleById(roleId);

        // 验证结果
        assertNotNull(actualRole);
        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
        verify(roleService, times(1)).getById(roleId);
    }

    @Test
    public void testGetRoleByCode() {
        // 准备测试数据
        String roleCode = "ADMIN";
        Role expectedRole = new Role();
        expectedRole.setId(1L);
        expectedRole.setRoleCode(roleCode);
        expectedRole.setRoleName("管理员");

        // 模拟行为
        when(roleMapper.selectByRoleCode(roleCode)).thenReturn(expectedRole);

        // 执行测试
        Role actualRole = roleService.getRoleByCode(roleCode);

        // 验证结果
        assertNotNull(actualRole);
        assertEquals(expectedRole.getRoleCode(), actualRole.getRoleCode());
        verify(roleMapper, times(1)).selectByRoleCode(roleCode);
    }

    @Test
    public void testGetRolesByUserId() {
        // 准备测试数据
        Long userId = 1L;
        List<Role> expectedRoles = Arrays.asList(
                new Role() {{ setId(1L); setRoleName("管理员"); }},
                new Role() {{ setId(2L); setRoleName("普通用户"); }}
        );

        // 模拟行为
        when(userRoleMapper.selectRolesByUserId(userId)).thenReturn(expectedRoles);

        // 执行测试
        List<Role> actualRoles = roleService.getRolesByUserId(userId);

        // 验证结果
        assertNotNull(actualRoles);
        assertEquals(expectedRoles.size(), actualRoles.size());
        verify(userRoleMapper, times(1)).selectRolesByUserId(userId);
    }

    @Test
    public void testGetRoleList() {
        // 准备测试数据
        int page = 1;
        int pageSize = 10;
        String keyword = "管理";

        Page<Role> pageParam = new Page<>(page, pageSize);
        List<Role> roleList = Arrays.asList(
                new Role() {{ setId(1L); setRoleName("管理员"); }}
        );
        pageParam.setRecords(roleList);
        pageParam.setTotal(1L);
        pageParam.setPages(1L);

        // 模拟行为
        when(roleMapper.selectPage(any(Page.class), anyMap())).thenReturn(pageParam);

        // 执行测试
        Map<String, Object> result = roleService.getRoleList(page, pageSize, keyword);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.get("total"));
        assertEquals(1, result.get("page"));
        assertEquals(10, result.get("pageSize"));
        assertEquals(1L, result.get("pages"));
        verify(roleMapper, times(1)).selectPage(any(Page.class), anyMap());
    }

    @Test
    public void testCreateRole() {
        // 准备测试数据
        Role role = new Role();
        role.setRoleName("新角色");
        role.setRoleCode("NEW_ROLE");

        // 模拟行为
        when(roleService.save(role)).thenReturn(true);

        // 执行测试
        boolean result = roleService.createRole(role);

        // 验证结果
        assertTrue(result);
        verify(roleService, times(1)).save(role);
    }

    @Test
    public void testUpdateRole() {
        // 准备测试数据
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("更新角色");
        role.setRoleCode("UPDATED_ROLE");

        // 模拟行为
        when(roleService.updateById(role)).thenReturn(true);

        // 执行测试
        boolean result = roleService.updateRole(role);

        // 验证结果
        assertTrue(result);
        verify(roleService, times(1)).updateById(role);
    }

    @Test
    public void testDeleteRole() {
        // 准备测试数据
        Long roleId = 1L;

        // 模拟行为
        when(roleService.removeById(roleId)).thenReturn(true);

        // 执行测试
        boolean result = roleService.deleteRole(roleId);

        // 验证结果
        assertTrue(result);
        verify(roleService, times(1)).removeById(roleId);
    }

    @Test
    public void testUpdateRoleStatus() {
        // 准备测试数据
        Long roleId = 1L;
        Integer newStatus = 1;

        Role role = new Role();
        role.setId(roleId);
        role.setStatus(0); // 原状态为禁用

        // 模拟行为
        when(roleService.getById(roleId)).thenReturn(role);
        when(roleService.updateById(role)).thenReturn(true);

        // 执行测试
        boolean result = roleService.updateRoleStatus(roleId, newStatus);

        // 验证结果
        assertTrue(result);
        assertEquals(newStatus, role.getStatus());
        verify(roleService, times(1)).getById(roleId);
        verify(roleService, times(1)).updateById(role);
    }

    @Test
    public void testUpdateRoleStatus_RoleNotFound() {
        // 准备测试数据
        Long roleId = 999L;
        Integer newStatus = 1;

        // 模拟行为
        when(roleService.getById(roleId)).thenReturn(null);

        // 执行测试
        boolean result = roleService.updateRoleStatus(roleId, newStatus);

        // 验证结果
        assertFalse(result);
        verify(roleService, times(1)).getById(roleId);
        verify(roleService, times(0)).updateById(any(Role.class));
    }

    @Test
    public void testGetAllRoles() {
        // 准备测试数据
        List<Role> expectedRoles = Arrays.asList(
                new Role() {{ setId(1L); setRoleName("管理员"); }},
                new Role() {{ setId(2L); setRoleName("普通用户"); }}
        );

        // 模拟行为
        when(roleService.list()).thenReturn(expectedRoles);

        // 执行测试
        List<Role> actualRoles = roleService.getAllRoles();

        // 验证结果
        assertNotNull(actualRoles);
        assertEquals(expectedRoles.size(), actualRoles.size());
        verify(roleService, times(1)).list();
    }

    @Test
    public void testIsRoleCodeExists_NewRole() {
        // 准备测试数据
        String roleCode = "NEW_ROLE";
        Long excludeId = null;

        // 模拟行为
        when(roleMapper.selectByRoleCode(roleCode)).thenReturn(null);

        // 执行测试
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);

        // 验证结果
        assertFalse(exists);
        verify(roleMapper, times(1)).selectByRoleCode(roleCode);
    }

    @Test
    public void testIsRoleCodeExists_ExistingRole() {
        // 准备测试数据
        String roleCode = "EXISTING_ROLE";
        Long excludeId = null;
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setRoleCode(roleCode);

        // 模拟行为
        when(roleMapper.selectByRoleCode(roleCode)).thenReturn(existingRole);

        // 执行测试
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);

        // 验证结果
        assertTrue(exists);
        verify(roleMapper, times(1)).selectByRoleCode(roleCode);
    }

    @Test
    public void testIsRoleCodeExists_SameRoleEditing() {
        // 准备测试数据
        String roleCode = "ROLE_TO_EDIT";
        Long excludeId = 1L;
        Role existingRole = new Role();
        existingRole.setId(excludeId); // 与排除ID相同
        existingRole.setRoleCode(roleCode);

        // 模拟行为
        when(roleMapper.selectByRoleCode(roleCode)).thenReturn(existingRole);

        // 执行测试
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);

        // 验证结果
        assertFalse(exists); // 编辑自己，不算重复
        verify(roleMapper, times(1)).selectByRoleCode(roleCode);
    }

    @Test
    public void testIsRoleCodeExists_DifferentRoleWithSameCode() {
        // 准备测试数据
        String roleCode = "DUPLICATE_CODE";
        Long excludeId = 2L;
        Role existingRole = new Role();
        existingRole.setId(1L); // 与排除ID不同
        existingRole.setRoleCode(roleCode);

        // 模拟行为
        when(roleMapper.selectByRoleCode(roleCode)).thenReturn(existingRole);

        // 执行测试
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);

        // 验证结果
        assertTrue(exists); // 其他角色使用相同代码，算重复
        verify(roleMapper, times(1)).selectByRoleCode(roleCode);
    }
}