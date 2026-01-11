import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 应用状态
  const sidebarCollapsed = ref(false)
  const theme = ref<'light' | 'dark'>('light')
  const loading = ref(false)
  const breadcrumbs = ref<Array<{ title: string; path?: string }>>([])

  // 切换侧边栏
  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  // 设置主题
  const setTheme = (newTheme: 'light' | 'dark') => {
    theme.value = newTheme
    document.documentElement.setAttribute('data-theme', newTheme)
  }

  // 设置加载状态
  const setLoading = (status: boolean) => {
    loading.value = status
  }

  // 设置面包屑
  const setBreadcrumbs = (items: Array<{ title: string; path?: string }>) => {
    breadcrumbs.value = items
  }

  // 添加面包屑
  const addBreadcrumb = (item: { title: string; path?: string }) => {
    breadcrumbs.value.push(item)
  }

  // 清除面包屑
  const clearBreadcrumbs = () => {
    breadcrumbs.value = []
  }

  return {
    // 状态
    sidebarCollapsed,
    theme,
    loading,
    breadcrumbs,
    
    // 方法
    toggleSidebar,
    setTheme,
    setLoading,
    setBreadcrumbs,
    addBreadcrumb,
    clearBreadcrumbs
  }
})
