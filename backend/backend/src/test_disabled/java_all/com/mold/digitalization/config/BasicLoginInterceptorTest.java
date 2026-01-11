package com.mold.digitalization.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.interceptor.LoginInterceptor;
import com.mold.digitalization.manager.UserLockManager;
import com.mold.digitalization.dao.UserMapper;
import com.mold.digitalization.dto.UserQueryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.ibatis.annotations.Param;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 绠€鍖栫殑鐧诲綍鎷︽埅鍣ㄦ祴璇曠被
 * 涓嶄緷璧朚ockito锛屼娇鐢ㄨ嚜瀹氫箟模拟瀵硅薄
 */
public class BasicLoginInterceptorTest {

    private LoginInterceptor loginInterceptor;
    private MockUserMapper userMapper;
    private MockPasswordEncoder passwordEncoder;
    private MockUserLockManager userLockManager;

    @BeforeEach
    void setUp() {
        userMapper = new MockUserMapper();
        passwordEncoder = new MockPasswordEncoder();
        userLockManager = new MockUserLockManager();

        // 创建LoginInterceptor实例
        loginInterceptor = new LoginInterceptor();

        // 浣跨敤鍙嶅皠设置绉佹湁瀛楁
        try {
            java.lang.reflect.Field userMapperField = LoginInterceptor.class.getDeclaredField("userMapper");
            userMapperField.setAccessible(true);
            userMapperField.set(loginInterceptor, userMapper);

            java.lang.reflect.Field passwordEncoderField = LoginInterceptor.class.getDeclaredField("passwordEncoder");
            passwordEncoderField.setAccessible(true);
            passwordEncoderField.set(loginInterceptor, passwordEncoder);

            java.lang.reflect.Field userLockManagerField = LoginInterceptor.class.getDeclaredField("userLockManager");
            userLockManagerField.setAccessible(true);
            userLockManagerField.set(loginInterceptor, userLockManager);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject dependencies", e);
        }
    }

    @Test
    void testUserLockedCheck() {
        // 创建涓€涓閿佸畾鐨勭敤鎴? User lockedUser = new User();
        lockedUser.setUsername("lockedUser");
        lockedUser.setLockedUntil(LocalDateTime.now().plusMinutes(30));
        lockedUser.setLoginFailedCount(5);
        lockedUser.setStatus(1); // 鍚敤状态 userMapper.addUser(lockedUser);

        // 设置用户閿佸畾状态 userLockManager.setLockedUser("lockedUser", true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", "lockedUser");
        request.addParameter("password", "anyPassword");

        // 验证琚攣瀹氱殑用户涓嶈兘鐧诲綍
        UserLockedException exception = assertThrows(UserLockedException.class, () -> {
            loginInterceptor.beforeLogin("lockedUser", request);
        });

        assertTrue(exception.getMessage().contains("璐﹀彿宸茶閿佸畾"));
    }

    @Test
    void testUserNotLockedCheck() {
        // 创建涓€涓湭琚攣瀹氱殑用户
        User normalUser = new User();
        normalUser.setUsername("normalUser");
        normalUser.setPassword("encodedPassword");
        normalUser.setStatus(1); // 鍚敤状态        userMapper.addUser(normalUser);

        // 设置用户鏈攣瀹氱姸鎬?        userLockManager.setLockedUser("normalUser", false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", "normalUser");
        request.addParameter("password", "password");

        // 验证未被锁定的用户可以继续登录流程
        assertDoesNotThrow(() -> {
            loginInterceptor.beforeLogin("normalUser", request);
        });
    }

    // 简单的模拟类实现
    private static class MockUserLockManager implements UserLockManager {
    private boolean isLocked = false;
    private User lockedUser;

    public void setLockedUser(String username, boolean locked) {
        this.isLocked = locked;
        if (locked) {
            lockedUser = new User();
            lockedUser.setUsername(username);
            lockedUser.setLockedUntil(LocalDateTime.now().plusMinutes(30));
            lockedUser.setLoginFailedCount(5);
        } else {
            lockedUser = null;
        }
    }

