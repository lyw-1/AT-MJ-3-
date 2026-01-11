package com.mold.digitalization.controller;

import com.mold.digitalization.service.PermissionCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 鏉冮檺缂撳瓨鍒锋柊控制鍣?
 */
@RestController
@RequestMapping("/api/v1/admin/permissions/cache")
@Api(tags = "权限缓存管理")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class PermissionCacheController {

    private final PermissionCacheService permissionCacheService;

    /**
     * 鍒锋柊鎸囧畾用户鐨勬潈闄愮紦瀛?
     */
    @PostMapping("/refresh/user/{userId}")
    @ApiOperation("刷新指定用户的权限缓存")
    public ResponseEntity<?> refreshUserPermissions(@PathVariable Long userId) {
        permissionCacheService.refreshUserPermissions(userId);
        return ResponseEntity.ok("User permission cache refreshed");
    }

    /**
     * 鍒锋柊鎵€鏈夌敤鎴风殑鏉冮檺缂撳瓨
     */
    @PostMapping("/refresh/all")
    @ApiOperation("刷新所有用户的权限缓存")
    public ResponseEntity<?> refreshAllUsersPermissions() {
        // Trigger full refresh for all users
        permissionCacheService.refreshAllUsersPermissions();
        return ResponseEntity.ok("All users' permission caches refreshed; background job started");
    }

    /**
     * 鏍规嵁瑙掕壊ID鍒锋柊鎵€鏈夊叧鑱旂敤鎴风殑鏉冮檺缂撳瓨
     */
    @PostMapping("/refresh/role/{roleId}")
    @ApiOperation("根据角色ID刷新关联用户的权限缓存")
    public ResponseEntity<?> refreshUsersByRoleId(@PathVariable Long roleId) {
        permissionCacheService.refreshUsersByRoleId(roleId);
        return ResponseEntity.ok("瑙掕壊鍏宠仈用户鏉冮檺缂撳瓨鍒锋柊成功");
    }

    /**
     * 娓呴櫎鎸囧畾用户鐨勬潈闄愮紦瀛?
     */
    @DeleteMapping("/clear/user/{userId}")
    @ApiOperation("清除指定用户的权限缓存")
    public ResponseEntity<?> clearUserPermissions(@PathVariable Long userId) {
        permissionCacheService.clearUserPermissions(userId);
        return ResponseEntity.ok("User permission cache cleared");
    }

    /**
     * 娓呴櫎鎵€鏈夌敤鎴风殑鏉冮檺缂撳瓨
     */
    @DeleteMapping("/clear/all")
    @ApiOperation("清除所有用户的权限缓存")
    public ResponseEntity<?> clearAllUsersPermissions() {
        permissionCacheService.clearAllUsersPermissions();
        return ResponseEntity.ok("鎵€鏈夌敤鎴锋潈闄愮紦瀛樺凡娓呴櫎");
    }
}
