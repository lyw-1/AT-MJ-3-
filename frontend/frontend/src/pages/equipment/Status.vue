<template>
  <div class="equipment-status-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">设备状态</h1>
        <p class="page-subtitle">实时监控设备运行状态和性能指标</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增设备
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新状态
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出报告
        </el-button>
      </div>
    </div>

    <!-- 设备状态统计卡片 -->
    <div class="status-cards">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="status-card running">
            <div class="card-icon">
              <el-icon size="32" color="#67c23a"><CircleCheck /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statusCounts.running }}</div>
              <div class="card-label">运行中</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="status-card idle">
            <div class="card-icon">
              <el-icon size="32" color="#e6a23c"><Clock /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statusCounts.idle }}</div>
              <div class="card-label">待机中</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="status-card maintenance">
            <div class="card-icon">
              <el-icon size="32" color="#409eff"><Tools /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statusCounts.maintenance }}</div>
              <div class="card-label">维修中</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="status-card offline">
            <div class="card-icon">
              <el-icon size="32" color="#f56c6c"><CircleClose /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statusCounts.offline }}</div>
              <div class="card-label">离线</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="设备编号">
          <el-input v-model="filterForm.equipmentNumber" placeholder="请输入设备编号" clearable />
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="filterForm.equipmentName" placeholder="请输入设备名称" clearable />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="filterForm.equipmentType" placeholder="请选择类型" clearable>
            <el-option label="挤出机" value="挤出机" />
            <el-option label="压机" value="压机" />
            <el-option label="混料机" value="混料机" />
            <el-option label="干燥机" value="干燥机" />
            <el-option label="检测设备" value="检测设备" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="运行状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="运行中" value="运行中" />
            <el-option label="待机中" value="待机中" />
            <el-option label="维修中" value="维修中" />
            <el-option label="离线" value="离线" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属车间">
          <el-select v-model="filterForm.workshop" placeholder="请选择车间" clearable>
            <el-option label="一车间" value="一车间" />
            <el-option label="二车间" value="二车间" />
            <el-option label="三车间" value="三车间" />
            <el-option label="检测中心" value="检测中心" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 设备列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>设备列表</h3>
        <div class="table-actions">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchStop">
            <el-icon><VideoPause /></el-icon>
            批量停机
          </el-button>
          <el-button type="success" :disabled="selectedIds.length === 0" @click="handleBatchStart">
            <el-icon><VideoPlay /></el-icon>
            批量启动
          </el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="equipmentList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="equipmentNumber" label="设备编号" width="120" fixed />
        <el-table-column prop="equipmentName" label="设备名称" width="150" show-overflow-tooltip />
        <el-table-column prop="equipmentType" label="设备类型" width="100" align="center" />
        <el-table-column prop="workshop" label="所属车间" width="100" align="center" />
        <el-table-column prop="status" label="运行状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="dark">
              <el-icon size="12" style="margin-right: 4px">
                <CircleCheck v-if="row.status === '运行中'" />
                <Clock v-else-if="row.status === '待机中'" />
                <Tools v-else-if="row.status === '维修中'" />
                <CircleClose v-else />
              </el-icon>
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentTask" label="当前任务" width="120" show-overflow-tooltip />
        <el-table-column prop="operator" label="操作员" width="100" align="center" />
        <el-table-column prop="runtime" label="运行时长" width="100" align="center">
          <template #default="{ row }">
            {{ formatRuntime(row.runtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="efficiency" label="效率" width="80" align="center">
          <template #default="{ row }">
            <span :class="getEfficiencyClass(row.efficiency)">{{ row.efficiency }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="温度(°C)" width="100" align="center">
          <template #default="{ row }">
            <span :class="getTemperatureClass(row.temperature)">{{ row.temperature }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pressure" label="压力(MPa)" width="100" align="center" />
        <el-table-column prop="power" label="功率(kW)" width="100" align="center" />
        <el-table-column prop="lastMaintenance" label="上次保养" width="120" />
        <el-table-column prop="nextMaintenance" label="下次保养" width="120" />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button 
              :type="row.status === '运行中' ? 'danger' : 'success'" 
              link 
              @click="handleToggleStatus(row)"
            >
              <el-icon><VideoPause v-if="row.status === '运行中'" /><VideoPlay v-else /></el-icon>
              {{ row.status === '运行中' ? '停机' : '启动' }}
            </el-button>
            <el-button type="primary" link @click="handleView(row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button type="warning" link @click="handleMaintenance(row)">
              <el-icon><Tools /></el-icon>
              保养
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
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

    <!-- 设备详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="设备详情"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentEquipment" class="equipment-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备编号">{{ currentEquipment.equipmentNumber }}</el-descriptions-item>
          <el-descriptions-item label="设备名称">{{ currentEquipment.equipmentName }}</el-descriptions-item>
          <el-descriptions-item label="设备类型">{{ currentEquipment.equipmentType }}</el-descriptions-item>
          <el-descriptions-item label="所属车间">{{ currentEquipment.workshop }}</el-descriptions-item>
          <el-descriptions-item label="运行状态">
            <el-tag :type="getStatusType(currentEquipment.status)">{{ currentEquipment.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="当前任务">{{ currentEquipment.currentTask || '无' }}</el-descriptions-item>
          <el-descriptions-item label="操作员">{{ currentEquipment.operator || '无' }}</el-descriptions-item>
          <el-descriptions-item label="运行时长">{{ formatRuntime(currentEquipment.runtime) }}</el-descriptions-item>
          <el-descriptions-item label="效率">{{ currentEquipment.efficiency }}%</el-descriptions-item>
          <el-descriptions-item label="温度">{{ currentEquipment.temperature }}°C</el-descriptions-item>
          <el-descriptions-item label="压力">{{ currentEquipment.pressure }} MPa</el-descriptions-item>
          <el-descriptions-item label="功率">{{ currentEquipment.power }} kW</el-descriptions-item>
          <el-descriptions-item label="上次保养">{{ currentEquipment.lastMaintenance }}</el-descriptions-item>
          <el-descriptions-item label="下次保养">{{ currentEquipment.nextMaintenance }}</el-descriptions-item>
          <el-descriptions-item label="生产厂家">{{ currentEquipment.manufacturer }}</el-descriptions-item>
          <el-descriptions-item label="购买日期">{{ currentEquipment.purchaseDate }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="chart-container">
          <h4>运行趋势</h4>
          <div class="chart-placeholder">
            <el-icon size="48" color="#909399"><TrendCharts /></el-icon>
            <p>设备运行数据图表</p>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Search,
  Refresh,
  View,
  Tools,
  Download,
  VideoPlay,
  VideoPause,
  CircleCheck,
  CircleClose,
  Clock,
  TrendCharts
} from '@element-plus/icons-vue'

interface EquipmentItem {
  id: number
  equipmentNumber: string
  equipmentName: string
  equipmentType: string
  workshop: string
  status: string
  currentTask?: string
  operator?: string
  runtime: number
  efficiency: number
  temperature: number
  pressure: number
  power: number
  lastMaintenance: string
  nextMaintenance: string
  manufacturer: string
  purchaseDate: string
}

interface FilterForm {
  equipmentNumber: string
  equipmentName: string
  equipmentType: string
  status: string
  workshop: string
}

interface StatusCounts {
  running: number
  idle: number
  maintenance: number
  offline: number
}

// 响应式数据
const loading = ref(false)
const selectedIds = ref<number[]>([])
const detailDialogVisible = ref(false)
const currentEquipment = ref<EquipmentItem | null>(null)

const filterForm = reactive<FilterForm>({
  equipmentNumber: '',
  equipmentName: '',
  equipmentType: '',
  status: '',
  workshop: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const statusCounts = reactive<StatusCounts>({
  running: 8,
  idle: 3,
  maintenance: 2,
  offline: 1
})

// 模拟数据
const equipmentList = ref<EquipmentItem[]>([
  {
    id: 1,
    equipmentNumber: 'E001',
    equipmentName: '主挤出机',
    equipmentType: '挤出机',
    workshop: '一车间',
    status: '运行中',
    currentTask: '蜂窝陶瓷载体生产',
    operator: '张三',
    runtime: 3600,
    efficiency: 92,
    temperature: 185,
    pressure: 15.2,
    power: 75.5,
    lastMaintenance: '2024-10-15',
    nextMaintenance: '2024-12-15',
    manufacturer: '德国科倍隆',
    purchaseDate: '2022-03-10'
  },
  {
    id: 2,
    equipmentNumber: 'E002',
    equipmentName: '辅助挤出机',
    equipmentType: '挤出机',
    workshop: '一车间',
    status: '待机中',
    runtime: 1800,
    efficiency: 0,
    temperature: 25,
    pressure: 0,
    power: 0,
    lastMaintenance: '2024-11-01',
    nextMaintenance: '2025-01-01',
    manufacturer: '德国科倍隆',
    purchaseDate: '2022-03-10'
  },
  {
    id: 3,
    equipmentNumber: 'P001',
    equipmentName: '液压机',
    equipmentType: '压机',
    workshop: '二车间',
    status: '运行中',
    currentTask: 'DPF载体压制',
    operator: '李四',
    runtime: 7200,
    efficiency: 88,
    temperature: 45,
    pressure: 25.8,
    power: 45.2,
    lastMaintenance: '2024-11-10',
    nextMaintenance: '2025-02-10',
    manufacturer: '日本住友',
    purchaseDate: '2021-08-20'
  },
  {
    id: 4,
    equipmentNumber: 'M001',
    equipmentName: '混料机',
    equipmentType: '混料机',
    workshop: '一车间',
    status: '维修中',
    runtime: 0,
    efficiency: 0,
    temperature: 0,
    pressure: 0,
    power: 0,
    lastMaintenance: '2024-11-20',
    nextMaintenance: '2024-11-25',
    manufacturer: '国产',
    purchaseDate: '2020-05-15'
  },
  {
    id: 5,
    equipmentNumber: 'D001',
    equipmentName: '干燥设备',
    equipmentType: '干燥机',
    workshop: '三车间',
    status: '运行中',
    currentTask: '产品干燥',
    operator: '王五',
    runtime: 5400,
    efficiency: 85,
    temperature: 120,
    pressure: 8.5,
    power: 32.1,
    lastMaintenance: '2024-10-30',
    nextMaintenance: '2025-01-30',
    manufacturer: '意大利',
    purchaseDate: '2021-12-01'
  },
  {
    id: 6,
    equipmentNumber: 'T001',
    equipmentName: '密度检测仪',
    equipmentType: '检测设备',
    workshop: '检测中心',
    status: '离线',
    runtime: 0,
    efficiency: 0,
    temperature: 0,
    pressure: 0,
    power: 0,
    lastMaintenance: '2024-09-15',
    nextMaintenance: '2024-12-15',
    manufacturer: '德国',
    purchaseDate: '2019-06-10'
  }
])

// 方法
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '运行中': 'success',
    '待机中': 'info',
    '维修中': 'warning',
    '离线': 'danger'
  }
  return statusMap[status] || 'info'
}

const getEfficiencyClass = (efficiency: number) => {
  if (efficiency >= 90) return 'text-success'
  if (efficiency >= 70) return 'text-warning'
  return 'text-danger'
}

const getTemperatureClass = (temperature: number) => {
  if (temperature >= 200) return 'text-danger'
  if (temperature >= 150) return 'text-warning'
  return 'text-success'
}

const formatRuntime = (seconds: number) => {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  if (hours > 0) {
    return `${hours}小时${minutes}分钟`
  }
  return `${minutes}分钟`
}

const handleSearch = () => {
  loading.value = true
  // 模拟搜索逻辑
  setTimeout(() => {
    loading.value = false
    ElMessage.success('搜索完成')
  }, 500)
}

const handleReset = () => {
  Object.assign(filterForm, {
    equipmentNumber: '',
    equipmentName: '',
    equipmentType: '',
    status: '',
    workshop: ''
  })
  handleSearch()
}

const handleRefresh = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('状态已刷新')
  }, 1000)
}

const handleSelectionChange = (selection: EquipmentItem[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleAdd = () => {
  ElMessage.info('新增设备功能')
}

const handleToggleStatus = async (row: EquipmentItem) => {
  const action = row.status === '运行中' ? '停机' : '启动'
  try {
    await ElMessageBox.confirm(
      `确定要${action}设备 "${row.equipmentName}" 吗？`,
      `${action}确认`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success(`${action}成功`)
  } catch {
    // 用户取消操作
  }
}

const handleView = (row: EquipmentItem) => {
  currentEquipment.value = row
  detailDialogVisible.value = true
}

const handleMaintenance = (row: EquipmentItem) => {
  ElMessage.info(`保养设备: ${row.equipmentName}`)
}

const handleBatchStart = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要启动的设备')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要启动选中的 ${selectedIds.value.length} 个设备吗？`,
      '批量启动确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('批量启动成功')
  } catch {
    // 用户取消操作
  }
}

const handleBatchStop = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要停机的设备')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要停机选中的 ${selectedIds.value.length} 个设备吗？`,
      '批量停机确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('批量停机成功')
  } catch {
    // 用户取消操作
  }
}

const handleExport = () => {
  ElMessage.info('导出设备状态报告')
}

const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  handleSearch()
}

// 生命周期
onMounted(() => {
  handleSearch()
})
</script>

<style scoped lang="scss">
.equipment-status-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;
}

.page-title-section {
  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 8px 0;
  }
  
  .page-subtitle {
    font-size: 14px;
    color: #909399;
    margin: 0;
  }
}

