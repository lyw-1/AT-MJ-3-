<template>
  <div class="process-preset-wrap">
    <div class="preset-header">
      <div class="header-content">
        <div class="meta">
          <el-tag>模号 {{ moldNumber }}</el-tag>
          <el-tag type="info">成品规格 {{ specification }}</el-tag>
          <el-tag type="success">负责人 {{ owner }}</el-tag>
        </div>
        <div class="header-buttons">
          <el-button @click="handleBack">
            返回列表
          </el-button>
          <el-button type="primary" @click="savePresets" :loading="saving">保存预设置</el-button>
        </div>
      </div>

    </div>
    
    <el-row :gutter="20">
      <!-- 左侧工序列表 -->
      <el-col :span="8">
        <el-card>
          <div class="card-title">工序列表</div>
          <div class="process-list">
            <el-radio-group v-model="selectedProcess" @change="loadPresets">
              <el-radio-button
                v-for="process in processes"
                :key="process.code"
                :label="process"
                class="process-item"
              >
                {{ process.name }}
              </el-radio-button>
            </el-radio-group>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧预设置表单或表格 -->
      <el-col :span="16">
        <!-- 备料工序和加工工序汇总 - 表格形式（适合A4打印） -->
        <el-card v-if="selectedProcess && (selectedProcess.code === 'PREP' || selectedProcess.code === 'SUMMARY')" class="preset-card">
          <div class="card-title">
            {{ selectedProcess.name }} - 预设置
            <div class="card-actions">
              <el-button type="success" size="small" @click="handlePrintPreview" icon="Printer">
                打印预览
              </el-button>
              <el-button type="primary" size="small" @click="openTemplateDialog">
                {{ selectedProcess.templateId ? '更换模板' : '选择模板' }}
              </el-button>
            </div>
          </div>
          
          <!-- 流转卡布局容器 -->
          <div class="flow-card-container">
            <!-- 标题栏 -->
            <div class="flow-card-header">
              <h2 class="flow-card-title">模具加工工艺流转卡</h2>
              <span class="flow-card-version">V2.0</span>
            </div>
            
            <!-- 模具基本信息表格 -->
            <div class="flow-card-section">
              <div class="section-label">模具基本信息</div>
              <table class="flow-card-table info-table">
                <tbody>
                  <!-- 第一行 -->
                  <tr>
                    <td class="label-cell">成品规格：</td>
                    <td colspan="2"><el-input v-model="moldInfo.productSpec" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">模具规格：</td>
                    <td colspan="10"><el-input v-model="moldInfo.moldSpec" placeholder="请输入" size="small" /></td>
                    <td rowspan="4" class="qrcode-cell">
                      <div class="qrcode-wrapper">
                        <qrcode-vue :value="qrcodeValue" :size="100" level="H" />
                        <div class="qrcode-text">二维码</div>
                      </div>
                    </td>
                  </tr>
                  
                  <!-- 第二行 -->
                  <tr>
                    <td class="label-cell">模具编号：</td>
                    <td colspan="2"><el-input v-model="moldInfo.moldNumber" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">模具刻字：</td>
                    <td colspan="10"><el-input v-model="moldInfo.moldEngraving" placeholder="请输入" size="small" /></td>
                  </tr>
                  
                  <!-- 第三行 -->
                  <tr>
                    <td class="label-cell">模具外形</td>
                    <td colspan="2"><el-input v-model="moldInfo.moldShape" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">模具厚度：</td>
                    <td colspan="2"><el-input v-model="moldInfo.moldThickness" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">定位孔中心距：</td>
                    <td colspan="2"><el-input v-model="moldInfo.positioningHoleDistance" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">材料：</td>
                    <td colspan="4"><el-input v-model="moldInfo.material" placeholder="请输入" size="small" /></td>
                  </tr>
                  
                  <!-- 第四行 -->
                  <tr>
                    <td class="label-cell">进泥孔径：</td>
                    <td colspan="2"><el-input v-model="moldInfo.mudInletDiameter" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">槽宽：</td>
                    <td colspan="2"><el-input v-model="moldInfo.slotWidth" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">槽间距：</td>
                    <td colspan="2"><el-input v-model="moldInfo.slotSpacing" placeholder="请输入" size="small" /></td>
                    <td class="label-cell">硬度：</td>
                    <td colspan="3"><el-input v-model="moldInfo.hardness" placeholder="请输入" size="small" /></td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <!-- 工序路线图 -->
            <div v-if="selectedProcess.code === 'SUMMARY'" class="flow-card-section">
              <div class="section-label">工序路线图</div>
              <div class="flow-diagram-area">
                <svg width="100%" height="150" viewBox="0 0 900 150" preserveAspectRatio="xMidYMid meet">
                  <defs>
                    <marker id="arrowhead-flow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto">
                      <polygon points="0 0, 8 3, 0 6" fill="#2563eb" />
                    </marker>
                  </defs>
                  <g stroke="#2563eb" stroke-width="1.5" fill="none">
                    <!-- 动态生成节点 -->
                    <rect 
                      v-for="node in flowChartNodes" 
                      :key="node.index"
                      :x="node.x" 
                      :y="node.y" 
                      :width="node.width" 
                      :height="node.height" 
                      rx="8" 
                      ry="8" 
                      fill="white" 
                    />
                    
                    <!-- 动态生成箭头 -->
                    <line 
                      v-for="(arrow, idx) in flowChartArrows" 
                      :key="'arrow-flow-' + idx"
                      :x1="arrow.x1" 
                      :y1="arrow.y1" 
                      :x2="arrow.x2" 
                      :y2="arrow.y2" 
                      marker-end="url(#arrowhead-flow)" 
                    />
                  </g>
                  
                  <!-- 动态生成文字 -->
                  <text 
                    v-for="node in flowChartNodes" 
                    :key="'text-' + node.index"
                    :x="node.x + node.width / 2" 
                    :y="node.y + node.height / 2 + 5" 
                    text-anchor="middle" 
                    font-size="12" 
                    fill="#333"
                    font-weight="500"
                  >
                    {{ processList[node.index]?.processName || '' }}
                  </text>
                </svg>
              </div>
            </div>
            
            <!-- 工序内容表格 -->
            <div class="flow-card-section no-label">
              <table class="flow-card-table process-list-table">
                <tbody>
                  <tr v-for="(row, index) in processList" :key="index" class="process-row">
                    <td class="seq-cell">{{ index + 1 }}</td>
                    <td class="content-cell">
                      <el-input 
                        v-model="row.processName" 
                        type="textarea" 
                        :rows="1" 
                        placeholder="" 
                        size="small"
                        class="borderless-input"
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </el-card>
        
        <!-- 其他工序 - 表单形式 -->
        <el-card v-else>
          <div class="card-title">
            {{ selectedProcess ? selectedProcess.name + ' - 预设置' : '请选择工序' }}
            <div class="card-actions">
              <el-button type="primary" size="small" @click="openTemplateDialog" v-if="selectedProcess">
                选择模板
              </el-button>
            </div>
          </div>
          
          <el-form v-if="selectedProcess" :model="presetForm" label-width="120px" class="preset-form">
            <!-- 动态生成预设置表单字段 -->
            <el-form-item
              v-for="(field, index) in presetFields"
              :key="index"
              :label="field.label"
              :prop="field.key"
              :rules="[{ required: field.required, message: '请输入' + field.label }]"
            >
              <!-- 根据字段类型渲染不同的表单控件 -->
              <el-input
                v-if="field.type === 'text'"
                v-model="presetForm[field.key]"
                :placeholder="'请输入' + field.label"
              />
              <el-input-number
                v-else-if="field.type === 'number'"
                v-model="presetForm[field.key]"
                :min="field.min || 0"
                :max="field.max || 99999"
                :step="field.step || 1"
                :placeholder="'请输入' + field.label"
              />
              <el-select
                v-else-if="field.type === 'select'"
                v-model="presetForm[field.key]"
                :placeholder="'请选择' + field.label"
              >
                <el-option
                  v-for="option in field.options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
              <el-switch
                v-else-if="field.type === 'boolean'"
                v-model="presetForm[field.key]"
              />
            </el-form-item>
            
            <!-- 自定义预设置字段 -->
            <el-divider />
            <div class="custom-fields">
              <div class="custom-title">自定义预设置</div>
              <div
                v-for="(customField, index) in customFields"
                :key="index"
                class="custom-field-item"
              >
                <el-input
                  v-model="customField.key"
                  placeholder="键名"
                  style="width: 200px; margin-right: 10px"
                />
                <el-input
                  v-model="customField.value"
                  placeholder="值"
                  style="width: 300px; margin-right: 10px"
                />
                <el-button
                  type="danger"
                  size="small"
                  @click="removeCustomField(index)"
                >
                  删除
                </el-button>
              </div>
              <el-button type="primary" size="small" @click="addCustomField">
                添加自定义字段
              </el-button>
            </div>
          </el-form>
          
          <!-- 无工序选择时的提示 -->
          <div v-else class="empty-tip">
            请选择一个工序来设置预参数
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
      <template #default>
        <template-selector
          :process-code="selectedProcess.code"
          @select="handleTemplateSelect"
        />
      </template>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showTemplateDialog = false">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProcessPresets, batchSaveProcessPresets, getMoldInitialParamDetail, updateMoldInitialParam } from '@/api/mold'
