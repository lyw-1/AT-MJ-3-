// pages/index/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null,
    todos: [
      {
        id: 1,
        title: '模具加工任务',
        description: '完成模具编号M001的加工任务',
        status: 'pending',
        statusText: '待处理'
      },
      {
        id: 2,
        title: '模具检测',
        description: '对模具M002进行质量检测',
        status: 'processing',
        statusText: '处理中'
      },
      {
        id: 3,
        title: '模具维护',
        description: '对设备E001进行维护保养',
        status: 'pending',
        statusText: '待处理'
      }
    ],
    stats: {
      totalTasks: 12,
      pendingTasks: 3,
      processingTasks: 5,
      completedTasks: 4
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 获取用户信息
    this.getUserInfo()
    // 获取待办事项
    this.getTodos()
    // 获取统计数据
    this.getStats()
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
    // 刷新数据
    this.getTodos()
    this.getStats()
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
    const app = getApp()
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })
    } else {
      // 模拟获取用户信息
      this.setData({
        userInfo: {
          name: '张三',
          avatar: ''
        }
      })
    }
  },

  // 获取待办事项
  getTodos() {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
  },

  // 获取统计数据
  getStats() {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
  },

  // 导航到任务管理
  navigateToTasks() {
    wx.navigateTo({
      url: '/pages/tasks/index'
    })
  },

  // 导航到模具管理
  navigateToMold() {
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    })
  },

  // 导航到加工管理
  navigateToProcess() {
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    })
  },

  // 导航到数据统计
  navigateToStatistics() {
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    })
  },

  // 导航到任务详情
  navigateToTaskDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/task-detail/index?id=${id}`
    })
  }
})