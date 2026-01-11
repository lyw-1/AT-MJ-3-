<template>
  <div class="table-container">
    <!-- 表格工具栏 -->
    <slot name="toolbar"></slot>
    
    <!-- 表格主体 -->
    <el-table
      :data="data"
      :height="height"
      :max-height="maxHeight"
      :stripe="stripe"
      :border="border"
      :fit="fit"
      :show-header="showHeader"
      :highlight-current-row="highlightCurrentRow"
      :current-row-key="currentRowKey"
      :row-class-name="rowClassName"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      :cell-style="cellStyle"
      :header-row-class-name="headerRowClassName"
      :header-row-style="headerRowStyle"
      :header-cell-class-name="headerCellClassName"
      :header-cell-style="headerCellStyle"
      :row-key="rowKey"
      :empty-text="emptyText"
      :default-expand-all="defaultExpandAll"
      :expand-row-keys="expandRowKeys"
      :default-sort="defaultSort"
      :tooltip-effect="tooltipEffect"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
      :select-on-indeterminate="selectOnIndeterminate"
      :indent="indent"
      :tree-props="treeProps"
      :lazy="lazy"
      :load="load"
      :tooltip-trigger="tooltipTrigger"
      :tooltip-placement="tooltipPlacement"
      :resize-column-width="resizeColumnWidth"
      :draggable="draggable"
      :flex-grow="flexGrow"
      :flex-shrink="flexShrink"
      :size="size"
      :tag="tag"
      @select="handleSelect"
      @select-all="handleSelectAll"
      @selection-change="handleSelectionChange"
      @cell-mouse-enter="handleCellMouseEnter"
      @cell-mouse-leave="handleCellMouseLeave"
      @cell-click="handleCellClick"
      @cell-dblclick="handleCellDblclick"
      @cell-contextmenu="handleCellContextmenu"
      @row-mouse-enter="handleRowMouseEnter"
      @row-mouse-leave="handleRowMouseLeave"
      @row-click="handleRowClick"
      @row-dblclick="handleRowDblclick"
      @row-contextmenu="handleRowContextmenu"
      @header-click="handleHeaderClick"
      @header-contextmenu="handleHeaderContextmenu"
      @sort-change="handleSortChange"
      @filter-change="handleFilterChange"
      @current-change="handleCurrentChange"
      @expand-change="handleExpandChange"
      @selection="handleSelection"
      @cell-update="handleCellUpdate"
      @column-resize="handleColumnResize"
      @column-drag-end="handleColumnDragEnd"
      @column-visible-change="handleColumnVisibleChange"
      v-bind="$attrs"
      class="custom-table"
    >
      <!-- 表格列插槽 -->
      <slot></slot>
      
      <!-- 自定义空状态 -->
      <template #empty>
        <div class="custom-empty">
          <el-icon :size="48" color="#c0c4cc"><DocumentDelete /></el-icon>
          <p>{{ emptyText }}</p>
        </div>
      </template>
    </el-table>
    
    <!-- 分页组件 -->
    <div class="pagination-container">
      <slot name="pagination"></slot>
    </div>
    
    <!-- 表格加载状态 -->
    <div v-if="loading" class="table-loading-overlay">
      <div class="loading-content">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElTable, Loading, DocumentDelete } from 'element-plus';

