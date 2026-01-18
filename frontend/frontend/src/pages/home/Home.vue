<template>
  <div class="home-container">
    <!-- 欢迎语模块 -->
    <div class="welcome-container">
      <div class="welcome-left-section">
        <div class="welcome-header">
          <div class="welcome-left">
            <div class="user-avatar">
              <el-icon class="avatar-icon"><User /></el-icon>
            </div>
            <div class="welcome-info">
              <h1 class="welcome-title">{{ welcomeMessage }}</h1>
              <div class="user-role">
              {{ (userInfo?.roles && userInfo.roles.length > 0) ? userInfo.roles[0].name : '普通用户' }}
            </div>
            </div>
          </div>
          <div class="welcome-right">
            <div class="welcome-date">{{ currentDate }}</div>
          </div>
        </div>
        <div class="today-tasks">
          <div class="today-tasks-header">
            <div class="today-tasks-label">今天的工作：</div>
            <div class="today-tasks-count">
              <span class="count-badge">{{ todayTodos.length }}</span>
            </div>
          </div>
          <div class="today-tasks-list">
            <span class="today-task-item" v-for="todo in todayTodos" :key="todo.id">
              <el-icon class="task-icon"><Warning /></el-icon>
              <span class="task-title">{{ todo.title }}</span>
              <span class="task-priority" :class="todo.priority.toLowerCase()">
                {{ todo.priority }}
              </span>
            </span>
            <span v-if="todayTodos.length === 0" class="today-task-item no-tasks">
              <el-icon class="task-icon"><CircleCheck /></el-icon>
              今天暂无待办事项
            </span>
          </div>
        </div>
      </div>
      <div class="welcome-right-section">
        <!-- 待办事项列表 -->
        <div class="bottom-section todo-section">
          <div class="section-header">
            <h3 class="section-title">待办事项列表</h3>
            <div class="section-actions">
              <button class="el-button el-button--small el-button--default">查看全部</button>
            </div>
          </div>
          <div class="section-content">
            <div class="todo-item" v-for="todo in todoList" :key="todo.id">
              <div class="todo-info">
                <div class="todo-title">{{ todo.title }}</div>
                <div class="todo-meta">{{ todo.dueDate }} · {{ todo.priority }}</div>
              </div>
              <div class="todo-status" :class="todo.status">{{ todo.statusText }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计数据 - Circle Soft风格 -->
    <el-row :gutter="15" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="stat in statistics" :key="stat.key">
        <div class="stats-card" :style="{ background: stat.gradient }">
          <div class="stats-value">{{ stat.value }}</div>
          <div class="stats-label">
            {{ stat.label }}
            <div v-if="stat.year" class="stats-year">{{ stat.year }}</div>
          </div>
          <div 
            v-if="stat.trend !== undefined" 
            class="stats-change" 
            :class="stat.trend > 0 ? 'positive' : 'negative'"
          >
            {{ Math.abs(stat.trend) }}%
          </div>
        </div>
      </el-col>
    </el-row>


    <!-- 快捷入口容器 -->
    <div class="quick-access-container">
      <!-- 快捷入口 - 图标墙风格 -->
      <div class="quick-access-wall">
        <div 
          class="quick-access-item" 
          :class="`quick-access-${item.permission}`" 
          @click="handleQuickAccessClick(item)"
          v-for="item in filteredQuickAccessItems" 
          :key="item.title"
        >
          <div class="quick-access-icon">
            <el-icon>
              <Tools v-if="item.icon === 'Tools'" />
              <Box v-else-if="item.icon === 'Box'" />
              <Setting v-else-if="item.icon === 'Setting'" />
              <TrendCharts v-else-if="item.icon === 'TrendCharts'" />
              <CircleCheck v-else-if="item.icon === 'CircleCheck'" />
              <ArrowRight v-else-if="item.icon === 'ArrowRight'" />
              <Bell v-else-if="item.icon === 'Bell'" />
              <Search v-else-if="item.icon === 'Search'" />
              <User v-else-if="item.icon === 'User'" />
              <DataAnalysis v-else-if="item.icon === 'DataAnalysis'" />
              <Document v-else-if="item.icon === 'Document'" />
              <Back v-else-if="item.icon === 'Back'" />
              <DataLine v-else-if="item.icon === 'DataLine'" />
              <Check v-else-if="item.icon === 'Check'" />
              <DocumentAdd v-else-if="item.icon === 'DocumentAdd'" />
              <Edit v-else-if="item.icon === 'Edit'" />
              <Setting v-else />
            </el-icon>
          </div>
          <div class="quick-access-title">{{ item.title }}</div>
        </div>
      </div>
    </div>

    <!-- 底部功能版块容器 -->
    <div class="bottom-sections-container">
      <!-- 模具加工任务列表 -->
      <div class="bottom-section mold-task-section">
        <div class="section-header">
          <h3 class="section-title">模具加工任务列表</h3>
          <div class="section-actions">
            <button class="el-button el-button--small el-button--default">查看全部</button>
          </div>
        </div>
        <div class="section-content">
          <div class="task-item" v-for="task in moldTasks" :key="task.id">
            <div class="task-info">
              <div class="task-title">{{ task.title }}</div>
              <div class="task-meta">{{ task.moldNumber }} · {{ task.progress }}%</div>
            </div>
            <div class="task-progress">
              <el-progress :percentage="task.progress" :stroke-width="6" :show-text="false"></el-progress>
            </div>
          </div>
        </div>
      </div>

      <!-- 非模具加工任务列表 -->
      <div class="bottom-section non-mold-task-section">
        <div class="section-header">
          <h3 class="section-title">非模具加工任务列表</h3>
          <div class="section-actions">
            <button class="el-button el-button--small el-button--default">查看全部</button>
          </div>
        </div>
        <div class="section-content">
          <div class="task-item" v-for="task in nonMoldTasks" :key="task.id">
            <div class="task-info">
              <div class="task-title">{{ task.title }}</div>
              <div class="task-meta">{{ task.type }} · {{ task.progress }}%</div>
            </div>
            <div class="task-progress">
              <el-progress :percentage="task.progress" :stroke-width="6" :show-text="false"></el-progress>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近成型录入记录 -->
      <div class="bottom-section production-data-section">
        <div class="section-header">
          <h3 class="section-title">最近成型录入记录</h3>
          <div class="section-actions">
            <button class="el-button el-button--small el-button--default">查看全部</button>
          </div>
        </div>
        <div class="section-content">
          <div class="data-item" v-for="data in productionData" :key="data.id">
            <div class="data-info">
              <div class="data-title">{{ data.title }}</div>
              <div class="data-meta">{{ data.product }} · {{ data.recordTime }}</div>
            </div>
            <div class="data-status" :class="data.status">{{ data.statusText }}</div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { 
  User, Tools, Box, Setting,
  Warning,
  CircleCheck, TrendCharts,
  ArrowRight, Bell, Search,
  DataAnalysis, Document, Back,
  DataLine, Check, DocumentAdd, Edit
} from '@element-plus/icons-vue'

const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => userStore.userInfo)

