package com.mold.digitalization.handler;

import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * 返回统一响应格式，加上200 HTTP状态码
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 统一将业务错误码映射为 HTTP 状态码
     * 规则：
     * - 显式 404/401/403 保留
     * - 资源未找到（包含 message 提示 not found/不存在，或业务码为常见的未找到）-> 404
     * - 1xxx 业务类错误 -> 422 Unprocessable Entity（不可处理实体）
     * - 3xxx/5xxx（数据库/服务器类）-> 500 Internal Server Error
     * - 4xx 其它客户端错误 -> 400 Bad Request
     * - 其余默认 400
     */
    private HttpStatus mapBusinessCodeToStatus(int code, String message) {
        // 资源未找到判断
        int[] notFoundCodes = new int[]{
                404,
                601,   // 新版 BusinessException.ErrorCode.DATA_NOT_FOUND（新异常类）
                1001,  // DATA_NOT_FOUND（ErrorCodeEnum）
                10001, // USER_NOT_FOUND（另一套枚举）
                2002,  // FILE_NOT_FOUND
                4000   // MOLD_NOT_FOUND
        };
        String msg = message == null ? "" : message.toLowerCase();
        boolean isNotFound = java.util.Arrays.stream(notFoundCodes).anyMatch(c -> c == code)
                || msg.contains("not found")
                || msg.contains("不存在");

        if (isNotFound) {
            return HttpStatus.NOT_FOUND;
        }
        if (code == 401) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (code == 403) {
            return HttpStatus.FORBIDDEN;
        }
        // 刷新令牌相关的业务码特殊处理：按客户端错误返回400
        // 4011: TOKEN_EXPIRED, 4012: TOKEN_INVALID
        if (code == 4011 || code == 4012) {
            return HttpStatus.BAD_REQUEST;
        }
        if (code >= 1000 && code < 2000) {
            return HttpStatus.UNPROCESSABLE_ENTITY; // 1xxx -> 422
        }
        if ((code >= 3000 && code < 4000) || (code >= 500 && code < 600) || (code >= 5000 && code < 6000)) {
            return HttpStatus.INTERNAL_SERVER_ERROR; // 3xxx/5xx/5xxx -> 500
        }
        if (code >= 400 && code < 500) {
            return HttpStatus.BAD_REQUEST; // 其它客户端错误
        }
        return HttpStatus.BAD_REQUEST; // 默认 400
    }

    /**
     * 处理数字模块业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO<String>> handleBusinessException(BusinessException e) {
        // 业务异常设置成功HTTP状态码，后端码为业务错误码值
        log.error("BusinessException: {}", e.getMessage(), e);
        HttpStatus status = mapBusinessCodeToStatus(e.getCode(), e.getMessage());
        return ResponseEntity.status(status).body(ResponseDTO.error(e.getCode(), e.getMessage()));
    }

    /**
     * 兼容旧版异常类 com.mold.digitalization.common.exception.BusinessException
     * 该类的 code 为字符串，不同模块可能使用业务错误码（如 10001 表示 USER_NOT_FOUND）。
     * 在此进行合理的 HTTP 状态映射，避免统一落到 500。
     */
    @ExceptionHandler(com.mold.digitalization.common.exception.BusinessException.class)
    public ResponseEntity<ResponseDTO<String>> handleLegacyBusinessException(com.mold.digitalization.common.exception.BusinessException e) {
        log.error("Legacy BusinessException: {}", e.getMessage(), e);
        int parsedCode;
        try {
            parsedCode = Integer.parseInt(String.valueOf(e.getCode()));
        } catch (Exception ex) {
            parsedCode = 400; // 无法解析则按 400 处理
        }
        HttpStatus status = mapBusinessCodeToStatus(parsedCode, e.getMessage());
        return ResponseEntity.status(status).body(ResponseDTO.error(parsedCode, e.getMessage()));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseDTO<String>> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDTO.error(500, "系统内部错误：空指针异常"));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, e.getMessage()));
    }

    /**
     * 处理方法参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<String>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage(), e);
        // 获取第一个校验错误信息
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, errorMessage));
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDTO<String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException: {}", e.getMessage(), e);
        String errorMessage = String.format("参数 '%s' 类型错误，期望类型: %s，实际值: %s", 
                e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知", e.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, errorMessage));
    }

    /**
     * 处理属性绑定时的类型不匹配异常（例如转换器失败抛出的 TypeMismatchException）
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ResponseDTO<String>> handleSpringTypeMismatch(TypeMismatchException e) {
        log.error("TypeMismatchException: {}", e.getMessage(), e);
        String errorMessage = String.format("参数 '%s' 类型错误，期望类型: %s，实际值: %s", 
                e.getPropertyName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知", e.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, errorMessage));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDTO<String>> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException: {}", e.getMessage(), e);
        String errorMessage = String.format("缺少必要参数 '%s'，类型: %s", e.getParameterName(), e.getParameterType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, errorMessage));
    }

    /**
     * 处理HTTP消息不可读异常
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<String>> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, "请求消息不可读"));
    }

    /**
     * 处理HTTP请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO<String>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ResponseDTO.error(405, "请求方法不支持"));
    }

    /**
     * 处理HTTP媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDTO<String>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ResponseDTO.error(415, "媒体类型不支持"));
    }

    /**
     * 处理无处理器异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDTO<String>> handleNoHandlerFound(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error(404, "请求路径不存在"));
    }

    /**
     * 处理认证失败（如用户名或密码错误、刷新令牌无效）
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<String>> handleBadCredentials(BadCredentialsException e) {
        log.error("BadCredentialsException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.error(401, e.getMessage()));
    }

    /**
     * 处理权限拒绝
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<String>> handleAccessDenied(AccessDeniedException e) {
        log.error("AccessDeniedException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDTO.error(403, "拒绝访问"));
    }

    /**
     * 处理运行时异常，返回具体的错误信息
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<String>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        // 返回具体的错误信息，避免被统一处理为系统内部错误
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error(400, e.getMessage()));
    }

    /**
     * 处理数据库唯一约束异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<String>> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException e) {
        log.error("SQLIntegrityConstraintViolationException: {}", e.getMessage(), e);
        
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            // 检查是否是物料编码唯一约束违反
            if (errorMessage.contains("uk_material_code") || errorMessage.contains("material_code")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDTO.error(400, "物料编码已存在，请使用其他编码"));
            }
            // 可以添加其他唯一约束的处理
        }
        
        // 其他数据库约束异常，返回通用错误信息
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.error(400, "数据库约束异常：" + errorMessage));
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> handleGenericException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDTO.error(500, "系统内部错误"));
    }
}
