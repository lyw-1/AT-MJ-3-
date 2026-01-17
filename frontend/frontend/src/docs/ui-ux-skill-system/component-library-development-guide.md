# 组件库开发指南

## 1. 概述

组件库是蜂窝陶瓷模具管理系统的核心基础设施，它提供了一套统一、可复用、响应式的UI组件，用于构建系统的各个页面。组件库的目标是提高开发效率、确保视觉一致性、提升用户体验，并支持跨平台适配。

本指南详细阐述了组件库的开发规范、组件分类、开发流程和最佳实践，旨在为开发团队提供清晰的指导，确保组件库的质量和可维护性。

## 2. 组件开发规范

### 2.1 命名规范

#### 2.1.1 组件名称

- 使用PascalCase命名法，如`Table`、`FormItem`
- 组件名称应清晰描述组件的功能，避免过于抽象
- 避免使用缩写，除非是广为人知的缩写，如`UI`、`API`

#### 2.1.2 文件命名

- 组件文件使用PascalCase命名法，如`Table.vue`、`FormItem.vue`
- 组件相关文件（如类型定义、测试文件）使用与组件相同的名称，如`Table.types.ts`、`Table.test.ts`

#### 2.1.3 目录结构

- 组件文件统一存放在`src/components`目录下
- 复杂组件可以创建子目录，如`src/components/Table/Table.vue`
- 组件相关文件（如类型定义、测试文件）存放在组件目录下

### 2.2 文件结构

组件文件应包含以下部分：

1. `<template>`：组件的HTML结构
2. `<script setup lang="ts">`：组件的逻辑实现，使用Composition API
3. `<style scoped lang="scss">`：组件的样式，使用scoped和SCSS

### 2.3 Props设计

- 使用TypeScript接口定义Props类型，提高类型安全性
- Props名称使用camelCase命名法
- 为所有Props提供默认值，确保组件的健壮性
- 使用`withDefaults(defineProps<PropsType>(), defaults)`语法定义Props
- 避免Props数量过多，保持组件的简洁性
- 对于复杂的Props，考虑使用对象形式传递

### 2.4 Events设计

- 使用TypeScript接口定义Events类型，提高类型安全性
- Events名称使用kebab-case命名法
- Events应清晰描述事件的触发时机和数据
- 避免Events数量过多，保持组件的简洁性
- 对于复杂的事件数据，考虑使用对象形式传递

### 2.5 Slots设计

- 合理使用Slots，提高组件的灵活性和可扩展性
- Slots名称使用kebab-case命名法
- 为Slots提供默认内容，确保组件在没有Slots时也能正常显示
- 使用具名Slots，避免匿名Slots的滥用

### 2.6 样式设计

- 使用SCSS预处理器，提高样式的可维护性
- 使用scoped属性，避免样式冲突
- 基于现有的设计变量，如颜色、间距、圆角等
- 避免使用!important，除非有特殊需求
- 为组件添加适当的过渡动画，提升用户体验
- 考虑响应式设计，适配不同屏幕尺寸

## 3. 基础组件开发

基础组件是基于Element Plus扩展的通用组件，它们提供了系统的基本UI元素，如按钮、输入框、表格、表单等。

### 3.1 基于Element Plus扩展

- 继承Element Plus组件的Props和Events
- 扩展Element Plus组件的功能和样式
- 保持与Element Plus组件的API兼容性
- 统一组件的样式，确保视觉一致性

### 3.2 示例：自定义Table组件

```vue
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

// 事件处理函数...
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

// 自定义表格样式...
</style>
```

## 4. 业务组件开发

业务组件是针对蜂窝陶瓷模具管理系统的特定业务需求开发的组件，它们封装了系统的核心业务逻辑，如模具列表、任务卡片、加工流程等。

### 4.1 业务组件分类

#### 4.1.1 数据展示组件

- **模具列表组件**：展示模具信息，支持筛选、排序和分页
- **任务列表组件**：展示加工任务，支持状态筛选和批量操作
- **设备状态组件**：展示设备状态，支持实时刷新和异常告警
- **数据可视化组件**：展示统计数据和图表，支持交互

#### 4.1.2 表单组件

- **模具信息表单**：用于录入和编辑模具信息
- **加工任务表单**：用于创建和编辑加工任务
- **调模记录表单**：用于录入和编辑调模记录
- **生产数据表单**：用于录入和编辑生产数据