    @Override
    public boolean checkUserLocked(String username) {
        return isLocked;
    }

    @Override
    public boolean isUserLocked(User user) {
        return isLocked;
    }

    @Override
    public User getLockedUserInfo(String username) {
        return lockedUser;
    }

    @Override
    public User autoLockUser(String username, int failedCount) {
        return null;
    }

    @Override
    public User manualLockUser(String username, String lockReason, int lockDurationMinutes, Long operatorId,
            String operatorName) {
        return null;
    }

    @Override
    public int batchManualLockUsers(java.util.List<String> usernames, String lockReason, int lockDurationMinutes,
            Long operatorId, String operatorName) {
        return 0;
    }

    @Override
    public User manualUnlockUser(String username, Long operatorId, String operatorName) {
        return null;
    }

    @Override
    public int batchManualUnlockUsers(java.util.List<String> usernames, Long operatorId, String operatorName) {
        return 0;
    }

    @Override
    public int autoUnlockExpiredUsers() {
        return 0;
    }

    @Override
    public void resetLoginFailureCount(String username) {
    }

    @Override
    public User updateLockExpireTime(String username, LocalDateTime lockExpireTime) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            user.setLockedUntil(lockExpireTime);
            userMapper.updateById(user);
        }
        return user;
    }

    @Override
    public void handleLoginFailure(String username, String loginIp) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            user.setLoginFailedCount(user.getLoginFailedCount() + 1);
            user.setLastLoginFailedTime(LocalDateTime.now());
            userMapper.updateById(user);

            // 濡傛灉失败娆℃暟杈惧埌闃堝€硷紝閿佸畾用户
            if (user.getLoginFailedCount() >= 5) {
                autoLockUser(username, user.getLoginFailedCount());
            }
        }
    }

    @Override
    public void handleLoginSuccess(User user, String loginIp) {
        user.setLoginFailedCount(0);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(loginIp);
        userMapper.updateById(user);
    }
}

private static class MockUserMapper implements UserMapper {
    private final java.util.Map<String, User> users = new java.util.HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User selectByUsername(String username) {
        return users.get(username);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public int updateById(User entity) {
        User existing = users.get(entity.getUsername());
        if (existing != null) {
            users.put(entity.getUsername(), entity);
            return 1;
        }
        return 0;
    }

    @Override
    public int insert(User entity) {
        users.put(entity.getUsername(), entity);
        return 1;
    }

    // 实现UserMapper接口涓殑鍏朵粬方法锛堢畝鍗曡繑鍥為粯璁ゅ€硷級
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要 // @Override public User selectByEmail(String email) {
    // return null; }
    @Override
    public User selectByPhone(String phone) {
        return null;
    }

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<User> selectPage(
            com.baomidou.mybatisplus.core.metadata.IPage<User> page, java.util.Map<String, Object> params) {
        return null;
    }

    @Override
    public java.util.List<com.mold.digitalization.entity.Role> selectUserRoles(Long userId) {
        return null;
    }

    @Override
    public int incrementLoginFailedCount(Long userId) {
        return 0;
    }

    @Override
    public int resetLoginFailedCount(Long userId) {
        return 0;
    }

    @Override
    public int lockUser(Long userId, java.time.LocalDateTime lockedUntil, String lockReason) {
        return 0;
    }

    @Override
    public int unlockUser(Long userId) {
        return 0;
    }

    @Override
    public int unlockExpiredUsers(java.time.LocalDateTime currentTime) {
        return 0;
    }

    @Override
    public java.util.List<User> selectExpiredLockedUsers(java.time.LocalDateTime currentTime) {
        return null;
    }

    @Override
    public int updateLastLoginInfo(Long userId, String lastLoginIp, java.time.LocalDateTime lastLoginTime) {
        return 0;
    }
}

private static class MockPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return "encoded_" + rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}}
