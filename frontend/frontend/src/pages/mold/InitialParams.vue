<template>
  <div class="initial-params-container">
    <!-- 页面标题和操作区 -->
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">模具初始参数</h1>
        <p class="page-subtitle">管理和维护模具的基础参数信息</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          创建《模具初始参数》
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
        <el-button @click="checkMoldNumberUniqueness">
          <el-icon><Search /></el-icon>
          检查模号唯一性
        </el-button>
      </div>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="filterForm" :inline="true" size="small">
        <el-form-item label="申请编号">
          <el-input v-model="filterForm.applicationNumber" placeholder="请输入申请编号" clearable />
        </el-form-item>
        <el-form-item label="模号">
          <el-input v-model="filterForm.moldNumber" placeholder="请输入模号" clearable />
        </el-form-item>
        <el-form-item label="成品类别">
          <el-select v-model="filterForm.productCategory" placeholder="请选择成品类别" clearable>
            <el-option
              v-for="category in productCategories"
              :key="category"
              :label="category"
              :value="category"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="filterForm.responsiblePerson" placeholder="请输入负责人" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        @filter-change="handleFilterChange"
        :cell-style="{ textAlign: 'left', verticalAlign: 'middle' }"
        :header-cell-style="{ textAlign: 'left', verticalAlign: 'middle' }"
      >
        <el-table-column type="selection" width="80" align="center" />
        <el-table-column 
          prop="applicationNumber" 
          label="申请编号" 
          width="100"
          align="left"
          filterable
          :filter-method="(value, row) => row.applicationNumber?.includes(value) || false"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="productCategory" 
          label="成品类别" 
          width="100"
          align="left"
          filterable
          :filters="[
            { text: '蜂窝陶瓷载体', value: '蜂窝陶瓷载体' },
            { text: '其他', value: '其他' }
          ]"
          :filter-method="(value, row) => row.productCategory === value"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="moldNumber" 
          label="模号" 
          width="80"
          align="left"
          filterable
          :filter-method="(value, row) => row.moldNumber?.includes(value) || false"
          show-overflow-tooltip
        />
        <el-table-column 
            prop="specification" 
            label="成品规格" 
            width="150"
            align="left"
            filterable
            :filter-method="(value, row) => row.specification?.includes(value) || false"
            show-overflow-tooltip
          />
          <el-table-column 
            prop="totalShrinkage" 
            label="总收缩" 
            width="90" 
            align="left"
            filterable
            :filter-method="(value, row) => {
              if (!value) return true;
              return row.totalShrinkage?.toString().includes(value) || false;
            }"
            show-overflow-tooltip
          />
        <el-table-column 
          prop="responsiblePerson" 
          label="负责人" 
          width="90"
          align="left"
          filterable
          :filter-method="(value, row) => row.responsiblePerson?.includes(value) || false"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="remarks" 
          label="备注" 
          min-width="120"
          align="left"
          filterable
          :filter-method="(value, row) => row.remarks?.includes(value) || false"
          show-overflow-tooltip
        />
        <el-table-column label="操作" width="200" align="center" @click.stop>
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click.stop="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="!row.routeSet" type="success" link size="small" @click.stop="handleSetRoute(row)">
              设置路线
            </el-button>
            <el-button type="danger" link size="small" @click.stop="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        class="param-form"
      >
        <!-- 第一行：申请编号、成品类别 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="申请编号" prop="applicationNumber">
              <el-input v-model="formData.applicationNumber" placeholder="请输入申请编号" @input="handleApplicationNumberInput" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成品类别" prop="productCategory">
              <el-select 
                v-model="formData.productCategory" 
                placeholder="请选择成品类别" 
                style="width: 100%"
                :loading="productLoading"
                @change="handleCategoryChange"
              >
                <el-option 
                  v-for="category in productCategories" 
                  :key="category" 
                  :label="category" 
                  :value="category" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第二行：模号、成品规格 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="模号" prop="moldNumber">
              <el-input v-model="formData.moldNumber" placeholder="请输入模号" @input="handleMoldNumberInput" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成品规格" prop="specification">
              <el-select 
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
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第三行：模具钢材、HRC -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="模具钢材" prop="material">
              <el-select 
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
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="HRC" prop="hrc">
              <el-input v-model="formData.hrc" placeholder="请输入HRC值，如：48-52" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第四行：结构、总收缩 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="结构" prop="structure">
              <el-select v-model="formData.structure" placeholder="请选择结构" style="width: 100%">
                <el-option label="斜边模" value="斜边模" />
                <el-option label="直压模" value="直压模" />
                <el-option label="收边模" value="收边模" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总收缩(%)" prop="totalShrinkage">
              <el-input-number
                v-model="formData.totalShrinkage"
                :max="100"
                :precision="2"
                placeholder="请输入总收缩（支持负值）"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第五行：模芯尺寸、外形 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="模芯尺寸">
              <el-input v-model="formData.coreSize" placeholder="请输入模芯尺寸" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="外形">
              <el-input v-model="formData.appearance" placeholder="请输入外形" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第六行：定位孔距、进泥孔径 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="定位孔距">
              <el-input v-model="formData.positioningHoleDistance" placeholder="请输入定位孔距" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="进泥孔径" prop="inletHoleDiameter">
              <el-input-number
                v-model="formData.inletHoleDiameter"
                :min="0"
                :precision="3"
                placeholder="请输入进泥孔径"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第七行：孔数、孔深 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="孔数">
              <el-input-number
                v-model="formData.holeCount"
                :min="0"
                :max="10000"
                placeholder="请输入孔数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="孔深">
              <el-input v-model="formData.holeDepth" placeholder="请输入孔深" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第八行：间孔或全孔、槽宽 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="间孔或全孔">
              <el-radio-group v-model="formData.holeType">
                <el-radio value="间孔">间孔</el-radio>
                <el-radio value="全孔">全孔</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="槽宽" prop="slotWidth">
              <el-input-number
                v-model="formData.slotWidth"
                :min="0.001"
                :precision="3"
                placeholder="请输入槽宽"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第九行：槽深、切入量 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="槽深">
              <el-input v-model="formData.slotDepth" placeholder="请输入槽深" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="切入量">
              <el-input v-model="formData.cutAmount" placeholder="请输入切入量" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第十行：中心距、供泥比 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="中心距" prop="centerDistance">
              <el-input-number
                v-model="formData.centerDistance"
                :min="0"
                :precision="3"
                placeholder="请输入中心距"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供泥比">
              <el-input v-model="formData.mudSupplyRatio" placeholder="请输入供泥比" />
              <div class="form-hint">
                <el-tooltip placement="top" content="点击查看计算公式">
                  <span class="hint-icon">i</span>
                </el-tooltip>
                <div class="formula-explanation">
                  <p>计算公式：</p>
                  <p v-if="formData.holeType === '间孔'">
                    进泥孔径的面积 ÷ （2 × (中心距² - (中心距 - 槽宽)²)）
                  </p>
                  <p v-else>
                    进泥孔径的面积 ÷ (中心距² - (中心距 - 槽宽)²)
                  </p>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第十一行：模芯台阶、负责人 -->
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="模芯台阶">
              <el-input 
                v-model="formData.coreStep" 
                placeholder="请输入模芯台阶" 
                clearable
                :class="{ 'is-focused': formData.coreStep }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-input 
                v-model="formData.responsiblePerson" 
                style="width: 100%"
                placeholder="请输入负责人"
                clearable
                :class="{ 'is-focused': formData.responsiblePerson }"
                class="mold-input"
              >
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 备注 -->
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="formData.remarks"
                type="textarea"
                :rows="4"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Download, Search, User, Refresh 
} from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getMoldInitialParams,
  createMoldInitialParam,
  updateMoldInitialParam,
  deleteMoldInitialParam,
  exportMoldInitialParams
} from '@/api/mold'
// 引入成品相关API和类型
import { getProducts } from '@/api/product'
import type { Product } from '@/api/product'
// 引入耗材相关API和类型
import { getConsumables } from '@/api/consumable'
import type { ConsumableItem } from '@/api/consumable'

