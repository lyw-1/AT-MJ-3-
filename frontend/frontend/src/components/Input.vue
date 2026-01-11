<template>
  <el-input
    :type="type"
    :model-value="modelValue"
    :placeholder="placeholder"
    :disabled="disabled"
    :size="size"
    :maxlength="maxlength"
    :minlength="minlength"
    :show-word-limit="showWordLimit"
    :readonly="readonly"
    :clearable="clearable"
    :show-password="showPassword"
    :prefix-icon="prefixIcon"
    :suffix-icon="suffixIcon"
    :autocomplete="autocomplete"
    :autofocus="autofocus"
    :name="name"
    :tabindex="tabindex"
    :validate-event="validateEvent"
    :resize="resize"
    :autosize="autosize"
    :fit="fit"
    :rows="rows"
    :cols="cols"
    :input-style="inputStyle"
    :input-class="inputClass"
    :show-message="showMessage"
    :inline-message="inlineMessage"
    :status-icon="statusIcon"
    :tag="tag"
    @input="handleInput"
    @change="handleChange"
    @blur="handleBlur"
    @focus="handleFocus"
    @clear="handleClear"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    v-bind="$attrs"
    class="custom-input"
  >
    <!-- 前缀插槽 -->
    <template #prefix>
      <slot name="prefix"></slot>
    </template>
    <!-- 后缀插槽 -->
    <template #suffix>
      <slot name="suffix"></slot>
    </template>
    <!-- 前置插槽 -->
    <template #prepend>
      <slot name="prepend"></slot>
    </template>
    <!-- 后置插槽 -->
    <template #append>
      <slot name="append"></slot>
    </template>
  </el-input>
</template>

<script setup lang="ts">
import { ElInput } from 'element-plus';

// 定义组件的Props
interface InputProps {
  // 绑定值
  modelValue: string | number;
  // 输入框类型
  type?: 'text' | 'password' | 'textarea' | 'number' | 'email' | 'url' | 'date' | 'datetime-local' | 'month' | 'time' | 'week';
  // 占位文本
  placeholder?: string;
  // 是否禁用
  disabled?: boolean;
  // 输入框尺寸
  size?: 'large' | 'default' | 'small';
  // 最大输入长度
  maxlength?: number;
  // 最小输入长度
  minlength?: number;
  // 是否显示输入字数统计
  showWordLimit?: boolean;
  // 是否只读
  readonly?: boolean;
  // 是否可清空
  clearable?: boolean;
  // 是否显示切换密码图标
  showPassword?: boolean;
  // 前缀图标
  prefixIcon?: string;
  // 后缀图标
  suffixIcon?: string;
  // 原生autocomplete属性
  autocomplete?: string;
  // 是否自动获取焦点
  autofocus?: boolean;
  // 原生name属性
  name?: string;
  // 原生tabindex属性
  tabindex?: number;
  // 是否触发表单验证
  validateEvent?: boolean;
  // 文本域是否可调整大小
  resize?: 'none' | 'both' | 'horizontal' | 'vertical';
  // 文本域自适应高度
  autosize?: boolean | { minRows: number; maxRows: number };
  // 图标尺寸适应方式
  fit?: 'cover' | 'contain' | 'fill' | 'none' | 'scale-down';
  // 输入框行数，仅在textarea类型下有效
  rows?: number;
  // 输入框列数，仅在textarea类型下有效
  cols?: number;
  // 输入框样式
  inputStyle?: string | object;
  // 输入框类名
  inputClass?: string | number;
  // 是否显示校验信息
  showMessage?: boolean;
  // 是否为行内表单校验信息
  inlineMessage?: boolean;
  // 是否显示校验状态图标
  statusIcon?: boolean;
  // 自定义标签
  tag?: string;
}

// 定义组件的Events
interface InputEmits {
  // 输入事件
  (e: 'update:modelValue', value: string | number): void;
  // 输入事件
  (e: 'input', value: string | number): void;
  // 失去焦点事件
  (e: 'blur', value: FocusEvent): void;
  // 获得焦点事件
  (e: 'focus', value: FocusEvent): void;
  // 输入值改变事件
  (e: 'change', value: string | number): void;
  // 清空事件
  (e: 'clear'): void;
  // 鼠标进入事件
  (e: 'mouseenter'): void;
  // 鼠标离开事件
  (e: 'mouseleave'): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<InputProps>(), {
  type: 'text',
  placeholder: '',
  disabled: false,
  size: 'default',
  maxlength: undefined,
  minlength: undefined,
  showWordLimit: false,
  readonly: false,
  clearable: false,
  showPassword: false,
  prefixIcon: '',
  suffixIcon: '',
  autocomplete: 'off',
  autofocus: false,
  name: '',
  tabindex: 0,
  validateEvent: true,
  resize: 'none',
  autosize: false,
  fit: 'cover',
  rows: 2,
  cols: 20,
  inputStyle: {},
  inputClass: '',
  showMessage: true,
  inlineMessage: false,
  statusIcon: false,
  tag: 'input'
});

