package com.mold.digitalization.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 鐧诲綍璇锋眰DTO
 */
@Data
public class LoginRequest {

    @NotBlank(message = "用户名嶄笉鑳戒负绌?")
    private String username;

    @NotBlank(message = "密码涓嶈兘为空")
    private String password;

    // 鎵嬪姩添加getter方法锛屽洜涓篅Data娉ㄨВ鍙兘娌℃湁姝ｇ‘宸ヤ綔
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
