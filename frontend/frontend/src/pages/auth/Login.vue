<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1 class="login-title">蜂窝陶瓷模具管理系统</h1>
        <p class="login-subtitle">欢迎使用，请登录您的账户</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        size="large"
        @keyup.enter="handleLogin"
        status-icon
        validate-on-rule-change
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
            @blur="handleFieldBlur('username')"
            @input="handleFieldInput('username')"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            clearable
            @blur="handleFieldBlur('password')"
            @input="handleFieldInput('password')"
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-actions">
        <el-button 
          type="text" 
          size="small"
          @click="handleDevLogin"
        >
          开发模式登录
        </el-button>
        <el-button 
          type="text" 
          size="small"
          @click="testLoginApi"
        >
          测试登录API
        </el-button>
        <el-button 
          type="text" 
          size="small"
          @click="showWechatLogin = true"
        >
          微信登录
        </el-button>
      </div>
    </div>
    
    <!-- 微信登录弹窗 -->
    <el-dialog
      v-model="showWechatLogin"
      title="微信登录"
      width="400px"
      center
    >
      <div class="wechat-login-content">
        <el-empty description="微信登录功能暂未开放" />
        <p class="wechat-login-tip">请联系管理员配置微信登录</p>
      </div>
      <template #footer>
        <el-button @click="showWechatLogin = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { LoginCredentials } from '@/types'

// 使用userStore进行登录操作
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const showWechatLogin = ref(false)

const loginForm = reactive<LoginCredentials>({
  username: '',
  password: '',
  rememberMe: false
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  console.log('[LOGIN] 开始处理登录请求')
  
  try {
    // 表单验证
    if (loginFormRef.value) {
      await loginFormRef.value.validate()
    }
    
    loading.value = true
    
    // 使用userStore的login方法进行登录
    const result = await userStore.login(loginForm)
    console.log('[LOGIN] 登录API返回结果:', result);
    
    // 检查登录结果
    if (result && result.code === 200) {
      ElMessage.success('登录成功')
      console.log('[LOGIN] 登录成功，跳转到首页')
      
      // 跳转到首页
      window.location.href = '/home'
    } else {
      console.error('[LOGIN] 登录失败，服务器返回无效结果')
      ElMessage.error('登录失败: ' + (result?.message || '未知错误'))
    }
  } catch (error: any) {
    console.error('[LOGIN] 登录过程中发生错误:', error)
    ElMessage.error('登录失败: ' + (error?.message || '网络错误'))
  } finally {
    console.log('[LOGIN] 登录流程结束，设置loading为false')
    loading.value = false
  }
}

// 开发模式使用测试账号
const handleDevLogin = () => {
  // 开发模式使用测试账号
  loginForm.username = 'admin'
  loginForm.password = 'admin123456'
  handleLogin()
}

// 处理字段输入事件，实现实时验证
const handleFieldInput = (field: string) => {
  loginFormRef.value?.validateField(field)
}

// 处理字段失焦事件，实现失焦验证
const handleFieldBlur = (field: string) => {
  loginFormRef.value?.validateField(field, (error: string) => {
    if (error) {
      console.log(`${field} 验证失败: ${error}`)
    }
  })
}

// 测试登录API是否可用
const testLoginApi = async () => {
  try {
    console.log('开始测试登录API...')
    const response = await fetch('http://localhost:8082/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: 'admin',
        password: 'admin123456'
      })
    })
    
    console.log('API响应状态:', response.status)
    const data = await response.json()
    console.log('API响应数据:', data)
    
    // 检查返回数据的结构
    let token = null
    if (data && typeof data === 'object') {
      if ('accessToken' in data) {
        // 直接返回了登录信息
        token = data.accessToken
      } else if ('data' in data && typeof data.data === 'object') {
        // 标准响应格式 {code: 200, message: 'success', data: {...}}
        token = data.data.accessToken
      }
    }
    
    if (token) {
      console.log('登录API测试成功！获取到token:', token.slice(0, 20) + '...')
      ElMessage.success('登录API测试成功！')
    } else {
      console.error('登录API测试失败，未获取到token')
      ElMessage.error('登录API测试失败，未获取到token')
    }
  } catch (error) {
    console.error('登录API测试出错:', error)
    ElMessage.error('登录API测试出错: ' + error.message)
  }
}

// 记住密码功能
onMounted(() => {
  const savedUsername = localStorage.getItem('rememberedUsername')
  if (savedUsername) {
    loginForm.username = savedUsername
    loginForm.rememberMe = true
  }
})
</script>

<style scoped lang="scss">
@use '@/styles/variables' as *;
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $bg-color;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: $border-radius-xl;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid $gray-200;
  padding: 40px;
  transition: all 0.2s ease;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-title {
  font-size: $font-size-3xl;
  font-weight: $font-weight-semibold;
  color: $gray-900;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: $font-size-sm;
  color: $gray-600;
  margin: 0;
}

.login-form {
  :deep(.el-input__wrapper) {
    border-radius: $input-radius;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    border: 1px solid $gray-300;
    
    &:hover {
      border-color: $primary-color;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    }
    
    &.is-focus {
      border-color: $primary-color;
      box-shadow: 0 0 0 3px rgba($primary-color, 0.2);
    }
  }
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: $button-radius;
  font-size: $font-size-lg;
  font-weight: $font-weight-medium;
  background: $primary-color;
  border: none;
  transition: all 0.2s ease;
  
  &:hover {
    background: $primary-dark;
    transform: translateY(-1px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }
  
  &:active {
    transform: translateY(0);
  }
}

.login-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  
  .el-button {
    color: $gray-600;
    
    &:hover {
      color: $primary-color;
    }
  }
}

.wechat-login-content {
  text-align: center;
  padding: 20px 0;
}

.wechat-login-tip {
  font-size: 14px;
  color: #999;
  margin-top: 16px;
}

// 响应式设计
@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }
  
  .login-title {
    font-size: 24px;
  }
}
</style>