// 定义组件的Props
interface TableProps {
  // 表格数据
  data: any[];
  // 表格高度
  height?: string | number;
  // 表格最大高度
  maxHeight?: string | number;
  // 是否为斑马纹表格
  stripe?: boolean;
  // 是否带有纵向边框
  border?: boolean;
  // 列的宽度是否自撑开
  fit?: boolean;
  // 是否显示表头
  showHeader?: boolean;
  // 是否高亮当前行
  highlightCurrentRow?: boolean;
  // 当前行的key
  currentRowKey?: string | number;
  // 行的className
  rowClassName?: string | ((row: any, rowIndex: number) => string);
  // 行的style
  rowStyle?: object | ((row: any, rowIndex: number) => object);
  // 单元格的className
  cellClassName?: string | ((row: any, column: any, rowIndex: number, columnIndex: number) => string);
  // 单元格的style
  cellStyle?: object | ((row: any, column: any, rowIndex: number, columnIndex: number) => object);
  // 表头行的className
  headerRowClassName?: string | ((row: any, rowIndex: number) => string);
  // 表头行的style
  headerRowStyle?: object | ((row: any, rowIndex: number) => object);
  // 表头单元格的className
  headerCellClassName?: string | ((column: any, columnIndex: number) => string);
  // 表头单元格的style
  headerCellStyle?: object | ((column: any, columnIndex: number) => object);
  // 行数据的Key
  rowKey?: string | ((row: any) => string | number);
  // 空表格的文本内容
  emptyText?: string;
  // 是否默认展开所有行
  defaultExpandAll?: boolean;
  // 展开行的Key数组
  expandRowKeys?: any[];
  // 默认排序规则
  defaultSort?: { prop: string; order: 'ascending' | 'descending' | null };
  // 工具提示效果
  tooltipEffect?: 'dark' | 'light';
  // 是否在表尾显示合计行
  showSummary?: boolean;
  // 合计行第一列的文本
  sumText?: string;
  // 自定义合计方法
  summaryMethod?: (data: any[]) => any[];
  // 合并单元格方法
  spanMethod?: (param: { row: any; column: any; rowIndex: number; columnIndex: number }) => [number, number] | undefined;
  // 在多选表格中，是否在选择一个节点的同时选中其所有子节点
  selectOnIndeterminate?: boolean;
  // 树状结构中父子节点之间的缩进
  indent?: number;
  // 树状结构的配置项
  treeProps?: { children?: string; hasChildren?: string; label?: string };
  // 是否懒加载子节点数据
  lazy?: boolean;
  // 加载子节点数据的方法
  load?: (row: any, treeNode: any, resolve: (data: any[]) => void) => void;
  // 提示框触发方式
  tooltipTrigger?: 'hover' | 'focus' | 'manual';
  // 提示框位置
  tooltipPlacement?: string;
  // 列宽调整触发的宽度
  resizeColumnWidth?: number;
  // 是否支持列拖拽
  draggable?: boolean;
  // 表格的flex-grow属性
  flexGrow?: number;
  // 表格的flex-shrink属性
  flexShrink?: number;
  // 表格尺寸
  size?: 'large' | 'default' | 'small';
  // 是否显示加载状态
  loading?: boolean;
  // 自定义标签
  tag?: string;
}

// 定义组件的Events
interface TableEmits {
  // 选择事件
  (e: 'select', selection: any[], row: any): void;
  // 全选事件
  (e: 'select-all', selection: any[]): void;
  // 选择项变化事件
  (e: 'selection-change', selection: any[]): void;
  // 单元格鼠标进入事件
  (e: 'cell-mouse-enter', row: any, column: any, cell: any, event: Event): void;
  // 单元格鼠标离开事件
  (e: 'cell-mouse-leave', row: any, column: any, cell: any, event: Event): void;
  // 单元格点击事件
  (e: 'cell-click', row: any, column: any, cell: any, event: Event): void;
  // 单元格双击事件
  (e: 'cell-dblclick', row: any, column: any, cell: any, event: Event): void;
  // 单元格右键菜单事件
  (e: 'cell-contextmenu', row: any, column: any, cell: any, event: Event): void;
  // 行鼠标进入事件
  (e: 'row-mouse-enter', row: any, column: any, cell: any, event: Event): void;
  // 行鼠标离开事件
  (e: 'row-mouse-leave', row: any, column: any, cell: any, event: Event): void;
  // 行点击事件
  (e: 'row-click', row: any, column: any, event: Event): void;
  // 行双击事件
  (e: 'row-dblclick', row: any, column: any, event: Event): void;
  // 行右键菜单事件
  (e: 'row-contextmenu', row: any, column: any, event: Event): void;
  // 表头点击事件
  (e: 'header-click', column: any, event: Event): void;
  // 表头右键菜单事件
  (e: 'header-contextmenu', column: any, event: Event): void;
  // 排序变化事件
  (e: 'sort-change', { column: any, prop: string, order: string }): void;
  // 筛选变化事件
  (e: 'filter-change', filters: any): void;
  // 当前行变化事件
  (e: 'current-change', currentRow: any, oldCurrentRow: any): void;
  // 展开行变化事件
  (e: 'expand-change', row: any, expandedRows: any[]): void;
  // 选择事件
  (e: 'selection', selection: any[]): void;
  // 单元格更新事件
  (e: 'cell-update', row: any, column: any, newValue: any, oldValue: any): void;
  // 列宽调整事件
  (e: 'column-resize', column: any, width: number, oldWidth: number): void;
  // 列拖拽结束事件
  (e: 'column-drag-end', column: any, columns: any[]): void;
  // 列可见性变化事件
  (e: 'column-visible-change', column: any, visible: boolean): void;
}