// 定义组件的Events
const emit = defineEmits<InputEmits>();

// 处理输入事件
const handleInput = (value: string | number) => {
  emit('update:modelValue', value);
  emit('input', value);
};

// 处理值改变事件
const handleChange = (value: string | number) => {
  emit('change', value);
};

// 处理失去焦点事件
const handleBlur = (e: FocusEvent) => {
  emit('blur', e);
};

// 处理获得焦点事件
const handleFocus = (e: FocusEvent) => {
  emit('focus', e);
};

// 处理清空事件
const handleClear = () => {
  emit('clear');
};

// 处理鼠标进入事件
const handleMouseEnter = () => {
  emit('mouseenter');
};

// 处理鼠标离开事件
const handleMouseLeave = () => {
  emit('mouseleave');
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 自定义输入框样式 - 增强交互体验
.custom-input {
  // 确保输入框样式符合设计规范
  font-family: inherit;
  
  // 平滑过渡效果
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  // 输入框容器
  &.el-input {
    // 统一圆角
    border-radius: $input-radius;
    
    // 焦点效果增强 - 更流畅的过渡
    &:focus-within {
      box-shadow: 0 0 0 3px rgba($primary-color, 0.15);
      border-color: $primary-color;
      transform: translateY(-1px);
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
    }
    
    // 禁用状态优化
    &.is-disabled {
      opacity: 0.6;
      cursor: not-allowed;
      
      .el-input__inner {
        background-color: #f5f7fa;
        border-color: #e4e7ed;
        color: rgba(0, 0, 0, 0.25);
      }
    }
    
    // 只读状态优化
    &.is-readonly {
      .el-input__inner {
        background-color: #f5f7fa;
        color: rgba(0, 0, 0, 0.65);
        cursor: not-allowed;
      }
    }
  }
  
  // 输入框内部样式
  .el-input__inner {
    // 统一圆角
    border-radius: $input-radius;
    
    // 更流畅的过渡动画
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    // 字体设置
    font-size: $font-size-base;
    font-family: inherit;
    
    // 边框样式
    border: 1px solid #dcdfe6;
    
    // 背景渐变效果
    background: linear-gradient(145deg, #ffffff, #f5f7fa);
    
    // 输入时的视觉反馈
    &:active {
      box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    
    // 聚焦效果 - 更明显的视觉反馈
    &:focus {
      border-color: $primary-color;
      box-shadow: 0 0 0 3px rgba($primary-color, 0.15);
      background: #ffffff;
    }
    
    // 鼠标悬停效果 - 更细腻的过渡
    &:hover {
      border-color: $primary-color;
      box-shadow: 0 0 0 1px rgba($primary-color, 0.1);
      background: #ffffff;
    }
    
    // 错误状态 - 更突出的视觉提示
    &.is-error {
      border-color: $error-color;
      background: linear-gradient(145deg, #fff5f5, #ffeaea);
      
      &:focus {
        border-color: $error-color;
        box-shadow: 0 0 0 3px rgba($error-color, 0.15);
      }
      
      &:hover {
        box-shadow: 0 0 0 1px rgba($error-color, 0.1);
      }
    }
    
    // 成功状态 - 更友好的视觉提示
    &.is-success {
      border-color: $success-color;
      background: linear-gradient(145deg, #f0fff4, #e6f9e9);
      
      &:focus {
        border-color: $success-color;
        box-shadow: 0 0 0 3px rgba($success-color, 0.15);
      }
      
      &:hover {
        box-shadow: 0 0 0 1px rgba($success-color, 0.1);
      }
    }
  }
  
  // 清空按钮优化
  .el-input__clear {
    // 平滑过渡
    transition: all 0.3s ease;
    
    // 悬停效果
    &:hover {
      color: $error-color;
      transform: scale(1.1);
    }
    
    // 显示/隐藏动画
    animation: fadeIn 0.3s ease;
  }
  
  // 密码切换按钮优化
  .el-input__password {
    // 平滑过渡
    transition: all 0.3s ease;
    
    // 悬停效果
    &:hover {
      color: $primary-color;
    }
  }
  
  // 前缀图标优化
  .el-input__prefix {
    .el-input__icon {
      // 平滑过渡
      transition: all 0.3s ease;
      
      &:hover {
        color: $primary-color;
      }
    }
  }
  
  // 后缀图标优化
  .el-input__suffix {
    .el-input__icon {
      // 平滑过渡
      transition: all 0.3s ease;
      
      &:hover {
        color: $primary-color;
      }
    }
  }
  
  // 字数统计优化
  .el-input__count {
    font-size: $font-size-xs;
    color: $gray-500;
    
    // 超出字数时的样式
    &.is-warning {
      color: $warning-color;
    }
  }
  
  // 文本域样式优化
  &.el-input--textarea {
    .el-input__inner {
      resize: vertical;
      min-height: 80px;
      
      // 聚焦效果
      &:focus {
        box-shadow: none;
        border-color: $primary-color;
      }
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
