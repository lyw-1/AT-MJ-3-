package com.mold.digitalization.scheduler;

import com.mold.digitalization.config.UserLockProperties;
import com.mold.digitalization.manager.UserLockManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户瑙ｉ攣瀹氭椂浠诲姟
 * 瀹氭湡妫€鏌ュ苟瑙ｉ攣杩囨湡鐨勭敤鎴疯处鎴?
 */
@Component
@ConditionalOnProperty(prefix = "mold.user-lock", name = "unlock-task-enabled", havingValue = "true", matchIfMissing = true)
public class UserUnlockScheduler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserUnlockScheduler.class);

    @Autowired
    private UserLockManager userLockManager;

    @Autowired
    private UserLockProperties userLockProperties;
    
    @Value("${mold.user-lock.unlock-task-interval-minutes:10}")
    private int unlockTaskIntervalMinutes;

    /**
     * 瀹氭湡瑙ｉ攣杩囨湡用户鐨勫畾鏃朵换鍔?
     * 榛樿涓烘瘡10鍒嗛挓执行涓€娆★紝可以閫氳繃配置鏂囦欢璋冩暣执行棰戠巼
     */
    @Scheduled(fixedRateString = "#{${mold.user-lock.unlock-task-interval-minutes:10} * 60000}")
    public void unlockExpiredUsers() {
        try {
            log.info("Starting unlockExpiredUsers task");

            int unlockedCount = userLockManager.autoUnlockExpiredUsers();

            log.info("Unlocked {} users", unlockedCount);
        } catch (Exception e) {
            log.error("Error executing unlockExpiredUsers task", e);
        }
    }
}
