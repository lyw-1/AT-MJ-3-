# 数据库初始化指南

## 概述

本文档提供模具数字化系统生产环境数据库的初始化说明。系统使用MySQL 8.0+数据库，支持utf8mb4字符集。

## 数据库初始化步骤

### 1. 环境准备

确保已安装以下环境：
- MySQL 8.0+ 数据库服务
- 数据库连接权限（创建数据库、表、索引等）

### 2. 执行初始化脚本

#### 方法一：使用MySQL命令行工具

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source d:/Trae/AT-MJ-3/backend/src/main/resources/db/init.sql
```

#### 方法二：使用MySQL Workbench

1. 打开MySQL Workbench
2. 连接到目标数据库服务器
3. 打开文件：`d:/Trae/AT-MJ-3/backend/src/main/resources/db/init.sql`
4. 执行整个脚本

#### 方法三：使用命令行批量执行

```bash
mysql -u root -p < d:/Trae/AT-MJ-3/backend/src/main/resources/db/init.sql
```

### 3. 验证初始化结果

执行以下SQL验证数据库和表是否创建成功：

```sql
-- 验证数据库
SHOW DATABASES LIKE 'mold_digitalization';

-- 验证表结构
USE mold_digitalization;
SHOW TABLES;

-- 验证默认数据
SELECT * FROM role;
SELECT * FROM permission;
SELECT * FROM user;
```

## 数据库结构说明

### 核心表结构

1. **用户权限模块**
   - `user` - 用户表
   - `role` - 角色表  
   - `permission` - 权限表
   - `user_role` - 用户角色关联表
   - `role_permission` - 角色权限关联表

2. **业务模块**
   - `mold` - 模具表
   - `equipment` - 设备表
   - `production_task` - 生产任务表
   - `production_record` - 生产记录表

3. **系统模块**
   - `operation_log` - 操作日志表

### 默认数据

初始化脚本会自动插入以下默认数据：

#### 角色数据
- SUPER_ADMIN (超级管理员)
- ADMIN (管理员)
- OPERATOR (操作员)
- QUALITY_INSPECTOR (质检员)

#### 权限数据
- 用户管理、角色管理、权限管理等9个基础权限

#### 用户数据
- 默认超级管理员账号：admin（密码由初始化脚本或运维在部署阶段设置；严禁使用固定示例密码。首次登录后必须立即修改密码并开启强密码策略）

## 配置说明

### 应用配置

在 `application.properties` 中配置数据库连接：

```properties
# 数据库连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/mold_digitalization?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password

# 数据库连接池配置
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.max-lifetime=1200000
```

### 字符集配置

数据库使用 `utf8mb4` 字符集，支持完整的Unicode字符（包括emoji表情）。

## 维护指南

### 数据库备份

```bash
# 备份数据库
mysqldump -u root -p mold_digitalization > mold_digitalization_backup_$(date +%Y%m%d).sql

# 恢复数据库
mysql -u root -p mold_digitalization < mold_digitalization_backup.sql
```

### 性能优化

1. **定期清理操作日志**
   ```sql
   -- 清理30天前的操作日志
   DELETE FROM operation_log WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
   ```

2. **定期优化表**
   ```sql
   OPTIMIZE TABLE operation_log;
   ```

### 安全建议

1. 修改默认管理员密码
2. 定期备份数据库
3. 限制数据库访问IP
4. 启用SSL连接

## 故障排除

### 常见问题

1. **字符集问题**
   - 确保MySQL配置支持utf8mb4
   - 检查连接字符串中的字符集参数

2. **权限问题**
   - 确保数据库用户有创建数据库和表的权限
   - 检查防火墙设置

3. **外键约束问题**
   - 确保表创建顺序正确
   - 检查外键引用的表是否存在

## 联系支持

如遇到问题，请联系系统管理员或查看项目文档。
### 通过 Flyway 进行增量变更（推荐）

1. 在 `backend/src/main/resources/db/migration/` 目录新增脚本，命名为 `V<version>__<description>.sql`
2. 开发环境可在 `application-dev.properties` 启用：
   ```properties
   spring.flyway.enabled=true
   spring.flyway.locations=classpath:db/migration
   spring.flyway.baseline-on-migrate=true
   spring.flyway.out-of-order=true
   ```
3. 首次接入已有库时建议 baseline，以当前结构作为基线；生产库变更需审批与备份
