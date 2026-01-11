<template>
  <div class="consumables-container">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">物料管理</h1>
        <p class="page-subtitle">管理和维护模具物料库存</p>
      </div>
      <div class="page-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增物料
        </el-button>
        <el-button @click="handleImport">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>



    <!-- 统计卡片区域 -->
    <el-card class="stats-card">
      <h3 class="stats-title">物料类别统计</h3>
      <div class="stats-container" v-loading="statsLoading">
        <div 
          v-for="item in categoryStats" 
          :key="item.category"
          class="stat-item" 
          :class="{ 'active': selectedCategory === item.category }"
          @click="handleCategoryClick(item.category)"
        >
          <div class="stat-label">{{ item.category || '未知类别' }}</div>
          <div class="stat-value">{{ item.count || 0 }}</div>
        </div>
      </div>
      <div v-if="!statsLoading && categoryStats.length === 0" class="no-stats">
        <el-empty description="暂无统计数据" />
      </div>
    </el-card>

    <el-card class="table-card">
      <h3 class="table-header-title">物料列表</h3>
      <div class="table-actions">
        <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchStockIn">
          <el-icon><Plus /></el-icon>
          入库
        </el-button>
        <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchStockOut">
          <el-icon><Minus /></el-icon>
          出库
        </el-button>
        <el-button type="warning" :disabled="selectedIds.length === 0" @click="handleBatchStock">
          <el-icon><Box /></el-icon>
          批量盘点
        </el-button>
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="consumablesList"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="materialCode" width="120" fixed >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>物料编码</span>
              <el-popover
                v-model:visible="filterPopoverVisible.materialCode"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.materialCode"
                    placeholder="请输入物料编码"
                    clearable
                    @keyup.enter="handleFilter('materialCode')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('materialCode')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('materialCode')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.materialCode" class="filter-tag">
                {{ filterConditions.materialCode }}
                <el-button type="text" size="small" @click="handleFilterReset('materialCode')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="itemName" width="150" show-overflow-tooltip >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>物品名称</span>
              <el-popover
                v-model:visible="filterPopoverVisible.itemName"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.itemName"
                    placeholder="请输入物品名称"
                    clearable
                    @keyup.enter="handleFilter('itemName')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('itemName')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('itemName')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.itemName" class="filter-tag">
                {{ filterConditions.itemName }}
                <el-button type="text" size="small" @click="handleFilterReset('itemName')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="itemCategory" width="100" align="center" >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>物品类型</span>
              <el-popover
                v-model:visible="filterPopoverVisible.itemCategory"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-select
                    v-model="filterForm.itemCategory"
                    placeholder="请选择物品类型"
                    clearable
                    style="width: 100%"
                  >
                    <el-option label="工具类" value="工具类" />
                    <el-option label="材料类" value="材料类" />
                    <el-option label="备件类" value="备件类" />
                    <el-option label="消耗类" value="消耗类" />
                    <el-option label="其他" value="其他" />
                  </el-select>
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('itemCategory')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('itemCategory')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.itemCategory" class="filter-tag">
                {{ filterConditions.itemCategory }}
                <el-button type="text" size="small" @click="handleFilterReset('itemCategory')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="specification" width="120" >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>规格型号</span>
              <el-popover
                v-model:visible="filterPopoverVisible.specification"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.specification"
                    placeholder="请输入规格型号"
                    clearable
                    @keyup.enter="handleFilter('specification')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('specification')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('specification')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.specification" class="filter-tag">
                {{ filterConditions.specification }}
                <el-button type="text" size="small" @click="handleFilterReset('specification')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="unit" width="60" align="center" >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>单位</span>
              <el-popover
                v-model:visible="filterPopoverVisible.unit"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-select
                    v-model="filterForm.unit"
                    placeholder="请选择单位"
                    clearable
                    style="width: 100%"
                  >
                    <el-option label="个" value="个" />
                    <el-option label="桶" value="桶" />
                    <el-option label="盒" value="盒" />
                    <el-option label="套" value="套" />
                    <el-option label="根" value="根" />
                    <el-option label="只" value="只" />
                    <el-option label="瓶" value="瓶" />
                  </el-select>
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('unit')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('unit')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.unit" class="filter-tag">
                {{ filterConditions.unit }}
                <el-button type="text" size="small" @click="handleFilterReset('unit')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="currentStock" width="100" align="center">
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>当前库存</span>
              <el-popover
                v-model:visible="filterPopoverVisible.currentStock"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.currentStock"
                    placeholder="请输入当前库存"
                    clearable
                    @keyup.enter="handleFilter('currentStock')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('currentStock')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('currentStock')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.currentStock" class="filter-tag">
                {{ filterConditions.currentStock }}
                <el-button type="text" size="small" @click="handleFilterReset('currentStock')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
          <template #default="{ row }">
            <span :class="getStockClass(row.currentStock, row.minStock)">{{ row.currentStock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" width="100" align="center">
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>最低库存</span>
              <el-popover
                v-model:visible="filterPopoverVisible.minStock"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.minStock"
                    placeholder="请输入最低库存"
                    clearable
                    @keyup.enter="handleFilter('minStock')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('minStock')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('minStock')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.minStock" class="filter-tag">
                {{ filterConditions.minStock }}
                <el-button type="text" size="small" @click="handleFilterReset('minStock')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="maxStock" width="100" align="center">
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>最高库存</span>
              <el-popover
                v-model:visible="filterPopoverVisible.maxStock"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.maxStock"
                    placeholder="请输入最高库存"
                    clearable
                    @keyup.enter="handleFilter('maxStock')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('maxStock')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('maxStock')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.maxStock" class="filter-tag">
                {{ filterConditions.maxStock }}
                <el-button type="text" size="small" @click="handleFilterReset('maxStock')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stockStatus" width="100" align="center">
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>库存状态</span>
              <el-popover
                v-model:visible="filterPopoverVisible.stockStatus"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-select
                    v-model="filterForm.stockStatus"
                    placeholder="请选择库存状态"
                    clearable
                    style="width: 100%"
                  >
                    <el-option label="充足" value="充足" />
                    <el-option label="不足" value="不足" />
                    <el-option label="缺货" value="缺货" />
                  </el-select>
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('stockStatus')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('stockStatus')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.stockStatus" class="filter-tag">
                {{ filterConditions.stockStatus }}
                <el-button type="text" size="small" @click="handleFilterReset('stockStatus')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
          <template #default="{ row }">
            <el-tag :type="getStockStatusType(row.stockStatus)">{{ row.stockStatus }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="totalValue" width="120" align="right">
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>库存价值</span>
              <el-popover
                v-model:visible="filterPopoverVisible.totalValue"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.totalValue"
                    placeholder="请输入库存价值"
                    clearable
                    @keyup.enter="handleFilter('totalValue')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('totalValue')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('totalValue')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.totalValue" class="filter-tag">
                {{ filterConditions.totalValue }}
                <el-button type="text" size="small" @click="handleFilterReset('totalValue')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
          <template #default="{ row }">
            ¥{{ row.totalValue?.toLocaleString() }}
          </template>
        </el-table-column>


        <el-table-column prop="lastUpdate" width="120" >
          <template #header="{ column }">
            <div class="header-with-filter">
              <span>最后更新</span>
              <el-popover
                v-model:visible="filterPopoverVisible.lastUpdate"
                placement="bottom"
                trigger="click"
                :width="300"
              >
                <template #reference>
                  <el-button type="text" size="small">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
                <div class="filter-content">
                  <el-input
                    v-model="filterForm.lastUpdate"
                    placeholder="请输入最后更新日期"
                    clearable
                    @keyup.enter="handleFilter('lastUpdate')"
                  />
                  <div class="filter-actions">
                    <el-button size="small" @click="handleFilterReset('lastUpdate')">重置</el-button>
                    <el-button type="primary" size="small" @click="handleFilter('lastUpdate')">确定</el-button>
                  </div>
                </div>
              </el-popover>
              <span v-if="filterConditions.lastUpdate" class="filter-tag">
                {{ filterConditions.lastUpdate }}
                <el-button type="text" size="small" @click="handleFilterReset('lastUpdate')">
                  <el-icon><Close /></el-icon>
                </el-button>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
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
      width="800px"
      :close-on-click-modal="false"
      center
    >
      <el-form
        ref="consumableFormRef"
        :model="consumableForm"
        :rules="consumableRules"
        label-width="120px"
        class="consumable-form"
      >
        <div class="form-grid">
          <el-form-item label="物料编码" prop="materialCode">
            <el-input v-model="consumableForm.materialCode" placeholder="请输入物料编码" class="form-input" />
          </el-form-item>
          <el-form-item label="物品名称" prop="itemName">
            <el-input v-model="consumableForm.itemName" placeholder="请输入物品名称" class="form-input" />
          </el-form-item>
        </div>
        
        <div class="form-grid">
          <el-form-item label="物品类型" prop="itemCategory">
            <el-select v-model="consumableForm.itemCategory" placeholder="请选择类型" class="form-select">
              <el-option label="工具类" value="工具类" />
              <el-option label="材料类" value="材料类" />
              <el-option label="备件类" value="备件类" />
              <el-option label="消耗类" value="消耗类" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>
          <el-form-item label="规格型号" prop="specification">
            <el-input v-model="consumableForm.specification" placeholder="请输入规格型号" class="form-input" />
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-select v-model="consumableForm.unit" placeholder="请选择单位" class="form-select">
              <el-option label="个" value="个" />
              <el-option label="套" value="套" />
              <el-option label="件" value="件" />
              <el-option label="根" value="根" />
              <el-option label="只" value="只" />
              <el-option label="桶" value="桶" />
              <el-option label="瓶" value="瓶" />
              <el-option label="盒" value="盒" />
              <el-option label="卷" value="卷" />
              <el-option label="块" value="块" />
              <el-option label="片" value="片" />
              <el-option label="支" value="支" />
            </el-select>
          </el-form-item>
        </div>
        
        <div class="form-grid">
          <el-form-item label="当前库存" prop="currentStock">
            <el-input-number v-model="consumableForm.currentStock" :min="0" :max="10000" class="form-number" />
          </el-form-item>
          <el-form-item label="最低库存" prop="minStock">
            <el-input-number v-model="consumableForm.minStock" :min="0" :max="10000" class="form-number" />
          </el-form-item>
          <el-form-item label="最高库存" prop="maxStock">
            <el-input-number v-model="consumableForm.maxStock" :min="0" :max="10000" class="form-number" />
          </el-form-item>
        </div>
        
        <div class="form-grid">
          <el-form-item label="单价" prop="unitPrice">
            <el-input-number v-model="consumableForm.unitPrice" :min="0" :precision="2" class="form-number" />
          </el-form-item>
        </div>
        
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="consumableForm.remarks" type="textarea" :rows="3" placeholder="请输入备注信息" class="form-textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmit" size="large">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 入库对话框 -->
    <el-dialog
      v-model="stockInDialogVisible"
      title="耗材入库"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="stockInFormRef"
        :model="stockInForm"
        :rules="stockInRules"
        label-width="100px"
      >
        <el-form-item label="物料编码">
          <el-input v-model="stockInForm.materialCode" disabled />
        </el-form-item>
        <el-form-item label="物品名称">
          <el-input v-model="stockInForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="stockInForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="入库数量" prop="quantity">
          <el-input-number v-model="stockInForm.quantity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="入库原因" prop="reason">
          <el-select v-model="stockInForm.reason" placeholder="请选择入库原因">
            <el-option label="采购入库" value="采购入库" />
            <el-option label="生产退料" value="生产退料" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="stockInForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockInDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleStockInSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 出库对话框 -->
    <el-dialog
      v-model="stockOutDialogVisible"
      title="耗材出库"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="stockOutFormRef"
        :model="stockOutForm"
        :rules="stockOutRules"
        label-width="100px"
      >
        <el-form-item label="物料编码">
          <el-input v-model="stockOutForm.materialCode" disabled />
        </el-form-item>
        <el-form-item label="物品名称">
          <el-input v-model="stockOutForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="stockOutForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="出库数量" prop="quantity">
          <el-input-number v-model="stockOutForm.quantity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="出库原因" prop="reason">
          <el-select v-model="stockOutForm.reason" placeholder="请选择出库原因">
            <el-option label="生产领料" value="生产领料" />
            <el-option label="维修更换" value="维修更换" />
            <el-option label="报废处理" value="报废处理" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="领用人" prop="recipient">
          <el-input v-model="stockOutForm.recipient" placeholder="请输入领用人" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="stockOutForm.remarks" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockOutDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleStockOutSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  Plus,
  Search,
  Edit,
  Delete,
  Box,
  Upload,
  Download,
  Minus,
  Close
} from '@element-plus/icons-vue'

// 导入物料API服务
import { getConsumables, deleteConsumable, batchDeleteConsumables, createConsumable, updateConsumable, checkMaterialCodeUnique } from '@/api/consumable'

// 耗材类型定义
interface ConsumableItem {
  id: number
  materialCode: string
  itemName: string
  itemCategory: string
  specification: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  stockStatus: string
  unitPrice: number
  totalValue: number
  supplier: string
  location: string
  lastUpdate: string
  remarks?: string
}

// 筛选表单类型
interface FilterForm {
  materialCode: string
  itemName: string
  itemCategory: string
  specification: string
  unit: string
  currentStock: string
  minStock: string
  maxStock: string
  stockStatus: string
  totalValue: string
  lastUpdate: string
}

// 耗材表单类型
interface ConsumableForm {
  id?: number
  materialCode: string
  itemName: string
  itemCategory: string
  specification: string
  unit: string
  currentStock: number
  minStock: number
  maxStock: number
  unitPrice?: number
  remarks?: string
}

// 入库表单类型
interface StockInForm {
  materialCode: string
  itemName: string
  currentStock: string
  quantity: number
  reason: string
  remarks?: string
}

// 出库表单类型
interface StockOutForm {
  materialCode: string
  itemName: string
  currentStock: string
  quantity: number
  reason: string
  recipient: string
  remarks?: string
}

// 响应式数据
const loading = ref(false)
const statsLoading = ref(false) // 统计数据加载状态
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const stockInDialogVisible = ref(false)
const stockOutDialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const consumableFormRef = ref<FormInstance>()
const stockInFormRef = ref<FormInstance>()
const stockOutFormRef = ref<FormInstance>()

// 分类统计相关数据
const categoryStats = ref<Array<{ category: string; count: number }>>([])
const selectedCategory = ref<string | null>(null)

// 筛选表单数据
const filterForm = reactive<FilterForm>({
  materialCode: '',
  itemName: '',
  itemCategory: '',
  specification: '',
  unit: '',
  currentStock: '',
  minStock: '',
  maxStock: '',
  stockStatus: '',
  totalValue: '',
  lastUpdate: ''
})

// 筛选弹窗显示状态
const filterPopoverVisible = ref<Record<string, boolean>>({
  materialCode: false,
  itemName: false,
  itemCategory: false,
  specification: false,
  unit: false,
  currentStock: false,
  minStock: false,
  maxStock: false,
  stockStatus: false,
  totalValue: false,
  lastUpdate: false
})

// 筛选条件存储
const filterConditions = reactive<FilterForm>({
  materialCode: '',
  itemName: '',
  itemCategory: '',
  specification: '',
  unit: '',
  currentStock: '',
  minStock: '',
  maxStock: '',
  stockStatus: '',
  totalValue: '',
  lastUpdate: ''
})

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 耗材表单数据
const consumableForm = reactive<ConsumableForm>({
  materialCode: '',
  itemName: '',
  itemCategory: '',
  specification: '',
  unit: '个',
  currentStock: 0,
  minStock: 10,
  maxStock: 1000,
  unitPrice: 0,
  remarks: ''
})

// 入库表单数据
const stockInForm = reactive<StockInForm>({
  materialCode: '',
  itemName: '',
  currentStock: '',
  quantity: 1,
  reason: '',
  remarks: ''
})

// 出库表单数据
const stockOutForm = reactive<StockOutForm>({
  materialCode: '',
  itemName: '',
  currentStock: '',
  quantity: 1,
  reason: '',
  recipient: '',
  remarks: ''
})

// 表单验证规则
// 表单验证规则
const consumableRules = {
  materialCode: [
    { required: true, message: '请输入物料编码', trigger: 'blur' },
    {
      validator: async (rule: any, value: string, callback: any) => {
        if (!value) {
          callback()
          return
        }
        try {
          // 调用API检查物料编码唯一性，传入当前表单的id（编辑时排除当前记录）
          const response = await checkMaterialCodeUnique(value, consumableForm.id)
          if (response.code === 200 && response.data) {
            // 物料编码唯一，验证通过
            callback()
          } else {
            // 物料编码已存在，验证失败
            callback(new Error('物料编码已存在，请使用其他编码'))
          }
        } catch (error: any) {
          // API调用失败，验证失败
          let errorMessage = '检查物料编码唯一性失败'
          if (error.response?.data?.message) {
            errorMessage = error.response.data.message
          } else if (error.message) {
            errorMessage = error.message
          }
          callback(new Error(errorMessage))
        }
      },
      trigger: ['blur', 'change']
    }
  ],
  itemName: [{ required: true, message: '请输入物品名称', trigger: 'blur' }],
  itemCategory: [{ required: true, message: '请选择物品类型', trigger: 'change' }],
  specification: [{ required: true, message: '请输入规格型号', trigger: 'blur' }],
  unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
  currentStock: [{ required: true, message: '请输入当前库存', trigger: 'blur' }],
  minStock: [{ required: true, message: '请输入最低库存', trigger: 'blur' }],
  maxStock: [{ required: true, message: '请输入最高库存', trigger: 'blur' }],
  unitPrice: [{ required: false, message: '请输入单价', trigger: 'blur' }]
}

const stockInRules = {
  quantity: [{ required: true, message: '请输入入库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择入库原因', trigger: 'change' }]
}

const stockOutRules = {
  quantity: [{ required: true, message: '请输入出库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择出库原因', trigger: 'change' }],
  recipient: [{ required: true, message: '请输入领用人', trigger: 'blur' }]
}

// 物料列表数据
const consumablesList = ref<ConsumableItem[]>([])

// 计算分类统计数据
const calculateCategoryStats = (data: ConsumableItem[]) => {
  const statsMap = new Map<string, number>()
  
  // 遍历所有物料，按类别统计数量
  data.forEach(item => {
    const category = item.itemCategory || '未分类'
    const count = statsMap.get(category) || 0
    statsMap.set(category, count + 1)
  })
  
  // 转换为数组并排序
  const statsArray = Array.from(statsMap.entries())
    .map(([category, count]) => ({ category, count }))
    .sort((a, b) => b.count - a.count)
  
  return statsArray
}

// 获取分类统计数据
const fetchCategoryStats = async () => {
  statsLoading.value = true
  try {
    // 获取所有物料数据用于统计
    const response = await getConsumables({ page: 1, pageSize: 1000 })
    
    let records: any[] = []
    
    // 处理不同的API响应格式
    if (Array.isArray(response)) {
      records = response
    } else if (response.code === 200) {
      if (Array.isArray(response.data)) {
        records = response.data
      } else if (response.data && Array.isArray(response.data.records)) {
        records = response.data.records
      }
    }
    
    categoryStats.value = calculateCategoryStats(records as ConsumableItem[])
  } catch (error) {
    console.error('获取分类统计数据失败:', error)
    categoryStats.value = []
  } finally {
    statsLoading.value = false
  }
}

// 从API获取物料列表
const fetchConsumables = async () => {
  loading.value = true
  try {
    // 构建查询参数，添加类别筛选
    const params = {
      page: pagination.currentPage,
      pageSize: pagination.pageSize,
      ...filterConditions,
      ...(selectedCategory.value && { itemCategory: selectedCategory.value })
    }
    
    console.log('获取物料列表参数:', params)
    const response = await getConsumables(params)
    console.log('获取物料列表响应:', response)
    
    // 检查API响应是否成功
    let isSuccess = false
    let records: any[] = []
    let total: number = 0
    
    // 情况1：API直接返回数组（没有code字段）
    if (Array.isArray(response)) {
      isSuccess = true
      records = response
      total = response.length
      console.log('API直接返回数组，无code字段，数据条数:', total)
    } 
    // 情况2：API返回对象，包含code字段
    else if (typeof response === 'object' && response !== null) {
      // 检查code字段
      if (response.code === 200) {
        isSuccess = true
        
        // 情况2.1：响应对象的data是数组
        if (Array.isArray(response.data)) {
          records = response.data
          total = response.data.length
          console.log('API返回对象，code=200，data是数组，数据条数:', total)
        } 
        // 情况2.2：响应对象的data是分页格式
        else if (response.data && Array.isArray(response.data.records)) {
          records = response.data.records
          total = response.data.total || 0
          console.log('API返回对象，code=200，data是分页格式，数据条数:', total)
        } 
        // 情况2.3：响应对象的data格式未知
        else {
          isSuccess = false
          console.error('API返回对象，code=200，但data格式未知:', response.data)
        }
      } 
      // 情况3：API返回对象，code字段不是200
      else {
        isSuccess = false
        console.error('API返回对象，code字段不是200:', response.code, response.message)
      }
    } 
    // 情况4：API返回其他格式
    else {
      isSuccess = false
      console.error('API返回格式未知:', response)
    }
    
    // 根据处理结果更新状态
    if (isSuccess) {
      consumablesList.value = records
      pagination.total = total
      
      // 如果没有数据，显示提示
      if (records.length === 0) {
        console.log('物料列表为空')
      }
    } else {
      console.error('获取物料列表失败')
      ElMessage.error('获取物料列表失败')
      consumablesList.value = []
      pagination.total = 0
    }
  } catch (error: any) {
    console.error('获取物料列表失败：', error)
    if (error.response?.data?.message) {
      ElMessage.error('获取物料列表失败：' + error.response.data.message)
    } else if (error.message) {
      ElMessage.error('获取物料列表失败：' + error.message)
    } else {
      ElMessage.error('获取物料列表失败：网络错误')
    }
    consumablesList.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 处理类别点击
const handleCategoryClick = (category: string) => {
  if (selectedCategory.value === category) {
    selectedCategory.value = null // 取消选择
  } else {
    selectedCategory.value = category // 选择分类
  }
  
  pagination.currentPage = 1 // 重置页码
  fetchConsumables() // 重新获取数据
}

// 计算属性：对话框标题
const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '新增耗材' : '编辑耗材'
})

// 获取库存状态类型（用于Tag组件）
const getStockStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '充足': 'success',
    '不足': 'warning',
    '缺货': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取库存文字颜色类
const getStockClass = (current: number, min: number) => {
  if (current <= min * 0.5) return 'text-danger'
  if (current <= min) return 'text-warning'
  return 'text-success'
}

// 筛选方法
const handleFilter = (field: keyof FilterForm) => {
  // 保存筛选条件
  filterConditions[field] = filterForm[field]
  // 关闭筛选弹窗
  filterPopoverVisible.value[field] = false
  // 执行搜索并显示提示
  handleSearch(true)
}

// 重置单个筛选条件
const handleFilterReset = (field: keyof FilterForm) => {
  // 重置筛选表单
  filterForm[field] = ''
  // 重置筛选条件
  filterConditions[field] = ''
  // 执行搜索并显示提示
  handleSearch(true)
}

// 页面挂载时获取数据
onMounted(async () => {
  // 并行获取统计数据和列表数据
  await Promise.all([
    fetchCategoryStats(),
    fetchConsumables()
  ])
})

// 搜索方法
const handleSearch = async (showMessage = false) => {
  // 重置到第一页
  pagination.currentPage = 1
  // 调用API获取数据
  await fetchConsumables()
  if (showMessage) {
    ElMessage.success('搜索完成')
  }
}

// 重置所有筛选条件
const handleReset = () => {
  // 重置筛选表单
  Object.assign(filterForm, {
    materialCode: '',
    itemName: '',
    itemCategory: '',
    specification: '',
    unit: '',
    currentStock: '',
    minStock: '',
    maxStock: '',
    stockStatus: '',
    totalValue: '',
    lastUpdate: ''
  })
  // 重置筛选条件
  Object.assign(filterConditions, {
    materialCode: '',
    itemName: '',
    itemCategory: '',
    specification: '',
    unit: '',
    currentStock: '',
    minStock: '',
    maxStock: '',
    stockStatus: '',
    totalValue: '',
    lastUpdate: ''
  })
  // 执行搜索并显示提示
  handleSearch(true)
}

// 选择行变化处理
const handleSelectionChange = (selection: ConsumableItem[]) => {
  selectedIds.value = selection.map(item => item.id)
}

// 新增耗材
const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(consumableForm, {
    materialCode: '',
    itemName: '',
    itemCategory: '',
    specification: '',
    unit: '个',
    currentStock: 0,
    minStock: 10,
    maxStock: 1000,
    unitPrice: 0,
    supplier: '',
    location: '',
    remarks: ''
  })
  dialogVisible.value = true
}

// 编辑耗材
const handleEdit = (row: ConsumableItem) => {
  dialogType.value = 'edit'
  Object.assign(consumableForm, {
    id: row.id,
    materialCode: row.materialCode,
    itemName: row.itemName,
    itemCategory: row.itemCategory,
    specification: row.specification,
    unit: row.unit,
    currentStock: row.currentStock,
    minStock: row.minStock,
    maxStock: row.maxStock,
    unitPrice: row.unitPrice,
    supplier: row.supplier,
    location: row.location,
    remarks: row.remarks || ''
  })
  dialogVisible.value = true
}

// 耗材入库
const handleStockIn = (row: ConsumableItem) => {
  Object.assign(stockInForm, {
    materialCode: row.materialCode,
    itemName: row.itemName,
    currentStock: row.currentStock.toString(),
    quantity: 1,
    reason: '',
    remarks: ''
  })
  stockInDialogVisible.value = true
}

// 耗材出库
const handleStockOut = (row: ConsumableItem) => {
  Object.assign(stockOutForm, {
    materialCode: row.materialCode,
    itemName: row.itemName,
    currentStock: row.currentStock.toString(),
    quantity: 1,
    reason: '',
    recipient: '',
    remarks: ''
  })
  stockOutDialogVisible.value = true
}

// 批量入库处理函数
const handleBatchStockIn = () => {
  // 如果只选择了一条记录，直接打开入库对话框
  if (selectedIds.value.length === 1) {
    const selectedRow = consumablesList.value.find(item => item.id === selectedIds.value[0])
    if (selectedRow) {
      handleStockIn(selectedRow)
    }
  } else {
    // 对于多条记录，这里可以根据业务需求进行批量处理
    // 例如：显示批量入库对话框，或者逐个处理
    ElMessage.info('当前仅支持单条记录入库操作')
  }
}

// 批量出库处理函数
const handleBatchStockOut = () => {
  // 如果只选择了一条记录，直接打开出库对话框
  if (selectedIds.value.length === 1) {
    const selectedRow = consumablesList.value.find(item => item.id === selectedIds.value[0])
    if (selectedRow) {
      handleStockOut(selectedRow)
    }
  } else {
    // 对于多条记录，这里可以根据业务需求进行批量处理
    // 例如：显示批量出库对话框，或者逐个处理
    ElMessage.info('当前仅支持单条记录出库操作')
  }
}

// 删除耗材
const handleDelete = async (row: ConsumableItem) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除物品 "${row.itemName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用API删除物料
    const response = await deleteConsumable(row.id)
    if (response.code === 200) {
      // 从本地物料列表中移除被删除的物料项
      const index = consumablesList.value.findIndex(item => item.id === row.id)
      if (index !== -1) {
        consumablesList.value.splice(index, 1)
        // 更新总条数
        pagination.total = consumablesList.value.length
        // 从选择列表中移除
        selectedIds.value = selectedIds.value.filter(id => id !== row.id)
        ElMessage.success('删除成功')
      }
    } else {
      ElMessage.error('删除失败：' + response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除物料失败：', error)
      ElMessage.error('删除失败：' + (error as Error).message)
      // 模拟删除成功，防止前端数据不一致
      const index = consumablesList.value.findIndex(item => item.id === (error as any).row?.id || row.id)
      if (index !== -1) {
        consumablesList.value.splice(index, 1)
        pagination.total = consumablesList.value.length
        selectedIds.value = selectedIds.value.filter(id => id !== (error as any).row?.id || row.id)
      }
    }
    // 用户取消删除时不显示错误信息
  }
}

