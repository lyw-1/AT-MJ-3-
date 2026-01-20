<template>
  <div class="color-theory-tips">
    <h3 class="section-title">色彩理论建议</h3>
    
    <div v-if="colorScheme" class="tips-content">
      <!-- 专业建议列表 -->
      <div class="tips-list">
        <div
          v-for="(tip, index) in tips"
          :key="index"
          class="tip-item"
        >
          <div class="tip-icon-placeholder"></div>
          <p class="tip-text">{{ tip }}</p>
        </div>
      </div>
      
      <!-- 色彩情感分析 -->
      <div class="emotion-analysis">
        <h4 class="subsection-title">色彩情感</h4>
        <div class="emotion-tags">
          <el-tag
            v-for="(emotion, index) in colorEmotions"
            :key="index"
            type="primary"
            size="small"
            class="emotion-tag"
          >
            {{ emotion }}
          </el-tag>
        </div>
      </div>
      
      <!-- 对比度检查 -->
      <div class="accessibility-check" v-if="accessibilityReport">
        <h4 class="subsection-title">无障碍检查</h4>
        <div class="accessibility-result" :class="{ 'pass': accessibilityReport.overallScore >= 80 }">
          <div class="accessibility-score">
            <span class="score-label">可访问性评分:</span>
            <span class="score-value">{{ accessibilityReport.overallScore }}/100</span>
          </div>
          
          <div class="contrast-issues" v-if="accessibilityReport.issues.length > 0">
            <h5 class="issues-title">潜在问题:</h5>
            <ul class="issues-list">
              <li
                v-for="(issue, index) in accessibilityReport.issues"
                :key="index"
                class="issue-item"
              >
                <div class="issue-icon-placeholder"></div>
                {{ issue }}
              </li>
            </ul>
          </div>
          
          <div class="accessibility-advice" v-else>
            <div class="advice-icon-placeholder"></div>
            <span>该配色方案符合无障碍标准，可以放心使用</span>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else class="no-scheme-tip">
      请选择基础颜色和配色模式以获取专业建议
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { ColorScheme } from '../../utils/colorSchemeGenerator';
import { ColorTheoryUtils, ColorEmotion } from '../../utils/colorTheoryUtils';

// 定义组件属性
interface Props {
  colorScheme?: ColorScheme;
}

const props = withDefaults(defineProps<Props>(), {
  colorScheme: null
});

// 生成专业建议
const tips = computed(() => {
  if (!props.colorScheme) return [];
  return ColorTheoryUtils.getSchemeAdvice(props.colorScheme);
});

// 分析色彩情感
const colorEmotions = computed(() => {
  if (!props.colorScheme) return [];
  const emotions = ColorTheoryUtils.getColorEmotions(props.colorScheme.baseColor);
  
  // 情感类型映射为中文
  const emotionMap: Record<ColorEmotion, string> = {
    [ColorEmotion.WARM]: '温暖',
    [ColorEmotion.COOL]: '凉爽',
    [ColorEmotion.NEUTRAL]: '中性',
    [ColorEmotion.ENERGETIC]: '充满活力',
    [ColorEmotion.CALM]: '平静',
    [ColorEmotion.CHEERFUL]: '愉快',
    [ColorEmotion.SERIOUS]: '严肃',
    [ColorEmotion.FRIENDLY]: '友好'
  };
  
  return emotions.map(emotion => emotionMap[emotion]);
});

// 无障碍检查报告
const accessibilityReport = computed(() => {
  if (!props.colorScheme) return null;
  return ColorTheoryUtils.checkSchemeAccessibility(props.colorScheme);
});
</script>

<style scoped>
.color-theory-tips {
  background-color: #FFFFFF;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #E5E7EB;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 16px 0;
}

.tips-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 12px;
  background-color: #F9FAFB;
  border-radius: 6px;
  border-left: 4px solid #6150BF;
}

.tip-icon-placeholder {
  width: 18px;
  height: 18px;
  margin-top: 2px;
  border-radius: 50%;
  background-color: #6150BF;
}

.tip-text {
  font-size: 14px;
  color: #4B5563;
  margin: 0;
  line-height: 1.5;
}

.emotion-analysis {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.subsection-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.emotion-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.emotion-tag {
  background-color: #EEF2FF;
  color: #6150BF;
  border: 1px solid #E0E7FF;
}

.accessibility-check {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.accessibility-result {
  padding: 12px;
  border-radius: 6px;
  background-color: #FEF3C7;
  border: 1px solid #FDE68A;
}

.accessibility-result.pass {
  background-color: #D1FAE5;
  border: 1px solid #A7F3D0;
}

.accessibility-score {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.score-label {
  font-size: 14px;
  font-weight: 600;
  color: #92400E;
}

.accessibility-result.pass .score-label {
  color: #065F46;
}

.score-value {
  font-size: 18px;
  font-weight: 700;
  color: #92400E;
}

.accessibility-result.pass .score-value {
  color: #065F46;
}

.contrast-issues {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.issues-title {
  font-size: 14px;
  font-weight: 600;
  color: #92400E;
  margin: 0;
}

.issues-list {
  margin: 0;
  padding-left: 24px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.issue-item {
  font-size: 13px;
  color: #92400E;
  display: flex;
  align-items: center;
  gap: 6px;
}

.issue-icon-placeholder {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #F59E0B;
}

.accessibility-advice {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #065F46;
  font-weight: 500;
}

.advice-icon-placeholder {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: #10B981;
}

.no-scheme-tip {
  text-align: center;
  padding: 24px;
  color: #6B7280;
  font-size: 14px;
  background-color: #F9FAFB;
  border-radius: 6px;
}
</style>
