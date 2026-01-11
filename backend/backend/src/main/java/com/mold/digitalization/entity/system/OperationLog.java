package com.mold.digitalization.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 记录用户的操作行为，用于审计和追踪
 */
@Data
@TableName("operation_log")
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 用户ID（表列：user_id）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 客户端IP地址（表列：ip）
     */
    @TableField("ip")
    private String userIp;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作模块
     */
    @TableField(exist = false)
    private String module;

    /**
     * 操作描述
     */
    private String operationDesc;

    /**
     * 操作内容（JSON格式）
     */
    private String operationContent;

    /**
     * 操作结果代码
     */
    @TableField(exist = false)
    private String resultCode;
    
    /**
     * 操作结果消息
     */
    @TableField(exist = false)
    private String resultMsg;
    
    /**
     * 是否敏感操作（标记需要特别关注的操作）
     * 数据库列：is_sensitive（TINYINT或BOOLEAN）
     */
    @TableField("is_sensitive")
    private Boolean isSensitive = false;
    
    /**
     * 敏感操作级别（normal/high/critical）
     * 数据库列：sensitive_level（VARCHAR）
     */
    @TableField("sensitive_level")
    private String sensitiveLevel = "normal";

    /**
     * 操作时间
     */
    @TableField("create_time")
    private LocalDateTime operationTime;

    /**
     * 操作结果（与表字段 result 对应）
     */
    @TableField("result")
    private String result;
    
    // 显式添加getter方法，避免Lombok注解编译问题
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getUserIp() {
        return userIp;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public String getModule() {
        return module;
    }
    
    public String getOperationContent() {
        return operationContent;
    }
    
    public String getOperationDesc() {
        return operationDesc;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public String getResultMsg() {
        return resultMsg;
    }
    
    public Boolean getIsSensitive() {
        return isSensitive;
    }
    
    public String getSensitiveLevel() {
        return sensitiveLevel;
    }
    
    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public String getResult() {
        return result;
    }

    // 显式添加setter方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public void setSensitiveLevel(String sensitiveLevel) {
        this.sensitiveLevel = sensitiveLevel;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