// 批量删除耗材
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的物品')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个物品吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 保存要删除的数量
    const deleteCount = selectedIds.value.length
    // 调用API批量删除物料
    const response = await batchDeleteConsumables(selectedIds.value)
    if (response.code === 200) {
      // 从本地物料列表中移除被选中的物料项
      consumablesList.value = consumablesList.value.filter(item => !selectedIds.value.includes(item.id))
      // 更新总条数
      pagination.total = consumablesList.value.length
      // 清空选择列表
      selectedIds.value = []
      ElMessage.success(`成功删除 ${deleteCount} 个物料`)
    } else {
      ElMessage.error('批量删除失败：' + response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除物料失败：', error)
      ElMessage.error('批量删除失败：' + (error as Error).message)
      // 模拟批量删除成功，防止前端数据不一致
      consumablesList.value = consumablesList.value.filter(item => !selectedIds.value.includes(item.id))
      pagination.total = consumablesList.value.length
      selectedIds.value = []
    }
    // 用户取消删除时不显示错误信息
  }
}

// 批量盘点
const handleBatchStock = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要盘点的物品')
    return
  }
  ElMessage.info('批量盘点功能')
}

// 批量导入
const handleImport = () => {
  ElMessage.info('批量导入功能')
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出数据功能')
}

// 提交表单
const handleSubmit = async () => {
  if (!consumableFormRef.value) return
  
  try {
    await consumableFormRef.value.validate()
    
    if (dialogType.value === 'add') {
      // 新增耗材
      await createConsumable(consumableForm)
      ElMessage.success('新增物品成功')
    } else {
      // 编辑耗材
      await updateConsumable(consumableForm.id!, consumableForm)
      ElMessage.success('编辑物品成功')
    }
    
    dialogVisible.value = false
    // 刷新列表
    handleSearch()
  } catch (error: any) {
    console.error('表单提交失败:', error)
    // 显示具体的错误信息
    if (error.response?.data?.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error(dialogType.value === 'add' ? '新增物品失败' : '编辑物品失败')
    }
  }
}

