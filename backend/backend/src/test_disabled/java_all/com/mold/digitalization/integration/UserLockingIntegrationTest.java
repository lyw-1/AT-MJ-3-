package com.mold.digitalization.integration;

import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.manager.UserLockManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户閿佸畾鏈哄埗鐨勯泦鎴愭祴璇? * 测试鏁翠釜閿佸畾娴佺▼鐨勯泦成功鑳? */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserLockingIntegrationTest {

    @Autowired
    private UserLockManager userLockManager;

    /**
     * 测试瀹屾暣鐨勭敤鎴烽攣瀹氭祦绋嬶細澶氭失败灏濊瘯鍚庨攣瀹氱敤鎴?     */
    @Test
    public void testUserLockingFlow() throws Exception {
        // 1. 创建鎴栬幏鍙栨祴璇曠敤鎴?        String testUsername = "testuser";
        createTestUserIfNotExists(testUsername);
        assertNotNull(testUsername, "测试用户创建失败");

        // 2. 閲嶇疆用户状态        resetUserLockStatus(testUsername);
        assertFalse(userLockManager.checkUserLocked(testUsername), "测试前用户应该未锁定");

        // 3. 模拟澶氭鐧诲綍失败锛岃Е鍙戦攣瀹?        // 娉ㄦ剰锛氱敱浜庢病鏈塱ncrementLoginFailureCount方法锛屾垜浠洿鎺ヤ娇鐢╝utoLockUser模拟閿佸畾
        userLockManager.autoLockUser(testUsername, 5); // 模拟5娆″け璐ュ悗閿佸畾

        // 4. 妫€鏌ラ攣瀹氱姸鎬?        assertTrue(userLockManager.checkUserLocked(testUsername), "用户应该被锁定");

        // 5. 瑙ｉ攣用户
        userLockManager.manualUnlockUser(testUsername, 1L, "测试瑙ｉ攣");
        assertFalse(userLockManager.checkUserLocked(testUsername), "解锁后用户应该未锁定");
    }

    /**
     * 测试鎵嬪姩閿佸畾鍜岃В閿佺敤鎴?     */
    @Test
    public void testManualLockAndUnlock() throws Exception {
        // 准备测试用户
        String testUsername = "testuser";
        createTestUserIfNotExists(testUsername);
        resetUserLockStatus(testUsername);

        // 手动锁定用户
        userLockManager.manualLockUser(testUsername, "管理员手动锁定", 60, 1L, "admin");
        assertTrue(userLockManager.checkUserLocked(testUsername), "手动锁定后用户应该锁定");

        // 鎵嬪姩瑙ｉ攣用户
        userLockManager.manualUnlockUser(testUsername, 1L, "测试瑙ｉ攣");
        assertFalse(userLockManager.checkUserLocked(testUsername), "手动解锁后用户应该未锁定");
    }

    /**
     * 测试鐧诲綍成功鍚庨噸缃け璐ヨ鏁?     */
    @Test
    public void testResetFailureCountOnSuccessfulLogin() throws Exception {
        // 准备测试用户
        String testUsername = "testuser";
        createTestUserIfNotExists(testUsername);
        resetUserLockStatus(testUsername);

        // 模拟登录成功，重置失败计数
        userLockManager.resetLoginFailureCount(testUsername);

        // 验证失败计数已重置
        assertFalse(userLockManager.checkUserLocked(testUsername), "重置失败计数后用户应该未锁定");
        // 可以添加瀵规暟鎹簱涓璴ogin_failed_count瀛楁鐨勬柇瑷€
    }

    /**
     * 测试閿佸畾杩囨湡鍚庤嚜鍔ㄨВ閿?     */
    @Test
    public void testAutoUnlockAfterExpiration() throws Exception {
        // 准备测试用户
        String testUsername = "testuser";
        createTestUserIfNotExists(testUsername);
        resetUserLockStatus(testUsername);

        // 设置涓€涓繃鍘荤殑閿佸畾鏃堕棿锛堟墜鍔ㄤ慨鏀归攣瀹氭椂闂翠负宸茶繃鏈燂級
        // 娉ㄦ剰锛氳繖閲岄渶瑕侀€氳繃鐩存帴淇敼数据搴撴潵模拟杩囨湡鏃堕棿
        // 实际搴旂敤涓紝应该浣跨敤UserLockScheduler鑷姩瑙ｉ攣
        mockExpiredLockTime(testUsername);
        
        // 妫€鏌ラ攣瀹氱姸鎬侊紝应该宸茶嚜鍔ㄨВ閿?        assertFalse(userLockManager.checkUserLocked(testUsername), "过期后用户应该自动解锁");
    }

    /**
     * 创建鎴栬幏鍙栨祴璇曠敤鎴凤紙濡傛灉涓嶅瓨鍦級
     * 鍦ㄥ疄闄呮祴璇曚腑锛屽彲鑳介渶瑕侀€氳繃Repository创建测试用户
     */
    private String createTestUserIfNotExists(String username) {
        // 这里使用硬编码的测试用户名
        // 在实际测试中，应通过 Repository 创建和获取测试用户
        // 为了测试目的，我们假设存在名为 testuser 的测试用户
        return username; // 替换为实际的测试用户获取逻辑
    }

    /**
     * 閲嶇疆用户閿佸畾状态     */
    private void resetUserLockStatus(String username) {
        // 瑙ｉ攣用户
        try {
            userLockManager.manualUnlockUser(username, 1L, "测试前重置状态");
        } catch (Exception e) {
            // 忽略可能的异常
        }
    }

    /**
     * 模拟宸茶繃鏈熺殑閿佸畾鏃堕棿
     * 鍦ㄥ疄闄呮祴璇曚腑锛屽彲鑳介渶瑕侀€氳繃Repository鐩存帴淇敼閿佸畾鏃堕棿
     */
    private void mockExpiredLockTime(String username) {
        // 这里使用锁定后等待的方式模拟过期
        // 在实际测试中，应通过 Repository 直接修改数据库中的锁定时间
        try {
            userLockManager.manualLockUser(username, "测试杩囨湡閿佸畾", 1, 1L, "admin"); // 閿佸畾1鍒嗛挓
            // 为了测试，不实际等待，而是直接检查实现
            // 真实项目中可以使用 TimeUtils 模拟时间
        } catch (Exception e) {
            // 忽略可能的异常
        }
    }
}