import { getProcessTemplateDetail } from '@/api/process'
import QrcodeVue from 'qrcode.vue'
import TemplateSelector from '@/components/process/TemplateSelector.vue'

const route = useRoute()
const router = useRouter()

// 页面状态
const saving = ref(false)
const moldNumber = ref('')
const specification = ref('')
const owner = ref('')
const moldId = ref<number>(0)

// 模板选择相关状态
const showTemplateDialog = ref(false)

// 二维码内容 - 生成工序预设置页面的链接
const qrcodeValue = ref('')

// 从路由参数获取工序列表
const getProcessesFromRoute = () => {
  try {
    const processesParam = route.query.processes as string
    if (processesParam) {
      const parsedProcesses = JSON.parse(processesParam)
      // 确保工序名称格式正确，并包含模板信息
      return parsedProcesses.map((process: any) => ({
        ...process,
        name: process.name,
        templateId: process.templateId,
        category: process.category,
        equipment: process.equipment || '',
        operator: process.operator || ''
      }))
    }
  } catch (error) {
    console.error('解析工序参数失败:', error)
  }
  // 默认返回空数组
  return []
}

// 工序列表
const processes = ref(getProcessesFromRoute())

// 选中的工序
const selectedProcess = ref<any>(null)

// 预设置字段定义（根据工序类型动态变化）
const presetFields = ref<any[]>([])

// 预设置表单数据
const presetForm = ref<Record<string, any>>({})

// 自定义字段
const customFields = ref<Array<{ key: string; value: string }>>([])

// 模具基本信息（用于备料工序表格）
const moldInfo = ref({
  responsiblePerson: '',
  moldEngraving: '',
  moldNumber: '',
  productSpec: '',
  moldSpec: '',
  moldShape: '',
  material: '',
  hardness: '',
  positioningHoleDistance: '',
  moldThickness: '',
  mudInletDiameter: '',
  slotWidth: '',
  slotSpacing: ''
})

// 工序内容列表（用于备料工序和加工工序汇总表格）
const processList = ref([
  { processName: '', details: '' },
  { processName: '', details: '' },
  { processName: '', details: '' },
  { processName: '', details: '' }
])

// 动态生成流程图节点
const flowChartNodes = computed(() => {
  const validProcesses = processList.value.filter(p => p.processName && p.processName.trim() !== '')
  if (validProcesses.length === 0) return []
  
  const nodes: any[] = []
  const minNodeWidth = 80   // 最小宽度
  const maxNodeWidth = 150  // 最大宽度
  const nodeHeight = 50
  const nodeGap = 25        // 从20增加到25，进一步加宽间距
  const startX = 10
  const firstRowY = 15
  const secondRowY = 80
  const charWidth = 14  // 每个字符的估算宽度（中文）
  const padding = 20    // 左右内边距
  
  // 计算每个节点的宽度
  const nodeWidths = validProcesses.map(process => {
    const textLength = process.processName.length
    const calculatedWidth = textLength * charWidth + padding
    // 限制在最小和最大宽度之间
    return Math.max(minNodeWidth, Math.min(maxNodeWidth, calculatedWidth))
  })
  
  const maxNodes = Math.min(validProcesses.length, 9)
  const firstRowCount = Math.min(maxNodes, 7)
  const secondRowCount = maxNodes > 7 ? Math.min(maxNodes - 7, 2) : 0
  
  // 生成第一行节点
  let currentX = startX
  for (let i = 0; i < firstRowCount; i++) {
    const width = nodeWidths[i]
    nodes.push({ 
      index: i, 
      x: currentX, 
      y: firstRowY, 
      width: width, 
      height: nodeHeight, 
      row: 1 
    })
    currentX += width + nodeGap
  }
  
  // 生成第二行节点（从右到左）
  if (secondRowCount > 0) {
    // 计算第二行起始位置（与第一行最后一个节点对齐）
    const lastFirstRowNode = nodes[firstRowCount - 1]
    currentX = lastFirstRowNode.x
    
    for (let i = 0; i < secondRowCount; i++) {
      const width = nodeWidths[firstRowCount + i]
      nodes.push({ 
        index: firstRowCount + i, 
        x: currentX, 
        y: secondRowY, 
        width: width, 
        height: nodeHeight, 
        row: 2 
      })
      currentX -= (width + nodeGap)
    }
  }
  
  return nodes
})

