import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'
import './styles/theme.scss'
import 'element-plus/dist/index.css'
import { setupPermissionDirective } from './directives/permission'


const app = createApp(App)
app.use(createPinia())
app.use(router)
// 注册权限控制指令
setupPermissionDirective(app)
// 配置Element Plus全局z-index
app.use(ElementPlus, {
  zIndex: 3000 // 设置全局z-index基准值
})

// 添加全局错误处理
app.config.errorHandler = (err, instance, info) => {
  console.error('Global error:', err)
  console.error('Error info:', info)
  // 可以在这里添加错误上报逻辑
  // 确保页面不会因为单个组件错误而完全崩溃
}

app.mount('#app')

// 开发环境配置同步验证（已禁用，避免阻塞页面加载）
// import { initDevConfigSync } from './utils/devConfigSync'
// initDevConfigSync().catch(error => {
//   console.error('[DEV-CONFIG-SYNC] 配置验证失败:', error)
// })
