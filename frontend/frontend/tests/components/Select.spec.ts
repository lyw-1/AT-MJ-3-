import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Select from '../../src/components/Select.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

describe('Select组件测试', () => {
  let wrapper: any
  let options: any[]

  beforeEach(() => {
    // 重置测试数据
    options = [
      { label: '选项1', value: '1' },
      { label: '选项2', value: '2' },
      { label: '选项3', value: '3' },
      { label: '选项4', value: '4' }
    ]
  })

  it('应该正确渲染Select组件', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证组件渲染
    expect(wrapper.findComponent({ name: 'Select' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'ElSelect' }).exists()).toBe(true)
  })

  it('应该正确渲染单选模式下的选项', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证选项渲染
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.exists()).toBe(true)
  })

  it('应该正确处理单选模式下的选择交互及值绑定', async () => {
    // 创建一个标志来记录事件是否被触发
    let changeEventTriggered = false
    let changeEventValue: any = null
    
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" @change="handleChange" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      },
      methods: {
        handleChange(value: any) {
          changeEventTriggered = true
          changeEventValue = value
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 获取内部的ElSelect组件
    const elSelect = wrapper.findComponent({ name: 'ElSelect' })
    
    // 触发选项选择
    await elSelect.vm.$emit('change', '1')
    
    // 验证值是否正确更新
    expect(wrapper.vm.value).toBe('1')
    // 验证change事件是否被触发
    expect(changeEventTriggered).toBe(true)
    expect(changeEventValue).toBe('1')
  })

  it('应该正确渲染多选模式下的选项', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" multiple placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: [],
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证多选模式渲染
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('multiple')).toBe(true)
  })

  it('应该正确处理多选模式下的选择和取消', async () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" multiple placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: [],
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 获取内部的ElSelect组件
    const elSelect = wrapper.findComponent({ name: 'ElSelect' })
    
    // 选择第一个选项
    await elSelect.vm.$emit('change', ['1'])
    expect(wrapper.vm.value).toEqual(['1'])
    
    // 选择第二个选项
    await elSelect.vm.$emit('change', ['1', '2'])
    expect(wrapper.vm.value).toEqual(['1', '2'])
    
    // 取消第一个选项
    await elSelect.vm.$emit('change', ['2'])
    expect(wrapper.vm.value).toEqual(['2'])
  })

  it('应该正确处理清空功能', async () => {
    // 创建一个标志来记录事件是否被触发
    let clearEventTriggered = false
    
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" clearable @clear="handleClear" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '1',
          options
        }
      },
      methods: {
        handleClear() {
          clearEventTriggered = true
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 获取内部的ElSelect组件
    const elSelect = wrapper.findComponent({ name: 'ElSelect' })
    
    // 触发清空事件
    await elSelect.vm.$emit('clear')
    
    // 验证clear事件是否被触发
    expect(clearEventTriggered).toBe(true)
  })

  it('应该正确处理禁用状态', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" disabled placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证禁用状态
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('disabled')).toBe(true)
  })

  it('应该正确处理搜索功能', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" filterable @change="handleChange" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      },
      methods: {
        handleChange: vi.fn()
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证搜索功能是否启用
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('filterable')).toBe(true)
  })

  it('应该正确处理远程搜索功能', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" remote :remote-method="remoteMethod" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options
        }
      },
      methods: {
        remoteMethod: vi.fn()
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证远程搜索功能是否启用
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('remote')).toBe(true)
  })

  it('应该正确处理自定义占位符', () => {
    // 创建测试组件
    const placeholder = '请选择一个选项'
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" :placeholder="placeholder">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: '',
          options,
          placeholder
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证占位符是否正确设置
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('placeholder')).toBe(placeholder)
  })

  it('应该正确处理不同尺寸', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <div>
          <Select v-model="value1" size="large" placeholder="大型" />
          <Select v-model="value2" size="default" placeholder="默认" />
          <Select v-model="value3" size="small" placeholder="小型" />
        </div>
      `,
      data() {
        return {
          value1: '',
          value2: '',
          value3: ''
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证不同尺寸是否正确设置
    const selects = wrapper.findAllComponents({ name: 'ElSelect' })
    expect(selects[0].props('size')).toBe('large')
    expect(selects[1].props('size')).toBe('default')
    expect(selects[2].props('size')).toBe('small')
  })

  it('应该正确处理collapse-tags属性', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" multiple collapse-tags placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: [],
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证collapse-tags属性是否正确设置
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('collapseTags')).toBe(true)
  })

  it('应该正确处理multiple-limit属性', () => {
    // 创建测试组件
    wrapper = mount({
      components: {
        Select
      },
      template: `
        <Select v-model="value" multiple :multiple-limit="2" placeholder="请选择">
          <el-option
            v-for="option in options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </Select>
      `,
      data() {
        return {
          value: [],
          options
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })

    // 验证multiple-limit属性是否正确设置
    const select = wrapper.findComponent({ name: 'ElSelect' })
    expect(select.props('multipleLimit')).toBe(2)
  })
})