#### 4.1.3 流程组件

- **加工流程组件**：展示和编辑加工工序流程
- **审批流程组件**：展示和处理审批流程
- **状态流转组件**：展示和处理任务状态流转

### 4.2 业务组件开发流程

1. **需求分析**：明确组件的功能需求和使用场景
2. **设计阶段**：设计组件的API、Props、Events和Slots
3. **开发阶段**：实现组件的逻辑和样式
4. **测试阶段**：编写单元测试和集成测试
5. **文档阶段**：编写组件的使用说明和示例
6. **发布阶段**：将组件发布到组件库中

### 4.3 示例：模具列表组件

```vue
<template>
  <div class="mold-list">
    <!-- 筛选工具栏 -->
    <div class="filter-toolbar">
      <el-input
        v-model="filterForm.name"
        placeholder="模具名称"
        prefix-icon="Search"
        class="filter-input"
      />
      <el-select
        v-model="filterForm.status"
        placeholder="模具状态"
        class="filter-select"
      >
        <el-option label="全部" value="" />
        <el-option label="在用" value="using" />
        <el-option label="闲置" value="idle" />
        <el-option label="维修" value="repair" />
        <el-option label="报废" value="scrapped" />
      </el-select>
      <el-button
        type="primary"
        :icon="Search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        type="success"
        :icon="Plus"
        @click="handleCreate"
      >
        新建模具
      </el-button>
    </div>
    
    <!-- 模具表格 -->
    <Table
      :data="moldData"
      :loading="loading"
      :row-key="'id'"
      @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="模具编号" width="120" sortable />
      <el-table-column prop="name" label="模具名称" min-width="150" />
      <el-table-column prop="model" label="型号" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag
            :type="getStatusType(scope.row.status)"
            :effect="'light'"
          >
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" sortable />
      <el-table-column prop="updateTime" label="更新时间" width="180" sortable />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            :icon="Edit"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            :icon="Delete"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
          <el-button
            size="small"
            :icon="View"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
        </template>
      </el-table-column>
    </Table>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { Search, Plus, Edit, Delete, View } from '@element-plus/icons-vue';
import Table from '@/components/Table.vue';
import { getMoldList } from '@/api/mold';
import type { Mold } from '@/types';

// 筛选表单
const filterForm = reactive({
  name: '',
  status: ''
});

// 分页配置
const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
});

// 模具数据
const moldData = ref<Mold[]>([]);
const loading = ref(false);

// 获取模具列表
const fetchMoldList = async () => {
  loading.value = true;
  try {
    const response = await getMoldList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      name: filterForm.name,
      status: filterForm.status
    });
    moldData.value = response.data;
    pagination.total = response.total;
  } catch (error) {
    console.error('Failed to fetch mold list:', error);
  } finally {
    loading.value = false;
  }
};

// 初始加载
fetchMoldList();

// 处理搜索
const handleFilter = () => {
  pagination.page = 1;
  fetchMoldList();
};

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.page = 1;
  fetchMoldList();
};

// 处理当前页变化
const handleCurrentChange = (current: number) => {
  pagination.page = current;
  fetchMoldList();
};

// 处理行点击
const handleRowClick = (row: Mold) => {
  emit('row-click', row);
};

// 处理编辑
const handleEdit = (row: Mold) => {
  emit('edit', row);
};

// 处理删除
const handleDelete = (row: Mold) => {
  emit('delete', row);
};

// 处理查看
const handleView = (row: Mold) => {
  emit('view', row);
};

// 处理新建
const handleCreate = () => {
  emit('create');
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

.mold-list {
  background-color: $white;
  border-radius: $border-radius-lg;
  padding: $spacing-3;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  
  .filter-toolbar {
    display: flex;
    gap: $spacing-2;
    margin-bottom: $spacing-3;
    
    .filter-input {
      width: 200px;
    }
    
    .filter-select {
      width: 150px;
    }
  }
  
  .pagination {
    margin-top: $spacing-3;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
```

## 5. 响应式组件开发

响应式组件是指能够根据不同屏幕尺寸自动调整样式和行为的组件。对于蜂窝陶瓷模具管理系统，响应式组件尤为重要，因为它需要同时适配办公室的桌面电脑和生产现场的移动设备。

### 5.1 响应式设计原则

