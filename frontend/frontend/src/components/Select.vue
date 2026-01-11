<template>
  <el-select
    :model-value="modelValue"
    :multiple="multiple"
    :disabled="disabled"
    :size="size"
    :clearable="clearable"
    :collapse-tags="collapseTags"
    :multiple-limit="multipleLimit"
    :name="name"
    :autocomplete="autocomplete"
    :autofocus="autofocus"
    :placeholder="placeholder"
    :filterable="filterable"
    :allow-create="allowCreate"
    :filter-method="filterMethod"
    :remote="remote"
    :remote-method="remoteMethod"
    :loading="loading"
    :loading-text="loadingText"
    :no-match-text="noMatchText"
    :no-data-text="noDataText"
    :popper-class="`custom-select-dropdown ${popperClass}`"
    :reserve-keyword="reserveKeyword"
    :default-first-option="defaultFirstOption"
    :teleported="teleported"
    :persistent="persistent"
    :automatic-dropdown="automaticDropdown"
    :fit-input-width="fitInputWidth"
    :suffix-icon="suffixIcon"
    :prefix-icon="prefixIcon"
    :clear-icon="clearIcon"
    :check-icon="checkIcon"
    :create-icon="createIcon"
    :effect="effect"
    :fallback-option="fallbackOption"
    :highlight-first-item="highlightFirstItem"
    :tag="tag"
    @change="handleChange"
    @visible-change="handleVisibleChange"
    @remove-tag="handleRemoveTag"
    @clear="handleClear"
    @blur="handleBlur"
    @focus="handleFocus"
    @popper-append-to-body="handlePopperAppendToBody"
    @remove-all-tags="handleRemoveAllTags"
    @current-change="handleCurrentChange"
    @focus-change="handleFocusChange"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    @keydown="handleKeydown"
    v-bind="$attrs"
    class="custom-select"
  >
    <!-- 选项插槽 -->
    <slot></slot>
    
    <!-- 自定义前缀图标插槽 -->
    <template #prefix>
      <slot name="prefix"></slot>
    </template>
    
    <!-- 自定义后缀图标插槽 -->
    <template #suffix>
      <slot name="suffix"></slot>
    </template>
    
    <!-- 自定义清除图标插槽 -->
    <template #clear>
      <slot name="clear"></slot>
    </template>
    
    <!-- 自定义检查图标插槽 -->
    <template #check>
      <slot name="check"></slot>
    </template>
    
    <!-- 自定义创建图标插槽 -->
    <template #create>
      <slot name="create"></slot>
    </template>
    
    <!-- 自定义标签插槽 -->
    <template #tag>
      <slot name="tag"></slot>
    </template>
    
    <!-- 自定义空状态插槽 -->
    <template #empty>
      <div class="custom-empty">
        <el-icon :size="24" color="#c0c4cc"><Search /></el-icon>
        <p>{{ noMatchText || noDataText }}</p>
      </div>
    </template>
    
    <!-- 自定义加载状态插槽 -->
    <template #loading>
      <div class="custom-loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>{{ loadingText }}</span>
      </div>
    </template>
    
    <!-- 自定义下拉菜单底部插槽 -->
    <template #popper-bottom>
      <slot name="popper-bottom"></slot>
    </template>
  </el-select>
</template>

<script setup lang="ts">
import { ElSelect, Search, Loading } from 'element-plus';

// 定义组件的Props
interface SelectProps {
  // 绑定值
  modelValue: string | number | boolean | object | (string | number | boolean | object)[];
  // 是否多选
  multiple?: boolean;
  // 是否禁用
  disabled?: boolean;
  // 尺寸
  size?: 'large' | 'default' | 'small';
  // 是否可清空
  clearable?: boolean;
  // 是否折叠多选标签
  collapseTags?: boolean;
  // 多选时最多显示的标签数
  multipleLimit?: number;
  // 原生name属性
  name?: string;
  // 原生autocomplete属性
  autocomplete?: string;
  // 是否自动获取焦点
  autofocus?: boolean;
  // 占位文本
  placeholder?: string;
  // 是否可搜索
  filterable?: boolean;
  // 是否允许创建新条目
  allowCreate?: boolean;
  // 自定义搜索方法
  filterMethod?: (query: string) => void;
  // 是否为远程搜索
  remote?: boolean;
  // 远程搜索方法
  remoteMethod?: (query: string) => void;
  // 是否正在加载
  loading?: boolean;
  // 加载时的文本
  loadingText?: string;
  // 无匹配结果的文本
  noMatchText?: string;
  // 无数据时的文本
  noDataText?: string;
  // 下拉框的类名
  popperClass?: string;
  // 是否保留关键字
  reserveKeyword?: boolean;
  // 是否默认选中第一个选项
  defaultFirstOption?: boolean;
  // 是否使用teleport
  teleported?: boolean;
  // 是否保持下拉框显示
  persistent?: boolean;
  // 是否自动弹出下拉框
  automaticDropdown?: boolean;
  // 下拉框是否适应输入框宽度
  fitInputWidth?: boolean;
  // 自定义后缀图标
  suffixIcon?: string;
  // 自定义前缀图标
  prefixIcon?: string;
  // 自定义清除图标
  clearIcon?: string;
  // 自定义选中图标
  checkIcon?: string;
  // 自定义创建图标
  createIcon?: string;
  // 主题效果
  effect?: 'light' | 'dark' | 'plain';
  // 回退选项
  fallbackOption?: any;
  // 是否高亮第一个选项
  highlightFirstItem?: boolean;
  // 自定义标签
  tag?: string;
}

