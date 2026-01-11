<template>
  <div class="users-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">用户管理</h1>
        <p class="page-subtitle">管理系统用户、角色和部门</p>
      </div>
    </div>

    <!-- 标签式导航栏 -->
    <el-card class="tabs-card">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="用户列表" name="users">
          <div class="tab-actions">
            <el-button type="primary" @click="handleAdd" v-permission="['admin', 'manager']">
              <el-icon><Plus /></el-icon>
              新增用户
            </el-button>
            <el-button @click="handleImport" v-permission="['admin', 'manager']">
              <el-icon><Upload /></el-icon>
              批量导入
            </el-button>
            <el-button @click="handleExport" v-permission="['admin', 'manager']">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
          </div>

    <!-- 用户统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stats-card total">
            <div class="card-icon">
              <el-icon size="32" color="#409eff"><User /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ stats.total }}</div>
              <div class="card-label">总用户数</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stats-card active">
            <div class="card-icon">
              <el-icon size="32" color="#67c23a"><CircleCheck /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ stats.active }}</div>
              <div class="card-label">活跃用户</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stats-card inactive">
            <div class="card-icon">
              <el-icon size="32" color="#e6a23c"><CircleClose /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ stats.inactive }}</div>
              <div class="card-label">禁用用户</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stats-card online">
            <div class="card-icon">
              <el-icon size="32" color="#409eff"><Connection /></el-icon>
            </div>
            <div class="card-content">
              <div class="card-number">{{ stats.online }}</div>
              <div class="card-label">在线用户</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="用户名">
          <el-input v-model="filterForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="filterForm.realName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="filterForm.department" placeholder="请选择部门" clearable>
            <el-option 
              v-for="dept in departments" 
              :key="dept.id" 
              :label="dept.name" 
              :value="dept.name" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="filterForm.role" placeholder="请选择角色" clearable>
            <el-option 
              v-for="role in roles" 
              :key="role.id" 
              :label="role.roleName" 
              :value="role.roleName" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="启用" />
            <el-option label="禁用" value="禁用" />
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

    <!-- 用户列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <h3>用户列表</h3>
        <div class="table-actions" v-permission="['admin', 'manager']">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDisable">
            <el-icon><CircleClose /></el-icon>
            批量禁用
          </el-button>
          <el-button type="success" :disabled="selectedIds.length === 0" @click="handleBatchEnable">
            <el-icon><CircleCheck /></el-icon>
            批量启用
          </el-button>
          <el-button type="warning" :disabled="selectedIds.length === 0" @click="handleBatchReset">
            <el-icon><Key /></el-icon>
            重置密码
          </el-button>
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="usersList"
        @selection-change="handleSelectionChange"
        @filter-change="handleFilterChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="username" label="用户名" width="120" fixed 
          filterable
          :filter-method="(value, row) => row.username?.includes(value) || false"
          show-overflow-tooltip
        />
        <el-table-column prop="realName" label="姓名" width="100" 
          filterable
          :filter-method="(value, row) => row.realName?.includes(value) || false"
        />
        <el-table-column prop="department" label="部门" width="100" align="center" 
          filterable
          :filters="departmentFilters"
          :filter-method="(value, row) => row.department === value"
        />
        <el-table-column prop="role" label="角色" width="100" align="center" 
          filterable
          :filters="roleFilters"
          :filter-method="(value, row) => row.role === value"
        />
        <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip 
          filterable
          :filter-method="(value, row) => row.email?.includes(value) || false"
        />
        <el-table-column prop="phone" label="电话" width="120" 
          filterable
          :filter-method="(value, row) => row.phone?.includes(value) || false"
        />
        <el-table-column prop="status" label="状态" width="80" align="center"
          filterable
          :filters="statusFilters"
          :filter-method="(value, row) => row.status === value"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === '启用' ? 'success' : 'danger'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" width="160" 
          filterable
          :filter-method="(value, row) => row.lastLogin?.includes(value) || false"
        />
        <el-table-column prop="createTime" label="创建时间" width="160" 
          filterable
          :filter-method="(value, row) => row.createTime?.includes(value) || false"
        />
        <el-table-column label="操作" width="300" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-permission="['admin', 'manager']">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              :type="row.status === '启用' ? 'danger' : 'success'" 
              link 
              @click="handleToggleStatus(row)"
              v-permission="['admin', 'manager']"
            >
              <el-icon><CircleClose v-if="row.status === '启用'" /><CircleCheck v-else /></el-icon>
              {{ row.status === '启用' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="warning" link @click="handleResetPassword(row)" v-permission="['admin', 'manager']">
              <el-icon><Key /></el-icon>
              重置密码
            </el-button>
            <el-button type="info" link @click="handleOpenPasswordDialog(row.id, true)" v-permission="['admin', 'manager']">
              <el-icon><Key /></el-icon>
              修改密码
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-permission="['admin', 'manager']">
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
      center
      append-to-body
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="120px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item v-if="dialogType === 'add'" label="初始密码" prop="password">
          <el-input v-model="userForm.password" placeholder="请输入初始密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select 
            v-model="userForm.department" 
            placeholder="请选择部门" 
            style="width: 100%"
            v-loading="departmentsLoading"
            :popper-class="'user-select-popper'"
            :popper-options="popperOptions"
            @visible-change="handleDropdownVisibleChange"
          >
            <el-option 
              v-for="dept in departments" 
              :key="dept.id" 
              :label="dept.name" 
              :value="dept.name" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select 
            v-model="userForm.role" 
            placeholder="请选择角色" 
            style="width: 100%"
            v-loading="rolesLoading"
            :popper-class="'user-select-popper'"
            :popper-options="popperOptions"
            @visible-change="handleDropdownVisibleChange"
          >
            <el-option 
              v-for="roleItem in roles" 
              :key="roleItem.id" 
              :label="roleItem.roleName" 
              :value="roleItem.roleName" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="启用">启用</el-radio>
            <el-radio label="禁用">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="userForm.remarks" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="passwordDialogVisible"
      :title="isAdminEdit ? '修改用户密码' : '修改密码'"
      width="500px"
      :close-on-click-modal="false"
      center
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="120px"
      >
        <el-form-item v-if="!isAdminEdit" label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handlePasswordSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-tab-pane>
  <!-- 角色管理标签页 -->
  <el-tab-pane label="角色管理" name="roles">
    <Roles @update="loadRoles" />
  </el-tab-pane>
  <!-- 部门管理标签页 -->
  <el-tab-pane label="部门管理" name="departments">
    <Departments @update="loadDepartments" />
  </el-tab-pane>
</el-tabs>
</el-card>
</div>
</template>

<script setup lang="ts">
// 导入角色和部门管理组件
import Roles from './Roles.vue'
import Departments from './Departments.vue'
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  Plus,
  Search,
  Refresh,
  Edit,
  Delete,
  CircleCheck,
  CircleClose,
  Key,
  Connection,
  Upload,
  Download,
  User
} from '@element-plus/icons-vue'
// 导入用户API服务
import {
  getAllUsers,
  createUser,
  updateUser,
  deleteUser,
  resetUserPassword,
  changeUserPassword,
  batchAssignRolesToUser,
  type UserResponse,
  type UserCreateRequest,
  type UserUpdateRequest
} from '@/api/system/user'

// 导入部门API服务
import {
  getDepartments,
  type Department
} from '@/api/system/department'

// 导入角色API服务
import {
  getAllRoles,
  type Role
} from '@/api/system/role'

// 使用API服务定义的类型
interface FilterForm {
  username: string
  realName: string
  department: string
  role: string
  status: string
}

interface UserForm {
  id?: number
  username: string
  password: string
  realName: string
  department: string
  role: string
  email: string
  phone: string
  status: string
  remarks?: string
}

interface UserStats {
  total: number
  active: number
  inactive: number
  online: number
}

// 响应式数据
const loading = ref(false)
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const userFormRef = ref<FormInstance>()
// 标签切换相关
const activeTab = ref('users')

// 下拉菜单popper选项配置
const popperOptions = {
  modifiers: [
    {
      name: 'preventOverflow',
      options: {
        padding: 10
      }
    },
    {
      name: 'flip',
      options: {
        fallbackPlacements: ['top', 'bottom', 'left', 'right'],
        padding: 5
      }
    },
    {
      name: 'computeStyles',
      options: {
        adaptive: true
      }
    }
  ]
}

// 部门和角色数据
const departments = ref<Department[]>([])
const roles = ref<Role[]>([])
const departmentsLoading = ref(false)
const rolesLoading = ref(false)

// 修改密码对话框相关数据
const passwordDialogVisible = ref(false)
const currentUserId = ref<number | undefined>(undefined)
const isAdminEdit = ref(false) // 是否是管理员直接修改密码（无需原密码）

interface PasswordForm {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

const passwordForm = reactive<PasswordForm>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: !isAdminEdit.value, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度为8-20个字符', trigger: 'blur' },
    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/, message: '密码必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: (rule: any, value: string, callback: any) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, message: '两次输入的密码不一致', trigger: 'blur' }
  ]
}

