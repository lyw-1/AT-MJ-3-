package com.mold.digitalization.listener;

import com.mold.digitalization.event.RolePermissionChangeEvent;
import com.mold.digitalization.event.RoleStatusChangeEvent;
import com.mold.digitalization.event.UserRoleChangeEvent;
import com.mold.digitalization.service.PermissionCacheService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 鏉冮檺鍙樻洿浜嬩欢鐩戝惉鍣?
 * 鐩戝惉鏉冮檺鐩稿叧鐨勬墍鏈変簨浠跺苟鍒锋柊瀵瑰簲鐨勭紦瀛?
 */
@Component
@Slf4j
public class PermissionChangeListener {
    
    @Autowired
    private PermissionCacheService permissionCacheService;

    /**
     * 鐩戝惉用户瑙掕壊鍙樻洿浜嬩欢
     * @param event 用户瑙掕壊鍙樻洿浜嬩欢
     */
    @EventListener
    @Async("permissionChangeExecutor")
    public void handleUserRoleChangeEvent(UserRoleChangeEvent event) {
        log.info("Handle UserRoleChangeEvent: {}", event);
        try {
            // 鍒锋柊鐗瑰畾用户鐨勬潈闄愮紦瀛?
            permissionCacheService.refreshUserPermissions(event.getUserId());
            log.info("Successfully refreshed permissions for user [{}]", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to refresh permissions for user [{}]: {}", event.getUserId(), e.getMessage(), e);
        }
    }

    /**
     * 鐩戝惉瑙掕壊鏉冮檺鍙樻洿浜嬩欢
     * @param event 瑙掕壊鏉冮檺鍙樻洿浜嬩欢
     */
    @EventListener
    @Async("permissionChangeExecutor")
    public void handleRolePermissionChangeEvent(RolePermissionChangeEvent event) {
        log.info("Handle RolePermissionChangeEvent: {}", event);
        try {
            // 鍒锋柊鎵€鏈夋嫢鏈夎瑙掕壊鐨勭敤鎴锋潈闄愮紦瀛?
            permissionCacheService.refreshUsersByRoleId(event.getRoleId());
            log.info("Successfully refreshed users' permissions for role [{}]", event.getRoleId());
        } catch (Exception e) {
            log.error("Failed to refresh users' permissions for role [{}]: {}", event.getRoleId(), e.getMessage(), e);
        }
    }

    /**
     * 鐩戝惉瑙掕壊鐘舵€佸彉鏇翠簨浠?
     * @param event 瑙掕壊鐘舵€佸彉鏇翠簨浠?
     */
    @EventListener
    @Async("permissionChangeExecutor")
    public void handleRoleStatusChangeEvent(RoleStatusChangeEvent event) {
        log.info("Handle RoleStatusChangeEvent: {}", event);
        try {
            // 鍒锋柊鎵€鏈夋嫢鏈夎瑙掕壊鐨勭敤鎴锋潈闄愮紦瀛?
            permissionCacheService.refreshUsersByRoleId(event.getRoleId());
            log.info("Successfully refreshed permissions for users of role [{}] due to status change", event.getRoleId());
        } catch (Exception e) {
            log.error("Failed to refresh permissions for role [{}] status change: {}", event.getRoleId(), e.getMessage(), e);
        }
    }
}
