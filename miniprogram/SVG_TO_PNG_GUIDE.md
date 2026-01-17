# SVG图标转PNG转换指南

## 问题说明
微信小程序的tabBar只支持以下图片格式：
- .png（推荐）
- .jpg
- .jpeg

当前创建的SVG图标需要转换为PNG格式才能在微信小程序中使用。

## 转换方案

### 方案1：使用在线转换工具（推荐，最简单）

1. 访问以下在线转换网站之一：
   - https://convertio.co/zh/svg-png
   - https://cloudconvert.com/svg-to-png
   - https://www.aconvert.com/cn/image/svg-to-png

2. 上传SVG文件：
   - home.svg
   - home-active.svg
   - mold.svg
   - mold-active.svg
   - tasks.svg
   - tasks-active.svg
   - profile.svg
   - profile-active.svg

3. 设置转换参数：
   - 输出格式：PNG
   - 尺寸：81x81像素（微信小程序标准尺寸）
   - 背景透明

4. 下载转换后的PNG文件

5. 将PNG文件保存到：`miniprogram/assets/icons/`目录

### 方案2：使用ImageMagick命令行工具

1. 安装ImageMagick：
   - Windows: 下载并安装 https://imagemagick.org/script/download.php
   - Mac: `brew install imagemagick`
   - Linux: `sudo apt-get install imagemagick`

2. 在miniprogram目录下运行转换脚本：
   ```bash
   node convert_svg_to_png.js
   ```

### 方案3：使用Python脚本

1. 安装依赖：
   ```bash
   pip install cairosvg pillow
   ```

2. 运行转换脚本：
   ```bash
   python convert_svg_to_png.py
   ```

### 方案4：使用HTML工具（已提供）

1. 在浏览器中打开：`miniprogram/svg-to-png.html`
2. 拖拽SVG文件到上传区域
3. 点击下载按钮保存PNG文件
4. 将PNG文件保存到`miniprogram/assets/icons/`目录

## 转换后的文件结构

转换完成后，`miniprogram/assets/icons/`目录应包含以下文件：

```
assets/icons/
├── home.png              (首页图标 - 未选中)
├── home-active.png        (首页图标 - 选中)
├── mold.png              (模库图标 - 未选中)
├── mold-active.png        (模库图标 - 选中)
├── tasks.png             (任务图标 - 未选中)
├── tasks-active.png       (任务图标 - 选中)
├── profile.png           (我的图标 - 未选中)
└── profile-active.png     (我的图标 - 选中)
```

## 更新app.json配置

转换完成后，需要更新`app.json`文件中的图标路径：

```json
{
  "tabBar": {
    "color": "#6B7280",
    "selectedColor": "#6150BF",
    "backgroundColor": "#FFFFFF",
    "list": [
      {
        "pagePath": "pages/index/index",
        "text": "首页",
        "iconPath": "assets/icons/home.png",
        "selectedIconPath": "assets/icons/home-active.png"
      },
      {
        "pagePath": "pages/mold/list/index",
        "text": "模库",
        "iconPath": "assets/icons/mold.png",
        "selectedIconPath": "assets/icons/mold-active.png"
      },
      {
        "pagePath": "pages/tasks/index",
        "text": "任务",
        "iconPath": "assets/icons/tasks.png",
        "selectedIconPath": "assets/icons/tasks-active.png"
      },
      {
        "pagePath": "pages/profile/index",
        "text": "我的",
        "iconPath": "assets/icons/profile.png",
        "selectedIconPath": "assets/icons/profile-active.png"
      }
    ]
  }
}
```

## 验证步骤

1. 确保所有PNG文件已正确放置在`miniprogram/assets/icons/`目录
2. 确认`app.json`中的图标路径正确
3. 在微信开发者工具中重新编译项目
4. 检查tabBar图标是否正常显示
5. 测试图标在选中/未选中状态下的颜色变化

## 注意事项

- 图标尺寸必须为81x81像素（微信小程序标准）
- 未选中图标建议使用灰色（#6B7280）
- 选中图标建议使用主题色（#6150BF）
- 图标背景必须透明
- 图标内容居中，留有适当边距
- 确保PNG文件质量良好，无锯齿或模糊