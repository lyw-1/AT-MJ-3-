# 模具加工流程数字化系统 - 后端

## 项目介绍

本项目是一个模具加工流程数字化系统的后端服务，旨在实现模具从申请到入库的全流程数字化管理，包括模具初始参数管理、模具档案管理、加工任务管理和槽宽记录管理等功能。

## 技术栈

- **框架**：Spring Boot 2.x
- **ORM**：MyBatis-Plus
- **数据库**：MySQL
- **认证**：JWT (JSON Web Token)
- **缓存**：Redis
- **消息队列**：RabbitMQ
- **安全框架**：Spring Security

## 项目结构

```
src/main/java/com/mold/digitalization/
├── config/          # 配置类
├── controller/      # 控制器层
├── dao/             # 数据访问层
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── service/         # 服务层
│   └── impl/        # 服务实现类
└── MoldDigitalizationApplication.java  # 应用程序入口
```

## 主要功能模块

1. **用户认证与授权**
   - 用户登录
   - JWT令牌生成与验证
   - 基于角色的权限控制

2. **模具管理**
   - 模具初始参数管理
   - 模具档案管理
   - 模具状态与位置更新

3. **加工任务管理**
   - 加工任务创建与分配
   - 任务状态跟踪
   - 任务完成与归档

4. **槽宽记录管理**
   - 槽宽数据录入
   - 槽宽趋势分析
   - 统计报表生成

## 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+
- RabbitMQ 3.8+

## 快速开始

### 1. 准备数据库

创建MySQL数据库：

```sql
CREATE DATABASE mold_digitalization CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 修改配置

编辑 `src/main/resources/application.properties` 文件，修改数据库连接信息和其他配置项。

### 3. 构建项目

```bash
mvn clean package -DskipTests
```

### 4. 运行项目

```bash
java -jar target/mold-digitalization.jar
```

或者直接在IDE中运行 `MoldDigitalizationApplication` 类。

## API 文档入口说明

项目当前使用 Springfox + Knife4j 文档方案，推荐入口与兼容入口如下：

- 主入口（Knife4j）：
  - http://localhost:8080/doc.html
- 兼容入口（自动重定向到文档页面）：
  - http://localhost:8080/swagger-ui/index.html → 重定向到 /doc.html
  - http://localhost:8080/swagger-ui.html → 重定向到 /doc.html
- Swagger JSON：
  - http://localhost:8080/v2/api-docs

说明：
- 项目在 Spring Boot 2.7.x 环境下运行，已设置 `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`，并启用 `knife4j.enable=true` 以保证 Springfox/Knife4j 的兼容性。
- 本项目未使用 Springdoc v2 的 `/swagger-ui/index.html` 静态资源，而是通过重定向提供兼容入口，实际文档页面为 `/doc.html`。

## 接口说明

### 认证接口
- POST `/api/auth/login` - 用户登录
- POST `/api/auth/getUserInfo` - 获取用户信息

### 用户管理接口
- GET `/api/users/list` - 获取用户列表
- GET `/api/users/{id}` - 根据ID获取用户
- POST `/api/users/create` - 创建用户
- PUT `/api/users/update` - 更新用户
- DELETE `/api/users/{id}` - 删除用户
- POST `/api/users/changePassword` - 修改密码

### 模具管理接口
- POST `/api/molds/initialParams/create` - 创建模具初始参数
- GET `/api/molds/initialParams/{moldNumber}` - 根据模具编号获取初始参数
- POST `/api/molds/archives/create` - 创建模具档案
- GET `/api/molds/archives/{moldNumber}` - 根据模具编号获取模具档案
- PUT `/api/molds/archives/update` - 更新模具档案
- PUT `/api/molds/archives/updateStatus` - 更新模具状态
- PUT `/api/molds/archives/updateLocation` - 更新模具位置
- GET `/api/molds/statistics` - 获取模具统计信息

### 加工任务接口
- POST `/api/tasks/create` - 创建加工任务
- GET `/api/tasks/{taskNumber}` - 根据任务编号获取加工任务
- PUT `/api/tasks/update` - 更新加工任务
- PUT `/api/tasks/updateStatus` - 更新任务状态
- PUT `/api/tasks/complete` - 完成加工任务
- GET `/api/tasks/statistics` - 获取任务统计信息

### 槽宽记录接口
- POST `/api/slotWidthRecords/create` - 创建槽宽记录
- GET `/api/slotWidthRecords/{id}` - 根据ID获取槽宽记录
- GET `/api/slotWidthRecords/latest/{moldNumber}` - 获取最新槽宽记录
- PUT `/api/slotWidthRecords/update` - 更新槽宽记录
- GET `/api/slotWidthRecords/statistics` - 获取槽宽记录统计信息
- GET `/api/slotWidthRecords/trend/{moldNumber}` - 获取槽宽趋势数据

## 注意事项

1. 生产环境中，请务必修改 `application.properties` 中的 `jwt.secret` 为强随机密钥
2. 生产环境中，建议使用HTTPS协议保护API通信
3. 定期备份数据库，确保数据安全
4. 根据实际需求调整Redis和RabbitMQ的配置

## 许可证

[MIT](LICENSE)
