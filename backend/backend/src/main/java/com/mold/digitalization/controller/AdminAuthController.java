package com.mold.digitalization.controller;

import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.dto.PasswordResetRequest;
import com.mold.digitalization.dto.UserLockRequest;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.service.UserLockService;
import com.mold.digitalization.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员樿璇佹帶鍒跺櫒
 */
@RestController
@RequestMapping("/api/v1/admin/auth")
@Api(tags = "管理员认证与控制")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DEV')")
public class AdminAuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserLockService userLockService;

    /**
     * 閲嶇疆用户密码
     * 
     * @param request 密码重置请求
     * @return 操作结果
     */
    @PostMapping("/reset-password")
    @ApiOperation("重置用户密码")
    @SensitiveOperation(level = "critical", description = "Admin action: reset user password")
    public ResponseEntity<?> resetPassword(@jakarta.validation.Valid @RequestBody PasswordResetRequest request) {
        // 新旧密码不匹配 → 参数错误
        if (request.getNewPassword() != null && request.getConfirmPassword() != null
                && !request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR.getCode().toString(), "新密码与确认密码不匹配");
        }

        // 验证用户是否存在
        if (userService.getById(request.getUserId()) == null) {
            // 统一抛出业务异常，由全局异常处理器映射为 HTTP 404 + 业务码 10001
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        // 执行密码重置
        try {
            boolean success = authService.resetPassword(request.getUserId(), request.getNewPassword());
            if (success) {
                return ResponseEntity.ok(ResponseDTO.success(null, "密码重置成功"));
            }
            // 业务执行失败 → 操作失败（400）
            throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            // 未知系统异常 → 500
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 閿佸畾/瑙ｉ攣用户璐﹀彿
     */
    @PostMapping("/lock-user")
    @ApiOperation("锁定/解锁用户账号")
    @SensitiveOperation(level = "critical", description = "Admin action: lock/unlock user account")
    public ResponseEntity<?> lockUser(@Validated @RequestBody UserLockRequest request) {
        // 验证用户是否存在
        if (userService.getById(request.getUserId()) == null) {
            // 统一抛出业务异常，由全局异常处理器映射为 HTTP 404 + 业务码 10001
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        boolean success = false;
        String message = "";

        // 鏍规嵁操作绫诲瀷执行閿佸畾鎴栬В閿?
        if ("lock".equals(request.getOperationType())) {
            // 浣跨敤鏂扮殑閿佸畾鏈哄埗閿佸畾用户
            success = userLockService.lockUser(request.getUserId(), request.getLockReason());
            message = success ? "用户锁定成功" : "用户锁定失败";
        } else if ("unlock".equals(request.getOperationType())) {
            // 浣跨敤鏂扮殑閿佸畾鏈哄埗瑙ｉ攣用户
            success = userLockService.unlockUser(request.getUserId());
            message = success ? "用户解锁成功" : "用户解锁失败";
        } else {
            // 不支持的操作类型 → 无效操作（400）
            throw new BusinessException(ErrorCodeEnum.INVALID_OPERATION.getCode().toString(), "不支持的操作类型；支持：lock, unlock");
        }

        if (success) {
            return ResponseEntity.ok(ResponseDTO.success(null, message));
        } else {
            // 业务执行失败 → 操作失败（400）
            throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED.getCode().toString(), message);
        }
    }
    
    /**
     * 瑙ｉ攣鐗瑰畾用户璐﹀彿锛堜笓鐢ㄦ帴鍙ｏ級
     */
    @PostMapping("/unlock-user/{userId}")
    @ApiOperation("解锁指定用户账号")
    @SensitiveOperation(level = "critical", description = "Admin action: unlock user account")
    public ResponseEntity<?> unlockUser(@PathVariable Long userId) {
        // 验证用户是否存在
        if (userService.getById(userId) == null) {
            // 统一抛出业务异常，由全局异常处理器映射为 HTTP 404 + 业务码 10001
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        boolean success = userLockService.unlockUser(userId);
        if (success) {
            return ResponseEntity.ok(ResponseDTO.success(null, "用户解锁成功"));
        } else {
            throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED.getCode().toString(), "用户解锁失败");
        }
    }

    /**
     * 对应测试期望：/api/v1/admin/auth/users/{userId}/unlock，返回统一JSON结构
     */
    @PostMapping("/users/{userId}/unlock")
    @ApiOperation("解锁用户（JSON响应）")
    @SensitiveOperation(level = "critical", description = "Admin action: unlock user account (JSON)")
    public ResponseEntity<?> unlockUserJson(@PathVariable Long userId) {
        // 使用测试中mock的方法命名，以确保一致：userService.getUserById
        var user = userService.getUserById(userId);
        if (user == null) {
            // 统一抛出业务异常，由全局异常处理器映射为 HTTP 404 + 业务码 10001
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        boolean success = userLockService.unlockUser(userId);
        if (success) {
            // 返回统一且可读的中文文案
            return ResponseEntity.ok(ResponseDTO.success(null, "用户解锁成功"));
        } else {
            throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED.getCode().toString(), "用户解锁失败");
        }
}
}
