# 图标系统规范

## 规范概述

建立统一的图标设计规范，确保所有图标在视觉风格、尺寸、使用方式上保持一致性，提升用户体验和品牌识别度。

## 一、图标尺寸规范

### 1.1 标准尺寸定义

| 用途 | 尺寸 | viewBox | 说明 |
|------|------|---------|------|
| 导航图标 | 48×48rpx | 0 0 48 48 | tabBar导航 |
| 功能图标 | 40×40rpx | 0 0 40 40 | 按钮内图标 |
| 小图标 | 24×24rpx | 0 0 24 24 | 列表项图标 |
| 大图标 | 64×64rpx | 0 0 64 64 | 头像等 |

### 1.2 尺寸使用原则

1. **优先使用标准尺寸** - 除非特殊需求，否则使用标准尺寸
2. **保持宽高比** - 所有图标保持1:1的宽高比
3. **使用相对坐标** - SVG中不使用绝对坐标，使用相对坐标
4. **统一viewBox** - 每个尺寸使用对应的viewBox

## 二、图标设计规范

### 2.1 线条规范

- **线条宽度**：2px
- **端点样式**：圆角（round）
- **连接点**：平滑连接，避免锐角
- **线条质量**：清晰、无锯齿

### 2.2 圆角规范

- **小圆角**：2px（小图标）
- **中圆角**：4px（功能图标）
- **大圆角**：8px（大图标）

### 2.3 填充和描边

- **优先使用描边**：图标主要使用描边（stroke）
- **填充使用**：仅用于实心图标
- **颜色使用**：使用currentColor支持CSS变量控制

## 三、图标颜色规范

### 3.1 颜色使用原则

1. **使用currentColor** - 图标颜色通过CSS变量控制
2. **支持深色模式** - 自动适配深色主题
3. **状态颜色** - 使用统一的状态色变量

### 3.2 颜色变量映射

```css
/* 主色调 */
--icon-color-primary: #74CA00;

/* 状态色 */
--icon-color-success: #10B981;
--icon-color-warning: #F59E0B;
--icon-color-error: #EF4444;
--icon-color-info: #6B7280;

/* 中性色 */
--icon-color-default: #6B7280;
--icon-color-disabled: #9CA3AF;
```

### 3.3 SVG中使用currentColor

```svg
<svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path 
        d="M24 4L24 44" 
        stroke="currentColor" 
        stroke-width="2" 
        stroke-linecap="round" 
        stroke-linejoin="round"
  />
</svg>
```

## 四、图标命名规范

### 4.1 命名规则

1. **使用小写字母** - home.svg, mold.svg, tasks.svg
2. **描述性命名** - 使用有意义的名称
3. **状态后缀** - 使用-active、-disabled等后缀
4. **尺寸后缀** - 使用-large、-small等后缀

### 4.2 命名示例

```
home.svg              - 首页图标
home-active.svg        - 首页图标（选中状态）
mold.svg              - 模具图标
tasks.svg             - 任务图标
scan-large.svg        - 大扫描图标
user-avatar.svg        - 用户头像
```

## 五、图标组件规范

### 5.1 Icon组件封装

创建统一的Icon组件，提供一致的图标使用方式：

```javascript
// components/icon/icon.js
Component({
  properties: {
    // 图标名称
    name: {
      type: String,
      value: ''
    },
    // 图标尺寸
    size: {
      type: String,
      value: 'normal' // small | normal | large
    },
    // 图标颜色
    color: {
      type: String,
      value: 'primary' // primary | success | warning | error | info | default
    }
  },

  methods: {
    getIconPath() {
      const iconMap = {
        home: '/assets/icons/home.svg',
        mold: '/assets/icons/mold.svg',
        tasks: '/assets/icons/tasks.svg',
        profile: '/assets/icons/profile.svg',
        scan: '/assets/icons/scan-large.svg'
      };
      
      return iconMap[this.data.name];
    },
    
    getSize() {
      const sizeMap = {
        small: '24rpx',
        normal: '40rpx',
        large: '64rpx'
      };
      
      return sizeMap[this.data.size];
    },
    
    getColor() {
      const colorMap = {
        primary: 'var(--icon-color-primary)',
        success: 'var(--icon-color-success)',
        warning: 'var(--icon-color-warning)',
        error: 'var(--icon-color-error)',
        info: 'var(--icon-color-info)',
        default: 'var(--icon-color-default)'
      };
      
      return colorMap[this.data.color];
    }
  }
});
```

### 5.2 Icon组件使用

