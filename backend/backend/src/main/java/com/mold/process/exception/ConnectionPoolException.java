package com.mold.process.exception;

/**
 * RabbitMQ连接姹犳搷浣滅殑鍩虹开傚父绫? * 
 * @author backend team
 */
public class ConnectionPoolException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建连接姹犲紓甯?     * @param message 开傚父娑堟伅
     */
    public ConnectionPoolException(String message) {
        super(message);
    }
    
    /**
     * 创建连接姹犲紓甯?     * @param message 开傚父娑堟伅
     * @param cause 鏍瑰紓甯?     */
    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
