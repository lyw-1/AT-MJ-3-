<template>
  <div class="color-input-container">
    <h3 class="section-title">基础颜色</h3>
    
    <div class="color-input-wrapper">
      <!-- 颜色预览 -->
      <div 
        class="color-preview"
        :style="{ backgroundColor: currentColor.hex }"
        @click="showColorPicker = !showColorPicker"
      >
        <div class="color-code">{{ currentColor.hex }}</div>
      </div>
      
      <!-- 颜色选择器 -->
      <el-popover
        v-model:visible="showColorPicker"
        placement="bottom-start"
        trigger="manual"
      >
        <el-color-picker
          v-model="pickerColor"
          show-alpha
          @change="handleColorChange"
          class="color-picker"
        />
      </el-popover>
      
      <!-- 手动输入 -->
      <el-input
        v-model="inputColor"
        placeholder="输入颜色值 (#RRGGBB)"
        @change="handleInputChange"
        @blur="validateInput"
        clearable
        class="color-input"
      >
        <template #prepend>#</template>
      </el-input>
      
      <!-- 随机生成按钮 -->
      <el-button
        type="primary"
        @click="generateRandomColor"
        class="random-button"
        icon="el-icon-random"
      >
        随机
      </el-button>
    </div>
    
    <!-- 预设主题 -->
    <div class="preset-themes">
      <h4 class="subsection-title">预设主题</h4>
      <div class="theme-grid">
        <div
          v-for="theme in presetThemes"
          :key="theme.name"
          class="theme-item"
          @click="selectTheme(theme)"
        >
          <div class="theme-colors">
            <div
              v-for="(color, index) in theme.colors"
              :key="index"
              class="theme-color"
              :style="{ backgroundColor: color }"
            ></div>
          </div>
          <div class="theme-name">{{ theme.name }}</div>
        </div>
      </div>
    </div>
    
    <!-- RGB 显示 -->
    <div class="rgb-info">
      <span class="label">RGB:</span>
      <span class="value">{{ currentColor.rgb.r }}, {{ currentColor.rgb.g }}, {{ currentColor.rgb.b }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { ColorConverter, Color } from '../../utils/colorUtils';

// 定义组件属性
interface Props {
  modelValue: Color;
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ColorConverter.toColor('#6150BF') // 默认使用系统主色
});

// 定义事件
const emit = defineEmits<{
  'update:modelValue': [value: Color];
  'color-change': [value: Color];
}>();

// 组件状态
const showColorPicker = ref(false);
const pickerColor = ref(props.modelValue.hex);
const inputColor = ref(props.modelValue.hex.replace('#', ''));
const currentColor = ref(props.modelValue);

// 预设主题
const presetThemes = [
  {
    name: '紫色主题',
    colors: ['#6150BF', '#4F46E5', '#8B5CF6', '#A855F7', '#C084FC']
  },
  {
    name: '蓝色主题',
    colors: ['#3B82F6', '#2563EB', '#1D4ED8', '#1E40AF', '#1E3A8A']
  },
  {
    name: '绿色主题',
    colors: ['#10B981', '#059669', '#047857', '#065F46', '#064E3B']
  },
  {
    name: '橙色主题',
    colors: ['#F97316', '#EA580C', '#D97706', '#B45309', '#92400E']
  },
  {
    name: '红色主题',
    colors: ['#EF4444', '#DC2626', '#B91C1C', '#991B1B', '#7F1D1D']
  },
  {
    name: '中性主题',
    colors: ['#6B7280', '#4B5563', '#374151', '#1F2937', '#111827']
  }
];

// 监听外部颜色变化
watch(
  () => props.modelValue,
  (newColor) => {
    currentColor.value = newColor;
    pickerColor.value = newColor.hex;
    inputColor.value = newColor.hex.replace('#', '');
  },
  { deep: true }
);

// 处理颜色选择器变化
const handleColorChange = (color: string) => {
  if (color) {
    updateColor(color);
  }
};

// 处理输入框变化
const handleInputChange = () => {
  validateInput();
};

// 验证输入并更新颜色
const validateInput = () => {
  let hex = inputColor.value.trim();
  if (!hex.startsWith('#')) {
    hex = `#${hex}`;
  }
  
  if (ColorConverter.isValidHex(hex)) {
    updateColor(hex);
  } else {
    // 输入无效，恢复为当前颜色
    inputColor.value = currentColor.value.hex.replace('#', '');
  }
};

// 生成随机颜色
const generateRandomColor = () => {
  const randomHex = ColorConverter.randomHex();
  updateColor(randomHex);
};

// 选择预设主题
const selectTheme = (theme: { name: string; colors: string[] }) => {
  updateColor(theme.colors[0]);
};

// 更新颜色
const updateColor = (hex: string) => {
  const color = ColorConverter.toColor(hex);
  currentColor.value = color;
  pickerColor.value = color.hex;
  inputColor.value = color.hex.replace('#', '');
  
  emit('update:modelValue', color);
  emit('color-change', color);
};
</script>

<style scoped>
.color-input-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.color-input-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.color-preview {
  width: 80px;
  height: 40px;
  border-radius: 8px;
  border: 2px solid #E5E7EB;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.color-preview:hover {
  border-color: #6150BF;
  box-shadow: 0 2px 8px rgba(97, 80, 191, 0.2);
}

.color-code {
  font-size: 12px;
  font-weight: 600;
  color: #FFFFFF;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.color-picker {
  width: 100%;
}

.color-input {
  flex: 1;
}

.random-button {
  white-space: nowrap;
}

.preset-themes {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.subsection-title {
  font-size: 14px;
  font-weight: 500;
  color: #4B5563;
  margin: 0;
}

.theme-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.theme-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s ease;
  border: 1px solid #E5E7EB;
}

.theme-item:hover {
  background-color: #F9FAFB;
  border-color: #6150BF;
  transform: translateY(-1px);
}

.theme-colors {
  display: flex;
  height: 30px;
  border-radius: 6px;
  overflow: hidden;
}

.theme-color {
  flex: 1;
  height: 100%;
}

.theme-name {
  font-size: 12px;
  text-align: center;
  color: #6B7280;
  font-weight: 500;
}

.rgb-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #6B7280;
}

.rgb-info .label {
  font-weight: 500;
}

.rgb-info .value {
  font-family: 'Courier New', Courier, monospace;
}
</style>
