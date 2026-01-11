package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿缁翠慨淇濆吇记录实体绫?
 * 瀵瑰簲数据搴撹〃锛歵ooling_maintenance
 * 记录妯″叿鐨勭淮淇拰淇濆吇淇℃伅
 */
@Data
@TableName("tooling_maintenance")
public class ToolingMaintenance {

    /**
     * 记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?
     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 妯″叿缂栧彿锛堝啑浣欏瓧娈碉紝鏂逛究查询锛?
     */
    @TableField("mold_code")
    private String moldCode;

    /**
     * 缁存姢绫诲瀷锛?-鏃ュ父淇濆吇锛?-棰勯槻鎬х淮鎶わ紝3-鏁呴殰缁翠慨锛?-澶т慨
     */
    @TableField("maintenance_type")
    private Integer maintenanceType;

    /**
     * 缁存姢开€濮嬫椂闂?
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 缁存姢缁撴潫鏃堕棿
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 缁存姢内容鎻忚堪
     */
    @TableField("content")
    private String content;

    /**
     * 缁存姢结果锛?-寰呭鐞嗭紝1-杩涜涓紝2-宸插畬鎴愶紝3-宸插彇娑?
     */
    @TableField("status")
    private Integer status;

    /**
     * 缁存姢璐熻矗浜篒D锛屽閿叧鑱攗ser琛?
     */
    @TableField("maintenance_user_id")
    private Long maintenanceUserId;

    /**
     * 缁存姢杞﹂棿ID锛屽閿叧鑱攚orkshop琛?
     */
    @TableField("workshop_id")
    private Long workshopId;

    /**
     * 璐圭敤记录
     */
    @TableField("cost")
    private Double cost;

    /**
     * 澶囨敞淇℃伅
     */
    @TableField("remark")
    private String remark;

    /**
     * 缁存姢结果鎻忚堪
     */
    @TableField("maintenance_result")
    private String maintenanceResult;
    
    /**
     * 涓嬫缁存姢鏃堕棿
     */
    @TableField("next_maintenance_time")
    private String nextMaintenanceTime;

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
     * 设置缁存姢结果
     * @param maintenanceResult 缁存姢结果鎻忚堪
     */
    public void setMaintenanceResult(String maintenanceResult) {
        this.maintenanceResult = maintenanceResult;
    }
    
    /**
     * 设置涓嬫缁存姢鏃堕棿
     * @param nextMaintenanceTime 涓嬫缁存姢鏃堕棿
     */
    public void setNextMaintenanceTime(String nextMaintenanceTime) {
        this.nextMaintenanceTime = nextMaintenanceTime;
    }

    public void setId(Long id) { this.id = id; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Double getCost() { return cost; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getId() { return id; }
}
