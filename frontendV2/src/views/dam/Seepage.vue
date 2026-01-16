<template>
  <!-- 渗流压力监测页面 -->
  <div class="min-h-screen bg-gray-50 p-6">
    <!-- 主Tab切换 -->
    <div class="bg-white rounded-lg shadow mb-4 px-4">
      <Tabs
        v-model="mainTab"
        :items="mainTabs"
      />
    </div>

    <!-- 渗流量统计查询 -->
    <template v-if="mainTab === 'seepage'">
      <!-- 查询面板 -->
      <SeepageQueryPanel
        :point-list="pointList"
        v-model:point-id="query.pointId"
        v-model:date-range-type="query.dateRangeType"
        v-model:date-range="query.dateRange"
        :display-date-range="displayDateRange"
        @search="onSearch"
        @export="exportData"
      />

      <!-- 图表 -->
      <div class="mb-4">
        <SeepageChart
          v-model:active-tab="activeTab"
          :chart-data="chartData"
          :chart-config="chartConfig"
          :point-id="query.pointId"
          :loading="chartLoading"
        />
      </div>

      <!-- 数据表格 -->
      <SeepageTable
        :table-data="tableData"
        :total="total"
        :current-page="query.current"
        :page-size="query.size"
        :loading="loading"
        @page-change="onPageChange"
      />
    </template>

    <!-- 浸润线观测图 -->
    <template v-else>
      <PhreaticLineChart />
    </template>
  </div>
</template>

<script setup>
/**
 * 渗流压力监测页面
 * 功能：渗流量统计查询 + 浸润线观测图
 * 依赖组件：SeepageQueryPanel, SeepageChart, SeepageTable, PhreaticLineChart
 */
import { ref, watch, onMounted } from 'vue'
import { useSeepage } from '@/composables/useSeepage'
import SeepageQueryPanel from '@/components/business/dam/SeepageQueryPanel.vue'
import SeepageChart from '@/components/business/dam/SeepageChart.vue'
import SeepageTable from '@/components/business/dam/SeepageTable.vue'
import PhreaticLineChart from '@/components/business/dam/PhreaticLineChart.vue'
import Tabs from '@/components/basic/Tabs.vue'

// 主Tab配置
const mainTabs = [
  { label: '渗流量统计查询', value: 'seepage' },
  { label: '浸润线', value: 'phreatic' }
]
const mainTab = ref('seepage')

// 使用渗流数据Composable
const {
  pointList,
  loading,
  chartLoading,
  tableData,
  total,
  chartData,
  query,
  activeTab,
  chartConfig,
  displayDateRange,
  setQuickDateRange,
  fetchPoints,
  fetchTableData,
  fetchChartData,
  onSearch,
  onPageChange,
  formatMinute,
  parseJsonField
} = useSeepage()

// 监听时间类型变化
watch(() => query.dateRangeType, (type) => {
  setQuickDateRange(type)
  if (type !== 'custom') {
    onSearch()
  }
})

// 监听图表Tab变化
watch(activeTab, () => {
  fetchChartData()
})

// 监听站点变化
watch(() => query.pointId, () => {
  onSearch()
})

// 导出数据
function exportData() {
  if (!tableData.value?.length) {
    alert('没有数据可导出！')
    return
  }

  const headers = ['采集时间', '测站名称', '水位高程', '水位(mm)', '温度(°C)', '水压']
  const rows = tableData.value.map(item => [
    formatMinute(item.time),
    item.pointId,
    parseJsonField(item.resultData, '水位高程'),
    parseJsonField(item.resultData, '水位'),
    parseJsonField(item.originalData, '温度'),
    parseJsonField(item.resultData, '水压')
  ])

  let csvContent = '\ufeff' + headers.join(',') + '\n'
  rows.forEach(row => {
    csvContent += row.map(e => `"${e}"`).join(',') + '\n'
  })

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = '渗流量数据.csv'
  link.click()
}

// 初始化
onMounted(async () => {
  setQuickDateRange('24h')
  await fetchPoints()
  fetchTableData()
})
</script>
