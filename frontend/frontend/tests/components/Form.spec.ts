import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Form from '../../src/components/Form.vue'
import FormItem from '../../src/components/FormItem.vue'
import Input from '../../src/components/Input.vue'
import Button from '../../src/components/Button.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

describe('Form组件测试', () => {
  let wrapper: any
  
  beforeEach(() => {
    // 重置测试环境
    vi.clearAllMocks()
  })

  it('应该正确渲染Form组件', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input,
        Button
      },
      template: `
        <Form :model="formData" :rules="rules" ref="formRef">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
          <FormItem label="密码" prop="password">
            <Input v-model="formData.password" type="password" placeholder="请输入密码" />
          </FormItem>
          <FormItem>
            <Button type="primary" native-type="submit">提交</Button>
            <Button @click="handleReset">重置</Button>
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: '',
            password: ''
          },
          rules: {
            username: [
              { required: true, message: '请输入用户名', trigger: 'blur' }
            ],
            password: [
              { required: true, message: '请输入密码', trigger: 'blur' }
            ]
          }
        }
      },
      methods: {
        handleReset() {
          // 实际组件没有暴露resetFields方法，这里使用简单的重置逻辑
          this.formData.username = ''
          this.formData.password = ''
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证组件渲染
    expect(wrapper.findComponent({ name: 'Form' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'ElForm' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'FormItem' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'Input' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'Button' }).exists()).toBe(true)
  })

  it('应该正确处理表单提交事件', async () => {
    // 创建测试组件
    let submitTriggered = false
    
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input,
        Button
      },
      template: `
        <Form :model="formData" :rules="rules" @submit.prevent="handleSubmit">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
          <FormItem>
            <Button type="primary" native-type="submit">提交</Button>
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: 'test'
          },
          rules: {
            username: [
              { required: true, message: '请输入用户名', trigger: 'blur' }
            ]
          }
        }
      },
      methods: {
        handleSubmit(e: Event) {
          submitTriggered = true
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 触发表单提交
    const form = wrapper.findComponent({ name: 'ElForm' })
    await form.trigger('submit')
    
    // 验证提交事件是否被触发
    expect(submitTriggered).toBe(true)
  })

  it('应该正确处理不同的labelPosition配置', () => {
    // 测试左对齐
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" label-position="left">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('labelPosition')).toBe('left')
    
    // 测试右对齐
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" label-position="right">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const formRight = wrapper.findComponent({ name: 'ElForm' })
    expect(formRight.props('labelPosition')).toBe('right')
    
    // 测试顶部对齐
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" label-position="top">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const formTop = wrapper.findComponent({ name: 'ElForm' })
    expect(formTop.props('labelPosition')).toBe('top')
  })

  it('应该正确处理disabled配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" disabled>
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('disabled')).toBe(true)
  })

  it('应该正确处理size配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" size="small">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('size')).toBe('small')
  })

  it('应该正确处理statusIcon配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" status-icon>
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('statusIcon')).toBe(true)
  })

  it('应该正确处理inline配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" inline>
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
          <FormItem label="密码" prop="password">
            <Input v-model="formData.password" type="password" placeholder="请输入密码" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: '',
            password: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('inline')).toBe(true)
  })

  it('应该正确处理validateOnRuleChange配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" :validate-on-rule-change="false">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('validateOnRuleChange')).toBe(false)
  })

  it('应该正确处理hideRequiredAsterisk配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" hide-required-asterisk>
          <FormItem label="用户名" prop="username" required>
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('hideRequiredAsterisk')).toBe(true)
  })

  it('应该正确处理labelWidth配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" label-width="120px">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('labelWidth')).toBe('120px')
  })

  it('应该正确处理labelSuffix配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" label-suffix=":">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('labelSuffix')).toBe(':')
  })

  it('应该正确处理inlineMessage配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" inline-message>
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('inlineMessage')).toBe(true)
  })

  it('应该正确处理showMessage配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData" :show-message="false">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    expect(form.props('showMessage')).toBe(false)
  })

  it('应该正确处理不同的tag配置', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Form,
        FormItem,
        Input
      },
      template: `
        <Form :model="formData">
          <FormItem label="用户名" prop="username">
            <Input v-model="formData.username" placeholder="请输入用户名" />
          </FormItem>
        </Form>
      `,
      data() {
        return {
          formData: {
            username: ''
          }
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    const form = wrapper.findComponent({ name: 'ElForm' })
    // 验证ElForm组件的tag属性是否默认是'form'
    expect(form.element.tagName).toBe('FORM')
  })
})
