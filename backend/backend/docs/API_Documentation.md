# 模具加工流程引擎 API 文档

## 概述

模具加工流程引擎是一个基于状态机的流程管理系统，用于管理模具从申请到入库的完整生命周期。系统支持多车间协同作业（模具车间、调试车间、成型车间），提供移动端用户体验（微信小程序），并支持业务流程标准化和异常处理。

## API 基础信息

- **基础URL**: `http://localhost:8080/api`
- **API版本**: v1
- **认证方式**: JWT Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体数据内容
  },
  "timestamp": "2023-12-01T10:30:00"
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "请求参数错误",
  "error": "详细错误信息",
  "timestamp": "2023-12-01T10:30:00"
}
```

## 通用错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 409 | 资源冲突 |
| 500 | 服务器内部错误 |

## API 分类

1. [流程管理 API](#流程管理-api)
2. [状态机 API](#状态机-api)
3. [进度跟踪 API](#进度跟踪-api)
4. [异常处理 API](#异常处理-api)
5. [统计分析 API](#统计分析-api)

## 流程管理 API

### 创建流程

**请求**

```http
POST /api/processes
Content-Type: application/json

{
  "moldId": "M001",
  "processType": "CNC",
  "equipmentId": "EQ001",
  "operatorId": "OP001",
  "priority": 1,
  "estimatedDuration": 120,
  "notes": "高精度模具加工"
}
```

**响应**

```json
{
  "code": 200,
  "message": "流程创建成功",
  "data": {
    "id": 1,
    "moldId": "M001",
    "processType": "CNC",
    "equipmentId": "EQ001",
    "operatorId": "OP001",
    "priority": 1,
    "estimatedDuration": 120,
    "actualDuration": 0,
    "status": "CREATED",
    "notes": "高精度模具加工",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00"
  },
  "timestamp": "2023-12-01T10:30:00"
}
```

### 获取流程详情

**请求**

```http
GET /api/processes/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "moldId": "M001",
    "processType": "CNC",
    "equipmentId": "EQ001",
    "operatorId": "OP001",
    "priority": 1,
    "estimatedDuration": 120,
    "actualDuration": 0,
    "status": "CREATED",
    "notes": "高精度模具加工",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00"
  },
  "timestamp": "2023-12-01T10:30:00"
}
```

### 更新流程

**请求**

```http
PUT /api/processes/{id}
Content-Type: application/json

