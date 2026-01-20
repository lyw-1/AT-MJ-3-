// 面包屑导航组件逻辑
Component({
  properties: {
    // 是否显示
    show: {
      type: Boolean,
      value: false
    },
    // 面包屑数据
    items: {
      type: Array,
      value: []
    }
  },

  methods: {
    // 点击面包屑
    onBreadcrumbTap(e) {
      const { index } = e.currentTarget.dataset;
      
      if (index < this.data.items.length - 1) {
        // 跳转到对应页面
        const item = this.data.items[index];
        if (item.path) {
          wx.navigateTo({
            url: item.path,
            fail: () => {
              console.error('导航失败:', item.path);
            }
          });
        }
      }
    }
  }
});
