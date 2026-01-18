import request from '@/utils/request'
import type { PageData, PageQuery, ApiResponse } from '@/types'

// 物料类型定义
export interface ConsumableItem {
  id: number
  materialCode: string
  itemName: string
  itemCategory: string
  specification: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  stockStatus: string
  unitPrice: number
  totalValue: number
  supplier: string
  location: string
  lastUpdate: string
  remarks: string
}

// 物料查询参数
export interface ConsumableQuery extends PageQuery {
  materialCode?: string
  itemName?: string
  itemCategory?: string
  specification?: string
  unit?: string
  currentStock?: string
  minStock?: string
  maxStock?: string
  stockStatus?: string
  unitPrice?: string
  totalValue?: string
  supplier?: string
  location?: string
  lastUpdate?: string
}

// 物料表单数据
export interface ConsumableForm {
  materialCode: string
  itemName: string
  itemCategory: string
  specification: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  unitPrice: number
  supplier: string
  location: string
  remarks: string
}

// 入库表单数据
export interface StockInForm {
  materialCode: string
  itemName: string
  currentStock: string
  quantity: number
  reason: string
  remarks: string
}

// 出库表单数据
export interface StockOutForm {
  materialCode: string
  itemName: string
  currentStock: string
  quantity: number
  reason: string
  recipient: string
  remarks: string
}

// 获取物料列表
export const getConsumables = (params: ConsumableQuery): Promise<ApiResponse<PageData<ConsumableItem>>> => {
  return request.get('/api/consumables', { params })
}

// 获取物料详情
export const getConsumableDetail = (id: number): Promise<ApiResponse<ConsumableItem>> => {
  return request.get(`/api/consumables/${id}`)
}

// 创建物料
export const createConsumable = (data: ConsumableForm): Promise<ApiResponse<ConsumableItem>> => {
  return request.post('/api/consumables', data)
}

// 更新物料
export const updateConsumable = (id: number, data: ConsumableForm): Promise<ApiResponse<ConsumableItem>> => {
  return request.put(`/api/consumables/${id}`, data)
}

// 删除物料
export const deleteConsumable = (id: number): Promise<ApiResponse<void>> => {
  return request.delete(`/api/consumables/${id}`)
}

// 批量删除物料
export const batchDeleteConsumables = (ids: number[]): Promise<ApiResponse<void>> => {
  return request.delete('/api/consumables/batch', { data: ids })
}

// 检查物料编码唯一性
export const checkMaterialCodeUnique = (materialCode: string, id?: number): Promise<ApiResponse<boolean>> => {
  return request.get('/api/consumables/check-material-code-unique', {
    params: { materialCode, id }
  })
}

// 物料入库
export const stockInConsumable = (id: number, data: { quantity: number; reason: string; remarks: string }): Promise<ApiResponse<void>> => {
  return request.post(`/api/consumables/${id}/inbound`, data)
}

// 物料出库
export const stockOutConsumable = (id: number, data: { quantity: number; reason: string; recipient: string; remarks: string }): Promise<ApiResponse<void>> => {
  return request.post(`/api/consumables/${id}/outbound`, data)
}
