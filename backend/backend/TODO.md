# 模具加工流程数字化系统 - 后续工作

## 环境准备

- [x] 安装JDK 1.8或更高版本（已成功配置JDK 17.0.16，当前会话中有效）
- [x] 安装Maven 3.6或更高版本 (已安装3.6.3)
- [x] 安装MySQL 5.7或更高版本 (已准备完整安装文档，需要手动执行)
- [x] 安装Redis 5.0或更高版本 (Redis文件已存在于D:\Redis目录，但服务未安装。可通过运行redis-server.exe手动启动)
- [x] 安装RabbitMQ 3.8或更高版本 (已安装3.12.0并配置完成)

## 数据库设置


- [ ] 创建MySQL数据库：`CREATE DATABASE mold_digitalization CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
  > 提示：使用MySQL命令行或MySQL Workbench执行此命令
- [ ] 导入数据库表结构（需要创建SQL脚本）
- [ ] 添加初始测试数据

## 配置调整

- [ ] 修改`application.properties`中的数据库连接信息
- [ ] 修改JWT密钥为强随机密钥
- [ ] 根据实际环境调整Redis和RabbitMQ配置

## 项目构建与部署

- [ ] 使用Maven构建项目：`mvn clean package`
- [ ] 运行单元测试：`mvn test`
- [ ] 部署应用程序：`java -jar target/mold-digitalization.jar`

## 功能完善

- [ ] 实现文件上传功能（模具图纸等）
- [ ] 添加任务提醒和通知功能
- [ ] 完善数据统计和报表功能
- [ ] 实现多用户角色的权限控制
- [ ] 添加日志审计功能

## 接口测试

- [ ] 使用Postman或Swagger测试所有API接口
- [ ] 进行压力测试和性能优化
- [ ] 进行安全测试

## 文档完善

- [ ] 编写详细的API文档
- [ ] 编写系统运维文档
- [ ] 编写用户操作手册

## 注意事项

1. 生产环境中，请务必修改`application.properties`中的`jwt.secret`为强随机密钥
2. 定期备份数据库，确保数据安全
3. 根据实际需求调整Redis和RabbitMQ的配置
4. 考虑使用HTTPS协议保护API通信
5. 考虑添加接口限流和熔断机制