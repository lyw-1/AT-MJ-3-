<template>
  <div class="profile-container">
    <!-- 用户信息卡片 -->
    <el-card class="user-card">
      <div class="user-info-wrapper">
        <div class="user-avatar">
          <el-image
            :src="userInfo?.avatar || '/assets/images/avatar-default.png'"
            fit="cover"
            :preview-src-list="[]"
          ></el-image>
        </div>
        <div class="user-details">
          <div class="user-name">{{ userInfo?.name || '未获取' }}</div>
          <div class="user-role">{{ userInfo?.role || '普通用户' }}</div>
          <div class="user-meta">
            <el-tag class="meta-tag">
              <el-icon><PieChart /></el-icon>
              任务数: {{ taskCount || 0 }}
            </el-tag>
            <el-tag class="meta-tag">
              <el-icon><Medal /></el-icon>
              等级: {{ level || 'Lv.1' }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 功能菜单 -->
    <el-card class="menu-card">
      <template #header>
        <div class="section-title">功能菜单</div>
      </template>
      <el-menu
        :default-active="activeMenu"
        class="profile-menu"
        router
      >
        <el-menu-item index="/mold/task-list">
          <template #icon>
            <el-icon><DocumentCopy /></el-icon>
          </template>
          <span>我的任务</span>
        </el-menu-item>
        <el-menu-item index="/storage/mold-list">
          <template #icon>
            <el-icon><Box /></el-icon>
          </template>
          <span>我的模具</span>
        </el-menu-item>
        <el-menu-item index="/system/profile">
          <template #icon>
            <el-icon><SetUp /></el-icon>
          </template>
          <span>设置</span>
        </el-menu-item>
        <el-menu-item index="/system/profile">
          <template #icon>
            <el-icon><InfoFilled /></el-icon>
          </template>
          <span>关于系统</span>
        </el-menu-item>
        <el-menu-item index="/system/profile">
          <template #icon>
            <el-icon><ChatDotRound /></el-icon>
          </template>
          <span>反馈</span>
        </el-menu-item>
      </el-menu>
    </el-card>

    <!-- 退出登录按钮 -->
    <div class="logout-section">
      <el-button type="danger" @click="handleLogout" class="logout-btn">
        <el-icon><SwitchButton /></el-icon>
        退出登录
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { PieChart, Medal, DocumentCopy, Box, SetUp, InfoFilled, ChatDotRound, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeMenu = ref('/system/profile')
const taskCount = ref(0)
const level = ref('Lv.1')

// 计算用户信息
const userInfo = computed(() => userStore.userInfo)

// 退出登录处理
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
}

// 页面挂载时初始化数据
onMounted(() => {
  // 这里可以添加获取用户统计数据的逻辑
  // 目前使用模拟数据
  taskCount.value = 5
  level.value = 'Lv.3'
})
</script>

<style scoped>
.profile-container {
  max-width: 960px;
  margin: 0 auto;
  padding: 20px;
}

/* 用户卡片样式 */
.user-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  border-radius: 12px;
}

.user-info-wrapper {
  display: flex;
  align-items: center;
  gap: 24px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 8px;
  line-height: 1.3;
}

.user-role {
  font-size: 16px;
  color: #6b7280;
  margin-bottom: 12px;
}

.user-meta {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.meta-tag {
  background-color: #f0f9ff;
  color: #0ea5e9;
  border-color: transparent;
}

/* 菜单卡片样式 */
.menu-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  border-radius: 12px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #6b7280;
}

.profile-menu {
  border: none;
  background-color: transparent;
}

.profile-menu .el-menu-item {
  height: 56px;
  line-height: 56px;
  border-radius: 8px;
  margin-bottom: 4px;
  transition: all 0.2s ease;
}

.profile-menu .el-menu-item:hover {
  background-color: #f0f9ff;
}

.profile-menu .el-menu-item.is-active {
  background-color: #e0f2fe;
  color: #0ea5e9;
}

/* 退出登录区域 */
.logout-section {
  display: flex;
  justify-content: center;
}

.logout-btn {
  width: 100%;
  max-width: 320px;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-info-wrapper {
    flex-direction: column;
    text-align: center;
  }

  .user-details {
    text-align: center;
  }

  .user-meta {
    justify-content: center;
  }
}
</style>
