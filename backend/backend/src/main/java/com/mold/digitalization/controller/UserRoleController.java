package com.mold.digitalization.controller;

import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.service.UserRoleService;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户瑙掕壊控制鍣?
 * 澶勭悊用户瑙掕壊鍏宠仈鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/user-roles")
@Api(tags = "用户角色管理")
public class UserRoleController extends BaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 鏍规嵁用户ID获取用户鐨勬墍鏈夎鑹?
     * @param userId 用户ID
     * @return 用户瑙掕壊鍒楄〃
     */
    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID获取角色列表")
    public ResponseEntity<List<UserRole>> getUserRolesByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userId);
        return success(userRoles);
    }
    
    /**
     * 鏍规嵁瑙掕壊ID获取鎷ユ湁璇ヨ鑹茬殑鎵€鏈夌敤鎴?
     * @param roleId 瑙掕壊ID
     * @return 用户瑙掕壊鍒楄〃
     */
    @GetMapping("/role/{roleId}")
    @ApiOperation("根据角色ID获取用户列表")
    public ResponseEntity<List<UserRole>> getUserRolesByRoleId(@ApiParam("瑙掕壊ID") @PathVariable Long roleId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByRoleId(roleId);
        return success(userRoles);
    }
    
    /**
     * 缁欑敤鎴峰垎閰嶈鑹?
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 操作结果
     */
    @PostMapping("/assign")
    @ApiOperation("为用户分配单个角色")
    @SensitiveOperation(level = "high", description = "Assign role to user - high sensitivity")
    public ResponseEntity<String> assignRoleToUser(@ApiParam("User ID") @NotNull @RequestParam Long userId, @ApiParam("Role ID") @NotNull @RequestParam Long roleId) {
        // 验证用户鏄惁瀛樺湪
        if (userService.getById(userId) == null) {
            logger.warn("User not found: userId={}", userId);
            return notFound("User not found");
        }
        
        // 验证瑙掕壊鏄惁瀛樺湪涓旂姸鎬佷负鍚敤
        if (roleService.getRoleById(roleId) == null) {
            logger.warn("Role not found: roleId={}", roleId);
            return notFound("Role not found");
        }
        
        try {
            boolean assigned = userRoleService.assignRoleToUser(userId, roleId);
            if (assigned) {
                logger.info("Role assignment successful: userId={}, roleId={}", userId, roleId);
                return success("Role assignment successful");
            } else {
                logger.error("Role assignment failed: userId={}, roleId={}", userId, roleId);
                return badRequest("Role assignment failed");
            }
        } catch (Exception e) {
            logger.error("Role assignment exception: userId={}, roleId={}", userId, roleId, e);
            return internalServerError("Role assignment failed due to server error");
        }
    }
    
    /**
     * 浠庣敤鎴风Щ闄よ鑹?
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 操作结果
     */
    @DeleteMapping("/remove")
    @ApiOperation("从用户移除指定角色")
    @SensitiveOperation(level = "high", description = "Remove role from user - high sensitivity")
    public ResponseEntity<String> removeRoleFromUser(@ApiParam("User ID") @NotNull @RequestParam Long userId, @ApiParam("Role ID") @NotNull @RequestParam Long roleId) {
        // 验证用户鏄惁瀛樺湪
        if (userService.getById(userId) == null) {
            logger.warn("User not found: userId={}", userId);
            return notFound("User not found");
        }
        
        try {
            boolean removed = userRoleService.removeRoleFromUser(userId, roleId);
            if (removed) {
                logger.info("Role removal successful: userId={}, roleId={}", userId, roleId);
                return success("Role removal successful");
            } else {
                logger.warn("User does not have the specified role: userId={}, roleId={}", userId, roleId);
                return badRequest("User does not have the specified role");
            }
        } catch (Exception e) {
            logger.error("Role removal exception: userId={}, roleId={}", userId, roleId, e);
            return internalServerError("Role removal failed due to server error");
        }
    }
    
    /**
     * 鎵归噺缁欑敤鎴峰垎閰嶅涓鑹?
     * @param userId 用户ID
     * @param roleIds 瑙掕壊ID鍒楄〃
     * @return 操作结果
     */
    @PostMapping("/batch-assign")
    @ApiOperation("为用户批量分配角色")
    @SensitiveOperation(level = "high", description = "Batch assign roles to user - high sensitivity")
    public ResponseEntity<String> batchAssignRolesToUser(@ApiParam("User ID") @NotNull @RequestParam Long userId, @ApiParam("Role ID list") @RequestBody List<Long> roleIds) {
        // 验证用户鏄惁瀛樺湪
        if (userService.getById(userId) == null) {
            logger.warn("User not found: userId={}", userId);
            return notFound("User not found");
        }
        
        // 验证瑙掕壊ID鍒楄〃鏄惁为空
        if (CollectionUtils.isEmpty(roleIds)) {
            logger.warn("Role ID list must not be empty: userId={}", userId);
            return badRequest("Role ID list must not be empty");
        }
        
        // 验证鎵€鏈夎鑹叉槸鍚﹀瓨鍦?
        for (Long roleId : roleIds) {
            if (roleService.getRoleById(roleId) == null) {
                logger.warn("Role not found: roleId={}", roleId);
                return badRequest("Role not found: " + roleId);
            }
        }
        
        try {
            boolean assigned = userRoleService.batchAssignRolesToUser(userId, roleIds);
            if (assigned) {
                logger.info("Batch role assignment successful: userId={}, roleCount={}", userId, roleIds.size());
                return success("Batch role assignment successful");
            } else {
                logger.error("Batch role assignment failed: userId={}", userId);
                return badRequest("Batch role assignment failed");
            }
        } catch (Exception e) {
            logger.error("Batch role assignment exception: userId={}", userId, e);
            return internalServerError("Batch role assignment failed due to server error");
        }
    }
}
