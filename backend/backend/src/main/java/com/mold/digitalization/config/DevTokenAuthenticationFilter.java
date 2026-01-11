package com.mold.digitalization.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Arrays;

    /**
     * 开发环境 Dev-Token 认证过滤器
     * 当开启开关且请求携带正确的 X-Dev-Token 时，将请求标记为已认证，便于开发环境对写操作进行受控联调。
     */
@Component
public class DevTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(DevTokenAuthenticationFilter.class);

    @Value("${app.security.dev.token.enabled:false}")
    private boolean devTokenEnabled;

    @Value("${app.security.dev.token.value:}")
    private String devTokenValue;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (devTokenEnabled && matchesDevTokenPath(request)) {
                String token = request.getHeader("X-Dev-Token");
                if (token == null || token.isEmpty()) token = request.getParameter("x-dev-token");
                if (token == null || token.isEmpty()) token = request.getParameter("dev-token");
                if (token != null && token.equals(devTokenValue)) {
                    log.info("DevToken：令牌匹配，请求已按开发环境进行受控认证");
                    // 使用一个简易的开发用户身份，授予多种角色权限
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            "dev-user", null, Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_DEV"),
                                new SimpleGrantedAuthority("ROLE_ADMIN"),
                                new SimpleGrantedAuthority("ROLE_MANAGER")
                            )
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.debug("DevToken：未提供或令牌不匹配，provided=\"{}\"", token);
                }
            }
        } catch (Exception ex) {
            log.error("DevToken 认证过滤器异常", ex);
        }

        filterChain.doFilter(request, response);
    }

    private boolean matchesDevTokenPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return (
                uri.startsWith("/api/process-state-machine/") ||
                uri.startsWith("/api/mold-process") ||
                uri.startsWith("/api/mold-initial-parameters") ||
                uri.startsWith("/api/consumables") ||
                uri.startsWith("/api/progress") ||
                uri.startsWith("/api/tuning-records") ||
                uri.startsWith("/api/mold-types") ||
                uri.startsWith("/api/mold-statuses") ||
                uri.startsWith("/api/molds") ||
                uri.startsWith("/api/mold-archive") ||
                uri.startsWith("/api/users") ||
                uri.startsWith("/api/roles") ||
                uri.startsWith("/api/user-roles") ||
                uri.startsWith("/api/departments") ||
                uri.startsWith("/api/equipment") ||
                uri.startsWith("/api/equipment-types") ||
                uri.startsWith("/api/products") ||
                uri.startsWith("/api/v1/admin/auth") ||
                uri.startsWith("/process")
        );
    }
}
