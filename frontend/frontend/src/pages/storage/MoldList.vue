<template>
  <div class="mold-list-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">模具列表</h1>
        <p class="page-subtitle">管理和查看所有模具的库存状态</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增模具
        </el-button>
        <el-button @click="handleImport">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 各类别统计数量 -->
    <el-card class="stats-card">
      <h3 class="stats-title">模具状态统计</h3>
      <div class="stats-container" v-loading="statsLoading">
        <div 
          v-for="item in categoryStats" 
          :key="item.category"
          class="stat-item" 
          :class="{ 'active': selectedCategory === item.category }"
          @click="handleCategoryClick(item.category)"
        >
          <div class="stat-label">{{ item.category }}</div>
          <div class="stat-value">{{ item.count }}</div>
        </div>
      </div>
      <div v-if="!statsLoading && categoryStats.length === 0" class="no-stats">
        <el-empty description="暂无统计数据" />
      </div>
    </el-card>

    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="模具编号">
          <el-input v-model="filterForm.moldNumber" placeholder="请输入模具编号" clearable />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="filterForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="模具状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="库存" value="库存" />
            <el-option label="使用中" value="使用中" />
            <el-option label="维修中" value="维修中" />
            <el-option label="报废" value="报废" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置">
          <el-input v-model="filterForm.location" placeholder="请输入存放位置" clearable />
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

    <el-card class="table-card">
      <div class="table-header">
        <h3>模具列表</h3>
        <div class="table-actions">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="moldList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="moldNumber" label="模具编号" width="120" fixed />
        <el-table-column prop="productName" label="产品名称" width="150" show-overflow-tooltip />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="cavityCount" label="腔数" width="80" align="center" />
        <el-table-column prop="material" label="材质" width="100" />
        <el-table-column prop="location" label="存放位置" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="manufacturer" label="制造商" width="120" show-overflow-tooltip />
        <el-table-column prop="manufactureDate" label="制造日期" width="120" />
        <el-table-column prop="serviceLife" label="使用寿命" width="100" align="center">
          <template #default="{ row }">
            {{ row.serviceLife }} 次
          </template>
        </el-table-column>
        <el-table-column prop="usedCount" label="已用次数" width="100" align="center" />
        <el-table-column prop="maintenanceCount" label="维修次数" width="100" align="center" />
        <el-table-column prop="lastMaintenanceDate" label="最后维修" width="120" />
        <el-table-column prop="price" label="价格" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.price?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="primary" link @click="handleView(row)">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button type="primary" link @click="handleMaintenance(row)">
              <el-icon><Tools /></el-icon>
              维修
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="moldFormRef"
        :model="moldForm"
        :rules="moldRules"
        label-width="100px"
        inline
      >
        <el-form-item label="模具编号" prop="moldNumber">
          <el-input v-model="moldForm.moldNumber" placeholder="请输入模具编号" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="moldForm.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="规格型号" prop="specification">
          <el-input v-model="moldForm.specification" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="腔数" prop="cavityCount">
          <el-input-number v-model="moldForm.cavityCount" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="材质" prop="material">
          <el-select v-model="moldForm.material" placeholder="请选择材质">
            <el-option label="45#钢" value="45#钢" />
            <el-option label="P20" value="P20" />
            <el-option label="718H" value="718H" />
            <el-option label="NAK80" value="NAK80" />
            <el-option label="S136" value="S136" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="moldForm.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="moldForm.status" placeholder="请选择状态">
            <el-option label="库存" value="库存" />
            <el-option label="使用中" value="使用中" />
            <el-option label="维修中" value="维修中" />
            <el-option label="报废" value="报废" />
          </el-select>
        </el-form-item>
        <el-form-item label="制造商" prop="manufacturer">
          <el-input v-model="moldForm.manufacturer" placeholder="请输入制造商" />
        </el-form-item>
        <el-form-item label="制造日期" prop="manufactureDate">
          <el-date-picker v-model="moldForm.manufactureDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="使用寿命" prop="serviceLife">
          <el-input-number v-model="moldForm.serviceLife" :min="1" :max="1000000" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="moldForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="moldForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  Plus,
  Search,
  Refresh,
  Edit,
  Delete,
  View,
  Tools,
  Upload,
  Download
} from '@element-plus/icons-vue'

interface MoldItem {
  id: number
  moldNumber: string
  productName: string
  specification: string
  cavityCount: number
  material: string
  location: string
  status: string
  manufacturer: string
  manufactureDate: string
  serviceLife: number
  usedCount: number
  maintenanceCount: number
  lastMaintenanceDate: string
  price: number
  remarks?: string
}

interface FilterForm {
  moldNumber: string
  productName: string
  status: string
  location: string
}

interface MoldForm {
  id?: number
  moldNumber: string
  productName: string
  specification: string
  cavityCount: number
  material: string
  location: string
  status: string
  manufacturer: string
  manufactureDate: string
  serviceLife: number
  price: number
  remarks?: string
}

