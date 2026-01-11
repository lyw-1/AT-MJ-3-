// components/button/button.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 按钮类型：primary / success / warning / danger / info / text
    type: {
      type: String,
      value: 'primary'
    },
    // 按钮尺寸：large / default / small
    size: {
      type: String,
      value: 'default'
    },
    // 按钮文字
    text: {
      type: String,
      value: '按钮'
    },
    // 是否为朴素按钮
    plain: {
      type: Boolean,
      value: false
    },
    // 是否为圆角按钮
    round: {
      type: Boolean,
      value: false
    },
    // 是否为圆形按钮
    circle: {
      type: Boolean,
      value: false
    },
    // 是否加载中状态
    loading: {
      type: Boolean,
      value: false
    },
    // 是否禁用状态
    disabled: {
      type: Boolean,
      value: false
    }
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 处理点击事件
    handleClick() {
      // 如果按钮禁用或加载中，不触发点击事件
      if (this.data.disabled || this.data.loading) {
        return
      }
      // 触发自定义点击事件
      this.triggerEvent('click')
    }
  }
})