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
  return request.get('/process/processes', { params })
}

// 获取所有工序（不分页）
export const getAllProcesses = (): Promise<ApiResponse<Process[]>> => {
  return request.get('/process/processes/all')
}

// 获取工序详情
export const getProcessDetail = (id: number): Promise<ApiResponse<Process>> => {
  return request.get(`/process/processes/${id}`)
}

// 创建工序
export const createProcess = (data: ProcessForm): Promise<ApiResponse<Process>> => {
  return request.post('/process/processes', data)
}

// 更新工序
export const updateProcess = (id: number, data: ProcessForm): Promise<ApiResponse<Process>> => {
  return request.put(`/process/processes/${id}`, data)
}

// 删除工序
export const deleteProcess = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/process/processes/${id}`)
}

// 批量删除工序
export const batchDeleteProcesses = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/process/processes/batch', { data: { ids } })
}

// 获取工序路线列表
export const getProcessRoutes = (params: ProcessRouteQuery): Promise<ApiResponse<PageData<ProcessRoute>>> => {
  return request.get('/process/routes', { params })
}

// 获取所有工序路线（不分页）
export const getAllProcessRoutes = (): Promise<ApiResponse<ProcessRoute[]>> => {
  return request.get('/process/routes/all')
}

// 获取工序路线详情
export const getProcessRouteDetail = (id: number): Promise<ApiResponse<{
  route: ProcessRoute
  processDetails: ProcessRouteDetail[]
}>> => {
  return request.get(`/process/routes/${id}`)
}

// 创建工序路线
export const createProcessRoute = (data: ProcessRouteForm): Promise<ApiResponse<ProcessRoute>> => {
  return request.post('/process/routes', data)
}

// 更新工序路线
export const updateProcessRoute = (id: number, data: ProcessRouteForm): Promise<ApiResponse<ProcessRoute>> => {
  return request.put(`/process/routes/${id}`, data)
}

// 删除工序路线
export const deleteProcessRoute = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/process/routes/${id}`)
}

// 批量删除工序路线
export const batchDeleteProcessRoutes = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/process/routes/batch', { data: { ids } })
}

// 获取调度任务列表
export const getScheduleTasks = (params: ScheduleTaskQuery): Promise<ApiResponse<PageData<ScheduleTask>>> => {
  return request.get('/process/schedule-tasks', { params })
}

// 获取调度任务详情
export const getScheduleTaskDetail = (id: number): Promise<ApiResponse<ScheduleTask>> => {
  return request.get(`/process/schedule-tasks/${id}`)
}

// 创建调度任务
export const createScheduleTask = (data: ScheduleTaskForm): Promise<ApiResponse<ScheduleTask>> => {
  return request.post('/process/schedule-tasks', data)
}

// 更新调度任务
export const updateScheduleTask = (id: number, data: Partial<ScheduleTaskForm> & {
  status?: string
  actualStartTime?: string
  actualEndTime?: string
}): Promise<ApiResponse<ScheduleTask>> => {
  return request.put(`/process/schedule-tasks/${id}`, data)
}

// 批量更新调度任务
export const batchUpdateScheduleTasks = (data: {
  ids: number[]
  status: string
}): Promise<ApiResponse<void>> => {
  return request.put('/process/schedule-tasks/batch', data)
}

// 删除调度任务
export const deleteScheduleTask = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/process/schedule-tasks/${id}`)
}

// 导出调度任务
export const exportScheduleTasks = (params: ScheduleTaskQuery): Promise<Blob> => {
  return request.get('/process/schedule-tasks/export', {
    params,
    responseType: 'blob'
  })
}

// 获取模具的工序路线
export const getMoldProcessRoute = (moldId: number): Promise<ApiResponse<ProcessRouteDetail[]>> => {
  return request.get(`/process/molds/${moldId}/route`)
}

// 设置模具的工序路线
export const setMoldProcessRoute = (moldId: number, routeId: number): Promise<ApiResponse<void>> => {
  return request.post(`/process/molds/${moldId}/set-route`, { routeId })
}

// 获取工序甘特图数据
export const getProcessGanttData = (params: {
  moldId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<any[]>> => {
  return request.get('/process/gantt-data', { params })
}
