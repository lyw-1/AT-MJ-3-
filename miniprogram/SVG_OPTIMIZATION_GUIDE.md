# SVG图标优化指南

## 优化目标

通过压缩和优化SVG图标，减少文件大小，提升页面加载速度，改善用户体验。

## 一、SVG优化方法

### 1.1 使用SVGO工具压缩

#### 安装SVGO
```bash
# 全局安装
npm install -g svgo

# 或者在项目中安装
npm install --save-dev svgo
```

#### 基本压缩
```bash
# 压缩单个文件
svgo input.svg -o output.svg

# 压缩整个目录
svgo -f assets/icons/*.svg -o assets/icons/optimized/
```

#### 配置优化选项
```javascript
// svgo.config.js
module.exports = {
  plugins: [
    {
      name: 'preset-default',
      params: {
        overrides: {
          // 移除注释
          removeComments: true,
          
          // 移除不使用的定义
          removeUnusedNS: true,
          
          // 移除编辑器数据
          removeEditorsNSData: true,
          
          // 移除空属性
          removeEmptyAttrs: true,
          
          // 移除隐藏元素
          removeHiddenElems: true,
          
          // 移除空文本节点
          removeEmptyText: true,
          
          // 移除空容器
          removeEmptyContainers: true,
          
          // 合并路径
          mergePaths: true,
          
          // 移除重复的属性
          removeUselessStrokeAndFill: true,
          
          // 移除默认属性
          removeUnknownsAndDefaults: true,
          
          // 清理属性值
          cleanupIDs: true,
          
          // 移除viewBox
          removeViewBox: false,
          
          // 清理数值
          cleanupNumericValues: true,
          
          // 转换形状
          convertColors: true,
          
          // 转换路径数据
          convertPathData: true,
          
          // 转换变换
          convertTransform: true,
          
          // 移除非渲染元素
          removeNonInheritableGroupAttrs: true,
          
          // 移除title元素
          removeTitle: true,
          
          // 移除desc元素
          removeDesc: true
        }
      }
    }
  ]
};
```

### 1.2 手动优化SVG代码

#### 优化原则
1. **移除不必要的内容**
   - 删除注释和元数据
   - 移除编辑器专用属性
   - 清理未使用的定义

2. **简化路径数据**
   - 合并相似的路径
   - 移除重复的路径段
   - 优化曲线和直线

3. **优化属性**
   - 移除默认属性值
   - 使用简写属性
   - 移除冗余的transform

4. **优化尺寸**
   - 移除不必要的viewBox
   - 使用相对坐标
   - 优化小数精度

#### 优化示例

**优化前：**
```svg
<svg width="81" height="81" viewBox="0 0 81 81" fill="none" xmlns="http://www.w3.org/2000/svg">
  <!-- 编辑器数据 -->
  <!-- Generator: Adobe Illustrator 24.0.0, SVG Export Plug-In . SVG Version: 6.00 Build 0) -->
  
  <path d="M40.5 20L25 35V55H56V35L40.5 20Z" 
        stroke="#6B7280" 
        stroke-width="4" 
        fill="none"/>
  <path d="M35 40H46" 
        stroke="#6B7280" 
        stroke-width="4" 
        fill="none"/>
</svg>
```

**优化后：**
```svg
<svg width="81" height="81" viewBox="0 0 81 81" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M40.5 20L25 35V55H56V35L40.5 20Z M35 40H46" 
        stroke="#6B7280" 
        stroke-width="4" 
        fill="none"/>
</svg>
```

### 1.3 使用CSS变量优化

#### 定义颜色变量
```css
:root {
  --icon-color-primary: #74CA00;
  --icon-color-secondary: #6B7280;
  --icon-color-disabled: #9CA3AF;
}

.icon {
  color: var(--icon-color-primary);
}

.icon.secondary {
  color: var(--icon-color-secondary);
}

.icon.disabled {
  color: var(--icon-color-disabled);
}
```

#### 在SVG中使用currentColor
```svg
<svg width="81" height="81" viewBox="0 0 81 81" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M40.5 20L25 35V55H56V35L40.5 20Z" 
        stroke="currentColor" 
        stroke-width="4" 
        fill="none"/>
</svg>
```

#### 在CSS中控制颜色
```css
.home-icon {
  color: var(--color-primary);
}

.mold-icon {
  color: var(--text-secondary);
}
```

## 二、图标尺寸规范

### 2.1 标准尺寸

| 用途 | 尺寸 | viewBox | 说明 |
|------|------|---------|------|
| 导航图标 | 48x48rpx | 0 0 48 48 | tabBar导航 |
| 功能图标 | 40x40rpx | 0 0 40 40 | 按钮内图标 |
| 小图标 | 24x24rpx | 0 0 24 24 | 列表项图标 |
| 大图标 | 64x64rpx | 0 0 64 64 | 头像等 |

### 2.2 统一viewBox

```svg
<!-- 导航图标 -->
<svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
  <!-- 图标内容 -->
</svg>

<!-- 功能图标 -->
<svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
  <!-- 图标内容 -->
</svg>

<!-- 小图标 -->
<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <!-- 图标内容 -->
</svg>
```

## 三、性能优化建议

