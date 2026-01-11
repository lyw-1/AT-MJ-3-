<template>
  <div class="scheduling-center-container">
    <!-- 页面标题和操作区 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">调度中心</h1>
        <p class="page-subtitle">管理和调度生产任务，优化资源利用</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAddTask">
          <el-icon><Plus /></el-icon>
          新建任务
        </el-button>
        <el-button @click="handleExportPlan">
          <el-icon><Download /></el-icon>
          导出计划
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-dropdown trigger="click">
          <el-button>
            <el-icon><Setting /></el-icon>
            设置
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="toggleViewMode('gantt')">甘特图视图</el-dropdown-item>
              <el-dropdown-item @click="toggleViewMode('list')">列表视图</el-dropdown-item>
              <el-dropdown-item @click="handleConflictCheck">检查冲突</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 视图切换选项卡 -->
    <el-card class="canvas-card card-rounded-lg">
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="生产计划" name="plan">
          <!-- 生产计划甘特图 -->
          <div class="gantt-container">
            <h3>生产计划甘特图</h3>
            <div class="gantt-placeholder">
              <el-empty description="甘特图功能开发中，敬请期待">
                <el-button type="primary" disabled>功能开发中</el-button>
              </el-empty>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="任务列表" name="tasks">
          <!-- 任务列表 -->
          <div class="tasks-container">
            <div class="tasks-header">
              <h3>任务列表</h3>
              <el-input
                v-model="taskSearch"
                placeholder="搜索任务"
                clearable
                prefix-icon="Search"
                style="width: 300px"
              />
            </div>
            <el-table
              v-loading="loading"
              :data="filteredTasks"
              style="width: 100%"
              stripe
              border
            >
              <el-table-column prop="id" label="任务ID" width="80" sortable />
              <el-table-column prop="moldNumber" label="模具编号" width="120" sortable />
              <el-table-column prop="productName" label="产品名称" width="150" sortable />
              <el-table-column prop="currentProcessName" label="当前工序" width="150" sortable />
              <el-table-column prop="scheduledStartTime" label="计划开始时间" width="180" sortable />
              <el-table-column prop="scheduledEndTime" label="计划结束时间" width="180" sortable />
              <el-table-column prop="operatorName" label="操作员" width="120" sortable />
              <el-table-column prop="machineName" label="设备" width="150" sortable />
              <el-table-column prop="status" label="状态" width="100" sortable>
                <template #default="scope">
                  <el-tag :type="getStatusTagType(scope.row.status)" size="small">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="priority" label="优先级" width="100" sortable>
                <template #default="scope">
                  <el-tag :type="getPriorityTagType(scope.row.priority)" size="small">
                    {{ getPriorityText(scope.row.priority) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" fixed="right">
                <template #default="scope">
                  <el-button size="small" type="primary" text @click="handleViewTask(scope.row)">
                    详情
                  </el-button>
                  <el-button size="small" type="warning" text @click="handleEditTask(scope.row)">
                    编辑
                  </el-button>
                  <el-button size="small" type="danger" text @click="handleDeleteTask(scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="资源管理" name="resources">
          <!-- 资源管理 -->
          <div class="resources-container">
            <h3>资源管理</h3>
            <div class="resources-placeholder">
              <el-empty description="资源管理功能开发中，敬请期待">
                <el-button type="primary" disabled>功能开发中</el-button>
              </el-empty>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Plus, 
  Download, 
  Refresh, 
  Setting, 
  ArrowDown 
} from '@element-plus/icons-vue'
import { 
  getScheduleTasks, 
  deleteScheduleTask,
  type ScheduleTask,
  type ScheduleTaskQuery
} from '@/api/process'

// 页面加载状态
const loading = ref(false)

// 活动标签页
const activeTab = ref('plan')

// 视图模式
const viewMode = ref('gantt')

// 任务搜索
const taskSearch = ref('')

// 任务列表数据
const tasks = ref<ScheduleTask[]>([])

// 过滤后的任务列表
const filteredTasks = computed(() => {
  if (!taskSearch.value) {
    return tasks.value
  }
  return tasks.value.filter(task => 
    task.moldNumber.includes(taskSearch.value) ||
    task.productName.includes(taskSearch.value) ||
    task.currentProcessName.includes(taskSearch.value) ||
    task.operatorName?.includes(taskSearch.value) ||
    task.machineName?.includes(taskSearch.value)
  )
})

// 加载任务数据
const loadTasks = async () => {
  loading.value = true
  try {
    const params: ScheduleTaskQuery = {
      page: 1,
      pageSize: 100 // 加载足够多的数据用于调度
    }
    const response = await getScheduleTasks(params)
    tasks.value = response.data.records || []
    ElMessage.success('加载任务数据成功')
  } catch (error) {
    ElMessage.error('加载任务数据失败')
    console.error('加载任务数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 切换视图模式
const toggleViewMode = (mode: string) => {
  viewMode.value = mode
  ElMessage.info(`已切换到${mode === 'gantt' ? '甘特图' : '列表'}视图`)
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'pending': 'info',
    'in_progress': 'primary',
    'completed': 'success',
    'delayed': 'warning',
    'canceled': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    'pending': '待处理',
    'in_progress': '进行中',
    'completed': '已完成',
    'delayed': '已延迟',
    'canceled': '已取消'
  }
  return textMap[status] || status
}

// 获取优先级标签类型
const getPriorityTagType = (priority: string) => {
  const typeMap: Record<string, string> = {
    'low': 'success',
    'medium': 'warning',
    'high': 'danger'
  }
  return typeMap[priority] || 'info'
}

// 获取优先级文本
const getPriorityText = (priority: string) => {
  const textMap: Record<string, string> = {
    'low': '低',
    'medium': '中',
    'high': '高'
  }
  return textMap[priority] || priority
}

// 处理新建任务
const handleAddTask = () => {
  ElMessage.info('新建任务功能开发中')
}

// 处理导出计划
const handleExportPlan = () => {
  ElMessage.info('导出计划功能开发中')
}

// 处理刷新
const handleRefresh = () => {
  loadTasks()
}

// 处理检查冲突
const handleConflictCheck = () => {
  ElMessage.info('冲突检查功能开发中')
}

// 处理查看任务
const handleViewTask = (row: ScheduleTask) => {
  ElMessage.info('查看任务详情功能开发中')
}

// 处理编辑任务
const handleEditTask = (row: ScheduleTask) => {
  ElMessage.info('编辑任务功能开发中')
}

// 处理删除任务
const handleDeleteTask = async (row: ScheduleTask) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除任务 "${row.moldNumber}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await deleteScheduleTask(row.id)
    ElMessage.success('删除任务成功')
    loadTasks()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除任务失败')
    }
  }
}

// 初始化加载数据
loadTasks()
</script>

<style scoped lang="scss">
.scheduling-center-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
  flex-wrap: wrap;
}

.page-title-section {
  flex: 1;
  min-width: 200px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  color: #1f2937;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 4px 0 0 0;
}

.page-actions {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.card-rounded-lg {
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  margin-bottom: 20px;
  overflow: hidden;
}

.canvas-card {
  margin-bottom: 20px;
}

.gantt-container,
.tasks-container,
.resources-container {
  padding: 20px 0;
}

.gantt-container h3,
.tasks-container h3,
.resources-container h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.tasks-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

.tasks-header h3 {
  margin: 0;
}

.gantt-placeholder,
.resources-placeholder {
  padding: 40px 20px;
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
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
    justify-content: flex-start;
  }
  
  .tasks-header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>