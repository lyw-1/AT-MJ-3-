<template>
  <div class="color-scheme-selector">
    <h3 class="section-title">配色模式</h3>
    
    <div class="scheme-grid">
      <div
        v-for="scheme in supportedSchemes"
        :key="scheme.type"
        class="scheme-card"
        :class="{ active: selectedScheme === scheme.type }"
        @click="selectScheme(scheme.type)"
      >
        <!-- 配色模式示意图 -->
        <div class="scheme-preview">
          <svg width="100" height="100" viewBox="0 0 100 100" class="scheme-diagram">
            <circle cx="50" cy="50" r="40" fill="#f3f4f6" stroke="#e5e7eb" stroke-width="1" />
            
            <!-- 根据不同配色模式绘制不同的点 -->
            <template v-if="scheme.type === 'complementary'">
              <!-- 互补色：两个相对的点 -->
              <circle cx="50" cy="10" r="8" fill="#6150BF" />
              <circle cx="50" cy="90" r="8" fill="#50BFAF" />
            </template>
            
            <template v-else-if="scheme.type === 'analogous'">
              <!-- 类似色：相邻的三个点 -->
              <circle cx="80" cy="30" r="8" fill="#6150BF" />
              <circle cx="50" cy="10" r="8" fill="#8B5CF6" />
              <circle cx="20" cy="30" r="8" fill="#A855F7" />
            </template>
            
            <template v-else-if="scheme.type === 'triad'">
              <!-- 三角色：等边三角形的三个点 -->
              <circle cx="50" cy="10" r="8" fill="#6150BF" />
              <circle cx="85" cy="75" r="8" fill="#50BF61" />
              <circle cx="15" cy="75" r="8" fill="#BF5061" />
            </template>
            
            <template v-else-if="scheme.type === 'tetrad_rectangle'">
              <!-- 四角色-矩形：矩形的四个点 -->
              <circle cx="30" cy="20" r="8" fill="#6150BF" />
              <circle cx="70" cy="20" r="8" fill="#5061BF" />
              <circle cx="70" cy="80" r="8" fill="#BF5061" />
              <circle cx="30" cy="80" r="8" fill="#61BF50" />
            </template>
            
            <template v-else-if="scheme.type === 'tetrad_square'">
              <!-- 四角色-正方形：正方形的四个点 -->
              <circle cx="50" cy="10" r="8" fill="#6150BF" />
              <circle cx="90" cy="50" r="8" fill="#50BF85" />
              <circle cx="50" cy="90" r="8" fill="#BF50A6" />
              <circle cx="10" cy="50" r="8" fill="#A6BF50" />
            </template>
            
            <template v-else-if="scheme.type === 'monochromatic'">
              <!-- 单色：同一色相的不同明度 -->
              <circle cx="50" cy="10" r="8" fill="#3B2C85" />
              <circle cx="50" cy="35" r="8" fill="#6150BF" />
              <circle cx="50" cy="60" r="8" fill="#8B5CF6" />
              <circle cx="50" cy="85" r="8" fill="#C084FC" />
            </template>
            
            <template v-else-if="scheme.type === 'split_complementary'">
              <!-- 分裂互补色：基础色和互补色两侧的颜色 -->
              <circle cx="50" cy="10" r="8" fill="#6150BF" />
              <circle cx="75" cy="75" r="8" fill="#50BF61" />
              <circle cx="25" cy="75" r="8" fill="#BF5061" />
            </template>
            
            <template v-else-if="scheme.type === 'neutral'">
              <!-- 中性色：从浅到深的灰色渐变 -->
              <rect x="10" y="10" width="15" height="15" fill="#F5F5F5" rx="2" />
              <rect x="30" y="10" width="15" height="15" fill="#D4D4D4" rx="2" />
              <rect x="50" y="10" width="15" height="15" fill="#A3A3A3" rx="2" />
              <rect x="70" y="10" width="15" height="15" fill="#737373" rx="2" />
              <rect x="10" y="30" width="15" height="15" fill="#525252" rx="2" />
              <rect x="30" y="30" width="15" height="15" fill="#404040" rx="2" />
              <rect x="50" y="30" width="15" height="15" fill="#262626" rx="2" />
              <rect x="70" y="30" width="15" height="15" fill="#171717" rx="2" />
            </template>
          </svg>
        </div>
        
        <!-- 配色模式信息 -->
        <div class="scheme-info">
          <h4 class="scheme-name">{{ scheme.name }}</h4>
          <p class="scheme-description">{{ scheme.description }}</p>
        </div>
        
        <!-- 选中状态指示 -->
        <div class="scheme-select-indicator" v-if="selectedScheme === scheme.type">
          <el-icon class="check-icon"><CircleCheckFilled /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { CircleCheckFilled } from '@element-plus/icons-vue';
import { ColorSchemeType, ColorSchemeGenerator } from '../../utils/colorSchemeGenerator';

// 定义组件属性
interface Props {
  modelValue?: ColorSchemeType;
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: ColorSchemeType.NEUTRAL // 默认选择中性色
});

// 定义事件
const emit = defineEmits<{
  'update:modelValue': [value: ColorSchemeType];
  'scheme-change': [value: ColorSchemeType];
}>();

// 组件状态
const selectedScheme = ref(props.modelValue);

// 获取支持的配色模式
const supportedSchemes = computed(() => {
  return ColorSchemeGenerator.getSupportedSchemes();
});

// 选择配色模式
const selectScheme = (type: ColorSchemeType) => {
  selectedScheme.value = type;
  emit('update:modelValue', type);
  emit('scheme-change', type);
};
</script>

<style scoped>
.color-scheme-selector {
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

.scheme-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

.scheme-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  border: 2px solid #E5E7EB;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  background-color: #FFFFFF;
}

.scheme-card:hover {
  border-color: #6150BF;
  box-shadow: 0 4px 12px rgba(97, 80, 191, 0.15);
  transform: translateY(-2px);
}

.scheme-card.active {
  border-color: #6150BF;
  background-color: #F9FAFB;
  box-shadow: 0 4px 12px rgba(97, 80, 191, 0.2);
}

.scheme-preview {
  margin-bottom: 12px;
  position: relative;
}

.scheme-diagram {
  transition: transform 0.3s ease;
}

.scheme-card:hover .scheme-diagram {
  transform: scale(1.05);
}

.scheme-info {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.scheme-name {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.scheme-description {
  font-size: 12px;
  color: #6B7280;
  margin: 0;
  line-height: 1.4;
}

.scheme-select-indicator {
  position: absolute;
  top: 8px;
  right: 8px;
  background-color: #6150BF;
  color: white;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.check-icon {
  font-size: 16px;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .scheme-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 12px;
  }
  
  .scheme-card {
    padding: 12px;
  }
  
  .scheme-name {
    font-size: 13px;
  }
  
  .scheme-description {
    font-size: 11px;
  }
}
</style>
