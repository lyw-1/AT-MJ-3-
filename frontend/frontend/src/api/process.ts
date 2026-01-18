import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 工序类型
export interface Process {
  id: number
  name: string
  code: string
  description?: string
  category: string
  sequence: number
  estimatedDuration: number // 预计耗时（分钟）
  machineType: string
  operatorRole: string
  qualityCheckRequired: boolean
  status: 'active' | 'inactive'
  createdAt: string
  updatedAt: string
}

// 工序路线
export interface ProcessRoute {
  id: number
  name: string
  code: string
  description?: string
  moldTypeId: number
  moldTypeName: string
  totalDuration: number // 总预计耗时（分钟）
  processCount: number // 工序数量
  status: 'active' | 'inactive'
  createdAt: string
  updatedAt: string
}

// 工序路线详情
export interface ProcessRouteDetail {
  id: number
  routeId: number
  processId: number
  processName: string
  processCode: string
  sequence: number
  estimatedDuration: number
  machineType: string
  operatorRole: string
  qualityCheckRequired: boolean
  predecessorId?: number // 前置工序ID
  successorId?: number // 后置工序ID
  createdAt: string
  updatedAt: string
}

// 调度任务
export interface ScheduleTask {
  id: number
  moldId: number
  moldNumber: string
  productName: string
  currentProcessId: number
  currentProcessName: string
  scheduledStartTime: string
  scheduledEndTime: string
  actualStartTime?: string
  actualEndTime?: string
  operatorId?: number
  operatorName?: string
  machineId?: number
  machineName?: string
  status: 'pending' | 'in_progress' | 'completed' | 'delayed' | 'canceled'
  priority: 'low' | 'medium' | 'high'
  remark?: string
  createdAt: string
  updatedAt: string
}

// 工序查询参数
export interface ProcessQuery extends PageQuery {
  name?: string
  code?: string
  category?: string
  status?: string
  machineType?: string
}

// 工序路线查询参数
export interface ProcessRouteQuery extends PageQuery {
  name?: string
  code?: string
  moldTypeId?: number
  status?: string
}

// 调度任务查询参数
export interface ScheduleTaskQuery extends PageQuery {
  moldId?: number
  moldNumber?: string
  currentProcessId?: number
  status?: string
  priority?: string
  operatorId?: number
  machineId?: number
  startTime?: string
  endTime?: string
}

// 工序模板
export interface ProcessTemplate {
  id: number
  code: string
  name: string
  description?: string
  category: string
  fields: TemplateField[]
  status: 'active' | 'inactive'
  createdAt: string
  updatedAt: string
}

// 模板字段
export interface TemplateField {
  id?: number
  fieldName: string
  fieldLabel: string
  fieldType: 'input' | 'number' | 'select' | 'date' | 'checkbox' | 'textarea'
  required: boolean
  defaultValue?: any
  options?: { label: string; value: any }[]
  placeholder?: string
  min?: number
  max?: number
  step?: number
  rows?: number
}

// 模板查询参数
export interface ProcessTemplateQuery extends PageQuery {
  code?: string
  name?: string
  category?: string
  status?: string
}

// 模板创建/更新参数
export interface ProcessTemplateForm {
  id?: number
  code: string
  name: string
  description?: string
  category: string
  fields: TemplateField[]
  status: 'active' | 'inactive'
}

// 工序创建/更新参数
export interface ProcessForm {
  id?: number
  name: string
  code: string
  description?: string
  category: string
  sequence: number
  estimatedDuration: number
  machineType: string
  operatorRole: string
  qualityCheckRequired: boolean
  status: 'active' | 'inactive'
}

// 工序路线创建/更新参数
export interface ProcessRouteForm {
  id?: number
  name: string
  code: string
  description?: string
  moldTypeId: number
  processIds: number[] // 工序ID列表，按顺序排列
}

// 调度任务创建/更新参数
export interface ScheduleTaskForm {
  id?: number
  moldId: number
  currentProcessId: number
  scheduledStartTime: string
  scheduledEndTime: string
  operatorId?: number
  machineId?: number
  priority: 'low' | 'medium' | 'high'
  remark?: string
}

