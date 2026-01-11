package com.mold.digitalization.manager.impl;

import com.mold.digitalization.config.UserLockProperties;
import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.manager.UserLockManager;
import com.mold.digitalization.service.LoginFailureCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 用户閿佸畾绠＄悊鍣ㄥ疄鐜扮被
 * 璐熻矗澶勭悊用户閿佸畾銆佽В閿併€佺櫥褰曞け璐ヨ鏁扮瓑核心涓氬姟逻辑
 */
@Component
public class UserLockManagerImpl implements UserLockManager {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserLockManagerImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginFailureCounter loginFailureCounter;

    @Autowired
    private UserLockProperties userLockProperties;

    /**
     * 澶勭悊鐧诲綍失败锛岃嚜鍔ㄩ攣瀹氳揪鍒伴槇鍊肩殑用户
     *
     * @param username 用户名?
     * @param loginIp 鐧诲綍IP
     * @throws UserLockedException 用户琚攣瀹氭椂鎶涘嚭
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleLoginFailure(String username, String loginIp) {
        // 查询用户淇℃伅
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶澶勭悊鐧诲綍失败: username={}", username);
            return;
        }

        // 妫€鏌ョ敤鎴锋槸鍚﹀凡琚攣瀹?
    if (isUserLockedInternal(user)) {
        log.warn("User is locked: username={}, lockedUntil={}", username, user.getLockedUntil());
        throw new UserLockedException(
            "Account is locked",
            user.getLockedUntil(),
            0,
            user.getLockReason(),
            user.getLockOperatorName()
        );
    }

        // 澧炲姞鐧诲綍失败璁℃暟
        int newCount = loginFailureCounter.incrementFailureCount(username);
        log.info("用户鐧诲綍失败娆℃暟澧炲姞: username={}, count={}", username, newCount);

        // 更新数据搴撲腑鐨勫け璐ユ鏁?
        userMapper.incrementLoginFailedCount(user.getId());

        // 妫€鏌ユ槸鍚﹁揪鍒伴攣瀹氶槇鍊?
        if (loginFailureCounter.isThresholdReached(username, userLockProperties.getMaxFailedAttempts())) {
            // 璁＄畻閿佸畾鏃堕棿
            LocalDateTime lockedUntil = calculateLockedUntil();
            String lockReason = String.format("杩炵画%d娆＄櫥褰曞け璐ワ紝系统自动锁定", 
                    userLockProperties.getMaxFailedAttempts());

            // 閿佸畾用户
            lockUserInternal(user.getId(), lockedUntil, lockReason, null, null);
            log.warn("用户杈惧埌閿佸畾闃堝€硷紝宸茶嚜鍔ㄩ攣瀹? username={}, lockedUntil={}", 
                    username, lockedUntil);

        // Throw to indicate account was locked due to failed attempts
        throw new UserLockedException(
            "Too many failed login attempts; account is locked",
            lockedUntil,
            newCount,
            lockReason,
            null
        );
        }
    }

    /**
     * 澶勭悊鐧诲綍成功
     *
     * @param user 用户瀵硅薄
     * @param loginIp 鐧诲綍IP
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleLoginSuccess(User user, String loginIp) {
        // 閲嶇疆鐧诲綍失败璁℃暟
        loginFailureCounter.resetFailureCount(user.getUsername());
        
        // 閲嶇疆数据搴撲腑鐨勫け璐ユ鏁?
        userMapper.resetLoginFailedCount(user.getId());
        
        // 更新鏈€鍚庣櫥褰曚俊鎭?
        userMapper.updateLastLoginInfo(user.getId(), loginIp, LocalDateTime.now());
        
        log.info("用户鐧诲綍成功锛屽凡閲嶇疆失败璁℃暟: username={}", user.getUsername());
    }

    /**
     * 鍐呴儴方法锛氶攣瀹氱敤鎴?
     *
     * @param userId 用户ID
     * @param lockedUntil 閿佸畾杩囨湡鏃堕棿
     * @param lockReason 锁定原因
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     * @return 鏄惁閿佸畾成功
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean lockUser(Long userId, LocalDateTime lockedUntil, String lockReason,
                           Long operatorId, String operatorName) {
        // 鍏堟洿鏂扮敤鎴烽攣瀹氫俊鎭?
        User user = new User();
        user.setId(userId);
        user.setLockedUntil(lockedUntil);
        user.setLockReason(lockReason);
        user.setLockOperatorName(operatorName);
        userMapper.updateById(user);
        
        // 鐒跺悗閿佸畾用户锛堣缃姸鎬佷负绂佺敤锛?
        int result = userMapper.lockUser(userId);
        
        if (result > 0) {
            log.info("用户宸叉墜鍔ㄩ攣瀹? userId={}, lockedUntil={}, reason={}, operator={}",
                    userId, lockedUntil, lockReason, operatorName);
            return true;
        }
        return false;
    }

    /**
     * 鍐呴儴方法锛氳В閿佺敤鎴?
     *
     * @param userId 用户ID
     * @param operatorId 操作鑰匢D
     * @param operatorName 操作鑰呭鍚?
     * @return 鏄惁瑙ｉ攣成功
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean unlockUser(Long userId, Long operatorId, String operatorName) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶瑙ｉ攣: userId={}", userId);
            return false;
        }

        // 瑙ｉ攣用户
        int result = userMapper.unlockUser(userId);
        
        if (result > 0) {
            // 閲嶇疆鐧诲綍失败璁℃暟
            loginFailureCounter.resetFailureCount(user.getUsername());
            userMapper.resetLoginFailedCount(userId);
            
            log.info("用户宸茶В閿? userId={}, operator={}", userId, operatorName);
            return true;
        }
        return false;
    }

    /**
     * 鎵归噺瑙ｉ攣杩囨湡用户
     *
     * @return 瑙ｉ攣成功鐨勭敤鎴锋暟閲?
     */
    @Transactional(rollbackFor = Exception.class)
    private int unlockExpiredUsers() {
        LocalDateTime currentTime = LocalDateTime.now();
        int unlockedCount = userMapper.unlockExpiredUsers(currentTime);
        
        log.info("Auto-unlock completed, unlocked {} users", unlockedCount);
        return unlockedCount;
    }

