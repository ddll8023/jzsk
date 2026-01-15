<template>
  <div class="min-h-full bg-gray-50 p-6">
    <!-- 页面标题 -->
    <header class="mb-6">
      <h1 class="text-xl font-semibold text-gray-900">降雨查询</h1>
    </header>

    <!-- 搜索栏 -->
    <section class="bg-white rounded-lg shadow-sm p-4 mb-6">
      <div class="flex flex-wrap items-center gap-4">
        <!-- 日期范围选择 -->
        <div class="flex items-center gap-2">
          <label class="text-sm text-gray-600">选择时间</label>
          <input
            v-model="startDate"
            type="datetime-local"
            class="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
          <span class="text-gray-500">至</span>
          <input
            v-model="endDate"
            type="datetime-local"
            class="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>

        <!-- 快捷选项 -->
        <div class="flex items-center gap-2">
          <button
            v-for="shortcut in dateShortcuts"
            :key="shortcut.label"
            class="px-3 py-1.5 text-sm border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
            @click="applyShortcut(shortcut)"
          >
            {{ shortcut.label }}
          </button>
        </div>

        <!-- 操作按钮 -->
        <div class="flex items-center gap-2 ml-auto">
          <Button type="primary" icon="search" @click="handleSearch">查询</Button>
          <Button type="success" icon="download" @click="handleExport">导出</Button>
        </div>
      </div>
    </section>

    <!-- 图表区 -->
    <section class="bg-white rounded-lg shadow-sm p-4 mb-6">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-base font-medium text-gray-900">
          {{ chartType === 'hourly' ? '小时降雨量柱状图' : '每日累计降雨量柱状图' }}
        </h2>
        <!-- 图表类型切换 -->
        <div class="flex items-center gap-2">
          <button
            :class="[
              'px-3 py-1.5 text-sm rounded-md transition-colors',
              chartType === 'hourly'
                ? 'bg-primary-600 text-white'
                : 'border border-gray-300 text-gray-700 hover:bg-gray-50'
            ]"
            @click="chartType = 'hourly'"
          >
            小时降雨量
          </button>
          <button
            :class="[
              'px-3 py-1.5 text-sm rounded-md transition-colors',
              chartType === 'daily'
                ? 'bg-primary-600 text-white'
                : 'border border-gray-300 text-gray-700 hover:bg-gray-50'
            ]"
            @click="chartType = 'daily'"
          >
            每日累计
          </button>
        </div>
      </div>
      <div ref="chartRef" class="w-full h-80"></div>
    </section>

    <!-- 数据表格 -->
    <section class="bg-white rounded-lg shadow-sm">
      <div class="px-4 py-3 border-b border-gray-200">
        <h2 class="text-base font-medium text-gray-900">降雨数据</h2>
      </div>
      <Table
        :columns="tableColumns"
        :data="pagedData"
        :loading="loading"
        :total="filteredData.length"
        :current-page="currentPage"
        :page-size="pageSize"
        row-key="id"
        @page-change="handlePageChange"
      />
    </section>
  </div>
</template>

<script setup>
/**
 * 降雨查询页面
 * 功能：查询和展示降雨数据，支持图表和表格两种展示方式
 * 依赖组件：Button、Table
 */
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// 组件导入
import Button from '@/components/basic/Button.vue'
import Table from '@/components/basic/Table.vue'

// API 导入
import { getHourlyRainfall } from '@/api/water'

// Composable 导入
import {
  parseTimeArray,
  formatTimeArray,
  formatDate,
  getDateShortcuts,
  getDefaultDateRange,
  exportToCSV
} from '@/composables/useWater'

// ==================== 状态定义 ====================

// 数据状态
const allData = ref([])
const loading = ref(false)
const error = ref(null)

// 日期范围
const startDate = ref('')
const endDate = ref('')
const dateShortcuts = getDateShortcuts()

// 图表
const chartRef = ref(null)
const chartType = ref('hourly')
let chartInstance = null

// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// ==================== 表格配置 ====================

const tableColumns = [
  { key: 'stationName', title: '测站名称', width: '200px' },
  { key: 'formattedTime', title: '时间', width: '180px' },
  { key: 'drp', title: '降雨量(mm)', width: '120px' }
]

// ==================== 计算属性 ====================

/**
 * 过滤后的数据
 * 根据日期范围筛选
 */
const filteredData = computed(() => {
  if (!startDate.value || !endDate.value) {
    return allData.value
  }

  const start = new Date(startDate.value)
  const end = new Date(endDate.value)

  return allData.value.filter((item) => {
    const itemTime = parseTimeArray(item.tm)
    return itemTime >= start && itemTime <= end
  })
})

/**
 * 排序后的数据（按时间降序）
 */
const sortedData = computed(() => {
  return [...filteredData.value].sort(
    (a, b) => parseTimeArray(b.tm) - parseTimeArray(a.tm)
  )
})

/**
 * 分页后的数据
 */
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return sortedData.value.slice(start, start + pageSize.value)
})