// 动态生成流程图箭头
const flowChartArrows = computed(() => {
  const nodes = flowChartNodes.value
  if (nodes.length === 0) return []
  
  const arrows: any[] = []
  const firstRowNodes = nodes.filter(n => n.row === 1)
  
  for (let i = 0; i < firstRowNodes.length - 1; i++) {
    const from = firstRowNodes[i]
    const to = firstRowNodes[i + 1]
    arrows.push({
      x1: from.x + from.width,
      y1: from.y + from.height / 2,
      x2: to.x,
      y2: to.y + to.height / 2
    })
  }
  
  const secondRowNodes = nodes.filter(n => n.row === 2)
  if (secondRowNodes.length > 0) {
    const lastFirstRowNode = firstRowNodes[firstRowNodes.length - 1]
    const firstSecondRowNode = secondRowNodes[0]
    
    arrows.push({
      x1: lastFirstRowNode.x + lastFirstRowNode.width / 2,
      y1: lastFirstRowNode.y + lastFirstRowNode.height,
      x2: firstSecondRowNode.x + firstSecondRowNode.width / 2,
      y2: firstSecondRowNode.y
    })
    
    if (secondRowNodes.length > 1) {
      arrows.push({
        x1: secondRowNodes[0].x,
        y1: secondRowNodes[0].y + secondRowNodes[0].height / 2,
        x2: secondRowNodes[1].x + secondRowNodes[1].width,
        y2: secondRowNodes[1].y + secondRowNodes[1].height / 2
      })
    }
  }
  
  return arrows
})

// 初始化页面
onMounted(() => {
  // 从路由参数获取模具ID
  const id = Number(route.params.id || 0)
  if (id) {
    moldId.value = id
    // 加载模具信息
    loadMoldInfo()
    // 加载工序列表
    loadProcessList()
    
    // 设置二维码内容
    qrcodeValue.value = `${window.location.origin}/mold/process-preset/${id}`
  }
})

// 加载模具信息
const loadMoldInfo = async () => {
  try {
    // 优先从路由查询参数获取模具信息
    const queryMoldNumber = route.query.moldNumber as string
    const querySpecification = route.query.specification as string
    const queryOwner = route.query.owner as string
    
    if (queryMoldNumber && querySpecification && queryOwner) {
      // 使用路由传递的数据
      moldNumber.value = queryMoldNumber
      specification.value = querySpecification
      owner.value = queryOwner
      
      // 从API获取完整的模具基本信息
      const response = await getMoldInitialParamDetail(moldId.value)
      const data = response || {}
      
      // 填充模具基本信息表格中已有的字段
      Object.assign(moldInfo.value, {
        responsiblePerson: data.ownerName || data.responsiblePerson || queryOwner,
        moldNumber: data.moldCode || data.moldNumber || queryMoldNumber,
        productSpec: data.productSpec || data.specification || querySpecification,
        material: data.material || data.steelMaterial,
        hardness: data.hrc
      })
    } else {
      // 调用API获取模具信息
      const response = await getMoldInitialParamDetail(moldId.value)
      const data = response || {}
      
      // 填充模具信息
      moldNumber.value = data.moldCode || data.moldNumber
      specification.value = data.productSpec || data.specification
      owner.value = data.ownerName || data.responsiblePerson
      
      // 填充模具基本信息表格中已有的字段
      Object.assign(moldInfo.value, {
        responsiblePerson: data.ownerName || data.responsiblePerson,
        moldNumber: data.moldCode || data.moldNumber,
        productSpec: data.productSpec || data.specification,
        material: data.material || data.steelMaterial,
        hardness: data.hrc,
        positioningHoleDistance: data.locationHolePitch || data.positioningHoleDistance,
        moldThickness: data.moldThickness,
        mudInletDiameter: data.inletDiameter,
        slotWidth: data.slotWidth,
        slotSpacing: data.slotSpacing
      })
    }
  } catch (error) {
    ElMessage.error('加载模具信息失败')
    console.error('加载模具信息失败:', error)
  }
}

// 加载工序列表
const loadProcessList = async () => {
  // 使用从路由参数获取的工序列表
  // 默认选择第一个工序
  if (processes.value.length > 0) {
    selectedProcess.value = processes.value[0]
    loadPresets()
  }
}

