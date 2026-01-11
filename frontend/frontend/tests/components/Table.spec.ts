import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Table from '../../src/components/Table.vue'
import Button from '../../src/components/Button.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

describe('Table组件测试', () => {
  let wrapper: any
  let tableData: any[]

  beforeEach(() => {
    // 重置测试数据
    tableData = [
      {
        date: '2023-05-10',
        name: '张三',
        address: '北京市朝阳区',
        age: 28
      },
      {
        date: '2023-05-11',
        name: '李四',
        address: '上海市浦东新区',
        age: 32
      },
      {
        date: '2023-05-12',
        name: '王五',
        address: '广州市天河区',
        age: 25
      },
      {
        date: '2023-05-13',
        name: '赵六',
        address: '深圳市南山区',
        age: 35
      }
    ]

    // 创建测试组件
    const testComponent = {
      components: {
        Table,
        Button
      },
      template: `
        <Table :data="tableData" @sort-change="handleSortChange" @row-click="handleRowClick" row-key="date">
          <!-- 表格工具栏 -->
          <template #toolbar>
            <div class="table-toolbar">
              <Button type="primary">新增</Button>
              <Button>导出</Button>
            </div>
          </template>
          
          <!-- 表格列配置 -->
          <el-table-column prop="date" label="日期" width="180" sortable />
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="age" label="年龄" width="100" sortable />
          <el-table-column prop="address" label="地址">
            <template #default="scope">
              <span class="address-text">{{ scope.row.address }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="scope">
              <Button type="primary" size="small" @click="handleEdit(scope.row)">编辑</Button>
              <Button type="danger" size="small" @click="handleDelete(scope.row)" style="margin-left: 10px;">删除</Button>
            </template>
          </el-table-column>
          
          <!-- 分页组件 -->
          <template #pagination>
            <div class="pagination">
              <Button size="small">上一页</Button>
              <span class="page-info">第 1 页 / 共 1 页</span>
              <Button size="small">下一页</Button>
            </div>
          </template>
        </Table>
      `,
      data() {
        return {
          tableData
        }
      },
      methods: {
        handleSortChange() {},
        handleRowClick() {},
        handleEdit() {},
        handleDelete() {}
      }
    }
    
    // 创建测试组件
    wrapper = mount(testComponent, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 创建间谍函数
    vi.spyOn(wrapper.vm, 'handleSortChange')
    vi.spyOn(wrapper.vm, 'handleRowClick')
    vi.spyOn(wrapper.vm, 'handleEdit')
    vi.spyOn(wrapper.vm, 'handleDelete')
  })

  it('应该正确渲染Table组件', () => {
    expect(wrapper.findComponent({ name: 'Table' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'ElTable' }).exists()).toBe(true)
  })

  it('应该正确渲染表头', () => {
    // 获取表头元素
    const headers = wrapper.findAll('.el-table__header th')
    
    // 验证表头数量和内容
    expect(headers.length).toBe(5) // 5列
    expect(headers[0].text()).toBe('日期')
    expect(headers[1].text()).toBe('姓名')
    expect(headers[2].text()).toBe('年龄')
    expect(headers[3].text()).toBe('地址')
    expect(headers[4].text()).toBe('操作')
  })

  it('应该正确渲染行数据', () => {
    // 获取表格行元素
    const rows = wrapper.findAll('.el-table__body tr')
    
    // 验证行数量
    expect(rows.length).toBe(4) // 4行数据
    
    // 验证第一行数据
    const firstRowCells = rows[0].findAll('td')
    expect(firstRowCells.length).toBe(5) // 5列
    expect(firstRowCells[0].text()).toBe('2023-05-10')
    expect(firstRowCells[1].text()).toBe('张三')
    expect(firstRowCells[2].text()).toBe('28')
  })

  it('应该正确渲染自定义单元格内容', () => {
    // 获取地址列的自定义内容
    const addressCells = wrapper.findAll('.address-text')
    
    // 验证自定义内容
    expect(addressCells.length).toBe(4)
    expect(addressCells[0].text()).toBe('北京市朝阳区')
    expect(addressCells[1].text()).toBe('上海市浦东新区')
    expect(addressCells[2].text()).toBe('广州市天河区')
    expect(addressCells[3].text()).toBe('深圳市南山区')
  })

  it('应该正确渲染工具栏插槽', () => {
    // 获取工具栏元素
    const toolbar = wrapper.find('.table-toolbar')
    const toolbarButtons = toolbar.findAllComponents({ name: 'Button' })
    
    // 验证工具栏渲染
    expect(toolbar.exists()).toBe(true)
    expect(toolbarButtons.length).toBe(2)
  })

  it('应该正确渲染分页插槽', () => {
    // 获取分页元素
    const pagination = wrapper.find('.pagination')
    const paginationButtons = pagination.findAllComponents({ name: 'Button' })
    const pageInfo = pagination.find('.page-info')
    
    // 验证分页渲染
    expect(pagination.exists()).toBe(true)
    expect(paginationButtons.length).toBe(2)
    expect(pageInfo.exists()).toBe(true)
    expect(pageInfo.text()).toBe('第 1 页 / 共 1 页')
  })

  it('应该正确处理排序事件', () => {
    // 测试Table组件是否能正确传递sort-change事件
    // 创建一个标志来记录事件是否被触发
    let eventTriggered = false
    
    const tableWrapper = mount({
      components: {
        Table
      },
      template: `
        <Table :data="tableData" @sort-change="handleSortChange" row-key="date">
          <el-table-column prop="date" label="日期" width="180" sortable />
          <el-table-column prop="name" label="姓名" width="180" />
        </Table>
      `,
      data() {
        return {
          tableData
        }
      },
      methods: {
        handleSortChange() {
          eventTriggered = true
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 获取内部的ElTable组件
    const elTable = tableWrapper.findComponent({ name: 'ElTable' })
    
    // 触发ElTable的sort-change事件
    elTable.vm.$emit('sort-change', { column: {}, prop: 'date', order: 'ascending' })
    
    // 验证事件是否被正确传递到父组件
    expect(eventTriggered).toBe(true)
  })

  it('应该正确处理行点击事件', () => {
    // 测试Table组件是否能正确传递row-click事件
    // 创建一个标志来记录事件是否被触发
    let eventTriggered = false
    
    const tableWrapper = mount({
      components: {
        Table
      },
      template: `
        <Table :data="tableData" @row-click="handleRowClick" row-key="date">
          <el-table-column prop="date" label="日期" width="180" />
          <el-table-column prop="name" label="姓名" width="180" />
        </Table>
      `,
      data() {
        return {
          tableData
        }
      },
      methods: {
        handleRowClick() {
          eventTriggered = true
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 获取内部的ElTable组件
    const elTable = tableWrapper.findComponent({ name: 'ElTable' })
    
    // 触发ElTable的row-click事件
    elTable.vm.$emit('row-click', tableData[0], {}, new Event('click'))
    
    // 验证事件是否被正确传递到父组件
    expect(eventTriggered).toBe(true)
  })

  it('应该正确处理操作按钮事件', async () => {
    // 触发编辑按钮事件
    const editButtons = wrapper.findAll('.el-button--primary')
    await editButtons[1].trigger('click') // 第一个是新增按钮，第二个是编辑按钮
    
    // 验证编辑事件是否被调用
    expect(wrapper.vm.handleEdit).toHaveBeenCalled()
    
    // 触发删除按钮事件
    const deleteButtons = wrapper.findAll('.el-button--danger')
    await deleteButtons[0].trigger('click')
    
    // 验证删除事件是否被调用
    expect(wrapper.vm.handleDelete).toHaveBeenCalled()
  })

  it('应该正确处理空数据状态', async () => {
    // 创建空数据测试组件
    const emptyWrapper = mount({
      components: {
        Table
      },
      template: `
        <Table :data="emptyData" row-key="date">
          <el-table-column prop="date" label="日期" width="180" />
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="address" label="地址" />
        </Table>
      `,
      data() {
        return {
          emptyData: []
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 验证空状态渲染
    const emptyText = emptyWrapper.find('.el-table__empty-text')
    expect(emptyText.exists()).toBe(true)
    expect(emptyText.text()).toBe('暂无数据')
  })

  it('应该正确渲染斑马纹表格', async () => {
    // 创建斑马纹测试组件
    const stripeWrapper = mount({
      components: {
        Table
      },
      template: `
        <Table :data="tableData" stripe row-key="date">
          <el-table-column prop="date" label="日期" width="180" />
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="address" label="地址" />
        </Table>
      `,
      data() {
        return {
          tableData
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 获取内部el-table组件
    const elTable = stripeWrapper.findComponent({ name: 'ElTable' })
    
    // 验证斑马纹属性是否正确传递
    expect(elTable.props('stripe')).toBe(true)
  })

  it('应该正确渲染带边框的表格', async () => {
    // 创建带边框测试组件
    const borderWrapper = mount({
      components: {
        Table
      },
      template: `
        <Table :data="tableData" border row-key="date">
          <el-table-column prop="date" label="日期" width="180" />
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="address" label="地址" />
        </Table>
      `,
      data() {
        return {
          tableData
        }
      }
    }, {
      global: {
        plugins: [ElementPlus]
      }
    })
    
    // 获取内部el-table组件
    const elTable = borderWrapper.findComponent({ name: 'ElTable' })
    
    // 验证边框属性是否正确传递
    expect(elTable.props('border')).toBe(true)
  })

  it('应该正确处理固定列', () => {
    // 验证操作列的fixed属性是否正确设置
    const tableColumns = wrapper.findAllComponents({ name: 'ElTableColumn' })
    // 找到操作列（最后一列）
    const actionColumn = tableColumns[tableColumns.length - 1]
    // 验证fixed属性是否为'right'
    expect(actionColumn.props('fixed')).toBe('right')
  })

  it('应该正确传递props到内部组件', () => {
    // 获取内部el-table组件
    const elTable = wrapper.findComponent({ name: 'ElTable' })
    
    // 验证props是否正确传递
    expect(elTable.props('data')).toStrictEqual(tableData)
    expect(elTable.props('fit')).toBe(true)
    expect(elTable.props('showHeader')).toBe(true)
  })
})
