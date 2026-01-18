<template>
  <div class="stock-records-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">出入库明细</h1>
        <p class="page-subtitle">管理和查看物料的出入库记录</p>
      </div>
      <div class="page-actions">
        <!-- 动态显示入库/出库按钮 -->
        <el-button 
          v-if="activeTab === 'in'" 
          type="primary" 
          @click="handleStockIn"
          :icon="Plus"
        >
          入库
        </el-button>
        <el-button 
          v-else 
          type="primary" 
          @click="handleStockOut"
          :icon="Minus"
        >
          出库
        </el-button>
      </div>
    </div>

    <!-- 出入库操作对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="stockFormRef"
        :model="stockForm"
        :rules="stockFormRules"
        label-width="100px"
        size="default"
      >
        <!-- 物品名称选择 -->
        <el-form-item label="物品名称" prop="itemName">
          <el-select
            v-model="stockForm.itemName"
            placeholder="请选择物品名称"
            @change="handleItemNameChange"
            clearable
          >
            <el-option
              v-for="item in availableItems"
              :key="item.id"
              :label="item.itemName"
              :value="item.itemName"
            />
          </el-select>
        </el-form-item>

        <!-- 规格选择（依赖物品名称） -->
        <el-form-item label="规格" prop="specification">
          <el-select
            v-model="stockForm.specification"
            placeholder="请选择规格"
            :disabled="!stockForm.itemName"
            @change="handleSpecificationChange"
            clearable
          >
            <el-option
              v-for="spec in availableSpecifications"
              :key="spec"
              :label="spec"
              :value="spec"
            />
          </el-select>
        </el-form-item>

        <!-- 数量输入 -->
        <el-form-item label="数量" prop="quantity">
          <el-input-number
            v-model="stockForm.quantity"
            :min="1"
            :precision="0"
            placeholder="请输入数量"
            @change="handleQuantityChange"
          />
        </el-form-item>

        <!-- 操作原因 -->
        <el-form-item label="操作原因" prop="reason">
          <el-input
            v-model="stockForm.reason"
            placeholder="请输入操作原因"
            type="textarea"
            :rows="3"
          />
        </el-form-item>

        <!-- 备注 -->
        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="stockForm.remarks"
            placeholder="请输入备注"
            type="textarea"
            :rows="2"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="handleSubmit"
            :disabled="!isFormValid"
            :loading="isSubmitting"
          >
            提交
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 顶部导航标签 -->
    <el-card class="tab-card">
      <el-tabs 
        v-model="activeTab" 
        @tab-change="handleTabChange"
        class="stock-tabs"
      >
        <el-tab-pane label="入库记录" name="in">
          <!-- 入库记录内容将通过表格组件渲染 -->
        </el-tab-pane>
        <el-tab-pane label="出库记录" name="out">
          <!-- 出库记录内容将通过表格组件渲染 -->
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据展示区域 -->
    <el-card class="table-card">
      <!-- 筛选区域 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm" size="small">
          <el-form-item label="物品名称">
            <div class="item-name-filter">
              <el-input 
                v-model="filterForm.itemName" 
                placeholder="请输入物品名称" 
                clearable
                @keyup.enter="handleSearch"
                @input="handleSearch"
                style="width: 200px"
              />
              <el-checkbox 
                v-model="filterForm.exactMatch" 
                label="精确匹配" 
                size="small"
                @change="handleSearch"
              />
            </div>
          </el-form-item>
          <el-form-item label="规格">
            <el-input 
              v-model="filterForm.specification" 
              placeholder="请输入规格" 
              clearable
              @keyup.enter="handleSearch"
              @input="handleSearch"
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              @change="handleSearch"
            />
          </el-form-item>
          <el-form-item label="操作人">
            <el-select
              v-model="filterForm.operator"
              placeholder="请选择操作人"
              clearable
              @change="handleSearch"
              style="width: 150px"
            >
              <el-option
                v-for="user in operatorList"
                :key="user"
                :label="user"
                :value="user"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :icon="Search">
              搜索
            </el-button>
            <el-button @click="handleReset" :icon="Refresh">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 筛选结果统计 -->
      <div class="filter-result">
        <el-badge :value="filteredCount" type="info" :class="'filter-badge'">
          <span>共 {{ filteredCount }} 条记录</span>
        </el-badge>
      </div>

      <!-- 表格数据展示 -->
      <el-table 
        v-loading="loading" 
        :data="stockRecords" 
        stripe 
        border
        style="width: 100%"
        @sort-change="handleSortChange"
      >
        <el-table-column prop="itemName" label="物品名称" width="180" show-overflow-tooltip sortable />
        <el-table-column prop="specification" label="规格" width="150" show-overflow-tooltip />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column 
          prop="quantity" 
          label="数量" 
          width="100" 
          align="right"
          sortable
        >
          <template #default="{ row }">
            <span :class="activeTab === 'in' ? 'text-success' : 'text-danger'">
              {{ activeTab === 'in' ? '+' : '-' }}{{ row.quantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="currentStock" label="当前库存" width="100" align="right" sortable />
        <el-table-column prop="operator" label="操作人" width="120" show-overflow-tooltip />
        <el-table-column prop="createTime" label="操作日期" width="150" sortable>
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="操作原因" width="150" show-overflow-tooltip />
        <el-table-column prop="remarks" label="备注" min-width="150" show-overflow-tooltip />
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

      <!-- 空数据状态 -->
      <el-empty 
        v-if="!loading && stockRecords.length === 0" 
        description="暂无数据"
        image-size="120"
      >
        <el-button type="primary" @click="handleSearch">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </el-empty>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, FormInstance } from 'element-plus'
import { Plus, Minus, Search, Refresh } from '@element-plus/icons-vue'

// 定义出入库记录类型
interface StockRecord {
  id: number
  itemName: string
  specification: string
  unit: string
  quantity: number
  currentStock: number
  operator: string
  createTime: string
  reason: string
  remarks: string
}

// 定义筛选表单类型
interface FilterForm {
  itemName: string
  specification: string
  dateRange: [string, string] | null
  operator: string
  exactMatch: boolean
}

// 定义分页参数类型
interface Pagination {
  currentPage: number
  pageSize: number
  total: number
}

// 响应式数据
const activeTab = ref('in') // 当前激活的标签
const loading = ref(false) // 加载状态

// 筛选表单
const filterForm = reactive<FilterForm>({
  itemName: '',
  specification: '',
  dateRange: null,
  operator: '',
  exactMatch: false
})

// 分页参数
const pagination = reactive<Pagination>({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 出入库对话框相关
const stockDialogVisible = ref(false) // 对话框显示状态
const dialogType = ref<'in' | 'out'>('in') // 对话框类型
const stockFormRef = ref<FormInstance>() // 表单引用
const isSubmitting = ref(false) // 提交状态

// 可用物品列表（模拟数据，实际应从API获取）
const availableItems = ref([
  { id: 1, itemName: '切削液', specifications: ['5L/桶', '10L/桶', '20L/桶'] },
  { id: 2, itemName: '润滑油', specifications: ['20L/桶', '50L/桶'] },
  { id: 3, itemName: '钻头', specifications: ['Φ8', 'Φ10', 'Φ12', 'Φ15'] },
  { id: 4, itemName: '砂纸', specifications: ['80#', '120#', '240#', '400#'] },
  { id: 5, itemName: '密封件', specifications: ['Φ40', 'Φ50', 'Φ60'] }
])

// 根据物品名称过滤的规格列表
const availableSpecifications = computed(() => {
  if (!stockForm.itemName) return []
  const selectedItem = availableItems.value.find(item => item.itemName === stockForm.itemName)
  return selectedItem?.specifications || []
})

// 出入库表单数据
const stockForm = reactive({
  itemName: '',
  specification: '',
  quantity: null,
  reason: '',
  remarks: ''
})

// 表单验证规则
const stockFormRules = {
  itemName: [{ required: true, message: '请选择物品名称', trigger: 'change' }],
  specification: [{ required: true, message: '请选择规格', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  reason: [{ required: true, message: '请输入操作原因', trigger: 'blur' }]
}

// 计算表单是否有效
const isFormValid = computed(() => {
  return !!stockForm.itemName && !!stockForm.specification && stockForm.quantity !== null && !!stockForm.reason
})

// 对话框标题
const dialogTitle = computed(() => {
  return dialogType.value === 'in' ? '物料入库' : '物料出库'
})

// 模拟出入库记录数据
const mockStockRecords: StockRecord[] = [
  {
    id: 1,
    itemName: '切削液',
    specification: '5L/桶',
    unit: '桶',
    quantity: 20,
    currentStock: 100,
    operator: '张三',
    createTime: '2024-12-17 10:30:00',
    reason: '采购入库',
    remarks: '正常采购'
  },
  {
    id: 2,
    itemName: '润滑油',
    specification: '20L/桶',
    unit: '桶',
    quantity: 10,
    currentStock: 50,
    operator: '李四',
    createTime: '2024-12-16 14:20:00',
    reason: '生产领用',
    remarks: '生产线1使用'
  },
  {
    id: 3,
    itemName: '钻头',
    specification: 'Φ10',
    unit: '个',
    quantity: 50,
    currentStock: 200,
    operator: '王五',
    createTime: '2024-12-15 09:15:00',
    reason: '采购入库',
    remarks: '备用库存'
  },
  {
    id: 4,
    itemName: '砂纸',
    specification: '120#',
    unit: '盒',
    quantity: 30,
    currentStock: 80,
    operator: '赵六',
    createTime: '2024-12-14 16:45:00',
    reason: '生产领用',
    remarks: '抛光工序使用'
  },
  {
    id: 5,
    itemName: '密封件',
    specification: 'Φ50',
    unit: '个',
    quantity: 25,
    currentStock: 150,
    operator: '孙七',
    createTime: '2024-12-13 11:20:00',
    reason: '采购入库',
    remarks: '设备维修备用'
  }
]

// 从模拟数据中提取所有操作人
const operatorList = ref<string[]>([
  '张三',
  '李四',
  '王五',
  '赵六',
  '孙七'
])

// 根据当前标签和筛选条件过滤的数据
const stockRecords = computed(() => {
  // 这里应该根据activeTab和筛选条件从API获取数据
  // 目前使用前端过滤模拟数据，实际项目中需要替换为API调用
  let filtered = [...mockStockRecords]
  
  // 1. 按物品名称过滤
  if (filterForm.itemName) {
    const itemName = filterForm.itemName.toLowerCase()
    if (filterForm.exactMatch) {
      // 精确匹配
      filtered = filtered.filter(item => item.itemName.toLowerCase() === itemName)
    } else {
      // 模糊匹配
      filtered = filtered.filter(item => item.itemName.toLowerCase().includes(itemName))
    }
  }
  
  // 2. 按规格过滤
  if (filterForm.specification) {
    const specification = filterForm.specification.toLowerCase()
    filtered = filtered.filter(item => item.specification.toLowerCase().includes(specification))
  }
  
  // 3. 按日期范围过滤
  if (filterForm.dateRange && filterForm.dateRange[0] && filterForm.dateRange[1]) {
    const startDate = new Date(filterForm.dateRange[0])
    const endDate = new Date(filterForm.dateRange[1])
    filtered = filtered.filter(item => {
      const itemDate = new Date(item.createTime)
      return itemDate >= startDate && itemDate <= endDate
    })
  }
  
  // 4. 按操作人过滤
  if (filterForm.operator) {
    filtered = filtered.filter(item => item.operator === filterForm.operator)
  }
  
  // 5. 按标签页类型过滤（入库/出库）
  // 注意：这里假设mock数据中没有type字段，实际项目中需要根据API返回的type字段过滤
  
  // 更新总条数
  pagination.total = filtered.length
  
  return filtered
})

// 筛选结果数量统计
const filteredCount = computed(() => {
  // 直接返回过滤后的数量
  return stockRecords.value.length
})

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 标签切换处理
const handleTabChange = (_tabName: string) => {
  // 重置筛选条件和分页
  handleReset()
  // 刷新数据
  fetchStockRecords()
  // 更新URL参数
  updateUrlParams()
}

// 搜索处理
const handleSearch = () => {
  // 重置到第一页
  pagination.currentPage = 1
  // 刷新数据
  fetchStockRecords()
  // 更新URL参数
  updateUrlParams()
}

// 重置筛选条件
const handleReset = () => {
  // 重置筛选表单
  Object.assign(filterForm, {
    itemName: '',
    specification: '',
    dateRange: null,
    operator: '',
    exactMatch: false
  })
  // 重置分页
  pagination.currentPage = 1
  // 刷新数据
  fetchStockRecords()
}

// 分页变化处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchStockRecords()
  // 更新URL参数
  updateUrlParams()
}

// 当前页变化处理
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchStockRecords()
  // 更新URL参数
  updateUrlParams()
}

// 排序变化处理
const handleSortChange = (sort: any) => {
  // 处理排序逻辑
  console.log('排序变化:', sort)
  fetchStockRecords()
  // 更新URL参数
  updateUrlParams()
}



// 获取出入库记录数据
const fetchStockRecords = async () => {
  loading.value = true
  try {
    // 构建请求参数
  const params = {
    type: activeTab.value,
    page: pagination.currentPage,
    pageSize: pagination.pageSize,
    itemName: filterForm.itemName,
    specification: filterForm.specification,
    startTime: filterForm.dateRange?.[0] || '',
    endTime: filterForm.dateRange?.[1] || '',
    operator: filterForm.operator,
    exactMatch: filterForm.exactMatch
  }
    
    // 这里应该调用API获取数据
    // 目前使用前端过滤模拟数据，实际项目中需要替换为API调用
    console.log('获取出入库记录:', params)
    
    // 模拟API请求延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 前端过滤数据时，total已经在stockRecords计算属性中更新
    // pagination.total = mockStockRecords.length
    
    // 更新URL参数
    updateUrlParams()
    
    // 筛选结果反馈
    if (stockRecords.value.length === 0) {
      ElMessage.info('没有找到匹配的记录')
    }
  } catch (error) {
    console.error('获取出入库记录失败:', error)
    ElMessage.error('获取出入库记录失败')
  } finally {
    loading.value = false
  }
}

// 打开入库对话框
const handleStockIn = () => {
  dialogType.value = 'in'
  resetStockForm()
  stockDialogVisible.value = true
}

// 打开出库对话框
const handleStockOut = () => {
  dialogType.value = 'out'
  resetStockForm()
  stockDialogVisible.value = true
}

// 重置表单数据
const resetStockForm = () => {
  Object.assign(stockForm, {
    itemName: '',
    specification: '',
    quantity: null,
    reason: '',
    remarks: ''
  })
  if (stockFormRef.value) {
    stockFormRef.value.clearValidate()
  }
}

// 物品名称变化处理
const handleItemNameChange = () => {
  // 重置规格和数量
  stockForm.specification = ''
  stockForm.quantity = null
}

// 规格变化处理
const handleSpecificationChange = () => {
  // 重置数量
  stockForm.quantity = null
}

// 数量变化处理
const handleQuantityChange = () => {
  // 数量变化时的处理（可选）
}

// 表单提交处理
const handleSubmit = async () => {
  if (!stockFormRef.value) return

  try {
    // 表单验证
    await stockFormRef.value.validate()
    
    isSubmitting.value = true
    
    // 构建提交数据
    const submitData = {
      ...stockForm,
      type: dialogType.value
    }
    
    // 这里应该调用API提交数据
    console.log('提交出入库数据:', submitData)
    
    // 模拟API请求延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 提交成功，关闭对话框并刷新数据
    stockDialogVisible.value = false
    ElMessage.success(`${dialogType.value === 'in' ? '入库' : '出库'}操作成功`)
    
    // 刷新数据列表
    fetchStockRecords()
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('出入库操作失败:', error)
      ElMessage.error(`${dialogType.value === 'in' ? '入库' : '出库'}操作失败`) 
    }
  } finally {
    isSubmitting.value = false
  }
}

// 从URL参数解析筛选条件
const parseUrlParams = () => {
  const url = new URL(window.location.href)
  const searchParams = url.searchParams
  
  // 解析物品名称
  const itemName = searchParams.get('itemName') || ''
  if (itemName) {
    filterForm.itemName = itemName
  }
  
  // 解析规格
  const specification = searchParams.get('specification') || ''
  if (specification) {
    filterForm.specification = specification
  }
  
  // 解析操作人
  const operator = searchParams.get('operator') || ''
  if (operator) {
    filterForm.operator = operator
  }
  
  // 解析精确匹配
  const exactMatch = searchParams.get('exactMatch') === 'true'
  filterForm.exactMatch = exactMatch
  
  // 解析日期范围
  const startTime = searchParams.get('startTime') || ''
  const endTime = searchParams.get('endTime') || ''
  if (startTime && endTime) {
    filterForm.dateRange = [startTime, endTime]
  }
  
  // 解析分页
  const page = searchParams.get('page') || '1'
  pagination.currentPage = parseInt(page, 10) || 1
  
  const pageSize = searchParams.get('pageSize') || '20'
  pagination.pageSize = parseInt(pageSize, 10) || 20
  
  // 解析标签页
  const tab = searchParams.get('tab') || 'in'
  activeTab.value = tab
}

// 更新URL参数
const updateUrlParams = () => {
  const url = new URL(window.location.href)
  const searchParams = url.searchParams
  
  // 更新物品名称
  if (filterForm.itemName) {
    searchParams.set('itemName', filterForm.itemName)
  } else {
    searchParams.delete('itemName')
  }
  
  // 更新规格
  if (filterForm.specification) {
    searchParams.set('specification', filterForm.specification)
  } else {
    searchParams.delete('specification')
  }
  
  // 更新操作人
  if (filterForm.operator) {
    searchParams.set('operator', filterForm.operator)
  } else {
    searchParams.delete('operator')
  }
  
  // 更新精确匹配
  searchParams.set('exactMatch', filterForm.exactMatch.toString())
  
  // 更新日期范围
  if (filterForm.dateRange && filterForm.dateRange[0] && filterForm.dateRange[1]) {
    searchParams.set('startTime', filterForm.dateRange[0])
    searchParams.set('endTime', filterForm.dateRange[1])
  } else {
    searchParams.delete('startTime')
    searchParams.delete('endTime')
  }
  
  // 更新分页
  searchParams.set('page', pagination.currentPage.toString())
  searchParams.set('pageSize', pagination.pageSize.toString())
  
  // 更新标签页
  searchParams.set('tab', activeTab.value)
  
  // 更新URL
  window.history.pushState({}, '', url)
}

// 页面挂载时获取数据
onMounted(() => {
  // 从URL参数解析筛选条件
  parseUrlParams()
  // 获取数据
  fetchStockRecords()
})
</script>

<style scoped lang="scss">
.stock-records-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title-section {
  h1 {
    margin: 0 0 5px 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
  }
  p {
    margin: 0;
    font-size: 14px;
    color: #606266;
  }
}

.tab-card {
  margin-bottom: 20px;
}

.stock-tabs {
  :deep(.el-tabs__nav) {
    display: flex;
    align-items: center;
  }
  :deep(.el-tabs__item) {
    font-size: 16px;
    padding: 0 30px;
  }
  :deep(.el-tabs__active-bar) {
    height: 3px;
  }
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 4px;
}

.table-card {
  .el-table {
    margin-bottom: 20px;
  }
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 15px 0;
}

.text-success {
  color: #67c23a;
  font-weight: 500;
}

.text-danger {
  color: #f56c6c;
  font-weight: 500;
}

// 物品名称筛选区域样式
.item-name-filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

// 筛选结果统计样式
.filter-result {
  margin: 10px 0;
  padding: 10px;
  background-color: #f0f9eb;
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.filter-badge {
  margin-right: 10px;
}

// 响应式设计
@media (max-width: 768px) {
  .stock-records-container {
    padding: 10px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .filter-section {
    padding: 10px;
  }
  
  .el-form {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .el-form-item {
    width: 100%;
  }
  
  .el-input {
    width: 100% !important;
  }
  
  .pagination-container {
    justify-content: center;
  }
}
</style>