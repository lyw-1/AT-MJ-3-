// 缓存工具类
class CacheManager {
  // 缓存存储对象
  private cache: Map<string, CacheItem> = new Map()
  // 默认缓存过期时间（毫秒）
  private defaultExpireTime = 10 * 60 * 1000 // 10分钟

  /**
   * 设置缓存
   * @param key 缓存键
   * @param value 缓存值
   * @param expireTime 过期时间（毫秒），默认10分钟
   */
  set(key: string, value: any, expireTime?: number): void {
    const item: CacheItem = {
      value,
      expireTime: expireTime || this.defaultExpireTime,
      createTime: Date.now()
    }
    this.cache.set(key, item)
  }

  /**
   * 获取缓存
   * @param key 缓存键
   * @returns 缓存值，如果缓存不存在或已过期则返回undefined
   */
  get(key: string): any | undefined {
    const item = this.cache.get(key)
    if (!item) {
      return undefined
    }
    // 检查缓存是否已过期
    if (Date.now() - item.createTime > item.expireTime) {
      // 缓存已过期，删除缓存
      this.cache.delete(key)
      return undefined
    }
    return item.value
  }

  /**
   * 删除缓存
   * @param key 缓存键
   * @returns 是否删除成功
   */
  delete(key: string): boolean {
    return this.cache.delete(key)
  }

  /**
   * 清空所有缓存
   */
  clear(): void {
    this.cache.clear()
  }

  /**
   * 检查缓存是否存在
   * @param key 缓存键
   * @returns 缓存是否存在且未过期
   */
  has(key: string): boolean {
    const item = this.cache.get(key)
    if (!item) {
      return false
    }
    // 检查缓存是否已过期
    if (Date.now() - item.createTime > item.expireTime) {
      // 缓存已过期，删除缓存
      this.cache.delete(key)
      return false
    }
    return true
  }

  /**
   * 获取缓存大小
   * @returns 缓存项数量
   */
  size(): number {
    // 先清理过期缓存
    this.cleanExpired()
    return this.cache.size
  }

  /**
   * 清理所有过期缓存
   */
  cleanExpired(): void {
    const now = Date.now()
    this.cache.forEach((item, key) => {
      if (now - item.createTime > item.expireTime) {
        this.cache.delete(key)
      }
    })
  }

  /**
   * 获取所有缓存键
   * @returns 缓存键数组
   */
  keys(): string[] {
    // 先清理过期缓存
    this.cleanExpired()
    return Array.from(this.cache.keys())
  }

  /**
   * 获取所有缓存项
   * @returns 缓存项数组
   */
  values(): any[] {
    // 先清理过期缓存
    this.cleanExpired()
    return Array.from(this.cache.values()).map(item => item.value)
  }

  /**
   * 刷新单个缓存项
   * @param key 缓存键
   */
  refresh(key: string): void {
    this.cache.delete(key)
  }

  /**
   * 按模式刷新缓存项
   * @param pattern 缓存键匹配模式（支持通配符*）
   * @returns 刷新的缓存项数量
   */
  refreshByPattern(pattern: string): number {
    // 将通配符转换为正则表达式
    const regex = new RegExp(`^${pattern.replace(/\*/g, '.*')}$`)
    let count = 0
    
    this.cache.forEach((_, key) => {
      if (regex.test(key)) {
        this.cache.delete(key)
        count++
      }
    })
    
    return count
  }
}

// 缓存项接口
interface CacheItem {
  value: any
  expireTime: number
  createTime: number
}

// 缓存键生成工具
const CacheKeys = {
  // 用户相关缓存键
  userList: 'user:list:v1',
  userItem: (id: number | string) => `user:item:${id}:v1`,
  // 角色相关缓存键
  roleList: 'role:list:v1',
  roleItem: (id: number | string) => `role:item:${id}:v1`,
  // 部门相关缓存键
  departmentList: 'department:list:v1',
  departmentItem: (id: number | string) => `department:item:${id}:v1`,
  // 模具相关缓存键
  moldList: 'mold:list:v1',
  moldItem: (id: number | string) => `mold:item:${id}:v1`,
  // 设备相关缓存键
  equipmentList: 'equipment:list:v1',
  equipmentItem: (id: number | string) => `equipment:item:${id}:v1`,
  // 系统参数缓存键
  systemParams: 'system:params:v1',
  // 权限相关缓存键
  permissions: (userId: number | string) => `permissions:${userId}:v1`
}

// 导出缓存管理器实例和缓存键生成工具
export const cacheManager = new CacheManager()
export { CacheKeys }
