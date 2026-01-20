<template>
  <div class="color-scheme-tool">
    <header class="tool-header">
      <h1 class="tool-title">配色技能工具</h1>
      <p class="tool-description">基于色彩理论生成和谐的配色方案，提升您的设计质量</p>
    </header>
    
    <main class="tool-main">
      <!-- 左侧控制面板 -->
      <aside class="control-panel">
        <!-- 颜色输入 -->
        <ColorInput
          v-model="baseColor"
          @color-change="handleBaseColorChange"
        />
        
        <!-- 配色模式选择 -->
        <ColorSchemeSelector
          v-model="selectedSchemeType"
          @scheme-change="handleSchemeTypeChange"
        />
        
        <!-- 颜色调整 -->
        <ColorAdjuster
          v-model="adjustedBaseColor"
          @adjustment-change="handleAdjustmentChange"
        />
      </aside>
      
      <!-- 右侧预览区域 -->
      <section class="preview-panel">
        <!-- 配色方案预览 -->
        <SchemePreview
          :color-scheme="currentScheme"
          :colors="currentSchemeColors"
        />
        
        <!-- 色彩理论建议 -->
        <ColorTheoryTips
          :color-scheme="currentScheme"
        />
        
        <!-- 方案管理 -->
        <SchemeManager
          :color-scheme="currentScheme"
          @save-scheme="saveCurrentScheme"
          @load-scheme="loadScheme"
        />
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import ColorInput from '@/components/color-scheme-tool/ColorInput.vue';
import ColorSchemeSelector from '@/components/color-scheme-tool/ColorSchemeSelector.vue';
import ColorAdjuster from '@/components/color-scheme-tool/ColorAdjuster.vue';
import SchemePreview from '@/components/color-scheme-tool/SchemePreview.vue';
import ColorTheoryTips from '@/components/color-scheme-tool/ColorTheoryTips.vue';
import SchemeManager from '@/components/color-scheme-tool/SchemeManager.vue';
import { ColorConverter, Color } from '../utils/colorUtils';
import { ColorSchemeType, ColorSchemeGenerator, ColorScheme } from '../utils/colorSchemeGenerator';

// 组件状态
const baseColor = ref<Color>(ColorConverter.toColor('#6150BF')); // 默认使用系统主色
const adjustedBaseColor = ref<Color>(baseColor.value); // 调整后的基础颜色
const selectedSchemeType = ref<ColorSchemeType>(ColorSchemeType.NEUTRAL); // 默认选择中性色
const currentScheme = ref<ColorScheme | null>(null); // 当前配色方案
const savedSchemes = ref<ColorScheme[]>([]); // 保存的配色方案

// 计算当前配色方案的颜色
const currentSchemeColors = computed(() => {
  return currentScheme.value?.colors || [];
});

// 生成配色方案
const generateScheme = () => {
  currentScheme.value = ColorSchemeGenerator.generateScheme(selectedSchemeType.value, adjustedBaseColor.value);
};

// 处理基础颜色变化
const handleBaseColorChange = (color: Color) => {
  adjustedBaseColor.value = color;
  generateScheme();
};

// 处理配色模式变化
const handleSchemeTypeChange = (type: ColorSchemeType) => {
  selectedSchemeType.value = type;
  generateScheme();
};

// 处理颜色调整变化
const handleAdjustmentChange = (color: Color) => {
  adjustedBaseColor.value = color;
  generateScheme();
};

// 保存当前配色方案
const saveCurrentScheme = (name?: string) => {
  if (currentScheme.value) {
    const schemeToSave = {
      ...currentScheme.value,
      name: name || `${currentScheme.value.name} - ${new Date().toLocaleString()}`
    };
    savedSchemes.value.push(schemeToSave);
    // 保存到本地存储
    localStorage.setItem('savedColorSchemes', JSON.stringify(savedSchemes.value));
  }
};

// 加载配色方案
const loadScheme = (scheme: ColorScheme) => {
  baseColor.value = scheme.baseColor;
  adjustedBaseColor.value = scheme.baseColor;
  selectedSchemeType.value = scheme.type;
  currentScheme.value = scheme;
};

// 从本地存储加载保存的配色方案
const loadSavedSchemes = () => {
  const saved = localStorage.getItem('savedColorSchemes');
  if (saved) {
    savedSchemes.value = JSON.parse(saved);
  }
};

// 初始化
onMounted(() => {
  loadSavedSchemes();
  generateScheme();
});
</script>

<style scoped>
.color-scheme-tool {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #F9FAFB;
}

.tool-header {
  background-color: #FFFFFF;
  padding: 24px 32px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid #E5E7EB;
}

.tool-title {
  font-size: 28px;
  font-weight: 700;
  color: #374151;
  margin: 0 0 8px 0;
}

.tool-description {
  font-size: 16px;
  color: #6B7280;
  margin: 0;
}

.tool-main {
  display: flex;
  flex: 1;
  gap: 24px;
  padding: 24px 32px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.control-panel {
  flex: 0 0 350px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.preview-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .tool-main {
    flex-direction: column;
  }
  
  .control-panel {
    flex: none;
    width: 100%;
  }
}

@media (max-width: 768px) {
  .tool-header {
    padding: 16px 20px;
  }
  
  .tool-title {
    font-size: 24px;
  }
  
  .tool-description {
    font-size: 14px;
  }
  
  .tool-main {
    padding: 16px 20px;
    gap: 16px;
  }
}
</style>
