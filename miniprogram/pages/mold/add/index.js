// pages/mold/add/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 表单数据
    formData: {
      code: '',
      name: '',
      type: '',
      description: '',
      status: 'active',
      length: '',
      width: '',
      height: '',
      weight: '',
      material: '',
      precision: ''
    },
    // 状态选项
    statusOptions: [
      { value: 'active', label: '在用' },
      { value: 'maintenance', label: '维护中' },
      { value: 'inactive', label: '停用' }
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

  // 处理输入事件
  onInput(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`formData.${field}`]: value
    })
  },

  // 处理文本域输入事件
  onTextareaInput(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`formData.${field}`]: value
    })
  },

  // 处理单选框变化
  onRadioChange(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`formData.${field}`]: value
    })
  },

  // 保存模具
  onSave() {
    // 表单验证
    if (!this.validateForm()) {
      return
    }

    // 显示加载动画
    wx.showLoading({
      title: '保存中...'
    })

    // 模拟保存请求
    setTimeout(() => {
      wx.hideLoading()
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })
      // 返回上一页
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }, 1500)
  },

  // 表单验证
  validateForm() {
    const { code, name, type } = this.data.formData
    
    if (!code) {
      wx.showToast({
        title: '请输入模具编号',
        icon: 'none'
      })
      return false
    }
    
    if (!name) {
      wx.showToast({
        title: '请输入模具名称',
        icon: 'none'
      })
      return false
    }
    
    if (!type) {
      wx.showToast({
        title: '请输入模具类型',
        icon: 'none'
      })
      return false
    }
    
    return true
  },

  // 取消操作
  onCancel() {
    // 返回上一页
    wx.navigateBack()
  }
})