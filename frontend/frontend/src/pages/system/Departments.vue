<template>
  <div class="departments-container">
    <!-- 页面标题和操作区 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">部门管理</h1>
        <p class="page-subtitle">管理系统部门结构和配置</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增部门
        </el-button>
      </div>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="部门名称">
          <el-input v-model="filterForm.name" placeholder="请输入部门名称" clearable />
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

    <!-- 部门列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>部门列表</h3>
        <div class="table-actions">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="departmentsList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="name" label="部门名称" width="150" show-overflow-tooltip />
        <el-table-column prop="userCount" label="关联用户数" width="120" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="110" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" size="small">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)" size="small">
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
      width="500px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form
        ref="departmentFormRef"
        :model="departmentForm"
        :rules="departmentRules"
        label-width="100px"
      >
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="departmentForm.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-select
            v-model="departmentForm.parentId"
            placeholder="请选择上级部门"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
              :disabled="departmentForm.id && dept.id === departmentForm.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number
            v-model="departmentForm.sortOrder"
            :min="0"
            :max="999"
            :step="1"
            placeholder="请输入排序值"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="departmentForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
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
  Delete
} from '@element-plus/icons-vue'

// 定义事件，用于通知父组件数据变化
const emit = defineEmits(['update'])
// 导入部门API服务
import {
  getDepartments,
  getDepartmentTree,
  createDepartment,
  updateDepartment,
  deleteDepartment,
  checkDepartmentName,
  checkDepartmentHasChildren,
  type Department
} from '@/api/system/department'

interface FilterForm {
  name: string
}

interface DepartmentForm {
  id?: number
  name: string
  parentId?: number
  sortOrder?: number
  status: string
}

// 响应式数据
const loading = ref(false)
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const departmentFormRef = ref<FormInstance>()

// 筛选条件
const filterForm = reactive<FilterForm>({
  name: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 部门选项（用于下拉选择）
const departmentOptions = ref<Department[]>([])

// 表单数据
const departmentForm = reactive<DepartmentForm>({
  name: '',
  parentId: undefined,
  sortOrder: 0,
  status: '1'
})

// 表单验证规则
const departmentRules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 20, message: '部门名称长度为2-20个字符', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 部门列表数据
const departmentsList = ref<Department[]>([])

// 计算属性
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增部门' : '编辑部门'
})

// 方法
const handleSearch = async (showMessage: boolean = true) => {
  loading.value = true
  try {
    // 使用部门树API获取数据
    const depts = await getDepartmentTree(filterForm.name) || []
    departmentsList.value = depts
    pagination.total = departmentsList.value.length
    if (showMessage) {
      ElMessage.success(`搜索完成，找到 ${departmentsList.value.length} 个部门`)
    }
  } catch (error) {
    console.error('获取部门列表失败:', error)
    ElMessage.error('获取部门列表失败')
  } finally {
    loading.value = false
  }
}

// 构建部门列表（带有层级信息）
const buildDepartmentList = (departments: Department[], level: number = 0): Department[] => {
  const result: Department[] = []
  
  const processDepartment = (dept: Department, currentLevel: number) => {
    const newDept = { ...dept, level: currentLevel }
    result.push(newDept)
    
    if (dept.children && dept.children.length > 0) {
      dept.children.forEach(child => {
        processDepartment(child, currentLevel + 1)
      })
    }
  }
  
  departments.forEach(dept => {
    processDepartment(dept, level)
  })
  
  return result
}

const handleReset = () => {
  Object.assign(filterForm, {
    name: ''
  })
  handleSearch()
}

const handleSelectionChange = (selection: Department[]) => {
  selectedIds.value = selection.map(item => item.id!)
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(departmentForm, {
    name: '',
    parentId: undefined,
    sortOrder: 0,
    status: '1'
  })
  dialogVisible.value = true
}

const handleEdit = (row: Department) => {
  dialogType.value = 'edit'
  Object.assign(departmentForm, {
    id: row.id,
    name: row.name,
    parentId: row.parentId,
    sortOrder: row.sortOrder || 0,
    status: row.status.toString()
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Department) => {
  try {
    if (!row.id) return
    
    // 检查部门是否有子部门
    const hasChildrenResult = await checkDepartmentHasChildren(row.id)
    if (hasChildrenResult.data.hasChildren) {
      ElMessage.error('该部门存在子部门，无法删除')
      return
    }
    
    await ElMessageBox.confirm(
      `确定要删除部门 "${row.name}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 调用API删除部门
    await deleteDepartment(row.id)
    ElMessage.success('删除成功')
    handleSearch(false)
    // 触发部门数据更新事件
    emit('update')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除部门失败:', error)
      ElMessage.error('删除部门失败')
    }
    // 用户取消操作
  }
}

const handleBatchDelete = async () => {
  try {
    if (selectedIds.value.length === 0) {
      ElMessage.warning('请选择要删除的部门')
      return
    }
    
    // 检查所有选中部门是否有子部门
    for (const id of selectedIds.value) {
      const hasChildrenResult = await checkDepartmentHasChildren(id)
      if (hasChildrenResult.data.hasChildren) {
        ElMessage.error('部分部门存在子部门，无法删除')
        return
      }
    }
    
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个部门吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 批量删除部门
    const promises = selectedIds.value.map(id => deleteDepartment(id))
    await Promise.all(promises)
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    handleSearch(false)
    // 触发部门数据更新事件
    emit('update')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除部门失败:', error)
      ElMessage.error('批量删除部门失败')
    }
    // 用户取消操作
  }
}

const handleSubmit = async () => {
  if (!departmentFormRef.value) return
  
  try {
    await departmentFormRef.value.validate()
    
    // 检查部门名称是否存在
    const nameExists = await checkDepartmentName(departmentForm.name, dialogType.value === 'edit' && departmentForm.id ? departmentForm.id : undefined)
    if (nameExists.data.exists) {
      ElMessage.error('部门名称已存在')
      return
    }
    
    if (dialogType.value === 'add') {
      // 调用API创建部门
      const newDepartment: Department = {
        name: departmentForm.name,
        parentId: departmentForm.parentId,
        sortOrder: departmentForm.sortOrder,
        status: parseInt(departmentForm.status)
      }
      await createDepartment(newDepartment)
      ElMessage.success('新增部门成功')
    } else {
      // 调用API更新部门
      if (!departmentForm.id) return
      const updatedDepartment: Partial<Department> = {
        name: departmentForm.name,
        parentId: departmentForm.parentId,
        sortOrder: departmentForm.sortOrder,
        status: parseInt(departmentForm.status)
      }
      await updateDepartment(departmentForm.id, updatedDepartment)
      ElMessage.success('编辑部门成功')
    }
    
    dialogVisible.value = false
    handleSearch(false)
    // 触发部门数据更新事件
    emit('update')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(dialogType.value === 'add' ? '新增部门失败' : '编辑部门失败')
  }
}

const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  handleSearch(false)
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  handleSearch(false)
}

// 生命周期
onMounted(async () => {
  // 初始加载时执行搜索，获取部门列表
  handleSearch(false) // 不显示搜索提示
  
  // 获取部门选项用于下拉选择
  try {
    const depts = await getDepartments() || []
    departmentOptions.value = depts
  } catch (error) {
    console.error('获取部门选项失败:', error)
  }
})
</script>

<style scoped lang="scss">
.departments-container {
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
}
</style>