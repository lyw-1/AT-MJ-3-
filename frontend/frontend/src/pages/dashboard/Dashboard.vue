<template>
  <div class="dashboard-container">
    <!-- 使用完全自定义的布局，绕过Element Plus的样式冲突 -->
    <div class="red-stats-container">
      <div class="red-stat-item clickable" @click="goList('density')">
        <div class="stat">
          <div class="stat-value-large">{{ densityOver }} 件</div>
          <div class="stat-title-small">容重超标统计</div>
        </div>
      </div>
      <div class="red-stat-item clickable" @click="goList('slot_width')">
        <div class="stat">
          <div class="stat-value-large">{{ slotOver }} 件</div>
          <div class="stat-title-small">槽宽超标统计</div>
        </div>
      </div>
    </div>

    <!-- 新建容器包裹所有小卡片 -->
    <el-card class="canvas-card card-rounded-lg">
      <el-row :gutter="2" class="row">
        <el-col :xs="12" :md="4" v-for="item in statusCards" :key="item.key">
          <el-card class="stat-card clickable" @click="goStatus(item.key)">
            <div class="stat">
              <div class="stat-title">{{ item.title }}</div>
              <div class="stat-value">{{ item.value }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="canvas-card card-rounded-lg">
      <div class="charts-container">
        <div class="charts-title">各部门员工模具统计</div>
        <div class="charts-grid">
          <!-- 成型车间柱状图 -->
          <div class="chart-module">
            <div class="chart-title">成型车间各员工模具统计</div>
            <div class="chart-container" ref="moldingChartRef"></div>
          </div>
          <!-- 调模组柱状图 -->
          <div class="chart-module">
            <div class="chart-title">调模组各员工模具统计</div>
            <div class="chart-container" ref="toolingChartRef"></div>
          </div>
          <!-- 其他部门柱状图 -->
          <div class="chart-module">
            <div class="chart-title">其他部门各员工模具统计</div>
            <div class="chart-container" ref="otherChartRef"></div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="canvas-card card-rounded-lg">
      <div class="supply">
        <div class="supply-title">重点模具供需情况</div>
        <div class="supply-grid">
          <div v-for="i in supplyItems" :key="i.id" class="supply-item">
            <div class="supply-name">{{ i.name }}</div>
            <div class="supply-bars">
              <div class="bar need" :style="{ width: i.need + '%' }"></div>
              <div class="bar supply" :style="{ width: i.supply + '%' }"></div>
            </div>
            <div class="supply-meta">需求{{ i.need }}% 供给{{ i.supply }}%</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 耗材库预警模块 -->
    <el-card class="canvas-card card-rounded-lg">
      <div class="consumables-warnings">
        <div class="supply-title">耗材库预警</div>
        <div class="supply-grid">
          <div v-for="item in consumablesWarnings" :key="item.id" class="supply-item consumables-item">
            <div class="consumables-header">
              <div class="consumables-name">{{ item.name }}</div>
              <el-tag :type="getWarningType(item.warningLevel)">
                {{ getWarningText(item.warningLevel) }}
              </el-tag>
            </div>
            <div class="consumables-stock">
              <div class="stock-current">当前库存: {{ item.currentStock }} 件</div>
              <div class="stock-min">最低库存: {{ item.minStock }} 件</div>
            </div>
            <div class="consumables-progress">
              <div class="progress-container">
                <div 
                  class="progress-bar" 
                  :style="{
                    width: getProgressPercentage(item.currentStock, item.minStock) + '%',
                    backgroundColor: getProgressColor(item.warningLevel)
                  }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'

const router = useRouter()
const densityOver = ref(12)
const slotOver = ref(8)
const statusCards = ref([
  { key: '待加工', title: '待加工模具', value: 23 },
  { key: '加工中', title: '加工中模具', value: 89 },
  { key: '委外加工', title: '委外加工模具', value: 7 },
  { key: '待调试', title: '待调试模具', value: 11 },
  { key: '调试中', title: '调试中模具', value: 5 },
  { key: '待验收', title: '待验收模具', value: 14 }
])
const supplyItems = ref([
  { id: 1, name: '重点模具 A', need: 70, supply: 55 },
  { id: 2, name: '重点模具 B', need: 40, supply: 60 },
  { id: 3, name: '重点模具 C', need: 85, supply: 30 }
])