// 欢迎消息
const welcomeMessage = computed(() => {
  const hour = new Date().getHours()
  let greeting = ''
  
  if (hour < 6) greeting = '凌晨好'
  else if (hour < 9) greeting = '早上好'
  else if (hour < 12) greeting = '上午好'
  else if (hour < 14) greeting = '中午好'
  else if (hour < 17) greeting = '下午好'
  else if (hour < 19) greeting = '傍晚好'
  else if (hour < 22) greeting = '晚上好'
  else greeting = '夜深了'
  
  return `${greeting}，${userInfo.value?.name || '用户'}`
})

// 当前日期
const currentDate = computed(() => {
  return new Date().toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})



// 快捷入口（按角色管理显示） - 与微信小程序端统一
const quickAccessItems = ref([
  { title: '加工申请', description: '提交非模具加工申请', path: '/mold/application', icon: 'DocumentAdd', permission: 'apply_non_mold' },
  { title: '初始参数', description: '创建模具初始参数', path: '/mold/initial-params-list', icon: 'DataAnalysis', permission: 'mold_initial_params_create' },
  { title: '新模建档', description: '新模入库与建档', path: '/storage/records', icon: 'Document', permission: 'mold_storage_create' },
  { title: '模具领用', description: '模具领用流程', path: '/storage/mold-list?action=use', icon: 'Tools', permission: 'mold_use_apply' },
  { title: '模具归还', description: '模具归还流程', path: '/storage/mold-list?action=return', icon: 'Back', permission: 'mold_return_apply' },
  { title: '耗材领用', description: '耗材/备品领用', path: '/storage/accessories', icon: 'Box', permission: 'consumables_apply' },
  { title: '成型录入', description: '录入生产关键数据', path: '/production/data-entry', icon: 'DataLine', permission: 'production_data_entry' },
  { title: '测量录入', description: '录入测量数据', path: '/storage/measure-input', icon: 'DataLine', permission: 'measure_data_entry' },
  { title: '调模录入', description: '记录调模过程', path: '/tuning/tuning-records', icon: 'Edit', permission: 'tuning_record_entry' },
  { title: '验收记录', description: '记录调模验收', path: '/tuning/acceptance-records', icon: 'Check', permission: 'tuning_acceptance_entry' }
])

