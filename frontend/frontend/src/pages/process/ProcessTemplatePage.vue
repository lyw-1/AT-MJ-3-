<template>
  <div class="process-template-wrap">
    <!-- 打印预览模态框 -->
    <el-dialog
      v-model="printPreviewVisible"
      title="加工工序汇总预览"
      width="90%"
      top="5vh"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="print-preview-container">
        <div class="print-preview-header">
          <el-button type="primary" @click="handlePrint">
            <el-icon><Printer /></el-icon> 打印
          </el-button>
          <el-button @click="printPreviewVisible = false">
            <el-icon><Close /></el-icon> 关闭
          </el-button>
        </div>
        <div class="print-preview-content">
          <div class="a4-paper" ref="printContent">
            <div class="preview-title">
              <h1>模具加工工艺流转卡</h1>
              <div class="preview-subtitle">V2.0</div>
            </div>
            
            <!-- 模具基本信息表格 -->
            <div class="preview-section">
              <table class="preview-table flow-card-table">
                <tbody>
                  <!-- 模具基本信息标题行 -->
                  <tr>
                    <td colspan="8" class="section-header">模具基本信息</td>
                  </tr>
                  
                  <!-- 第一行：负责人和模具刻字 -->
                  <tr>
                    <td class="field-name">负责人:</td>
                    <td class="field-value fillable">{{ moldInfo.responsiblePerson }}</td>
                    <td class="field-name">模具刻字:</td>
                    <td class="field-value fillable" colspan="5">{{ moldInfo.moldEngraving }}</td>
                  </tr>
                  
                  <!-- 第二行：模具编号、成品规格、材料、硬度 -->
                  <tr>
                    <td class="field-name">模具编号:</td>
                    <td class="field-value fillable">{{ moldInfo.moldNumber }}</td>
                    <td class="field-name">成品规格:</td>
                    <td class="field-value fillable">{{ moldInfo.productSpec }}</td>
                    <td class="field-name">材料:</td>
                    <td class="field-value fillable">{{ moldInfo.material }}</td>
                    <td class="field-name">硬度:</td>
                    <td class="field-value fillable">{{ moldInfo.hardness }}</td>
                  </tr>
                  
                  <!-- 第三行：模具规格和定位孔中心距 -->
                  <tr>
                    <td class="field-name">模具规格:</td>
                    <td class="field-value fillable" colspan="5">{{ moldInfo.moldSpec }}</td>
                    <td class="field-name">定位孔中心距:</td>
                    <td class="field-value fillable">{{ moldInfo.positioningHoleDistance }}</td>
                  </tr>
                  
                  <!-- 第四行：模具厚度、进泥孔直径、槽宽、槽间距 -->
                  <tr>
                    <td class="field-name">模具厚度:</td>
                    <td class="field-value fillable">{{ moldInfo.moldThickness }}</td>
                    <td class="field-name">进泥孔直径:</td>
                    <td class="field-value fillable">{{ moldInfo.mudInletDiameter }}</td>
                    <td class="field-name">槽宽:</td>
                    <td class="field-value fillable">{{ moldInfo.slotWidth }}</td>
                    <td class="field-name">槽间距:</td>
                    <td class="field-value fillable">{{ moldInfo.slotSpacing }}</td>
                  </tr>
                  
                  <!-- 工序路线图标题 -->
                  <tr>
                    <td colspan="8" class="section-header">工序路线图</td>
                  </tr>
                  
                  <!-- 工序路线图 -->
                  <tr>
                    <td colspan="8" class="process-flow-section">
                      <div class="process-flow-diagram">
                        <svg width="100%" height="100" viewBox="0 0 840 100" preserveAspectRatio="xMidYMid meet">
                          <defs>
                            <marker id="arrowhead" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto">
                              <polygon points="0 0, 8 3, 0 6" fill="#1971c2" />
                            </marker>
                          </defs>
                          <g stroke="#1971c2" stroke-width="1.5" fill="none">
                            <!-- 动态生成节点 -->
                            <rect 
                              v-for="node in flowChartNodes" 
                              :key="node.index"
                              :x="node.x" 
                              :y="node.y" 
                              :width="node.width" 
                              :height="node.height" 
                              rx="6" 
                              ry="6" 
                              fill="white" 
                            />
                            
                            <!-- 动态生成箭头 -->
                            <line 
                              v-for="(arrow, idx) in flowChartArrows" 
                              :key="'arrow-' + idx"
                              :x1="arrow.x1" 
                              :y1="arrow.y1" 
                              :x2="arrow.x2" 
                              :y2="arrow.y2" 
                              marker-end="url(#arrowhead)" 
                            />
                          </g>
                        </svg>
                      </div>
                    </td>
                  </tr>
                  
                  <!-- 工序列表行 (1-14) -->
                  <tr v-for="i in 14" :key="i">
                    <td class="process-number">{{ i }}</td>
                    <td class="process-content fillable" colspan="7">
                      <template v-if="processList[i - 1]">
                        {{ processList[i - 1].processName }}
                        <span v-if="processList[i - 1].equipment" class="process-equipment">
                          （设备: {{ processList[i - 1].equipment }}）
                        </span>
                        <span v-if="processList[i - 1].responsiblePerson" class="process-person">
                          - 负责人: {{ processList[i - 1].responsiblePerson }}
                        </span>
                      </template>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <div class="template-header">
      <div class="header-buttons">
        <el-button type="primary" @click="saveTemplate" :loading="saving">保存模板</el-button>
      </div>
    </div>
    
    <el-row :gutter="20">
      <!-- 左侧工序列表 -->
      <el-col :span="8">
        <el-card>
          <div class="left-card-title">工序模板列表</div>

          <div class="template-list">
            <el-skeleton v-if="loadingTemplates" :rows="5" animated :throttle="0">
              <template #template>
                <div class="template-skeleton">
                  <div class="template-skeleton-title"></div>
                </div>
              </template>
            </el-skeleton>
            <div v-else class="process-template-list">
              <div 
                v-for="template in templates" 
                :key="template.id"
                class="process-template-item"
                :class="{ 'selected': selectedTemplate?.id === template.id }"
              >
                <div class="template-item-content" @click="selectTemplate(template)">
                  {{ template.name }}
                </div>
                <!-- 只为加工工序汇总添加预览按钮 -->
                <div v-if="template.code === 'TEMPLATE_PROCESS_SUMMARY'" class="template-item-actions">
                  <el-button 
                    type="primary" 
                    size="small" 
                    @click.stop="openPrintPreview"
                  >
                    预览
                  </el-button>
                </div>
              </div>
            </div>
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
        <transition name="fade" mode="out-in">
          <el-card v-if="selectedTemplate" :key="selectedTemplate.id" class="template-config-card">
            <div class="card-title-container">
              <div class="card-title">{{ selectedTemplate.name }}</div>
              <div class="card-subtitle">模板配置</div>
            </div>
            
            <!-- 加载状态指示器 -->
            <el-skeleton v-if="loadingTemplateDetail" :rows="10" animated :throttle="0" style="padding: 0 20px;">
              <template #template>
                <div class="template-detail-skeleton">
                  <div class="template-detail-skeleton-header">
                    <div class="template-detail-skeleton-title"></div>
                  </div>
                  <div class="template-detail-skeleton-content">
                    <div class="template-detail-skeleton-row">
                      <div class="template-detail-skeleton-label"></div>
                      <div class="template-detail-skeleton-input"></div>
                    </div>
                    <div class="template-detail-skeleton-row">
                      <div class="template-detail-skeleton-label"></div>
                      <div class="template-detail-skeleton-input"></div>
                    </div>
                    <div class="template-detail-skeleton-row">
                      <div class="template-detail-skeleton-label"></div>
                      <div class="template-detail-skeleton-textarea"></div>
                    </div>
                  </div>
                </div>
              </template>
            </el-skeleton>
            
            <!-- 模板配置内容 -->
            <template v-else>
              <!-- 模板基本信息 -->
              <div class="template-basic-info card-section">
                <div class="section-header">
                  <el-icon class="section-icon"><InfoFilled /></el-icon>
                  <h3 class="section-title">基本信息</h3>
                </div>
                <el-form :model="selectedTemplate" label-width="140px" class="template-info-form">
                  <el-row :gutter="24">
                    <el-col :span="12">
                      <el-form-item label="模板名称" prop="name" :rules="[{ required: true, message: '请输入模板名称' }]">
                        <el-input v-model="selectedTemplate.name" placeholder="请输入模板名称" size="large" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="适用工序" prop="processCode" :rules="[{ required: true, message: '请选择适用工序' }]">
                        <el-select v-model="selectedTemplate.processCode" placeholder="请选择适用工序" style="width: 100%" size="large">
                          <el-option
                            v-for="process in processes"
                            :key="process.code"
                            :label="process.name"
                            :value="process.code"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-form-item label="模板描述">
                    <el-input 
                      v-model="selectedTemplate.description" 
                      placeholder="请输入模板描述" 
                      type="textarea" 
                      :rows="3"
                      size="large"
                    />
                  </el-form-item>
                  <el-form-item label="是否启用" prop="isEnabled">
                    <el-switch v-model="selectedTemplate.isEnabled" size="large" />
                  </el-form-item>
                </el-form>
              </div>
              
              <!-- 模板配置字段 -->
              <div class="template-config-fields card-section">
                <div class="section-header">
                  <el-icon class="section-icon"><Setting /></el-icon>
                  <h3 class="section-title">工序配置</h3>
                </div>
                
                <!-- 备料工序和加工工序汇总 - 表格形式 -->
                <div v-if="selectedTemplate.processCode === 'PREP' || selectedTemplate.category === 'SUMMARY'">
                  <!-- 模具基本信息模板 -->
                  <div class="table-section prep-section">
                    <div class="subsection-header">
                      <el-icon class="subsection-icon"><Metal /></el-icon>
                      <h4 class="subsection-title">模具基本信息</h4>
                    </div>
                    <div class="table-container">
                      <table class="basic-info-table">
                        <tr>
                          <th>负责人:</th>
                          <td><el-input v-model="moldInfo.responsiblePerson" placeholder="请输入负责人" size="small" /></td>
                          <th>模具刻字:</th>
                          <td colspan="5"><el-input v-model="moldInfo.moldEngraving" placeholder="请输入模具刻字" size="small" /></td>
                        </tr>
                        <tr>
                          <th>模具编号:</th>
                          <td><el-input v-model="moldInfo.moldNumber" placeholder="请输入模具编号" size="small" /></td>
                          <th>成品规格:</th>
                          <td><el-input v-model="moldInfo.productSpec" placeholder="请输入成品规格" size="small" /></td>
                          <th>材料:</th>
                          <td><el-input v-model="moldInfo.material" placeholder="请输入材料" size="small" /></td>
                          <th>硬度:</th>
                          <td><el-input v-model="moldInfo.hardness" placeholder="请输入硬度" size="small" /></td>
                        </tr>
                        <tr>
                          <th>模具规格:</th>
                          <td><el-input v-model="moldInfo.moldSpec" placeholder="请输入模具规格" size="small" /></td>
                          <th colspan="3">定位孔中心距:</th>
                          <td colspan="3"><el-input v-model="moldInfo.positioningHoleDistance" placeholder="请输入定位孔中心距" size="small" /></td>
                        </tr>
                        <tr>
                          <th>模具厚度:</th>
                          <td><el-input v-model="moldInfo.moldThickness" placeholder="请输入模具厚度" size="small" /></td>
                          <th>进泥孔直径:</th>
                          <td><el-input v-model="moldInfo.mudInletDiameter" placeholder="请输入进泥孔直径" size="small" /></td>
                          <th>槽宽:</th>
                          <td><el-input v-model="moldInfo.slotWidth" placeholder="请输入槽宽" size="small" /></td>
                          <th>槽间距:</th>
                          <td><el-input v-model="moldInfo.slotSpacing" placeholder="请输入槽间距" size="small" /></td>
                        </tr>
                      </table>
                    </div>
                  </div>
                  
                  <!-- 加工工序汇总特有：工序路线图 -->
                  <div v-if="selectedTemplate.category === 'SUMMARY'" class="table-section prep-section">
                    <div class="subsection-header">
                      <el-icon class="subsection-icon"><Connection /></el-icon>
                      <h4 class="subsection-title">工序路线图</h4>
                    </div>
                    <div class="table-container">
                      <div class="process-flow-diagram" style="padding: 20px; background: white;">
                        <svg width="100%" height="100" viewBox="0 0 840 100" preserveAspectRatio="xMidYMid meet">
                          <defs>
                            <marker id="arrowhead-edit" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto">
                              <polygon points="0 0, 8 3, 0 6" fill="#1971c2" />
                            </marker>
                          </defs>
                          <g stroke="#1971c2" stroke-width="1.5" fill="none">
                            <!-- 动态生成节点 -->
                            <rect 
                              v-for="node in flowChartNodes" 
                              :key="node.index"
                              :x="node.x" 
                              :y="node.y" 
                              :width="node.width" 
                              :height="node.height" 
                              rx="6" 
                              ry="6" 
                              fill="white" 
                            />
                            
                            <!-- 动态生成箭头 -->
                            <line 
                              v-for="(arrow, idx) in flowChartArrows" 
                              :key="'arrow-edit-' + idx"
                              :x1="arrow.x1" 
                              :y1="arrow.y1" 
                              :x2="arrow.x2" 
                              :y2="arrow.y2" 
                              marker-end="url(#arrowhead-edit)" 
                            />
                          </g>
                        </svg>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 工序内容表格 -->
                  <div class="table-section prep-section">
                    <div class="subsection-header">
                      <el-icon class="subsection-icon"><List /></el-icon>
                      <h4 class="subsection-title">工序内容</h4>
                    </div>
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
                            <td><el-input v-model="process.processName" placeholder="请输入工艺名称" size="small" /></td>
                            <td><el-input v-model="process.equipment" placeholder="请输入设备" size="small" /></td>
                            <td><el-input v-model="process.details" placeholder="请输入详细内容" size="small" /></td>
                            <td><el-input v-model="process.responsiblePerson" placeholder="请输入责任人" size="small" /></td>
                            <td><el-date-picker v-model="process.date" type="date" placeholder="选择日期" style="width: 100%" size="small" /></td>
                            <td><el-input v-model="process.remark" placeholder="请输入备注" size="small" /></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
                
                <!-- 其他工序 - 表单形式 -->
                <div v-else class="form-section">
                  <el-form :model="templateForm" label-width="140px" class="template-form">
                    <!-- 动态生成模板配置字段 -->
                    <el-row :gutter="24" style="margin-bottom: 24px;">
                      <el-col :span="12" v-for="(field, index) in templateFields" :key="index">
                        <el-form-item
                          :label="field.label"
                          :prop="field.key"
                          :rules="[{ required: field.required, message: '请输入' + field.label }]"
                        >
                          <!-- 根据字段类型渲染不同的表单控件 -->
                          <el-input
                            v-if="field.type === 'text'"
                            v-model="templateForm[field.key]"
                            :placeholder="'请输入' + field.label"
                            size="large"
                          />
                          <el-input-number
                            v-else-if="field.type === 'number'"
                            v-model="templateForm[field.key]"
                            :min="field.min || 0"
                            :max="field.max || 99999"
                            :step="field.step || 1"
                            :placeholder="'请输入' + field.label"
                            size="large"
                          />
                          <el-select
                            v-else-if="field.type === 'select'"
                            v-model="templateForm[field.key]"
                            :placeholder="'请选择' + field.label"
                            size="large"
                            style="width: 100%"
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
                            size="large"
                          />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    
                    <!-- 自定义模板字段 -->
                    <div class="custom-fields-section">
                      <div class="section-header">
                        <el-icon class="section-icon"><Plus /></el-icon>
                        <h4 class="section-title">自定义字段</h4>
                      </div>
                      <div class="custom-fields">
                        <transition-group name="custom-field" tag="div">
                          <div
                            v-for="(customField, index) in customFields"
                            :key="index"
                            class="custom-field-item"
                          >
                            <el-input
                              v-model="customField.key"
                              placeholder="键名"
                              style="width: 220px; margin-right: 12px"
                              size="large"
                            />
                            <el-input
                              v-model="customField.value"
                              placeholder="值"
                              style="width: 320px; margin-right: 12px"
                              size="large"
                            />
                            <el-button
                              type="danger"
                              size="large"
                              @click="removeCustomField(index)"
                            >
                              <el-icon><Delete /></el-icon> 删除
                            </el-button>
                          </div>
                        </transition-group>
                        <div class="custom-field-actions">
                          <el-button type="primary" size="large" @click="addCustomField">
                            <el-icon><Plus /></el-icon> 添加自定义字段
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </el-form>
                </div>
              </div>
            </template>
          </el-card>
          
          <!-- 无模板选择时的提示 -->
          <div v-else key="empty" class="empty-tip-container">
            <el-empty
              description="请选择或添加一个模板来配置工序参数"
              image="empty"
            >
              <el-button type="primary" @click="addTemplate">
                <el-icon><Plus /></el-icon> 添加模板
              </el-button>
            </el-empty>
          </div>
        </transition>
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
const loadingTemplates = ref(false)
const loadingTemplateDetail = ref(false)

