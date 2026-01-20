# 统一错误响应规范

## 📊 当前状态评估

### ✅ 优点
1. 已有统一的 `ResponseDTO` 类
2. 完善的错误码枚举 `ErrorCodeEnum`
3. 支持成功和错误两种响应

### ⚠️ 可改进点
1. 缺少时间戳字段
2. 缺少请求追踪ID
3. 缺少详细的错误信息（开发环境）
4. 缺少标准化的错误结构

## 🎯 标准化错误响应格式

### 1. 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 实际数据
  },
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-abc-123"
}
```

### 2. 错误响应
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null,
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-abc-123",
  "details": "详细错误信息（仅开发环境）"
}
```

### 3. 业务异常响应
```json
{
  "code": 1001,
  "message": "数据未找到",
  "data": null,
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-abc-123",
  "details": {
    "resource": "User",
    "id": 123,
    "hint": "请检查ID是否正确"
  }
}
```

## 🔧 实施步骤

### Step 1: 增强 ResponseDTO

```java
@Data
public class ResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 状态码
    private int code;
    
    // 消息
    private String message;
    
    // 数据
    private T data;
    
    // 时间戳
    private long timestamp;
    
    // 请求追踪ID
    private String requestId;
    
    // 详细信息（仅开发环境）
    private Object details;
    
    // 成功响应
    public static <T> ResponseDTO<T> success(T data) {
        return success(data, "success");
    }
    
    // 成功响应（带消息）
    public static <T> ResponseDTO<T> success(T data, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        response.setRequestId(generateRequestId());
        return response;
    }
    
    // 错误响应
    public static <T> ResponseDTO<T> error(int code, String message) {
        return error(code, message, null);
    }
    
    // 错误响应（带详情）
    public static <T> ResponseDTO<T> error(int code, String message, Object details) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        response.setRequestId(generateRequestId());
        response.setDetails(details);
        return response;
    }
    
    private static String generateRequestId() {
        return "req-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
```

### Step 2: 添加全局异常处理器

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseDTO<?> handleBusinessException(BusinessException ex) {
        return ResponseDTO.error(
            ex.getCode(), 
            ex.getMessage(),
            getDevelopmentDetails(ex)
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseDTO<?> handleException(Exception ex) {
        // 生产环境不暴露详细信息
        return ResponseDTO.error(
            500, 
            "系统内部错误",
            isDevelopmentEnvironment() ? getStackTrace(ex) : null
        );
    }
    
    private Object getDevelopmentDetails(Exception ex) {
        if (isDevelopmentEnvironment()) {
            Map<String, Object> details = new HashMap<>();
            details.put("exception", ex.getClass().getName());
            details.put("message", ex.getMessage());
            details.put("stackTrace", getStackTrace(ex));
            return details;
        }
        return null;
    }
}
```

### Step 3: 添加请求追踪过滤器

```java
@Component
public class RequestIdFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestId = httpRequest.getHeader("X-Request-ID");
        
        if (StringUtils.isEmpty(requestId)) {
            requestId = "req-" + UUID.randomUUID().toString().substring(0, 8);
        }
        
        // 将requestId设置到请求属性中
        request.setAttribute("requestId", requestId);
        
        // 添加到响应头
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Request-ID", requestId);
        
        chain.doFilter(request, response);
    }
}
```

## 📋 错误码规范

### HTTP 状态码
- `200` - 成功
- `400` - 请求参数错误
- `401` - 未认证
- `403` - 无权限
- `404` - 资源不存在
- `500` - 系统内部错误

### 业务错误码 (4xxx)
- `1000-1999` - 通用业务错误
- `2000-2999` - 文件相关错误
- `3000-3999` - 数据库相关错误
- `4000-4999` - 模具相关错误
- `10000-19999` - 用户相关错误

## 🎨 错误响应示例

### 认证错误
```json
{
  "code": 401,
  "message": "未认证",
  "data": null,
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-abc123",
  "details": {
    "required": "Authorization header",
    "hint": "请在请求头中添加 Bearer token"
  }
}
```

### 参数验证错误
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null,
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-def456",
  "details": {
    "field": "email",
    "rejectedValue": "invalid-email",
    "message": "邮箱格式不正确"
  }
}
```

### 业务逻辑错误
```json
{
  "code": 1001,
  "message": "数据未找到",
  "data": null,
  "timestamp": "2026-01-20T12:00:00",
  "requestId": "req-ghi789",
  "details": {
    "resource": "User",
    "query": "id=123",
    "hint": "请检查ID是否正确，或该资源可能已被删除"
  }
}
```

## 🔄 渐进式实施

### Phase 1: 增强 ResponseDTO (本周)
- [ ] 添加 timestamp、requestId、details 字段
- [ ] 更新工厂方法
- [ ] 添加 RequestIdFilter
- [ ] 单元测试覆盖

### Phase 2: 完善异常处理 (下周)
- [ ] 添加全局异常处理器
- [ ] 统一错误码管理
- [ ] 开发/生产环境区分
- [ ] 错误日志规范

### Phase 3: 文档和培训 (本月)
- [ ] 更新API文档
- [ ] 编写错误处理指南
- [ ] 团队培训
- [ ] 监控告警配置

## ✅ 验证清单

实施完成后请检查：
- [ ] 所有API返回统一格式
- [ ] 包含请求追踪ID
- [ ] 错误信息清晰明确
- [ ] 开发环境显示详细信息
- [ ] 生产环境不暴露敏感信息
- [ ] 单元测试覆盖边界情况
- [ ] 集成测试验证完整流程

## 📞 常见问题

### Q: 如何区分开发环境和生产环境？
A: 使用 Spring Profile 或 application.yml 中的配置：
```yaml
spring:
  profiles:
    active: dev  # 或 prod
```

### Q: 错误详情会不会泄露安全信息？
A: 生产环境只显示通用错误信息，详细错误只记录到日志。

### Q: 如何处理第三方API的错误？
A: 统一转换为项目标准错误格式，保留原始错误码作为details。
