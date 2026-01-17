<template>
  <el-button
    :type="type"
    :size="size"
    :plain="plain"
    :round="round"
    :circle="circle"
    :loading="loading"
    :disabled="disabled"
    :icon="icon"
    :autofocus="autofocus"
    :native-type="nativeType"
    :fit="fit"
    :tag="tag"
    :effect="effect"
    :dark="dark"
    @click="handleClick"
    v-bind="$attrs"
    class="custom-button"
  >
    <slot></slot>
  </el-button>
</template>

<script setup lang="ts">
import { ElButton } from 'element-plus';

// 定义组件的Props
interface ButtonProps {
  // 按钮类型：primary / success / warning / danger / info / text
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text';
  // 按钮尺寸：large / default / small
  size?: 'large' | 'default' | 'small';
  // 是否为朴素按钮
  plain?: boolean;
  // 是否为圆角按钮
  round?: boolean;
  // 是否为圆形按钮
  circle?: boolean;
  // 是否加载中状态
  loading?: boolean;
  // 是否禁用状态
  disabled?: boolean;
  // 按钮图标
  icon?: string;
  // 是否自动获取焦点
  autofocus?: boolean;
  // 原生type属性
  nativeType?: 'button' | 'submit' | 'reset';
  // 图标尺寸适应方式
  fit?: 'cover' | 'contain' | 'fill' | 'none' | 'scale-down';
  // 按钮标签类型
  tag?: string;
  // 主题效果
  effect?: 'light' | 'dark' | 'plain';
  // 是否为暗黑模式
  dark?: boolean;
}

// 定义组件的Events
interface ButtonEmits {
  // 点击事件
  (e: 'click', value: MouseEvent): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<ButtonProps>(), {
  type: 'default',
  size: 'default',
  plain: false,
  round: false,
  circle: false,
  loading: false,
  disabled: false,
  autofocus: false,
  nativeType: 'button',
  fit: 'cover',
  tag: 'button',
  effect: 'light',
  dark: false
});

// 定义组件的Events
const emit = defineEmits<ButtonEmits>();

// 处理点击事件
const handleClick = (e: MouseEvent) => {
  emit('click', e);
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// Clarity Design风格按钮样式
.custom-button {
  // 确保按钮样式符合Clarity Design规范
  font-family: $font-family;
  border-radius: $button-radius;
  font-weight: $font-weight-medium;
  box-shadow: none;
  transition: all 0.2s ease;
  border: 1px solid transparent;
  
  // 增强悬停效果 - Clarity Design规范
  &:not(:disabled):not(.is-loading) {
    &:hover {
      transform: translateY(0);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    }
    
    // 点击反馈 - 按压效果 - Clarity Design规范
    &:active {
      transform: translateY(1px);
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
    }
  }
  
  // 加载状态优化 - Clarity Design规范
  &.is-loading {
    // 确保加载动画在不同尺寸按钮上显示正常
    .el-icon-loading {
      margin-right: 4px;
      animation: rotate 1s linear infinite;
    }
  }
  
  // 禁用状态优化 - Clarity Design规范
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    
    // 确保禁用状态下的文字清晰可见
    color: rgba(0, 0, 0, 0.25);
    background-color: $gray-50;
    border-color: $gray-200;
  }
  
  // 主按钮样式 - Clarity Design规范
  &.el-button--primary {
    background-color: $primary-color;
    border-color: $primary-color;
    color: $white;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: $primary-dark;
      border-color: $primary-dark;
      box-shadow: 0 2px 8px rgba($primary-color, 0.3);
    }
    
    &:focus {
      box-shadow: 0 0 0 3px rgba($primary-color, 0.2);
    }
  }
  
  // 次要按钮样式 - Clarity Design规范
  &.el-button--default {
    background-color: $white;
    border-color: $gray-300;
    color: $gray-700;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: $gray-50;
      border-color: $gray-400;
      color: $gray-800;
    }
  }
  
  // 成功按钮样式 - Clarity Design规范
  &.el-button--success {
    background-color: $success-color;
    border-color: $success-color;
    color: $white;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: #{color.adjust($success-color, $lightness: -10%)};
      border-color: #{color.adjust($success-color, $lightness: -10%)};
    }
  }
  
  // 警告按钮样式 - Clarity Design规范
  &.el-button--warning {
    background-color: $warning-color;
    border-color: $warning-color;
    color: $white;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: #{color.adjust($warning-color, $lightness: -10%)};
      border-color: #{color.adjust($warning-color, $lightness: -10%)};
    }
  }
  
  // 危险按钮样式 - Clarity Design规范
  &.el-button--danger {
    background-color: $error-color;
    border-color: $error-color;
    color: $white;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: #{color.adjust($error-color, $lightness: -10%)};
      border-color: #{color.adjust($error-color, $lightness: -10%)};
    }
  }
  
  // 信息按钮样式 - Clarity Design规范
  &.el-button--info {
    background-color: $info-color;
    border-color: $info-color;
    color: $white;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: #{color.adjust($info-color, $lightness: -10%)};
      border-color: #{color.adjust($info-color, $lightness: -10%)};
    }
  }
  
  // 尺寸优化 - Clarity Design规范
  &.el-button--large {
    height: 44px;
    padding: 0 24px;
    font-size: $font-size-lg;
  }
  
  &.el-button--medium {
    height: 36px;
    padding: 0 16px;
    font-size: $font-size-base;
  }
  
  &.el-button--small {
    height: 32px;
    padding: 0 12px;
    font-size: $font-size-sm;
  }
  
  // 朴素按钮样式 - Clarity Design规范
  &.el-button--plain {
    &:not(:disabled):not(.is-loading):hover {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    }
  }
  
  // 文本按钮样式 - Clarity Design规范
  &.el-button--text {
    background-color: transparent;
    border-color: transparent;
    color: $primary-color;
    box-shadow: none;
    
    &:not(:disabled):not(.is-loading):hover {
      background-color: $primary-light;
      border-color: transparent;
    }
    
    &:active {
      transform: translateY(1px);
    }
  }
}

// 加载动画
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
