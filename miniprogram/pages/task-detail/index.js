// pages/task-detail/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 任务详情
    task: {
      id: 1,
      title: '模具加工任务',
      description: '完成模具编号M001的加工任务，按照图纸要求进行加工，确保尺寸精度和表面质量符合标准要求。',
      status: 'pending',
      statusText: '待处理',
      assignee: '张三',
      createTime: '2026-01-09 14:30',
      updateTime: '2026-01-09 14:30',
      priority: '高',
      progress: 30,
      steps: [
        {
          id: 1,
          title: '领料',
          description: '领取加工所需的原材料',
          completed: true
        },
        {
          id: 2,
          title: '粗加工',
          description: '对模具进行粗加工',
          completed: true
        },
        {
          id: 3,
          title: '精加工',
          description: '对模具进行精加工',
          completed: false
        },
        {
          id: 4,
          title: '检测',
          description: '对加工完成的模具进行质量检测',
          completed: false
        },
        {
          id: 5,
          title: '入库',
          description: '将合格的模具入库',
          completed: false
        }
      ],
      logs: [
        {
          id: 1,
          content: '任务创建',
          user: '系统',
          createTime: '2026-01-09 14:30'
        },
        {
          id: 2,
          content: '任务分配给张三',
          user: '管理员',
          createTime: '2026-01-09 14:35'
        },
        {
          id: 3,
          content: '张三开始处理任务',
          user: '张三',
          createTime: '2026-01-10 09:00'
        },
        {
          id: 4,
          content: '完成领料步骤',
          user: '张三',
          createTime: '2026-01-10 10:00'
        },
        {
          id: 5,
          content: '完成粗加工步骤',
          user: '张三',
          createTime: '2026-01-10 12:00'
        }
      ]
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const id = options.id
    // 根据ID获取任务详情
    this.getTaskDetail(id)
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
    // 刷新任务详情
    this.getTaskDetail(this.data.task.id)
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

  // 获取任务详情
  getTaskDetail(id) {
    // 这里应该调用API获取真实数据
    // 模拟数据已在data中定义
    console.log('获取任务详情:', id)
  },

  // 编辑任务
  onEdit() {
    wx.showToast({
      title: '编辑功能开发中',
      icon: 'none'
    })
  },

  // 完成任务
  onComplete() {
    wx.showModal({
      title: '确认完成',
      content: '确定要标记此任务为已完成吗？',
      success: (res) => {
        if (res.confirm) {
          // 更新任务状态
          this.setData({
            task: {
              ...this.data.task,
              status: 'completed',
              statusText: '已完成',
              progress: 100
            }
          })
          wx.showToast({
            title: '任务已完成',
            icon: 'success'
          })
        }
      }
    })
  }
})