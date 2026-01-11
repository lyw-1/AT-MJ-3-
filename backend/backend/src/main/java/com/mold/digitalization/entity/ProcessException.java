package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 娴佺▼开傚父记录实体绫? * 瀵瑰簲数据搴撹〃锛歱rocess_exception
 */
@Data
@TableName("process_exception")
public class ProcessException {

    /**
     * 开傚父记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 娴佺▼ID锛屽閿叧鑱攎old_process琛?     */
    @TableField("process_id")
    private Long processId;

    /**
     * 开傚父绫诲瀷锛?-璁惧鏁呴殰锛?-鏉愭枡闂锛?-宸ヨ壓闂锛?-浜哄憳操作锛?-璐ㄩ噺闂锛?-鍏朵粬
     */
    @TableField("exception_type")
    private Integer exceptionType;

    /**
     * 开傚父浠ｇ爜
     */
    @TableField("exception_code")
    private String exceptionCode;

    /**
     * 开傚父鎻忚堪
     */
    @TableField("exception_description")
    private String exceptionDescription;

    /**
     * 开傚父绾у埆锛?-杞诲井锛?-涓€鑸紝3-涓ラ噸锛?-绱ф€?     */
    @TableField("severity_level")
    private Integer severityLevel;

    /**
     * 开傚父鍙戠敓鏃堕棿
     */
    @TableField("occurrence_time")
    private LocalDateTime occurrenceTime;

    /**
     * 开傚父澶勭悊鐘舵€侊細0-鏈鐞嗭紝1-澶勭悊涓紝2-宸插鐞?     */
    @TableField("handling_status")
    private Integer handlingStatus;

    /**
     * 澶勭悊浜哄憳ID锛屽閿叧鑱攗ser琛?     */
    @TableField("handler_id")
    private Long handlerId;

    /**
     * 澶勭悊开€濮嬫椂闂?     */
    @TableField("handling_start_time")
    private LocalDateTime handlingStartTime;

    /**
     * 澶勭悊瀹屾垚鏃堕棿
     */
    @TableField("handling_end_time")
    private LocalDateTime handlingEndTime;

    /**
     * 澶勭悊结果鎻忚堪
     */
    @TableField("handling_result")
    private String handlingResult;

    /**
     * 澶勭悊鎺柦
     */
    @TableField("handling_measures")
    private String handlingMeasures;

    /**
     * 鏄惁褰卞搷鐢熶骇锛?-涓嶅奖鍝嶏紝1-褰卞搷
     */
    @TableField("affects_production")
    private Boolean affectsProduction;

    /**
     * 棰勮鎭㈠鏃堕棿
     */
    @TableField("estimated_recovery_time")
    private LocalDateTime estimatedRecoveryTime;

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
     * 开傚父鏃堕棿
     */
    @TableField("exception_time")
    private LocalDateTime exceptionTime;

    /**
     * 鏄惁宸茶В鍐?     */
    @TableField("resolved")
    private Boolean resolved;

    /**
     * 开傚父娑堟伅
     */
    @TableField("exception_message")
    private String exceptionMessage;

    /**
     * 操作浜哄憳ID
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 设置开傚父记录ID
     * @param id 开傚父记录ID
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
     * 设置开傚父绫诲瀷
     * @param exceptionType 开傚父绫诲瀷
     */
    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * 设置澶勭悊状态     * @param handlingStatus 澶勭悊状态     */
    public void setHandlingStatus(Integer handlingStatus) {
        this.handlingStatus = handlingStatus;
    }

    /**
     * 设置开傚父鏃堕棿
     * @param exceptionTime 开傚父鏃堕棿
     */
    public void setExceptionTime(LocalDateTime exceptionTime) {
        this.exceptionTime = exceptionTime;
    }

    /**
     * 设置鏄惁宸茶В鍐?     * @param resolved 鏄惁宸茶В鍐?     */
    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * 获取开傚父鏃堕棿
     * @return 开傚父鏃堕棿
     */
    public LocalDateTime getExceptionTime() {
        return exceptionTime;
    }

    /**
     * 获取鏄惁宸茶В鍐?     * @return 鏄惁宸茶В鍐?     */
    public Boolean getResolved() {
        return resolved;
    }

    /**
     * 设置开傚父娑堟伅
     * @param exceptionMessage 开傚父娑堟伅
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * 获取开傚父记录ID
     * @return 开傚父记录ID
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
     * 获取开傚父绫诲瀷
     * @return 开傚父绫诲瀷
     */
    public Integer getExceptionType() {
        return exceptionType;
    }

    /**
     * 获取开傚父浠ｇ爜
     * @return 开傚父浠ｇ爜
     */
    public String getExceptionCode() {
        return exceptionCode;
    }

