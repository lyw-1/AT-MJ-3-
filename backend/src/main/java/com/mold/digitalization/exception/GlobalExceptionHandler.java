package com.mold.digitalization.exception;

import com.mold.digitalization.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Unified global exception handler for the backend.
 * Returns structured error responses to the frontend.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Development env check: default to dev if not specified
    private boolean isDevelopmentEnvironment(HttpServletRequest request) {
        String env = System.getProperty("spring.profiles.active", "");
        return env.isEmpty() || env.contains("dev") || env.contains("local");
    }

    private HttpStatus mapCodeToStatus(int code) {
        if (code == 401) return HttpStatus.UNAUTHORIZED;
        if (code == 403) return HttpStatus.FORBIDDEN;
        if (code == 404) return HttpStatus.NOT_FOUND;
        if (code >= 1000 && code < 2000) return HttpStatus.UNPROCESSABLE_ENTITY;
        if ((code >= 3000 && code < 4000) || (code >= 500 && code < 600)) return HttpStatus.INTERNAL_SERVER_ERROR;
        if (code >= 400 && code < 500) return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String stackTrace(Throwable t) {
        if (t == null) return "";
        return java.util.Arrays.stream(t.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n"));
    }

    private ResponseDTO<String> errorResponse(int code, String message, Map<String, Object> details, HttpStatus status, boolean dev) {
        if (details == null) details = new HashMap<>();
        details.putIfAbsent("type", "internal_error");
        if (dev && details.get("stackTrace") == null) {
            details.put("stackTrace", stackTrace(new Throwable(message)));
        }
        return ResponseDTO.<String>error(code, message, details);
    }

    // 1. 业务异常（自定义实现）
    @ExceptionHandler(com.mold.digitalization.exception.BusinessException.class)
    public ResponseEntity<ResponseDTO<String>> handleBusiness(com.mold.digitalization.exception.BusinessException e, HttpServletRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("type", "business_error");
        details.put("path", request.getRequestURI());
        details.put("code", e.getCode());
        details.put("message", e.getMessage());
        HttpStatus status = mapCodeToStatus(e.getCode());
        return ResponseEntity.status(status).body(ResponseDTO.error(e.getCode(), e.getMessage(), details));
    }

    // 兼容旧的 legacy BusinessException
    @ExceptionHandler(com.mold.digitalization.common.exception.BusinessException.class)
    public ResponseEntity<ResponseDTO<String>> handleLegacyBusiness(com.mold.digitalization.common.exception.BusinessException e, HttpServletRequest request) {
        int parsedCode; try { parsedCode = Integer.parseInt(String.valueOf(e.getCode())); } catch (Exception ex) { parsedCode = 400; }
        HttpStatus status = mapCodeToStatus(parsedCode);
        Map<String, Object> details = new HashMap<>();
        details.put("type", "business_error");
        details.put("path", request.getRequestURI());
        details.put("code", parsedCode);
        details.put("message", e.getMessage());
        return ResponseEntity.status(status).body(ResponseDTO.error(parsedCode, e.getMessage(), details));
    }

    // 2. 参数绑定/校验错误
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDTO<String>> handleBind(BindException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        Map<String, Object> details = new HashMap<>();
        details.put("type", "bind_error");
        details.put("errors", ex.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, message == null ? "参数错误" : message, details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<String>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst().orElse("参数校验失败");
        Map<String, Object> details = new HashMap<>();
        details.put("type", "validation_error");
        details.put("errors", ex.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, message, details));
    }

    // 3. 请求参数缺失
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDTO<String>> handleMissing(MissingServletRequestParameterException ex) {
        String msg = String.format("缺少必要参数 '%s'，类型: %s", ex.getParameterName(), ex.getParameterType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, msg));
    }

    // 4. 请求体不可读
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<String>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, "请求消息不可读"));
    }

    // 5. URL找不到
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDTO<String>> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error(404, "请求路径不存在"));
    }

    // 6. HTTP方法不支持
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO<String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ResponseDTO.error(405, "请求方法不支持"));
    }

    // 7. 媒体类型不支持
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDTO<String>> handleMediaNotSupported(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ResponseDTO.error(415, "媒体类型不支持"));
    }

    // 8. 认证/授权相关
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDTO.error(403, "拒绝访问"));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.error(401, ex.getMessage()));
    }

    // 9. 数据库约束异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<String>> handleSqlIntegrity(SQLIntegrityConstraintViolationException ex) {
        String msg = ex.getMessage();
        if (msg != null && (msg.contains("uk_material_code") || msg.contains("material_code"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, "物料编码已存在，请使用其他编码"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, "数据库约束异常：" + msg));
    }

    // 10. 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<String>> handleRuntime(RuntimeException ex) {
        log.error("RuntimeException:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, ex.getMessage()));
    }
}
