<template>
  <div class="process-detail-container">
    <!-- 页面标题 -->
    <el-page-header
      @back="handleBack"
      content="工序详细设置"
      :title="currentProcess.name"
      class="mb-20"
    />

    <!-- 工序信息概览 -->
    <el-card class="mb-20">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">工序编号</div>
            <div class="info-value">{{ currentProcess.code }}</div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">工序类型</div>
            <div class="info-value">{{ getProcessTypeName(currentProcess.type) }}</div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">预计工时</div>
            <div class="info-value">{{ currentProcess.estimatedTime }} 小时</div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">设备状态</div>
            <div class="info-value">
              <el-tag :type="currentProcess.equipment.status === 'available' ? 'success' : currentProcess.equipment.status === 'maintenance' ? 'warning' : 'danger'">
                {{ getEquipmentStatusName(currentProcess.equipment.status) }}
              </el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">操作人员</div>
            <div class="info-value">{{ currentProcess.operator.name || '未分配' }}</div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="info-item">
            <div class="info-label">物料数量</div>
            <div class="info-value">{{ currentProcess.materials.length }} 种</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工序导航 -->
    <el-card class="mb-20">
      <div class="process-navigation">
        <el-steps :active="currentProcessIndex" align-center direction="vertical" space="medium">
          <el-step
            v-for="(process, index) in processList"
            :key="index"
            :title="process.name"
            :description="`工序 ${index + 1}`"
            @click="switchProcess(index)"
          />
        </el-steps>
        <div class="navigation-buttons">
          <el-button @click="handlePrevStep" :disabled="currentProcessIndex === 0" class="mr-10">上一道工序</el-button>
          <el-button type="primary" @click="handleNextStep" :disabled="currentProcessIndex === processList.length - 1">下一道工序</el-button>
        </div>
      </div>
    </el-card>

    <!-- 工序详细表单 -->
    <el-card>
      <el-tabs v-model="activeTab" type="card" @tab-change="handleTabChange">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="currentProcess" label-position="top" label-width="120px" class="process-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="工序名称" required>
                  <el-input v-model="currentProcess.name" placeholder="请输入工序名称" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工序编号" required>
                  <el-input v-model="currentProcess.code" placeholder="请输入工序编号" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预计工时" required>
                  <el-input-number
                    v-model="currentProcess.estimatedTime"
                    :min="0"
                    :step="0.5"
                    placeholder="请输入预计工时"
                    suffix="小时"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工序类型" required>
                  <el-select v-model="currentProcess.type" placeholder="请选择工序类型" clearable style="width: 100%">
                    <el-option label="加工工序" value="processing" />
                    <el-option label="检测工序" value="inspection" />
                    <el-option label="装配工序" value="assembly" />
                    <el-option label="包装工序" value="packaging" />
                    <el-option label="热处理工序" value="heat_treatment" />
                    <el-option label="表面处理工序" value="surface_treatment" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="工序优先级">
                  <el-select v-model="currentProcess.priority" placeholder="请选择工序优先级" clearable style="width: 100%">
                    <el-option label="高" value="high" />
                    <el-option label="中" value="medium" />
                    <el-option label="低" value="low" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否关键工序">
                  <el-switch v-model="currentProcess.isCritical" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="工序描述">
                  <el-input
                    v-model="currentProcess.description"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入工序描述"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="工序备注">
                  <el-input
                    v-model="currentProcess.remark"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入工序备注"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 设备设置 -->
        <el-tab-pane label="设备设置" name="equipment">
          <el-form :model="currentProcess.equipment" label-position="top" label-width="120px" class="process-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="设备名称" required>
                  <el-select v-model="currentProcess.equipment.name" placeholder="请选择设备" clearable style="width: 100%">
                    <el-option label="设备A" value="equipmentA" />
                    <el-option label="设备B" value="equipmentB" />
                    <el-option label="设备C" value="equipmentC" />
                    <el-option label="深孔钻1号" value="deep_drill_1" />
                    <el-option label="深孔钻2号" value="deep_drill_2" />
                    <el-option label="中走丝1号" value="wire_cut_1" />
                    <el-option label="切槽机1号" value="groove_cutter_1" />
                    <el-option label="精雕机" value="cnc_carving" />
                    <el-option label="电火花成型机1号" value="edm_1" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备编号" required>
                  <el-input v-model="currentProcess.equipment.code" placeholder="请输入设备编号" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="设备状态" required>
                  <el-select v-model="currentProcess.equipment.status" placeholder="请选择设备状态" clearable style="width: 100%">
                    <el-option label="可用" value="available" />
                    <el-option label="维护中" value="maintenance" />
                    <el-option label="故障" value="faulty" />
                    <el-option label="停用" value="disabled" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备负责人">
                  <el-input v-model="currentProcess.equipment.manager" placeholder="请输入设备负责人" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备参数配置">
                  <el-input
                    v-model="currentProcess.equipment.parameters"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入设备参数（如：转速、温度、压力等），每行一个参数"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备维护要求">
                  <el-input
                    v-model="currentProcess.equipment.maintenanceRequirements"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入设备维护要求"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 操作人员 -->
        <el-tab-pane label="操作人员" name="operator">
          <el-form :model="currentProcess.operator" label-position="top" label-width="120px" class="process-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="操作人员" required>
                  <el-select v-model="currentProcess.operator.name" placeholder="请选择操作人员" clearable style="width: 100%">
                    <el-option label="张三" value="zhangsan" />
                    <el-option label="李四" value="lisi" />
                    <el-option label="王五" value="wangwu" />
                    <el-option label="赵六" value="zhaoliu" />
                    <el-option label="孙七" value="sunqi" />
                    <el-option label="周八" value="zhouba" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="操作人员编号" required>
                  <el-input v-model="currentProcess.operator.code" placeholder="请输入操作人员编号" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="所属部门" required>
                  <el-select v-model="currentProcess.operator.department" placeholder="请选择所属部门" clearable style="width: 100%">
                    <el-option label="加工部" value="processing" />
                    <el-option label="质检部" value="quality" />
                    <el-option label="装配部" value="assembly" />
                    <el-option label="设备部" value="equipment" />
                    <el-option label="生产部" value="production" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="技能等级" required>
                  <el-select v-model="currentProcess.operator.skillLevel" placeholder="请选择技能等级" clearable style="width: 100%">
                    <el-option label="初级" value="junior" />
                    <el-option label="中级" value="intermediate" />
                    <el-option label="高级" value="senior" />
                    <el-option label="技师" value="technician" />
                    <el-option label="高级技师" value="senior_technician" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="持证要求">
                  <el-input v-model="currentProcess.operator.certification" placeholder="请输入持证要求" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="培训要求">
                  <el-input v-model="currentProcess.operator.trainingRequirements" placeholder="请输入培训要求" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="操作注意事项">
                  <el-input
                    v-model="currentProcess.operator.operationNotes"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入操作注意事项"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 质量标准 -->
        <el-tab-pane label="质量标准" name="quality">
          <el-form :model="currentProcess.quality" label-position="top" label-width="120px" class="process-form">
            <el-row>
              <el-col :span="24">
                <el-form-item label="质量要求" required>
                  <el-input
                    v-model="currentProcess.quality.requirements"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入质量要求"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检验方法" required>
                  <el-select v-model="currentProcess.quality.inspectionMethod" placeholder="请选择检验方法" clearable style="width: 100%">
                    <el-option label="目视检查" value="visual" />
                    <el-option label="尺寸测量" value="measurement" />
                    <el-option label="性能测试" value="performance" />
                    <el-option label="无损检测" value="nondestructive" />
                    <el-option label="化学分析" value="chemical" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检验工具" required>
                  <el-input v-model="currentProcess.quality.inspectionTools" placeholder="请输入检验工具，多个工具用逗号分隔" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检验频率">
                  <el-select v-model="currentProcess.quality.inspectionFrequency" placeholder="请选择检验频率" clearable style="width: 100%">
                    <el-option label="首件检验" value="first" />
                    <el-option label="批次检验" value="batch" />
                    <el-option label="全检" value="100%" />
                    <el-option label="抽样检验" value="sampling" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="抽样比例">
                  <el-input v-model="currentProcess.quality.samplingRatio" placeholder="请输入抽样比例（如：10%）" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检测项目及验收标准">
                  <el-input
                    v-model="currentProcess.quality.inspectionItems"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入检测项目及验收标准，每行一个项目"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="不合格处理方式" required>
                  <el-input
                    v-model="currentProcess.quality.unqualifiedHandling" 
                    type="textarea"
                    :rows="3"
                    placeholder="请输入不合格处理方式"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 物料需求 -->
        <el-tab-pane label="物料需求" name="materials">
          <div class="materials-section">
            <el-table :data="currentProcess.materials" style="width: 100%" border>
              <el-table-column prop="name" label="物料名称" min-width="150" />
              <el-table-column prop="code" label="物料编号" min-width="120" />
              <el-table-column prop="specification" label="规格型号" min-width="150" />
              <el-table-column prop="quantity" label="需求数量" min-width="100">
                <template #default="scope">
                  <el-input-number
                    v-model="scope.row.quantity"
                    :min="0"
                    :step="1"
                    :precision="0"
                    style="width: 120px"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="单位" min-width="80">
                <template #default="scope">
                  <el-select v-model="scope.row.unit" placeholder="单位" style="width: 100px">
                    <el-option label="件" value="件" />
                    <el-option label="个" value="个" />
                    <el-option label="kg" value="kg" />
                    <el-option label="m" value="m" />
                    <el-option label="mm" value="mm" />
                    <el-option label="套" value="套" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" min-width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.remark" placeholder="备注" clearable size="small" />
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="100" fixed="right">
                <template #default="scope">
                  <el-button type="danger" size="small" @click="handleDeleteMaterial(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="mt-20">
              <el-button type="primary" @click="handleAddMaterial">添加物料</el-button>
            </div>
          </div>
        </el-tab-pane>

        <!-- 安全要求 -->
        <el-tab-pane label="安全要求" name="safety">
          <el-form :model="currentProcess.safety" label-position="top" label-width="120px" class="process-form">
            <el-row>
              <el-col :span="24">
                <el-form-item label="安全操作规程" required>
                  <el-input
                    v-model="currentProcess.safety.operatingProcedures"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入安全操作规程"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="防护用品" required>
                  <el-select v-model="currentProcess.safety.protectiveEquipment" placeholder="请选择防护用品" multiple collapse-tags clearable style="width: 100%">
                    <el-option label="安全帽" value="helmet" />
                    <el-option label="安全眼镜" value="safety_glasses" />
                    <el-option label="防护手套" value="safety_gloves" />
                    <el-option label="防护鞋" value="safety_shoes" />
                    <el-option label="防护面罩" value="face_shield" />
                    <el-option label="耳塞" value="ear_plugs" />
                    <el-option label="防尘口罩" value="dust_mask" />
                    <el-option label="防毒面具" value="gas_mask" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="安全等级">
                  <el-select v-model="currentProcess.safety.level" placeholder="请选择安全等级" clearable style="width: 100%">
                    <el-option label="低风险" value="low" />
                    <el-option label="中风险" value="medium" />
                    <el-option label="高风险" value="high" />
                    <el-option label="极高风险" value="extreme" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="危险有害因素">
                  <el-input
                    v-model="currentProcess.safety.hazards"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入危险有害因素"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="应急处理措施" required>
                  <el-input
                    v-model="currentProcess.safety.emergencyMeasures"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入应急处理措施"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="安全培训要求">
                  <el-input
                    v-model="currentProcess.safety.trainingRequirements"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入安全培训要求"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 操作按钮 -->
    <div class="bottom-buttons">
      <el-button @click="handleBack">返回</el-button>
      <el-button type="primary" @click="handleSave">保存当前工序</el-button>
      <el-button type="success" @click="handleSaveAll">保存所有工序</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 工序列表（模拟数据）
