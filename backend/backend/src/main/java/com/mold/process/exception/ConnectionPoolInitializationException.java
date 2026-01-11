package com.mold.process.exception;

/**
 * RabbitMQ连接姹犲垵濮嬪寲失败鐨勫紓甯哥被
 * 
 * @author backend team
 */
public class ConnectionPoolInitializationException extends ConnectionPoolException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建鍒濆鍖栧紓甯?
     * @param message 开傚父娑堟伅
     */
    public ConnectionPoolInitializationException(String message) {
        super(message);
    }
    
    /**
     * 创建鍒濆鍖栧紓甯?
     * @param message 开傚父娑堟伅
     * @param cause 鏍瑰紓甯?
     */
    public ConnectionPoolInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
