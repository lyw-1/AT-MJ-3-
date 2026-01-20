// 优化筛选面板组件逻辑
Component({
  properties: {
    // 是否展开
    expanded: {
      type: Boolean,
      value: false
    },
    // 筛选组数据
    filterGroups: {
      type: Array,
      value: []
    },
    // 已选择的数量
    activeCount: {
      type: Number,
      value: 0
    }
  },

  data: {
    // 选中的选项值
    selectedOptions: {}
  },

  methods: {
    // 切换筛选面板
    toggleFilter() {
      this.setData({
        expanded: !this.data.expanded
      });
    },

    // 防止关闭面板
    preventClose() {
      // 阻止事件冒泡
    },

    // 选择筛选选项
    selectOption(e) {
      const { group, value } = e.currentTarget.dataset;
      
      // 获取当前组的选中选项
      const currentSelected = this.data.selectedOptions[group] || [];
      
      // 检查是否已选中
      const index = currentSelected.indexOf(value);
      
      if (index > -1) {
        // 取消选中
        currentSelected.splice(index, 1);
      } else {
        // 添加选中
        currentSelected.push(value);
      }
      
      // 更新选中状态
      this.setData({
        [`selectedOptions.${group}`]: currentSelected
      });
      
      // 计算总选中数量
      this.calculateActiveCount();
      
      // 触发选择事件
      this.triggerEvent('change', {
        group,
        value,
        selected: index > -1
      });
    },

    // 计算选中数量
    calculateActiveCount() {
      let count = 0;
      const selectedOptions = this.data.selectedOptions;
      
      Object.keys(selectedOptions).forEach(key => {
        count += selectedOptions[key].length;
      });
      
      this.setData({
        activeCount: count
      });
    },

    // 重置筛选
    resetFilter() {
      this.setData({
        selectedOptions: {},
        activeCount: 0
      });
      
      // 触发重置事件
      this.triggerEvent('reset');
    },

    // 确认筛选
    confirmFilter() {
      const selectedOptions = this.data.selectedOptions;
      
      // 触发确认事件
      this.triggerEvent('confirm', {
        selectedOptions
      });
      
      // 关闭面板
      this.setData({
        expanded: false
      });
    }
  }
});
