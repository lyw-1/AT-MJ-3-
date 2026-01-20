<template>
  <div class="template-selector">
    <el-form :model="templateForm" label-position="top" class="template-selector-form">
      <el-form-item label="模板搜索">
        <el-input
          v-model="searchQuery"
          placeholder="请输入模板名称搜索"
          clearable
          prefix-icon="Search"
          style="margin-bottom: 15px;"
        />
      </el-form-item>
      
      <el-form-item label="模板列表">
        <div class="template-list">
          <el-radio-group v-model="selectedTemplate" @change="handleTemplateSelect">
            <el-radio
              v-for="template in filteredTemplates"
              :key="template.id"
              :label="template"
              class="template-item"
            >
              <div class="template-info">
                <div class="template-name">{{ template.name }}</div>
                <div class="template-desc">{{ template.description || '无描述' }}</div>
                <div class="template-meta">
                  <span class="template-category">{{ getProcessName(template.category) }}</span>
                  <el-tag :type="template.status === 'active' ? 'success' : 'danger'" size="small">
                    {{ template.status === 'active' ? '启用' : '禁用' }}
                  </el-tag>
                </div>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
      </el-form-item>
      
      <el-form-item label="应用方式">
        <el-radio-group v-model="applyMode">
          <el-radio label="replace">完全替换</el-radio>
          <el-radio label="merge">合并应用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    
    <div class="template-preview" v-if="selectedTemplate">
      <h4>模板预览</h4>
      <div class="preview-content">
        <p><strong>模板名称：</strong>{{ selectedTemplate.name }}</p>
        <p><strong>模板描述：</strong>{{ selectedTemplate.description }}</p>
        <p><strong>适用工序：</strong>{{ getProcessName(selectedTemplate.category) }}</p>
        <p><strong>状态：</strong>{{ selectedTemplate.status === 'active' ? '启用' : '禁用' }}</p>
        <p><strong>创建时间：</strong>{{ selectedTemplate.createdAt }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { getProcessTemplates } from '@/api/process'

const props = defineProps({
  processCode: {
    type: String,
    required: true
  },
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits<{
  (e: 'select', template: any, applyMode: string): void
  (e: 'cancel'): void
}>()

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

// 模板列表
const templates = ref<any[]>([])

// 搜索查询
const searchQuery = ref('')

// 选中的模板
const selectedTemplate = ref<any>(null)

// 应用方式
const applyMode = ref('replace')

// 模板表单
const templateForm = ref({
  templateId: '',
  processCode: props.processCode
})

// 过滤后的模板列表
const filteredTemplates = computed(() => {
  return templates.value.filter(template => {
    const matchesSearch = template.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesProcessCode = template.category === props.processCode
    return matchesSearch && matchesProcessCode
  })
})

// 获取工序名称
const getProcessName = (code: string) => {
  const process = processes.value.find(p => p.code === code)
  return process ? process.name : code
}

// 加载模板列表
const loadTemplates = async () => {
  try {
    const response = await getProcessTemplates({ category: props.processCode })
    templates.value = response.data || []
  } catch (error) {
    console.error('加载模板列表失败:', error)
  }
}

// 模板选择处理
const handleTemplateSelect = () => {
  if (selectedTemplate.value) {
    emit('select', selectedTemplate.value, applyMode.value)
  }
}



// 监听工序代码变化
watch(() => props.processCode, () => {
  loadTemplates()
  selectedTemplate.value = null
})

// 初始化
onMounted(() => {
  loadTemplates()
})
</script>

<style scoped lang="scss">
.template-selector {
  width: 100%;
  padding: 15px;
}

.template-selector-form {
  margin-bottom: 20px;
}

.template-list {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  padding: 10px;
  background-color: var(--el-bg-color);
}

.template-item {
  margin-bottom: 10px;
  border-bottom: 1px solid var(--el-border-color-light);
  padding-bottom: 10px;
  width: 100%;
}

.template-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
  padding-bottom: 0;
}

.template-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.template-name {
  font-weight: bold;
  font-size: 16px;
  color: var(--el-text-color-primary);
}

.template-desc {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 5px;
}

.template-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.template-category {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
  background-color: var(--el-color-primary-light-9);
  padding: 2px 8px;
  border-radius: 4px;
}

.template-preview {
  background-color: var(--el-bg-color-light);
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  padding: 15px;
  margin-top: 20px;
}

.template-preview h4 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.preview-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.preview-content p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.preview-content strong {
  color: var(--el-text-color-primary);
}
</style>