- **移动优先**：从移动设备开始设计，然后逐步扩展到更大的屏幕
- **流体布局**：使用相对单位定义容器宽度、间距和字体大小
- **弹性网格**：使用栅格系统，允许内容在不同屏幕尺寸下重新排列
- **断点设计**：在特定的屏幕尺寸下调整布局和内容
- **自适应组件**：组件能够根据屏幕尺寸自动调整样式和行为

### 5.2 响应式组件实现

#### 5.2.1 使用Element Plus的响应式工具

Element Plus提供了一套响应式工具，如栅格系统、响应式表单、响应式表格等，可以帮助开发者快速实现响应式设计。

#### 5.2.2 使用CSS媒体查询

对于自定义组件，可以使用CSS媒体查询来实现响应式设计：

```scss
// 导入变量
@import '../styles/variables.scss';

// 自定义组件样式
.custom-component {
  // 默认样式
  width: 100%;
  padding: $spacing-2;
  
  // 小屏幕样式
  @media (max-width: $breakpoint-sm) {
    padding: $spacing-1;
  }
  
  // 中等屏幕样式
  @media (min-width: $breakpoint-md) {
    padding: $spacing-3;
  }
  
  // 大屏幕样式
  @media (min-width: $breakpoint-lg) {
    padding: $spacing-4;
  }
}
```

#### 5.2.3 使用Vue的响应式API

对于需要根据屏幕尺寸调整行为的组件，可以使用Vue的响应式API：

```vue
<template>
  <div class="responsive-component">
    <div v-if="isMobile" class="mobile-layout">
      <!-- 移动端布局 -->
    </div>
    <div v-else class="desktop-layout">
      <!-- 桌面端布局 -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

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
</script>
```

### 5.3 示例：响应式导航组件

```vue
<template>
  <nav class="responsive-nav">
    <!-- 桌面端导航 -->
    <div v-if="!isMobile" class="desktop-nav">
      <ul class="nav-list">
        <li v-for="item in navItems" :key="item.path" class="nav-item">
          <router-link :to="item.path" class="nav-link">
            <el-icon :size="20"><component :is="item.icon" /></el-icon>
            <span class="nav-text">{{ item.label }}</span>
          </router-link>
        </li>
      </ul>
    </div>
    
    <!-- 移动端导航 -->
    <div v-else class="mobile-nav">
      <!-- 汉堡菜单按钮 -->
      <el-button
        type="text"
        :icon="Menu"
        @click="showMenu = !showMenu"
        class="hamburger-btn"
      />
      
      <!-- 移动端菜单 -->
      <div v-if="showMenu" class="mobile-menu">
        <ul class="nav-list">
          <li v-for="item in navItems" :key="item.path" class="nav-item">
            <router-link :to="item.path" class="nav-link" @click="showMenu = false">
              <el-icon :size="20"><component :is="item.icon" /></el-icon>
              <span class="nav-text">{{ item.label }}</span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Menu, Home, DataAnalysis, Tools, Box, Operation, TrendCharts, SetUp } from '@element-plus/icons-vue';

// 导航项
const navItems = [
  { path: '/home', label: '首页', icon: Home },
  { path: '/dashboard', label: '仪表盘', icon: DataAnalysis },
  { path: '/mold', label: '加工管理', icon: Tools },
  { path: '/storage', label: '模库管理', icon: Box },
  { path: '/tuning', label: '调模管理', icon: Operation },
  { path: '/production', label: '生产管理', icon: TrendCharts },
  { path: '/system', label: '系统管理', icon: SetUp }
];

// 是否为移动端
const isMobile = ref(false);
// 是否显示移动端菜单
const showMenu = ref(false);

// 监听窗口大小变化
const handleResize = () => {
  isMobile.value = window.innerWidth < 768;
  // 窗口大小变化时隐藏移动端菜单
  if (isMobile.value) {
    showMenu.value = false;
  }
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
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

.responsive-nav {
  background-color: $white;
  border-bottom: 1px solid $gray-200;
  padding: 0 $spacing-3;
  
  // 桌面端导航
  .desktop-nav {
    .nav-list {
      display: flex;
      gap: $spacing-4;
      list-style: none;
      padding: 0;
      margin: 0;
      
      .nav-item {
        .nav-link {
          display: flex;
          align-items: center;
          gap: $spacing-1;
          padding: $spacing-2 $spacing-3;
          color: $gray-700;
          text-decoration: none;
          border-radius: $border-radius-md;
          transition: all 0.3s ease;
          
          &:hover {
            background-color: $primary-light;
            color: $primary-color;
          }
          
          &.router-link-active {
            background-color: $primary-light;
            color: $primary-color;
            font-weight: $font-weight-medium;
          }
        }
      }
    }
  }
  
  // 移动端导航
  .mobile-nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .hamburger-btn {
      font-size: 20px;
    }
    
    .mobile-menu {
      position: absolute;
      top: 72px;
      left: 0;
      right: 0;
      background-color: $white;
      border-bottom: 1px solid $gray-200;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      z-index: 1000;
      
      .nav-list {
        list-style: none;
        padding: 0;
        margin: 0;
        
        .nav-item {
          .nav-link {
            display: flex;
            align-items: center;
            gap: $spacing-2;
            padding: $spacing-3;
            color: $gray-700;
            text-decoration: none;
            border-bottom: 1px solid $gray-100;
            transition: all 0.3s ease;
            
            &:hover {
              background-color: $primary-light;
              color: $primary-color;
            }
            
            &.router-link-active {
              background-color: $primary-light;
              color: $primary-color;
              font-weight: $font-weight-medium;
            }
          }
        }
      }
    }
  }
  
  // 响应式断点
  @media (min-width: 768px) {
    .mobile-nav {
      display: none;
    }
  }
  
  @media (max-width: 767px) {
    .desktop-nav {
      display: none;
    }
  }
}
</style>
```

