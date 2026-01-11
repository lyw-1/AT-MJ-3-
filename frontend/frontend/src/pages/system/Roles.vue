<template>
  <div class="roles-container">
    <!-- 页面标题和操作区 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">角色管理</h1>
        <p class="page-subtitle">管理系统角色和权限配置</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
      </div>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="角色名称">
          <el-input v-model="filterForm.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="filterForm.roleCode" placeholder="请输入角色编码" clearable />
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

    <!-- 角色列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>角色列表</h3>
      </div>
      
      <el-table
        v-loading="loading"
        :data="rolesList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="roleName" label="角色名称" width="120" show-overflow-tooltip />
        <el-table-column prop="roleCode" label="角色编码" width="120" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="userCount" label="关联用户数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="updateTime" label="更新时间" width="160" />
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
      width="600px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限配置">
          <el-checkbox-group v-model="roleForm.permissions" style="display: flex; flex-wrap: wrap; gap: 16px;">
            <el-checkbox 
              v-for="permission in availablePermissions" 
              :key="permission.code" 
              :label="permission.code"
              style="width: calc(50% - 8px)"
            >
              <div class="permission-item">
                <div class="permission-name">{{ permission.name }}</div>
                <div class="permission-description">{{ permission.description }}</div>
              </div>
            </el-checkbox>
          </el-checkbox-group>
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

// 定义事件，用于通知父组件数据变化
const emit = defineEmits(['update'])
import type { FormInstance } from 'element-plus'
import {
  Plus,
  Search,
  Refresh,
  Edit,
  Delete
} from '@element-plus/icons-vue'
// 导入角色API服务
import {
  getAllRoles,
  createRole,
  updateRole,
  deleteRole,
  checkRoleCode,
  type Role
} from '@/api/system/role'

interface Permission {
  id: string
  name: string
  code: string
  description: string
}

// 使用API服务定义的Role类型
interface FilterForm {
  roleName: string
  roleCode: string
}

interface RoleForm {
  id?: number
  roleName: string
  roleCode: string
  description: string
  status: string
  permissions: string[]
}

// 响应式数据
const loading = ref(false)
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const roleFormRef = ref<FormInstance>()

// 筛选条件
const filterForm = reactive<FilterForm>({
  roleName: '',
  roleCode: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 可用权限列表
const availablePermissions = ref<Permission[]>([
  { id: '1', name: '查看用户', code: 'user_view', description: '查看系统用户列表' },
  { id: '2', name: '编辑用户', code: 'user_edit', description: '编辑系统用户信息' },
  { id: '3', name: '查看模具', code: 'mold_view', description: '查看模具信息' },
  { id: '4', name: '编辑模具', code: 'mold_edit', description: '编辑模具信息' },
  { id: '5', name: '工序管理', code: 'process_manage', description: '管理加工工序' },
  { id: '6', name: '查看报表', code: 'report_view', description: '查看系统报表' },
  { id: '7', name: '角色管理', code: 'role_manage', description: '管理系统角色' },
  { id: '8', name: '部门管理', code: 'department_manage', description: '管理系统部门' }
])

// 表单数据
const roleForm = reactive<RoleForm>({
  roleName: '',
  roleCode: '',
  description: '',
  status: '1',
  permissions: []
})

// 表单验证规则
const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度为2-20个字符', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { min: 2, max: 20, message: '角色编码长度为2-20个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '角色编码只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 角色列表数据
const rolesList = ref<Role[]>([])

// 计算属性
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增角色' : '编辑角色'
})

// 方法
const handleSearch = async (showSuccessMessage: boolean = true) => {
  loading.value = true
  try {
    const roles = await getAllRoles() || []
    rolesList.value = roles
    pagination.total = rolesList.value.length
    if (showSuccessMessage) {
      ElMessage.success('搜索完成')
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  Object.assign(filterForm, {
    roleName: '',
    roleCode: ''
  })
  handleSearch()
}

const handleSelectionChange = (selection: Role[]) => {
  selectedIds.value = selection.map(item => item.id!)
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(roleForm, {
    roleName: '',
    roleCode: '',
    description: '',
    status: '1',
    permissions: []
  })
  dialogVisible.value = true
}

const handleEdit = (row: Role) => {
  dialogType.value = 'edit'
  Object.assign(roleForm, {
    id: row.id,
    roleName: row.roleName,
    roleCode: row.roleCode,
    description: row.description || '',
    status: row.status.toString(),
    permissions: [] // 权限需要从其他接口获取，这里简化处理
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Role) => {
  try {
    if (!row.id) return
    await ElMessageBox.confirm(
      `确定要删除角色 "${row.roleName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 调用API删除角色
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    handleSearch()
    // 触发角色数据更新事件
    emit('update')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除角色失败:', error)
      ElMessage.error('删除角色失败')
    }
    // 用户取消操作
  }
}

const handleSubmit = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    
    // 检查角色编码是否存在
    const codeExists = await checkRoleCode(roleForm.roleCode, dialogType.value === 'edit' && roleForm.id ? roleForm.id : undefined)
    if (codeExists.data.exists) {
      ElMessage.error('角色编码已存在')
      return
    }
    
    if (dialogType.value === 'add') {
      // 调用API创建角色
      const newRole: Role = {
        roleName: roleForm.roleName,
        roleCode: roleForm.roleCode,
        description: roleForm.description,
        status: parseInt(roleForm.status)
      }
      await createRole(newRole)
      ElMessage.success('新增角色成功')
    } else {
      // 调用API更新角色
      if (!roleForm.id) return
      const updatedRole: Partial<Role> = {
        roleName: roleForm.roleName,
        roleCode: roleForm.roleCode,
        description: roleForm.description,
        status: parseInt(roleForm.status)
      }
      await updateRole(roleForm.id, updatedRole)
      ElMessage.success('编辑角色成功')
    }
    
    dialogVisible.value = false
    handleSearch()
    // 触发角色数据更新事件
    emit('update')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(dialogType.value === 'add' ? '新增角色失败' : '编辑角色失败')
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
onMounted(() => {
  handleSearch(false) // 页面加载时不显示"搜索完成"提示
})
</script>

<style scoped lang="scss">
.roles-container {
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

// 权限选择样式
.permission-item {
  .permission-name {
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .permission-description {
    font-size: 12px;
    color: #909399;
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
  
  .permission-item {
    width: 100%;
  }
}
</style>