// 打印预览相关
const printPreviewVisible = ref(false)
const printContent = ref<HTMLElement | null>(null)

// 是否从模具初始参数页面跳转过来
const isFromMoldParam = computed(() => {
  return !!route.params.id
})

// 接收的模具基本数据
const receivedMoldData = ref<any>(null)

// 模板列表 - 确保初始值是数组
const templates = ref<any[]>([])

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

// 加载本地模板数据
const loadLocalTemplates = () => {
  // 本地模板数据，与API返回格式保持一致
  const localTemplates = [
    {
      id: 1,
      name: "加工工序汇总",
      code: "TEMPLATE_PROCESS_SUMMARY",
      category: "SUMMARY",
      description: "用于汇总和管理模具加工的所有工序，提供完整的工艺路线视图",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 2,
      name: "备料",
      code: "TEMPLATE_MATERIAL_PREP",
      category: "PREP",
      description: "备料工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 3,
      name: "进泥孔粗加工",
      code: "TEMPLATE_MUD_HOLE_ROUGH",
      category: "DRILL_NON_LAYER",
      description: "进泥孔粗加工工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 4,
      name: "热处理",
      code: "TEMPLATE_HEAT_TREATMENT",
      category: "HEAT_TREATMENT",
      description: "热处理工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 5,
      name: "精磨",
      code: "TEMPLATE_PRECISION_GRINDING",
      category: "GRINDING",
      description: "精磨工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 6,
      name: "进泥孔精加工",
      code: "TEMPLATE_MUD_HOLE_FINISH",
      category: "DRILL_LAYER",
      description: "进泥孔精加工工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 7,
      name: "导料槽加工-中丝",
      code: "TEMPLATE_GUIDE_SLOT_WIRE_CUT",
      category: "GUIDE_SLOT",
      description: "导料槽加工-中丝工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 8,
      name: "导料槽加工-切槽机",
      code: "TEMPLATE_GUIDE_SLOT_CUTTER",
      category: "GUIDE_SLOT",
      description: "导料槽加工-切槽机工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 9,
      name: "导料槽加工-放电",
      code: "TEMPLATE_GUIDE_SLOT_EDM",
      category: "GUIDE_SLOT",
      description: "导料槽加工-放电工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 10,
      name: "外形加工",
      code: "TEMPLATE_SHAPE_ROUGH",
      category: "SHAPE",
      description: "外形加工工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 11,
      name: "斜边放电",
      code: "TEMPLATE_CHAMFER_EDM",
      category: "EDM",
      description: "斜边放电工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 12,
      name: "外形精加工",
      code: "TEMPLATE_SHAPE_FINISH",
      category: "SHAPE",
      description: "外形精加工工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 13,
      name: "模芯台阶加工",
      code: "TEMPLATE_CORE_STEP",
      category: "CORE_STEP",
      description: "模芯台阶加工工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 14,
      name: "自检",
      code: "TEMPLATE_SELF_CHECK",
      category: "SELF_CHECK",
      description: "自检工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 15,
      name: "品质检测",
      code: "TEMPLATE_QUALITY_CHECK",
      category: "QUALITY_CHECK",
      description: "品质检测工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    },
    {
      id: 16,
      name: "入库",
      code: "TEMPLATE_IN_STORAGE",
      category: "IN_STORAGE",
      description: "入库工序模板",
      status: "active",
      fields: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }
  ]
  
  return localTemplates
}

// 加载模板列表
const loadTemplates = async () => {
  loadingTemplates.value = true
  try {
    // 传递空参数，避免不必要的查询条件
    const response = await getProcessTemplates({})
    // 确保templates.value始终是数组
    if (Array.isArray(response) && response.length > 0) {
      templates.value = response
      ElMessage.success('模板列表加载成功')
    } else {
      // 如果API返回为空，加载本地模板
      templates.value = loadLocalTemplates()
      ElMessage.info('使用本地模板数据')
    }
  } catch (error) {
    console.error('加载模板列表失败:', error)
    // API调用失败，加载本地模板
    templates.value = loadLocalTemplates()
    ElMessage.warning('API调用失败，使用本地模板数据')
  } finally {
    loadingTemplates.value = false
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

// 选择模板
const selectTemplate = (template: any) => {
  selectedTemplate.value = template
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
    
    loadingTemplateDetail.value = true
    
    // 初始化数据
    if (selectedTemplate.value.processCode === 'PREP' || selectedTemplate.value.category === 'SUMMARY') {
      // 备料工序和加工工序汇总 - 初始化表格数据
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
      
      // 工序内容列表重置 - 加工工序汇总需要14行，备料只需4行
      const rowCount = selectedTemplate.value.category === 'SUMMARY' ? 14 : 4
      processList.value = Array.from({ length: rowCount }, () => ({
        processName: '',
        equipment: '',
        details: '',
        responsiblePerson: '',
        date: '',
        remark: ''
      }))
      
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
        if (response && response.data) {
          const templateData = response.data
          if (selectedTemplate.value.processCode === 'PREP' || selectedTemplate.value.category === 'SUMMARY') {
            // 备料工序和加工工序汇总 - 填充表格数据
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
            // 其他工序 - 填充表单数据
            templateForm.value = { ...templateData.config || {} }
          }
        }
      }
    } catch (error) {
      // 只有在有模板ID的情况下才记录错误，否则忽略（新增模板）
      if (selectedTemplate.value.id) {
        console.error('加载模板数据失败:', error)
        // 不显示错误信息，避免影响用户体验
        // ElMessage.error('加载模板数据失败')
      }
    } finally {
      loadingTemplateDetail.value = false
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
      code: `TEMPLATE_${Date.now()}`,
      description: selectedTemplate.value.description,
      category: selectedTemplate.value.processCode,
      status: selectedTemplate.value.isEnabled ? 'active' : 'inactive',
      fields: []
    }
    
    if (selectedTemplate.value.processCode === 'PREP' || selectedTemplate.value.category === 'SUMMARY') {
      // 备料工序和加工工序汇总 - 处理表格数据
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

// 打印预览相关方法
const handlePrint = () => {
  window.print()
}

// 动态生成流程图节点
const flowChartNodes = computed(() => {
  // 过滤出有工序名称的项
  const validProcesses = processList.value.filter(p => p.processName && p.processName.trim() !== '')
  
  if (validProcesses.length === 0) {
    return []
  }
  
  const nodes: any[] = []
  const nodeWidth = 85
  const nodeHeight = 35
  const nodeGap = 20
  const startX = 10
  const firstRowY = 15
  const secondRowY = 60
  
  // 最多显示9个节点：第一行7个，第二行2个
  const maxNodes = Math.min(validProcesses.length, 9)
  const firstRowCount = Math.min(maxNodes, 7)
  const secondRowCount = maxNodes > 7 ? Math.min(maxNodes - 7, 2) : 0
  
  // 生成第一行节点
  for (let i = 0; i < firstRowCount; i++) {
    const x = startX + i * (nodeWidth + nodeGap)
    nodes.push({
      index: i,
      x,
      y: firstRowY,
      width: nodeWidth,
      height: nodeHeight,
      row: 1,
      name: validProcesses[i].processName
    })
  }
  
  // 生成第二行节点（从右到左）
  for (let i = 0; i < secondRowCount; i++) {
    // 第二行第一个节点位于第一行最后一个节点下方
    // 第二行第二个节点在其左侧
    const x = startX + (firstRowCount - 1 - i) * (nodeWidth + nodeGap)
    nodes.push({
      index: firstRowCount + i,
      x,
      y: secondRowY,
      width: nodeWidth,
      height: nodeHeight,
      row: 2,
      name: validProcesses[firstRowCount + i].processName
    })
  }
  
  return nodes
})

// 动态生成流程图箭头
const flowChartArrows = computed(() => {
  const nodes = flowChartNodes.value
  if (nodes.length === 0) return []
  
  const arrows: any[] = []
  
  // 第一行的箭头（从左到右）
  const firstRowNodes = nodes.filter(n => n.row === 1)
  for (let i = 0; i < firstRowNodes.length - 1; i++) {
    const from = firstRowNodes[i]
    const to = firstRowNodes[i + 1]
    arrows.push({
      x1: from.x + from.width,
      y1: from.y + from.height / 2,
      x2: to.x,
      y2: to.y + to.height / 2,
      type: 'horizontal'
    })
  }
  
  // 如果有第二行，添加向下的箭头
  const secondRowNodes = nodes.filter(n => n.row === 2)
  if (secondRowNodes.length > 0) {
    const lastFirstRowNode = firstRowNodes[firstRowNodes.length - 1]
    const firstSecondRowNode = secondRowNodes[0]
    
    // 向下的箭头
    arrows.push({
      x1: lastFirstRowNode.x + lastFirstRowNode.width / 2,
      y1: lastFirstRowNode.y + lastFirstRowNode.height,
      x2: firstSecondRowNode.x + firstSecondRowNode.width / 2,
      y2: firstSecondRowNode.y,
      type: 'vertical'
    })
    
    // 第二行的箭头（从右到左）
    if (secondRowNodes.length > 1) {
      arrows.push({
        x1: secondRowNodes[0].x,
        y1: secondRowNodes[0].y + secondRowNodes[0].height / 2,
        x2: secondRowNodes[1].x + secondRowNodes[1].width,
        y2: secondRowNodes[1].y + secondRowNodes[1].height / 2,
        type: 'horizontal'
      })
    }
  }
  
  return arrows
})

// 打开打印预览
const openPrintPreview = () => {
  // 如果当前工序数据为空,填充示例数据
  if (!processList.value || processList.value.length === 0 || !processList.value.some(p => p.processName)) {
    // 初始化14行工序数据
    processList.value = [
      { processName: '备料', equipment: '磨床', details: '材料准备和检查', responsiblePerson: '', date: '', remark: '' },
      { processName: '进泥孔粗加工', equipment: '深孔钻1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '进泥孔精加工', equipment: '深孔钻2号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '热处理', equipment: '热处理设备1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '精磨', equipment: '磨床1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '导料槽加工-中丝', equipment: '中走丝1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '外形加工', equipment: '铣床1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '外形精加工', equipment: '线切割机1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '模芯台阶加工', equipment: '铣床2号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '裸模自检', equipment: '无需设备', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '品质检测', equipment: '检测设备1号', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '入库', equipment: '无需设备', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' },
      { processName: '', equipment: '', details: '', responsiblePerson: '', date: '', remark: '' }
    ]
  }
  
  // 如果模具基本信息为空,填充示例数据
  if (!moldInfo.value || Object.values(moldInfo.value).every(v => !v)) {
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
  }
  
  printPreviewVisible.value = true
}
</script>

<style scoped lang="scss">
.process-template-wrap {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

/* 打印预览样式 */
.print-preview-container {
  background-color: #f5f7fa;
  min-height: 600px;
}

.print-preview-header {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 20px;
}

.print-preview-content {
  padding: 20px;
  display: flex;
  justify-content: center;
  overflow-y: auto;
  max-height: calc(90vh - 120px);
}

.a4-paper {
  width: 210mm;
  min-height: 297mm;
  padding: 15mm 10mm;
  background: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin: 0 auto;
}

.preview-title {
  text-align: center;
  margin: 0 0 10px 0;
  padding: 8px 0;
  background-color: white;
  border-bottom: 1px solid #000;
}

.preview-title h1 {
  font-size: 20px;
  margin: 0;
  color: #000;
  font-weight: bold;
  letter-spacing: 2px;
}

.preview-subtitle {
  font-size: 12px;
  color: #555;
  margin-top: 4px;
}

.preview-section {
  margin: 0;
  padding: 0;
}

/* 流程卡表格样式 */
.flow-card-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #000;
  font-size: 11px;
  font-family: "SimSun", "宋体", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.flow-card-table td {
  border: 1px solid #000;
  padding: 4px 6px;
  vertical-align: middle;
}

.section-header {
  text-align: left;
  background-color: #e9ecef;
  font-weight: bold;
  font-size: 13px;
  padding: 6px 8px !important;
  border: 1px solid #000 !important;
}

.field-name {
  font-weight: bold;
  text-align: left;
  background-color: #f5f7fa;
  width: 80px;
  white-space: nowrap;
  font-size: 11px;
}

.field-value {
  text-align: left;
  padding-left: 8px;
  min-width: 80px;
  min-height: 24px;
  font-size: 11px;
}

.field-value.fillable {
  position: relative;
  padding-bottom: 2px;
  background: linear-gradient(to bottom, transparent 0%, transparent calc(100% - 1px), #1971c2 calc(100% - 1px), #1971c2 100%);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-position: 0 0;
  min-height: 24px;
  font-weight: 500;
  color: #000;
  font-size: 11px;
}

.process-flow-section {
  padding: 10px;
  text-align: center;
}

.process-flow-diagram {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.process-number {
  text-align: center;
  font-weight: bold;
  background-color: #f5f7fa;
  width: 30px;
  font-size: 11px;
}

.process-content {
  text-align: left;
  padding-left: 8px;
  min-height: 24px;
  font-size: 11px;
}

.process-equipment,
.process-person {
  font-size: 10px;
  color: #666;
  margin-left: 6px;
}

.process-equipment {
  color: #1971c2;
}

.process-person {
  color: #2d3748;
}

/* 打印专用样式 */
@media print {
  /* 隐藏不需要打印的元素 */
  .print-preview-header {
    display: none !important;
  }
  
  /* 调整打印样式 */
  .print-preview-content {
    overflow: visible !important;
    background-color: white !important;
    padding: 0 !important;
    margin: 0 !important;
  }
  
  .a4-paper {
    box-shadow: none !important;
    margin: 0 !important;
    background: white !important;
    width: 100% !important;
    min-height: auto !important;
    padding: 10mm 8mm !important;
  }
  
  /* 优化打印边距和分页 */
  @page {
    size: A4 portrait;
    margin: 10mm;
  }
  
  /* 确保表格在一页内显示 */
  table {
    page-break-inside: avoid !important;
  }
  
  /* 确保填空下划线在打印时显示清晰 */
  .field-value.fillable {
    background: linear-gradient(to bottom, transparent 0%, transparent calc(100% - 0.4mm), #1971c2 calc(100% - 0.4mm), #1971c2 100%) !important;
    background-repeat: no-repeat !important;
    background-size: 100% 100% !important;
    -webkit-print-color-adjust: exact !important;
    print-color-adjust: exact !important;
  }
  
  * {
    -webkit-print-color-adjust: exact !important;
    print-color-adjust: exact !important;
  }
  
  /* 打印时调整字体大小 */
  .preview-title h1 {
    font-size: 18px !important;
  }
  
  .flow-card-table {
    font-size: 10px !important;
  }
  
  .field-name,
  .field-value,
  .process-number,
  .process-content {
    font-size: 10px !important;
  }
  
  .process-equipment,
  .process-person {
    font-size: 9px !important;
  }
}

.template-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--el-border-color);
}

.header-buttons {
  display: flex;
  gap: 12px;
}

/* 右侧配置卡片样式 */
.template-config-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--el-border-color-light);
  background: linear-gradient(135deg, var(--el-bg-color), var(--el-color-primary-light-5));
}

.template-config-card:hover {
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.left-card-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  padding: 0;
  background: none;
  text-shadow: none;
  letter-spacing: 0.3px;
}

.card-title-container {
  margin-bottom: 32px;
  padding: 24px 24px 20px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.9), rgba(103, 186, 255, 0.9));
  color: white;
  border-bottom: none;
  border-radius: 8px 8px 0 0;
}

.card-title {
  font-size: 22px;
  font-weight: 700;
  color: rgba(255, 255, 255, 1);
  margin-bottom: 8px;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  letter-spacing: 0.5px;
  opacity: 1;
}

.card-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.95);
  font-weight: 500;
  opacity: 0.95;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 卡片内部区域样式 */
.card-section {
  margin-bottom: 32px;
  padding: 24px;
  background-color: var(--el-bg-color-light);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--el-border-color-light);
}

.card-section:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

/* 区域标题样式 */
.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--el-color-primary-light-5);
  background: linear-gradient(90deg, var(--el-color-primary-light-9), transparent);
  padding-left: 8px;
  border-radius: 4px 0 0 4px;
}

