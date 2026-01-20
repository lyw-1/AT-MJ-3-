<template>
  <div class="color-adjuster">
    <h3 class="section-title">颜色调整</h3>
    
    <!-- 当前颜色预览 -->
    <div class="current-color-preview">
      <div 
        class="color-swatch"
        :style="{ backgroundColor: adjustedColor.hex }"
      ></div>
      <div class="color-values">
        <div class="color-value-item">
          <span class="value-label">HEX:</span>
          <span class="value-text">{{ adjustedColor.hex }}</span>
        </div>
        <div class="color-value-item">
          <span class="value-label">HSL:</span>
          <span class="value-text">{{ adjustedColor.hsl.h }}° {{ adjustedColor.hsl.s }}% {{ adjustedColor.hsl.l }}%</span>
        </div>
      </div>
    </div>
    
    <!-- 色相调整 -->
    <div class="adjustment-item">
      <div class="adjustment-header">
        <label class="adjustment-label">色相 (Hue)</label>
        <el-input-number
          v-model="hsl.h"
          :min="0"
          :max="360"
          :step="1"
          @change="updateColor"
          class="value-input"
          size="small"
        />
      </div>
      <el-slider
        v-model="hsl.h"
        :min="0"
        :max="360"
        :step="1"
        @change="updateColor"
        class="adjustment-slider"
        :marks="{
          0: '0°',
          90: '90°',
          180: '180°',
          270: '270°',
          360: '360°'
        }"
      />
      <div class="hue-gradient"></div>
    </div>
    
    <!-- 饱和度调整 -->
    <div class="adjustment-item">
      <div class="adjustment-header">
        <label class="adjustment-label">饱和度 (Saturation)</label>
        <el-input-number
          v-model="hsl.s"
          :min="0"
          :max="100"
          :step="1"
          @change="updateColor"
          class="value-input"
          size="small"
        />
      </div>
      <el-slider
        v-model="hsl.s"
        :min="0"
        :max="100"
        :step="1"
        @change="updateColor"
        class="adjustment-slider"
        :marks="{
          0: '0%',
          50: '50%',
          100: '100%'
        }"
      />
      <div 
        class="gradient-preview"
        :style="{ background: `linear-gradient(to right, #808080, ${saturationGradientColor})` }"
      ></div>
    </div>
    
    <!-- 明度调整 -->
    <div class="adjustment-item">
      <div class="adjustment-header">
        <label class="adjustment-label">明度 (Lightness)</label>
        <el-input-number
          v-model="hsl.l"
          :min="0"
          :max="100"
          :step="1"
          @change="updateColor"
          class="value-input"
          size="small"
        />
      </div>
      <el-slider
        v-model="hsl.l"
        :min="0"
        :max="100"
        :step="1"
        @change="updateColor"
        class="adjustment-slider"
        :marks="{
          0: '0%',
          50: '50%',
          100: '100%'
        }"
      />
      <div 
        class="gradient-preview"
        :style="{ background: `linear-gradient(to right, #000000, ${lightnessGradientColor}, #ffffff)` }"
      ></div>
    </div>
    
    <!-- 操作按钮 -->
    <div class="adjustment-actions">
      <el-button
        type="default"
        @click="resetToOriginal"
        class="reset-button"
        icon="el-icon-refresh"
      >
        重置
      </el-button>
      <el-button
        type="primary"
        @click="applyAdjustments"
        class="apply-button"
        icon="el-icon-check"
      >
        应用
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { ColorConverter, Color, HSL } from '../../utils/colorUtils';

// 定义组件属性
interface Props {
  modelValue: Color;
}

const props = defineProps<Props>();

// 定义事件
const emit = defineEmits<{
  'update:modelValue': [value: Color];
  'adjustment-change': [value: Color];
  'adjustment-apply': [value: Color];
}>();

// 组件状态
const originalColor = ref(props.modelValue); // 保存原始颜色，用于重置
const hsl = ref<HSL>({ ...props.modelValue.hsl }); // 当前HSL值
const adjustedColor = ref(props.modelValue); // 调整后的颜色

// 饱和度渐变参考色（基于当前色相和明度，只改变饱和度）
const saturationGradientColor = computed(() => {
  const tempHsl: HSL = { ...hsl.value, s: 100 };
  return ColorConverter.hslToHex(tempHsl);
});

// 明度渐变参考色（基于当前色相和饱和度，只改变明度）
const lightnessGradientColor = computed(() => {
  const tempHsl: HSL = { ...hsl.value, l: 50 };
  return ColorConverter.hslToHex(tempHsl);
});

// 监听外部颜色变化
watch(
  () => props.modelValue,
  (newColor) => {
    originalColor.value = newColor;
    hsl.value = { ...newColor.hsl };
    adjustedColor.value = newColor;
  },
  { deep: true }
);

// 更新颜色
const updateColor = () => {
  const newColor = ColorConverter.toColor(hsl.value);
  adjustedColor.value = newColor;
  emit('update:modelValue', newColor);
  emit('adjustment-change', newColor);
};

// 重置到原始颜色
const resetToOriginal = () => {
  hsl.value = { ...originalColor.value.hsl };
  adjustedColor.value = originalColor.value;
  emit('update:modelValue', originalColor.value);
  emit('adjustment-change', originalColor.value);
};

// 应用调整
const applyAdjustments = () => {
  originalColor.value = adjustedColor.value;
  emit('adjustment-apply', adjustedColor.value);
};
</script>

<style scoped>
.color-adjuster {
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

.current-color-preview {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background-color: #F9FAFB;
  border-radius: 8px;
  border: 1px solid #E5E7EB;
}

.color-swatch {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  border: 2px solid #FFFFFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.color-values {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.color-value-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.value-label {
  font-size: 12px;
  font-weight: 500;
  color: #6B7280;
  width: 35px;
}

.value-text {
  font-size: 13px;
  font-weight: 500;
  color: #374151;
  font-family: 'Courier New', Courier, monospace;
}

.adjustment-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.adjustment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.adjustment-label {
  font-size: 14px;
  font-weight: 500;
  color: #4B5563;
}

.value-input {
  width: 80px;
}

.adjustment-slider {
  margin: 0;
}

.hue-gradient {
  height: 8px;
  border-radius: 4px;
  background: linear-gradient(
    to right,
    #ff0000 0%,
    #ffff00 16.67%,
    #00ff00 33.33%,
    #00ffff 50%,
    #0000ff 66.67%,
    #ff00ff 83.33%,
    #ff0000 100%
  );
  border: 1px solid #E5E7EB;
}

.gradient-preview {
  height: 8px;
  border-radius: 4px;
  border: 1px solid #E5E7EB;
}

.adjustment-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.reset-button,
.apply-button {
  flex: 1;
}

/* 滑块样式自定义 */
:deep(.el-slider__bar) {
  background-color: #6150BF;
}

:deep(.el-slider__button) {
  border-color: #6150BF;
}

:deep(.el-slider__button:hover),
:deep(.el-slider__button:focus) {
  border-color: #4F46E5;
  box-shadow: 0 0 0 5px rgba(97, 80, 191, 0.1);
}
</style>
