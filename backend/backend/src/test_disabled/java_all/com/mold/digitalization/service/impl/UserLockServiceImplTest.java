package com.mold.digitalization.service.impl;

import com.mold.digitalization.config.UserLockCacheConfig;
import com.mold.digitalization.dao.UserMapper;
import com.mold.digitalization.dto.UserLockInfo;
import com.mold.digitalization.exception.UserLockedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户閿佸畾服务实现绫荤殑鍗曞厓测试
 */
public class UserLockServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private UserLockServiceImpl userLockService;

    private UserLockCacheConfig userLockCacheConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟Redis操作
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        // 鍒濆鍖栭厤缃?        userLockCacheConfig = new UserLockCacheConfig();
        // 浣跨敤鍙嶅皠娉ㄥ叆配置锛堝洜涓篣serLockServiceImpl娌℃湁鐩存帴渚濊禆娉ㄥ叆配置绫伙級
        try {
            java.lang.reflect.Field field = UserLockServiceImpl.class.getDeclaredField("userLockCacheConfig");
            field.setAccessible(true);
            field.set(userLockService, userLockCacheConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLockUser() {
        // 准备测试数据
        Long userId = 1L;
        String lockReason = "测试锁定原因";
        
        // 模拟数据搴撴搷浣?        when(userMapper.lockUser(anyLong(), any(), anyString())).thenReturn(1);
        
        // 执行测试方法
        boolean result = userLockService.lockUser(userId, lockReason);
        
        // 验证结果
        assertTrue(result);
        verify(userMapper).lockUser(anyLong(), any(), eq(lockReason));
    }
    
    @Test
    void testLockUser_CacheContainsReason() {
        // 准备测试数据
        Long userId = 1L;
        String lockReason = "管理员手动锁定";
        
        // 模拟数据库操作
        when(userMapper.lockUser(anyLong(), any(), eq(lockReason))).thenReturn(1);
        
        // 浣跨敤ArgumentCaptor鏉ユ崟鑾蜂紶閫掔粰Redis鐨勫€?        ArgumentCaptor<UserLockInfo> lockInfoCaptor = ArgumentCaptor.forClass(UserLockInfo.class);
        
        // 执行测试方法
        boolean result = userLockService.lockUser(userId, lockReason);
        
        // 验证结果
        assertTrue(result);
        
        // 验证Redis缂撳瓨涓槸鍚﹀寘鍚攣瀹氬師鍥?        verify(valueOperations).set(anyString(), lockInfoCaptor.capture());
        
        // 验证鎹曡幏鐨勫璞′腑鏄惁鍖呭惈姝ｇ‘鐨勯攣瀹氬師鍥?        assertEquals(lockReason, lockInfoCaptor.getValue().getLockReason());
    }

    @Test
    void testUnlockUser() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟数据搴撴搷浣?        when(userMapper.unlockUser(userId)).thenReturn(1);
        when(userMapper.resetLoginFailedCount(userId)).thenReturn(1);
        // 模拟Redis删除操作
        when(redisTemplate.delete(anyString())).thenReturn(true);
        
        // 执行测试方法
        boolean result = userLockService.unlockUser(userId);
        
        // 验证结果
        assertTrue(result);
        verify(userMapper).unlockUser(userId);
        verify(userMapper).resetLoginFailedCount(userId);
        verify(redisTemplate).delete(contains(userId.toString()));
    }
    
    @Test
    void testUnlockUser_ClearsLockInfoFromCache() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟数据搴撴搷浣?        when(userMapper.unlockUser(userId)).thenReturn(1);
        when(userMapper.resetLoginFailedCount(userId)).thenReturn(1);
        // 模拟Redis删除操作返回成功
        when(redisTemplate.delete(contains(userId.toString()))).thenReturn(true);
        
        // 执行测试方法
        boolean result = userLockService.unlockUser(userId);
        
        // 验证结果
        assertTrue(result);
        verify(redisTemplate).delete(contains(userId.toString()));
    }

    @Test
    void testIsUserLocked_NotLocked() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟Redis操作 - 返回null琛ㄧず鏈攣瀹?        when(valueOperations.get(contains("user:lock:"))).thenReturn(null);
        
        // 执行测试方法
        boolean result = userLockService.isUserLocked(userId);
        
        // 验证结果
        assertFalse(result);
    }

    @Test
    void testIsUserLocked_Locked() {
        // 准备测试数据
        Long userId = 1L;
        UserLockInfo lockInfo = new UserLockInfo();
        lockInfo.setUserId(userId);
        lockInfo.setLocked(true);
        
        // 模拟Redis操作 - 返回閿佸畾淇℃伅
        when(valueOperations.get(contains("user:lock:"))).thenReturn(lockInfo);
        
        // 执行测试方法
        boolean result = userLockService.isUserLocked(userId);
        
        // 验证结果
        assertTrue(result);
    }

    @Test
    void testHandleLoginFailure_NotLockedYet() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟Redis操作 - 鍒濆失败娆℃暟涓?锛堝皬浜庢渶澶ф鏁?锛?        when(valueOperations.get(contains("login:failure:"))).thenReturn(2);
        when(valueOperations.increment(contains("login:failure:"))).thenReturn(3L);
        
        // 执行测试方法
        userLockService.handleLoginFailure(userId);
        
        // 验证结果 - 应该澧炲姞失败娆℃暟浣嗕笉閿佸畾
        verify(valueOperations).increment(contains("login:failure:"));
        verify(userMapper, never()).lockUser(any(), any(), any());
    }

    @Test
    void testHandleLoginFailure_LockAfterMultipleFailures() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟Redis操作 - 鍒濆失败娆℃暟涓?锛堣揪鍒版渶澶ф鏁?锛?        when(valueOperations.get(contains("login:failure:"))).thenReturn(4);
        when(valueOperations.increment(contains("login:failure:"))).thenReturn(5L);
        // 模拟数据搴撻攣瀹氭搷浣?        when(userMapper.lockUser(anyLong(), any(), anyString())).thenReturn(1);
        
        // 执行测试方法 - 应该鎶涘嚭閿佸畾开傚父
        assertThrows(UserLockedException.class, () -> {
            userLockService.handleLoginFailure(userId);
        });
        
        // 验证结果 - 应该閿佸畾用户
        verify(userMapper).lockUser(anyLong(), any(), anyString());
    }

    @Test
    void testResetLoginFailures() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟数据搴撴搷浣?        when(userMapper.resetLoginFailedCount(userId)).thenReturn(1);
        // 模拟Redis删除操作
        when(redisTemplate.delete(anyString())).thenReturn(true);
        
        // 执行测试方法
        userLockService.resetLoginFailedCount(userId);
        
        // 验证结果
        verify(userMapper).resetLoginFailedCount(userId);
        verify(redisTemplate).delete(contains("login:failure:"));
    }

    @Test
    void testGetUserLockInfo() {
        // 准备测试数据
        Long userId = 1L;
        UserLockInfo lockInfo = new UserLockInfo();
        lockInfo.setUserId(userId);
        lockInfo.setLocked(true);
        lockInfo.setLockReason("测试锁定原因");
        
        // 模拟Redis操作
        when(valueOperations.get(contains("user:lock:"))).thenReturn(lockInfo);
        
        // 执行测试方法
        UserLockInfo result = userLockService.getUserLockInfo(userId);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.isLocked());
        assertEquals("测试锁定原因", result.getLockReason());
    }
}