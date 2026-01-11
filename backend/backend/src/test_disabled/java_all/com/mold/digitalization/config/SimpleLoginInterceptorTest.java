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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 鐧诲綍鎷︽埅鍣ㄦ祴璇曠被
 * 测试鐧诲綍杩囩▼涓殑鎷︽埅逻辑
 */
@ExtendWith(MockitoExtension.class)
public class SimpleLoginInterceptorTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserLockManager userLockManager;

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曞璞?        request = new MockHttpServletRequest();
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
        user.setLockedUntil(null); // 浣跨敤null琛ㄧず鏈攣瀹?        user.setLockReason(null);
        user.setLockOperatorName(null);
        
        // 模拟UserMapper返回用户
        when(userMapper.selectByUsername(username)).thenReturn(user);
        
        // 模拟UserLockManager返回用户鏈攣瀹?        when(userLockManager.checkUserLocked(username)).thenReturn(false);

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
        
        // 模拟UserLockManager返回用户宸查攣瀹?        when(userLockManager.checkUserLocked(username)).thenReturn(true);

        // 执行测试 - 应该鎶涘嚭UserLockedException
        UserLockedException exception = assertThrows(UserLockedException.class, 
                () -> loginInterceptor.beforeLogin(username, request));

        // 验证开傚父
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
        
        // 模拟UserMapper返回null锛堢敤鎴蜂笉瀛樺湪锛?        when(userMapper.selectByUsername(username)).thenReturn(null);

        // 执行测试 - 应该姝ｅ父閫氳繃锛堜负浜嗗畨鍏紝涓嶇洿鎺ヨ繑鍥炵敤鎴蜂笉瀛樺湪锛?        assertDoesNotThrow(() -> loginInterceptor.beforeLogin(username, request));
        verify(userMapper).selectByUsername(username);
        verify(userLockManager, never()).checkUserLocked(username); // 涓嶅簲璇ユ鏌ラ攣瀹氱姸鎬?    }

    /**
     * 测试鍓嶇疆鎷︽埅鍣?- 鏃犵敤鎴峰悕锛屽簲璇ラ€氳繃
     */
    @Test
    void testBeforeLogin_NoUsername() throws Exception {
        // 执行测试 - 涓嶅簲璇ユ姏鍑哄紓甯?        assertDoesNotThrow(() -> loginInterceptor.beforeLogin(null, request));

        // 验证结果 - 涓嶅簲璇ヨ皟鐢ㄤ换浣曟鏌ユ柟娉?        verify(userMapper, never()).selectByUsername(any());
        verify(userLockManager, never()).checkUserLocked(any());
    }
}
