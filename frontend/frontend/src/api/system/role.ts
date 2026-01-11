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
  return request.get<Role[]>('/roles')
}

// 根据ID获取角色
export const getRoleById = (id: number) => {
  return request.get<Role>(`/roles/${id}`)
}

// 根据角色编码获取角色
export const getRoleByCode = (roleCode: string) => {
  return request.get<Role>(`/roles/code/${roleCode}`)
}

// 检查角色编码是否存在
export const checkRoleCode = (roleCode: string, excludeId?: number) => {
  const params: any = { roleCode }
  if (excludeId !== undefined) {
    params.excludeId = excludeId
  }
  return request.get<{ exists: boolean }>('/roles/check-code', { params })
}

// 创建角色
export const createRole = (role: Role) => {
  return request.post<Role>('/roles', role)
}

// 更新角色
export const updateRole = (id: number, role: Partial<Role>) => {
  return request.put(`/roles/${id}`, role)
}

// 更新角色状态
export const updateRoleStatus = (id: number, status: number) => {
  return request.put(`/roles/${id}/status`, { status })
}

// 删除角色
export const deleteRole = (id: number) => {
  return request.delete(`/roles/${id}`)
}

// 分页查询角色列表
export const getRoleList = (page: number, pageSize: number, keyword?: string) => {
  return request.get<{ list: Role[], total: number }>('/roles/page', {
    params: {
      page,
      pageSize,
      keyword
    }
  })
}