// 定义组件的Props默认值
const _props = withDefaults(defineProps<TableProps>(), {
  data: () => [],
  height: '',
  maxHeight: '',
  stripe: false,
  border: false,
  fit: true,
  showHeader: true,
  highlightCurrentRow: false,
  currentRowKey: '',
  rowClassName: '',
  rowStyle: {},
  cellClassName: '',
  cellStyle: {},
  headerRowClassName: '',
  headerRowStyle: {},
  headerCellClassName: '',
  headerCellStyle: {},
  rowKey: '',
  emptyText: '暂无数据',
  defaultExpandAll: false,
  expandRowKeys: () => [],
  defaultSort: () => ({ prop: '', order: null }),
  tooltipEffect: 'dark',
  showSummary: false,
  sumText: '合计',
  summaryMethod: undefined,
  spanMethod: undefined,
  selectOnIndeterminate: false,
  indent: 16,
  treeProps: () => ({ children: 'children', hasChildren: 'hasChildren' }),
  lazy: false,
  load: undefined,
  tooltipTrigger: 'hover',
  tooltipPlacement: 'top',
  resizeColumnWidth: 50,
  draggable: false,
  flexGrow: 1,
  flexShrink: 1,
  size: 'default',
  loading: false,
  tag: 'table'
});

// 定义组件的Events
const emit = defineEmits<TableEmits>();

// 处理选择事件
const handleSelect = (selection: any[], row: any) => {
  emit('select', selection, row);
};

// 处理全选事件
const handleSelectAll = (selection: any[]) => {
  emit('select-all', selection);
};

// 处理选择项变化事件
const handleSelectionChange = (selection: any[]) => {
  emit('selection-change', selection);
};

// 处理单元格鼠标进入事件
const handleCellMouseEnter = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-mouse-enter', row, column, cell, event);
};

// 处理单元格鼠标离开事件
const handleCellMouseLeave = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-mouse-leave', row, column, cell, event);
};

// 处理单元格点击事件
const handleCellClick = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-click', row, column, cell, event);
};

// 处理单元格双击事件
const handleCellDblclick = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-dblclick', row, column, cell, event);
};

// 处理单元格右键菜单事件
const handleCellContextmenu = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-contextmenu', row, column, cell, event);
};

// 处理行鼠标进入事件
const handleRowMouseEnter = (row: any, column: any, cell: any, event: Event) => {
  emit('row-mouse-enter', row, column, cell, event);
};

// 处理行鼠标离开事件
const handleRowMouseLeave = (row: any, column: any, cell: any, event: Event) => {
  emit('row-mouse-leave', row, column, cell, event);
};

// 处理行点击事件
const handleRowClick = (row: any, column: any, event: Event) => {
  emit('row-click', row, column, event);
};

// 处理行双击事件
const handleRowDblclick = (row: any, column: any, event: Event) => {
  emit('row-dblclick', row, column, event);
};

// 处理行右键菜单事件
const handleRowContextmenu = (row: any, column: any, event: Event) => {
  emit('row-contextmenu', row, column, event);
};

// 处理表头点击事件
const handleHeaderClick = (column: any, event: Event) => {
  emit('header-click', column, event);
};

// 处理表头右键菜单事件
const handleHeaderContextmenu = (column: any, event: Event) => {
  emit('header-contextmenu', column, event);
};

// 处理排序变化事件
const handleSortChange = ({ column, prop, order }: { column: any; prop: string; order: string }) => {
  emit('sort-change', { column, prop, order });
};

// 处理筛选变化事件
const handleFilterChange = (filters: any) => {
  emit('filter-change', filters);
};

// 处理当前行变化事件
const handleCurrentChange = (currentRow: any, oldCurrentRow: any) => {
  emit('current-change', currentRow, oldCurrentRow);
};

// 处理展开行变化事件
const handleExpandChange = (row: any, expandedRows: any[]) => {
  emit('expand-change', row, expandedRows);
};

// 处理选择事件
const handleSelection = (selection: any[]) => {
  emit('selection', selection);
};

// 处理单元格更新事件
const handleCellUpdate = (row: any, column: any, newValue: any, oldValue: any) => {
  emit('cell-update', row, column, newValue, oldValue);
};

// 处理列宽调整事件
const handleColumnResize = (column: any, width: number, oldWidth: number) => {
  emit('column-resize', column, width, oldWidth);
};

// 处理列拖拽结束事件
const handleColumnDragEnd = (column: any, columns: any[]) => {
  emit('column-drag-end', column, columns);
};

