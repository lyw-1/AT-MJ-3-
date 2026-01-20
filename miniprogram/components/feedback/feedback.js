// 反馈组件逻辑
Component({
  properties: {
    // Toast配置
    toast: {
      type: Object,
      value: {
        show: false,
        message: '',
        icon: '',
        color: '#6B7280',
        duration: 2000
      }
    },
    // 加载状态配置
    loading: {
      type: Object,
      value: {
        show: false,
        message: '加载中...',
        overlay: true
      }
    },
    // 骨架屏配置
    skeleton: {
      type: Object,
      value: {
        show: false
      }
    },
    // 空状态配置
    emptyState: {
      type: Object,
      value: {
        show: false,
        icon: '📭',
        title: '暂无数据',
        description: '当前没有相关数据',
        actionText: ''
      }
    },
    // 进度条配置
    progress: {
      type: Object,
      value: {
        show: false,
        percent: 0
      }
    }
  },

  data: {},

  methods: {
    // 隐藏Toast
    hideToast() {
      this.setData({
        'toast.show': false
      });
    },

    // 隐藏加载
    hideLoading() {
      this.setData({
        'loading.show': false
      });
    },

    // 隐藏骨架屏
    hideSkeleton() {
      this.setData({
        'skeleton.show': false
      });
    },

    // 隐藏空状态
    hideEmptyState() {
      this.setData({
        'emptyState.show': false
      });
    },

    // 隐藏进度条
    hideProgress() {
      this.setData({
        'progress.show': false
      });
    }
  }
});
