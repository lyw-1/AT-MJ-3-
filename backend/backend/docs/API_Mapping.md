# 前端页面结构与后端接口映射

## 首页
- 统计数据：`GET /api/home/stats`
- 总览（待办、进行中任务、最近生产记录）：`GET /api/home/overview`
- 点击统计跳转：使用各模块列表筛选接口（如 `GET /api/molds?statusId=...` 或 `/api/mold-initial-parameters/search`）

## 仪表盘
- 超标统计（容重、槽宽）：`GET /api/dashboard/overlimit`
- 流程状态统计：`GET /api/dashboard/mold-status-counts`
- 重点模具供需：`GET /api/dashboard/key-molds-supply-demand`

## 加工管理
- 模具初始参数列表：
  - 列表：`GET /api/mold-initial-parameters`
  - 搜索筛选：`GET /api/mold-initial-parameters/search?applicationNo=&category=&moldCode=&productSpec=&material=&ownerName=`
  - 创建：`POST /api/mold-initial-parameters`
  - 详情：`GET /api/mold-initial-parameters/{id}`
  - 编辑：`PUT /api/mold-initial-parameters/{id}` 或 `PUT /api/mold-initial-parameters/{id}/fields`
  - 删除：`DELETE /api/mold-initial-parameters/{id}`
- 工序路线：
  - 模板：`GET /api/process-route/templates`
  - 路线查询：`GET /api/process-route/{moldId}/steps`
  - 路线保存：`POST /api/process-route/{moldId}/steps`
  - 流程实例化：`POST /api/process-route/{moldId}/instantiate`
- 流程操作：
  - 开始加工：`POST /api/mold-process/{id}/start`
  - 加工完成：`POST /api/mold-process/{id}/complete`

## 模库管理
- 模具列表：`GET /api/molds`、按类型/状态筛选：`GET /api/molds/type/{typeId}`、`GET /api/molds/status/{statusId}`
- 重点模具：`GET /api/molds/key`、设置：`PUT /api/molds/{id}/key?key=true|false`
- 出入库记录：`GET /api/mold-storage-records`（如需补充，可结合已有 Mapper 编写）
- 配件列表：`GET /api/accessories`（如需补充，可结合已有 Mapper 编写）
- 测量数据录入：基于相关记录表（如 `slot_width_record`）的 CRUD 接口（按需扩展）

## 调模管理
- 预计7天下线：`GET /api/molds/offline7days`
- 调模记录/验收记录：`/api/tuning-records`（若无控制器，按实体补充基础 CRUD）

## 耗材及备品
- 耗材列表：`GET /api/consumables`
- 入库：`POST /api/consumables/{id}/inbound`
- 出库：`POST /api/consumables/{id}/outbound`
- 入库记录：`POST /api/consumables/inbound-records`
- 出入库明细：`GET /api/consumables/stock-records?type=IN|OUT&itemId=`

## 设备管理
- 设备列表与详情：`/api/equipment`、`/api/equipment/{id}`
- 使用统计（含预计下线天数）：`GET /api/equipment/usage-stats`

## 生产管理
- 生产任务：`/api/production-tasks` 基本 CRUD 与按状态、设备筛选
- 生产记录：`/api/production-records`（已存在，可按需扩展）

## 系统管理
- 成品规格：`/api/product-specs` CRUD
- 成品：`/api/products` CRUD
- 用户/角色：现有模块接口（按需筛选）

---

备注：字典 `mold_status.status_code` 已通过迁移初始化（`BARE/AVAILABLE/SEALED/WAIT_PROCESS/PROCESSING/OUTSOURCED/WAIT_TUNING/TUNING/WAIT_ACCEPT`）。
