# Button 按钮组件

## 组件简介
Button 按钮组件是一个基于 Element Plus 的 ElButton 封装的通用按钮组件，用于触发操作或提交表单。

## 基本用法
```vue
<template>
  <Button type="primary" @click="handleClick">主要按钮</Button>
  <Button type="success">成功按钮</Button>
  <Button type="warning">警告按钮</Button>
  <Button type="danger">危险按钮</Button>
  <Button type="info">信息按钮</Button>
  <Button type="text">文本按钮</Button>
</template>

<script setup lang="ts">
const handleClick = () => {
  console.log('按钮被点击了');
};
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| type | string | 'default' | 按钮类型，可选值：primary / success / warning / danger / info / text |
| size | string | 'default' | 按钮尺寸，可选值：large / default / small |
| plain | boolean | false | 是否为朴素按钮 |
| round | boolean | false | 是否为圆角按钮 |
| circle | boolean | false | 是否为圆形按钮 |
| loading | boolean | false | 是否加载中状态 |
| disabled | boolean | false | 是否禁用状态 |
| icon | string | '' | 按钮图标 |
| autofocus | boolean | false | 是否自动获取焦点 |
| nativeType | string | 'button' | 原生type属性，可选值：button / submit / reset |
| fit | string | 'cover' | 图标尺寸适应方式，可选值：cover / contain / fill / none / scale-down |
| tag | string | 'button' | 按钮标签类型 |
| effect | string | 'light' | 主题效果，可选值：light / dark / plain |
| dark | boolean | false | 是否为暗黑模式 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| click | 点击事件 | (e: MouseEvent) |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 按钮文本内容 |

## 代码示例

### 不同尺寸的按钮
```vue
<template>
  <Button type="primary" size="large">大按钮</Button>
  <Button type="primary">默认按钮</Button>
  <Button type="primary" size="small">小按钮</Button>
</template>
```

### 加载状态的按钮
```vue
<template>
  <Button type="primary" :loading="loading" @click="handleClick">加载中</Button>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const loading = ref(false);

const handleClick = () => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 2000);
};
</script>
```

### 图标按钮
```vue
<template>
  <Button type="primary" icon="el-icon-search">搜索</Button>
  <Button type="primary" icon="el-icon-plus">新增</Button>
  <Button type="primary" circle icon="el-icon-delete"></Button>
</template>
```

### 朴素按钮
```vue
<template>
  <Button type="primary" plain>朴素按钮</Button>
  <Button type="success" plain>成功朴素</Button>
  <Button type="warning" plain>警告朴素</Button>
  <Button type="danger" plain>危险朴素</Button>
</template>
```

### 圆角按钮
```vue
<template>
  <Button type="primary" round>圆角按钮</Button>
  <Button type="success" round>成功圆角</Button>
  <Button type="warning" round>警告圆角</Button>
  <Button type="danger" round>危险圆角</Button>
</template>
```

## 注意事项
1. 当使用 `circle` 属性时，建议不要设置按钮文本，只使用图标。
2. 当 `loading` 属性为 `true` 时，按钮会自动禁用。
3. 按钮的 `type` 属性会影响按钮的样式和交互效果。
4. 按钮的 `size` 属性会影响按钮的尺寸和内边距。
5. 按钮的 `plain` 属性会改变按钮的样式，使其更适合作为次要操作按钮。
