// 登录页面逻辑
Page({
  /**
   * 页面的初始数据
   */
  data: {
    account: '', // 账号
    password: '', // 密码
    loading: false, // 登录按钮加载状态
    wechatLoading: false, // 微信登录按钮加载状态
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 检查是否已登录
    const token = wx.getStorageSync('token');
    if (token) {
      wx.switchTab({ url: '/pages/index/index' });
    }
  },

  /**
   * 账号输入事件
   */
  onAccountInput(e) {
    this.setData({
      account: e.detail.value
    });
  },

  /**
   * 密码输入事件
   */
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  /**
   * 账号密码登录
   */
  onLogin() {
    const { account, password } = this.data;

    // 简单校验
    if (!account.trim()) {
      wx.showToast({ title: '请输入账号', icon: 'none' });
      return;
    }
    if (!password.trim()) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    // 模拟登录请求
    setTimeout(() => {
      // 登录成功，保存token
      wx.setStorageSync('token', 'mock-token-123456');
      wx.setStorageSync('userInfo', { account });
      
      // 调用全局登录状态更新
      getApp().updateLoginStatus();
      
      wx.showToast({ title: '登录成功', icon: 'success' });
      wx.switchTab({ url: '/pages/index/index' });
      
      this.setData({ loading: false });
    }, 1500);
  },

  /**
   * 微信授权登录
   */
  onWechatLogin(e) {
    const { userInfo } = e.detail;
    
    if (userInfo) {
      this.setData({ wechatLoading: true });
      
      // 模拟微信登录请求
      setTimeout(() => {
        // 登录成功，保存token和用户信息
        wx.setStorageSync('token', 'wechat-token-123456');
        wx.setStorageSync('userInfo', userInfo);
        
        // 调用全局登录状态更新
        getApp().updateLoginStatus();
        
        wx.showToast({ title: '微信登录成功', icon: 'success' });
        wx.switchTab({ url: '/pages/index/index' });
        
        this.setData({ wechatLoading: false });
      }, 1500);
    } else {
      wx.showToast({ title: '请授权微信登录', icon: 'none' });
    }
  },

  /**
   * 跳转到隐私政策页面
   */
  navigateToPrivacy() {
    wx.navigateTo({
      url: '/pages/privacy/index'
    });
  },

  /**
   * 跳转到用户协议页面
   */
  navigateToTerms() {
    wx.navigateTo({
      url: '/pages/terms/index'
    });
  },

  /**
   * 注册链接点击事件
   */
  onRegisterTap() {
    // 这里可以跳转到注册页面或显示注册提示
    wx.showToast({
      title: '注册功能开发中',
      icon: 'none'
    });
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
    // 页面显示时检查登录状态
    const token = wx.getStorageSync('token');
    if (token) {
      wx.switchTab({ url: '/pages/index/index' });
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

  }
});
