<template>
  <!-- 渗流量监测分析页面 -->
  <div class="min-h-screen bg-gray-50 p-6">
    <!-- 查询面板 -->
    <SeepageFlowQueryPanel
      :station-list="stationList"
      v-model:station-id="query.stationId"
      v-model:date-range-type="query.dateRangeType"
      v-model:date-range="query.dateRange"
      :display-date-range="displayDateRange"
      @search="onSearch"
      @export="exportData"
    />

    <!-- 图表 -->
    <div class="mb-4">
      <SeepageFlowChart
        :time-axis="timeAxis"
        :flow-data="flowData"
        :loading="chartLoading"
      />
    </div>

    <!-- 数据表格 -->
    <SeepageFlowTable
      :table-data="pagedData"
      :total="total"
      :current-page="query.current"
      :page-size="query.size"
      :loading="loading"
      @page-change="onPageChange"
      @size-change="onSizeChange"
    />
  </div>
</template>

<script setup>
/**
 * 渗流量监测分析页面
 * 功能：渗流量数据查询、图表展示、数据导出
 * 依赖组件：SeepageFlowQueryPanel, SeepageFlowChart, SeepageFlowTable
 */
import { ref, computed, watch, onMounted } from 'vue'
import { useSeepageFlow } from '@/composables/useSeepageFlow'
import SeepageFlowQueryPanel from '@/components/business/dam/SeepageFlowQueryPanel.vue'
import SeepageFlowChart from '@/components/business/dam/SeepageFlowChart.vue'
import SeepageFlowTable from '@/components/business/dam/SeepageFlowTable.vue'

// 使用渗流量数据Composable
const {
  stationList,
  loading,
  chartLoading,
  pagedData,
  total,
  chartData,
  query,
  displayDateRange,
  setQuickDateRange,
  fetchData,
  updateChartData,
  generateTimeAxis,
  generateFlowData,
  onSearch,
  onStationChange,
  onPageChange,
  onSizeChange,
  formatMinute
} = useSeepageFlow()

// 图表时间轴和流量数据
const timeAxis = ref([])
const flowData = ref([])

// 监听时间类型变化
watch(() => query.dateRangeType, (type) => {
  setQuickDateRange(type)
  if (type !== 'custom') {
    onSearch()
  }
})

// 监听测站变化
watch(() => query.stationId, () => {
  onStationChange()
  updateChartDisplay()
})

// 监听图表数据变化
watch(chartData, () => {
  updateChartDisplay()
}, { deep: true })

// 更新图表显示
function updateChartDisplay() {
  timeAxis.value = generateTimeAxis()
  flowData.value = generateFlowData(timeAxis.value)
}

// 导出数据
function exportData() {
  if (!pagedData.value?.length) {
    alert('没有数据可导出！')
    return
  }

  const headers = ['测站名称', '时间', '流量(L/s)']
  const rows = pagedData.value.map(item => [
    item.stationName,
    item.formattedTime,
    (Number(item.q1) * 1000).toFixed(3)
  ])

  let csvContent = '\ufeff' + headers.join(',') + '\n'
  rows.forEach(row => {
    csvContent += row.map(e => `"${e}"`).join(',') + '\n'
  })

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = '渗流量监测数据.csv'
  link.click()
}

// 初始化
onMounted(async () => {
  setQuickDateRange('24h')
  await fetchData()
  updateChartDisplay()
})
</script>
