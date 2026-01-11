<template>
  <div class="material-preparation-page">
    <!-- 页面标题 -->
    <h1 class="page-title">备料工序</h1>
    
    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="handlePrint">打印</el-button>
      <el-button @click="handleBack">返回</el-button>
    </div>
    
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
            <th>材 料:</th>
            <td><el-input v-model="moldInfo.material" placeholder="请输入材料" /></td>
            <th>硬 度:</th>
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
            <th>槽 宽:</th>
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
    
    <!-- 打印样式占位符 -->
    <div class="print-placeholder"></div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 模具基本信息
const moldInfo = reactive({
  responsiblePerson: '罗京',
  moldNumber: '',
  moldSpec: '',
  moldThickness: '',
  moldEngraving: '',
  productSpec: '',
  material: '',
  hardness: '',
  positioningHoleDistance: '',
  mudInletDiameter: '',
  slotWidth: '',
  slotSpacing: ''
})

// 工序内容列表
const processList = reactive([
  {
    processName: '',
    equipment: '',
    details: '',
    responsiblePerson: '',
    date: '',
    remark: ''
  },
  {
    processName: '',
    equipment: '',
    details: '',
    responsiblePerson: '',
    date: '',
    remark: ''
  },
  {
    processName: '',
    equipment: '',
    details: '',
    responsiblePerson: '',
    date: '',
    remark: ''
  },
  {
    processName: '',
    equipment: '',
    details: '',
    responsiblePerson: '',
    date: '',
    remark: ''
  }
])

// 打印功能
const handlePrint = () => {
  window.print()
}

// 返回功能
const handleBack = () => {
  router.back()
}
</script>

<style scoped>
/* 页面基本样式 */
.material-preparation-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  font-family: Arial, sans-serif;
  box-sizing: border-box;
}

/* 页面标题 */
.page-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 30px 0;
  padding: 10px 0;
  color: #333;
  border-bottom: 1px solid #eaeaea;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
  padding: 10px 0;
  gap: 10px;
}

/* 表格区域 */
.table-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 10px 0;
  padding: 5px 0;
  color: #333;
  border-bottom: 1px solid #eaeaea;
}

/* 表格容器 */
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
}

.basic-info-table th,
.basic-info-table td {
  border: 1px solid #000;
  padding: 10px 8px;
  text-align: left;
  vertical-align: middle;
  min-height: 36px;
}

.basic-info-table th {
  font-weight: bold;
  background-color: #fafafa;
  color: #333;
}

.basic-info-table td {
  background-color: #fff;
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
}

.process-content-table th,
.process-content-table td {
  border: 1px solid #000;
  padding: 10px 8px;
  text-align: left;
  vertical-align: middle;
  min-height: 36px;
}

.process-content-table th {
  font-weight: bold;
  background-color: #fafafa;
  color: #333;
}

.process-content-table td {
  background-color: #fff;
}

.process-content-table th:nth-child(1),
.process-content-table td:nth-child(1) {
  width: 5%;
  text-align: center;
}

.process-content-table th:nth-child(2),
.process-content-table td:nth-child(2) {
  width: 15%;
}

.process-content-table th:nth-child(3),
.process-content-table td:nth-child(3) {
  width: 15%;
}

.process-content-table th:nth-child(4),
.process-content-table td:nth-child(4) {
  width: 30%;
}

.process-content-table th:nth-child(5),
.process-content-table td:nth-child(5) {
  width: 10%;
}

.process-content-table th:nth-child(6),
.process-content-table td:nth-child(6) {
  width: 10%;
}

.process-content-table th:nth-child(7),
.process-content-table td:nth-child(7) {
  width: 15%;
}

/* 打印样式占位符 */
.print-placeholder {
  height: 20px;
  clear: both;
}

/* 打印样式 */
@media print {
  /* 隐藏操作按钮 */
  .action-buttons {
    display: none !important;
  }
  
  /* 调整页面边距 */
  @page {
    margin: 2cm;
  }
  
  /* 页面样式 */
  body {
    background-color: #fff;
    font-size: 12pt;
  }
  
  .material-preparation-page {
    padding: 0;
    max-width: 100%;
  }
  
  /* 表格样式 */
  .table-container {
    border: 1px solid #000;
    box-shadow: none;
    overflow: visible;
    page-break-inside: avoid;
  }
  
  .basic-info-table,
  .process-content-table {
    border-collapse: collapse;
    page-break-inside: avoid;
  }
  
  .basic-info-table th,
  .basic-info-table td,
  .process-content-table th,
  .process-content-table td {
    border: 1px solid #000;
    padding: 8px;
    font-size: 10pt;
  }
  
  /* 标题样式 */
  .page-title {
    font-size: 18pt;
    margin-bottom: 20pt;
    padding-bottom: 10pt;
    border-bottom: 1px solid #000;
  }
  
  .section-title {
    font-size: 14pt;
    margin-bottom: 10pt;
    padding-bottom: 5pt;
    border-bottom: 1px solid #000;
  }
  
  /* 防止表格跨页断裂 */
  thead {
    display: table-header-group;
  }
  
  tfoot {
    display: table-footer-group;
  }
  
  tr {
    page-break-inside: avoid;
  }
  
  /* 打印样式占位符 */
  .print-placeholder {
    display: none;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .material-preparation-page {
    padding: 10px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: flex-end;
  }
  
  .table-container {
    font-size: 12px;
  }
  
  .basic-info-table th,
  .basic-info-table td,
  .process-content-table th,
  .process-content-table td {
    padding: 5px;
  }
}

/* 打印样式 */
@media print {
  /* 隐藏不必要的元素 */
  .el-menu, .el-header, .action-buttons {
    display: none !important;
  }
  
  /* 设置A4纸大小 */
  @page {
    size: A4;
    margin: 1cm;
  }
  
  /* 页面样式 */
  .material-preparation-page {
    padding: 0;
    background-color: #fff;
    width: 210mm;
    margin: 0 auto;
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