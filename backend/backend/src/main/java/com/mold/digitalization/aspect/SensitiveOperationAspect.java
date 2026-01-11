package com.mold.digitalization.aspect;

import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.config.SensitiveOperationConfig;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.utils.SensitiveInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感操作切面类
 * 自动记录标注了@SensitiveOperation注解的方法调用
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SensitiveOperationAspect {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SensitiveOperationAspect.class);
    
    private final OperationLogService operationLogService;
    private final SensitiveOperationConfig sensitiveOperationConfig;
    
    // 存储操作开始时间的ThreadLocal，用于计算操作耗时
    private static final ThreadLocal<Map<String, Object>> operationContext = new ThreadLocal<>();
    
    /**
     * 在执行敏感操作前记录操作开始信息
     */
    @Before("@annotation(sensitiveOperation)")
    public void beforeSensitiveOperation(JoinPoint joinPoint, SensitiveOperation sensitiveOperation) {
        try {
            // 获取方法信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            
            // 创建操作上下文
            Map<String, Object> context = new HashMap<>();
            context.put("startTime", System.currentTimeMillis());
            context.put("method", method);
            context.put("annotation", sensitiveOperation);
            context.put("args", joinPoint.getArgs());
            
            operationContext.set(context);
            
            log.debug("开始执行敏感操作: {}", method.getName());
        } catch (Exception e) {
            log.error("敏感操作前置处理失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 敏感操作成功执行后记录操作日志
     */
    @AfterReturning(pointcut = "@annotation(sensitiveOperation)", returning = "result")
    public void afterSensitiveOperationSuccess(JoinPoint joinPoint, SensitiveOperation sensitiveOperation, Object result) {
        try {
            Map<String, Object> context = operationContext.get();
            if (context == null) {
                return;
            }
            
            // 计算操作耗时
            long startTime = (Long) context.get("startTime");
            long costTime = System.currentTimeMillis() - startTime;
            
            // 创建操作日志记录
            OperationLog operationLog = buildOperationLog(joinPoint, sensitiveOperation, context);
            operationLog.setResultCode("200");
            operationLog.setResultMsg("操作成功");
            
            // 如果设置了记录详细参数，则处理返回值
            if (sensitiveOperation.recordDetail() && result != null) {
                String resultContent = processResultContent(result, sensitiveOperation.level());
                operationLog.setOperationContent(resultContent);
            }
            
            // 保存操作日志
            operationLogService.save(operationLog);
            
            log.debug("敏感操作执行成功: {}, 耗时: {}ms", joinPoint.getSignature().getName(), costTime);
        } catch (Exception e) {
            log.error("敏感操作后置处理失败: {}", e.getMessage(), e);
        } finally {
            operationContext.remove();
        }
    }
    
    /**
     * 敏感操作执行失败后记录错误日志
     */
    @AfterThrowing(pointcut = "@annotation(sensitiveOperation)", throwing = "exception")
    public void afterSensitiveOperationFailure(JoinPoint joinPoint, SensitiveOperation sensitiveOperation, Exception exception) {
        try {
            Map<String, Object> context = operationContext.get();
            if (context == null) {
                return;
            }
            
            // 计算操作耗时
            long startTime = (Long) context.get("startTime");
            long costTime = System.currentTimeMillis() - startTime;
            
            // 创建操作日志记录
            OperationLog operationLog = buildOperationLog(joinPoint, sensitiveOperation, context);
            operationLog.setResultCode("500");
            operationLog.setResultMsg("操作失败: " + exception.getMessage());
            
            // 如果设置了记录详细参数，则处理异常信息
            if (sensitiveOperation.recordDetail()) {
                String errorContent = "{\"error\": \"" + exception.getClass().getSimpleName() + ": " + exception.getMessage() + "\"}";
                operationLog.setOperationContent(errorContent);
            }
            
            // 保存操作日志
            operationLogService.save(operationLog);
            
            log.warn("敏感操作执行失败: {}, 耗时: {}ms, 错误: {}", joinPoint.getSignature().getName(), costTime, exception.getMessage());
        } catch (Exception e) {
            log.error("敏感操作异常处理失败: {}", e.getMessage(), e);
        } finally {
            operationContext.remove();
        }
    }
    
    /**
     * 构建操作日志对象
     */
    private OperationLog buildOperationLog(JoinPoint joinPoint, SensitiveOperation sensitiveOperation, Map<String, Object> context) {
        OperationLog operationLog = new OperationLog();
        
        // 设置基本操作信息
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setOperationType(joinPoint.getSignature().getName());
        operationLog.setModule(getModuleName(joinPoint));
        operationLog.setOperationDesc(sensitiveOperation.description());
        
        // 设置敏感操作标记和级别
        operationLog.setIsSensitive(true);
        operationLog.setSensitiveLevel(sensitiveOperation.level());
        
        // 获取用户信息（从请求上下文）
        setUserInfo(operationLog);
        
        // 处理操作参数
        if (sensitiveOperation.recordDetail()) {
            String argsContent = processArguments((Object[]) context.get("args"));
            operationLog.setOperationContent(argsContent);
        }
        
        return operationLog;
    }
    
    /**
     * 获取模块名称
     */
    private String getModuleName(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        if (className.endsWith("Controller")) {
            return "控制层";
        } else if (className.endsWith("Service")) {
            return "业务层";
        } else if (className.endsWith("Mapper")) {
            return "数据层";
        }
        return "未知模块";
    }
    
    /**
     * 设置用户信息
     */
    private void setUserInfo(OperationLog operationLog) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 获取客户端IP
                String ip = getClientIpAddress(request);
                operationLog.setUserIp(ip);
                
                // 这里应该从JWT token或session中获取用户信息
                // 暂时使用模拟数据
                operationLog.setUsername("admin");
                operationLog.setUserId(1L);
            }
        } catch (Exception e) {
            log.warn("获取用户信息失败: {}", e.getMessage());
            // 设置默认值
            operationLog.setUsername("system");
            operationLog.setUserId(0L);
            operationLog.setUserIp("127.0.0.1");
        }
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 处理操作参数
     */
    private String processArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "{}";
        }
        
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("\"arg").append(i).append("\": ");
            
            if (args[i] == null) {
                sb.append("null");
            } else {
                String argStr = args[i].toString();
                // 对敏感信息进行脱敏处理
                argStr = SensitiveInfoUtil.maskJsonSensitiveInfo(argStr, "normal");
                sb.append("\"").append(argStr).append("\"");
            }
        }
        sb.append("}");
        
        return sb.toString();
    }
    
    /**
     * 处理返回值内容
     */
    private String processResultContent(Object result, String sensitiveLevel) {
        if (result == null) {
            return "{}";
        }
        
        String resultStr = result.toString();
        // 对返回值中的敏感信息进行脱敏处理
        return SensitiveInfoUtil.maskJsonSensitiveInfo(resultStr, sensitiveLevel);
    }
}
