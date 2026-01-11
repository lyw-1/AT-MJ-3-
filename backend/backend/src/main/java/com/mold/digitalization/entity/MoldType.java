package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿绫诲瀷实体绫? * 瀵瑰簲数据搴撹〃锛歮old_type
 */
@Data
@TableName("mold_type")
public class MoldType {

    /**
     * 妯″叿绫诲瀷ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 绫诲瀷鍚嶇О
     */
    private String typeName;

    /**
     * 绫诲瀷浠ｇ爜锛屽敮涓€
     */
    private String typeCode;

    /**
     * 绫诲瀷鎻忚堪
     */
    private String description;

    /**
     * 鎺掑簭鍙?     */
    private Integer sortOrder;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updateTime;
    
    /**
     * 设置妯″叿绫诲瀷ID
     * @param id 妯″叿绫诲瀷ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取妯″叿绫诲瀷ID
     * @return 妯″叿绫诲瀷ID
     */
    public Long getId() {
        return id;
    }
}