// 根据工序类型获取对应的预设置字段
const getFieldsByProcess = (processCode: string) => {
  // 不同工序类型的预设置字段
  const fieldConfig: Record<string, any[]> = {
    'PREP': [
      { key: 'material', label: '材质', type: 'select', required: true, options: [{ label: 'H13', value: 'H13' }, { label: 'P20', value: 'P20' }, { label: '718H', value: '718H' }] },
      { key: 'thickness', label: '厚度', type: 'number', required: true, min: 0, max: 100, step: 0.1 },
      { key: 'hardness', label: '硬度', type: 'text', required: true }
    ],
    'DRILL_NON_LAYER': [
      { key: 'holeDiameter', label: '孔径', type: 'number', required: true, min: 0, max: 50, step: 0.1 },
      { key: 'holeDepth', label: '孔深', type: 'number', required: true, min: 0, max: 200, step: 0.1 },
      { key: 'holeCount', label: '孔数', type: 'number', required: true, min: 1, max: 1000 },
      { key: 'drillSpeed', label: '钻孔速度', type: 'number', required: true, min: 100, max: 10000, step: 100 }
    ],
    'DRILL_LAYER': [
      { key: 'holeDiameter', label: '孔径', type: 'number', required: true, min: 0, max: 50, step: 0.1 },
      { key: 'holeDepth', label: '孔深', type: 'number', required: true, min: 0, max: 200, step: 0.1 },
      { key: 'holeCount', label: '孔数', type: 'number', required: true, min: 1, max: 1000 },
      { key: 'layerCount', label: '层数', type: 'number', required: true, min: 1, max: 50 },
      { key: 'layerDepth', label: '每层深度', type: 'number', required: true, min: 1, max: 20, step: 0.1 }
    ],
    'HEAT_STRESS': [
      { key: 'temperature', label: '温度', type: 'number', required: true, min: 100, max: 2000, step: 10 },
      { key: 'duration', label: '时长（小时）', type: 'number', required: true, min: 1, max: 100, step: 1 },
      { key: 'coolingMethod', label: '冷却方式', type: 'select', required: true, options: [{ label: '空冷', value: '空冷' }, { label: '水冷', value: '水冷' }, { label: '油冷', value: '油冷' }] }
    ],
    'HEAT_HARDNESS': [
      { key: 'temperature', label: '温度', type: 'number', required: true, min: 100, max: 2000, step: 10 },
      { key: 'duration', label: '时长（小时）', type: 'number', required: true, min: 1, max: 100, step: 1 },
      { key: 'expectedHardness', label: '预期硬度', type: 'text', required: true }
    ],
    'GUIDE_SLOT': [
      { key: 'slotWidth', label: '槽宽', type: 'number', required: true, min: 0.1, max: 10, step: 0.01 },
      { key: 'slotDepth', label: '槽深', type: 'number', required: true, min: 0.1, max: 50, step: 0.1 },
      { key: 'machineType', label: '加工设备', type: 'select', required: true, options: [{ label: '中走丝', value: '中走丝' }, { label: '切槽机', value: '切槽机' }, { label: '电火花成型机', value: '电火花成型机' }] }
    ],
    'SHAPE': [
      { key: 'machineType', label: '加工设备', type: 'select', required: true, options: [{ label: '铣床', value: '铣床' }, { label: '精雕机', value: '精雕机' }, { label: '线切割机', value: '线切割机' }] },
      { key: 'tolerance', label: '加工公差', type: 'number', required: true, min: 0.001, max: 1, step: 0.001 },
      { key: 'surfaceRoughness', label: '表面粗糙度', type: 'text', required: true }
    ],
    'SELF_CHECK': [
      { key: 'checkItems', label: '检查项目', type: 'text', required: true },
      { key: 'tolerance', label: '检查公差', type: 'number', required: true, min: 0.001, max: 1, step: 0.001 }
    ],
    'IN_STORAGE': [
      { key: 'storageLocation', label: '存放位置', type: 'text', required: true },
      { key: 'isKeyMold', label: '是否为重点模具', type: 'boolean', required: true }
    ]
  }
  
  return fieldConfig[processCode] || []
}

// 加载预设置数据
const loadPresets = async () => {
  if (!selectedProcess.value) return
  
  // 初始化数据
  if (selectedProcess.value.code === 'PREP') {
    // 备料工序 - 初始化表格数据
    // 保留负责人默认值，其他字段清空
    const responsiblePerson = moldInfo.value.responsiblePerson
    moldInfo.value = {
      responsiblePerson,
      moldEngraving: '',
      moldNumber: '',
      productSpec: '',
      material: '',
      hardness: '',
      moldSpec: '',
      positioningHoleDistance: '',
      moldThickness: '',
      mudInletDiameter: '',
      slotWidth: '',
      slotSpacing: ''
    }
    
    // 工序内容列表重置 - 备料工序4行
    processList.value = [
      { processName: '', details: '' },
      { processName: '', details: '' },
      { processName: '', details: '' },
      { processName: '', details: '' }
    ]
 } else if (selectedProcess.value.code === 'SUMMARY') {
    // 加工工序汇总 - 初始化表格数据（14行）
    const responsiblePerson = moldInfo.value.responsiblePerson
    moldInfo.value = {
      responsiblePerson,
      moldEngraving: '',
      moldNumber: '',
      productSpec: '',
      material: '',
      hardness: '',
      moldSpec: '',
      positioningHoleDistance: '',
      moldThickness: '',
      mudInletDiameter: '',
      slotWidth: '',
      slotSpacing: ''
    }
    
    // 工序内容列表重置 - 加工工序汇总：从路由参数中的已选工序自动填充
    // 过滤掉"加工工序汇总"自身，因为它是汇总页面不是实际工序
    const selectedProcesses = processes.value.filter(p => p.code !== 'SUMMARY')
    
    // 根据已选工序数量动态生成列表，有几个工序就有几行
    processList.value = selectedProcesses.map(process => ({
      processName: process.name || '',
      details: ''
    }))
  } else {
    // 其他工序 - 初始化表单数据
    presetFields.value = getFieldsByProcess(selectedProcess.value.code)
    presetForm.value = {}
  }
  
  // 加载已有的预设置
  try {
    // 这里应该调用API获取预设置数据，暂时使用模拟数据
    const presets = await getProcessPresets(moldId.value, selectedProcess.value.code)
    
    if (selectedProcess.value.code === 'PREP' || selectedProcess.value.code === 'SUMMARY') {
      // 备料工序和加工工序汇总 - 填充表格数据
      presets.forEach((preset: any) => {
        const key = preset.presetKey
        const value = preset.presetValue
        
        if (key.startsWith('mold_')) {
          // 模具基本信息
          const field = key.replace('mold_', '')
          if (moldInfo.value.hasOwnProperty(field)) {
            moldInfo.value[field as keyof typeof moldInfo.value] = value
          }
        } else if (key.startsWith('process_')) {
          // 工序内容
          const match = key.match(/^process_(\d+)_(\w+)$/)
          if (match) {
            const index = parseInt(match[1])
            const field = match[2]
            if (processList.value[index] && processList.value[index].hasOwnProperty(field)) {
              if (field === 'date') {
                processList.value[index][field as keyof typeof processList.value[0]] = new Date(value)
              } else {
                processList.value[index][field as keyof typeof processList.value[0]] = value
              }
            }
          }
        }
      })
    } else {
      // 其他工序 - 填充表单数据
      presets.forEach((preset: any) => {
        presetForm.value[preset.presetKey] = preset.presetValue
      })
    }
    
    // 处理自定义字段
    // 这里可以根据实际需求处理自定义字段

    // 如果当前工序有预设的模板ID，自动应用该模板
    if (selectedProcess.value.templateId) {
      console.log('检测到预设模板ID:', selectedProcess.value.templateId)
      loadTemplateData(selectedProcess.value.templateId, 'replace')
    }
  } catch (error) {
    ElMessage.error('加载预设置失败')
    console.error('加载预设置失败:', error)
  }
}

