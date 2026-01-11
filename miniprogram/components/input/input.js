// components/input/input.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 输入框类型：text / number / idcard / digit / password
    type: {
      type: String,
      value: 'text'
    },
    // 输入框尺寸：large / default / small
    size: {
      type: String,
      value: 'default'
    },
    // 输入框标签
    label: {
      type: String,
      value: ''
    },
    // 输入框值
    value: {
      type: String,
      value: ''
    },
    // 占位符
    placeholder: {
      type: String,
      value: ''
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      value: false
    },
    // 是否显示清除按钮
    clearable: {
      type: Boolean,
      value: false
    },
    // 是否显示错误状态
    error: {
      type: Boolean,
      value: false
    },
    // 错误信息
    errorMessage: {
      type: String,
      value: ''
    },
    // 最大长度
    maxlength: {
      type: Number,
      value: -1
    },
    // 是否自动聚焦
    focus: {
      type: Boolean,
      value: false
    },
    // 前缀图标
    prefixIcon: {
      type: String,
      value: ''
    },
    // 后缀图标
    suffixIcon: {
      type: String,
      value: ''
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    // 内部输入值
    inputValue: '',
    // 是否显示清除按钮
    showClear: false
  },

  /**
   * 组件生命周期函数-在组件实例进入页面节点树时执行
   */
  attached() {
    // 初始化输入值
    this.setData({
      inputValue: this.properties.value
    })
    // 初始化清除按钮显示状态
    this.updateClearStatus()
  },

  /**
   * 组件生命周期函数-监听属性变化
   */
  observers: {
    'value': function(newVal) {
      this.setData({
        inputValue: newVal
      })
      this.updateClearStatus()
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 处理输入事件
    handleInput(e) {
      const value = e.detail.value
      this.setData({
        inputValue: value
      })
      this.updateClearStatus()
      // 触发输入事件
      this.triggerEvent('input', { value })
    },

    // 处理聚焦事件
    handleFocus(e) {
      this.triggerEvent('focus', e.detail)
    },

    // 处理失焦事件
    handleBlur(e) {
      this.triggerEvent('blur', e.detail)
    },

    // 处理确认事件
    handleConfirm(e) {
      this.triggerEvent('confirm', e.detail)
    },

    // 处理清除事件
    handleClear() {
      this.setData({
        inputValue: ''
      })
      this.updateClearStatus()
      // 触发输入事件
      this.triggerEvent('input', { value: '' })
      // 触发清除事件
      this.triggerEvent('clear')
    },

    // 更新清除按钮显示状态
    updateClearStatus() {
      const showClear = this.properties.clearable && this.data.inputValue !== '' && !this.properties.disabled
      this.setData({
        showClear
      })
    }
  }
})