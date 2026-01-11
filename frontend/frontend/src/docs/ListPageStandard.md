# 列表页面标准规范

## 1. 页面结构

### 1.1 整体布局
```html
<div class="[page-name]-container">
  <!-- 页面头部 -->
  <div class="page-header">
    <div class="page-title-section">
      <h1 class="page-title">页面标题</h1>
      <p class="page-subtitle">页面描述</p>
    </div>
    <div class="page-actions">
      <!-- 操作按钮 -->
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增
      </el-button>
      <!-- 其他操作按钮：导入、导出等 -->
    </div>
  </div>

  <!-- 统计卡片区域 -->
  <el-card class="stats-card">
    <h3 class="stats-title">统计标题</h3>
    <div class="stats-container" v-loading="statsLoading">
      <div 
        v-for="item in categoryStats" 
        :key="item.category"
        class="stat-item" 
        :class="{ 'active': selectedCategory === item.category }"
        @click="handleCategoryClick(item.category)"
      >
        <div class="stat-label">{{ item.category || '未知类别' }}</div>
        <div class="stat-value">{{ item.count || 0 }}</div>
      </div>
    </div>
    <div v-if="!statsLoading && categoryStats.length === 0" class="no-stats">
      <el-empty description="暂无统计数据" />
    </div>
  </el-card>

  <!-- 数据表格区域 -->
  <el-card class="table-card">
    <h3 class="table-header-title">表格标题</h3>
    <div class="table-actions">
      <!-- 表格操作按钮：批量删除等 -->
      <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>
    
    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="listData"
      @selection-change="handleSelectionChange"
      stripe
      border
    >
      <!-- 表格列配置 -->
      <el-table-column type="selection" width="80" />
      <!-- 其他表格列 -->
    </el-table>
    
    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </el-card>

  <!-- 新增/编辑对话框 -->
  <el-dialog>
    <!-- 表单内容 -->
  </el-dialog>

  <!-- 导入对话框（可选） -->
  <el-dialog>
    <!-- 导入内容 -->
  </el-dialog>
</div>
```

## 2. 数据结构

### 2.1 基础数据
```typescript
// 加载状态
const loading = ref(false) // 表格加载状态
const statsLoading = ref(false) // 统计数据加载状态

// 列表数据
const listData = ref<T[]>([]) // 替换T为实际数据类型

// 选中项ID
const selectedIds = ref<number[]>([]) // 用于批量操作

// 分类统计数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])

// 当前选中的类别（用于筛选）
const selectedCategory = ref<string | null>(null)

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
```

### 2.2 对话框数据
```typescript
// 对话框状态
const dialogVisible = ref(false)
const dialogTitle = ref('新增')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<T>({ /* 表单字段 */ })
const formRules = reactive<FormRules>({ /* 表单验证规则 */ })
```

## 3. 方法命名规范

### 3.1 数据获取方法
```typescript
// 获取列表数据
const fetchList = async () => {
  loading.value = true
  try {
    const response = await apiMethod({
      page: pagination.currentPage,
      size: pagination.pageSize,
      category: selectedCategory.value || undefined
    })
    listData.value = response.records || []
    pagination.total = response.total || 0
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
    listData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 获取分类统计数据
const fetchCategoryStats = async () => {
  statsLoading.value = true
  try {
    // 获取足够多的数据用于统计
    const response = await apiMethod({ page: 1, size: 1000 })
    categoryStats.value = calculateCategoryStats(response.records || [])
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 计算分类统计数据
const calculateCategoryStats = (data: T[]) => {
  const statsMap = new Map<string, number>()
  
  // 遍历数据，按类别统计数量
  data.forEach(item => {
    const category = item.category || '未分类' // 替换category为实际字段名
    const count = statsMap.get(category) || 0
    statsMap.set(category, count + 1)
  })
  
  // 转换为数组并排序
  const statsArray = Array.from(statsMap.entries())
    .map(([category, count]) => ({ category, count }))
    .sort((a, b) => b.count - a.count)
  
  return statsArray
}
```

