// custom-tab-bar/index.js
Component({
  data: {
    selected: 0,
    color: '#6B7280',
    selectedColor: '#3B82F6',
    list: [
      {
        pagePath: '/pages/index/index',
        text: '首页',
        iconPath: '/assets/icons/home.svg',
        selectedIconPath: '/assets/icons/home-active.svg'
      },
      {
        pagePath: '/pages/mold/list/index',
        text: '模库',
        iconPath: '/assets/icons/mold.svg',
        selectedIconPath: '/assets/icons/mold-active.svg'
      },
      {
        pagePath: '/pages/scan/index',
        text: '扫描',
        iconPath: '/assets/icons/scan-large.svg',
        selectedIconPath: '/assets/icons/scan-large.svg'
      },
      {
        pagePath: '/pages/tasks/index',
        text: '任务',
        iconPath: '/assets/icons/tasks.svg',
        selectedIconPath: '/assets/icons/tasks-active.svg'
      },
      {
        pagePath: '/pages/profile/index',
        text: '我的',
        iconPath: '/assets/icons/profile.svg',
        selectedIconPath: '/assets/icons/profile-active.svg'
      }
    ]
  },
  attached() {
    console.log('TabBar attached, initial selected:', this.data.selected)
  },
  
  pageLifetimes: {
    show() {
      console.log('TabBar pageLifetimes.show triggered')
      // 当页面显示时，更新tabBar选中状态
      const pages = getCurrentPages()
      console.log('All pages:', pages.map(p => p.route))
      
      if (pages.length === 0) {
        console.log('No pages found')
        return
      }
      
      const currentPage = pages[pages.length - 1]
      console.log('Current page:', currentPage)
      
      if (!currentPage || !currentPage.route) {
        console.log('Invalid current page')
        return
      }
      
      const currentPath = '/' + currentPage.route
      console.log('Current page path:', currentPath)
      console.log('List pagePaths:', this.data.list.map(item => item.pagePath))
      
      const index = this.data.list.findIndex(item => item.pagePath === currentPath)
      console.log('Found index:', index, 'Current selected:', this.data.selected)
      
      if (index !== -1 && index !== this.data.selected) {
        console.log('Updating selected from', this.data.selected, 'to', index)
        this.setData({
          selected: index
        }, () => {
          console.log('Selected updated in pageLifetimes.show:', this.data.selected)
        })
      }
    }
  },
  methods: {
    switchTab(e) {
      const data = e.currentTarget.dataset
      const path = data.path
      const index = data.index
      
      console.log('switchTab clicked:', { path, index, currentSelected: this.data.selected })
      
      // 特殊处理：点击扫描按钮直接调用扫码API
      if (index === 2) {
        // 调用微信小程序扫码API
        wx.scanCode({
          onlyFromCamera: true, // 只允许从相机扫码
          success: (res) => {
            console.log('扫码成功:', res)
            // 可以在这里处理扫码结果，比如跳转到对应页面或执行相应逻辑
          },
          fail: (err) => {
            console.error('扫码失败:', err)
            // 可以在这里处理扫码失败的情况
          }
        })
      } else {
        // 先更新选中状态，再执行页面切换
        console.log('Updating selected to:', index)
        this.setData({
          selected: index
        }, () => {
          console.log('Selected updated, current value:', this.data.selected)
        })
        // 其他按钮执行正常的tab切换
        wx.switchTab({
          url: path
        })
      }
    }
  }
})
