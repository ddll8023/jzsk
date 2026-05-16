<template>
  <div
    class="relative overflow-hidden rounded-xl border transition-all duration-200 cursor-pointer select-none bg-white"
    :class="[
      active ? 'ring-2 shadow-lg -translate-y-0.5' : 'shadow-md hover:shadow-lg hover:-translate-y-0.5',
      active ? activeRingClass : 'ring-transparent'
    ]"
    @click="$emit('click')"
  >
    <!-- 顶部色带 -->
    <div class="h-1 w-full" :class="colorBarClass" />

    <div class="p-5">
      <!-- 标题行 -->
      <div class="flex items-center justify-between mb-3">
        <div class="flex items-center gap-2">
          <div
            class="w-8 h-8 rounded-lg flex items-center justify-center"
            :class="iconBgClass"
          >
            <i :class="`fa ${config.icon}`" class="text-sm" />
          </div>
          <span class="text-sm font-medium text-gray-700">{{ config.label }}</span>
        </div>
        <span class="text-2xl font-bold tracking-tight text-gray-900">{{ stats.total }}</span>
      </div>

      <!-- 到报率进度条 -->
      <div class="mb-3">
        <div class="flex items-center justify-between mb-1">
          <span class="text-xs text-gray-400">到报率</span>
          <span class="text-xs font-semibold" :class="rateColorClass">{{ onlineRate }}%</span>
        </div>
        <div class="w-full h-1.5 bg-gray-100 rounded-full overflow-hidden">
          <div
            class="h-full rounded-full transition-all duration-500"
            :class="rateBarClass"
            :style="{ width: `${onlineRate}%` }"
          />
        </div>
      </div>

      <!-- 到报状态行 -->
      <div class="flex items-center gap-4 text-xs">
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
          <span class="text-gray-500">已到报</span>
          <span class="font-semibold text-emerald-600">{{ stats.online }}</span>
        </div>
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-amber-500" />
          <span class="text-gray-500">未到报</span>
          <span class="font-semibold text-amber-600">{{ stats.offline }}</span>
        </div>
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-red-500 animate-pulse" style="animation-duration: 2s" />
          <span class="text-gray-500">异常</span>
          <span class="font-semibold text-red-600">{{ stats.abnormal }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 设备类型统计卡片
 * 功能: 展示单个设备类型（GNSS/雨水情/渗流渗压）的到报统计信息
 * 交互: 点击可筛选该类型的设备列表
 */
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    required: true
  },
  stats: {
    type: Object,
    required: true
  },
  active: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])

const TYPE_CONFIG = {
  gnss: { label: 'GNSS 地表位移', icon: 'fa-satellite', color: 'blue' },
  rain: { label: '水雨情设备', icon: 'fa-cloud-rain', color: 'cyan' },
  seepage: { label: '渗流渗压设备', icon: 'fa-water', color: 'teal' }
}

const COLOR_CLASS_MAP = {
  blue: {
    bar: 'bg-blue-500',
    iconBg: 'bg-blue-100 text-blue-600',
    ring: 'ring-blue-400'
  },
  cyan: {
    bar: 'bg-cyan-500',
    iconBg: 'bg-cyan-100 text-cyan-600',
    ring: 'ring-cyan-400'
  },
  teal: {
    bar: 'bg-teal-500',
    iconBg: 'bg-teal-100 text-teal-600',
    ring: 'ring-teal-400'
  }
}

const config = computed(() => TYPE_CONFIG[props.type] || TYPE_CONFIG.gnss)
const colorClasses = computed(() => COLOR_CLASS_MAP[config.value.color] || COLOR_CLASS_MAP.blue)

const colorBarClass = computed(() => colorClasses.value.bar)
const iconBgClass = computed(() => colorClasses.value.iconBg)
const activeRingClass = computed(() => colorClasses.value.ring)

const onlineRate = computed(() => {
  if (!props.stats.total) return 0
  return Math.round((props.stats.online / props.stats.total) * 100)
})

const rateColorClass = computed(() => {
  if (onlineRate.value >= 80) return 'text-emerald-600'
  if (onlineRate.value >= 50) return 'text-amber-600'
  return 'text-red-600'
})

const rateBarClass = computed(() => {
  if (onlineRate.value >= 80) return 'bg-emerald-500'
  if (onlineRate.value >= 50) return 'bg-amber-500'
  return 'bg-red-500'
})
</script>
