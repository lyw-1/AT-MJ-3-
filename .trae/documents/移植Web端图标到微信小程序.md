## 移植Web端图标到微信小程序

### 问题分析
Web端使用的是Element Plus图标库（@element-plus/icons-vue），这些是SVG格式的图标，以Vue组件的形式使用。微信小程序需要PNG格式的图标文件（通常尺寸为81x81像素）。

### Web端图标分析
从Web端路由配置中发现的图标：
- House (首页)
- Box (模库管理) 
- Tools (加工管理)
- TrendCharts (生产管理)
- Monitor (设备管理)
- Operation (调模管理)
- SetUp (系统管理)
- DataAnalysis (仪表盘)
- List (工序管理)

### 解决方案
由于无法直接将Element Plus的SVG图标转换为PNG文件，需要采用以下方案之一：

#### 方案1：使用在线图标库下载（推荐）
1. 访问免费图标网站（如iconfont、Flaticon、Iconfinder等）
2. 搜索并下载对应的PNG图标：
   - 首页图标：home/home.png, home-active.png
   - 模库图标：mold/box.png, mold-active.png  
   - 任务图标：tasks/list.png, tasks-active.png
   - 我的图标：profile/user.png, profile-active.png
3. 图标尺寸：81x81像素（微信小程序标准尺寸）
4. 创建miniprogram/assets/icons目录
5. 将下载的图标放入对应目录
6. 更新app.json配置，添加图标路径

#### 方案2：使用微信小程序官方图标
1. 使用微信开发者工具自带的图标资源
2. 或者使用微信小程序官方推荐的图标库

#### 方案3：使用CSS绘制图标（临时方案）
1. 使用CSS和伪元素绘制简单的图标
2. 不需要外部图片文件
3. 但视觉效果可能不如专业图标

### 实施步骤（方案1）
1. 创建目录结构：miniprogram/assets/icons/
2. 下载并放置图标文件
3. 修改app.json，添加iconPath和selectedIconPath配置
4. 验证图标显示效果

### 注意事项
- 图标尺寸必须为81x81像素（微信小程序标准）
- 未选中图标建议使用灰色（#6B7280）
- 选中图标建议使用主题色（#6150BF）
- 图标背景必须透明
- 图标内容居中，留有适当边距