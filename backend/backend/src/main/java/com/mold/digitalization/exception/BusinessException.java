package com.mold.digitalization.exception;

import lombok.Getter;

/**
 * 涓氬姟开傚父绫?
 * 鐢ㄤ簬澶勭悊涓氬姟逻辑涓殑开傚父鎯呭喌
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 错误鐮?
     */
    private final int code;
    
    /**
     * 错误娑堟伅
     */
    private final String message;
    
    /**
     * 鏋勯€犲嚱鏁?
     * @param code 错误鐮?
     * @param message 错误娑堟伅
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 鏋勯€犲嚱鏁?
     * @param code 错误鐮?
     * @param message 错误娑堟伅
     * @param cause 鍘熷洜开傚父
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 鏋勯€犲嚱鏁?- 榛樿错误鐮佷负500
     * @param message 错误娑堟伅
     */
    public BusinessException(String message) {
        this(500, message);
    }
    
    /**
     * 鏋勯€犲嚱鏁?- 榛樿错误鐮佷负500
     * @param message 错误娑堟伅
     * @param cause 鍘熷洜开傚父
     */
    public BusinessException(String message, Throwable cause) {
        this(500, message, cause);
    }
    
    /**
     * 涓氬姟开傚父鏋氫妇
     */
    public enum ErrorCode {
    SUCCESS(200, "Operation successful"),
    BAD_REQUEST(400, "Bad request parameters"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    INTERNAL_ERROR(500, "Internal server error"),
    BUSINESS_ERROR(600, "Business logic error"),
    DATA_NOT_FOUND(601, "Data not found"),
    DATA_DUPLICATE(602, "Duplicate data"),
    DATA_INVALID(603, "Invalid data"),
    OPERATION_NOT_ALLOWED(604, "Operation not allowed"),
    RESOURCE_LOCKED(605, "Resource locked"),
    QUOTA_EXCEEDED(606, "Quota exceeded"),
    RATE_LIMIT_EXCEEDED(607, "Rate limit exceeded");
        
        private final int code;
        private final String message;
        
        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * 创建涓氬姟开傚父
     * @param errorCode 错误鐮佹灇涓?
     * @return 涓氬姟开傚父
     */
    public static BusinessException of(ErrorCode errorCode) {
        return new BusinessException(errorCode.getCode(), errorCode.getMessage());
    }
    
    /**
     * 创建涓氬姟开傚父
     * @param errorCode 错误鐮佹灇涓?
     * @param message 鑷畾涔夐敊璇秷鎭?
     * @return 涓氬姟开傚父
     */
    public static BusinessException of(ErrorCode errorCode, String message) {
        return new BusinessException(errorCode.getCode(), message);
    }
    
    /**
     * 创建涓氬姟开傚父
     * @param errorCode 错误鐮佹灇涓?
     * @param message 鑷畾涔夐敊璇秷鎭?
     * @param cause 鍘熷洜开傚父
     * @return 涓氬姟开傚父
     */
    public static BusinessException of(ErrorCode errorCode, String message, Throwable cause) {
        return new BusinessException(errorCode.getCode(), message, cause);
    }

    public int getCode() { return code; }
    @Override
    public String getMessage() { return message; }
}
