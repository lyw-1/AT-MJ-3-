# 密码重置功能共识文档

## 明确的需求描述
实现完整的密码重置功能，包括以下核心功能点：
1. **忘记密码请求**：用户通过用户名/邮箱/手机号请求重置密码
2. **发送重置验证码**：系统生成并发送6位数字验证码
3. **验证码验证**：验证用户输入的重置验证码是否有效
4. **密码重置**：验证成功后允许用户设置新密码

## 验收标准
1. 用户可以通过用户名、邮箱或手机号请求密码重置
2. 系统成功生成6位数字验证码并存储到Redis
3. 验证功能可以正确验证Redis中的验证码
4. 验证成功后可以成功更新用户密码
5. 实现发送频率限制（每分钟1次）和24小时内发送次数限制（10次）
6. 验证码有效期为15分钟
7. 所有API接口返回统一格式的响应
8. 异常情况有明确的错误提示

## 技术实现方案
1. **数据传输对象**：
   - `ForgotPasswordRequest`：包含usernameOrEmail字段
   - `ResetPasswordByCodeRequest`：包含resetCode、newPassword、confirmPassword字段

2. **服务层**：
   - 在`AuthService`接口中添加密码重置相关方法
   - 在`AuthServiceImpl`中实现具体业务逻辑
   - 使用`RedisService`进行验证码缓存管理

3. **控制器**：
   - 创建`PasswordResetController`处理密码重置相关请求
   - 实现RESTful API接口

4. **缓存设计**：
   - 创建`PasswordResetCacheConfig`配置类管理缓存键前缀和过期时间
   - 使用Redis存储验证码、发送时间和发送次数

## 技术约束
1. 使用Spring Boot 2.7框架
2. 遵循RESTful API设计规范
3. 使用统一的响应格式和状态码
4. 参数校验使用Spring Validation
5. 缓存使用Redis + Spring Cache

## 集成方案
1. 与现有的用户实体和服务集成
2. 与现有的Redis缓存服务集成
3. 复用现有的统一响应和异常处理机制