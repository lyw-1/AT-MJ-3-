# API 版本控制策略

## 当前状态
项目当前支持多种API路径格式：
- `/api/auth` (无版本)
- `/v1/api/auth` (v1版本)
- `/auth` (简化路径)

## 目标状态
统一使用标准的版本控制格式：`/api/v{version}/{module}/{resource}`

## 版本控制规则

### 1. 版本号格式
- 使用语义化版本号：`v1`, `v2`, `v3`
- 递增版本号表示重大变更
- 保持向后兼容至少一个版本

### 2. 路径规范
```
/api/v1/auth/login      # 认证模块
/api/v1/users/{id}      # 用户管理
/api/v1/molds/archives  # 模具管理
/api/v1/tasks/create    # 任务管理
```

### 3. 迁移策略

#### Phase 1: 统一新接口 (本周)
- 新开发的接口直接使用 `/api/v1/` 前缀
- 保持旧接口 `/api/` 和 `/auth` 的兼容

#### Phase 2: 标记废弃接口 (本月)
- 在旧接口的Swagger文档中添加 `@Deprecated` 注解
- 在响应头中添加 `Warning` 头
- 更新API文档说明迁移计划

#### Phase 3: 移除旧接口 (下月)
- 发布v2版本前移除api/` 和所有 `/ `/auth` 路径
- 保留至少3个月的过渡期

### 4. Controller 示例

```java
@RestController
@RequestMapping("/api/v1/auth")  // 统一使用v1版本
@Api(tags = "认证管理")
@Deprecated  // 标记将被废弃，使用新的AuthControllerV2
public class AuthController {
    // ... 现有代码
}
```

### 5. 版本兼容性策略

#### 向前兼容
- 新增字段可选
- 不删除现有字段
- 不改变字段类型

#### 向后兼容
- 旧版本API保持可用
- 提供版本检测中间件
- 渐进式迁移文档

### 6. 实施检查清单

- [x] 制定版本控制策略
- [ ] 更新所有新Controller使用v1版本
- [ ] 在Swagger文档中明确版本信息
- [ ] 创建API版本迁移指南
- [ ] 更新前端调用代码
- [ ] 设置废弃接口的Warning头
- [ ] 制定v2版本的发布时间表

## 快速行动

对于新开发的模块，请直接使用：
```java
@RestController
@RequestMapping("/api/v1/{module-name}")
public class NewModuleController {
    // 控制器代码
}
```

## 常见问题

### Q: 何时需要升级版本？
A: 当发生以下情况时：
- 改变API响应结构
- 删除或重命名字段
- 改变认证方式
- 重大功能变更

### Q: 如何处理紧急bug修复？
A: 在当前版本内修复，如：
- `/api/v1.1/auth/login` (补丁版本)
- 或直接修复v1版本
