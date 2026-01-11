package com.mold.digitalization.service;

import com.mold.digitalization.entity.User;
import com.mold.digitalization.dto.LoginRequest;
import com.mold.digitalization.dto.LoginResponse;

/**
 * 认证服务接口
 * 鎻愪緵用户鐧诲綍銆佺櫥鍑恒€佸埛鏂颁护鐗岀瓑功能
 */
public interface AuthService {

    /**
     * 用户鐧诲綍
     * @param loginRequest 鐧诲綍璇锋眰鍙傛暟
     * @return 鐧诲綍鍝嶅簲锛屽寘鍚护鐗屼俊鎭?
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 用户鐧诲嚭
     * @param token 浠ょ墝
     */
    void logout(String token);

    /**
     * 鍒锋柊浠ょ墝
     * @param refreshToken 鍒锋柊浠ょ墝
     * @return 鏂扮殑访问浠ょ墝
     */
    String refreshToken(String refreshToken);

    /**
     * 验证浠ょ墝鏄惁鏈夋晥
     * @param token 浠ょ墝
     * @return 鏄惁鏈夋晥
     */
    boolean validateToken(String token);

    /**
     * 浠庝护鐗岃幏鍙栫敤鎴蜂俊鎭?
     * @param token 浠ょ墝
     * @return 用户淇℃伅
     */
    User getUserFromToken(String token);
    
    /**
     * 获取褰撳墠用户淇℃伅
     */
    Object getCurrentUserInfo();
    
    /**
     * 閲嶇疆用户密码
     * @param userId 用户ID
     * @param newPassword 鏂板瘑鐮?
     * @return 鏄惁成功
     */
    boolean resetPassword(Long userId, String newPassword);
    
    /**
     * 瑙ｉ攣用户璐﹀彿
     * @param userId 用户ID
     * @return 鏄惁成功
     */
    boolean unlockUser(Long userId);
    
    /**
     * 鍙戦€侀噸缃瘑鐮侀獙璇佺爜
     * @param username 用户名?     * @return 鏄惁鍙戦€佹垚鍔?     */
    boolean sendResetPasswordCode(String username);
    
    /**
     * 验证閲嶇疆密码验证鐮?
     * @param resetCode 閲嶇疆验证鐮?
     * @return 验证鏄惁成功
     */
    boolean validateResetPasswordCode(String resetCode);
    
    /**
     * 閫氳繃验证鐮侀噸缃瘑鐮?
     * @param resetCode 閲嶇疆验证鐮?
     * @param newPassword 鏂板瘑鐮?
     * @return 鏄惁成功
     */
    boolean resetPasswordByCode(String resetCode, String newPassword);
}
