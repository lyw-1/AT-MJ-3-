import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 状态类型定义
export interface State {
  id: number
  name: string
  code: string
  description?: string
  type: 'initial' | 'processing' | 'completed' | 'abnormal' | 'canceled'
  color: string
  createdAt: string
  updatedAt: string
}

// 状态转换规则类型
export interface StateTransition {
  id: number
  fromStateId: number
  toStateId: number
  event: string
  description?: string
  allowedRoles: string[]
  createdAt: string
  updatedAt: string
}

// 状态历史记录类型
export interface StateHistory {
  id: number
  moldId: number
  fromStateId: number
  toStateId: number
  event: string
  operatorId: number
  operatorName: string
  remark?: string
  createdAt: string
}

// 状态查询参数
export interface StateQuery extends PageQuery {
  name?: string
  code?: string
  type?: string
}

// 状态转换规则查询参数
export interface StateTransitionQuery extends PageQuery {
  fromStateId?: number
  toStateId?: number
  event?: string
}

// 状态历史查询参数
export interface StateHistoryQuery extends PageQuery {
  moldId?: number
  fromStateId?: number
  toStateId?: number
  startTime?: string
  endTime?: string
}

// 状态更新参数
export interface StateUpdateForm {
  moldId: number
  toStateId: number
  event: string
  remark?: string
}

// 获取状态列表
export const getStates = (params: StateQuery): Promise<ApiResponse<PageData<State>>> => {
  return request.get('/state-machine/states', { params })
}

// 获取所有状态（不分页）
export const getAllStates = (): Promise<ApiResponse<State[]>> => {
  return request.get('/state-machine/states/all')
}

// 获取状态详情
export const getStateDetail = (id: number): Promise<ApiResponse<State>> => {
  return request.get(`/state-machine/states/${id}`)
}

// 创建状态
export const createState = (data: Omit<State, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<State>> => {
  return request.post('/state-machine/states', data)
}

// 更新状态
export const updateState = (id: number, data: Omit<State, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<State>> => {
  return request.put(`/state-machine/states/${id}`, data)
}

// 删除状态
export const deleteState = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/state-machine/states/${id}`)
}

// 获取状态转换规则列表
export const getStateTransitions = (params: StateTransitionQuery): Promise<ApiResponse<PageData<StateTransition>>> => {
  return request.get('/state-machine/transitions', { params })
}

// 获取状态转换规则详情
export const getStateTransitionDetail = (id: number): Promise<ApiResponse<StateTransition>> => {
  return request.get(`/state-machine/transitions/${id}`)
}

// 创建状态转换规则
export const createStateTransition = (data: Omit<StateTransition, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<StateTransition>> => {
  return request.post('/state-machine/transitions', data)
}

// 更新状态转换规则
export const updateStateTransition = (id: number, data: Omit<StateTransition, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<StateTransition>> => {
  return request.put(`/state-machine/transitions/${id}`, data)
}

// 删除状态转换规则
export const deleteStateTransition = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/state-machine/transitions/${id}`)
}

// 获取模具状态历史记录
export const getStateHistories = (params: StateHistoryQuery): Promise<ApiResponse<PageData<StateHistory>>> => {
  return request.get('/state-machine/histories', { params })
}

// 获取模具当前状态
export const getCurrentState = (moldId: number): Promise<ApiResponse<State>> => {
  return request.get(`/state-machine/molds/${moldId}/current-state`)
}

// 更新模具状态
export const updateMoldState = (data: StateUpdateForm): Promise<ApiResponse<void>> => {
  return request.post('/state-machine/molds/update-state', data)
}

// 获取可用的状态转换选项
export const getAvailableTransitions = (moldId: number): Promise<ApiResponse<StateTransition[]>> => {
  return request.get(`/state-machine/molds/${moldId}/available-transitions`)
}
