package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 娴佺▼鐘舵€佸巻鍙插疄浣撶被
 * 瀵瑰簲数据搴撹〃锛歱rocess_status_history
 */
@Data
@TableName("process_status_history")
public class ProcessStatusHistory {

    /**
     * 鍘嗗彶记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 娴佺▼ID锛屽閿叧鑱攎old_process琛?
     */
    @TableField("process_id")
    private Long processId;

    /**
     * 鐘舵€侊細0-寰呭紑濮嬶紝1-准备涓紝2-鍔犲伐涓紝3-鏆傚仠涓紝4-宸插畬鎴愶紝5-宸插彇娑堬紝6-开傚父涓?
     */
    @TableField("status")
    private Integer status;

    /**
     * 鐘舵€佹弿杩?
     */
    @TableField("status_description")
    private String statusDescription;

    /**
     * 杩涘害锛堢櫨鍒嗘瘮锛?
     */
    @TableField("progress")
    private Integer progress;

    /**
     * 操作浜哄憳ID锛屽閿叧鑱攗ser琛?
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作鏃堕棿
     */
    @TableField("operation_time")
    private LocalDateTime operationTime;

    /**
     * 操作澶囨敞
     */
    @TableField("operation_remark")
    private String operationRemark;

    /**
     * 创建鏃堕棿
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 鍘熺姸鎬侊紙鐘舵€佽浆鎹㈠墠锛?
     */
    @TableField("from_status")
    private Integer fromStatus;

    /**
     * 鐩爣鐘舵€侊紙鐘舵€佽浆鎹㈠悗锛?
     */
    @TableField("to_status")
    private Integer toStatus;

    /**
     * 瑙﹀彂浜嬩欢
     */
    @TableField("event")
    private String event;

    /**
     * 鐘舵€佽浆鎹㈡弿杩?
     */
    @TableField("description")
    private String description;

    /**
     * 鐘舵€佽浆鎹㈡椂闂?
     */
    @TableField("transition_time")
    private LocalDateTime transitionTime;

    /**
     * 设置鍘嗗彶记录ID
     * @param id 鍘嗗彶记录ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 设置娴佺▼ID
     * @param processId 娴佺▼ID
     */
    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    /**
     * 设置状态
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 设置操作浜哄憳ID
     * @param operatorId 操作浜哄憳ID
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 设置操作澶囨敞
     * @param operationRemark 操作澶囨敞
     */
    public void setOperationRemark(String operationRemark) {
        this.operationRemark = operationRemark;
    }

    /**
     * 设置操作鏃堕棿
     * @param operationTime 操作鏃堕棿
     */
    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    /**
     * 设置鍘熺姸鎬?
     * @param fromStatus 鍘熺姸鎬?
     */
    public void setFromStatus(Integer fromStatus) {
        this.fromStatus = fromStatus;
    }

    /**
     * 设置鐩爣状态
     * @param toStatus 鐩爣状态
     */
    public void setToStatus(Integer toStatus) {
        this.toStatus = toStatus;
    }

    /**
     * 设置瑙﹀彂浜嬩欢
     * @param event 瑙﹀彂浜嬩欢
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 设置鐘舵€佽浆鎹㈡弿杩?
     * @param description 鐘舵€佽浆鎹㈡弿杩?
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 设置鐘舵€佽浆鎹㈡椂闂?
     * @param transitionTime 鐘舵€佽浆鎹㈡椂闂?
     */
    public void setTransitionTime(LocalDateTime transitionTime) {
        this.transitionTime = transitionTime;
    }

    /**
     * 获取鍘熺姸鎬?
     * @return 鍘熺姸鎬?
     */
    public Integer getFromStatus() {
        return fromStatus;
    }

    /**
     * 获取鐩爣状态
     * @return 鐩爣状态
     */
    public Integer getToStatus() {
        return toStatus;
    }

    /**
     * 获取瑙﹀彂浜嬩欢
     * @return 瑙﹀彂浜嬩欢
     */
    public String getEvent() {
        return event;
    }

    /**
     * 获取鐘舵€佽浆鎹㈡弿杩?
     * @return 鐘舵€佽浆鎹㈡弿杩?
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取鐘舵€佽浆鎹㈡椂闂?
     * @return 鐘舵€佽浆鎹㈡椂闂?
     */
    public LocalDateTime getTransitionTime() {
        return transitionTime;
    }

    /**
     * 获取鍘嗗彶记录ID
     * @return 鍘嗗彶记录ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 获取娴佺▼ID
     * @return 娴佺▼ID
     */
    public Long getProcessId() {
        return processId;
    }

    /**
     * 获取状态
     * @return 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 获取鐘舵€佹弿杩?
     * @return 鐘舵€佹弿杩?
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    /**
     * 获取杩涘害
     * @return 杩涘害
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * 获取操作浜哄憳ID
     * @return 操作浜哄憳ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 获取操作鏃堕棿
     * @return 操作鏃堕棿
     */
    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    /**
     * 获取操作澶囨敞
     * @return 操作澶囨敞
     */
    public String getOperationRemark() {
        return operationRemark;
    }

    /**
     * 获取创建鏃堕棿
     * @return 创建鏃堕棿
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
