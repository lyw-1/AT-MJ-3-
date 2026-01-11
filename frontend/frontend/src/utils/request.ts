import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.MODE === 'development' 
    ? '/api'  // 开发模式使用vite代理，避免URL重复
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
        ElMessage.error(data.message || '请求失败')
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
      
      switch (status) {
        case 401:
          // 未授权，清除token并跳转登录
          if (useDev) {
            console.error('[DEV-AUTH] 401 Unauthorized - 开发模式下跳过登出处理')
            // 开发模式下，不执行登出操作，避免影响开发体验
            return Promise.reject(error)
          }
          
          // 检查当前是否已经在登录页面，避免重复显示登录过期提示
          const isLoginPage = window.location.pathname === '/login'
          
          if (isLoginRequest) {
            // 登录请求返回401，说明用户名或密码错误
            ElMessage.error('用户名或密码错误')
          } else if (!isLoginPage) {
            // 只有在非登录页面才显示登录过期提示，避免在登录页面闪烁
            // 直接清除token和localStorage，避免使用Pinia store
            localStorage.removeItem('token')
            localStorage.removeItem('user_info')
            window.location.href = '/login'
            ElMessage.error('登录已过期，请重新登录')
          }
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 405:
          ElMessage.error('请求方法不允许')
          break
        case 406:
          ElMessage.error('请求的内容无法满足客户端要求')
          break
        case 407:
          ElMessage.error('需要代理认证')
          break
        case 408:
          ElMessage.error('请求超时')
          break
        case 409:
          ElMessage.error(data?.message || '数据冲突，请检查申请编号或模号是否已存在')
          break
        case 410:
          ElMessage.error('请求的资源已被永久移除')
          break
        case 429:
          ElMessage.error('请求过于频繁，请稍后重试')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        case 501:
          ElMessage.error('服务器不支持该请求方法')
          break
        case 502:
          ElMessage.error('网关错误')
          break
        case 503:
          ElMessage.error('服务器暂时不可用')
          break
        case 504:
          ElMessage.error('网关超时')
          break
        default:
          // 根据请求类型显示不同的错误信息
          if (isLoginRequest) {
            ElMessage.error('登录失败，请检查用户名和密码')
          } else {
            ElMessage.error(data?.message || '请求失败')
          }
      }
    } else if (error.request) {
      // 网络错误，添加重试建议
      ElMessage.error('网络错误，请检查网络连接，稍后重试')
    } else {
      // 请求配置错误
      ElMessage.error('请求配置错误，请联系管理员')
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