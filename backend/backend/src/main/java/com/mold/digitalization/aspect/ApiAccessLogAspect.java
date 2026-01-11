package com.mold.digitalization.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * API访问日志切面
 * 记录所有API访问的详细信息，包括请求路径、方法、参数、响应时间等
 */
@Aspect
@Component
@Order(2) // 优先级低于操作日志切面
@Slf4j
public class ApiAccessLogAspect {

    /**
     * 定义切入点，拦截所有Controller层方法
     */
    @Pointcut("execution(* com.mold.digitalization.controller..*.*(..))")
    public void apiAccessLogPointCut() {
    }

    /**
     * 环绕通知，记录API访问日志
     */
    @Around("apiAccessLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        
        // 格式化时间
        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        
        // 请求信息
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String remoteAddr = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        // 请求参数（过滤敏感信息）
        Object[] args = point.getArgs();
        String requestParams = filterSensitiveParams(args);
        
        // 执行方法
        Object result = null;
        String responseStatus = "SUCCESS";
        String errorMsg = null;
        
        try {
            result = point.proceed();
        } catch (Exception e) {
            responseStatus = "FAILURE";
            errorMsg = e.getMessage();
            throw e;
        } finally {
            // 结束时间和耗时
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            // 构建日志消息
            StringBuilder logMsg = new StringBuilder();
            logMsg.append("[API_ACCESS] ")
                  .append("时间: ").append(requestTime).append(", ")
                  .append("IP: ").append(remoteAddr).append(", ")
                  .append("请求: ").append(httpMethod).append(" ").append(requestUri).append(", ")
                  .append("类方法: ").append(className).append(".").append(methodName).append(", ")
                  .append("参数: ").append(requestParams).append(", ")
                  .append("状态: ").append(responseStatus).append(", ")
                  .append("耗时: ").append(costTime).append("ms");
            
            // 如果有错误信息，添加到日志中
            if (errorMsg != null) {
                logMsg.append(", 错误: ").append(errorMsg);
            }
            
            // 记录日志
            if ("SUCCESS".equals(responseStatus)) {
                log.info(logMsg.toString());
            } else {
                log.error(logMsg.toString());
            }
        }
        
        return result;
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多个IP时，取第一个
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 过滤敏感参数
     */
    private String filterSensitiveParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        
        // 过滤掉HttpServletRequest和HttpServletResponse等对象
        Object[] filteredArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse))
                .toArray();
        
        // 简单的字符串表示，实际项目中可以考虑更复杂的序列化和敏感信息脱敏
        return Arrays.toString(filteredArgs);
    }
}