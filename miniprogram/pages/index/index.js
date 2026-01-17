// pages/index/index.js
Page({
  data: {
    date: '',
    stats: {
      moldPassRate: { value: 98.5, label: '模具合格率', color: '#0066cc' },
      bareMoldCount: { value: 123, label: '裸模库数量', color: '#0099ff' },
      availableCount: { value: 456, label: '可用库数量', color: '#00b8d4' },
      lockedCount: { value: 89, label: '封层库数量', color: '#006699' }
    },
    // 所有快捷功能列表
    allQuickActions: [
      { id: 'process-apply', name: '加工申请', icon: '/assets/icons/tasks.svg', color: '#0066cc', show: true },
      { id: 'initial-params', name: '初始参数', icon: '/assets/icons/settings.svg', color: '#4a4a4a', show: true },
      { id: 'new-mold', name: '新模建档', icon: '/assets/icons/mold.svg', color: '#0099ff', show: true },
      { id: 'mold-use', name: '模具领用', icon: '/assets/icons/mold-new.svg', color: '#008080', show: true },
      { id: 'mold-return', name: '模具归还', icon: '/assets/icons/arrow-right.svg', color: '#6b6b6b', show: true },
      { id: 'consumables-use', name: '耗材领用', icon: '/assets/icons/wechat.svg', color: '#006699', show: false },
      { id: 'production-data', name: '成型录入', icon: '/assets/icons/stats.svg', color: '#00b8d4', show: false },
      { id: 'measure-data', name: '测量录入', icon: '/assets/icons/search.svg', color: '#5a5a5a', show: false },
      { id: 'adjust-data', name: '调模录入', icon: '/assets/icons/profile.svg', color: '#0073e6', show: false },
      { id: 'adjust-inspection', name: '调模验收记录录入', icon: '/assets/icons/tasks-new.svg', color: '#008080', show: false }
    ],
    // 当前显示的快捷功能
    currentQuickActions: [],
    // 管理模式状态
    isManageMode: false,
    // 排序模式状态
    isSortMode: false,
    // 当前正在拖拽的索引
    draggedIndex: -1,
    // 拖拽开始时的Y坐标
    startY: 0,
    // 拖拽开始时的索引
    startDragIndex: -1,
    // 最近生产数据录入记录
    recentProductionData: [
      { id: 'prod-1', productName: '产品A', operator: '张三', time: '2026-01-16 09:30' },
      { id: 'prod-2', productName: '产品B', operator: '李四', time: '2026-01-16 09:20' },
      { id: 'prod-3', productName: '产品C', operator: '王五', time: '2026-01-16 09:10' }
    ],
    // 模具加工任务列表
    moldProcessTasks: [
      { id: 'mold-1', moldName: '模具X', status: '进行中', dueDate: '2026-01-17' },
      { id: 'mold-2', moldName: '模具Y', status: '待处理', dueDate: '2026-01-18' },
      { id: 'mold-3', moldName: '模具Z', status: '已完成', dueDate: '2026-01-15' }
    ],
    // 非模具加工任务列表
    nonMoldProcessTasks: [
      { id: 'non-mold-1', taskName: '任务A', status: '进行中', dueDate: '2026-01-16' },
      { id: 'non-mold-2', taskName: '任务B', status: '待处理', dueDate: '2026-01-17' },
      { id: 'non-mold-3', taskName: '任务C', status: '已完成', dueDate: '2026-01-15' }
    ]
  },

  onLoad() {
    // 更新日期信息
    this.updateDate()
    // 初始化显示的快捷功能
    this.updateCurrentQuickActions()
  },

  onShow() {
    // 更新tabBar选中状态
    try {
      if (typeof this.getTabBar === 'function') {
        const tabBar = this.getTabBar()
        if (tabBar) {
          console.log('Index page setting tabBar selected to 0')
          tabBar.setData({
            selected: 0
          })
        } else {
          console.log('TabBar not found in index page')
        }
      } else {
        console.log('getTabBar is not a function in index page')
      }
    } catch (error) {
      console.error('更新tabBar选中状态失败:', error)
    }
    
    // 重置页面内容的上边距，因为移除了固定头部
    this.setData({
      pageContentMarginTop: 84 // 只保留搜索栏的高度
    })
  },

  // 更新日期信息
  updateDate() {
    const now = new Date()
    const date = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`
    
    this.setData({
      date
    })
  },

  // 查看模具合格率详情
  viewMoldPassRate() {
    wx.showToast({
      title: '查看模具合格率详情',
      icon: 'none'
    })
  },

  // 查看裸模库数量详情
  viewBareMoldCount() {
    wx.showToast({
      title: '查看裸模库数量详情',
      icon: 'none'
    })
  },

  // 查看可用库数量详情
  viewAvailableCount() {
    wx.showToast({
      title: '查看可用库数量详情',
      icon: 'none'
    })
  },

  // 查看封库库数量详情
  viewLockedCount() {
    wx.showToast({
      title: '查看封库库数量详情',
      icon: 'none'
    })
  },

  // 更新当前显示的快捷功能
  updateCurrentQuickActions() {
    const { allQuickActions } = this.data
    const currentQuickActions = allQuickActions.filter(item => item.show)
    console.log('updateCurrentQuickActions called')
    console.log('allQuickActions:', allQuickActions)
    console.log('currentQuickActions:', currentQuickActions)
    this.setData({
      currentQuickActions
    })
  },

  // 切换管理模式
  toggleManageMode() {
    this.setData({
      isManageMode: !this.data.isManageMode,
      isSortMode: false,
      draggedIndex: -1
    })
  },

  // 切换排序模式
  toggleSortMode() {
    this.setData({
      isSortMode: !this.data.isSortMode
    })
  },

  // 开始拖拽
  onDragStart(e) {
    const { index } = e.currentTarget.dataset
    this.setData({
      draggedIndex: index
    })
  },

  // 拖拽移动
  onDragMove() {
    // 暂时不实现拖拽移动的视觉反馈
  },

  // 拖拽结束，交换位置
  onDrop(e) {
    const { index } = e.currentTarget.dataset
    const { draggedIndex, allQuickActions } = this.data

    if (draggedIndex === -1 || draggedIndex === index) {
      this.setData({
        draggedIndex: -1
      })
      return
    }

    // 交换两个元素的位置
    const newActions = [...allQuickActions]
    const temp = newActions[draggedIndex]
    newActions[draggedIndex] = newActions[index]
    newActions[index] = temp

    this.setData({
      allQuickActions: newActions,
      draggedIndex: -1
    }, () => {
      this.updateCurrentQuickActions()
    })
  },

  // 上移（备用方法）
  onMoveUp(e) {
    console.log('onMoveUp triggered', e)
    const { index } = e.currentTarget.dataset
    console.log('index:', index)
    const { allQuickActions } = this.data

    if (index === 0) return

    const newActions = [...allQuickActions]
    const temp = newActions[index]
    newActions[index] = newActions[index - 1]
    newActions[index - 1] = temp

    console.log('newActions:', newActions)

    this.setData({
      allQuickActions: newActions
    }, () => {
      this.updateCurrentQuickActions()
    })
  },

  // 下移（备用方法）
  onMoveDown(e) {
    console.log('onMoveDown triggered', e)
    const { index } = e.currentTarget.dataset
    console.log('index:', index)
    const { allQuickActions } = this.data

    if (index === allQuickActions.length - 1) return

    const newActions = [...allQuickActions]
    const temp = newActions[index]
    newActions[index] = newActions[index + 1]
    newActions[index + 1] = temp

    console.log('newActions:', newActions)

    this.setData({
      allQuickActions: newActions
    }, () => {
      this.updateCurrentQuickActions()
    })
  },

  // 长按开始拖拽
  onLongPressItem(e) {
    const { index } = e.currentTarget.dataset
    console.log('长按开始拖拽', index)
    this.setData({
      draggedIndex: index,
      startDragIndex: index
    })
    wx.vibrateShort({
      type: 'light'
    })
  },

  // 触摸开始
  onTouchStart(e) {
    const { index } = e.currentTarget.dataset
    this.setData({
      startY: e.touches[0].clientY,
      draggedIndex: index,
      startDragIndex: index
    })
  },

  // 触摸移动
  onTouchMove(e) {
    const { startY, startDragIndex, draggedIndex, allQuickActions } = this.data
    if (startDragIndex === -1) return

    const currentY = e.touches[0].clientY
    const itemHeight = 120 // 每个item的高度估计值
    const moveDistance = currentY - startY
    const moveItems = Math.round(moveDistance / itemHeight)

    if (moveItems !== 0 && draggedIndex + moveItems >= 0 && draggedIndex + moveItems < allQuickActions.length) {
      const newIndex = draggedIndex + moveItems
      const newActions = [...allQuickActions]
      const temp = newActions[draggedIndex]
      newActions[draggedIndex] = newActions[newIndex]
      newActions[newIndex] = temp

      this.setData({
        allQuickActions: newActions,
        draggedIndex: newIndex,
        startY: currentY
      })
    }
  },

  // 触摸结束
  onTouchEnd() {
    this.setData({
      draggedIndex: -1,
      startDragIndex: -1,
      startY: 0
    }, () => {
      this.updateCurrentQuickActions()
    })
  },

  // 切换快捷功能显示/隐藏
  toggleActionShow(e) {
    const { id } = e.currentTarget.dataset
    const { allQuickActions } = this.data
    const updatedActions = allQuickActions.map(item => {
      if (item.id === id) {
        return { ...item, show: !item.show }
      }
      return item
    })
    this.setData({
      allQuickActions: updatedActions
    }, () => {
      this.updateCurrentQuickActions()
    })
  },



  // 快捷功能点击事件
  onActionTap(e) {
    const { id } = e.currentTarget.dataset
    wx.showToast({
      title: `${id}点击事件`,
      icon: 'none'
    })
  },

  // 查看更多生产数据录入记录
  viewMoreProductionData() {
    wx.showToast({
      title: '查看更多生产数据录入记录',
      icon: 'none'
    })
  },

  // 查看更多模具加工任务
  viewMoreMoldTasks() {
    wx.showToast({
      title: '查看更多模具加工任务',
      icon: 'none'
    })
  },

  // 查看更多非模具加工任务
  viewMoreNonMoldTasks() {
    wx.showToast({
      title: '查看更多非模具加工任务',
      icon: 'none'
    })
  },

  // 查看生产数据详情
  viewProductionDataDetail(e) {
    const { id } = e.currentTarget.dataset
    wx.showToast({
      title: `查看生产数据详情: ${id}`,
      icon: 'none'
    })
  },

  // 查看模具加工任务详情
  viewMoldTaskDetail(e) {
    const { id } = e.currentTarget.dataset
    wx.showToast({
      title: `查看模具加工任务详情: ${id}`,
      icon: 'none'
    })
  },

  // 查看非模具加工任务详情
  viewNonMoldTaskDetail(e) {
    const { id } = e.currentTarget.dataset
    wx.showToast({
      title: `查看非模具加工任务详情: ${id}`,
      icon: 'none'
    })
  }
})