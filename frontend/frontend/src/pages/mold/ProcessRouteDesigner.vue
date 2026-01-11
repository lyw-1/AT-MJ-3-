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
          <div class="list-title">选择并排序流程步骤</div>
          <div class="step-list">
            <div v-for="s in stepsLeft" :key="s.key" class="step-item" @click="addStep(s)">{{ s.name }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <div class="selected-title">已选工序</div>
          <div class="selected-list">
            <div v-for="(s,idx) in selected" :key="s.uid" class="selected-item">
              <div class="selected-name">{{ idx+1 }}. {{ s.name }}</div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getMoldInitialParamDetail } from '@/api/mold'
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

// 下一步按钮点击事件
const handleNextStep = () => {
  if (selected.value.length === 0) {
    ElMessage.warning('请先选择工序')
    return
  }
  
  // 导航到详细工序设置页面
  router.push(`/mold/process-preset/${moldId}`)
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

// 页面挂载时加载模具详情
onMounted(() => {
  loadMoldDetail()
})
// 所有可选工序
const allSteps = ref([
  { key: '备料', name: '备料' },
  { key: '非分层进泥孔', name: '进泥孔粗加工' },
  { key: '分层进泥孔', name: '进泥孔精加工' },
  { key: '热处理', name: '热处理' },
  { key: '导料槽加工', name: '导料槽加工' },
  { key: '外形加工', name: '外形加工' },
  { key: '裸模自检', name: '裸模自检' },
  { key: '品质检测', name: '品质检测' },
  { key: '入库', name: '入库' }
])

const equipmentOptions: Record<string, string[]> = {
  '备料': ['磨床'],
  '非分层进泥孔': ['深孔钻1号','深孔钻2号','深孔钻3号','深孔钻4号','深孔钻5号','深孔钻6号','精雕机','电火花成型机1号'],
  '分层进泥孔': ['深孔钻1号','深孔钻2号','深孔钻3号','深孔钻4号','深孔钻5号','深孔钻6号','精雕机'],
  '导料槽加工': ['中走丝1号','中走丝2号','中走丝3号','切槽机1号','切槽机2号','切槽机3号','切槽机4号','切槽机5号','切槽机6号','切槽机7号','切槽机8号','切槽机9号','电火花成型机1号','电火花成型机2号','精雕机']
}

const selected = ref<Array<any>>([])

// 计算属性：已选工序的key集合
const selectedKeys = computed(() => {
  return new Set(selected.value.map(item => item.key))
})

// 计算属性：可选工序列表（过滤掉已选工序）
const stepsLeft = computed(() => {
  return allSteps.value.filter(step => !selectedKeys.value.has(step.key))
})

// 计算属性：已选工序的完整信息
const selectedSteps = computed(() => {
  return selected.value.map(item => {
    const originalStep = allSteps.value.find(step => step.key === item.key)
    return {
      ...item,
      ...originalStep
    }
  })
})

// 添加工序
const addStep = (s: any) => {
  // 检查工序是否已存在
  if (selectedKeys.value.has(s.key)) {
    ElMessage.warning('该工序已添加，不能重复选择')
    return
  }
  
  selected.value.push({ uid: Math.random().toString(36).slice(2), key: s.key, name: s.name, equipment: '', operator: '' })
}
const moveUp = (i: number) => {
  if (i<=0) return
  const t = selected.value.splice(i,1)[0]
  selected.value.splice(i-1,0,t)
}
const moveDown = (i: number) => {
  if (i>=selected.value.length-1) return
  const t = selected.value.splice(i,1)[0]
  selected.value.splice(i+1,0,t)
}
const remove = (i: number) => {
  selected.value.splice(i,1)
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
.list-title { font-weight: 600; margin-bottom: 8px; text-align: center; }
.step-list { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; text-align: center; }
.step-item { padding: 8px; border: 1px solid var(--el-border-color-light); border-radius: 8px; cursor: pointer; text-align: center; }
.selected-title { font-weight: 600; margin-bottom: 8px; }
.selected-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid var(--el-border-color-lighter); }
.selected-item > * { text-align: left; }
.selected-name { flex: 0 0 auto; min-width: 120px; }
.selected-item :deep(.el-select),
.selected-item :deep(.el-input) {
  text-align: left;
}
.selected-item :deep(.el-button) {
  text-align: center;
}
</style>
