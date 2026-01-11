import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginCredentials, AuthResponse } from '@/types'
import { login as apiLogin, getUserInfo as apiGetUserInfo } from '@/api/auth'
import { removeToken, setToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  // 状态初始化
  const storedToken = localStorage.getItem('token')
  const token = ref<string>(storedToken || '')
  
  // 正确初始化userInfo，先获取本地存储数据
  const storedUserInfo = localStorage.getItem('user_info')
  const userInfo = ref<User | null>(storedUserInfo ? JSON.parse(storedUserInfo) : null)
  
  // 正确初始化permissions，从userInfo中提取
  const initPermissions = (): string[] => {
    // 从 userInfo 中提取权限，增强错误处理
    if (userInfo.value && userInfo.value.roles && Array.isArray(userInfo.value.roles)) {
      return userInfo.value.roles.flatMap(role => {
        if (role && role.permissions && Array.isArray(role.permissions)) {
          return role.permissions.map(p => p.code || '').filter(Boolean)
        }
        return []
      })
    }
    return []
  }
  
  const permissions = ref<string[]>(initPermissions())
  
  // 初始化时确保token值正确
  if (storedToken) {
    token.value = storedToken
  }

  // 计算属性
  const isLogin = computed(() => !!token.value)
  const hasPermission = computed(() => (permission: string) => {
    return permissions.value.includes(permission)
  })

  // 登录
  const login = async (credentials: LoginCredentials) => {
    const useDev = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    let tokenVal: string = ''
    let userId: number = 1
    let username: string = 'admin'
    let realName: string = '系统管理员'
    let roles: any = []

    try {
      if (!useDev) {
        // 直接调用登录API
        const loginResult = await apiLogin(credentials)
        console.log('[STORE-LOGIN] 登录API返回结果:', loginResult);
        
        // 检查返回数据的结构
        let responseData = loginResult;
        if (loginResult && typeof loginResult === 'object' && 'code' in loginResult) {
          // 如果是标准响应格式 {code: 200, message: 'success', data: {...}}
          responseData = loginResult.data;
        }
        
        // 检查responseData是否为数组，如果是数组则抛出错误
        if (Array.isArray(responseData)) {
          console.error('[STORE-LOGIN] 登录API返回了数组而不是预期的对象:', responseData);
          throw new Error('登录失败，服务器返回了无效的数据格式');
        }
        
        if (responseData) {
          // 直接从responseData中获取accessToken或token
          tokenVal = responseData.accessToken || responseData.token || ''
          userId = responseData.userId || responseData.user_id || 1
          username = responseData.username || 'admin'
          realName = responseData.realName || responseData.name || responseData.username || '系统管理员'
          roles = responseData.roles || []
        } else {
          // API调用返回了无效数据
          throw new Error('登录失败，服务器返回无效数据')
        }
      }

      // 检查是否有token，或者是否是开发模式
      if (tokenVal || useDev) {
        // 如果是开发模式，生成开发token
        if (useDev && !tokenVal) {
          tokenVal = 'dev-token-' + Math.random().toString(36).slice(2)
          username = credentials.username || 'admin'
        }

        // 确保roles是数组格式
        let rolesArray: any[] = [];
        if (typeof roles === 'string') {
          // 如果是字符串，处理特殊情况"null"和空字符串
          if (roles === 'null' || roles.trim() === '') {
            rolesArray = [];
          } else if (roles.includes(',')) {
            // 如果是逗号分隔的角色代码，分割成数组
            rolesArray = roles.split(',').map(r => r.trim()).filter(Boolean);
          } else {
            // 单个角色代码，转换为数组
            rolesArray = [roles];
          }
        } else if (Array.isArray(roles)) {
          // 如果已经是数组，直接使用并过滤掉无效值
          rolesArray = roles.filter(role => role && role !== 'null' && role !== null);
        } else {
          // 否则使用空数组
          rolesArray = [];
        }

        // 如果是开发模式且没有角色，添加默认角色
        if (useDev && rolesArray.length === 0) {
          rolesArray = ['admin', 'user'];
        }

        // 构建user对象
        const user: User = {
          id: userId,
          username: username,
          name: realName,
          roles: rolesArray.map(role => ({
            id: 1,
            name: role,
            code: role,
            permissions: []
          })),
          status: 'active',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        } as any

        // 保存登录信息
        token.value = tokenVal
        userInfo.value = user
        setToken(tokenVal)
        localStorage.setItem('user_info', JSON.stringify(user))

        // 设置权限
        permissions.value = ['mold:initial-params:view', 'mold:initial-params:create', 'mold:initial-params:update', 'mold:initial-params:delete', 'consumables:view', 'products:view']

        // 简化返回结果，移除类型断言，避免类型错误
        return { code: 200, message: 'login success', data: { token: tokenVal, user: user } }
      } else {
        // 登录失败，没有找到token
        throw new Error('登录失败，未获取到有效的认证令牌')
      }
    } catch (error: any) {
      console.error('[STORE-LOGIN] 登录过程中发生错误:', error);
      // 开发模式下的兜底登录
      if (useDev) {
        const devToken = 'dev-token-' + Math.random().toString(36).slice(2)
        const devUser: User = {
          id: 1,
          username: credentials.username || 'admin',
          name: '开发用户',
          roles: [
            {
              id: 1,
              name: '管理员',
              code: 'admin',
              permissions: []
            },
            {
              id: 2,
              name: '用户',
              code: 'user',
              permissions: []
            }
          ],
          status: 'active',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        } as any
        
        token.value = devToken
        userInfo.value = devUser
        setToken(devToken)
        localStorage.setItem('user_info', JSON.stringify(devUser))
        permissions.value = ['mold:initial-params:view', 'mold:initial-params:create', 'mold:initial-params:update', 'mold:initial-params:delete', 'consumables:view', 'products:view']
        
        // 简化返回结果，移除类型断言，避免类型错误
        return { code: 200, message: 'dev login', data: { token: devToken, user: devUser } }
      }
      
      // 确保错误被正确抛出，让调用者能够捕获
      throw error
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    removeToken()
    localStorage.removeItem('user_info')
  }

  // 获取用户信息
  const getUserInfo = async () => {
    try {
      const response = await apiGetUserInfo()
      if (response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('user_info', JSON.stringify(response.data))
        
        // 提取权限
        // 增强的权限提取逻辑，增加错误处理和默认值
        if (response.data.roles && Array.isArray(response.data.roles)) {
          permissions.value = response.data.roles.flatMap((role: any) => {
            // 确保role.permissions是数组
            if (role.permissions && Array.isArray(role.permissions)) {
              return role.permissions.map((permission: any) => permission.code || '')
            }
            return []
          }).filter((code: string) => code) // 过滤掉空字符串
        } else {
          // 默认权限，确保开发模式下能正常访问
          permissions.value = ['user:view', 'role:view', 'department:view']
        }
        
        return Promise.resolve(response.data)
      } else {
        return Promise.reject(new Error(response.message))
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 更新用户信息
  const updateUserInfo = (user: User) => {
    userInfo.value = user
    localStorage.setItem('user_info', JSON.stringify(user))
    // 重新提取权限
    if (user.roles) {
      permissions.value = user.roles.flatMap(role => role.permissions.map(p => p.code))
    }
  }

  // 检查权限
  const checkPermission = (permission: string) => {
    return permissions.value.includes(permission)
  }

  // 检查角色
  const hasRole = (roleCode: string) => {
    if (!userInfo.value || !userInfo.value.roles) return false
    return userInfo.value.roles.some(role => role.code === roleCode)
  }

  return {
    // 状态
    token,
    userInfo,
    permissions,
    
    // 计算属性
    isLogin,
    hasPermission,
    
    // 方法
    login,
    logout,
    getUserInfo,
    updateUserInfo,
    checkPermission,
    hasRole
  }
})
