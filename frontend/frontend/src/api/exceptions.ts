import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 异常类型
export interface ExceptionType {
  id: number
  name: string
  code: string
  description?: string
  severity: 'low' | 'medium' | 'high' | 'critical' // 严重程度
  color: string
  defaultHandlerRole?: string
  createdAt: string
  updatedAt: string
}

// 异常记录
export interface ExceptionRecord {
  id: number
  moldId: number
  moldNumber: string
  productName: string
  exceptionTypeId: number
  exceptionTypeName: string
  exceptionTypeCode: string
  severity: 'low' | 'medium' | 'high' | 'critical'
  description: string
  currentProcessId: number
  currentProcessName: string
  reporterId: number
  reporterName: string
  reportTime: string
  status: 'reported' | 'processing' | 'resolved' | 'closed' // 异常状态
  resolveTime?: string // 解决时间
  closeTime?: string // 关闭时间
  resolution?: string // 解决措施
  resolvedById?: number // 解决人ID
  resolvedByName?: string // 解决人姓名
  remark?: string
  createdAt: string
  updatedAt: string
}

// 异常处理日志
export interface ExceptionHandleLog {
  id: number
  exceptionId: number
  operatorId: number
  operatorName: string
  operation: string // 操作类型：report, assign, process, resolve, close
  content: string // 操作内容
  createdAt: string
}

// 异常处理任务
export interface ExceptionHandleTask {
  id: number
  exceptionId: number
  assigneeId: number
  assigneeName: string
  title: string
  description: string
  status: 'pending' | 'processing' | 'completed' | 'overdue' // 任务状态
  deadline?: string // 截止时间
  completedTime?: string // 完成时间
  remark?: string
  createdAt: string
  updatedAt: string
}

// 异常类型查询参数
export interface ExceptionTypeQuery extends PageQuery {
  name?: string
  code?: string
  severity?: string
}

// 异常记录查询参数
export interface ExceptionRecordQuery extends PageQuery {
  moldId?: number
  moldNumber?: string
  productName?: string
  exceptionTypeId?: number
  severity?: string
  status?: string
  reporterId?: number
  startTime?: string
  endTime?: string
  currentProcessId?: number
}

// 异常处理日志查询参数
export interface ExceptionHandleLogQuery extends PageQuery {
  exceptionId: number
  operatorId?: number
  operation?: string
  startTime?: string
  endTime?: string
}

// 异常处理任务查询参数
export interface ExceptionHandleTaskQuery extends PageQuery {
  exceptionId?: number
  assigneeId?: number
  status?: string
  startTime?: string
  endTime?: string
}

// 异常记录创建参数
export interface ExceptionRecordCreateForm {
  moldId: number
  exceptionTypeId: number
  description: string
  currentProcessId?: number
  remark?: string
}

// 异常记录更新参数
export interface ExceptionRecordUpdateForm {
  id: number
  status?: string
  resolution?: string
  resolvedById?: number
  remark?: string
}

// 异常处理任务创建参数
export interface ExceptionHandleTaskCreateForm {
  exceptionId: number
  assigneeId: number
  title: string
  description: string
  deadline?: string
  remark?: string
}

// 获取异常类型列表
export const getExceptionTypes = (params: ExceptionTypeQuery): Promise<ApiResponse<PageData<ExceptionType>>> => {
  return request.get('/api/exceptions/types', { params })
}

// 获取所有异常类型（不分页）
export const getAllExceptionTypes = (): Promise<ApiResponse<ExceptionType[]>> => {
  return request.get('/api/exceptions/types/all')
}

// 获取异常类型详情
export const getExceptionTypeDetail = (id: number): Promise<ApiResponse<ExceptionType>> => {
  return request.get(`/api/exceptions/types/${id}`)
}

