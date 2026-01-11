package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体绫? * 瀵瑰簲数据搴撹〃锛歶ser
 */
@Data
@TableName("`user`")
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名嶏紝鍞竴
     */
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
    private String username;

    /**
     * 密码锛堝姞瀵嗗瓨鍌級
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password length must be between 6 and 100 characters")
    private String password;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 部门名称
     */
    @TableField(value = "department_name")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String department;

    /**
     * 角色（当前数据库无该列，避免 MyBatis-Plus 自动查询不存在的列）
     */
    @TableField(exist = false)
    @NotBlank(message = "Role must not be blank")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String role;

    /**
     * 联系电话
     */
    @TableField("phone")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid mobile phone format")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * 用户鐘舵€侊細0-绂佺敤锛?-鍚敤
     */
    @Min(value = 0, message = "Invalid user status")
    @Max(value = 1, message = "Invalid user status")
    private Integer status;
    
    /**
     * 鐧诲綍失败娆℃暟（当前数据库无该列，使用缓存记录）
     */
    @TableField(exist = false)
    private Integer loginFailedCount = 0;
    
    /**
     * 閿佸畾鎴鏃堕棿（当前数据库无该列，使用缓存管理）
     */
    @TableField(exist = false)
    private LocalDateTime lockedUntil;
    
    /**
     * 锁定原因（当前数据库无该列，使用缓存管理）
     */
    @TableField(exist = false)
    private String lockReason;
    
    /**
     * 閿佸畾操作浜哄鍚?（当前数据库无该列）
     */
    @TableField(exist = false)
    private String lockOperatorName;

    /**
     * 创建时间（当前数据库可能无该列，避免自动查询失败）
     */
    @TableField(exist = false)
    private LocalDateTime createTime;

    /**
     * 更新时间（当前数据库可能无该列，避免自动查询失败）
     */
    @TableField(exist = false)
    private LocalDateTime updateTime;
    
    /**
     * 鏈€鍚庝竴娆℃暟鏃堕棿（当前数据库无该列）
     */
    @TableField(exist = false)
    private LocalDateTime lastLoginTime;
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 获取用户名?     * @return 用户名?     */
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) { this.username = username; }
    
    /**
     * 获取鐪熷疄濮撳悕
     * @return 鐪熷疄濮撳悕
     */
    public String getRealName() {
        return realName;
    }
    
    /**
     * 获取鐢佃瘽鍙风爜
     * @return 鐢佃瘽鍙风爜
     */
    public String getPhone() {
        return phone;
    }
    public void setRealName(String realName) { this.realName = realName; }
    public void setPhone(String phone) { this.phone = phone; }
    
    /**
     * 获取用户状态     * @return 用户状态     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取创建鏃堕棿
     * @return 创建鏃堕棿
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    /**
     * 获取密码
     * @return 密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置用户ID
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 设置用户状态     * @param status 用户状态     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
     * @return 鏄惁琚攣瀹?     */
    @JsonIgnore
    public boolean isLocked() {
        return status != null && status == 0;
    }
    
    /**
     * 序列化只读属性：用于响应输出；反序列化时忽略该字段，避免请求体包含 "locked" 导致 400。
     */
    @JsonProperty(value = "locked", access = JsonProperty.Access.READ_ONLY)
    public boolean getLockedForJson() {
        return isLocked();
    }
    
    /**
     * 获取鐧诲綍失败娆℃暟
     * @return 鐧诲綍失败娆℃暟
     */
    public Integer getLoginFailedCount() {
        return loginFailedCount;
    }
    
    /**
     * 设置鐧诲綍失败娆℃暟
     * @param loginFailedCount 鐧诲綍失败娆℃暟
     */
    public void setLoginFailedCount(Integer loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }
    
    /**
     * 获取閿佸畾鎴鏃堕棿
     * @return 閿佸畾鎴鏃堕棿
     */
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
    
    /**
     * 设置閿佸畾鎴鏃堕棿
     * @param lockedUntil 閿佸畾鎴鏃堕棿
     */
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    /**
     * 获取锁定原因
     * @return 锁定原因
     */
    public String getLockReason() {
        return lockReason;
    }
    
    /**
     * 设置锁定原因
     * @param lockReason 锁定原因
     */
    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }
    
    /**
     * 获取閿佸畾操作浜哄鍚?     * @return 閿佸畾操作浜哄鍚?     */
    public String getLockOperatorName() {
        return lockOperatorName;
    }
    
    /**
     * 设置閿佸畾操作浜哄鍚?     * @param lockOperatorName 閿佸畾操作浜哄鍚?     */
    public void setLockOperatorName(String lockOperatorName) {
        this.lockOperatorName = lockOperatorName;
    }

    /**
     * 获取鏈€鍚庝竴娆℃暟鏃堕棿
     * @return 鏈€鍚庝竴娆℃暟鏃堕棿
     */
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置鏈€鍚庝竴娆℃暟鏃堕棿
     * @param lastLoginTime 鏈€鍚庝竴娆℃暟鏃堕棿
     */
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
