<template>
  <el-form-item
    :prop="prop"
    :label="label"
    :label-width="labelWidth"
    :required="required"
    :rules="rules"
    :error="error"
    :validate-status="validateStatus"
    :inline-message="inlineMessage"
    :show-message="showMessage"
    :size="size"
    :tag="tag"
    @validate="handleValidate"
    v-bind="$attrs"
    class="custom-form-item"
  >
    <!-- 表单项内容插槽 -->
    <slot></slot>
    <!-- 辅助文本插槽 -->
    <template #help>
      <div class="custom-help-text">
        <slot name="help"></slot>
      </div>
    </template>
    <!-- 错误信息插槽 -->
    <template #error>
      <div class="custom-error-text">
        <slot name="error"></slot>
      </div>
    </template>
    <!-- 额外内容插槽 -->
    <template #extra>
      <div class="custom-extra-text">
        <slot name="extra"></slot>
      </div>
    </template>
  </el-form-item>
</template>

<script setup lang="ts">
import { ElFormItem } from 'element-plus';

// 定义组件的Props
interface FormItemProps {
  // 表单域model字段
  prop?: string;
  // 标签文本
  label?: string;
  // 标签宽度
  labelWidth?: string | number;
  // 是否必填
  required?: boolean;
  // 表单验证规则
  rules?: any | any[];
  // 错误信息
  error?: string;
  // 验证状态
  validateStatus?: 'success' | 'warning' | 'error' | 'validating';
  // 是否为行内表单校验信息
  inlineMessage?: boolean;
  // 是否显示校验信息
  showMessage?: boolean;
  // 表单项尺寸
  size?: 'large' | 'default' | 'small';
  // 自定义标签
  tag?: string;
}

// 定义组件的Events
interface FormItemEmits {
  // 验证事件
  (e: 'validate', prop: string, isValid: boolean, message: string): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<FormItemProps>(), {
  prop: '',
  label: '',
  labelWidth: '',
  required: false,
  rules: () => [],
  error: '',
  validateStatus: undefined,
  inlineMessage: undefined,
  showMessage: true,
  size: 'default',
  tag: 'div'
});

// 定义组件的Events
const emit = defineEmits<FormItemEmits>();

// 处理验证事件
const handleValidate = (prop: string, isValid: boolean, message: string) => {
  emit('validate', prop, isValid, message);
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 定义表单项的自定义样式
.custom-form-item {
  // 确保表单项样式符合设计规范
  font-family: inherit;
  margin-bottom: 20px;
  // 更流畅的过渡动画
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  // 表单项标签样式优化
  & .el-form-item__label {
    font-weight: $font-weight-medium;
    color: $gray-700;
    font-size: $font-size-base;
    padding-right: 12px;
    // 更细腻的过渡效果
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    // 必填项星号样式优化
    &::before {
      color: $error-color;
      margin-right: 4px;
      content: "*";
      // 星号动画效果
      animation: pulse 1.5s infinite;
    }
    
    &.is-required::before {
      visibility: visible;
    }
    
    &.is-no-required::before {
      visibility: hidden;
    }
    
    // 标签悬停效果
    &:hover {
      color: $primary-color;
    }
  }
  
  // 表单项内容区域样式
  & .el-form-item__content {
    line-height: 1.5;
    font-size: $font-size-base;
    // 更细腻的过渡效果
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }
  
  // 辅助文本样式优化
  & .el-form-item__help {
    color: $gray-500;
    font-size: $font-size-xs;
    line-height: 1.5;
    margin-top: 4px;
    // 更细腻的过渡效果
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    // 自定义辅助文本样式
    .custom-help-text {
      color: $gray-500;
      font-size: $font-size-xs;
      line-height: 1.5;
    }
  }
  
  // 错误信息样式优化
  & .el-form-item__error {
    color: $error-color;
    font-size: $font-size-xs;
    line-height: 1.5;
    margin-top: 4px;
    // 更细腻的过渡效果
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    animation: fadeIn 0.3s ease-in-out;
    // 错误信息背景
    padding: 4px 8px;
    background-color: rgba($error-color, 0.05);
    border-radius: $border-radius-sm;
    border-left: 3px solid $error-color;
    
    // 自定义错误信息样式
    .custom-error-text {
      color: $error-color;
      font-size: $font-size-xs;
      line-height: 1.5;
    }
  }
  
  // 额外内容样式
  .custom-extra-text {
    color: $gray-600;
    font-size: $font-size-xs;
    line-height: 1.5;
    margin-top: 4px;
  }
  
  // 聚焦状态样式 - 更明显的视觉反馈
  &:focus-within {
    .el-form-item__label {
      color: $primary-color;
      // 标签缩放效果
      transform: translateX(2px);
    }
    
    // 聚焦时内容区域轻微上浮
    .el-form-item__content {
      transform: translateY(-2px);
    }
  }
  
  // 验证状态样式 - 更流畅的过渡
  &.is-success {
    .el-form-item__label {
      color: $success-color;
      // 成功状态动画
      animation: successPulse 0.5s ease-in-out;
    }
  }
  
  &.is-warning {
    .el-form-item__label {
      color: $warning-color;
      // 警告状态动画
      animation: warningPulse 0.5s ease-in-out;
    }
  }
  
  &.is-error {
    .el-form-item__label {
      color: $error-color;
      // 错误状态动画
      animation: errorPulse 0.5s ease-in-out;
    }
    
    // 错误状态下的输入框容器阴影
    :deep(.el-input),
    :deep(.el-select) {
      box-shadow: 0 0 0 2px rgba($error-color, 0.1);
    }
  }
  
  &.is-validating {
    .el-form-item__label {
      color: $primary-color;
      // 验证中状态动画
      animation: validatingPulse 1s ease-in-out infinite;
    }
  }
}

// 淡入动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-4px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 验证中脉冲动画
@keyframes validatingPulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

// 成功脉冲动画
@keyframes successPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

// 警告脉冲动画
@keyframes warningPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

// 错误脉冲动画
@keyframes errorPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

// 星号脉冲动画
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}
</style>
