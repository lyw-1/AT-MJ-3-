<template>
  <el-container class="app-container">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="header-left">
        <!-- 系统标题 -->
        <h1 class="system-title">AT模具管理系统</h1>
        
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" class="app-breadcrumb" v-if="breadcrumbs.length">
          <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="item.path">
            {{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
        
        <!-- 搜索框 -->
        <div class="header-search">
          <el-input
            placeholder="搜索..."
            class="search-input"
            prefix-icon="Search"
            size="small"
          />
        </div>
      </div>
      
      <div class="header-right">
        <!-- 新消息提示图标 -->
        <div class="header-notification">
          <el-button
            circle
            :icon="Bell"
            class="header-btn notification-btn"
            aria-label="新消息通知"
          >
            <div class="notification-badge"></div>
          </el-button>
        </div>
        
        <!-- 全屏按钮 -->
        <el-button
          circle
          :icon="FullScreen"
          @click="toggleFullscreen"
          class="header-btn"
          aria-label="切换全屏"
        />
        
        <!-- 主题切换 -->
        <el-button
          circle
          :icon="isDark ? Sunny : Moon"
          @click="toggleTheme"
          class="header-btn"
          aria-label="切换主题"
        />
        
        <!-- 用户菜单 -->
        <el-dropdown @command="handleUserCommand">
          <div class="user-info">
            <el-avatar :size="32" :src="userInfo?.avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="username">{{ userInfo?.name || '用户' }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                系统设置
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    
    <el-container>
      <!-- 侧边栏 -->
      <el-aside 
        :width="sidebarCollapsed ? '64px' : '240px'" 
        class="app-sidebar"
        :class="{ collapsed: sidebarCollapsed }"
      >
        <!-- 侧边栏头部 -->
        <div class="sidebar-header">
          <!-- 折叠/展开图标 -->
          <div class="sidebar-toggle" @click="toggleSidebar">
            <el-icon>
              <Expand v-if="sidebarCollapsed" />
              <Fold v-else />
            </el-icon>
          </div>
          
          <!-- 用户信息 -->
          <div class="user-profile">
            <el-avatar :size="40" :src="userInfo?.avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <div class="user-info-text" v-if="!sidebarCollapsed">
              <div class="username">{{ userInfo?.name || '用户' }}</div>
              <div class="user-role">{{ (userInfo?.roles && userInfo.roles.length > 0) ? userInfo.roles[0].name : '管理员' }}</div>
            </div>
          </div>
        </div>
        
        <el-menu
          :default-active="$route.path"
          :collapse="sidebarCollapsed"
          :collapse-transition="false"
          router
          unique-opened
          class="sidebar-menu"
        >
          <template v-for="route in menuRoutes" :key="route.path">
            <!-- 单级菜单 -->
            <el-menu-item 
              v-if="!route.children || route.children.length === 1"
              :index="route.children ? route.children[0].path : route.path"
              @click="handleMenuClick(route)"
            >
              <!-- 自定义图片图标 -->
              <img 
                v-if="route.meta?.icon && route.meta.icon.includes('/')"
                :src="route.meta.icon"
                class="custom-menu-icon"
                :alt="route.meta.title"
              >
              <!-- Element Plus 图标组件 -->
              <el-icon v-else-if="route.meta?.icon && iconMap[route.meta.icon]">
                <component :is="iconMap[route.meta.icon]" />
              </el-icon>
              <template #title>
                <span>{{ route.meta?.title }}</span>
              </template>
            </el-menu-item>
            
            <!-- 多级菜单 -->
            <el-sub-menu 
              v-else
              :index="route.path"
            >
              <template #title>
                <!-- 自定义图片图标 -->
                <img 
                  v-if="route.meta?.icon && route.meta.icon.includes('/')"
                  :src="route.meta.icon"
                  class="custom-menu-icon"
                  :alt="route.meta.title"
                >
                <!-- Element Plus 图标组件 -->
                <el-icon v-else-if="route.meta?.icon && iconMap[route.meta.icon]">
                  <component :is="iconMap[route.meta.icon]" />
                </el-icon>
                <span>{{ route.meta?.title }}</span>
              </template>
              
              <el-menu-item
                v-for="child in route.children.filter(item => !item.meta?.hidden)"
                :key="child.path"
                :index="`${route.path}/${child.path}`"
                @click="handleMenuClick(child)"
              >
                <!-- 自定义图片图标 -->
                <img 
                  v-if="child.meta?.icon && child.meta.icon.includes('/')"
                  :src="child.meta.icon"
                  class="custom-menu-icon"
                  :alt="child.meta.title"
                >
                <!-- Element Plus 图标组件 -->
                <el-icon v-else-if="child.meta?.icon && iconMap[child.meta.icon]">
                  <component :is="iconMap[child.meta.icon]" />
                </el-icon>
                <span>{{ child.meta?.title }}</span>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区域 -->
      <el-main class="app-main">
        <!-- 页面内容 -->
        <div class="main-content">
          <!-- 移除自定义遮罩层，统一使用Element Plus对话框自带遮罩 -->
          <RouterView />
        </div>
      </el-main>
    </el-container>
    

  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore, useAppStore } from '@/stores'
import { 
  Box, User, ArrowDown, Setting, SwitchButton,
  Expand, Fold, FullScreen, Sunny, Moon, Bell,
  House, DataAnalysis, Tools, Monitor, Operation, TrendCharts, SetUp
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'

// 使用appStore
const appStore = useAppStore()

// 图标映射表，将路由中的图标名称映射到实际组件
const iconMap = {
  House,
  DataAnalysis,
  Tools,
  Box,
  Setting,
  Monitor,
  Operation,
  TrendCharts,
  SetUp
}

const route = useRoute()
const userStore = useUserStore()

// 计算属性
const userInfo = computed(() => userStore.userInfo)
const sidebarCollapsed = computed(() => appStore.sidebarCollapsed)
const breadcrumbs = computed(() => appStore.breadcrumbs)
const isDark = computed(() => appStore.theme === 'dark')
// 初始化全屏状态，检查浏览器实际状态
const isFullscreen = ref(!!document.fullscreenElement)

// 菜单路由
const menuRoutes = computed(() => {
  const routes = route.matched[0]?.children || []
  return routes.filter(route => route.meta?.title && !route.meta?.hidden)
})

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar()
}

// 切换主题
const toggleTheme = () => {
  const newTheme = isDark.value ? 'light' : 'dark'
  appStore.setTheme(newTheme)
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

// 切换全屏
const toggleFullscreen = () => {
  try {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen()
    } else {
      document.exitFullscreen()
    }
  } catch (error) {
    console.error('全屏操作失败:', error)
    // 手动同步状态作为备份
    isFullscreen.value = !!document.fullscreenElement
  }
}

// 添加生命周期钩子
onMounted(() => {
  // 添加全屏变化事件监听
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onUnmounted(() => {
  // 移除全屏变化事件监听
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

// 处理用户菜单命令
const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      // 跳转到个人中心
      break
    case 'settings':
      // 跳转到系统设置
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 处理登出
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 显示加载状态
    const loading = ElLoading.service({
      lock: true,
      text: '正在退出登录...',
      background: 'rgba(255, 255, 255, 0.8)'
    })
    
    // 执行登出操作
    userStore.logout()
    
    // 延迟关闭加载状态，提升用户体验
    setTimeout(() => {
      loading.close()
      ElMessage.success('已退出登录')
      // 跳转到登录页面
      window.location.href = '/login'
    }, 500)
  })
}

// 处理菜单点击
const handleMenuClick = (route: any) => {
  // 更新面包屑
  updateBreadcrumbs(route)
}

// 更新面包屑
const updateBreadcrumbs = (route: any) => {
  const breadcrumbs = []
  
  // 从路由匹配中提取完整路径
  const matched = route.matched
  
  matched.forEach(item => {
    if (item.meta && item.meta.title && !item.path.includes('*')) {
      breadcrumbs.push({
        title: item.meta.title,
        path: item.path
      })
    }
  })
  
  // 如果没有匹配到面包屑，添加默认首页
  if (breadcrumbs.length === 0) {
    breadcrumbs.push({ title: '首页', path: '/home' })
  }
  
  appStore.setBreadcrumbs(breadcrumbs)
}

// 监听路由变化
watch(() => route.path, () => {
  updateBreadcrumbs(route)
}, { immediate: true })
</script>

<style scoped lang="scss">
// 引入外部样式变量
@use '@/styles/variables' as *;

// Circle Soft设计风格 - 主容器样式
.app-container {
  height: 100vh;
  background-color: $white;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative; // 添加相对定位，确保全局容器能正确定位
}

// 主内容区域
.main-content {
  position: relative;
  flex: 1;
  overflow: hidden;
}

// 自定义遮罩层，仅覆盖主内容区域
// 自定义遮罩层样式已移至theme.scss统一管理
// 这里不再重复定义，确保使用统一的样式



// Circle Soft设计风格 - 顶部栏样式
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 72px;
  padding: 0 16px;
  background: white;
  border-bottom: 1px solid $gray-200;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  z-index: 2001;
  position: sticky;
  top: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: $spacing-4;
  flex: 1;
  overflow: hidden;
}

// 面包屑导航样式调整
.app-breadcrumb {
  flex: 0 0 33.333%; /* 宽度减三分之二，只占原来的三分之一 */
  max-width: 33.333%;
  overflow: hidden;
  margin: 0;
  padding: 0;
  height: 100%;
  display: flex;
  align-items: center;
  
  :deep(.el-breadcrumb__item) {
    line-height: 1;
    height: auto;
    display: inline-flex;
    align-items: center;
    vertical-align: middle;
    
    .el-breadcrumb__inner {
      color: $gray-600;
      font-size: $font-size-sm; /* 字体调小 */
      height: auto;
      display: inline-flex;
      align-items: center;
      vertical-align: middle;
      line-height: 1;
      margin: 0;
      padding: 0;
    }
    
    .el-breadcrumb__separator {
      color: $gray-400;
      margin: 0 $spacing-1;
      height: auto;
      display: inline-flex;
      align-items: center;
      font-size: $font-size-sm; /* 字体调小 */
      line-height: 1;
      vertical-align: middle;
    }
  }
}

// 搜索框样式
.header-search {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
  margin: 0 0 0 calc($spacing-4 - 48px);
  padding: 0;
  flex: 1;
  max-width: 300px;
  
  .search-input {
    width: 100%;
    height: 36px;
    margin: 0;
    padding: 0;
    
    :deep(.el-input__wrapper) {
      border-radius: $border-radius-lg;
      padding: 0 $spacing-2;
      height: 36px;
      display: flex;
      align-items: center;
      
      &:hover {
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
    }
    
    :deep(.el-input__inner) {
      height: 36px;
      line-height: 36px;
      font-size: $font-size-sm;
      padding: 0;
      margin: 0;
    }
    
    :deep(.el-input__prefix) {
      margin-right: $spacing-1;
    }
  }
}

// 新消息提示样式
.header-notification {
  display: flex;
  align-items: center;
  height: 100%;
  position: relative;
  margin-right: 8px;
}

.notification-btn {
  position: relative;
  
  .notification-badge {
    position: absolute;
    top: 4px;
    right: 4px;
    width: 8px;
    height: 8px;
    background-color: #ef4444; /* 替换为具体颜色值，避免使用未定义变量 */
    border-radius: 50%;
    border: 2px solid white;
  }
}

.notification-btn:hover {
  :deep(.el-button__inner) {
    background-color: $gray-50;
    color: $primary-color;
  }
}

// 调整header-right按钮之间的间距
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

// 调整header-btn的样式
.header-btn {
  margin: 0;
  padding: 0;
  
  :deep(.el-button__inner) {
    padding: 0;
    width: 36px;
    height: 36px;
  }
}

// 系统标题样式 - 参考Circle Soft设计
.system-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $gray-900;
  margin: 0;
  line-height: 72px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

// 头部按钮样式
.header-btn {
  border: none;
  background: transparent;
  color: $gray-700;
  font-size: 20px;
  transition: all 0.2s ease;
  
  &:hover {
    background: $gray-50;
    color: $primary-color;
  }
}

// 用户信息样式 - 参考Circle Soft设计
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  border-radius: $border-radius-lg;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
  border: 1px solid $gray-200;
  
  &:hover {
    background: $primary-light;
    border-color: $primary-color;
  }
  
  .username {
    font-size: 14px;
    font-weight: 500;
    color: $gray-800;
  }
  
  .dropdown-icon {
    font-size: 12px;
    color: $gray-600;
  }
}

// Circle Soft设计风格 - 侧边栏样式
.app-sidebar {
  background: white;
  border-right: 1px solid $gray-200;
  box-shadow: none;
  transition: width 0.3s ease;
  width: 240px;
  z-index: 2001;
  position: sticky;
  top: 0;
  height: 100%;
  overflow-y: auto;
  
  &.collapsed {
    width: 64px;
  }
}

// 调整主内容区域和侧边栏之间的间距
.el-container {
  display: flex;
  flex: 1;
  overflow: hidden;
  
  // 移除侧边栏和主内容区域之间的间距
  &.el-container--horizontal {
    > .el-main {
      padding-left: 0;
    }
  }
  
  // 调整垂直布局的间距
  &.el-container--vertical {
    > .el-main {
      padding-top: 0;
    }
  }
}

// 侧边栏头部样式
.sidebar-header {
  padding: $spacing-2;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  border-bottom: 1px solid $gray-200;
  background: white;
  position: relative;
  min-height: 80px;
  
  // 用户信息样式
  .user-profile {
    display: flex;
    align-items: center;
    gap: $spacing-2;
    padding: $spacing-2;
    border-radius: $border-radius-lg;
    margin-top: 8px;
    
    // 用户信息文本样式
    .user-info-text {
      display: flex;
      flex-direction: column;
      gap: 2px;
      
      .username {
        font-size: $font-size-base;
        font-weight: $font-weight-semibold;
        color: $gray-800;
      }
      
      .user-role {
        font-size: $font-size-xs;
        color: $gray-500;
      }
    }
  }
  
  // 折叠/展开图标样式
  .sidebar-toggle {
    position: absolute;
    top: $spacing-1;
    right: $spacing-2;
    height: auto;
    border-bottom: none;
    background: transparent;
    padding: $spacing-1;
    border-radius: $border-radius-lg;
    
    &:hover {
      background: $gray-100;
      color: $primary-color;
    }
  }
}

// 侧边栏折叠时隐藏文本内容
.app-sidebar.collapsed {
  .user-info-text {
    display: none;
  }
  
  .sidebar-header {
    padding: $spacing-2;
    height: auto;
    min-height: 80px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: $spacing-2;
    
    .user-profile {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 0;
      height: 40px;
      width: 40px;
      margin: 0;
    }
    
    .sidebar-toggle {
      position: static;
      margin-top: $spacing-1;
    }
  }
}

// 侧边栏切换按钮基础样式已移至sidebar-header样式中
// 这里只保留特定样式

// 侧边栏菜单样式 - 参考Circle Soft设计
.sidebar-menu {
  border-right: none;
  background: white;
  
  // 菜单项公共样式
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      height: 56px;
      line-height: 56px;
      padding: 0 $spacing-4 0 $spacing-5;
      color: $gray-700;
      font-weight: $font-weight-medium;
      font-size: $font-size-base;
      text-align: left;
      transition: all 0.2s ease;
      position: relative;
      z-index: 10;
      will-change: transform, background-color, box-shadow;
      display: flex;
      align-items: center;
      
      // 图标样式
      :deep(.el-icon) {
        font-size: $font-size-xl;
        color: $gray-600;
        margin-right: $spacing-3;
      }
      
      // 自定义图片图标样式
      .custom-menu-icon {
        width: 24px;
        height: 24px;
        margin-right: $spacing-3;
        object-fit: contain;
      }
      
      &:hover {
        background-color: $primary-light;
        color: $primary-color;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        
        // 悬停时图标颜色变化
        :deep(.el-icon) {
          color: $primary-color;
        }
        
        // 悬停时自定义图标保持不变
        .custom-menu-icon {
          filter: brightness(0.8);
        }
      }
    
    // 激活状态 - 左侧紫色指示器，参考Circle Soft设计
    &.is-active {
      background-color: $primary-light;
      color: $primary-color;
      border-left: 4px solid $primary-color;
      font-weight: $font-weight-semibold;
      padding: 0 $spacing-4 0 $spacing-4;
      
      // 激活时图标颜色变化
      :deep(.el-icon) {
        color: $primary-color;
      }
      
      // 激活时自定义图标保持不变
      .custom-menu-icon {
        filter: brightness(0.8);
      }
    }
  }
  
  // 子菜单项样式
  :deep(.el-sub-menu .el-menu-item) {
    padding-left: $spacing-6;
    
    &.is-active {
      padding-left: $spacing-5;
    }
  }
  
  // 折叠状态下的样式调整
  .app-sidebar.collapsed & {
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      padding: 0;
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      
      &:hover,
      &.is-active {
        padding: 0;
      }
      
      :deep(.el-icon) {
        margin-right: 0;
        display: inline-block;
      }
      
      // 折叠状态下自定义图标样式
      .custom-menu-icon {
        margin-right: 0;
      }
      
      // 确保所有内容都居中
      :deep(.el-icon),
      :deep(.el-sub-menu__icon-arrow) {
        margin: 0;
      }
    }
    
    // 折叠状态下子菜单弹出框样式
    :deep(.el-menu--popup),
    :deep(.el-dropdown-menu),
    :deep(.el-popper__content),
    :deep(.el-popover__content),
    :deep(.el-menu--vertical .el-sub-menu__popper),
    :deep(.el-menu--collapse .el-sub-menu__popper) {
      border-radius: 10px !important;
      overflow: hidden;
    }
    
    // 确保弹出框的圆角被正确应用到所有弹出层
    :deep(.el-popper),
    :deep(.el-popover),
    :deep(.el-sub-menu__popper),
    :deep(.el-menu--vertical .el-popper),
    :deep(.el-menu--collapse .el-popper) {
      border-radius: 10px !important;
      overflow: hidden;
    }
    
    // 确保弹出框内容的圆角也被应用
    :deep(.el-popper__inner),
    :deep(.el-popover__inner),
    :deep(.el-menu--popup .el-menu),
    :deep(.el-dropdown-menu__item) {
      border-radius: 10px !important;
      overflow: hidden;
    }
  }
}

