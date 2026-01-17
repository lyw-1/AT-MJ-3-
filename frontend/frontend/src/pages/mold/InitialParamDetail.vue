<template>
  <div class="detail-wrap">
    <!-- 操作按钮区域 - 独立的按钮组，确保绝对可见 -->
    <div id="action-buttons" style="
      position: relative;
      z-index: 9999;
      display: flex;
      gap: 12px;
      margin: 10px 0;
      padding: 8px;
      background-color: white;
      border: 1px solid #e4e7ed;
      border-radius: 6px;
      box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
      justify-content: flex-start;
      align-items: center;
      width: 100%;
      box-sizing: border-box;
    ">
      <!-- 明确的按钮定义，确保没有样式冲突 -->
      <el-button 
        id="btn-back" 
        @click="$router.push('/mold/initial-params-list')"
        style="
          display: inline-flex !important;
          visibility: visible !important;
          opacity: 1 !important;
          background-color: #67c23a !important;
          color: white !important;
          border-color: #67c23a !important;
          padding: 8px 16px !important;
          font-size: 14px !important;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04) !important;
        "
      >
        返回列表
      </el-button>
      
      <el-button 
        id="btn-set-route" 
        type="primary" 
        @click="$router.push('/mold/process-route/'+id)"
        style="
          display: inline-flex !important;
          visibility: visible !important;
          opacity: 1 !important;
          background-color: #409eff !important;
          color: white !important;
          border-color: #409eff !important;
          padding: 8px 16px !important;
          font-size: 14px !important;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04) !important;
        "
      >
        设置模具加工工序路线
      </el-button>
      
      <el-button 
        id="btn-edit-save" 
        type="primary" 
        @click="handleSave" 
        :loading="submitLoading"
        style="
          display: inline-flex !important;
          visibility: visible !important;
          opacity: 1 !important;
          background-color: #e6a23c !important;
          color: white !important;
          border-color: #e6a23c !important;
          padding: 8px 16px !important;
          font-size: 14px !important;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04) !important;
        "
      >
        {{ editing ? '保存' : '编辑' }}
      </el-button>
    </div>
    
    <!-- 页面标题区域 -->
    <div class="page-header">
      <h1 id="page-title" class="page-title" tabindex="0">模具初始参数详情</h1>
      <p class="page-subtitle" tabindex="0">查看和编辑模具的初始参数信息</p>
    </div>
    
    <el-card v-loading="loading">
      <!-- 基本信息表格 -->
      <div class="print-section">
        <div class="section-header">
          <h3 class="print-section-title">基本信息</h3>
          <div class="qrcode-container">
            <qrcode-vue
              :value="qrcodeValue"
              :size="100"
              level="H"
            />
          </div>
        </div>
        <table class="print-table info-table">
          <colgroup>
            <col style="width: 15%">
            <col style="width: 35%">
            <col style="width: 15%">
            <col style="width: 35%">
          </colgroup>
          <tr>
            <th>申请编号</th>
            <td>
              <el-input v-if="editing" v-model="formData.applicationNumber" placeholder="请输入申请编号" />
              <span v-else>{{ formData.applicationNumber || '-' }}</span>
            </td>
            <th>成品类别</th>
            <td>
              <el-select 
                v-if="editing"
                v-model="formData.productCategory" 
                placeholder="请选择成品类别" 
                style="width: 100%"
                filterable
              >
                <el-option 
                  v-for="category in productCategories" 
                  :key="category" 
                  :label="category" 
                  :value="category" 
                />
              </el-select>
              <span v-else>{{ formData.productCategory || '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>模号</th>
            <td>
              <el-input v-if="editing" v-model="formData.moldNumber" placeholder="请输入模号" @input="handleMoldNumberInput" />
              <span v-else>{{ formData.moldNumber || '-' }}</span>
            </td>
            <th>成品规格</th>
            <td>
              <el-select 
                v-if="editing"
                v-model="formData.specification" 
                placeholder="请选择或输入成品规格" 
                style="width: 100%" 
                filterable 
                allow-create
                :loading="productLoading"
              >
                <el-option 
                  v-for="spec in filteredProductSpecs" 
                  :key="spec" 
                  :label="spec" 
                  :value="spec" 
                />
              </el-select>
              <span v-else>{{ formData.specification || '-' }}</span>
            </td>
          </tr>
        </table>
      </div>
      
      <!-- 材料与结构表格 -->
      <div class="print-section">
        <h3 class="print-section-title">材料与结构</h3>
        <table class="print-table info-table">
          <colgroup>
            <col style="width: 15%">
            <col style="width: 35%">
            <col style="width: 15%">
            <col style="width: 35%">
          </colgroup>
          <tr>
            <th>模具钢材</th>
            <td>
              <el-select 
                v-if="editing"
                v-model="formData.material" 
                placeholder="请选择模具钢材" 
                style="width: 100%"
                :loading="steelLoading"
              >
                <el-option 
                  v-for="steel in steelMaterials" 
                  :key="steel" 
                  :label="steel" 
                  :value="steel" 
                />
              </el-select>
              <span v-else>{{ formData.material || '-' }}</span>
            </td>
            <th>HRC</th>
            <td>
              <el-input v-if="editing" v-model="formData.hrc" placeholder="请输入HRC值，如：48-52" />
              <span v-else>{{ formData.hrc || '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>结构</th>
            <td>
              <el-select v-if="editing" v-model="formData.structure" placeholder="请选择结构" style="width: 100%">
                <el-option label="斜边模" value="斜边模" />
                <el-option label="直压模" value="直压模" />
                <el-option label="收边模" value="收边模" />
              </el-select>
              <span v-else>{{ formData.structure || '-' }}</span>
            </td>
            <th>总收缩(%)</th>
            <td>
              <el-input-number
                v-if="editing"
                v-model="formData.totalShrinkage"
                :max="100"
                :precision="2"
                placeholder="请输入总收缩（支持负值）"
                style="width: 100%"
              />
              <span v-else>{{ formData.totalShrinkage !== undefined && formData.totalShrinkage !== null ? formData.totalShrinkage : '-' }}</span>
            </td>
          </tr>
        </table>
      </div>
      
      <!-- 尺寸与外观表格 -->
      <div class="print-section">
        <h3 class="print-section-title">尺寸与外观</h3>
        <table class="print-table info-table">
          <colgroup>
            <col style="width: 15%">
            <col style="width: 35%">
            <col style="width: 15%">
            <col style="width: 35%">
          </colgroup>
          <tr>
            <th>模芯尺寸</th>
            <td>
              <el-input v-if="editing" v-model="formData.coreSize" placeholder="请输入模芯尺寸" />
              <span v-else>{{ formData.coreSize || '-' }}</span>
            </td>
            <th>外形</th>
            <td>
              <el-input v-if="editing" v-model="formData.appearance" placeholder="请输入外形" />
              <span v-else>{{ formData.appearance || '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>定位孔距</th>
            <td>
              <el-input v-if="editing" v-model="formData.positioningHoleDistance" placeholder="请输入定位孔距" />
              <span v-else>{{ formData.positioningHoleDistance || '-' }}</span>
            </td>
            <th>模芯台阶</th>
            <td>
              <el-input v-if="editing" v-model="formData.coreStep" placeholder="请输入模芯台阶" />
              <span v-else>{{ formData.coreStep || '-' }}</span>
            </td>
          </tr>
        </table>
      </div>
      
      <!-- 孔槽参数表格 -->
      <div class="print-section">
        <h3 class="print-section-title">孔槽参数</h3>
        <table class="print-table info-table">
          <colgroup>
            <col style="width: 15%">
            <col style="width: 35%">
            <col style="width: 15%">
            <col style="width: 35%">
          </colgroup>
          <tr>
            <th>进泥孔径</th>
            <td>
              <el-input-number
                v-if="editing"
                v-model="formData.inletHoleDiameter"
                :min="0"
                :precision="3"
                placeholder="请输入进泥孔径"
                style="width: 100%"
              />
              <span v-else>{{ formData.inletHoleDiameter !== undefined && formData.inletHoleDiameter !== null ? formData.inletHoleDiameter : '-' }}</span>
            </td>
            <th>孔数</th>
            <td>
              <el-input-number
                v-if="editing"
                v-model="formData.holeCount"
                :min="0"
                :max="10000"
                placeholder="请输入孔数"
                style="width: 100%"
              />
              <span v-else>{{ formData.holeCount !== undefined && formData.holeCount !== null ? formData.holeCount : '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>孔深</th>
            <td>
              <el-input v-if="editing" v-model="formData.holeDepth" placeholder="请输入孔深" />
              <span v-else>{{ formData.holeDepth || '-' }}</span>
            </td>
            <th>间孔或全孔</th>
            <td>
              <el-radio-group v-if="editing" v-model="formData.holeType">
                <el-radio value="间孔">间孔</el-radio>
                <el-radio value="全孔">全孔</el-radio>
              </el-radio-group>
              <span v-else>{{ formData.holeType || '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>槽宽</th>
            <td>
              <el-input-number
                v-if="editing"
                v-model="formData.slotWidth"
                :min="0.001"
                :precision="3"
                placeholder="请输入槽宽"
                style="width: 100%"
              />
              <span v-else>{{ formData.slotWidth !== undefined && formData.slotWidth !== null ? formData.slotWidth : '-' }}</span>
            </td>
            <th>槽深</th>
            <td>
              <el-input v-if="editing" v-model="formData.slotDepth" placeholder="请输入槽深" />
              <span v-else>{{ formData.slotDepth || '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>切入量</th>
            <td>
              <el-input v-if="editing" v-model="formData.cutAmount" placeholder="请输入切入量" />
              <span v-else>{{ formData.cutAmount || '-' }}</span>
            </td>
            <th>中心距</th>
            <td>
              <el-input-number
                v-if="editing"
                v-model="formData.centerDistance"
                :min="0"
                :precision="3"
                placeholder="请输入中心距"
                style="width: 100%"
              />
              <span v-else>{{ formData.centerDistance !== undefined && formData.centerDistance !== null ? formData.centerDistance : '-' }}</span>
            </td>
          </tr>
          <tr>
            <th>供泥比</th>
            <td colspan="3">
              <el-input v-if="editing" v-model="formData.mudSupplyRatio" placeholder="请输入供泥比" :disabled="true" />
              <div v-else class="form-hint">
                <div class="formula-container">
                  <el-button 
                    type="text" 
                    @click="toggleFormula" 
                    class="formula-toggle-btn"
                  >
                    {{ showFormula ? '收起计算公式' : '查看计算公式' }}
                    <el-icon>{{ showFormula ? 'ArrowUp' : 'ArrowDown' }}</el-icon>
                  </el-button>
                  <div v-if="showFormula" class="formula-explanation expanded">
                    <h3>计算公式：</h3>
                    <div class="formula-math">
                      <p v-if="formData.holeType === '间孔'">
                        供泥比 = <span class="formula-numerator">进泥孔径的面积</span> ÷ <span class="formula-denominator">(2 × (中心距² - (中心距 - 槽宽)²))</span>
                      </p>
                      <p v-else>
                        供泥比 = <span class="formula-numerator">进泥孔径的面积</span> ÷ <span class="formula-denominator">(中心距² - (中心距 - 槽宽)²)</span>
                      </p>
                      <p class="formula-note">
                        <small>说明：进泥孔径的面积 = π × (进泥孔径 ÷ 2)²</small>
                      </p>
                    </div>
                    <div class="formula-variables">
                      <h4>变量说明：</h4>
                      <ul>
                        <li><strong>进泥孔径</strong>：{{ formData.inletHoleDiameter || 0 }}</li>
                        <li><strong>中心距</strong>：{{ formData.centerDistance || 0 }}</li>
                        <li><strong>槽宽</strong>：{{ formData.slotWidth || 0 }}</li>
                        <li><strong>孔类型</strong>：{{ formData.holeType }}</li>
                      </ul>
                    </div>
                  </div>
                </div>
                <span v-if="!showFormula">{{ formData.mudSupplyRatio || '-' }}</span>
                </div>
              </td>
            </tr>
        </table>
      </div>
      
      <!-- 其他信息表格 -->
      <div class="print-section">
        <h3 class="print-section-title">其他信息</h3>
        <table class="print-table info-table">
          <colgroup>
            <col style="width: 15%">
            <col style="width: 35%">
            <col style="width: 15%">
            <col style="width: 35%">
          </colgroup>
          <tr>
            <th>负责人</th>
            <td>
              <el-input 
                v-if="editing"
                v-model="formData.responsiblePerson" 
                style="width: 100%"
                placeholder="请输入负责人"
                clearable
              >
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
              <span v-else>{{ formData.responsiblePerson || '-' }}</span>
            </td>
            <th>状态</th>
            <td>
              <el-tag v-if="formData.status === 'active'" type="success">启用</el-tag>
              <el-tag v-else-if="formData.status === 'inactive'" type="info">停用</el-tag>
              <el-tag v-else type="warning">未知</el-tag>
            </td>
          </tr>
          <tr>
            <th>备注</th>
            <td colspan="3">
              <el-input
                v-if="editing"
                v-model="formData.remarks"
                type="textarea"
                :rows="4"
                placeholder="请输入备注信息"
                resize="vertical"
              />
              <span v-else>{{ formData.remarks || '-' }}</span>
            </td>
          </tr>
        </table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getMoldInitialParamDetail, updateMoldInitialParam } from '@/api/mold'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import QrcodeVue from 'qrcode.vue'

// 引入成品相关API和类型
import { getProducts } from '@/api/product'
import type { Product } from '@/api/product'

// 引入耗材相关API和类型
import { getConsumables } from '@/api/consumable'
import type { ConsumableItem } from '@/api/consumable'

const route = useRoute()
const id = Number(route.params.id || 0)
const editing = ref(false)
const loading = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 控制计算公式显示
const showFormula = ref(false)
const toggleFormula = () => {
  showFormula.value = !showFormula.value
}

// 表单数据
const formData = reactive({
  id: id,
  applicationNumber: '', // 申请编号(唯一值)
  productCategory: '', // 成品类别(下拉选择)
  moldNumber: '', // 模号(唯一值)
  specification: '', // 成品规格(可输入，可下拉选择)
  material: '', // 模具钢材(下拉选择)
  hrc: '', // HRC
  structure: '斜边模', // 结构(下拉选择：斜边模、直压模、收边模)
  totalShrinkage: 0, // 总收缩(%)
  coreSize: '', // 模芯尺寸
  appearance: '', // 外形
  positioningHoleDistance: '', // 定位孔距
  inletHoleDiameter: 0, // 进泥孔径
  holeCount: 0, // 孔数
  holeDepth: '', // 孔深
  holeType: '间孔', // 间孔或全孔
  slotWidth: 0, // 槽宽
  slotDepth: '', // 槽深
  cutAmount: '', // 切入量
  centerDistance: 0, // 中心距
  mudSupplyRatio: '', // 供泥比
  coreStep: '', // 模芯台阶
  responsiblePerson: '', // 负责人(下拉选择，筛选模具加工组员工角色)
  remarks: '', // 备注(多行文字属性)
  status: 'active' // 状态
})

// 二维码内容 - 生成模具详情页面的链接
const qrcodeValue = computed(() => {
  return `${window.location.origin}/mold/initial-params-detail/${formData.id}`
})

// 成品相关数据
const products = ref<Product[]>([])
const productCategories = ref<string[]>([])
const productSpecs = ref<string[]>([])
const productLoading = ref(false)

// 耗材相关数据
const consumables = ref<ConsumableItem[]>([])
const steelMaterials = ref<string[]>([])
const steelLoading = ref(false)

// 根据选中的成品类别过滤出对应的成品规格
const filteredProductSpecs = computed(() => {
  if (!formData.productCategory) {
    return productSpecs.value
  }
  return products.value
    .filter(product => product.productCategory === formData.productCategory)
    .map(product => product.productSpec)
    .filter((spec, index, self) => self.indexOf(spec) === index) // 去重
    .sort()
})

// 计算供泥比相关函数
const calculateInletArea = (diameter: number): number => {
  // 计算进泥孔面积（圆面积公式：πr²）
  const radius = diameter / 2;
  return Math.PI * Math.pow(radius, 2);
};

const calculateMudSupplyRatio = () => {
  // 验证输入值
  const inletDiameter = formData.inletHoleDiameter;
  const slotWidth = formData.slotWidth;
  const centerDistance = formData.centerDistance;
  
  if (!inletDiameter || !slotWidth || !centerDistance) {
    formData.mudSupplyRatio = '';
    return;
  }
  
  // 计算进泥孔面积
  const inletArea = calculateInletArea(inletDiameter);
  
  // 计算分母部分
  const denominator = Math.pow(centerDistance, 2) - Math.pow(centerDistance - slotWidth, 2);
  
  // 根据孔类型计算供泥比
  let mudRatio;
  if (formData.holeType === '间孔') {
    mudRatio = inletArea / (2 * denominator);
  } else {
    mudRatio = inletArea / denominator;
  }
  
  // 返回保留2位小数的结果（四舍五入）
  formData.mudSupplyRatio = mudRatio.toFixed(2);
};

// 监听相关字段变化，自动计算供泥比
watch(() => [formData.inletHoleDiameter, formData.slotWidth, formData.centerDistance, formData.holeType], () => {
  calculateMudSupplyRatio();
}, { deep: true, immediate: true });

// 表单验证规则
const formRules = reactive<FormRules>({
  applicationNumber: [
    { required: true, message: '请输入申请编号', trigger: 'blur' }
  ],
  productCategory: [
    { required: true, message: '请选择成品类别', trigger: 'change' }
  ],
  moldNumber: [
    { required: true, message: '请输入模号', trigger: 'blur' }
  ],
  specification: [
    { required: true, message: '请输入或选择成品规格', trigger: 'blur' }
  ],
  material: [
    { required: true, message: '请选择模具钢材', trigger: 'change' }
  ],
  structure: [
    { required: true, message: '请选择结构', trigger: 'change' }
  ],
  inletHoleDiameter: [
    { required: true, message: '请输入进泥孔径', trigger: 'blur' }
  ],
  slotWidth: [
    { required: true, message: '请输入槽宽', trigger: 'blur' }
  ],
  centerDistance: [
    { required: true, message: '请输入中心距', trigger: 'blur' }
  ],
  responsiblePerson: [
    { required: true, message: '请输入负责人', trigger: 'blur' }
  ]
});

// 加载模具参数详情
const loadMoldParamDetail = async () => {
  loading.value = true
  try {
    console.log('[MoldParamDetail] 开始加载详情数据，ID:', id)
    const response = await getMoldInitialParamDetail(id)
    console.log('[MoldParamDetail] API调用成功，响应数据:', response)
    
    // 统一数据格式，确保字段名与表单绑定一致
    // 注意：API响应拦截器已经直接返回数据，所以直接使用response而不是response.data
    const data = response || {};
    console.log('[MoldParamDetail] 处理后的数据:', data)
    
    // 恢复正确的字段映射，确保API返回的数据能正确赋值给表单字段
    // 这是解决点击编辑按钮后数据被清除的关键
    Object.assign(formData, {
      applicationNumber: data.applicationNo || data.applicationNumber,
      productCategory: data.category || data.productCategory,
      moldNumber: data.moldCode || data.moldNumber,
      specification: data.productSpec || data.specification,
      material: data.material || data.steelMaterial,
      hrc: data.hrc,
      structure: data.structure || '斜边模',
      totalShrinkage: data.totalShrinkage || 0,
      coreSize: data.coreSize,
      appearance: data.outline || data.appearance,
      positioningHoleDistance: data.locationHolePitch || data.positioningHoleDistance,
      inletHoleDiameter: parseFloat(data.inletDiameter) || 0,
      holeCount: data.holeCount || 0,
      holeDepth: data.holeDepth,
      holeType: data.porosityType || data.holeType || '间孔',
      slotWidth: parseFloat(data.slotWidth) || 0,
      slotDepth: data.slotDepth,
      cutAmount: data.cutInAmount || data.cutAmount,
      centerDistance: parseFloat(data.centerDistance) || 0,
      mudSupplyRatio: data.feedRatio || data.mudSupplyRatio,
      coreStep: data.coreStepHeight || data.coreStep,
      responsiblePerson: data.ownerName || data.responsiblePerson || data.owner,
      remarks: data.remark || data.remarks,
      status: data.status || 'active'
    })
    
    console.log('[MoldParamDetail] 加载详情数据成功，表单数据:', formData)
  } catch (error: any) {
    console.error('[MoldParamDetail] 加载详情数据失败:', error)
    console.error('[MoldParamDetail] 错误信息:', error.message)
    console.error('[MoldParamDetail] 错误堆栈:', error.stack)
    ElMessage.error('加载模具参数详情失败')
  } finally {
    loading.value = false
  }
}

// 获取成品数据
const fetchProducts = async () => {
  productLoading.value = true
  try {
    const response = await getProducts()
    // 直接使用response，因为API响应拦截器已经处理过数据
    const productData = Array.isArray(response) ? response : response.records || []
    products.value = productData
    
    // 提取并去重成品类别
    const categories = [...new Set(products.value.map(product => product.productCategory))]
    productCategories.value = categories.sort()
    
    // 提取并去重所有成品规格
    const specs = [...new Set(products.value.map(product => product.productSpec))]
    productSpecs.value = specs.sort()
  } catch (error: any) {
    // 检查是否为401错误，如果是则不显示错误消息
    if (error.response?.status !== 401) {
      ElMessage.error('获取成品数据失败，请稍后重试')
      console.error('获取成品数据失败:', error)
    }
  } finally {
    productLoading.value = false
  }
}

// 获取钢材列表（从耗材中筛选）
const fetchSteelMaterials = async () => {
  steelLoading.value = true
  try {
    // 调用耗材API获取所有耗材数据
    const response = await getConsumables({ page: 1, pageSize: 100 })
    
    // 检查API返回格式，考虑响应拦截器的处理
    let consumableList: ConsumableItem[] = []
    if (Array.isArray(response)) {
      consumableList = response
    } else if (response.records && Array.isArray(response.records)) {
      consumableList = response.records
    } else if (response.data && Array.isArray(response.data)) {
      consumableList = response.data
    }
    
    consumables.value = consumableList
    
    // 筛选出物品名称包含"钢材"关键词的物料
    const steelItems = consumableList.filter(item => {
      return item.itemName && item.itemName.includes('钢材')
    })
    
    // 提取物品名称并去重，排序
    const steelNames = [...new Set(steelItems.map(item => item.itemName))]
    steelMaterials.value = steelNames.sort()
    
    // 添加默认选项（如果没有筛选到钢材）
    if (steelMaterials.value.length === 0) {
      steelMaterials.value = ['模具钢材']
    }
  } catch (error: any) {
    // 检查是否为401错误，如果是则使用默认数据，不显示错误消息
    if (error.response?.status === 401) {
      // 使用默认数据，避免触发重定向
      steelMaterials.value = ['模具钢材']
      console.warn('获取钢材列表API返回401，使用默认数据')
    } else {
      ElMessage.error('获取钢材列表失败，请稍后重试')
      console.error('获取钢材列表失败:', error)
      // 使用默认数据作为后备
      steelMaterials.value = ['模具钢材']
    }
  } finally {
    steelLoading.value = false
  }
}



// 处理模号输入 - 目前仅用于调试，可根据业务需求添加实际处理逻辑
const handleMoldNumberInput = () => {
  // 可以在这里添加模号输入的处理逻辑，如格式验证、实时查询等
  console.log('[调试] 模号输入:', formData.moldNumber)
}

// 处理保存
const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    if (!editing.value) {
      // 切换到编辑模式
      editing.value = true
      // 加载相关数据
      await Promise.all([
        fetchProducts(),
        fetchSteelMaterials()
      ])
      return
    }
    
    // 验证表单
    await formRef.value.validate()
    
    submitLoading.value = true
    
    // 调用API更新模具参数
    await updateMoldInitialParam(formData.id, formData)
    
    ElMessage.success('保存成功')
    editing.value = false
    
    // 重新加载数据，确保数据一致性
    await loadMoldParamDetail()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('保存失败:', error)
      if (error.name === 'ValidationError') {
        ElMessage.error('表单验证失败，请检查输入')
      } else {
        ElMessage.error('保存失败，请稍后重试')
      }
    }
  } finally {
    submitLoading.value = false
  }
}

// 页面挂载时加载数据
onMounted(async () => {
  await loadMoldParamDetail()
})
</script>

<style scoped lang="scss">
.detail-wrap {
  padding: 12px;
  background-color: var(--bg-color-primary);
  min-height: calc(100vh - 84px);
}

// 打印友好的表格样式
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

// 色彩系统定义
:root {
  // 字体系统
  --font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  
  // 字号层级
  --font-size-extra-large: 24px;
  --font-size-large: 20px;
  --font-size-medium: 18px;
  --font-size-base: 16px;
  --font-size-small: 14px;
  --font-size-extra-small: 13px;
  --font-size-mini: 12px;
  
  // 行高
  --line-height-extra-large: 1.8;
  --line-height-large: 1.6;
  --line-height-base: 1.5;
  --line-height-small: 1.4;
  
  // 字重
  --font-weight-bold: 600;
  --font-weight-medium: 500;
  --font-weight-regular: 400;
  
  // 主色
  --primary-color: #409eff;
  --primary-color-light: #66b1ff;
  --primary-color-dark: #3a8ee6;
  
  // 辅助色
  --success-color: #67c23a;
  --warning-color: #e6a23c;
  --danger-color: #f56c6c;
  --info-color: #909399;
  
  // 中性色
  --text-color-primary: #303133;
  --text-color-regular: #606266;
  --text-color-secondary: #909399;
  --text-color-placeholder: #c0c4cc;
  
  // 背景色
  --bg-color-primary: #f5f7fa;
  --bg-color-secondary: #ffffff;
  --bg-color-tertiary: #fafafa;
  
  // 边框色
  --border-color-light: #e4e7ed;
  --border-color-base: #dcdfe6;
  --border-color-dark: #c0c4cc;
  
  // 阴影
  --box-shadow-light: 0 2px 8px rgba(0, 0, 0, 0.08);
  --box-shadow-base: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  --box-shadow-dark: 0 4px 20px rgba(0, 0, 0, 0.15);
}

// 全局字体样式
body {
  font-family: var(--font-family);
  font-size: var(--font-size-base);
  line-height: var(--line-height-base);
  color: var(--text-color-primary);
  background-color: var(--bg-color-primary);
}



// 页面标题样式
.page-header {
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-color-light);
}

.page-title {
  font-size: var(--font-size-medium);
  font-weight: var(--font-weight-bold);
  line-height: var(--line-height-base);
  color: var(--text-color-primary);
  margin: 0 0 2px 0;
}

.page-subtitle {
  font-size: var(--font-size-extra-small);
  line-height: var(--line-height-small);
  color: var(--text-color-regular);
  margin: 0;
}

// 操作按钮样式优化
.detail-actions {
  display: flex !important;
  gap: 12px;
  margin-bottom: 20px;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  height: auto;
  visibility: visible !important;
  opacity: 1 !important;
}

// 主要按钮样式增强
.primary-btn {
  font-weight: var(--font-weight-medium);
  box-shadow: var(--box-shadow-light);
  display: inline-flex !important;
  visibility: visible !important;
  opacity: 1 !important;
}

// 次要按钮样式
.secondary-btn {
  font-weight: var(--font-weight-regular);
  display: inline-flex !important;
  visibility: visible !important;
  opacity: 1 !important;
}



// 响应式设计优化
@media (max-width: 1024px) {
  // 平板设备：调整表单布局
  :deep(.el-row) {
    margin-bottom: 12px;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 12px;
  }
}

@media (max-width: 768px) {
  // 手机设备：单列布局
  .detail-wrap {
    padding: 8px;
  }
  
  .page-header {
    margin-bottom: 12px;
  }
  
  .page-title {
    font-size: 18px;
  }
  
  .page-subtitle {
    font-size: 12px;
  }
  
  .detail-actions {
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
  }
  
  .detail-actions .el-button {
    width: 100%;
  }
  
  :deep(.el-card__body) {
    padding: 8px;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 8px;
  }
  
  :deep(.el-form-item__label) {
    width: 100px;
    font-size: 13px;
    padding: 0 8px 0 0;
  }
  
  :deep(.el-col) {
    width: 100% !important;
    max-width: 100% !important;
    flex: 0 0 100% !important;
  }
  
  .form-section {
    padding: 12px;
    margin-bottom: 16px;
  }
  
  .form-section-title {
    font-size: 14px;
    margin-bottom: 12px;
  }
}

// 优化表单输入框样式
:deep(.el-input__inner),
:deep(.el-select__wrapper),
:deep(.el-input-number__decrease),
:deep(.el-input-number__increase),
:deep(.el-textarea__inner) {
  border-radius: 3px;
  border: 1px solid var(--border-color-base);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: var(--text-color-primary);
  background-color: var(--bg-color-secondary);
}

:deep(.el-input__inner:focus),
:deep(.el-select__wrapper.is-focus),
:deep(.el-textarea__inner:focus) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
  transform: translateY(-1px);
}

// 优化输入框占位符样式
:deep(.el-input__inner::placeholder),
:deep(.el-textarea__inner::placeholder) {
  color: var(--text-color-placeholder);
  transition: color 0.3s ease;
}

// 优化输入框禁用状态
:deep(.el-input.is-disabled .el-input__inner),
:deep(.el-textarea.is-disabled .el-textarea__inner) {
  background-color: var(--bg-color-tertiary);
  color: var(--text-color-placeholder);
  cursor: not-allowed;
}

// 优化单选按钮样式
:deep(.el-radio__input.is-checked .el-radio__inner) {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  transform: scale(1.1);
}

:deep(.el-radio__input .el-radio__inner) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-radio__input:hover .el-radio__inner) {
  border-color: var(--primary-color-light);
}

// 优化单选按钮文本样式
:deep(.el-radio__label) {
  color: var(--text-color-primary);
  transition: color 0.3s ease;
}

:deep(.el-radio:hover .el-radio__label) {
  color: var(--primary-color);
}

// 优化提示图标样式
.hint-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: rgba(64, 158, 255, 0.1);
  color: var(--primary-color);
  text-align: center;
  line-height: 16px;
  margin-right: 6px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform: scale(1);
}

.hint-icon:hover {
  background-color: var(--primary-color);
  color: #fff;
  transform: scale(1.1);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

// 优化按钮交互效果
:deep(.el-button) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 3px;
  font-weight: var(--font-weight-medium);
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.el-button:active) {
  transform: translateY(0);
}

:deep(.el-button--primary) {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

:deep(.el-button--primary:hover) {
  background-color: var(--primary-color-light);
  border-color: var(--primary-color-light);
}

:deep(.el-button--primary:active) {
  background-color: var(--primary-color-dark);
  border-color: var(--primary-color-dark);
}

// 优化加载状态
:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.8);
}

:deep(.el-loading-spinner .path) {
  stroke: var(--primary-color);
}

// 优化选择器选项样式
:deep(.el-select-dropdown__item) {
  transition: all 0.2s ease;
  border-radius: 3px;
}

:deep(.el-select-dropdown__item:hover) {
  background-color: rgba(64, 158, 255, 0.1);
  color: var(--primary-color);
}

:deep(.el-select-dropdown__item.selected) {
  background-color: rgba(64, 158, 255, 0.1);
  color: var(--primary-color);
}

// 优化文本区域自动高度
:deep(.el-textarea.is-auto-size) {
  transition: all 0.3s ease;
}

// 优化卡片阴影效果
:deep(.el-card) {
  transition: all 0.3s ease;
  border-radius: 6px;
  box-shadow: var(--box-shadow-light);
}

:deep(.el-card:hover) {
  box-shadow: var(--box-shadow-base);
}

// 优化表单分组动画
.form-section {
  transition: all 0.3s ease;
}

.form-section:hover {
  box-shadow: var(--box-shadow-base);
  transform: translateY(-1px);
}

// 优化计算公式显示
.formula-container {
  margin-top: 8px;
}

.formula-toggle-btn {
  padding: 4px 0;
  font-size: var(--font-size-extra-small);
  line-height: var(--line-height-small);
  color: var(--primary-color);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.formula-toggle-btn:hover {
  color: var(--primary-color-light);
}

.formula-toggle-btn :deep(.el-icon) {
  font-size: var(--font-size-mini);
  transition: transform 0.3s ease;
}

.formula-explanation {
  margin-top: 8px;
  padding: 10px 12px;
  background-color: rgba(103, 194, 58, 0.05);
  border: 1px solid rgba(103, 194, 58, 0.2);
  border-radius: 6px;
  color: var(--success-color);
  font-size: var(--font-size-extra-small);
  line-height: var(--line-height-base);
  box-shadow: var(--box-shadow-light);
  transition: all 0.3s ease;
  overflow: hidden;
  max-height: 0;
  opacity: 0;
  transform: translateY(-10px);
}

.formula-explanation.expanded {
  max-height: 500px;
  opacity: 1;
  transform: translateY(0);
  padding: 16px;
  margin-top: 8px;
}

.formula-explanation h3 {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
  line-height: var(--line-height-small);
  margin: 0 0 12px 0;
  color: var(--success-color);
}

.formula-explanation h4 {
  font-size: var(--font-size-extra-small);
  font-weight: var(--font-weight-bold);
  line-height: var(--line-height-small);
  margin: 12px 0 8px 0;
  color: var(--success-color);
}

.formula-math {
  font-family: 'Courier New', Courier, monospace;
  background-color: rgba(255,255,255,0.8);
  padding: 12px;
  border-radius: 4px;
  border: 1px solid rgba(103, 194, 58, 0.2);
  margin-bottom: 12px;
}

.formula-math p {
  margin: 8px 0;
  line-height: var(--line-height-large);
}

.formula-numerator, .formula-denominator {
  font-weight: var(--font-weight-bold);
}

.formula-denominator {
  background-color: rgba(255,255,255,0.6);
  padding: 2px 6px;
  border-radius: 3px;
}

.formula-note {
  font-size: var(--font-size-mini);
  line-height: var(--line-height-small);
  color: var(--text-color-secondary);
  margin-top: 8px;
}

.formula-variables {
  background-color: rgba(255,255,255,0.8);
  padding: 12px;
  border-radius: 4px;
  border: 1px solid rgba(103, 194, 58, 0.2);
}

.formula-variables ul {
  list-style-type: disc;
  padding-left: 20px;
  margin: 0;
}

.formula-variables li {
  margin: 4px 0;
  font-size: var(--font-size-mini);
  line-height: var(--line-height-small);
  color: var(--text-color-regular);
}

.formula-variables strong {
  color: var(--success-color);
  font-weight: var(--font-weight-bold);
}

// 二维码容器样式
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.qrcode-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
  background-color: #fff;
  border: 1px solid var(--border-color-light);
  border-radius: 6px;
  box-shadow: var(--box-shadow-light);
}

.qrcode-container :deep(img) {
  display: block;
  border-radius: 4px;
}

// 响应式设计优化
@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .qrcode-container {
    align-self: flex-end;
  }
}
</style>