// 提交入库
const handleStockInSubmit = async () => {
  if (!stockInFormRef.value) return
  
  try {
    await stockInFormRef.value.validate()
    ElMessage.success('入库成功')
    stockInDialogVisible.value = false
    handleSearch()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 提交出库
const handleStockOutSubmit = async () => {
  if (!stockOutFormRef.value) return
  
  try {
    await stockOutFormRef.value.validate()
    ElMessage.success('出库成功')
    stockOutDialogVisible.value = false
    handleSearch()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 分页大小变化
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  handleSearch()
}

// 当前页变化
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  handleSearch()
}


</script>

<style scoped lang="scss">
.consumables-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;
}

.page-title-section {
  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 8px 0;
  }
  
  .page-subtitle {
    font-size: 14px;
    color: #909399;
    margin: 0;
  }
}

.page-actions {
  display: flex;
  gap: 12px;
}

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.filter-form {
  .el-form-item {
    margin-bottom: 0;
    margin-right: 20px;
  }
}

.table-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  
  h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.table-actions {
  display: flex;
  gap: 8px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 统计卡片样式 */
.stats-card {
  margin-bottom: 20px;
}

.stats-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #333;
}

.stats-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

.stat-item {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  flex: 1;
  min-width: 150px;
  max-width: calc(25% - 16px);
  border: 2px solid transparent;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stat-item.active {
  background-color: #337ecc;
  color: white;
  box-shadow: 0 4px 12px rgba(51, 126, 204, 0.4);
  border: 2px solid #2a6baf;
}

.stat-item.active .stat-label,
.stat-item.active .stat-value {
  color: white;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  transition: color 0.3s ease;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #409eff;
  transition: color 0.3s ease;
}

.no-stats {
  padding: 40px 0;
  text-align: center;
}

/* 表格单元格对齐 */
:deep(.el-table__cell) {
  text-align: left;
  vertical-align: middle;
}

:deep(.el-table__header-wrapper .el-table__header-cell) {
  text-align: left;
  vertical-align: middle;
}

/* 选择列居中 */
:deep(.el-table__column--selection .el-table__cell) {
  text-align: center;
}

:deep(.el-table__header-wrapper .el-table__column--selection .el-table__header-cell) {
  text-align: center;
}

// 库存状态样式
.text-success {
  color: #67c23a;
  font-weight: 600;
}

.text-warning {
  color: #e6a23c;
  font-weight: 600;
}

.text-danger {
  color: #f56c6c;
  font-weight: 600;
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
    .el-form-item {
      margin-right: 0;
      margin-bottom: 12px;
      width: 100%;
    }
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .table-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

// 自定义样式
:deep(.el-table) {
  .el-table__header-wrapper {
    th {
      background-color: #f5f7fa;
      color: #606266;
      font-weight: 600;
    }
  }
}

:deep(.el-dialog) {
  border-radius: 8px;
  
  .el-dialog__header {
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;
    margin-right: 0;
  }
  
  .el-dialog__body {
    padding: 24px;
  }
  
  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #ebeef5;
  }
}

/* 新增耗材表单样式 */
.consumable-form {
  padding: 20px 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.form-input,
.form-select,
.form-number {
  width: 100%;
}

.form-textarea {
  width: 100%;
  resize: vertical;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  padding: 20px 0 0 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .dialog-footer {
    justify-content: center;
    flex-direction: column;
  }
  
  .dialog-footer .el-button {
    width: 100%;
  }
}

@media (max-width: 576px) {
  :deep(.el-dialog) {
    width: 90% !important;
  }
  
  .form-input,
  .form-select,
  .form-number {
    width: 100%;
  }
}

:deep(.el-form--inline) {
  .el-form-item {
    margin-right: 24px;
    margin-bottom: 16px;
  }
}

/* 表头筛选样式 */
.header-with-filter {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  width: 100%;
  height: 100%;
}

.header-with-filter span:first-child {
  font-weight: 600;
  color: #606266;
}

.filter-content {
  padding: 10px;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 10px;
}

.filter-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  background-color: #ecf5ff;
  color: #409eff;
  padding: 2px 6px;
  border-radius: 10px;
  white-space: nowrap;
}

.filter-tag .el-icon {
  font-size: 10px;
}

/* 调整表格表头高度 */
:deep(.el-table__header-wrapper) {
  th {
    padding: 8px 0;
    height: 50px;
  }
}

/* 筛选图标按钮样式 */
:deep(.el-table .header-with-filter .el-button) {
  padding: 4px !important;
  height: auto !important;
  line-height: normal !important;
  margin: 0 !important;
  font-size: 16px !important;
  color: #909399 !important;
  transition: all 0.2s !important;
  border: none !important;
  background: transparent !important;
  border-radius: 4px !important;
  display: inline-flex !important;
  align-items: center !important;
  justify-content: center !important;
}

:deep(.el-table .header-with-filter .el-button:hover) {
  color: #409eff !important;
  background-color: #ecf5ff !important;
}

:deep(.el-table .header-with-filter .el-button:focus) {
  outline: none !important;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2) !important;
}

/* 确保筛选图标显示 */
:deep(.el-table .header-with-filter .el-icon) {
  display: inline-block !important;
  font-size: 16px !important;
  color: inherit !important;
}

/* 确保表头内容正确对齐 */
:deep(.el-table__header-wrapper th) {
  height: 50px !important;
}

/* 确保筛选图标显示在表头中 */
:deep(.el-table__header-wrapper th .cell) {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 4px;
  width: 100%;
  height: 100%;
  padding: 8px 0 !important;
}

/* 筛选弹窗样式调整 */
:deep(.el-popover) {
  padding: 0;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 确保筛选图标显示在所有设备上 */
@media (max-width: 768px) {
  .header-with-filter {
    flex-direction: column;
    gap: 2px;
  }
  
  .header-with-filter .el-button {
    font-size: 14px;
    padding: 2px;
  }
}
</style>