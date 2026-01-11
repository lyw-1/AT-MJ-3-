import request from '@/utils/request'

// 定义部门相关的类型
export interface Department {
  id?: number
  name: string
  parentId?: number
  parentName?: string
  sortOrder?: number
  status: number
  createTime?: string
  updateTime?: string
  children?: Department[]
}

// 获取部门列表
export const getDepartments = (keyword?: string) => {
  return request.get<Department[]>('/departments', {
    params: { keyword }
  })
}

// 获取部门树
export const getDepartmentTree = (keyword?: string) => {
  return request.get<Department[]>('/departments/tree', {
    params: { keyword }
  })
}

// 获取指定部门的子部门
export const getDepartmentChildren = (parentId: number) => {
  return request.get<Department[]>(`/departments/children/${parentId}`)
}

// 检查部门是否有子部门
export const checkDepartmentHasChildren = (id: number) => {
  return request.get<{ hasChildren: boolean }>(`/departments/has-children/${id}`)
}

// 根据ID获取部门
export const getDepartmentById = (id: number) => {
  return request.get<Department>(`/departments/${id}`)
}

// 检查部门名称是否存在
export const checkDepartmentName = (name: string, excludeId?: number) => {
  const params: any = { name }
  if (excludeId !== undefined) {
    params.excludeId = excludeId
  }
  return request.get<{ exists: boolean }>('/departments/check-name', { params })
}

// 创建部门
export const createDepartment = (department: Department) => {
  return request.post<Department>('/departments', department)
}

// 更新部门
export const updateDepartment = (id: number, department: Partial<Department>) => {
  return request.put(`/departments/${id}`, department)
}

// 删除部门
export const deleteDepartment = (id: number) => {
  return request.delete(`/departments/${id}`)
}
