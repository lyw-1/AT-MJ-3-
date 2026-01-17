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
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
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
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { LoginCredentials } from '@/types'

const router = useRouter()
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
    // 跳过表单验证，直接登录
    console.log('[LOGIN] 直接开始登录')
    loading.value = true
    
    // 简单处理：直接调用后端API获取token
    console.log('[LOGIN] 直接调用登录API')
    const response = await fetch('http://localhost:8082/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: loginForm.username,
        password: loginForm.password
      })
    });
    
    const result = await response.json();
    console.log('[LOGIN] 登录API返回结果:', result);
    
    // 检查登录结果
    if (result && result.code === 200 && result.data) {
      ElMessage.success('登录成功')
      console.log('[LOGIN] 登录成功，保存token并跳转到首页')
      
      // 保存token和用户信息到localStorage
      const token = result.data.accessToken || result.data.token
      localStorage.setItem('token', token)
      
      // 保存用户信息
      const userInfo = {
        id: result.data.userId || result.data.user_id,
        username: result.data.username,
        realName: result.data.realName || result.data.name,
        roles: result.data.roles || []
      }
      localStorage.setItem('user_info', JSON.stringify(userInfo))
      
      // 跳转到首页
      console.log('[LOGIN] 跳转到首页')
      window.location.href = '/home'
    } else {
      console.error('[LOGIN] 登录失败，服务器返回无效结果')
      ElMessage.error('登录失败: ' + (result?.message || '未知错误'))
    }
  } catch (error) {
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
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FFFFFF;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.login-form {
  :deep(.el-input__wrapper) {
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    
    &:hover {
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
    }
    
    &.is-focus {
      box-shadow: 0 0 0 2px rgba(97, 80, 191, 0.2);
    }
  }
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(135deg, #6150BF 0%, #4F46E5 100%);
  border: none;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(97, 80, 191, 0.3);
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
    color: #666;
    
    &:hover {
      color: #6150BF;
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