### 3.2 事件处理方法
```typescript
// 处理分类点击（筛选）
const handleCategoryClick = (category: string) => {
  if (selectedCategory.value === category) {
    selectedCategory.value = null // 取消选择
  } else {
    selectedCategory.value = category // 选择分类
  }
  
  pagination.currentPage = 1 // 重置页码
  fetchList() // 重新获取数据
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增'
  // 重置表单数据
  Object.assign(formData, { /* 初始值 */ })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: T) => {
  dialogTitle.value = '编辑'
  // 填充表单数据
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  // 表单验证和提交逻辑
}

// 删除
const handleDelete = async (row: T) => {
  // 删除确认和执行逻辑
}

// 批量删除
const handleBatchDelete = async () => {
  // 批量删除确认和执行逻辑
}

// 导出数据
const handleExport = async () => {
  // 导出逻辑
}

// 导入数据
const handleImport = () => {
  // 打开导入对话框
}

// 选择变化
const handleSelectionChange = (selection: T[]) => {
  selectedIds.value = selection.map(item => item.id!).filter(Boolean)
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchList()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchList()
}
```

### 3.3 生命周期
```typescript
// 页面挂载时并行获取数据
onMounted(async () => {
  await Promise.all([
    fetchCategoryStats(),
    fetchList()
  ])
})
```

## 4. 样式规范

### 4.1 基础样式
```scss
.[page-name]-container {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title-section {
  flex: 1;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.page-actions {
  display: flex;
  gap: 12px;
}
```

### 4.2 统计卡片样式
```scss
.stats-card {
  margin-bottom: 20px;
}

.stats-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #333;
}

.stats-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

.stat-item {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  flex: 1;
  min-width: 150px;
  max-width: calc(25% - 16px);
  border: 2px solid transparent;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stat-item.active {
  background-color: #337ecc;
  color: white;
  box-shadow: 0 4px 12px rgba(51, 126, 204, 0.4);
  border: 2px solid #2a6baf;
}

.stat-item.active .stat-label,
.stat-item.active .stat-value {
  color: white;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  transition: color 0.3s ease;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #409eff;
  transition: color 0.3s ease;
}

.no-stats {
  padding: 40px 0;
  text-align: center;
}
```

### 4.3 表格样式
```scss
.table-card {
  margin-bottom: 20px;
}

.table-header-title {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.table-actions {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

// 表格单元格对齐
:deep(.el-table__cell) {
  text-align: left;
  vertical-align: middle;
}

:deep(.el-table__header-wrapper .el-table__header-cell) {
  text-align: left;
  vertical-align: middle;
}
```

### 4.4 响应式设计
```scss
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .page-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  // 统计卡片在移动端自适应
  .stat-item {
    max-width: calc(50% - 16px);
  }
}

@media (max-width: 480px) {
  .stat-item {
    max-width: 100%;
  }
}
```

## 5. 最佳实践

### 5.1 性能优化
- 并行获取统计数据和列表数据
- 合理设置统计数据获取的size（避免获取过多数据）
- 使用v-loading显示加载状态
- 分页加载数据

### 5.2 错误处理
- 捕获API错误并显示友好提示
- 加载失败时重置数据
- 提供重试机制（可选）

### 5.3 用户体验
- 统计卡片点击反馈
- 表单验证实时反馈
- 操作成功/失败提示
- 批量操作确认
- 导入导出功能

### 5.4 代码组织
- 按功能模块组织代码
- 合理使用注释
- 遵循命名规范
- 保持代码简洁

## 6. 示例页面

- [成品列表](https://github.com/example/project/blob/main/src/pages/system/Products.vue)

## 7. 迁移指南

### 7.1 现有页面改造步骤
1. 分析现有页面结构和功能
2. 按照标准规范调整HTML结构
3. 统一数据结构命名
4. 调整方法命名和实现
5. 应用标准样式
6. 测试功能完整性
7. 优化用户体验

### 7.2 注意事项
- 保留原有功能不变
- 确保数据兼容
- 考虑响应式设计
- 保持代码可维护性
- 测试所有功能路径

## 8. 后续维护

- 定期更新规范
- 审核新页面是否符合规范
- 提供培训和支持
- 收集反馈并持续改进