## 6. 主题定制

主题定制是指支持动态切换主题的组件开发，它允许用户根据自己的偏好选择主题，如浅色主题、深色主题等。

### 6.1 主题定制方案

- **CSS变量**：使用CSS变量定义主题颜色、间距、圆角等
- **SCSS变量**：使用SCSS变量定义主题样式，并通过编译生成不同主题的CSS
- **动态样式**：使用JavaScript动态添加和移除CSS类，实现主题切换
- **Element Plus主题**：利用Element Plus的主题定制功能，实现统一的主题切换

### 6.2 主题定制实现

#### 6.2.1 使用CSS变量

```css
/* 主题变量 */
:root {
  /* 浅色主题 */
  --primary-color: #6150BF;
  --primary-light: #EEF2FF;
  --primary-dark: #4F46E5;
  --bg-color: #F9FAFB;
  --text-color: #111827;
  --border-color: #E5E7EB;
}

/* 深色主题 */
[data-theme="dark"] {
  --primary-color: #8B5CF6;
  --primary-light: #1E1B4B;
  --primary-dark: #A78BFA;
  --bg-color: #111827;
  --text-color: #F9FAFB;
  --border-color: #374151;
}
```

#### 6.2.2 使用Vue的响应式API

```vue
<template>
  <div class="theme-component" :class="{ 'dark-theme': isDark }">
    <!-- 组件内容 -->
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAppStore } from '@/stores/app';

// 使用appStore中的主题状态
const appStore = useAppStore();
const isDark = computed(() => appStore.theme === 'dark');
</script>

<style scoped lang="scss">
// 导入变量
@import '../styles/variables.scss';

.theme-component {
  // 默认浅色主题样式
  background-color: $bg-color;
  color: $text-color;
  border: 1px solid $border-color;
  
  // 深色主题样式
  &.dark-theme {
    background-color: $dark-bg-color;
    color: $dark-text-color;
    border: 1px solid $dark-border-color;
  }
}
</style>
```

## 7. 组件测试

组件测试是确保组件库质量的重要环节，它包括单元测试和集成测试。

### 7.1 单元测试

单元测试是指测试组件的单个功能，如Props、Events、Slots等。

#### 7.1.1 使用Vitest

项目使用Vitest作为单元测试框架，可以使用以下命令运行单元测试：

```bash
npm run test
```

#### 7.1.2 单元测试示例

