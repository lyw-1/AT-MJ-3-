package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 瑙掕壊实体绫?
 * 瀵瑰簲数据搴撹〃锛歳ole
 */
@Data
@TableName("role")
public class Role {

    /**
     * 瑙掕壊ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 瑙掕壊鍚嶇О
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 瑙掕壊浠ｇ爜锛屽敮涓€
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 瑙掕壊鎻忚堪
     */
    @TableField("description")
    private String description;

    /**
     * 瑙掕壊鐘舵€侊細0-绂佺敤锛?-鍚敤
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建鏃堕棿
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 获取瑙掕壊浠ｇ爜
     * @return 瑙掕壊浠ｇ爜
     */
    public String getRoleCode() {
        return roleCode;
    }
    
    /**
     * 设置瑙掕壊ID
     * @param id 瑙掕壊ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取瑙掕壊ID
     * @return 瑙掕壊ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 获取瑙掕壊状态
     * @return 瑙掕壊状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * 设置瑙掕壊状态
     * @param status 瑙掕壊状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
