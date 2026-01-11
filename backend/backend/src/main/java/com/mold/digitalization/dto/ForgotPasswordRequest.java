package com.mold.digitalization.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 蹇樿密码璇锋眰DTO
 */
@Data
public class ForgotPasswordRequest {

    /**
     * 用户名嶏紙系统内部使用锛岀敱管理员樺垎閰嶏級
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    
    // 鎵嬪姩添加getter方法
    public String getUsername() {
        return username;
    }
}
