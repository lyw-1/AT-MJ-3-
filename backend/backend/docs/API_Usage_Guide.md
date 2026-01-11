# API文档使用指南

## 概述

本项目的API文档使用Swagger 2生成，提供了完整的RESTful API接口文档和在线测试功能。

## 访问方式

### 本地开发环境

1. 启动后端应用程序
2. 在浏览器中访问以下URL：
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API文档JSON: http://localhost:8080/v2/api-docs

### 生产环境

根据实际部署的服务器地址和端口访问相应的URL。

## 文档结构

API文档按照功能模块进行组织，主要包括以下几个部分：

1. **流程管理 API** - 管理模具加工流程的创建、查询、更新和删除等操作
2. **状态机 API** - 管理流程状态的转换和查询
3. **进度跟踪 API** - 跟踪和更新流程进度信息
4. **异常处理 API** - 管理流程中的异常情况
5. **统计分析 API** - 提供各种统计和分析数据

## 使用方法

### 查看API详情

1. 在Swagger UI页面中，点击相应的API模块展开接口列表
2. 点击具体的接口名称，查看接口的详细信息
3. 接口详情包括：
   - 请求方法（GET/POST/PUT/DELETE）
   - 请求URL
   - 请求参数
   - 响应示例
   - 错误码说明

### 在线测试API

1. 在接口详情页面中，点击"Try it out"按钮
2. 填写必要的请求参数
3. 点击"Execute"按钮发送请求
4. 查看响应结果

### 认证

如果API需要认证，请按照以下步骤操作：

1. 在Swagger UI页面顶部，点击"Authorize"按钮
2. 在弹出的对话框中，输入认证信息
3. 点击"Authorize"按钮完成认证

## 数据模型

API文档中包含了所有数据模型的详细说明，包括：

- 字段名称
- 数据类型
- 是否必填
- 字段说明

## 错误处理

API错误响应遵循统一的格式：

```json
{
  "code": 400,
  "message": "请求参数错误",
  "error": "详细错误信息",
  "timestamp": "2023-12-01T10:30:00"
}
```

常见错误码：

- 200: 请求成功
- 400: 请求参数错误
- 401: 未授权访问
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器内部错误

## 常见问题

### 1. 如何获取完整的API文档？

访问 http://localhost:8080/v2/api-docs 可以获取完整的API文档JSON数据。

### 2. 如何在代码中集成API？

可以使用以下工具根据Swagger文档生成客户端代码：

- Java: swagger-codegen
- JavaScript: swagger-js
- Python: swagger-codegen

### 3. 如何获取实时API更新？

API文档会随着代码更新自动更新，重启应用程序后即可看到最新的文档。

## 联系方式

如有任何关于API文档的问题，请联系开发团队：

- 邮箱: dev@atmj.com
- 电话: 400-123-4567

---

## Postman集合一键联调与自动登录（新增）

为提升联调效率，项目提供了 Postman 集合：`backend/docs/API_Test_Collection.postman_collection.json`，并在集合根级内置了“预请求脚本”，可自动完成管理员与普通用户的登录并写入令牌。

使用步骤：
- 在 Postman 中导入上述集合。
- 在集合的变量或环境变量中配置以下字段（留空则不自动登录）：
  - adminUsername / adminPassword：管理员登录凭据。
  - userUsername / userPassword：普通用户登录凭据。
- 运行集合中的各请求：
  - 若集合变量 `token` 为空且提供了管理员凭据，将自动调用 `{{baseUrl}}/admin/login`，从响应中解析 `data.accessToken` 并写入集合变量与环境变量 `token`。
  - 若集合变量 `userToken` 为空且提供了普通用户凭据，将自动调用 `{{baseUrl}}/api/auth/login`，解析令牌并写入集合变量与环境变量 `userToken`。

注意事项：
- 集合默认定义了以下变量：`baseUrl`、`token`、`userToken`、`adminUsername`、`adminPassword`、`userUsername`、`userPassword`。
- 为避免意外覆盖，若 `token`/`userToken` 已有值，预请求脚本不会重复登录。
- 若后端的响应令牌字段不是 `data.accessToken`，脚本也会尝试读取顶层 `accessToken` 作为兼容方案。

推荐的流水化执行（Collection Runner）：
- 先在集合变量或环境变量中填好登录凭据。
- 使用 Runner 运行“管理员接口”分组下的请求，可一键验证 401（未携带令牌）、403（普通用户令牌）与 404（管理员令牌 + 不存在用户）等场景。
- 如需跨环境验证，只需切换 `baseUrl` 和对应环境中的登录凭据。

故障定位建议：
- 若自动登录失败，请检查 `baseUrl`、登录接口路径是否可达、以及用户名密码是否正确。
- 可在 Postman 控制台查看集合级脚本的日志（包含请求状态与解析提示）。
