import type { User } from '@/types'

/**
 * 日志级别枚举
 */
export enum LogLevel {
  DEBUG = 'debug',
  INFO = 'info',
  WARN = 'warn',
  ERROR = 'error'
}

/**
 * 操作类型枚举
 */
export enum OperationType {
  LOGIN = 'login',
  LOGOUT = 'logout',
  CREATE = 'create',
  UPDATE = 'update',
  DELETE = 'delete',
  SEARCH = 'search',
  VIEW = 'view',
  EXPORT = 'export',
  IMPORT = 'import',
  UPLOAD = 'upload',
  DOWNLOAD = 'download'
}

/**
 * 日志配置接口
 */
interface LogConfig {
  level: LogLevel
  prefix: string
  enableConsole: boolean
  enableApi: boolean
  apiUrl?: string
}

/**
 * 日志数据接口
 */
export interface LogData {
  id?: string
  timestamp: number
  level: LogLevel
  operationType: OperationType
  module: string
  description: string
  userId?: number
  username?: string
  realName?: string
  ip?: string
  browser?: string
  system?: string
  url?: string
  params?: any
  result?: any
  error?: string
}

/**
 * 日志服务类
 * 用于记录前端关键操作日志
 */
export class Logger {
  private config: LogConfig
  private user: User | null = null

  constructor(config?: Partial<LogConfig>) {
    this.config = {
      level: LogLevel.INFO,
      prefix: '[MOLD-LOGGER]',
      enableConsole: true,
      enableApi: true,
      apiUrl: '/api/logs',
      ...config
    }
  }

  /**
   * 设置当前用户
   */
  setUser(user: User | null): void {
    this.user = user
  }

  /**
   * 记录日志
   */
  log(
    operationType: OperationType,
    module: string,
    description: string,
    options?: {
      level?: LogLevel
      params?: any
      result?: any
      error?: string
    }
  ): void {
    const logData: LogData = {
      timestamp: Date.now(),
      level: options?.level || LogLevel.INFO,
      operationType,
      module,
      description,
      userId: this.user?.id,
      username: this.user?.username,
      realName: this.user?.name,
      ip: this.getClientIp(),
      browser: this.getBrowserInfo(),
      system: this.getSystemInfo(),
      url: window.location.href,
      params: options?.params,
      result: options?.result,
      error: options?.error
    }

    // 控制台输出
    if (this.config.enableConsole) {
      this.consoleLog(logData)
    }

    // API 上报
    if (this.config.enableApi) {
      this.apiLog(logData)
    }
  }

  /**
   * 记录调试日志
   */
  debug(
    operationType: OperationType,
    module: string,
    description: string,
    options?: {
      params?: any
      result?: any
      error?: string
    }
  ): void {
    this.log(operationType, module, description, { ...options, level: LogLevel.DEBUG })
  }

  /**
   * 记录信息日志
   */
  info(
    operationType: OperationType,
    module: string,
    description: string,
    options?: {
      params?: any
      result?: any
      error?: string
    }
  ): void {
    this.log(operationType, module, description, { ...options, level: LogLevel.INFO })
  }

  /**
   * 记录警告日志
   */
  warn(
    operationType: OperationType,
    module: string,
    description: string,
    options?: {
      params?: any
      result?: any
      error?: string
    }
  ): void {
    this.log(operationType, module, description, { ...options, level: LogLevel.WARN })
  }

  /**
   * 记录错误日志
   */
  error(
    operationType: OperationType,
    module: string,
    description: string,
    options?: {
      params?: any
      result?: any
      error?: string
    }
  ): void {
    this.log(operationType, module, description, { ...options, level: LogLevel.ERROR })
  }

  /**
   * 控制台输出日志
   */
  private consoleLog(logData: LogData): void {
    const { prefix } = this.config
    const { level, operationType, module, description, timestamp, params, error } = logData
    const time = new Date(timestamp).toISOString()
    const message = `${prefix} [${time}] [${level.toUpperCase()}] [${operationType}] [${module}] ${description}`

    switch (level) {
      case LogLevel.DEBUG:
        console.debug(message, params, error)
        break
      case LogLevel.INFO:
        console.info(message, params)
        break
      case LogLevel.WARN:
        console.warn(message, params, error)
        break
      case LogLevel.ERROR:
        console.error(message, params, error)
        break
    }
  }

  /**
   * API 上报日志
   */
  private async apiLog(logData: LogData): Promise<void> {
    try {
      if (!this.config.apiUrl) return

      await fetch(this.config.apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(logData)
      })
    } catch (error) {
      console.error('[MOLD-LOGGER] API 日志上报失败:', error)
    }
  }

  /**
   * 获取客户端 IP
   */
  private getClientIp(): string | undefined {
    // 前端无法直接获取客户端真实 IP，需要通过后端接口获取
    // 这里暂时返回空，实际项目中可以通过调用后端接口获取
    return undefined
  }

  /**
   * 获取浏览器信息
   */
  private getBrowserInfo(): string {
    const { userAgent } = navigator
    let browserName = 'Unknown'

    if (userAgent.includes('Chrome')) {
      browserName = 'Chrome'
    } else if (userAgent.includes('Firefox')) {
      browserName = 'Firefox'
    } else if (userAgent.includes('Safari')) {
      browserName = 'Safari'
    } else if (userAgent.includes('Edge')) {
      browserName = 'Edge'
    } else if (userAgent.includes('Opera')) {
      browserName = 'Opera'
    } else if (userAgent.includes('MSIE')) {
      browserName = 'Internet Explorer'
    }

    return `${browserName} ${this.getBrowserVersion(userAgent)}`
  }

  /**
   * 获取浏览器版本
   */
  private getBrowserVersion(userAgent: string): string {
    const versionRegex = /(Chrome|Firefox|Safari|Edge|Opera|MSIE)[/\s]*(\d+\.\d+)/i
    const match = userAgent.match(versionRegex)
    return match ? match[2] : 'Unknown'
  }

  /**
   * 获取系统信息
   */
  private getSystemInfo(): string {
    const { userAgent } = navigator
    let osName = 'Unknown'

    if (userAgent.includes('Windows')) {
      osName = 'Windows'
    } else if (userAgent.includes('Mac')) {
      osName = 'macOS'
    } else if (userAgent.includes('Linux')) {
      osName = 'Linux'
    } else if (userAgent.includes('Android')) {
      osName = 'Android'
    } else if (userAgent.includes('iOS')) {
      osName = 'iOS'
    }

    return osName
  }
}

// 创建全局日志实例
export const logger = new Logger()