{
  "moldId": "M001",
  "processType": "CNC",
  "equipmentId": "EQ002",
  "operatorId": "OP002",
  "priority": 2,
  "estimatedDuration": 150,
  "notes": "更新后的高精度模具加工"
}
```

**响应**

```json
{
  "code": 200,
  "message": "流程更新成功",
  "data": {
    "id": 1,
    "moldId": "M001",
    "processType": "CNC",
    "equipmentId": "EQ002",
    "operatorId": "OP002",
    "priority": 2,
    "estimatedDuration": 150,
    "actualDuration": 0,
    "status": "CREATED",
    "notes": "更新后的高精度模具加工",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T11:00:00"
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 删除流程

**请求**

```http
DELETE /api/processes/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "流程删除成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 分页查询流程

**请求**

```http
GET /api/processes?page=1&size=10&sort=createdAt,desc
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "content": [
      {
        "id": 1,
        "moldId": "M001",
        "processType": "CNC",
        "equipmentId": "EQ001",
        "operatorId": "OP001",
        "priority": 1,
        "estimatedDuration": 120,
        "actualDuration": 0,
        "status": "CREATED",
        "notes": "高精度模具加工",
        "createdAt": "2023-12-01T10:30:00",
        "updatedAt": "2023-12-01T10:30:00"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false
      },
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "numberOfElements": 1
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 按模具ID查询流程

**请求**

```http
GET /api/processes/mold/{moldId}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "moldId": "M001",
      "processType": "CNC",
      "equipmentId": "EQ001",
      "operatorId": "OP001",
      "priority": 1,
      "estimatedDuration": 120,
      "actualDuration": 0,
      "status": "CREATED",
      "notes": "高精度模具加工",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 按设备ID查询流程

**请求**

```http
GET /api/processes/equipment/{equipmentId}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "moldId": "M001",
      "processType": "CNC",
      "equipmentId": "EQ001",
      "operatorId": "OP001",
      "priority": 1,
      "estimatedDuration": 120,
      "actualDuration": 0,
      "status": "CREATED",
      "notes": "高精度模具加工",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 按操作员ID查询流程

**请求**

```http
GET /api/processes/operator/{operatorId}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "moldId": "M001",
      "processType": "CNC",
      "equipmentId": "EQ001",
      "operatorId": "OP001",
      "priority": 1,
      "estimatedDuration": 120,
      "actualDuration": 0,
      "status": "CREATED",
      "notes": "高精度模具加工",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 按状态查询流程

**请求**

```http
GET /api/processes/status/{status}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "moldId": "M001",
      "processType": "CNC",
      "equipmentId": "EQ001",
      "operatorId": "OP001",
      "priority": 1,
      "estimatedDuration": 120,
      "actualDuration": 0,
      "status": "CREATED",
      "notes": "高精度模具加工",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 按优先级查询流程

**请求**

```http
GET /api/processes/priority/{priority}
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "moldId": "M001",
      "processType": "CNC",
      "equipmentId": "EQ001",
      "operatorId": "OP001",
      "priority": 1,
      "estimatedDuration": 120,
      "actualDuration": 0,
      "status": "CREATED",
      "notes": "高精度模具加工",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取流程统计

**请求**

```http
GET /api/processes/statistics
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total": 100,
    "created": 10,
    "inProgress": 50,
    "paused": 5,
    "completed": 30,
    "cancelled": 5,
    "priority1": 20,
    "priority2": 50,
    "priority3": 30,
    "averageDuration": 115.5,
    "totalEstimatedDuration": 11550,
    "totalActualDuration": 10000
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 开始流程

**请求**

```http
POST /api/processes/{id}/start?operatorId=OP001
```

**响应**

```json
{
  "code": 200,
  "message": "流程开始成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 暂停流程

**请求**

```http
POST /api/processes/{id}/pause?operatorId=OP001
```

**响应**

```json
{
  "code": 200,
  "message": "流程暂停成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 恢复流程

**请求**

```http
POST /api/processes/{id}/resume?operatorId=OP001
```

**响应**

```json
{
  "code": 200,
  "message": "流程恢复成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 完成流程

**请求**

```http
POST /api/processes/{id}/complete?operatorId=OP001
```

**响应**

```json
{
  "code": 200,
  "message": "流程完成成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 取消流程

**请求**

```http
POST /api/processes/{id}/cancel?operatorId=OP001&reason=设备故障
```

**响应**

```json
{
  "code": 200,
  "message": "流程取消成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

## 状态机 API

### 获取当前状态

**请求**

```http
GET /api/state-machine/current/{processId}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "processId": 1,
    "currentState": "CREATED",
    "description": "已创建"
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取可能的状态转换

**请求**

```http
GET /api/state-machine/transitions/{processId}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "fromStatus": "CREATED",
      "toStatus": "IN_PROGRESS",
      "description": "开始流程"
    },
    {
      "fromStatus": "CREATED",
      "toStatus": "CANCELLED",
      "description": "取消流程"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 检查状态转换是否有效

**请求**

```http
GET /api/state-machine/can-transition?processId=1&targetStatus=IN_PROGRESS
```

**响应**

```json
{
  "code": 200,
  "message": "检查成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 执行状态转换

**请求**

```http
POST /api/state-machine/transition
Content-Type: application/json

{
  "processId": 1,
  "targetStatus": "IN_PROGRESS",
  "operatorId": "OP001",
  "notes": "开始加工"
}
```

**响应**

```json
{
  "code": 200,
  "message": "状态转换成功",
  "data": {
    "processId": 1,
    "fromStatus": "CREATED",
    "toStatus": "IN_PROGRESS",
    "transitionTime": "2023-12-01T11:00:00",
    "operatorId": "OP001",
    "notes": "开始加工"
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取状态历史

**请求**

```http
GET /api/state-machine/history/{processId}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "fromStatus": "CREATED",
      "toStatus": "IN_PROGRESS",
      "transitionTime": "2023-12-01T11:00:00",
      "operatorId": "OP001",
      "notes": "开始加工"
    }
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 重置状态机

**请求**

```http
POST /api/state-machine/reset/{processId}?operatorId=OP001&reason=重新开始
```

**响应**

```json
{
  "code": 200,
  "message": "状态机重置成功",
  "data": true,
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取所有状态枚举

**请求**

```http
GET /api/state-machine/states
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    "CREATED",
    "IN_PROGRESS",
    "PAUSED",
    "COMPLETED",
    "CANCELLED"
  ],
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取状态描述

**请求**

```http
GET /api/state-machine/state-description/{status}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": "已创建",
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取状态转换图数据

**请求**

```http
GET /api/state-machine/transition-graph
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "nodes": [
      {
        "id": "CREATED",
        "label": "已创建",
        "x": 100,
        "y": 100
      },
      {
        "id": "IN_PROGRESS",
        "label": "进行中",
        "x": 300,
        "y": 100
      }
    ],
    "edges": [
      {
        "from": "CREATED",
        "to": "IN_PROGRESS",
        "label": "开始"
      }
    ]
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取状态统计

**请求**

```http
GET /api/state-machine/statistics
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "created": 10,
    "inProgress": 50,
    "paused": 5,
    "completed": 30,
    "cancelled": 5
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

## 进度跟踪 API

### 创建进度记录

**请求**

```http
POST /api/progress
Content-Type: application/json

{
  "processId": 1,
  "currentStep": 1,
  "totalSteps": 10,
  "progressPercentage": 10.0,
  "currentOperation": "装夹",
  "nextOperation": "粗加工",
  "estimatedTimeRemaining": 108,
  "actualTimeUsed": 12
}
```

**响应**

```json
{
  "code": 200,
  "message": "进度记录创建成功",
  "data": {
    "id": 1,
    "processId": 1,
    "currentStep": 1,
    "totalSteps": 10,
    "progressPercentage": 10.0,
    "currentOperation": "装夹",
    "nextOperation": "粗加工",
    "estimatedTimeRemaining": 108,
    "actualTimeUsed": 12,
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00"
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 获取进度记录详情

**请求**

```http
GET /api/progress/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "processId": 1,
    "currentStep": 1,
    "totalSteps": 10,
    "progressPercentage": 10.0,
    "currentOperation": "装夹",
    "nextOperation": "粗加工",
    "estimatedTimeRemaining": 108,
    "actualTimeUsed": 12,
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00"
  },
  "timestamp": "2023-12-01T11:00:00"
}
```

### 更新进度记录

**请求**

```http
PUT /api/progress/{id}
Content-Type: application/json

{
  "processId": 1,
  "currentStep": 2,
  "totalSteps": 10,
  "progressPercentage": 20.0,
  "currentOperation": "粗加工",
  "nextOperation": "半精加工",
  "estimatedTimeRemaining": 96,
  "actualTimeUsed": 24
}
```

**响应**

```json
{
  "code": 200,
  "message": "进度记录更新成功",
  "data": {
    "id": 1,
    "processId": 1,
    "currentStep": 2,
    "totalSteps": 10,
    "progressPercentage": 20.0,
    "currentOperation": "粗加工",
    "nextOperation": "半精加工",
    "estimatedTimeRemaining": 96,
    "actualTimeUsed": 24,
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:30:00"
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 删除进度记录

**请求**

```http
DELETE /api/progress/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "进度记录删除成功",
  "data": true,
  "timestamp": "2023-12-01T11:30:00"
}
```

### 按流程ID获取进度记录

**请求**

```http
GET /api/progress/process/{processId}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "processId": 1,
    "currentStep": 2,
    "totalSteps": 10,
    "progressPercentage": 20.0,
    "currentOperation": "粗加工",
    "nextOperation": "半精加工",
    "estimatedTimeRemaining": 96,
    "actualTimeUsed": 24,
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:30:00"
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 按时间范围查询进度记录

**请求**

```http
GET /api/progress/time-range?startTime=2023-12-01T00:00:00&endTime=2023-12-01T23:59:59
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "currentStep": 2,
      "totalSteps": 10,
      "progressPercentage": 20.0,
      "currentOperation": "粗加工",
      "nextOperation": "半精加工",
      "estimatedTimeRemaining": 96,
      "actualTimeUsed": 24,
      "createdAt": "2023-12-01T11:00:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:30:00"
}
```

### 按操作查询进度记录

**请求**

```http
GET /api/progress/operation?operation=粗加工
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "currentStep": 2,
      "totalSteps": 10,
      "progressPercentage": 20.0,
      "currentOperation": "粗加工",
      "nextOperation": "半精加工",
      "estimatedTimeRemaining": 96,
      "actualTimeUsed": 24,
      "createdAt": "2023-12-01T11:00:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:30:00"
}
```

### 获取所有进度记录

**请求**

```http
GET /api/progress
```

**响应**

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "currentStep": 2,
      "totalSteps": 10,
      "progressPercentage": 20.0,
      "currentOperation": "粗加工",
      "nextOperation": "半精加工",
      "estimatedTimeRemaining": 96,
      "actualTimeUsed": 24,
      "createdAt": "2023-12-01T11:00:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:30:00"
}
```

### 更新进度步骤

**请求**

```http
PUT /api/progress/{processId}/step?step=3
```

**响应**

```json
{
  "code": 200,
  "message": "进度步骤更新成功",
  "data": true,
  "timestamp": "2023-12-01T11:30:00"
}
```

### 更新进度百分比

**请求**

```http
PUT /api/progress/{processId}/percentage?percentage=30.0
```

**响应**

```json
{
  "code": 200,
  "message": "进度百分比更新成功",
  "data": true,
  "timestamp": "2023-12-01T11:30:00"
}
```

### 更新当前操作

**请求**

```http
PUT /api/progress/{processId}/operation?currentOperation=半精加工&nextOperation=精加工
```

**响应**

```json
{
  "code": 200,
  "message": "当前操作更新成功",
  "data": true,
  "timestamp": "2023-12-01T11:30:00"
}
```

### 更新时间信息

**请求**

```http
PUT /api/progress/{processId}/time?estimatedTimeRemaining=84&actualTimeUsed=36
```

**响应**

```json
{
  "code": 200,
  "message": "时间信息更新成功",
  "data": true,
  "timestamp": "2023-12-01T11:30:00"
}
```

### 获取进度统计

**请求**

```http
GET /api/progress/statistics
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "averageProgress": 55.5,
    "completedCount": 30,
    "inProgressCount": 70
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 获取进度图表数据

**请求**

```http
GET /api/progress/{processId}/chart-data
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "processId": 1,
    "currentStep": 2,
    "totalSteps": 10,
    "progressPercentage": 20.0,
    "currentOperation": "粗加工",
    "nextOperation": "半精加工",
    "estimatedTimeRemaining": 96,
    "actualTimeUsed": 24
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 获取进度历史数据

**请求**

```http
GET /api/progress/{processId}/history
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "currentStep": 1,
      "totalSteps": 10,
      "progressPercentage": 10.0,
      "currentOperation": "装夹",
      "nextOperation": "粗加工",
      "estimatedTimeRemaining": 108,
      "actualTimeUsed": 12,
      "createdAt": "2023-12-01T11:00:00",
      "updatedAt": "2023-12-01T11:00:00"
    },
    {
      "id": 2,
      "processId": 1,
      "currentStep": 2,
      "totalSteps": 10,
      "progressPercentage": 20.0,
      "currentOperation": "粗加工",
      "nextOperation": "半精加工",
      "estimatedTimeRemaining": 96,
      "actualTimeUsed": 24,
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T11:30:00"
}
```

### 计算进度

**请求**

```http
GET /api/progress/{processId}/calculate
```

**响应**

```json
{
  "code": 200,
  "message": "计算成功",
  "data": {
    "stepPercentage": 20.0,
    "timePercentage": 20.0,
    "estimatedCompletionTime": "2023-12-01T13:00:00"
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

## 异常处理 API

### 创建异常记录

**请求**

```http
POST /api/exceptions
Content-Type: application/json

{
  "processId": 1,
  "exceptionType": "EQUIPMENT_FAILURE",
  "exceptionLevel": "HIGH",
  "title": "设备故障",
  "description": "主轴异常震动",
  "reportedBy": "OP001"
}
```

**响应**

```json
{
  "code": 200,
  "message": "异常记录创建成功",
  "data": {
    "id": 1,
    "processId": 1,
    "exceptionType": "EQUIPMENT_FAILURE",
    "exceptionLevel": "HIGH",
    "title": "设备故障",
    "description": "主轴异常震动",
    "exceptionStatus": "OPEN",
    "reportedBy": "OP001",
    "createdAt": "2023-12-01T11:30:00",
    "updatedAt": "2023-12-01T11:30:00"
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 获取异常记录详情

**请求**

```http
GET /api/exceptions/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "processId": 1,
    "exceptionType": "EQUIPMENT_FAILURE",
    "exceptionLevel": "HIGH",
    "title": "设备故障",
    "description": "主轴异常震动",
    "exceptionStatus": "OPEN",
    "reportedBy": "OP001",
    "createdAt": "2023-12-01T11:30:00",
    "updatedAt": "2023-12-01T11:30:00"
  },
  "timestamp": "2023-12-01T11:30:00"
}
```

### 更新异常记录

**请求**

```http
PUT /api/exceptions/{id}
Content-Type: application/json

{
  "processId": 1,
  "exceptionType": "EQUIPMENT_FAILURE",
  "exceptionLevel": "HIGH",
  "title": "设备故障",
  "description": "主轴异常震动，需要更换轴承",
  "exceptionStatus": "IN_PROGRESS",
  "reportedBy": "OP001"
}
```

**响应**

```json
{
  "code": 200,
  "message": "异常记录更新成功",
  "data": {
    "id": 1,
    "processId": 1,
    "exceptionType": "EQUIPMENT_FAILURE",
    "exceptionLevel": "HIGH",
    "title": "设备故障",
    "description": "主轴异常震动，需要更换轴承",
    "exceptionStatus": "IN_PROGRESS",
    "reportedBy": "OP001",
    "createdAt": "2023-12-01T11:30:00",
    "updatedAt": "2023-12-01T12:00:00"
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 删除异常记录

**请求**

```http
DELETE /api/exceptions/{id}
```

**响应**

```json
{
  "code": 200,
  "message": "异常记录删除成功",
  "data": true,
  "timestamp": "2023-12-01T12:00:00"
}
```

### 按流程ID获取异常记录

**请求**

```http
GET /api/exceptions/process/{processId}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 按异常类型获取异常记录

**请求**

```http
GET /api/exceptions/type/{type}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 按异常状态获取异常记录

**请求**

```http
GET /api/exceptions/status/{status}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 按异常级别获取异常记录

**请求**

```http
GET /api/exceptions/level/{level}
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 按时间范围获取异常记录

**请求**

```http
GET /api/exceptions/time-range?startTime=2023-12-01T00:00:00&endTime=2023-12-01T23:59:59
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取所有异常记录

**请求**

```http
GET /api/exceptions
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "processId": 1,
      "exceptionType": "EQUIPMENT_FAILURE",
      "exceptionLevel": "HIGH",
      "title": "设备故障",
      "description": "主轴异常震动",
      "exceptionStatus": "OPEN",
      "reportedBy": "OP001",
      "createdAt": "2023-12-01T11:30:00",
      "updatedAt": "2023-12-01T11:30:00"
    }
  ],
  "timestamp": "2023-12-01T12:00:00"
}
```

### 更新异常状态

**请求**

```http
PUT /api/exceptions/{id}/status?status=IN_PROGRESS&updatedBy=TECH001
```

**响应**

```json
{
  "code": 200,
  "message": "异常状态更新成功",
  "data": true,
  "timestamp": "2023-12-01T12:00:00"
}
```

### 处理异常

**请求**

```http
PUT /api/exceptions/{id}/handle?resolution=更换主轴轴承&resolvedBy=TECH001
```

**响应**

```json
{
  "code": 200,
  "message": "异常处理成功",
  "data": true,
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取异常统计

**请求**

```http
GET /api/exceptions/statistics
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "open": 30,
    "inProgress": 20,
    "resolved": 50,
    "high": 20,
    "medium": 50,
    "low": 30,
    "equipmentFailure": 40,
    "qualityIssue": 30,
    "materialShortage": 20,
    "other": 10
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

## 统计分析 API

### 获取流程统计

**请求**

```http
GET /api/statistics/process
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "created": 10,
    "inProgress": 50,
    "paused": 5,
    "completed": 30,
    "cancelled": 5,
    "priority1": 20,
    "priority2": 50,
    "priority3": 30,
    "averageDuration": 115.5,
    "totalEstimatedDuration": 11550,
    "totalActualDuration": 10000
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取状态统计

**请求**

```http
GET /api/statistics/state
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "created": 10,
    "inProgress": 50,
    "paused": 5,
    "completed": 30,
    "cancelled": 5
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取进度统计

**请求**

```http
GET /api/statistics/progress
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "averageProgress": 55.5,
    "completedCount": 30,
    "inProgressCount": 70
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取异常统计

**请求**

```http
GET /api/statistics/exception
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "open": 30,
    "inProgress": 20,
    "resolved": 50,
    "high": 20,
    "medium": 50,
    "low": 30,
    "equipmentFailure": 40,
    "qualityIssue": 30,
    "materialShortage": 20,
    "other": 10
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取综合统计

**请求**

```http
GET /api/statistics/comprehensive
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "process": {
      "total": 100,
      "created": 10,
      "inProgress": 50,
      "paused": 5,
      "completed": 30,
      "cancelled": 5
    },
    "state": {
      "total": 100,
      "created": 10,
      "inProgress": 50,
      "paused": 5,
      "completed": 30,
      "cancelled": 5
    },
    "progress": {
      "total": 100,
      "averageProgress": 55.5,
      "completedCount": 30,
      "inProgressCount": 70
    },
    "exception": {
      "total": 100,
      "open": 30,
      "inProgress": 20,
      "resolved": 50,
      "high": 20,
      "medium": 50,
      "low": 30
    }
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取时间范围统计

**请求**

```http
GET /api/statistics/time-range?startTime=2023-12-01T00:00:00&endTime=2023-12-01T23:59:59
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "process": {
      "total": 10,
      "created": 1,
      "inProgress": 5,
      "paused": 0,
      "completed": 3,
      "cancelled": 1
    },
    "state": {
      "total": 10,
      "created": 1,
      "inProgress": 5,
      "paused": 0,
      "completed": 3,
      "cancelled": 1
    },
    "progress": {
      "total": 10,
      "averageProgress": 55.5,
      "completedCount": 3,
      "inProgressCount": 7
    },
    "exception": {
      "total": 5,
      "open": 2,
      "inProgress": 1,
      "resolved": 2,
      "high": 1,
      "medium": 3,
      "low": 1
    }
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

### 获取图表数据

**请求**

```http
GET /api/statistics/chart-data?chartType=process&timeRange=daily
```

**响应**

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "labels": ["2023-11-27", "2023-11-28", "2023-11-29", "2023-11-30", "2023-12-01"],
    "datasets": [
      {
        "label": "创建",
        "data": [2, 3, 1, 2, 1],
        "backgroundColor": "#36A2EB"
      },
      {
        "label": "完成",
        "data": [1, 2, 3, 2, 3],
        "backgroundColor": "#4BC0C0"
      },
      {
        "label": "取消",
        "data": [0, 1, 0, 1, 1],
        "backgroundColor": "#FF6384"
      }
    ]
  },
  "timestamp": "2023-12-01T12:00:00"
}
```

## 数据模型

### 流程实体 (MoldProcess)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 流程ID |
| moldId | String | 模具ID |
| processType | String | 流程类型 |
| equipmentId | String | 设备ID |
| operatorId | String | 操作员ID |
| priority | Integer | 优先级 |
| estimatedDuration | Integer | 预估持续时间(分钟) |
| actualDuration | Integer | 实际持续时间(分钟) |
| status | String | 状态 |
| notes | String | 备注 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

### 状态历史实体 (ProcessStatusHistory)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | ID |
| processId | Long | 流程ID |
| fromStatus | String | 原状态 |
| toStatus | String | 目标状态 |
| transitionTime | LocalDateTime | 转换时间 |
| operatorId | String | 操作员ID |
| notes | String | 备注 |

### 进度跟踪实体 (ProgressTracking)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | ID |
| processId | Long | 流程ID |
| currentStep | Integer | 当前步骤 |
| totalSteps | Integer | 总步骤数 |
| progressPercentage | Double | 进度百分比 |
| currentOperation | String | 当前操作 |
| nextOperation | String | 下一步操作 |
| estimatedTimeRemaining | Integer | 预估剩余时间(分钟) |
| actualTimeUsed | Integer | 实际使用时间(分钟) |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

### 异常记录实体 (ExceptionRecord)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | ID |
| processId | Long | 流程ID |
| exceptionType | String | 异常类型 |
| exceptionLevel | String | 异常级别 |
| title | String | 异常标题 |
| description | String | 异常描述 |
| exceptionStatus | String | 异常状态 |
| resolution | String | 解决方案 |
| reportedBy | String | 报告人 |
| resolvedBy | String | 解决人 |
| resolvedAt | LocalDateTime | 解决时间 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

## 枚举类型

### 流程状态 (ProcessStatus)

| 值 | 说明 |
|----|------|
| CREATED | 已创建 |
| IN_PROGRESS | 进行中 |
| PAUSED | 已暂停 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

### 异常类型 (ExceptionType)

| 值 | 说明 |
|----|------|
| EQUIPMENT_FAILURE | 设备故障 |
| QUALITY_ISSUE | 质量问题 |
| MATERIAL_SHORTAGE | 材料短缺 |
| OTHER | 其他 |

### 异常级别 (ExceptionLevel)

| 值 | 说明 |
|----|------|
| HIGH | 高 |
| MEDIUM | 中 |
| LOW | 低 |

### 异常状态 (ExceptionStatus)

| 值 | 说明 |
|----|------|
| OPEN | 开放 |
| IN_PROGRESS | 处理中 |
| RESOLVED | 已解决 |

## 错误处理

### 业务异常

| 错误码 | 说明 |
|--------|------|
| 40001 | 参数错误 |
| 40002 | 状态转换无效 |
| 40003 | 资源不存在 |
| 40004 | 业务规则违反 |

### 系统异常

| 错误码 | 说明 |
|--------|------|
| 50001 | 数据库错误 |
| 50002 | 网络错误 |
| 50003 | 内部服务错误 |
| 50004 | 未知错误 |

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0.0 | 2023-12-01 | 初始版本 |