.section-icon {
  font-size: 22px;
  color: var(--el-color-primary);
  margin-right: 12px;
  transition: transform 0.3s ease;
}

.section-header:hover .section-icon {
  transform: scale(1.1) rotate(5deg);
}

.section-title {
  font-size: 19px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  margin: 0;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}

.subsection-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 12px 16px;
  background-color: var(--el-color-primary-light-9);
  border-radius: 8px;
  border-left: 4px solid var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.subsection-icon {
  font-size: 20px;
  color: var(--el-color-primary);
  margin-right: 10px;
  transition: transform 0.3s ease;
}

.subsection-header:hover .subsection-icon {
  transform: scale(1.1);
}

.subsection-title {
  font-size: 17px;
  font-weight: 600;
  margin: 0;
  color: var(--el-text-color-primary);
  letter-spacing: 0.3px;
}

/* 表单样式 */
.template-info-form {
  margin-bottom: 0;
}

.form-section {
  padding: 0;
}

.template-form {
  padding: 0;
}

/* 左侧模板列表样式 */
.template-list {
  margin-bottom: 20px;
  max-height: 650px;
  overflow-y: auto;
  padding-right: 8px;
  scroll-behavior: smooth;
}

.template-list::-webkit-scrollbar {
  width: 6px;
}

