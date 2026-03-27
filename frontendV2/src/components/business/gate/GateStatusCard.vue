<script setup>
/**
 * 闸门状态卡片组件
 * 功能：SVG可视化展示闸门开度、水位、状态
 * 依赖组件：Card
 */
import { computed } from 'vue'
import Card from '@/components/basic/Card.vue'

const props = defineProps({
  latestData: { type: Object, default: null },
  gateName: { type: String, default: '' },
  latestKD: { type: Number, default: 0 },
  waterPercent: { type: Number, default: 100 },
  loading: { type: Boolean, default: false },
  hasError: { type: Boolean, default: false }  // 区分"无数据"和"请求失败"
})

const emit = defineEmits(['refresh'])

// 显示用开度（两位小数）
const displayKD = computed(() => Number(props.latestKD).toFixed(2))

// 显示用水位（两位小数）
const displayWater = computed(() => Number(props.waterPercent).toFixed(2))

// 状态颜色映射（Tailwind class）
const statusColorClass = computed(() => {
  const kd = props.latestKD
  if (kd === 0) return 'bg-red-500'
  if (kd < 30) return 'bg-orange-500'
  if (kd < 70) return 'bg-yellow-500'
  return 'bg-green-500'
})

// SVG 填充颜色（十六进制）
const statusFillColor = computed(() => {
  const kd = props.latestKD
  if (kd === 0) return '#EF4444'
  if (kd < 30) return '#F97316'
  if (kd < 70) return '#EAB308'
  return '#22C55E'
})

// 状态文本
const statusText = computed(() => {
  const kd = props.latestKD
  if (kd === 0) return '关闭'
  if (kd < 30) return '微开'
  if (kd < 70) return '半开'
  return '全开'
})

// SVG 闸门高度计算（最大160，最小20）
const gateHeight = computed(() => 160 - (props.latestKD / 100) * 140)

// SVG 闸门Y坐标
const gateY = computed(() => 80 + (160 - gateHeight.value))

// 水位Y坐标
const waterY = computed(() => 80 + 160 - (props.waterPercent / 100) * 160)

// 水位填充路径
const waterFillPath = computed(() => {
  const y = waterY.value
  return `M40 ${y} L360 ${y} L360 240 L40 240 Z`
})

// 波浪路径
const wavePath = computed(() => {
  const y = waterY.value
  return `M40 ${y} Q80 ${y-8} 120 ${y} T200 ${y} T280 ${y} T360 ${y}`
})

// 刻度线配置
const scaleTicks = computed(() => {
  const ticks = []
  for (let p = 0; p <= 100; p += 10) {
    const y = 80 + 160 - (p / 100) * 160
    ticks.push({ y, label: `${p}%`, major: true })
  }
  return ticks
})
</script>

