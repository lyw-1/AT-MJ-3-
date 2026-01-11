package com.mold.digitalization.integration;

import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.event.RolePermissionChangeEvent;
import com.mold.digitalization.event.RoleStatusChangeEvent;
import com.mold.digitalization.event.UserRoleChangeEvent;
import com.mold.digitalization.listener.PermissionChangeListener;
import com.mold.digitalization.service.PermissionCacheService;
import com.mold.digitalization.service.PermissionService;
import com.mold.digitalization.service.RoleService;
import com.mold.digitalization.service.UserRoleService;
import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 鏉冮檺缂撳瓨鍒锋柊鏈哄埗闆嗘垚测试绫? * 测试鏉冮檺缂撳瓨鍒锋柊鏈哄埗鐨勫畬鏁存祦绋? */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PermissionCacheIntegrationTest {

    @Autowired
    private PermissionCacheService permissionCacheService;

    @Autowired
    private PermissionChangeListener permissionChangeListener;

    @MockBean
    private UserService userService;

    @MockBean
    private PermissionService permissionService;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    private User testUser;
    private Role testRole;
    private Permission testPermission;
    private UserRole testUserRole;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // 创建测试瑙掕壊
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("TEST_ROLE");
        testRole.setStatus("ACTIVE");

        // 创建测试鏉冮檺
        testPermission = new Permission();
        testPermission.setId(1L);
        testPermission.setCode("TEST_PERMISSION");
        testPermission.setName("测试鏉冮檺");

        // 创建用户瑙掕壊鍏宠仈
        testUserRole = new UserRole();
        testUserRole.setId(1L);
        testUserRole.setUserId(testUser.getId());
        testUserRole.setRoleId(testRole.getId());
    }

    @Test
    void testUserRoleChangeEvent_RefreshesUserPermissions() {
        // 准备测试数据
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 创建用户瑙掕壊鍙樻洿浜嬩欢
        UserRoleChangeEvent event = new UserRoleChangeEvent(
                this, testUser.getId(), testRole.getId(), UserRoleChangeEvent.OperationType.ASSIGN);

        // 鍙戝竷浜嬩欢
        eventPublisher.publishEvent(event);

        // 验证结果
        verify(userService, timeout(1000)).getById(testUser.getId());
        verify(permissionService, timeout(1000)).getPermissionsByUserId(testUser.getId());
    }

    @Test
    void testRolePermissionChangeEvent_RefreshesUserPermissions() {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(userRoles);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 创建瑙掕壊鏉冮檺鍙樻洿浜嬩欢
        RolePermissionChangeEvent event = new RolePermissionChangeEvent(
                this, testRole.getId(), Arrays.asList(testPermission.getId()));

        // 鍙戝竷浜嬩欢
        eventPublisher.publishEvent(event);

        // 验证结果
        verify(userRoleService, timeout(1000)).getUserRolesByRoleId(testRole.getId());
        verify(userService, timeout(1000)).getById(testUser.getId());
        verify(permissionService, timeout(1000)).getPermissionsByUserId(testUser.getId());
    }

    @Test
    void testRoleStatusChangeEvent_RefreshesUserPermissions() {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(userRoles);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 创建瑙掕壊鐘舵€佸彉鏇翠簨浠?        RoleStatusChangeEvent event = new RoleStatusChangeEvent(
                this, testRole.getId(), "INACTIVE", "ACTIVE");

        // 鍙戝竷浜嬩欢
        eventPublisher.publishEvent(event);

        // 验证结果
        verify(userRoleService, timeout(1000)).getUserRolesByRoleId(testRole.getId());
        verify(userService, timeout(1000)).getById(testUser.getId());
        verify(permissionService, timeout(1000)).getPermissionsByUserId(testUser.getId());
    }

    @Test
    void testRefreshUserPermissions_DirectCall() {
        // 准备测试数据
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 鐩存帴璋冪敤鍒锋柊方法
        permissionCacheService.refreshUserPermissions(testUser.getId());

        // 验证结果
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
    }

    @Test
    void testRefreshAllUsersPermissions_DirectCall() {
        // 准备测试数据
        List<User> users = Arrays.asList(testUser);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userService.list()).thenReturn(users);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 鐩存帴璋冪敤鍒锋柊方法
        permissionCacheService.refreshAllUsersPermissions();

        // 验证结果
        verify(userService).list();
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
    }

    @Test
    void testRefreshUsersByRoleId_DirectCall() {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(userRoles);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);

        // 鐩存帴璋冪敤鍒锋柊方法
        permissionCacheService.refreshUsersByRoleId(testRole.getId());

        // 验证结果
        verify(userRoleService).getUserRolesByRoleId(testRole.getId());
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
    }
}