.template-list::-webkit-scrollbar-track {
  background: var(--el-bg-color-light);
  border-radius: 3px;
}

.template-list::-webkit-scrollbar-thumb {
  background: var(--el-border-color);
  border-radius: 3px;
  transition: background-color 0.3s ease;
}

.template-list::-webkit-scrollbar-thumb:hover {
  background: var(--el-color-primary-light-5);
}

/* 工序模板列表 */
.process-template-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

/* 工序模板项 */
.process-template-item {
  padding: 12px 16px;
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  color: #333333;
  text-align: left;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-item-content {
  flex: 1;
}

.template-item-actions {
  margin-left: 12px;
  flex-shrink: 0;
}

.process-template-item:hover {
  background-color: #f5f7fa;
  border-color: #c6e2ff;
}

.process-template-item.selected {
  background-color: #e3f2fd;
  border-color: #2196f3;
  color: #2196f3;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(33, 150, 243, 0.15);
}

/* 模板操作按钮 */
.template-actions {
  display: flex;
  gap: 8px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--el-border-color-light);
}

.template-actions .el-button {
  flex: 1;
  border-radius: 4px;
  font-size: 12px;
  padding: 6px 12px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.template-actions .el-button--primary {
  box-shadow: none;
}

.template-actions .el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.template-actions .el-button--danger {
  box-shadow: none;
}

.template-actions .el-button--danger:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

/* 空状态提示 */
.empty-tip-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background-color: var(--el-bg-color-light);
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
}

