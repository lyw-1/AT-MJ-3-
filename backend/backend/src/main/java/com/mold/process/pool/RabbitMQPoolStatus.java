package com.mold.process.pool;

/**
 * 连接姹犵姸鎬佺殑鍏蜂綋实现
 * 
 * @author backend team
 */
class RabbitMQPoolStatus implements PoolStatus {
    
    private volatile int activeConnections;
    private volatile int idleConnections;
    private volatile int maxConnections;
    private volatile boolean initialized;
    private volatile boolean shutdown;
    
    /**
     * 创建连接姹犵姸鎬?
     */
    public RabbitMQPoolStatus(int maxConnections) {
        this.activeConnections = 0;
        this.idleConnections = 0;
        this.maxConnections = maxConnections;
        this.initialized = false;
        this.shutdown = false;
    }
    
    /**
     * 获取褰撳墠娲昏穬连接鏁?
     */
    @Override
    public int getActiveConnections() {
        return activeConnections;
    }
    
    /**
     * 获取褰撳墠绌洪棽连接鏁?
     */
    @Override
    public int getIdleConnections() {
        return idleConnections;
    }
    
    /**
     * 获取鎬昏繛鎺ユ暟
     */
    @Override
    public int getTotalConnections() {
        return activeConnections + idleConnections;
    }
    
    /**
     * 获取鏈€澶ц繛鎺ユ暟闄愬埗
     */
    @Override
    public int getMaxConnections() {
        return maxConnections;
    }
    
    /**
     * 获取连接姹犳槸鍚﹀凡初始化
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 获取连接姹犳槸鍚﹀凡关闭
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }
    
    /**
     * 澧炲姞娲昏穬连接鏁?
     */
    public synchronized void incrementActive() {
        activeConnections++;
    }
    
    /**
     * 鍑忓皯娲昏穬连接鏁?
     */
    public synchronized void decrementActive() {
        if (activeConnections > 0) {
            activeConnections--;
        }
    }
    
    /**
     * 澧炲姞绌洪棽连接鏁?
     */
    public synchronized void incrementIdle() {
        idleConnections++;
    }
    
    /**
     * 鍑忓皯绌洪棽连接鏁?
     */
    public synchronized void decrementIdle() {
        if (idleConnections > 0) {
            idleConnections--;
        }
    }
    
    /**
     * 设置绌洪棽连接鏁?
     */
    public synchronized void setIdle(int idle) {
        this.idleConnections = idle;
    }
    
    /**
     * 设置娲昏穬连接鏁?
     */
    public synchronized void setActive(int active) {
        this.activeConnections = active;
    }
    
    /**
     * 设置连接姹犲凡初始化
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    
    /**
     * 设置连接姹犲凡关闭
     */
    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }
    
    @Override
    public String toString() {
        return "RabbitMQPoolStatus{" +
                "activeConnections=" + activeConnections +
                ", idleConnections=" + idleConnections +
                ", totalConnections=" + getTotalConnections() +
                ", maxConnections=" + maxConnections +
                ", initialized=" + initialized +
                ", shutdown=" + shutdown +
                '}';
    }
}