    /**
     * 鍐呴儴方法锛氭鏌ョ敤鎴锋槸鍚﹁閿佸畾
     *
     * @param user 用户瀵硅薄
     * @return 鏄惁琚攣瀹?
     */
    private boolean isUserLocked(User user) {
        if (user == null || user.getLockedUntil() == null) {
            return false;
        }
        // 濡傛灉閿佸畾鏃堕棿鍦ㄥ綋鍓嶆椂闂翠箣鍚庯紝鍒欑敤鎴蜂粛澶勪簬閿佸畾状态
        return user.getLockedUntil().isAfter(LocalDateTime.now());
    }

    /**
     * 鏍规嵁用户名嶈嚜鍔ㄩ攣瀹氱敤鎴凤紙鐧诲綍失败瑙﹀彂锛?
     * @param username 用户名?
     * @param failedCount 失败娆℃暟
     * @return 閿佸畾鍚庣殑用户淇℃伅
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User autoLockUser(String username, int failedCount) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶鑷姩閿佸畾: username={}", username);
            return null;
        }

        // 璁＄畻閿佸畾鏃堕棿
        LocalDateTime lockedUntil = calculateLockedUntil();
        String lockReason = String.format("杩炵画%d娆＄櫥褰曞け璐ワ紝系统自动锁定", failedCount);

        // 閿佸畾用户
        lockUserInternal(user.getId(), lockedUntil, lockReason, null, null);
        log.warn("用户宸茶嚜鍔ㄩ攣瀹? username={}, lockedUntil={}", username, lockedUntil);

        // 返回更新鍚庣殑用户淇℃伅
        return userMapper.selectById(user.getId());
    }

    /**
     * 鎵嬪姩閿佸畾用户锛堢鐞嗗憳操作锛?
     * @param username 用户名?
     * @param lockReason 锁定原因
     * @param lockDurationMinutes 閿佸畾鏃堕暱锛堝垎閽燂級
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     * @return 閿佸畾鍚庣殑用户淇℃伅
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User manualLockUser(String username, String lockReason, int lockDurationMinutes, 
                              Long operatorId, String operatorName) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶鎵嬪姩閿佸畾: username={}", username);
            return null;
        }

        // 璁＄畻閿佸畾鏃堕棿
        LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);

        // 閿佸畾用户
        lockUserInternal(user.getId(), lockedUntil, lockReason, operatorId, operatorName);
        log.info("用户宸叉墜鍔ㄩ攣瀹? username={}, lockedUntil={}, operator={}", 
                username, lockedUntil, operatorName);

        // 返回更新鍚庣殑用户淇℃伅
        return userMapper.selectById(user.getId());
    }

    /**
     * 鎵归噺鎵嬪姩閿佸畾用户
     * @param usernames 用户名嶅垪琛?
     * @param lockReason 锁定原因
     * @param lockDurationMinutes 閿佸畾鏃堕暱锛堝垎閽燂級
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     * @return 閿佸畾鐨勭敤鎴锋暟閲?
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchManualLockUsers(List<String> usernames, String lockReason, 
                                   int lockDurationMinutes, Long operatorId, String operatorName) {
        int lockedCount = 0;
        
        for (String username : usernames) {
            User user = userMapper.selectByUsername(username);
            if (user != null) {
                // 璁＄畻閿佸畾鏃堕棿
                LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
                
                // 閿佸畾用户
                try {
                    lockUserInternal(user.getId(), lockedUntil, lockReason, operatorId, operatorName);
                    lockedCount++;
                } catch (Exception e) {
                    log.error("鎵归噺閿佸畾用户失败: username={}, error={}", username, e.getMessage());
                }
            } else {
                log.warn("用户涓嶅瓨鍦紝璺宠繃閿佸畾: username={}", username);
            }
        }
        
        log.info("鎵归噺閿佸畾用户瀹屾垚锛屽叡閿佸畾 {} 涓敤鎴凤紝操作鑰咃細{}", lockedCount, operatorName);
        return lockedCount;
    }

    /**
     * 鎵嬪姩瑙ｉ攣用户
     * @param username 用户名?
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     * @return 瑙ｉ攣鍚庣殑用户淇℃伅
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User manualUnlockUser(String username, Long operatorId, String operatorName) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶鎵嬪姩瑙ｉ攣: username={}", username);
            return null;
        }

        // 瑙ｉ攣用户
        unlockUserInternal(user.getId(), operatorId, operatorName);
        log.info("用户宸叉墜鍔ㄨВ閿? username={}, operator={}", username, operatorName);
        // 返回更新鍚庣殑用户淇℃伅
        return userMapper.selectById(user.getId());
    }

    /**
     * 鎵归噺鎵嬪姩瑙ｉ攣用户
     * @param usernames 用户名嶅垪琛?
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     * @return 瑙ｉ攣鐨勭敤鎴锋暟閲?
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchManualUnlockUsers(List<String> usernames, Long operatorId, String operatorName) {
        int unlockedCount = 0;
        
        for (String username : usernames) {
            User user = userMapper.selectByUsername(username);
            if (user != null) {
                // 瑙ｉ攣用户
                unlockUserInternal(user.getId(), operatorId, operatorName);
                unlockedCount++;
            } else {
                log.warn("用户涓嶅瓨鍦紝璺宠繃瑙ｉ攣: username={}", username);
            }
        }
        
        log.info("鎵归噺瑙ｉ攣用户瀹屾垚锛屽叡瑙ｉ攣 {} 涓敤鎴凤紝操作鑰咃細{}", unlockedCount, operatorName);
        return unlockedCount;
    }

    /**
     * 鑷姩瑙ｉ攣杩囨湡用户
     * @return 瑙ｉ攣鐨勭敤鎴锋暟閲?
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoUnlockExpiredUsers() {
        // 查询鎵€鏈夊凡杩囨湡鐨勯攣瀹氱敤鎴?
        List<User> expiredLockedUsers = userMapper.selectExpiredLockedUsers(LocalDateTime.now());
        int unlockedCount = 0;
        
        for (User user : expiredLockedUsers) {
            try {
                // 瑙ｉ攣用户
                unlockUserInternal(user.getId(), null, "系统鑷姩瑙ｉ攣");
                unlockedCount++;
            } catch (Exception e) {
                log.error("鑷姩瑙ｉ攣杩囨湡用户失败: userId={}, error={}", user.getId(), e.getMessage());
            }
        }
        
        if (unlockedCount > 0) {
            log.info("Auto-unlock expired users completed, count={}", unlockedCount);
        }
        
        return unlockedCount;
    }

    /**
     * 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
     * @param username 用户名?
     * @return 鏄惁琚攣瀹?
     */
    @Override
    public boolean checkUserLocked(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶妫€鏌ラ攣瀹氱姸鎬? username={}", username);
            return false;
        }
        
