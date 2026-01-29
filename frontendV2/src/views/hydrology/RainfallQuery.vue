<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">降雨查询</h1>
      <p class="mt-1 text-sm text-gray-500">查询和分析各测站的历史降雨数据</p>
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
                <Input
                  v-model="startDate"
                  type="datetime-local"
                  inputClass="pl-10"
                />
              </div>
              <span class="text-gray-400 font-medium">-</span>
              <div class="relative group">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                </div>
                <Input
                  v-model="endDate"
                  type="datetime-local"
                  inputClass="pl-10"
                />
              </div>
            </div>
          </div>

          <!-- 快捷选项 Tags -->
          <div class="flex flex-col gap-2">
             <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide">快速选择</label>
             <div class="flex flex-wrap items-center gap-2">
              <button
                v-for="shortcut in dateShortcuts"
                :key="shortcut.label"
                class="px-3 py-1.5 text-xs font-medium rounded-md border transition-all duration-200 active:scale-95 bg-gray-50 border-gray-200 text-gray-600 hover:bg-white hover:text-primary-600 hover:border-primary-200 hover:shadow-sm"
                @click="applyShortcut(shortcut)"
              >
                {{ shortcut.label }}
              </button>
            </div>
          </div>
        </div>

        <!-- 右侧：操作按钮 -->
        <div class="flex items-end gap-3 pt-4 lg:pt-0 border-t lg:border-t-0 border-gray-100">
           <!-- 使用 w-full 在移动端撑满 -->
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
          <!-- 视觉锚点装饰 -->
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <h2 class="text-lg font-bold text-gray-900">
            {{ chartType === 'hourly' ? '小时降雨量分布' : '每日累计降雨趋势' }}
          </h2>
        </div>
        
        <!-- 分段控制器 Segmented Control -->
        <div class="bg-gray-100 p-1 rounded-lg flex items-center">
          <button
            v-for="type in [{value: 'hourly', label: '小时降雨'}, {value: 'daily', label: '每日累计'}]"
            :key="type.value"
            @click="chartType = type.value"
            :class="[
              'px-4 py-1.5 text-sm font-medium rounded-md transition-all duration-200 ease-out',
              chartType === type.value
                ? 'bg-white text-primary-600 shadow-sm ring-1 ring-black/5'
                : 'text-gray-500 hover:text-gray-700'
            ]"
          >
            {{ type.label }}
          </button>
        </div>
      </div>
      
      <!-- Chart Container -->
      <div class="relative w-full bg-gray-50/50 rounded-lg border border-gray-100 p-4">
        <div ref="chartRef" class="w-full h-96"></div>
      </div>
    </Card>

    <!-- 数据表格区 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-2">
           <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
            </svg>
           <h2 class="text-base font-bold text-gray-800">详细数据列表</h2>
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
 * 降雨查询页面
 * 功能：查询和展示降雨数据，支持图表和表格两种展示方式
 * 依赖组件：Button、Table
 */
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// 组件导入
import Button from '@/components/basic/Button.vue'
import Table from '@/components/basic/Table.vue'
import Card from '@/components/basic/Card.vue'
import Input from '@/components/basic/Input.vue'

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
const pageSize = ref(20)

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
    // 传递日期范围参数，后端按范围查询
    const params = {}
    if (startDate.value) {
      params.startDate = startDate.value.replace('T', ' ') + ':00'
    }
    if (endDate.value) {
      params.endDate = endDate.value.replace('T', ' ') + ':00'
    }

    const res = await getHourlyRainfall(params)
    const data = res.data

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

  // 显示加载动画
  if (loading.value) {
    chartInstance.showLoading({
      text: '加载中...',
      color: '#3b82f6',
      textColor: '#3b82f6',
      maskColor: 'rgba(255, 255, 255, 0.9)',
      zlevel: 0
    })
    return
  }

  // 隐藏加载动画
  chartInstance.hideLoading()

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

// 监听 loading 状态变化
watch(loading, () => {
  renderChart()
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
