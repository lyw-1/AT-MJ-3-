package com.mold.digitalization.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 鐧诲綍失败澶勭悊结果DTO
 * 鍖呭惈鐧诲綍失败鍚庣殑澶勭悊结果淇℃伅
 */
@Data
public class LoginFailureResult {

    /**
     * 鏄惁琚攣瀹?     * 姝ゆ鐧诲綍失败鍚庯紝用户鏄惁琚攣瀹?     */
    private boolean locked;

    /**
     * 閿佸畾鏃堕棿
     * 濡傛灉用户琚攣瀹氾紝鍖呭惈閿佸畾鐨勫叿浣撴椂闂寸偣
     */
    private LocalDateTime lockedUntil;

    /**
     * 褰撳墠失败娆℃暟
     * 用户褰撳墠绱鐨勭櫥褰曞け璐ユ鏁?     */
    private Integer currentFailedCount;

    /**
     * 鏈€澶уけ璐ユ鏁?     * 系统配置鐨勬渶澶х櫥褰曞け璐ユ鏁伴槇鍊?     */
    private Integer maxFailedAttempts;

    /**
     * 鍓╀綑鍙敤灏濊瘯娆℃暟
     * 璺濈璐﹀彿琚攣瀹氳繕鍓╁灏戞失败灏濊瘯鏈轰細
     */
    private Integer remainingAttempts;

    /**
     * 错误娑堟伅
     * 鐧诲綍失败鐨勬彁绀烘秷鎭?     */
    private String errorMessage;
    
    /**
     * 鏄惁琚攣瀹?     * @return 鏄惁琚攣瀹?     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * 获取错误娑堟伅
     * @return 错误娑堟伅
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 获取鍓╀綑灏濊瘯娆℃暟
     * @return 鍓╀綑灏濊瘯娆℃暟
     */
    public Integer getRemainingAttempts() {
        return remainingAttempts;
    }
    
    /**
     * 设置鏄惁琚攣瀹?     * @param locked 鏄惁琚攣瀹?     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    /**
     * 设置閿佸畾鏃堕棿
     * @param lockedUntil 閿佸畾鏃堕棿
     */
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    /**
     * 设置褰撳墠失败娆℃暟
     * @param currentFailedCount 褰撳墠失败娆℃暟
     */
    public void setCurrentFailedCount(Integer currentFailedCount) {
        this.currentFailedCount = currentFailedCount;
    }
    
    /**
     * 设置鏈€澶уけ璐ユ鏁?     * @param maxFailedAttempts 鏈€澶уけ璐ユ鏁?     */
    public void setMaxFailedAttempts(Integer maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }
    
    /**
     * 设置鍓╀綑灏濊瘯娆℃暟
     * @param remainingAttempts 鍓╀綑灏濊瘯娆℃暟
     */
    public void setRemainingAttempts(Integer remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }
    
    /**
     * 设置错误娑堟伅
     * @param errorMessage 错误娑堟伅
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
