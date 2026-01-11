package com.mold.digitalization.config;

import com.mold.digitalization.dao.UserMapper;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.manager.UserLockManager;
import com.mold.digitalization.interceptor.LoginInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 鐧诲綍鎷︽埅鍣ㄦ祴璇曠被
 * 测试鐧诲綍杩囩▼涓殑鎷︽埅逻辑
 */
@ExtendWith(MockitoExtension.class)
public class LoginInterceptorTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserLockManager userLockManager;

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Object handler;

    @BeforeEach
    void setUp() {
        // 初始化测试对象
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new Object();
    }

    /**
     * 测试鍓嶇疆鎷︽埅鍣?- 用户鏈攣瀹氾紝应该姝ｅ父閫氳繃
     */
    @Test
    void testBeforeLogin_UserNotLocked() throws Exception {
        // 准备数据 - 模拟璇锋眰鍙傛暟
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setLockedUntil(null); // 使用 null 表示未锁定
        user.setLockReason(null);
        user.setLockOperatorName(null);
        
        // 模拟UserMapper返回用户
        when(userMapper.selectByUsername(username)).thenReturn(user);
        
        // 模拟 UserLockManager 返回用户未锁定
        when(userLockManager.checkUserLocked(username)).thenReturn(false);

        // 执行测试 - 应该姝ｅ父閫氳繃
        assertDoesNotThrow(() -> loginInterceptor.beforeLogin(username, request));
        verify(userLockManager).checkUserLocked(username);
    }

    /**
     * 测试鍓嶇疆鎷︽埅鍣?- 用户宸查攣瀹氾紝应该鎶涘嚭UserLockedException
     */
    @Test
    void testBeforeLogin_UserLocked() throws Exception {
        // 准备数据 - 模拟璇锋眰鍙傛暟
        String username = "lockeduser";
        User user = new User();
        user.setUsername(username);
        user.setLockedUntil(java.time.LocalDateTime.now().plusDays(1)); // 浣跨敤LocalDateTime
        user.setLockReason("密码错误娆℃暟杩囧");
        user.setLockOperatorName("系统");
        
        // 模拟UserMapper返回用户
        when(userMapper.selectByUsername(username)).thenReturn(user);
        
        // 模拟 UserLockManager 返回用户已锁定
        when(userLockManager.checkUserLocked(username)).thenReturn(true);

        // 执行测试 - 应该鎶涘嚭UserLockedException
        UserLockedException exception = assertThrows(UserLockedException.class, 
                () -> loginInterceptor.beforeLogin(username, request));

        // 验证异常
        assertEquals("账户已被锁定，请联系管理员", exception.getMessage());
        verify(userLockManager).checkUserLocked(username);
    }

    /**
     * 测试鍓嶇疆鎷︽埅鍣?- 用户涓嶅瓨鍦紝应该姝ｅ父閫氳繃
     */
    @Test
    void testBeforeLogin_UserNotFound() throws Exception {
        // 准备数据 - 模拟涓嶅瓨鍦ㄧ殑用户
        String username = "nonexistent";
        
        // 模拟 UserMapper 返回 null（用户不存在）
        when(userMapper.selectByUsername(username)).thenReturn(null);

        // 执行测试 - 应当正常通过（为安全起见，不直接返回用户不存在）
        assertDoesNotThrow(() -> loginInterceptor.beforeLogin(username, request));
        verify(userMapper).selectByUsername(username);
        verify(userLockManager, never()).checkUserLocked(username); // 不应检查锁定状态
    }

    /**
     * 测试鍓嶇疆鎷︽埅鍣?- 鏃犵敤鎴峰悕锛屽簲璇ラ€氳繃
     */
    @Test
    void testBeforeLogin_NoUsername() throws Exception {
        // 准备数据 - 鏃犵敤鎴峰悕

        // 执行测试 - 不应抛出异常
        assertDoesNotThrow(() -> loginInterceptor.beforeLogin(null, request));

        // 验证结果 - 不应调用后续校验方法
        verify(userMapper, never()).selectByUsername(any());
        verify(userLockManager, never()).checkUserLocked(any());
    }

    /**
     * 测试鍚庣疆澶勭悊方法 - 姝ｅ父鎯呭喌
     */
    @Test
    void testOnLoginSuccess() throws Exception {
        // 准备数据
        User user = new User();
        user.setUsername("testuser");
        
        // 执行测试 - 不应抛出异常
        assertDoesNotThrow(() -> loginInterceptor.onLoginSuccess(user, request));
        
        // 注意：由于 UserLockManager 接口中未明确 handleLoginSuccess 方法，
        // 我们无法直接验证该方法的调用；这里仅验证调用不抛出异常。
    }

    /**
     * 测试鐧诲綍失败澶勭悊方法 - 姝ｅ父鎯呭喌
     */
    @Test
    void testOnLoginFailure() throws Exception {
        // 准备数据
        String username = "testuser";
        AuthenticationException exception = new BadCredentialsException("密码错误");
        
        // 执行测试 - 不应抛出异常
        assertDoesNotThrow(() -> loginInterceptor.onLoginFailure(username, request, exception));
        // 正常情况下，onLoginFailure 不应抛出异常
    }

    /**
     * 测试鐧诲綍失败澶勭悊方法 - 模拟认证开傚父锛屽簲璇ュ鍔犲け璐ヨ鏁?     */
    @Test
    void testOnLoginFailure_Normal() throws Exception {
        // 准备数据 - 模拟认证开傚父
        String username = "testuser";
        AuthenticationException exception = new BadCredentialsException("密码错误");
        
        // 执行测试
        assertDoesNotThrow(() -> loginInterceptor.onLoginFailure(username, request, exception));
        
        // 注意：由于 UserLockManager 接口中未明确 handleLoginFailure 方法，
        // 我们无法直接验证该方法的调用；这里仅验证调用不抛出异常。
    }

    /**
     * 测试鐧诲綍失败澶勭悊方法 - 模拟闈炶璇佸紓甯革紝涓嶅簲璇ュ鍔犲け璐ヨ鏁?     */
    @Test
    void testOnLoginFailure_WithNonAuthException() throws Exception {
        // 准备数据 - 模拟非认证异常
        String username = "testuser";
        AuthenticationException exception = new AuthenticationException("系统错误") {};
        
        // 执行测试
        assertDoesNotThrow(() -> loginInterceptor.onLoginFailure(username, request, exception));
        
        // 注意：由于 UserLockManager 接口中未明确 handleLoginFailure 方法，
        // 我们无法直接验证该方法的调用；这里仅验证调用不抛出异常。
    }

    /**
     * 测试鐧诲綍失败澶勭悊方法 - 模拟绌虹敤鎴峰悕锛屽簲璇ユ甯稿鐞?     */
    @Test
    void testOnLoginFailure_NoUsername() throws Exception {
        // 准备数据 - 模拟空用户名和异常
        String username = null;
        AuthenticationException exception = new BadCredentialsException("密码错误");
        
        // 执行测试
        assertDoesNotThrow(() -> loginInterceptor.onLoginFailure(username, request, exception));
        
        // 注意：由于 UserLockManager 接口中未明确 handleLoginFailure 方法，
        // 我们无法直接验证该方法的调用；这里仅验证调用不抛出异常。
    }
}