    /**
     * 设置开傚父浠ｇ爜
     * @param exceptionCode 开傚父浠ｇ爜
     */
    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * 获取开傚父鎻忚堪
     * @return 开傚父鎻忚堪
     */
    public String getExceptionDescription() {
        return exceptionDescription;
    }

    /**
     * 设置开傚父鎻忚堪
     * @param exceptionDescription 开傚父鎻忚堪
     */
    public void setExceptionDescription(String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }

    /**
     * 获取开傚父绾у埆
     * @return 开傚父绾у埆
     */
    public Integer getSeverityLevel() {
        return severityLevel;
    }

    /**
     * 设置开傚父绾у埆
     * @param severityLevel 开傚父绾у埆
     */
    public void setSeverityLevel(Integer severityLevel) {
        this.severityLevel = severityLevel;
    }

    /**
     * 获取开傚父鍙戠敓鏃堕棿
     * @return 开傚父鍙戠敓鏃堕棿
     */
    public LocalDateTime getOccurrenceTime() {
        return occurrenceTime;
    }

    /**
     * 设置开傚父鍙戠敓鏃堕棿
     * @param occurrenceTime 开傚父鍙戠敓鏃堕棿
     */
    public void setOccurrenceTime(LocalDateTime occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    /**
     * 获取澶勭悊状态     * @return 澶勭悊状态     */
    public Integer getHandlingStatus() {
        return handlingStatus;
    }

    /**
     * 获取澶勭悊浜哄憳ID
     * @return 澶勭悊浜哄憳ID
     */
    public Long getHandlerId() {
        return handlerId;
    }

    /**
     * 设置澶勭悊浜哄憳ID
     * @param handlerId 澶勭悊浜哄憳ID
     */
    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    /**
     * 获取澶勭悊开€濮嬫椂闂?     * @return 澶勭悊开€濮嬫椂闂?     */
    public LocalDateTime getHandlingStartTime() {
        return handlingStartTime;
    }

    /**
     * 设置澶勭悊开€濮嬫椂闂?     * @param handlingStartTime 澶勭悊开€濮嬫椂闂?     */
    public void setHandlingStartTime(LocalDateTime handlingStartTime) {
        this.handlingStartTime = handlingStartTime;
    }

    /**
     * 获取澶勭悊瀹屾垚鏃堕棿
     * @return 澶勭悊瀹屾垚鏃堕棿
     */
    public LocalDateTime getHandlingEndTime() {
        return handlingEndTime;
    }

    /**
     * 设置澶勭悊瀹屾垚鏃堕棿
     * @param handlingEndTime 澶勭悊瀹屾垚鏃堕棿
     */
    public void setHandlingEndTime(LocalDateTime handlingEndTime) {
        this.handlingEndTime = handlingEndTime;
    }

    /**
     * 获取澶勭悊结果鎻忚堪
     * @return 澶勭悊结果鎻忚堪
     */
    public String getHandlingResult() {
        return handlingResult;
    }

    /**
     * 设置澶勭悊结果鎻忚堪
     * @param handlingResult 澶勭悊结果鎻忚堪
     */
    public void setHandlingResult(String handlingResult) {
        this.handlingResult = handlingResult;
    }

    /**
     * 获取澶勭悊鎺柦
     * @return 澶勭悊鎺柦
     */
    public String getHandlingMeasures() {
        return handlingMeasures;
    }

    /**
     * 设置澶勭悊鎺柦
     * @param handlingMeasures 澶勭悊鎺柦
     */
    public void setHandlingMeasures(String handlingMeasures) {
        this.handlingMeasures = handlingMeasures;
    }

    /**
     * 获取鏄惁褰卞搷鐢熶骇
     * @return 鏄惁褰卞搷鐢熶骇
     */
    public Boolean getAffectsProduction() {
        return affectsProduction;
    }

    /**
     * 设置鏄惁褰卞搷鐢熶骇
     * @param affectsProduction 鏄惁褰卞搷鐢熶骇
     */
    public void setAffectsProduction(Boolean affectsProduction) {
        this.affectsProduction = affectsProduction;
    }

    /**
     * 获取棰勮鎭㈠鏃堕棿
     * @return 棰勮鎭㈠鏃堕棿
     */
    public LocalDateTime getEstimatedRecoveryTime() {
        return estimatedRecoveryTime;
    }

    /**
     * 设置棰勮鎭㈠鏃堕棿
     * @param estimatedRecoveryTime 棰勮鎭㈠鏃堕棿
     */
    public void setEstimatedRecoveryTime(LocalDateTime estimatedRecoveryTime) {
        this.estimatedRecoveryTime = estimatedRecoveryTime;
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

    /**
     * 获取开傚父娑堟伅
     * @return 开傚父娑堟伅
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * 获取操作浜哄憳ID
     * @return 操作浜哄憳ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作浜哄憳ID
     * @param operatorId 操作浜哄憳ID
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
}