<template>
  <div class="task-list-container">
    <h2>模具任务（临时占位）</h2>
    <p>为修复模板解析错误，页面已用临时模板替换。后续会恢复完整功能和表格。</p>
    <el-button type="primary" @click="handleRefresh">刷新</el-button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { type ScheduleTask } from '@/api/process'

// 加载状态
const loading = ref(false)
const statsLoading = ref(false)

// 调度任务数据
const scheduleTasks = ref<ScheduleTask[]>([])

// 筛选条件
const filterState = ref({})

// 选中的类别
const selectedCategory = ref<string | null>(null)

// 分类统计数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])

// 导入API函数
import {
  getScheduleTasks,
  type ScheduleTaskQuery
} from '@/api/process'

// 计算分类统计数据
const calculateCategoryStats = (tasks: ScheduleTask[]) => {
  const statsMap = new Map<string, number>()
  
  // 遍历所有任务，按状态统计数量
  tasks.forEach(item => {
    const category = item.status || 'unknown'
    statsMap.set(category, (statsMap.get(category) || 0) + 1)
  })
  
  // 转换为数组格式
  const statsArray = Array.from(statsMap.entries()).map(([category, count]) => ({
    category,
    count
  }))
  
  // 按数量降序排序
  statsArray.sort((a, b) => b.count - a.count)
  
  return statsArray
}

// 获取分类统计数据
const fetchCategoryStats = async () => {
  statsLoading.value = true
  try {
    // 构造请求参数
    const params: ScheduleTaskQuery = {
      page: 1,
      pageSize: 1000, // 获取足够多的数据来统计
      ...filterState.value
    }
    
    // 调用后端API获取任务数据
    const response = await getScheduleTasks(params)
    
    // 计算分类统计数据
    categoryStats.value = calculateCategoryStats(response.records || [])
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 加载调度任务数据
const loadScheduleTasks = async () => {
  loading.value = true
  try {
    // 构造请求参数
    const params: ScheduleTaskQuery = {
      page: 1,
      pageSize: 20,
      // 添加筛选条件
      ...filterState.value,
      // 添加状态筛选
      ...(selectedCategory.value && { status: selectedCategory.value })
    }
    
    // 调用后端API获取任务数据
    const response = await getScheduleTasks(params)
    
    // 更新任务列表
    scheduleTasks.value = response.records || []
    
    ElMessage.success('加载调度任务成功')
  } catch (error) {
    ElMessage.error('加载调度任务失败')
    console.error('加载调度任务失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理刷新
const handleRefresh = async () => {
  // 并行获取分类统计数据和任务列表
  await Promise.all([
    fetchCategoryStats(),
    loadScheduleTasks()
  ])
}

// 初始化加载数据
onMounted(async () => {
  // 并行获取分类统计数据和任务列表
  await Promise.all([
    fetchCategoryStats(),
    loadScheduleTasks()
  ])
})
</script>

<style scoped lang="scss">
.task-list-container {
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

.tab-content {
  padding: 20px 0;
}

.card-rounded-lg {
  border-radius: 15px;
}

.canvas-card {
  margin-bottom: 20px;
}

/* 统计卡片样式 */
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

/* 表格样式 */
.table-card {
  margin-bottom: 20px;
}

.table-header-title {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.table-wrapper {
  margin-bottom: 20px;
  overflow: auto;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 任务详情、编辑、创建样式 */
.task-detail,
.task-edit,
.task-create {
  padding: 20px 0;
}

/* 表格单元格对齐 */
:deep(.el-table__cell) {
  text-align: left;
  vertical-align: middle;
}

:deep(.el-table__header-wrapper .el-table__header-cell) {
  text-align: left;
  vertical-align: middle;
}

/* 选择列居中 */
:deep(.el-table__column--selection .el-table__cell) {
  text-align: center;
}

:deep(.el-table__header-wrapper .el-table__column--selection .el-table__header-cell) {
  text-align: center;
}

/* 响应式设计 */
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
  
  /* 统计卡片在移动端自适应 */
  .stat-item {
    max-width: calc(50% - 16px);
  }
}

@media (max-width: 480px) {
  .stat-item {
    max-width: 100%;
  }
}
</style>