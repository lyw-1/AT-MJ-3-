package com.mold.digitalization.service;

/**
 * 鏉冮檺缂撳瓨服务接口
 */
public interface PermissionCacheService {

    /**
     * 鍒锋柊鎸囧畾用户鐨勬潈闄愮紦瀛?     * @param userId 用户ID
     */
    void refreshUserPermissions(Long userId);

    /**
     * 鍒锋柊鎵€鏈夌敤鎴风殑鏉冮檺缂撳瓨
     */
    void refreshAllUsersPermissions();

    /**
     * 鏍规嵁瑙掕壊ID鍒锋柊鎵€鏈夊叧鑱旂敤鎴风殑鏉冮檺缂撳瓨
     * @param roleId 瑙掕壊ID
     */
    void refreshUsersByRoleId(Long roleId);

    /**
     * 娓呴櫎鎸囧畾用户鐨勬潈闄愮紦瀛?     * @param userId 用户ID
     */
    void clearUserPermissions(Long userId);

    /**
     * 娓呴櫎鎵€鏈夌敤鎴风殑鏉冮檺缂撳瓨
     */
    void clearAllUsersPermissions();
}