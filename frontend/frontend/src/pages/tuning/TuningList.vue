<template>
  <div class="tuning-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">调模任务管理</h1>
        <p class="page-subtitle">管理和监控模具调试任务的执行情况</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增调模任务
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <el-card class="stats-card">
      <h3 class="stats-title">任务状态统计</h3>
      <div class="stats-container" v-loading="statsLoading">
        <div 
          v-for="item in categoryStats" 
          :key="item.category"
          class="stat-item" 
          :class="{ 'active': selectedCategory === item.category }"
          @click="handleCategoryClick(item.category)"
        >
          <div class="stat-label">{{ getStatusText(item.category) }}</div>
          <div class="stat-value">{{ item.count || 0 }}</div>
        </div>
      </div>
      <div v-if="!statsLoading && categoryStats.length === 0" class="no-stats">
        <el-empty description="暂无统计数据" />
      </div>
    </el-card>

    <!-- 数据表格区域 -->
    <el-card class="table-card">
      <h3 class="table-header-title">调模任务列表</h3>
      
      <el-table
        v-loading="loading"
        :data="tuningList"
        @selection-change="handleSelectionChange"
        stripe
        border
        :cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        :header-cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        
        <el-table-column prop="id" label="任务ID" width="100" sortable />
        <el-table-column prop="moldNumber" label="模具编号" width="150" show-overflow-tooltip />
        <el-table-column prop="productName" label="产品名称" width="150" show-overflow-tooltip />
        <el-table-column prop="currentProcess" label="当前工序" width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="120" sortable>
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100" sortable>
          <template #default="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scheduledStartTime" label="计划开始时间" width="180" sortable />
        <el-table-column prop="scheduledEndTime" label="计划结束时间" width="180" sortable />
        <el-table-column prop="actualStartTime" label="实际开始时间" width="180" sortable />
        <el-table-column prop="actualEndTime" label="实际结束时间" width="180" sortable />
        <el-table-column prop="operator" label="操作员" width="120" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleView(scope.row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button type="warning" link @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 任务详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="任务详情"
      width="60%"
    >
      <div v-if="selectedTask" class="task-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ selectedTask.id }}</el-descriptions-item>
          <el-descriptions-item label="模具编号">{{ selectedTask.moldNumber }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ selectedTask.productName }}</el-descriptions-item>
          <el-descriptions-item label="当前工序">{{ selectedTask.currentProcess }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ getStatusText(selectedTask.status) }}</el-descriptions-item>
          <el-descriptions-item label="优先级">{{ getPriorityText(selectedTask.priority) }}</el-descriptions-item>
          <el-descriptions-item label="计划开始时间">{{ selectedTask.scheduledStartTime }}</el-descriptions-item>
          <el-descriptions-item label="计划结束时间">{{ selectedTask.scheduledEndTime }}</el-descriptions-item>
          <el-descriptions-item label="实际开始时间">{{ selectedTask.actualStartTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="实际结束时间">{{ selectedTask.actualEndTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="操作员">{{ selectedTask.operator || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ selectedTask.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增/编辑任务弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="dialogTitle"
      width="60%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="模具编号" prop="moldNumber">
          <el-input v-model="formData.moldNumber" placeholder="请输入模具编号" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="当前工序" prop="currentProcess">
          <el-input v-model="formData.currentProcess" placeholder="请输入当前工序" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="formData.priority" placeholder="请选择优先级">
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划开始时间" prop="scheduledStartTime">
          <el-input v-model="formData.scheduledStartTime" type="datetime-local" />
        </el-form-item>
        <el-form-item label="计划结束时间" prop="scheduledEndTime">
          <el-input v-model="formData.scheduledEndTime" type="datetime-local" />
        </el-form-item>
        <el-form-item label="操作员">
          <el-input v-model="formData.operator" placeholder="请输入操作员" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'

// 调模任务类型定义
interface TuningTask {
  id: number
  moldNumber: string
  productName: string
  currentProcess: string
  status: string
  priority: string
  scheduledStartTime: string
  scheduledEndTime: string
  actualStartTime?: string
  actualEndTime?: string
  operator?: string
  remark?: string
  createTime?: string
  updateTime?: string
}

// 加载状态
const loading = ref(false)
const statsLoading = ref(false)

// 任务列表数据
const tuningList = ref<TuningTask[]>([])

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 分类统计数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])
const selectedCategory = ref<string | null>(null)

// 弹窗相关数据
const detailDialogVisible = ref(false)
const editDialogVisible = ref(false)
const selectedTask = ref<TuningTask | null>(null)
const dialogTitle = ref('新增调模任务')
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<TuningTask>({
  id: 0,
  moldNumber: '',
  productName: '',
  currentProcess: '',
  status: 'pending',
  priority: 'medium',
  scheduledStartTime: '',
  scheduledEndTime: '',
  operator: '',
  remark: ''
})

// 表单验证规则
const formRules = {
  moldNumber: [{ required: true, message: '请输入模具编号', trigger: 'blur' }],
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  currentProcess: [{ required: true, message: '请输入当前工序', trigger: 'blur' }],
  scheduledStartTime: [{ required: true, message: '请选择计划开始时间', trigger: 'blur' }],
  scheduledEndTime: [{ required: true, message: '请选择计划结束时间', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
}

// 模拟API函数 - 实际项目中替换为真实API
const mockGetTuningTasks = async (params: any) => {
  // 模拟延迟
  await new Promise(resolve => setTimeout(resolve, 500))
  
  // 模拟数据
  const mockData = [
    {
      id: 1,
      moldNumber: 'M001',
      productName: '产品A',
      currentProcess: '注塑',
      status: 'pending',
      priority: 'high',
      scheduledStartTime: '2025-12-26 09:00:00',
      scheduledEndTime: '2025-12-26 12:00:00',
      operator: '张三',
      remark: '首次调试'
    },
    {
      id: 2,
      moldNumber: 'M002',
      productName: '产品B',
      currentProcess: '挤出',
      status: 'in_progress',
      priority: 'medium',
      scheduledStartTime: '2025-12-26 13:00:00',
      scheduledEndTime: '2025-12-26 15:00:00',
      actualStartTime: '2025-12-26 13:15:00',
      operator: '李四',
      remark: '调整参数'
    },
    {
      id: 3,
      moldNumber: 'M003',
      productName: '产品C',
      currentProcess: '吹塑',
      status: 'completed',
      priority: 'low',
      scheduledStartTime: '2025-12-25 09:00:00',
      scheduledEndTime: '2025-12-25 11:00:00',
      actualStartTime: '2025-12-25 09:00:00',
      actualEndTime: '2025-12-25 10:30:00',
      operator: '王五',
      remark: '调试完成'
    }
  ]
  
  // 应用状态筛选
  const filteredData = params.status 
    ? mockData.filter(item => item.status === params.status)
    : mockData
  
  return {
    code: 200,
    data: {
      records: filteredData,
      total: filteredData.length
    }
  }
}

// 计算分类统计数据
const calculateCategoryStats = (data: TuningTask[]) => {
  const statsMap = new Map<string, number>()
  
  data.forEach(item => {
    const category = item.status || 'unknown'
    const count = statsMap.get(category) || 0
    statsMap.set(category, count + 1)
  })
  
  return Array.from(statsMap.entries())
    .map(([category, count]) => ({ category, count }))
    .sort((a, b) => b.count - a.count)
}

// 获取分类统计数据
const fetchCategoryStats = async () => {
  statsLoading.value = true
  try {
    const response = await mockGetTuningTasks({ page: 1, pageSize: 1000 })
    categoryStats.value = calculateCategoryStats(response.data.records)
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 获取调模任务列表
const fetchTuningTasks = async () => {
  loading.value = true
  try {
    const response = await mockGetTuningTasks({
      page: pagination.currentPage,
      pageSize: pagination.pageSize,
      ...(selectedCategory.value && { status: selectedCategory.value })
    })
    
    tuningList.value = response.data.records
    pagination.total = response.data.total
    
    ElMessage.success('加载任务列表成功')
  } catch (error) {
    console.error('获取调模任务列表失败:', error)
    ElMessage.error('获取调模任务列表失败')
    tuningList.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 处理类别点击筛选
const handleCategoryClick = (category: string) => {
  selectedCategory.value = selectedCategory.value === category ? null : category
  pagination.currentPage = 1
  fetchTuningTasks()
}

// 获取状态文本
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': '待处理',
    'in_progress': '进行中',
    'completed': '已完成',
    'delayed': '已延迟',
    'canceled': '已取消',
    'unknown': '未知状态'
  }
  return statusMap[status] || status
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'pending': 'info',
    'in_progress': 'primary',
    'completed': 'success',
    'delayed': 'warning',
    'canceled': 'danger',
    'unknown': 'info'
  }
  return typeMap[status] || 'info'
}

// 获取优先级文本
const getPriorityText = (priority: string) => {
  const priorityMap: Record<string, string> = {
    'low': '低',
    'medium': '中',
    'high': '高'
  }
  return priorityMap[priority] || priority
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

// 处理新增任务
const handleAdd = () => {
  dialogTitle.value = '新增调模任务'
  // 重置表单数据
  Object.assign(formData, {
    id: 0,
    moldNumber: '',
    productName: '',
    currentProcess: '',
    status: 'pending',
    priority: 'medium',
    scheduledStartTime: '',
    scheduledEndTime: '',
    operator: '',
    remark: ''
  })
  editDialogVisible.value = true
}

// 处理编辑任务
const handleEdit = (row: TuningTask) => {
  dialogTitle.value = '编辑调模任务'
  // 填充表单数据
  Object.assign(formData, { ...row })
  editDialogVisible.value = true
}

// 处理查看详情
const handleView = (row: TuningTask) => {
  selectedTask.value = { ...row }
  detailDialogVisible.value = true
}

// 处理删除任务
const handleDelete = async (row: TuningTask) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除调模任务 ${row.moldNumber} 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    ElMessage.success('删除成功')
    fetchTuningTasks()
    fetchCategoryStats()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除任务失败:', error)
      ElMessage.error('删除任务失败')
    }
  }
}

// 处理表单提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // 模拟提交操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('保存成功')
    editDialogVisible.value = false
    fetchTuningTasks()
    fetchCategoryStats()
  } catch (error) {
    console.error('表单提交失败:', error)
    ElMessage.error('表单提交失败')
  }
}

// 处理刷新
const handleRefresh = async () => {
  await Promise.all([
    fetchCategoryStats(),
    fetchTuningTasks()
  ])
}

// 分页相关方法
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchTuningTasks()
}

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchTuningTasks()
}

// 选择变化
const handleSelectionChange = (selection: TuningTask[]) => {
  // 处理选择变化
  console.log('选择变化:', selection)
}

// 页面挂载时获取数据
onMounted(async () => {
  await Promise.all([
    fetchCategoryStats(),
    fetchTuningTasks()
  ])
})
</script>

<style scoped lang="scss">
.tuning-container {
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

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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
