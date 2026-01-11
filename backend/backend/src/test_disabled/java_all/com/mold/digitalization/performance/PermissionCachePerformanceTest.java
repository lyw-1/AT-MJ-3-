package com.mold.digitalization.performance;

import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.service.PermissionCacheService;
import com.mold.digitalization.service.PermissionService;
import com.mold.digitalization.service.UserRoleService;
import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 鏉冮檺缂撳瓨鍒锋柊鏈哄埗鎬ц兘测试绫? * 测试鏉冮檺缂撳瓨鍒锋柊鏈哄埗鐨勬€ц兘琛ㄧ幇
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PermissionCachePerformanceTest {

    @Autowired
    private PermissionCacheService permissionCacheService;

    @MockBean
    private UserService userService;

    @MockBean
    private PermissionService permissionService;

    @MockBean
    private UserRoleService userRoleService;

    private List<User> testUsers;
    private List<Permission> testPermissions;
    private Role testRole;

    @BeforeEach
    void setUp() {
        // 创建测试瑙掕壊
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("TEST_ROLE");
        testRole.setStatus("ACTIVE");

        // 创建测试鏉冮檺
        testPermissions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Permission permission = new Permission();
            permission.setId((long) i);
            permission.setCode("PERMISSION_" + i);
            permission.setName("鏉冮檺_" + i);
            testPermissions.add(permission);
        }

        // 创建测试用户
        testUsers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("user_" + i);
            testUsers.add(user);
        }
    }

    @Test
    void testRefreshUserPermissions_SingleUser_Performance() {
        // 准备测试数据
        User user = testUsers.get(0);

        // 模拟服务璋冪敤
        when(userService.getById(user.getId())).thenReturn(user);
        when(permissionService.getPermissionsByUserId(user.getId())).thenReturn(testPermissions);

        // 执行鎬ц兘测试
        long startTime = System.currentTimeMillis();
        permissionCacheService.refreshUserPermissions(user.getId());
        long endTime = System.currentTimeMillis();

        // 验证结果
        long executionTime = endTime - startTime;
        System.out.println("鍒锋柊鍗曚釜用户鏉冮檺缂撳瓨鑰楁椂: " + executionTime + "ms");
        
        // 验证服务璋冪敤
        verify(userService).getById(user.getId());
        verify(permissionService).getPermissionsByUserId(user.getId());
        
        // 鎬ц兘鏂█ - 鍗曚釜用户鍒锋柊搴斿湪100ms内容畬鎴?        assertTrue(executionTime < 100, "鍗曚釜用户鏉冮檺缂撳瓨鍒锋柊鏃堕棿搴斿皬浜?00ms");
    }

    @Test
    void testRefreshAllUsersPermissions_Performance() {
        // 模拟服务璋冪敤
        when(userService.list()).thenReturn(testUsers);
        when(userService.getById(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            return testUsers.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        });
        when(permissionService.getPermissionsByUserId(anyLong())).thenReturn(testPermissions);

        // 执行鎬ц兘测试
        long startTime = System.currentTimeMillis();
        permissionCacheService.refreshAllUsersPermissions();
        long endTime = System.currentTimeMillis();

        // 验证结果
        long executionTime = endTime - startTime;
        System.out.println("鍒锋柊鎵€鏈夌敤鎴锋潈闄愮紦瀛樿€楁椂: " + executionTime + "ms");
        
        // 验证服务璋冪敤
        verify(userService).list();
        verify(userService, times(testUsers.size())).getById(anyLong());
        verify(permissionService, times(testUsers.size())).getPermissionsByUserId(anyLong());
        
        // 性能阈值 - 100个用户刷新应在5秒内完成
        assertTrue(executionTime < 5000, "所有用户权限缓存刷新时间应小于5秒");
    }

    @Test
    void testRefreshUsersByRoleId_Performance() {
        // 准备测试数据
        List<UserRole> userRoles = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            UserRole userRole = new UserRole();
            userRole.setId((long) i);
            userRole.setUserId((long) i);
            userRole.setRoleId(testRole.getId());
            userRoles.add(userRole);
        }

        // 模拟服务璋冪敤
        when(userRoleService.getUserRolesByRoleId(testRole.getId())).thenReturn(userRoles);
        when(userService.getById(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            return testUsers.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        });
        when(permissionService.getPermissionsByUserId(anyLong())).thenReturn(testPermissions);

        // 执行鎬ц兘测试
        long startTime = System.currentTimeMillis();
        permissionCacheService.refreshUsersByRoleId(testRole.getId());
        long endTime = System.currentTimeMillis();

        // 验证结果
        long executionTime = endTime - startTime;
        System.out.println("鎸夎鑹插埛鏂扮敤鎴锋潈闄愮紦瀛樿€楁椂: " + executionTime + "ms");
        
        // 验证服务璋冪敤
        verify(userRoleService).getUserRolesByRoleId(testRole.getId());
        verify(userService, times(userRoles.size())).getById(anyLong());
        verify(permissionService, times(userRoles.size())).getPermissionsByUserId(anyLong());
        
        // 性能阈值 - 50个用户刷新应在3秒内完成
        assertTrue(executionTime < 3000, "按角色刷新用户权限缓存时间应小于3秒");
    }

    @Test
    void testConcurrentRefreshUserPermissions_Performance() throws InterruptedException {
        // 模拟服务璋冪敤
        when(userService.getById(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            return testUsers.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        });
        when(permissionService.getPermissionsByUserId(anyLong())).thenReturn(testPermissions);

        // 创建绾跨▼姹?        ExecutorService executorService = Executors.newFixedThreadPool(10);
        
        // 执行骞跺彂鎬ц兘测试
        long startTime = System.currentTimeMillis();
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (User user : testUsers) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                permissionCacheService.refreshUserPermissions(user.getId());
            }, executorService);
            futures.add(future);
        }
        
        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        
        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // 验证结果
        long executionTime = endTime - startTime;
        System.out.println("骞跺彂鍒锋柊100涓敤鎴锋潈闄愮紦瀛樿€楁椂: " + executionTime + "ms");
        
        // 验证服务璋冪敤
        verify(userService, times(testUsers.size())).getById(anyLong());
        verify(permissionService, times(testUsers.size())).getPermissionsByUserId(anyLong());
        
        // 性能阈值 - 并发刷新100个用户应在2秒内完成
        assertTrue(executionTime < 2000, "并发刷新100个用户权限缓存时间应小于2秒");
    }
}
