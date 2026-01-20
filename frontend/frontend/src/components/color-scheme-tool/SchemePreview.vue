<template>
  <div class="scheme-preview">
    <div class="section-header">
      <h3 class="section-title">配色方案预览</h3>
      <el-button
        v-if="colorScheme"
        type="primary"
        @click="applyScheme"
        icon="el-icon-check"
        class="apply-button"
      >
        应用到UI
      </el-button>
    </div>
    
    <!-- 配色方案名称和描述 -->
    <div class="scheme-header" v-if="colorScheme">
      <h4 class="scheme-name">{{ colorScheme.name }}</h4>
      <p class="scheme-description">{{ colorScheme.description }}</p>
    </div>
    
    <!-- 配色方案颜色卡片 -->
    <div class="color-cards">
      <div
        v-for="(color, index) in colors"
        :key="index"
        class="color-card"
      >
        <div 
          class="color-swatch"
          :style="{ backgroundColor: color.hex }"
        ></div>
        <div class="color-details">
          <div class="color-value hex">{{ color.hex }}</div>
          <div class="color-value rgb">RGB: {{ color.rgb.r }}, {{ color.rgb.g }}, {{ color.rgb.b }}</div>
          <div class="color-value hsl">HSL: {{ color.hsl.h }}°, {{ color.hsl.s }}%, {{ color.hsl.l }}%</div>
          <div class="color-value cmyk">CMYK: {{ color.cmyk.c }}%, {{ color.cmyk.m }}%, {{ color.cmyk.y }}%, {{ color.cmyk.k }}%</div>
        </div>
        <!-- 复制按钮 -->
        <el-button
          type="text"
          @click="copyColorValue(color.hex)"
          class="copy-button"
          size="small"
          icon="el-icon-document-copy"
        >
          复制
        </el-button>
      </div>
    </div>
    
    <!-- 场景预览 -->
    <div class="scene-preview" v-if="colors.length > 0">
      <h4 class="subsection-title">场景预览</h4>
      
      <!-- 选项卡切换不同场景 -->
      <el-tabs v-model="activeScene" type="card" class="scene-tabs">
        <!-- UI组件预览 -->
        <el-tab-pane label="UI组件" name="ui-components">
          <div class="ui-preview">
            <div class="preview-section">
              <h5>按钮组件</h5>
              <div class="button-group">
                <el-button 
                  v-for="(color, index) in colors" 
                  :key="index"
                  :style="{ 
                    '--button-color': index === 0 ? '#FFFFFF' : '#374151',
                    '--button-background': color.hex 
                  }"
                  class="preview-button"
                >
                  按钮 {{ index + 1 }}
                </el-button>
              </div>
            </div>
            
            <div class="preview-section">
              <h5>卡片组件</h5>
              <div class="card-group">
                <div 
                  v-for="(color, index) in colors" 
                  :key="index"
                  :style="{ 
                    '--card-background': index === 0 ? '#FFFFFF' : color.hex,
                    '--card-text': index === 0 ? '#374151' : '#FFFFFF',
                    '--card-border': index === 0 ? '#E5E7EB' : 'transparent'
                  }"
                  class="preview-card"
                >
                  <h6>卡片标题</h6>
                  <p>这是卡片内容，用于展示不同配色的视觉效果。</p>
                  <el-button 
                    size="small"
                    :style="{ 
                      '--button-color': index === 0 ? color.hex : '#FFFFFF',
                      '--button-background': index === 0 ? 'transparent' : 'rgba(255, 255, 255, 0.2)',
                      '--button-border': index === 0 ? color.hex : 'transparent'
                    }"
                    class="preview-button small"
                  >
                    操作按钮
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <!-- 网页布局预览 -->
        <el-tab-pane label="网页布局" name="web-layout">
          <div class="web-preview">
            <header :style="{ backgroundColor: colors[0]?.hex }">
              <h5>网站标题</h5>
              <nav>
                <ul>
                  <li v-for="i in 4" :key="i">导航链接 {{ i }}</li>
                </ul>
              </nav>
            </header>
            <main>
              <aside :style="{ backgroundColor: colors[1]?.hex }">
                <h6>侧边栏</h6>
                <ul>
                  <li v-for="i in 5" :key="i">菜单项目 {{ i }}</li>
                </ul>
              </aside>
              <section>
                <article>
                  <h5>文章标题</h5>
                  <p>这是文章内容，用于展示网页主体的配色效果。良好的配色方案能够提升用户体验，增强内容的可读性。</p>
                  <p>通过合理的颜色搭配，可以引导用户注意力，突出重要信息，营造特定的氛围和情感。</p>
                </article>
                <div class="cards-row">
                  <div 
                    v-for="(color, index) in colors.slice(2)" 
                    :key="index"
                    :style="{ backgroundColor: color.hex }"
                    class="layout-card"
                  >
                    <h6>卡片 {{ index + 1 }}</h6>
                    <p>卡片内容示例</p>
                  </div>
                </div>
              </section>
            </main>
            <footer :style="{ backgroundColor: colors[colors.length - 1]?.hex }">
              <p>网站页脚 - 版权信息</p>
            </footer>
          </div>
        </el-tab-pane>
        
        <!-- 文本对比预览 -->
        <el-tab-pane label="文本对比" name="text-contrast">
          <div class="text-preview">
            <div 
              v-for="(bgColor, bgIndex) in colors" 
              :key="bgIndex"
              class="text-preview-section"
              :style="{ backgroundColor: bgColor.hex }"
            >
              <h5>背景色: {{ bgColor.hex }}</h5>
              <div class="text-samples">
                <div
                  v-for="(textColor, textIndex) in colors"
                  :key="textIndex"
                  class="text-sample"
                  :style="{ color: textColor.hex }"
                >
                  <div class="text-size large">大文本 - Lorem Ipsum</div>
                  <div class="text-size medium">中文本 - Lorem Ipsum</div>
                  <div class="text-size small">小文本 - Lorem Ipsum</div>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Color } from '../../utils/colorUtils';
