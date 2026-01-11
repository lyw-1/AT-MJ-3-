package com.mold.digitalization.service;

/**
 * 鐧诲綍失败璁℃暟鍣ㄦ湇鍔℃帴鍙?
 * 鐢ㄤ簬璺熻釜鍜岀鐞嗙敤鎴风櫥褰曞け璐ョ殑娆℃暟
 */
public interface LoginFailureCounter {
    
    /**
     * 澧炲姞用户鐧诲綍失败璁℃暟
     * @param username 用户名?
     * @return 澧炲姞鍚庣殑失败娆℃暟
     */
    int incrementFailureCount(String username);
    
    /**
     * 閲嶇疆用户鐧诲綍失败璁℃暟
     * @param username 用户名?
     */
    void resetFailureCount(String username);
    
    /**
     * 获取用户褰撳墠鐧诲綍失败娆℃暟
     * @param username 用户名?
     * @return 失败娆℃暟
     */
    int getFailureCount(String username);
    
    /**
     * 妫€鏌ョ敤鎴锋槸鍚﹁揪鍒伴攣瀹氶槇鍊?
     * @param username 用户名?
     * @param threshold 鏈€澶уけ璐ユ鏁伴槇鍊?
     * @return 鏄惁杈惧埌閿佸畾闃堝€?
     */
    boolean isThresholdReached(String username, int threshold);
}
