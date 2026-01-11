package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 鏉冮檺实体绫?
 * 鐢ㄤ簬瀛樺偍系统鏉冮檺淇℃伅
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("permission")
public class Permission {
    
    /**
     * 鏉冮檺ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 鏉冮檺鍚嶇О
     */
    private String name;
    
    /**
     * 鏉冮檺缂栫爜
     */
    private String code;
    public String getCode() { return code; }
    
    /**
     * 鏉冮檺绫诲瀷
     */
    private String type;
    
    /**
     * 鏉冮檺鎻忚堪
     */
    private String description;
    
    /**
     * 鐖舵潈闄怚D
     */
    private Long parentId;
    
    /**
     * 鎺掑簭
     */
    private Integer sort;
    
    /**
     * 鐘舵€侊紙0锛氱鐢紝1锛氬惎鐢級
     */
    private Integer status;
    
    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;
    
    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建鑰匢D
     */
    private Long createBy;
    
    /**
     * 更新鑰匢D
     */
    private Long updateBy;
    
    /**
     * 鏄惁删除锛?锛氭湭删除锛?锛氬凡删除锛?
     */
    private Integer deleted;
}
