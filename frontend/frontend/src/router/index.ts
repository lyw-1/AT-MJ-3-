import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/auth/Login.vue'),
    meta: { 
      title: '登录',
      requiresAuth: false 
    }
  },
  {
    path: '/',
    redirect: '/home',
    component: () => import('@/layouts/AppLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/pages/home/Home.vue'),
        meta: { 
          title: '主页',
          icon: 'House',
          roles: ['admin', 'manager', 'user', 'operator']
        }
      },
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/pages/dashboard/Dashboard.vue'),
        meta: { 
          title: '仪表盘',
          icon: 'DataAnalysis',
          roles: ['admin', 'manager']
        }
      },
      // 工序管理
      {
        path: '/process',
        name: 'Process',
        meta: { 
          title: '工序管理',
          icon: 'List',
          roles: ['admin', 'manager', 'user']
        },
        children: [
          {
            path: 'process-detail',
            name: 'ProcessDetail',
            component: () => import('@/pages/process/ProcessDetail.vue'),
            meta: {
              title: '工序详细设置',
              hidden: true,
              roles: ['admin', 'manager', 'user']
            }
          },
          {
            path: 'material-preparation',
            name: 'MaterialPreparation',
            component: () => import('@/pages/process/MaterialPreparation.vue'),
            meta: {
              title: '备料工序',
              hidden: true,
              roles: ['admin', 'manager', 'user']
            }
          }
        ]
      },
      // 加工管理
      {
        path: '/mold',
        name: 'Mold',
        redirect: '/mold/scheduling-center',
        meta: { 
          title: '加工管理',
          icon: 'Tools',
          roles: ['admin', 'manager', 'user', 'operator']
        },
        children: [
          {
            path: 'scheduling-center',
            name: 'SchedulingCenter',
            component: () => import('@/pages/mold/SchedulingCenter.vue'),
            meta: { title: '调度中心', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'initial-params-list',
            name: 'MoldInitialParamsList',
            component: () => import('@/pages/mold/InitialParams.vue'),
            meta: { title: '模具初始参数列表', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'task-list',
            name: 'TaskList',
            component: () => import('@/pages/mold/TaskList.vue'),
            meta: { title: '任务列表', roles: ['admin', 'manager', 'user', 'operator'] }
          },
          {
            path: 'initial-params/:id',
            name: 'MoldInitialParamDetail',
            component: () => import('@/pages/mold/InitialParamDetail.vue'),
            meta: {
              title: '模具初始参数详情',
              hidden: true,
              roles: ['admin', 'manager', 'user']
            }
          },
          {
            path: 'process-route/:moldId',
            name: 'MoldProcessRoute',
            component: () => import('@/pages/mold/ProcessRouteDesigner.vue'),
            meta: {
              title: '模具加工工序路线',
              hidden: true,
              roles: ['admin', 'manager', 'user']
            }
          },
          {
            path: 'process-preset/:id',
            name: 'ProcessPreset',
            component: () => import('@/pages/mold/ProcessPresetPage.vue'),
            meta: {
              title: '工序预设置',
              hidden: true,
              roles: ['admin', 'manager', 'user']
            }
          },
          {
            path: 'non-mold-application',
            name: 'NonMoldApplication',
            component: () => import('@/pages/mold/Application.vue'),
            meta: { title: '加工申请单（非模具）', roles: ['admin', 'manager', 'user'] }
          },

        ]
      },
      // 模库管理
      {
        path: '/storage',
        name: 'Storage',
        redirect: '/storage/mold-list',
        meta: { 
          title: '模库管理',
          icon: 'Box',
          roles: ['admin', 'manager', 'user', 'operator']
        },
        children: [
          {
            path: 'mold-list',
            name: 'MoldList',
            component: () => import('@/pages/storage/MoldList.vue'),
            meta: { title: '模具列表', roles: ['admin', 'manager', 'user', 'operator'] }
          },
          {
            path: 'accessories',
            name: 'Accessories',
            component: () => import('@/pages/storage/Accessories.vue'),
            meta: { title: '配件管理', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'records',
            name: 'StorageRecords',
            component: () => import('@/pages/storage/StockRecords.vue'),
            meta: { title: '出入库记录', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'key-molds',
            name: 'KeyMolds',
            component: () => import('@/pages/storage/KeyMolds.vue'),
            meta: { title: '重点模具', roles: ['admin', 'manager'] }
          },
          {
            path: 'measure-input',
            name: 'MeasureInput',
            component: () => import('@/pages/storage/MeasureInput.vue'),
            meta: { title: '测量数据录入', roles: ['admin', 'manager', 'user', 'operator'] }
          }
        ]
      },
      // 调模管理
      {
        path: '/tuning',
        name: 'Tuning',
        redirect: '/tuning/tuning-list',
        meta: { 
          title: '调模管理',
          icon: 'Operation',
          roles: ['admin', 'manager', 'user', 'operator']
        },
        children: [
          {
            path: 'offline-7days',
            name: 'Offline7Days',
            component: () => import('@/pages/tuning/Offline7Days.vue'),
            meta: { title: '预计7天下线', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'tuning-list',
            name: 'TuningList',
            component: () => import('@/pages/tuning/TuningList.vue'),
            meta: { title: '调模任务', roles: ['admin', 'manager', 'user', 'operator'] }
          },
          {
            path: 'tuning-records',
            name: 'TuningRecords',
            component: () => import('@/pages/tuning/TuningRecords.vue'),
            meta: { title: '调模记录', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'acceptance-records',
            name: 'AcceptanceRecords',
            component: () => import('@/pages/tuning/AcceptanceRecords.vue'),
            meta: { title: '验收记录', roles: ['admin', 'manager', 'user'] }
          }
        ]
      },
      // 耗材及备品管理
      {
        path: '/consumables',
        name: 'Consumables',
        redirect: '/consumables/list',
        meta: { 
          title: '耗材及备品管理',
          icon: 'Box',
          roles: ['admin', 'manager', 'user']
        },
        children: [
          {
            path: 'list',
            name: 'ConsumablesList',
            component: () => import('@/pages/storage/ConsumablesList.vue'),
            meta: { title: '耗材列表', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'records',
            name: 'ConsumablesRecords',
            component: () => import('@/pages/storage/StockRecords.vue'),
            meta: { title: '出入库明细', roles: ['admin', 'manager', 'user'] }
          }
        ]
      },
      // 设备管理
      {
        path: '/equipment',
        name: 'Equipment',
        redirect: '/equipment/status',
        meta: { 
          title: '设备管理',
          icon: 'Monitor',
          roles: ['admin', 'manager', 'user']
        },
        children: [
          {
            path: 'status',
            name: 'EquipmentStatus',
            component: () => import('@/pages/equipment/Status.vue'),
            meta: { title: '设备状态', roles: ['admin', 'manager', 'user', 'operator'] }
          },
          {
            path: 'list',
            name: 'EquipmentList',
            component: () => import('@/pages/equipment/List.vue'),
            meta: { title: '设备列表', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'maintenance',
            name: 'EquipmentMaintenance',
            component: () => import('@/pages/equipment/Maintenance.vue'),
            meta: { title: '保养记录', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'replacement',
            name: 'EquipmentReplacement',
            component: () => import('@/pages/equipment/Replacement.vue'),
            meta: { title: '配件更换', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'reminders',
            name: 'EquipmentReminders',
            component: () => import('@/pages/equipment/Reminders.vue'),
            meta: { title: '保养提醒', roles: ['admin', 'manager', 'user'] }
          }
        ]
      },
      // 生产管理
      {
        path: '/production',
        name: 'Production',
        redirect: '/production/mold-match',
        meta: { 
          title: '生产管理',
          icon: 'TrendCharts',
          roles: ['admin', 'manager', 'user', 'operator']
        },
        children: [
          {
            path: 'mold-match',
            name: 'MoldMatch',
            component: () => import('@/pages/production/MoldMatch.vue'),
            meta: { title: '模具匹配', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'available-molds',
            name: 'AvailableMolds',
            component: () => import('@/pages/production/AvailableMolds.vue'),
            meta: { title: '可用模具', roles: ['admin', 'manager', 'user', 'operator'] }
          },
          {
            path: 'data-entry',
            name: 'ProductionDataEntry',
            component: () => import('@/pages/production/DataEntry.vue'),
            meta: { title: '生产数据录入', roles: ['admin', 'manager', 'user', 'operator'] }
          }
        ]
      },
      // 系统管理
      {
        path: '/system',
        name: 'System',
        redirect: '/system/users',
        meta: { 
          title: '系统管理',
          icon: 'SetUp',
          roles: ['admin', 'manager']
        },
        children: [
          {
            path: 'users',
            name: 'SystemUsers',
            component: () => import('@/pages/system/Users.vue'),
            meta: { title: '用户管理', roles: ['admin', 'manager'] }
          },
          {
            path: 'roles',
            name: 'SystemRoles',
            component: () => import('@/pages/system/Roles.vue'),
            meta: { title: '角色管理', hidden: true, roles: ['admin'] }
          },
          {
            path: 'departments',
            name: 'SystemDepartments',
            component: () => import('@/pages/system/Departments.vue'),
            meta: { title: '部门管理', hidden: true, roles: ['admin', 'manager'] }
          },
          {
            path: 'products',
            name: 'SystemProducts',
            component: () => import('@/pages/system/Products.vue'),
            meta: { title: '成品列表', roles: ['admin', 'manager', 'user'] }
          },
          {
            path: 'logs',
            name: 'SystemLogs',
            component: () => import('@/pages/system/Logs.vue'),
            meta: { title: '日志管理', roles: ['admin', 'manager'] }
          },
          {
            path: 'profile',
            name: 'UserProfile',
            component: () => import('@/pages/system/Profile.vue'),
            meta: { title: '个人中心', roles: ['admin', 'manager', 'user', 'operator'] }
          }
        ]
      }
    ]
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/error/NotFound.vue'),
    meta: { title: '页面未找到' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const useDev = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 蜂窝陶瓷模具管理系统`
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth !== false) {
    // 无论是否登录，都先尝试从localStorage获取token和userInfo，确保状态同步
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('user_info')
    
    // 直接检查localStorage中的token，避免依赖userStore的响应式更新
    if (storedToken) {
      // 如果localStorage中有token，确保store中的token也被更新
      if (storedToken !== userStore.token) {
        userStore.token = storedToken
      }
      
      // 如果localStorage中有userInfo，确保store中的userInfo也被更新
      if (storedUserInfo) {
        const parsedUserInfo = JSON.parse(storedUserInfo)
        if (JSON.stringify(parsedUserInfo) !== JSON.stringify(userStore.userInfo)) {
          userStore.userInfo = parsedUserInfo
        }
      }
      
      // 已登录，检查角色权限
      if (!useDev) {
        // 开发模式跳过角色检查
        if (!checkRolePermission(to, userStore)) {
          // 没有权限，重定向到404或主页
          next('/')
          return
        }
      }
      next()
    } else {
      // 如果localStorage中没有token，跳转到登录页面
      next('/login')
    }
  } else {
    // 不需要认证的页面
    if (to.path === '/login') {
      // 修复登录页面闪烁问题：只在真正登录状态下才重定向
      // 避免因localStorage有旧token但store中token为空导致的循环重定向
      const storedToken = localStorage.getItem('token')
      
      if (storedToken) {
        // 如果localStorage中有token，跳转到首页
        next('/')
      } else {
        next()
      }
    } else {
      next()
    }
  }
})

// 检查角色权限
function checkRolePermission(to: any, userStore: any): boolean {
  // 如果路由没有设置roles，则默认允许访问
  if (!to.meta.roles || to.meta.roles.length === 0) {
    return true
  }
  
  // 检查用户是否有匹配的角色，增强错误处理
  if (!userStore.userInfo || !userStore.userInfo.roles) {
    return false
  }
  
  // 提取用户角色代码
  let userRoles: string[] = []
  
  // 处理不同类型的roles
  if (typeof userStore.userInfo.roles === 'string') {
    // 如果是字符串，处理特殊情况
    const rolesStr = userStore.userInfo.roles.trim()
    if (rolesStr && rolesStr !== 'null') {
      userRoles = rolesStr.split(',').map(r => r.trim()).filter(Boolean)
    }
  } else if (Array.isArray(userStore.userInfo.roles)) {
    // 如果是数组，提取角色代码
    userRoles = userStore.userInfo.roles.map((role: any) => {
      return role?.code || (typeof role === 'string' ? role : '')
    }).filter(Boolean)
  }
  
  // 检查是否有任意角色匹配
  return to.meta.roles.some((role: string) => userRoles.includes(role))
}

export default router
