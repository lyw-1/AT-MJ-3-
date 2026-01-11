package com.mold.digitalization.enums;

/**
 * 娴佺▼浼樺厛绾ф灇涓?
 */
public enum ProcessPriorityEnum {
    
    /**
     * Low priority
     */
    LOW(1, "Low"),

    /**
     * Medium priority
     */
    MEDIUM(2, "Medium"),

    /**
     * High priority
     */
    HIGH(3, "High");
    
    /**
     * 浼樺厛绾т唬鐮?
     */
    private final Integer code;
    
    /**
     * 浼樺厛绾ф弿杩?
     */
    private final String desc;
    
    /**
     * 鏋勯€犲嚱鏁?
     */
    ProcessPriorityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取浼樺厛绾т唬鐮?
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取浼樺厛绾ф弿杩?
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 鏍规嵁浠ｇ爜获取鏋氫妇
     */
    public static ProcessPriorityEnum getByCode(Integer code) {
        for (ProcessPriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return null;
    }
}
