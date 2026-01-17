<template>
  <div class="mold-card" :class="{ 'mobile-mode': isMobile }" @click="handleClick">
    <!-- 模具状态标签 -->
    <div class="mold-status">
      <el-tag :type="getStatusType(status)" :effect="'light'">
        {{ getStatusText(status) }}
      </el-tag>
    </div>
    
    <!-- 模具基本信息 -->
    <div class="mold-info">
      <h3 class="mold-name">{{ name }}</h3>
      <div class="mold-details">
        <div class="detail-item">
          <span class="detail-label">模具编号：</span>
          <span class="detail-value">{{ id }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">型号：</span>
          <span class="detail-value">{{ model }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">创建时间：</span>
          <span class="detail-value">{{ formatDate(createTime) }}</span>
        </div>
      </div>
    </div>
    
    <!-- 操作按钮 -->
    <div class="mold-actions">
      <el-button
        type="primary"
        size="small"
        :icon="View"
        @click.stop="handleView"
      >
        查看详情
      </el-button>
      <el-button
        type="success"
        size="small"
        :icon="Edit"
        @click.stop="handleEdit"
      >
        编辑
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { View, Edit } from '@element-plus/icons-vue';
import { formatDate } from '@/utils/date';

// 定义组件的Props
interface MoldCardProps {
  // 模具ID
  id: string | number;
  // 模具名称
  name: string;
  // 模具型号
  model: string;
  // 模具状态
  status: string;
  // 创建时间
  createTime: string | Date;
  // 是否可点击
  clickable?: boolean;
}

// 定义组件的Events
interface MoldCardEmits {
  // 点击事件
  (e: 'click', id: string | number): void;
  // 查看详情事件
  (e: 'view', id: string | number): void;
  // 编辑事件
  (e: 'edit', id: string | number): void;
}

// 定义组件的Props默认值
const props = withDefaults(defineProps<MoldCardProps>(), {
  clickable: true
});

// 定义组件的Events
const emit = defineEmits<MoldCardEmits>();

// 判断是否为移动端
const isMobile = ref(false);

// 监听窗口大小变化
const handleResize = () => {
  isMobile.value = window.innerWidth < 768;
};

// 组件挂载时初始化
onMounted(() => {
  handleResize();
  window.addEventListener('resize', handleResize);
});

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});

// 处理卡片点击事件
const handleClick = () => {
  if (props.clickable) {
    emit('click', props.id);
  }
};

// 处理查看详情事件
const handleView = () => {
  emit('view', props.id);
};

// 处理编辑事件
const handleEdit = () => {
  emit('edit', props.id);
};

// 获取状态类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, any> = {
    using: 'success',
    idle: 'info',
    repair: 'warning',
    scrapped: 'danger'
  };
  return statusMap[status] || 'info';
};

// 获取状态文本
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    using: '在用',
    idle: '闲置',
    repair: '维修',
    scrapped: '报废'
  };
  return statusMap[status] || status;
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

.mold-card {
  background-color: $white;
  border-radius: $border-radius-lg;
  padding: $spacing-3;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  // 悬停效果
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  }
  
  // 模具状态标签
  .mold-status {
    position: absolute;
    top: $spacing-2;
    right: $spacing-2;
    z-index: 1;
  }
  
  // 模具基本信息
  .mold-info {
    margin-bottom: $spacing-3;
    
    // 模具名称
    .mold-name {
      font-size: $font-size-lg;
      font-weight: $font-weight-semibold;
      color: $gray-900;
      margin: 0 0 $spacing-2 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    // 模具详情
    .mold-details {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-2;
      
      // 详情项
      .detail-item {
        display: flex;
        align-items: center;
        gap: $spacing-1;
        
        // 详情标签
        .detail-label {
          font-size: $font-size-sm;
          color: $gray-600;
          font-weight: $font-weight-medium;
        }
        
        // 详情值
        .detail-value {
          font-size: $font-size-sm;
          color: $gray-800;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
  
  // 操作按钮
  .mold-actions {
    display: flex;
    gap: $spacing-2;
    justify-content: flex-end;
    
    // 按钮样式优化
    :deep(.el-button) {
      border-radius: $border-radius-md;
      font-size: $font-size-sm;
      padding: 6px 12px;
      
      &:hover {
        opacity: 0.9;
      }
    }
  }
}

// 移动端样式
.mold-card.mobile-mode {
  padding: $spacing-2;
  
  // 移动端布局优化
  .mold-info {
    .mold-details {
      display: block;
      gap: $spacing-1;
      
      .detail-item {
        margin-bottom: $spacing-1;
        
        // 调整详情项布局
        flex: 1 0 100%;
      }
    }
  }
  
  // 移动端按钮布局
  .mold-actions {
    justify-content: stretch;
    gap: $spacing-1;
    
    .el-button {
      flex: 1;
      text-align: center;
    }
  }
}

// 响应式断点
@media (max-width: 767px) {
  .mold-card {
    // 移动端卡片样式
    padding: $spacing-2;
    
    .mold-info {
      .mold-name {
        font-size: $font-size-base;
      }
      
      .mold-details {
        display: block;
        
        .detail-item {
          margin-bottom: $spacing-1;
          
          flex: 1 0 100%;
        }
      }
    }
    
    .mold-actions {
      justify-content: stretch;
      gap: $spacing-1;
      
      .el-button {
        flex: 1;
        text-align: center;
      }
    }
  }
}

@media (min-width: 768px) {
  .mold-card {
    // 桌面端卡片样式
    padding: $spacing-3;
    
    .mold-info {
      .mold-name {
        font-size: $font-size-lg;
      }
      
      .mold-details {
        display: flex;
        flex-wrap: wrap;
        gap: $spacing-2;
        
        .detail-item {
          margin-bottom: 0;
          
          flex: 1;
          min-width: 200px;
        }
      }
    }
    
    .mold-actions {
      justify-content: flex-end;
      gap: $spacing-2;
      
      .el-button {
        flex: none;
      }
    }
  }
}
</style>