// ==================== 方法定义 ====================

/**
 * 初始化日期范围为最近一天
 */
const initDateRange = () => {
  const [start, end] = getDefaultDateRange()
  startDate.value = formatDate(start, 'datetime').replace(' ', 'T').slice(0, 16)
  endDate.value = formatDate(end, 'datetime').replace(' ', 'T').slice(0, 16)
}

/**
 * 应用快捷日期选项
 */
const applyShortcut = (shortcut) => {
  const [start, end] = shortcut.value()
  startDate.value = formatDate(start, 'datetime').replace(' ', 'T').slice(0, 16)
  endDate.value = formatDate(end, 'datetime').replace(' ', 'T').slice(0, 16)
  handleSearch()
}

/**
 * 加载数据
 */
const loadData = async () => {
  loading.value = true
  error.value = null

  try {
    const res = await getHourlyRainfall()
    const data = res.data || []

    // 处理数据，添加格式化字段
    allData.value = data.map((item, index) => ({
      ...item,
      id: index,
      formattedTime: formatTimeArray(item.tm),
      stationName: '坝前雨量水位站（新站）'
    }))
  } catch (e) {
    error.value = e.message || '数据加载失败'
    console.error('降雨数据加载失败:', e)
  } finally {
    loading.value = false
  }
}

/**
 * 查询
 */
const handleSearch = () => {
  currentPage.value = 1
  renderChart()
}

/**
 * 导出 CSV
 */
const handleExport = () => {
  if (filteredData.value.length === 0) {
    alert('没有数据可导出')
    return
  }

  exportToCSV({
    data: filteredData.value,
    headers: [
      { label: '测站名称', key: 'stationName' },
      { label: '时间', key: 'formattedTime' },
      { label: '降雨量(mm)', key: 'drp' }
    ],
    filename: '降雨数据'
  })
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
}

/**
 * 渲染图表
 */
const renderChart = () => {
  if (!chartRef.value) return

  // 初始化图表实例
  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
  }

  const data = filteredData.value
  if (!data.length) {
    chartInstance.clear()
    return
  }

  if (chartType.value === 'hourly') {
    renderHourlyChart(data)
  } else {
    renderDailyChart(data)
  }
}

/**
 * 渲染小时降雨量图表
 */
const renderHourlyChart = (data) => {
  // 按时间升序排序
  const chartData = [...data].sort(
    (a, b) => parseTimeArray(a.tm) - parseTimeArray(b.tm)
  )

  const times = chartData.map((item) => formatTimeArray(item.tm))
  const values = chartData.map((item) => item.drp)

  // 计算 Y 轴范围
  const max = Math.max(...values, 1)
  const yMax = Math.ceil(max * 1.2)

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      formatter: (params) => {
        const item = params[0]
        return `${item.name}<br/>降雨量: ${item.value}mm`
      }
    },
    grid: { left: 50, right: 30, top: 40, bottom: 40 },
    xAxis: {
      type: 'category',
      data: times,
      axisLabel: {
        rotate: 0,
        fontSize: 12,
        formatter: (value) => {
          // 根据时间跨度决定显示格式
          const parts = value.split(' ')
          return parts.length > 1 ? parts[1].slice(0, 5) : value
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '降雨量(mm)',
      min: 0,
      max: yMax
    },
    series: [
      {
        name: '降雨量',
        type: 'bar',
        data: values,
        itemStyle: { color: '#3b82f6' }
      }
    ]
  })
}

/**
 * 渲染每日累计降雨量图表
 */
const renderDailyChart = (data) => {
  // 按天分组累加
  const dailyMap = {}
  data.forEach((item) => {
    const dateStr = formatTimeArray(item.tm, 'date')
    if (!dailyMap[dateStr]) {
      dailyMap[dateStr] = 0
    }
    dailyMap[dateStr] += Number(item.drp) || 0
  })

  const days = Object.keys(dailyMap).sort()
  const values = days.map((day) => dailyMap[day])

  // 计算 Y 轴范围
  const max = Math.max(...values, 1)
  const yMax = Math.ceil(max * 1.2)

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      formatter: (params) => {
        const item = params[0]
        return `${item.name}<br/>累计降雨量: ${item.value.toFixed(1)}mm`
      }
    },
    grid: { left: 50, right: 30, top: 40, bottom: 40 },
    xAxis: {
      type: 'category',
      data: days,
      axisLabel: { rotate: 0, fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      name: '累计降雨量(mm)',
      min: 0,
      max: yMax
    },
    series: [
      {
        name: '累计降雨量',
        type: 'bar',
        data: values,
        itemStyle: { color: '#22c55e' }
      }
    ]
  })
}

// ==================== 生命周期 ====================

onMounted(async () => {
  initDateRange()
  await loadData()
  renderChart()
})

onBeforeUnmount(() => {
  // 销毁图表实例
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})

// 监听图表类型变化
watch(chartType, () => {
  renderChart()
})

// 监听过滤数据变化，更新图表
watch(filteredData, () => {
  renderChart()
})
</script>
