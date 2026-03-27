<template>
  <!-- 闸门实时状态页面 -->
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

    <!-- 状态卡片 -->
    <GateStatusCard
      :latest-data="latestData"
      :gate-name="currentGateName"
      :latest-k-d="latestKD"
      :water-percent="waterPercent"
      :loading="loading"
      :has-error="hasError"
      @refresh="fetchGateData"
    />

    <!-- 数据表格（分页） -->
    <GateReportTable
      :columns="tableColumns"
      :data="tableData"
      :loading="loading"
      :pagination="paginationWithTotal"
      @update:pagination="updatePagination"
    />
  </div>
</template>

<script setup>
/**
 * 闸门实时状态页面
 * 功能：闸门实时状态查询、状态卡片、数据表格分页展示
 * 依赖组件：GateQueryPanel, GateStatusCard, GateReportTable
 */
import { computed } from 'vue'
import { useGateStatus } from '@/composables/useGateStatus'
import GateQueryPanel from '@/components/business/gate/GateQueryPanel.vue'
import GateStatusCard from '@/components/business/gate/GateStatusCard.vue'
import GateReportTable from '@/components/business/gate/GateReportTable.vue'

// 使用 Composable
const {
  gateList,
  loading,
  tableData,
  tableColumns,
  latestData,
  hasError,
  query,
  pagination,
  currentGateName,
  latestKD,
  waterPercent,
  latestCollectTime,
  totalFiltered,
  setQuickDateRange,
  fetchGateData,
  exportData
} = useGateStatus()

// 动态分页总数
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
