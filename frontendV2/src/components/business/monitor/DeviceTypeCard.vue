<template>
  <div
    class="relative overflow-hidden rounded-xl border transition-all duration-200 cursor-pointer select-none"
    :class="[
      active ? 'ring-2 shadow-lg -translate-y-0.5' : 'shadow-md hover:shadow-lg hover:-translate-y-0.5',
      active ? `ring-${config.color}-400` : 'ring-transparent'
    ]"
    @click="$emit('click')"
  >
    <!-- 顶部色带 -->
    <div
      class="h-1 w-full"
      :class="`bg-${config.color}-500`"
    />

    <div class="p-5">
      <!-- 标题行 -->
      <div class="flex items-center justify-between mb-4">
        <div class="flex items-center gap-2">
          <div
            class="w-8 h-8 rounded-lg flex items-center justify-center"
            :class="`bg-${config.color}-100 text-${config.color}-600`"
          >
            <i :class="`fa ${config.icon}`" class="text-sm" />
          </div>
          <span class="text-sm font-medium text-gray-700">{{ config.label }}</span>
        </div>
        <span class="text-2xl font-bold tracking-tight text-gray-900">{{ stats.total }}</span>
      </div>

      <!-- 状态行 -->
      <div class="flex items-center gap-4 text-xs">
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
          <span class="text-gray-500">在线</span>
          <span class="font-semibold text-emerald-600">{{ stats.online }}</span>
        </div>
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-red-500" />
          <span class="text-gray-500">离线</span>
          <span class="font-semibold text-red-600">{{ stats.offline }}</span>
        </div>
        <div class="flex items-center gap-1.5">
          <span class="w-2 h-2 rounded-full bg-amber-500 animate-pulse" style="animation-duration: 2s" />
          <span class="text-gray-500">异常</span>
          <span class="font-semibold text-amber-600">{{ stats.abnormal }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 设备类型统计卡片
 * 功能: 展示单个设备类型（GNSS/雨水情/渗流渗压）的统计信息
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
  rain: { label: '雨水情设备', icon: 'fa-cloud-rain', color: 'cyan' },
  seepage: { label: '渗流渗压设备', icon: 'fa-water', color: 'teal' }
}

const config = computed(() => TYPE_CONFIG[props.type] || TYPE_CONFIG.gnss)
</script>
