package com.mold.digitalization.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 密码閲嶇疆结果DTO
 */
@Data
public class ResetPasswordResult {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 閲嶇疆鏃堕棿
     */
    private LocalDateTime resetTime;
    
    /**
     * 閲嶇疆鏃堕棿锛圖ate绫诲瀷锛岀敤浜庡吋瀹规棫浠ｇ爜锛?     */
    private Date resetTimeDate;

    /**
     * 鏄惁浣跨敤浜嗛粯璁ゅ瘑鐮?     */
    private boolean defaultPassword;
    
    /**
     * 默认密码具体值（仅在本次响应中返回，便于管理员通知用户；日志中将进行脱敏处理）
     */
    private String defaultPasswordValue;
    
    /**
     * 设置用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 设置閲嶇疆鏃堕棿锛圠ocalDateTime锛?     */
    public void setResetTime(LocalDateTime resetTime) {
        this.resetTime = resetTime;
    }
    
    /**
     * 设置閲嶇疆鏃堕棿锛圖ate绫诲瀷锛岀敤浜庡吋瀹规棫浠ｇ爜锛?     */
    public void setResetTime(Date resetTimeDate) {
        this.resetTimeDate = resetTimeDate;
        if (resetTimeDate != null) {
            this.resetTime = LocalDateTime.ofInstant(resetTimeDate.toInstant(), 
                java.time.ZoneId.systemDefault());
        }
    }
    
    /**
     * 设置鏄惁浣跨敤榛樿密码
     */
    public void setDefaultPassword(boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
    
    /**
     * 设置默认密码具体值
     */
    public void setDefaultPasswordValue(String defaultPasswordValue) {
        this.defaultPasswordValue = defaultPasswordValue;
    }
}
