# 开发环境鉴权放行与 DEV BYPASS 使用说明

本说明文档介绍在开发环境下为前端联调与页面结构预览，如何安全地开启“匿名访问流程状态机接口”以及前端登录绕过，同时强调生产环境的禁用要求与安全注意事项。

## 后端：流程状态机接口开发放行（GET-only）

为了方便前端在未登录状态下查看流程数据与页面结构，后端支持通过配置开关允许匿名访问流程状态机的只读接口（GET）。

- 配置文件位置：`backend/src/main/resources/application.properties`
- 开关配置：

```
app.security.dev.bypass.processStateMachine=true
```

当该开关为 `true` 时，Spring Security 将对以下路径进行 GET-only 放行：

- `/api/process-state-machine/**` 的 GET 请求，例如：
  - `GET /api/process-state-machine/{processId}/current`
  - `GET /api/process-state-machine/{processId}/history`
  - `GET /api/process-state-machine/{processId}/available-events`
  - `GET /api/process-state-machine/states`
  - `GET /api/process-state-machine/events`
  - `GET /api/process-state-machine/status-description`
  - `GET /api/process-state-machine/event-description`

对于涉及状态变更的接口（如 `POST /api/process-state-machine/{processId}/transition`），仍需正常鉴权，未登录将返回 401 未授权。

### 生效步骤
1. 修改 `application.properties` 中的开关值；
2. 重启后端服务：
   - Maven: `mvn -DskipTests spring-boot:run`
3. 验证：
   - 未携带令牌访问 `GET /api/process-state-machine/{id}/current` 应返回 200；
   - 未携带令牌访问 `POST /api/process-state-machine/{id}/transition` 应返回 401。

### 生产环境注意
- 生产环境必须关闭此开关：

```
app.security.dev.bypass.processStateMachine=false
```

- 如果存在多环境配置（如 `application-dev.properties` / `application-prod.properties`），务必确保仅在 `dev` 环境开启，`prod` 环境关闭。

## 前端：开发环境登录绕过（DEV BYPASS）

前端在开发环境下可通过环境变量绕过登录页面与 401 跳转，不影响正常的后端鉴权：

- 文件位置：`frontend/.env.development`
- 环境变量：

```
VITE_DEV_AUTH_BYPASS=true
```

效果：
- 访问受保护页面时不再强制跳转登录；
- 顶部导航显示 “DEV BYPASS” 指示；
- 前端将允许在无令牌时尝试请求后端，如后端放行为 GET-only，则只读接口可访问，变更操作仍返回 401。

关闭方式：将该变量置为 `false` 或删除。

## 安全建议
- DEV BYPASS 仅用于本地开发与联调，请勿在生产环境启用；
- 后端放行采用 GET-only，避免误触发状态变更；
- 若需要在开发环境进行受控的写操作（POST/PUT）联调，建议使用以下更安全方案：
  1. Dev-Token 方案：在安全过滤器链中新增仅在开发开启时有效的固定伪令牌（Header：`X-Dev-Token`），使用该令牌才允许写操作；
  2. 前端伪令牌或 Mock 数据：在开发环境中注入伪令牌或使用 Mock 服务，避免直接匿名执行写操作；

## 常见问题（FAQ）
- 访问返回 401：确认仅访问 GET 接口；POST/PUT 等仍需鉴权。
- 前端预览失败：检查前端 DEV BYPASS 是否开启、后端端口是否监听、CORS 是否允许当前开发端口（5173/5174/5175）。
- 放行范围过大：目前后端已限制为 GET-only；如需进一步收紧，可按具体路径进行更细粒度控制。

---

## 开发环境受控写操作：Dev-Token 使用说明（新增）

为满足开发环境对流程状态机写操作（POST/PUT）的受控联调需求，系统支持 Dev-Token 认证机制，仅对指定接口生效，且仅在开发环境启用。

### 1. 配置项

- 配置文件位置：`backend/src/main/resources/application.properties`
- 开关与令牌值：

```
app.security.dev.token.enabled=true
app.security.dev.token.value=dev-token-123
```

说明：
- `app.security.dev.token.enabled` 为总开关，生产环境必须为 `false`。
- `app.security.dev.token.value` 为固定令牌值，建议在本地根据需要自定义；请勿提交真实生产令牌。

### 2. 适用范围与生效条件

- 仅对非 GET 的 `/api/process-state-machine/**` 请求生效（如 `POST /api/process-state-machine/{processId}/transition`）。
- 请求需携带正确的 HTTP Header：`X-Dev-Token: <配置中的令牌值>`。
- 过滤器链位置：`DevTokenAuthenticationFilter` 在 `JwtAuthenticationFilter` 之前注册，仅当开关启用时起效。
- 成功匹配后，将在 `SecurityContext` 中注入一个 `ROLE_DEV` 的开发身份，用于放行受控写操作；业务校验仍按实际状态机逻辑执行。

### 3. 示例调用

PowerShell：

```
try {
  $resp = Invoke-WebRequest -Uri "http://localhost:8081/api/process-state-machine/1/transition?event=START&operator=dev" -Method POST -Headers @{ "X-Dev-Token" = "dev-token-123" } -ErrorAction Stop
  "StatusCode=$($resp.StatusCode)"
  "Body=$($resp.Content)"
} catch {
  "ErrorStatus=$($_.Exception.Response.StatusCode.value__)"
  $sr = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
  $body = $sr.ReadToEnd()
  "ErrorBody=$body"
}
```

curl：

```
curl -i -X POST "http://localhost:8081/api/process-state-machine/1/transition?event=START&operator=dev" \
  -H "X-Dev-Token: dev-token-123"
```

期望结果：
- 若未携带或令牌不匹配：返回 401 未授权；
- 若令牌正确：通过安全鉴权，随后根据状态机业务规则返回 200/201/204 或 4xx（如：当前状态不允许此事件则可能为 422）。

### 4. 注意事项与最佳实践

- 生产环境必须关闭：

```
app.security.dev.token.enabled=false
```

- 建议将令牌值通过本地配置或环境变量覆盖，避免硬编码或提交敏感值；
- Dev-Token 仅解决“鉴权放行”，不绕过业务校验；若收到 422（非法操作），请检查当前流程状态与事件是否可用；
- 可配合 `GET /api/process-state-machine/{processId}/available-events` 查看当前可用事件，或参考业务文档《模具加工流程引擎》；
- 前端联调时可在 Axios/Fetch 中为特定写接口动态添加 `X-Dev-Token` 头，避免影响全局请求。

## 变更记录
 - 2025-11-12：新增后端开发开关并改为 GET-only 放行，文档首次建立；前端 DEV BYPASS 已可配套使用。
 - 2025-11-12：新增 Dev-Token 受控写操作说明，示例命令与注意事项；生产环境禁用指引明确。
