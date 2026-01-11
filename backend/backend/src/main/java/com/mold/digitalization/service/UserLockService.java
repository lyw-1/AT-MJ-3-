package com.mold.digitalization.service;

import com.mold.digitalization.dto.LoginFailureResult;
import com.mold.digitalization.dto.UserLockInfo;

/**
 * 用户閿佸畾服务接口
 * 鎻愪緵用户閿佸畾銆佽В閿併€佺姸鎬佹鏌ョ瓑功能
 */
public interface UserLockService {

    /**
     * 閿佸畾用户
     * @param userId 用户ID
     * @param lockReason 锁定原因
     * @return 鏄惁成功閿佸畾
     */
    boolean lockUser(Long userId, String lockReason);

    /**
     * 瑙ｉ攣用户
     * @param userId 用户ID
     * @return 鏄惁成功
     */
    boolean unlockUser(Long userId);

    /**
     * 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
     * @param userId 用户ID
     * @return 鏄惁琚攣瀹?     */
    boolean isUserLocked(Long userId);

    /**
     * 获取用户閿佸畾淇℃伅
     * @param userId 用户ID
     * @return 閿佸畾淇℃伅锛屽寘鍚攣瀹氭椂闂村拰失败娆℃暟
     */
    UserLockInfo getUserLockInfo(Long userId);

    /**
     * 澶勭悊鐧诲綍失败
     * @param userId 用户ID
     * @return 澶勭悊结果锛屽寘鍚槸鍚﹁閿佸畾
     */
    LoginFailureResult handleLoginFailure(Long userId);

    /**
     * 閲嶇疆鐧诲綍失败娆℃暟
     * @param userId 用户ID
     */
    void resetLoginFailedCount(Long userId);
}