// 添加自定义字段
const addCustomField = () => {
  customFields.value.push({ key: '', value: '' })
}

// 删除自定义字段
const removeCustomField = (index: number) => {
  customFields.value.splice(index, 1)
}

// 保存预设置
const savePresets = async () => {
  if (!selectedProcess.value) {
    ElMessage.warning('请选择工序')
    return
  }
  
  saving.value = true
  try {
    let presets = []
    
    if (selectedProcess.value.code === 'PREP' || selectedProcess.value.code === 'SUMMARY') {
      // 备料工序和加工工序汇总 - 处理表格数据
      // 模具基本信息
      Object.entries(moldInfo.value).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          presets.push({
            moldId: moldId.value,
            processCode: selectedProcess.value.code,
            presetKey: `mold_${key}`,
            presetValue: String(value)
          })
        }
      })
      
      // 工序内容
      processList.value.forEach((process, index) => {
        Object.entries(process).forEach(([key, value]) => {
          if (value !== null && value !== undefined && value !== '') {
            presets.push({
              moldId: moldId.value,
              processCode: selectedProcess.value.code,
              presetKey: `process_${index}_${key}`,
              presetValue: String(value)
            })
          }
        })
      })
    } else {
      // 其他工序 - 处理表单数据
      // 构建预设置数据
      presets = Object.entries(presetForm.value).map(([key, value]) => ({
        moldId: moldId.value,
        processCode: selectedProcess.value.code,
        presetKey: key,
        presetValue: String(value)
      }))
      
      // 添加自定义字段
      customFields.value.forEach(field => {
        if (field.key && field.value) {
          presets.push({
            moldId: moldId.value,
            processCode: selectedProcess.value.code,
            presetKey: field.key,
            presetValue: field.value
          })
        }
      })
    }
    
    // 调用API保存预设置
    await batchSaveProcessPresets(presets)
    
    // 保存成功后，将模具状态更新为待加工状态
    await updateMoldInitialParam(moldId.value, {
      status: 'pending' // 待加工状态
    })
    
    ElMessage.success('保存预设置成功，模具已进入待加工状态')
  } catch (error) {
    ElMessage.error('保存预设置失败')
    console.error('保存预设置失败:', error)
  } finally {
    saving.value = false
  }
}

// 打开模板选择对话框
const openTemplateDialog = () => {
  if (selectedProcess.value) {
    showTemplateDialog.value = true
  } else {
    ElMessage.warning('请先选择一个工序')
  }
}

// 打印预览功能
const handlePrintPreview = () => {
  const printContent = document.querySelector('.flow-card-container')
  if (!printContent) {
    ElMessage.warning('没有可打印的内容')
    return
  }
  
  // 创建新窗口用于打印
  // 克隆内容以便处理
  const clonedContent = printContent.cloneNode(true) as HTMLElement
  
  // 处理所有El-Input组件，提取value值
  clonedContent.querySelectorAll('.el-input, .el-textarea').forEach(inputWrapper => {
    const input = inputWrapper.querySelector('input, textarea') as HTMLInputElement | HTMLTextAreaElement
    if (input) {
      const value = input.value || ''
      // 创建一个简单的span替换
      const span = document.createElement('span')
      span.textContent = value
      span.style.cssText = 'display: block; padding: 4px 8px; min-height: 24px; font-size: 13px; line-height: 1.4;'
      inputWrapper.replaceWith(span)
    }
  })
  
  // 创建新窗口用于打印
  const printWindow = window.open('', '_blank', 'width=900,height=1000')
  if (!printWindow) {
    ElMessage.error('无法打开打印窗口，请检查浏览器弹窗设置')
    return
  }
  
  // 写入打印内容
  printWindow.document.write(`
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="utf-8">
      <title>模具加工工艺流转卡</title>
      <style>
        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }
        
        @page {
          size: A4 portrait;
          margin: 15mm 10mm;
        }
        
        body {
          font-family: Arial, 'Microsoft YaHei', 'SimSun', sans-serif;
          padding: 5mm;
          background: white;
        }
        
        /* 流转卡样式 */
        .flow-card-container {
          border: 2px solid #000;
          padding: 15px;
          background: white;
          page-break-inside: avoid;
        }
        
        .flow-card-header {
          display: flex;
          justify-content: center;
          align-items: center;
          position: relative;
          margin-bottom: 15px;
          padding-bottom: 8px;
          border-bottom: 2px solid #000;
        }
        
        .flow-card-title {
          font-size: 22px;
          font-weight: bold;
          margin: 0;
          text-align: center;
        }
        
        .flow-card-version {
          position: absolute;
          right: 0;
          top: 0;
          font-size: 12px;
        }
        
        .flow-card-section {
          margin-bottom: 15px;
        }
        
        .section-label {
          font-size: 13px;
          font-weight: 600;
          margin-bottom: 6px;
          margin-left: -15px;
          padding: 3px 6px;
          background: #f5f5f5;
          border-left: 4px solid #2563eb;
        }
        
        .flow-card-table {
          width: 100%;
          border-collapse: collapse;
          border: 1.5px solid #000;
        }
        
        .flow-card-table td,
        .flow-card-table th {
          border: 1px solid #000;
          padding: 4px 6px;
          vertical-align: middle;
          font-size: 12px;
          line-height: 1.4;
        }
        
        .label-cell {
          background: #f5f5f5;
          font-weight: 600;
          font-size: 11px;
          white-space: nowrap;
          text-align: center;
          width: 80px;
          -webkit-print-color-adjust: exact;
          print-color-adjust: exact;
        }
        
        .qrcode-cell {
          text-align: center;
          vertical-align: middle;
          padding: 6px;
          width: 120px;
        }
        
        .qrcode-wrapper {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 8px;
        }
        
        .qrcode-text {
          font-size: 12px;
          font-weight: 600;
          text-align: center;
          letter-spacing: 4px;
          margin-top: 4px;
        }
        
        .flow-diagram-area {
          background: #f8f9fa;
          padding: 10px;
          border: 1px solid #ddd;
          min-height: 160px;
          -webkit-print-color-adjust: exact;
          print-color-adjust: exact;
        }
        
        svg {
          display: block;
          width: 100%;
          height: auto;
          max-height: 160px;
        }
        
        .process-list-table .seq-cell {
          width: 50px;
          text-align: center;
          font-weight: 600;
          font-size: 12px;
          background: #f5f5f5;
          -webkit-print-color-adjust: exact;
          print-color-adjust: exact;
        }
        
        .process-list-table .content-cell {
          padding: 3px 8px;
          font-size: 11px;
          line-height: 1.4;
        }
        
        .process-row {
          height: auto;
          min-height: 28px;
        }
        
        span {
          display: block;
          word-wrap: break-word;
          word-break: break-all;
        }
        
        @media print {
          @page {
            size: A4 portrait;
            margin: 10mm 8mm;
          }
          
          body {
            padding: 0;
            margin: 0;
          }
          
          .flow-card-container {
            border: 1.5pt solid #000;
            padding: 8mm;
          }
          
          .flow-card-title {
            font-size: 16pt;
          }
          
          .flow-card-table td,
          .flow-card-table th {
            border: 0.5pt solid #000;
            padding: 2pt 4pt;
            font-size: 8pt;
          }
          
          .label-cell {
            font-size: 7pt !important;
          }
          
          svg {
            max-height: 140px;
          }
        }
      <\/style>
    <\/head>
    <body>
      <div class="flow-card-container">
        ${printHtml}
      </div>
      <script>
        window.onload = function() {
          setTimeout(function() {
            window.print();
          }, 500);
          window.onafterprint = function() {
            window.close();
          };
        };
      <\/script>
    <\/body>
    <\/html>
  `)
  
  printWindow.document.close()
}

