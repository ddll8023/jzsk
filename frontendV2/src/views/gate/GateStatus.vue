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
      @refresh="fetchGateData"
    />

    <!-- 数据表格 -->
    <GateDataTable
      :columns="tableColumns"
      :data="realtimeData"
      :loading="loading"
    />
  </div>
</template>

<script setup>
/**
 * 闸门实时状态页面
 * 功能：闸门实时状态查询、SVG可视化、数据表格
 * 依赖组件：GateQueryPanel, GateStatusCard, GateDataTable
 */
import { computed } from 'vue'
import { useGateStatus } from '@/composables/useGateStatus'
import GateQueryPanel from '@/components/business/gate/GateQueryPanel.vue'
import GateStatusCard from '@/components/business/gate/GateStatusCard.vue'
import GateDataTable from '@/components/business/gate/GateDataTable.vue'

// 使用 Composable
const {
  gateList,
  loading,
  allTableData,
  tableColumns,
  latestData,
  query,
  currentGateName,
  latestKD,
  waterPercent,
  latestCollectTime,
  setQuickDateRange,
  fetchGateData,
  exportData
} = useGateStatus()

// 实时数据（仅最新一条）
const realtimeData = computed(() => {
  if (!allTableData.value?.length) return []
  return [allTableData.value[0]]
})

// 处理闸门变化
function handleGateChange(gateCode) {
  query.selectedGate = gateCode
  fetchGateData()
}

// 处理时间范围变化
function handleDateRangeChange(range) {
  query.dateRange = range
  query.quickType = 'custom'
}

// 处理快捷选择
function handleQuickSelect(type) {
  setQuickDateRange(type)
  fetchGateData()
}
</script>
