import request from '@/utils/request'

// 定义用户相关的类型
export interface User {
  id?: number
  username: string
  password: string
  realName: string
  phone: string
  status: number
  email?: string
  department?: string
  departmentName?: string
  roles?: string[]
  lastLogin?: string
  createTime?: string
  locked?: boolean
  remarks?: string
}

export interface UserCreateRequest {
  username: string
  password: string
  realName: string
  phone: string
  status: number
  email?: string
  department?: string
}

export interface UserUpdateRequest {
  username?: string
  realName?: string
  phone?: string
  status?: number
  email?: string
  departmentName?: string
}

export interface UserResponse {
  id: number
  username: string
  realName: string
  phone: string
  status: number
  email?: string
  departmentName?: string
  roles: string[]
  lastLogin?: string
  createTime?: string
  locked?: boolean
  remarks?: string
}

// 获取所有用户
export const getAllUsers = () => {
  return request.get<UserResponse[]>('/api/users')
}

// 根据ID获取用户
export const getUserById = (id: number) => {
  return request.get<UserResponse>(`/api/users/${id}`)
}

// 根据用户名获取用户
export const getUserByUsername = (username: string) => {
  return request.get<UserResponse>(`/api/users/username/${username}`)
}

// 创建用户
export const createUser = (data: UserCreateRequest) => {
  return request.post<UserResponse>('/api/users', data)
}

// 更新用户
export const updateUser = (id: number, data: UserUpdateRequest) => {
  return request.put(`/api/users/${id}`, data)
}

// 删除用户
export const deleteUser = (id: number) => {
  return request.delete(`/api/users/${id}`)
}

// 重置用户密码
export const resetUserPassword = (id: number, password: string) => {
  return request.post(`/api/users/${id}/reset-password`, { newPassword: password })
}

// 修改用户密码（需要原密码验证）
export const changeUserPassword = (id: number, oldPassword: string, newPassword: string) => {
  return request.post(`/api/users/${id}/change-password`, { oldPassword, newPassword })
}

// 为用户批量分配角色
export const batchAssignRolesToUser = (userId: number, roleIds: number[]) => {
  return request.post(`/api/user-roles/batch-assign?userId=${userId}`, roleIds)
}