// 获取工序列表
export const getProcesses = (params: ProcessQuery): Promise<ApiResponse<PageData<Process>>> => {
  return request.get('/api/process/processes', {
    params,
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取所有工序（不分页）
export const getAllProcesses = (): Promise<ApiResponse<Process[]>> => {
  return request.get('/api/process/processes/all', {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序详情
export const getProcessDetail = (id: number): Promise<ApiResponse<Process>> => {
  return request.get(`/api/process/processes/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 创建工序
export const createProcess = (data: ProcessForm): Promise<ApiResponse<Process>> => {
  return request.post('/api/process/processes', data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 更新工序
export const updateProcess = (id: number, data: ProcessForm): Promise<ApiResponse<Process>> => {
  return request.put(`/api/process/processes/${id}`, data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 删除工序
export const deleteProcess = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/process/processes/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 批量删除工序
export const batchDeleteProcesses = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/process/processes/batch', {
    data: { ids },
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序路线列表
export const getProcessRoutes = (params: ProcessRouteQuery): Promise<ApiResponse<PageData<ProcessRoute>>> => {
  return request.get('/api/process/routes', {
    params,
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取所有工序路线（不分页）
export const getAllProcessRoutes = (): Promise<ApiResponse<ProcessRoute[]>> => {
  return request.get('/api/process/routes/all', {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序路线详情
export const getProcessRouteDetail = (id: number): Promise<ApiResponse<{
  route: ProcessRoute
  processDetails: ProcessRouteDetail[]
}>> => {
  return request.get(`/api/process/routes/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 创建工序路线
export const createProcessRoute = (data: ProcessRouteForm): Promise<ApiResponse<ProcessRoute>> => {
  return request.post('/api/process/routes', data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 更新工序路线
export const updateProcessRoute = (id: number, data: ProcessRouteForm): Promise<ApiResponse<ProcessRoute>> => {
  return request.put(`/api/process/routes/${id}`, data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 删除工序路线
export const deleteProcessRoute = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/process/routes/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 批量删除工序路线
export const batchDeleteProcessRoutes = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/process/routes/batch', {
    data: { ids },
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取调度任务列表
export const getScheduleTasks = (params: ScheduleTaskQuery): Promise<ApiResponse<PageData<ScheduleTask>>> => {
  return request.get('/api/process/schedule-tasks', {
    params,
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取调度任务详情
export const getScheduleTaskDetail = (id: number): Promise<ApiResponse<ScheduleTask>> => {
  return request.get(`/api/process/schedule-tasks/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 创建调度任务
export const createScheduleTask = (data: ScheduleTaskForm): Promise<ApiResponse<ScheduleTask>> => {
  return request.post('/api/process/schedule-tasks', data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 更新调度任务
export const updateScheduleTask = (id: number, data: Partial<ScheduleTaskForm> & {
  status?: string
  actualStartTime?: string
  actualEndTime?: string
}): Promise<ApiResponse<ScheduleTask>> => {
  return request.put(`/api/process/schedule-tasks/${id}`, data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 批量更新调度任务
export const batchUpdateScheduleTasks = (data: {
  ids: number[]
  status: string
}): Promise<ApiResponse<void>> => {
  return request.put('/api/process/schedule-tasks/batch', data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 删除调度任务
export const deleteScheduleTask = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/process/schedule-tasks/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 导出调度任务
export const exportScheduleTasks = (params: ScheduleTaskQuery): Promise<Blob> => {
  return request.get('/api/process/schedule-tasks/export', {
    params,
    responseType: 'blob',
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取模具的工序路线
export const getMoldProcessRoute = (moldId: number): Promise<ApiResponse<ProcessRouteDetail[]>> => {
  return request.get(`/api/process/molds/${moldId}/route`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 设置模具的工序路线
export const setMoldProcessRoute = (moldId: number, routeId: number): Promise<ApiResponse<void>> => {
  return request.post(`/api/process/molds/${moldId}/set-route`, { routeId }, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序甘特图数据
export const getProcessGanttData = (params: {
  moldId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<any[]>> => {
  return request.get('/api/process/gantt-data', {
    params,
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序模板列表
export const getProcessTemplates = (params: ProcessTemplateQuery): Promise<ApiResponse<PageData<ProcessTemplate>>> => {
  // 添加日志以调试请求
  console.log('[API_CALL] getProcessTemplates', params)
  return request.get('/api/process/templates', {
    params,
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取所有工序模板（不分页）
export const getAllProcessTemplates = (): Promise<ApiResponse<ProcessTemplate[]>> => {
  return request.get('/api/process/templates/all', {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序模板详情
export const getProcessTemplateDetail = (id: number): Promise<ApiResponse<ProcessTemplate>> => {
  return request.get(`/api/process/templates/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 创建工序模板
export const createProcessTemplate = (data: ProcessTemplateForm): Promise<ApiResponse<ProcessTemplate>> => {
  return request.post('/api/process/templates', data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 更新工序模板
export const updateProcessTemplate = (id: number, data: ProcessTemplateForm): Promise<ApiResponse<ProcessTemplate>> => {
  return request.put(`/api/process/templates/${id}`, data, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 删除工序模板
export const deleteProcessTemplate = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/process/templates/${id}`, {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 批量删除工序模板
export const batchDeleteProcessTemplates = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/process/templates/batch', {
    data: { ids },
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}

// 获取工序模板类型
export const getProcessTemplateCategories = (): Promise<ApiResponse<{ label: string; value: string }[]>> => {
  return request.get('/api/process/templates/categories', {
    headers: {
      'X-Dev-Token': import.meta.env.VITE_DEV_TOKEN || 'dev-123'
    }
  })
}
