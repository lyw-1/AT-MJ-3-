import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 员工类型定义
export interface Employee {
  id: number
  name: string
  role: string
}

// 模具初始参数类型定义
export interface MoldInitialParam {
  id: number
  applicationNumber: string
  productCategory: string
  moldNumber: string
  specification: string
  material: string
  hrc: string
  structure: string
  totalShrinkage: number
  coreSize: string
  appearance: string
  positioningHoleDistance: string
  inletHoleDiameter: string
  holeCount: number
  holeDepth: string
  holeType: string
  slotWidth: string
  slotDepth: string
  cutAmount: string
  centerDistance: string
  mudSupplyRatio: string
  coreStep: string
  responsiblePerson: string
  remarks: string
  status: string
  routeSet: boolean
  createdAt?: string
  updatedAt?: string
}

// 模具初始参数查询参数
export interface MoldInitialParamQuery extends PageQuery {
  applicationNumber?: string
  productCategory?: string
  moldNumber?: string
  specification?: string
  totalShrinkage?: string
  responsiblePerson?: string
  remarks?: string
}

// 模具初始参数创建/更新参数
export interface MoldInitialParamForm {
  id?: number
  applicationNumber: string
  productCategory: string
  moldNumber: string
  specification: string
  material: string
  hrc: string
  structure: string
  totalShrinkage: number
  coreSize: string
  appearance: string
  positioningHoleDistance: string
  inletHoleDiameter: string
  holeCount: number
  holeDepth: string
  holeType: string
  slotWidth: string
  slotDepth: string
  cutAmount: string
  centerDistance: string
  mudSupplyRatio: string
  coreStep: string
  responsiblePerson: string
  remarks: string
  status: string
}

// 获取模具初始参数列表
export const getMoldInitialParams = (params: MoldInitialParamQuery): Promise<ApiResponse<PageData<MoldInitialParam>>> => {
  return request.get('/api/mold-initial-parameters', { params })
}

// 获取模具初始参数详情
export const getMoldInitialParamDetail = (id: number): Promise<ApiResponse<MoldInitialParam>> => {
  return request.get(`/api/mold-initial-parameters/${id}`)
}

// 创建模具初始参数
export const createMoldInitialParam = (data: any): Promise<ApiResponse<MoldInitialParam>> => {
  // 将前端表单数据转换为后端期望的MoldInitialParameter结构
  const backendData = {
    applicationNo: data.applicationNumber, // 申请编号
    category: data.productCategory, // 成品类别
    moldCode: data.moldNumber, // 模号
    productSpec: data.specification, // 成品规格
    material: data.material, // 模具钢材
    hrc: data.hrc, // HRC
    structure: data.structure, // 结构
    totalShrinkage: data.totalShrinkage, // 总收缩
    coreSize: data.coreSize, // 模芯尺寸
    outline: data.appearance, // 外形
    locationHolePitch: data.positioningHoleDistance, // 定位孔距
    inletDiameter: String(data.inletHoleDiameter), // 进泥孔径 - 转换为字符串
    holeCount: data.holeCount, // 孔数
    holeDepth: data.holeDepth, // 孔深
    porosityType: data.holeType, // 间孔或全孔
    slotWidth: String(data.slotWidth), // 槽宽 - 转换为字符串
    slotDepth: data.slotDepth, // 槽深
    cutInAmount: data.cutAmount, // 切入量
    centerDistance: String(data.centerDistance), // 中心距 - 转换为字符串
    feedRatio: data.mudSupplyRatio, // 供泥比
    coreStepHeight: data.coreStep, // 模芯台阶
    ownerName: data.responsiblePerson, // 负责人
    remark: data.remarks, // 备注
    status: data.status // 状态
  }
  return request.post('/api/mold-initial-parameters', backendData)
}

