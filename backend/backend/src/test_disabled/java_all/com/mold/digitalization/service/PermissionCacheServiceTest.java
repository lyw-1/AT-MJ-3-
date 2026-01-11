package com.mold.digitalization.service;

import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.event.RolePermissionChangeEvent;
import com.mold.digitalization.event.RoleStatusChangeEvent;
import com.mold.digitalization.event.UserRoleChangeEvent;
import com.mold.digitalization.listener.PermissionChangeListener;
import com.mold.digitalization.service.impl.PermissionCacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 鏉冮檺缂撳瓨服务测试绫? * 测试鏉冮檺缂撳瓨鍒锋柊鏈哄埗鐨勫姛鑳? */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PermissionCacheServiceTest {

    @Mock
    private RedisService redisService;

    @Mock
    private UserService userService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private PermissionCacheServiceImpl permissionCacheService;

    @Mock
    private PermissionChangeListener permissionChangeListener;

    @Mock
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
    void testRefreshUserPermissions() {
        // 准备测试数据
        List<Permission> permissions = Arrays.asList(testPermission);
        String[] permissionCodes = permissions.stream()
                .map(Permission::getCode)
                .toArray(String[]::new);

        // 模拟服务璋冪敤
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionCacheService.refreshUserPermissions(testUser.getId());

        // 验证结果
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), permissionCodes);
    }

    @Test
    void testRefreshUserPermissions_UserNotExists() {
        // 模拟用户涓嶅瓨鍦?        when(userService.getById(testUser.getId())).thenReturn(null);

        // 执行测试
        permissionCacheService.refreshUserPermissions(testUser.getId());

        // 验证结果
        verify(userService).getById(testUser.getId());
        verify(permissionService, never()).getPermissionsByUserId(anyLong());
        verify(redisService, never()).sAdd(anyString(), any(String[].class));
    }

    @Test
    void testRefreshUserPermissions_NoPermissions() {
        // 模拟用户娌℃湁鏉冮檺
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(Arrays.asList());

        // 执行测试
        permissionCacheService.refreshUserPermissions(testUser.getId());

        // 验证结果
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService, never()).sAdd(anyString(), any(String[].class));
    }

    @Test
    void testRefreshAllUsersPermissions() {
        // 准备测试数据
        List<User> users = Arrays.asList(testUser);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userService.list()).thenReturn(users);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionCacheService.refreshAllUsersPermissions();

        // 验证结果
        verify(userService).list();
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), any(String[].class));
    }

    @Test
    void testRefreshUsersByRoleId() {
        // 准备测试数据
        List<UserRole> userRoles = Arrays.asList(testUserRole);
        List<Permission> permissions = Arrays.asList(testPermission);

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(userRoles);
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(permissions);
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionCacheService.refreshUsersByRoleId(testRole.getId());

        // 验证结果
        verify(userRoleService).getUserRolesByRoleId(testRole.getId());
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), any(String[].class));
    }

    @Test
    void testClearUserPermissions() {
        // 执行测试
        permissionCacheService.clearUserPermissions(testUser.getId());

        // 验证结果
        verify(redisService).delete("user:permissions:" + testUser.getId());
    }

    @Test
    void testClearAllUsersPermissions() {
        // 准备测试数据
        Set<String> keys = Set.of("user:permissions:1", "user:permissions:2");

        // 模拟服务璋冪敤
        when(redisService.keys("user:permissions:*")).thenReturn(keys);

        // 执行测试
        permissionCacheService.clearAllUsersPermissions();

        // 验证结果
        verify(redisService).keys("user:permissions:*");
        verify(redisService, times(keys.size())).delete(anyString());
    }

    @Test
    void testPermissionChangeListener_UserRoleChangeEvent() {
        // 准备测试浜嬩欢
        UserRoleChangeEvent event = new UserRoleChangeEvent(
                this, testUser.getId(), testRole.getId(), UserRoleChangeEvent.OperationType.ASSIGN);

        // 模拟服务璋冪敤
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(Arrays.asList(testPermission));
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionChangeListener.handleUserRoleChange(event);

        // 验证结果
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), any(String[].class));
    }

    @Test
    void testPermissionChangeListener_RolePermissionChangeEvent() {
        // 准备测试浜嬩欢
        RolePermissionChangeEvent event = new RolePermissionChangeEvent(
                this, testRole.getId(), Arrays.asList(testPermission.getId()));

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(Arrays.asList(testUserRole));
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(Arrays.asList(testPermission));
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionChangeListener.handleRolePermissionChange(event);

        // 验证结果
        verify(userRoleService).getUserRolesByRoleId(testRole.getId());
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), any(String[].class));
    }

    @Test
    void testPermissionChangeListener_RoleStatusChangeEvent() {
        // 准备测试浜嬩欢
        RoleStatusChangeEvent event = new RoleStatusChangeEvent(
                this, testRole.getId(), "INACTIVE", "ACTIVE");

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(Arrays.asList(testUserRole));
        when(userService.getById(testUser.getId())).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(testUser.getId())).thenReturn(Arrays.asList(testPermission));
        when(redisService.sAdd(anyString(), any(String[].class))).thenReturn(1L);

        // 执行测试
        permissionChangeListener.handleRoleStatusChange(event);

        // 验证结果
        verify(userRoleService).getUserRolesByRoleId(testRole.getId());
        verify(userService).getById(testUser.getId());
        verify(permissionService).getPermissionsByUserId(testUser.getId());
        verify(redisService).delete("user:permissions:" + testUser.getId());
        verify(redisService).sAdd("user:permissions:" + testUser.getId(), any(String[].class));
    }
}