<template>
  <div class="mold-chart" :class="{ 'mobile-mode': isMobile }">
    <!-- 图表标题 -->
    <div class="chart-header">
      <h3 class="chart-title">{{ title }}</h3>
      <div class="chart-actions">
        <el-select
          v-model="chartType"
          placeholder="选择图表类型"
          size="small"
          @change="handleChartTypeChange"
        >
          <el-option label="柱状图" value="bar" />
          <el-option label="折线图" value="line" />
          <el-option label="饼图" value="pie" />
        </el-select>
      </div>
    </div>
    
    <!-- 图表容器 -->
    <div class="chart-container" ref="chartContainer" :style="{ height: chartHeight }"></div>
    
    <!-- 图表加载状态 -->
    <div v-if="loading" class="chart-loading">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, computed, nextTick } from 'vue';
import { Loading } from '@element-plus/icons-vue';
import * as echarts from 'echarts';

// 定义组件的Props
interface MoldChartProps {
  // 图表标题
  title: string;
  // 图表数据
  data: any[];
  // 图表类型
  type?: 'bar' | 'line' | 'pie';
  // 图表高度
  height?: string | number;
  // 是否显示加载状态
  loading?: boolean;
  // X轴字段
  xField?: string;
  // Y轴字段
  yField?: string;
  // 系列名称
  seriesName?: string;
}

// 定义组件的Events
interface MoldChartEmits {
  // 图表类型变化事件
  (e: 'chart-type-change', type: string): void;
  // 图表点击事件
  (e: 'chart-click', params: any): void;
}

// 定义组件的Props默认值
const props = withDefaults(defineProps<MoldChartProps>(), {
  type: 'bar',
  height: '400px',
  loading: false,
  xField: 'name',
  yField: 'value',
  seriesName: '数据'
});

// 定义组件的Events
const emit = defineEmits<MoldChartEmits>();

// 图表实例
let chartInstance: echarts.ECharts | null = null;

// 图表容器
const chartContainer = ref<HTMLElement>();

// 判断是否为移动端
const isMobile = ref(false);

// 当前图表类型
const chartType = ref(props.type);

// 图表高度
const chartHeight = computed(() => {
  // 移动端图表高度自适应
  if (isMobile.value) {
    return '300px';
  }
  return props.height;
});

// 监听窗口大小变化
const handleResize = () => {
  isMobile.value = window.innerWidth < 768;
  // 调整图表大小
  chartInstance?.resize();
};

// 初始化图表
const initChart = () => {
  if (!chartContainer.value) return;
  
  // 销毁现有图表实例
  if (chartInstance) {
    chartInstance.dispose();
  }
  
  // 创建新的图表实例
  chartInstance = echarts.init(chartContainer.value);
  
  // 监听图表点击事件
  chartInstance.on('click', (params) => {
    emit('chart-click', params);
  });
  
  // 渲染图表
  renderChart();
};

// 渲染图表
const renderChart = () => {
  if (!chartInstance) return;
  
  // 准备图表配置
  const option = getChartOption();
  
  // 设置图表配置
  chartInstance.setOption(option);
};

// 获取图表配置
const getChartOption = () => {
  // 根据图表类型生成不同的配置
  switch (chartType.value) {
    case 'bar':
      return getBarChartOption();
    case 'line':
      return getLineChartOption();
    case 'pie':
      return getPieChartOption();
    default:
      return getBarChartOption();
  }
};

// 获取柱状图配置
const getBarChartOption = () => {
  // 提取X轴数据和Y轴数据
  const xAxisData = props.data.map(item => item[props.xField!]);
  const seriesData = props.data.map(item => item[props.yField!]);
  
  return {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: xAxisData,
      axisLabel: {
        // 移动端优化：X轴标签旋转
        rotate: isMobile.value ? 45 : 0,
        fontSize: isMobile.value ? 10 : 12
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        fontSize: isMobile.value ? 10 : 12
      }
    },
    series: [
      {
        name: props.seriesName,
        type: 'bar',
        data: seriesData,
        itemStyle: {
          color: '#6150BF',
          borderRadius: isMobile.value ? 4 : 8
        },
        // 移动端优化：调整柱状图宽度
        barWidth: isMobile.value ? '40%' : '60%'
      }
    ]
  };
};

// 获取折线图配置
const getLineChartOption = () => {
  // 提取X轴数据和Y轴数据
  const xAxisData = props.data.map(item => item[props.xField!]);
  const seriesData = props.data.map(item => item[props.yField!]);
  
  return {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: xAxisData,
      axisLabel: {
        // 移动端优化：X轴标签旋转
        rotate: isMobile.value ? 45 : 0,
        fontSize: isMobile.value ? 10 : 12
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        fontSize: isMobile.value ? 10 : 12
      }
    },
    series: [
      {
        name: props.seriesName,
        type: 'line',
        data: seriesData,
        itemStyle: {
          color: '#6150BF'
        },
        lineStyle: {
          width: isMobile.value ? 2 : 3
        },
        symbol: isMobile.value ? 'circle' : 'rect',
        symbolSize: isMobile.value ? 6 : 8
      }
    ]
  };
};

