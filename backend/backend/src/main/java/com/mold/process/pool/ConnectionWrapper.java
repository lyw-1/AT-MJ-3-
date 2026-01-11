package com.mold.process.pool;

import com.rabbitmq.client.Connection;

/**
 * 连接鍖呰绫伙紝鐢ㄤ簬璺熻釜连接鐨勪娇鐢ㄦ儏鍐靛拰鍏冩暟鎹? * 
 * @author backend team
 */
class ConnectionWrapper {
    
    private final Connection connection;
    private final long creationTime;
    private long lastUsedTime;
    private boolean active;
    
    /**
     * 创建连接鍖呰
     * @param connection RabbitMQ连接
     */
    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
        this.creationTime = System.currentTimeMillis();
        this.lastUsedTime = System.currentTimeMillis();
        this.active = false;
    }
    
    /**
     * 获取RabbitMQ连接
     * @return 鍘熷连接瀵硅薄
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * 获取连接创建鏃堕棿
     * @return 创建鏃堕棿鎴筹紙姣锛?     */
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * 获取鏈€鍚庝娇鐢ㄦ椂闂?     * @return 鏈€鍚庝娇鐢ㄦ椂闂存埑锛堟绉掞級
     */
    public long getLastUsedTime() {
        return lastUsedTime;
    }
    
    /**
     * 更新鏈€鍚庝娇鐢ㄦ椂闂翠负褰撳墠鏃堕棿
     */
    public void updateLastUsedTime() {
        this.lastUsedTime = System.currentTimeMillis();
    }
    
    /**
     * 获取连接鏄惁娲昏穬
     * @return 鏄惁琚娇鐢ㄤ腑
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * 设置连接娲昏穬状态     * @param active 鏄惁娲昏穬
     */
    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            updateLastUsedTime();
        }
    }
    
    /**
     * 获取连接瀛樻椿鏃堕棿
     * @return 瀛樻椿鏃堕棿锛堟绉掞級
     */
    public long getLifetime() {
        return System.currentTimeMillis() - creationTime;
    }
    
    /**
     * 获取连接绌洪棽鏃堕棿
     * @return 绌洪棽鏃堕棿锛堟绉掞級
     */
    public long getIdleTime() {
        return System.currentTimeMillis() - lastUsedTime;
    }
    
    /**
     * 妫€鏌ヨ繛鎺ユ槸鍚︽湁鏁?     * @return 鏄惁鏈夋晥
     */
    public boolean isValid() {
        return connection != null && connection.isOpen();
    }
    
    /**
     * 关闭连接
     */
    public void close() {
        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (Exception e) {
                // 记录关闭开傚父浣嗕笉鎶涘嚭
            }
        }
    }
}
