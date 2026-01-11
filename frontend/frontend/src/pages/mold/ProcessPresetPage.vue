<template>
  <div class="process-preset-wrap">
    <div class="preset-header">
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
        <!-- 备料工序 - 表格形式 -->
        <el-card v-if="selectedProcess && selectedProcess.code === 'PREP'">
          <div class="card-title">{{ selectedProcess.name }} - 预设置</div>
          
          <!-- 模具基本信息表格 -->
          <div class="table-section">
            <h2 class="section-title">模具基本信息</h2>
            <div class="table-container">
              <table class="basic-info-table">
                <tr>
                  <th>负责人:</th>
                  <td><el-input v-model="moldInfo.responsiblePerson" placeholder="请输入负责人" /></td>
                  <th>模具刻字:</th>
                  <td colspan="5"><el-input v-model="moldInfo.moldEngraving" placeholder="请输入模具刻字" /></td>
                </tr>
                <tr>
                  <th>模具编号:</th>
                  <td><el-input v-model="moldInfo.moldNumber" placeholder="请输入模具编号" /></td>
                  <th>成品规格:</th>
                  <td><el-input v-model="moldInfo.productSpec" placeholder="请输入成品规格" /></td>
                  <th>材料:</th>
                  <td><el-input v-model="moldInfo.material" placeholder="请输入材料" /></td>
                  <th>硬度:</th>
                  <td><el-input v-model="moldInfo.hardness" placeholder="请输入硬度" /></td>
                </tr>
                <tr>
                  <th>模具规格:</th>
                  <td><el-input v-model="moldInfo.moldSpec" placeholder="请输入模具规格" /></td>
                  <th colspan="3">定位孔中心距:</th>
                  <td colspan="3"><el-input v-model="moldInfo.positioningHoleDistance" placeholder="请输入定位孔中心距" /></td>
                </tr>
                <tr>
                  <th>模具厚度:</th>
                  <td><el-input v-model="moldInfo.moldThickness" placeholder="请输入模具厚度" /></td>
                  <th>进泥孔直径:</th>
                  <td><el-input v-model="moldInfo.mudInletDiameter" placeholder="请输入进泥孔直径" /></td>
                  <th>槽宽:</th>
                  <td><el-input v-model="moldInfo.slotWidth" placeholder="请输入槽宽" /></td>
                  <th>槽间距:</th>
                  <td><el-input v-model="moldInfo.slotSpacing" placeholder="请输入槽间距" /></td>
                </tr>
              </table>
            </div>
          </div>
          
          <!-- 工序内容表格 -->
          <div class="table-section">
            <h2 class="section-title">工序内容</h2>
            <div class="table-container">
              <table class="process-content-table">
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
                  <tr v-for="(process, index) in processList" :key="index">
                    <td>{{ index + 1 }}</td>
                    <td><el-input v-model="process.processName" placeholder="请输入工艺名称" /></td>
                    <td><el-input v-model="process.equipment" placeholder="请输入设备" /></td>
                    <td><el-input v-model="process.details" placeholder="请输入详细内容" /></td>
                    <td><el-input v-model="process.responsiblePerson" placeholder="请输入责任人" /></td>
                    <td><el-date-picker v-model="process.date" type="date" placeholder="选择日期" style="width: 100%" /></td>
                    <td><el-input v-model="process.remark" placeholder="请输入备注" /></td>
                  </tr>
                </tbody>
              </table>
            </div>
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
import { getProcessPresets, batchSaveProcessPresets } from '@/api/mold'

const route = useRoute()
const router = useRouter()

// 页面状态
const saving = ref(false)
const moldNumber = ref('')
const specification = ref('')
const owner = ref('')
const moldId = ref<number>(0)

// 工序列表
const processes = ref([
  { code: 'PREP', name: '备料工序' },
  { code: 'DRILL_NON_LAYER', name: '进泥孔粗加工工序' },
  { code: 'DRILL_LAYER', name: '进泥孔精加工工序' },
  { code: 'HEAT_TREATMENT', name: '热处理' },
  { code: 'GUIDE_SLOT', name: '导料槽加工工序' },
  { code: 'SHAPE', name: '外形加工工序' },
  { code: 'SELF_CHECK', name: '裸模自检' },
  { code: 'QUALITY_CHECK', name: '品质检测工序' },
  { code: 'IN_STORAGE', name: '入库' }
])

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
  responsiblePerson: '罗京',
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
  }
})

// 加载模具信息
const loadMoldInfo = async () => {
  // 这里应该调用API获取模具信息，暂时使用模拟数据
  moldNumber.value = 'MJ2025001'
  specification.value = '标准型'
  owner.value = '张三'
}

// 加载工序列表
const loadProcessList = async () => {
  // 这里应该调用API获取该模具的工序列表，暂时使用预设数据
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
    
    ElMessage.success('保存预设置成功')
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
  router.push('/process/material-preparation')
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
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--el-border-color);
}

