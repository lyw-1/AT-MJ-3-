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
      <div class="qrcode-container">
        <qrcode-vue
          :value="qrcodeValue"
          :size="80"
          level="H"
        />
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
        <!-- 备料工序 - 表格形式（适合A4打印） -->
        <el-card v-if="selectedProcess && selectedProcess.code === 'PREP'" class="preset-card">
          <div class="card-title">{{ selectedProcess.name }} - 预设置</div>
          
          <!-- 模具基本信息表格 -->
          <div class="print-section">
            <h3 class="print-section-title">模具基本信息</h3>
            <table class="print-table info-table">
              <colgroup>
                <col style="width: 10%">
                <col style="width: 15%">
                <col style="width: 10%">
                <col style="width: 65%">
              </colgroup>
              <tbody>
                <tr>
                  <th>负责人</th>
                  <td><el-input v-model="moldInfo.responsiblePerson" placeholder="请输入" /></td>
                  <th>模具刻字</th>
                  <td><el-input v-model="moldInfo.moldEngraving" placeholder="请输入" /></td>
                </tr>
                <tr>
                  <th>模具编号</th>
                  <td><el-input v-model="moldInfo.moldNumber" placeholder="请输入" /></td>
                  <th>成品规格</th>
                  <td>
                    <el-input v-model="moldInfo.productSpec" placeholder="请输入" style="flex: 2" />
                    <span class="cell-label">材料</span>
                    <el-input v-model="moldInfo.material" placeholder="请输入" style="flex: 1" />
                    <span class="cell-label">硬度</span>
                    <el-input v-model="moldInfo.hardness" placeholder="请输入" style="flex: 1" />
                  </td>
                </tr>
                <tr>
                  <th>定位孔中心距</th>
                  <td><el-input v-model="moldInfo.positioningHoleDistance" placeholder="请输入" /></td>
                  <th>模具厚度</th>
                  <td><el-input v-model="moldInfo.moldThickness" placeholder="请输入" /></td>
                </tr>
                <tr>
                  <th>进泥孔直径</th>
                  <td>
                    <el-input v-model="moldInfo.mudInletDiameter" placeholder="请输入" style="flex: 2" />
                    <span class="cell-label">槽宽</span>
                    <el-input v-model="moldInfo.slotWidth" placeholder="请输入" style="flex: 1" />
                    <span class="cell-label">槽间距</span>
                    <el-input v-model="moldInfo.slotSpacing" placeholder="请输入" style="flex: 1" />
                  </td>
                  <th></th>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <!-- 工序内容表格 -->
          <div class="print-section">
            <h3 class="print-section-title">工序内容</h3>
            <table class="print-table process-table">
              <colgroup>
                <col style="width: 5%">
                <col style="width: 14%">
                <col style="width: 14%">
                <col style="width: 27%">
                <col style="width: 10%">
                <col style="width: 12%">
                <col style="width: 18%">
              </colgroup>
              <thead>
                <tr>
                  <th>序号</th>
                  <th>工艺名称</th>
                  <th>设备</th>
                  <th>详细内容</th>
                  <th>责任人</th>
                  <th>日期</th>
                  <th>备注</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(row, index) in processList" :key="index">
                  <td class="center">{{ index + 1 }}</td>
                  <td><el-input v-model="row.processName" placeholder="请输入" /></td>
                  <td><el-input v-model="row.equipment" placeholder="请输入" /></td>
                  <td><el-input v-model="row.details" placeholder="请输入" /></td>
                  <td><el-input v-model="row.responsiblePerson" placeholder="请输入" /></td>
                  <td><el-date-picker v-model="row.date" type="date" placeholder="选择" format="YYYY-MM-DD" value-format="YYYY-MM-DD" /></td>
                  <td><el-input v-model="row.remark" placeholder="请输入" /></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
        
        <!-- 其他工序 - 表单形式 -->
        <el-card v-else>
          <div class="card-title">{{ selectedProcess ? selectedProcess.name + ' - 预设置' : '请选择工序' }}</div>
          
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProcessPresets, batchSaveProcessPresets, getMoldInitialParamDetail, updateMoldInitialParam } from '@/api/mold'
import QrcodeVue from 'qrcode.vue'

const route = useRoute()
const router = useRouter()

// 页面状态
const saving = ref(false)
const moldNumber = ref('')
const specification = ref('')
const owner = ref('')
const moldId = ref<number>(0)

// 二维码内容 - 生成工序预设置页面的链接
const qrcodeValue = ref('')

// 从路由参数获取工序列表
const getProcessesFromRoute = () => {
  try {
    const processesParam = route.query.processes as string
    if (processesParam) {
      const parsedProcesses = JSON.parse(processesParam)
      // 确保工序名称格式正确
      return parsedProcesses.map((process: any) => ({
        ...process,
        name: process.name
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
  material: '',
  hardness: '',
  positioningHoleDistance: '',
  moldThickness: '',
  mudInletDiameter: '',
  slotWidth: '',
  slotSpacing: ''
})

// 工序内容列表（用于备料工序表格）
const processList = ref([
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' }
])

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
    
    // 工序内容列表重置
    processList.value = [
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' }
    ]
  } else {
    // 其他工序 - 初始化表单数据
    presetFields.value = getFieldsByProcess(selectedProcess.value.code)
    presetForm.value = {}
  }
  
  // 加载已有的预设置
  try {
    // 这里应该调用API获取预设置数据，暂时使用模拟数据
    const presets = await getProcessPresets(moldId.value, selectedProcess.value.code)
    
    if (selectedProcess.value.code === 'PREP') {
      // 备料工序 - 填充表格数据
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
    
    if (selectedProcess.value.code === 'PREP') {
      // 备料工序 - 处理表格数据
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

// 返回列表
const handleBack = () => {
  router.push('/mold/initial-params-list')
}

// 跳转到备料工序表格页面
const goToMaterialPreparation = () => {
  router.push('/mold/process/material-preparation')
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
  padding: 0 16px;
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
    border: 1px solid #d9d9d9;
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
  
  /* 设置A4纸大小 */
  @page {
    size: A4;
    margin: 15mm;
  }
  
  .process-preset-wrap {
    padding: 0;
    background: #fff;
  }
  
  .el-row {
    display: block !important;
  }
  
  .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: none !important;
  }
  
  :deep(.el-card) {
    border: none;
    box-shadow: none;
  }
  
  .card-title {
    font-size: 18px;
    text-align: center;
    margin-bottom: 15px;
    
    &::before {
      display: none;
    }
  }
  
  .print-section {
    padding: 0 0 15px;
  }
  
  .print-section-title {
    font-size: 14px;
    border-bottom: 1px solid #333;
  }
  
  .print-table {
    font-size: 11px;
    
    th, td {
      border: 1px solid #333;
      padding: 6px 8px;
    }
    
    th {
      background: #fff;
    }
  }
  
  /* 输入框在打印时显示为纯文本 */
  .print-table :deep(.el-input__wrapper),
  .print-table :deep(.el-date-editor .el-input__wrapper) {
    box-shadow: none !important;
    background: transparent !important;
    border: none !important;
  }
  
  .print-table :deep(.el-input__suffix),
  .print-table :deep(.el-input__prefix) {
    display: none;
  }
  
  .cell-row {
    gap: 4px;
  }
  
  .cell-label {
    font-size: 11px;
  }
  
  /* 防止表格跨页断开 */
  .print-table {
    page-break-inside: avoid;
  }
  
  tr {
    page-break-inside: avoid;
  }
}
</style>
