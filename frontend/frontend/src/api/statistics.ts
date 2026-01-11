import request from '@/utils/request'
import type { ApiResponse } from '@/types'

// 模具生产统计
export interface MoldProductionStats {
  totalMolds: number // 总模具数
  activeMolds: number // 活跃模具数
  completedMolds: number // 已完成模具数
  inProgressMolds: number // 进行中模具数
  delayedMolds: number // 延迟模具数
  abnormalMolds: number // 异常模具数
  monthlyProduction: number // 月产量
  monthlyCompletionRate: number // 月完成率
  avgCycleTime: number // 平均周期时间（天）
}

// 工序效率统计
export interface ProcessEfficiencyStats {
  processId: number
  processName: string
  totalExecutions: number // 总执行次数
  completedExecutions: number // 已完成次数
  avgDuration: number // 平均耗时（分钟）
  avgDelay: number // 平均延迟（分钟）
  successRate: number // 成功率
  operatorEfficiency: Array<{
    operatorId: number
    operatorName: string
    avgDuration: number
    totalExecutions: number
  }>
}

// 异常统计
export interface ExceptionStats {
  totalExceptions: number // 总异常数
  resolvedExceptions: number // 已解决异常数
  avgResolutionTime: number // 平均解决时间（小时）
  exceptionsBySeverity: {
    low: number
    medium: number
    high: number
    critical: number
  }
  exceptionsByType: Array<{
    exceptionTypeId: number
    exceptionTypeName: string
    count: number
  }>
  monthlyTrend: Array<{
    month: string
    count: number
  }>
}

// 人员绩效统计
export interface OperatorPerformanceStats {
  operatorId: number
  operatorName: string
  totalWorkingHours: number // 总工时（小时）
  effectiveWorkingHours: number // 有效工时（小时）
  completedProcesses: number // 完成工序数
  efficiencyRate: number // 效率率
  qualityScore: number // 质量评分
  exceptionCount: number // 异常数
}

// 生产进度趋势
export interface ProductionTrend {
  date: string
  completedMolds: number
  inProgressMolds: number
  newMolds: number
  completionRate: number
}

// 生产计划达成率
export interface ProductionPlanAchievement {
  period: 'week' | 'month' | 'quarter' | 'year' // 统计周期
  plannedMolds: number // 计划模具数
  actualMolds: number // 实际完成模具数
  achievementRate: number // 达成率
  breakdown: Array<{
    date: string
    planned: number
    actual: number
    rate: number
  }>
}

// 设备利用率统计
export interface EquipmentUtilizationStats {
  equipmentId: number
  equipmentName: string
  totalTime: number // 总时间（小时）
  workingTime: number // 工作时间（小时）
  idleTime: number // 空闲时间（小时）
  maintenanceTime: number // 维护时间（小时）
  utilizationRate: number // 利用率
  monthlyTrend: Array<{
    month: string
    utilizationRate: number
  }>
}

// 获取模具生产统计
export const getMoldProductionStats = (params: {
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<MoldProductionStats>> => {
  return request.get('/statistics/mold-production', { params })
}

// 获取工序效率统计
export const getProcessEfficiencyStats = (params: {
  processId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<ProcessEfficiencyStats[]>> => {
  return request.get('/statistics/process-efficiency', { params })
}

// 获取异常统计
export const getExceptionStats = (params: {
  startTime?: string
  endTime?: string
  severity?: string
}): Promise<ApiResponse<ExceptionStats>> => {
  return request.get('/statistics/exceptions', { params })
}

// 获取人员绩效统计
export const getOperatorPerformanceStats = (params: {
  operatorId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<OperatorPerformanceStats[]>> => {
  return request.get('/statistics/operator-performance', { params })
}

// 获取生产进度趋势
export const getProductionTrend = (params: {
  period: 'day' | 'week' | 'month' | 'year' // 统计周期
  days?: number // 天数范围，默认为30天
}): Promise<ApiResponse<ProductionTrend[]>> => {
  return request.get('/statistics/production-trend', { params })
}

// 获取生产计划达成率
export const getProductionPlanAchievement = (params: {
  period: 'week' | 'month' | 'quarter' | 'year'
  year?: number
  month?: number
  quarter?: number
}): Promise<ApiResponse<ProductionPlanAchievement>> => {
  return request.get('/statistics/production-plan-achievement', { params })
}

// 获取设备利用率统计
export const getEquipmentUtilizationStats = (params: {
  equipmentId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<EquipmentUtilizationStats[]>> => {
  return request.get('/statistics/equipment-utilization', { params })
}

// 获取工时统计
export const getWorkTimeStats = (params: {
  operatorId?: number
  processId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<any>> => {
  return request.get('/statistics/work-time', { params })
}

// 获取模具生命周期统计
export const getMoldLifecycleStats = (params: {
  moldTypeId?: number
  startTime?: string
  endTime?: string
}): Promise<ApiResponse<any>> => {
  return request.get('/statistics/mold-lifecycle', { params })
}

// 导出统计报表
export const exportStatisticsReport = (params: {
  reportType: 'mold' | 'process' | 'exception' | 'operator' | 'equipment'
  startTime: string
  endTime: string
  format: 'excel' | 'pdf' | 'csv'
}): Promise<Blob> => {
  return request.get('/statistics/export-report', {
    params,
    responseType: 'blob'
  })
}

// 获取实时生产看板数据
export const getRealTimeProductionBoard = (): Promise<ApiResponse<{
  totalMolds: number
  inProgressMolds: number
  completedToday: number
  activeProcesses: number
  currentExceptions: number
  highPriorityExceptions: number
  productionRate: number
  efficiencyRate: number
  topOperators: Array<{
    operatorId: number
    operatorName: string
    completedProcesses: number
  }>
  recentExceptions: Array<{
    exceptionId: number
    moldNumber: string
    exceptionTypeName: string
    severity: string
    reportTime: string
  }>
}>> => {
  return request.get('/statistics/real-time-board')
}

// 获取质量统计
export const getQualityStats = (params: {
  startTime?: string
  endTime?: string
  processId?: number
}): Promise<ApiResponse<{
  totalInspections: number
  passInspections: number
  passRate: number
  defectTypes: Array<{
    defectTypeId: number
    defectTypeName: string
    count: number
  }>
  processQuality: Array<{
    processId: number
    processName: string
    passRate: number
    defectCount: number
  }>
}>> => {
  return request.get('/statistics/quality', { params })
}
