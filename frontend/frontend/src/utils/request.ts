import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.MODE === 'development' 
    ? ''  // 开发模式直接使用相对路径，避免URL重复
    : import.meta.env.VITE_API_BASE_URL || 'http://localhost:8082/api',  // 预览模式使用环境变量或默认地址
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 开发模式日志：记录请求开始
    const useDev = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    console.log(`[REQUEST] ${config.method?.toUpperCase()} ${config.url}`)
    console.log(`[REQUEST] Full URL: ${config.baseURL}${config.url}`)
    console.log(`[REQUEST] BaseURL: ${config.baseURL}`)
    console.log(`[REQUEST] Headers:`, config.headers)
    console.log(`[REQUEST] Data:`, config.data)
    
    // 登录请求不添加Authorization头，避免无效token导致401
    const isLoginRequest = config.url && (config.url.includes('/auth/login') || config.url.includes('/login'))
    if (!isLoginRequest) {
      // 从localStorage直接获取token，避免在拦截器中使用Pinia store
      const token = localStorage.getItem('token')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
        if (useDev) {
          console.log(`[DEV-REQUEST] Authorization: Bearer ${token.slice(0, 10)}...`)
        }
      }
    }
    
    // 开发模式下添加Dev-Token，解决删除操作401问题
    if (useDev) {
      const devToken = import.meta.env.VITE_DEV_TOKEN || 'dev-123'
      config.headers['X-Dev-Token'] = devToken
      console.log(`[DEV-REQUEST] X-Dev-Token: ${devToken}`)
    }
    
    // 确保所有process相关请求都添加X-Dev-Token
    if (config.url && (config.url.includes('/api/process-preset') || config.url.includes('/api/process'))) {
      const devToken = import.meta.env.VITE_DEV_TOKEN || 'dev-123'
      config.headers['X-Dev-Token'] = devToken
      console.log(`[DEV-REQUEST] Forced X-Dev-Token for process: ${devToken}`)
    }
    
    return config
  },
  (error) => {
    // 开发模式日志：记录请求错误
    const useDev = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    console.error(`[REQUEST-ERROR] ${error.message}`)
    if (useDev) {
      console.error(`[DEV-REQUEST-ERROR] ${error.message}`)
    }
    return Promise.reject(error)
  }
)

