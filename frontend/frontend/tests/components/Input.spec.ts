import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Input from '../../src/components/Input.vue'

describe('Input组件测试', () => {
  let wrapper: any
  
  beforeEach(() => {
    // 重置包装器
    wrapper = mount(Input, {
      props: {
        modelValue: ''
      }
    })
  })
  
  it('应该正确渲染并接受初始值', () => {
    wrapper = mount(Input, {
      props: {
        modelValue: '初始值'
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    expect(elInput.exists()).toBe(true)
    expect(elInput.props('modelValue')).toBe('初始值')
  })
  
  it('应该正确传递props到内部组件', () => {
    wrapper = mount(Input, {
      props: {
        modelValue: '测试值',
        type: 'text',
        placeholder: '请输入',
        disabled: false,
        size: 'default',
        maxlength: 10,
        minlength: 2,
        showWordLimit: true,
        readonly: false,
        clearable: true,
        showPassword: false,
        prefixIcon: 'el-icon-search',
        suffixIcon: 'el-icon-date'
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    expect(elInput.exists()).toBe(true)
    expect(elInput.props('modelValue')).toBe('测试值')
    expect(elInput.props('type')).toBe('text')
    expect(elInput.props('placeholder')).toBe('请输入')
    expect(elInput.props('disabled')).toBe(false)
    expect(elInput.props('size')).toBe('default')
    expect(elInput.props('maxlength')).toBe(10)
    expect(elInput.props('minlength')).toBe(2)
    expect(elInput.props('showWordLimit')).toBe(true)
    expect(elInput.props('readonly')).toBe(false)
    expect(elInput.props('clearable')).toBe(true)
    expect(elInput.props('showPassword')).toBe(false)
    expect(elInput.props('prefixIcon')).toBe('el-icon-search')
    expect(elInput.props('suffixIcon')).toBe('el-icon-date')
  })
  
  it('应该正确处理输入事件', async () => {
    const handleUpdate = vi.fn()
    wrapper = mount(Input, {
      props: {
        modelValue: '',
        'onUpdate:modelValue': handleUpdate
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    await elInput.vm.$emit('input', '新值')
    
    expect(handleUpdate).toHaveBeenCalledWith('新值')
  })
  
  it('应该正确处理清空事件', async () => {
    const handleClear = vi.fn()
    
    wrapper = mount(Input, {
      props: {
        modelValue: '测试值',
        clearable: true,
        onClear: handleClear
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    await elInput.vm.$emit('clear')
    
    expect(handleClear).toHaveBeenCalled()
  })
  
  it('应该正确处理焦点和失焦事件', async () => {
    const handleFocus = vi.fn()
    const handleBlur = vi.fn()
    
    wrapper = mount(Input, {
      props: {
        modelValue: '',
        onFocus: handleFocus,
        onBlur: handleBlur
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    
    // 模拟焦点事件
    await elInput.vm.$emit('focus', new FocusEvent('focus'))
    expect(handleFocus).toHaveBeenCalled()
    
    // 模拟失焦事件
    await elInput.vm.$emit('blur', new FocusEvent('blur'))
    expect(handleBlur).toHaveBeenCalled()
  })
  
  it('应该正确处理密码显示切换', () => {
    wrapper = mount(Input, {
      props: {
        modelValue: 'password123',
        type: 'password',
        showPassword: true
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    expect(elInput.exists()).toBe(true)
    expect(elInput.props('type')).toBe('password')
    expect(elInput.props('showPassword')).toBe(true)
  })
  
  it('应该正确处理多行文本', () => {
    wrapper = mount(Input, {
      props: {
        modelValue: '多行\n文本',
        type: 'textarea',
        rows: 4,
        resize: 'vertical',
        autosize: { minRows: 2, maxRows: 6 }
      }
    })
    
    const elInput = wrapper.findComponent({ name: 'ElInput' })
    expect(elInput.exists()).toBe(true)
    expect(elInput.props('type')).toBe('textarea')
    expect(elInput.props('rows')).toBe(4)
    expect(elInput.props('resize')).toBe('vertical')
    expect(elInput.props('autosize')).toEqual({ minRows: 2, maxRows: 6 })
  })
})