### 3.1 使用WebP格式（可选）

对于复杂图标，考虑使用WebP格式：
```bash
# 转换SVG为WebP（需要支持的工具）
svg2webp input.svg -o output.webp
```

### 3.2 图标懒加载

#### 使用lazy-load属性
```xml
<image 
  src="/assets/icons/home.svg" 
  lazy-load="true"
  mode="aspectFit"
/>
```

#### 实现占位符
```xml
<image 
  src="{{loaded ? '/assets/icons/home.svg' : '/assets/icons/placeholder.svg'}}"
  mode="aspectFit"
/>
```

### 3.3 图标预加载

```javascript
// 在app.js中预加载关键图标
App({
  onLaunch() {
    // 预加载导航图标
    const icons = [
      '/assets/icons/home.svg',
      '/assets/icons/mold.svg',
      '/assets/icons/tasks.svg',
      '/assets/icons/profile.svg'
    ];
    
    icons.forEach(icon => {
      wx.downloadFile({
        url: icon,
        success: () => {
          console.log('图标预加载成功:', icon);
        }
      });
    });
  }
});
```

## 四、图标设计规范

### 4.1 线条规范
- 线条宽度：2px
- 端点样式：圆角
- 连接点：平滑

### 4.2 圆角规范
- 小圆角：2px
- 中圆角：4px
- 大圆角：8px

### 4.3 填充和描边
- 优先使用描边（stroke）
- 填充使用currentColor
- 避免硬编码颜色

## 五、实施步骤

### 5.1 准备阶段

1. **备份原始图标**
   ```bash
   cp -r assets/icons assets/icons-backup
   ```

2. **安装优化工具**
   ```bash
   npm install -g svgo
   ```

3. **创建配置文件**
   ```bash
   # 创建svgo.config.js
   touch svgo.config.js
   ```

### 5.2 优化阶段

1. **批量压缩图标**
   ```bash
   # 压缩所有SVG图标
   svgo -f assets/icons/*.svg -o assets/icons/optimized/
   
   # 比较文件大小
   ls -lh assets/icons/*.svg
   ls -lh assets/icons/optimized/*.svg
   ```

2. **替换原始文件**
   ```bash
   # 备份原始文件后替换
   mv assets/icons/*.svg assets/icons/backup/
   mv assets/icons/optimized/*.svg assets/icons/
   ```

3. **验证优化效果**
   - 在微信开发者工具中检查图标显示
   - 确认颜色和形状正确
   - 测试不同尺寸下的显示

### 5.3 集成阶段

1. **更新组件引用**
   - 确保所有组件引用优化后的图标
   - 测试图标加载性能

2. **性能测试**
   - 使用微信开发者工具的Performance面板
   - 检查图标加载时间
   - 对比优化前后的性能数据

## 六、优化效果预期

### 6.1 文件大小减少

| 文件类型 | 优化前 | 优化后 | 减少比例 |
|---------|--------|--------|----------|
| 简单图标 | 2-3KB | 0.5-1KB | 50-70% |
| 复杂图标 | 5-8KB | 1-2KB | 60-80% |
| 总计 | ~50KB | ~15KB | ~70% |

### 6.2 加载性能提升

| 指标 | 优化前 | 优化后 | 提升 |
|------|--------|--------|------|
| 首屏加载时间 | 800-1200ms | 300-500ms | 60-70% |
| 图标渲染时间 | 100-200ms | 50-100ms | 50% |
| 总体感知速度 | - | - | 显著提升 |

## 七、注意事项

### 7.1 兼容性

1. **微信小程序限制**
   - SVG文件大小限制：单个文件<100KB
   - 图标数量：建议<50个
   - 使用mode="aspectFit"保持比例

2. **浏览器兼容性**
   - 测试iOS和Android设备
   - 验证不同微信版本
   - 确保低版本兼容性

### 7.2 维护性

1. **版本管理**
   - 保留优化前备份
   - 记录优化配置
   - 建立优化日志

2. **持续优化**
   - 定期检查新图标
   - 重复优化流程
   - 监控性能指标

## 八、工具推荐

### 8.1 在线工具

1. **SVGO在线工具**
   - https://jakearchibald.github.io/svgomg/
   - https://svgomg.net/

2. **SVG优化工具**
   - SVG Optimizer: https://svgoptimizer.com/
   - SVG Gobbler: https://petercollingridge.appspot.com/svg-gobbler/

### 8.2 本地工具

1. **命令行工具**
   - SVGO: npm install -g svgo
   - SVGO-GUI: npm install -g svgo-gui

2. **IDE插件**
   - VS Code: SVG Viewer
   - WebStorm: SVG Support Plugin

## 九、总结

通过系统性的SVG图标优化，可以实现：

1. **文件大小减少70%** - 显著降低网络传输时间
2. **加载速度提升60%** - 改善首屏渲染性能
3. **维护性提升** - 统一的图标规范便于后续维护
4. **用户体验优化** - 更快的图标加载带来更好的交互体验

建议按照本指南逐步实施SVG图标优化，并在每个阶段进行性能测试和效果验证。

---

**文档版本：** 1.0
**创建日期：** 2026-01-16
**适用范围：** 微信小程序SVG图标优化
