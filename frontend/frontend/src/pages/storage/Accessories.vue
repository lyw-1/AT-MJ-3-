<template>
  <div class="accessories-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">配件管理</h1>
        <p class="page-subtitle">管理和维护模具配件库存</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增配件
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

    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="配件编号">
          <el-input v-model="filterForm.accessoryNumber" placeholder="请输入配件编号" clearable />
        </el-form-item>
        <el-form-item label="配件名称">
          <el-input v-model="filterForm.accessoryName" placeholder="请输入配件名称" clearable />
        </el-form-item>
        <el-form-item label="配件类型">
          <el-select v-model="filterForm.accessoryType" placeholder="请选择类型" clearable>
            <el-option label="模芯" value="模芯" />
            <el-option label="模套" value="模套" />
            <el-option label="导柱" value="导柱" />
            <el-option label="导套" value="导套" />
            <el-option label="弹簧" value="弹簧" />
            <el-option label="螺栓" value="螺栓" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="适用模具">
          <el-input v-model="filterForm.compatibleMold" placeholder="请输入适用模具" clearable />
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select v-model="filterForm.stockStatus" placeholder="请选择状态" clearable>
            <el-option label="充足" value="充足" />
            <el-option label="不足" value="不足" />
            <el-option label="缺货" value="缺货" />
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

    <el-card class="table-card">
      <div class="table-header">
        <h3>配件列表</h3>
        <div class="table-actions">
          <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchStockIn">
            <el-icon><Plus /></el-icon>
            入库
          </el-button>
          <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchStockOut">
            <el-icon><Minus /></el-icon>
            出库
          </el-button>
          <el-button type="warning" :disabled="selectedIds.length === 0" @click="handleBatchStock">
            <el-icon><Box /></el-icon>
            批量盘点
          </el-button>
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="accessoriesList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="accessoryNumber" label="配件编号" width="120" fixed />
        <el-table-column prop="accessoryName" label="配件名称" width="150" show-overflow-tooltip />
        <el-table-column prop="accessoryType" label="配件类型" width="100" align="center" />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="material" label="材质" width="100" />
        <el-table-column prop="unit" label="单位" width="60" align="center" />
        <el-table-column prop="currentStock" label="当前库存" width="100" align="center">
          <template #default="{ row }">
            <span :class="getStockClass(row.currentStock, row.minStock)">{{ row.currentStock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最低库存" width="100" align="center" />
        <el-table-column prop="maxStock" label="最高库存" width="100" align="center" />
        <el-table-column prop="stockStatus" label="库存状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStockStatusType(row.stockStatus)">{{ row.stockStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.unitPrice?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="totalValue" label="库存价值" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.totalValue?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="supplier" label="供应商" width="120" show-overflow-tooltip />
        <el-table-column prop="location" label="存放位置" width="120" />
        <el-table-column prop="lastUpdate" label="最后更新" width="120" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
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
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="accessoryFormRef"
        :model="accessoryForm"
        :rules="accessoryRules"
        label-width="100px"
        inline
      >
        <el-form-item label="配件编号" prop="accessoryNumber">
          <el-input v-model="accessoryForm.accessoryNumber" placeholder="请输入配件编号" />
        </el-form-item>
        <el-form-item label="配件名称" prop="accessoryName">
          <el-input v-model="accessoryForm.accessoryName" placeholder="请输入配件名称" />
        </el-form-item>
        <el-form-item label="配件类型" prop="accessoryType">
          <el-select v-model="accessoryForm.accessoryType" placeholder="请选择类型">
            <el-option label="模芯" value="模芯" />
            <el-option label="模套" value="模套" />
            <el-option label="导柱" value="导柱" />
            <el-option label="导套" value="导套" />
            <el-option label="弹簧" value="弹簧" />
            <el-option label="螺栓" value="螺栓" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格型号" prop="specification">
          <el-input v-model="accessoryForm.specification" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="材质" prop="material">
          <el-input v-model="accessoryForm.material" placeholder="请输入材质" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="accessoryForm.unit" placeholder="请选择单位">
            <el-option label="个" value="个" />
            <el-option label="套" value="套" />
            <el-option label="件" value="件" />
            <el-option label="根" value="根" />
            <el-option label="只" value="只" />
            <el-option label="桶" value="桶" />
            <el-option label="瓶" value="瓶" />
            <el-option label="盒" value="盒" />
            <el-option label="卷" value="卷" />
            <el-option label="块" value="块" />
            <el-option label="片" value="片" />
            <el-option label="支" value="支" />
          </el-select>
        </el-form-item>
        <el-form-item label="当前库存" prop="currentStock">
          <el-input-number v-model="accessoryForm.currentStock" :min="0" :max="10000" />
        </el-form-item>
        <el-form-item label="最低库存" prop="minStock">
          <el-input-number v-model="accessoryForm.minStock" :min="0" :max="10000" />
        </el-form-item>
        <el-form-item label="最高库存" prop="maxStock">
          <el-input-number v-model="accessoryForm.maxStock" :min="0" :max="10000" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="accessoryForm.unitPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="accessoryForm.supplier" placeholder="请输入供应商" />
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="accessoryForm.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="适用模具" prop="compatibleMold">
          <el-input v-model="accessoryForm.compatibleMold" placeholder="请输入适用模具型号" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="accessoryForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 入库对话框 -->
    <el-dialog
      v-model="stockInDialogVisible"
      title="配件入库"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="stockInFormRef"
        :model="stockInForm"
        :rules="stockInRules"
        label-width="100px"
      >
        <el-form-item label="配件编号">
          <el-input v-model="stockInForm.accessoryNumber" disabled />
        </el-form-item>
        <el-form-item label="配件名称">
          <el-input v-model="stockInForm.accessoryName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="stockInForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="入库数量" prop="quantity">
          <el-input-number v-model="stockInForm.quantity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="入库原因" prop="reason">
          <el-select v-model="stockInForm.reason" placeholder="请选择入库原因">
            <el-option label="采购入库" value="采购入库" />
            <el-option label="生产退料" value="生产退料" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="stockInForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockInDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleStockInSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 出库对话框 -->
    <el-dialog
      v-model="stockOutDialogVisible"
      title="配件出库"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="stockOutFormRef"
        :model="stockOutForm"
        :rules="stockOutRules"
        label-width="100px"
      >
        <el-form-item label="配件编号">
          <el-input v-model="stockOutForm.accessoryNumber" disabled />
        </el-form-item>
        <el-form-item label="配件名称">
          <el-input v-model="stockOutForm.accessoryName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="stockOutForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="出库数量" prop="quantity">
          <el-input-number v-model="stockOutForm.quantity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="出库原因" prop="reason">
          <el-select v-model="stockOutForm.reason" placeholder="请选择出库原因">
            <el-option label="生产领料" value="生产领料" />
            <el-option label="维修更换" value="维修更换" />
            <el-option label="报废处理" value="报废处理" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="领用人" prop="recipient">
          <el-input v-model="stockOutForm.recipient" placeholder="请输入领用人" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="stockOutForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockOutDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleStockOutSubmit">确 定</el-button>
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
  Box,
  Upload,
  Download,
  Minus
} from '@element-plus/icons-vue'

interface AccessoryItem {
  id: number
  accessoryNumber: string
  accessoryName: string
  accessoryType: string
  specification: string
  material: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  stockStatus: string
  unitPrice: number
  totalValue: number
  supplier: string
  location: string
  compatibleMold: string
  lastUpdate: string
  remarks?: string
}

interface FilterForm {
  accessoryNumber: string
  accessoryName: string
  accessoryType: string
  compatibleMold: string
  stockStatus: string
}

interface AccessoryForm {
  id?: number
  accessoryNumber: string
  accessoryName: string
  accessoryType: string
  specification: string
  material: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  unitPrice: number
  supplier: string
  location: string
  compatibleMold: string
  remarks?: string
}

interface StockInForm {
  accessoryNumber: string
  accessoryName: string
  currentStock: string
  quantity: number
  reason: string
  remarks?: string
}

interface StockOutForm {
  accessoryNumber: string
  accessoryName: string
  currentStock: string
  quantity: number
  reason: string
  recipient: string
  remarks?: string
}

// 响应式数据
const loading = ref(false)
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const stockInDialogVisible = ref(false)
const stockOutDialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const accessoryFormRef = ref<FormInstance>()
const stockInFormRef = ref<FormInstance>()
const stockOutFormRef = ref<FormInstance>()

const filterForm = reactive<FilterForm>({
  accessoryNumber: '',
  accessoryName: '',
  accessoryType: '',
  compatibleMold: '',
  stockStatus: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const accessoryForm = reactive<AccessoryForm>({
  accessoryNumber: '',
  accessoryName: '',
  accessoryType: '',
  specification: '',
  material: '',
  unit: '个',
  currentStock: 0,
  minStock: 10,
  maxStock: 1000,
  unitPrice: 0,
  supplier: '',
  location: '',
  compatibleMold: '',
  remarks: ''
})

const stockInForm = reactive<StockInForm>({
  accessoryNumber: '',
  accessoryName: '',
  currentStock: '',
  quantity: 1,
  reason: '',
  remarks: ''
})

const stockOutForm = reactive<StockOutForm>({
  accessoryNumber: '',
  accessoryName: '',
  currentStock: '',
  quantity: 1,
  reason: '',
  recipient: '',
  remarks: ''
})

const accessoryRules = {
  accessoryNumber: [{ required: true, message: '请输入配件编号', trigger: 'blur' }],
  accessoryName: [{ required: true, message: '请输入配件名称', trigger: 'blur' }],
  accessoryType: [{ required: true, message: '请选择配件类型', trigger: 'change' }],
  specification: [{ required: true, message: '请输入规格型号', trigger: 'blur' }],
  material: [{ required: true, message: '请输入材质', trigger: 'blur' }],
  unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
  currentStock: [{ required: true, message: '请输入当前库存', trigger: 'blur' }],
  minStock: [{ required: true, message: '请输入最低库存', trigger: 'blur' }],
  maxStock: [{ required: true, message: '请输入最高库存', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }],
  supplier: [{ required: true, message: '请输入供应商', trigger: 'blur' }],
  location: [{ required: true, message: '请输入存放位置', trigger: 'blur' }]
}

const stockInRules = {
  quantity: [{ required: true, message: '请输入入库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择入库原因', trigger: 'change' }]
}

const stockOutRules = {
  quantity: [{ required: true, message: '请输入出库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择出库原因', trigger: 'change' }],
  recipient: [{ required: true, message: '请输入领用人', trigger: 'blur' }]
}

// 模拟数据
const accessoriesList = ref<AccessoryItem[]>([
  {
    id: 1,
    accessoryNumber: 'A001',
    accessoryName: '标准模芯',
    accessoryType: '模芯',
    specification: 'Φ50x100',
    material: '45#钢',
    unit: '个',
    currentStock: 25,
    minStock: 10,
    maxStock: 100,
    stockStatus: '充足',
    unitPrice: 280,
    totalValue: 7000,
    supplier: 'XX配件厂',
    location: 'C区-01-01',
    compatibleMold: 'M001,M002',
    lastUpdate: '2024-11-15',
    remarks: '常用配件'
  },
  {
    id: 2,
    accessoryNumber: 'A002',
    accessoryName: '导柱套件',
    accessoryType: '导柱',
    specification: 'Φ20x150',
    material: 'GCr15',
    unit: '套',
    currentStock: 8,
    minStock: 15,
    maxStock: 80,
    stockStatus: '不足',
    unitPrice: 450,
    totalValue: 3600,
    supplier: 'YY精密',
    location: 'C区-01-02',
    compatibleMold: '通用',
    lastUpdate: '2024-11-20',
    remarks: '需要补货'
  },
  {
    id: 3,
    accessoryNumber: 'A003',
    accessoryName: '压缩弹簧',
    accessoryType: '弹簧',
    specification: 'Φ30x80',
    material: '65Mn',
    unit: '个',
    currentStock: 2,
    minStock: 20,
    maxStock: 200,
    stockStatus: '缺货',
    unitPrice: 35,
    totalValue: 70,
    supplier: 'ZZ弹簧厂',
    location: 'C区-01-03',
    compatibleMold: 'M003',
    lastUpdate: '2024-11-25',
    remarks: '紧急补货'
  }
])

// 计算属性
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增配件' : '编辑配件'
})

// 方法
const getStockStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '充足': 'success',
    '不足': 'warning',
    '缺货': 'danger'
  }
  return statusMap[status] || 'info'
}

const getStockClass = (current: number, min: number) => {
  if (current <= min * 0.5) return 'text-danger'
  if (current <= min) return 'text-warning'
  return 'text-success'
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
    accessoryNumber: '',
    accessoryName: '',
    accessoryType: '',
    compatibleMold: '',
    stockStatus: ''
  })
  handleSearch()
}

