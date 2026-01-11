package com.mold.digitalization.entity;

import lombok.Data;

/**
 * 妫€楠岀粺璁′俊鎭疄浣撶被
 * 鐢ㄤ簬返回妫€楠岀浉鍏崇殑缁熻淇℃伅
 */
@Data
public class InspectionStatistics {
    
    /**
     * 鎬绘楠屾暟閲?     */
    private Integer totalInspections;
    
    /**
     * 鍚堟牸妫€楠屾暟閲?     */
    private Integer passedCount;
    
    /**
     * 涓嶅悎鏍兼楠屾暟閲?     */
    private Integer failedCount;
    
    /**
     * 寰呮楠屾暟閲?     */
    private Integer pendingCount;
    
    /**
     * 鍚堟牸鐜囷紙鐧惧垎姣旓級
     */
    private Double passRate;
    
    /**
     * 涓嶅悎鏍肩巼锛堢櫨鍒嗘瘮锛?     */
    private Double failureRate;
    
    /**
     * 平均妫€楠屾椂闂达紙鍒嗛挓锛?     */
    private Double averageInspectionTime;
    
    /**
     * 鏋勯€犲嚱鏁?     */
    public InspectionStatistics() {
        this.totalInspections = 0;
        this.passedCount = 0;
        this.failedCount = 0;
        this.pendingCount = 0;
        this.passRate = 0.0;
        this.failureRate = 0.0;
        this.averageInspectionTime = 0.0;
    }
    
    /**
     * 璁＄畻鍚堟牸鐜?     */
    public void calculatePassRate() {
        if (totalInspections == 0) {
            this.passRate = 0.0;
        } else {
            this.passRate = (passedCount * 100.0) / totalInspections;
        }
    }
    
    /**
     * 璁＄畻涓嶅悎鏍肩巼
     */
    public void calculateFailureRate() {
        if (totalInspections == 0) {
            this.failureRate = 0.0;
        } else {
            this.failureRate = (failedCount * 100.0) / totalInspections;
        }
    }
    
    /**
     * 璁＄畻平均妫€楠屾椂闂?     */
    public void calculateAverageInspectionTime() {
        if (totalInspections == 0) {
            this.averageInspectionTime = 0.0;
        } else {
            // 杩欓噷闇€瑕佹牴鎹疄闄呬笟鍔￠€昏緫璁＄畻平均妫€楠屾椂闂?            // 鏆傛椂设置涓?0鍒嗛挓
            this.averageInspectionTime = 30.0;
        }
    }
}
