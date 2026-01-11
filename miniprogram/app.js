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
    if (token) {
      this.globalData.isLoggedIn = true
      this.globalData.token = token
    }
  },
  
  globalData: {
    userInfo: null,
    isLoggedIn: false,
    token: '',
    baseUrl: 'http://localhost:8080/api'
  }
})