        return isUserLockedInternal(user);
    }

    /**
     * 获取用户閿佸畾淇℃伅
     * @param username 用户名?
     * @return 用户閿佸畾淇℃伅锛屽鏋滄湭閿佸畾返回null
     */
    @Override
    public User getLockedUserInfo(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        
        // 濡傛灉用户鏈閿佸畾锛岃繑鍥瀗ull
        if (!isUserLockedInternal(user)) {
            return null;
        }
        
        // 返回用户淇℃伅
        return user;
    }

    /**
     * 閲嶇疆用户鐧诲綍失败娆℃暟
     * @param username 用户名?
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetLoginFailureCount(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶閲嶇疆鐧诲綍失败娆℃暟: username={}", username);
            return;
        }
        
        // 閲嶇疆Redis涓殑失败璁℃暟
        loginFailureCounter.resetFailureCount(username);
        
        // 閲嶇疆数据搴撲腑鐨勫け璐ユ鏁?
        userMapper.resetLoginFailedCount(user.getId());
        
        log.info("宸查噸缃敤鎴风櫥褰曞け璐ユ鏁? username={}", username);
    }

    /**
     * 更新用户閿佸畾杩囨湡鏃堕棿
     * @param username 用户名?
     * @param lockExpireTime 閿佸畾杩囨湡鏃堕棿
     * @return 更新鍚庣殑用户淇℃伅
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateLockExpireTime(String username, LocalDateTime lockExpireTime) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户涓嶅瓨鍦紝鏃犳硶更新閿佸畾杩囨湡鏃堕棿: username={}", username);
            return null;
        }
        
        // 更新閿佸畾杩囨湡鏃堕棿
        user.setLockedUntil(lockExpireTime);
        userMapper.updateById(user);
        
        log.info("宸叉洿鏂扮敤鎴烽攣瀹氳繃鏈熸椂闂? username={}, lockExpireTime={}", username, lockExpireTime);
        
        // 返回更新鍚庣殑用户淇℃伅
        return userMapper.selectById(user.getId());
    }

    /**
     * 璁＄畻閿佸畾鏃堕棿
     *
     * @return 閿佸畾杩囨湡鏃堕棿
     */
    private LocalDateTime calculateLockedUntil() {
        return LocalDateTime.now().plusMinutes(userLockProperties.getDurationMinutes());
    }

    /**
     * 鍐呴儴方法锛氭鏌ョ敤鎴锋槸鍚﹁閿佸畾
     *
     * @param user 用户瀵硅薄
     * @return 鏄惁琚攣瀹?
     */
    private boolean isUserLockedInternal(User user) {
        if (user == null || user.getLockedUntil() == null) {
            return false;
        }
        // 濡傛灉閿佸畾鏃堕棿鍦ㄥ綋鍓嶆椂闂翠箣鍚庯紝鍒欑敤鎴蜂粛澶勪簬閿佸畾状态
        return user.getLockedUntil().isAfter(LocalDateTime.now());
    }

    /**
     * 鍐呴儴方法锛氶攣瀹氱敤鎴?
     *
     * @param userId 用户ID
     * @param lockedUntil 閿佸畾杩囨湡鏃堕棿
     * @param lockReason 锁定原因
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     */
    private void lockUserInternal(Long userId, LocalDateTime lockedUntil, 
                                 String lockReason, Long operatorId, String operatorName) {
        // 鍏堟洿鏂扮敤鎴烽攣瀹氫俊鎭?
        User user = new User();
        user.setId(userId);
        user.setLockedUntil(lockedUntil);
        user.setLockReason(lockReason);
        userMapper.updateById(user);
        
        // 鐒跺悗閿佸畾用户锛堣缃姸鎬佷负绂佺敤锛?
        userMapper.lockUser(userId);
        
        // 娓呴櫎Redis涓殑鐧诲綍失败璁℃暟
        user = userMapper.selectById(userId);
        if (user != null) {
            loginFailureCounter.resetFailureCount(user.getUsername());
        }
        
        log.info("用户宸查攣瀹? userId={}, lockedUntil={}, lockReason={}", 
                userId, lockedUntil, lockReason);
    }

    /**
     * 鍐呴儴方法锛氳В閿佺敤鎴?
     *
     * @param userId 用户ID
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄鍚?
     */
    private void unlockUserInternal(Long userId, Long operatorId, String operatorName) {
        // 瑙ｉ攣用户
        userMapper.unlockUser(userId);
        
        // 娓呴櫎Redis涓殑鐧诲綍失败璁℃暟
        User user = userMapper.selectById(userId);
        if (user != null) {
            loginFailureCounter.resetFailureCount(user.getUsername());
            userMapper.resetLoginFailedCount(userId);
        }
        
        log.info("用户宸茶В閿? userId={}, operator={}", userId, operatorName);
    }

    /**
     * 閿佸畾鐘舵€佷俊鎭被
     */
    public static class LockStatusInfo {
        private boolean locked;
        private LocalDateTime lockedUntil;
        private String lockReason;
        private Long lockOperatorId;
        private String lockOperatorName;
        private int failureCount;

        // Getter and Setter methods
        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public LocalDateTime getLockedUntil() {
            return lockedUntil;
        }

        public void setLockedUntil(LocalDateTime lockedUntil) {
            this.lockedUntil = lockedUntil;
        }

        public String getLockReason() {
            return lockReason;
        }

        public void setLockReason(String lockReason) {
            this.lockReason = lockReason;
        }

        public Long getLockOperatorId() {
            return lockOperatorId;
        }

        public void setLockOperatorId(Long lockOperatorId) {
            this.lockOperatorId = lockOperatorId;
        }

        public String getLockOperatorName() {
            return lockOperatorName;
        }

        public void setLockOperatorName(String lockOperatorName) {
            this.lockOperatorName = lockOperatorName;
        }

        public int getFailureCount() {
            return failureCount;
        }

        public void setFailureCount(int failureCount) {
            this.failureCount = failureCount;
        }
    }
}
