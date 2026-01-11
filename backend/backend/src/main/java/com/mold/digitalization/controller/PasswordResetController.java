package com.mold.digitalization.controller;

import com.mold.process.common.Result;
import com.mold.process.common.ResultCode;
import com.mold.digitalization.dto.ForgotPasswordRequest;
import com.mold.digitalization.dto.ResetPasswordByCodeRequest;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 密码閲嶇疆控制鍣?
 * 澶勭悊密码閲嶇疆鐩稿叧鐨凙PI璇锋眰
 */
@RestController
@RequestMapping("/v1/api/auth")
@Api(tags = "密码重置")
public class PasswordResetController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;

    /**
     * 蹇樿密码锛屽彂閫侀噸缃獙璇佺爜
     * @param request 蹇樿密码璇锋眰
     * @return 操作结果
     */
    @PostMapping("/forgot-password")
    @ApiOperation(value = "申请重置验证码", notes = "向用户邮箱或手机发送重置验证码")
    public Result<String> forgotPassword(@Validated @RequestBody ForgotPasswordRequest request) {
        // 鍙戦€侀噸缃獙璇佺爜
        boolean sent = authService.sendResetPasswordCode(request.getUsername());
        if (sent) {
            return Result.success("Reset code sent");
        } else {
            return Result.failed("Failed to send reset code");
        }
    }

    /**
     * 验证閲嶇疆密码验证鐮?
     * @param code 閲嶇疆验证鐮?
     * @return 操作结果
     */
    @GetMapping("/validate-reset-code/{code}")
    @ApiOperation(value = "验证重置验证码")
    public Result<Boolean> validateResetCode(@PathVariable String code) {
        boolean valid = authService.validateResetPasswordCode(code);
        return Result.success(valid);
    }

    /**
     * 閫氳繃验证鐮侀噸缃瘑鐮?
     * @param request 閲嶇疆密码璇锋眰
     * @return 操作结果
     */
    @PostMapping("/reset-password-by-code")
    @ApiOperation(value = "通过验证码重置密码", notes = "使用校验码重置用户密码")
    public Result<String> resetPasswordByCode(@Validated @RequestBody ResetPasswordByCodeRequest request) {
        // 验证涓ゆ密码鏄惁涓€鑷?
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.failed("The two entered passwords do not match");
        }

        boolean success = authService.resetPasswordByCode(request.getResetCode(), request.getNewPassword());
        if (success) {
            return Result.success("Password reset successfully");
        } else {
            return Result.failed("Invalid or expired reset code");
        }
    }
}
