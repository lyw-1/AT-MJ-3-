<template>
  <div class="process-template-wrap">
    <div class="template-header">
      <div class="header-buttons">
        <el-button @click="handleBack">
          返回列表
        </el-button>
        <el-button type="primary" @click="saveTemplate" :loading="saving">保存模板</el-button>
      </div>
    </div>
    
    <el-row :gutter="20">
      <!-- 左侧工序列表 -->
      <el-col :span="8">
        <el-card>
          <div class="card-title">工序模板列表</div>
          <div class="template-list">
            <el-radio-group v-model="selectedTemplate" @change="loadTemplate">
              <el-radio-button
                v-for="template in templates"
                :key="template.id"
                :label="template"
                class="template-item"
              >
                {{ template.name }}
              </el-radio-button>
            </el-radio-group>
          </div>
          <div class="template-actions">
            <el-button type="primary" size="small" @click="addTemplate">
              添加模板
            </el-button>
            <el-button type="danger" size="small" @click="deleteTemplate" :disabled="!selectedTemplate">
              删除模板
            </el-button>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧模板配置表单 -->
      <el-col :span="16">
        <el-card v-if="selectedTemplate">
          <div class="card-title">{{ selectedTemplate.name }} - 模板配置</div>
          
          <!-- 模板基本信息 -->
          <div class="template-basic-info">
            <el-form :model="selectedTemplate" label-width="120px" class="template-info-form">
              <el-form-item label="模板名称" prop="name" :rules="[{ required: true, message: '请输入模板名称' }]">
                <el-input v-model="selectedTemplate.name" placeholder="请输入模板名称" />
              </el-form-item>
              <el-form-item label="模板描述">
                <el-input 
                  v-model="selectedTemplate.description" 
                  placeholder="请输入模板描述" 
                  type="textarea" 
                  :rows="3"
                />
              </el-form-item>
              <el-form-item label="适用工序" prop="processCode" :rules="[{ required: true, message: '请选择适用工序' }]">
                <el-select v-model="selectedTemplate.processCode" placeholder="请选择适用工序" style="width: 100%">
                  <el-option
                    v-for="process in processes"
                    :key="process.code"
                    :label="process.name"
                    :value="process.code"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="是否启用" prop="isEnabled">
                <el-switch v-model="selectedTemplate.isEnabled" />
              </el-form-item>
            </el-form>
          </div>
          
          <!-- 模板配置字段 -->
          <div class="template-config-fields">
            <h3 class="section-title">工序配置</h3>
            
            <!-- 备料工序 - 表格形式 -->
            <div v-if="selectedTemplate.processCode === 'PREP'">
              <!-- 模具基本信息模板 -->
              <div class="table-section">
                <h4 class="subsection-title">模具基本信息</h4>
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
                <h4 class="subsection-title">工序内容</h4>
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
            </div>
            
            <!-- 其他工序 - 表单形式 -->
            <el-form v-else :model="templateForm" label-width="120px" class="template-form">
              <!-- 动态生成模板配置字段 -->
              <el-form-item
                v-for="(field, index) in templateFields"
                :key="index"
                :label="field.label"
                :prop="field.key"
                :rules="[{ required: field.required, message: '请输入' + field.label }]"
              >
                <!-- 根据字段类型渲染不同的表单控件 -->
                <el-input
                  v-if="field.type === 'text'"
                  v-model="templateForm[field.key]"
                  :placeholder="'请输入' + field.label"
                />
                <el-input-number
                  v-else-if="field.type === 'number'"
                  v-model="templateForm[field.key]"
                  :min="field.min || 0"
                  :max="field.max || 99999"
                  :step="field.step || 1"
                  :placeholder="'请输入' + field.label"
                />
                <el-select
                  v-else-if="field.type === 'select'"
                  v-model="templateForm[field.key]"
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
                  v-model="templateForm[field.key]"
                />
              </el-form-item>
              
              <!-- 自定义模板字段 -->
              <el-divider />
              <div class="custom-fields">
                <div class="custom-title">自定义字段</div>
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
          </div>
        </el-card>
        
        <!-- 无模板选择时的提示 -->
        <div v-else class="empty-tip">
          请选择或添加一个模板来配置工序参数
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProcessTemplates, getProcessTemplateDetail, createProcessTemplate, updateProcessTemplate, deleteProcessTemplate } from '@/api/process'