// 定义组件的Events
interface SelectEmits {
  // 绑定值变化事件
  (e: 'update:modelValue', value: string | number | boolean | object | (string | number | boolean | object)[]): void;
  // 选中值变化事件
  (e: 'change', value: string | number | boolean | object | (string | number | boolean | object)[]): void;
  // 下拉框显示/隐藏事件
  (e: 'visible-change', visible: boolean): void;
  // 移除标签事件
  (e: 'remove-tag', tag: any): void;
  // 清空事件
  (e: 'clear'): void;
  // 失去焦点事件
  (e: 'blur', e: FocusEvent): void;
  // 获得焦点事件
  (e: 'focus', e: FocusEvent): void;
  // 下拉框挂载到body事件
  (e: 'popper-append-to-body', appendToBody: boolean): void;
  // 移除所有标签事件
  (e: 'remove-all-tags'): void;
  // 当前选中项变化事件
  (e: 'current-change', current: any): void;
  // 焦点变化事件
  (e: 'focus-change', focused: boolean): void;
  // 鼠标进入事件
  (e: 'mouseenter', e: MouseEvent): void;
  // 鼠标离开事件
  (e: 'mouseleave', e: MouseEvent): void;
  // 键盘事件
  (e: 'keydown', e: KeyboardEvent): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<SelectProps>(), {
  multiple: false,
  disabled: false,
  size: 'default',
  clearable: false,
  collapseTags: false,
  multipleLimit: 0,
  name: '',
  autocomplete: 'off',
  autofocus: false,
  placeholder: '',
  filterable: false,
  allowCreate: false,
  filterMethod: undefined,
  remote: false,
  remoteMethod: undefined,
  loading: false,
  loadingText: '加载中',
  noMatchText: '无匹配数据',
  noDataText: '无数据',
  popperClass: '',
  reserveKeyword: true,
  defaultFirstOption: false,
  teleported: true,
  persistent: false,
  automaticDropdown: false,
  fitInputWidth: false,
  suffixIcon: '',
  prefixIcon: '',
  clearIcon: '',
  checkIcon: '',
  createIcon: '',
  effect: 'light',
  fallbackOption: undefined,
  highlightFirstItem: false,
  tag: 'select'
});

// 定义组件的Events
const emit = defineEmits<SelectEmits>();

// 处理绑定值变化事件
const handleChange = (value: string | number | boolean | object | (string | number | boolean | object)[]) => {
  emit('update:modelValue', value);
  emit('change', value);
};

// 处理下拉框显示/隐藏事件
const handleVisibleChange = (visible: boolean) => {
  emit('visible-change', visible);
};

// 处理移除标签事件
const handleRemoveTag = (tag: any) => {
  emit('remove-tag', tag);
};

// 处理清空事件
const handleClear = () => {
  emit('clear');
};

// 处理失去焦点事件
const handleBlur = (e: FocusEvent) => {
  emit('blur', e);
};

// 处理获得焦点事件
const handleFocus = (e: FocusEvent) => {
  emit('focus', e);
};

// 处理下拉框挂载到body事件
const handlePopperAppendToBody = (appendToBody: boolean) => {
  emit('popper-append-to-body', appendToBody);
};

// 处理移除所有标签事件
const handleRemoveAllTags = () => {
  emit('remove-all-tags');
};

// 处理当前选中项变化事件
const handleCurrentChange = (current: any) => {
  emit('current-change', current);
};

// 处理焦点变化事件
const handleFocusChange = (focused: boolean) => {
  emit('focus-change', focused);
};

// 处理鼠标进入事件
const handleMouseEnter = (e: MouseEvent) => {
  emit('mouseenter', e);
};

// 处理鼠标离开事件
const handleMouseLeave = (e: MouseEvent) => {
  emit('mouseleave', e);
};

