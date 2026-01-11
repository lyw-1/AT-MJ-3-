package com.mold.digitalization.enums;

/**
 * 娴佺▼鐘舵€佹灇涓?*/
public enum ProcessStatusEnum {
    
    /**
     * 寰呭紑濮?
     */
    PENDING(0, "PENDING"),
    
    /**
     * 准备涓?
     */
    PREPARING(1, "PREPARING"),
    
    /**
     * 鍔犲伐涓?
     */
    PROCESSING(2, "PROCESSING"),
    
    /**
     * 鏆傚仠涓?
     */
    PAUSED(3, "PAUSED"),
    
    /**
     * 宸插畬鎴?
     */
    COMPLETED(4, "COMPLETED"),
    
    /**
     * 宸插彇娑?
     */
    CANCELLED(5, "CANCELLED"),
    
    /**
     * 开傚父涓?
     */
    EXCEPTION(6, "EXCEPTION");
    
    /**
     * 鐘舵€佺爜
     */
    private final Integer code;
    
    /**
     * 鐘舵€佹弿杩?
     */
    private final String desc;
    
    /**
     * 鏋勯€犲嚱鏁?
     */
    ProcessStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取鐘舵€佺爜
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取鐘舵€佹弿杩?
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 鏍规嵁鐘舵€佺爜获取鏋氫妇
     */
    public static ProcessStatusEnum getByCode(Integer code) {
        for (ProcessStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 鍒ゆ柇鏄惁涓鸿繘琛屼腑状态
     */
    public static boolean isProcessingStatus(Integer code) {
        return code != null && (code == PROCESSING.getCode() || code == PREPARING.getCode());
    }
    
    /**
     * 鍒ゆ柇鏄惁涓虹粨鏉熺姸鎬?
     */
    public static boolean isEndStatus(Integer code) {
        return code != null && (code == COMPLETED.getCode() || code == CANCELLED.getCode());
    }
    
    /**
     * 鍒ゆ柇鏄惁涓哄彲操作状态
     */
    public static boolean isOperableStatus(Integer code) {
        return code != null && (code == PENDING.getCode() || code == PREPARING.getCode() || 
                               code == PROCESSING.getCode() || code == PAUSED.getCode());
    }
}
