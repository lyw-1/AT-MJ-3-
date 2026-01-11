# Table 表格组件

## 组件简介
Table 表格组件是一个基于 Element Plus 的 ElTable 封装的通用表格组件，用于展示和处理大量数据，支持排序、筛选、分页等功能。

## 基本用法
```vue
<template>
  <Table :data="tableData">
    <el-table-column prop="date" label="日期" width="180" />
    <el-table-column prop="name" label="姓名" width="180" />
    <el-table-column prop="address" label="地址" />
    <el-table-column label="操作" width="150" fixed="right">
      <template #default="scope">
        <Button type="primary" size="small" @click="handleEdit(scope.row)">编辑</Button>
        <Button type="danger" size="small" @click="handleDelete(scope.row)" style="margin-left: 10px;">删除</Button>
      </template>
    </el-table-column>
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Table from './Table.vue';
import Button from './Button.vue';

const tableData = ref([
  {
    date: '2023-05-10',
    name: '张三',
    address: '北京市朝阳区'
  },
  {
    date: '2023-05-11',
    name: '李四',
    address: '上海市浦东新区'
  },
  {
    date: '2023-05-12',
    name: '王五',
    address: '广州市天河区'
  }
]);

const handleEdit = (row: any) => {
  console.log('编辑行:', row);
};

const handleDelete = (row: any) => {
  console.log('删除行:', row);
};
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| data | any[] | 必填 | 表格数据 |
| height | string / number | '' | 表格高度 |
| maxHeight | string / number | '' | 表格最大高度 |
| stripe | boolean | false | 是否为斑马纹表格 |
| border | boolean | false | 是否带有纵向边框 |
| fit | boolean | true | 列的宽度是否自撑开 |
| showHeader | boolean | true | 是否显示表头 |
| highlightCurrentRow | boolean | false | 是否高亮当前行 |
| currentRowKey | string / number | '' | 当前行的key |
| rowClassName | string / function | '' | 行的className |
| rowStyle | object / function | {} | 行的style |
| cellClassName | string / function | '' | 单元格的className |
| cellStyle | object / function | {} | 单元格的style |
| headerRowClassName | string / function | '' | 表头行的className |
| headerRowStyle | object / function | {} | 表头行的style |
| headerCellClassName | string / function | '' | 表头单元格的className |
| headerCellStyle | object / function | {} | 表头单元格的style |
| rowKey | string / function | '' | 行数据的Key |
| emptyText | string | '暂无数据' | 空表格的文本内容 |
| defaultExpandAll | boolean | false | 是否默认展开所有行 |
| expandRowKeys | any[] | [] | 展开行的Key数组 |
| defaultSort | object | { prop: '', order: null } | 默认排序规则 |
| tooltipEffect | string | 'dark' | 工具提示效果，可选值：dark / light |
| showSummary | boolean | false | 是否在表尾显示合计行 |
| sumText | string | '合计' | 合计行第一列的文本 |
| summaryMethod | function | undefined | 自定义合计方法 |
| spanMethod | function | undefined | 合并单元格方法 |
| selectOnIndeterminate | boolean | false | 在多选表格中，是否在选择一个节点的同时选中其所有子节点 |
| indent | number | 16 | 树状结构中父子节点之间的缩进 |
| treeProps | object | { children: 'children', hasChildren: 'hasChildren' } | 树状结构的配置项 |
| lazy | boolean | false | 是否懒加载子节点数据 |
| load | function | undefined | 加载子节点数据的方法 |
| tooltipTrigger | string | 'hover' | 提示框触发方式，可选值：hover / focus / manual |
| tooltipPlacement | string | 'top' | 提示框位置 |
| resizeColumnWidth | number | 50 | 列宽调整触发的宽度 |
| draggable | boolean | false | 是否支持列拖拽 |
| flexGrow | number | 1 | 表格的flex-grow属性 |
| flexShrink | number | 1 | 表格的flex-shrink属性 |
| size | string | 'default' | 表格尺寸，可选值：large / default / small |
| tag | string | 'table' | 自定义标签 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| select | 选择事件 | (selection: any[], row: any) |
| select-all | 全选事件 | (selection: any[]) |
| selection-change | 选择项变化事件 | (selection: any[]) |
| cell-mouse-enter | 单元格鼠标进入事件 | (row: any, column: any, cell: any, event: Event) |
| cell-mouse-leave | 单元格鼠标离开事件 | (row: any, column: any, cell: any, event: Event) |
| cell-click | 单元格点击事件 | (row: any, column: any, cell: any, event: Event) |
| cell-dblclick | 单元格双击事件 | (row: any, column: any, cell: any, event: Event) |
| cell-contextmenu | 单元格右键菜单事件 | (row: any, column: any, cell: any, event: Event) |
| row-mouse-enter | 行鼠标进入事件 | (row: any, column: any, cell: any, event: Event) |
| row-mouse-leave | 行鼠标离开事件 | (row: any, column: any, cell: any, event: Event) |
| row-click | 行点击事件 | (row: any, column: any, event: Event) |
| row-dblclick | 行双击事件 | (row: any, column: any, event: Event) |
| row-contextmenu | 行右键菜单事件 | (row: any, column: any, event: Event) |
| header-click | 表头点击事件 | (column: any, event: Event) |
| header-contextmenu | 表头右键菜单事件 | (column: any, event: Event) |
| sort-change | 排序变化事件 | ({ column: any, prop: string, order: string }) |
| filter-change | 筛选变化事件 | (filters: any) |
| current-change | 当前行变化事件 | (currentRow: any, oldCurrentRow: any) |
| expand-change | 展开行变化事件 | (row: any, expandedRows: any[]) |
| selection | 选择事件 | (selection: any[]) |
| cell-update | 单元格更新事件 | (row: any, column: any, newValue: any, oldValue: any) |
| column-resize | 列宽调整事件 | (column: any, width: number, oldWidth: number) |
| column-drag-end | 列拖拽结束事件 | (column: any, columns: any[]) |
| column-visible-change | 列可见性变化事件 | (column: any, visible: boolean) |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 表格列配置，包含多个 el-table-column 组件 |
| toolbar | 表格工具栏，用于放置操作按钮等 |
| pagination | 表格分页，用于放置分页组件 |

## 代码示例

### 带选择功能的表格
```vue
<template>
  <Table :data="tableData" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column prop="date" label="日期" width="180" />
    <el-table-column prop="name" label="姓名" width="180" />
    <el-table-column prop="address" label="地址" />
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tableData = ref([
  // 数据内容...
]);

