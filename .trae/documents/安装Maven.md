## Maven安装计划

### 1. 环境检查
- Maven安装包已存在：`d:\trae\AT-MJ-3\apache-maven-3.9.12`
- JDK已存在：`d:\trae\AT-MJ-3\jdk-21.0.9`
- 目前Java和Maven的环境变量都未配置

### 2. 安装步骤

#### 步骤1：创建Maven安装目录
1. 创建目录：`D:\Program Files\Apache\Maven`
2. 将`d:\trae\AT-MJ-3\apache-maven-3.9.12`下的所有文件复制到`D:\Program Files\Apache\Maven`

#### 步骤2：配置Java环境变量
1. 设置`JAVA_HOME`环境变量为`d:\trae\AT-MJ-3\jdk-21.0.9`
2. 将`%JAVA_HOME%\bin`添加到`PATH`环境变量
3. 验证Java安装：运行`java -version`

#### 步骤3：配置Maven环境变量
1. 设置`MAVEN_HOME`环境变量为`D:\Program Files\Apache\Maven`
2. 将`%MAVEN_HOME%\bin`添加到`PATH`环境变量
3. 验证Maven安装：运行`mvn --version`

#### 步骤4：配置Maven settings.xml（可选）
1. 编辑`D:\Program Files\Apache\Maven\conf\settings.xml`
2. 配置本地仓库路径
3. 配置镜像源（如阿里云镜像）

### 3. 验证步骤
- 运行`java -version`确认Java已安装
- 运行`mvn --version`确认Maven已安装

### 4. 注意事项
- 环境变量配置需要管理员权限
- 配置后需要重新打开命令行窗口才能生效
- Maven需要Java 1.8或更高版本，JDK 21.0.9完全满足要求