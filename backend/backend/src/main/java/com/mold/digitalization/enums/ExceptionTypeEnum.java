package com.mold.digitalization.enums;

/**
 * 开傚父绫诲瀷鏋氫妇
 */
public enum ExceptionTypeEnum {
    
    /**
     * 璁惧鏁呴殰
     */
    EQUIPMENT_FAILURE(1, "璁惧鏁呴殰"),
    
    /**
     * 鏉愭枡闂
     */
    MATERIAL_ISSUE(2, "鏉愭枡闂"),
    
    /**
     * 宸ヨ壓闂
     */
    PROCESS_ISSUE(3, "宸ヨ壓闂"),
    
    /**
     * 浜哄憳操作
     */
    OPERATOR_ERROR(4, "浜哄憳操作"),
    
    /**
     * 璐ㄩ噺闂
     */
    QUALITY_ISSUE(5, "璐ㄩ噺闂"),
    
    /**
     * 鍏朵粬开傚父
     */
    OTHER(6, "鍏朵粬");
    
    /**
     * 开傚父绫诲瀷浠ｇ爜
     */
    private final Integer code;
    
    /**
     * 开傚父绫诲瀷鎻忚堪
     */
    private final String desc;
    
    /**
     * 鏋勯€犲嚱鏁?     */
    ExceptionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取开傚父绫诲瀷浠ｇ爜
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 获取开傚父绫诲瀷鎻忚堪
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 鏍规嵁浠ｇ爜获取鏋氫妇
     */
    public static ExceptionTypeEnum getByCode(Integer code) {
        for (ExceptionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}