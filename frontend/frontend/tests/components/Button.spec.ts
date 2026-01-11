import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Button from '../../src/components/Button.vue'

describe('Button组件测试', () => {
  let wrapper: any
  
  beforeEach(() => {
    // 重置包装器
    wrapper = mount(Button, {
      slots: {
        default: '测试按钮'
      }
    })
  })
  
  it('应该正确渲染按钮文本', () => {
    expect(wrapper.text()).toBe('测试按钮')
  })
  
  it('应该正确传递props到内部组件', () => {
    wrapper = mount(Button, {
      props: {
        type: 'primary',
        size: 'large',
        plain: true,
        round: true,
        circle: false,
        loading: false,
        disabled: false
      },
      slots: {
        default: '测试按钮'
      }
    })
    
    // 检查内部el-button元素的props
    const elButton = wrapper.findComponent({ name: 'ElButton' })
    expect(elButton.exists()).toBe(true)
    expect(elButton.props('type')).toBe('primary')
    expect(elButton.props('size')).toBe('large')
    expect(elButton.props('plain')).toBe(true)
    expect(elButton.props('round')).toBe(true)
    expect(elButton.props('circle')).toBe(false)
    expect(elButton.props('loading')).toBe(false)
    expect(elButton.props('disabled')).toBe(false)
  })
  
  it('应该正确传递圆形属性', () => {
    wrapper = mount(Button, {
      props: {
        circle: true
      },
      slots: {
        default: ''
      }
    })
    
    const elButton = wrapper.findComponent({ name: 'ElButton' })
    expect(elButton.exists()).toBe(true)
    expect(elButton.props('circle')).toBe(true)
  })
  
  it('应该正确处理加载状态', () => {
    wrapper = mount(Button, {
      props: {
        loading: true
      },
      slots: {
        default: '加载按钮'
      }
    })
    
    const elButton = wrapper.findComponent({ name: 'ElButton' })
    expect(elButton.exists()).toBe(true)
    expect(elButton.props('loading')).toBe(true)
  })
  
  it('应该在禁用状态下不响应点击事件', async () => {
    const onClick = vi.fn()
    wrapper = mount(Button, {
      props: {
        disabled: true
      },
      slots: {
        default: '禁用按钮'
      },
      attrs: {
        onClick
      }
    })
    
    await wrapper.trigger('click')
    expect(onClick).not.toHaveBeenCalled()
  })
  
  it('应该正确触发点击事件', async () => {
    const onClick = vi.fn()
    wrapper = mount(Button, {
      slots: {
        default: '可点击按钮'
      },
      attrs: {
        onClick
      }
    })
    
    await wrapper.trigger('click')
    expect(onClick).toHaveBeenCalledTimes(1)
  })
})
