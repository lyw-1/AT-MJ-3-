package com.mold.digitalization.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.utils.SensitiveInfoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 操作日志切面
 * 自动记录用户操作日志
 */
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class OperationLogAspect {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OperationLogAspect.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final OperationLogService operationLogService;

    /**
     * 定义切点，拦截所有Controller层方法
     */
    @Pointcut("execution(* com.mold.digitalization.controller..*.*(..))")
    public void operationLogPointCut() {
    }
    
    /**
     * 对敏感内容进行脱敏处理
     * @param args 请求参数数组
     * @return 脱敏后的内容
     */
    private String maskSensitiveContent(Object[] args) {
        // 默认使用 normal 级别脱敏
        return maskSensitiveContent(args, "normal");
    }
    
    /**
     * 对敏感内容进行脱敏处理
     * @param args 请求参数数组
     * @param sensitiveLevel 敏感级别
     * @return 脱敏后的内容
     */
    private String maskSensitiveContent(Object[] args, String sensitiveLevel) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        try {
            ArrayNode jsonArray = MAPPER.createArrayNode();
            for (Object arg : args) {
                JsonNode node = MAPPER.valueToTree(arg);
                // add JsonNode directly covers object/array/value
                jsonArray.add(node);
            }

            ArrayNode masked = maskArrayContent(jsonArray, sensitiveLevel);
            return masked.toString();
        } catch (Exception e) {
            log.error("Masking failed: {}", e.getMessage(), e);
            try {
                return maskSimpleContent(MAPPER.writeValueAsString(args), sensitiveLevel);
            } catch (Exception ex) {
                return String.valueOf(args);
            }
        }
    }
    
    /**
     * 对数组内容进行脱敏
     */
    private ArrayNode maskArrayContent(ArrayNode array) {
        return maskArrayContent(array, "normal");
    }
    
    /**
     * 对数组内容进行脱敏
     * @param array JSON数组
     * @param sensitiveLevel 敏感级别
     * @return 脱敏后的JSON字符串
     */
    private ArrayNode maskArrayContent(ArrayNode array, String sensitiveLevel) {
        ArrayNode maskedArray = MAPPER.createArrayNode();

        for (JsonNode obj : array) {
            if (obj.isObject()) {
                maskedArray.add(maskObjectContent((ObjectNode) obj, sensitiveLevel));
            } else if (obj.isTextual()) {
                maskedArray.add(maskSimpleContent(obj.asText(), sensitiveLevel));
            } else if (obj.isArray()) {
                // recursion
                maskedArray.add(maskArrayContent((ArrayNode) obj, sensitiveLevel));
            } else {
                maskedArray.add(obj);
            }
        }

        return maskedArray;
    }
    
    /**
     * 对对象内容进行脱敏
     */
    private ObjectNode maskObjectContent(ObjectNode jsonObject) {
        return maskObjectContent(jsonObject, "normal");
    }
    
    /**
     * 对对象内容进行脱敏
     * @param jsonObject JSON对象
     * @param sensitiveLevel 敏感级别
     * @return 脱敏后的JSON对象
     */
    private ObjectNode maskObjectContent(ObjectNode jsonObject, String sensitiveLevel) {
        ObjectNode maskedObject = MAPPER.createObjectNode();

        jsonObject.fieldNames().forEachRemaining(key -> {
            JsonNode value = jsonObject.get(key);

            if (value == null || value.isNull()) {
                maskedObject.putNull(key);
            } else if (value.isTextual()) {
                String strValue = value.asText();
                if (isSensitiveField(key)) {
                    maskedObject.put(key, SensitiveInfoUtil.maskBySensitiveLevel(strValue, sensitiveLevel, key));
                } else {
                    maskedObject.put(key, strValue);
                }
            } else if (value.isObject()) {
                maskedObject.set(key, maskObjectContent((ObjectNode) value, sensitiveLevel));
            } else if (value.isArray()) {
                ArrayNode array = (ArrayNode) value;
                ArrayNode maskedArray = MAPPER.createArrayNode();
                for (JsonNode item : array) {
                    if (item.isObject()) {
                        maskedArray.add(maskObjectContent((ObjectNode) item, sensitiveLevel));
                    } else if (item.isTextual()) {
                        maskedArray.add(maskSimpleContent(item.asText(), sensitiveLevel));
                    } else {
                        maskedArray.add(item);
                    }
                }
                maskedObject.set(key, maskedArray);
            } else if (value.isNumber()) {
                maskedObject.putPOJO(key, value.numberValue());
            } else if (value.isBoolean()) {
                maskedObject.put(key, value.booleanValue());
            } else {
                maskedObject.putPOJO(key, value);
            }
        });

        return maskedObject;
    }

    /**
     * 环绕通知，用于记录操作日志
     */
    @Around("operationLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        String errorMsg = null;
        String resultStatus = "SUCCESS";

        try {
            // 执行方法
            result = point.proceed();
        } catch (Exception e) {
            errorMsg = e.getMessage();
            resultStatus = "FAILURE";
            throw e;
        } finally {
            // 异步记录操作日志
            final String finalErrorMsg = errorMsg;
            final String finalResultStatus = resultStatus;
            CompletableFuture.runAsync(() -> {
                try {
                    saveOperationLog(point, beginTime, finalResultStatus, finalErrorMsg);
                } catch (Exception e) {
                    log.error("记录操作日志失败: {}", e.getMessage(), e);
                }
            });
        }
        return result;
    }

    /**
     * 保存操作日志
     */
    private void saveOperationLog(ProceedingJoinPoint point, long beginTime, String resultStatus, String errorMsg) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        OperationLog operationLog = new OperationLog();

        // 获取操作用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            User user = (User) authentication.getPrincipal();
            operationLog.setUserId(user.getId());
            operationLog.setUsername(user.getUsername());
        }

        // 获取用户IP
        String ip = request.getRemoteAddr();
        // 处理代理IP
        String forwardedIp = request.getHeader("X-Forwarded-For");
        if (forwardedIp != null && !forwardedIp.isEmpty()) {
            ip = forwardedIp.split(",")[0].trim();
        }
        operationLog.setUserIp(ip);

        // 获取操作信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取操作类型
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            operationLog.setOperationDesc(apiOperation.value());
        }

        // 获取操作类型（GET/POST/PUT/DELETE等）
        operationLog.setOperationType(request.getMethod());

        // 获取请求参数
        try {
            Object[] args = point.getArgs();
            // 过滤敏感参数
            Object[] filteredArgs = Arrays.stream(args)
                    .filter(arg -> !(arg instanceof HttpServletRequest || arg instanceof jakarta.servlet.http.HttpServletResponse))
                    .toArray();
            
            // 设置敏感级别，默认为normal
            if (operationLog.getSensitiveLevel() == null) {
                operationLog.setSensitiveLevel("normal");
            }
            
            // 对请求参数进行脱敏处理，传入敏感级别
            String sensitiveContent = maskSensitiveContent(filteredArgs, operationLog.getSensitiveLevel());
            operationLog.setOperationContent(sensitiveContent);
        } catch (Exception e) {
            operationLog.setOperationContent("无法解析请求参数");
        }

        // 检查是否为敏感操作
        checkSensitiveOperation(method, operationLog);
        
        // 设置操作结果（与表字段 result 对应）
        operationLog.setResult(resultStatus);
        if (errorMsg != null) {
            operationLog.setOperationContent(operationLog.getOperationContent() + "\n错误信息: " + errorMsg);
        }

        // 设置操作时间
        operationLog.setOperationTime(LocalDateTime.now());

        // 保存日志
        operationLogService.saveOperationLog(operationLog);
    }
    
    /**
     * 检查是否为敏感操作
     */
    private void checkSensitiveOperation(Method method, OperationLog operationLog) {
        // 检查方法是否有SensitiveOperation注解
        SensitiveOperation sensitiveOperation = method.getAnnotation(SensitiveOperation.class);
        if (sensitiveOperation != null) {
            operationLog.setIsSensitive(true);
            
            // 添加敏感操作相关信息
            String level = sensitiveOperation.level();
            String description = sensitiveOperation.description();
            
            // 设置敏感级别
            operationLog.setSensitiveLevel(level);
            
            // 在操作描述中添加敏感信息
            String originalDesc = operationLog.getOperationDesc();
            if (!StringUtils.hasText(originalDesc)) {
                operationLog.setOperationDesc(description + " [敏感操作-" + level + "]");
            } else {
                operationLog.setOperationDesc(originalDesc + " [敏感操作-" + level + "]");
            }
            
            // 对于高危敏感操作，可以在这里添加额外的处理逻辑
            // 例如：发送告警通知、记录更详细的上下文信息等
            if ("high".equals(level) || "critical".equals(level)) {
                // 可以在这里添加额外的处理逻辑
                log.info("检测到高危敏感操作: {} 用户: {} IP: {}", 
                        operationLog.getOperationDesc(), operationLog.getUsername(), operationLog.getUserIp());
            }
        } else {
            // 默认非敏感操作
            operationLog.setIsSensitive(false);
            operationLog.setSensitiveLevel("normal");
            
            // 自动检测常见的敏感操作
            detectAutoSensitiveOperation(operationLog);
        }
    }
    
    /**
     * 检查字段是否为敏感字段
     * @param fieldName 字段名
     * @return 是否为敏感字段
     */
    private boolean isSensitiveField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        
        // List of common sensitive field names (English-only to avoid encoding issues)
        String[] sensitiveFields = {
            "password", "pwd", "passwd",
            "idcard", "id_card", "id",
            "mobile", "phone", "telephone",
            "bankcard", "bank_card",
            "email",
            "address",
            "realname", "name",
            "credit",
            "token", "key", "secret",
            "pay", "payment",
            "salary"
        };
        
        String lowerFieldName = fieldName.toLowerCase();
        for (String sensitive : sensitiveFields) {
            if (lowerFieldName.contains(sensitive.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 简单内容脱敏处理
     * @param content 内容
     * @param sensitiveLevel 敏感级别
     * @return 脱敏后的内容
     */
    private String maskSimpleContent(String content, String sensitiveLevel) {
        if (!StringUtils.hasText(content)) {
            return content;
        }
        
        // 这里可以添加简单的脱敏逻辑，例如替换手机号、身份证号等
        String maskedContent = content;
        
        // 根据敏感级别调整脱敏策略
        if ("critical".equals(sensitiveLevel)) {
            // 关键级别：更严格的脱敏
            maskedContent = SensitiveInfoUtil.maskMobile(maskedContent);
            maskedContent = SensitiveInfoUtil.maskIdCard(maskedContent);
            maskedContent = SensitiveInfoUtil.maskBankCard(maskedContent);
            // email字段已移除，不再调用maskEmail
            maskedContent = SensitiveInfoUtil.maskAddress(maskedContent);
            maskedContent = SensitiveInfoUtil.maskName(maskedContent);
        } else if ("high".equals(sensitiveLevel)) {
            // 高级别：标准脱敏
            maskedContent = SensitiveInfoUtil.maskMobile(maskedContent);
            maskedContent = SensitiveInfoUtil.maskIdCard(maskedContent);
            maskedContent = SensitiveInfoUtil.maskBankCard(maskedContent);
            // email字段已移除，不再调用maskEmail
        } else {
            // 普通级别：基本脱敏
            maskedContent = SensitiveInfoUtil.maskMobile(maskedContent);
            maskedContent = SensitiveInfoUtil.maskIdCard(maskedContent);
        }
        
        return maskedContent;
    }
    
    /**
     * 自动检测常见的敏感操作
     */
    private void detectAutoSensitiveOperation(OperationLog operationLog) {
        // 根据操作类型、路径等自动判断是否为敏感操作
        String operationType = operationLog.getOperationType();
        String operationDesc = operationLog.getOperationDesc();
        String operationContent = operationLog.getOperationContent();
        
        // 定义高风险敏感操作关键词
    String[] highRiskKeywords = {
        "delete", "remove", "drop", "truncate",
        "password", "grant", "authorize", "permission"
    };
        
        // 定义中风险敏感操作关键词
    String[] normalRiskKeywords = {
        "update", "reset", "admin", "role"
    };
        
        boolean isHighRisk = false;
        String keywordFound = null;
        
        // 首先检查高风险关键词
        for (String keyword : highRiskKeywords) {
            if ((operationDesc != null && operationDesc.toLowerCase().contains(keyword.toLowerCase())) ||
                (operationContent != null && operationContent.toLowerCase().contains(keyword.toLowerCase())) ||
                (operationType != null && operationType.equals("DELETE"))) {
                isHighRisk = true;
                keywordFound = keyword;
                break;
            }
        }
        
        // 如果没有高风险关键词，检查中风险关键词
        if (!isHighRisk) {
            for (String keyword : normalRiskKeywords) {
                if ((operationDesc != null && operationDesc.toLowerCase().contains(keyword.toLowerCase())) ||
                    (operationContent != null && operationContent.toLowerCase().contains(keyword.toLowerCase()))) {
                    keywordFound = keyword;
                    break;
                }
            }
        }
        
        // 如果找到敏感关键词，标记为敏感操作并设置适当的级别
        if (keywordFound != null) {
            operationLog.setIsSensitive(true);
            operationLog.setSensitiveLevel(isHighRisk ? "high" : "normal");
            operationLog.setOperationDesc((operationDesc != null ? operationDesc : "") + " [自动标记-敏感操作-" + 
                               (isHighRisk ? "high" : "normal") + "]");
            
            // 记录高风险敏感操作的日志
            if (isHighRisk) {
                log.info("自动检测到高风险敏感操作: {} 关键词: {} 用户: {} IP: {}", 
                        operationLog.getOperationDesc(), keywordFound, operationLog.getUsername(), operationLog.getUserIp());
            }
        }
    }
}