const passwordFormRef = ref<FormInstance>()

// 处理标签切换
const handleTabChange = (tabName: string) => {
  activeTab.value = tabName
  console.log('切换到标签:', tabName)
}

// 筛选条件
const filterForm = reactive<FilterForm>({
  username: '',
  realName: '',
  department: '',
  role: '',
  status: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 统计数据 - 从用户列表动态计算
const stats = computed<UserStats>(() => {
  const total = usersList.value.length
  const active = usersList.value.filter(user => user.status === 1 || user.status === '启用').length
  const inactive = usersList.value.filter(user => user.status === 0 || user.status === '禁用').length
  // 在线用户需要后端支持，暂时统计为0
  const online = 0
  return {
    total,
    active,
    inactive,
    online
  }
})

// 筛选选项
const departmentFilters = [
  { text: '生产部', value: '生产部' },
  { text: '技术部', value: '技术部' },
  { text: '质量部', value: '质量部' },
  { text: '设备部', value: '设备部' },
  { text: '仓储部', value: '仓储部' },
  { text: '行政部', value: '行政部' },
  { text: '财务部', value: '财务部' }
]

const roleFilters = [
  { text: '管理员', value: '管理员' },
  { text: '部门经理', value: '部门经理' },
  { text: '班组长', value: '班组长' },
  { text: '操作员', value: '操作员' },
  { text: '质检员', value: '质检员' },
  { text: '维修员', value: '维修员' }
]

const statusFilters = [
  { text: '启用', value: '启用' },
  { text: '禁用', value: '禁用' }
]

const userForm = reactive<UserForm>({
  username: '',
  password: '',
  realName: '',
  department: '',
  role: '',
  email: '',
  phone: '',
  status: '启用',
  remarks: ''
})

const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  department: [{ required: true, message: '请选择部门', trigger: 'change' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  email: [
    { required: false, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: false, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  password: [
    { required: false, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度为8-20个字符', trigger: 'blur' },
    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/, message: '密码必须包含字母和数字', trigger: 'blur' }
  ]
}

// 用户列表数据
const usersList = ref<UserResponse[]>([])

// 计算属性
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增用户' : '编辑用户'
})

// 方法
const handleSearch = async (showSuccessMessage: boolean = true) => {
  loading.value = true
  try {
    const users = await getAllUsers() || []
    
    // 确保角色列表已加载
    if (roles.value.length === 0) {
      await loadRoles()
    }
    
    // 为每个用户添加role字段，将角色代码映射为角色名称
    usersList.value = users.map(user => {
      // 获取角色代码
      const roleCode = user.roles && user.roles.length > 0 ? user.roles[0] : ''
      
      // 查找对应的角色名称
      const roleItem = roles.value.find(role => role.roleCode === roleCode)
      
      // 使用角色名称，如果找不到则使用原始代码
      const roleName = roleItem ? roleItem.roleName : roleCode
      
      return {
        ...user,
        role: roleName, // 使用映射后的角色名称
        department: user.departmentName // 同时处理department字段，确保初始加载时也能正确显示
      }
    })
    
    pagination.total = usersList.value.length
    if (showSuccessMessage) {
      ElMessage.success('搜索完成')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 加载部门数据
const loadDepartments = async () => {
  departmentsLoading.value = true
  try {
    const depts = await getDepartments() || []
    departments.value = depts
  } catch (error) {
    console.error('加载部门失败:', error)
    ElMessage.error('加载部门失败，请重试')
  } finally {
    departmentsLoading.value = false
  }
}

// 加载角色数据
const loadRoles = async () => {
  rolesLoading.value = true
  try {
    const roleList = await getAllRoles() || []
    roles.value = roleList
  } catch (error) {
    console.error('加载角色失败:', error)
    ElMessage.error('加载角色失败，请重试')
  } finally {
    rolesLoading.value = false
  }
}

// 加载所有数据
const loadAllData = async () => {
  await Promise.all([loadDepartments(), loadRoles()])
}

// 打开修改密码对话框
const handleOpenPasswordDialog = (userId: number, isAdmin: boolean = false) => {
  currentUserId.value = userId
  isAdminEdit.value = isAdmin
  // 重置表单
  Object.assign(passwordForm, {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  })
  passwordDialogVisible.value = true
}

// 提交修改密码表单
const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value || !currentUserId.value) return
  
  try {
    await passwordFormRef.value.validate()
    
    // 调用API修改密码
    // 管理员修改：直接重置密码
    // 用户自行修改：需要原密码验证
    if (isAdminEdit.value) {
      // 管理员直接修改密码
      await resetUserPassword(currentUserId.value, passwordForm.newPassword)
      ElMessage.success('密码修改成功')
    } else {
      // 用户自行修改密码，需要原密码验证
      await changeUserPassword(currentUserId.value, passwordForm.oldPassword, passwordForm.newPassword)
      ElMessage.success('密码修改成功')
    }
    
    passwordDialogVisible.value = false
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('修改密码失败')
  }
}

// 处理筛选变化
const handleFilterChange = (filters: any) => {
  // 保存筛选状态
  Object.keys(filters).forEach(key => {
    if (filters[key] && filters[key].length > 0) {
      filterForm[key as keyof FilterForm] = filters[key][0]
    } else {
      filterForm[key as keyof FilterForm] = ''
    }
  })
  
  // 重置页码
  pagination.currentPage = 1
  
  // 触发搜索
  handleSearch()
}

const handleReset = () => {
  Object.assign(filterForm, {
    username: '',
    realName: '',
    department: '',
    role: '',
    status: ''
  })
  handleSearch()
}

const handleSelectionChange = (selection: UserResponse[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleAdd = async () => {
  dialogType.value = 'add'
  Object.assign(userForm, {
    username: '',
    password: '',
    realName: '',
    department: '',
    role: '',
    email: '',
    phone: '',
    status: '启用',
    remarks: ''
  })
  // 加载部门和角色数据
  await loadAllData()
  dialogVisible.value = true
}

const handleEdit = async (row: UserResponse) => {
  dialogType.value = 'edit'
  Object.assign(userForm, {
    id: row.id,
    username: row.username,
    password: '', // 编辑用户时不设置密码
    realName: row.realName,
    department: row.departmentName || '',
    role: row.roles[0] || '',
    email: row.email || '',
    phone: row.phone,
    status: row.status === 1 ? '启用' : '禁用',
    remarks: row.remarks || ''
  })
  // 加载部门和角色数据
  await loadAllData()
  dialogVisible.value = true
}

const handleToggleStatus = async (row: UserResponse) => {
  const action = row.status === 1 ? '禁用' : '启用'
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await ElMessageBox.confirm(
      `确定要${action}用户 "${row.realName}" 吗？`,
      `${action}确认`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 调用API更新状态
    await updateUser(row.id, { status: newStatus })
    // 更新本地数据
    row.status = newStatus
    ElMessage.success(`${action}成功`)
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${action}用户失败:`, error)
      ElMessage.error(`${action}用户失败`)
    }
    // 用户取消操作
  }
}

const handleDelete = async (row: UserResponse) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.realName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 调用API删除用户
    await deleteUser(row.id)
    // 更新本地数据
    usersList.value = usersList.value.filter(user => user.id !== row.id)
    ElMessage.success('删除用户成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error('删除用户失败')
    }
    // 用户取消操作
  }
}

const handleResetPassword = async (row: UserResponse) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${row.realName}" 的密码吗？\n重置后密码为：123456`,
      '重置密码确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 调用API重置密码
    await resetUserPassword(row.id, '123456')
    ElMessage.success('密码重置成功，新密码为：123456')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重置密码失败:', error)
      ElMessage.error('重置密码失败')
    }
    // 用户取消操作
  }
}

const handleBatchEnable = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要启用的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要启用选中的 ${selectedIds.value.length} 个用户吗？`,
      '批量启用确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 批量启用用户
    const promises = selectedIds.value.map(id => updateUser(id, { status: 1 }))
    await Promise.all(promises)
    // 更新本地数据
    usersList.value.forEach(user => {
      if (selectedIds.value.includes(user.id)) {
        user.status = 1
      }
    })
    ElMessage.success('批量启用成功')
    selectedIds.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量启用失败:', error)
      ElMessage.error('批量启用失败')
    }
    // 用户取消操作
  }
}

const handleBatchDisable = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要禁用的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要禁用选中的 ${selectedIds.value.length} 个用户吗？`,
      '批量禁用确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 批量禁用用户
    const promises = selectedIds.value.map(id => updateUser(id, { status: 0 }))
    await Promise.all(promises)
    // 更新本地数据
    usersList.value.forEach(user => {
      if (selectedIds.value.includes(user.id)) {
        user.status = 0
      }
    })
    ElMessage.success('批量禁用成功')
    selectedIds.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量禁用失败:', error)
      ElMessage.error('批量禁用失败')
    }
    // 用户取消操作
  }
}

const handleBatchReset = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要重置密码的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要重置选中的 ${selectedIds.value.length} 个用户的密码吗？\n重置后密码为：123456`,
      '批量重置密码确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    // 批量重置密码
    const promises = selectedIds.value.map(id => resetUserPassword(id, '123456'))
    await Promise.all(promises)
    ElMessage.success('批量重置密码成功')
    selectedIds.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量重置密码失败:', error)
      ElMessage.error('批量重置密码失败')
    }
    // 用户取消操作
  }
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个用户吗？\n此操作不可恢复，请谨慎操作！\n注意：如果用户有相关业务数据，可能无法删除！`,
      '批量删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger'
      }
    )
    
    let successCount = 0
    let failCount = 0
    let errorMessages: string[] = []
    
    // 逐个删除用户，处理每个用户的删除结果
    for (const id of selectedIds.value) {
      try {
        await deleteUser(id)
        // 删除成功，从列表中移除
        usersList.value = usersList.value.filter(user => user.id !== id)
        successCount++
      } catch (err: any) {
        failCount++
        // 解析错误信息，提取关键内容
        const errorMsg = err.response?.data?.message || err.message || '删除失败'
        // 检查是否为外键约束错误
        if (errorMsg.includes('foreign key constraint')) {
          errorMessages.push(`用户 ${id} 有相关业务数据，无法删除`)
        } else {
          errorMessages.push(`用户 ${id} 删除失败: ${errorMsg}`)
        }
      }
    }
    
    // 显示删除结果
    let message = ''
    if (successCount > 0) {
      message += `成功删除 ${successCount} 个用户`
    }
    if (failCount > 0) {
      message += `，${failCount} 个用户删除失败`
      // 显示失败原因
      errorMessages.forEach(msg => {
        ElMessage.warning(msg)
      })
    }
    
    if (message) {
      if (successCount > 0) {
        ElMessage.success(message)
      } else {
        ElMessage.error(message)
      }
    }
    
    selectedIds.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
    // 用户取消操作
  }
}

