package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿鍔犲伐娴佺▼实体绫? * 瀵瑰簲数据搴撹〃锛歮old_process
 */
@Data
@TableName("mold_process")
public class MoldProcess {

    /**
     * 娴佺▼ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 娴佺▼缂栧彿锛屽敮涓€
     */
    @TableField("process_code")
    private String processCode;

    /**
     * 娴佺▼鍚嶇О
     */
    @TableField("process_name")
    private String processName;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 鐢熶骇浠诲姟ID锛屽閿叧鑱攑roduction_task琛?     */
    @TableField("production_task_id")
    private Long productionTaskId;

    /**
     * 褰撳墠鐘舵€侊細0-寰呭紑濮嬶紝1-准备涓紝2-鍔犲伐涓紝3-鏆傚仠涓紝4-宸插畬鎴愶紝5-宸插彇娑堬紝6-开傚父涓?     */
    @TableField("current_status")
    private Integer currentStatus;

    /**
     * 褰撳墠杩涘害锛堢櫨鍒嗘瘮锛?     */
    @TableField("current_progress")
    private Integer currentProgress;

    /**
     * 璁″垝开€濮嬫椂闂?     */
    @TableField("planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 实际开€濮嬫椂闂?     */
    @TableField("actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 璁″垝瀹屾垚鏃堕棿
     */
    @TableField("planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际瀹屾垚鏃堕棿
     */
    @TableField("actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 浼樺厛绾э細1-浣庯紝2-涓紝3-楂?     */
    @TableField("priority")
    private Integer priority;

    /**
     * 棰勮宸ユ椂锛堝垎閽燂級
     */
    @TableField("estimated_duration")
    private Integer estimatedDuration;

    /**
     * 实际宸ユ椂锛堝垎閽燂級
     */
    @TableField("actual_duration")
    private Integer actualDuration;

    /**
     * 璐熻矗浜篒D锛屽閿叧鑱攗ser琛?     */
    @TableField("responsible_user_id")
    private Long responsibleUserId;

    /**
     * 操作浜哄憳ID锛屽閿叧鑱攗ser琛?     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 璁惧ID锛屽閿叧鑱攅quipment琛?     */
    @TableField("equipment_id")
    private Long equipmentId;

    /**
     * 娴佺▼鎻忚堪
     */
    @TableField("description")
    private String description;

    /**
     * 澶囨敞淇℃伅
     */
    @TableField("remark")
    private String remark;

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
     * 鏈€鍚庢殏鍋滄椂闂?     */
    @TableField("last_pause_time")
    private LocalDateTime lastPauseTime;

    /**
     * 设置娴佺▼ID
     * @param id 娴佺▼ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 设置褰撳墠状态     * @param currentStatus 褰撳墠状态     */
    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * 设置褰撳墠杩涘害
     * @param currentProgress 褰撳墠杩涘害
     */
    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    /**
     * 设置鏈€鍚庢殏鍋滄椂闂?     * @param lastPauseTime 鏈€鍚庢殏鍋滄椂闂?     */
    public void setLastPauseTime(LocalDateTime lastPauseTime) {
        this.lastPauseTime = lastPauseTime;
    }

    /**
     * 获取鏈€鍚庢殏鍋滄椂闂?     * @return 鏈€鍚庢殏鍋滄椂闂?     */
    public LocalDateTime getLastPauseTime() {
        return lastPauseTime;
    }

    public Integer getCurrentStatus() { return currentStatus; }
    public Integer getCurrentProgress() { return currentProgress; }
    public LocalDateTime getActualStartTime() { return actualStartTime; }
    public LocalDateTime getActualEndTime() { return actualEndTime; }
    public void setActualStartTime(LocalDateTime actualStartTime) { this.actualStartTime = actualStartTime; }
    public void setActualEndTime(LocalDateTime actualEndTime) { this.actualEndTime = actualEndTime; }
    public void setProcessName(String processName) { this.processName = processName; }
    public void setProcessCode(String processCode) { this.processCode = processCode; }
    public void setDescription(String description) { this.description = description; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setMoldId(Long moldId) { this.moldId = moldId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }
    public Long getId() { return id; }
    public LocalDateTime getPlannedEndTime() { return plannedEndTime; }
    public String getProcessCode() { return processCode; }
    public void setActualDuration(Integer actualDuration) { this.actualDuration = actualDuration; }
}
