package com.mold.digitalization.dto;

import lombok.Data;
import com.mold.digitalization.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 閫氳繃验证鐮侀噸缃瘑鐮佽姹侱TO
 */
@Data
public class ResetPasswordByCodeRequest {

    /**
     * 閲嶇疆验证鐮?
     */
    @NotBlank(message = "Reset code must not be blank")
    private String resetCode;

    /**
     * 鏂板瘑鐮?
     */
    @NotBlank(message = "New password must not be blank")
    @Size(min = 8, max = 20, message = "Password length must be between 8 and 20 characters")
    @ValidPassword
    private String newPassword;

    /**
     * 纭鏂板瘑鐮?
     */
    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;
    
    // 鎵嬪姩添加getter方法
    public String getResetCode() {
        return resetCode;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
