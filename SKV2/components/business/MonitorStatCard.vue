<template>
  <view class="flex items-center p-4 bg-white rounded-lg">
    <view
      class="w-11 h-11 rounded-xl flex items-center justify-center mr-3"
      :class="statusClass"
    >
      <text class="text-lg font-bold" :class="statusTextClass">{{ displayValue }}</text>
    </view>
    <view class="flex-1">
      <text class="text-sm text-gray-500 block">{{ label }}</text>
      <view class="flex items-baseline mt-1">
        <text class="text-2xl font-bold text-gray-900">{{ displayValue }}</text>
        <text v-if="unit" class="text-xs text-gray-400 ml-1">{{ unit }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * MonitorStatCard 监测指标卡片
 * 功能：展示单个监测指标的数值、单位、标签
 */
import { computed } from 'vue'

const props = defineProps({
  label: { type: String, required: true },
  value: { type: [String, Number], default: null },
  unit: { type: String, default: '' },
  status: { type: String, default: 'normal' },
})

const displayValue = computed(() => {
  if (props.value === null || props.value === undefined || props.value === '') return '--'
  const num = Number(props.value)
  if (isNaN(num)) return '--'
  return num.toFixed(2)
})

const statusClass = computed(() => {
  const map = {
    normal: 'bg-primary/10',
    warning: 'bg-yellow-100',
    danger: 'bg-red-100',
  }
  return map[props.status] || map.normal
})

const statusTextClass = computed(() => {
  const map = {
    normal: 'text-primary',
    warning: 'text-yellow-600',
    danger: 'text-red-600',
  }
  return map[props.status] || map.normal
})
</script>