const router = useRouter()
const route = useRoute()

// 页面状态
const saving = ref(false)

// 是否从模具初始参数页面跳转过来
const isFromMoldParam = computed(() => {
  return !!route.params.id
})

// 接收的模具基本数据
const receivedMoldData = ref<any>(null)

// 模板列表
const templates = ref([
  { id: 1, name: '标准备料模板', processCode: 'PREP', description: '标准备料工序模板', isEnabled: true },
  { id: 2, name: '钻孔工序模板', processCode: 'DRILL_NON_LAYER', description: '标准钻孔工序模板', isEnabled: true },
  { id: 3, name: '热处理模板', processCode: 'HEAT_TREATMENT', description: '标准热处理工序模板', isEnabled: false }
])

// 选中的模板
const selectedTemplate = ref<any>(null)

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

// 模板配置字段定义（根据工序类型动态变化）
const templateFields = ref<any[]>([])

// 模板表单数据
const templateForm = ref<Record<string, any>>({})

// 自定义字段
const customFields = ref<Array<{ key: string; value: string }>>([])

// 模具基本信息（用于备料工序模板）
const moldInfo = ref({
  responsiblePerson: '',
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

// 工序内容列表（用于备料工序模板）
const processList = ref([
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
  { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' }
])

// 初始化页面
onMounted(() => {
  // 获取路由参数，检查是否从模具初始参数页面跳转过来
  if (isFromMoldParam.value) {
    // 从路由参数中获取模具基本数据
    receivedMoldData.value = route.query.moldData ? JSON.parse(decodeURIComponent(String(route.query.moldData))) : null
    
    // 如果有模具数据，自动填充到模具信息中
    if (receivedMoldData.value) {
      Object.entries(receivedMoldData.value).forEach(([key, value]) => {
        if (moldInfo.value.hasOwnProperty(key)) {
          moldInfo.value[key as keyof typeof moldInfo.value] = value
        }
      })
    }
  }
  
  // 加载模板列表
  loadTemplates()
  // 默认选择第一个模板
  if (templates.value.length > 0) {
    selectedTemplate.value = templates.value[0]
    loadTemplate()
  }
})

// 加载模板列表
const loadTemplates = async () => {
  try {
    const response = await getProcessTemplates()
    templates.value = response || []
  } catch (error) {
    ElMessage.error('加载模板列表失败')
    console.error('加载模板列表失败:', error)
  }
}

// 添加模板
const addTemplate = () => {
  const newTemplate = {
    id: Date.now(),
    name: '新模板',
    processCode: processes.value[0].code,
    description: '',
    isEnabled: true
  }
  templates.value.push(newTemplate)
  selectedTemplate.value = newTemplate
  loadTemplate()
}

// 删除模板
const deleteTemplate = async () => {
  if (!selectedTemplate.value) return
  
  try {
    await deleteProcessTemplate(selectedTemplate.value.id)
    const index = templates.value.findIndex(t => t.id === selectedTemplate.value.id)
    if (index > -1) {
      templates.value.splice(index, 1)
      selectedTemplate.value = templates.value[0] || null
      if (selectedTemplate.value) {
        loadTemplate()
      }
    }
    ElMessage.success('删除模板成功')
  } catch (error) {
    ElMessage.error('删除模板失败')
    console.error('删除模板失败:', error)
  }
}

// 根据工序类型获取对应的模板配置字段
const getFieldsByProcess = (processCode: string) => {
  // 不同工序类型的模板配置字段
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
    'HEAT_TREATMENT': [
      { key: 'temperature', label: '温度', type: 'number', required: true, min: 100, max: 2000, step: 10 },
      { key: 'duration', label: '时长（小时）', type: 'number', required: true, min: 1, max: 100, step: 1 },
      { key: 'coolingMethod', label: '冷却方式', type: 'select', required: true, options: [{ label: '空冷', value: '空冷' }, { label: '水冷', value: '水冷' }, { label: '油冷', value: '油冷' }] }
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

// 加载模板数据
const loadTemplate = async () => {
  if (!selectedTemplate.value) return
  
  // 初始化数据
  if (selectedTemplate.value.processCode === 'PREP') {
    // 备料工序 - 初始化表格数据
    moldInfo.value = {
      responsiblePerson: '',
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
    
    // 如果有接收到模具数据，自动填充
    if (receivedMoldData.value) {
      Object.entries(receivedMoldData.value).forEach(([key, value]) => {
        if (moldInfo.value.hasOwnProperty(key)) {
          moldInfo.value[key as keyof typeof moldInfo.value] = value
        }
      })
    }
  } else {
    // 其他工序 - 初始化表单数据
    templateFields.value = getFieldsByProcess(selectedTemplate.value.processCode)
    templateForm.value = {}
  }
  
  // 加载模板数据
  try {
    if (selectedTemplate.value.id) {
      const response = await getProcessTemplateDetail(selectedTemplate.value.id)
      if (response) {
        if (selectedTemplate.value.processCode === 'PREP') {
          // 备料工序 - 填充表格数据
          Object.entries(response.config || {}).forEach(([key, value]) => {
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
          templateForm.value = { ...response.config || {} }
        }
      }
    }
  } catch (error) {
    // 只有在有模板ID的情况下才显示错误，否则忽略（新增模板）
    if (selectedTemplate.value.id) {
      ElMessage.error('加载模板数据失败')
      console.error('加载模板数据失败:', error)
    }
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

// 保存模板
const saveTemplate = async () => {
  if (!selectedTemplate.value) {
    ElMessage.warning('请选择或添加一个模板')
    return
  }
  
  saving.value = true
  try {
    let templateData: any = {
      name: selectedTemplate.value.name,
      code: selectedTemplate.value.code,
      description: selectedTemplate.value.description,
      category: selectedTemplate.value.processCode,
      status: selectedTemplate.value.isEnabled ? 'active' : 'inactive',
      fields: []
    }
    
    if (selectedTemplate.value.processCode === 'PREP') {
      // 备料工序 - 处理表格数据
      const prepData: any = {}
      
      // 模具基本信息
      Object.entries(moldInfo.value).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          prepData[`mold_${key}`] = String(value)
        }
      })
      
      // 工序内容
      processList.value.forEach((process, index) => {
        Object.entries(process).forEach(([key, value]) => {
          if (value !== null && value !== undefined && value !== '') {
            prepData[`process_${index}_${key}`] = String(value)
          }
        })
      })
      
      templateData.config = prepData
    } else {
      // 其他工序 - 处理表单数据
      templateData.config = { ...templateForm.value }
      
      // 添加自定义字段
      customFields.value.forEach(field => {
        if (field.key && field.value) {
          templateData.config[field.key] = field.value
        }
      })
      
      // 生成模板字段配置
      templateFields.value.forEach(field => {
        templateData.fields.push({
          fieldName: field.key,
          fieldLabel: field.label,
          fieldType: field.type,
          required: field.required,
          defaultValue: templateForm.value[field.key]
        })
      })
    }
    
    // 调用API保存模板
    if (selectedTemplate.value.id) {
      // 更新模板
      await updateProcessTemplate(selectedTemplate.value.id, templateData)
    } else {
      // 创建模板
      await createProcessTemplate(templateData)
    }
    
    ElMessage.success('保存模板成功')
    
    // 重新加载模板列表
    await loadTemplates()
    
    // 如果是从模具初始参数页面过来的，保存成功后返回该页面
    if (isFromMoldParam.value) {
      handleBack()
    }
  } catch (error) {
    ElMessage.error('保存模板失败')
    console.error('保存模板失败:', error)
  } finally {
    saving.value = false
  }
}

// 返回列表
const handleBack = () => {
  if (isFromMoldParam.value) {
    // 从模具初始参数页面过来，返回模具初始参数详情页面
    router.push(`/mold/initial-params/${route.params.id}`)
  } else {
    // 从工序管理过来，返回模板列表页面
    router.push('/mold/process/templates')
  }
}
</script>

<style scoped lang="scss">
.process-template-wrap {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.template-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--el-border-color);
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

.template-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

.template-item {
  width: 100%;
  text-align: left;
}

.template-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin: 20px 0 10px 0;
  color: var(--el-text-color-primary);
}

.subsection-title {
  font-size: 14px;
  font-weight: 600;
  margin: 10px 0;
  color: var(--el-text-color-primary);
}

.template-basic-info {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--el-border-color);
}

.template-info-form {
  margin-bottom: 20px;
}

.template-config-fields {
  margin-top: 20px;
}

.template-form {
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
</style>