<template>
  <!-- 渗流数据图表 -->
  <div class="bg-white rounded-lg shadow p-4">
    <!-- Tab切换 -->
    <Tabs
      :model-value="activeTab"
      :items="tabs"
      @update:model-value="onTabChange"
      class="border-b"
    />

    <!-- 图表容器 -->
    <div class="p-4">
      <div ref="chartRef" class="w-full h-[400px]"></div>
    </div>
  </div>
</template>

<script setup>
/**
 * 渗流数据图表组件
 * 功能：展示水位高程/水位/温度/水压的时序折线图
 */
import { ref, watch, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import * as echarts from 'echarts'
import Tabs from '@/components/basic/Tabs.vue'

const props = defineProps({
  activeTab: { type: String, default: 'waterElevation' },
  chartData: { type: Array, default: () => [] },
  chartConfig: { type: Object, default: () => ({}) },
  pointId: { type: String, default: '' },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['update:activeTab'])

// Tab配置
const tabs = [
  { label: '水位高程', value: 'waterElevation' },
  { label: '水位', value: 'waterLevel' },
  { label: '温度', value: 'temperature' },
  { label: '水压', value: 'waterPressure' }
]

// 图表实例
const chartRef = ref(null)
const chart = shallowRef(null)

// Tab切换
function onTabChange(tab) {
  emit('update:activeTab', tab)
}

// 解析后端时间
function parseBackendTime(t) {
  if (t == null) return NaN
  if (typeof t === 'number') return t < 1e12 ? t * 1000 : t
  return new Date(String(t).replace(/-/g, '/')).getTime()
}

// 更新图表
function updateChart() {
  if (!chart.value) return

  const config = props.chartConfig[props.activeTab] || {
    title: '数据趋势',
    yAxisName: '数值'
  }

  // 1. 处理数据 (KISS: 仅做转换，不进行复杂的空判断分支)
  const data = (props.chartData || [])
    .map(item => [parseBackendTime(item.time), Number(item.value)])
    .filter(p => !isNaN(p[0]) && !isNaN(p[1]))
    .sort((a, b) => a[0] - b[0]) // 确保时间顺序

  // 2. 构造基础 Option (SOLID: 配置与逻辑分离)
  const option = {
    title: { 
      text: config.title, 
      left: 'center',
      top: 10,
      textStyle: { fontSize: 16, fontWeight: 'bold', color: '#374151' } // text-gray-700
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#e5e7eb',
      borderWidth: 1,
      textStyle: { color: '#374151' },
      formatter: (params) => {
        if (!params[0]) return ''
        const date = new Date(params[0].value[0])
        const timeStr = date.toLocaleString('zh-CN', { 
          month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' 
        })
        const value = Number(params[0].value[1]).toFixed(2)
        return `${timeStr}<br/><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:#3b82f6;"></span>${config.yAxisName}: <strong>${value}</strong>`
      }
    },
    // 参考 RainFallQuery.vue 的 Grid 配置
    grid: { 
      top: 60, 
      right: 30, 
      bottom: 20, 
      left: 50, 
      containLabel: true 
    },
    xAxis: {
      type: 'time',
      show: true, 
      axisLine: { show: true, lineStyle: { color: '#e5e7eb' } },
      axisTick: { show: false },
      axisLabel: { 
        color: '#6b7280',
        formatter: (val) => {
          const date = new Date(val)
          const dateStr = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
          const timeStr = `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
          return `${timeStr}\n${dateStr}`
        }
      },
      splitLine: { show: false }
    },
    yAxis: {
      type: 'value',
      name: config.yAxisName,
      nameTextStyle: { color: '#6b7280', padding: [0, 0, 0, 20] },
      show: true,
      scale: true, // 核心修改：自动缩放，无需手动计算 min/max
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#6b7280' },
      splitLine: { 
        show: true, 
        lineStyle: { type: 'dashed', color: '#e5e7eb' } 
      }
    },
    series: [{
      type: 'line',
      name: config.yAxisName,
      data: data,
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      showSymbol: false, // 鼠标悬浮时才显示点
      itemStyle: { color: '#3b82f6', borderWidth: 2, borderColor: '#fff' }, // primary-500
      lineStyle: { width: 3, color: '#3b82f6' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(59, 130, 246, 0.2)' },
          { offset: 1, color: 'rgba(59, 130, 246, 0.02)' }
        ])
      }
    }]
  }

  // 3. 处理无数据情况 (UX优化)
  if (data.length === 0) {
    option.title.text = props.pointId ? '暂无监测数据' : '请选择测点'
    option.title.textStyle.color = '#9ca3af'
    option.title.top = 'center'
    // 即使无数据，也保留坐标轴显示，避免突兀的空白
  }

  // 4. 执行渲染 (关键：notMerge = true)
  chart.value.setOption(option, true)
}

// 更新Loading状态
function updateLoading() {
  if (!chart.value) return
  if (props.loading) {
    chart.value.showLoading({
      text: '加载中...',
      color: '#2563EB',
      textColor: '#2563EB',
      maskColor: 'rgba(255, 255, 255, 0.9)',
      zlevel: 0
    })
  } else {
    chart.value.hideLoading()
  }
}

// 初始化图表
function initChart() {
  if (chartRef.value && !chart.value) {
    chart.value = echarts.init(chartRef.value)
    updateLoading()
    updateChart()
  }
}

// 监听Loading变化
watch(() => props.loading, updateLoading)

// 监听数据变化
watch(() => [props.chartData, props.activeTab, props.pointId], updateChart, { deep: true })

// 窗口resize处理
function handleResize() {
  chart.value?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart.value?.dispose()
})
</script>
