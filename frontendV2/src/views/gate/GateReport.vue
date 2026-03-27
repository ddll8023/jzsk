<template>
  <!-- 闸门报表页面 -->
  <div class="min-h-screen bg-gray-50 p-6">
    <!-- 查询面板 -->
    <GateQueryPanel
      :gate-options="gateList"
      :selected-gate="query.selectedGate"
      :date-range="query.dateRange"
      :quick-type="query.quickType"
      :current-gate-name="currentGateName"
      :latest-collect-time="latestCollectTime"
      :loading="loading"
      @update:selected-gate="handleGateChange"
      @update:date-range="handleDateRangeChange"
      @quick-select="handleQuickSelect"
      @export="exportData"
      @refresh="fetchGateData"
    />

    <!-- 报表表格 -->
    <GateReportTable
      :columns="tableColumns"
      :data="pagedData"
      :loading="loading"
      :pagination="paginationWithTotal"
      @update:pagination="updatePagination"
    />
  </div>
</template>

<script setup>
/**
 * 闸门报表页面
 * 功能：闸门历史数据查询、分页展示、导出
 * 依赖组件：GateQueryPanel, GateReportTable
 * 遵循原则：KISS - 复用现有逻辑，YAGNI - 只实现必需功能
 */
import { computed } from 'vue'
import { useGateStatus } from '@/composables/useGateStatus'
import GateQueryPanel from '@/components/business/gate/GateQueryPanel.vue'
import GateReportTable from '@/components/business/gate/GateReportTable.vue'

// 使用 Composable
const {
  gateList,
  loading,
  tableData,
  tableColumns,
  query,
  pagination,
  currentGateName,
  latestCollectTime,
  totalFiltered,
  pagedData,
  setQuickDateRange,
  fetchGateData,
  exportData,
  handlePageChange,
  handleSizeChange
} = useGateStatus()

// 动态分页总数（基于后端返回的总数）
const paginationWithTotal = computed(() => ({
  ...pagination,
  total: totalFiltered.value
}))

// 处理闸门变化
function handleGateChange(gateCode) {
  query.selectedGate = gateCode
  pagination.current = 1
  fetchGateData()
}

// 处理时间范围变化
function handleDateRangeChange(range) {
  query.dateRange = range
  query.quickType = 'custom'
  pagination.current = 1
  fetchGateData()
}

// 处理快捷选择
function handleQuickSelect(type) {
  setQuickDateRange(type)
  pagination.current = 1
  fetchGateData()
}

// 更新分页
function updatePagination(newPagination) {
  let needFetch = false
  if (newPagination.current && newPagination.current !== pagination.current) {
    pagination.current = newPagination.current
    needFetch = true
  }
  if (newPagination.size && newPagination.size !== pagination.size) {
    pagination.size = newPagination.size
    needFetch = true
  }
  if (needFetch) {
    fetchGateData()
  }
}
</script>
