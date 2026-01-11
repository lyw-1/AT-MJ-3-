const TOKEN_KEY = 'token'
const USER_INFO_KEY = 'user_info'

// 获取token
export const getToken = (): string | null => {
  return localStorage.getItem(TOKEN_KEY)
}

// 设置token
export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token)
}

// 移除token
export const removeToken = (): void => {
  localStorage.removeItem(TOKEN_KEY)
}

// 获取用户信息
export const getUserInfo = (): any | null => {
  const userInfo = localStorage.getItem(USER_INFO_KEY)
  return userInfo ? JSON.parse(userInfo) : null
}

// 设置用户信息
export const setUserInfo = (userInfo: any): void => {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
}

// 移除用户信息
export const removeUserInfo = (): void => {
  localStorage.removeItem(USER_INFO_KEY)
}

// 清除所有认证信息
export const clearAuth = (): void => {
  removeToken()
  removeUserInfo()
}