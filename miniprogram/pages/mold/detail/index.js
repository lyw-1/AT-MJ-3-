// pages/mold/detail/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 模具详情
    mold: {
      id: 1,
      code: 'M001',
      name: '蜂窝陶瓷模具A',
      description: '用于生产型号为A的蜂窝陶瓷产品，采用先进的制造工艺，具有高精度和长寿命的特点。',
      status: 'active',
      statusText: '在用',
      type: '类型A',
      createTime: '2026-01-09 14:30',
      updateTime: '2026-01-09 14:30',
      life: '10000次',
      params: [
        { id: 1, label: '长度', value: '200mm' },
        { id: 2, label: '宽度', value: '150mm' },
        { id: 3, label: '高度', value: '100mm' },
        { id: 4, label: '重量', value: '5kg' },
        { id: 5, label: '材质', value: '不锈钢' },
        { id: 6, label: '精度', value: '±0.01mm' }
      ],
      usageRecords: [
        {
          id: 1,
          content: '模具使用',
          user: '张三',
          time: '2026-01-10 09:00'
        },
        {
          id: 2,
          content: '模具维护',
          user: '李四',
          time: '2026-01-09 16:00'
        },
        {
          id: 3,
          content: '模具检测',
          user: '王五',
          time: '2026-01-08 14:30'
        }
      ]
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const id = options.id
    // 根据ID获取模具详情
    this.getMoldDetail(id)
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
    // 刷新模具详情
    this.getMoldDetail(this.data.mold.id)
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

  // 获取模具详情
  getMoldDetail(id) {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
    console.log('获取模具详情:', id)
  },

  // 编辑模具
  onEdit() {
    wx.showToast({
      title: '编辑功能开发中',
      icon: 'none'
    })
  },

  // 删除模具
  onDelete() {
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此模具吗？',
      success: (res) => {
        if (res.confirm) {
          // 执行删除操作
          wx.showToast({
            title: '模具已删除',
            icon: 'success'
          })
          // 返回上一页
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        }
      }
    })
  },

  // 跳转到使用记录页面
  navigateToUsageRecords() {
    wx.navigateTo({
      url: `/pages/mold/usage-records/index?id=${this.data.mold.id}`
    })
  }
})