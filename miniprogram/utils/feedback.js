// 交互反馈工具组件
// 提供Toast、Loading、Skeleton等多种反馈机制

class FeedbackUtil {
  /**
   * 显示Toast提示
   * @param {string} message - 提示消息
   * @param {string} type - 提示类型: 'success' | 'error' | 'warning' | 'info'
   * @param {number} duration - 显示时长(毫秒)，默认2000ms
   */
  static showToast(message, type = 'info', duration = 2000) {
    const icons = {
      success: '✓',
      error: '✕',
      warning: '!',
      info: 'i'
    };
    
    const colors = {
      success: '#10B981',
      error: '#EF4444',
      warning: '#F59E0B',
      info: '#6B7280'
    };

    // 获取当前页面栈
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    // 创建Toast数据
    const toastData = {
      message,
      icon: icons[type],
      color: colors[type],
      duration,
      show: true
    };

    // 设置到页面数据中
    if (currentPage.setData) {
      currentPage.setData({
        toast: toastData
      });

      // 自动隐藏
      setTimeout(() => {
        currentPage.setData({
          toast: { show: false }
        });
      }, duration);
    }
  }

  /**
   * 显示加载状态
   * @param {string} message - 加载提示文字，可选
   * @param {boolean} overlay - 是否显示遮罩层，默认true
   */
  static showLoading(message = '加载中...', overlay = true) {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        loading: {
          show: true,
          message,
          overlay
        }
      });
    }
  }

  /**
   * 隐藏加载状态
   */
  static hideLoading() {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        loading: {
          show: false
        }
      });
    }
  }

  /**
   * 显示骨架屏
   * @param {boolean} show - 是否显示
   */
  static toggleSkeleton(show) {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        skeleton: {
          show
        }
      });
    }
  }

  /**
   * 触觉反馈 - 震动
   * @param {string} type - 震动类型: 'light' | 'medium' | 'heavy'
   */
  static vibrate(type = 'medium') {
    const types = {
      light: 10,
      medium: 15,
      heavy: 25
    };

    if (wx.vibrateShort) {
      wx.vibrateShort(types[type]);
    }
  }

  /**
   * 触觉反馈 - 触感
   * @param {string} type - 触感类型: 'light' | 'medium' | 'heavy'
   */
  static haptic(type = 'medium') {
    const types = {
      light: 'light',
      medium: 'medium',
      heavy: 'heavy'
    };

    if (wx.vibrateShort) {
      wx.vibrateShort({
        type: types[type]
      });
    }
  }

  /**
   * 显示确认对话框
   * @param {object} options - 配置选项
   * @param {string} options.title - 标题
   * @param {string} options.content - 内容
   * @param {array} options.buttons - 按钮数组
   * @param {function} options.success - 确认回调
   * @param {function} options.cancel - 取消回调
   */
  static showConfirm(options) {
    const {
      title = '提示',
      content = '确定执行此操作吗？',
      buttons = [
        { text: '取消', type: 'default' },
        { text: '确定', type: 'primary' }
      ],
      success,
      cancel
    } = options;

    wx.showModal({
      title,
      content,
      confirmText: buttons[1].text,
      cancelText: buttons[0].text,
      confirmColor: buttons[1].type === 'primary' ? '#74CA00' : '#6B7280',
      cancelColor: '#6B7280',
      success: (res) => {
        if (res.confirm && success) {
          success();
        } else if (res.cancel && cancel) {
          cancel();
        }
      }
    });
  }

  /**
   * 显示操作菜单
   * @param {array} items - 菜单项数组
   * @param {function} success - 选择回调
   */
  static showActionSheet(items, success) {
    wx.showActionSheet({
      itemList: items.map(item => item.name),
      success: (res) => {
        if (res.tapIndex >= 0 && success) {
          success(items[res.tapIndex]);
        }
      }
    });
  }

  /**
   * 显示空状态
   * @param {object} options - 配置选项
   */
  static showEmptyState(options) {
    const {
      icon = '📭',
      title = '暂无数据',
      description = '当前没有相关数据',
      actionText = '去创建',
      action = null
    } = options;

    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        emptyState: {
          icon,
          title,
          description,
          actionText,
          show: true
        }
      });
    }
  }

  /**
   * 隐藏空状态
   */
  static hideEmptyState() {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        emptyState: {
          show: false
        }
      });
    }
  }

  /**
   * 显示进度条
   * @param {number} percent - 进度百分比(0-100)
   */
  static showProgress(percent) {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        progress: {
          percent,
          show: true
        }
      });
    }
  }

  /**
   * 隐藏进度条
   */
  static hideProgress() {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];

    if (currentPage.setData) {
      currentPage.setData({
        progress: {
          show: false
        }
      });
    }
  }
}

module.exports = FeedbackUtil;
