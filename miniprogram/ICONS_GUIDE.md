# 微信小程序图标配置指南

## 问题说明
微信小程序的tabBar只支持PNG、JPG、JPEG格式，不支持SVG格式。当前项目中已经创建了SVG图标，但需要转换为PNG格式才能正常使用。

## 当前状态
已成功修改`app.json`文件，移除了所有`iconPath`和`selectedIconPath`配置。现在小程序能够正常启动，tabBar将只显示文字。

## 后续操作步骤

### 步骤1：查看当前图标文件

当前`miniprogram/assets/icons/`目录下有8个SVG图标文件：
- home.svg（首页图标 - 未选中）
- home-active.svg（首页图标 - 选中）
- mold.svg（模库图标 - 未选中）
- mold-active.svg（模库图标 - 选中）
- tasks.svg（任务图标 - 未选中）
- tasks-active.svg（任务图标 - 选中）
- profile.svg（我的图标 - 未选中）
- profile-active.svg（我的图标 - 选中）

### 步骤2：将SVG转换为PNG

#### 方法1：使用HTML转换工具（推荐，最简单）

1. 在浏览器中打开 `miniprogram/svg-to-png.html`
2. 点击上传区域或拖拽SVG文件到上传区域
3. 系统会自动将SVG转换为81x81像素的PNG格式
4. 点击"下载"按钮保存PNG文件
5. 将PNG文件保存到`miniprogram/assets/icons/`目录

#### 方法2：使用在线转换工具

1. 访问在线转换网站：
   - https://convertio.co/zh/svg-png
   - https://cloudconvert.com/svg-to-png
   - https://www.aconvert.com/cn/image/svg-to-png

2. 上传SVG文件，设置转换参数：
   - 输出格式：PNG
   - 尺寸：81x81像素
   - 背景：透明

3. 下载转换后的PNG文件
4. 将PNG文件保存到`miniprogram/assets/icons/`目录

### 步骤3：恢复图标配置

当所有SVG文件都转换为PNG格式后，需要恢复`app.json`中的图标配置。

1. 打开`miniprogram/app.json`文件
2. 恢复所有`iconPath`和`selectedIconPath`配置
3. 使用转换后的PNG文件路径

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

### 步骤4：验证效果

1. 保存修改后的`app.json`文件
2. 重新启动微信小程序模拟器
3. 检查tabBar是否正常显示图标
4. 测试图标在选中/未选中状态下的颜色变化

## 预期效果

- 小程序能够正常启动
- tabBar显示四个标签：首页、模库、任务、我的
- 每个标签显示文字和图标
- 未选中状态显示灰色图标
- 选中状态显示主题色图标

## 图标设计说明

- 图标尺寸：81x81像素（微信小程序标准尺寸）
- 未选中状态：灰色（#6B7280）
- 选中状态：主题色（#6150BF）
- 背景透明
- 设计风格：简洁、现代、符合WEI设计原则

## 注意事项

1. 确保PNG文件名称与SVG文件名称一致（仅扩展名不同）
2. 确保PNG文件尺寸为81x81像素
3. 确保PNG文件背景透明
4. 保存文件时选择"PNG"格式
5. 转换后的PNG文件要保存到正确的目录

## 常见问题

### Q: 为什么小程序启动失败？
A: 因为app.json中配置了PNG图标路径，但实际上PNG文件不存在。移除图标配置或添加正确的PNG文件可以解决问题。

### Q: 为什么转换后的PNG图标显示不正确？
A: 可能是转换参数设置不正确。请确保转换时设置尺寸为81x81像素，背景透明。

### Q: 可以使用其他尺寸的图标吗？
A: 建议使用81x81像素，这是微信小程序推荐的标准尺寸。其他尺寸可能会导致图标显示失真。

### Q: 可以使用JPG格式的图标吗？
A: 可以，但建议使用PNG格式，因为PNG支持透明背景，显示效果更好。

## 相关文件

- `app.json`：小程序配置文件
- `miniprogram/svg-to-png.html`：SVG转PNG工具
- `miniprogram/assets/icons/`：图标文件目录
- `miniprogram/SVG_TO_PNG_GUIDE.md`：详细转换指南