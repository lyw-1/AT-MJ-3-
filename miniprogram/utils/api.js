// API工具类

// 引入app实例
const app = getApp()

// 基础URL
const baseUrl = app.globalData.baseUrl

// 请求超时时间
const timeout = 10000

// 请求方法
const request = (url, method, data = {}, options = {}) => {
  const {
    showLoading = true,
    loadingTitle = '加载中...',
    loadingMask = false
  } = options
  
  return new Promise((resolve, reject) => {
    // 显示加载动画
    if (showLoading) {
      wx.showLoading({
        title: loadingTitle,
        mask: loadingMask
      })
    }
    
    // 发送请求
    wx.request({
      url: `${baseUrl}${url}`,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      timeout: timeout,
      success: (res) => {
        // 隐藏加载动画
        if (showLoading) {
          wx.hideLoading()
        }
        
        // 处理响应
        if (res.statusCode === 200) {
          resolve(res.data)
        } else if (res.statusCode === 401) {
          // 未授权，跳转到登录页
          wx.showToast({
            title: '请先登录',
            icon: 'none'
          })
          wx.navigateTo({
            url: '/pages/auth/login'
          })
          reject(new Error('未授权'))
        } else {
          wx.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          })
          reject(new Error(res.data.message || '请求失败'))
        }
      },
      fail: (err) => {
        // 隐藏加载动画
        if (showLoading) {
          wx.hideLoading()
        }
        
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// GET请求
export const get = (url, data = {}, options = {}) => {
  return request(url, 'GET', data, options)
}

// POST请求
export const post = (url, data = {}, options = {}) => {
  return request(url, 'POST', data, options)
}

// PUT请求
export const put = (url, data = {}, options = {}) => {
  return request(url, 'PUT', data, options)
}

// DELETE请求
export const del = (url, data = {}, options = {}) => {
  return request(url, 'DELETE', data, options)
}

// API接口列表
export const api = {
  // 用户相关
  user: {
    login: (data) => post('/auth/login', data),
    logout: () => post('/auth/logout'),
    getInfo: () => get('/user/info')
  },
  // 任务相关
  tasks: {
    list: (data) => get('/tasks', data),
    detail: (id) => get(`/tasks/${id}`),
    create: (data) => post('/tasks', data),
    update: (id, data) => put(`/tasks/${id}`, data),
    delete: (id) => del(`/tasks/${id}`),
    complete: (id) => put(`/tasks/${id}/complete`)
  },
  // 模具相关
  molds: {
    list: (data) => get('/molds', data),
    detail: (id) => get(`/molds/${id}`),
    create: (data) => post('/molds', data),
    update: (id, data) => put(`/molds/${id}`, data),
    delete: (id) => del(`/molds/${id}`)
  },
  // 加工相关
  process: {
    list: (data) => get('/process', data)
  },
  // 统计相关
  stats: {
    getDashboard: () => get('/stats/dashboard')
  }
}