import { ColorScheme } from '../../utils/colorSchemeGenerator';

// 定义组件属性
interface Props {
  colorScheme?: ColorScheme;
  colors?: Color[];
}

const props = withDefaults(defineProps<Props>(), {
  colors: () => []
});

// 组件状态
const activeScene = ref('ui-components');
const copySuccess = ref(false);

// 计算属性：使用传入的配色方案或直接使用颜色数组
const colors = computed(() => {
  if (props.colorScheme?.colors) {
    return props.colorScheme.colors;
  }
  return props.colors;
});

// 复制颜色值到剪贴板
const copyColorValue = async (value: string) => {
  try {
    await navigator.clipboard.writeText(value);
    copySuccess.value = true;
    setTimeout(() => {
      copySuccess.value = false;
    }, 2000);
  } catch (err) {
    console.error('复制失败:', err);
  }
};

// 应用配色方案到UI
const applyScheme = () => {
  if (!colors.value || colors.value.length === 0) {
    console.error('没有可应用的配色方案');
    return;
  }
  
  // 更新CSS变量
  const root = document.documentElement;
  
  // 主色调
  const primaryColor = colors.value[0];
  root.style.setProperty('--el-color-primary', primaryColor.hex);
  root.style.setProperty('--el-color-primary-light-3', adjustLightness(primaryColor, 15));
  root.style.setProperty('--el-color-primary-light-5', adjustLightness(primaryColor, 25));
  root.style.setProperty('--el-color-primary-light-7', adjustLightness(primaryColor, 35));
  root.style.setProperty('--el-color-primary-light-8', adjustLightness(primaryColor, 40));
  root.style.setProperty('--el-color-primary-light-9', adjustLightness(primaryColor, 45));
  root.style.setProperty('--el-color-primary-dark-2', adjustLightness(primaryColor, -15));
  
  // 辅助色（根据需要调整）
  if (colors.value.length > 1) {
    const secondaryColor = colors.value[1];
    // 可以添加辅助色相关的CSS变量
  }
  
  // 成功色
  if (colors.value.length > 2) {
    const successColor = colors.value[2];
    root.style.setProperty('--el-color-success', successColor.hex);
    root.style.setProperty('--el-color-success-light-3', adjustLightness(successColor, 15));
    root.style.setProperty('--el-color-success-light-5', adjustLightness(successColor, 25));
    root.style.setProperty('--el-color-success-light-7', adjustLightness(successColor, 35));
    root.style.setProperty('--el-color-success-light-8', adjustLightness(successColor, 40));
    root.style.setProperty('--el-color-success-light-9', adjustLightness(successColor, 45));
    root.style.setProperty('--el-color-success-dark-2', adjustLightness(successColor, -15));
  }
  
  // 警告色
  if (colors.value.length > 3) {
    const warningColor = colors.value[3];
    root.style.setProperty('--el-color-warning', warningColor.hex);
    root.style.setProperty('--el-color-warning-light-3', adjustLightness(warningColor, 15));
    root.style.setProperty('--el-color-warning-light-5', adjustLightness(warningColor, 25));
    root.style.setProperty('--el-color-warning-light-7', adjustLightness(warningColor, 35));
    root.style.setProperty('--el-color-warning-light-8', adjustLightness(warningColor, 40));
    root.style.setProperty('--el-color-warning-light-9', adjustLightness(warningColor, 45));
    root.style.setProperty('--el-color-warning-dark-2', adjustLightness(warningColor, -15));
  }
  
  // 错误色
  if (colors.value.length > 4) {
    const dangerColor = colors.value[4];
    root.style.setProperty('--el-color-danger', dangerColor.hex);
    root.style.setProperty('--el-color-danger-light-3', adjustLightness(dangerColor, 15));
    root.style.setProperty('--el-color-danger-light-5', adjustLightness(dangerColor, 25));
    root.style.setProperty('--el-color-danger-light-7', adjustLightness(dangerColor, 35));
    root.style.setProperty('--el-color-danger-light-8', adjustLightness(dangerColor, 40));
    root.style.setProperty('--el-color-danger-light-9', adjustLightness(dangerColor, 45));
    root.style.setProperty('--el-color-danger-dark-2', adjustLightness(dangerColor, -15));
  }
  
  // 信息色
  if (colors.value.length > 5) {
    const infoColor = colors.value[5];
    root.style.setProperty('--el-color-info', infoColor.hex);
    root.style.setProperty('--el-color-info-light-3', adjustLightness(infoColor, 15));
    root.style.setProperty('--el-color-info-light-5', adjustLightness(infoColor, 25));
    root.style.setProperty('--el-color-info-light-7', adjustLightness(infoColor, 35));
    root.style.setProperty('--el-color-info-light-8', adjustLightness(infoColor, 40));
    root.style.setProperty('--el-color-info-light-9', adjustLightness(infoColor, 45));
    root.style.setProperty('--el-color-info-dark-2', adjustLightness(infoColor, -15));
  }
  
  // 触发成功提示
  ElMessage.success('配色方案已成功应用到UI');
};

