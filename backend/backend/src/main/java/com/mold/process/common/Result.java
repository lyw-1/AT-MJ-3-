package com.mold.process.common;

import java.io.Serializable;

/**
 * 缁熶竴鍝嶅簲结果绫? * 瑙勮寖API返回鐨勬暟鎹粨鏋? */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 鐘舵€佺爜锛?00琛ㄧず成功锛屽叾浠栬〃绀洪敊璇?     */
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
     * 鏃堕棿鎴?     */
    private long timestamp;

    /**
     * 鏋勯€犳柟娉?     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 鏋勯€犳柟娉?     * @param code 鐘舵€佺爜
     * @param message 鍝嶅簲娑堟伅
     * @param data 鍝嶅簲数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功鍝嶅簲
     * @param data 鍝嶅簲数据
     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功鍝嶅簲锛堟棤数据锛?     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 错误鍝嶅簲
     * @param resultCode 错误鐮佹灇涓?     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 错误鍝嶅簲锛堣嚜瀹氫箟娑堟伅锛?     * @param resultCode 错误鐮佹灇涓?     * @param message 鑷畾涔夐敊璇秷鎭?     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    /**
     * 错误鍝嶅簲锛堣嚜瀹氫箟鐘舵€佺爜鍜屾秷鎭級
     * @param code 鑷畾涔夌姸鎬佺爜
     * @param message 鑷畾涔夐敊璇秷鎭?     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败鍝嶅簲锛堝揩鎹锋柟娉曪級
     * @param message 错误娑堟伅
     * @param <T> 数据绫诲瀷
     * @return Result瀵硅薄
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    // getter鍜宻etter方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + "'" +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