// 处理模板选择
const handleTemplateSelect = (template: any, applyMode: string) => {
  if (template) {
    loadTemplateData(template.id, applyMode)
    showTemplateDialog.value = false
  }
}

// 加载模板数据并填充
const loadTemplateData = async (templateId: number, applyMode: string) => {
  try {
    const response = await getProcessTemplateDetail(templateId)
    if (response.data) {
      const templateData = response.data
      if (selectedProcess.value.code === 'PREP') {
        // 备料工序 - 填充表格数据
        if (applyMode === 'replace') {
          // 完全替换模式
          Object.entries(templateData.config || {}).forEach(([key, value]) => {
            if (key.startsWith('mold_')) {
              // 模具基本信息
              const field = key.replace('mold_', '')
              if (moldInfo.value.hasOwnProperty(field)) {
                moldInfo.value[field as keyof typeof moldInfo.value] = value
              }
            } else if (key.startsWith('process_')) {
              // 工序内容
              const match = key.match(/^process_(\d+)_(\w+)$/)
              if (match) {
                const index = parseInt(match[1])
                const field = match[2]
                if (processList.value[index] && processList.value[index].hasOwnProperty(field)) {
                  if (field === 'date') {
                    processList.value[index][field as keyof typeof processList.value[0]] = new Date(value)
                  } else {
                    processList.value[index][field as keyof typeof processList.value[0]] = value
                  }
                }
              }
            }
          })
        } else {
          // 合并模式 - 只填充空值
          Object.entries(templateData.config || {}).forEach(([key, value]) => {
            if (key.startsWith('mold_')) {
              // 模具基本信息
              const field = key.replace('mold_', '')
              if (moldInfo.value.hasOwnProperty(field) && !moldInfo.value[field as keyof typeof moldInfo.value]) {
                moldInfo.value[field as keyof typeof moldInfo.value] = value
              }
            } else if (key.startsWith('process_')) {
              // 工序内容
              const match = key.match(/^process_(\d+)_(\w+)$/)
              if (match) {
                const index = parseInt(match[1])
                const field = match[2]
                if (processList.value[index] && processList.value[index].hasOwnProperty(field) && !processList.value[index][field as keyof typeof processList.value[0]]) {
                  if (field === 'date') {
                    processList.value[index][field as keyof typeof processList.value[0]] = new Date(value)
                  } else {
                    processList.value[index][field as keyof typeof processList.value[0]] = value
                  }
                }
              }
            }
          })
        }
        ElMessage.success('模板应用成功')
      } else {
        // 其他工序 - 填充表单数据
        if (applyMode === 'replace') {
          // 完全替换模式
          Object.assign(presetForm.value, templateData.config || {})
        } else {
          // 合并模式 - 只填充空值
          Object.entries(templateData.config || {}).forEach(([key, value]) => {
            if (!presetForm.value[key]) {
              presetForm.value[key] = value
            }
          })
        }
        ElMessage.success('模板应用成功')
      }
    }
  } catch (error) {
    ElMessage.error('加载模板数据失败')
    console.error('加载模板数据失败:', error)
  }
}

// 返回列表
const handleBack = () => {
  router.push('/mold/initial-params-list')
}
</script>

