<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">水库水位查询</h1>
      <p class="mt-1 text-sm text-gray-500">查询和分析水库水位历史数据与变化趋势</p>
    </header>

    <!-- 搜索控制面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <div class="flex flex-col lg:flex-row lg:items-center gap-6 justify-between">
        <!-- 左侧：筛选条件 -->
        <div class="flex flex-col md:flex-row gap-6 md:items-center flex-1">
          <!-- 日期范围选择 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide">时间范围</label>
            <div class="flex items-center gap-3">
              <div class="relative group">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                </div>
                <Input v-model="startDate" type="datetime-local" inputClass="pl-10" />
              </div>
              <span class="text-gray-400 font-medium">-</span>
              <div class="relative group">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                </div>
                <Input v-model="endDate" type="datetime-local" inputClass="pl-10" />
              </div>
            </div>
          </div>
          <!-- 快捷选项 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide">快速选择</label>
            <div class="flex flex-wrap items-center gap-2">
              <button
                v-for="shortcut in dateShortcuts"
                :key="shortcut.label"
                :class="[
                  'px-3 py-1.5 text-xs font-medium rounded-md border transition-all duration-200 active:scale-95',
                  activeShortcut === shortcut.label
                    ? 'bg-primary-50 text-primary-700 shadow-md border-primary-500 ring-2 ring-primary-500/20'
                    : 'bg-gray-50 border-gray-200 text-gray-600 hover:bg-white hover:text-primary-600 hover:border-primary-300 hover:shadow-sm'
                ]"
                @click="applyShortcut(shortcut)"
              >
                {{ shortcut.label }}
              </button>
            </div>
          </div>
        </div>
        <!-- 右侧：操作按钮 -->
        <div class="flex items-end gap-3 pt-4 lg:pt-0 border-t lg:border-t-0 border-gray-100">
          <Button type="primary" icon="search" class="w-full lg:w-auto shadow-md shadow-primary-500/20" @click="handleSearch">
            查询数据
          </Button>
          <Button type="success" icon="download" class="w-full lg:w-auto shadow-md shadow-green-500/20" @click="handleExport">
            导出报表
          </Button>
        </div>
      </div>
    </Card>

    <!-- 图表展示区 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="md" class="mb-6">
      <div class="flex flex-wrap items-center justify-between gap-4 mb-6">
        <div class="flex items-center gap-3">
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <h2 class="text-lg font-bold text-gray-900">水库水位过程线</h2>
        </div>
      </div>
      <div class="relative w-full bg-gray-50/50 rounded-lg border border-gray-100 p-4">
        <div ref="chartRef" class="w-full h-96"></div>
        <div v-if="loading" class="absolute inset-0 flex items-center justify-center bg-white/80 rounded-lg">
          <div class="flex items-center gap-2 text-gray-500">
            <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span>加载中...</span>
          </div>
        </div>
      </div>
    </Card>

    <!-- 数据表格区 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
          </svg>
          <h2 class="text-base font-bold text-gray-800">水位数据列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ filteredData.length }} 条记录</span>
      </div>
      <div class="p-0">
        <Table
          :columns="tableColumns"
          :data="pagedData"
          :loading="loading"
          :total="filteredData.length"
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          row-key="id"
        />
      </div>
    </Card>
  </div>
</template>

<script setup>
/**
 * 水库水位查询页面
 * 功能：查询和展示水库水位数据，支持折线图和表格两种展示方式
 * 依赖组件：Button、Table、Card、Input
 */
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// 组件导入
import Button from '@/components/basic/Button.vue'
import Table from '@/components/basic/Table.vue'
import Card from '@/components/basic/Card.vue'
import Input from '@/components/basic/Input.vue'

// API 导入
import { getWaterLevelPage } from '@/api/water'

// Composable 导入
import {
  parseTimeArray,
  formatTimeArray,
  formatDate,
  getDateShortcuts,
  getDefaultDateRange,
  exportToCSV
} from '@/composables/useWater'

