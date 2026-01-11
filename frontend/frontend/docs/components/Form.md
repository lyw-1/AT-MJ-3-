# Form 表单组件

## 组件简介
Form 表单组件是一个基于 Element Plus 的 ElForm 封装的通用表单组件，用于表单数据的收集、验证和提交。

## 基本用法
```vue
<template>
  <Form :model="formData" :rules="rules" @submit="handleSubmit">
    <FormItem label="用户名" prop="username">
      <Input v-model="formData.username" placeholder="请输入用户名" />
    </FormItem>
    <FormItem label="密码" prop="password">
      <Input v-model="formData.password" type="password" placeholder="请输入密码" />
    </FormItem>
    <FormItem label="确认密码" prop="confirmPassword">
      <Input v-model="formData.confirmPassword" type="password" placeholder="请确认密码" />
    </FormItem>
    <FormItem>
      <Button type="primary" native-type="submit">提交</Button>
      <Button @click="handleReset">重置</Button>
    </FormItem>
  </Form>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Form from './Form.vue';
import FormItem from './FormItem.vue';
import Input from './Input.vue';
import Button from './Button.vue';

const formData = ref({
  username: '',
  password: '',
  confirmPassword: ''
});

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== formData.value.password) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const handleSubmit = (e: Event) => {
  e.preventDefault();
  console.log('表单提交', formData.value);
};

const handleReset = () => {
  formData.value = {
    username: '',
    password: '',
    confirmPassword: ''
  };
};
</script>
```

## Props
| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| model | Record<string, any> | 必填 | 表单数据对象 |
| rules | Record<string, any[]> | {} | 表单验证规则 |
| labelPosition | string | 'right' | 表单域标签的位置，可选值：left / right / top |
| labelWidth | string / number | '' | 表单域标签的宽度 |
| labelSuffix | string | '' | 表单域标签的后缀 |
| inline | boolean | false | 是否为行内表单 |
| inlineMessage | boolean | false | 是否为行内表单校验信息 |
| statusIcon | boolean | false | 是否显示校验状态图标 |
| validateOnRuleChange | boolean | true | 是否在表单规则改变时立即触发校验 |
| hideRequiredAsterisk | boolean | false | 是否隐藏必填字段的星号 |
| disabled | boolean | false | 是否禁用表单 |
| size | string | 'default' | 表单尺寸，可选值：large / default / small |
| showMessage | boolean | true | 是否显示校验信息 |
| tag | string | 'form' | 自定义标签 |

## Events
| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| validate | 表单验证事件 | (prop: string, isValid: boolean, message: string) |
| submit | 表单提交事件 | (e: Event) |
| reset | 表单重置事件 | - |

## Slots
| 插槽名 | 说明 |
|--------|------|
| default | 表单内容，包含多个 FormItem 组件 |

## 代码示例

### 行内表单
```vue
<template>
  <Form :model="formData" inline>
    <FormItem label="用户名" prop="username">
      <Input v-model="formData.username" placeholder="请输入用户名" />
    </FormItem>
    <FormItem label="密码" prop="password">
      <Input v-model="formData.password" type="password" placeholder="请输入密码" />
    </FormItem>
    <FormItem>
      <Button type="primary" native-type="submit">提交</Button>
    </FormItem>
  </Form>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const formData = ref({
  username: '',
  password: ''
});
</script>
```

### 自定义标签宽度
```vue
<template>
  <Form :model="formData" labelWidth="120px">
    <FormItem label="用户名" prop="username">
      <Input v-model="formData.username" placeholder="请输入用户名" />
    </FormItem>
    <FormItem label="密码" prop="password">
      <Input v-model="formData.password" type="password" placeholder="请输入密码" />
    </FormItem>
  </Form>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const formData = ref({
  username: '',
  password: ''
});
</script>
```

### 表单验证
```vue
<template>
  <Form :model="formData" :rules="rules" ref="formRef">
    <FormItem label="邮箱" prop="email">
      <Input v-model="formData.email" type="email" placeholder="请输入邮箱" />
    </FormItem>
    <FormItem label="手机号" prop="phone">
      <Input v-model="formData.phone" placeholder="请输入手机号" />
    </FormItem>
    <FormItem>
      <Button type="primary" @click="handleSubmit">提交</Button>
    </FormItem>
  </Form>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const formRef = ref<any>(null);
const formData = ref({
  email: '',
  phone: ''
});

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
};

const handleSubmit = () => {
  if (formRef.value) {
    formRef.value.validate((valid: boolean) => {
      if (valid) {
        console.log('表单验证通过，提交数据:', formData.value);
      } else {
        console.log('表单验证失败');
        return false;
      }
    });
  }
};
</script>
```

## 注意事项
1. Form 组件需要配合 FormItem 组件使用，FormItem 组件用于包裹具体的表单控件。
2. 表单验证规则需要通过 `rules` 属性传递给 Form 组件，每个 FormItem 组件需要指定 `prop` 属性来关联对应的验证规则。
3. 表单提交时，需要阻止默认的提交行为，并调用表单的 `validate` 方法进行验证。
4. 可以通过 `labelPosition` 和 `labelWidth` 属性来调整表单域标签的位置和宽度。
5. 使用 `inline` 属性可以将表单设置为行内表单，适合在搜索栏等场景中使用。
