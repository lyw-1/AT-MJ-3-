# 错误处理最佳实践

## 📋 快速参考

### 1. 在Service层抛出业务异常
```java
@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            // 使用标准异常抛出方式
            throw new BusinessException(
                ErrorCodeEnum.USER_NOT_FOUND.getCode(),
                "用户不存在，ID: " + id
            );
        }
        return user;
    }
    
    @Override
    public void updateUser(Long id, UserUpdateRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(
                ErrorCodeEnum.USER_NOT_FOUND.getCode(),
                "用户不存在，无法更新"
            );
        }
        
        // 业务逻辑验证
        if (!user.getStatus().equals("ACTIVE")) {
            throw new BusinessException(
                ErrorCodeEnum.STATE_INVALID.getCode(),
                "只有激活状态的用户才能更新"
            );
        }
    }
}
```

### 2. 在Controller层处理异常
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseDTO<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseDTO.success(user);
    }
    
    @PutMapping("/{id}")
    public ResponseDTO<User> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        return ResponseDTO.success(user, "更新成功");
    }
}
```

### 3. 使用@Valid进行参数校验
```java
@Data
public class UserCreateRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$", 
             message = "密码必须包含大小写字母和数字，且长度不少于8位")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\d{9}$", message = "手机号格式不正确")
    private String phone;
}
```

### 4. 全局异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO<?> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        return ResponseDTO.error(400, "参数校验失败", message);
    }
}
```

## 🎯 错误处理原则

### 1. 统一错误格式
- 所有API响应使用相同的格式
- 错误信息要清晰明确
- 包含足够的调试信息（开发环境）

### 2. 适当抽象错误
- 使用业务异常类（BusinessException）
- 定义标准的错误码枚举
- 避免暴露底层实现细节

### 3. 记录关键信息
- 记录错误日志（包含请求ID）
- 区分警告和错误
- 包含足够的上下文信息

### 4. 友好的错误提示
- 避免暴露敏感信息
- 提供解决建议
- 区分用户错误和系统错误

## ❌ 避免的做法

### 1. 避免返回null
```java
// ❌ 错误做法
public User getUser(Long id) {
    return userMapper.selectById(id); // 可能返回null
}

// ✅ 正确做法
public User getUser(Long id) {
    User user = userMapper.selectById(id);
    if (user == null) {
        throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
    }
    return user;
}
```

### 2. 避免吞掉异常
```java
// ❌ 错误做法
try {
    // 业务逻辑
} catch (Exception e) {
    // 什么都不做，异常被吞掉了
}

// ✅ 正确做法
try {
    // 业务逻辑
} catch (SpecificException e) {
    log.warn("业务异常: {}", e.getMessage());
    throw e;
} catch (Exception e) {
    log.error("系统异常", e);
    throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
}
```

### 3. 避免暴露敏感信息
```java
// ❌ 错误做法
return ResponseDTO.error(500, "SQL: " + e.getSQLState());

// ✅ 正确做法
log.error("数据库错误", e);
return ResponseDTO.error(500, "系统内部错误");
```

### 4. 避免过于泛化的错误码
```java
// ❌ 错误做法
throw new Exception("出错了");

// ✅ 正确做法
throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED, "保存用户失败，请稍后重试");
```

## 🧪 测试建议

### 1. 单元测试异常场景
```java
@Test
void getUser_WhenUserNotFound_ShouldThrowException() {
    // Given
    Long userId = 999L;
    when(userMapper.selectById(userId)).thenReturn(null);
    
    // When & Then
    BusinessException exception = assertThrows(
        BusinessException.class, 
        () -> userService.getUserById(userId)
    );
    
    assertEquals(ErrorCodeEnum.USER_NOT_FOUND.getCode(), exception.getCode());
}
```

### 2. 集成测试错误响应
```java
@Test
void updateUser_WithInvalidData_ShouldReturn400() throws Exception {
    // Given
    UserUpdateRequest invalidRequest = new UserUpdateRequest();
    
    // When
    ResultActions result = mockMvc.perform(put("/api/v1/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)));
    
    // Then
    result.andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.code").value(400))
          .andExpect(jsonPath("$.message").value("参数校验失败"));
}
```

## 📊 监控和告警

### 1. 错误日志规范
```java
// 使用结构化日志
log.warn("业务异常 - userId: {}, action: {}, error: {}", 
        userId, action, errorMessage);

log.error("系统异常 - requestId: {}, path: {}", 
        requestId, requestURI, exception);
```

### 2. 关键指标监控
- 错误率（每分钟错误数）
- 响应时间（P95, P99）
- 业务异常分布
- 系统异常告警

### 3. 告警规则示例
```yaml
alerts:
  - name: high_error_rate
    condition: error_rate > 5% for 5 minutes
    severity: warning
    message: "错误率过高，请检查系统状态"
    
  - name: critical_error
    condition: any 5xx error
    severity: critical
    message: "检测到系统错误，请立即处理"
```

## 🔗 相关资源

- 错误码枚举: `ErrorCodeEnum.java`
- 统一响应: `ResponseDTO.java`
- 全局异常处理: `GlobalExceptionHandler.java`
- API版本控制: `docs/api/versioning.md`
- 错误响应标准: `docs/api/error-response-standard.md`
