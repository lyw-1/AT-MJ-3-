package com.mold.digitalization.entity.dto;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 操作鏃ュ織数据浼犺緭瀵硅薄
 * 鐢ㄤ簬鍓嶅悗绔暟鎹氦浜?
 */
@Getter
public class OperationLogDTO {
    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 操作用户名?
     */
    private String username;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 瀹㈡埛绔疘P鍦板潃
     */
    private String ip;

    /**
     * 操作绫诲瀷
     */
    private String operationType;

    /**
     * 操作鎻忚堪
     */
    private String operationDesc;

    /**
     * 操作内容锛圝SON鏍煎紡锛?
     */
    private String operationContent;

    /**
     * 操作结果锛圫UCCESS/FAILURE锛?
     */
    private String result;
    
    /**
     * 鏄惁鏁忔劅操作锛堟爣璁伴渶瑕佺壒鍒叧娉ㄧ殑操作锛?
     */
    private Boolean isSensitive;

    /**
     * 操作鏃堕棿
     */
    private LocalDateTime createTime;
    
    // Setter方法
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }
    
    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}