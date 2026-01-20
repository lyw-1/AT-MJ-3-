<template>
  <div class="designer-wrap" v-loading="loading">
    <div class="designer-header">
      <div class="meta">
        <el-tag v-if="moldInfo">模号：{{ moldInfo.moldNumber }}</el-tag>
        <el-tag v-else type="warning">加载中...</el-tag>
        <el-tag v-if="moldInfo" type="info">成品规格：{{ moldInfo.specification }}</el-tag>
        <el-tag v-if="moldInfo" type="success">负责人：{{ moldInfo.responsiblePerson }}</el-tag>
      </div>
      <div class="header-buttons">
        <el-button @click="handleBack">
          返回列表
        </el-button>
        <el-button type="primary">创建模具加工任务</el-button>
      </div>
    </div>
    <el-row :gutter="12">
      <el-col :span="6">
        <el-card>
          <div class="list-title">选择工序模板</div>
          <div class="template-list">
            <div
              v-for="template in availableTemplates"
              :key="template.id"
              class="template-item"
              @click="addTemplate(template)"
            >
              <div class="template-info">
                <div class="template-name">{{ template.name }}</div>
                <div class="template-desc">{{ template.description || '无描述' }}</div>
                <div class="template-meta">
                  <el-tag :type="template.status === 'active' ? 'success' : 'danger'" size="small">
                    {{ template.status === 'active' ? '启用' : '禁用' }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <div class="selected-title">已选工序</div>
          <div class="selected-list">
            <div v-for="(s,idx) in selected" :key="s.uid" class="selected-item">
              <div class="selected-name">{{ idx+1 }}. {{ s.name }}</div>
              <div class="template-badge">
                <el-tag type="info" size="small">模板: {{ s.templateName }}</el-tag>
              </div>
              <el-select v-model="s.equipment" placeholder="设备" style="width:180px">
                <el-option v-for="e in equipmentOptions[s.key] || []" :key="e" :label="e" :value="e" />
              </el-select>
              <el-input v-model="s.operator" placeholder="操作人" style="width:140px" />
              <el-button size="small" @click="moveUp(idx)">上移</el-button>
              <el-button size="small" @click="moveDown(idx)">下移</el-button>
              <el-button size="small" type="danger" @click="remove(idx)">移除</el-button>
            </div>
          </div>
          
          <!-- 下一步按钮 -->
          <div style="margin-top: 20px; display: flex; justify-content: center;">
            <el-button 
              type="primary" 
              size="large"
              @click="handleNextStep"
              :disabled="selected.length === 0"
            >
              下一步
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模板选择对话框 -->
    <el-dialog
      v-model="showTemplateDialog"
      title="选择工序模板"
      width="600px"
    >
      <template-selector
        :process-code="currentProcess.category"
        @select="handleTemplateSelect"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getMoldInitialParamDetail } from '@/api/mold'
import { getProcessTemplates } from '@/api/process'
import TemplateSelector from '@/components/process/TemplateSelector.vue'
import type { MoldInitialParam } from '@/api/mold'

const route = useRoute()
const router = useRouter()

// 从路由参数中获取模具ID
const moldId = Number(route.params.moldId || 0)

// 模具信息
const moldInfo = ref<MoldInitialParam | null>(null)
const loading = ref(false)

// 返回列表按钮点击事件
const handleBack = () => {
  router.push('/mold/initial-params-list')
}

// 工序key到code的映射关系，与工序模板管理中的模板category严格一致
const processKeyToCodeMap: Record<string, string> = {
  '加工工序汇总': 'SUMMARY',
  '备料': 'PREP',
  '进泥孔粗加工': 'DRILL_NON_LAYER',
  '进泥孔精加工': 'DRILL_LAYER',
  '热处理': 'HEAT_TREATMENT',
  '导料槽加工-中丝': 'GUIDE_SLOT',
  '导料槽加工-切槽机': 'GUIDE_SLOT',
  '导料槽加工-放电': 'GUIDE_SLOT',
  '外形加工': 'SHAPE',
  '外形精加工': 'SHAPE',
  '精磨': 'GRINDING',
  '斜边放电': 'EDM',
  '模芯台阶加工': 'CORE_STEP',
  '裸模自检': 'SELF_CHECK',
  '品质检测': 'QUALITY_CHECK',
  '入库': 'IN_STORAGE'
}

