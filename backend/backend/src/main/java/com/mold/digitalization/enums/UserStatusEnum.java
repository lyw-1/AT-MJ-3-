package com.mold.digitalization.enums;

/**
 * 用户鐘舵€佹灇涓?
 */
public enum UserStatusEnum {
    
    /**
     * 绂佺敤状态
     */
    DISABLED(0, "绂佺敤"),
    
    /**
     * 鍚敤状态
     */
    ACTIVE(1, "鍚敤"),
    
    /**
     * 閿佸畾状态
     */
    LOCKED(2, "閿佸畾");
    
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
    UserStatusEnum(Integer code, String desc) {
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
    public static UserStatusEnum getByCode(Integer code) {
        for (UserStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