// 调整颜色亮度的辅助函数
const adjustLightness = (color: Color, percentage: number): string => {
  // 将HSL转换为可调整的格式
  const h = color.hsl.h;
  const s = color.hsl.s;
  let l = color.hsl.l;
  
  // 调整亮度
  l += percentage;
  
  // 确保亮度在0-100之间
  l = Math.max(0, Math.min(100, l));
  
  // 将HSL转换回十六进制
  return hslToHex(h, s, l);
};

// HSL转十六进制的辅助函数
const hslToHex = (h: number, s: number, l: number): string => {
  h /= 360;
  s /= 100;
  l /= 100;
  
  let r, g, b;
  
  if (s === 0) {
    r = g = b = l; // 灰色
  } else {
    const hue2rgb = (p: number, q: number, t: number) => {
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1/6) return p + (q - p) * 6 * t;
      if (t < 1/2) return q;
      if (t < 2/3) return p + (q - p) * (2/3 - t) * 6;
      return p;
    };
    
    const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
    const p = 2 * l - q;
    r = hue2rgb(p, q, h + 1/3);
    g = hue2rgb(p, q, h);
    b = hue2rgb(p, q, h - 1/3);
  }
  
  const toHex = (x: number) => {
    const hex = Math.round(x * 255).toString(16);
    return hex.length === 1 ? '0' + hex : hex;
  };
  
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
};
</script>

