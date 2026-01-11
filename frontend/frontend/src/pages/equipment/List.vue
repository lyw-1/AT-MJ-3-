<template>
  <div class="equipment-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">设备管理</h1>
        <p class="page-subtitle">管理和监控生产设备的运行状态和维护记录</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增设备
        </el-button>
        <el-button @click="handleImport">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <el-card class="stats-card">
      <h3 class="stats-title">设备类型统计</h3>
      <div class="stats-container" v-loading="statsLoading">
        <div 
          v-for="item in categoryStats" 
          :key="item.category"
          class="stat-item" 
          :class="{ 'active': selectedCategory === item.category }"
          @click="handleCategoryClick(item.category)"
        >
          <div class="stat-label">{{ item.category || '未知类型' }}</div>
          <div class="stat-value">{{ item.count || 0 }}</div>
        </div>
      </div>
      <div v-if="!statsLoading && categoryStats.length === 0" class="no-stats">
        <el-empty description="暂无统计数据" />
      </div>
    </el-card>

    <!-- 数据表格区域 -->
    <el-card class="table-card">
      <h3 class="table-header-title">设备列表</h3>
      <div class="table-actions">
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="equipmentList"
        @selection-change="handleSelectionChange"
        stripe
        border
        :cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        :header-cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        
        <el-table-column prop="equipmentCode" label="设备编码" width="150" sortable show-overflow-tooltip />
        <el-table-column prop="equipmentName" label="设备名称" width="180" show-overflow-tooltip />
        <el-table-column prop="equipmentType" label="设备类型" width="120" show-overflow-tooltip />
        <el-table-column prop="model" label="型号规格" width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="运行状态" width="120" sortable>
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="安装位置" width="150" show-overflow-tooltip />
        <el-table-column prop="manufacturer" label="制造商" width="150" show-overflow-tooltip />
        <el-table-column prop="purchaseDate" label="购买日期" width="150" sortable />
        <el-table-column prop="lastMaintenanceDate" label="上次维护" width="150" sortable />
        <el-table-column prop="nextMaintenanceDate" label="下次维护" width="150" sortable />
        <el-table-column prop="responsiblePerson" label="负责人" width="120" show-overflow-tooltip />
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

    <!-- 设备详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="设备详情"
      width="60%"
    >
      <div v-if="selectedEquipment" class="equipment-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备编码">{{ selectedEquipment.equipmentCode }}</el-descriptions-item>
          <el-descriptions-item label="设备名称">{{ selectedEquipment.equipmentName }}</el-descriptions-item>
          <el-descriptions-item label="设备类型">{{ selectedEquipment.equipmentType }}</el-descriptions-item>
          <el-descriptions-item label="型号规格">{{ selectedEquipment.model }}</el-descriptions-item>
          <el-descriptions-item label="运行状态">{{ getStatusText(selectedEquipment.status) }}</el-descriptions-item>
          <el-descriptions-item label="安装位置">{{ selectedEquipment.location }}</el-descriptions-item>
          <el-descriptions-item label="制造商">{{ selectedEquipment.manufacturer }}</el-descriptions-item>
          <el-descriptions-item label="购买日期">{{ selectedEquipment.purchaseDate }}</el-descriptions-item>
          <el-descriptions-item label="上次维护">{{ selectedEquipment.lastMaintenanceDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="下次维护">{{ selectedEquipment.nextMaintenanceDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="负责人">{{ selectedEquipment.responsiblePerson || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ selectedEquipment.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增/编辑设备弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="dialogTitle"
      width="60%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="设备编码" prop="equipmentCode">
          <el-input v-model="formData.equipmentCode" placeholder="请输入设备编码" />
        </el-form-item>
        <el-form-item label="设备名称" prop="equipmentName">
          <el-input v-model="formData.equipmentName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备类型" prop="equipmentType">
          <el-input v-model="formData.equipmentType" placeholder="请输入设备类型" />
        </el-form-item>
        <el-form-item label="型号规格" prop="model">
          <el-input v-model="formData.model" placeholder="请输入型号规格" />
        </el-form-item>
        <el-form-item label="运行状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择运行状态">
            <el-option label="正常" value="normal" />
            <el-option label="维护中" value="maintenance" />
            <el-option label="故障" value="fault" />
            <el-option label="停用" value="disabled" />
          </el-select>
        </el-form-item>
        <el-form-item label="安装位置">
          <el-input v-model="formData.location" placeholder="请输入安装位置" />
        </el-form-item>
        <el-form-item label="制造商">
          <el-input v-model="formData.manufacturer" placeholder="请输入制造商" />
        </el-form-item>
        <el-form-item label="购买日期">
          <el-input v-model="formData.purchaseDate" type="date" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人" />
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
import { Plus, Upload, Download, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'

// 设备类型定义
interface Equipment {
  id: number
  equipmentCode: string
  equipmentName: string
  equipmentType: string
  model: string
  status: string
  location: string
  manufacturer: string
  purchaseDate: string
  lastMaintenanceDate?: string
  nextMaintenanceDate?: string
  responsiblePerson?: string
  remark?: string
  createTime?: string
  updateTime?: string
}

// 加载状态
const loading = ref(false)
const statsLoading = ref(false)

// 设备列表数据
const equipmentList = ref<Equipment[]>([])

// 选中的设备ID列表
const selectedIds = ref<number[]>([])

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
const selectedEquipment = ref<Equipment | null>(null)
const dialogTitle = ref('新增设备')
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<Equipment>({
  id: 0,
  equipmentCode: '',
  equipmentName: '',
  equipmentType: '',
  model: '',
  status: 'normal',
  location: '',
  manufacturer: '',
  purchaseDate: new Date().toISOString().split('T')[0],
  responsiblePerson: '',
  remark: ''
})

// 表单验证规则
const formRules = {
  equipmentCode: [{ required: true, message: '请输入设备编码', trigger: 'blur' }],
  equipmentName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  equipmentType: [{ required: true, message: '请输入设备类型', trigger: 'blur' }],
  model: [{ required: true, message: '请输入型号规格', trigger: 'blur' }],
  status: [{ required: true, message: '请选择运行状态', trigger: 'change' }]
}

// 模拟API函数 - 实际项目中替换为真实API
const mockGetEquipments = async (params: any) => {
  // 模拟延迟
  await new Promise(resolve => setTimeout(resolve, 500))
  
  // 模拟数据
  const mockData = [
    {
      id: 1,
      equipmentCode: 'EQ001',
      equipmentName: '注塑机',
      equipmentType: '注塑设备',
      model: 'SZ-1200',
      status: 'normal',
      location: '车间A-01',
      manufacturer: '海天塑机',
      purchaseDate: '2023-01-15',
      lastMaintenanceDate: '2025-01-01',
      nextMaintenanceDate: '2025-07-01',
      responsiblePerson: '张三',
      remark: '主要用于生产塑料外壳'
    },
    {
      id: 2,
      equipmentCode: 'EQ002',
      equipmentName: '挤出机',
      equipmentType: '挤出设备',
      model: 'JC-800',
      status: 'maintenance',
      location: '车间B-02',
      manufacturer: '上海金纬',
      purchaseDate: '2023-03-20',
      lastMaintenanceDate: '2025-01-15',
      nextMaintenanceDate: '2025-07-15',
      responsiblePerson: '李四',
      remark: '用于生产管材'
    },
    {
      id: 3,
      equipmentCode: 'EQ003',
      equipmentName: '吹塑机',
      equipmentType: '吹塑设备',
      model: 'CS-600',
      status: 'fault',
      location: '车间C-03',
      manufacturer: '广东仕诚',
      purchaseDate: '2023-05-10',
      lastMaintenanceDate: '2024-12-10',
      nextMaintenanceDate: '2025-06-10',
      responsiblePerson: '王五',
      remark: '用于生产塑料瓶'
    }
  ]
  
  // 应用类型筛选
  const filteredData = params.equipmentType 
    ? mockData.filter(item => item.equipmentType === params.equipmentType)
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
const calculateCategoryStats = (data: Equipment[]) => {
  const statsMap = new Map<string, number>()
  
  data.forEach(item => {
    const category = item.equipmentType || '未知类型'
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
    const response = await mockGetEquipments({ page: 1, pageSize: 1000 })
    categoryStats.value = calculateCategoryStats(response.data.records)
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 获取设备列表
const fetchEquipments = async () => {
  loading.value = true
  try {
    const response = await mockGetEquipments({
      page: pagination.currentPage,
      pageSize: pagination.pageSize,
      ...(selectedCategory.value && { equipmentType: selectedCategory.value })
    })
    
    equipmentList.value = response.data.records
    pagination.total = response.data.total
    
    ElMessage.success('加载设备列表成功')
  } catch (error) {
    console.error('获取设备列表失败:', error)
    ElMessage.error('获取设备列表失败')
    equipmentList.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 处理类别点击筛选
const handleCategoryClick = (category: string) => {
  selectedCategory.value = selectedCategory.value === category ? null : category
  pagination.currentPage = 1
  fetchEquipments()
}

// 获取状态文本
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'normal': '正常',
    'maintenance': '维护中',
    'fault': '故障',
    'disabled': '停用'
  }
  return statusMap[status] || status
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'normal': 'success',
    'maintenance': 'warning',
    'fault': 'danger',
    'disabled': 'info'
  }
  return typeMap[status] || 'info'
}

// 处理新增设备
const handleAdd = () => {
  dialogTitle.value = '新增设备'
  // 重置表单数据
  Object.assign(formData, {
    id: 0,
    equipmentCode: '',
    equipmentName: '',
    equipmentType: '',
    model: '',
    status: 'normal',
    location: '',
    manufacturer: '',
    purchaseDate: new Date().toISOString().split('T')[0],
    responsiblePerson: '',
    remark: ''
  })
  editDialogVisible.value = true
}

// 处理编辑设备
const handleEdit = (row: Equipment) => {
  dialogTitle.value = '编辑设备'
  // 填充表单数据
  Object.assign(formData, { ...row })
  editDialogVisible.value = true
}

// 处理查看详情
const handleView = (row: Equipment) => {
  selectedEquipment.value = { ...row }
  detailDialogVisible.value = true
}

// 处理删除设备
const handleDelete = async (row: Equipment) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备 ${row.equipmentName} 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    ElMessage.success('删除成功')
    fetchEquipments()
    fetchCategoryStats()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除设备失败:', error)
      ElMessage.error('删除设备失败')
    }
  }
}

// 处理批量删除设备
const handleBatchDelete = async () => {
  if (selectedIds.length === 0) {
    ElMessage.warning('请选择要删除的设备')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.length} 台设备吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟批量删除操作
    ElMessage.success(`成功删除 ${selectedIds.length} 台设备`)
    selectedIds.value = []
    fetchEquipments()
    fetchCategoryStats()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除设备失败:', error)
      ElMessage.error('批量删除设备失败')
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
    fetchEquipments()
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
    fetchEquipments()
  ])
}

// 处理批量导入
const handleImport = () => {
  ElMessage.info('批量导入功能')
}

// 处理导出数据
const handleExport = () => {
  ElMessage.info('导出数据功能')
}

// 分页相关方法
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchEquipments()
}

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchEquipments()
}

// 选择变化
const handleSelectionChange = (selection: Equipment[]) => {
  selectedIds.value = selection.map(item => item.id).filter(Boolean)
}

// 页面挂载时获取数据
onMounted(async () => {
  await Promise.all([
    fetchCategoryStats(),
    fetchEquipments()
  ])
})
</script>

<style scoped lang="scss">
.equipment-container {
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
