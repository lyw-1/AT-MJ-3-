// pages/tasks/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 加载状态
    loading: true,
    // 下拉刷新状态
    refreshing: false,
    // 上拉加载更多状态
    loadingMore: false,
    // 是否还有更多数据
    hasMore: true,
    // 当前页码
    page: 1,
    // 每页条数
    pageSize: 10,
    // 筛选标签
    filterTags: [
      { id: 1, name: '全部', type: 'all', active: true },
      { id: 2, name: '待处理', type: 'pending', active: false },
      { id: 3, name: '处理中', type: 'processing', active: false },
      { id: 4, name: '已完成', type: 'completed', active: false }
    ],
    // 所有任务
    allTasks: [],
    // 显示的任务列表
    tasks: [],
    // 搜索关键词
    searchKeyword: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 初始化任务列表
    this.setData({ loading: true })
    this.getTasks()
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
    
    // 更新tabBar选中状态 - 任务图标索引是3
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 3
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
    // 刷新任务列表
    this.setData({ refreshing: true })
    this.getTasks(true)
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    // 上拉加载更多
    if (!this.data.loadingMore && this.data.hasMore) {
      this.setData({ loadingMore: true })
      // 模拟加载更多数据
      setTimeout(() => {
        const newPage = this.data.page + 1
        this.setData({ page: newPage })
        this.getTasks()
      }, 800) // 模拟网络延迟
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 获取任务列表
  getTasks(refresh = false) {
    // 重置数据
    if (refresh) {
      this.setData({
        page: 1,
        allTasks: [],
        hasMore: true
      })
    }
    
    // 模拟网络请求
    setTimeout(() => {
      // 模拟任务数据
      const mockTasks = [
        {
          id: 1,
          title: '模具加工任务',
          description: '完成模具编号M001的加工任务，按照图纸要求进行加工',
          status: 'pending',
          statusText: '待处理',
          assignee: '张三',
          createTime: '2026-01-09 14:30'
        },
        {
          id: 2,
          title: '模具检测',
          description: '对模具M002进行质量检测，确保符合标准要求',
          status: 'processing',
          statusText: '处理中',
          assignee: '李四',
          createTime: '2026-01-09 10:15'
        },
        {
          id: 3,
          title: '设备维护',
          description: '对设备E001进行维护保养，更换润滑油和易损件',
          status: 'pending',
          statusText: '待处理',
          assignee: '王五',
          createTime: '2026-01-08 16:45'
        },
        {
          id: 4,
          title: '模具设计',
          description: '完成新模具M003的设计工作，出详细图纸',
          status: 'completed',
          statusText: '已完成',
          assignee: '赵六',
          createTime: '2026-01-08 09:30'
        },
        {
          id: 5,
          title: '原材料采购',
          description: '采购一批模具加工所需的原材料，确保质量和交期',
          status: 'processing',
          statusText: '处理中',
          assignee: '孙七',
          createTime: '2026-01-07 14:20'
        }
      ]
      
      let newTasks = []
      if (refresh) {
        newTasks = mockTasks
      } else {
        newTasks = [...this.data.allTasks, ...mockTasks]
      }
      
      this.setData({
        allTasks: newTasks,
        loading: false,
        refreshing: false,
        loadingMore: false
      })
      
      // 过滤任务
      this.filterTasks()
      
      // 停止下拉刷新
      if (refresh) {
        wx.stopPullDownRefresh()
      }
    }, 800) // 模拟网络延迟
  },

  // 搜索任务
  onSearch(e) {
    const keyword = e.detail.value
    this.setData({
      searchKeyword: keyword
    })
    this.filterTasks()
  },

  // 筛选任务
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
    this.filterTasks()
  },

  // 过滤任务
  filterTasks() {
    const keyword = this.data.searchKeyword
    const activeFilter = this.data.filterTags.find(tag => tag.active).type
    
    let filteredTasks = this.data.allTasks
    
    // 按关键词过滤
    if (keyword) {
      filteredTasks = filteredTasks.filter(task => 
        task.title.includes(keyword) || 
        task.description.includes(keyword) || 
        task.assignee.includes(keyword)
      )
    }
    
    // 按状态过滤
    if (activeFilter !== 'all') {
      filteredTasks = filteredTasks.filter(task => task.status === activeFilter)
    }
    
    this.setData({
      tasks: filteredTasks
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