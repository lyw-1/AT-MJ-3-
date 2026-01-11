package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户瑙掕壊鍏宠仈实体绫? * 瀵瑰簲数据搴撹〃锛歶ser_role
 */
@Data
@TableName("user_role")
public class UserRole {

    /**
     * 鍏宠仈ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID锛屽閿叧鑱攗ser琛?     */
    @TableField("user_id")
    private Long userId;

    /**
     * 瑙掕壊ID锛屽閿叧鑱攔ole琛?     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 创建鏃堕棿
     */
    @TableField(exist = false)
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    @TableField(exist = false)
    private LocalDateTime updateTime;
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 获取瑙掕壊ID
     * @return 瑙掕壊ID
     */
    public Long getRoleId() {
        return roleId;
    }
    
    /**
     * 设置瑙掕壊ID
     * @param roleId 瑙掕壊ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    /**
     * 获取创建鏃堕棿
     * @return 创建鏃堕棿
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置创建鏃堕棿
     * @param createTime 创建鏃堕棿
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取更新鏃堕棿
     * @return 更新鏃堕棿
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 设置更新鏃堕棿
     * @param updateTime 更新鏃堕棿
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