// 表格数据
const loading = ref(false)
const tableData = ref<any[]>([])

// 筛选表单
const filterForm = reactive({
  applicationNumber: '',
  productCategory: '',
  moldNumber: '',
  specification: '',
  totalShrinkage: '',
  responsiblePerson: '',
  remarks: '',
  page: 1,
  size: 10
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 5
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增模具参数')
const submitLoading = ref(false)

// 表单引用和规则
const formRef = ref<FormInstance>()

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
    return '';
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
  return mudRatio.toFixed(2);
};

// 表单数据
const formData = reactive({
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

// 成品相关数据
const products = ref<Product[]>([]) // 所有成品数据
const productCategories = ref<string[]>([]) // 成品类别列表
const productSpecs = ref<string[]>([]) // 成品规格列表
const productLoading = ref(false) // 成品数据加载状态

// 耗材相关数据
const consumables = ref<ConsumableItem[]>([]); // 所有耗材数据
const steelMaterials = ref<string[]>([]); // 钢材列表（从耗材中筛选）
const steelLoading = ref(false); // 钢材数据加载状态

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

// 获取成品数据
const fetchProducts = async () => {
  productLoading.value = true
  try {
    const response = await getProducts()
    products.value = response.records || []
    
    // 提取并去重成品类别
    const categories = [...new Set(products.value.map(product => product.productCategory))]
    productCategories.value = categories.sort()
    
    // 提取并去重所有成品规格
    const specs = [...new Set(products.value.map(product => product.productSpec))]
    productSpecs.value = specs.sort()
  } catch (error: any) {
    console.error('获取成品数据失败:', error)
    
    // 添加默认数据，确保下拉选项能正常显示
    productCategories.value = ['蜂窝陶瓷载体', '其他']
    productSpecs.value = ['130-400', '150-600', '200-800']
    products.value = []
    
    // 不再显示错误信息，由fetchDataWithRetry统一处理
  } finally {
    productLoading.value = false
  }
}

// 获取钢材列表（从耗材中筛选）
const fetchSteelMaterials = async () => {
  steelLoading.value = true
  try {
    // 调用耗材API获取所有耗材数据
    const response = await getConsumables({ page: 1, pageSize: 100 }) // 获取足够多的数据
    
    // 检查API返回格式
    let consumableList: ConsumableItem[] = []
    if (Array.isArray(response)) {
      // 情况1：API直接返回数组
      consumableList = response
    } else if (response.data && Array.isArray(response.data)) {
      // 情况2：API返回对象，data是数组
      consumableList = response.data
    } else if (response.data && Array.isArray(response.data.records)) {
      // 情况3：API返回分页格式，data.records是数组
      consumableList = response.data.records
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
    console.error('获取钢材列表失败:', error)
    
    // 使用默认数据，确保下拉选项能正常显示
    steelMaterials.value = ['模具钢材', '不锈钢', '合金钢', '碳素钢']
    
    // 不再显示错误信息，由fetchDataWithRetry统一处理
    console.log('获取钢材列表失败，使用默认数据')
  } finally {
    steelLoading.value = false
  }
}



// 监听成品类别变化，清空当前选择的规格
const handleCategoryChange = () => {
  formData.specification = ''
}



// 处理申请编号输入
const handleApplicationNumberInput = () => {
  // 过滤字符：仅保留大写字母、数字和连字符，其他字符删除
  formData.applicationNumber = formData.applicationNumber
    .toUpperCase() // 转换为大写
    .replace(/[^A-Z0-9-]/g, ''); // 过滤不符合规范的字符
}

// 处理模号输入
const handleMoldNumberInput = () => {
  // 过滤字符：仅保留字母和数字，其他字符删除
  formData.moldNumber = formData.moldNumber
    .replace(/[^a-zA-Z0-9]/g, ''); // 过滤不符合规范的字符
}

// 表单验证规则
const formRules: FormRules = {
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
    { required: true, message: '请输入成品规格', trigger: 'blur' }
  ],
  material: [
    { required: true, message: '请选择模具钢材', trigger: 'change' }
  ],
  hrc: [
    { required: true, message: '请输入HRC', trigger: 'blur' }
  ],
  structure: [
    { required: true, message: '请选择结构', trigger: 'change' }
  ],
  totalShrinkage: [
    { required: true, message: '请输入总收缩', trigger: 'blur' }
  ],
  responsiblePerson: [
    { required: true, message: '请输入负责人', trigger: 'blur' }
  ],
  inletHoleDiameter: [
    { type: 'number', min: 0, message: '进泥孔径不能为负数', trigger: ['blur', 'change'] }
  ],
  slotWidth: [
    { type: 'number', min: 0.001, message: '槽宽必须大于或等于0.001', trigger: ['blur', 'change'] }
  ],
  centerDistance: [
    { type: 'number', min: 0, message: '中心距不能为负数', trigger: ['blur', 'change'] }
  ]
}

// 监听相关字段变化，自动计算供泥比
watch(
  [() => formData.inletHoleDiameter, () => formData.slotWidth, () => formData.centerDistance, () => formData.holeType],
  () => {
    formData.mudSupplyRatio = calculateMudSupplyRatio();
  },
  { immediate: true }
)

// 选中的行
const selectedRows = ref<any[]>([])

// 搜索
const handleSearch = async (showMessage: boolean = true) => {
  loading.value = true
  try {
    // 构造请求参数
    const requestParams = {
      ...filterForm,
      page: pagination.current,
      size: pagination.size
    }
    // 调试日志：记录请求参数
    console.log('[MoldParams] handleSearch 请求参数:', requestParams)
    // 从后端获取数据
    const response = await getMoldInitialParams(requestParams)
    // 调试日志：记录响应数据
    console.log('[MoldParams] handleSearch 响应数据:', response)
    
    // 处理多种响应格式
    let dataList = []
    let totalCount = 0
    
    if (response && Array.isArray(response.list)) {
      // 格式1：response.list
      dataList = response.list
      totalCount = response.total
    } else if (response && Array.isArray(response.records)) {
      // 格式2：response.records
      dataList = response.records
      totalCount = response.total
    } else if (Array.isArray(response)) {
      // 格式3：response 直接是数组
      dataList = response
      totalCount = dataList.length
    } else if (response && typeof response === 'object') {
      // 格式4：单个对象
      dataList = [response]
      totalCount = 1
    }
    
    // 统一所有列表项的数据格式
    dataList = dataList.map(item => ({
      id: item.id,
      applicationNumber: item.applicationNo || item.applicationNumber,
      productCategory: item.category || item.productCategory,
      moldNumber: item.moldCode || item.moldNumber,
      specification: item.productSpec || item.specification,
      material: item.material,
      hrc: item.hrc,
      structure: item.structure,
      totalShrinkage: item.totalShrinkage,
      coreSize: item.coreSize,
      appearance: item.outline || item.appearance,
      positioningHoleDistance: item.locationHolePitch || item.positioningHoleDistance,
      inletHoleDiameter: item.inletDiameter,
      holeCount: item.holeCount,
      holeDepth: item.holeDepth,
      holeType: item.porosityType || item.holeType,
      slotWidth: item.slotWidth,
      slotDepth: item.slotDepth,
      cutAmount: item.cutInAmount || item.cutAmount,
      centerDistance: item.centerDistance,
      mudSupplyRatio: item.feedRatio || item.mudSupplyRatio,
      coreStep: item.coreStepHeight || item.coreStep,
      responsiblePerson: item.ownerName || item.responsiblePerson,
      remarks: item.remark || item.remarks,
      status: item.status,
      routeSet: item.routeSet,
      createdAt: item.createdAt,
      updatedAt: item.updatedAt,
      // 保留原始数据，确保所有字段可用
      ...item
    }))
    
    // 调试日志：处理后的数据列表
    console.log('[MoldParams] handleSearch 处理后的数据列表:', dataList)
    
    tableData.value = dataList
    pagination.total = totalCount
    
    // 调试日志：设置表格数据后
    console.log('[MoldParams] handleSearch 设置表格数据后 tableData.length:', tableData.value.length)
    
    if (showMessage) {
      ElMessage.success('搜索完成')
    }
  } catch (error: any) {
    // 检查是否为401错误，如果是则不显示错误消息
    // 因为这可能是权限问题，避免触发重定向
    if (error.response?.status !== 401) {
      ElMessage.error('获取数据失败')
      console.error('获取数据失败:', error)
    } else {
      console.warn('API返回401，可能是权限问题或会话过期')
      // 不显示错误消息，避免触发重定向
    }
    // 出错时确保tableData为空数组，避免显示错误数据
    tableData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 重置
const handleReset = () => {
  // 重置所有筛选字段
  Object.keys(filterForm).forEach(key => {
    if (key !== 'page' && key !== 'size') {
      (filterForm as any)[key] = ''
    }
  })
  pagination.current = 1
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增模具参数'
  // 重置表单数据 - 使用formData中定义的实际字段
  Object.assign(formData, {
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
    responsiblePerson: '', // 负责人(文本输入)
    remarks: '', // 备注(多行文字属性)
    status: 'active' // 状态
  })
  // 删除可能存在的id字段，确保是新增操作
  delete (formData as any).id
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑模具参数'
  // 填充表单数据 - 使用row中存在的实际字段
  Object.assign(formData, {
    applicationNumber: row.applicationNumber || '', // 申请编号(唯一值)
    productCategory: row.productCategory || '', // 成品类别(下拉选择)
    moldNumber: row.moldNumber || '', // 模号(唯一值)
    specification: row.specification || '', // 成品规格(可输入，可下拉选择)
    material: row.material || '', // 模具钢材(下拉选择)
    hrc: row.hrc || '', // HRC
    structure: row.structure || '斜边模', // 结构(下拉选择：斜边模、直压模、收边模)
    totalShrinkage: row.totalShrinkage || 0, // 总收缩(%)
    coreSize: row.coreSize || '', // 模芯尺寸
    appearance: row.appearance || '', // 外形
    positioningHoleDistance: row.positioningHoleDistance || '', // 定位孔距
    inletHoleDiameter: parseFloat(row.inletHoleDiameter) || 0, // 进泥孔径
    holeCount: row.holeCount || 0, // 孔数
    holeDepth: row.holeDepth || '', // 孔深
    holeType: row.holeType || '间孔', // 间孔或全孔
    slotWidth: parseFloat(row.slotWidth) || 0, // 槽宽
    slotDepth: row.slotDepth || '', // 槽深
    cutAmount: row.cutAmount || '', // 切入量
    centerDistance: parseFloat(row.centerDistance) || 0, // 中心距
    mudSupplyRatio: row.mudSupplyRatio || '', // 供泥比
    coreStep: row.coreStep || '', // 模芯台阶
    responsiblePerson: row.responsiblePerson || '', // 负责人(文本输入)
    remarks: row.remarks || '', // 备注(多行文字属性)
    status: row.status || 'active' // 状态
  })
  // 保存ID用于更新
  ;(formData as any).id = row.id
  dialogVisible.value = true
}

// 行点击事件
const router = useRouter()

const handleRowClick = (row: any, column: any, _event: any) => {
  // 如果点击的是操作列，不执行跳转
  if (column && column.label !== '操作') {
    router.push(`/mold/initial-params-detail/${row.id}`)
  }
}

// 删除
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除模具参数 "${row.moldNumber}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteMoldInitialParam(row.id)
    ElMessage.success('删除成功')
    // 重新获取数据，不显示搜索完成提示
    handleSearch(false)
  } catch (err: any) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 导出
const handleExport = async () => {
  try {
    ElMessage.success('正在导出数据...')
    const blob = await exportMoldInitialParams(filterForm)
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `模具初始参数列表_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出完成')
  } catch {
    ElMessage.error('导出失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
        if (valid) {
          submitLoading.value = true
          try {
            // 表单提交前的预处理，设置默认值
            const form = { ...formData }
            
            // 1. 进泥孔径：未填写时自动设置为0
            if (form.inletHoleDiameter === undefined || form.inletHoleDiameter === null || form.inletHoleDiameter === '') {
              form.inletHoleDiameter = 0
            }
            
            // 2. 槽宽：未填写时自动设置为0
            if (form.slotWidth === undefined || form.slotWidth === null || form.slotWidth === '') {
              form.slotWidth = 0
            }
            
            // 3. 中心距：未填写时自动设置为0
            if (form.centerDistance === undefined || form.centerDistance === null || form.centerDistance === '') {
              form.centerDistance = 0
            }
            
            // 二次验证：确保槽宽符合最小值要求
            if (form.slotWidth < 0.001) {
              form.slotWidth = 0.001
            }
            
            const id = (form as any).id
            
            if (id) {
              // 编辑
              // 调试日志：记录编辑请求
              console.log('[MoldParams] handleSubmit 编辑请求:', { id, form })
              const response = await updateMoldInitialParam(id, form)
              ElMessage.success('修改成功')
              // 调试日志：记录编辑响应
              console.log('[MoldParams] handleSubmit 编辑响应:', response)
              
              // 乐观更新：立即更新列表中的数据
              const index = tableData.value.findIndex(item => item.id === id)
              if (index !== -1) {
                tableData.value[index] = { ...tableData.value[index], ...response.data }
              }
            } else {
              // 新增
              // 调试日志：记录新增请求
              console.log('[MoldParams] handleSubmit 新增请求:', form)
              const response = await createMoldInitialParam(form)
              ElMessage.success('新增成功')
              // 调试日志：记录新增响应
              console.log('[MoldParams] handleSubmit 新增响应:', response)
              
              // 统一数据格式，确保新增的数据格式与列表期望的格式一致
              const newItem = {
                id: response.data.id,
                applicationNumber: response.data.applicationNo || response.data.applicationNumber,
                productCategory: response.data.category || response.data.productCategory,
                moldNumber: response.data.moldCode || response.data.moldNumber,
                specification: response.data.productSpec || response.data.specification,
                material: response.data.material,
                hrc: response.data.hrc,
                structure: response.data.structure,
                totalShrinkage: response.data.totalShrinkage,
                coreSize: response.data.coreSize,
                appearance: response.data.outline || response.data.appearance,
                positioningHoleDistance: response.data.locationHolePitch || response.data.positioningHoleDistance,
                inletHoleDiameter: response.data.inletDiameter,
                holeCount: response.data.holeCount,
                holeDepth: response.data.holeDepth,
                holeType: response.data.porosityType || response.data.holeType,
                slotWidth: response.data.slotWidth,
                slotDepth: response.data.slotDepth,
                cutAmount: response.data.cutInAmount || response.data.cutAmount,
                centerDistance: response.data.centerDistance,
                mudSupplyRatio: response.data.feedRatio || response.data.mudSupplyRatio,
                coreStep: response.data.coreStepHeight || response.data.coreStep,
                responsiblePerson: response.data.ownerName || response.data.responsiblePerson,
                remarks: response.data.remark || response.data.remarks,
                status: response.data.status,
                routeSet: response.data.routeSet,
                createdAt: response.data.createdAt,
                updatedAt: response.data.updatedAt,
                // 保留原始数据，确保所有字段可用
                ...response.data
              }
              
              // 调试日志：记录新创建的数据
              console.log('[MoldParams] handleSubmit 新创建的数据:', newItem)
              
              // 先关闭对话框
              dialogVisible.value = false
              // 清除可能影响新增数据显示的筛选条件
              filterForm.applicationNumber = ''
              filterForm.productCategory = ''
              filterForm.moldNumber = ''
              filterForm.specification = ''
              filterForm.totalShrinkage = ''
              filterForm.responsiblePerson = ''
              filterForm.remarks = ''
              // 重置分页到第一页，确保新增的数据能显示
              pagination.current = 1
              
              // 调试日志：重新获取数据前
              console.log('[MoldParams] handleSubmit 重新获取数据前 tableData.length:', tableData.value.length)
              
              // 重新获取完整数据，确保列表数据与后端一致
              await handleSearch(false)
              
              // 调试日志：重新获取数据后
              console.log('[MoldParams] handleSubmit 重新获取数据后 tableData.length:', tableData.value.length)
              
              // 检查新创建的数据是否存在于列表中
              const exists = tableData.value.some(item => item.id === newItem.id)
              console.log('[MoldParams] handleSubmit 新数据是否存在于列表中:', exists)
              
              if (!exists) {
                // 如果不存在，手动添加到列表顶部
                console.warn('[MoldParams] 新增数据未在列表中找到，手动添加:', newItem)
                tableData.value.unshift(newItem)
                // 更新分页总数
                pagination.total += 1
                console.log('[MoldParams] 手动添加后 tableData.length:', tableData.value.length)
              }
            }
      } catch (_error: any) {
        // 修复：使用formData中的id，而不是try块内部的id变量
        const id = (formData as any).id
        
        // 检查是否为409冲突错误，如果是则不显示额外错误消息
        // 因为响应拦截器已经显示了详细的409错误消息
        if (_error.response?.status !== 409) {
          ElMessage.error(id ? '修改失败' : '新增失败')
        }
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  handleSearch()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  handleSearch()
}

// 筛选条件变化处理
const handleFilterChange = (filters: any) => {
  // 将筛选条件转换为filterForm的属性
  if (filters.applicationNumber) {
    filterForm.applicationNumber = filters.applicationNumber
  }
  if (filters.productCategory) {
    filterForm.productCategory = filters.productCategory
  }
  if (filters.moldNumber) {
    filterForm.moldNumber = filters.moldNumber
  }
  if (filters.specification) {
    filterForm.specification = filters.specification
  }
  if (filters.totalShrinkage) {
    filterForm.totalShrinkage = filters.totalShrinkage
  }
  if (filters.responsiblePerson) {
    filterForm.responsiblePerson = filters.responsiblePerson
  }
  if (filters.remarks) {
    // 备注字段特殊处理，因为它是数组
    filterForm.remarks = filters.remarks[0]
  }
  
  // 重置页码，从第一页开始显示
  pagination.current = 1
  
  // 调用搜索方法，从后端获取数据
  handleSearch()
}

// 检查模号唯一性
const checkMoldNumberUniqueness = async () => {
  ElMessage.info('开始检查模号唯一性...')
  
  try {
    // 保存当前的filterForm和pagination设置
    const originalFilter = { ...filterForm }
    const originalPage = pagination.current
    const originalSize = pagination.size
    
    // 设置分页为获取所有数据（使用较大的size值）
    const tempFilter = { ...filterForm, page: 1, size: 1000 } // 假设1000足够获取所有数据
    
    // 从后端获取所有数据
    const response = await getMoldInitialParams(tempFilter)
    const allData = response.data.records || response.data.list || []
    
    if (!allData || allData.length === 0) {
      ElMessage.info('没有找到模具参数数据')
      return
    }
    
    ElMessage.info(`共获取到 ${allData.length} 条模具参数数据`)
    
    // 检查显性重复
    const moldNumberMap = new Map()
    const duplicateMoldNumbers = new Set()
    
    allData.forEach((item, index) => {
      const moldNumber = item.moldNumber
      if (moldNumber) {
        if (moldNumberMap.has(moldNumber)) {
          duplicateMoldNumbers.add(moldNumber)
          moldNumberMap.set(moldNumber, [...moldNumberMap.get(moldNumber), index])
        } else {
          moldNumberMap.set(moldNumber, [index])
        }
      }
    })
    
    if (duplicateMoldNumbers.size > 0) {
      ElMessage.warning(`发现 ${duplicateMoldNumbers.size} 个重复模号：${Array.from(duplicateMoldNumbers).join(', ')}`)
      console.log('重复模号详情：', Object.fromEntries(moldNumberMap))
    } else {
      ElMessage.success('未发现显性重复模号')
    }
    
    // 检查隐性重复
    const normalizedMap = new Map()
    const hiddenDuplicates = new Set()
    
    allData.forEach((item, index) => {
      const originalMoldNumber = item.moldNumber
      if (originalMoldNumber) {
        // 标准化模号：去除空格、转换为大写、去除特殊字符
        const normalized = originalMoldNumber
          .trim()
          .toUpperCase()
          .replace(/[^A-Z0-9]/g, '')
        
        if (normalizedMap.has(normalized)) {
          hiddenDuplicates.add({ original: originalMoldNumber, normalized })
          normalizedMap.set(normalized, [...normalizedMap.get(normalized), { index, original: originalMoldNumber }])
        } else {
          normalizedMap.set(normalized, [{ index, original: originalMoldNumber }])
        }
      }
    })
    
    if (hiddenDuplicates.size > 0) {
      ElMessage.warning(`发现 ${hiddenDuplicates.size} 个隐性重复模号`)
      console.log('隐性重复模号详情：', Object.fromEntries(normalizedMap))
    } else {
      ElMessage.success('未发现隐性重复模号')
    }
    
    // 恢复原始设置
    Object.assign(filterForm, originalFilter)
    pagination.current = originalPage
    pagination.size = originalSize
    
    return {
      totalCount: allData.length,
      duplicateMoldNumbers: Array.from(duplicateMoldNumbers),
      hiddenDuplicates: Array.from(hiddenDuplicates),
      allData
    }
  } catch (error) {
    ElMessage.error('检查模号唯一性失败')
    console.error('检查模号唯一性失败：', error)
    return null
  }
}

// 添加加载状态
const loadingStates = ref({
  params: false,
  products: false,
  materials: false
})

// 添加重试机制
const retryCounts = ref({
  params: 0,
  products: 0,
  materials: 0
})

// 初始化
onMounted(() => {
  // 添加try-catch包装，确保单个API调用失败不会影响整个页面加载
  fetchDataWithRetry('params', handleSearch, false)
  fetchDataWithRetry('products', fetchProducts)
  fetchDataWithRetry('materials', fetchSteelMaterials)
})

// 带重试机制的数据获取函数
const fetchDataWithRetry = async (type: keyof typeof loadingStates.value, fetchFunc: any, ...args: any[]) => {
  const maxRetries = 2 // 最多重试2次
  let retryCount = 0
  
  while (retryCount <= maxRetries) {
    try {
      // 设置加载状态
      loadingStates.value[type] = true
      
      // 调用数据获取函数
      await fetchFunc(...args)
      
      // 重置重试计数
      retryCounts.value[type] = 0
      break
    } catch (error: any) {
      retryCount++
      retryCounts.value[type] = retryCount
      
      console.error(`获取${type}数据失败 (重试${retryCount}/${maxRetries}):`, error)
      
      // 如果是最后一次重试，显示错误提示
      if (retryCount > maxRetries) {
        let errorMessage = ''
        switch (type) {
          case 'params':
            errorMessage = '获取模具初始参数失败，页面可能无法正常显示数据'
            break
          case 'products':
            errorMessage = '获取成品数据失败，部分下拉选项可能无法正常显示'
            break
          case 'materials':
            errorMessage = '获取钢材列表失败，钢材下拉选项可能无法正常显示'
            break
          default:
            errorMessage = '获取数据失败，部分功能可能受到影响'
        }
        
        // 使用警告信息
        ElMessage.warning(errorMessage)
      } else {
        // 等待1秒后重试
        await new Promise(resolve => setTimeout(resolve, 1000))
      }
    } finally {
      // 重置加载状态
      loadingStates.value[type] = false
    }
  }
}

// 设置模具加工工序路线
const handleSetRoute = async (row: any) => {
  try {
    // 直接跳转到工序路线设置页面，不需要先调用API
    router.push(`/mold/process-route/${row.id}`)
  } catch {
    ElMessage.error('跳转失败，请重试')
  }
}


</script>

<style scoped lang="scss">
.initial-params-container {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title-section {
  flex: 1;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.page-actions {
  display: flex;
  gap: 12px;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.table-card {
  margin-bottom: 20px;
}

/* 选择列居中 */
:deep(.el-table-column--selection) .cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.param-form {
  padding: 20px 0;
}

/* 表单提示样式 */
.form-hint {
  display: flex;
  align-items: center;
  margin-top: 8px;
}

.hint-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  background-color: #409eff;
  color: white;
  border-radius: 50%;
  text-align: center;
  line-height: 16px;
  font-size: 12px;
  cursor: pointer;
  margin-right: 8px;
}

.formula-explanation {
  font-size: 12px;
  color: #606266;
  background-color: #f5f7fa;
  padding: 8px 12px;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

/* 模具选择器样式 */
.mold-select {
  /* 与系统其他下拉组件保持一致的样式 */
}

/* 下拉选项样式 */
:deep(.option-content) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 4px 0;
}

:deep(.option-name) {
  font-weight: 500;
  color: #333;
}

:deep(.option-role) {
  font-size: 12px;
  color: #999;
  background-color: #f0f2f5;
  padding: 2px 8px;
  border-radius: 10px;
  white-space: nowrap;
}

.formula-explanation p {
  margin: 4px 0 0 0;
  margin: 4px 0;
  line-height: 1.4;
}

.search-card {
  margin-bottom: 20px;
  overflow: hidden;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
}

// 响应式设计
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .page-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .filter-form {
    flex-direction: column;
  }
  
  .filter-form .el-form-item {
    margin-right: 0;
    width: 100%;
  }
}
</style>
