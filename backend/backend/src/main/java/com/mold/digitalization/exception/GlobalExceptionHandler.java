package com.mold.digitalization.exception;

import com.mold.digitalization.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理所有异常，返回标准错误响应
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseDTO<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("业务异常 - path: {}, code: {}, message: {}", 
                request.getRequestURI(), ex.getCode(), ex.getMessage());
        
        Map<String, Object> details = createErrorDetails(ex, request);
        return ResponseDTO.error(ex.getCode(), ex.getMessage(), details);
    }
    
    /**
     * 处理参数校验异常 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.warn("参数校验失败 - path: {}, errors: {}", request.getRequestURI(), message);
        
        Map<String, Object> details = new HashMap<>();
        details.put("type", "validation_error");
        details.put("errors", ex.getBindingResult().getFieldErrors());
        
        return ResponseDTO.error(400, "参数校验失败", details);
    }
    
    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseDTO<?> handleBindException(BindException ex, HttpServletRequest request) {
        String message = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.warn("参数绑定失败 - path: {}, errors: {}", request.getRequestURI(), message);
        
        return ResponseDTO.error(400, "参数错误", message);
    }
    
    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDTO<?> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("非法参数 - path: {}, message: {}", request.getRequestURI(), ex.getMessage());
        
        return ResponseDTO.error(400, ex.getMessage());
    }
    
    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseDTO<?> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.error("空指针异常 - path: {}", request.getRequestURI(), ex);
        
        Map<String, Object> details = new HashMap<>();
        details.put("type", "null_pointer");
        details.put("hint", "请检查请求参数是否完整");
        
        return ResponseDTO.error(500, "系统内部错误", details);
    }
    
    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseDTO<?> handleException(Exception ex, HttpServletRequest request) {
        log.error("未处理异常 - path: {}", request.getRequestURI(), ex);
        
        Map<String, Object> details = new HashMap<>();
        details.put("type", "internal_error");
        details.put("message", "系统内部错误，请稍后重试");
        
        // 生产环境不显示堆栈信息
        if (isDevelopmentEnvironment()) {
            details.put("stackTrace", getStackTrace(ex));
        }
        
        return ResponseDTO.error(500, "系统内部错误", details);
    }
    
    /**
     * 创建错误详情
     */
    private Map<String, Object> createErrorDetails(BusinessException ex, HttpServletRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("type", "business_error");
        details.put("path", request.getRequestURI());
        details.put("method", request.getMethod());
        
        // 如果是开发环境，添加额外信息
        if (isDevelopmentEnvironment()) {
            details.put("exception", ex.getClass().getName());
            details.put("stackTrace", getStackTrace(ex));
        }
        
        return details;
    }
    
    /**
     * 获取堆栈信息
     */
    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * 判断是否为开发环境
     */
    private boolean isDevelopmentEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "");
        return profile.isEmpty() || profile.contains("dev");
    }
}
