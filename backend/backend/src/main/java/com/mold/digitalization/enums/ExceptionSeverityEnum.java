package com.mold.digitalization.enums;

/**
 * 开傚父涓ラ噸绋嬪害鏋氫妇
 */
public enum ExceptionSeverityEnum {
    
    /**
     * 杞诲井
     */
    MINOR(1, "杞诲井"),
    
    /**
     * Normal severity
     */
    NORMAL(2, "Normal"),
    
    /**
     * 涓ラ噸
     */
    SEVERE(3, "涓ラ噸"),
    
    /**
     * Critical severity
     */
    CRITICAL(4, "Critical");
    
    /**
     * 涓ラ噸绋嬪害浠ｇ爜
     */
    private final Integer code;
    
    /**
     * 涓ラ噸绋嬪害鎻忚堪
     */
    private final String desc;
    
    /**
     * 鏋勯€犲嚱鏁?
     */
    ExceptionSeverityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取涓ラ噸绋嬪害浠ｇ爜
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取涓ラ噸绋嬪害鎻忚堪
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 鏍规嵁浠ｇ爜获取鏋氫妇
     */
    public static ExceptionSeverityEnum getByCode(Integer code) {
        for (ExceptionSeverityEnum severity : values()) {
            if (severity.getCode().equals(code)) {
                return severity;
            }
        }
        return null;
    }
    
    /**
     * 鍒ゆ柇鏄惁涓洪珮涓ラ噸绋嬪害
     */
    public static boolean isHighSeverity(Integer code) {
        return code != null && (code == SEVERE.getCode() || code == CRITICAL.getCode());
    }
}