// 下一步按钮点击事件
const handleNextStep = () => {
  if (selected.value.length === 0) {
    ElMessage.warning('请先选择工序')
    return
  }

  // 检查是否所有工序都选择了模板
  const processesWithoutTemplate = selected.value.filter(item => !item.templateId && !item.required)
  if (processesWithoutTemplate.length > 0) {
    const processNames = processesWithoutTemplate.map(p => p.name).join('、')
    ElMessage.warning(`请为以下工序选择模板：${processNames}`)
    return
  }

  // 转换选中的工序列表为ProcessPresetPage所需的格式
  const selectedProcesses = selected.value.map(item => ({
    code: processKeyToCodeMap[item.key],
    name: item.name,
    templateId: item.templateId,
    category: item.category,
    equipment: item.equipment,
    operator: item.operator
  }))

  // 导航到详细工序设置页面，使用路由名称和参数对象
  router.push({
      name: 'ProcessPreset',
      params: {
        id: moldId
      },
      query: {
        processes: JSON.stringify(selectedProcesses),
        moldNumber: moldInfo.value?.moldNumber,
        specification: moldInfo.value?.specification,
        owner: moldInfo.value?.responsiblePerson
      }
    })
}

// 加载模具详情
const loadMoldDetail = async () => {
  if (!moldId) {
    ElMessage.error('模具ID无效')
    return
  }
  
  loading.value = true
  try {
    const response = await getMoldInitialParamDetail(moldId)
    console.log('[ProcessRouteDesigner] API返回数据:', response)
    
    // 进行正确的字段映射，确保模板中引用的字段能获取到正确的数据
    // 这是解决模号、成品规格、负责人显示问题的关键
    if (response) {
      // 创建一个包含正确字段映射的对象
      const mappedMoldInfo = {
        ...response,
        // 映射模板中需要的字段
        moldNumber: response.moldCode || response.moldNumber,
        specification: response.productSpec || response.specification,
        responsiblePerson: response.ownerName || response.responsiblePerson || response.owner
      } as MoldInitialParam
      
      moldInfo.value = mappedMoldInfo
      console.log('[ProcessRouteDesigner] 映射后的数据:', moldInfo.value)
    }
  } catch (error: any) {
    console.error('加载模具详情失败:', error)
    console.error('错误信息:', error.message)
    console.error('错误堆栈:', error.stack)
    ElMessage.error('加载模具详情失败')
  } finally {
    loading.value = false
  }
}

// 页面挂载时加载模具详情和工序模板
onMounted(() => {
  loadMoldDetail()
  loadProcessTemplates()
})
// 所有可选工序，与工序模板管理中的工序模板列表一一对应
const allSteps = ref([
  { key: '加工工序汇总', name: '加工工序汇总', required: true, category: 'SUMMARY' }
])

// 所有工序模板
const allTemplates = ref<any[]>([])

// 模板选择相关状态
const showTemplateDialog = ref(false)
const currentProcess = ref<any>(null)

// 设备选项，根据工序模板动态生成
const equipmentOptions: Record<string, string[]> = {
  '加工工序汇总': ['无需设备'],
  '备料': ['磨床'],
  '进泥孔粗加工': ['深孔钻1号','深孔钻2号','深孔钻3号','深孔钻4号','深孔钻5号','深孔钻6号','精雕机','电火花成型机1号'],
  '进泥孔精加工': ['深孔钻1号','深孔钻2号','深孔钻3号','深孔钻4号','深孔钻5号','深孔钻6号','精雕机'],
  '热处理': ['热处理设备1号','热处理设备2号'],
  '精磨': ['磨床1号','磨床2号','磨床3号'],
  '导料槽加工-中丝': ['中走丝1号','中走丝2号','中走丝3号'],
  '导料槽加工-切槽机': ['切槽机1号','切槽机2号','切槽机3号','切槽机4号','切槽机5号','切槽机6号','切槽机7号','切槽机8号','切槽机9号'],
  '导料槽加工-放电': ['电火花成型机1号','电火花成型机2号'],
  '外形加工': ['铣床1号','铣床2号','精雕机'],
  '外形精加工': ['线切割机1号','线切割机2号','精雕机'],
  '斜边放电': ['电火花成型机1号','电火花成型机2号'],
  '模芯台阶加工': ['铣床1号','铣床2号','精雕机'],
  '裸模自检': ['无需设备'],
  '品质检测': ['检测设备1号','检测设备2号'],
  '入库': ['无需设备']
}

