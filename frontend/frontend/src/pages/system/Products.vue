<template>
  <div class="products-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">成品列表</h1>
        <p class="page-subtitle">管理成品类别、规格和特殊要求</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增成品
        </el-button>
        <el-button @click="handleImport">
          <el-icon><Upload /></el-icon>
          导入数据
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>



    <!-- 各类别统计数量 -->
    <el-card class="stats-card">
      <h3 class="stats-title">成品类别统计</h3>
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

    <!-- 成品列表 -->
    <el-card class="table-card">
      <h3 class="table-header-title">成品列表</h3>
      <div class="table-actions">
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="productsList"
        @selection-change="handleSelectionChange"
        @filter-change="handleFilterChange"
        stripe
        border
        :cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        :header-cell-style="{ 'text-align': 'left', 'vertical-align': 'middle' }"
        style="width: 100%"
      >
        <el-table-column 
          type="selection" 
          width="80"
          :cell-style="{ 'text-align': 'center' }"
          :header-cell-style="{ 'text-align': 'center' }"
        />
        <el-table-column 
          prop="productCategory" 
          label="成品类别" 
          width="150"
          filterable
          :filter-method="(value, row) => row.productCategory.includes(value)"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="productSpec" 
          label="成品规格" 
          width="150"
          filterable
          :filter-method="(value, row) => row.productSpec.includes(value)"
          show-overflow-tooltip
        />
        <el-table-column 
          label="容重要求" 
          width="150"
          filterable
          :filter-method="(value, row) => {
            if (!value) return true;
            const densityStr = `${row.densityRequirement?.min || 0}-${row.densityRequirement?.max || 0}`;
            return densityStr.includes(value.toString());
          }"
        >
          <template #default="{ row }">
            <span v-if="row.densityRequirement">
              {{ row.densityRequirement.min }} - {{ row.densityRequirement.max }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column 
          label="壁厚要求" 
          width="150"
          filterable
          :filter-method="(value, row) => {
            if (!value) return true;
            const wallThicknessStr = `${row.slotWidthRequirement?.min || 0}-${row.slotWidthRequirement?.max || 0}`;
            return wallThicknessStr.includes(value.toString());
          }"
        >
          <template #default="{ row }">
            <span v-if="row.slotWidthRequirement">
              {{ row.slotWidthRequirement.min }} - {{ row.slotWidthRequirement.max }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column 
          prop="customer" 
          label="客户名称" 
          width="150"
          show-overflow-tooltip
          filterable
          :filter-method="(value, row) => row.customer?.includes(value) || false"
        />
        <el-table-column 
          prop="otherRequirements" 
          label="其他特殊要求" 
          min-width="200" 
          show-overflow-tooltip
          filterable
          :filter-method="(value, row) => row.otherRequirements?.includes(value) || false"
        />
        <el-table-column 
          prop="createTime" 
          label="创建时间" 
          width="160"
          filterable
          :filter-method="(value, row) => {
            if (!value) return true;
            return row.createTime?.includes(value) || false;
          }"
        />
        <el-table-column 
          prop="updateTime" 
          label="更新时间" 
          width="160"
          filterable
          :filter-method="(value, row) => {
            if (!value) return true;
            return row.updateTime?.includes(value) || false;
          }"
        />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
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
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="成品类别" prop="productCategory">
          <el-input
            v-model="formData.productCategory"
            placeholder="请输入成品类别"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="成品规格" prop="productSpec">
          <el-input
            v-model="formData.productSpec"
            placeholder="请输入成品规格"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="容重要求">
          <div class="range-input">
            <el-input-number
              v-model="formData.densityRequirement.min"
              :min="0"
              :max="1000"
              :precision="0"
              placeholder="最小值"
              style="width: 45%"
              @input="handleDensityInput('min')"
            />
            <span class="range-separator">-</span>
            <el-input-number
              v-model="formData.densityRequirement.max"
              :min="0"
              :max="1000"
              :precision="0"
              placeholder="最大值"
              style="width: 45%"
              @input="handleDensityInput('max')"
            />
          </div>
        </el-form-item>
        <el-form-item label="壁厚要求">
          <div class="range-input">
            <el-input-number
              v-model="formData.slotWidthRequirement.min"
              :min="0"
              :max="10"
              :precision="3"
              placeholder="最小值"
              style="width: 45%"
            />
            <span class="range-separator">-</span>
            <el-input-number
              v-model="formData.slotWidthRequirement.max"
              :min="0"
              :max="10"
              :precision="3"
              placeholder="最大值"
              style="width: 45%"
            />
          </div>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input
            v-model="formData.customer"
            placeholder="请输入客户名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="其他特殊要求">
          <el-input
            v-model="formData.otherRequirements"
            type="textarea"
            :rows="4"
            placeholder="请输入其他特殊要求"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog
      v-model="importDialogVisible"
      title="导入成品数据"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="import-content">
        <el-upload
          ref="uploadRef"
          v-model:file-list="importFileList"
          :auto-upload="false"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleFileChange"
          accept=".xlsx,.xls"
          drag
        >
          <el-icon class="el-icon--upload"><Upload /></el-icon>
          <div class="el-upload__text">
            拖拽文件到此处或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              <el-button type="text" size="small" @click="handleDownloadTemplate">
                下载导入模板
              </el-button>
              <span class="tip-text">支持 .xlsx, .xls 格式文件，单次导入不超过1000条</span>
            </div>
          </template>
        </el-upload>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleImportCancel">取消</el-button>
          <el-button type="primary" @click="handleImportSubmit" :loading="importLoading">导入</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Download, Edit, Delete 
} from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { 
  getProducts, 
  createProduct, 
  updateProduct, 
  deleteProduct, 
  batchDeleteProducts, 
  exportProducts,
  importProducts,
  type Product 
} from '@/api/product'

