<template>
  <!-- 地表位移图表组件 -->
  <div class="bg-white rounded-lg shadow p-4">
    <!-- 加载状态 -->
    <div v-show="loading" class="flex items-center justify-center h-96">
      <i class="fa fa-spinner fa-spin text-3xl text-primary-500" aria-hidden="true"></i>
      <span class="ml-3 text-gray-500">图表加载中...</span>
    </div>

    <!-- 图表区域（使用 v-show 保留 DOM） -->
    <div v-show="!loading" ref="chartRef" class="w-full h-96"></div>

    <!-- 统计表格 -->
    <div class="mt-6 pt-4 border-t border-gray-100">
      <h3 class="text-base font-medium text-gray-800 mb-3">均值统计</h3>
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead>
            <tr class="bg-gray-50">
              <th class="px-4 py-3 text-left font-medium text-gray-600">监测属性</th>
              <th class="px-4 py-3 text-right font-medium text-gray-600">均值</th>
              <th class="px-4 py-3 text-right font-medium text-gray-600">最大值</th>
              <th class="px-4 py-3 text-right font-medium text-gray-600">最小值</th>
              <th class="px-4 py-3 text-right font-medium text-gray-600">标准差</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(row, index) in statRows"
              :key="index"
              class="border-b border-gray-100 hover:bg-gray-50"
            >
              <td class="px-4 py-3 text-gray-800">{{ row.attr }}</td>
              <td class="px-4 py-3 text-right text-gray-600">{{ row.mean }}</td>
              <td class="px-4 py-3 text-right text-gray-600">{{ row.max }}</td>
              <td class="px-4 py-3 text-right text-gray-600">{{ row.min }}</td>
              <td class="px-4 py-3 text-right text-gray-600">{{ row.std }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 地表位移图表组件
 * 功能：ECharts 多系列折线图 + 统计表格
 */
import { ref, watch, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  chartData: { type: Array, default: () => [] },
  statRows: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

const chartRef = ref(null)
const chartInstance = shallowRef(null)

// 图表配置
const seriesConfig = [
  { name: 'X位移', key: 'x', color: '#3B82F6' },
  { name: 'Y位移', key: 'y', color: '#10B981' },
  { name: 'Z位移', key: 'z', color: '#EF4444' },
  { name: '合位移', key: 'd3', color: '#F59E0B' },
  { name: '水平位移', key: 'd2', color: '#6366F1' }
]

// 渲染图表
function renderChart() {
  if (!chartRef.value) return

  if (!chartInstance.value) {
    chartInstance.value = echarts.init(chartRef.value)
  }

  const data = props.chartData
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: { color: '#374151' }
    },
    legend: {
      data: seriesConfig.map(s => s.name),
      top: 0,
      left: 'center',
      textStyle: { color: '#6B7280' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '8%',
      top: '12%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.map(d => d.time),
      axisLabel: {
        color: '#9CA3AF',
        formatter: (val) => val ? val.slice(5, 16) : ''
      },
      axisLine: { lineStyle: { color: '#E5E7EB' } },
      splitLine: { show: false }
    },
    yAxis: {
      type: 'value',
      name: 'mm',
      nameTextStyle: { color: '#9CA3AF' },
      axisLabel: { color: '#9CA3AF' },
      axisLine: { lineStyle: { color: '#E5E7EB' } },
      splitLine: { lineStyle: { type: 'dashed', color: '#F3F4F6' } }
    },
    series: seriesConfig.map(s => ({
      name: s.name,
      type: 'line',
      data: data.map(d => d[s.key]),
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: { width: 2, color: s.color },
      itemStyle: { color: s.color },
      emphasis: { focus: 'series' }
    }))
  }

  chartInstance.value.setOption(option, true)
}

// 监听数据变化
watch(() => props.chartData, () => {
  renderChart()
}, { deep: true })

// 窗口resize处理
function handleResize() {
  chartInstance.value?.resize()
}

onMounted(() => {
  renderChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance.value?.dispose()
})
</script>