// 主内容区域样式
.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: $gray-50;
  overflow: hidden;
  height: 100%;
  
  // 移除Element Plus默认的padding，减小侧边栏和主内容区域之间的间距
  padding-left: 0;
  
  // 确保el-main组件没有默认的padding
  &.el-main {
    padding: 0;
    margin: 0;
  }
}

// 面包屑样式
.app-breadcrumb {
  padding: 16px 32px;
  background: white;
  border-bottom: 1px solid $gray-200;
}

// 主要内容区域样式 - 参考Circle Soft设计
.main-content {
  flex: 1;
  padding: 2.5px;
  overflow-y: auto;
  background-color: $gray-50;
  
  // 滚动条样式优化
  &::-webkit-scrollbar {
    width: 8px;
  }
  
  &::-webkit-scrollbar-track {
    background: $gray-100;
  }
  
  &::-webkit-scrollbar-thumb {
    background: $gray-300;
    border-radius: $border-radius-lg;
    
    &:hover {
      background: $gray-400;
    }
  }
}

// 响应式设计 - 参考Circle Soft设计
@media (max-width: 768px) {
  .app-header {
    padding: 0 16px;
  }
  
  .brand-text {
    font-size: 18px;
  }
  
  .app-sidebar {
    position: fixed;
    top: 72px;
    left: 0;
    height: calc(100vh - 72px);
    z-index: 1000;
    width: 240px;
    box-shadow: 2px 0 16px rgba(0, 0, 0, 0.1);
    
    &.collapsed {
      width: 0;
      overflow: hidden;
    }
  }
  
  .main-content {
    padding: 16px;
  }
  
  .app-breadcrumb {
    padding: 12px 16px;
  }
}
</style>
