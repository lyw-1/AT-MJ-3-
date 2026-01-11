package com.mold.digitalization.exception;

import java.time.LocalDateTime;

/**
 * 用户閿佸畾开傚父
 * 褰撶敤鎴疯处鍙疯閿佸畾鏃舵姏鍑烘开傚父
 */
public class UserLockedException extends RuntimeException {

    /**
     * 閿佸畾杩囨湡鏃堕棿
     */
    private final LocalDateTime lockedUntil;

    /**
     * 鐧诲綍失败娆℃暟
     */
    private final Integer failedCount;
    
    /**
     * 锁定原因
     */
    private final String lockReason;
    
    /**
     * 閿佸畾操作鑰?     */
    private final String lockOperatorName;

    /**
     * 鏋勯€犲嚱鏁?     * @param message 开傚父娑堟伅
     * @param lockedUntil 閿佸畾杩囨湡鏃堕棿
     * @param failedCount 失败娆℃暟
     * @param lockReason 锁定原因
     * @param lockOperatorName 閿佸畾操作鑰呭悕绉?     */
    public UserLockedException(String message, LocalDateTime lockedUntil, Integer failedCount, String lockReason, String lockOperatorName) {
        super(message);
        this.lockedUntil = lockedUntil;
        this.failedCount = failedCount;
        this.lockReason = lockReason;
        this.lockOperatorName = lockOperatorName;
    }
    
    /**
     * 鏋勯€犲嚱鏁?     * @param message 开傚父娑堟伅
     * @param lockedUntil 閿佸畾杩囨湡鏃堕棿
     * @param failedCount 失败娆℃暟
     */
    public UserLockedException(String message, LocalDateTime lockedUntil, Integer failedCount) {
        super(message);
        this.lockedUntil = lockedUntil;
        this.failedCount = failedCount;
        this.lockReason = null;
        this.lockOperatorName = null;
    }

    /**
     * 鏋勯€犲嚱鏁?     * @param message 开傚父娑堟伅
     */
    public UserLockedException(String message) {
        super(message);
        this.lockedUntil = null;
        this.failedCount = null;
        this.lockReason = null;
        this.lockOperatorName = null;
    }

    /**
     * 获取閿佸畾鏃堕棿
     * @return 閿佸畾鏃堕棿
     */
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    /**
     * 获取失败娆℃暟
     * @return 失败娆℃暟
     */
    public Integer getFailedCount() {
        return failedCount;
    }
    
    /**
     * 获取锁定原因
     * @return 锁定原因
     */
    public String getLockReason() {
        return lockReason;
    }
    
    /**
     * 获取閿佸畾操作鑰呭悕绉?     * @return 閿佸畾操作鑰呭悕绉?     */
    public String getLockOperatorName() {
        return lockOperatorName;
    }
}