// 从工序模板管理获取工序列表
const loadProcessTemplates = async () => {
  console.log('[ProcessRouteDesigner] 开始加载工序模板')
  try {
    // 添加默认分页参数，确保获取到足够多的模板
    const response = await getProcessTemplates({ page: 1, pageSize: 100 })
    console.log('[ProcessRouteDesigner] API响应:', response)
    
    let templates = []
    
    // 处理后端返回的不同数据格式
    if (response && response.data) {
      // 检查是否是分页格式
      if (Array.isArray(response.data)) {
        // 直接返回数组格式
        templates = response.data
      } else if (response.data.records && Array.isArray(response.data.records)) {
        // 分页格式
        templates = response.data.records
      } else if (response.data.data && Array.isArray(response.data.data)) {
        // 嵌套数据格式
        templates = response.data.data
      }
    }
    
    console.log('[ProcessRouteDesigner] 从API获取到的模板数量:', templates.length)
    console.log('[ProcessRouteDesigner] 模板详情:', templates)
    
    // 如果获取到的模板数量不足或只有标准模板，使用默认工序列表
    if (templates.length <= 1 || templates.every(t => t.name === '标准模板')) {
      console.log('[ProcessRouteDesigner] API返回模板不足，使用默认工序列表')
      // 使用默认数据作为备份
      const defaultProcesses = [
        { key: '备料', name: '备料', category: 'PREP', required: false },
        { key: '进泥孔粗加工', name: '进泥孔粗加工', category: 'DRILL_NON_LAYER', required: false },
        { key: '进泥孔精加工', name: '进泥孔精加工', category: 'DRILL_LAYER', required: false },
        { key: '热处理', name: '热处理', category: 'HEAT_TREATMENT', required: false },
        { key: '精磨', name: '精磨', category: 'GRINDING', required: false },
        { key: '导料槽加工-中丝', name: '导料槽加工-中丝', category: 'GUIDE_SLOT', required: false },
        { key: '导料槽加工-切槽机', name: '导料槽加工-切槽机', category: 'GUIDE_SLOT', required: false },
        { key: '导料槽加工-放电', name: '导料槽加工-放电', category: 'GUIDE_SLOT', required: false },
        { key: '外形加工', name: '外形加工', category: 'SHAPE', required: false },
        { key: '外形精加工', name: '外形精加工', category: 'SHAPE', required: false },
        { key: '斜边放电', name: '斜边放电', category: 'EDM', required: false },
        { key: '模芯台阶加工', name: '模芯台阶加工', category: 'CORE_STEP', required: false },
        { key: '裸模自检', name: '裸模自检', category: 'SELF_CHECK', required: false },
        { key: '品质检测', name: '品质检测', category: 'QUALITY_CHECK', required: false },
        { key: '入库', name: '入库', category: 'IN_STORAGE', required: false }
      ]
      allSteps.value = [
        { key: '加工工序汇总', name: '加工工序汇总', required: true, category: 'SUMMARY' },
        ...defaultProcesses
      ]
    } else {
      // 使用API返回的模板生成工序列表
      allTemplates.value = templates
      // 生成工序列表，排除加工工序汇总（已默认添加）
      const processSteps = templates
        .filter(template => template.category !== 'SUMMARY')
        .map(template => ({
          key: template.name,
          name: template.name,
          category: template.category,
          required: false
        }))
      // 重置allSteps，保留默认的加工工序汇总
      allSteps.value = [
        { key: '加工工序汇总', name: '加工工序汇总', required: true, category: 'SUMMARY' },
        ...processSteps
      ]
    }
    
    console.log('[ProcessRouteDesigner] 最终的allSteps数量:', allSteps.value.length)
    console.log('[ProcessRouteDesigner] 最终的allSteps:', allSteps.value)
  } catch (error) {
    console.error('加载工序模板失败:', error)
    ElMessage.warning('加载工序模板失败，使用默认数据')
    // 使用默认数据作为备份
    const defaultProcesses = [
      { key: '备料', name: '备料', category: 'PREP', required: false },
      { key: '进泥孔粗加工', name: '进泥孔粗加工', category: 'DRILL_NON_LAYER', required: false },
      { key: '进泥孔精加工', name: '进泥孔精加工', category: 'DRILL_LAYER', required: false },
      { key: '热处理', name: '热处理', category: 'HEAT_TREATMENT', required: false },
      { key: '精磨', name: '精磨', category: 'GRINDING', required: false },
      { key: '导料槽加工-中丝', name: '导料槽加工-中丝', category: 'GUIDE_SLOT', required: false },
      { key: '导料槽加工-切槽机', name: '导料槽加工-切槽机', category: 'GUIDE_SLOT', required: false },
      { key: '导料槽加工-放电', name: '导料槽加工-放电', category: 'GUIDE_SLOT', required: false },
      { key: '外形加工', name: '外形加工', category: 'SHAPE', required: false },
      { key: '外形精加工', name: '外形精加工', category: 'SHAPE', required: false },
      { key: '斜边放电', name: '斜边放电', category: 'EDM', required: false },
      { key: '模芯台阶加工', name: '模芯台阶加工', category: 'CORE_STEP', required: false },
      { key: '裸模自检', name: '裸模自检', category: 'SELF_CHECK', required: false },
      { key: '品质检测', name: '品质检测', category: 'QUALITY_CHECK', required: false },
      { key: '入库', name: '入库', category: 'IN_STORAGE', required: false }
    ]
    allSteps.value = [
      { key: '加工工序汇总', name: '加工工序汇总', required: true, category: 'SUMMARY' },
      ...defaultProcesses
    ]
    console.log('[ProcessRouteDesigner] 使用默认数据，allSteps数量:', allSteps.value.length)
    console.log('[ProcessRouteDesigner] 使用默认数据，allSteps:', allSteps.value)
  }

  // 自动添加加工工序汇总模板
    const summaryTemplate = allTemplates.value.find(t => t.category === 'SUMMARY')
    if (summaryTemplate) {
      addTemplate(summaryTemplate)
    }

    console.log('[ProcessRouteDesigner] 加载工序模板完成，selected:', selected.value)
    console.log('[ProcessRouteDesigner] 加载工序模板完成，availableTemplates:', availableTemplates.value)
}

