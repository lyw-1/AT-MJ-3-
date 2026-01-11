package com.mold.digitalization.enums;

/**
 * 澶勭悊鐘舵€佹灇涓?
 */
public enum HandlingStatusEnum {
    
    /**
     * 鏈鐞?
     */
    UNHANDLED(0, "Unhandled"),
    
    /**
     * 澶勭悊涓?
     */
    HANDLING(1, "Handling"),
    
    /**
     * 宸插鐞?
     */
    HANDLED(2, "Handled");
    
    /**
     * 澶勭悊鐘舵€佷唬鐮?
     */
    private final Integer code;
    
    /**
     * 澶勭悊鐘舵€佹弿杩?
     */
    private final String desc;
    
    /**
     * 鏋勯€犲嚱鏁?
     */
    HandlingStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取澶勭悊鐘舵€佷唬鐮?
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取澶勭悊鐘舵€佹弿杩?
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 鏍规嵁浠ｇ爜获取鏋氫妇
     */
    public static HandlingStatusEnum getByCode(Integer code) {
        for (HandlingStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 鍒ゆ柇鏄惁涓哄彲澶勭悊状态
     */
    public static boolean isProcessableStatus(Integer code) {
        return code != null && (code == UNHANDLED.getCode() || code == HANDLING.getCode());
    }
}