const handleImport = () => {
  ElMessage.info('批量导入功能')
}

const handleExport = () => {
  ElMessage.info('导出用户数据')
}

const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    if (dialogType.value === 'add') {
      // 调用API创建用户
      const requestData: UserCreateRequest = {
        username: userForm.username,
        password: userForm.password || '123456', // 使用管理员输入的密码，默认为123456
        realName: userForm.realName,
        phone: userForm.phone,
        status: userForm.status === '启用' ? 1 : 0,
        email: userForm.email,
        department: userForm.department
      }
      // 调用API创建用户，获取返回结果
      const response = await createUser(requestData)
      const newUserId = response.data.id
      
      // 分配角色
      // 查找选中角色的ID
      const selectedRole = roles.value.find(role => role.roleName === userForm.role)
      if (selectedRole && selectedRole.id) {
        // 调用角色分配API，将角色ID数组传递给后端
        await batchAssignRolesToUser(newUserId, [selectedRole.id])
      }
      
      ElMessage.success('新增用户成功')
      
      dialogVisible.value = false
      handleSearch() // 新增用户仍需刷新列表
    } else {
      // 调用API更新用户
        if (!userForm.id) return
        const requestData: UserUpdateRequest = {
          realName: userForm.realName,
          phone: userForm.phone,
          status: userForm.status === '启用' ? 1 : 0,
          email: userForm.email,
          departmentName: userForm.department
        }
      // 1. 更新用户基本信息
      await updateUser(userForm.id, requestData)
      
      // 2. 更新角色信息
      // 查找选中角色的ID
      const selectedRole = roles.value.find(role => role.roleName === userForm.role)
      if (selectedRole) {
        // 调用角色更新API，将角色ID数组传递给后端
        await batchAssignRolesToUser(userForm.id, [selectedRole.id!])
      }
      
      ElMessage.success('编辑用户成功')
      
      dialogVisible.value = false
      
      // 3. 调用handleSearch刷新列表数据，确保数据一致性
      handleSearch(false)
    }
  } catch (error: any) {
    console.error('提交失败:', error)
    // 提供更详细的错误信息
    const errorMessage = error.response?.data?.message || error.message || (dialogType.value === 'add' ? '新增用户失败' : '编辑用户失败')
    ElMessage.error(errorMessage)
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
  // 先加载角色和部门数据，再加载用户数据
  await loadAllData()
  await handleSearch(false) // 页面加载时不显示"搜索完成"提示
})
</script>

<style scoped lang="scss">
.users-container {
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

// 统计卡片
.stats-cards {
  margin-bottom: 20px;
  
  .stats-card {
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
    
    &.total {
      border-left: 4px solid #409eff;
    }
    
    &.active {
      border-left: 4px solid #67c23a;
    }
    
    &.inactive {
      border-left: 4px solid #f56c6c;
    }
    
    &.online {
      border-left: 4px solid #e6a23c;
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
</style>