const filteredQuickAccessItems = computed(() => {
  const perms = userStore.permissions
  if (!perms || perms.length === 0) return quickAccessItems.value
  return quickAccessItems.value.filter(i => !i.permission || perms.includes(i.permission))
})

// 统计数据（点击进入对应过滤列表，合格率不跳转）
const statistics = ref([
  { 
    key: 'acceptance_rate', 
    label: '模具验收合格率', 
    year: '（2025年之后）',
    value: '98.5%', 
    icon: 'TrendCharts',
    gradient: 'linear-gradient(135deg, #10B981 0%, #34D399 100%)'
  },
  { 
    key: 'bare_storage', 
    label: '裸模库数量', 
    value: '123', 
    icon: 'Box', 
    clickPath: '/storage/mold-list', 
    clickQuery: { store: 'bare' },
    gradient: 'linear-gradient(135deg, #3B82F6 0%, #60A5FA 100%)'
  },
  { 
    key: 'available_storage', 
    label: '可用库数量', 
    value: '456', 
    icon: 'Box', 
    clickPath: '/storage/mold-list', 
    clickQuery: { store: 'available' },
    gradient: 'linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%)'
  },
  { 
    key: 'sealed_storage', 
    label: '封存库数量', 
    value: '89', 
    icon: 'Box', 
    clickPath: '/storage/mold-list', 
    clickQuery: { store: 'sealed' },
    gradient: 'linear-gradient(135deg, #64748B 0%, #94A3B8 100%)'
  }
])





// 快捷入口点击处理函数
const handleQuickAccessClick = (item: any) => {
  // 使用Vue Router进行页面跳转
  window.location.href = item.path
}



// 待办事项模拟数据
const todoList = ref([
  { id: 1, title: '完成模具初始参数设置', dueDate: '2025-12-05', priority: '高', status: 'pending', statusText: '待处理' },
  { id: 2, title: '审核模具领用', dueDate: '2025-12-06', priority: '中', status: 'pending', statusText: '待处理' },
  { id: 3, title: '更新生产数据记录', dueDate: '2025-12-07', priority: '低', status: 'pending', statusText: '待处理' }
])

// 计算今天的待办事项
const todayTodos = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return todoList.value.filter(todo => todo.dueDate === today)
})

