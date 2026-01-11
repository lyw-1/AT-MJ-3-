package com.mold.process.common;

/**
 * 鍝嶅簲结果鐘舵€佺爜鏋氫妇
 * 瀹氫箟系统涓墍鏈夊彲鑳界殑鍝嶅簲鐘舵€佺爜
 */
public enum ResultCode {

    // 成功鐘舵€佺爜
    SUCCESS(200, "Operation successful"),
    FAILED(1004, "Operation failed"),

    // System errors
    SYSTEM_ERROR(500, "System internal error"),
    SERVICE_UNAVAILABLE(503, "Service temporarily unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway timeout"),

    // Parameter errors
    PARAM_ERROR(400, "Request parameter error"),
    INVALID_REQUEST(400, "Invalid request"),
    MISSING_PARAM(400, "Missing required parameter"),

    // Authentication / authorization
    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRED(401, "Token expired"),
    TOKEN_INVALID(401, "Invalid token"),
    NO_PERMISSION(403, "No permission"),

    // Resource errors
    NOT_FOUND(404, "Resource not found"),
    RESOURCE_LOCKED(423, "Resource locked"),
    RESOURCE_CONFLICT(409, "Resource conflict"),

    // Business errors
    BUSINESS_ERROR(600, "Business logic error"),
    DATA_ERROR(601, "Data error"),
    OPERATION_FAILED(602, "Operation failed"),

    // Database errors
    DATA_INTEGRITY_VIOLATION(701, "Data integrity violation"),
    DUPLICATE_KEY(702, "Duplicate key"),

    // File errors
    FILE_UPLOAD_ERROR(800, "File upload failed"),
    FILE_DOWNLOAD_ERROR(801, "File download failed"),
    FILE_SIZE_EXCEED(802, "File size exceeds limit"),
    FILE_TYPE_ERROR(803, "Invalid file type"),

    // Application-specific codes
    USER_NOT_FOUND(10001, "User not found"),
    USER_DISABLED(10002, "User disabled"),
    DEPARTMENT_NOT_FOUND(20001, "Department not found"),
    ROLE_NOT_FOUND(30001, "Role not found"),
    PERMISSION_NOT_FOUND(40001, "Permission not found"),
    MOLD_NOT_FOUND(50001, "Mold not found"),
    PART_NOT_FOUND(60001, "Part not found"),
    INVENTORY_NOT_FOUND(70001, "Inventory record not found"),
    INVENTORY_INSUFFICIENT(70002, "Inventory insufficient");

    private final int code;
    private final String message;

    /**
     * 鏋勯€犳柟娉?
     * @param code 鐘舵€佺爜
     * @param message 鐘舵€佷俊鎭?
     */
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取鐘舵€佺爜
     * @return 鐘舵€佺爜
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取鐘舵€佷俊鎭?
     * @return 鐘舵€佷俊鎭?
     */
    public String getMessage() {
        return message;
    }

    /**
     * 鏍规嵁鐘舵€佺爜获取鏋氫妇
     * @param code 鐘舵€佺爜
     * @return ResultCode鏋氫妇
     */
    public static ResultCode getByCode(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", message='" + message + "'" +
                '}';
    }
}
