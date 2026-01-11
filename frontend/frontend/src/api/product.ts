// 成品管理相关API
import request from '@/utils/request'
import { logApiResponse } from '@/utils/logUtils'

// 范围区间类型
export interface Range {
  min: number // 最小值
  max: number // 最大值
}

// 后端返回的成品类型（带独立的范围字段）
interface BackendProduct {
  id?: number
  productCategory: string
  productSpec: string
  densityRequirementMin?: number
  densityRequirementMax?: number
  wallThicknessRequirement?: number
  slotWidthRequirementMin?: number
  slotWidthRequirementMax?: number
  otherRequirements?: string
  customer?: string
  createTime?: string
  updateTime?: string
}

// 成品类型定义（前端使用，带Range范围字段）
export interface Product {
  id?: number
  productCategory: string // 成品类别
  productSpec: string // 成品规格
  densityRequirement?: Range // 容重要求，范围区间
  wallThicknessRequirement?: number // 壁厚要求
  slotWidthRequirement?: Range // 槽宽要求，范围区间
  otherRequirements?: string // 其他特殊要求
  customer?: string // 客户名称
  createTime?: string // 创建时间
  updateTime?: string // 更新时间
}

// 成品查询参数
export interface ProductQuery {
  productCategory?: string
  productSpec?: string
  page?: number
  size?: number
}

/**
 * 将后端返回的成品数据转换为前端需要的格式
 * @param backendProduct 后端返回的成品数据
 * @returns 前端需要的成品数据格式
 */
const convertToFrontendProduct = (backendProduct: BackendProduct): Product => {
  return {
    id: backendProduct.id,
    productCategory: backendProduct.productCategory || '',
    productSpec: backendProduct.productSpec || '',
    densityRequirement: backendProduct.densityRequirementMin !== undefined && backendProduct.densityRequirementMax !== undefined
      ? {
          min: backendProduct.densityRequirementMin,
          max: backendProduct.densityRequirementMax
        }
      : undefined,
    wallThicknessRequirement: backendProduct.wallThicknessRequirement,
    slotWidthRequirement: backendProduct.slotWidthRequirementMin !== undefined && backendProduct.slotWidthRequirementMax !== undefined
      ? {
          min: backendProduct.slotWidthRequirementMin,
          max: backendProduct.slotWidthRequirementMax
        }
      : undefined,
    otherRequirements: backendProduct.otherRequirements || '',
    customer: backendProduct.customer || '',
    createTime: backendProduct.createTime,
    updateTime: backendProduct.updateTime
  }
}

/**
 * 将前端的成品数据转换为后端需要的格式
 * @param frontendProduct 前端的成品数据
 * @returns 后端需要的成品数据格式
 */
const convertToBackendProduct = (frontendProduct: Product): BackendProduct => {
  return {
    id: frontendProduct.id,
    productCategory: frontendProduct.productCategory,
    productSpec: frontendProduct.productSpec,
    densityRequirementMin: frontendProduct.densityRequirement?.min,
    densityRequirementMax: frontendProduct.densityRequirement?.max,
    wallThicknessRequirement: frontendProduct.wallThicknessRequirement,
    slotWidthRequirementMin: frontendProduct.slotWidthRequirement?.min,
    slotWidthRequirementMax: frontendProduct.slotWidthRequirement?.max,
    otherRequirements: frontendProduct.otherRequirements,
    customer: frontendProduct.customer,
    createTime: frontendProduct.createTime,
    updateTime: frontendProduct.updateTime
  }
}

