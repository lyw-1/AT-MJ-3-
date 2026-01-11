package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿鐘舵€佸疄浣撶被
 * 瀵瑰簲数据搴撹〃锛歮old_status
 */
@Data
@TableName("mold_status")
public class MoldStatus {

    /**
     * 鐘舵€両D锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 鐘舵€佸悕绉?     */
    private String statusName;

    /**
     * 鐘舵€佷唬鐮侊紝鍞竴
     */
    private String statusCode;

    /**
     * 鐘舵€佹弿杩?     */
    private String description;

    /**
     * 鐘舵€侀鑹叉爣璇?     */
    private String color;

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
    
    // 鎵嬪姩添加setter方法浠ョ‘淇濈紪璇戦€氳繃
    public void setId(Long id) {
        this.id = id;
    }
}