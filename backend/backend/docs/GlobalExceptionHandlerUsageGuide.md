# 全局异常处理器使用指南

## 概述

本文档介绍了项目中统一的全局异常处理器，用于处理系统中的各种异常情况，并返回统一的响应格式。

## 功能特性

- 统一异常响应格式
- 支持多模块业务异常处理
- 自动记录异常日志
- 支持各种常见异常类型处理
- 提供友好的错误信息

## 核心组件

### 1. GlobalExceptionHandler

全局异常处理器，使用`@ControllerAdvice`注解标记，负责捕获和处理系统中的各种异常。

位置：`com.mold.digitalization.handler.GlobalExceptionHandler`

### 2. BusinessException

业务异常类，用于处理业务逻辑中的异常情况。

位置：`com.mold.digitalization.exception.BusinessException`

### 3. ResponseDTO

统一响应DTO，用于封装API响应结果。

位置：`com.mold.digitalization.dto.ResponseDTO`

## 异常处理类型

### 1. 业务异常

#### 数字模块业务异常
```java
// 使用枚举创建异常
throw new BusinessException(BusinessException.ErrorCode.BUSINESS_ERROR);
// 使用自定义消息
throw new BusinessException(BusinessException.ErrorCode.DATA_NOT_FOUND, "用户不存在");
// 使用静态方法
throw BusinessException.of(BusinessException.ErrorCode.DATA_DUPLICATE);
```

#### 流程模块业务异常
```java
// 使用流程模块的BusinessException
throw new com.mold.process.common.exception.BusinessException(400, "流程模块业务异常");
```

### 2. 系统异常

系统会自动处理以下系统异常：

- `NullPointerException` - 空指针异常
- `IllegalArgumentException` - 非法参数异常
- `MethodArgumentNotValidException` - 方法参数校验异常
- `MethodArgumentTypeMismatchException` - 方法参数类型不匹配异常
- `MissingServletRequestParameterException` - 缺少请求参数异常
- `HttpMessageNotReadableException` - HTTP消息不可读异常
- `HttpRequestMethodNotSupportedException` - HTTP请求方法不支持异常
- `HttpMediaTypeNotSupportedException` - HTTP媒体类型不支持异常
- `NoHandlerFoundException` - 无处理器异常
- `Exception` - 通用异常

## 响应格式

所有异常都会返回统一的响应格式：

```json
{
  "code": 500,
  "message": "错误信息",
  "data": null
}
```

### 常见错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 405 | 请求方法不支持 |
| 415 | 媒体类型不支持 |
| 500 | 系统内部错误 |
| 600 | 业务处理错误 |
| 601 | 数据不存在 |
| 602 | 数据已存在 |
| 603 | 数据无效 |
| 604 | 操作不允许 |
| 605 | 资源已锁定 |
| 606 | 配额已超限 |
| 607 | 请求频率超限 |

## 使用示例

### 1. 控制器中使用业务异常

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseDTO<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw BusinessException.of(BusinessException.ErrorCode.DATA_NOT_FOUND, "用户不存在");
        }
        return ResponseDTO.success(user);
    }
    
    @PostMapping
    public ResponseDTO<User> createUser(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(user.getUsername())) {
            throw BusinessException.of(BusinessException.ErrorCode.DATA_DUPLICATE, "用户名已存在");
        }
        return ResponseDTO.success(userService.save(user));
    }
}
```

### 2. 服务层中使用业务异常

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> BusinessException.of(
                BusinessException.ErrorCode.DATA_NOT_FOUND, "用户不存在"));
    }
    
    public User save(User user) {
        // 校验用户数据
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw BusinessException.of(BusinessException.ErrorCode.DATA_INVALID, "用户名不能为空");
        }
        return userRepository.save(user);
    }
}
```

### 3. 自定义异常处理

```java
// 自定义异常类
public class CustomException extends RuntimeException {
    private final int code;
    private final String message;
    
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}

// 在GlobalExceptionHandler中添加处理方法
@ExceptionHandler(CustomException.class)
public ResponseEntity<ResponseDTO<String>> handleCustomException(
        CustomException e, HttpServletRequest request) {
    log.error("自定义异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
    return ResponseEntity.ok(ResponseDTO.error(e.getCode(), e.getMessage()));
}
```

## 最佳实践

1. **使用业务异常**：在业务逻辑中使用`BusinessException`而不是直接抛出`RuntimeException`。

2. **提供有意义的错误信息**：错误信息应该清晰、具体，帮助用户理解问题所在。

3. **使用适当的错误码**：根据错误类型选择合适的错误码，便于前端处理。

4. **记录异常日志**：全局异常处理器会自动记录异常日志，无需在业务代码中重复记录。

5. **避免敏感信息泄露**：错误信息中不要包含敏感信息，如密码、密钥等。

6. **统一响应格式**：所有API都应返回统一的响应格式，包括异常情况。

## 故障排查

### 1. 异常未被捕获

如果异常未被全局异常处理器捕获，可能的原因：

