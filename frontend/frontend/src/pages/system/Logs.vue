<template>
  <div class="system-logs-container">
    <el-page-header
      @back="$router.back"
      content="日志管理"
    />

    <el-card class="logs-card">
      <!-- 查询条件 -->
      <el-form
        :inline="true"
        :model="searchForm"
        class="logs-search-form"
      >
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>

        <el-form-item label="操作类型">
          <el-select
            v-model="searchForm.operationType"
            placeholder="请选择操作类型"
            clearable
          >
            <el-option
              v-for="type in operationTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="操作结果">
          <el-select
            v-model="searchForm.result"
            placeholder="请选择操作结果"
            clearable
          >
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILURE" />
          </el-select>
        </el-form-item>

        <el-form-item label="敏感级别">
          <el-select
            v-model="searchForm.sensitiveLevel"
            placeholder="请选择敏感级别"
            clearable
          >
            <el-option label="普通" value="normal" />
            <el-option label="高" value="high" />
            <el-option label="关键" value="critical" />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 日志列表 -->
      <el-table
        v-loading="loading"
        :data="logsData"
        style="width: 100%"
        stripe
        :height="tableHeight"
      >
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100" />
        <el-table-column prop="operationDesc" label="操作描述" min-width="180" />
        <el-table-column prop="operationContent" label="操作内容" min-width="200" show-overflow-tooltip />
        <el-table-column
          prop="result"
          label="操作结果"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="scope.row.result === 'SUCCESS' ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.result === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="isSensitive"
          label="是否敏感"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="scope.row.isSensitive ? 'warning' : 'info'"
              size="small"
            >
              {{ scope.row.isSensitive ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="sensitiveLevel"
          label="敏感级别"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="
                scope.row.sensitiveLevel === 'critical' ? 'danger' :
                scope.row.sensitiveLevel === 'high' ? 'warning' : 'info'
              "
              size="small"
            >
              {{ 
                scope.row.sensitiveLevel === 'critical' ? '关键' :
                scope.row.sensitiveLevel === 'high' ? '高' : '普通' 
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userIp" label="IP地址" width="150" />
        <el-table-column
          prop="operationTime"
          label="操作时间"
          width="180"
        >
          <template #default="scope">
            {{ formatDateTime(scope.row.operationTime) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="logs-pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDateTime } from '@/utils/date'
import { getOperationLogs } from '@/api/system'

// 表格高度计算
const tableHeight = computed(() => {
  return window.innerHeight - 300
})

// 操作类型选项
const operationTypes = [
  { label: '新增', value: 'POST' },
  { label: '查询', value: 'GET' },
  { label: '更新', value: 'PUT' },
  { label: '删除', value: 'DELETE' },
]

// 搜索表单
const searchForm = reactive({
  username: '',
  operationType: '',
  result: '',
  sensitiveLevel: '',
  dateRange: [] as string[],
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
})

// 日志数据
const logsData = ref([])
const loading = ref(false)

// 获取日志列表
const getLogsList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.size,
      username: searchForm.username,
      operationType: searchForm.operationType,
      result: searchForm.result,
      sensitiveLevel: searchForm.sensitiveLevel,
      startTime: searchForm.dateRange[0] || '',
      endTime: searchForm.dateRange[1] || '',
    }

    const response = await getOperationLogs(params)
    logsData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取日志列表失败')
    console.error('获取日志列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 查询
const handleSearch = () => {
  pagination.current = 1
  getLogsList()
}

// 重置表单
const resetForm = () => {
  Object.assign(searchForm, {
    username: '',
    operationType: '',
    result: '',
    sensitiveLevel: '',
    dateRange: [],
  })
  pagination.current = 1
  getLogsList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  getLogsList()
}

// 当前页码变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  getLogsList()
}

// 初始化
onMounted(() => {
  getLogsList()
})
</script>

<style scoped>
.system-logs-container {
  padding: 20px;
}

.logs-card {
  margin-top: 20px;
}

.logs-search-form {
  margin-bottom: 20px;
}

.logs-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>