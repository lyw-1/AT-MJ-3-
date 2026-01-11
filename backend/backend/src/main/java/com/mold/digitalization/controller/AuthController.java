package com.mold.digitalization.controller;

import com.mold.digitalization.dto.LoginRequest;
import com.mold.digitalization.dto.LoginResponse;
import com.mold.digitalization.dto.RefreshTokenRequest;
import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 认证控制鍣? * 澶勭悊鐧诲綍銆佺櫥鍑哄拰浠ょ墝鍒锋柊
 */
@RestController
@RequestMapping({"/v1/api/auth", "/auth", "/api/auth"})
@Api(tags = "认证管理")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户鐧诲綍
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseDTO<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseDTO.success(loginResponse);
    }

    /**
     * 用户鐧诲嚭
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public ResponseDTO<String> logout(HttpServletRequest request) {
        // 从请求头中获取 token 并做格式校验
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.trim().isEmpty()) {
            // 未认证：缺少 Authorization 令牌
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未认证：缺少 Authorization 令牌");
        }
        if (!authHeader.startsWith("Bearer ")) {
            // 未认证：令牌格式错误
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "未认证：令牌格式错误");
        }
        String token = authHeader.substring(7);
        authService.logout(token);
        // 统一中文编码，返回正确的中文文案
        return ResponseDTO.success(null, "登出成功");
    }

    /**
     * 鍒锋柊访问浠ょ墝
     */
    @PostMapping("/refresh-token")
    @ApiOperation("刷新访问令牌")
    public ResponseDTO<String> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            // 参数错误：缺少刷新令牌
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR.getCode(), "缺少刷新令牌");
        }
        String newAccessToken = authService.refreshToken(refreshToken.trim());
        return ResponseDTO.success(newAccessToken);
    }

    /**
     * 获取褰撳墠用户淇℃伅
     */
    @GetMapping("/me")
    @ApiOperation("获取当前用户信息")
    public ResponseDTO<Object> getCurrentUser() {
        Object userInfo = authService.getCurrentUserInfo();
        return ResponseDTO.success(userInfo);
    }
}
