# 权限缓存刷新机制使用指南

## 概述

权限缓存刷新机制是模具加工流程数字化系统中的核心组件，用于提高权限验证的性能和响应速度。本文档介绍了如何使用和管理权限缓存刷新机制。

## 功能特性

- **自动缓存刷新**：当用户角色、角色权限或角色状态发生变化时，自动刷新相关用户的权限缓存
- **手动缓存管理**：提供REST API接口，支持手动刷新或清除权限缓存
- **异步处理**：使用线程池异步处理缓存刷新操作，避免阻塞主线程
- **事件驱动**：基于Spring事件机制，实现松耦合的缓存刷新策略

## 核心组件

### 1. PermissionCacheService

权限缓存服务，提供权限缓存的刷新和清除功能。

#### 主要方法

- `refreshUserPermissions(Long userId)`: 刷新指定用户的权限缓存
- `refreshAllUsersPermissions()`: 刷新所有用户的权限缓存
- `refreshUsersByRoleId(Long roleId)`: 刷新指定角色关联的所有用户的权限缓存
- `clearUserPermissions(Long userId)`: 清除指定用户的权限缓存
- `clearAllUsersPermissions()`: 清除所有用户的权限缓存

### 2. PermissionCacheController

权限缓存控制器，提供REST API接口管理权限缓存。

#### API接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/permission-cache/refresh/user/{userId} | 刷新指定用户的权限缓存 |
| POST | /api/permission-cache/refresh/all | 刷新所有用户的权限缓存 |
| POST | /api/permission-cache/refresh/role/{roleId} | 刷新指定角色关联用户的权限缓存 |
| DELETE | /api/permission-cache/clear/user/{userId} | 清除指定用户的权限缓存 |
| DELETE | /api/permission-cache/clear/all | 清除所有用户的权限缓存 |

### 3. PermissionChangeListener

权限变更事件监听器，监听权限相关事件并自动刷新缓存。

#### 监听的事件

- `UserRoleChangeEvent`: 用户角色变更事件
- `RolePermissionChangeEvent`: 角色权限变更事件
- `RoleStatusChangeEvent`: 角色状态变更事件

## 使用示例

### 1. 手动刷新用户权限缓存

```bash
# 刷新用户ID为1的权限缓存
curl -X POST "http://localhost:8080/api/permission-cache/refresh/user/1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

响应示例：
```json
{
  "success": true,
  "message": "用户权限缓存刷新成功",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 2. 手动刷新所有用户权限缓存

```bash
# 刷新所有用户的权限缓存
curl -X POST "http://localhost:8080/api/permission-cache/refresh/all" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

响应示例：
```json
{
  "success": true,
  "message": "所有用户权限缓存刷新任务已启动",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 3. 按角色刷新用户权限缓存

```bash
# 刷新角色ID为1关联的所有用户的权限缓存
curl -X POST "http://localhost:8080/api/permission-cache/refresh/role/1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

响应示例：
```json
{
  "success": true,
  "message": "角色关联用户权限缓存刷新成功",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 4. 清除用户权限缓存

```bash
# 清除用户ID为1的权限缓存
curl -X DELETE "http://localhost:8080/api/permission-cache/clear/user/1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

响应示例：
```json
{
  "success": true,
  "message": "用户权限缓存清除成功",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 5. 清除所有用户权限缓存

```bash
# 清除所有用户的权限缓存
curl -X DELETE "http://localhost:8080/api/permission-cache/clear/all" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

响应示例：
```json
{
  "success": true,
  "message": "所有用户权限缓存清除成功",
  "timestamp": "2023-12-01T10:30:00"
}
```

## 代码中使用

### 1. 直接调用服务方法

```java
@Autowired
private PermissionCacheService permissionCacheService;

// 刷新指定用户的权限缓存
permissionCacheService.refreshUserPermissions(1L);

// 刷新所有用户的权限缓存
permissionCacheService.refreshAllUsersPermissions();

// 按角色刷新用户权限缓存
permissionCacheService.refreshUsersByRoleId(1L);

// 清除指定用户的权限缓存
permissionCacheService.clearUserPermissions(1L);

// 清除所有用户的权限缓存
permissionCacheService.clearAllUsersPermissions();
```

### 2. 发布事件触发缓存刷新

```java
@Autowired
private ApplicationEventPublisher eventPublisher;

// 发布用户角色变更事件
UserRoleChangeEvent userRoleChangeEvent = new UserRoleChangeEvent(
    this, 1L, 1L, UserRoleChangeEvent.OperationType.ASSIGN);
eventPublisher.publishEvent(userRoleChangeEvent);

// 发布角色权限变更事件
RolePermissionChangeEvent rolePermissionChangeEvent = new RolePermissionChangeEvent(
    this, 1L, Arrays.asList(1L, 2L));
eventPublisher.publishEvent(rolePermissionChangeEvent);

// 发布角色状态变更事件
RoleStatusChangeEvent roleStatusChangeEvent = new RoleStatusChangeEvent(
    this, 1L, "INACTIVE", "ACTIVE");
eventPublisher.publishEvent(roleStatusChangeEvent);
```

## 配置说明

### 异步线程池配置

权限缓存刷新机制使用专门的线程池进行异步处理，配置如下：

```java
@Bean("permissionChangeExecutor")
public Executor permissionChangeExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5); // 核心线程数
    executor.setMaxPoolSize(10); // 最大线程数
    executor.setQueueCapacity(100); // 队列容量
    executor.setThreadNamePrefix("permission-change-"); // 线程名称前缀
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
    executor.initialize();
    return executor;
}
```

### Redis缓存配置

权限缓存使用Redis存储，配置如下：

```properties
# Redis配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0

# 权限缓存配置
permission.cache.key.prefix=user:permissions:
permission.cache.expire.seconds=3600
```

## 最佳实践

1. **合理使用缓存刷新**：
   - 在权限变更后及时刷新缓存，确保权限变更立即生效
   - 避免频繁刷新所有用户缓存，可考虑按需刷新特定用户或角色

2. **监控缓存性能**：
   - 定期检查缓存命中率，确保缓存有效性
   - 监控缓存刷新操作的性能，避免影响系统响应

3. **异常处理**：
   - 缓存刷新失败时，记录错误日志并尝试恢复
   - 考虑实现缓存刷新的重试机制

4. **安全考虑**：
   - 限制权限缓存管理接口的访问权限，仅允许管理员操作
   - 记录所有缓存刷新操作的审计日志

## 故障排查

### 1. 权限缓存未刷新

**可能原因**：
- 事件监听器未正确注册
- 异步线程池配置错误
- Redis连接异常

**排查步骤**：
1. 检查事件监听器是否正确注册
2. 检查异步线程池配置是否正确
3. 检查Redis连接是否正常
4. 查看应用日志，确认是否有错误信息

### 2. 缓存刷新性能问题

**可能原因**：
- 用户数量过多，刷新操作耗时较长
- 数据库查询性能问题
- Redis操作性能问题

**排查步骤**：
1. 分析缓存刷新操作的性能瓶颈
2. 优化数据库查询，提高查询效率
3. 检查Redis性能，考虑增加Redis实例或优化配置
4. 考虑分批刷新大量用户的权限缓存

## 总结

权限缓存刷新机制是提高系统性能的重要组件，通过合理使用和管理权限缓存，可以显著提升系统的响应速度和用户体验。在实际使用中，需要根据业务需求和系统负载，合理配置缓存策略和刷新机制。