// 模具加工任务模拟数据
const moldTasks = ref([
  { id: 1, title: '模具加工任务A', moldNumber: 'MJ-2025001', progress: 75 },
  { id: 2, title: '模具加工任务B', moldNumber: 'MJ-2025002', progress: 45 },
  { id: 3, title: '模具加工任务C', moldNumber: 'MJ-2025003', progress: 90 }
])

// 非模具加工任务模拟数据
const nonMoldTasks = ref([
  { id: 1, title: '非模具加工任务X', type: '普通加工', progress: 60 },
  { id: 2, title: '非模具加工任务Y', type: '特殊加工', progress: 30 },
  { id: 3, title: '非模具加工任务Z', type: '紧急加工', progress: 85 }
])

// 最近成型录入记录模拟数据
const productionData = ref([
  { id: 1, title: '成型录入1', product: '产品A', recordTime: '2025-12-04 14:30', status: 'completed', statusText: '已完成' },
  { id: 2, title: '成型录入2', product: '产品B', recordTime: '2025-12-04 13:15', status: 'completed', statusText: '已完成' },
  { id: 3, title: '成型录入3', product: '产品C', recordTime: '2025-12-04 12:00', status: 'pending', statusText: '待审核' }
])

// 加载数据
onMounted(() => {
  // 这里可以加载实际的数据
  console.log('主页数据加载完成')
})
</script>

<style scoped lang="scss">
// 引入外部样式变量
@use '@/styles/variables' as *;
@use 'sass:color';

// Circle Soft设计风格 - 主页样式
.home-container {
  padding: 15px 15px 2.5px 15px;
  background-color: $white;
  min-height: calc(100vh - 120px);
}


// 快捷入口容器样式
.quick-access-container {
  background-color: $white;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid $gray-200;
  padding: $spacing-3;
  margin: 1px 0 1px 0;
  transition: all 0.3s ease;
}

// 图标墙布局
.quick-access-wall {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: $spacing-1;
  width: 100%;
  justify-items: center;
}

// 单个图标项样式
.quick-access-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
  padding: $spacing-2;
  border-radius: $border-radius-lg;
  width: 100%;
  max-width: 140px;
  
  &:hover {
    transform: translateY(-2px);
  }
  
  // 图标样式 - 与微信小程序端统一
  .quick-access-icon {
    width: 81px;
    height: 81px;
    border-radius: 22px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40px;
    transition: all 0.2s ease;
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.18), 0 2px 6px rgba(0, 0, 0, 0.1);
    
    .el-icon {
      font-size: 40px;
      transition: all 0.2s ease;
    }
  }
  
  // 标题样式
  .quick-access-title {
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    color: $gray-800;
    white-space: normal;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    line-height: 1.4;
    margin-top: $spacing-1;
    width: 100%;
  }
}
  
// 移除旧样式，直接为每个快捷操作项设置明确样式

// 悬停状态样式
.quick-access-item {
  &:hover .quick-access-icon {
    el-icon,
    .el-icon,
    svg {
      color: $white !important;
      fill: $white !important;
    }
  }
}

// 加工申请 - 蓝色系
.quick-access-apply_non_mold {
  .quick-access-icon {
    background: $quick-apply-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-apply-color !important;
      fill: $quick-apply-color !important;
    }
    
    &:hover {
      background: $quick-apply-hover !important;
    }
  }
}

// 模具初始参数 - 绿色系
.quick-access-mold_initial_params_create {
  .quick-access-icon {
    background: $quick-initial-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-initial-color !important;
      fill: $quick-initial-color !important;
    }
    
    &:hover {
      background: $quick-initial-hover !important;
    }
  }
}

// 新模建档 - 紫色系
.quick-access-mold_storage_create {
  .quick-access-icon {
    background: $quick-new-mold-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-new-mold-color !important;
      fill: $quick-new-mold-color !important;
    }
    
    &:hover {
      background: $quick-new-mold-hover !important;
    }
  }
}