// 初始化已选工序
const selected = ref<Array<any>>([])

// 计算属性：已选工序的key集合
const selectedKeys = computed(() => {
  return new Set(selected.value.map(item => item.key))
})

// 计算属性：可用模板列表（过滤掉已使用的模板）
const availableTemplates = computed(() => {
  const usedTemplateIds = new Set(selected.value.map(s => s.templateId).filter(id => id))
  return allTemplates.value.filter(template =>
    !usedTemplateIds.has(template.id) && template.status === 'active'
  )
})

// 添加模板（创建对应的工序）
const addTemplate = (template: any) => {
  // 检查是否已添加该模板
  const existing = selected.value.find(s => s.templateId === template.id)
  if (existing) {
    ElMessage.warning('该模板已添加，不能重复选择')
    return
  }

  // 根据模板类型创建对应的工序
  const processKey = Object.keys(processKeyToCodeMap).find(key => processKeyToCodeMap[key] === template.category)
  if (!processKey) {
    ElMessage.error('无法识别模板对应的工序类型')
    return
  }

  selected.value.push({
    uid: Math.random().toString(36).slice(2),
    key: processKey,
    name: processKey,
    category: template.category,
    equipment: '',
    operator: '',
    templateId: template.id,
    templateName: template.name,
    required: template.category === 'SUMMARY'
  })
}

  selected.value.push({
    uid: Math.random().toString(36).slice(2),
    key: s.key,
    name: s.name,
    category: s.category,
    equipment: '',
    operator: '',
    templateId: null
  })
}

const moveUp = (i: number) => {
  // 如果是第一个元素（加工工序汇总），不能上移
  if (i <= 0) return
  const t = selected.value.splice(i, 1)[0]
  selected.value.splice(i - 1, 0, t)
}

const moveDown = (i: number) => {
  if (i >= selected.value.length - 1) return
  const t = selected.value.splice(i, 1)[0]
  selected.value.splice(i + 1, 0, t)
}

const remove = (i: number) => {
  // 检查是否是加工工序汇总，不能移除
  if (selected.value[i]?.required) {
    ElMessage.warning('加工工序汇总是必须的，不能移除')
    return
  }
  selected.value.splice(i, 1)
}

// 处理模板选择变化
const handleTemplateChange = (process: any) => {
  // 如果选择了模板，验证是否匹配工序类型
  if (process.templateId) {
    const template = allTemplates.value.find(t => t.id === process.templateId)
    if (template && template.category !== process.category) {
      ElMessage.warning(`选择的模板"${template.name}"不适用于"${process.name}"工序`)
      process.templateId = null
      return
    }
  }
}

// 获取适用于指定工序的模板列表
const getTemplatesForProcess = (processCategory: string) => {
  return allTemplates.value.filter(template => template.category === processCategory)
}

