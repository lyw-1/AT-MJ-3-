package com.mold.digitalization.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 缁熶竴鍝嶅簲DTO
 */
@Data
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 鐘舵€佺爜
    private int code;

    // 娑堟伅
    private String message;

    // 数据
    private T data;

    /**
     * 成功鍝嶅簲
     */
    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 成功鍝嶅簲锛堝甫娑堟伅锛?     */
    public static <T> ResponseDTO<T> success(T data, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 错误鍝嶅簲
     */
    public static <T> ResponseDTO<T> error(int code, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 错误鍝嶅簲锛堥粯璁ょ姸鎬佺爜锛?     */
    public static <T> ResponseDTO<T> error(String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 设置鐘舵€佺爜
     * @param code 鐘舵€佺爜
     */
    public void setCode(int code) {
        this.code = code;
    }
    
    /**
     * 设置娑堟伅
     * @param message 娑堟伅
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 设置数据
     * @param data 数据
     */
    public void setData(T data) {
        this.data = data;
    }
}
