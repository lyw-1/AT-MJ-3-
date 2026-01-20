package com.mold.digitalization.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 增强版统一响应DTO
 * 包含时间戳、请求追踪ID和详细信息
 */
@Data
public class EnhancedResponseDTO<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final boolean IS_DEV = isDevelopmentEnvironment();
    
    // 状态码
    private int code;
    
    // 消息
    private String message;
    
    // 数据
    private T data;
    
    // 时间戳
    private long timestamp;
    
    // 请求追踪ID
    private String requestId;
    
    // 详细信息（仅开发环境显示）
    private Object details;
    
    // ==================== 成功响应 ====================
    
    /**
     * 成功响应（最小格式）
     */
    public static <T> EnhancedResponseDTO<T> success(T data) {
        return success(data, "success");
    }
    
    /**
     * 成功响应（带自定义消息）
     */
    public static <T> EnhancedResponseDTO<T> success(T data, String message) {
        EnhancedResponseDTO<T> response = new EnhancedResponseDTO<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        response.setRequestId(generateRequestId());
        return response;
    }
    
    // ==================== 错误响应 ====================
    
    /**
     * 错误响应（最小格式）
     */
    public static <T> EnhancedResponseDTO<T> error(int code, String message) {
        return error(code, message, null);
    }
    
    /**
     * 错误响应（带详情）
     */
    public static <T> EnhancedResponseDTO<T> error(int code, String message, Object details) {
        EnhancedResponseDTO<T> response = new EnhancedResponseDTO<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        response.setRequestId(generateRequestId());
        
        // 开发环境显示详细信息，生产环境隐藏
        response.setDetails(IS_DEV ? details : null);
        
        return response;
    }
    
    /**
     * 使用错误码枚举创建错误响应
     */
    public static <T> EnhancedResponseDTO<T> error(Enum<?> errorCode) {
        return error(errorCode, null);
    }
    
    /**
     * 使用错误码枚举创建错误响应（带自定义详情）
     */
    public static <T> EnhancedResponseDTO<T> error(Enum<?> errorCode, Object details) {
        try {
            // 尝试获取code和message字段
            java.lang.reflect.Method getCode = errorCode.getClass().getMethod("getCode");
            java.lang.reflect.Method getMessage = errorCode.getClass().getMethod("getMessage");
            
            int code = (int) getCode.invoke(errorCode);
            String message = (String) getMessage.invoke(errorCode);
            
            return error(code, message, details);
        } catch (Exception e) {
            // 如果反射失败，使用默认错误
            return error(500, "系统错误", e.getMessage());
        }
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 生成请求追踪ID
     */
    private static String generateRequestId() {
        return "req-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 判断是否为开发环境
     */
    private static boolean isDevelopmentEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "");
        return profile.contains("dev") || profile.isEmpty();
    }
    
    // ==================== 便捷方法 ====================
    
    /**
     * 创建成功响应（链式调用）
     */
    public EnhancedResponseDTO<T> withData(T data) {
        this.setData(data);
        return this;
    }
    
    /**
     * 创建成功响应（链式调用）
     */
    public EnhancedResponseDTO<T> withMessage(String message) {
        this.setMessage(message);
        return this;
    }
    
    /**
     * 检查是否为成功响应
     */
    public boolean isSuccess() {
        return this.code == 200;
    }
    
    /**
     * 检查是否为错误响应
     */
    public boolean isError() {
        return this.code != 200;
    }
}
