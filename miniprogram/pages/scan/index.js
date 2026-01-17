// pages/scan/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    scanHistory: [
      {
        id: 1,
        title: '模具M001',
        time: '2026-01-14 10:30'
      },
      {
        id: 2,
        title: '任务T018',
        time: '2026-01-14 09:15'
      },
      {
        id: 3,
        title: '模具M025',
        time: '2026-01-13 16:45'
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

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
        selected: 2
      })
    }
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
    // 刷新扫描历史
    setTimeout(() => {
      wx.stopPullDownRefresh()
    }, 500)
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

  // 开始扫描
  startScan() {
    wx.scanCode({
      onlyFromCamera: false,
      scanType: ['qrCode', 'barCode'],
      success: (res) => {
        console.log('扫描结果:', res)
        // 处理扫描结果
        this.handleScanResult(res)
      },
      fail: (err) => {
        console.error('扫描失败:', err)
        wx.showToast({
          title: '扫描失败',
          icon: 'none'
        })
      }
    })
  },

  // 处理扫描结果
  handleScanResult(res) {
    const result = res.result
    // 这里可以根据扫描结果进行相应的处理
    // 例如：根据二维码内容跳转到对应的模具详情或任务详情
    
    // 模拟添加到扫描历史
    const newHistory = {
      id: Date.now(),
      title: result,
      time: new Date().toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    this.setData({
      scanHistory: [newHistory, ...this.data.scanHistory]
    })
    
    wx.showToast({
      title: '扫描成功',
      icon: 'success'
    })
    
    // 根据扫描结果跳转
    // 这里可以根据实际业务逻辑进行调整
    if (result.includes('mold')) {
      // 跳转到模具详情
      wx.navigateTo({
        url: `/pages/mold/detail/index?id=${result.split(':')[1]}`
      })
    } else if (result.includes('task')) {
      // 跳转到任务详情
      wx.navigateTo({
        url: `/pages/task-detail/index?id=${result.split(':')[1]}`
      })
    }
  }
})
