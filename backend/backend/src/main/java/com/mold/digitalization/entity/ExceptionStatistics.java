package com.mold.digitalization.entity;

import lombok.Data;

/**
 * 开傚父缁熻淇℃伅实体绫?
 * 鐢ㄤ簬返回开傚父鐩稿叧鐨勭粺璁′俊鎭?
 */
@Data
public class ExceptionStatistics {
    
    /**
     * 鎬诲紓甯告暟閲?
     */
    private Integer totalExceptions;
    
    /**
     * 璁惧鏁呴殰开傚父鏁伴噺
     */
    private Integer equipmentFailureCount;
    
    /**
     * 鏉愭枡闂开傚父鏁伴噺
     */
    private Integer materialIssueCount;
    
    /**
     * 宸ヨ壓闂开傚父鏁伴噺
     */
    private Integer processIssueCount;
    
    /**
     * 璐ㄩ噺闂开傚父鏁伴噺
     */
    private Integer qualityIssueCount;
    
    /**
     * 操作鍛橀敊璇紓甯告暟閲?
     */
    private Integer operatorErrorCount;
    
    /**
     * 鍏朵粬开傚父鏁伴噺
     */
    private Integer otherCount;
    
    /**
     * 杞诲井开傚父鏁伴噺
     */
    private Integer minorCount;
    
    /**
     * 涓€鑸紓甯告暟閲?
     */
    private Integer normalCount;
    
    /**
     * 涓ラ噸开傚父鏁伴噺
     */
    private Integer seriousCount;
    
    /**
     * 绱ф€ュ紓甯告暟閲?
     */
    private Integer urgentCount;
    
    /**
     * 鏈鐞嗗紓甯告暟閲?
     */
    private Integer unhandledCount;
    
    /**
     * 澶勭悊涓紓甯告暟閲?
     */
    private Integer processingCount;
    
    /**
     * 宸插鐞嗗紓甯告暟閲?
     */
    private Integer handledCount;
    
    /**
     * 平均澶勭悊鏃堕棿锛堝垎閽燂級
     */
    private Double averageResolutionTime;
    
    /**
     * 开傚父瑙ｅ喅鐜囷紙鐧惧垎姣旓級
     */
    private Double resolutionRate;
    
    /**
     * 鏋勯€犲嚱鏁?
     */
    public ExceptionStatistics() {
        this.totalExceptions = 0;
        this.equipmentFailureCount = 0;
        this.materialIssueCount = 0;
        this.processIssueCount = 0;
        this.qualityIssueCount = 0;
        this.operatorErrorCount = 0;
        this.otherCount = 0;
        this.minorCount = 0;
        this.normalCount = 0;
        this.seriousCount = 0;
        this.urgentCount = 0;
        this.unhandledCount = 0;
        this.processingCount = 0;
        this.handledCount = 0;
        this.averageResolutionTime = 0.0;
        this.resolutionRate = 0.0;
    }
    
    /**
     * 璁＄畻开傚父瑙ｅ喅鐜?
     */
    public void calculateResolutionRate() {
        if (totalExceptions == 0) {
            this.resolutionRate = 0.0;
        } else {
            this.resolutionRate = (handledCount * 100.0) / totalExceptions;
        }
    }
    
    /**
     * 璁＄畻平均澶勭悊鏃堕棿
     */
    public void calculateAverageResolutionTime() {
        if (handledCount == 0) {
            this.averageResolutionTime = 0.0;
        } else {
            // 杩欓噷闇€瑕佹牴鎹疄闄呬笟鍔￠€昏緫璁＄畻平均澶勭悊鏃堕棿
            // 鏆傛椂设置涓?20鍒嗛挓
            this.averageResolutionTime = 120.0;
        }
    }

    public void setTotalExceptions(int totalExceptions) { this.totalExceptions = totalExceptions; }
    public void setUnhandledCount(int unhandledCount) { this.unhandledCount = unhandledCount; }
    public void setHandledCount(int handledCount) { this.handledCount = handledCount; }
    public void setSeriousCount(int seriousCount) { this.seriousCount = seriousCount; }
    public void setNormalCount(int normalCount) { this.normalCount = normalCount; }
}
