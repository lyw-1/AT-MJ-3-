package com.mold.digitalization.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户閿佸畾淇℃伅DTO
 * 鍖呭惈用户閿佸畾鐨勮缁嗕俊鎭?
 */
@Data
public class UserLockInfo {

    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名?
     */
    private String username;
    
    /**
     * 鐪熷疄濮撳悕
     */
    private String realName;

    /**
     * 閿佸畾鏃堕棿
     * 璐﹀彿琚攣瀹氱殑鍏蜂綋鏃堕棿鐐?
     */
    private LocalDateTime lockedUntil;

    /**
     * 鐧诲綍失败娆℃暟
     * 瑙﹀彂閿佸畾鐨勭櫥褰曞け璐ョ疮绉鏁?
     */
    private Integer failedCount;
    
    /**
     * 褰撳墠失败娆℃暟
     */
    private Integer currentFailedCount;
    
    /**
     * 鏈€澶уけ璐ユ鏁?
     */
    private Integer maxFailedAttempts;

    /**
     * 锁定原因
     * SYSTEM: 系统自动锁定
     * ADMIN: 管理员樻墜鍔ㄩ攣瀹?
     */
    private String lockedBy;

    /**
     * 锁定原因鎻忚堪
     */
    private String lockReason;

    /**
     * 鏄惁琚攣瀹?
     * 褰撳墠用户璐﹀彿鏄惁澶勪簬閿佸畾状态
     */
    private boolean locked;
    
    // 鎵嬪姩添加getter鍜宻etter方法
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
    
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    public Integer getFailedCount() {
        return failedCount;
    }
    
    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }
    
    public Integer getCurrentFailedCount() {
        return currentFailedCount;
    }
    
    public void setCurrentFailedCount(Integer currentFailedCount) {
        this.currentFailedCount = currentFailedCount;
    }
    
    public Integer getMaxFailedAttempts() {
        return maxFailedAttempts;
    }
    
    public void setMaxFailedAttempts(Integer maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }
    
    public String getLockedBy() {
        return lockedBy;
    }
    
    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }
    
    public String getLockReason() {
        return lockReason;
    }
    
    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
