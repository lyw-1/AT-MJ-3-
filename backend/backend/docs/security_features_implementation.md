# 安全功能实现文档

## 1. 功能概述

本次实现了四个核心安全功能：
- **操作日志审计系统**：记录用户操作行为，支持查询、管理
- **用户锁定机制**：基于登录失败次数自动锁定用户
- **密码重置功能**：管理员可重置用户密码
- **权限缓存刷新机制**：支持实时更新用户权限缓存

## 2. 数据库变更

### 2.1 新增操作日志表 `operation_log`

```sql
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '操作用户名',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_desc` varchar(200) DEFAULT NULL COMMENT '操作描述',
  `operation_content` text COMMENT '操作内容',
  `result` tinyint DEFAULT 0 COMMENT '操作结果(0:失败 1:成功)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_create_time` (`create_time`),
  INDEX `idx_operation_type` (`operation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
```

### 2.2 用户表 `user` 新增字段

```sql
ALTER TABLE `user` ADD COLUMN `login_failed_count` int DEFAULT 0 COMMENT '登录失败次数';
ALTER TABLE `user` ADD COLUMN `locked_until` datetime DEFAULT NULL COMMENT '锁定截止时间';
ALTER TABLE `user` ADD COLUMN `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP';
ALTER TABLE `user` ADD COLUMN `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间';
```

## 3. 核心功能实现

### 3.1 操作日志审计系统

#### 主要类
- `OperationLog`：操作日志实体类
- `OperationLogMapper`：操作日志数据访问接口
- `OperationLogService`：操作日志服务接口
- `OperationLogServiceImpl`：操作日志服务实现
- `OperationLogController`：操作日志控制器
- `OperationLogAspect`：操作日志AOP切面，自动记录操作

#### 功能特性
- 自动记录Controller层方法调用
- 支持异步保存操作日志，不影响主业务流程
- 记录用户名、IP地址、操作类型、操作内容、操作结果等信息
- 提供分页查询、批量删除、清空日志等管理功能

### 3.2 用户锁定机制

#### 实现原理
- 用户连续登录失败达到阈值后自动锁定账号
- 锁定时间可配置（默认15分钟）
- 锁定期间禁止登录
- 登录成功后自动重置失败次数

#### 核心流程
1. 用户登录时检查锁定状态
2. 登录失败则增加失败计数
3. 失败次数达到阈值则锁定账号
4. 登录成功则重置失败计数并记录登录信息

### 3.3 密码重置功能

#### 功能描述
- 管理员可重置指定用户的密码
- 支持用户账号解锁操作
- 密码重置后需要重新登录

#### API接口
- `POST /api/v1/admin/auth/reset-password`：重置用户密码
- `POST /api/v1/admin/auth/user-lock`：锁定/解锁用户账号

### 3.4 权限缓存刷新机制

#### 实现方案
- 使用Redis存储用户权限信息
- 提供灵活的缓存刷新策略
- 支持单用户和全用户权限缓存刷新
- 异步执行大批量操作，避免阻塞

#### 主要类
- `PermissionCacheService`：权限缓存服务接口
- `PermissionCacheServiceImpl`：权限缓存服务实现
- `PermissionCacheController`：权限缓存管理控制器

#### API接口
- `POST /api/v1/admin/permissions/cache/refresh/user/{userId}`：刷新指定用户权限缓存
- `POST /api/v1/admin/permissions/cache/refresh/all`：刷新所有用户权限缓存
- `DELETE /api/v1/admin/permissions/cache/clear/user/{userId}`：清除指定用户权限缓存
- `DELETE /api/v1/admin/permissions/cache/clear/all`：清除所有用户权限缓存

## 4. 安全最佳实践

1. **密码安全**：使用BCrypt加密存储密码
2. **权限控制**：敏感操作需要ADMIN或SUPER_ADMIN角色
3. **输入验证**：所有接口参数都进行严格验证
4. **异常处理**：统一的异常处理机制，避免暴露敏感信息
5. **日志记录**：详细记录操作日志，便于审计和故障排查
6. **缓存安全**：定期刷新权限缓存，确保权限变更及时生效

## 5. 使用说明

### 5.1 操作日志查询
- 访问 `/api/v1/admin/operation-logs` 可查询操作日志
- 支持按用户名、操作类型、时间范围等条件过滤

### 5.2 密码重置
- 管理员可通过 `/api/v1/admin/auth/reset-password` 接口重置用户密码
- 请求参数需包含用户ID、新密码和确认密码

### 5.3 用户锁定管理
- 管理员可通过 `/api/v1/admin/auth/user-lock` 接口手动锁定/解锁用户
- 系统会自动根据登录失败次数锁定用户

### 5.4 权限缓存管理
- 权限变更后，应调用缓存刷新接口使变更生效
- 批量权限变更推荐使用全用户刷新接口（异步执行）

## 6. 注意事项

1. 生产环境中应定期清理操作日志，避免数据量过大
2. 锁定时间和失败次数阈值可根据安全需求调整
3. 权限缓存刷新可能影响系统性能，大批量操作应在低峰期执行
4. 数据库备份应包含操作日志表，以便审计追溯

## 7. 测试覆盖

- 单元测试：覆盖权限缓存服务的核心方法
- 集成测试：验证安全功能的交互和流程正确性
- 推荐在上线前进行完整的安全测试和性能测试