// 获取饼图配置
const getPieChartOption = () => {
  // 提取饼图数据
  const pieData = props.data.map(item => ({
    name: item[props.xField!],
    value: item[props.yField!]
  }));
  
  return {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      // 移动端优化：调整图例位置和方向
      orient: isMobile.value ? 'horizontal' : 'vertical',
      bottom: isMobile.value ? 0 : '5%',
      left: isMobile.value ? 'center' : 'left',
      textStyle: {
        fontSize: isMobile.value ? 10 : 12
      },
      // 移动端优化：只显示前5个图例，其余折叠
      type: isMobile.value ? 'scroll' : 'plain'
    },
    series: [
      {
        name: props.seriesName,
        type: 'pie',
        radius: isMobile.value ? ['40%', '70%'] : ['50%', '70%'],
        center: ['50%', isMobile.value ? '40%' : '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: isMobile.value ? 14 : 18,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: pieData,
        // 移动端优化：调整扇区颜色
        color: [
          '#6150BF',
          '#8B5CF6',
          '#A78BFA',
          '#C4B5FD',
          '#DDD6FE',
          '#EDE9FE',
          '#F5F3FF'
        ]
      }
    ]
  };
};

// 处理图表类型变化
const handleChartTypeChange = () => {
  emit('chart-type-change', chartType.value);
  // 重新渲染图表
  nextTick(() => {
    renderChart();
  });
};

// 监听图表数据变化
watch(() => props.data, () => {
  nextTick(() => {
    renderChart();
  });
}, { deep: true });

// 监听图表类型变化
watch(() => props.type, (newType) => {
  chartType.value = newType;
  nextTick(() => {
    renderChart();
  });
});

// 监听加载状态变化
watch(() => props.loading, () => {
  // 加载状态变化时，重新渲染图表
  nextTick(() => {
    renderChart();
  });
});

// 组件挂载时初始化
onMounted(() => {
  handleResize();
  window.addEventListener('resize', handleResize);
  // 初始化图表
  initChart();
});

// 组件卸载时清理
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  // 销毁图表实例
  if (chartInstance) {
    chartInstance.dispose();
    chartInstance = null;
  }
});
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

.mold-chart {
  background-color: $white;
  border-radius: $border-radius-lg;
  padding: $spacing-3;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
  
  // 图表标题和操作
  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-3;
    
    .chart-title {
      font-size: $font-size-lg;
      font-weight: $font-weight-semibold;
      color: $gray-900;
      margin: 0;
    }
    
    .chart-actions {
      display: flex;
      gap: $spacing-2;
      
      .el-select {
        width: 120px;
      }
    }
  }
  
  // 图表容器
  .chart-container {
    width: 100%;
    background-color: $gray-50;
    border-radius: $border-radius-md;
    overflow: hidden;
    
    // 图表加载状态
    .chart-loading {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: flex;
      align-items: center;
      gap: $spacing-1;
      color: $gray-600;
      
      .el-icon {
        font-size: 20px;
        animation: rotate 1s linear infinite;
      }
      
      @keyframes rotate {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
      }
    }
  }
  
  // 图表加载状态
  .chart-loading {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(2px);
    z-index: 10;
    
    .el-icon {
      font-size: 32px;
      color: $primary-color;
      margin-bottom: $spacing-1;
      animation: rotate 1s linear infinite;
    }
    
    span {
      color: $gray-600;
      font-size: $font-size-base;
    }
    
    @keyframes rotate {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }
  }
}

// 移动端样式
.mold-chart.mobile-mode {
  padding: $spacing-2;
  
  .chart-header {
    margin-bottom: $spacing-2;
    flex-direction: column;
    align-items: stretch;
    gap: $spacing-2;
    
    .chart-title {
      font-size: $font-size-base;
      text-align: center;
    }
    
    .chart-actions {
      justify-content: center;
      
      .el-select {
        width: 100%;
        max-width: 120px;
      }
    }
  }
  
  .chart-container {
    background-color: transparent;
  }
}

// 响应式断点
@media (max-width: 767px) {
  .mold-chart {
    padding: $spacing-2;
    
    .chart-header {
      margin-bottom: $spacing-2;
      flex-direction: column;
      align-items: stretch;
      gap: $spacing-2;
      
      .chart-title {
        font-size: $font-size-base;
        text-align: center;
      }
      
      .chart-actions {
        justify-content: center;
        
        .el-select {
          width: 100%;
          max-width: 120px;
        }
      }
    }
    
    .chart-container {
      background-color: transparent;
    }
  }
}

@media (min-width: 768px) {
  .mold-chart {
    padding: $spacing-3;
    
    .chart-header {
      margin-bottom: $spacing-3;
      flex-direction: row;
      align-items: center;
      gap: 0;
      
      .chart-title {
        font-size: $font-size-lg;
        text-align: left;
      }
      
      .chart-actions {
        justify-content: flex-end;
        
        .el-select {
          width: 120px;
        }
      }
    }
    
    .chart-container {
      background-color: $gray-50;
    }
  }
}
</style>