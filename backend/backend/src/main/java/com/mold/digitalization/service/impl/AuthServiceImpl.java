package com.mold.digitalization.service.impl;

import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.dto.LoginRequest;
import com.mold.digitalization.dto.LoginResponse;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.service.AuthService;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.RoleService;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.mold.digitalization.config.PasswordResetCacheConfig;
import com.mold.digitalization.config.CacheConstants;
import com.mold.digitalization.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import com.mold.digitalization.service.MailService;
import com.mold.digitalization.service.UserLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 认证服务实现绫?
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordResetCacheConfig passwordResetCacheConfig;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private UserLockService userLockService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long jwtRefreshExpiration;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String ipAddress = "unknown"; // IP地址暂时设为unknown，后续可以从请求中获取
        
        log.info("[LOGIN] 收到登录请求: username={}", username);
        
        // 1. 检查用户是否存在- 使用UserService.getUserByUsername方法避免查询不存在的字段
        User user = userService.getUserByUsername(username);
        if (user == null) {
            log.error("[LOGIN] 用户不存在: username={}", username);
            // 统一使用 BusinessException + ErrorCodeEnum
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }
        
        log.info("[LOGIN] 找到用户: id={}, username={}, status={}, password={}", user.getId(), user.getUsername(), user.getStatus(), user.getPassword());

        // 2. 检查用户锁定状态
        if (userLockService.isUserLocked(user.getId())) {
            com.mold.digitalization.dto.UserLockInfo lockInfo = userLockService.getUserLockInfo(user.getId());
            log.error("[LOGIN] 用户被锁定: username={}, lockedUntil={}", username, lockInfo.getLockedUntil());
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED.getCode(), String.format("账号被锁定，直到 %s", lockInfo.getLockedUntil()));
        }
        
        log.info("[LOGIN] 用户未被锁定: username={}", username);

        try {
            // 3. 手动验证密码，方便调试
            log.info("[LOGIN] 开始密码验证: username={}, rawPassword={}, storedPassword={}", 
                    username, password, user.getPassword());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            log.info("[LOGIN] 密码验证结果: username={}, passwordMatch={}, rawPassword={}, storedPassword={}", 
                    username, passwordMatch, password, user.getPassword());
            
            // 如果密码不匹配，直接抛出异常，避免后续认证
            if (!passwordMatch) {
                log.error("[LOGIN] 密码验证失败: username={}", username);
                throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR.getMessage());
            }
            
            // 4. 认证用户
            log.info("[LOGIN] 准备认证用户: username={}", username);
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            log.info("[LOGIN] 认证成功: username={}, authentication={}", username, authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. 登录成功，重置失败次数和锁定状态
            userLockService.resetLoginFailedCount(user.getId());
            
            // 6. 更新最后登录信息
            userMapper.updateLastLoginInfo(user.getId(), ipAddress, LocalDateTime.now());

            // 7. 获取用户角色
            List<Role> roles = roleService.getRolesByUserId(user.getId());
            log.info("[LOGIN] 获取到用户角色列表: username={}, rolesSize={}, roles={}", username, roles.size(), roles);
            
            // 检查角色列表是否为空
            if (roles == null || roles.isEmpty()) {
                log.info("[LOGIN] 用户没有角色: username={}", username);
            }
            
            String roleCodes = roles.stream()
                    .map(role -> {
                        String code = role.getRoleCode();
                        log.info("[LOGIN] 处理角色: roleId={}, roleName={}, roleCode={}, codeType={}", 
                                role.getId(), role.getRoleName(), code, code != null ? code.getClass().getName() : "null");
                        return code != null ? code : "";
                    })
                    .filter(roleCode -> roleCode != null && !roleCode.isEmpty() && !roleCode.equals("null"))
                    .collect(Collectors.joining(","));
            
            log.info("[LOGIN] 用户角色代码: username={}, roleCodes={}, roleCodesType={}", username, roleCodes, roleCodes != null ? roleCodes.getClass().getName() : "null");
            
            // 确保roleCodes不为null且不是字符串"null"，防止前端处理异常
            if (roleCodes == null) {
                roleCodes = "";
                log.info("[LOGIN] 角色代码修正为空字符串: username={}", username);
            } else if (roleCodes.equals("null")) {
                roleCodes = "";
                log.info("[LOGIN] 角色代码修正为空字符串: username={}", username);
            }

            // 8. 生成访问令牌
            String accessToken = tokenProvider.generateToken(user.getUsername(), user.getId(), roleCodes);
            log.info("[LOGIN] 生成访问令牌: username={}, token={}", username, accessToken);

            // 9. 生成刷新令牌
            String refreshToken = UUID.randomUUID().toString();
            log.info("[LOGIN] 生成刷新令牌: username={}, refreshToken={}", username, refreshToken);

            // 10. 将刷新令牌存储到Redis
            String refreshTokenKey = "refresh_token:" + refreshToken;
            redisService.set(refreshTokenKey, user.getId().toString(), jwtRefreshExpiration);
            log.info("[LOGIN] 存储刷新令牌到Redis: username={}, key={}", username, refreshTokenKey);

            // 11. 将访问令牌添加到Redis
            String tokenKey = "token:" + user.getId();
            redisService.set(tokenKey, accessToken, jwtExpiration);
            log.info("[LOGIN] 存储访问令牌到Redis: username={}, key={}", username, tokenKey);

            // 12. 构建响应
            LoginResponse response = new LoginResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtExpiration);
            response.setUsername(user.getUsername());
            response.setUserId(user.getId());
            // 确保roles不为null且不是字符串"null"，防止前端处理异常
            String finalRoles = roleCodes;
            if (finalRoles == null || finalRoles.equals("null")) {
                finalRoles = "";
                log.info("[LOGIN] 响应中角色代码修正为空字符串: username={}", username);
            }
            response.setRoles(finalRoles);
            log.info("[LOGIN] 登录成功，返回响应: username={}, roles={}", username, finalRoles);

            return response;
        } catch (BadCredentialsException e) {
            // 13. 登录失败：处理失败策略
            log.error("[LOGIN] 密码验证失败: username={}, error={}", username, e.getMessage(), e);
            com.mold.digitalization.dto.LoginFailureResult failureResult = userLockService.handleLoginFailure(user.getId());
            if (failureResult.isLocked()) {
                throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED.getCode(), failureResult.getErrorMessage());
            }
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR.getMessage());
        } catch (Exception e) {
            // 14. 其他登录失败处理
            log.error("[LOGIN] 登录失败：username={}, error={}", username, e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        // 接收纯 token（由控制器剥离 Bearer 前缀）；校验 token
        if (!tokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCodeEnum.TOKEN_INVALID.getCode(), ErrorCodeEnum.TOKEN_INVALID.getMessage());
        }

        // 获取用户ID
        Long userId = tokenProvider.getUserIdFromToken(token);

        // 从 Redis 移除访问令牌与权限缓存
        String tokenKey = "token:" + userId;
        redisService.delete(tokenKey);
        // 删除鏉冮檺缂撳瓨
        redisService.delete("user:permissions:" + userId);
    }

    @Override
    public String refreshToken(String refreshToken) {
        // 浠嶳edis涓獙璇佸埛鏂颁护鐗?
        String refreshTokenKey = "refresh_token:" + refreshToken;
        String userIdStr = redisService.get(refreshTokenKey);

        if (userIdStr == null) {
            throw new BusinessException(ErrorCodeEnum.TOKEN_INVALID.getCode(), "Invalid refresh token");
        }

        Long userId = Long.parseLong(userIdStr);
        User user = userService.getById(userId);

        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), "User does not exist");
        }

        // 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
        if (userLockService.isUserLocked(user.getId())) {
            com.mold.digitalization.dto.UserLockInfo lockInfo = userLockService.getUserLockInfo(user.getId());
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED.getCode(), String.format("账号被锁定，直到 %s", lockInfo.getLockedUntil()));
        }

        // 获取用户瑙掕壊
        List<Role> roles = roleService.getRolesByUserId(userId);
        String roleCodes = roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.joining(","));

        // 鐢熸垚鏂扮殑访问浠ょ墝
        String newAccessToken = tokenProvider.generateToken(user.getUsername(), userId, roleCodes);

        // 更新Redis涓殑访问浠ょ墝
        String tokenKey = "token:" + userId;
        redisService.set(tokenKey, newAccessToken, jwtExpiration);

        return newAccessToken;
    }

    @Override
    public boolean validateToken(String token) {
        // 接收纯 token；验证是否有效
        if (!tokenProvider.validateToken(token)) {
            return false;
        }

        // 妫€鏌ヤ护鐗屾槸鍚﹀湪榛戝悕鍗曚腑
        Long userId = tokenProvider.getUserIdFromToken(token);
        String tokenKey = "token:" + userId;
        String storedToken = redisService.get(tokenKey);

        return storedToken != null && storedToken.equals(token);
    }

    @Override
    public User getUserFromToken(String token) {
        // 接收纯 token；校验有效性
        if (!tokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCodeEnum.TOKEN_INVALID.getCode(), "Invalid token");
        }

        Long userId = tokenProvider.getUserIdFromToken(token);
        User user = userService.getById(userId);
        
        if (user != null) {
            // Check account locked status
            if (userLockService.isUserLocked(user.getId())) {
                com.mold.digitalization.dto.UserLockInfo lockInfo = userLockService.getUserLockInfo(user.getId());
                throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED.getCode(), String.format("账号被锁定，直到 %s", lockInfo.getLockedUntil()));
            }
        }
        
        return user;
    }

    @Override
    public Object getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            if (user != null) {
                // 鏋勫缓用户淇℃伅DTO锛屾帓闄ゆ晱鎰熶俊鎭?
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("realName", user.getRealName());
                // Email field removed as per instruction
                userInfo.put("phone", user.getPhone());
                userInfo.put("status", user.getStatus());
                
                // 获取用户瑙掕壊淇℃伅
                List<Role> roles = roleService.getRolesByUserId(user.getId());
                List<String> roleCodes = roles.stream()
                        .map(Role::getRoleCode)
                        .collect(Collectors.toList());
                userInfo.put("roles", roleCodes);
                
                return userInfo;
            }
        }
        // Failed to retrieve current user info (possibly unauthenticated or empty request)
        log.warn("Failed to get current user info: possible unauthenticated or empty request");
        return null;
    }
    

    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        User user = userService.getById(userId);
        if (user == null) {
            // 使用旧版业务异常，携带业务错误码 10001，以便全局异常处理器映射为 404
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND.getCode(), ErrorCodeEnum.USER_NOT_FOUND.getMessage());
        }
        
        // 鍔犲瘑鏂板瘑鐮?
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        
        // 淇濆瓨用户淇℃伅
        boolean updated = userService.updateById(user);
        
        // 瑙ｉ攣用户锛堝鏋滆閿佸畾锛?
        userLockService.unlockUser(userId);
        
        // 开哄埗用户閲嶆柊鐧诲綍
        // 注意：当 Redis 未启用或不可用时，删除登录令牌的操作不应阻断密码重置流程。
        // 因此这里增加容错处理，避免 Redis 连接失败导致 500。
        try {
            redisService.delete(CacheConstants.LOGIN_TOKEN_KEY + user.getUsername());
        } catch (Exception e) {
            log.warn("Redis 不可用，清除用户 {} 的登录令牌失败（已忽略，不影响密码重置）: {}",
                    user.getUsername(), e.toString());
        }
        
        log.info("用户 {} 鐨勫瘑鐮佸凡閲嶇疆", user.getUsername());
        return updated;
    }
    
    @Override
    public boolean sendResetPasswordCode(String username) {
        // 濡傛灉閫氳繃用户名嶆煡鎵?
        User user = userService.getUserByUsername(username);
        
        // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
        // 濡傛灉閫氳繃鎵嬫満鍙锋煡鎵撅紙getUserByEmail方法实际鎸夋墜鏈哄彿查询锛?
        // if (user == null) {
        //     user = userService.getUserByEmail(username);
        // }
        
        // 濡傛灉閫氳繃鎵嬫満鍙锋煡鎵撅紙澶囩敤鏂瑰紡锛?
        if (user == null) {
            user = userService.getUserByPhone(username);
        }
        
        if (user == null) {
            log.warn("User not found: {}", username);
            return false;
        }
        
        // 妫€鏌ュ彂閫侀鐜囬檺鍒?
        if (!checkSendFrequency(user.getId())) {
            log.warn("User {} send frequency limit reached", user.getUsername());
            return false;
        }
        
        // 妫€鏌ュ彂閫佹鏁伴檺鍒?
            if (!checkSendCount(user.getId())) {
            log.warn("User {} reached max daily reset code attempts", user.getUsername());
            return false;
        }
        
        // 鐢熸垚6浣嶆暟鐨勯噸缃獙璇佺爜
        String resetCode = generateResetCode();
        
        // 获取閲嶇疆验证鐮佺紦瀛橀敭
        String resetCodeKey = passwordResetCacheConfig.getResetCodeKey(user.getId());
        
        // 淇濆瓨鍒癛edis锛岃缃湁鏁堟湡
        redisService.set(resetCodeKey, resetCode, passwordResetCacheConfig.getResetCodeExpireTime());
        
        // 更新鍙戦€佽鏁?
        updateSendCount(user.getId());
        
        // 更新鏈€鍚庡彂閫佹椂闂?
        updateLastSendTime(user.getId());
        
        // 鍙戦€侀獙璇佺爜鍒扮敤鎴锋墜鏈猴紙鏇夸唬閭鍙戦€侊級
        boolean sendSuccess = false;
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                try {
                // 验证鐮佹湁鏁堟湡15鍒嗛挓
                int expiresInMinutes = 15;
                // 杩欓噷应该璋冪敤鐭俊服务鍙戦€侀獙璇佺爜锛岃€屼笉鏄偖浠舵湇鍔?
                // sendSuccess = smsService.sendPasswordResetSms(user.getPhone(), resetCode, expiresInMinutes);
                log.info("Generated reset code for user {}: {}", user.getUsername(), resetCode);
                log.info("Reset code expires in {} minutes", expiresInMinutes);
                sendSuccess = true; // 涓存椂设置涓烘垚鍔燂紝实际应该鏍规嵁鐭俊鍙戦€佺粨鏋滆缃?
            } catch (Exception e) {
                log.error("Failed to send reset code to user {}", user.getUsername(), e);
                // 鐭俊鍙戦€佸け璐ヤ笉褰卞搷验证鐮佺殑鐢熸垚鍜屽瓨鍌紝鍏佽用户閫氳繃鍏朵粬鏂瑰紡获取验证鐮?
            }
        } else {
            log.warn("User {} has no phone number configured, cannot send reset code via SMS", user.getUsername());
        }
        
    // Record code generation event
    log.info("Generated reset code for user {}: {}", user.getUsername(), resetCode);
        
        return true;
    }
    
    /**
     * 妫€鏌ュ彂閫侀鐜囬檺鍒?
     * @param userId 用户ID
     * @return 鏄惁可以鍙戦€?
     */
    private boolean checkSendFrequency(Long userId) {
        String lastSendTimeKey = passwordResetCacheConfig.getLastSendTimeKey(userId);
        String lastSendTimeStr = redisService.get(lastSendTimeKey);
        
        if (lastSendTimeStr != null) {
            long lastSendTime = Long.parseLong(lastSendTimeStr);
            long currentTime = System.currentTimeMillis() / 1000;
            
            if (currentTime - lastSendTime < passwordResetCacheConfig.getMinSendInterval()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 妫€鏌ュ彂閫佹鏁伴檺鍒?
     * @param userId 用户ID
     * @return 鏄惁可以鍙戦€?
     */
    private boolean checkSendCount(Long userId) {
        String sendCountKey = passwordResetCacheConfig.getSendCountKey(userId);
        String countStr = redisService.get(sendCountKey);
        
        if (countStr != null) {
            int count = Integer.parseInt(countStr);
            if (count >= passwordResetCacheConfig.getMaxSendTimesPerDay()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 更新鍙戦€佽鏁?
     * @param userId 用户ID
     */
    private void updateSendCount(Long userId) {
        String sendCountKey = passwordResetCacheConfig.getSendCountKey(userId);
        String countStr = redisService.get(sendCountKey);
        
        int count = countStr != null ? Integer.parseInt(countStr) + 1 : 1;
        redisService.set(sendCountKey, String.valueOf(count), getSecondsUntilMidnight());
    }
    
    /**
     * 更新鏈€鍚庡彂閫佹椂闂?
     * @param userId 用户ID
     */
    private void updateLastSendTime(Long userId) {
        String lastSendTimeKey = passwordResetCacheConfig.getLastSendTimeKey(userId);
        long currentTime = System.currentTimeMillis() / 1000;
        redisService.set(lastSendTimeKey, String.valueOf(currentTime), 3600); // 1灏忔椂杩囨湡
    }
    
    /**
     * 获取璺濈鍗堝鐨勭鏁?
     * @return 绉掓暟
     */
    private int getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return (int) now.until(midnight, java.time.temporal.ChronoUnit.SECONDS);
    }
    
    @Override
    public boolean validateResetPasswordCode(String resetCode) {
        // 鎼滅储鎵€鏈夐噸缃爜閿?
        Set<String> keys = redisService.keys(PasswordResetCacheConfig.RESET_CODE_KEY_PREFIX + "*");
        
        for (String key : keys) {
            String storedCode = redisService.get(key);
            if (resetCode.equals(storedCode)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPasswordByCode(String resetCode, String password) {
        // 鎼滅储鎵€鏈夐噸缃爜閿?
        Set<String> keys = redisService.keys(PasswordResetCacheConfig.RESET_CODE_KEY_PREFIX + "*");
        
        for (String key : keys) {
            String storedCode = redisService.get(key);
            if (resetCode.equals(storedCode)) {
                // 鎻愬彇用户ID
                String userIdStr = key.replace(PasswordResetCacheConfig.RESET_CODE_KEY_PREFIX, "");
                Long userId = Long.parseLong(userIdStr);
                
                // 璋冪敤鐜版湁鐨勫瘑鐮侀噸缃柟娉?
                boolean success = resetPassword(userId, password);
                
                if (success) {
                    // Password reset succeeded, remove the stored code
                    redisService.delete(key);
                    log.info("Successfully reset password for user {}", userId);
                }
                
                return success;
            }
        }
        
        log.warn("鏃犳晥鐨勫瘑鐮侀噸缃獙璇佺爜: {}", resetCode);
        return false;
    }
    
    /**
     * 鐢熸垚6浣嶆暟鐨勯噸缃獙璇佺爜
     */
    private String generateResetCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockUser(Long userId) {
        boolean result = userLockService.unlockUser(userId);
        if (result) {
            User user = userService.getById(userId);
            if (user != null) {
                log.info("User {} has been unlocked by admin", user.getUsername());
            }
        }
        return result;
    }
}
