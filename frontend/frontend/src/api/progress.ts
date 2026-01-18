import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 进度记录类型
export interface ProgressRecord {
  id: number
  moldId: number
  moldNumber: string
  productName: string
  currentProcessId: number
  currentProcessName: string
  progress: number // 整体进度百分比
  completedProcesses: number // 已完成工序数
  totalProcesses: number // 总工序数
  estimatedCompletionTime: string // 预计完成时间
  actualCompletionTime?: string // 实际完成时间
  status: 'not_started' | 'in_progress' | 'completed' | 'delayed' | 'abnormal'
  createdAt: string
  updatedAt: string
}

// 工序进度类型
export interface ProcessProgress {
  id: number
  progressRecordId: number
  processId: number
  processName: string
  processSequence: number
  status: 'pending' | 'in_progress' | 'completed' | 'abnormal'
  startTime?: string
  endTime?: string
  actualDuration?: number // 实际耗时（分钟）
  estimatedDuration: number // 预计耗时（分钟）
  operatorId?: number
  operatorName?: string
  remark?: string
  createdAt: string
  updatedAt: string
}

// 工时记录类型
export interface WorkTimeRecord {
  id: number
  moldId: number
  processId: number
  processName: string
  operatorId: number
  operatorName: string
  startTime: string
  endTime: string
  duration: number // 耗时（分钟）
  pauseDuration: number // 暂停时长（分钟）
  effectiveDuration: number // 有效时长（分钟）
  remark?: string
  createdAt: string
  updatedAt: string
}

// 进度查询参数
export interface ProgressQuery extends PageQuery {
  moldId?: number
  moldNumber?: string
  productName?: string
  status?: string
  currentProcessId?: number
  startTime?: string
  endTime?: string
}

// 工序进度查询参数
export interface ProcessProgressQuery extends PageQuery {
  progressRecordId?: number
  processId?: number
  status?: string
  moldId?: number
}

// 工时记录查询参数
export interface WorkTimeQuery extends PageQuery {
  moldId?: number
  processId?: number
  operatorId?: number
  startTime?: string
  endTime?: string
}

// 进度更新参数
export interface ProgressUpdateForm {
  id: number
  progress?: number
  currentProcessId?: number
  status?: string
  estimatedCompletionTime?: string
  actualCompletionTime?: string
  remark?: string
}

// 工序进度更新参数
export interface ProcessProgressUpdateForm {
  id: number
  status: string
  startTime?: string
  endTime?: string
  operatorId?: number
  remark?: string
}

// 工时记录创建参数
export interface WorkTimeCreateForm {
  moldId: number
  processId: number
  operatorId: number
  startTime: string
  endTime: string
  duration: number
  pauseDuration: number
  effectiveDuration: number
  remark?: string
}

// 获取进度记录列表
export const getProgressRecords = (params: ProgressQuery): Promise<ApiResponse<PageData<ProgressRecord>>> => {
  return request.get('/api/progress/records', { params })
}

// 获取进度记录详情
export const getProgressRecordDetail = (id: number): Promise<ApiResponse<ProgressRecord>> => {
  return request.get(`/api/progress/records/${id}`)
}

// 更新进度记录
export const updateProgressRecord = (id: number, data: ProgressUpdateForm): Promise<ApiResponse<ProgressRecord>> => {
  return request.put(`/api/progress/records/${id}`, data)
}

// 获取模具进度详情
export const getMoldProgress = (moldId: number): Promise<ApiResponse<ProgressRecord>> => {
  return request.get(`/api/progress/molds/${moldId}`)
}

// 获取工序进度列表
export const getProcessProgresses = (params: ProcessProgressQuery): Promise<ApiResponse<PageData<ProcessProgress>>> => {
  return request.get('/api/progress/processes', { params })
}

// 更新工序进度
export const updateProcessProgress = (id: number, data: ProcessProgressUpdateForm): Promise<ApiResponse<ProcessProgress>> => {
  return request.put(`/api/progress/processes/${id}`, data)
}

// 批量更新工序进度
export const batchUpdateProcessProgress = (data: { ids: number[]; status: string }): Promise<ApiResponse<void>> => {
  return request.put('/api/progress/processes/batch-update', data)
}

// 创建工时记录
export const createWorkTimeRecord = (data: WorkTimeCreateForm): Promise<ApiResponse<WorkTimeRecord>> => {
  return request.post('/api/progress/work-time', data)
}

// 获取工时记录列表
export const getWorkTimeRecords = (params: WorkTimeQuery): Promise<ApiResponse<PageData<WorkTimeRecord>>> => {
  return request.get('/api/progress/work-time', { params })
}

// 导出工时记录
export const exportWorkTimeRecords = (params: WorkTimeQuery): Promise<Blob> => {
  return request.get('/api/progress/work-time/export', {
    params,
    responseType: 'blob'
  })
}

// 获取工时统计
export const getWorkTimeStatistics = (params: {
  startTime: string
  endTime: string
  operatorId?: number
  processId?: number
}): Promise<ApiResponse<any>> => {
  return request.get('/api/progress/work-time/statistics', { params })
}

// 获取进度统计
export const getProgressStatistics = (params: {
  startTime: string
  endTime: string
  status?: string
}): Promise<ApiResponse<any>> => {
  return request.get('/api/progress/statistics', { params })
}

// 暂停进度
export const pauseProgress = (id: number, remark?: string): Promise<ApiResponse<void>> => {
  return request.post(`/api/progress/records/${id}/pause`, { remark })
}

// 恢复进度
export const resumeProgress = (id: number, remark?: string): Promise<ApiResponse<void>> => {
  return request.post(`/api/progress/records/${id}/resume`, { remark })
}

// 获取进度趋势数据
export const getProgressTrend = (params: {
  moldId: number
  days: number
}): Promise<ApiResponse<any[]>> => {
  return request.get('/api/progress/records/trend', { params })
}