// ==================== 常量定义 ====================
// 水位有效值阈值（过滤无效数据）
const WATER_LEVEL_THRESHOLD = 50

// ==================== 状态定义 ====================
const allData = ref([])
const loading = ref(false)
const error = ref(null)
const startDate = ref('')
const endDate = ref('')
const dateShortcuts = getDateShortcuts()
const activeShortcut = ref('') // 当前选中的快速选择按钮
const chartRef = ref(null)
let chartInstance = null
const currentPage = ref(1)
const pageSize = ref(20)

// ==================== 表格配置 ====================
const tableColumns = [
  { key: 'stationName', title: '测站名称', width: '200px' },
  { key: 'formattedTime', title: '时间', width: '180px' },
  { key: 'z1', title: '水位(m)', width: '120px' }
]

// ==================== 计算属性 ====================

/**
 * 过滤后的数据
 * 1. 根据日期范围筛选
 * 2. 过滤 z1 > WATER_LEVEL_THRESHOLD 的有效数据
 * 3. 每个时间点只保留一条记录
 */
const filteredData = computed(() => {
  if (!startDate.value || !endDate.value) {
    return allData.value
  }
  const start = new Date(startDate.value)
  const end = new Date(endDate.value)
  const mapByTime = new Map()
  
  allData.value.forEach((item) => {
    const itemTime = parseTimeArray(item.tm)
    if (itemTime < start || itemTime > end) return
    const z1Val = Number(item.z1)
    if (z1Val <= WATER_LEVEL_THRESHOLD) return
    const timeKey = item.formattedTime
    if (!mapByTime.has(timeKey)) {
      mapByTime.set(timeKey, item)
    }
  })
  return Array.from(mapByTime.values())
})

/** 排序后的数据（按时间降序） */
const sortedData = computed(() => {
  return [...filteredData.value].sort(
    (a, b) => parseTimeArray(b.tm) - parseTimeArray(a.tm)
  )
})

/** 分页后的数据 */
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return sortedData.value.slice(start, start + pageSize.value)
})

// ==================== 方法定义 ====================

/** 初始化日期范围为最近一天 */
const initDateRange = () => {
  const [start, end] = getDefaultDateRange()
  startDate.value = formatDate(start, 'datetime').replace(' ', 'T').slice(0, 16)
  endDate.value = formatDate(end, 'datetime').replace(' ', 'T').slice(0, 16)
}

/** 应用快捷日期选项 */
const applyShortcut = (shortcut) => {
  activeShortcut.value = shortcut.label
  const [start, end] = shortcut.value()
  startDate.value = formatDate(start, 'datetime').replace(' ', 'T').slice(0, 16)
  endDate.value = formatDate(end, 'datetime').replace(' ', 'T').slice(0, 16)
  handleSearch()
}

/** 加载数据 - 循环分页获取数据 */
const loadData = async () => {
  loading.value = true
  error.value = null
  try {
    const fetchSize = 200
    let currentFetchPage = 1
    let total = Infinity
    const accumulated = []
    const expectedPoints = 49

    while (accumulated.length < expectedPoints && (currentFetchPage - 1) * fetchSize < total) {
      const res = await getWaterLevelPage({ page: currentFetchPage, size: fetchSize })
      const pageData = res.data.data || {}
      const records = pageData.records || []
      total = Number(pageData.total || 0)

      records.forEach((item) => {
        const z1Val = Number(item.z1)
        if (z1Val <= WATER_LEVEL_THRESHOLD) return
        accumulated.push({
          ...item,
          id: accumulated.length,
          formattedTime: formatTimeArray(item.tm),
          stationName: '坝前雨量水位站（新站）'
        })
      })
      currentFetchPage++
    }
    allData.value = accumulated
  } catch (e) {
    error.value = e.message || '数据加载失败'
    console.error('水位数据加载失败:', e)
  } finally {
    loading.value = false
  }
}

/** 查询 */
const handleSearch = () => {
  currentPage.value = 1
  renderChart()
}

/** 导出 CSV */
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
      { label: '水位(m)', key: 'z1' }
    ],
    filename: '水库水位数据'
  })
}