// 耗材库预警模拟数据
const consumablesWarnings = ref([
  { id: 1, name: '模具钢材', currentStock: 15, minStock: 50, warningLevel: 'high' },
  { id: 2, name: '耐磨涂层', currentStock: 8, minStock: 20, warningLevel: 'high' },
  { id: 3, name: '冷却液', currentStock: 30, minStock: 40, warningLevel: 'medium' },
  { id: 4, name: '砂纸', currentStock: 45, minStock: 50, warningLevel: 'low' }
])

// 图表容器引用
const moldingChartRef = ref<HTMLElement | null>(null)
const toolingChartRef = ref<HTMLElement | null>(null)
const otherChartRef = ref<HTMLElement | null>(null)

// 耗材库预警辅助函数
// 获取预警类型（用于Tag组件）
const getWarningType = (level: string) => {
  switch (level) {
    case 'high': return 'danger'
    case 'medium': return 'warning'
    case 'low': return 'info'
    default: return 'info'
  }
}

// 获取预警文本
const getWarningText = (level: string) => {
  switch (level) {
    case 'high': return '严重不足'
    case 'medium': return '即将不足'
    case 'low': return '接近不足'
    default: return '正常'
  }
}

// 计算库存进度百分比
const getProgressPercentage = (current: number, min: number) => {
  return Math.min(Math.round((current / min) * 100), 100)
}

// 获取进度条颜色
const getProgressColor = (level: string) => {
  switch (level) {
    case 'high': return '#EF4444'
    case 'medium': return '#F59E0B'
    case 'low': return '#6366F1'
    default: return '#10B981'
  }
}

// 图表实例
let moldingChart: echarts.ECharts | null = null
let toolingChart: echarts.ECharts | null = null
let otherChart: echarts.ECharts | null = null

// 成型车间数据
const moldingData = ref({
  categories: ['张三', '李四', '王五', '赵六', '钱七'],
  values: [45, 32, 67, 54, 78]
})

// 调模组数据
const toolingData = ref({
  categories: ['孙八', '周九', '吴十', '郑一', '王二'],
  values: [23, 45, 34, 65, 56]
})

// 其他部门数据
const otherData = ref({
  categories: ['冯三', '陈四', '褚五', '卫六', '蒋七'],
  values: [34, 28, 45, 39, 51]
})

// 计算数组总和
const calculateTotal = (values: number[]): number => {
  return values.reduce((sum, value) => sum + value, 0)
}

// 初始化图表
  const initCharts = () => {
    // 计算各部门总数
    const moldingTotal = calculateTotal(moldingData.value.values)
    const toolingTotal = calculateTotal(toolingData.value.values)
    const otherTotal = calculateTotal(otherData.value.values)

    // 成型车间图表
    if (moldingChartRef.value) {
      // 强制使用canvas渲染器，解决CSSStyleDeclaration索引访问错误
      moldingChart = echarts.init(moldingChartRef.value, null, { renderer: 'canvas' })
    moldingChart.setOption({
      title: {
        show: false
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: '{b}: {c} 件'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: moldingData.value.categories,
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value',
        name: '模具数量(件)'
      },
      graphic: [
        {
          type: 'text',
          left: '90%',
          top: '10%',
          style: {
            text: `总数: ${moldingTotal}件`,
            fontSize: 14,
            fontWeight: 'bold',
            fill: '#333'
          }
        }
      ],
      series: [
        {
          name: '模具数量',
          type: 'bar',
          data: moldingData.value.values,
          itemStyle: {
            color: '#5470c6'
          },
          emphasis: {
            itemStyle: {
              color: '#91cc75'
            }
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}',
            fontSize: 12,
            color: '#333'
          }
        }
      ]
    })
  }

  // 调模组图表
    if (toolingChartRef.value) {
      // 强制使用canvas渲染器，解决CSSStyleDeclaration索引访问错误
      toolingChart = echarts.init(toolingChartRef.value, null, { renderer: 'canvas' })
    toolingChart.setOption({
      title: {
        show: false
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: '{b}: {c} 件'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: toolingData.value.categories,
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value',
        name: '模具数量(件)'
      },
      graphic: [
        {
          type: 'text',
          left: '90%',
          top: '10%',
          style: {
            text: `总数: ${toolingTotal}件`,
            fontSize: 14,
            fontWeight: 'bold',
            fill: '#333'
          }
        }
      ],
      series: [
        {
          name: '模具数量',
          type: 'bar',
          data: toolingData.value.values,
          itemStyle: {
            color: '#91cc75'
          },
          emphasis: {
            itemStyle: {
              color: '#fac858'
            }
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}',
            fontSize: 12,
            color: '#333'
          }
        }
      ]
    })
  }

  // 其他部门图表
    if (otherChartRef.value) {
      // 强制使用canvas渲染器，解决CSSStyleDeclaration索引访问错误
      otherChart = echarts.init(otherChartRef.value, null, { renderer: 'canvas' })
    otherChart.setOption({
      title: {
        show: false
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: '{b}: {c} 件'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: otherData.value.categories,
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value',
        name: '模具数量(件)'
      },
      graphic: [
        {
          type: 'text',
          left: '90%',
          top: '10%',
          style: {
            text: `总数: ${otherTotal}件`,
            fontSize: 14,
            fontWeight: 'bold',
            fill: '#333'
          }
        }
      ],
      series: [
        {
          name: '模具数量',
          type: 'bar',
          data: otherData.value.values,
          itemStyle: {
            color: '#fac858'
          },
          emphasis: {
            itemStyle: {
              color: '#ee6666'
            }
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}',
            fontSize: 12,
            color: '#333'
          }
        }
      ]
    })
  }
}