// 打开模板选择对话框
const openTemplateSelector = (process: any) => {
  currentProcess.value = process
  showTemplateDialog.value = true
}

// 处理模板选择
const handleTemplateSelect = (template: any, applyMode: string) => {
  if (template && currentProcess.value) {
    currentProcess.value.templateId = template.id
    currentProcess.value.template = template
    showTemplateDialog.value = false
    currentProcess.value = null
    ElMessage.success(`已选择模板"${template.name}"`)
  }
}
</script>

<style scoped lang="scss">
.designer-wrap {
  padding: 20px;
  background-color: var(--el-bg-color-page);
  min-height: 100vh;
  background-image: linear-gradient(135deg, var(--el-bg-color) 0%, var(--el-bg-color-page) 100%);
}
.designer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 16px 24px;
  background-color: var(--el-bg-color);
  border-radius: 12px;
  border: 1px solid var(--el-border-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.header-buttons { display: flex; gap: 12px; }
.meta { display: flex; gap: 16px; }
.meta :deep(.el-tag) {
  font-size: 16px;
  font-weight: bold;
  padding: 8px 16px;
  border-radius: 8px;
  border: 2px solid var(--el-border-color);
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}
.list-title {
  font-size: 20px;
  font-family: PingFang SC;
  font-weight: 600;
  line-height: 30px;
  margin-bottom: 20px;
  color: rgba(38, 38, 38, 1);
  display: flex;
  align-items: center;
  padding: 0 16px;
  position: relative;
}

/* 添加标题前的蓝色指示点 */
.list-title::before {
  content: "";
  display: inline-block;
  width: 8px;
  height: 28px;
  background-color: rgba(27, 154, 238, 1);
  border-radius: 4px;
  margin-right: 8px;
}

.selected-title {
  font-size: 20px;
  font-family: PingFang SC;
  font-weight: 600;
  line-height: 30px;
  margin-bottom: 20px;
  color: rgba(38, 38, 38, 1);
  display: flex;
  align-items: center;
  padding: 0 16px;
  position: relative;
}

/* 添加标题前的蓝色指示点 */
.selected-title::before {
  content: "";
  display: inline-block;
  width: 8px;
  height: 28px;
  background-color: rgba(27, 154, 238, 1);
  border-radius: 4px;
  margin-right: 8px;
}

/* 调整卡片样式 */
:deep(.el-card) {
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-card__body) {
  padding: 0;
}

.template-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px;
  background-color: #f9fafb;
  max-height: 400px;
  overflow-y: auto;
}

.template-item {
  padding: 12px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  cursor: pointer;
  text-align: left;
  background-color: #fff;
  transition: all 0.3s ease;
}

.template-item:hover {
  background-color: #f0f9ff;
  border-color: #93c5fd;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.template-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.template-name {
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 600;
  line-height: 20px;
  color: rgba(38, 38, 38, 1);
}

.template-desc {
  font-size: 12px;
  color: rgba(107, 114, 128, 1);
  line-height: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.template-meta {
  display: flex;
  justify-content: flex-end;
}

.template-badge {
  display: flex;
  align-items: center;
  margin-right: 12px;
}

.selected-list {
  padding: 16px;
  background-color: #f9fafb;
  min-height: 200px;
}

.selected-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background-color: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.selected-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.selected-name {
  flex: 0 0 auto;
  min-width: 140px;
  font-size: 14px;
  font-family: PingFang SC;
  line-height: 20px;
  color: rgba(38, 38, 38, 1);
}

.selected-item :deep(.el-select),
.selected-item :deep(.el-input) {
  text-align: left;
  font-size: 14px;
  font-family: PingFang SC;
}

.selected-item :deep(.el-button) {
  text-align: center;
  font-size: 14px;
  font-family: PingFang SC;
  padding: 4px 12px;
  border-radius: 4px;
}

/* 调整下一步按钮样式 */
:deep(.el-button--primary) {
  background-color: rgba(27, 154, 238, 1);
  border-color: rgba(27, 154, 238, 1);
  font-size: 16px;
  font-family: PingFang SC;
  padding: 8px 32px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

:deep(.el-button--primary:hover) {
  background-color: rgba(22, 130, 200, 1);
  border-color: rgba(22, 130, 200, 1);
}

/* 调整整体布局 */
:deep(.el-row) {
  margin-top: 16px;
}

:deep(.el-col) {
  padding: 0 6px;
}
</style>
