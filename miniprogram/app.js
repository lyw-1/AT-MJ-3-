// app.js
App({
  onLaunch() {
    // 初始化
    this.init()
  },
  
  init() {
    // 检查登录状态
    this.checkLoginStatus()
  },
  
  checkLoginStatus() {
    // 从本地存储获取token
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    if (token) {
      this.globalData.isLoggedIn = true
      this.globalData.token = token
      this.globalData.userInfo = userInfo
    }
  },
  
  // 更新登录状态
  updateLoginStatus() {
    this.checkLoginStatus()
  },
  
  // 退出登录
  logout() {
    wx.removeStorageSync('token')
    wx.removeStorageSync('userInfo')
    this.globalData.isLoggedIn = false
    this.globalData.token = ''
    this.globalData.userInfo = null
    // 跳转到登录页面
    wx.redirectTo({
      url: '/pages/login/index'
    })
  },
  
  globalData: {
    userInfo: null,
    isLoggedIn: false,
    token: '',
    baseUrl: 'http://localhost:8080/api'
  }
})