// 响应式数据
const loading = ref(false)
const statsLoading = ref(false)
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const moldFormRef = ref<FormInstance>()
const selectedCategory = ref<string | null>(null)

const filterForm = reactive<FilterForm>({
  moldNumber: '',
  productName: '',
  status: '',
  location: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const moldForm = reactive<MoldForm>({
  moldNumber: '',
  productName: '',
  specification: '',
  cavityCount: 1,
  material: '',
  location: '',
  status: '库存',
  manufacturer: '',
  manufactureDate: '',
  serviceLife: 100000,
  price: 0,
  remarks: ''
})

const moldRules = {
  moldNumber: [{ required: true, message: '请输入模具编号', trigger: 'blur' }],
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  specification: [{ required: true, message: '请输入规格型号', trigger: 'blur' }],
  cavityCount: [{ required: true, message: '请输入腔数', trigger: 'blur' }],
  material: [{ required: true, message: '请选择材质', trigger: 'change' }],
  location: [{ required: true, message: '请输入存放位置', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  manufacturer: [{ required: true, message: '请输入制造商', trigger: 'blur' }],
  manufactureDate: [{ required: true, message: '请选择制造日期', trigger: 'change' }],
  serviceLife: [{ required: true, message: '请输入使用寿命', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

// 模拟数据
const moldList = ref<MoldItem[]>([
  {
    id: 1,
    moldNumber: 'M001',
    productName: '蜂窝陶瓷载体',
    specification: '100x100x50',
    cavityCount: 4,
    material: '45#钢',
    location: 'A区-01-01',
    status: '库存',
    manufacturer: 'XX模具厂',
    manufactureDate: '2024-01-15',
    serviceLife: 100000,
    usedCount: 25000,
    maintenanceCount: 2,
    lastMaintenanceDate: '2024-10-15',
    price: 15000,
    remarks: '正常使用'
  },
  {
    id: 2,
    moldNumber: 'M002',
    productName: 'DPF载体',
    specification: '150x150x100',
    cavityCount: 2,
    material: 'P20',
    location: 'A区-01-02',
    status: '使用中',
    manufacturer: 'YY模具厂',
    manufactureDate: '2024-02-20',
    serviceLife: 80000,
    usedCount: 15000,
    maintenanceCount: 1,
    lastMaintenanceDate: '2024-09-20',
    price: 25000,
    remarks: '生产中'
  },
  {
    id: 3,
    moldNumber: 'M003',
    productName: 'SCR载体',
    specification: '120x120x80',
    cavityCount: 6,
    material: '718H',
    location: 'B区-02-01',
    status: '维修中',
    manufacturer: 'ZZ模具厂',
    manufactureDate: '2023-12-10',
    serviceLife: 120000,
    usedCount: 85000,
    maintenanceCount: 5,
    lastMaintenanceDate: '2024-11-01',
    price: 18000,
    remarks: '需要大修'
  },
  {
    id: 4,
    moldNumber: 'M004',
    productName: 'TWC载体',
    specification: '90x90x45',
    cavityCount: 8,
    material: 'S136',
    location: 'B区-02-02',
    status: '库存',
    manufacturer: 'XX模具厂',
    manufactureDate: '2024-03-10',
    serviceLife: 150000,
    usedCount: 5000,
    maintenanceCount: 0,
    lastMaintenanceDate: '',
    price: 22000,
    remarks: '新模具'
  },
  {
    id: 5,
    moldNumber: 'M005',
    productName: 'DOC载体',
    specification: '110x110x60',
    cavityCount: 6,
    material: 'P20',
    location: 'C区-03-01',
    status: '报废',
    manufacturer: 'YY模具厂',
    manufactureDate: '2023-05-20',
    serviceLife: 100000,
    usedCount: 100000,
    maintenanceCount: 8,
    lastMaintenanceDate: '2024-08-15',
    price: 18000,
    remarks: '已达到使用寿命'
  }
])

// 分类统计数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])

// 计算属性
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增模具' : '编辑模具'
})

// 计算分类统计数据
const calculateCategoryStats = (list: MoldItem[]) => {
  const statsMap = new Map<string, number>()
  
  // 遍历所有模具，按状态统计数量
  list.forEach(item => {
    const category = item.status || '未知状态'
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
    // 使用模拟数据计算分类统计
    categoryStats.value = calculateCategoryStats(moldList.value)
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 方法
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '库存': 'info',
    '使用中': 'success',
    '维修中': 'warning',
    '报废': 'danger'
  }
  return statusMap[status] || 'info'
}

const handleSearch = () => {
  loading.value = true
  // 模拟搜索逻辑
  setTimeout(() => {
    loading.value = false
    pagination.total = moldList.value.length
    ElMessage.success('搜索完成')
  }, 500)
}

const handleReset = () => {
  Object.assign(filterForm, {
    moldNumber: '',
    productName: '',
    status: '',
    location: ''
  })
  selectedCategory.value = null
  handleSearch()
}

const handleSelectionChange = (selection: MoldItem[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(moldForm, {
    moldNumber: '',
    productName: '',
    specification: '',
    cavityCount: 1,
    material: '',
    location: '',
    status: '库存',
    manufacturer: '',
    manufactureDate: '',
    serviceLife: 100000,
    price: 0,
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: MoldItem) => {
  dialogType.value = 'edit'
  Object.assign(moldForm, {
    id: row.id,
    moldNumber: row.moldNumber,
    productName: row.productName,
    specification: row.specification,
    cavityCount: row.cavityCount,
    material: row.material,
    location: row.location,
    status: row.status,
    manufacturer: row.manufacturer,
    manufactureDate: row.manufactureDate,
    serviceLife: row.serviceLife,
    price: row.price,
    remarks: row.remarks || ''
  })
  dialogVisible.value = true
}

const handleView = (row: MoldItem) => {
  ElMessage.info(`查看模具: ${row.moldNumber}`)
}

const handleMaintenance = (row: MoldItem) => {
  ElMessage.info(`维修模具: ${row.moldNumber}`)
}

const handleDelete = async (row: MoldItem) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除模具 "${row.moldNumber}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 从本地模具列表中移除被删除的模具项
    const index = moldList.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      moldList.value.splice(index, 1)
      // 更新总条数
      pagination.total = moldList.value.length
      // 更新分类统计数据
      fetchCategoryStats()
      // 从选择列表中移除
      selectedIds.value = selectedIds.value.filter(id => id !== row.id)
      ElMessage.success('删除成功')
    } else {
      ElMessage.error('删除失败：未找到该模具')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + (error as Error).message)
    }
    // 用户取消删除时不显示错误信息
  }
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的模具')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个模具吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 保存要删除的数量
    const deleteCount = selectedIds.value.length
    // 从本地模具列表中移除被选中的模具项
    moldList.value = moldList.value.filter(item => !selectedIds.value.includes(item.id))
    // 更新总条数
    pagination.total = moldList.value.length
    // 更新分类统计数据
    fetchCategoryStats()
    // 清空选择列表
    selectedIds.value = []
    ElMessage.success(`成功删除 ${deleteCount} 个模具`)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败：' + (error as Error).message)
    }
    // 用户取消删除时不显示错误信息
  }
}

const handleImport = () => {
  ElMessage.info('批量导入功能')
}

const handleExport = () => {
  ElMessage.info('导出数据功能')
}

const handleSubmit = async () => {
  if (!moldFormRef.value) return
  
  try {
    await moldFormRef.value.validate()
    if (dialogType.value === 'add') {
      // 模拟添加逻辑
      const newMold: MoldItem = {
        id: moldList.value.length + 1,
        ...moldForm,
        usedCount: 0,
        maintenanceCount: 0,
        lastMaintenanceDate: ''
      }
      moldList.value.push(newMold)
      ElMessage.success('新增模具成功')
    } else {
      // 模拟编辑逻辑
      const index = moldList.value.findIndex(item => item.id === moldForm.id)
      if (index !== -1) {
        Object.assign(moldList.value[index], moldForm)
        ElMessage.success('编辑模具成功')
      } else {
        ElMessage.error('编辑失败：未找到该模具')
      }
    }
    dialogVisible.value = false
    // 更新分类统计数据
    fetchCategoryStats()
    // 刷新列表
    handleSearch()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  handleSearch()
}

// 处理类别点击
const handleCategoryClick = (category: string) => {
  // 如果点击的是已经选中的类别，则取消选中
  if (selectedCategory.value === category) {
    selectedCategory.value = null
    filterForm.status = ''
  } else {
    selectedCategory.value = category
    filterForm.status = category
  }
  
  // 重置页码为1
  pagination.currentPage = 1
  
  // 重新获取产品列表
  handleSearch()
}

// 生命周期
onMounted(async () => {
  // 并行获取分类统计数据和产品列表
  await Promise.all([
    fetchCategoryStats(),
    handleSearch()
  ])
})
</script>

<style scoped lang="scss">
.mold-list-container {
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

/* 统计卡片样式 */
.stats-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.stats-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #333;
}

/* 统计容器样式 */
.stats-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

/* 统计卡片样式 */
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

/* 统计卡片悬停样式 */
.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 统计卡片选中样式 */
.stat-item.active {
  background-color: #337ecc;
  color: white;
  box-shadow: 0 4px 12px rgba(51, 126, 204, 0.4);
  border: 2px solid #2a6baf;
}

.stat-item.active .stat-label {
  color: white;
}

.stat-item.active .stat-value {
  color: white;
}

/* 统计标签样式 */
.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  transition: color 0.3s ease;
}

/* 统计数值样式 */
.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #409eff;
  transition: color 0.3s ease;
}

/* 无统计数据样式 */
.no-stats {
  padding: 40px 0;
  text-align: center;
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

:deep(.el-form--inline) {
  .el-form-item {
    margin-right: 24px;
    margin-bottom: 16px;
  }
}
</style>