package com.mold.digitalization.vo;

import lombok.Data;

import java.util.List;

/**
 * 鐧诲綍返回瀵硅薄
 */
@Data
public class LoginVO {
    
    /**
     * JWT浠ょ墝
     */
    private String token;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名?     */
    private String username;
    
    /**
     * 鐪熷疄濮撳悕
     */
    private String realName;
    
    /**
     * 瑙掕壊鍒楄〃
     */
    private List<String> roles;

    /**
     * 设置瑙掕壊鍒楄〃
     * @param roles 瑙掕壊鍒楄〃
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * 设置JWT浠ょ墝
     * @param token JWT浠ょ墝
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 设置用户名?     * @param username 用户名?     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 设置鐪熷疄濮撳悕
     * @param realName 鐪熷疄濮撳悕
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }
}
