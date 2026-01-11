package com.mold.process.pool;

import com.mold.process.exception.ConnectionPoolException;
import com.mold.process.exception.ConnectionPoolInitializationException;
import com.rabbitmq.client.Connection;

/**
 * RabbitMQ连接姹犳帴鍙ｏ紝鎻愪緵连接获取鍜屽綊杩樼殑核心API
 * 
 * @author backend team
 */
public interface ConnectionPool extends AutoCloseable {
    
    /**
     * 获取涓€涓猂abbitMQ连接
     * @return 鍙敤鐨勮繛鎺?     * @throws ConnectionPoolException 获取连接失败鏃舵姏鍑?     */
    Connection getConnection() throws ConnectionPoolException;
    
    /**
     * 褰掕繕连接鍒拌繛鎺ユ睜
     * @param connection 瑕佸綊杩樼殑连接
     * @throws ConnectionPoolException 褰掕繕连接失败鏃舵姏鍑?     */
    void returnConnection(Connection connection) throws ConnectionPoolException;
    
    /**
     * 鍒濆鍖栬繛鎺ユ睜
     * @throws ConnectionPoolInitializationException 鍒濆鍖栧け璐ユ椂鎶涘嚭
     */
    void initialize() throws ConnectionPoolInitializationException;
    
    /**
     * 关闭连接姹狅紝閲婃斁鎵€鏈夎祫婧?     */
    void shutdown();
    
    /**
     * 获取连接姹犵姸鎬佷俊鎭?     * @return 连接姹犵姸鎬?     */
    PoolStatus getStatus();
    
    /**
     * 绔嬪嵆创建鎸囧畾鏁伴噺鐨勮繛鎺?     * @param count 瑕佸垱寤虹殑连接鏁伴噺
     */
    void prewarm(int count);
}