<style scoped lang="scss">
.process-preset-wrap {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.preset-header {
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

.header-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.meta {
  display: flex;
  gap: 16px;
  align-items: center;
}

.meta :deep(.el-tag) {
  font-size: 16px;
  font-weight: bold;
  padding: 8px 16px;
  border-radius: 8px;
  border: 2px solid var(--el-border-color);
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.header-buttons {
  display: flex;
  gap: 12px;
}

/* 二维码样式 */
.qrcode-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
  background-color: #fff;
  border: 1px solid var(--el-border-color);
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.qrcode-container img {
  display: block;
  border-radius: 4px;
}

/* 模具信息和二维码布局 */
.mold-info-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}

/* 章节标题和二维码布局 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

/* 底部二维码样式 */
.qrcode-container-bottom {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px;
  background-color: #fff;
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.qrcode-container-bottom :deep(.qrcode-vue) {
  margin: 0 auto;
}

@media (max-width: 1200px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .qrcode-container-bottom {
    align-self: flex-start;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .preset-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
  
  .qrcode-container {
    align-self: flex-end;
  }
}

.card-title {
  font-size: 20px;
  font-family: PingFang SC;
  font-weight: 600;
  line-height: 30px;
  margin-bottom: 20px;
  color: rgba(38, 38, 38, 1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.card-actions {
  display: flex;
  gap: 10px;
}

.ml-20 {
  margin-left: 20px;
}

/* 添加标题前的蓝色指示点 */
.card-title::before {
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

.process-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px 0;
}

.process-item {
  width: 100%;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
  background-color: #fff;
  border: 1px solid #e5e7eb;
}

.process-item:hover {
  background-color: #f0f9ff;
  border-color: #93c5fd;
}

:deep(.el-radio-button__original-radio:checked) + .el-radio-button__inner {
  background-color: #1ba2e1;
  border-color: #1ba2e1;
  color: #fff;
}

:deep(.el-radio-button__inner) {
  background-color: transparent;
  border: none;
  color: #333;
  padding: 0;
  box-shadow: none;
  font-size: 14px;
  font-family: PingFang SC;
  line-height: 20px;
}

:deep(.el-radio-button__original-radio:checked) + .el-radio-button__inner::before {
  content: "";
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #fff;
  border-radius: 50%;
  margin-right: 8px;
}

:deep(.el-radio-button) {
  border: none;
  box-shadow: none;
}

:deep(.el-radio-button__wrapper) {
  border: none;
  box-shadow: none;
  padding: 0;
  background-color: transparent;
}

.preset-form {
  padding: 0 20px 20px;
}

.empty-tip {
  text-align: center;
  padding: 40px 0;
  color: var(--el-text-color-placeholder);
}

.custom-fields {
  margin-top: 20px;
  padding: 20px;
  background-color: var(--el-bg-color-light);
  border-radius: 6px;
}

.custom-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--el-text-color-primary);
}

.custom-field-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  gap: 10px;
}

/* 打印友好的表格样式 */
.print-section {
  padding: 0 20px 20px;
}

.print-section-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #1890ff;
}

.print-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
  background: #fff;
  
  th, td {
    border: 0.5pt solid #000;
    padding: 8px 10px;
    text-align: left;
    vertical-align: middle;
  }
  
  th {
    background: #fafafa;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
  }
  
  td {
    background: #fff;
  }
  
  .center {
    text-align: center;
  }
}

/* 基本信息表格内的行布局 */
.cell-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cell-label {
  color: #666;
  font-weight: 500;
  white-space: nowrap;
  padding: 0 4px;
}

/* 表格内输入框样式 */
.print-table :deep(.el-input) {
  .el-input__wrapper {
    box-shadow: none;
    background: transparent;
    padding: 0 4px;
    
    &:hover, &:focus {
      box-shadow: 0 0 0 1px #409eff inset;
    }
  }
  
  .el-input__inner {
    font-size: 13px;
    color: #333;
    
    &::placeholder {
      color: #bfbfbf;
    }
  }
}

.print-table :deep(.el-date-editor) {
  width: 100%;
  
  .el-input__wrapper {
    box-shadow: none;
    background: transparent;
    padding: 0 4px;
  }
}

/* 打印样式 */
@media print {
  /* 隐藏不必要的元素 */
  .preset-header,
  .process-list,
  .el-col:first-child {
    display: none !important;
  }
  
  /* 设置A4纸大小 - 窄边距 */
  @page {
    size: A4 portrait;
    margin: 12.7mm;
  }
  
  * {
    box-sizing: border-box;
  }
  
  html, body {
    width: 210mm;
    height: 297mm;
  }
  
  .process-preset-wrap {
    padding: 0;
    background: #fff;
    width: 184.6mm;
    max-width: 184.6mm;
    margin: 0 auto;
  }
  
  .el-row {
    display: block !important;
    margin: 0 !important;
  }
  
  .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: none !important;
    padding: 0 !important;
  }
  
  :deep(.el-card) {
    border: none;
    box-shadow: none;
  }
  
  :deep(.el-card__body) {
    padding: 0 !important;
  }
  
  .card-title {
    font-size: 14px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 3px;
    padding: 0;
    
    &::before {
      display: none;
    }
  }
  
  .print-section {
    padding: 0;
    margin-bottom: 4px;
  }
  
  .print-section-title {
    font-size: 10px;
    font-weight: 600;
    border-bottom: 0.5pt solid #000;
    margin-bottom: 3px;
    padding-bottom: 1px;
  }
  
  .print-table {
    width: 100%;
    max-width: 100%;
    font-size: 8px;
    table-layout: fixed;
    
    th, td {
      border: 0.5pt solid #000;
      padding: 2px 3px;
      word-wrap: break-word;
      word-break: break-all;
      overflow: hidden;
      line-height: 1.2;
      vertical-align: middle;
    }
    
    th {
      background: #f5f5f5;
      font-weight: 600;
    }
    
    .center {
      text-align: center;
    }
  }
  
  /* 基本信息表格 - 精确列宽控制 */
  .info-table {
    colgroup col:nth-child(1) {
      width: 11%;
    }
    colgroup col:nth-child(2) {
      width: 17%;
    }
    colgroup col:nth-child(3) {
      width: 11%;
    }
    colgroup col:nth-child(4) {
      width: 61%;
    }
  }
  
  /* 工序内容表格 - 精确列宽控制 */
  .process-table {
    colgroup col:nth-child(1) {
      width: 5%;
    }
    colgroup col:nth-child(2) {
      width: 13%;
    }
    colgroup col:nth-child(3) {
      width: 13%;
    }
    colgroup col:nth-child(4) {
      width: 28%;
    }
    colgroup col:nth-child(5) {
      width: 10%;
    }
    colgroup col:nth-child(6) {
      width: 11%;
    }
    colgroup col:nth-child(7) {
      width: 20%;
    }
  }
  
  .cell-row {
    display: flex;
    gap: 2px;
    align-items: center;
    flex-wrap: nowrap;
  }
  
  .cell-label {
    font-size: 8px;
    font-weight: 500;
    white-space: nowrap;
    flex-shrink: 0;
  }
  
  /* 输入框在打印时显示为纯文本 */
  .print-table :deep(.el-input__wrapper),
  .print-table :deep(.el-date-editor .el-input__wrapper) {
    box-shadow: none !important;
    background: transparent !important;
    border: none !important;
    padding: 0 !important;
  }
  
  .print-table :deep(.el-input__inner) {
    font-size: 8px !important;
    padding: 0 !important;
    height: auto !important;
    line-height: 1.2 !important;
  }
  
  .print-table :deep(.el-input__suffix),
  .print-table :deep(.el-input__prefix) {
    display: none;
  }
  
  /* 防止表格跨页断开 */
  .print-section {
    page-break-inside: avoid;
  }
  
  .print-table {
    page-break-inside: auto;
  }
  
  tbody tr {
    page-break-inside: avoid;
    page-break-after: auto;
  }
  
  /* 模具信息和二维码打印布局 */
  .section-header {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 3px;
  }
  
  .mold-info-wrapper {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
  }
  
  .qrcode-container-bottom {
    display: flex !important;
    padding: 3px;
    border: 0.5pt solid #000;
    margin-top: 0;
  }
}

