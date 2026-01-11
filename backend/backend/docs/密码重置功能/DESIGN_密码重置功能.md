# 密码重置功能设计文档

## 1. 整体架构图

```mermaid
flowchart TD
    Client[前端客户端] --> PasswordResetController[密码重置控制器]
    PasswordResetController --> AuthService[认证服务]
    AuthService --> UserService[用户服务]
    AuthService --> RedisService[Redis缓存服务]
    UserService --> UserMapper[用户数据访问层]
    UserMapper --> Database[(数据库)]
    RedisService --> Redis[(Redis缓存)]
```

## 2. 分层设计和核心组件

### 2.1 控制器层 (Controller)
- **PasswordResetController**：处理密码重置相关的HTTP请求
  - 接收用户忘记密码请求
  - 验证重置验证码
  - 处理密码重置请求

### 2.2 服务层 (Service)
- **AuthService**：提供密码重置相关的业务逻辑
  - 生成和发送重置验证码
  - 验证重置验证码
  - 通过验证码重置密码
- **UserService**：提供用户相关的服务
  - 根据用户名/邮箱/手机号查找用户
  - 更新用户密码
- **RedisService**：提供缓存服务
  - 存储和获取验证码
  - 设置缓存过期时间
  - 管理发送频率和次数限制

### 2.3 数据访问层 (Mapper)
- **UserMapper**：用户数据访问接口

### 2.4 数据传输对象 (DTO)
- **ForgotPasswordRequest**：忘记密码请求DTO
- **ResetPasswordByCodeRequest**：通过验证码重置密码请求DTO
- **PasswordResetRequest**：密码重置请求DTO（已有）

### 2.5 配置类
- **PasswordResetCacheConfig**：密码重置缓存配置类

## 3. 模块依赖关系图

```mermaid
graph TD
    PasswordResetController --> AuthService
    AuthService --> UserService
    AuthService --> RedisService
    AuthService --> PasswordResetCacheConfig
    UserService --> UserMapper
```

## 4. 接口契约定义

### 4.1 REST API接口

| 接口路径 | 方法 | 描述 | 请求体 | 成功响应 |
|---------|------|------|--------|----------|
| `/v1/api/auth/forgot-password` | POST | 请求忘记密码（发送验证码） | `{"usernameOrEmail": "..."}` | `{"code": 200, "message": "验证码发送成功", "data": null}` |
| `/v1/api/auth/validate-reset-code` | POST | 验证重置验证码 | `{"resetCode": "...", "usernameOrEmail": "..."}` | `{"code": 200, "message": "验证码验证成功", "data": null}` |
| `/v1/api/auth/reset-password` | POST | 通过验证码重置密码 | `{"resetCode": "...", "newPassword": "...", "confirmPassword": "..."}` | `{"code": 200, "message": "密码重置成功", "data": null}` |

### 4.2 服务层接口

```java
public interface AuthService {
    // 发送重置密码验证码
    Result sendResetPasswordCode(String usernameOrEmail);
    
    // 验证重置密码验证码
    Result validateResetPasswordCode(String resetCode, String usernameOrEmail);
    
    // 通过验证码重置密码
    Result resetPasswordByCode(String resetCode, String newPassword, String confirmPassword);
}
```

## 5. 数据流向图

```mermaid
sequenceDiagram
    participant Client as 前端
    participant Controller as 密码重置控制器
    participant AuthService as 认证服务
    participant UserService as 用户服务
    participant RedisService as Redis服务
    participant DB as 数据库
    participant Redis as Redis缓存

    %% 忘记密码流程
    Client->>Controller: POST /v1/api/auth/forgot-password
    Controller->>AuthService: sendResetPasswordCode(usernameOrEmail)
    AuthService->>UserService: 根据用户名/邮箱/手机号查找用户
    UserService->>DB: 查询用户信息
    DB-->>UserService: 返回用户信息
    UserService-->>AuthService: 返回用户信息
    AuthService->>RedisService: 检查发送频率和次数限制
    RedisService->>Redis: 查询发送记录
    Redis-->>RedisService: 返回发送记录
    RedisService-->>AuthService: 返回限制检查结果
    AuthService->>AuthService: 生成6位验证码
    AuthService->>RedisService: 存储验证码(15分钟过期)
    RedisService->>Redis: 存储验证码
    Redis-->>RedisService: 存储成功
    RedisService->>Redis: 更新发送记录
    Redis-->>RedisService: 更新成功
    AuthService-->>Controller: 返回成功响应
    Controller-->>Client: 返回成功响应

    %% 验证验证码流程
    Client->>Controller: POST /v1/api/auth/validate-reset-code
    Controller->>AuthService: validateResetPasswordCode(resetCode, usernameOrEmail)
    AuthService->>RedisService: 获取存储的验证码
    RedisService->>Redis: 查询验证码
    Redis-->>RedisService: 返回验证码
    RedisService-->>AuthService: 返回验证码
    AuthService->>AuthService: 验证验证码
    AuthService-->>Controller: 返回验证结果
    Controller-->>Client: 返回验证结果

    %% 重置密码流程
    Client->>Controller: POST /v1/api/auth/reset-password
    Controller->>AuthService: resetPasswordByCode(resetCode, newPassword, confirmPassword)
    AuthService->>RedisService: 验证验证码
    RedisService->>Redis: 查询验证码
    Redis-->>RedisService: 返回验证码
    RedisService-->>AuthService: 返回验证码
    AuthService->>AuthService: 验证密码一致性
    AuthService->>UserService: 更新用户密码
    UserService->>DB: 更新密码
    DB-->>UserService: 更新成功
    UserService-->>AuthService: 更新成功
    AuthService->>RedisService: 删除验证码
    RedisService->>Redis: 删除验证码
    Redis-->>RedisService: 删除成功
    AuthService-->>Controller: 返回重置结果
    Controller-->>Client: 返回重置结果
```

## 6. 异常处理策略

### 6.1 常见异常情况
- 用户不存在：返回错误码和提示信息
- 验证码错误：返回错误码和提示信息
- 验证码过期：返回错误码和提示信息
- 发送频率过高：返回错误码和提示信息
- 发送次数超限：返回错误码和提示信息
- 密码不一致：返回错误码和提示信息
- 密码格式不符合要求：返回错误码和提示信息

### 6.2 异常处理机制
- 使用全局异常处理器捕获和处理异常
- 返回统一格式的错误响应
- 记录详细的错误日志
- 提供友好的错误提示信息