// 监听窗口大小变化，调整图表大小
const handleResize = () => {
  moldingChart?.resize()
  toolingChart?.resize()
  otherChart?.resize()
}

// 路由跳转
const goList = (type: string) => {
  router.push({ path: '/mold/tasks', query: { metric: type, abnormal: 'true' } })
}

const goStatus = (status: string) => {
  router.push({ path: '/mold/tasks', query: { status } })
}

// 组件挂载时初始化图表
onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

// 组件卸载时销毁图表
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  moldingChart?.dispose()
  toolingChart?.dispose()
  otherChart?.dispose()
})
</script>

<style scoped lang="scss">
/* 主容器样式 - 符合边距规范 */
.dashboard-container {
  padding: 15px; /* 符合页面容器边距规范 */
}

/* 自定义红色统计容器样式 - 符合设计规范 */
.red-stats-container {
  display: flex;
  width: 100%; /* 保持在容器内，不超出 */
  margin: 0 0 10px 0; /* 符合卡片外边距规范：10px 0 */
  background-color: transparent; /* 透明背景，不影响卡片样式 */
  height: 120px; /* 保持与其他统计卡片相同的高度 */
  overflow: hidden;
  border-radius: 0; /* 容器不需要圆角，卡片本身有圆角 */
  box-shadow: none; /* 容器不需要阴影，卡片本身有阴影 */
  padding: 0;
  gap: 2px; /* 卡片间横向间距2px */
}

/* 红色统计项样式 - 符合卡片组件规范 */
.red-stat-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #EF4444;
  color: #FFFFFF;
  cursor: pointer;
  transition: all 0.3s ease; /* 符合过渡动画规范 */
  height: 120px; /* 符合统计卡片固定高度规范 */
  border: 1px solid #EF4444; /* 符合卡片边框规范 */
  border-radius: 15px; /* 符合卡片边框半径规范 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 符合卡片阴影规范 */
  padding: 0;
  margin: 0;
}

/* 移除最后一个统计项的右侧间距 */
.red-stat-item:last-child {
  margin-right: 0;
}

/* 为第一个统计项添加左侧圆角 */
.red-stat-item:first-child {
  border-top-left-radius: 15px;
  border-bottom-left-radius: 15px;
}

/* 为最后一个统计项添加右侧圆角 */
.red-stat-item:last-child {
  border-top-right-radius: 15px;
  border-bottom-right-radius: 15px;
}

/* 统计内容样式 */
.red-stat-item .stat {
  text-align: center;
  padding: 20px;
}

/* 悬停效果 - 与home页面悬停效果一致 */
.red-stat-item:hover {
  background-color: #DC2626; /* 红色加深 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 与home页面悬停效果一致 */
}