```typescript
import { describe, it, expect, mount } from '@vue/test-utils';
import Table from '@/components/Table.vue';

describe('Table Component', () => {
  it('should render correctly with default props', () => {
    const wrapper = mount(Table);
    expect(wrapper.exists()).toBe(true);
  });
  
  it('should display data correctly', () => {
    const data = [
      { id: 1, name: '模具1', status: 'using' },
      { id: 2, name: '模具2', status: 'idle' },
      { id: 3, name: '模具3', status: 'repair' }
    ];
    const wrapper = mount(Table, {
      props: {
        data
      },
      slots: {
        default: `
          <el-table-column prop="id" label="模具编号" />
          <el-table-column prop="name" label="模具名称" />
          <el-table-column prop="status" label="模具状态" />
        `
      }
    });
    
    // 检查表格是否显示了3行数据
    const rows = wrapper.findAll('.el-table__row');
    expect(rows.length).toBe(3);
  });
  
  it('should emit row-click event when row is clicked', () => {
    const data = [
      { id: 1, name: '模具1', status: 'using' }
    ];
    const wrapper = mount(Table, {
      props: {
        data
      },
      slots: {
        default: `
          <el-table-column prop="id" label="模具编号" />
          <el-table-column prop="name" label="模具名称" />
        `
      }
    });
    
    // 点击表格行
    const row = wrapper.find('.el-table__row');
    row.trigger('click');
    
    // 检查是否触发了row-click事件
    expect(wrapper.emitted('row-click')).toBeTruthy();
    expect(wrapper.emitted('row-click')?.[0]?.[0]).toEqual(data[0]);
  });
});
```

### 7.2 集成测试

集成测试是指测试组件在实际应用中的表现，如与其他组件的交互、与后端API的交互等。

## 8. 组件文档

组件文档是组件库的重要组成部分，它提供了组件的使用说明、API文档和示例代码，帮助开发者快速了解和使用组件。

### 8.1 文档结构

组件文档应包含以下部分：

1. **组件名称**：清晰描述组件的功能
2. **组件介绍**：组件的用途和使用场景
3. **基本用法**：组件的基本使用示例
4. **API文档**：
   - Props：组件的属性列表
   - Events：组件的事件列表
   - Slots：组件的插槽列表
   - Methods：组件的方法列表
5. **高级用法**：组件的高级使用示例
6. **注意事项**：使用组件时的注意事项

### 8.2 示例：Table组件文档

```markdown
# Table 表格

## 介绍

表格组件用于展示大量结构化数据，支持排序、筛选、分页、多选等功能。

## 基本用法

```vue
<template>
  <Table :data="tableData" @row-click="handleRowClick">
    <el-table-column prop="id" label="ID" width="80" />
    <el-table-column prop="name" label="名称" min-width="150" />
    <el-table-column prop="status" label="状态" width="100" />
    <el-table-column prop="createTime" label="创建时间" width="180" />
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Table from '@/components/Table.vue';

const tableData = ref([
  { id: 1, name: '项目1', status: 'active', createTime: '2023-01-01' },
  { id: 2, name: '项目2', status: 'inactive', createTime: '2023-01-02' },
  { id: 3, name: '项目3', status: 'active', createTime: '2023-01-03' }
]);

const handleRowClick = (row) => {
  console.log('Row clicked:', row);
};
</script>
```

## API

### Props

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| data | array | [] | 表格数据 |
| height | string / number | '' | 表格高度 |
| maxHeight | string / number | '' | 表格最大高度 |
| stripe | boolean | false | 是否为斑马纹表格 |
| border | boolean | false | 是否带有纵向边框 |
| fit | boolean | true | 列的宽度是否自撑开 |
| showHeader | boolean | true | 是否显示表头 |
| highlightCurrentRow | boolean | false | 是否高亮当前行 |
| currentRowKey | string / number | '' | 当前行的key |
| rowKey | string / function | '' | 行数据的Key |
| emptyText | string | '暂无数据' | 空表格的文本内容 |
| loading | boolean | false | 是否显示加载状态 |

### Events

| 事件名 | 说明 | 参数 |
| --- | --- | --- |
| select | 当用户手动勾选数据行的 Checkbox 时触发的事件 | selection, row |
| select-all | 当用户手动勾选全选 Checkbox 时触发的事件 | selection |
| selection-change | 当选择项发生变化时会触发该事件 | selection |
| row-click | 当某一行被点击时会触发该事件 | row, column, event |
| row-dblclick | 当某一行被双击时会触发该事件 | row, column, event |
| sort-change | 当表格的排序条件发生变化时会触发该事件 | { column, prop, order } |
| filter-change | 当表格的筛选条件发生变化时会触发该事件 | filters |
| current-change | 当表格的当前行发生变化时会触发该事件 | currentRow, oldCurrentRow |

### Slots

| 插槽名 | 说明 |
| --- | --- |
| default | 表格列插槽 |
| toolbar | 表格工具栏插槽 |
| pagination | 表格分页插槽 |

## 高级用法

### 带分页的表格

```vue
<template>
  <Table :data="tableData" :loading="loading">
    <template #toolbar>
      <el-button type="primary" :icon="Plus" @click="handleAdd">添加</el-button>
    </template>
    
    <el-table-column type="selection" width="55" />
    <el-table-column prop="id" label="ID" width="80" sortable />
    <el-table-column prop="name" label="名称" min-width="150" />
    <el-table-column prop="status" label="状态" width="100">
      <template #default="scope">
        <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
          {{ scope.row.status }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="创建时间" width="180" sortable />
    <el-table-column label="操作" width="150" fixed="right">
      <template #default="scope">
        <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
        <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
      </template>
    </el-table-column>
    
    <template #pagination>
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </template>
  </Table>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { Plus } from '@element-plus/icons-vue';
import Table from '@/components/Table.vue';

const tableData = ref([]);
const loading = ref(false);
const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
});

