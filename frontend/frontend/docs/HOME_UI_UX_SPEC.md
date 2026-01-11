# AT模具管理系统 - HOME页面UI/UX设计规范

## 1. 概述

本设计规范基于HOME页面主区域的现有设计，旨在为整个系统的UI/UX设计提供统一的指导标准。所有规范均与HOME页面的设计风格保持高度一致，确保系统视觉风格和用户体验的统一连贯。

## 2. 色彩系统

### 2.1 主色调

| 色值 | 名称 | 应用场景 | 示例 |
|------|------|----------|------|
| `#6150BF` | 主色 | 主要按钮、导航高亮、强调文本 | <div style="width: 20px; height: 20px; background-color: #6150BF; border-radius: 4px; display: inline-block;"></div> |
| `#4F46E5` | 主色加深 | 按钮悬停、激活状态 | <div style="width: 20px; height: 20px; background-color: #4F46E5; border-radius: 4px; display: inline-block;"></div> |
| `#EEF2FF` | 主色浅色 | 背景、边框、辅助元素 | <div style="width: 20px; height: 20px; background-color: #EEF2FF; border-radius: 4px; display: inline-block;"></div> |

### 2.2 辅助色

| 色值 | 名称 | 应用场景 | 示例 |
|------|------|----------|------|
| `#10B981` | 成功色 | 成功状态、积极数据 | <div style="width: 20px; height: 20px; background-color: #10B981; border-radius: 4px; display: inline-block;"></div> |
| `#F59E0B` | 警告色 | 警告状态、中等优先级 | <div style="width: 20px; height: 20px; background-color: #F59E0B; border-radius: 4px; display: inline-block;"></div> |
| `#EF4444` | 错误色 | 错误状态、高优先级 | <div style="width: 20px; height: 20px; background-color: #EF4444; border-radius: 4px; display: inline-block;"></div> |
| `#6B7280` | 信息色 | 中性信息、次要文本 | <div style="width: 20px; height: 20px; background-color: #6B7280; border-radius: 4px; display: inline-block;"></div> |

### 2.3 中性色

| 色值 | 名称 | 应用场景 | 示例 |
|------|------|----------|------|
| `#FFFFFF` | 白色 | 卡片背景、容器背景 | <div style="width: 20px; height: 20px; background-color: #FFFFFF; border: 1px solid #E5E7EB; border-radius: 4px; display: inline-block;"></div> |
| `#F8FAFB` | 浅灰1 | 页面背景、次要背景 | <div style="width: 20px; height: 20px; background-color: #F8FAFB; border-radius: 4px; display: inline-block;"></div> |
| `#E2E8F0` | 浅灰2 | 边框、分隔线 | <div style="width: 20px; height: 20px; background-color: #E2E8F0; border-radius: 4px; display: inline-block;"></div> |
| `#CBD5E1` | 浅灰3 | 禁用状态、辅助文本 | <div style="width: 20px; height: 20px; background-color: #CBD5E1; border-radius: 4px; display: inline-block;"></div> |
| `#94A3B8` | 中灰 | 次要文本、图标 | <div style="width: 20px; height: 20px; background-color: #94A3B8; border-radius: 4px; display: inline-block;"></div> |
| `#64748B` | 深灰1 | 主要文本、标题 | <div style="width: 20px; height: 20px; background-color: #64748B; border-radius: 4px; display: inline-block;"></div> |
| `#475569` | 深灰2 | 重要文本、强调文本 | <div style="width: 20px; height: 20px; background-color: #475569; border-radius: 4px; display: inline-block;"></div> |
| `#334155` | 深灰3 | 页面标题、重要信息 | <div style="width: 20px; height: 20px; background-color: #334155; border-radius: 4px; display: inline-block;"></div> |

### 2.4 功能色

| 色值 | 名称 | 应用场景 | 示例 |
|------|------|----------|------|
| `linear-gradient(135deg, #047857 0%, #1E3A8A 100%)` | 统计卡片1 | 验收合格率卡片 | <div style="width: 20px; height: 20px; background: linear-gradient(135deg, #047857 0%, #1E3A8A 100%); border-radius: 4px; display: inline-block;"></div> |
| `linear-gradient(135deg, #1E3A8A 0%, #3B82F6 100%)` | 统计卡片2 | 裸模库数量卡片 | <div style="width: 20px; height: 20px; background: linear-gradient(135deg, #1E3A8A 0%, #3B82F6 100%); border-radius: 4px; display: inline-block;"></div> |
| `linear-gradient(135deg, #0E7490 0%, #06B6D4 100%)` | 统计卡片3 | 可用库数量卡片 | <div style="width: 20px; height: 20px; background: linear-gradient(135deg, #0E7490 0%, #06B6D4 100%); border-radius: 4px; display: inline-block;"></div> |
| `linear-gradient(135deg, #6B7280 0%, #9CA3AF 100%)` | 统计卡片4 | 封存库数量卡片 | <div style="width: 20px; height: 20px; background: linear-gradient(135deg, #6B7280 0%, #9CA3AF 100%); border-radius: 4px; display: inline-block;"></div> |