// 表格数据
const loading = ref(false)
const statsLoading = ref(false)
const productsList = ref<Product[]>([])
const selectedIds = ref<number[]>([])

// 分类统计数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])

// 当前选中的类别
const selectedCategory = ref<string | null>(null)



// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增成品')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<Product>({
  productCategory: '',
  productSpec: '',
  densityRequirement: { min: 0, max: 0 },
  slotWidthRequirement: { min: 0, max: 0 },
  customer: '',
  otherRequirements: ''
})

// 导入相关
const importDialogVisible = ref(false)
const importLoading = ref(false)
const importFileList = ref<any[]>([])
const uploadRef = ref<any>()
let selectedFile: File | null = null

// 表单验证规则
const formRules: FormRules = {
  productCategory: [
    { required: true, message: '请输入成品类别', trigger: 'blur' }
  ],
  productSpec: [
    { required: true, message: '请输入成品规格', trigger: 'blur' }
  ]
}

// 计算分类统计数据
const calculateCategoryStats = (products: Product[]) => {
  const statsMap = new Map<string, number>()
  
  // 遍历所有成品，按类别统计数量
  if (Array.isArray(products)) {
    products.forEach(product => {
      // 确保product存在且productCategory有值
      if (product && product.productCategory && typeof product.productCategory === 'string') {
        // 处理可能的乱码字符，即使有乱码也进行统计
        const category = product.productCategory.trim() || '未分类'
        if (category) {
          const count = statsMap.get(category) || 0
          statsMap.set(category, count + 1)
        }
      }
    })
  }
  
  // 转换为数组格式
  const statsArray = Array.from(statsMap.entries()).map(([category, count]) => ({
    category,
    count
  }))
  
  // 按数量降序排序
  statsArray.sort((a, b) => b.count - a.count)
  
  return statsArray
}

