package com.mold.process.exception;

/**
 * 获取RabbitMQ连接失败鐨勫紓甯哥被
 * 
 * @author backend team
 */
public class ConnectionAcquireException extends ConnectionPoolException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建获取连接开傚父
     * @param message 开傚父娑堟伅
     */
    public ConnectionAcquireException(String message) {
        super(message);
    }
    
    /**
     * 创建获取连接开傚父
     * @param message 开傚父娑堟伅
     * @param cause 鏍瑰紓甯?     */
    public ConnectionAcquireException(String message, Throwable cause) {
        super(message, cause);
    }
}
