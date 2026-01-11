package com.mold.process.exception;

/**
 * 褰掕繕RabbitMQ连接失败鐨勫紓甯哥被
 * 
 * @author backend team
 */
public class ConnectionReturnException extends ConnectionPoolException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建褰掕繕连接开傚父
     * @param message 开傚父娑堟伅
     */
    public ConnectionReturnException(String message) {
        super(message);
    }
    
    /**
     * 创建褰掕繕连接开傚父
     * @param message 开傚父娑堟伅
     * @param cause 鏍瑰紓甯?     */
    public ConnectionReturnException(String message, Throwable cause) {
        super(message, cause);
    }
}