/* 流转卡布局样式 */
.flow-card-container {
  border: 2px solid #000;
  padding: 20px;
  background: white;
}

.flow-card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #000;
}

.flow-card-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  text-align: center;
}

.flow-card-version {
  position: absolute;
  right: 0;
  top: 0;
  font-size: 14px;
  font-weight: normal;
}

.flow-card-section {
  margin-bottom: 20px;
}

.section-label {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
  margin-left: -20px;
  padding: 4px 8px;
  background: #f5f5f5;
  border-left: 4px solid #2563eb;
}

.flow-card-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #000;
}

.flow-card-table td,
.flow-card-table th {
  border: 1px solid #000;
  padding: 6px 8px;
  vertical-align: middle;
}

.flow-card-table .label-cell {
  background: #f9f9f9;
  font-weight: 600;
  font-size: 13px;
  white-space: nowrap;
  text-align: center;
  width: 100px;
}

.flow-card-table .qrcode-cell {
  text-align: center;
  vertical-align: middle;
  padding: 8px;
  width: 120px;
}

.qrcode-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.qrcode-text {
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  color: #333;
  letter-spacing: 5px;
  margin-top: 6px;
}

.flow-card-table .qrcode-label {
  text-align: center;
  vertical-align: middle;
  font-size: 12px;
}

.flow-card-table :deep(.el-input) {
  font-size: 13px;
}

.flow-card-table :deep(.el-input__wrapper) {
  padding: 2px 8px;
  box-shadow: none;
  border: 1px solid #dcdfe6;
}

.flow-diagram-area {
  padding: 20px;
  background: #fafafa;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  min-height: 140px;
}

/* 工序列表表格 */
.process-list-table {
  border: 1px solid #000;
}

.process-list-table .seq-cell {
  width: 60px;
  text-align: center;
  font-weight: 600;
  font-size: 14px;
  background: #f9f9f9;
}

.process-list-table .content-cell {
  padding: 4px 12px;  /* 垂直内边距从12px减小到4px */
}

.process-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.process-name {
  font-weight: 600;
  font-size: 14px;
  color: #333;
  padding: 4px 0;
}

.process-details :deep(.el-textarea__inner) {
  font-size: 13px;
  border: 1px solid #dcdfe6;
  padding: 8px;
}

.process-row {
  min-height: 32px;  /* 从80px大幅减小到32px */
}

/* 无边框输入框 */
.borderless-input :deep(.el-textarea__inner) {
  border: none !important;
  padding: 4px 8px !important;  /* 从8px减小到4px，减少垂直内边距 */
  font-size: 13px;
  resize: none;
  background: transparent;
  box-shadow: none !important;
  line-height: 1.3 !important;  /* 设置行高为1.3，压缩高度 */
  min-height: 24px !important;  /* 设置最小高度 */
}

.borderless-input :deep(.el-textarea__inner):focus {
  border: 1px solid #dcdfe6 !important;
  background: #fff;
}

/* 去掉section-label */
.flow-card-section.no-label .section-label {
  display: none;
}

/* 打印优化 */
@media print {
  /* 隐藏整个页面的所有元素 */
  * {
    visibility: hidden !important;
  }
  
  /* 只显示流转卡及其子元素 */
  .flow-card-container,
  .flow-card-container * {
    visibility: visible !important;
  }
  
  /* 隐藏所有导航、菜单、头部等 */
  nav,
  header,
  aside,
  .el-aside,
  .el-header,
  .el-menu,
  .sidebar,
  .navbar,
  .layout-header,
  .layout-sidebar,
  .main-sidebar,
  .preset-header,
  .process-list,
  .card-title,
  .card-actions,
  .header-buttons,
  .el-card,
  .el-row,
  .el-col,
  .el-card__body {
    display: none !important;
    visibility: hidden !important;
  }
  
  /* 将流转卡定位到页面顶部 */
  .flow-card-container {
    position: fixed !important;
    left: 0 !important;
    top: 0 !important;
    width: 100% !important;
    border: 1.5pt solid #000;
    padding: 10mm;
    page-break-after: always;
    margin: 0 !important;
    background: white !important;
    z-index: 9999 !important;
  }
  
  /* 页面设置 */
  @page {
    size: A4;
    margin: 10mm;
  }
  
  html,
  body {
    margin: 0 !important;
    padding: 0 !important;
    overflow: visible !important;
  }
  
  .flow-card-title {
    font-size: 18pt;
  }
  
  .flow-card-version {
    font-size: 10pt;
  }
  
  .section-label {
    font-size: 10pt;
    page-break-after: avoid;
  }
  
  .flow-card-table {
    border: 1pt solid #000;
    page-break-inside: avoid;
  }
  
  .flow-card-table td,
  .flow-card-table th {
    border: 0.5pt solid #000;
    padding: 2pt 4pt;
    font-size: 9pt;
  }
  
  .label-cell {
    font-size: 8pt !important;
    background: #f9f9f9 !important;
    -webkit-print-color-adjust: exact;
    print-color-adjust: exact;
  }
  
  /* 输入框打印优化 */
  .flow-card-table :deep(.el-input__wrapper),
  .flow-card-table :deep(.el-textarea__inner) {
    box-shadow: none !important;
    background: transparent !important;
    border: none !important;
    padding: 0 !important;
  }
  
  .flow-card-table :deep(.el-input__inner) {
    font-size: 8pt !important;
    padding: 0 !important;
  }
  
  .flow-card-table :deep(.el-textarea__inner) {
    font-size: 8pt !important;
    padding: 2pt !important;
    line-height: 1.3 !important;
  }
  
  /* 二维码打印优化 */
  .qrcode-wrapper {
    gap: 8pt;
  }
  
  .qrcode-text {
    font-size: 10pt;
    letter-spacing: 3pt;
  }
  
  /* 流程图打印优化 */
  .flow-diagram-area svg {
    max-height: 100pt;
  }
  
  /* 工序表格打印优化 */
  .process-list-table {
    page-break-inside: auto;
  }
  
  .process-list-table tr {
    page-break-inside: avoid;
    page-break-after: auto;
  }
  
  .seq-cell {
    width: 30pt;
    text-align: center;
    font-size: 9pt;
  }
  
  /* 隐藏所有输入框的后缀和前缀图标 */
  :deep(.el-input__suffix),
  :deep(.el-input__prefix),
  :deep(.el-textarea__suffix) {
    display: none !important;
  }
}
</style>