/* 确保统计标题和数值为白色 */
.red-stat-item .stat-title-small,
.red-stat-item .stat-value-large {
  color: #FFFFFF !important;
}

/* 移除卡片的默认样式影响 */
.red-stat-item .stat-card,
.red-stat-item .el-card,
.red-stat-item .el-card__body {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding: 0 !important;
  margin: 0 !important;
}

/* 行间距 - 符合模块间距规范 */
.row {
  margin-bottom: 10px; /* 纵向间距调整为10px，符合设计规范 */
}

/* 卡片通用样式 - 符合卡片组件规范 */
.el-card {
  border-radius: 15px; /* 符合卡片边框半径规范 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 符合卡片阴影规范 */
  transition: all 0.3s ease; /* 符合过渡动画规范 */
  overflow: hidden;
  margin: 0 0 10px 0; /* 符合卡片外边距规范：10px 0 */
  padding: 0;

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 与home页面悬停效果一致 */
  }

  /* 调整卡片内边距 - 符合卡片内边距规范 */
  .el-card__body {
    padding: 24px; /* 符合卡片内部边距规范 */
  }
}

/* 统计卡片样式 */
.stat-card {
  height: 120px; /* 符合统计卡片固定高度规范 */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease; /* 符合过渡动画规范 */
}

/* 重置所有卡片的默认样式 - 确保所有卡片都是白色背景 */
.stat-card {
  background-color: #FFFFFF !important;
  color: inherit !important;
  border-color: var(--el-border-color-light) !important;
  
  /* 确保卡片内部元素使用默认白色背景和文字颜色 */
  .el-card__body,
  .stat,
  .stat-title,
  .stat-value {
    background-color: #FFFFFF !important;
    color: inherit !important;
    box-shadow: none !important;
    border: none !important;
  }
  
  /* 标题文字颜色 */
  .stat-title {
    color: #6b7280 !important;
  }
  
  /* 数值文字颜色 */
  .stat-value {
    color: #374151 !important;
  }
}

/* 修复红色卡片的白色边框问题 */
/* 确保第一行的整个列都使用红色背景 */
.dashboard-container > .el-row:first-of-type > .el-col {
  background-color: #EF4444 !important;
  padding: 0 !important;
  margin: 0 !important;
}

/* 只对dashboard-container的第一个直接子元素el-row中的两个超标统计卡片应用红色背景 */
.dashboard-container > .el-row:first-of-type > .el-col > .stat-card {
  background-color: #EF4444 !important;
  color: #FFFFFF !important;
  border-color: #EF4444 !important;
  padding: 0 !important;
  margin: 0 !important;
  
  /* 确保卡片内部所有元素都使用红色背景和白色文字 */
  /* 使用更广泛的选择器确保所有子元素都继承红色背景 */
  &,
  .el-card__body,
  .el-card__body *,
  .stat,
  .stat *,
  .stat-title-small,
  .stat-value-large {
    background-color: #EF4444 !important;
    background: #EF4444 !important;
    color: #FFFFFF !important;
    box-shadow: none !important;
    border: none !important;
    padding: 0 !important;
    margin: 0 !important;
  }
  
  /* 标题文字颜色 */
  .stat-title-small {
    color: #FFFFFF !important;
    opacity: 0.9;
  }
  
  /* 数值文字颜色 */
  .stat-value-large {
    color: #FFFFFF !important;
  }
}

/* 移除卡片的默认边距和内边距 */
.dashboard-container > .el-row:first-of-type > .el-col > .stat-card > .el-card__body {
  padding: 0 !important;
  margin: 0 !important;
  box-shadow: none !important;
  border: none !important;
}

/* 移除统计区域的内边距 */
.dashboard-container > .el-row:first-of-type > .el-col > .stat-card > .el-card__body > .stat {
  padding: 20px !important;
  margin: 0 !important;
}

