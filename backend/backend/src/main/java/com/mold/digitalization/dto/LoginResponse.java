package com.mold.digitalization.dto;

import lombok.Data;

/**
 * 鐧诲綍鍝嶅簲DTO
 */
@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private Long userId;
    private String roles;
    
    /**
     * 设置用户瑙掕壊
     * @param roles 瑙掕壊瀛楃涓?     */
    public void setRoles(String roles) {
        this.roles = roles;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
