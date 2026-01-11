package com.mold.process.config;

/**
 * RabbitMQ连接姹犻厤缃帴鍙? * 
 * @author backend team
 */
public interface ConnectionPoolConfig {
    
    /**
     * 获取鏈€澶ц繛鎺ユ暟
     * @return 鏈€澶ц繛鎺ユ暟
     */
    int getMaxConnections();
    
    /**
     * 获取鍒濆连接鏁?     * @return 鍒濆连接鏁?     */
    int getInitialConnections();
    
    /**
     * 获取绌洪棽连接瓒呮椂鏃堕棿锛堟绉掞級
     * @return 绌洪棽连接瓒呮椂鏃堕棿
     */
    long getIdleTimeoutMs();
    
    /**
     * 获取连接鏈€澶у瓨娲绘椂闂达紙姣锛?     * @return 连接鏈€澶у瓨娲绘椂闂?     */
    long getConnectionMaxLifetimeMs();
    
    /**
     * 获取获取连接瓒呮椂鏃堕棿锛堟绉掞級
     * @return 获取连接瓒呮椂鏃堕棿
     */
    long getAcquireTimeoutMs();
    
    /**
     * 获取鍋ュ悍妫€鏌ラ棿闅旓紙姣锛?     * @return 鍋ュ悍妫€鏌ラ棿闅?     */
    long getHealthCheckIntervalMs();
}