// 更新模具初始参数
export const updateMoldInitialParam = (id: number, data: any): Promise<ApiResponse<MoldInitialParam>> => {
  // 将前端表单数据转换为后端期望的MoldInitialParameter结构
  const backendData = {
    applicationNo: data.applicationNumber, // 申请编号
    category: data.productCategory, // 成品类别
    moldCode: data.moldNumber, // 模号
    productSpec: data.specification, // 成品规格
    material: data.material, // 模具钢材
    hrc: data.hrc, // HRC
    structure: data.structure, // 结构
    totalShrinkage: data.totalShrinkage, // 总收缩
    coreSize: data.coreSize, // 模芯尺寸
    outline: data.appearance, // 外形
    locationHolePitch: data.positioningHoleDistance, // 定位孔距
    inletDiameter: String(data.inletHoleDiameter), // 进泥孔径 - 转换为字符串
    holeCount: data.holeCount, // 孔数
    holeDepth: data.holeDepth, // 孔深
    porosityType: data.holeType, // 间孔或全孔
    slotWidth: String(data.slotWidth), // 槽宽 - 转换为字符串
    slotDepth: data.slotDepth, // 槽深
    cutInAmount: data.cutAmount, // 切入量
    centerDistance: String(data.centerDistance), // 中心距 - 转换为字符串
    feedRatio: data.mudSupplyRatio, // 供泥比
    coreStepHeight: data.coreStep, // 模芯台阶
    ownerName: data.responsiblePerson, // 负责人
    remark: data.remarks, // 备注
    status: data.status // 状态
  }
  return request.put(`/api/mold-initial-parameters/${id}`, backendData)
}

// 删除模具初始参数
export const deleteMoldInitialParam = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/mold-initial-parameters/${id}`)
}

// 批量删除模具初始参数
export const batchDeleteMoldInitialParams = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/mold-initial-parameters/batch', { data: { ids } })
}

// 导出模具初始参数
export const exportMoldInitialParams = (params: MoldInitialParamQuery): Promise<Blob> => {
  return request.get('/api/mold-initial-parameters/export', {
    params,
    responseType: 'blob'
  })
}

// 设置模具加工工序路线
export const setMoldProcessRoute = (id: number): Promise<ApiResponse<void>> => {
  return request.post(`/api/mold-initial-parameters/${id}/set-route`)
}

// 加工预设置类型定义
export interface ProcessPreset {
  id?: number
  moldId: number
  processCode: string
  presetKey: string
  presetValue: string
  createdAt?: string
  updatedAt?: string
}

// 加工预设置查询参数
export interface ProcessPresetQuery {
  moldId?: number
  processCode?: string
  presetKey?: string
}

// 获取加工预设置列表
export const getProcessPresets = (moldId: number, processCode: string): Promise<ApiResponse<ProcessPreset[]>> => {
  return request.get('/api/process-preset', {
    params: { moldId, processCode }
  })
}

// 获取单个加工预设置
export const getProcessPreset = (id: number): Promise<ApiResponse<ProcessPreset>> => {
  return request.get(`/api/process-preset/${id}`)
}

// 创建加工预设置
export const createProcessPreset = (data: ProcessPreset): Promise<ApiResponse<ProcessPreset>> => {
  return request.post('/api/process-preset', data)
}

// 批量保存加工预设置
export const batchSaveProcessPresets = (data: ProcessPreset[]): Promise<ApiResponse<void>> => {
  return request.post('/api/process-preset/batch', data)
}

// 更新加工预设置
export const updateProcessPreset = (id: number, data: ProcessPreset): Promise<ApiResponse<ProcessPreset>> => {
  return request.put(`/api/process-preset/${id}`, data)
}

// 删除加工预设置
export const deleteProcessPreset = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/process-preset/${id}`)
}

// 批量删除加工预设置
export const batchDeleteProcessPresets = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/process-preset/batch', { data: { ids } })
}

// 根据角色获取员工列表
export const getEmployeesByRole = (role: string): Promise<ApiResponse<Employee[]>> => {
  return request.get('/api/employees', {
    params: { role }
  })
}
