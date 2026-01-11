package com.mold.digitalization.entity.dto.response;

import lombok.Data;

/**
 * 閫氱敤API鍝嶅簲鍖呰绫? * 鐢ㄤ簬缁熶竴API返回鏍煎紡
 */
@Data
public class ApiResponse<T> {

    /**
     * 鍝嶅簲鐘舵€佺爜锛?-成功锛岄潪0-失败
     */
    private int code;

    /**
     * 鍝嶅簲娑堟伅
     */
    private String message;

    /**
     * 鍝嶅簲数据
     */
    private T data;

    /**
     * 鏋勯€犲嚱鏁?     */
    public ApiResponse() {
    }

    /**
     * 鏋勯€犲嚱鏁?     * @param code 鐘舵€佺爜
     * @param message 娑堟伅
     * @param data 数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功鍝嶅簲
     * @param data 鍝嶅簲数据
     * @param <T> 数据绫诲瀷
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    /**
     * 成功鍝嶅簲锛堟棤数据锛?     * @param <T> 数据绫诲瀷
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(0, "success", null);
    }

    /**
     * 失败鍝嶅簲
     * @param code 错误鐮?     * @param message 错误娑堟伅
     * @param <T> 数据绫诲瀷
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 失败鍝嶅簲锛堥粯璁ら敊璇爜锛?     * @param message 错误娑堟伅
     * @param <T> 数据绫诲瀷
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(1, message, null);
    }
}