// 模具领用 - 橙色系
.quick-access-mold_use_apply {
  .quick-access-icon {
    background: $quick-use-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-use-color !important;
      fill: $quick-use-color !important;
    }
    
    &:hover {
      background: $quick-use-hover !important;
    }
  }
}

// 模具归还 - 青色系
.quick-access-mold_return_apply {
  .quick-access-icon {
    background: $quick-return-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-return-color !important;
      fill: $quick-return-color !important;
    }
    
    &:hover {
      background: $quick-return-hover !important;
    }
  }
}

// 耗材领用 - 粉色系
.quick-access-consumables_apply {
  .quick-access-icon {
    background: $quick-consumable-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-consumable-color !important;
      fill: $quick-consumable-color !important;
    }
    
    &:hover {
      background: $quick-consumable-hover !important;
    }
  }
}

// 成型录入 - 黄色系
.quick-access-production_data_entry {
  .quick-access-icon {
    background: $quick-production-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-production-color !important;
      fill: $quick-production-color !important;
    }
    
    &:hover {
      background: $quick-production-hover !important;
    }
  }
}

// 测量录入 - 蓝绿色系
.quick-access-measure_data_entry {
  .quick-access-icon {
    background: $quick-measure-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-measure-color !important;
      fill: $quick-measure-color !important;
    }
    
    &:hover {
      background: $quick-measure-hover !important;
    }
  }
}

// 调模录入 - 红色系
.quick-access-tuning_record_entry {
  .quick-access-icon {
    background: $quick-tuning-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-tuning-color !important;
      fill: $quick-tuning-color !important;
    }
    
    &:hover {
      background: $quick-tuning-hover !important;
    }
  }
}

// 验收记录 - 黄色系
.quick-access-tuning_acceptance_entry {
  .quick-access-icon {
    background: $quick-acceptance-bg !important;
    
    el-icon,
    .el-icon,
    svg {
      color: $quick-acceptance-color !important;
      fill: $quick-acceptance-color !important;
    }
    
    &:hover {
      background: $quick-acceptance-hover !important;
    }
  }
}

// 默认样式（确保所有图标都有颜色）
.quick-access-item .quick-access-icon {
  background-color: $primary-light !important;
  
  el-icon,
  .el-icon,
  svg {
    color: $primary-color !important;
    fill: $primary-color !important;
    transition: all 0.2s ease !important;
  }
  
  &:hover {
    background-color: $primary-dark !important;
  }
}

// 统计行样式
.stats-row {
  margin: 0 0 1px 0;
  
  .el-col {
    margin-bottom: 1px;
    padding: 0;
  }
}

// 欢迎语模块样式
.welcome-container {
  display: flex;
  gap: $spacing-3;
  width: 100%;
  margin: 0 0 1px 0;
  background-color: $white;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  border: 1px solid $gray-300;
  padding: $spacing-3;
  transition: all 0.3s ease;
  color: $gray-800;
  
  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  // 响应式布局
  @media (max-width: 768px) {
    flex-direction: column;
  }
}

// 欢迎语左侧区域样式
.welcome-left-section {
  flex: 4;
  display: flex;
  flex-direction: column;
}

// 欢迎语右侧区域样式
.welcome-right-section {
  flex: 5;
  display: flex;
  flex-direction: column;
}

// 欢迎语头部样式
.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: $spacing-3;
}

// 欢迎语左侧样式
.welcome-left {
  display: flex;
  align-items: center;
  gap: $spacing-3;
}

// 欢迎语右侧样式
.welcome-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: $spacing-2;
}

// 用户头像样式
.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #CBD5E1 0%, #E2E8F0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(100, 116, 139, 0.3);
  transition: all 0.3s ease;
  
  &:hover {
    transform: scale(1.05);
    border-color: rgba(100, 116, 139, 0.4);
  }
}

// 头像图标样式
.avatar-icon {
  font-size: 32px;
  color: $primary-color;
}

// 欢迎信息样式
.welcome-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

