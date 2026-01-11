# Select 选择器组件

## 组件简介
Select 选择器组件是一个基于 Element Plus 的 ElSelect 封装的通用选择器组件，用于从多个选项中选择一个或多个值，支持搜索、远程加载等功能。

## 基本用法
```vue
<template>
  <div>
    <Select v-model="selectedValue" placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </Select>
    <p>选中值: {{ selectedValue }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Select from './Select.vue';

const selectedValue = ref('');
const options = ref([
  { value: 'option1', label: '选项1' },
  { value: 'option2', label: '选项2' },
  { value: 'option3', label: '选项3' },
  { value: 'option4', label: '选项4' },
  { value: 'option5', label: '选项5' }
]);
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| modelValue | string / number / boolean / object / array | 必填 | 绑定值，多选时为数组 |
| multiple | boolean | false | 是否多选 |
| disabled | boolean | false | 是否禁用 |
| size | string | 'default' | 选择器尺寸，可选值：large / default / small |
| clearable | boolean | false | 是否可清空 |
| collapseTags | boolean | false | 是否折叠多选标签 |
| multipleLimit | number | 0 | 多选时最多显示的标签数，0表示不限制 |
| name | string | '' | 原生name属性 |
| autocomplete | string | 'off' | 原生autocomplete属性 |
| autofocus | boolean | false | 是否自动获取焦点 |
| placeholder | string | '' | 占位文本 |
| filterable | boolean | false | 是否可搜索 |
| allowCreate | boolean | false | 是否允许创建新条目 |
| filterMethod | function | undefined | 自定义搜索方法 |
| remote | boolean | false | 是否为远程搜索 |
| remoteMethod | function | undefined | 远程搜索方法 |
| loading | boolean | false | 是否正在加载 |
| loadingText | string | '加载中' | 加载时的文本 |
| noMatchText | string | '无匹配数据' | 无匹配结果的文本 |
| noDataText | string | '无数据' | 无数据时的文本 |
| popperClass | string | '' | 下拉框的类名 |
| reserveKeyword | boolean | true | 是否保留关键字 |
| defaultFirstOption | boolean | false | 是否默认选中第一个选项 |
| teleported | boolean | true | 是否使用teleport |
| persistent | boolean | false | 是否保持下拉框显示 |
| automaticDropdown | boolean | false | 是否自动弹出下拉框 |
| fitInputWidth | boolean | false | 下拉框是否适应输入框宽度 |
| suffixIcon | string | '' | 自定义后缀图标 |
| prefixIcon | string | '' | 自定义前缀图标 |
| clearIcon | string | '' | 自定义清除图标 |
| checkIcon | string | '' | 自定义选中图标 |
| createIcon | string | '' | 自定义创建图标 |
| effect | string | 'light' | 主题效果，可选值：light / dark / plain |
| fallbackOption | any | undefined | 回退选项 |
| highlightFirstItem | boolean | false | 是否高亮第一个选项 |
| tag | string | 'select' | 自定义标签 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| update:modelValue | 绑定值变化事件 | (value: any) |
| change | 选中值变化事件 | (value: any) |
| visible-change | 下拉框显示/隐藏事件 | (visible: boolean) |
| remove-tag | 移除标签事件 | (tag: any) |
| clear | 清空事件 | - |
| blur | 失去焦点事件 | (e: FocusEvent) |
| focus | 获得焦点事件 | (e: FocusEvent) |
| popper-append-to-body | 下拉框挂载到body事件 | (appendToBody: boolean) |
| remove-all-tags | 移除所有标签事件 | - |
| current-change | 当前选中项变化事件 | (current: any) |
| focus-change | 焦点变化事件 | (focused: boolean) |
| mouseenter | 鼠标进入事件 | (e: MouseEvent) |
| mouseleave | 鼠标离开事件 | (e: MouseEvent) |
| keydown | 键盘事件 | (e: KeyboardEvent) |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 选择器选项，包含多个 el-option 组件 |
| prefix | 选择器前缀内容 |
| suffix | 选择器后缀内容 |
| clear | 清除按钮内容 |
| check | 选中图标内容 |
| create | 创建图标内容 |
| empty | 空状态内容 |
| loading | 加载状态内容 |
| popper-bottom | 下拉框底部内容 |
| tag | 标签内容 |

## 代码示例

### 多选选择器
```vue
<template>
  <Select
    v-model="selectedValues"
    multiple
    placeholder="请选择"
    clearable
    collapseTags
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </Select>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const selectedValues = ref([]);
const options = ref([
  { value: 'option1', label: '选项1' },
  { value: 'option2', label: '选项2' },
  { value: 'option3', label: '选项3' },
  { value: 'option4', label: '选项4' },
  { value: 'option5', label: '选项5' }
]);
</script>
```

### 可搜索选择器
```vue
<template>
  <Select
    v-model="selectedValue"
    filterable
    placeholder="请输入搜索内容"
    clearable
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </Select>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const selectedValue = ref('');
const options = ref([
  { value: 'beijing', label: '北京' },
  { value: 'shanghai', label: '上海' },
  { value: 'guangzhou', label: '广州' },
  { value: 'shenzhen', label: '深圳' },
  { value: 'hangzhou', label: '杭州' }
]);
</script>
```

### 远程搜索选择器
```vue
<template>
  <Select
    v-model="selectedValue"
    remote
    :remote-method="remoteMethod"
    :loading="loading"
    placeholder="请输入城市名称"
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </Select>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const selectedValue = ref('');
const options = ref([]);
const loading = ref(false);

const remoteMethod = (query: string) => {
  if (query !== '') {
    loading.value = true;
    // 模拟远程请求
    setTimeout(() => {
      loading.value = false;
      const cities = [
        { value: 'beijing', label: '北京' },
        { value: 'shanghai', label: '上海' },
        { value: 'guangzhou', label: '广州' },
        { value: 'shenzhen', label: '深圳' },
        { value: 'hangzhou', label: '杭州' },
        { value: 'chengdu', label: '成都' },
        { value: 'wuhan', label: '武汉' }
      ];
      options.value = cities.filter(city => {
        return city.label.toLowerCase().indexOf(query.toLowerCase()) > -1;
      });
    }, 200);
  } else {
    options.value = [];
  }
};
</script>
```

### 带图标的选择器
```vue
<template>
  <Select
    v-model="selectedValue"
    placeholder="请选择"
    prefixIcon="el-icon-search"
    clearable
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </Select>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const selectedValue = ref('');
const options = ref([
  { value: 'option1', label: '选项1' },
  { value: 'option2', label: '选项2' },
  { value: 'option3', label: '选项3' }
]);
</script>
```

## 注意事项
1. Select 组件需要配合 Element Plus 的 el-option 组件使用，用于定义选项。
2. 多选模式下，绑定值应为数组类型。
3. 使用 `filterable` 属性可以启用搜索功能，配合 `filterMethod` 可以自定义搜索逻辑。
4. 远程搜索时，需要设置 `remote` 属性为 true，并通过 `remoteMethod` 实现远程数据加载。
5. 使用 `collapseTags` 属性可以折叠多选标签，配合 `multipleLimit` 可以限制最多显示的标签数。
6. `allowCreate` 属性允许用户创建新的选项，适合需要动态添加选项的场景。
