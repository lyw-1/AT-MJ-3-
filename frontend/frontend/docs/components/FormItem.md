# FormItem 表单项组件

## 组件简介
FormItem 表单项组件是一个基于 Element Plus 的 ElFormItem 封装的通用表单项组件，用于包裹表单控件并提供标签、验证等功能。

## 基本用法
```vue
<template>
  <FormItem label="用户名" prop="username">
    <Input v-model="username" placeholder="请输入用户名" />
  </FormItem>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import FormItem from './FormItem.vue';
import Input from './Input.vue';

const username = ref('');
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| prop | string | '' | 表单域model字段，用于绑定表单验证规则 |
| label | string | '' | 标签文本 |
| labelWidth | string / number | '' | 标签宽度，覆盖Form组件的labelWidth |
| required | boolean | false | 是否必填 |
| rules | any / any[] | [] | 表单验证规则，优先级高于Form组件的rules |
| error | string | '' | 错误信息，设置此属性会覆盖验证的错误信息 |
| validateStatus | string | undefined | 验证状态，可选值：success / warning / error / validating |
| inlineMessage | boolean | undefined | 是否为行内表单校验信息，覆盖Form组件的inlineMessage |
| showMessage | boolean | true | 是否显示校验信息 |
| size | string | 'default' | 表单项尺寸，可选值：large / default / small |
| tag | string | 'div' | 自定义标签 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| validate | 验证事件 | (prop: string, isValid: boolean, message: string) |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 表单项内容，通常是表单控件 |
| help | 辅助文本插槽，用于显示额外的说明信息 |
| error | 错误信息插槽，用于自定义错误信息的显示 |
| extra | 额外内容插槽，用于显示额外的内容 |

## 代码示例

### 带辅助文本的表单项
```vue
<template>
  <FormItem label="用户名" prop="username">
    <Input v-model="username" placeholder="请输入用户名" />
    <template #help>
      用户名长度在 3 到 20 个字符之间
    </template>
  </FormItem>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const username = ref('');
</script>
```

### 自定义错误信息
```vue
<template>
  <FormItem label="密码" prop="password" :error="passwordError">
    <Input v-model="password" type="password" placeholder="请输入密码" />
    <template #error>
      <span style="color: #f56c6c;">{{ passwordError }}</span>
    </template>
  </FormItem>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const password = ref('');
const passwordError = ref('');

watch(password, (newVal) => {
  if (newVal.length < 6) {
    passwordError.value = '密码长度不能少于 6 个字符';
  } else {
    passwordError.value = '';
  }
});
</script>
```

### 自定义验证状态
```vue
<template>
  <FormItem label="邮箱" prop="email" :validateStatus="emailStatus">
    <Input v-model="email" type="email" placeholder="请输入邮箱" />
  </FormItem>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const email = ref('');
const emailStatus = ref('');

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

watch(email, (newVal) => {
  if (!newVal) {
    emailStatus.value = '';
  } else if (emailRegex.test(newVal)) {
    emailStatus.value = 'success';
  } else {
    emailStatus.value = 'error';
  }
});
</script>
```

## 注意事项
1. FormItem 组件需要配合 Form 组件使用，不能单独使用。
2. 每个 FormItem 组件可以通过 `prop` 属性关联 Form 组件的 `rules` 中的验证规则。
3. 可以通过 `required` 属性标记表单项为必填项，会自动添加星号标记。
4. 使用 `rules` 属性可以为单个表单项设置独立的验证规则，优先级高于 Form 组件的 `rules`。
5. 通过插槽可以自定义表单项的辅助文本、错误信息和额外内容。