// 处理列可见性变化事件
const handleColumnVisibleChange = (column: any, visible: boolean) => {
  emit('column-visible-change', column, visible);
};
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 自定义表格样式 - 增强交互体验
.table-container {
  width: 100%;
  font-family: inherit;
  position: relative;
  
  // 表格主体样式
  .custom-table {
    // 确保表格样式符合设计规范
    
    // 平滑过渡
    transition: all 0.3s ease;
    
    // 表格边框优化
    &.el-table {
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
      
      // 表头样式优化
      .el-table__header-wrapper {
        .el-table__header {
          th {
            // 表头单元格样式
            font-weight: $font-weight-semibold;
            background-color: $gray-50;
            color: $gray-700;
            border-bottom: 2px solid $gray-200;
            
            // 平滑过渡
            transition: all 0.3s ease;
            
            // 悬停效果
            &:hover {
              background-color: $gray-100;
            }
            
            // 排序图标优化
            .el-table__sort-icon {
              .sort-caret {
                &.ascending {
                  color: $primary-color;
                }
                
                &.descending {
                  color: $primary-color;
                }
                
                // 排序图标动画
                transition: all 0.3s ease;
              }
            }
          }
        }
      }
      
      // 表格内容样式
      .el-table__body-wrapper {
        .el-table__body {
          td {
            // 内容单元格样式
            color: $gray-600;
            border-bottom: 1px solid $gray-100;
            
            // 平滑过渡
            transition: all 0.3s ease;
            
            // 悬停效果
            &:hover {
              background-color: rgba($primary-color, 0.05);
            }
          }
          
          // 悬停行样式
          .el-table__row {
            transition: all 0.3s ease;
            
            &:hover {
              background-color: $primary-light;
              transform: translateY(-1px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }
          }
          
          // 当前行样式
          .current-row {
            background-color: rgba($primary-color, 0.1);
            font-weight: $font-weight-medium;
          }
          
          // 斑马纹样式优化
          &.el-table__body--striped {
            .el-table__row.el-table__row--striped {
              background-color: $gray-50;
              
              &:hover {
                background-color: $primary-light;
              }
            }
          }
        }
      }
      
      // 空表格样式优化
      .el-table__empty-block {
        .el-table__empty-text {
          color: $gray-500;
        }
      }
      
      // 表格边框样式
      &.el-table--border {
        border: 1px solid $gray-200;
        
        .el-table__header-wrapper,
        .el-table__body-wrapper {
          border-right: none;
          border-left: none;
        }
        
        .el-table__header,
        .el-table__body {
          th,
          td {
            border-right: 1px solid $gray-200;
          }
        }
      }
      
      // 表格尺寸优化
      &.el-table--small {
        .el-table__header {
          th {
            padding: 8px 0;
          }
        }
        
        .el-table__body {
          td {
            padding: 10px 0;
          }
        }
      }
      
      // 加载中状态优化
      &.is-loading {
        .el-table__loading-wrapper {
          background-color: rgba(255, 255, 255, 0.8);
          backdrop-filter: blur(2px);
        }
      }
    }
  }
  
  // 自定义空状态
  .custom-empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px 0;
    color: $gray-500;
    
    p {
      margin-top: 16px;
      font-size: $font-size-base;
    }
  }
  
  // 分页容器样式
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
    
    // 分页组件样式优化
    :deep(.el-pagination) {
      .el-pagination__total {
        color: $gray-600;
      }
      
      .el-pager {
        li {
          border-radius: 4px;
          margin: 0 4px;
          
          &:hover {
            color: $primary-color;
          }
          
          &.active {
            background-color: $primary-color;
            color: $white;
          }
        }
      }
      
      .el-pagination__prev,
      .el-pagination__next,
      .el-pagination__jump-prev,
      .el-pagination__jump-next {
        border-radius: 4px;
        margin: 0 4px;
        
        &:hover {
          color: $primary-color;
          background-color: $primary-light;
        }
      }
      
      .el-pagination__editor {
        margin: 0 8px;
      }
    }
  }
  
  // 表格加载状态
  .table-loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(2px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    border-radius: 8px;
    
    // 平滑过渡
    animation: fadeIn 0.3s ease;
    
    .loading-content {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 24px;
      background-color: $white;
      border-radius: $border-radius-md;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      
      .el-icon {
        font-size: 20px;
        color: $primary-color;
      }
      
      span {
        font-size: $font-size-base;
        color: $gray-700;
      }
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