const processList = reactive([
  {
    id: 1,
    name: '备料',
    code: 'process001',
    estimatedTime: 1.5,
    type: 'processing',
    priority: 'medium',
    isCritical: false,
    description: '准备生产所需的原材料',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [
      { name: '原材料A', code: 'material001', specification: '规格A', quantity: 10, unit: '件', remark: '' }
    ],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'medium',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 2,
    name: '选择粗加工',
    code: 'process002',
    estimatedTime: 2,
    type: 'processing',
    priority: 'high',
    isCritical: true,
    description: '对原材料进行初步加工',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'high',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 3,
    name: '热处理',
    code: 'process003',
    estimatedTime: 3,
    type: 'heat_treatment',
    priority: 'high',
    isCritical: true,
    description: '对加工件进行热处理',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'high',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 4,
    name: '选配精加工',
    code: 'process004',
    estimatedTime: 4,
    type: 'processing',
    priority: 'high',
    isCritical: true,
    description: '对热处理后的工件进行精加工',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'medium',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 5,
    name: '零件精加工',
    code: 'process005',
    estimatedTime: 3.5,
    type: 'processing',
    priority: 'medium',
    isCritical: false,
    description: '对零件进行最终精加工',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'medium',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 6,
    name: '外协加工',
    code: 'process006',
    estimatedTime: 5,
    type: 'processing',
    priority: 'medium',
    isCritical: false,
    description: '将部分工序外包加工',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'low',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 7,
    name: '装配初检',
    code: 'process007',
    estimatedTime: 2,
    type: 'inspection',
    priority: 'medium',
    isCritical: false,
    description: '对装配后的工件进行初步检测',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'low',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 8,
    name: '品质终检',
    code: 'process008',
    estimatedTime: 3,
    type: 'inspection',
    priority: 'high',
    isCritical: true,
    description: '对成品进行最终品质检测',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'medium',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  },
  {
    id: 9,
    name: '入库',
    code: 'process009',
    estimatedTime: 1,
    type: 'packaging',
    priority: 'low',
    isCritical: false,
    description: '将合格产品入库',
    remark: '',
    equipment: {
      name: '',
      code: '',
      parameters: '',
      status: 'available',
      manager: ''
    },
    operator: {
      name: '',
      code: '',
      department: '',
      skillLevel: '',
      certification: '',
      trainingRequirements: '',
      operationNotes: ''
    },
    quality: {
      requirements: '',
      inspectionMethod: '',
      inspectionTools: '',
      inspectionFrequency: '',
      samplingRatio: '',
      inspectionItems: '',
      unqualifiedHandling: ''
    },
    materials: [],
    safety: {
      operatingProcedures: '',
      protectiveEquipment: [],
      level: 'low',
      hazards: '',
      emergencyMeasures: '',
      trainingRequirements: ''
    }
  }
])

