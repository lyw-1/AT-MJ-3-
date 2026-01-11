import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 权限控制指令
 * 基于用户角色控制元素的显示和隐藏
 * 使用方式：v-permission="['admin', 'manager']"
 */
export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const userStore = useUserStore()
    const requiredRoles = binding.value || []
    
    // 如果没有指定角色，则显示元素
    if (!requiredRoles || requiredRoles.length === 0) {
      return
    }
    
    // 检查用户是否有匹配的角色
    const userRoles = userStore.userInfo?.roles?.map((role: any) => role.code) || []
    // 添加开发模式下的默认角色支持
    const isDevMode = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    const effectiveRoles = isDevMode ? [...userRoles, 'admin', 'manager'] : userRoles
    const hasPermission = requiredRoles.some((role: string) => effectiveRoles.includes(role))
    
    // 如果没有权限，则隐藏元素
    if (!hasPermission) {
      el.style.display = 'none'
    }
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    // 当用户角色发生变化时，重新检查权限
    const userStore = useUserStore()
    const requiredRoles = binding.value || []
    
    // 如果没有指定角色，则显示元素
    if (!requiredRoles || requiredRoles.length === 0) {
      el.style.display = ''
      return
    }
    
    // 检查用户是否有匹配的角色
    const userRoles = userStore.userInfo?.roles?.map((role: any) => role.code) || []
    // 添加开发模式下的默认角色支持
    const isDevMode = (import.meta.env.VITE_USE_DEV_TOKEN + '') === 'true'
    const effectiveRoles = isDevMode ? [...userRoles, 'admin', 'manager'] : userRoles
    const hasPermission = requiredRoles.some((role: string) => effectiveRoles.includes(role))
    
    // 更新元素显示状态
    if (hasPermission) {
      el.style.display = ''
    } else {
      el.style.display = 'none'
    }
  }
}

/**
 * 权限控制指令工厂函数
 * @returns 权限控制指令
 */
export function setupPermissionDirective(app: any) {
  app.directive('permission', permissionDirective)
}