.meta {
  display: flex;
  gap: 12px;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 12px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
  display: flex;
  align-items: center;
}

.ml-20 {
  margin-left: 20px;
}

.process-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.process-item {
  width: 100%;
  text-align: left;
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
/* 表格样式 */
.table-section {
  margin-bottom: 30px;
  
  .section-title {
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 15px 0;
    padding: 10px 0;
    color: #333;
    border-bottom: 1px solid #eaeaea;
  }
  
  .table-container {
    width: 100%;
    overflow-x: auto;
    border: 1px solid #000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin: 0;
  }
  
  /* 模具基本信息表格 */
  .basic-info-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
    background-color: #fff;
    
    th,
    td {
      border: 1px solid #000;
      padding: 10px 8px;
      text-align: left;
      vertical-align: middle;
      min-height: 36px;
    }
    
    th {
      font-weight: bold;
      background-color: #fafafa;
      color: #333;
    }
    
    td {
      background-color: #fff;
    }
  }
  
  /* 列宽设置 */
  .col-1 { width: 8%; }
  .col-2 { width: 12%; }
  .col-3 { width: 18%; }
  .col-5 { width: 30%; }
  
  /* 工序内容表格 */
  .process-content-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
    background-color: #fff;
    
    th,
    td {
      border: 1px solid #000;
      padding: 10px 8px;
      text-align: left;
      vertical-align: middle;
      min-height: 36px;
    }
    
    th {
      font-weight: bold;
      background-color: #fafafa;
      color: #333;
    }
    
    td {
      background-color: #fff;
    }
    
    th:nth-child(1),
    td:nth-child(1) {
      width: 5%;
      text-align: center;
    }
    
    th:nth-child(2),
    td:nth-child(2) {
      width: 15%;
    }
    
    th:nth-child(3),
    td:nth-child(3) {
      width: 15%;
    }
    
    th:nth-child(4),
    td:nth-child(4) {
      width: 30%;
    }
    
    th:nth-child(5),
    td:nth-child(5) {
      width: 10%;
    }
    
    th:nth-child(6),
    td:nth-child(6) {
      width: 10%;
    }
    
    th:nth-child(7),
    td:nth-child(7) {
      width: 15%;
    }
  }
}

/* 打印样式 */
@media print {
  /* 隐藏不必要的元素 */
  .el-menu, .el-header, .preset-header, .process-list {
    display: none !important;
  }
  
  /* 设置A4纸大小 */
  @page {
    size: A4;
    margin: 1cm;
  }
  
  /* 页面样式 */
  .process-preset-wrap {
    padding: 0;
    background-color: #fff;
    width: 210mm;
    margin: 0 auto;
  }
  
  /* 调整布局，只显示内容区域 */
  .el-row {
    display: block !important;
  }
  
  .el-col {
    width: 100% !important;
  }
  
  /* 表格样式优化 */
  .table-section {
    margin-bottom: 15px;
    
    .section-title {
      font-size: 14px;
      margin-bottom: 10px;
      padding: 5px 0;
    }
    
    .table-container {
      border: 1px solid #000;
      box-shadow: none;
      overflow: visible;
    }
    
    /* 模具基本信息表格 */
    .basic-info-table {
      table-layout: fixed;
      font-size: 12px;
      width: 100%;
      
      th,
      td {
        padding: 5px 4px;
        border: 1px solid #000;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      th {
        background-color: #fff;
      }
    }
    
    /* 工序内容表格 */
    .process-content-table {
      table-layout: fixed;
      font-size: 12px;
      width: 100%;
      
      th,
      td {
        padding: 5px 4px;
        border: 1px solid #000;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      th {
        background-color: #fff;
      }
      
      th:nth-child(1),
      td:nth-child(1) {
        width: 4%;
        text-align: center;
      }
      
      th:nth-child(2),
      td:nth-child(2) {
        width: 14%;
      }
      
      th:nth-child(3),
      td:nth-child(3) {
        width: 14%;
      }
      
      th:nth-child(4),
      td:nth-child(4) {
        width: 28%;
      }
      
      th:nth-child(5),
      td:nth-child(5) {
        width: 10%;
      }
      
      th:nth-child(6),
      td:nth-child(6) {
        width: 10%;
      }
      
      th:nth-child(7),
      td:nth-child(7) {
        width: 14%;
      }
    }
  }
  
  /* 输入框样式优化 */
  .el-input, .el-date-picker {
    width: 100% !important;
    box-shadow: none !important;
    border: none !important;
    padding: 0 !important;
    
    .el-input__inner {
      border: none !important;
      box-shadow: none !important;
      padding: 0 !important;
      font-size: 12px !important;
      width: 100% !important;
      background-color: transparent !important;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
  
  /* 确保内容不超出A4纸 */
  .table-container {
    page-break-inside: avoid;
  }
  
  tr {
    page-break-inside: avoid;
  }
}
</style>
