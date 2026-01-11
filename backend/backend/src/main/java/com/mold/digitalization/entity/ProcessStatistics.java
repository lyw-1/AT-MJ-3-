package com.mold.digitalization.entity;

import lombok.Data;

/**
 * 娴佺▼缁熻淇℃伅实体绫? * 鐢ㄤ簬返回娴佺▼鐩稿叧鐨勭粺璁′俊鎭? */
@Data
public class ProcessStatistics {
    
    /**
     * 鎬绘祦绋嬫暟閲?     */
    private Integer totalProcesses;
    
    /**
     * 寰呭紑濮嬫祦绋嬫暟閲?     */
    private Integer pendingCount;
    
    /**
     * 准备涓祦绋嬫暟閲?     */
    private Integer preparingCount;
    
    /**
     * 鍔犲伐涓祦绋嬫暟閲?     */
    private Integer processingCount;
    
    /**
     * 鏆傚仠涓祦绋嬫暟閲?     */
    private Integer pausedCount;
    
    /**
     * 宸插畬鎴愭祦绋嬫暟閲?     */
    private Integer completedCount;
    
    /**
     * 宸插彇娑堟祦绋嬫暟閲?     */
    private Integer cancelledCount;
    
    /**
     * 开傚父涓祦绋嬫暟閲?     */
    private Integer exceptionCount;
    
    /**
     * 平均瀹屾垚鏃堕棿锛堝垎閽燂級
     */
    private Double averageDuration;
    
    /**
     * 鎸夋椂瀹屾垚鐜囷紙鐧惧垎姣旓級
     */
    private Double onTimeCompletionRate;
    
    /**
     * 鏋勯€犲嚱鏁?     */
    public ProcessStatistics() {
        this.totalProcesses = 0;
        this.pendingCount = 0;
        this.preparingCount = 0;
        this.processingCount = 0;
        this.pausedCount = 0;
        this.completedCount = 0;
        this.cancelledCount = 0;
        this.exceptionCount = 0;
        this.averageDuration = 0.0;
        this.onTimeCompletionRate = 0.0;
    }
    
    /**
     * 璁＄畻鎸夋椂瀹屾垚鐜?     */
    public void calculateOnTimeCompletionRate() {
        if (completedCount == 0) {
            this.onTimeCompletionRate = 0.0;
        } else {
            // 杩欓噷闇€瑕佹牴鎹疄闄呬笟鍔￠€昏緫璁＄畻鎸夋椂瀹屾垚鐜?            // 鏆傛椂设置涓?00%
            this.onTimeCompletionRate = 100.0;
        }
    }
    
    /**
     * 璁＄畻平均瀹屾垚鏃堕棿
     */
    public void calculateAverageDuration() {
        if (completedCount == 0) {
            this.averageDuration = 0.0;
        } else {
            // 杩欓噷闇€瑕佹牴鎹疄闄呬笟鍔￠€昏緫璁＄畻平均瀹屾垚鏃堕棿
            // 鏆傛椂设置涓?0鍒嗛挓
            this.averageDuration = 60.0;
        }
    }
}
