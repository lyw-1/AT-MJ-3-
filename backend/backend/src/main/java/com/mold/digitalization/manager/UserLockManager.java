package com.mold.digitalization.manager;

import com.mold.digitalization.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户閿佸畾绠＄悊鍣ㄦ帴鍙? * 璐熻矗用户閿佸畾鐩稿叧鐨勬牳蹇冧笟鍔￠€昏緫
 */
public interface UserLockManager {
    
    /**
     * 鏍规嵁用户名嶈嚜鍔ㄩ攣瀹氱敤鎴凤紙鐧诲綍失败瑙﹀彂锛?     * @param username 用户名?     * @param failedCount 失败娆℃暟
     * @return 閿佸畾鍚庣殑用户淇℃伅
     */
    User autoLockUser(String username, int failedCount);
    
    /**
     * 鎵嬪姩閿佸畾用户锛堢鐞嗗憳操作锛?     * @param username 用户名?     * @param lockReason 锁定原因
     * @param lockDurationMinutes 閿佸畾鏃堕暱锛堝垎閽燂級
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄悕绉?     * @return 閿佸畾鍚庣殑用户淇℃伅
     */
    User manualLockUser(String username, String lockReason, int lockDurationMinutes, Long operatorId, String operatorName);
    
    /**
     * 鎵归噺鎵嬪姩閿佸畾用户
     * @param usernames 用户名嶅垪琛?     * @param lockReason 锁定原因
     * @param lockDurationMinutes 閿佸畾鏃堕暱锛堝垎閽燂級
     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄悕绉?     * @return 閿佸畾鐨勭敤鎴锋暟閲?     */
    int batchManualLockUsers(List<String> usernames, String lockReason, int lockDurationMinutes, Long operatorId, String operatorName);
    
    /**
     * 鎵嬪姩瑙ｉ攣用户
     * @param username 用户名?     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄悕绉?     * @return 瑙ｉ攣鍚庣殑用户淇℃伅
     */
    User manualUnlockUser(String username, Long operatorId, String operatorName);
    
    /**
     * 鎵归噺鎵嬪姩瑙ｉ攣用户
     * @param usernames 用户名嶅垪琛?     * @param operatorId 操作浜篒D
     * @param operatorName 操作浜哄悕绉?     * @return 瑙ｉ攣鐨勭敤鎴锋暟閲?     */
    int batchManualUnlockUsers(List<String> usernames, Long operatorId, String operatorName);
    
    /**
     * 鑷姩瑙ｉ攣杩囨湡用户
     * @return 瑙ｉ攣鐨勭敤鎴锋暟閲?     */
    int autoUnlockExpiredUsers();
    
    /**
     * 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
     * @param username 用户名?     * @return 鏄惁琚攣瀹?     */
    boolean checkUserLocked(String username);
    
    /**
     * 获取用户閿佸畾淇℃伅
     * @param username 用户名?     * @return 用户閿佸畾淇℃伅锛屽鏋滄湭閿佸畾返回null
     */
    User getLockedUserInfo(String username);
    
    /**
     * 閲嶇疆用户鐧诲綍失败娆℃暟
     * @param username 用户名?     */
    void resetLoginFailureCount(String username);
    
    /**
     * 更新用户閿佸畾杩囨湡鏃堕棿
     * @param username 用户名?     * @param lockExpireTime 閿佸畾杩囨湡鏃堕棿
     * @return 更新鍚庣殑用户淇℃伅
     */
    User updateLockExpireTime(String username, LocalDateTime lockExpireTime);
    
    /**
     * 澶勭悊鐧诲綍失败
     * @param username 用户名?     * @param loginIp 鐧诲綍IP
     */
    void handleLoginFailure(String username, String loginIp);
    
    /**
     * 澶勭悊鐧诲綍成功
     * @param user 用户瀵硅薄
     * @param loginIp 鐧诲綍IP
     */
    void handleLoginSuccess(User user, String loginIp);
}