## 3. 排版体系

### 3.1 字体家族

- 主要字体：系统默认无衬线字体（如：Microsoft YaHei, PingFang SC, Roboto）
- 字体栈：`'Segoe UI', 'Microsoft YaHei', 'PingFang SC', 'Roboto', sans-serif`

### 3.2 字号层级

| 字号 | 名称 | 应用场景 | 字重 | 行高 | 颜色 |
|------|------|----------|------|------|------|
| 20px | H1 | 页面大标题、欢迎语 | 700 | 1.3 | 深灰3 (#334155) |
| 16px | H2 | 区域标题、卡片标题 | 600 | 1.4 | 深灰2 (#475569) |
| 14px | H3 | 小标题、模块标题 | 600 | 1.5 | 深灰2 (#475569) |
| 14px | 正文 | 主要文本、描述 | 500 | 1.5 | 深灰1 (#64748B) |
| 13px | 辅助文本 | 次要信息、元数据 | 500 | 1.4 | 中灰 (#94A3B8) |
| 12px | 小号文本 | 标签、提示、状态 | 500 | 1.3 | 中灰 (#94A3B8) |

### 3.3 段落间距

- 段落间距：16px
- 标题与正文间距：8px
- 列表项间距：8px

## 4. 组件规则

### 4.1 卡片组件

#### 基本卡片

| 属性 | 值 | 示例 |
|------|-----|------|
| 背景色 | 白色 (#FFFFFF) | |
| 边框 | 1px solid 浅灰2 (#E2E8F0) | |
| 边框半径 | 15px | |
| 阴影 | 0 4px 12px rgba(0, 0, 0, 0.15) | |
| 内边距 | 24px | |
| 外边距 | 1px 0 | |
| 悬停效果 | 阴影减弱：0 2px 8px rgba(0, 0, 0, 0.1) | |

```html
<div class="card">
  <div class="card-header">
    <h3 class="card-title">卡片标题</h3>
    <div class="card-actions">
      <button class="el-button el-button--small el-button--default">查看全部</button>
    </div>
  </div>
  <div class="card-content">
    <!-- 卡片内容 -->
  </div>
</div>
```

#### 统计卡片

| 属性 | 值 |
|------|-----|
| 固定高度 | 120px |
| 渐变背景 | 见色彩系统-功能色 |
| 文字颜色 | 白色 (#FFFFFF) |
| 悬停效果 | 阴影减弱：0 2px 8px rgba(0, 0, 0, 0.1) |

#### 快捷入口卡片

| 属性 | 值 |
|------|-----|
| 最小高度 | 50px |
| 文字居中 | 是 |
| 悬停效果 | 阴影减弱：0 2px 8px rgba(0, 0, 0, 0.1) |

### 4.2 按钮样式

#### 主按钮

| 属性 | 值 | 示例 |
|------|-----|------|
| 背景色 | 主色 (#6150BF) | |
| 文字颜色 | 白色 (#FFFFFF) | |
| 边框半径 | 18px | |
| 内边距 | 8px 16px | |
| 悬停效果 | 背景色加深 (#4F46E5)，阴影加深 | |

#### 次要按钮

| 属性 | 值 | 示例 |
|------|-----|------|
| 背景色 | 浅灰1 (#F8FAFB) | |
| 文字颜色 | 深灰1 (#64748B) | |
| 边框 | 1px solid 浅灰2 (#E2E8F0) | |
| 边框半径 | 18px | |
| 内边距 | 8px 16px | |
| 悬停效果 | 背景色加深 (#E2E8F0) | |

### 4.3 表单元素

#### 输入框

| 属性 | 值 | 示例 |
|------|-----|------|
| 边框半径 | 12px | |
| 边框颜色 | 浅灰2 (#E2E8F0) | |
| 背景色 | 白色 (#FFFFFF) | |
| 内边距 | 10px 12px | |
| 聚焦效果 | 边框颜色变为主色 (#6150BF)，添加阴影 | |

### 4.4 导航元素

#### 顶部导航

| 属性 | 值 | 示例 |
|------|-----|------|
| 高度 | 64px | |
| 背景色 | 白色 (#FFFFFF) | |
| 边框底部 | 1px solid 浅灰2 (#E2E8F0) | |
| 阴影 | 0 2px 8px rgba(0, 0, 0, 0.08) | |

#### 快捷入口导航

| 属性 | 值 | 示例 |
|------|-----|------|
| 背景色 | 白色 (#FFFFFF) | |
| 边框 | 1px solid 浅灰2 (#E2E8F0) | |
| 边框半径 | 15px | |
| 阴影 | 0 2px 8px rgba(0, 0, 0, 0.08) | |
| 间距 | 16px | |

## 5. 布局标准

### 5.1 网格系统

采用Element Plus的24列栅格系统，具体应用如下：

| 屏幕尺寸 | 列数分配 | 示例 |
|----------|----------|------|
| 超大屏幕 (≥1920px) | 统计卡片：6列/个 | |
| 大屏幕 (≥1200px) | 统计卡片：6列/个 | |
| 中等屏幕 (≥992px) | 统计卡片：6列/个 | |
| 小屏幕 (≥768px) | 统计卡片：12列/个 | |
| 超小屏幕 (<768px) | 统计卡片：24列/个 | |

### 5.2 边距规范

| 元素 | 边距 | 示例 |
|------|------|------|
| 页面容器 | 15px (左右) | |
| 卡片内部 | 24px | |
| 卡片之间 | 10px (上下) | |
| 模块之间 | 10px (上下) | |
| 组件内部 | 8px-16px | |

### 5.3 容器尺寸

| 容器 | 最大宽度 | 示例 |
|------|----------|------|
| 页面主容器 | 100% | |
| 卡片容器 | 100% | |
| 统计卡片 | 100% (固定高度120px) | |

### 5.4 响应式断点

| 断点 | 尺寸 | 布局调整 |
|------|------|----------|
| 超大屏幕 | ≥1920px | 4列布局 |
| 大屏幕 | ≥1200px | 4列布局 |
| 中等屏幕 | ≥992px | 4列布局 |
| 小屏幕 | ≥768px | 2列布局 |
| 超小屏幕 | <768px | 1列布局 |

## 6. 交互模式

### 6.1 悬停效果

| 元素 | 悬停效果 | 示例 |
|------|----------|------|
| 卡片 | 阴影加深，轻微上浮 | |
| 按钮 | 背景色变化，阴影加深 | |
| 快捷入口 | 上移1px，阴影加深 | |
| 统计卡片 | 上移3px，阴影加深 | |

### 6.2 过渡动画

| 元素 | 动画属性 | 时长 | 示例 |
|------|----------|------|------|
| 卡片 | box-shadow, transform | 0.3s | |
| 按钮 | background-color, box-shadow | 0.2s | |
| 导航元素 | color, border-color | 0.2s | |
| 模态框 | opacity, transform | 0.3s | |

### 6.3 加载状态

- 加载动画：使用Element Plus的`el-loading`组件
- 加载遮罩：半透明黑色背景 (rgba(0, 0, 0, 0.3))
- 加载文本：居中显示 "加载中..."

### 6.4 反馈机制

- 成功提示：绿色背景，白色文字，自动消失
- 错误提示：红色背景，白色文字，自动消失
- 警告提示：黄色背景，深色文字，自动消失
- 信息提示：蓝色背景，白色文字，自动消失

## 7. 代码规范

### 7.1 命名规范

- CSS类名：使用BEM命名规范
- 组件名：首字母大写，驼峰命名
- 变量名：小写字母，下划线分隔

### 7.2 样式组织

```scss
// 导入外部样式变量
@use '@/styles/variables' as *;
@use 'sass:color';

// 组件样式
.component-name {
  // 基础样式
  
  // 子元素样式
  &__child-element {
    // 样式
  }
  
  // 修饰符样式
  &--modifier {
    // 样式
  }
  
  // 响应式样式
  @media (max-width: 768px) {
    // 样式
  }
}
```

## 8. 设计工具与资源

### 8.1 设计工具

- Figma：用于设计稿创作
- Adobe Photoshop：用于图片处理
- Adobe Illustrator：用于图标设计

### 8.2 资源链接

- Element Plus图标库：https://element-plus.org/zh-CN/component/icon.html
- 颜色参考：https://tailwindcss.com/docs/customizing-colors
- 渐变生成器：https://cssgradient.io/

## 9. 附录

### 9.1 设计原则

1. **简洁明了**：保持界面简洁，减少视觉干扰
2. **一致性**：所有组件和页面遵循统一的设计规范
3. **可用性**：确保界面易于理解和使用
4. **响应式**：适配不同屏幕尺寸
5. **反馈及时**：为用户操作提供明确的反馈

### 9.2 可访问性

- 颜色对比度：符合WCAG AA级标准（至少4.5:1）
- 键盘导航：支持纯键盘操作
- 屏幕阅读器：兼容屏幕阅读器
- 语义化HTML：使用正确的HTML标签

### 9.3 性能优化

- 图片优化：压缩图片，使用适当的格式
- 减少HTTP请求：合并CSS和JavaScript文件
- 懒加载：延迟加载非关键资源
- 缓存策略：合理使用浏览器缓存

## 10. 版本历史

| 版本 | 日期 | 作者 | 更改内容 |
|------|------|------|----------|
| 1.0 | 2025-12-05 | AI助手 | 初始版本 |

## 11. 批准

| 角色 | 姓名 | 日期 | 签名 |
|------|------|------|------|
| 产品经理 | - | - | - |
| UI设计师 | - | - | - |
| 开发负责人 | - | - | - |

---

**文档说明**：本规范将根据产品迭代和设计优化不断更新，确保始终与最新的设计趋势和用户需求保持一致。