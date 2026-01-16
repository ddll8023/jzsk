<template>
  <!-- 地表位移统计卡片 -->
  <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-4 mb-4">
    <div
      v-for="(card, index) in cards"
      :key="index"
      class="bg-white rounded-lg shadow p-4 text-center hover:shadow-md transition-shadow duration-200"
    >
      <div class="text-sm text-gray-500 mb-2">{{ card.label }}</div>
      <!-- 加载状态 -->
      <div v-if="loading" class="flex items-center justify-center py-2">
        <i class="fa fa-spinner fa-spin text-primary-500" aria-hidden="true"></i>
      </div>
      <!-- 数值显示 -->
      <template v-else>
        <div class="text-2xl font-bold text-primary-600">
          {{ formatValue(card.value) }}
        </div>
        <div class="text-xs text-gray-400 mt-1">{{ card.unit }}</div>
      </template>
    </div>
  </div>
</template>

<script setup>
/**
 * 地表位移统计卡片组件
 * 功能：展示5个位移指标的最新值或平均值
 */

defineProps({
  cards: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
})

// 格式化数值显示
function formatValue(val) {
  if (val === null || val === undefined || val === '-') return '-'
  const num = Number(val)
  if (isNaN(num)) return val
  return num.toFixed(2)
}
</script>
