package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 鐢熶骇浠诲姟实体绫? * 瀵瑰簲数据搴撹〃锛歱roduction_task
 */
@Data
@TableName("production_task")
public class ProductionTask {

    /**
     * 浠诲姟ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 浠诲姟缂栧彿锛屽敮涓€
     */
    @TableField("task_code")
    private String taskCode;

    /**
     * 浠诲姟鍚嶇О
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 璁惧ID锛屽閿叧鑱攅quipment琛?     */
    @TableField("equipment_id")
    private Long equipmentId;

    /**
     * 操作浜哄憳ID锛屽閿叧鑱攗ser琛?     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 当前工序，关联工序表
     */
    @TableField("current_process")
    private String currentProcess;

    /**
     * 璁″垝鐢熶骇鏁伴噺
     */
    @TableField("planned_quantity")
    private Integer plannedQuantity;

    /**
     * 实际鐢熶骇鏁伴噺
     */
    @TableField("actual_quantity")
    private Integer actualQuantity;

    /**
     * 鍚堟牸鏁伴噺
     */
    @TableField("qualified_quantity")
    private Integer qualifiedQuantity;

    /**
     * 涓嶅悎鏍兼暟閲?     */
    @TableField("defective_quantity")
    private Integer defectiveQuantity;

    /**
     * 浠诲姟鐘舵€侊細0-鏈紑濮嬶紝1-杩涜涓紝2-宸插畬鎴愶紝3-宸插彇娑?     */
    @TableField("status")
    private Integer status;

    /**
     * 开€濮嬫椂闂?     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 缁撴潫鏃堕棿
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 鎻忚堪淇℃伅
     */
    @TableField(value = "description", updateStrategy = FieldStrategy.NOT_NULL)
    private String description;

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
     * 设置浠诲姟状态     * @param status 浠诲姟状态     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * 设置浠诲姟ID
     * @param id 浠诲姟ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }
    public String getTaskName() { return taskName; }
    public Integer getPlannedQuantity() { return plannedQuantity; }
    public Integer getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getUpdateTime() { return updateTime; }
}