<template>
  <Card class="mb-4">
    <template #header>
      <div class="flex items-center justify-between">
        <span class="text-lg font-semibold">闸门实时状态</span>
        <button 
          @click="$emit('refresh')"
          class="text-gray-400 hover:text-primary-600 transition-colors cursor-pointer"
        >
          <i class="fa fa-refresh" :class="{ 'fa-spin': loading }"></i>
        </button>
      </div>
    </template>

    <!-- Loading 状态（无数据时的全屏 loading） -->
    <div v-if="loading && !latestData" class="text-center py-16">
      <i class="fa fa-spinner fa-spin text-primary-600 text-4xl"></i>
      <p class="mt-3 text-gray-500">数据加载中...</p>
    </div>

    <!-- 有数据时显示 -->
    <div v-else-if="latestData" class="text-center relative">
      <!-- Loading 遮罩层（刷新时） -->
      <div 
        v-if="loading" 
        class="absolute inset-0 bg-white/80 flex items-center justify-center z-10 rounded-lg"
      >
        <div class="text-center">
          <i class="fa fa-spinner fa-spin text-primary-600 text-3xl"></i>
          <p class="mt-2 text-gray-500 text-sm">数据加载中...</p>
        </div>
      </div>

      <!-- 闸门名称和状态 -->
      <div class="mb-4">
        <h3 class="text-xl font-bold text-gray-800">{{ gateName }}</h3>
        <div class="flex items-center justify-center gap-4 mt-2 text-sm">
          <span class="text-gray-600">
            状态：
            <span :class="[statusColorClass.replace('bg-', 'text-'), 'font-semibold']">
              {{ statusText }}
            </span>
          </span>
          <span class="text-gray-600">
            开度：<span class="text-primary-600 font-semibold">{{ displayKD }}%</span>
          </span>
          <span class="text-gray-600">
            水位：<span class="text-blue-600 font-semibold">{{ displayWater }}%</span>
          </span>
        </div>
      </div>

      <!-- SVG 可视化 -->
      <svg width="400" height="280" class="mx-auto">
        <defs>
          <!-- 阴影滤镜 -->
          <filter id="gateShadow" x="-20%" y="-20%" width="140%" height="140%">
            <feDropShadow dx="0" dy="3" stdDeviation="3" flood-color="#000" flood-opacity="0.15"/>
          </filter>
          <!-- 水面渐变 -->
          <linearGradient id="waterGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#4FC3F7;stop-opacity:0.6" />
            <stop offset="100%" style="stop-color:#0288D1;stop-opacity:0.4" />
          </linearGradient>
        </defs>

        <!-- 水池背景 -->
        <rect x="40" y="80" width="320" height="160" fill="#E3F2FD" rx="12" stroke="#90CAF9" stroke-width="2" />

        <!-- 水位填充 -->
        <path :d="waterFillPath" fill="url(#waterGradient)" />

        <!-- 水面波浪 -->
        <path :d="wavePath" stroke="#4FC3F7" stroke-width="2" fill="none" />

        <!-- 闸门轨道 -->
        <rect x="160" y="50" width="12" height="200" fill="#BDBDBD" rx="4" />
        <rect x="228" y="50" width="12" height="200" fill="#BDBDBD" rx="4" />

        <!-- 闸门本体 -->
        <rect 
          x="172" 
          :y="gateY" 
          width="56" 
          :height="gateHeight" 
          fill="#546E7A" 
          rx="4"
          filter="url(#gateShadow)"
        />

        <!-- 闸门开度指示条 -->
        <rect x="176" :y="gateY - 4" width="48" height="6" fill="#FF9800" rx="3" />

        <!-- 刻度线 -->
        <g v-for="tick in scaleTicks" :key="tick.y">
          <line x1="365" :y1="tick.y" x2="375" :y2="tick.y" stroke="#9E9E9E" stroke-width="1.5" />
          <text x="380" :y="tick.y + 4" font-size="10" fill="#757575">{{ tick.label }}</text>
        </g>

        <!-- 开度数值显示 -->
        <text x="200" y="265" font-size="16" fill="#333" text-anchor="middle" font-weight="bold">
          开度: {{ displayKD }}%
        </text>

        <!-- 状态指示圆点 -->
        <circle cx="200" cy="40" r="12" :fill="statusFillColor" />
        <text x="200" y="44" font-size="10" fill="#fff" text-anchor="middle" font-weight="bold">
          {{ statusText.charAt(0) }}
        </text>
      </svg>
    </div>

    <!-- 无数据且非 loading 时显示 -->
    <div v-else class="text-center py-12 text-gray-500">
      <i class="fa fa-inbox text-4xl text-gray-300 mb-3"></i>
      <p>暂无数据</p>
      <p v-if="hasError" class="text-sm text-gray-400 mt-1">请检查网络连接或稍后重试</p>
      <button
        @click="$emit('refresh')"
        class="mt-4 px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors cursor-pointer"
      >
        重新加载
      </button>
    </div>
  </Card>
</template>

<style scoped>
/* 无需额外样式，使用 Tailwind 工具类 */
</style>
