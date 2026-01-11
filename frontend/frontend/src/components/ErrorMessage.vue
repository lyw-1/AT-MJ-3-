<template>
  <div class="error-message-container" :class="type" @mouseenter="isHovered = true" @mouseleave="isHovered = false">
    <div class="message-icon">
      <el-icon v-if="type === 'success'" :class="{ 'pulse-animation': isHovered }"><CircleCheck /></el-icon>
      <el-icon v-else-if="type === 'warning'" :class="{ 'pulse-animation': isHovered }"><Warning /></el-icon>
      <el-icon v-else-if="type === 'error'" :class="{ 'pulse-animation': isHovered }"><CircleClose /></el-icon>
      <el-icon v-else-if="type === 'info'" :class="{ 'pulse-animation': isHovered }"><Information /></el-icon>
    </div>
    <div class="message-content">
      <h3 class="message-title" v-if="title">{{ title }}</h3>
      <p class="message-text">{{ message }}</p>
    </div>
    <div class="message-actions" v-if="showClose || actions.length > 0">
      <el-button v-if="showClose" type="text" @click="handleClose" class="close-button">
        <el-icon><Close /></el-icon>
      </el-button>
      <el-button v-for="action in actions" :key="action.text" :type="action.type" @click="action.callback" class="action-button">
        {{ action.text }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, ref } from 'vue'
import { CircleCheck, Warning, CircleClose, Information, Close } from '@element-plus/icons-vue'

type MessageType = 'success' | 'warning' | 'error' | 'info'

type MessageAction = {
  text: string
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text'
  callback: () => void
}

const _props = defineProps<{
  type?: MessageType
  title?: string
  message: string
  showClose?: boolean
  actions?: MessageAction[]
}>()

const emit = defineEmits<{
  close: []
}>()

// 定义悬停状态
const isHovered = ref(false)

const handleClose = () => {
  emit('close')
}
</script>

<style scoped lang="scss">
// 定义动画效果
@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulseAnimation {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.error-message-container {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  animation: messageSlideIn 0.3s ease-out;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  
  // 悬停效果
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-1px);
  }
  
  &.success {
    background-color: #f0f9eb;
    border-color: #e1f3d8;
    color: #67c23a;
    
    .message-icon {
      color: #67c23a;
    }
  }
  
  &.warning {
    background-color: #fdf6ec;
    border-color: #faecd8;
    color: #e6a23c;
    
    .message-icon {
      color: #e6a23c;
    }
  }
  
  &.error {
    background-color: #fef0f0;
    border-color: #fbc4c4;
    color: #f56c6c;
    
    .message-icon {
      color: #f56c6c;
    }
  }
  
  &.info {
    background-color: #edf2fc;
    border-color: #d9ecff;
    color: #409eff;
    
    .message-icon {
      color: #409eff;
    }
  }
  
  .message-icon {
    margin-right: 12px;
    font-size: 24px;
    transition: all 0.3s ease;
  }
  
  // 脉冲动画效果
  .pulse-animation {
    animation: pulseAnimation 0.6s ease-in-out;
  }
  
  .message-content {
    flex: 1;
    
    .message-title {
      margin: 0 0 4px 0;
      font-size: 16px;
      font-weight: 600;
      transition: color 0.3s ease;
    }
    
    .message-text {
      margin: 0;
      font-size: 14px;
      line-height: 1.5;
      transition: color 0.3s ease;
    }
  }
  
  .message-actions {
    display: flex;
    gap: 8px;
    margin-left: 16px;
  }
  
  // 关闭按钮样式优化
  .close-button {
    transition: all 0.3s ease;
    opacity: 0.6;
    padding: 4px;
    border-radius: 4px;
    
    &:hover {
      opacity: 1;
      background-color: rgba(0, 0, 0, 0.05);
      transform: rotate(90deg);
    }
  }
  
  // 操作按钮样式优化
  .action-button {
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
  }
}
</style>