// 错误处理工具函数
const showErrorMessage = (message: string, type: 'error' | 'warning' | 'info' = 'error') => {
  // 统一错误提示样式
  ElMessage({
    message,
    type,
    duration: 3000,
    showClose: true
  })
}

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data, status, statusText, config } = response
    
    // 开发模式日志：记录响应成功
    console.log(`[RESPONSE] ${config.method?.toUpperCase()} ${config.url} ${status} ${statusText}`)
    console.log(`[RESPONSE] Data:`, data)
    
    // 处理成功响应
    // 兼容三种格式：
    // 1. { code: 200, data: ... }  // 标准格式，返回data.data
    // 2. { code: 200, data: { accessToken: ... } } // 登录响应格式，返回data.data
    // 3. 直接返回数据
    if (data && typeof data === 'object' && 'code' in data) {
      if (data.code === 200) {
        // 返回data.data字段，不管是否是登录响应
        return data.data
      } else {
        // 处理业务错误
        showErrorMessage(data.message || '请求失败', 'error')
        return Promise.reject(new Error(data.message || '请求失败'))
      }
    } else {
      // 直接返回数据的情况，包装成前端期望的格式
      return data
    }
  },
  (error) => {
    const useDev = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    
    // 开发模式日志：记录响应错误
    console.error(`[RESPONSE-ERROR] ${error.message}`)
    if (error.response) {
      console.error(`[RESPONSE-ERROR] Status: ${error.response.status} ${error.response.statusText}`)
      console.error(`[RESPONSE-ERROR] Data:`, error.response.data)
      console.error(`[RESPONSE-ERROR] Config:`, error.config)
    } else if (error.request) {
      console.error(`[RESPONSE-ERROR] No response received`)
      console.error(`[RESPONSE-ERROR] Request:`, error.request)
    } else {
      console.error(`[RESPONSE-ERROR] Request setup error:`, error.message)
    }
    
    if (error.response) {
      const { status, data, config } = error.response
      
      // 排除配置同步接口的401错误处理
      if (config && config.url && config.url.includes('/api/dev-config')) {
        console.warn('[DEV-CONFIG-SYNC] 配置同步接口返回401，跳过登出处理')
        return Promise.reject(error)
      }
      
      // 登录请求特殊处理
      const isLoginRequest = config && config.url && (config.url.includes('/login') || config.url.includes('/auth/login'))
      
      // 更具体的错误信息
      const errorMessages: Record<number, string> = {
        400: data?.message || '请求参数错误，请检查输入内容',
        401: isLoginRequest ? '用户名或密码错误' : '登录已过期，请重新登录',
        403: '没有权限访问该功能，请联系管理员',
        404: `请求的资源不存在: ${config?.url}`,
        405: '请求方法不允许',
        406: '请求的内容无法满足客户端要求',
        407: '需要代理认证',
        408: '请求超时，请检查网络连接',
        409: data?.message || '数据冲突，请检查申请编号或模号是否已存在',
        410: '请求的资源已被永久移除',
        429: '请求过于频繁，请稍后重试（建议1分钟后再试）',
        500: '服务器内部错误，请联系管理员或稍后重试',
        501: '服务器不支持该请求方法',
        502: '网关错误，请联系管理员',
        503: '服务器暂时不可用，请稍后重试',
        504: '网关超时，请检查网络连接或联系管理员'
      }
      
      // 获取对应的错误信息
      // 对process相关请求进行特殊处理，避免显示"登录过期"信息
      const isProcessRequest = config && config.url && (config.url.includes('/api/process-preset') || config.url.includes('/api/process'))
      const errorMessage = status === 401 && isProcessRequest 
        ? '获取数据失败，请检查后端配置' 
        : errorMessages[status as keyof typeof errorMessages] || (isLoginRequest ? '登录失败，请检查用户名和密码' : data?.message || '请求失败')
      
      // 处理401特殊情况
      if (status === 401 && !isLoginRequest && !useDev) {
        // 检查是否为process相关请求，如果是，则不跳转到登录页面
        const isProcessRequest = config && config.url && (config.url.includes('/api/process-preset') || config.url.includes('/api/process'))
        if (!isProcessRequest) {
          // 检查当前是否已经在登录页面，避免重复显示登录过期提示
          const isLoginPage = window.location.pathname === '/login'
          if (!isLoginPage) {
            // 清除token和localStorage，避免使用Pinia store
            localStorage.removeItem('token')
            localStorage.removeItem('user_info')
            
            // 立即跳转到登录页面，不需要延迟
            window.location.href = '/login'
          }
        }
      }
      
      // 显示错误信息，但是对于process相关的401错误，不显示
      if (!(status === 401 && isProcessRequest)) {
        showErrorMessage(errorMessage, 'error')
      }
    } else if (error.request) {
      // 网络错误，添加重试建议
      showErrorMessage('网络错误，请检查网络连接，稍后重试', 'warning')
    } else {
      // 请求配置错误
      showErrorMessage('请求配置错误，请联系管理员', 'error')
    }
    
    return Promise.reject(error)
  }
)

// 添加带重试机制的请求方法
export const requestWithRetry = async (config: AxiosRequestConfig, maxRetries: number = 2, delay: number = 1000): Promise<any> => {
  let retryCount = 0
  
  while (retryCount <= maxRetries) {
    try {
      return await request(config)
    } catch (error: any) {
      retryCount++
      
      if (retryCount > maxRetries) {
        throw error
      }
      
      // 只对特定错误进行重试
      const isRetryable = error.response?.status >= 500 || !error.response
      if (isRetryable) {
        console.log(`[REQUEST-RETRY] Retrying request (${retryCount}/${maxRetries})...`)
        await new Promise(resolve => setTimeout(resolve, delay))
      } else {
        throw error
      }
    }
  }
  
  throw new Error('Max retries exceeded')
}

export default request