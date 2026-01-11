package com.mold.digitalization.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户瑙嗗浘瀵硅薄
 */
@Data
public class UserVO {
    
    /**
     * 涓婚敭ID
     */
    private Long id;
    
    /**
     * 用户名?
     */
    private String username;
    
    /**
     * 鐪熷疄濮撳悕
     */
    private String realName;
    
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // /**
    //  * 閭
    //  */
    // private String email;
    
    /**
     * 鎵嬫満鍙?
     */
    private String phone;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 鏈€鍚庣櫥褰曟椂闂?
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;
    
    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updateTime;
    
    /**
     * 瑙掕壊鍒楄〃
     */
    private List<String> roles;
    
    /**
     * 閮ㄩ棬ID
     */
    private Long departmentId;
    
    /**
     * 閮ㄩ棬鍚嶇О
     */
    private String departmentName;
    
    /**
     * 鐧诲綍失败娆℃暟
     */
    private Integer loginFailedCount;
    
    /**
     * 閿佸畾鏃堕棿
     */
    private LocalDateTime lockedUntil;

    /**
     * 设置瑙掕壊鍒楄〃
     * @param roles 瑙掕壊鍒楄〃
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}