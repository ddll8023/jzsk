<template>
  <!-- 渗流量图表 -->
  <div class="bg-white rounded-lg shadow">
    <!-- 标题 -->
    <div class="border-b border-gray-200 px-4 py-3">
      <h3 class="text-base font-semibold text-gray-900">渗流量过程线</h3>
    </div>

    <!-- 图表容器 -->
    <div class="p-4">
      <div ref="chartRef" class="w-full h-[320px]"></div>
    </div>
  </div>
</template>

<script setup>
/**
 * 渗流量图表组件
 * 功能：展示渗流量时序折线图
 */
import { ref, watch, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  chartData: { type: Array, default: () => [] },
  timeAxis: { type: Array, default: () => [] },
  flowData: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

// 图表实例
const chartRef = ref(null)
const chart = shallowRef(null)

/**
 * 更新图表
 */
/**
 * 更新图表
 */
function updateChart() {
  if (!chart.value) return

  const hasData = props.timeAxis?.length && props.flowData?.length

  // 1. 构造基础 Option
  const option = {
    title: { 
      text: '', 
      left: 'center',
      textStyle: { fontSize: 16, fontWeight: 'bold', color: '#374151' } 
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#e5e7eb',
      borderWidth: 1,
      textStyle: { color: '#374151' },
      formatter: function (params) {
        if (!params.length) return ''
        const item = params[0]
        const time = item.axisValue
        const val = item.data !== null && item.data !== undefined ? item.data.toFixed(3) : '-'
        return `${time}<br/><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:#22c55e;"></span>流量: <strong>${val}</strong> L/s`
      }
    },
    grid: { 
      top: 40, 
      right: 30, 
      bottom: 20, 
      left: 50, 
      containLabel: true 
    },
    xAxis: {
      type: 'category',
      data: props.timeAxis || [],
      axisLine: { show: true, lineStyle: { color: '#e5e7eb' } },
      axisTick: { show: false },
      axisLabel: { 
        color: '#6b7280',
        rotate: 0, 
        fontSize: 12 
      },
      show: true 
    },
    yAxis: {
      type: 'value',
      name: '流量(L/s)',
      nameTextStyle: { color: '#6b7280', padding: [0, 0, 0, 20] },
      axisLabel: { color: '#6b7280', fontSize: 12 },
      splitLine: { 
        show: true, 
        lineStyle: { type: 'dashed', color: '#e5e7eb' } 
      },
      min: 0,
      scale: true,
      show: true
    },
    series: [{
      name: '流量',
      type: 'line',
      data: props.flowData || [],
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      showSymbol: false,
      lineStyle: { width: 3, color: '#22c55e' }, // green-500
      itemStyle: { color: '#22c55e', borderWidth: 2, borderColor: '#fff' },
      areaStyle: { 
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(34, 197, 94, 0.2)' },
          { offset: 1, color: 'rgba(34, 197, 94, 0.02)' }
        ])
      },
      connectNulls: true
    }]
  }

  // 2. 处理无数据展示优化
  if (!hasData) {
    option.title.text = '暂无监测数据'
    option.title.textStyle.color = '#9ca3af'
    option.title.top = 'center'
    // 保持坐标轴显示，仅清空数据
    option.series[0].data = []
    option.xAxis.data = []
  }

  // 3. 执行渲染 (关键：notMerge = true)
  chart.value.setOption(option, true)
}

/**
 * 更新Loading状态
 */
function updateLoading() {
  if (!chart.value) return
  if (props.loading) {
    chart.value.showLoading({
      text: '加载中...',
      color: '#67C23A',
      textColor: '#67C23A',
      maskColor: 'rgba(255, 255, 255, 0.9)',
      zlevel: 0
    })
  } else {
    chart.value.hideLoading()
  }
}

/**
 * 初始化图表
 */
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
watch(() => [props.timeAxis, props.flowData], updateChart, { deep: true })

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