const currentProcessIndex = ref(0)
const currentProcess = ref(processList[0])
const activeTab = ref('basic')

// 获取工序类型名称
const getProcessTypeName = (type) => {
  const typeMap = {
    'processing': '加工工序',
    'inspection': '检测工序',
    'assembly': '装配工序',
    'packaging': '包装工序',
    'heat_treatment': '热处理工序',
    'surface_treatment': '表面处理工序'
  }
  return typeMap[type] || type
}

// 获取设备状态名称
const getEquipmentStatusName = (status) => {
  const statusMap = {
    'available': '可用',
    'maintenance': '维护中',
    'faulty': '故障',
    'disabled': '停用'
  }
  return statusMap[status] || status
}

// 切换到上一道工序
const handlePrevStep = () => {
  if (currentProcessIndex.value > 0) {
    currentProcessIndex.value--
    currentProcess.value = processList[currentProcessIndex.value]
  }
}

// 切换到下一道工序
const handleNextStep = () => {
  if (currentProcessIndex.value < processList.length - 1) {
    currentProcessIndex.value++
    currentProcess.value = processList[currentProcessIndex.value]
  }
}

// 直接切换到指定工序
const switchProcess = (index) => {
  currentProcessIndex.value = index
  currentProcess.value = processList[index]
}

