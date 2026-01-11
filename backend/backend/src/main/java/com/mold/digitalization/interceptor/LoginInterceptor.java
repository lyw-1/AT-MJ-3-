package com.mold.digitalization.interceptor;

import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.exception.UserLockedException;
import com.mold.digitalization.manager.UserLockManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 鐧诲綍鎷︽埅鍣?
 * 鐢ㄤ簬鍦ㄧ敤鎴风櫥褰曡繃绋嬩腑妫€鏌ラ攣瀹氱姸鎬併€佸鐞嗙櫥褰曞け璐ヨ鏁扮瓑
 */
@Component
public class LoginInterceptor {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserLockManager userLockManager;

    /**
     * 鐧诲綍鍓嶆鏌?
     * 鍦ㄧ敤鎴峰皾璇曠櫥褰曞墠妫€鏌ョ敤鎴风姸鎬?
     *
     * @param username 用户名?
     * @param request HTTP璇锋眰
     * @throws AuthenticationException 认证开傚父
     */
    public void beforeLogin(String username, HttpServletRequest request) throws AuthenticationException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            // 用户涓嶅瓨鍦紝浣嗕负浜嗗畨鍏ㄤ笉鐩存帴返回杩欎釜淇℃伅
            return;
        }

        // 妫€鏌ョ敤鎴锋槸鍚﹁閿佸畾
        if (userLockManager.checkUserLocked(username)) {
        log.warn("Login attempt while account locked - username={}, lockedUntil={}",
            username, user.getLockedUntil());
        throw new UserLockedException(
            "Account locked",
            user.getLockedUntil(),
            0, // remaining attempts unknown
            user.getLockReason(),
            user.getLockOperatorName()
        );
        }

        // 妫€鏌ョ敤鎴风姸鎬?
        if (user.getStatus() != 1) { // 鍋囪1琛ㄧず姝ｅ父状态
        log.warn("Login attempt for disabled user - username={}, status={}",
            username, user.getStatus());
        throw new DisabledException("Account disabled");
        }
    }

    /**
     * 澶勭悊鐧诲綍失败
     * 记录鐧诲綍失败骞舵鏌ユ槸鍚﹂渶瑕侀攣瀹氱敤鎴?
     *
     * @param username 用户名?
     * @param request HTTP璇锋眰
     * @param exception 认证开傚父
     */
    public void onLoginFailure(String username, HttpServletRequest request, AuthenticationException exception) {
        String loginIp = getClientIp(request);
        
        // 澶勭悊密码错误鐨勬儏鍐碉紙鏈€甯歌鐨勭櫥褰曞け璐ュ師鍥狅級
        if (exception instanceof BadCredentialsException || 
            exception instanceof UsernameNotFoundException) {
            try {
                userLockManager.handleLoginFailure(username, loginIp);
            } catch (UserLockedException e) {
                // 閲嶆柊鎶涘嚭閿佸畾开傚父锛岃涓婂眰澶勭悊
                throw e;
            } catch (Exception e) {
                log.error("Error handling login failure", e);
            }
        } else {
            log.warn("User login failed, username={}, error={}",
                    username, exception.getClass().getSimpleName());
        }
    }

    /**
     * 澶勭悊鐧诲綍成功
     * 閲嶇疆失败璁℃暟骞舵洿鏂扮櫥褰曚俊鎭?
     *
     * @param user 用户瀵硅薄
     * @param request HTTP璇锋眰
     */
    public void onLoginSuccess(User user, HttpServletRequest request) {
        String loginIp = getClientIp(request);
        try {
            userLockManager.handleLoginSuccess(user, loginIp);
            log.info("用户鐧诲綍成功锛歶sername={}, ip={}", user.getUsername(), loginIp);
        } catch (Exception e) {
            log.error("Error handling login success", e);
        }
    }

    /**
     * 获取瀹㈡埛绔湡瀹濱P鍦板潃
     * 鑰冭檻浠ｇ悊銆佽礋杞藉潎琛＄瓑鎯呭喌
     *
     * @param request HTTP璇锋眰
     * @return 瀹㈡埛绔疘P鍦板潃
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 澶氱骇浠ｇ悊鐨勬儏鍐典笅锛屽彇绗竴涓狪P
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}