/** 生成等间隔时间轴（每半小时一个点） */
const generateChartTimeAxis = () => {
  let startTime, endTime
  if (startDate.value && endDate.value) {
    startTime = new Date(startDate.value)
    endTime = new Date(endDate.value)
  } else {
    endTime = new Date()
    startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000)
  }
  startTime.setSeconds(0, 0)
  startTime.setMinutes(startTime.getMinutes() < 30 ? 0 : 30)
  endTime.setSeconds(0, 0)
  if (endTime.getMinutes() < 30) {
    endTime.setMinutes(30)
  } else {
    endTime.setMinutes(0)
    endTime.setHours(endTime.getHours() + 1)
  }
  const timeAxis = []
  const current = new Date(startTime)
  // 使用与 formattedTime 一致的格式（不带秒）
  const pad = (n) => String(n).padStart(2, '0')
  while (current <= endTime) {
    const y = current.getFullYear()
    const m = pad(current.getMonth() + 1)
    const d = pad(current.getDate())
    const h = pad(current.getHours())
    const min = pad(current.getMinutes())
    timeAxis.push(`${y}-${m}-${d} ${h}:${min}`)
    current.setMinutes(current.getMinutes() + 30)
  }
  return timeAxis
}

/** 生成水位数据映射到时间轴 */
const generateWaterLevelData = (data, timeAxis) => {
  if (!data.length || !timeAxis.length) return []
  const dataMap = new Map()
  data.forEach((item) => {
    dataMap.set(item.formattedTime, Number(item.z1))
  })
  const result = []
  let lastValue = null
  timeAxis.forEach((time) => {
    if (dataMap.has(time)) {
      lastValue = dataMap.get(time)
    }
    result.push(lastValue)
  })
  if (result[0] === null) {
    const firstNonNull = result.find((v) => v !== null)
    if (firstNonNull !== undefined) {
      for (let i = 0; i < result.length && result[i] === null; i++) {
        result[i] = firstNonNull
      }
    }
  }
  return result
}

/** 渲染图表 */
const renderChart = () => {
  if (!chartRef.value) return
  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
  }
  const data = filteredData.value
  if (!data.length) {
    chartInstance.clear()
    return
  }
  const chartData = [...data].sort(
    (a, b) => parseTimeArray(a.tm) - parseTimeArray(b.tm)
  )
  const timeAxis = generateChartTimeAxis()
  const waterLevelData = generateWaterLevelData(chartData, timeAxis)
  const validValues = waterLevelData.filter((v) => v !== null)
  const minVal = Math.min(...validValues)
  const maxVal = Math.max(...validValues)
  const yMin = Math.floor(minVal - 1)
  const yMax = Math.ceil(maxVal + 1)

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      formatter: (params) => {
        if (!params.length) return ''
        const item = params[0]
        const val = item.data !== null ? item.data + 'm' : '-'
        return `${item.axisValue}<br/>水位: ${val}`
      }
    },
    grid: { left: 20, right: 30, top: 40, bottom: 40, containLabel: true },
    xAxis: {
      type: 'category',
      data: timeAxis,
      axisLabel: {
        rotate: 0,
        fontSize: 12,
        formatter: (value) => {
          const parts = value.split(' ')
          return parts.length > 1 ? parts[1].slice(0, 5) : value
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '水位(m)',
      nameTextStyle: { fontSize: 14 },
      axisLabel: { fontSize: 12 },
      min: yMin,
      max: yMax
    },
    series: [{
      name: '水位',
      type: 'line',
      data: waterLevelData,
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: { width: 3, color: '#3b82f6' },
      itemStyle: { color: '#3b82f6' },
      areaStyle: { color: 'rgba(59, 130, 246, 0.1)' },
      connectNulls: true
    }]
  })
}

// ==================== 生命周期 ====================
onMounted(async () => {
  initDateRange()
  await loadData()
  renderChart()
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})

watch(filteredData, () => {
  renderChart()
})
</script>
