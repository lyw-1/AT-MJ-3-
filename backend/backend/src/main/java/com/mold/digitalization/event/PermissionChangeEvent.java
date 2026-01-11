package com.mold.digitalization.event;

import org.springframework.context.ApplicationEvent;

/**
 * 鏉冮檺鍙樻洿鍩虹被浜嬩欢
 * 鎵€鏈夋潈闄愮浉鍏崇殑浜嬩欢閮藉簲璇ョ户鎵挎鍩虹被
 */
public abstract class PermissionChangeEvent extends ApplicationEvent {

    /**
     * 创建涓€涓柊鐨勬潈闄愬彉鏇翠簨浠?     * @param source 浜嬩欢婧?     */
    public PermissionChangeEvent(Object source) {
        super(source);
    }
}
