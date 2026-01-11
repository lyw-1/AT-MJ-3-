package com.mold.process.util;

import java.util.List;

/**
 * RabbitMQ连接鐘舵€佹帴鍙?
 * 鎻愪緵连接鐘舵€佷俊鎭拰璇婃柇数据
 */
public interface ConnectionStatus {
    /**
     * 鍒ゆ柇鏄惁成功连接
     * @return 鏄惁宸茶繛鎺?     */
    boolean isConnected();
    
    /**
     * 获取连接璇︽儏淇℃伅
     * @return 连接璇︽儏瀛楃涓?     */
    String getConnectionDetails();
    
    /**
     * 设置连接璇︽儏
     * @param details 连接璇︽儏瀛楃涓?     */
    void setConnectionDetails(String details);
    
    /**
     * 获取鍙敤鐨勪氦鎹㈡満鍒楄〃
     * @return 浜ゆ崲鏈哄悕绉板垪琛?     */
    List<String> getAvailableExchanges();
    
    /**
     * 获取鍙敤鐨勯槦鍒楀垪琛?     * @return 闃熷垪鍚嶇О鍒楄〃
     */
    List<String> getAvailableQueues();
    
    /**
     * 设置鍙敤浜ゆ崲鏈哄垪琛?     * @param exchanges 浜ゆ崲鏈哄悕绉板垪琛?     */
    void setAvailableExchanges(List<String> exchanges);
    
    /**
     * 设置鍙敤闃熷垪鍒楄〃
     * @param queues 闃熷垪鍚嶇О鍒楄〃
     */
    void setAvailableQueues(List<String> queues);
    
    /**
     * 获取鏈€鍚庝竴娆￠敊璇俊鎭?     * @return 开傚父瀵硅薄锛屽鏋滄病鏈夐敊璇垯返回null
     */
    Exception getLastError();
    
    /**
     * 获取连接鑰楁椂锛堟绉掞級
     * @return 连接鑰楁椂
     */
    long getConnectionTime();
    
    /**
     * 设置连接状态     * @param connected 鏄惁连接
     */
    void setConnected(boolean connected);
    
    /**
     * 设置错误淇℃伅
     * @param error 开傚父瀵硅薄
     */
    void setLastError(Exception error);
}
