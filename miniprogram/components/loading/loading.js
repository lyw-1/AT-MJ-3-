Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 尺寸：small, medium, large
    size: {
      type: String,
      value: 'medium'
    },
    // 类型：spinner, dots, pulse
    type: {
      type: String,
      value: 'spinner'
    },
    // 加载文字
    text: {
      type: String,
      value: ''
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    // 尺寸映射表，单位rpx
    sizeMap: {
      small: '40rpx',
      medium: '60rpx',
      large: '80rpx'
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 组件方法可以在这里添加
  }
});