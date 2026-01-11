package com.mold.digitalization.dto;

import lombok.Data;
import com.mold.digitalization.validation.ValidPassword;

/**
 * 管理员橀噸缃瘑鐮佽姹侱TO
 */
@Data
public class PasswordResetRequest {

    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 新密码（可选，不传或传空则使用默认初始密码）
     * 注：将校验器设置为允许为空，以便走默认密码分支；
     *     当提供具体密码时，仍由 PasswordValidator + PasswordStrengthChecker 进行合规校验。
     */
    @ValidPassword(nullable = true)
    private String newPassword;
    
    /**
     * 纭密码
     */
    private String confirmPassword;
    
    // 鎵嬪姩添加getter方法
    public Long getUserId() {
        return userId;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