// 模拟获取数据
const fetchData = () => {
  loading.value = true;
  setTimeout(() => {
    // 模拟数据
    const data = [];
    for (let i = 1; i <= pagination.pageSize; i++) {
      const index = (pagination.page - 1) * pagination.pageSize + i;
      data.push({
        id: index,
        name: `项目${index}`,
        status: index % 2 === 0 ? 'active' : 'inactive',
        createTime: `2023-01-${String(index).padStart(2, '0')}`
      });
    }
    tableData.value = data;
    pagination.total = 100;
    loading.value = false;
  }, 1000);
};

// 初始化数据
fetchData();

const handleAdd = () => {
  // 添加操作
};

const handleEdit = (row) => {
  // 编辑操作
};

const handleDelete = (row) => {
  // 删除操作
};

const handleSizeChange = (size) => {
  pagination.pageSize = size;
  pagination.page = 1;
  fetchData();
};

const handleCurrentChange = (current) => {
  pagination.page = current;
  fetchData();
};
</script>
```

## 注意事项

1. 当表格数据量较大时，建议使用`height`或`maxHeight`属性固定表格高度，避免页面滚动性能问题
2. 对于复杂的表格列，可以使用`template`插槽自定义列内容
3. 当需要进行大量数据渲染时，建议使用虚拟滚动技术，提高表格性能
4. 表格组件继承了Element Plus Table的所有Props和Events，可以参考Element Plus Table文档使用更多功能
```
```

## 9. 最佳实践

### 9.1 组件设计原则

- **单一职责原则**：每个组件只负责一个功能，保持组件的简洁性
- **可复用性原则**：组件应具有良好的可复用性，避免过度定制
- **可扩展性原则**：组件应具有良好的可扩展性，支持通过Props、Events和Slots进行扩展
- **易用性原则**：组件的API应简单易用，避免过于复杂
- **性能优化原则**：组件应考虑性能优化，避免不必要的渲染和计算

### 9.2 组件开发注意事项

- 避免组件嵌套过深，保持组件的扁平结构
- 避免在组件中直接操作DOM，使用Vue的响应式API
- 避免在组件中进行复杂的计算，考虑使用computed或watch
- 避免在组件中硬编码样式，使用设计变量和主题定制
- 避免在组件中直接调用后端API，考虑使用状态管理或组合式API

### 9.3 组件测试注意事项

- 为所有组件编写单元测试，确保组件的质量
- 测试组件的各种边界情况，如空数据、大量数据、特殊字符等
- 测试组件的交互功能，如点击、输入、选择等
- 测试组件的响应式设计，确保在不同屏幕尺寸下的表现

### 9.4 组件文档注意事项

- 提供清晰的组件使用示例，帮助开发者快速上手
- 详细描述组件的API，包括Props、Events、Slots等
- 提供高级用法示例，展示组件的高级功能
- 及时更新组件文档，确保与组件实现保持一致

## 10. 结论

组件库是蜂窝陶瓷模具管理系统的核心基础设施，它提供了一套统一、可复用、响应式的UI组件，用于构建系统的各个页面。通过遵循本指南的开发规范和最佳实践，开发团队可以开发出高质量、可维护、可扩展的组件库，满足系统的需求，提高开发效率，提升用户体验。