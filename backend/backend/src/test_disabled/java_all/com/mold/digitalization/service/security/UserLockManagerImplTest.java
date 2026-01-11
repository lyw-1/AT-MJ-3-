package com.mold.digitalization.service.security;

import com.mold.digitalization.entity.User;
import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.dao.UserMapper;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.config.UserLockProperties;
import com.mold.digitalization.manager.impl.UserLockManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * 用户閿佸畾绠＄悊鍣ㄥ疄鐜扮被鐨勫崟鍏冩祴璇? * 测试用户閿佸畾鍜岃В閿佺殑核心涓氬姟逻辑
 */
@ExtendWith(MockitoExtension.class)
public class UserLockManagerImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private UserLockProperties userLockProperties;

    @InjectMocks
    private UserLockManagerImpl userLockManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曠敤鎴?        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setLoginFailedCount(0);

        // 配置UserLockProperties鐨勯粯璁よ涓?        when(userLockProperties.getMaxFailedAttempts()).thenReturn(5);
        when(userLockProperties.getDurationMinutes()).thenReturn(30);
    }

    /**
     * 测试妫€鏌ユ湭閿佸畾用户 - 应该姝ｅ父閫氳繃
     */
    @Test
    void testCheckUserLockedStatus_NotLocked() {
        // 准备数据
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试 - 涓嶅簲璇ユ姏鍑哄紓甯?        assertDoesNotThrow(() -> userLockManager.checkUserLocked("testuser"));
        verify(userMapper).selectByUsername("testuser");
    }

    /**
     * 测试妫€鏌ュ凡閿佸畾用户 - 应该鎶涘嚭UserLockedException
     */
    @Test
    void testCheckUserLockedStatus_Locked() {
        // 准备数据
        testUser.setLockedUntil(LocalDateTime.now().plusHours(1)); // 1灏忔椂鍚庤В閿?        testUser.setLockReason("密码错误娆℃暟杩囧");
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // 执行测试 - 应该返回true琛ㄧず用户琚攣瀹?        assertTrue(userLockManager.checkUserLocked("testuser"));
        verify(userMapper).selectByUsername("testuser");
    }

    /**
     * 测试妫€鏌ュ凡杩囨湡鐨勯攣瀹?- 应该返回false
     */
    @Test
    void testCheckUserLockedStatus_ExpiredLock() {
        // 准备数据
        testUser.setLockedUntil(LocalDateTime.now().minusHours(1)); // 1灏忔椂鍓嶅凡杩囨湡
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // 执行测试 - 应该返回false锛屽洜涓洪攣瀹氬凡杩囨湡
        assertFalse(userLockManager.checkUserLocked("testuser"));
        
        // 验证查询操作
        verify(userMapper).selectByUsername("testuser");
    }

    /**
     * 测试鎵嬪姩閿佸畾用户
     */
    @Test
    void testManualLockUser() {
        // 准备数据
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // 执行测试
        userLockManager.manualLockUser("testuser", "测试閿佸畾", 60, 1L, "admin");

        // 验证閿佸畾操作
        verify(userMapper).selectByUsername("testuser");
        verify(userMapper).lockUser(eq(1L), any(LocalDateTime.class), eq("测试閿佸畾"));
    }

    /**
     * 测试鎵嬪姩瑙ｉ攣用户
     */
    @Test
    void testManualUnlockUser() {
        // 准备数据
        testUser.setLockReason("密码错误娆℃暟杩囧");
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // 执行测试
        userLockManager.manualUnlockUser("testuser", 1L, "admin");

        // 验证瑙ｉ攣操作
        verify(userMapper).selectByUsername("testuser");
        verify(userMapper).unlockUser(1L);
    }

    /**
     * 测试鑷姩閿佸畾用户锛堢櫥褰曞け璐ユ鏁拌揪鍒伴槇鍊硷級
     */
    @Test
    void testAutoLockUser() {
        // 准备数据
        testUser.setLoginFailedCount(5); // 宸茶揪鍒版渶澶уけ璐ユ鏁?        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // 执行测试
        userLockManager.autoLockUser("testuser", 5);

        // 验证閿佸畾操作
        verify(userMapper).selectByUsername("testuser");
        verify(userMapper).lockUser(eq(1L), any(LocalDateTime.class), eq("鐧诲綍失败娆℃暟杩囧"));
    }

    /**
     * 测试鑷姩瑙ｉ攣杩囨湡用户
     */
    @Test
    void testAutoUnlockExpiredUsers() {
        // 准备数据
        List<User> lockedUsers = Arrays.asList(testUser);
        when(userMapper.selectExpiredLockedUsers(any(LocalDateTime.class))).thenReturn(lockedUsers);

        // 执行测试
        userLockManager.autoUnlockExpiredUsers();

        // 验证瑙ｉ攣操作
        verify(userMapper).selectExpiredLockedUsers(any(LocalDateTime.class));
        verify(userMapper).unlockUser(1L);
    }
}
