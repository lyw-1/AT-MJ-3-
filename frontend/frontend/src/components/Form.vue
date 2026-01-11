<template>
  <el-form
    :model="model"
    :rules="rules"
    :label-position="labelPosition"
    :label-width="labelWidth"
    :label-suffix="labelSuffix"
    :inline="inline"
    :inline-message="inlineMessage"
    :status-icon="statusIcon"
    :validate-on-rule-change="validateOnRuleChange"
    :hide-required-asterisk="hideRequiredAsterisk"
    :disabled="disabled"
    :size="size"
    :show-message="showMessage"
    :tag="tag"
    @validate="handleValidate"
    @submit="handleSubmit"
    @reset="handleReset"
    v-bind="$attrs"
    class="custom-form"
    ref="formRef"
  >
    <slot></slot>
    
    <!-- 表单提交状态覆盖层 -->
    <div v-if="submitting" class="form-submitting-overlay">
      <div class="submitting-content">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>提交中...</span>
      </div>
    </div>
  </el-form>
</template>

<script setup lang="ts">
import { ElForm, Loading } from 'element-plus';
import { ref } from 'vue';

// 定义组件的Props
interface FormProps {
  // 表单数据对象
  model: Record<string, any>;
  // 表单验证规则
  rules?: Record<string, any[]>;
  // 表单域标签的位置
  labelPosition?: 'left' | 'right' | 'top';
  // 表单域标签的宽度
  labelWidth?: string | number;
  // 表单域标签的后缀
  labelSuffix?: string;
  // 是否为行内表单
  inline?: boolean;
  // 是否为行内表单校验信息
  inlineMessage?: boolean;
  // 是否显示校验状态图标
  statusIcon?: boolean;
  // 是否在表单规则改变时立即触发校验
  validateOnRuleChange?: boolean;
  // 是否隐藏必填字段的星号
  hideRequiredAsterisk?: boolean;
  // 是否禁用表单
  disabled?: boolean;
  // 表单尺寸
  size?: 'large' | 'default' | 'small';
  // 是否显示校验信息
  showMessage?: boolean;
  // 是否正在提交中
  submitting?: boolean;
  // 自定义标签
  tag?: string;
}

// 定义组件的Events
interface FormEmits {
  // 表单验证事件
  (e: 'validate', prop: string, isValid: boolean, message: string): void;
  // 表单提交事件
  (e: 'submit', e: Event): void;
  // 表单重置事件
  (e: 'reset'): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<FormProps>(), {
  rules: () => ({}),
  labelPosition: 'right',
  labelWidth: '',
  labelSuffix: '',
  inline: false,
  inlineMessage: false,
  statusIcon: true,
  validateOnRuleChange: true,
  hideRequiredAsterisk: false,
  disabled: false,
  size: 'default',
  showMessage: true,
  submitting: false,
  tag: 'form'
});

// 定义组件的Events
const emit = defineEmits<FormEmits>();

// 表单引用
const formRef = ref<InstanceType<typeof ElForm> | null>(null);

// 处理表单验证事件
const handleValidate = (prop: string, isValid: boolean, message: string) => {
  emit('validate', prop, isValid, message);
};

// 处理表单提交事件
const handleSubmit = (e: Event) => {
  e.preventDefault();
  e.stopPropagation();
  emit('submit', e);
};

// 处理表单重置事件
const handleReset = () => {
  emit('reset');
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 自定义表单样式 - 增强交互体验
.custom-form {
  // 确保表单样式符合设计规范
  font-family: inherit;
  
  // 表单间距优化
  &.el-form {
    // 统一表单域间距
    .el-form-item {
      margin-bottom: 20px;
      
      // 平滑过渡
      transition: all 0.3s ease;
      
      // 聚焦效果增强
      &:focus-within {
        .el-form-item__label {
          color: $primary-color;
        }
      }
      
      // 表单项标签优化
      .el-form-item__label {
        font-weight: $font-weight-medium;
        color: $gray-700;
        
        // 必填项星号样式
        &.is-required:not(.el-form-item__label--leave)::before {
          color: $error-color;
          margin-right: 4px;
        }
      }
      
      // 表单验证信息优化
      .el-form-item__error {
        font-size: $font-size-xs;
        color: $error-color;
        
        // 显示/隐藏动画
        animation: fadeIn 0.3s ease;
        
        // 错误提示位置优化
        margin-top: 4px;
        padding: 2px 8px;
        background-color: rgba($error-color, 0.05);
        border-radius: 4px;
        border-left: 3px solid $error-color;
      }
      
      // 成功状态样式
      &.is-success {
        .el-form-item__label {
          color: $success-color;
        }
      }
      
      // 错误状态样式
      &.is-error {
        .el-form-item__label {
          color: $error-color;
        }
      }
      
      // 警告状态样式
      &.is-warning {
        .el-form-item__label {
          color: $warning-color;
        }
      }
    }
    
    // 行内表单样式优化
    &.el-form--inline {
      .el-form-item {
        margin-right: 20px;
        margin-bottom: 10px;
      }
    }
    
    // 禁用状态样式优化
    &.is-disabled {
      opacity: 0.6;
      
      .el-form-item {
        cursor: not-allowed;
      }
    }
    
    // 状态图标优化
    &.el-form--status-icon {
      .el-input__icon {
        margin-right: 8px;
      }
    }
  }
  
  // 表单提交状态覆盖层
  .form-submitting-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(2px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    border-radius: 8px;
    
    // 平滑过渡
    animation: fadeIn 0.3s ease;
    
    .submitting-content {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 24px;
      background-color: $white;
      border-radius: $border-radius-md;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      
      .el-icon {
        font-size: 20px;
        color: $primary-color;
      }
      
      span {
        font-size: $font-size-base;
        color: $gray-700;
      }
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