const handleSelectionChange = (selection: any[]) => {
  console.log('选中的行:', selection);
};
</script>
```

### 带排序和筛选的表格
```vue
<template>
  <Table :data="tableData" @sort-change="handleSortChange" @filter-change="handleFilterChange">
    <el-table-column prop="date" label="日期" width="180" sortable />
    <el-table-column prop="name" label="姓名" width="180" filterable :filters="[{ text: '张三', value: '张三' }, { text: '李四', value: '李四' }]" />
    <el-table-column prop="address" label="地址" />
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tableData = ref([
  // 数据内容...
]);

const handleSortChange = ({ column, prop, order }: any) => {
  console.log('排序变化:', { column, prop, order });
};

const handleFilterChange = (filters: any) => {
  console.log('筛选变化:', filters);
};
</script>
```

### 带分页的表格
```vue
<template>
  <Table :data="tableData">
    <el-table-column prop="date" label="日期" width="180" />
    <el-table-column prop="name" label="姓名" width="180" />
    <el-table-column prop="address" label="地址" />
    
    <template #pagination>
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </template>
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tableData = ref([
  // 数据内容...
]);

const total = ref(100);
const pageSize = ref(10);
const currentPage = ref(1);

const handleSizeChange = (val: number) => {
  console.log(`每页 ${val} 条`);
  pageSize.value = val;
};

const handleCurrentChange = (val: number) => {
  console.log(`当前页: ${val}`);
  currentPage.value = val;
};
</script>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
```

### 带斑马纹和边框的表格
```vue
<template>
  <Table :data="tableData" stripe border>
    <el-table-column prop="date" label="日期" width="180" />
    <el-table-column prop="name" label="姓名" width="180" />
    <el-table-column prop="address" label="地址" />
  </Table>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tableData = ref([
  // 数据内容...
]);
</script>
```

## 注意事项
1. Table 组件需要配合 Element Plus 的 el-table-column 组件使用，用于定义表格列。
2. 可以通过 `data` 属性传递表格数据，支持动态更新。
3. 支持多种交互功能，如选择、排序、筛选、分页等。
4. 可以通过插槽自定义表格的工具栏和分页。
5. 对于大量数据，建议使用虚拟滚动或分页来优化性能。
6. 支持树状数据展示，需要设置 `treeProps` 属性并在数据中包含 children 字段。
