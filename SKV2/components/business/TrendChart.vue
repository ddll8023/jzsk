<template>
  <view class="w-full">
    <view v-if="title" class="flex items-center justify-between mb-3">
      <text class="text-sm font-medium text-gray-700">{{ title }}</text>
      <text v-if="unit" class="text-xs text-gray-400">单位: {{ unit }}</text>
    </view>
    <view v-if="data.length === 0" class="flex items-center justify-center" :style="{ height: chartHeight + 'px' }">
      <text class="text-sm text-gray-500">暂无数据</text>
    </view>
    <canvas
      v-else
      :canvas-id="canvasId"
      :id="canvasId"
      :style="{ width: chartWidth + 'px', height: chartHeight + 'px' }"
      class="w-full"
    ></canvas>
  </view>
</template>

<script setup>
/**
 * TrendChart 趋势图组件
 * 功能：基于 canvas 绘制折线/柱状趋势图，支持时间 X 轴和数值 Y 轴
 */
import { ref, watch, nextTick, getCurrentInstance, onMounted } from 'vue'
import { formatDate } from '@/utils/format.js'

const props = defineProps({
  data: { type: Array, default: () => [] },
  xField: { type: String, default: 'tm' },
  yField: { type: String, default: 'value' },
  title: { type: String, default: '' },
  unit: { type: String, default: '' },
  height: { type: Number, default: 200 },
  type: { type: String, default: 'line' },
})

const instance = getCurrentInstance()
const canvasId = `trend_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
const chartWidth = ref(375)
const chartHeight = ref(props.height)

let ctx = null

const padding = { top: 20, right: 15, bottom: 35, left: 45 }

onMounted(() => {
  nextTick(() => {
    const query = uni.createSelectorQuery().in(instance.proxy)
    query
      .select(`#${canvasId}`)
      .boundingClientRect((rect) => {
        if (rect) {
          chartWidth.value = rect.width
          chartHeight.value = rect.height || props.height
        }
        initCanvas()
      })
      .exec()
  })
})

watch(
  () => props.data,
  () => nextTick(draw),
  { deep: true },
)

function initCanvas() {
  ctx = uni.createCanvasContext(canvasId, instance.proxy)
  draw()
}

function draw() {
  if (!ctx || props.data.length === 0) return

  const width = chartWidth.value
  const height = chartHeight.value
  const chartW = width - padding.left - padding.right
  const chartH = height - padding.top - padding.bottom

  ctx.clearRect(0, 0, width, height)

  const values = props.data.map((d) => Number(d[props.yField])).filter((v) => !isNaN(v))
  if (values.length === 0) return

  const minVal = Math.min(...values)
  const maxVal = Math.max(...values)
  const range = maxVal - minVal || 1
  const yMin = minVal - range * 0.1
  const yMax = maxVal + range * 0.1

  // 绘制 Y 轴网格线和标签
  const ySteps = 4
  ctx.setStrokeStyle('#f0f0f0')
  ctx.setLineWidth(1)
  ctx.setFontSize(10)
  ctx.setFillStyle('#999999')
  for (let i = 0; i <= ySteps; i++) {
    const y = padding.top + (chartH / ySteps) * i
    const val = yMax - ((yMax - yMin) / ySteps) * i
    ctx.beginPath()
    ctx.moveTo(padding.left, y)
    ctx.lineTo(width - padding.right, y)
    ctx.stroke()
    ctx.fillText(val.toFixed(1), 2, y + 4)
  }

  const dataLen = props.data.length
  const xStep = chartW / Math.max(dataLen - 1, 1)

  // X 轴标签（最多显示 5 个）
  const labelInterval = Math.max(1, Math.ceil(dataLen / 5))

  if (props.type === 'bar') {
    // 柱状图
    const barWidth = Math.max(Math.min(xStep * 0.6, 30), 4)
    props.data.forEach((item, i) => {
      const val = Number(item[props.yField])
      if (isNaN(val)) return
      const x = padding.left + i * xStep
      const y = padding.top + ((yMax - val) / (yMax - yMin)) * chartH
      ctx.setFillStyle('#00b783')
      ctx.fillRect(x - barWidth / 2, y, barWidth, padding.top + chartH - y)

      // X 轴标签
      if (i % labelInterval === 0 || i === dataLen - 1) {
        const label = formatDate(item[props.xField], 'HH:mm')
        ctx.setFillStyle('#999999')
        ctx.setFontSize(9)
        ctx.fillText(label, x - 15, height - 5)
      }
    })
  } else {
    // 折线图
    ctx.beginPath()
    ctx.setStrokeStyle('#00b783')
    ctx.setLineWidth(2)
    props.data.forEach((item, i) => {
      const val = Number(item[props.yField])
      if (isNaN(val)) return
      const x = padding.left + i * xStep
      const y = padding.top + ((yMax - val) / (yMax - yMin)) * chartH
      if (i === 0) ctx.moveTo(x, y)
      else ctx.lineTo(x, y)
    })
    ctx.stroke()

    // 数据点
    props.data.forEach((item, i) => {
      const val = Number(item[props.yField])
      if (isNaN(val)) return
      const x = padding.left + i * xStep
      const y = padding.top + ((yMax - val) / (yMax - yMin)) * chartH
      ctx.beginPath()
      ctx.arc(x, y, 3, 0, Math.PI * 2)
      ctx.setFillStyle('#ffffff')
      ctx.fill()
      ctx.setStrokeStyle('#00b783')
      ctx.setLineWidth(1.5)
      ctx.stroke()

      // X 轴标签
      if (i % labelInterval === 0 || i === dataLen - 1) {
        const label = formatDate(item[props.xField], 'HH:mm')
        ctx.setFillStyle('#999999')
        ctx.setFontSize(9)
        ctx.fillText(label, x - 15, height - 5)
      }
    })
  }

  ctx.draw()
}
</script>