/* 确保容器卡片和内部的小卡片保持白色背景 */
.canvas-card > .el-row > .el-col > .stat-card {
  background-color: #FFFFFF !important;
  color: inherit !important;
  border-color: var(--el-border-color-light) !important;
  
  /* 确保卡片内部元素使用默认白色背景和文字颜色 */
  .el-card__body,
  .stat,
  .stat-title,
  .stat-value {
    background-color: #FFFFFF !important;
    color: inherit !important;
  }
  
  /* 标题文字颜色 */
  .stat-title {
    color: #6b7280 !important;
  }
  
  /* 数值文字颜色 */
  .stat-value {
    color: #374151 !important;
  }
}

.clickable { cursor: pointer; }
.stat { text-align: center; }

/* 统计标题和数值的默认样式 */
.stat-title { font-size: 16px; color: #6b7280; }
.stat-value { font-size: 28px; font-weight: 700; color: #374151; }

/* 超标统计卡片 - 大数值样式 */
.stat-value-large {
  font-size: 48px !important;
  font-weight: 700 !important;
  color: #FFFFFF !important;
  margin-bottom: 4px !important;
}

/* 超标统计卡片 - 小标题样式 */
.stat-title-small {
  font-size: 14px !important;
  color: #FFFFFF !important;
  font-weight: 500 !important;
  opacity: 0.9 !important;
}

/* 确保其他统计卡片的样式不受影响 */
.el-row:not(:first-of-type) .stat-title,
.el-row:not(:first-of-type) .stat-value {
  font-size: inherit !important;
  color: inherit !important;
  font-weight: inherit !important;
}

/* 图表容器样式 */
.charts-container {
  padding: 24px; /* 符合卡片内部边距规范 */
}

.charts-title {
  font-size: 16px; 
  font-weight: 600;
  margin-bottom: 16px; /* 符合组件内部间距规范 */
  text-align: center;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2px; /* 横向间距调整为2 */
}

.chart-module {
  border: 1px solid var(--el-border-color-light);
  border-radius: 15px; /* 符合卡片边框半径规范 */
  padding: 24px; /* 符合卡片内边距规范 */
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 符合卡片阴影规范 */
  transition: all 0.3s ease; /* 符合过渡动画规范 */

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 与home页面悬停效果一致 */
  }
}

.chart-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px; /* 符合组件内部间距规范 */
  text-align: center;
  color: #374151;
}

.chart-container {
  height: 300px; /* 设置图表高度 */
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-container {
    height: 250px;
  }
}

/* 重点模具供需情况样式 */
.supply {
  padding: 24px; /* 符合卡片内部边距规范 */
}

.supply-title {
  font-size: 16px; 
  font-weight: 600;
  margin-bottom: 16px; /* 符合组件内部间距规范 */
}

.supply-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 2px; /* 横向间距调整为2 */
}

.supply-item {
  border: 1px solid var(--el-border-color-light);
  border-radius: 15px; /* 符合卡片边框半径规范 */
  padding: 24px; /* 符合卡片内边距规范 */
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 符合卡片阴影规范 */
  transition: all 0.3s ease; /* 符合过渡动画规范 */

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 与home页面悬停效果一致 */
  }
}

.supply-name {
  font-weight: 600;
  margin-bottom: 8px; /* 符合组件内部间距规范 */
}

.supply-bars {
  display: flex;
  gap: 6px; /* 符合组件内部间距规范 */
  height: 8px;
  margin: 8px 0; /* 符合组件内部间距规范 */
}

.bar {
  height: 8px;
  border-radius: 4px; /* 符合功能条边框半径规范 */
}

.bar.need { background: #fca5a5; }
.bar.supply { background: #93c5fd; }

.supply-meta {
  font-size: 12px; 
  color: #6b7280;
  margin-top: 8px; /* 符合组件内部间距规范 */
}

/* 耗材库预警模块样式 */
.consumables-warnings {
  padding: 24px; /* 符合卡片内部边距规范 */
}

.consumables-item {
  padding: 20px;
  border: 1px solid var(--el-border-color-light);
  border-radius: 15px;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.consumables-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.consumables-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.consumables-name {
  font-weight: 600;
  font-size: 16px;
  color: #374151;
}

.consumables-stock {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  color: #6b7280;
}

.stock-current {
  color: #374151;
}

.stock-min {
  color: #6b7280;
}

.consumables-progress {
  margin-top: 12px;
}

/* 自定义进度条样式 */
.progress-container {
  width: 100%;
  height: 8px;
  background-color: #f3f4f6;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}
</style>
