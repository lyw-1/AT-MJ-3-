// pages/mold/list/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 筛选标签
    filterTags: [
      { id: 1, name: '全部', type: 'all', active: true },
      { id: 2, name: '在用', type: 'active', active: false },
      { id: 3, name: '维护中', type: 'maintenance', active: false },
      { id: 4, name: '停用', type: 'inactive', active: false }
    ],
    // 所有模具
    allMolds: [
      {
        id: 1,
        code: 'M001',
        name: '蜂窝陶瓷模具A',
        description: '用于生产型号为A的蜂窝陶瓷产品',
        status: 'active',
        statusText: '在用',
        type: '类型A',
        createTime: '2026-01-09 14:30'
      },
      {
        id: 2,
        code: 'M002',
        name: '蜂窝陶瓷模具B',
        description: '用于生产型号为B的蜂窝陶瓷产品',
        status: 'maintenance',
        statusText: '维护中',
        type: '类型B',
        createTime: '2026-01-09 10:15'
      },
      {
        id: 3,
        code: 'M003',
        name: '蜂窝陶瓷模具C',
        description: '用于生产型号为C的蜂窝陶瓷产品',
        status: 'active',
        statusText: '在用',
        type: '类型A',
        createTime: '2026-01-08 16:45'
      },
      {
        id: 4,
        code: 'M004',
        name: '蜂窝陶瓷模具D',
        description: '用于生产型号为D的蜂窝陶瓷产品',
        status: 'inactive',
        statusText: '停用',
        type: '类型C',
        createTime: '2026-01-08 09:30'
      },
      {
        id: 5,
        code: 'M005',
        name: '蜂窝陶瓷模具E',
        description: '用于生产型号为E的蜂窝陶瓷产品',
        status: 'active',
        statusText: '在用',
        type: '类型B',
        createTime: '2026-01-07 14:20'
      }
    ],
    // 显示的模具列表
    molds: [],
    // 搜索关键词
    searchKeyword: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 初始化模具列表
    this.setData({
      molds: this.data.allMolds
    })
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
        selected: 1
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
    // 刷新模具列表
    this.getMolds()
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

  // 获取模具列表
  getMolds() {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
  },

  // 搜索模具
  onSearch(e) {
    const keyword = e.detail.value
    this.setData({
      searchKeyword: keyword
    })
    this.filterMolds()
  },

  // 筛选模具
  onFilter(e) {
    const type = e.currentTarget.dataset.type
    // 更新筛选标签状态
    const filterTags = this.data.filterTags.map(tag => ({
      ...tag,
      active: tag.type === type
    }))
    this.setData({
      filterTags
    })
    this.filterMolds()
  },

  // 过滤模具
  filterMolds() {
    const keyword = this.data.searchKeyword
    const activeFilter = this.data.filterTags.find(tag => tag.active).type
    
    let filteredMolds = this.data.allMolds
    
    // 按关键词过滤
    if (keyword) {
      filteredMolds = filteredMolds.filter(mold => 
        mold.code.includes(keyword) || 
        mold.name.includes(keyword) || 
        mold.description.includes(keyword)
      )
    }
    
    // 按状态过滤
    if (activeFilter !== 'all') {
      filteredMolds = filteredMolds.filter(mold => mold.status === activeFilter)
    }
    
    this.setData({
      molds: filteredMolds
    })
  },

  // 跳转到添加模具页面
  navigateToAddMold() {
    wx.navigateTo({
      url: '/pages/mold/add/index'
    })
  },

  // 跳转到模具详情页面
  navigateToMoldDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/mold/detail/index?id=${id}`
    })
  }
})