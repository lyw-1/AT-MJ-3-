import request from '@/utils/request'

// 定义角色相关的类型
export interface Role {
  id?: number
  roleName: string
  roleCode: string
  description?: string
  status: number
  createTime?: string
  updateTime?: string
}

// 获取所有角色
export const getAllRoles = () => {
  return request.get<Role[]>('/api/roles')
}

// 根据ID获取角色
export const getRoleById = (id: number) => {
  return request.get<Role>(`/api/roles/${id}`)
}

// 根据角色编码获取角色
export const getRoleByCode = (roleCode: string) => {
  return request.get<Role>(`/api/roles/code/${roleCode}`)
}

// 检查角色编码是否存在
export const checkRoleCode = (roleCode: string, excludeId?: number) => {
  const params: any = { roleCode }
  if (excludeId !== undefined) {
    params.excludeId = excludeId
  }
  return request.get<{ exists: boolean }>('/api/roles/check-code', { params })
}

// 创建角色
export const createRole = (role: Role) => {
  return request.post<Role>('/api/roles', role)
}

// 更新角色
export const updateRole = (id: number, role: Partial<Role>) => {
  return request.put(`/api/roles/${id}`, role)
}

// 更新角色状态
export const updateRoleStatus = (id: number, status: number) => {
  return request.put(`/api/roles/${id}/status`, { status })
}

// 删除角色
export const deleteRole = (id: number) => {
  return request.delete(`/api/roles/${id}`)
}

// 分页查询角色列表
export const getRoleList = (page: number, pageSize: number, keyword?: string) => {
  return request.get<{ list: Role[], total: number }>('/api/roles/page', {
    params: {
      page,
      pageSize,
      keyword
    }
  })
}
