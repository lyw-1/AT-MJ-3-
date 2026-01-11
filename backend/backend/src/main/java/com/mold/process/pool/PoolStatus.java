package com.mold.process.pool;

/**
 * 连接姹犵姸鎬佹帴鍙ｏ紝鎻愪緵连接姹犲綋鍓嶇姸鎬佺殑查询方法
 * 
 * @author backend team
 */
public interface PoolStatus {
    
    /**
     * 获取褰撳墠娲昏穬连接鏁?     * @return 娲昏穬连接鏁?     */
    int getActiveConnections();
    
    /**
     * 获取褰撳墠绌洪棽连接鏁?     * @return 绌洪棽连接鏁?     */
    int getIdleConnections();
    
    /**
     * 获取鎬昏繛鎺ユ暟
     * @return 鎬昏繛鎺ユ暟
     */
    int getTotalConnections();
    
    /**
     * 获取鏈€澶ц繛鎺ユ暟闄愬埗
     * @return 鏈€澶ц繛鎺ユ暟
     */
    int getMaxConnections();
    
    /**
     * 获取连接姹犳槸鍚﹀凡初始化     * @return 鏄惁宸插垵濮嬪寲
     */
    boolean isInitialized();
    
    /**
     * 获取连接姹犳槸鍚﹀凡关闭
     * @return 鏄惁宸插叧闂?     */
    boolean isShutdown();
}
