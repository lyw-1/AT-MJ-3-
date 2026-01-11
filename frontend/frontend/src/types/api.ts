export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageData<T> {
  list: T[]
  total: number
  page: number
  size: number
}

export interface PageQuery {
  page?: number
  size?: number
  keyword?: string
  status?: string
  startDate?: string
  endDate?: string
}

export interface Mold {
  id: number
  moldNumber: string
  productCategory: string
  specification: string
  steelMaterial?: string
  structureType?: '斜边模' | '直压模' | '收边模'
  totalShrinkage?: number
  coreSize?: string
  appearance?: string
  positioningHoleDistance?: string
  inletHoleDiameter?: string
  holeCount?: number
  holeDepth?: string
  holeType?: '间孔' | '全孔'
  slotWidth?: string
  slotDepth?: string
  cutAmount?: string
  centerDistance?: string
  mudSupplyRatio?: string
  coreStep?: string
  hrc?: string
  responsibleUser?: User
  remark?: string
  status: MoldStatus
  processRoute?: ProcessStep[]
  createdAt: string
  updatedAt: string
}

export type MoldStatus = 
  | '待加工'
  | '加工中'
  | '委外加工'
  | '待调试'
  | '调试中'
  | '待验收'
  | '已完成'
  | '已入库'

export interface ProcessStep {
  id: number
  stepNumber: number
  name: string
  equipment?: Equipment
  operator?: User
  estimatedTime?: number
  actualTime?: number
  status: 'pending' | 'processing' | 'completed'
}

export interface Equipment {
  id: number
  name: string
  code: string
  type: EquipmentType
  status: EquipmentStatus
  location: string
  manufacturer?: string
  purchaseDate?: string
  warrantyExpiry?: string
  maintenanceCycle?: number
  lastMaintenance?: string
  nextMaintenance?: string
  responsibleUser?: User
  description?: string
  createdAt: string
}

export type EquipmentType = 
  | '磨床'
  | '深孔钻'
  | '精雕机'
  | '电火花成型机'
  | '中走丝'
  | '切槽机'

export type EquipmentStatus = 
  | '正常'
  | '维修中'
  | '保养中'
  | '停机'
  | '报废'

// 导入用户类型
import type { User } from './user'