.page-actions {
  display: flex;
  gap: 12px;
}

// 状态统计卡片
.status-cards {
  margin-bottom: 20px;
  
  .status-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    transition: transform 0.2s;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
    }
    
    &.running {
      border-left: 4px solid #67c23a;
    }
    
    &.idle {
      border-left: 4px solid #e6a23c;
    }
    
    &.maintenance {
      border-left: 4px solid #409eff;
    }
    
    &.offline {
      border-left: 4px solid #f56c6c;
    }
    
    .card-icon {
      margin-right: 16px;
    }
    
    .card-content {
      .card-number {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .card-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.filter-form {
  .el-form-item {
    margin-bottom: 0;
    margin-right: 20px;
  }
}

.table-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  
  h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.table-actions {
  display: flex;
  gap: 8px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

// 状态样式
.text-success {
  color: #67c23a;
  font-weight: 600;
}

.text-warning {
  color: #e6a23c;
  font-weight: 600;
}

.text-danger {
  color: #f56c6c;
  font-weight: 600;
}

// 设备详情
.equipment-detail {
  .chart-container {
    margin-top: 20px;
    text-align: center;
    
    h4 {
      margin-bottom: 16px;
      color: #303133;
    }
    
    .chart-placeholder {
      padding: 40px;
      background-color: #f5f7fa;
      border-radius: 8px;
      border: 2px dashed #dcdfe6;
      
      p {
        margin-top: 16px;
        color: #909399;
        font-size: 14px;
      }
    }
  }
}

// 响应式设计
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
  
  .filter-form {
    .el-form-item {
      margin-right: 0;
      margin-bottom: 12px;
      width: 100%;
    }
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .table-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

// 自定义样式
:deep(.el-table) {
  .el-table__header-wrapper {
    th {
      background-color: #f5f7fa;
      color: #606266;
      font-weight: 600;
    }
  }
}

:deep(.el-dialog) {
  border-radius: 8px;
  
  .el-dialog__header {
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;
    margin-right: 0;
  }
  
  .el-dialog__body {
    padding: 24px;
  }
  
  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #ebeef5;
  }
}

:deep(.el-descriptions) {
  margin-bottom: 20px;
  
  .el-descriptions__label {
    font-weight: 600;
    color: #606266;
  }
}
</style>