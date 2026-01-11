// pages/tasks/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 筛选标签
    filterTags: [
      { id: 1, name: '全部', type: 'all', active: true },
      { id: 2, name: '待处理', type: 'pending', active: false },
      { id: 3, name: '处理中', type: 'processing', active: false },
      { id: 4, name: '已完成', type: 'completed', active: false }
    ],
    // 所有任务
    allTasks: [
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
    ],
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
    this.setData({
      tasks: this.data.allTasks
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
    this.getTasks()
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

  // 获取任务列表
  getTasks() {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
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