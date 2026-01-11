package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.MailService;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.UserLockService;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.config.PasswordResetCacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AuthServiceImpl测试绫? * 涓昏测试管理员樺瘑鐮侀噸缃姛鑳? */
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisService redisService;

    @Mock
    private UserLockService userLockService;

    @Mock
    private MailService mailService;

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private PasswordResetCacheConfig passwordResetCacheConfig;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曠敤鎴?        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiM");
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        // email瀛楁宸叉寜瑕佹眰绉婚櫎
        testUser.setStatus(1); // 鍚敤状态
        // 配置PasswordResetCacheConfig鐨勯粯璁よ涓?        when(passwordResetCacheConfig.getResetCodeKey(anyLong())).thenReturn("reset_code:1:");
        when(passwordResetCacheConfig.getResetCodeExpireTime()).thenReturn(900L); // 15鍒嗛挓
        when(passwordResetCacheConfig.getMinSendInterval()).thenReturn(60); // 1鍒嗛挓
        when(passwordResetCacheConfig.getMaxSendTimesPerDay()).thenReturn(5);
        when(passwordResetCacheConfig.getSendCountKey(anyLong())).thenReturn("send_count:1:");
        when(passwordResetCacheConfig.getLastSendTimeKey(anyLong())).thenReturn("last_send_time:1:");
    }

    @Test
    void testResetPassword_Success() {
        // 准备测试数据
        Long userId = 1L;
        String newPassword = "NewPassword123!";

        // 模拟行为
        when(userService.getById(userId)).thenReturn(testUser);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userService.updateById(any(User.class))).thenReturn(true);

        // 执行测试
        boolean result = authService.resetPassword(userId, newPassword);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userService, times(1)).updateById(any(User.class));
        verify(userLockService, times(1)).unlockUser(userId);
        verify(redisService, times(1)).delete(anyString());
        verify(operationLogService, times(1)).saveOperationLog(any());
    }

    @Test
    void testResetPassword_UserNotFound() {
        // 准备测试数据
        Long userId = 999L;
        String newPassword = "NewPassword123!";

        // 模拟行为
        when(userService.getById(userId)).thenReturn(null);

        // 执行测试
        boolean result = authService.resetPassword(userId, newPassword);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getById(userId);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userService, never()).updateById(any(User.class));
        verify(userLockService, never()).unlockUser(anyLong());
    }

    @Test
    void testResetPassword_DatabaseUpdateFailure() {
        // 准备测试数据
        Long userId = 1L;
        String newPassword = "NewPassword123!";

        // 模拟行为
        when(userService.getById(userId)).thenReturn(testUser);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userService.updateById(any(User.class))).thenReturn(false);

        // 执行测试
        boolean result = authService.resetPassword(userId, newPassword);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userService, times(1)).updateById(any(User.class));
        verify(userLockService, never()).unlockUser(userId);
    }

    @Test
    void testResetPassword_WithNullPassword() {
        // 准备测试数据
        Long userId = 1L;

        // 模拟行为
        when(userService.getById(userId)).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.updateById(any(User.class))).thenReturn(true);

        // 执行测试
        boolean result = authService.resetPassword(userId, null);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getById(userId);
        verify(passwordEncoder, times(1)).encode(any()); // 验证鐢熸垚浜嗛殢鏈哄瘑鐮?        verify(userService, times(1)).updateById(any(User.class));
        verify(userLockService, times(1)).unlockUser(userId);
    }

    @Test
    void testSendResetPasswordCode_Success() {
        // 准备测试数据
        String username = "testuser";

        // 模拟行为
        when(userService.getUserByUsername(username)).thenReturn(testUser);
        when(redisService.get(anyString())).thenReturn(null); // 娌℃湁鍙戦€佽褰?        when(redisService.keys(anyString())).thenReturn(new HashSet<String>()); // 娌℃湁閲嶇疆鐮?        when(mailService.sendPasswordResetMail(anyString(), anyString(), anyInt())).thenReturn(true);

        // 执行测试
        boolean result = authService.sendResetPasswordCode(username);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getUserByUsername(username);
        verify(redisService, times(1)).set(anyString(), anyString(), anyLong());
        verify(mailService, times(1)).sendPasswordResetMail(eq(testUser.getPhone()), anyString(), anyInt());
    }

    @Test
    void testSendResetPasswordCode_UserNotFound() {
        // 准备测试数据
        String username = "nonexistentuser";

        // 模拟行为
        when(userService.getUserByUsername(username)).thenReturn(null);
        when(userService.getUserByEmail(username)).thenReturn(null);
        when(userService.getUserByPhone(username)).thenReturn(null);

        // 执行测试
        boolean result = authService.sendResetPasswordCode(username);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getUserByUsername(username);
        verify(userService, times(1)).getUserByEmail(username);
        verify(userService, times(1)).getUserByPhone(username);
        verify(redisService, never()).set(anyString(), anyString(), anyLong());
        verify(mailService, never()).sendPasswordResetMail(anyString(), anyString(), anyInt());
    }

    @Test
    void testSendResetPasswordCode_SendFrequencyLimit() {
        // 准备测试数据
        String username = "testuser";
        long currentTime = System.currentTimeMillis() / 1000;
        long lastSendTime = currentTime - 30; // 30绉掑墠鍙戦€侊紝灏忎簬60绉掔殑闄愬埗

        // 模拟行为
        when(userService.getUserByUsername(username)).thenReturn(testUser);
        when(redisService.get(contains("last_send_time"))).thenReturn(String.valueOf(lastSendTime));

        // 执行测试
        boolean result = authService.sendResetPasswordCode(username);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getUserByUsername(username);
        verify(redisService, never()).set(anyString(), anyString(), anyLong());
        verify(mailService, never()).sendPasswordResetMail(anyString(), anyString(), anyInt());
    }

    @Test
    void testSendResetPasswordCode_SendCountLimit() {
        // 准备测试数据
        String username = "testuser";

        // 模拟行为
        when(userService.getUserByUsername(username)).thenReturn(testUser);
        when(redisService.get(contains("last_send_time"))).thenReturn(null); // 娌℃湁鍙戦€佹椂闂磋褰?        when(redisService.get(contains("send_count"))).thenReturn("5"); // 宸茶揪鍒版瘡鏃ュ彂閫佷笂闄?
        // 执行测试
        boolean result = authService.sendResetPasswordCode(username);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(userService, times(1)).getUserByUsername(username);
        verify(redisService, never()).set(anyString(), anyString(), anyLong());
        verify(mailService, never()).sendPasswordResetMail(anyString(), anyString(), anyInt());
    }

    @Test
    void testValidateResetPasswordCode_Success() {
        // 准备测试数据
        String resetCode = "123456";
        Set<String> keys = new HashSet<>();
        keys.add("reset_code:1:");

        // 模拟行为
        when(redisService.keys(contains("reset_code"))).thenReturn(keys);
        when(redisService.get("reset_code:1:")).thenReturn(resetCode);

        // 执行测试
        boolean result = authService.validateResetPasswordCode(resetCode);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(redisService, times(1)).keys(contains("reset_code"));
        verify(redisService, times(1)).get("reset_code:1:");
    }

    @Test
    void testValidateResetPasswordCode_Failure() {
        // 准备测试数据
        String resetCode = "123456";
        Set<String> keys = new HashSet<>();
        keys.add("reset_code:1:");

        // 模拟行为
        when(redisService.keys(contains("reset_code"))).thenReturn(keys);
        when(redisService.get("reset_code:1:")).thenReturn("654321"); // 涓嶅悓鐨勯獙璇佺爜

        // 执行测试
        boolean result = authService.validateResetPasswordCode(resetCode);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(redisService, times(1)).keys(contains("reset_code"));
        verify(redisService, times(1)).get("reset_code:1:");
    }

    @Test
    void testResetPasswordByCode_Success() {
        // 准备测试数据
        String resetCode = "123456";
        String newPassword = "NewPassword123!";
        Set<String> keys = new HashSet<>();
        keys.add("reset_code:1:");

        // 模拟行为
        when(redisService.keys(contains("reset_code"))).thenReturn(keys);
        when(redisService.get("reset_code:1:")).thenReturn(resetCode);
        when(userService.getById(1L)).thenReturn(testUser);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userService.updateById(any(User.class))).thenReturn(true);

        // 执行测试
        boolean result = authService.resetPasswordByCode(resetCode, newPassword);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(redisService, times(1)).keys(contains("reset_code"));
        verify(redisService, times(1)).get("reset_code:1:");
        verify(redisService, times(1)).delete("reset_code:1:");
        verify(userService, times(1)).getById(1L);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userService, times(1)).updateById(any(User.class));
    }

    @Test
    void testResetPasswordByCode_InvalidCode() {
        // 准备测试数据
        String resetCode = "123456";
        String newPassword = "NewPassword123!";
        Set<String> keys = new HashSet<>();
        keys.add("reset_code:1:");

        // 模拟行为
        when(redisService.keys(contains("reset_code"))).thenReturn(keys);
        when(redisService.get("reset_code:1:")).thenReturn("654321"); // 涓嶅悓鐨勯獙璇佺爜

        // 执行测试
        boolean result = authService.resetPasswordByCode(resetCode, newPassword);

        // 验证结果
        assertFalse(result);
        
        // 验证璋冪敤
        verify(redisService, times(1)).keys(contains("reset_code"));
        verify(redisService, times(1)).get("reset_code:1:");
        verify(redisService, never()).delete(anyString());
        verify(userService, never()).getById(anyLong());
    }

    @Test
    void testUnlockUser_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 模拟行为
        when(userLockService.unlockUser(userId)).thenReturn(true);
        when(userService.getById(userId)).thenReturn(testUser);

        // 执行测试
        boolean result = authService.unlockUser(userId);

        // 验证结果
        assertTrue(result);
        
        // 验证璋冪敤
        verify(userLockService, times(1)).unlockUser(userId);
        verify(userService, times(1)).getById(userId);
    }
}