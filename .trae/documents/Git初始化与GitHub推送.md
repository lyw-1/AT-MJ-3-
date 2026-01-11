## Git初始化与GitHub推送计划

### 目标
将项目代码提交到GitHub仓库 `https://github.com/lyw-1/AT-MJ-3`

### 步骤
1. **初始化Git仓库**
   - 在项目根目录执行 `"D:\Program Files\Git\cmd\git.exe" init`
   - 创建 `.gitignore` 文件，添加不必要的文件（如node_modules、日志文件等）

2. **配置Git用户信息**
   - 设置用户名：`"D:\Program Files\Git\cmd\git.exe" config user.name "xiaoluoke86"`
   - 设置邮箱：`"D:\Program Files\Git\cmd\git.exe" config user.email "xiaoluoke86@qq.com"`

3. **添加文件到暂存区**
   - 执行 `"D:\Program Files\Git\cmd\git.exe" add .` 添加所有文件

4. **提交文件**
   - 执行 `"D:\Program Files\Git\cmd\git.exe" commit -m "Initial commit"` 提交文件

5. **连接到GitHub仓库**
   - 添加远程仓库：`"D:\Program Files\Git\cmd\git.exe" remote add origin https://github.com/lyw-1/AT-MJ-3`

6. **推送代码到GitHub**
   - 执行 `"D:\Program Files\Git\cmd\git.exe" push -u origin main` 推送代码
   - 当需要验证时，使用提供的GitHub账号：`xiaoluoke86@qq.com` 和密码：`Luoman86`

### 注意事项
- 使用完整的Git路径：`D:\Program Files\Git\cmd\git.exe`
- 确保 `.gitignore` 文件包含所有不必要的文件和目录
- 推送时需要验证GitHub账号密码

### 预期结果
- 项目成功初始化Git仓库
- 代码成功推送到GitHub仓库 `https://github.com/lyw-1/AT-MJ-3`