// 创建异常类型
export const createExceptionType = (data: Omit<ExceptionType, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<ExceptionType>> => {
  return request.post('/api/exceptions/types', data)
}

// 更新异常类型
export const updateExceptionType = (id: number, data: Omit<ExceptionType, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<ExceptionType>> => {
  return request.put(`/api/exceptions/types/${id}`, data)
}

// 删除异常类型
export const deleteExceptionType = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/exceptions/types/${id}`)
}

// 获取异常记录列表
export const getExceptionRecords = (params: ExceptionRecordQuery): Promise<ApiResponse<PageData<ExceptionRecord>>> => {
  return request.get('/api/exceptions/records', { params })
}

// 获取异常记录详情
export const getExceptionRecordDetail = (id: number): Promise<ApiResponse<ExceptionRecord>> => {
  return request.get(`/api/exceptions/records/${id}`)
}

// 创建异常记录
export const createExceptionRecord = (data: ExceptionRecordCreateForm): Promise<ApiResponse<ExceptionRecord>> => {
  return request.post('/api/exceptions/records', data)
}

// 更新异常记录
export const updateExceptionRecord = (id: number, data: ExceptionRecordUpdateForm): Promise<ApiResponse<ExceptionRecord>> => {
  return request.put(`/api/exceptions/records/${id}`, data)
}

// 删除异常记录
export const deleteExceptionRecord = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/exceptions/records/${id}`)
}

// 批量删除异常记录
export const batchDeleteExceptionRecords = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/exceptions/records/batch', { data: { ids } })
}

// 获取异常处理日志
export const getExceptionHandleLogs = (params: ExceptionHandleLogQuery): Promise<ApiResponse<PageData<ExceptionHandleLog>>> => {
  return request.get('/api/exceptions/handle-logs', { params })
}

// 创建异常处理日志
export const createExceptionHandleLog = (data: {
  exceptionId: number
  operation: string
  content: string
}): Promise<ApiResponse<ExceptionHandleLog>> => {
  return request.post('/api/exceptions/handle-logs', data)
}

// 获取异常处理任务列表
export const getExceptionHandleTasks = (params: ExceptionHandleTaskQuery): Promise<ApiResponse<PageData<ExceptionHandleTask>>> => {
  return request.get('/api/exceptions/handle-tasks', { params })
}

// 获取异常处理任务详情
export const getExceptionHandleTaskDetail = (id: number): Promise<ApiResponse<ExceptionHandleTask>> => {
  return request.get(`/api/exceptions/handle-tasks/${id}`)
}

// 创建异常处理任务
export const createExceptionHandleTask = (data: ExceptionHandleTaskCreateForm): Promise<ApiResponse<ExceptionHandleTask>> => {
  return request.post('/api/exceptions/handle-tasks', data)
}

// 更新异常处理任务
export const updateExceptionHandleTask = (id: number, data: {
  status: string
  completedTime?: string
  remark?: string
}): Promise<ApiResponse<ExceptionHandleTask>> => {
  return request.put(`/api/exceptions/handle-tasks/${id}`, data)
}

// 批量更新异常处理任务
export const batchUpdateExceptionHandleTasks = (data: {
  ids: number[]
  status: string
}): Promise<ApiResponse<void>> => {
  return request.put('/api/exceptions/handle-tasks/batch', data)
}

// 获取异常统计数据
export const getExceptionStatistics = (params: {
  startTime: string
  endTime: string
  severity?: string
  exceptionTypeId?: number
  status?: string
}): Promise<ApiResponse<any>> => {
  return request.get('/api/exceptions/statistics', { params })
}

// 获取模具异常历史
export const getMoldExceptionHistory = (moldId: number): Promise<ApiResponse<ExceptionRecord[]>> => {
  return request.get(`/api/exceptions/molds/${moldId}/history`)
}

// 导出异常记录
export const exportExceptionRecords = (params: ExceptionRecordQuery): Promise<Blob> => {
  return request.get('/api/exceptions/records/export', {
    params,
    responseType: 'blob'
  })
}
