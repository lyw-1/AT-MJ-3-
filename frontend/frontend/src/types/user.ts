export interface User {
  id: number
  username: string
  name: string
  email?: string
  phone?: string
  avatar?: string
  roles: Role[]
  department?: Department
  status: 'active' | 'inactive'
  createdAt: string
  updatedAt: string
}

export interface Role {
  id: number
  name: string
  code: string
  permissions: Permission[]
  description?: string
}

export interface Permission {
  id: number
  name: string
  code: string
  menu?: string
  action?: string
}

export interface Department {
  id: number
  name: string
  code: string
  description?: string
}

export interface LoginCredentials {
  username: string
  password: string
  rememberMe?: boolean
}

export interface AuthResponse {
  code: number
  message: string
  data: {
    token: string
    user: User
  }
}