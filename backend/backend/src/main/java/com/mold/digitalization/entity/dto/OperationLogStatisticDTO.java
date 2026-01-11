package com.mold.digitalization.entity.dto;

import lombok.Data;

/**
 * 操作鏃ュ織缁熻数据DTO
 * 鐢ㄤ簬缁熻鍒嗘瀽结果灞曠ず
 */
@Data
public class OperationLogStatisticDTO {
    /**
     * 缁熻缁村害鍊硷紙濡傛搷浣滅被鍨嬨€佺敤鎴峰悕銆佹ā鍧楀悕绛夛級
     */
    private String name;
    
    /**
     * 操作绫诲瀷
     */
    private String operationType;
    
    /**
     * 用户名?
     */
    private String username;
    
    /**
     * 妯″潡鍚?
     */
    private String module;
    
    /**
     * 缁熻鏁伴噺
     */
    private Long count;
    
    /**
     * 鎬绘暟閲?
     */
    private Long totalCount;
    
    /**
     * 成功鏁伴噺
     */
    private Long successCount;
    
    /**
     * 失败鏁伴噺
     */
    private Long failCount;
    
    /**
     * 鏁忔劅操作鏁伴噺
     */
    private Long sensitiveCount;
    
    /**
     * 姣斾緥锛堜繚鐣欎袱浣嶅皬鏁帮級
     */
    private Double percentage;
    
    /**
     * 成功鐜?
     */
    private String successRate;
    
    /**
     * 鏁忔劅操作鐜?
     */
    private String sensitiveRate;
    
    /**
     * 璇︾粏淇℃伅锛堝操作绫诲瀷鍒嗗竷绛夛級
     */
    private java.util.Map<String, Long> detail;
    
    /**
     * 设置鎬绘暟閲?
     * @param totalCount 鎬绘暟閲?
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
    
    /**
     * 设置成功鏁伴噺
     * @param successCount 成功鏁伴噺
     */
    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }
    
    /**
     * 设置失败鏁伴噺
     * @param failCount 失败鏁伴噺
     */
    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }
    
    /**
     * 设置鏁忔劅操作鏁伴噺
     * @param sensitiveCount 鏁忔劅操作鏁伴噺
     */
    public void setSensitiveCount(Long sensitiveCount) {
        this.sensitiveCount = sensitiveCount;
    }
    
    /**
     * 设置成功鐜?
     * @param successRate 成功鐜?
     */
    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }
    
    /**
     * 设置鏁忔劅操作鐜?
     * @param sensitiveRate 鏁忔劅操作鐜?
     */
    public void setSensitiveRate(String sensitiveRate) {
        this.sensitiveRate = sensitiveRate;
    }
    
    /**
     * 设置璇︾粏淇℃伅
     * @param detail 璇︾粏淇℃伅
     */
    public void setDetail(java.util.Map<String, Long> detail) {
        this.detail = detail;
    }
}