// 处理键盘事件
const handleKeydown = (e: KeyboardEvent) => {
  emit('keydown', e);
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 自定义选择器样式 - 增强交互体验
.custom-select {
  // 确保选择器样式符合设计规范
  font-family: inherit;
  
  // 平滑过渡
  transition: all 0.3s ease;
  
  // 选择器容器
  &.el-select {
    // 统一圆角
    border-radius: $input-radius;
    
    // 聚焦效果增强
    &:focus-within {
      box-shadow: 0 0 0 2px rgba($primary-color, 0.2);
      border-color: $primary-color;
    }
    
    // 输入框样式
    :deep(.el-select__input) {
      color: $gray-600;
      font-size: $font-size-base;
    }
    
    // 占位符样式
    :deep(.el-select__placeholder) {
      color: $gray-400;
      font-size: $font-size-base;
    }
    
    // 清除按钮优化
    :deep(.el-select__clear) {
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
    
    // 后缀图标优化
    :deep(.el-select__caret) {
      // 平滑过渡
      transition: all 0.3s ease;
      color: $gray-500;
      
      // 展开/收起动画
      &.is-reverse {
        transform: rotate(180deg);
      }
      
      // 悬停效果
      &:hover {
        color: $primary-color;
      }
    }
    
    // 标签样式优化
    :deep(.el-select__tag) {
      background-color: $primary-light;
      border-color: $primary-light;
      color: $primary-color;
      border-radius: $border-radius-sm;
      font-size: $font-size-sm;
      
      // 平滑过渡
      transition: all 0.3s ease;
      
      &:hover {
        background-color: rgba($primary-color, 0.2);
        border-color: $primary-color;
        transform: translateY(-1px);
      }
      
      // 标签关闭按钮优化
      .el-tag__close {
        // 平滑过渡
        transition: all 0.3s ease;
        
        &:hover {
          color: $error-color;
          transform: scale(1.2);
        }
      }
    }
    
    // 折叠标签优化
    :deep(.el-select__tags-text) {
      color: $primary-color;
      font-size: $font-size-sm;
    }
    
    // 下拉指示器优化
    :deep(.el-input__suffix-inner) {
      .el-select__caret {
        transition: transform 0.3s ease;
      }
    }
  }
  
  // 下拉菜单样式
  :deep(.custom-select-dropdown) {
    // 统一圆角
    border-radius: $border-radius-md;
    // 阴影效果
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
    // 边框样式
    border: 1px solid $gray-200;
    // 背景色
    background-color: $white;
    // 平滑过渡
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    // 防止内容溢出
    overflow: hidden;
    
    // 下拉菜单显示动画
    animation: dropdownSlideIn 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    
    // 选项样式优化
    .el-select-dropdown__item {
      // 平滑过渡
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
      color: $gray-600;
      font-size: $font-size-base;
      padding: 10px 16px;
      // 防止文字溢出
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      
      // 悬停效果 - 更明显的视觉反馈
      &:hover {
        background-color: $primary-light;
        color: $primary-color;
        transform: translateX(4px);
        box-shadow: inset 4px 0 0 $primary-color;
      }
      
      // 选中状态 - 更突出的视觉提示
      &.selected {
        background-color: rgba($primary-color, 0.15);
        color: $primary-color;
        font-weight: $font-weight-medium;
        box-shadow: inset 4px 0 0 $primary-color;
      }
      
      // 高亮状态 - 更流畅的过渡
      &.highlighted {
        background-color: $primary-light;
        color: $primary-color;
        transform: translateX(2px);
      }
      
      // 禁用状态
      &.disabled {
        color: $gray-300;
        cursor: not-allowed;
        
        &:hover {
          background-color: transparent;
          color: $gray-300;
          transform: none;
          box-shadow: none;
        }
      }
    }
    
    // 下拉菜单滚动条样式优化
    &::-webkit-scrollbar {
      width: 6px;
    }
    
    &::-webkit-scrollbar-track {
      background: $gray-100;
    }
    
    &::-webkit-scrollbar-thumb {
      background: $gray-300;
      border-radius: $border-radius-sm;
      
      &:hover {
        background: $gray-400;
      }
    }
    
    // 搜索无结果样式
    .el-select-dropdown__empty {
      color: $gray-500;
      padding: 20px 0;
      text-align: center;
    }
    
    // 加载状态样式
    .el-select-dropdown__loading {
      color: $primary-color;
      padding: 20px 0;
      text-align: center;
    }
  }
  
  // 自定义空状态
  .custom-empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px 0;
    color: $gray-500;
    
    p {
      margin-top: 8px;
      font-size: $font-size-base;
    }
  }
  
  // 自定义加载状态
  .custom-loading {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 20px 0;
    color: $primary-color;
    
    .el-icon {
      font-size: 18px;
      animation: rotate 1s linear infinite;
    }
    
    span {
      font-size: $font-size-base;
    }
  }
  
  // 禁用状态优化
  &.is-disabled {
    opacity: 0.6;
    cursor: not-allowed;
    
    :deep(.el-select__input) {
      color: $gray-300;
    }
  }
}

// 动画效果
@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

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

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
