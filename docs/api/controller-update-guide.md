# Controller 更新指南

## 快速参考：如何更新现有Controller

### 1. 修改路径注解

**旧代码：**
```java
@RestController
@RequestMapping({"/v1/api/auth", "/auth", "/api/auth"})
public class AuthController {
```

**新代码：**
```java
@RestController
@RequestMapping("/api/v1/auth")  // 只保留v1版本
public class AuthController {
```

### 2. 添加废弃注解

为需要废弃的Controller添加 `@Deprecated` 注解：

```java
@RestController
@RequestMapping("/api/v1/auth")
@Deprecated  // 标记为废弃
@Api(tags = "认证管理 - 已废弃，请使用新的认证接口")
public class AuthController {
```

### 3. 遵循RESTful规范

#### GET (查询)
```java
// 获取列表
@GetMapping("/users")
// 获取单个
@GetMapping("/users/{id}")
```

#### POST (创建)
```java
// 创建资源
@PostMapping("/users")
```

#### PUT (更新)
```java
// 全量更新
@PutMapping("/users/{id}")
// 部分更新
@PatchMapping("/users/{id}")
```

#### DELETE (删除)
```java
@DeleteMapping("/users/{id}")
```

### 4. 模块命名规范

| 模块 | 路径 | 示例 |
|------|------|------|
| 认证 | `/api/v1/auth` | `/api/v1/auth/login` |
| 用户 | `/api/v1/users` | `/api/v1/users/{id}` |
| 模具 | `/api/v1/molds` | `/api/v1/molds/archives` |
| 任务 | `/api/v1/tasks` | `/api/v1/tasks/{taskNumber}` |
| 记录 | `/api/v1/records` | `/api/v1/records/{id}` |

### 5. 实际更新步骤

#### Step 1: 备份现有代码
```bash
git commit -m "backup: existing controllers before versioning update"
```

#### Step 2: 逐个更新Controller
```bash
# 例如更新AuthController
vim backend/backend/src/main/java/com/mold/digitalization/controller/AuthController.java
```

#### Step 3: 测试验证
```bash
# 运行测试确保兼容性
mvn test
```

#### Step 4: 更新Swagger文档
确保新的路径在API文档中正确显示

### 6. 验证清单

更新后请检查：
- [ ] API路径符合 `/api/v1/{module}/{action}` 格式
- [ ] Swagger文档正确显示新路径
- [ ] 旧路径在过渡期仍可访问
- [ ] 单元测试覆盖新增接口
- [ ] 错误码文档更新

### 7. 常见问题解决

#### 问题1: 路径冲突
**症状**: 多个Controller使用相同路径
**解决**: 统一模块命名，避免重复

#### 问题2: 向后兼容失败
**症状**: 旧客户端无法访问API
**解决**: 保留旧路径至少3个月

#### 问题3: Swagger文档混乱
**症状**: 多个版本的API混在一起
**解决**: 使用Swagger分组功能区分版本