// 标签页切换事件
const handleTabChange = (tab) => {
  console.log('切换到标签页:', tab)
  // 可以在这里添加标签页切换的逻辑，如加载数据等
}

// 添加物料
const handleAddMaterial = () => {
  currentProcess.value.materials.push({
    name: '',
    code: '',
    specification: '',
    quantity: 1,
    unit: '件',
    remark: ''
  })
}

// 删除物料
const handleDeleteMaterial = (index) => {
  currentProcess.value.materials.splice(index, 1)
  ElMessage.success('物料删除成功')
}

// 保存当前工序
const handleSave = () => {
  // 这里可以添加保存逻辑
  console.log('保存当前工序:', currentProcess.value)
  ElMessage.success('当前工序保存成功')
}

// 保存所有工序
const handleSaveAll = () => {
  // 这里可以添加保存所有工序的逻辑
  console.log('保存所有工序:', processList)
  ElMessage.success('所有工序保存成功')
}

// 返回上一页
const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.process-detail-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  background-color: var(--el-bg-color-page);
  min-height: 100vh;
}

.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.mr-10 {
  margin-right: 10px;
}

/* 工序信息概览样式 */
.info-item {
  text-align: center;
  padding: 15px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.info-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.info-label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 8px;
}

.info-value {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 工序导航样式 */
.process-navigation {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 30px;
}

.process-navigation :deep(.el-steps) {
  flex: 1;
}

.navigation-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
}

/* 表单样式 */
.process-form {
  padding: 20px 0;
}

.process-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.process-form :deep(.el-textarea) {
  resize: vertical;
}

/* 物料表格样式 */
.materials-section {
  padding: 20px 0;
}

.materials-section :deep(.el-table) {
  margin-bottom: 20px;
}

.materials-section :deep(.el-table__empty-text) {
  padding: 40px 0;
}

/* 底部按钮样式 */
.bottom-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
  padding: 20px 0;
  border-top: 1px solid var(--el-border-color);
}

/* 标签页样式 */
:deep(.el-tabs__header) {
  margin-bottom: 20px;
}

:deep(.el-tabs__content) {
  padding: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .process-detail-container {
    max-width: 100%;
    padding: 10px;
  }
  
  .info-item {
    padding: 10px;
  }
  
  .process-navigation {
    flex-direction: column;
    gap: 20px;
  }
  
  .navigation-buttons {
    flex-direction: row;
    justify-content: center;
  }
}
</style>