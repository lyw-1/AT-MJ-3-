# API 变更日志

## 版本 1.0.0 (2023-12-01)

### 新增功能

#### 流程管理 API
- 创建流程：`POST /api/processes`
- 获取流程详情：`GET /api/processes/{id}`
- 更新流程：`PUT /api/processes/{id}`
- 删除流程：`DELETE /api/processes/{id}`
- 分页查询流程：`GET /api/processes`
- 按模具ID查询流程：`GET /api/processes/mold/{moldId}`
- 按设备ID查询流程：`GET /api/processes/equipment/{equipmentId}`
- 按操作员ID查询流程：`GET /api/processes/operator/{operatorId}`
- 按状态查询流程：`GET /api/processes/status/{status}`
- 按优先级查询流程：`GET /api/processes/priority/{priority}`
- 获取流程统计：`GET /api/processes/statistics`
- 开始流程：`POST /api/processes/{id}/start`
- 暂停流程：`POST /api/processes/{id}/pause`
- 恢复流程：`POST /api/processes/{id}/resume`
- 完成流程：`POST /api/processes/{id}/complete`
- 取消流程：`POST /api/processes/{id}/cancel`

#### 状态机 API
- 获取当前状态：`GET /api/state-machine/current/{processId}`
- 获取可能的状态转换：`GET /api/state-machine/transitions/{processId}`
- 检查状态转换是否有效：`GET /api/state-machine/can-transition`
- 执行状态转换：`POST /api/state-machine/transition`
- 获取状态历史：`GET /api/state-machine/history/{processId}`
- 重置状态机：`POST /api/state-machine/reset/{processId}`
- 获取所有状态枚举：`GET /api/state-machine/states`
- 获取状态描述：`GET /api/state-machine/state-description/{status}`
- 获取状态转换图数据：`GET /api/state-machine/transition-graph`
- 获取状态统计：`GET /api/state-machine/statistics`

#### 进度跟踪 API
- 创建进度记录：`POST /api/progress`
- 获取进度记录详情：`GET /api/progress/{id}`
- 更新进度记录：`PUT /api/progress/{id}`
- 删除进度记录：`DELETE /api/progress/{id}`
- 按流程ID获取进度记录：`GET /api/progress/process/{processId}`
- 按时间范围查询进度记录：`GET /api/progress/time-range`
- 按操作查询进度记录：`GET /api/progress/operation`
- 获取所有进度记录：`GET /api/progress`
- 更新进度步骤：`PUT /api/progress/{id}/step`
- 更新进度百分比：`PUT /api/progress/{id}/percentage`
- 更新当前操作：`PUT /api/progress/{id}/operation`
- 更新时间信息：`PUT /api/progress/{id}/time`
- 获取进度统计：`GET /api/progress/statistics`
- 获取进度图表数据：`GET /api/progress/{id}/chart-data`
- 获取进度历史数据：`GET /api/progress/{id}/history`
- 计算进度：`GET /api/progress/{id}/calculate`

#### 异常处理 API
- 创建异常记录：`POST /api/exceptions`
- 获取异常记录详情：`GET /api/exceptions/{id}`
- 更新异常记录：`PUT /api/exceptions/{id}`
- 删除异常记录：`DELETE /api/exceptions/{id}`
- 按流程ID获取异常记录：`GET /api/exceptions/process/{processId}`
- 按异常类型获取异常记录：`GET /api/exceptions/type/{exceptionType}`
- 按异常状态获取异常记录：`GET /api/exceptions/status/{exceptionStatus}`
- 按异常级别获取异常记录：`GET /api/exceptions/level/{exceptionLevel}`
- 按时间范围获取异常记录：`GET /api/exceptions/time-range`
- 获取所有异常记录：`GET /api/exceptions`
- 更新异常状态：`PUT /api/exceptions/{id}/status`
- 处理异常：`PUT /api/exceptions/{id}/handle`
- 获取异常统计：`GET /api/exceptions/statistics`

#### 统计分析 API
- 获取流程统计：`GET /api/statistics/process`
- 获取状态统计：`GET /api/statistics/state`
- 获取进度统计：`GET /api/statistics/progress`
- 获取异常统计：`GET /api/statistics/exception`
- 获取综合统计：`GET /api/statistics/comprehensive`
- 获取时间范围统计：`GET /api/statistics/time-range`
- 获取图表数据：`GET /api/statistics/chart-data`

### 数据模型

#### MoldProcess (流程)
- id: 流程ID
- moldId: 模具ID
- processType: 流程类型
- equipmentId: 设备ID
- operatorId: 操作员ID
- status: 流程状态
- priority: 优先级
- estimatedDuration: 预估持续时间
- actualDuration: 实际持续时间
- notes: 备注
- createdAt: 创建时间
- updatedAt: 更新时间
- createdBy: 创建人
- updatedBy: 更新人

#### ProcessStatusHistory (状态历史)
- id: 记录ID
- processId: 流程ID
- fromStatus: 原状态
- toStatus: 目标状态
- operatorId: 操作员ID
- notes: 备注
- createdAt: 创建时间

#### ProgressTracking (进度跟踪)
- id: 进度ID
- processId: 流程ID
- currentStep: 当前步骤
- totalSteps: 总步骤
- progressPercentage: 进度百分比
- currentOperation: 当前操作
- nextOperation: 下一步操作
- estimatedTimeRemaining: 预估剩余时间
- actualTimeUsed: 实际使用时间
- createdAt: 创建时间
- updatedAt: 更新时间

#### ProcessException (异常)
- id: 异常ID
- processId: 流程ID
- exceptionType: 异常类型
- exceptionLevel: 异常级别
- title: 异常标题
- description: 异常描述
- exceptionStatus: 异常状态
- reportedBy: 报告人
- resolvedBy: 解决人
- resolution: 解决方案
- createdAt: 创建时间
- updatedAt: 更新时间

### 错误码

| 错误码 | 描述 |
|--------|------|
| 200 | 请求成功 |
| 201 | 创建成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 409 | 资源冲突 |
| 500 | 服务器内部错误 |

### 认证方式

使用JWT Token进行认证，请求头中添加：
```
Authorization: Bearer {token}
```

### 版本控制

API版本通过URL路径控制，当前版本为v1：
```
/api/v1/...
```

### 分页

支持分页查询，参数：
- page: 页码，从1开始
- size: 每页大小，默认10
- sort: 排序字段和方向，如"createdAt,desc"

### 过滤

支持多种过滤条件，如按状态、类型、时间范围等。

## 未来计划

### 版本 1.1.0 (计划中)
- 添加批量操作API
- 添加高级搜索API
- 添加数据导出API
- 添加实时通知API

### 版本 1.2.0 (计划中)
- 添加工作流配置API
- 添加权限管理API
- 添加审计日志API
- 添加数据备份API