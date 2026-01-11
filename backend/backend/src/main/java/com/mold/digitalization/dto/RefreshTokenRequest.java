package com.mold.digitalization.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 鍒锋柊浠ょ墝璇锋眰DTO
 */
@Data
public class RefreshTokenRequest {

    @NotBlank(message = "鍒锋柊浠ょ墝涓嶈兘为空")
    private String refreshToken;
    
    /**
     * 获取鍒锋柊浠ょ墝
     * @return 鍒锋柊浠ょ墝
     */
    public String getRefreshToken() {
        return refreshToken;
    }
}