/* 自定义字段样式 */
.custom-fields-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed var(--el-border-color);
}

.custom-fields {
  margin-top: 16px;
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-light);
}

.custom-field-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  gap: 12px;
  padding: 12px;
  background-color: var(--el-bg-color-light);
  border-radius: 6px;
  transition: all 0.2s ease;
}

.custom-field-item:hover {
  background-color: var(--el-color-primary-light-9);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.custom-field-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

/* 备料工序表格区域 */
.prep-section {
  margin-bottom: 24px;
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-light);
}

/* 表格样式 */
.table-section {
  margin-bottom: 24px;
  
  .table-container {
    width: 100%;
    overflow-x: auto;
    border: 1px solid var(--el-border-color-light);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    margin: 0;
    border-radius: 8px;
  }
  
  /* 模具基本信息表格 */
  .basic-info-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 13px;
    background-color: #fff;
    
    th,
    td {
      border: 1px solid var(--el-border-color-light);
      padding: 12px 10px;
      text-align: left;
      vertical-align: middle;
      min-height: 36px;
    }
    
    th {
      font-weight: 600;
      background-color: var(--el-color-primary-light-9);
      color: var(--el-text-color-primary);
      font-size: 14px;
      white-space: nowrap;
    }
    
    td {
      background-color: #fff;
    }
    
    tr:hover td {
      background-color: var(--el-bg-color-light);
    }
  }
  
  /* 工序内容表格 */
  .process-content-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 13px;
    background-color: #fff;
    
    th,
    td {
      border: 1px solid var(--el-border-color-light);
      padding: 12px 10px;
      text-align: left;
      vertical-align: middle;
      min-height: 36px;
    }
    
    th {
      font-weight: 600;
      background-color: var(--el-color-primary-light-9);
      color: var(--el-text-color-primary);
      font-size: 14px;
      white-space: nowrap;
    }
    
    td {
      background-color: #fff;
    }
    
    th:nth-child(1),
    td:nth-child(1) {
      width: 6%;
      text-align: center;
    }
    
    th:nth-child(2),
    td:nth-child(2) {
      width: 16%;
    }
    
    th:nth-child(3),
    td:nth-child(3) {
      width: 16%;
    }
    
    th:nth-child(4),
    td:nth-child(4) {
      width: 28%;
    }
    
    th:nth-child(5),
    td:nth-child(5) {
      width: 12%;
    }
    
    th:nth-child(6),
    td:nth-child(6) {
      width: 12%;
    }
    
    th:nth-child(7),
    td:nth-child(7) {
      width: 16%;
    }
    
    tr:hover td {
      background-color: var(--el-bg-color-light);
    }
    
    :deep(.el-input) {
      transition: all 0.2s ease;
    }
    
    :deep(.el-input:hover) {
      box-shadow: 0 0 0 2px var(--el-color-primary-light-5);
    }
  }
}