// 获取成品列表
export const getProducts = async (params?: ProductQuery) => {
  console.log('调用getProducts API，参数:', params)
  try {
    const response = await request({
      url: '/api/products',
      method: 'get',
      params
    })
    
    logApiResponse('getProducts', response)
    
    // 确保response是对象且不为null
    if (!response || typeof response !== 'object') {
      console.error('API返回数据格式异常，不是对象:', response)
      return {
        records: [],
        total: 0
      }
    }
    
    // 直接使用response，因为响应拦截器已经处理过格式
    let resultData = response
    
    console.log('处理前的resultData:', resultData)
    console.log('resultData.constructor.name:', resultData.constructor.name)
    console.log('resultData类型:', typeof resultData)
    
    // 初始化默认值
    let records: BackendProduct[] = []
    let total = 0
    
    // 检查resultData是否有records属性
    if ('records' in resultData) {
      console.log('resultData包含records属性')
      console.log('resultData.records:', resultData.records)
      console.log('resultData.records类型:', typeof resultData.records)
      
      // 确保records是数组
      if (Array.isArray(resultData.records)) {
        // 转换数据格式 - 处理后端返回的分页数据
        records = resultData.records.map((item: BackendProduct | null | undefined) => {
          // 确保item不是null或undefined
          if (!item || typeof item !== 'object') {
            console.error('API返回的records中包含无效对象:', item)
            return {
              id: undefined,
              productCategory: '',
              productSpec: '',
              densityRequirementMin: undefined,
              densityRequirementMax: undefined,
              wallThicknessRequirement: undefined,
              slotWidthRequirementMin: undefined,
              slotWidthRequirementMax: undefined,
              otherRequirements: '',
              customer: '',
              createTime: undefined,
              updateTime: undefined
            }
          }
          return item
        })
      } else {
        console.error('API返回的records不是数组:', resultData.records)
      }
    } else {
      console.error('resultData不包含records属性，resultData结构:', JSON.stringify(resultData, null, 2))
    }
    
    // 确保total是数字
    if ('total' in resultData && typeof resultData.total === 'number') {
      total = resultData.total
    } else {
      console.error('API返回的total不是数字:', resultData.total)
      total = 0
    }
    
    // 转换records为前端使用的Product格式
    const transformedRecords = records.map(convertToFrontendProduct)
    
    const finalResult = {
      records: transformedRecords,
      total: total
    }
    
    console.log('API响应处理后数据:', finalResult)
    
    // 返回分页数据，与前端期望的结构一致
    return finalResult
  } catch (error) {
    console.error('获取成品列表失败:', error)
    return {
      records: [],
      total: 0
    }
  }
}

// 获取单个成品详情
export const getProductDetail = async (id: number) => {
  console.log('调用getProductDetail API，id:', id)
  const response = await request({
    url: `/api/products/${id}`,
    method: 'get'
  })
  
  console.log('API响应原始数据:', response)
  
  // 确保response是对象且不为null
  if (!response || typeof response !== 'object') {
    console.error('API返回数据格式异常，不是对象:', response)
    return null
  }
  
  // 转换数据格式 - 从response中获取产品数据（响应拦截器已经处理过格式）
  const productData = response
  
  // 确保productData是对象且不为null
  if (!productData || typeof productData !== 'object') {
    console.error('API返回的产品数据格式异常:', productData)
    return null
  }
  
  const product = convertToFrontendProduct(productData)
  console.log('转换后的产品数据:', product)
  
  return product
}

// 创建成品
export const createProduct = async (data: Product) => {
  console.log('调用createProduct API，data:', data)
  // 转换为后端需要的数据格式
  const backendData = convertToBackendProduct(data)
  
  const response = await request({
    url: '/api/products',
    method: 'post',
    data: backendData
  })
  
  console.log('API响应原始数据:', response)
  
  // 确保response是对象且不为null
  if (!response || typeof response !== 'object') {
    console.error('API返回数据格式异常，不是对象:', response)
    throw new Error('创建产品失败，返回数据格式异常')
  }
  
  // 转换数据格式 - 直接使用response，因为响应拦截器已经处理过格式
  return convertToFrontendProduct(response)
}

// 更新成品
export const updateProduct = async (id: number, data: Product) => {
  console.log('调用updateProduct API，id:', id, 'data:', data)
  // 转换为后端需要的数据格式
  const backendData = convertToBackendProduct(data)
  
  const response = await request({
    url: `/api/products/${id}`,
    method: 'put',
    data: backendData
  })
  
  console.log('API响应原始数据:', response)
  
  // 确保response是对象且不为null
  if (!response || typeof response !== 'object') {
    console.error('API返回数据格式异常，不是对象:', response)
    throw new Error('更新产品失败，返回数据格式异常')
  }
  
  // 转换数据格式 - 直接使用response，因为响应拦截器已经处理过格式
  return convertToFrontendProduct(response)
}

// 删除成品
export const deleteProduct = (id: number) => {
  return request({
    url: `/api/products/${id}`,
    method: 'delete'
  })
}

// 批量删除成品
export const batchDeleteProducts = (ids: number[]) => {
  return request({
    url: '/api/products/batch-delete',
    method: 'delete',
    data: ids // 注意：后端期望的是数组，直接传递ids
  })
}

// 导出成品列表
export const exportProducts = (params?: ProductQuery) => {
  // 注意：导出函数返回的是blob，不是JSON数据，所以需要跳过响应拦截器的处理
  // 使用axios.create创建一个新的实例，不使用默认的响应拦截器
  const axios = require('axios')
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 10000
  })
  
  return instance({
    url: '/api/products/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

// 导入成品列表
export const importProducts = (data: FormData) => {
  return request({
    url: '/api/products/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
