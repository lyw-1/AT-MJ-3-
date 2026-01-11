import request from '@/utils/request'
import type { LoginCredentials } from '@/types'


// 登录
export const login = async (credentials: LoginCredentials): Promise<any> => {
  try {
    console.log('[API-LOGIN] 发送登录请求:', credentials);
    // 直接使用fetch API调用登录接口
    const response = await fetch('http://localhost:8082/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    });
    
    console.log('[API-LOGIN] 响应状态:', response.status);
    const data = await response.json();
    console.log('[API-LOGIN] 响应数据:', data);
    
    // 直接返回完整响应数据，让上层处理
    return data;
  } catch (error) {
    console.error('[API-LOGIN] 登录请求失败:', error);
    throw error;
  }
}

// 获取用户信息
export const getUserInfo = (): Promise<any> => {
  return request.get('/auth/userinfo')
}

// 刷新token
export const refreshToken = (refreshToken: string): Promise<any> => {
  return request.post('/auth/refresh', { refreshToken })
}

// 登出
export const logout = (): Promise<any> => {
  return request.post('/auth/logout')
}

// 修改密码
export const changePassword = (oldPassword: string, newPassword: string): Promise<any> => {
  return request.put('/auth/password', { oldPassword, newPassword })
}

// 重置密码
export const resetPassword = (username: string, email: string): Promise<any> => {
  return request.post('/auth/reset-password', { username, email })
}