// 获取成品列表
const fetchProducts = async () => {
  loading.value = true
  try {
    // 调用后端API获取数据
    const response = await getProducts({
      page: pagination.currentPage,
      size: pagination.pageSize,
      productCategory: selectedCategory.value || undefined
    })
    
    // 使用API返回的数据
    productsList.value = response.records || []
    pagination.total = response.total || 0
    
  } catch (error) {
    console.error('获取成品列表失败:', error)
    ElMessage.error('获取成品列表失败')
    productsList.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 获取分类统计数据
const fetchCategoryStats = async () => {
  statsLoading.value = true
  try {
    // 调用API获取所有产品数据，用于统计分类
    const response = await getProducts({
      page: 1,
      size: 1000 // 获取足够多的数据来统计分类
    })
    
    // 计算分类统计数据
    categoryStats.value = calculateCategoryStats(response.records || [])
    
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 处理类别点击
const handleCategoryClick = (category: string) => {
  // 如果点击的是已经选中的类别，则取消选中
  if (selectedCategory.value === category) {
    selectedCategory.value = null
  } else {
    selectedCategory.value = category
  }
  
  // 重置页码为1
  pagination.currentPage = 1
  
  // 重新获取产品列表
  fetchProducts()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增成品'
  // 重置表单数据，包括清除id字段
  Object.assign(formData, {
    id: undefined,
    productCategory: '',
    productSpec: '',
    densityRequirement: { min: 0, max: 0 },
    slotWidthRequirement: { min: 0, max: 0 },
    customer: '',
    otherRequirements: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: Product) => {
  dialogTitle.value = '编辑成品'
  // 填充表单数据
  Object.assign(formData, {
    id: row.id,
    productCategory: row.productCategory,
    productSpec: row.productSpec,
    densityRequirement: row.densityRequirement || { min: 0, max: 0 },
    slotWidthRequirement: row.slotWidthRequirement || { min: 0, max: 0 },
    customer: row.customer || '',
    otherRequirements: row.otherRequirements
  })
  dialogVisible.value = true
}

// 容重输入处理函数 - 只保留整数部分
const handleDensityInput = (type: 'min' | 'max') => {
  // 将值转换为整数，忽略小数部分
  formData.densityRequirement[type] = Math.floor(formData.densityRequirement[type])
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  // 提交前再次确保容重值为整数
  formData.densityRequirement.min = Math.floor(formData.densityRequirement.min)
  formData.densityRequirement.max = Math.floor(formData.densityRequirement.max)
  
  // 验证容重最大值是否大于等于最小值
  if (formData.densityRequirement.max < formData.densityRequirement.min) {
    ElMessage.error('容重最大值不能小于最小值')
    return
  }
  
  // 验证壁厚要求的最大值是否大于等于最小值
  if (formData.slotWidthRequirement.max < formData.slotWidthRequirement.min) {
    ElMessage.error('壁厚最大值不能小于最小值')
    return
  }
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const form = { ...formData }
        const id = form.id
        
        if (id) {
          // 编辑模式 - 调用更新API
          await updateProduct(id, form)
          ElMessage.success('修改成功')
        } else {
          // 新增模式 - 调用创建API
          await createProduct(form)
          ElMessage.success('新增成功')
        }
        
        // 关闭对话框
        dialogVisible.value = false
        
        // 重新获取数据，更新列表
        await fetchProducts()
      } catch (error: any) {
        console.error('提交失败:', error)
        // 处理重复数据错误，显示具体错误信息
        const errorMessage = error.response?.data?.message || error.message || '操作失败，请重试'
        ElMessage.error(errorMessage)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 单条删除
const handleDelete = async (row: Product) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除成品 "${row.productCategory}-${row.productSpec}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用删除API
    await deleteProduct(row.id!)
    
    ElMessage.success('删除成功')
    // 重新获取数据
    fetchProducts()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 条成品吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用批量删除API
    await batchDeleteProducts(selectedIds.value)
    
    ElMessage.success('批量删除成功')
    // 重新获取数据
    fetchProducts()
    // 清空选择
    selectedIds.value = []
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出数据
const handleExport = async () => {
  try {
    // 调用导出API
    const response = await exportProducts()
    
    // 创建下载链接
    const blob = new Blob([response.data], { type: 'text/csv;charset=utf-8;' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `成品列表_${new Date().toISOString().slice(0, 10)}.csv`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 选择变化
const handleSelectionChange = (selection: Product[]) => {
  selectedIds.value = selection.map(item => item.id!).filter(Boolean)
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchProducts()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  fetchProducts()
}

// 处理导入按钮点击
const handleImport = () => {
  importDialogVisible.value = true
}

// 处理文件超过限制
const handleExceed = () => {
  ElMessage.warning('每次只能上传一个文件')
}

// 处理文件选择变化
const handleFileChange = (file: any) => {
  selectedFile = file.raw
}

// 处理导入取消
const handleImportCancel = () => {
  importDialogVisible.value = false
  resetImportForm()
}

// 重置导入表单
const resetImportForm = () => {
  importFileList.value = []
  selectedFile = null
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 处理导入提交
const handleImportSubmit = async () => {
  if (!selectedFile) {
    ElMessage.warning('请选择要导入的文件')
    return
  }

  importLoading.value = true
  try {
    const formData = new FormData()
    formData.append('file', selectedFile)

    await importProducts(formData)
    ElMessage.success('导入成功')
    importDialogVisible.value = false
    resetImportForm()
    // 重新获取数据
    fetchProducts()
  } catch (error) {
    ElMessage.error('导入失败，请检查文件格式和内容')
  } finally {
    importLoading.value = false
  }
}

// 处理下载模板
const handleDownloadTemplate = () => {
  // 这里实现下载模板的逻辑
  ElMessage.info('下载模板功能开发中')
}

// 处理筛选变化
const handleFilterChange = (filters: any) => {
  // 保存筛选状态
  // TODO: 实现筛选状态保存逻辑
  
  // 重置页码
  pagination.currentPage = 1
  
  // 触发数据加载
  fetchProducts()
}

// 初始化
onMounted(async () => {
  // 并行获取分类统计数据和产品列表
  await Promise.all([
    fetchCategoryStats(),
    fetchProducts()
  ])
})
</script>

<style scoped>
.products-container {
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

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

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
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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

/* 选择列复选框居中样式 */
:deep(.el-table__column--selection) .el-table__cell {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-left: 0 !important;
  padding-right: 0 !important;
}

:deep(.el-table__column--selection) .el-checkbox {
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 表头选择框居中 */
:deep(.el-table__header-wrapper) .el-table__column--selection .el-table__cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.stats-content {
  padding: 8px 0;
}

.no-stats {
  padding: 40px 0;
  text-align: center;
}

/* 导入相关样式 */
.import-content {
  padding: 20px 0;
}

.import-content :deep(.el-upload-dragger) {
  padding: 40px 20px;
}

.import-content :deep(.el-upload__tip) {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 16px;
  text-align: center;
}

.import-content .tip-text {
  font-size: 12px;
  color: #909399;
}

/* 范围输入样式 */
.range-input {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.range-separator {
  font-size: 16px;
  color: #909399;
  font-weight: bold;
  width: 10%;
  text-align: center;
}

.range-input :deep(.el-input-number) {
  margin: 0;
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
  
  .import-content :deep(.el-upload-dragger) {
    padding: 30px 10px;
  }
}
</style>