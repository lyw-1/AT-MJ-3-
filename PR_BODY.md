## Pull Request Description

### 变更内容
- 新增 GlobalExceptionHandler 统一处理所有 API 错误响应
- 支持 15+ 种异常类型的结构化处理
- 开发环境显示完整堆栈，生产环境隐藏敏感信息

### 新增文件
- backend/src/main/java/com/mold/digitalization/exception/GlobalExceptionHandler.java (167 lines)

### 相关 Issue
- 解决前端 500 错误问题，提供统一的错误响应结构