/* 输入框和按钮样式增强 */
:deep(.el-input__wrapper) {
  border-radius: 6px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 2px var(--el-color-primary-light-5);
}

:deep(.el-button--primary) {
  border-radius: 6px;
  transition: all 0.3s ease;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

/* 切换动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* 自定义字段过渡动画 */
.custom-field-enter-active,
.custom-field-leave-active {
  transition: all 0.3s ease;
}

.custom-field-enter-from,
.custom-field-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.custom-field-move {
  transition: transform 0.3s ease;
}

/* 模板列表骨架屏样式 */
.template-skeleton {
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 12px;
}

.template-skeleton-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.template-skeleton-title {
  width: 60%;
  height: 20px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
}

.template-skeleton-status {
  width: 50px;
  height: 20px;
  background-color: var(--el-bg-color-light);
  border-radius: 12px;
}

.template-skeleton-content {
  width: 100%;
  height: 40px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
  margin-bottom: 12px;
}

.template-skeleton-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-skeleton-category {
  width: 80px;
  height: 16px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
}

.template-skeleton-code {
  width: 120px;
  height: 16px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
}

/* 模板详情骨架屏样式 */
.template-detail-skeleton {
  padding: 0 20px;
}

.template-detail-skeleton-header {
  margin-bottom: 20px;
}

.template-detail-skeleton-title {
  width: 30%;
  height: 24px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
  margin-bottom: 8px;
}

.template-detail-skeleton-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.template-detail-skeleton-row {
  display: flex;
  align-items: center;
  gap: 20px;
}

.template-detail-skeleton-label {
  width: 140px;
  height: 20px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
  flex-shrink: 0;
}

.template-detail-skeleton-input {
  flex: 1;
  height: 32px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
}

.template-detail-skeleton-textarea {
  flex: 1;
  height: 80px;
  background-color: var(--el-bg-color-light);
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .el-col {
    &:span-8 {
      width: 100%;
      margin-bottom: 20px;
    }
    
    &:span-16 {
      width: 100%;
    }
  }
  
  .template-list {
    max-height: 400px;
  }
}

@media (max-width: 768px) {
  .process-template-wrap {
    padding: 10px;
  }
  
  .template-info-form .el-col {
    &:span-12 {
      width: 100%;
    }
  }
  
  .custom-field-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .custom-field-item .el-input {
    width: 100% !important;
    margin-right: 0 !important;
  }
}
</style>