```xml
<!-- 使用Icon组件 -->
<icon 
  name="home" 
  size="normal" 
  color="primary"
/>

<!-- 不同尺寸 -->
<icon 
  name="home" 
  size="large" 
  color="primary"
/>

<!-- 不同颜色 -->
<icon 
  name="home" 
  size="normal" 
  color="success"
/>
```

## 六、图标优化指南

### 6.1 SVG优化

1. **移除不必要内容**
   - 删除注释和元数据
   - 移除编辑器专用属性
   - 清理未使用的定义

2. **优化路径数据**
   - 合并相似路径
   - 移除重复的路径段
   - 使用相对坐标

3. **优化属性**
   - 移除默认属性值
   - 使用简写属性
   - 移除冗余的transform

4. **优化尺寸**
   - 移除不必要的viewBox
   - 使用相对坐标
   - 优化小数精度

### 6.2 性能优化

1. **文件大小控制**
   - 单个SVG文件<50KB
   - 总图标数量<50个
   - 使用SVGO压缩

2. **懒加载**
   - 使用lazy-load属性
   - 优先加载首屏图标
   - 实现占位符

3. **缓存策略**
   - 使用wx.setStorage缓存常用图标
   - 设置合理的缓存时间
   - 支持手动刷新缓存

## 七、图标使用示例

### 7.1 在页面中使用

```xml
<!-- 首页 -->
<view class="quick-actions">
  <view class="action-item">
    <icon name="mold" size="normal" color="primary"/>
    <text class="action-text">模库</text>
  </view>
</view>

<!-- 任务页 -->
<view class="task-item">
  <icon name="tasks" size="small" color="default"/>
  <text class="task-title">任务标题</text>
</view>

<!-- 个人中心 -->
<view class="user-avatar">
  <icon name="user-avatar" size="large" color="primary"/>
</view>
```

### 7.2 在CSS中使用

```css
/* 使用CSS变量控制图标颜色 */
.icon {
  color: var(--icon-color-primary);
}

.icon.success {
  color: var(--icon-color-success);
}

.icon.warning {
  color: var(--icon-color-warning);
}

/* 支持深色模式 */
@media (prefers-color-scheme: dark) {
  .icon {
    color: #8CD622;
  }
  
  .icon.success {
    color: #34D399;
  }
}
```

## 八、质量检查清单

### 8.1 设计检查

- [ ] 图标尺寸符合规范
- [ ] 线条宽度一致（2px）
- [ ] 圆角使用正确
- [ ] 颜色使用currentColor
- [ ] viewBox尺寸正确

### 8.2 技术检查

- [ ] SVG文件已优化（SVGO压缩）
- [ ] 文件大小<50KB
- [ ] 图标路径正确
- [ ] 无语法错误

### 8.3 兼容性检查

- [ ] 在不同设备上显示正常
- [ ] 支持深色模式
- [ ] 在不同屏幕尺寸下显示正常
- [ ] 支持横竖屏

## 九、实施步骤

### 9.1 创建阶段

1. **创建Icon组件**
   - 创建components/icon/icon.js
   - 创建components/icon/icon.wxml
   - 创建components/icon/icon.wxss
   - 创建components/icon/icon.json

2. **优化现有图标**
   - 使用SVGO批量压缩
   - 调整图标尺寸符合规范
   - 统一使用currentColor

3. **更新组件引用**
   - 在页面中使用Icon组件
   - 移除直接使用image标签

### 9.2 测试阶段

1. **功能测试**
   - 测试Icon组件各种尺寸
   - 测试Icon组件各种颜色
   - 测试深色模式适配

2. **兼容性测试**
   - 测试不同设备显示
   - 测试不同微信版本

3. **性能测试**
   - 测试图标加载速度
   - 测试页面渲染性能

### 9.3 部署阶段

1. **逐步替换**
   - 优先替换核心页面图标
   - 逐步推广到所有页面
   - 收集用户反馈

2. **监控优化**
   - 监控图标加载性能
   - 收集用户使用反馈
   - 持续优化改进

## 十、维护建议

### 10.1 版本管理

1. **版本控制**
   - 使用Git管理图标版本
   - 记录每次优化变更
   - 保留历史版本

2. **更新日志**
   - 记录新增图标
   - 记录修改内容
   - 记录优化效果

### 10.2 持续优化

1. **定期审查**
   - 定期检查图标一致性
   - 根据用户反馈优化
   - 跟踪行业最佳实践

2. **性能监控**
   - 监控图标文件大小
   - 监控加载性能
   - 优化加载策略

---

**文档版本：** 1.0
**创建日期：** 2026-01-16
**适用范围：** 微信小程序图标系统规范
