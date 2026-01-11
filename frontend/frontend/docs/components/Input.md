# Input 输入框组件

## 组件简介
Input 输入框组件是一个基于 Element Plus 的 ElInput 封装的通用输入框组件，用于接收用户输入的文本、数字等内容。

## 基本用法
```vue
<template>
  <div>
    <Input v-model="inputValue" placeholder="请输入内容" />
    <p>输入内容: {{ inputValue }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const inputValue = ref('');
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| modelValue | string / number | 必填 | 绑定值 |
| type | string | 'text' | 输入框类型，可选值：text / password / textarea / number / email / url / date / datetime-local / month / time / week |
| placeholder | string | '' | 占位文本 |
| disabled | boolean | false | 是否禁用 |
| size | string | 'default' | 输入框尺寸，可选值：large / default / small |
| maxlength | number | undefined | 最大输入长度 |
| minlength | number | undefined | 最小输入长度 |
| showWordLimit | boolean | false | 是否显示输入字数统计 |
| readonly | boolean | false | 是否只读 |
| clearable | boolean | false | 是否可清空 |
| showPassword | boolean | false | 是否显示切换密码图标 |
| prefixIcon | string | '' | 前缀图标 |
| suffixIcon | string | '' | 后缀图标 |
| autocomplete | string | 'off' | 原生autocomplete属性 |
| autofocus | boolean | false | 是否自动获取焦点 |
| name | string | '' | 原生name属性 |
| tabindex | number | 0 | 原生tabindex属性 |
| validateEvent | boolean | true | 是否触发表单验证 |
| resize | string | 'none' | 文本域是否可调整大小，可选值：none / both / horizontal / vertical |
| autosize | boolean / object | false | 文本域自适应高度，可设置为true或对象{ minRows: number; maxRows: number } |
| fit | string | 'cover' | 图标尺寸适应方式，可选值：cover / contain / fill / none / scale-down |
| rows | number | 2 | 输入框行数，仅在textarea类型下有效 |
| cols | number | 20 | 输入框列数，仅在textarea类型下有效 |
| inputStyle | string / object | {} | 输入框样式 |
| inputClass | string / object | '' | 输入框类名 |
| showMessage | boolean | true | 是否显示校验信息 |
| inlineMessage | boolean | false | 是否为行内表单校验信息 |
| statusIcon | boolean | false | 是否显示校验状态图标 |
| tag | string | 'input' | 自定义标签 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| update:modelValue | 绑定值变化事件 | (value: string / number) |
| input | 输入事件 | (value: string / number) |
| change | 输入值改变事件 | (value: string / number) |
| blur | 失去焦点事件 | (e: FocusEvent) |
| focus | 获得焦点事件 | (e: FocusEvent) |
| clear | 清空事件 | - |
| mouseenter | 鼠标进入事件 | - |
| mouseleave | 鼠标离开事件 | - |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 输入框内容，仅在特定类型下可用 |
| prefix | 输入框前缀内容 |
| suffix | 输入框后缀内容 |
| prepend | 输入框前置内容 |
| append | 输入框后置内容 |

## 代码示例

### 密码输入框
```vue
<template>
  <Input
    v-model="password"
    type="password"
    placeholder="请输入密码"
    showPassword
    clearable
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

const password = ref('');
</script>
```

### 带图标的输入框
```vue
<template>
  <Input
    v-model="searchText"
    placeholder="请输入搜索内容"
    prefixIcon="el-icon-search"
    clearable
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

const searchText = ref('');
</script>
```

### 多行文本输入框
```vue
<template>
  <Input
    v-model="textareaValue"
    type="textarea"
    placeholder="请输入多行文本"
    :rows="4"
    resize="vertical"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

const textareaValue = ref('');
</script>
```

### 自适应高度的文本域
```vue
<template>
  <Input
    v-model="autosizeValue"
    type="textarea"
    placeholder="请输入自适应高度的文本"
    :autosize="{ minRows: 2, maxRows: 6 }"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

const autosizeValue = ref('');
</script>
```

### 带字数统计的输入框
```vue
<template>
  <Input
    v-model="textValue"
    placeholder="请输入内容，最多100个字符"
    :maxlength="100"
    showWordLimit
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

const textValue = ref('');
</script>
```

## 注意事项
1. 当使用 `type="textarea"` 时，可以通过 `rows` 属性设置行数，通过 `resize` 属性控制是否可以调整大小。
2. 使用 `autosize` 属性可以实现文本域的自适应高度，适合需要输入大量文本的场景。
3. `showPassword` 属性仅在 `type="password"` 时有效，用于切换密码的显示/隐藏。
4. `showWordLimit` 属性需要配合 `maxlength` 属性使用，用于显示输入字数统计。
5. `clearable` 属性可以添加清空按钮，方便用户快速清除输入内容。
