package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.config.UserLockCacheConfig;
import com.mold.digitalization.config.UserLockProperties;
import com.mold.digitalization.dto.LoginFailureResult;
import com.mold.digitalization.dto.UserLockInfo;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.UserLockService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户閿佸畾服务实现绫? * 
 * @author system
 */
@Service
@Slf4j
public class UserLockServiceImpl implements UserLockService {

    private static final Logger logger = LoggerFactory.getLogger(UserLockServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLockProperties userLockProperties;

    @Autowired
    private UserLockCacheConfig userLockCacheConfig;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean lockUser(Long userId, String reason) {
        if (userId == null) {
            logger.warn("User ID is null, cannot lock user");
            return false;
        }

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                logger.warn("User does not exist, ID: {}", userId);
                return false;
            }

            // 璁＄畻閿佸畾鏃堕棿
            LocalDateTime lockTime = LocalDateTime.now();
            LocalDateTime unlockTime = lockTime.plusMinutes(userLockProperties.getDurationMinutes());

            // 使用状态字段进行锁定（数据库存在该列），锁定原因与截止时间仅缓存管理
            userMapper.lockUser(userId);

            // 缂撳瓨閿佸畾淇℃伅
            cacheUserLockInfo(userId, unlockTime, reason);

            logger.info("User locked: ID: {}, reason: {}, unlockTime: {}", userId, reason, unlockTime);
            return true;
        } catch (Exception e) {
            logger.error("Failed to lock user, ID: {}", userId, e);
            throw new RuntimeException("Failed to lock user", e);
        }
    }

    @Override
    public boolean unlockUser(Long userId) {
        if (userId == null) {
            logger.warn("User ID is null, cannot unlock user");
            return false;
        }

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                logger.warn("User does not exist, ID: {}", userId);
                return false;
            }

            // 使用状态字段进行解锁
            userMapper.unlockUser(userId);

            // 娓呴櫎缂撳瓨
            clearUserLockCache(userId);
            clearLoginFailedCountCache(userId);

            logger.info("User unlocked: ID: {}", userId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to unlock user, ID: {}", userId, e);
            throw new RuntimeException("Failed to unlock user", e);
        }
    }

    @Override
    public boolean isUserLocked(Long userId) {
        if (userId == null) {
            return false;
        }

        try {
            // 鍏堜粠缂撳瓨获取
            if (userLockCacheConfig.isCacheEnabled()) {
                String lockKey = userLockCacheConfig.getUserLockKey(userId);
                String cachedInfoStr = redisService.get(lockKey);
                if (cachedInfoStr != null) {
                        if (cachedInfoStr.equals("locked")) {
                            return true;
                        }
                    }
            }

            // 缂撳瓨涓病鏈夛紝浠庢暟鎹簱获取
            User user = userMapper.selectById(userId);
            if (user == null) {
                return false;
            }

            // 使用状态字段判断锁定
            boolean isLocked = user.getStatus() != null && user.getStatus() == 0;
            if (isLocked && userLockCacheConfig.isCacheEnabled()) {
                cacheUserLockInfo(userId, null, null);
            }
            return isLocked;
        } catch (Exception e) {
            logger.error("Error checking user lock status, ID: {}", userId, e);
            return false;
        }
    }

    @Override
    public UserLockInfo getUserLockInfo(Long userId) {
        if (userId == null) {
            return null;
        }

        try {
            // 鍏堜粠缂撳瓨获取
            if (userLockCacheConfig.isCacheEnabled()) {
                String lockKey = userLockCacheConfig.getUserLockKey(userId);
                String cachedInfoStr = redisService.get(lockKey);
                if (cachedInfoStr != null) {
                    // 绠€鍖栧鐞嗭細鐩存帴浠庣紦瀛樹腑获取閿佸畾淇℃伅
                    // 杩欓噷绠€鍖栧鐞嗭紝实际项目涓彲鑳介渶瑕佹洿澶嶆潅鐨勫簭鍒楀寲/鍙嶅簭鍒楀寲逻辑
                    UserLockInfo lockInfo = new UserLockInfo();
                    lockInfo.setUserId(userId);
                    lockInfo.setLocked(true);
                    return lockInfo;
                }
            }

            // 缂撳瓨涓病鏈夛紝浠庢暟鎹簱获取
            User user = userMapper.selectById(userId);
            if (user == null) {
                return null;
            }

            // 鏋勫缓用户閿佸畾淇℃伅
            UserLockInfo lockInfo = new UserLockInfo();
            lockInfo.setUserId(userId);
            lockInfo.setUsername(user.getUsername());
            lockInfo.setRealName(user.getRealName());
            
            // 使用状态字段判断锁定
            boolean isLocked = user.getStatus() != null && user.getStatus() == 0;
            lockInfo.setLocked(isLocked);
            // 失败次数仅通过缓存管理（数据库无该列），因此这里不从数据库读取
            lockInfo.setCurrentFailedCount(0);
            lockInfo.setMaxFailedAttempts(userLockProperties.getMaxFailedAttempts());

            // 更新缂撳瓨
            if (isLocked && userLockCacheConfig.isCacheEnabled()) {
                cacheUserLockInfo(userId, null, null);
            }

            return lockInfo;
        } catch (Exception e) {
            logger.error("Failed to get user lock info, ID: {}", userId, e);
            return null;
        }
    }

    @Override
    public LoginFailureResult handleLoginFailure(Long userId) {
        if (userId == null) {
            logger.warn("User ID is null, cannot handle login failure");
            return null;
        }

        LoginFailureResult result = new LoginFailureResult();
        
        try {
            // 获取鏈€澶уけ璐ユ鏁板拰閿佸畾鏃堕暱
            int maxFailedAttempts = userLockProperties.getMaxFailedAttempts();
            int durationMinutes = userLockProperties.getDurationMinutes();

            // 获取褰撳墠失败娆℃暟
            Integer currentFailedCount = incrementLoginFailedCount(userId);
            result.setCurrentFailedCount(currentFailedCount);
            result.setMaxFailedAttempts(maxFailedAttempts);
            result.setRemainingAttempts(Math.max(0, maxFailedAttempts - currentFailedCount));

            if (currentFailedCount >= maxFailedAttempts) {
                // lock the user
                LocalDateTime unlockTime = LocalDateTime.now().plusMinutes(durationMinutes);
                lockUser(userId, "User locked due to too many failed login attempts");
                result.setLocked(true);
                result.setLockedUntil(unlockTime);
                result.setErrorMessage("User locked due to too many failed login attempts");
            } else {
                result.setLocked(false);
                result.setErrorMessage("Username or password incorrect, remaining attempts: " + result.getRemainingAttempts());
            }

            return result;
        } catch (Exception e) {
            logger.error("Error handling login failure, user ID: {}", userId, e);
            result.setErrorMessage("System error, please try again later");
            return result;
        }
    }

    @Override
    public void resetLoginFailedCount(Long userId) {
        if (userId == null) {
            logger.warn("User ID is null, cannot reset login failed count");
            return;
        }

        try {
            // 数据库无 login_failed_count 列，直接清理缓存
            clearLoginFailedCountCache(userId);
            logger.info("Reset login failed count (cache only), ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error resetting login failed count, user ID: {}", userId, e);
            throw new RuntimeException("Error resetting login failed count", e);
        }
    }

    /**
     * 澧炲姞鐧诲綍失败娆℃暟
     * 
     * @param userId 用户ID
     * @return 澧炲姞鍚庣殑失败娆℃暟
     */
    private Integer incrementLoginFailedCount(Long userId) {
        // 鍏堜粠缂撳瓨获取
        if (userLockCacheConfig.isCacheEnabled()) {
            String failedKey = userLockCacheConfig.getLoginFailedKey(userId);
            String countStr = redisService.get(failedKey);
            if (countStr != null) {
                int count = Integer.parseInt(countStr);
                count++;
                // 更新缂撳瓨锛岃缃繃鏈熸椂闂?
                redisService.set(failedKey, String.valueOf(count), userLockCacheConfig.getCacheExpireSeconds());
                return count;
            }
        }

        // 缂撳瓨涓病鏈夛紝仅在内存中递增并写入缓存（数据库无该列）
        int currentCount = 1;
        if (userLockCacheConfig.isCacheEnabled()) {
            String failedKey = userLockCacheConfig.getLoginFailedKey(userId);
            redisService.set(failedKey, String.valueOf(currentCount), userLockCacheConfig.getCacheExpireSeconds());
        }
        return currentCount;
    }

    /**
     * 缂撳瓨用户閿佸畾淇℃伅
     * 
     * @param userId 用户ID
     * @param unlockTime 瑙ｉ攣鏃堕棿
     * @param reason 锁定原因
     */
    private void cacheUserLockInfo(Long userId, LocalDateTime unlockTime, String reason) {
        if (!userLockCacheConfig.isCacheEnabled()) {
            return;
        }

        try {
            String lockKey = userLockCacheConfig.getUserLockKey(userId);
            // 绠€鍖栧鐞嗭細鐩存帴瀛樺偍閿佸畾鐘舵€佸瓧绗︿覆
            // 实际项目涓彲鑳介渶瑕佹洿澶嶆潅鐨勫簭鍒楀寲/鍙嶅簭鍒楀寲逻辑
            redisService.set(lockKey, "locked", userLockCacheConfig.getCacheExpireSeconds());
        } catch (Exception e) {
            logger.error("缂撳瓨用户閿佸畾淇℃伅失败锛岀敤鎴稩D: {}", userId, e);
        }
    }

    /**
     * 娓呴櫎用户閿佸畾缂撳瓨
     * 
     * @param userId 用户ID
     */
    private void clearUserLockCache(Long userId) {
        if (!userLockCacheConfig.isCacheEnabled()) {
            return;
        }

        try {
            String lockKey = userLockCacheConfig.getUserLockKey(userId);
            redisService.delete(lockKey);
        } catch (Exception e) {
            logger.error("娓呴櫎用户閿佸畾缂撳瓨失败锛岀敤鎴稩D: {}", userId, e);
        }
    }

    /**
     * 娓呴櫎鐧诲綍失败娆℃暟缂撳瓨
     * 
     * @param userId 用户ID
     */
    private void clearLoginFailedCountCache(Long userId) {
        if (!userLockCacheConfig.isCacheEnabled()) {
            return;
        }

        try {
            String failedKey = userLockCacheConfig.getLoginFailedKey(userId);
            redisService.delete(failedKey);
        } catch (Exception e) {
            logger.error("娓呴櫎鐧诲綍失败娆℃暟缂撳瓨失败锛岀敤鎴稩D: {}", userId, e);
        }
    }
}