<style scoped>
.scheme-preview {
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

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.apply-button {
  height: 32px;
  font-size: 14px;
  padding: 0 16px;
}

.scheme-header {
  background-color: #F9FAFB;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #E5E7EB;
}

.scheme-name {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.scheme-description {
  font-size: 14px;
  color: #6B7280;
  margin: 0;
}

.color-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.color-card {
  display: flex;
  flex-direction: column;
  border: 1px solid #E5E7EB;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  background-color: #FFFFFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.color-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.color-swatch {
  height: 120px;
  width: 100%;
  position: relative;
}

.color-details {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.color-value {
  font-size: 12px;
  font-family: 'Courier New', Courier, monospace;
  color: #6B7280;
}

.color-value.hex {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.copy-button {
  margin: 0 12px 12px 12px;
  padding: 0;
  height: auto;
  line-height: normal;
  color: #6150BF;
}

.copy-button:hover {
  color: #4F46E5;
  background-color: transparent;
}

.scene-preview {
  margin-top: 16px;
}

.subsection-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 12px 0;
}

.scene-tabs {
  margin-bottom: 16px;
}

/* UI组件预览 */
.ui-preview {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.preview-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.preview-section h5 {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.button-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.preview-button {
  background-color: var(--button-background);
  color: var(--button-color);
  border: 1px solid var(--button-background, transparent);
  transition: all 0.3s ease;
}

.preview-button.small {
  padding: 4px 12px;
  font-size: 12px;
}

.preview-button:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.card-group {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.preview-card {
  background-color: var(--card-background);
  color: var(--card-text);
  border: 1px solid var(--card-border);
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition: all 0.3s ease;
}

.preview-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.preview-card h6 {
  font-size: 14px;
  font-weight: 600;
  margin: 0;
}

.preview-card p {
  font-size: 12px;
  margin: 0;
  line-height: 1.4;
  opacity: 0.9;
}

/* 网页布局预览 */
.web-preview {
  display: flex;
  flex-direction: column;
  gap: 0;
  background-color: #F9FAFB;
  border: 1px solid #E5E7EB;
  border-radius: 8px;
  overflow: hidden;
}

.web-preview header {
  color: #FFFFFF;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.web-preview header h5 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.web-preview nav ul {
  display: flex;
  gap: 20px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.web-preview nav li {
  font-size: 14px;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.web-preview nav li:hover {
  opacity: 0.8;
}

.web-preview main {
  display: flex;
  gap: 0;
  min-height: 200px;
}

.web-preview aside {
  color: #FFFFFF;
  width: 200px;
  padding: 16px;
}

.web-preview aside h6 {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 12px 0;
}

.web-preview aside ul {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.web-preview aside li {
  font-size: 12px;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.web-preview aside li:hover {
  opacity: 0.8;
}

.web-preview section {
  flex: 1;
  padding: 16px;
  background-color: #FFFFFF;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.web-preview article {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.web-preview article h5 {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.web-preview article p {
  font-size: 14px;
  color: #6B7280;
  margin: 0;
  line-height: 1.5;
}

.cards-row {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 12px;
}

.layout-card {
  color: #FFFFFF;
  padding: 12px;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.layout-card h6 {
  font-size: 13px;
  font-weight: 600;
  margin: 0;
}

.layout-card p {
  font-size: 11px;
  margin: 0;
  opacity: 0.9;
}

.web-preview footer {
  color: #FFFFFF;
  padding: 12px 16px;
  text-align: center;
  font-size: 12px;
}

.web-preview footer p {
  margin: 0;
  opacity: 0.9;
}

/* 文本对比预览 */
.text-preview {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.text-preview-section {
  padding: 16px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.text-preview-section h5 {
  font-size: 14px;
  font-weight: 600;
  margin: 0;
  color: inherit;
  opacity: 0.9;
}

.text-samples {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.text-sample {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 12px;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.text-size.large {
  font-size: 18px;
  font-weight: 600;
}

.text-size.medium {
  font-size: 14px;
}

.text-size.small {
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .color-cards {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 12px;
  }
  
  .button-group {
    flex-direction: column;
  }
  
  .web-preview main {
    flex-direction: column;
  }
  
  .web-preview aside {
    width: 100%;
  }
  
  .text-samples {
    grid-template-columns: 1fr;
  }
}
</style>