const handleSelectionChange = (selection: AccessoryItem[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(accessoryForm, {
    accessoryNumber: '',
    accessoryName: '',
    accessoryType: '',
    specification: '',
    material: '',
    unit: '个',
    currentStock: 0,
    minStock: 10,
    maxStock: 1000,
    unitPrice: 0,
    supplier: '',
    location: '',
    compatibleMold: '',
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: AccessoryItem) => {
  dialogType.value = 'edit'
  Object.assign(accessoryForm, {
    id: row.id,
    accessoryNumber: row.accessoryNumber,
    accessoryName: row.accessoryName,
    accessoryType: row.accessoryType,
    specification: row.specification,
    material: row.material,
    unit: row.unit,
    currentStock: row.currentStock,
    minStock: row.minStock,
    maxStock: row.maxStock,
    unitPrice: row.unitPrice,
    supplier: row.supplier,
    location: row.location,
    compatibleMold: row.compatibleMold,
    remarks: row.remarks || ''
  })
  dialogVisible.value = true
}

const handleStockIn = (row: AccessoryItem) => {
  Object.assign(stockInForm, {
    accessoryNumber: row.accessoryNumber,
    accessoryName: row.accessoryName,
    currentStock: row.currentStock.toString(),
    quantity: 1,
    reason: '',
    remarks: ''
  })
  stockInDialogVisible.value = true
}

const handleStockOut = (row: AccessoryItem) => {
  Object.assign(stockOutForm, {
    accessoryNumber: row.accessoryNumber,
    accessoryName: row.accessoryName,
    currentStock: row.currentStock.toString(),
    quantity: 1,
    reason: '',
    recipient: '',
    remarks: ''
  })
  stockOutDialogVisible.value = true
}

// 批量入库处理函数
const handleBatchStockIn = () => {
  // 如果只选择了一条记录，直接打开入库对话框
  if (selectedIds.value.length === 1) {
    const selectedRow = accessoriesList.value.find(item => item.id === selectedIds.value[0])
    if (selectedRow) {
      handleStockIn(selectedRow)
    }
  } else {
    // 对于多条记录，这里可以根据业务需求进行批量处理
    // 例如：显示批量入库对话框，或者逐个处理
    ElMessage.info('当前仅支持单条记录入库操作')
  }
}

// 批量出库处理函数
const handleBatchStockOut = () => {
  // 如果只选择了一条记录，直接打开出库对话框
  if (selectedIds.value.length === 1) {
    const selectedRow = accessoriesList.value.find(item => item.id === selectedIds.value[0])
    if (selectedRow) {
      handleStockOut(selectedRow)
    }
  } else {
    // 对于多条记录，这里可以根据业务需求进行批量处理
    // 例如：显示批量出库对话框，或者逐个处理
    ElMessage.info('当前仅支持单条记录出库操作')
  }
}

const handleDelete = async (row: AccessoryItem) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除配件 "${row.accessoryName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 从本地配件列表中移除被删除的配件项
    const index = accessoriesList.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      accessoriesList.value.splice(index, 1)
      // 更新总条数
      pagination.total = accessoriesList.value.length
      // 从选择列表中移除
      selectedIds.value = selectedIds.value.filter(id => id !== row.id)
      ElMessage.success('删除成功')
    } else {
      ElMessage.error('删除失败：未找到该配件')
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
    ElMessage.warning('请选择要删除的配件')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个配件吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 保存要删除的数量
    const deleteCount = selectedIds.value.length
    // 从本地配件列表中移除被选中的配件项
    accessoriesList.value = accessoriesList.value.filter(item => !selectedIds.value.includes(item.id))
    // 更新总条数
    pagination.total = accessoriesList.value.length
    // 清空选择列表
    selectedIds.value = []
    ElMessage.success(`成功删除 ${deleteCount} 个配件`)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败：' + (error as Error).message)
    }
    // 用户取消删除时不显示错误信息
  }
}

const handleBatchStock = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要盘点的配件')
    return
  }
  ElMessage.info('批量盘点功能')
}

const handleImport = () => {
  ElMessage.info('批量导入功能')
}

const handleExport = () => {
  ElMessage.info('导出数据功能')
}

const handleSubmit = async () => {
  if (!accessoryFormRef.value) return
  
  try {
    await accessoryFormRef.value.validate()
    if (dialogType.value === 'add') {
      ElMessage.success('新增配件成功')
    } else {
      ElMessage.success('编辑配件成功')
    }
    dialogVisible.value = false
    handleSearch()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleStockInSubmit = async () => {
  if (!stockInFormRef.value) return
  
  try {
    await stockInFormRef.value.validate()
    ElMessage.success('入库成功')
    stockInDialogVisible.value = false
    handleSearch()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleStockOutSubmit = async () => {
  if (!stockOutFormRef.value) return
  
  try {
    await stockOutFormRef.value.validate()
    ElMessage.success('出库成功')
    stockOutDialogVisible.value = false
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

// 生命周期
onMounted(() => {
  handleSearch()
})
</script>

<style scoped lang="scss">
.accessories-container {
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

// 库存状态样式
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