// 欢迎语标题样式
.welcome-title {
  font-size: $font-size-2xl;
  font-weight: 700;
  color: $gray-800;
  margin: 0;
  background: linear-gradient(135deg, #334155 0%, #475569 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

// 用户角色样式
.user-role {
  font-size: $font-size-sm;
  color: $gray-700;
  background: rgba(148, 163, 184, 0.15);
  padding: 2px 10px;
  border-radius: 10px;
  align-self: flex-start;
  font-weight: 500;
  border: 1px solid rgba(148, 163, 184, 0.3);
}

// 欢迎语日期样式
.welcome-date {
  font-size: $font-size-sm;
  color: $gray-700;
  background: rgba(148, 163, 184, 0.15);
  padding: 6px 14px;
  border-radius: 16px;
  font-weight: 500;
  border: 1px solid rgba(148, 163, 184, 0.3);
}

// 系统统计样式
.system-stats {
  display: flex;
  gap: $spacing-2;
}

// 统计项样式
.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: $font-size-sm;
  color: $gray-700;
  background: rgba(148, 163, 184, 0.15);
  padding: 6px 12px;
  border-radius: 14px;
  border: 1px solid rgba(148, 163, 184, 0.3);
  transition: all 0.2s ease;
  
  &:hover {
    background: rgba(148, 163, 184, 0.2);
    transform: translateY(-1px);
  }
}

// 统计图标样式
.stat-icon {
  font-size: 16px;
  color: $primary-color;
}

// 统计文本样式
.stat-text {
  font-weight: 500;
}

// 今日工作样式
.today-tasks {
  display: flex;
  flex-direction: column;
  gap: $spacing-2;
  background: rgba(148, 163, 184, 0.15);
  border-radius: 12px;
  padding: $spacing-3;
  border: 1px solid rgba(148, 163, 184, 0.3);
  height: 100%;
}

// 今日工作头部样式
.today-tasks-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

// 今日工作标签样式
.today-tasks-label {
  font-size: $font-size-base;
  font-weight: 600;
  color: $gray-800;
  white-space: nowrap;
}

// 今日工作数量样式
.today-tasks-count {
  display: flex;
  align-items: center;
}

// 数量徽章样式
.count-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: linear-gradient(135deg, #64748B 0%, #475569 100%);
  color: $white;
  font-size: $font-size-xs;
  font-weight: 700;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(100, 116, 139, 0.3);
}

// 今日工作列表样式
.today-tasks-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-2;
  overflow-y: auto;
  max-height: 200px;
}

// 今日工作项样式
.today-task-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: $font-size-sm;
  color: $gray-700;
  background: rgba(148, 163, 184, 0.2);
  padding: 10px 16px;
  border-radius: 18px;
  border: 1px solid rgba(148, 163, 184, 0.3);
  transition: all 0.2s ease;
  width: 100%;
  box-sizing: border-box;
  
  &:hover {
    background: rgba(148, 163, 184, 0.25);
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  }
  
  &.no-tasks {
    background: rgba(148, 163, 184, 0.15);
    color: $gray-600;
    border-color: rgba(148, 163, 184, 0.25);
    
    &:hover {
      background: rgba(148, 163, 184, 0.2);
      color: $gray-700;
      border-color: rgba(148, 163, 184, 0.35);
    }
  }
}

// 任务图标样式
.task-icon {
  font-size: 14px;
  color: $primary-color;
  flex-shrink: 0;
}

