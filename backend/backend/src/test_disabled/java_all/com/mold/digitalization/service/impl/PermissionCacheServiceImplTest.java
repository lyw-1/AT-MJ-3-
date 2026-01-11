package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.PermissionService;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PermissionCacheServiceImplTest {

    @Mock
    private RedisService redisService;

    @Mock
    private UserService userService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionCacheServiceImpl permissionCacheService;

    private User testUser;
    private Permission testPermission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        
        testPermission = new Permission();
        testPermission.setId(1L);
        testPermission.setCode("user:view");
    }

    @Test
    void testRefreshUserPermissions_Success() {
        // 模拟用户存在
        when(userService.getById(1L)).thenReturn(testUser);
        // 模拟用户有权限
        when(permissionService.getPermissionsByUserId(1L)).thenReturn(List.of(testPermission));
        
        // 执行测试方法
        permissionCacheService.refreshUserPermissions(1L);
        
        // 验证方法调用
        verify(userService).getById(1L);
        verify(redisService).delete("user:permissions:1");
        verify(permissionService).getPermissionsByUserId(1L);
        verify(redisService).sAdd(eq("user:permissions:1"), eq("user:view"));
    }

    @Test
    void testRefreshUserPermissions_UserNotFound() {
        // 模拟用户不存在
        when(userService.getById(1L)).thenReturn(null);
        
        // 执行测试方法
        permissionCacheService.refreshUserPermissions(1L);
        
        // 验证方法调用
        verify(userService).getById(1L);
        verify(redisService, never()).delete(anyString());
        verify(permissionService, never()).getPermissionsByUserId(anyLong());
    }

    @Test
    void testRefreshUserPermissions_NoPermissions() {
        // 模拟用户存在但没有权限
        when(userService.getById(1L)).thenReturn(testUser);
        when(permissionService.getPermissionsByUserId(1L)).thenReturn(Collections.emptyList());
        
        // 执行测试方法
        permissionCacheService.refreshUserPermissions(1L);
        
        // 验证方法调用
        verify(userService).getById(1L);
        verify(redisService).delete("user:permissions:1");
        verify(permissionService).getPermissionsByUserId(1L);
        verify(redisService, never()).sAdd(anyString(), anyString());
    }

    @Test
    void testRefreshAllUsersPermissions() {
        // 模拟有多个用户
        List<User> users = List.of(testUser, new User());
        users.get(1).setId(2L);
        
        when(userService.list()).thenReturn(users);
        when(userService.getById(1L)).thenReturn(testUser);
        when(userService.getById(2L)).thenReturn(users.get(1));
        when(permissionService.getPermissionsByUserId(anyLong())).thenReturn(Collections.emptyList());
        
        // 执行测试方法
        permissionCacheService.refreshAllUsersPermissions();
        
        // 验证方法调用
        verify(userService).list();
        verify(redisService, times(2)).delete(anyString());
        verify(permissionService, times(2)).getPermissionsByUserId(anyLong());
    }

    @Test
    void testClearUserPermissions() {
        // 执行测试方法
        permissionCacheService.clearUserPermissions(1L);
        
        // 验证方法调用
        verify(redisService).delete("user:permissions:1");
    }

    @Test
    void testClearAllUsersPermissions() {
        // 模拟有多个用户权限缓存键
        Set<String> keys = Set.of("user:permissions:1", "user:permissions:2");
        when(redisService.keys("user:permissions:*")).thenReturn(keys);
        
        // 执行测试方法
        permissionCacheService.clearAllUsersPermissions();
        
        // 验证方法调用
        verify(redisService).keys("user:permissions:*");
        // 验证每个键都被删除
        for (String key : keys) {
            verify(redisService).delete(key);
        }
    }
}