- 异常被其他异常处理器捕获
- 异常处理器的优先级设置问题
- 异常处理器的包扫描范围问题

### 2. 响应格式不一致

如果响应格式不一致，可能的原因：

- 某个控制器直接返回了其他格式的响应
- 某个异常处理方法返回了其他格式的响应
- 前端处理响应的方式不一致

### 3. 异常信息不明确

如果异常信息不明确，可以：

- 在业务异常中提供更详细的错误信息
- 检查异常日志获取更多信息
- 使用调试模式查看完整的异常堆栈

## 测试

项目提供了完整的异常处理器测试，包括：

- 单元测试：`GlobalExceptionHandlerTest`
- 测试控制器：`ExceptionTestController`

运行测试：
```bash
mvn test -Dtest=GlobalExceptionHandlerTest
```

## 总结

全局异常处理器是系统中的重要组件，它提供了统一的异常处理机制，简化了业务代码，提高了系统的可维护性。通过合理使用业务异常和遵循最佳实践，可以构建更加健壮和用户友好的系统。

---

## 附录：用户不存在场景返回 404 的标准化与验证

本项目针对“用户不存在”的业务场景进行了标准化处理：当资源不存在时，统一返回 HTTP 404，并在业务响应中携带业务错误码 `10001` 与明确的错误信息 `User not found`。

### 业务规则与责任边界

- 安全边界：若请求未通过认证（未携带或携带错误令牌），Spring Security 会在到达控制器之前返回 `401 Unauthorized`，此时不会触发业务异常映射。
- 业务异常边界：当请求已通过认证但资源不存在时，由业务层抛出 `BusinessException`，全局异常处理器将其映射为统一结构并返回 `404`。

### 响应结构（统一）

```json
{
  "code": 10001,
  "message": "User not found",
  "data": null
}
```

### 验证步骤（Windows PowerShell 示例）

1. 登录获取 accessToken（接口路径允许多前缀，以下任选其一）：
   - /api/auth/login
   - /auth/login
   - /v1/api/auth/login

```powershell
$body = '{"username":"admin","password":"admin12345"}'
$r = Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/login -ContentType 'application/json' -Body $body
$token = $r.data.accessToken
```

2. 使用 accessToken 验证用户不存在的 404 行为：

```powershell
# GET /api/users/{id} 不存在的用户
curl.exe -s -H "Authorization: Bearer $token" http://localhost:8080/api/users/999
curl.exe -s -o NUL -w "GET /api/users/999 -> HTTP %{http_code}\n" -H "Authorization: Bearer $token" http://localhost:8080/api/users/999

# POST /api/users/{id}/reset-password 不存在的用户（需要管理员权限）
curl.exe -s -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d "{}" http://localhost:8080/api/users/999/reset-password
curl.exe -s -o NUL -w "POST /api/users/999/reset-password -> HTTP %{http_code}\n" -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d "{}" http://localhost:8080/api/users/999/reset-password
```

预期：两条请求均返回 HTTP 404，响应体为上述统一结构，`code=10001`，`message=User not found`。

### 开发联调提示

- 如需在本地临时对 GET 路径开放以验证 404（不登录），可在 `SecurityConfig` 中为 GET /api/users/** 设置 `permitAll`，仅限本地调试，切勿提交至生产配置。
- 登录接口在安全配置中已显式 `permitAll`，便于获取令牌。
- BCrypt 哈希每次生成的前缀相同但整体随机（含盐），只要使用一致的明文密码即可登录；示例演示密码为 `admin12345`，请在 UI 中尽快修改为更强随机密码。

### 参考文件

- 控制器：`UserController.java`（路径前缀 `/api/users`，包含 `GET /{id}` 与 `POST /{id}/reset-password`）
- 认证控制器：`AuthController.java`（路径前缀 `"/api/auth", "/auth", "/v1/api/auth"`，`POST /login`）
- 安全配置：`SecurityConfig.java`（`/api/auth/login` 等登录路径 `permitAll`）
- 统一响应：`ResponseDTO.java`（字段 `code`, `message`, `data`）
- 业务异常：`BusinessException`及枚举 `ErrorCodeEnum.USER_NOT_FOUND(10001)`

### 管理员接口异常映射规范（统一化）

自 2025-11-10 起，管理员接口（`/api/v1/admin/auth`）在“用户不存在”场景统一抛出 `BusinessException(ErrorCodeEnum.USER_NOT_FOUND)`，由全局异常处理器返回 HTTP 404 与统一 JSON 结构：

```json
{
  "code": 10001,
  "message": "User not found",
  "data": null
}
```

受影响的接口：
- POST `/api/v1/admin/auth/reset-password`
- POST `/api/v1/admin/auth/lock-user`
- POST `/api/v1/admin/auth/unlock-user/{userId}`
- POST `/api/v1/admin/auth/users/{userId}/unlock`

同时，成功响应统一为 `ResponseDTO.success(...)`，其他错误使用 `ResponseDTO.error(code, message)`。未认证统一返回 401；已认证但非管理员角色统一返回 403。
