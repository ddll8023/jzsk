<template>
  <!-- 地表位移监测页面 -->
  <div class="min-h-screen bg-gray-50 p-6">
    <!-- 查询面板 -->
    <SurfaceDisplacementQueryPanel
      :station-options="stationOptions"
      :station-id="query.stationId"
      :date-range="query.dateRange"
      :quick-type="query.quickType"
      :current-station-name="currentStationName"
      :latest-collect-time="latestCollectTime"
      @update:station-id="handleStationChange"
      @update:date-range="handleDateRangeChange"
      @quick-select="handleQuickSelect"
      @export="exportData"
    />

    <!-- 统计卡片 -->
    <SurfaceDisplacementStatCards :cards="statCards" :loading="loading" />

    <!-- 显示模式切换 -->
    <div class="bg-white rounded-lg shadow px-4 py-2 mb-4">
      <div class="flex items-center gap-2">
        <Button
          size="sm"
          :type="query.displayMode === 'chart' ? 'primary' : 'default'"
          icon="line-chart"
          @click="query.displayMode = 'chart'"
        >
          图表
        </Button>
        <Button
          size="sm"
          :type="query.displayMode === 'table' ? 'primary' : 'default'"
          icon="table"
          @click="query.displayMode = 'table'"
        >
          表格
        </Button>
      </div>
    </div>

    <!-- 图表视图 -->
    <SurfaceDisplacementChart
      v-if="query.displayMode === 'chart'"
      :chart-data="chartData"
      :stat-rows="statRows"
      :loading="loading"
    />

    <!-- 表格视图 -->
    <SurfaceDisplacementTable
      v-else
      :table-data="tableData"
      :loading="loading"
    />
  </div>
</template>

<script setup>
/**
 * 地表位移监测页面
 * 功能：地表位移数据查询、图表展示、表格展示
 * 依赖组件：QueryPanel, StatCards, Chart, Table
 */
import { onMounted } from 'vue'
import { useSurfaceDisplacement } from '@/composables/useSurfaceDisplacement'
import Button from '@/components/basic/Button.vue'
import SurfaceDisplacementQueryPanel from '@/components/business/dam/SurfaceDisplacementQueryPanel.vue'
import SurfaceDisplacementStatCards from '@/components/business/dam/SurfaceDisplacementStatCards.vue'
import SurfaceDisplacementChart from '@/components/business/dam/SurfaceDisplacementChart.vue'
import SurfaceDisplacementTable from '@/components/business/dam/SurfaceDisplacementTable.vue'

// 使用 Composable
const {
  stationOptions,
  loading,
  tableData,
  chartData,
  statRows,
  query,
  currentStationName,
  latestCollectTime,
  statCards,
  setQuickDateRange,
  fetchData,
  exportData
} = useSurfaceDisplacement()

// 处理站点变化
function handleStationChange(stationId) {
  query.stationId = stationId
  fetchData()
}

// 处理时间范围变化
function handleDateRangeChange(range) {
  query.dateRange = range
  query.quickType = 'custom'
}

// 处理快捷选择
function handleQuickSelect(type) {
  setQuickDateRange(type)
  fetchData()
}

// 初始化
onMounted(() => {
  setQuickDateRange('today')
  fetchData()
})
</script>
