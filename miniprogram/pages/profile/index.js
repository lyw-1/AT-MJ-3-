// pages/profile/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 用户信息
    userInfo: null,
    // 统计数据
    taskCount: 0,
    level: 'Lv.1',
    // 快捷操作
    quickActions: [
      { id: 'tasks', name: '我的任务', iconText: '📋', color: '#0066cc' },
      { id: 'molds', name: '我的模具', iconText: '🔧', color: '#0099ff' }
    ],
    // 功能菜单配置
    menuItems: [
      { id: 'process-management', name: '加工管理', icon: '/assets/icons/settings.svg', color: '#00b8d4' },
      { id: 'adjust-management', name: '调模管理', icon: '/assets/icons/search.svg', color: '#008080' },
      { id: 'production-management', name: '生产管理', icon: '/assets/icons/stats.svg', color: '#006699' },
      { id: 'equipment-management', name: '设备管理', icon: '/assets/icons/profile.svg', color: '#4a4a4a' },
      { id: 'consumables-management', name: '耗材管理', icon: '/assets/icons/arrow-right.svg', color: '#5a5a5a' },
      { id: 'settings', name: '设置', icon: '/assets/icons/mold-new.svg', color: '#0073e6' },
      { id: 'about', name: '关于', icon: '/assets/icons/tasks-new.svg', color: '#6b6b6b' },
      { id: 'feedback', name: '反馈', icon: '/assets/icons/wechat.svg', color: '#13c2c2' }
    ],
    // 是否有反馈功能
    hasFeedback: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    // 获取用户信息
    this.getUserInfo()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 检查登录状态
    const app = getApp()
    if (!app.globalData.isLoggedIn) {
      wx.redirectTo({
        url: '/pages/login/index'
      })
      return
    }
    
    // 更新tabBar选中状态
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 4
      })
    }
    // 每次显示页面时更新用户信息
    this.getUserInfo()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    // 刷新用户信息
    this.getUserInfo()
    wx.stopPullDownRefresh()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 获取用户信息
  getUserInfo() {
    // 从全局数据中获取用户信息
    const app = getApp()
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })
    } else {
      // 模拟用户信息
      this.setData({
        userInfo: {
          name: '张三',
          role: '普通用户'
        }
      })
    }
  },

  // 菜单点击事件
  onMenuItemClick(e) {
    const item = e.currentTarget.dataset.item
    switch (item) {
      case 'tasks':
        wx.navigateTo({
          url: '/pages/tasks/index'
        })
        break
      case 'molds':
        wx.navigateTo({
          url: '/pages/mold/list/index'
        })
        break
      case 'process-management':
      case 'adjust-management':
      case 'production-management':
      case 'equipment-management':
      case 'consumables-management':
      case 'settings':
      case 'about':
      case 'feedback':
        wx.showToast({
          title: `${this.data.menuItems.find(m => m.id === item)?.name || item}功能开发中`,
          icon: 'none'
        })
        break
      default:
        break
    }
  },

  // 退出登录
  onLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用全局退出登录方法
          const app = getApp()
          app.logout()
          
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          })
        }
      }
    })
  }
})