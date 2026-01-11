package com.mold.digitalization.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户閿佸畾/瑙ｉ攣璇锋眰DTO
 */
@Data
public class UserLockRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "User ID must not be null")
    private Long userId;

    /**
     * 操作绫诲瀷锛歭ock-閿佸畾锛寀nlock-瑙ｉ攣
     */
    @NotBlank(message = "Operation type must not be blank")
    private String operationType;
    
    /**
     * 锁定原因锛堜粎鍦ㄩ攣瀹氭椂蹇呭～锛?
     */
    @Size(max = 200, message = "Lock reason must not exceed 200 characters")
    private String lockReason;
    
    // 鎵嬪姩添加getter方法
    public Long getUserId() {
        return userId;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public String getLockReason() {
        return lockReason;
    }
}
