package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.service.PermissionCacheService;
import com.mold.digitalization.service.PermissionService;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.UserRoleService;
import com.mold.digitalization.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * 鏉冮檺缂撳瓨服务实现绫?
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionCacheServiceImpl implements PermissionCacheService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionCacheServiceImpl.class);

    private final RedisService redisService;
    private final UserService userService;
    private final PermissionService permissionService;
    private final UserRoleService userRoleService;

    private static final String USER_PERMISSIONS_KEY_PREFIX = "user:permissions:";

    @Override
    public void refreshUserPermissions(Long userId) {
        try {
            // 获取用户
            User user = userService.getUserById(userId);
            if (user == null) {
                logger.warn("User ID {} not found, skip refreshing permissions", userId);
                return;
            }

            // Clear existing permissions cache for this user
            clearUserPermissions(userId);

            // 获取用户鏉冮檺鍒楄〃
            List<Permission> permissions = permissionService.getPermissionsByUserId(userId);
            if (!CollectionUtils.isEmpty(permissions)) {
                // 灏嗘潈闄愪繚瀛樺埌缂撳瓨
                String key = USER_PERMISSIONS_KEY_PREFIX + userId;
                List<String> permissionCodes = permissions.stream()
                        .map(Permission::getCode)
                        .toList();
                
                redisService.sAdd(key, permissionCodes.toArray(new String[0]));
                logger.info("Permissions for user {} refreshed, count: {}", user.getUsername(), permissionCodes.size());
            } else {
                logger.info("User {} has no permissions, cache cleared", user.getUsername());
            }
        } catch (Exception e) {
            logger.error("Failed to refresh permissions for user {}", userId, e);
        }
    }

    @Override
    @Async("permissionChangeExecutor")
    public void refreshAllUsersPermissions() {
        try {
            // Get all users
            List<User> users = userService.list();
            if (!CollectionUtils.isEmpty(users)) {
                int refreshedCount = 0;
                for (User user : users) {
                    refreshUserPermissions(user.getId());
                    refreshedCount++;
                }
                logger.info("All users permissions refreshed, total refreshed: {}", refreshedCount);
            }
        } catch (Exception e) {
            logger.error("Failed to refresh all users permissions", e);
        }
    }

    @Override
    public void clearUserPermissions(Long userId) {
        String key = USER_PERMISSIONS_KEY_PREFIX + userId;
        redisService.delete(key);
        logger.debug("用户 {} 鐨勬潈闄愮紦瀛樺凡娓呴櫎", userId);
    }

    @Override
    public void clearAllUsersPermissions() {
        try {
            // Use pattern to find keys for all users
            Set<String> keys = redisService.keys(USER_PERMISSIONS_KEY_PREFIX + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                // 鎵归噺删除鎵€鏈夊尮閰嶇殑key
                for (String key : keys) {
                    redisService.delete(key);
                }
                logger.info("Cleared permissions cache for all users, keys removed: {}", keys.size());
            }
        } catch (Exception e) {
            logger.error("Failed to clear all users permissions cache", e);
        }
    }

    @Override
    public void refreshUsersByRoleId(Long roleId) {
        try {
            // Get userRoles associated with the role
            List<UserRole> userRoles = userRoleService.getUserRolesByRoleId(roleId);
            if (!CollectionUtils.isEmpty(userRoles)) {
                int refreshedCount = 0;
                for (UserRole userRole : userRoles) {
                    // Refresh permissions for each user
                    refreshUserPermissions(userRole.getUserId());
                    refreshedCount++;
                }
                logger.info("Refreshed permissions for users related to role [{}], total refreshed: {}", roleId, refreshedCount);
            } else {
                logger.info("Role [{}] has no related users, nothing to refresh", roleId);
            }
        } catch (Exception e) {
            logger.error("Failed to refresh permissions for users of role [{}]", roleId, e);
        }
    }
}