// 任务标题样式
.task-title {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

// 任务优先级样式
.task-priority {
  font-size: $font-size-xs;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 10px;
  white-space: nowrap;
  flex-shrink: 0;
  
  &.high {
    background: linear-gradient(135deg, #FCA5A5 0%, #F87171 100%);
    color: #991B1B;
  }
  
  &.medium {
    background: linear-gradient(135deg, #FDE68A 0%, #FBBF24 100%);
    color: #92400E;
  }
  
  &.low {
    background: linear-gradient(135deg, #A7F3D0 0%, #34D399 100%);
    color: #065F46;
  }
}

// 统计卡片样式 - Circle Soft风格
.stats-card {
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: $spacing-3;
  transition: all 0.3s ease;
  cursor: pointer;
  margin: 0;
  text-align: center;
  height: 120px; /* 固定高度，确保所有卡片高度一致 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
  }
  
  .stats-value {
    font-size: 36px;
    font-weight: 700;
    color: #FFFFFF;
    margin-bottom: $spacing-1;
  }
  
  .stats-label {
    font-size: $font-size-sm;
    color: #FFFFFF;
    margin-bottom: 2px;
    font-weight: 700;
    text-align: center;
  }
  
  .stats-year {
    font-size: $font-size-xs;
    font-weight: 600;
    color: rgba(255, 255, 255, 0.9);
    margin-top: 0;
  }
  
  .stats-change {
    font-size: $font-size-xs;
    font-weight: 700;
    color: #FFFFFF;
    
    &.positive {
      display: flex;
      align-items: center;
      justify-content: center;
      
      &::before {
        content: '↑';
        margin-right: 4px;
      }
    }
    
    &.negative {
      display: flex;
      align-items: center;
      justify-content: center;
      
      &::before {
        content: '↓';
        margin-right: 4px;
      }
    }
  }
}



// 底部功能版块容器样式
.bottom-sections-container {
  margin: 10px 0 0 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 15px;
}

// 底部版块基础样式
.bottom-section {
  background-color: $white;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid $gray-200;
  padding: $spacing-3;
  transition: all 0.3s ease;
  min-height: 250px;
  
  &:hover {
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
  }
}

// 版块头部样式
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-3;
  padding-bottom: $spacing-2;
  border-bottom: 1px solid $gray-200;
}

// 版块标题样式
.section-title {
  font-size: $font-size-lg;
  font-weight: 600;
  color: $gray-900;
  margin: 0;
}

// 版块操作按钮样式
.section-actions {
  display: flex;
  gap: $spacing-2;
}

// 版块内容样式
.section-content {
  display: flex;
  flex-direction: column;
  gap: $spacing-2;
}

// 待办事项项样式
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-2;
  background-color: $gray-50;
  border-radius: 8px;
  transition: all 0.2s ease;
  
  &:hover {
    background-color: $gray-100;
    transform: translateY(-1px);
  }
}

// 任务项样式
.task-item {
  display: flex;
  flex-direction: column;
  gap: $spacing-1;
  padding: $spacing-2;
  background-color: $gray-50;
  border-radius: 8px;
  transition: all 0.2s ease;
  
  &:hover {
    background-color: $gray-100;
    transform: translateY(-1px);
  }
}

// 数据项样式
.data-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-2;
  background-color: $gray-50;
  border-radius: 8px;
  transition: all 0.2s ease;
  
  &:hover {
    background-color: $gray-100;
    transform: translateY(-1px);
  }
}

// 信息内容样式
.todo-info,
.task-info,
.data-info {
  flex: 1;
}

// 标题样式
.todo-title,
.task-title,
.data-title {
  font-size: $font-size-sm;
  font-weight: 500;
  color: $gray-900;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

// 元信息样式
.todo-meta,
.task-meta,
.data-meta {
  font-size: $font-size-xs;
  color: $gray-600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

// 进度条样式
.task-progress {
  width: 100%;
  margin-top: $spacing-1;
}

// 状态标签样式
.todo-status,
.data-status {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: $font-size-xs;
  font-weight: 500;
  white-space: nowrap;
  
  &.pending {
    background-color: #FEF3C7;
    color: $warning-color;
  }
  
  &.completed {
    background-color: #D1FAE5;
    color: $success-color;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .home-container {
    padding: $spacing-2;
  }
  
  .bottom-sections-container {
    grid-template-columns: 1fr;
  }
}

// 小屏幕下的特殊处理
@media (max-width: 576px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-2;
  }
  
  .section-actions {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>
