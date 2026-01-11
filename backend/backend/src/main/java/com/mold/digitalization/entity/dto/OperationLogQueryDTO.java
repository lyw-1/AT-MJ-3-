package com.mold.digitalization.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作鏃ュ織查询鏉′欢DTO
 * 鐢ㄤ簬鎺ユ敹鍓嶇查询鍙傛暟
 */
@Data
public class OperationLogQueryDTO {
    /**
     * 椤电爜
     */
    private Integer pageNum = 1;
    
    /**
     * 姣忛〉澶у皬
     */
    private Integer pageSize = 10;
    
    /**
     * 操作用户名?     */
    private String username;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 操作绫诲瀷
     */
    private String operationType;
    
    /**
     * 操作妯″潡
     */
    private String module;
    
    /**
     * 操作鎻忚堪锛堟ā绯婃煡璇級
     */
    private String operationDesc;
    
    /**
     * 操作结果
     */
    private String result;
    
    /**
     * 鏄惁鏁忔劅操作
     */
    private Boolean isSensitive;
    
    /**
     * 开€濮嬫椂闂?     */
    private LocalDateTime startTime;
    
    /**
     * 缁撴潫鏃堕棿
     */
    private LocalDateTime endTime;
    
    /**
     * IP鍦板潃锛堟ā绯婃煡璇級
     */
    private String ip;
    
    // 鏄惧紡添加getter鍜宻etter方法锛岄伩鍏峀ombok娉ㄨВ缂栬瘧闂
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public String getOperationDesc() {
        return operationDesc;
    }
    
    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public Boolean getIsSensitive() {
        return isSensitive;
    }
    
    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
}
