package com.mold.digitalization.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误浠ｇ爜鏋氫妇
 * 瀹氫箟系统涓娇鐢ㄧ殑鏍囧噯错误鐮佸拰娑堟伅
 */
public enum ErrorCodeEnum {
    
    // Success codes
    SUCCESS(200, "操作成功"),

    // System errors
    SYSTEM_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    // Parameter errors
    PARAM_ERROR(400, "参数错误"),
    PARAM_VALIDATE_ERROR(401, "参数校验失败"),
    NULL_POINTER_ERROR(402, "空指针异常"),
    ARGUMENT_ERROR(403, "参数异常"),

    // Business errors
    BUSINESS_ERROR(1000, "业务异常"),
    DATA_NOT_FOUND(1001, "数据未找到"),
    DATA_ALREADY_EXIST(1002, "数据已存在"),
    OPERATION_FAILED(1003, "操作失败"),
    FAILED(1004, "失败"),
    STATE_INVALID(1005, "状态非法"),
    PERMISSION_DENIED(1006, "无权限"),
    INVALID_OPERATION(1007, "非法操作"),

    // Authorization errors
    UNAUTHORIZED(401, "未认证"),
    TOKEN_EXPIRED(4011, "令牌过期"),
    TOKEN_INVALID(4012, "令牌无效"),
    USER_NOT_LOGIN(4013, "用户未登录"),
    LOGIN_FAILED(4014, "登录失败"),
    USERNAME_OR_PASSWORD_ERROR(4015, "用户名或密码错误"),
    USER_DISABLED(4016, "用户已禁用"),

    // User related
    USER_NOT_FOUND(10001, "用户未找到"),
    USERNAME_ALREADY_EXISTS(10002, "用户名已存在"),
    PASSWORD_INCORRECT(10003, "密码错误"),

    // File errors
    FILE_UPLOAD_FAILED(2000, "文件上传失败"),
    FILE_DOWNLOAD_FAILED(2001, "文件下载失败"),
    FILE_NOT_FOUND(2002, "文件不存在"),
    FILE_SIZE_EXCEEDED(2003, "文件大小超出限制"),
    FILE_TYPE_NOT_ALLOWED(2004, "文件类型不允许"),

    // Database errors
    DATABASE_ERROR(3000, "数据库错误"),
    SQL_EXCEPTION(3001, "SQL异常"),
    DATA_INTEGRITY_VIOLATION(3002, "数据完整性违反"),

    // Mold related
    MOLD_NOT_FOUND(4000, "模具未找到"),
    MOLD_IN_USE(4001, "模具使用中"),
    MOLD_IN_REPAIR(4002, "模具维修中"),
    MOLD_SCRAPPED(4003, "模具已报废"),
    MOLD_QUANTITY_INSUFFICIENT(4004, "模具数量不足"),
    MOLD_NOT_IN_STORAGE(4005, "模具未入库"),
    MOLD_CODE_EXIST(4006, "模具编码已存在"),

    // Other
    UNKNOWN_ERROR(9999, "未知错误");
    
    /**
     * 错误鐮?
     */
    private final Integer code;
    
    /**
     * 错误娑堟伅
     */
    private final String message;
    
    /**
     * 鏋勯€犲嚱鏁?
     * @param code 错误鐮?
     * @param message 错误娑堟伅
     */
    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 获取错误鐮?
     * @return 错误鐮?
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取错误娑堟伅
     * @return 错误娑堟伅
     */
    public String getMessage() {
        return message;
    }
}
