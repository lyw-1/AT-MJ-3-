// 存储工具类
class StorageManager {
  /**
   * 保存数据到LocalStorage
   * @param key 存储键
   * @param value 存储值
   */
  static setLocal(key: string, value: any): void {
    try {
      const stringValue = JSON.stringify(value)
      localStorage.setItem(key, stringValue)
    } catch (error) {
      console.error('Error saving to localStorage:', error)
    }
  }

  /**
   * 从LocalStorage获取数据
   * @param key 存储键
   * @returns 存储值，如果不存在则返回null
   */
  static getLocal<T>(key: string): T | null {
    try {
      const stringValue = localStorage.getItem(key)
      if (stringValue === null) {
        return null
      }
      return JSON.parse(stringValue) as T
    } catch (error) {
      console.error('Error getting from localStorage:', error)
      return null
    }
  }

  /**
   * 从LocalStorage删除数据
   * @param key 存储键
   */
  static removeLocal(key: string): void {
    try {
      localStorage.removeItem(key)
    } catch (error) {
      console.error('Error removing from localStorage:', error)
    }
  }

  /**
   * 清空LocalStorage
   */
  static clearLocal(): void {
    try {
      localStorage.clear()
    } catch (error) {
      console.error('Error clearing localStorage:', error)
    }
  }

  /**
   * 保存数据到SessionStorage
   * @param key 存储键
   * @param value 存储值
   */
  static setSession(key: string, value: any): void {
    try {
      const stringValue = JSON.stringify(value)
      sessionStorage.setItem(key, stringValue)
    } catch (error) {
      console.error('Error saving to sessionStorage:', error)
    }
  }

  /**
   * 从SessionStorage获取数据
   * @param key 存储键
   * @returns 存储值，如果不存在则返回null
   */
  static getSession<T>(key: string): T | null {
    try {
      const stringValue = sessionStorage.getItem(key)
      if (stringValue === null) {
        return null
      }
      return JSON.parse(stringValue) as T
    } catch (error) {
      console.error('Error getting from sessionStorage:', error)
      return null
    }
  }

  /**
   * 从SessionStorage删除数据
   * @param key 存储键
   */
  static removeSession(key: string): void {
    try {
      sessionStorage.removeItem(key)
    } catch (error) {
      console.error('Error removing from sessionStorage:', error)
    }
  }

  /**
   * 清空SessionStorage
   */
  static clearSession(): void {
    try {
      sessionStorage.clear()
    } catch (error) {
      console.error('Error clearing sessionStorage:', error)
    }
  }

  /**
   * 保存数据到指定的存储对象
   * @param storage 存储对象，localStorage或sessionStorage
   * @param key 存储键
   * @param value 存储值
   */
  static set(storage: Storage, key: string, value: any): void {
    try {
      const stringValue = JSON.stringify(value)
      storage.setItem(key, stringValue)
    } catch (error) {
      console.error('Error saving to storage:', error)
    }
  }

  /**
   * 从指定的存储对象获取数据
   * @param storage 存储对象，localStorage或sessionStorage
   * @param key 存储键
   * @returns 存储值，如果不存在则返回null
   */
  static get<T>(storage: Storage, key: string): T | null {
    try {
      const stringValue = storage.getItem(key)
      if (stringValue === null) {
        return null
      }
      return JSON.parse(stringValue) as T
    } catch (error) {
      console.error('Error getting from storage:', error)
      return null
    }
  }

  /**
   * 从指定的存储对象删除数据
   * @param storage 存储对象，localStorage或sessionStorage
   * @param key 存储键
   */
  static remove(storage: Storage, key: string): void {
    try {
      storage.removeItem(key)
    } catch (error) {
      console.error('Error removing from storage:', error)
    }
  }

  /**
   * 清空指定的存储对象
   * @param storage 存储对象，localStorage或sessionStorage
   */
  static clear(storage: Storage): void {
    try {
      storage.clear()
    } catch (error) {
      console.error('Error clearing storage:', error)
    }
  }

  /**
   * 检查存储对象中是否存在指定的键
   * @param storage 存储对象，localStorage或sessionStorage
   * @param key 存储键
   * @returns 是否存在
   */
  static has(storage: Storage, key: string): boolean {
    return storage.getItem(key) !== null
  }
}

// 存储键生成工具
const StorageKeys = {
  // 用户相关存储键
  userInfo: 'user:info:v1',
  token: 'auth:token:v1',
  refreshToken: 'auth:refresh_token:v1',
  // 系统相关存储键
  theme: 'system:theme:v1',
  sidebarCollapsed: 'system:sidebar_collapsed:v1',
  language: 'system:language:v1',
  // 权限相关存储键
  permissions: (userId: number | string) => `permissions:${userId}:v1`,
  roles: (userId: number | string) => `roles:${userId}:v1`,
  // 缓存相关存储键
  cacheVersion: 'cache:version:v1'
}

// 导出存储管理器和存储键生成工具
export { StorageManager, StorageKeys }
