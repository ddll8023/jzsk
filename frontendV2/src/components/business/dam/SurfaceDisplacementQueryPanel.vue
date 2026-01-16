<script setup>
/**
 * 地表位移查询面板组件
 * 功能：站点选择、时间范围、快捷按钮、导出
 * 重构：使用基础组件 Button/Select 替代原生元素
 */
import { computed } from 'vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'

const props = defineProps({
  stationOptions: { type: Array, required: true },
  stationId: { type: Number, required: true },
  dateRange: { type: Array, default: () => [] },
  quickType: { type: String, default: 'today' },
  currentStationName: { type: String, default: '' },
  latestCollectTime: { type: String, default: '' }
})

const emit = defineEmits(['update:stationId', 'update:dateRange', 'quick-select', 'export'])

// 双向绑定站点ID
const modelStationId = computed({
  get: () => props.stationId,
  set: (val) => emit('update:stationId', val)
})

// 站点选项映射为 Select 组件格式
const selectOptions = computed(() => {
  return props.stationOptions.map(s => ({
    label: s.name,
    value: s.stationId
  }))
})

// 快捷按钮配置
const quickButtons = [
  { label: '今天', value: 'today' },
  { label: '昨天', value: 'yesterday' },
  { label: '近15天', value: 'last15' },
  { label: '本月', value: 'month' }
]

// 格式化为 input datetime-local 格式
function formatForInput(dateStr) {
  if (!dateStr) return ''
  return dateStr.replace(' ', 'T').slice(0, 16)
}

// 更新开始时间
function updateStartTime(val) {
  if (!val) return
  const formatted = val.replace('T', ' ') + ':00'
  emit('update:dateRange', [formatted, props.dateRange[1] || ''])
}

// 更新结束时间
function updateEndTime(val) {
  if (!val) return
  const formatted = val.replace('T', ' ') + ':00'
  emit('update:dateRange', [props.dateRange[0] || '', formatted])
}
</script>

<template>
  <!-- 地表位移查询面板 -->
  <div class="bg-white rounded-lg shadow p-4 mb-4">
    <div class="flex flex-wrap items-center gap-4">
      <!-- 站点选择 -->
      <div class="flex items-center gap-2 w-64">
        <Select
          label="站点选择"
          v-model="modelStationId"
          :options="selectOptions"
        />
      </div>

      <!-- 时间选择 -->
      <div class="flex items-center gap-2">
        <span class="text-gray-600 text-sm whitespace-nowrap">时间范围</span>
        <input
          type="datetime-local"
          :value="formatForInput(dateRange[0])"
          @change="updateStartTime($event.target.value)"
          class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
        />
        <span class="text-gray-400">至</span>
        <input
          type="datetime-local"
          :value="formatForInput(dateRange[1])"
          @change="updateEndTime($event.target.value)"
          class="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
        />
      </div>

      <!-- 快捷按钮 -->
      <div class="flex items-center gap-1">
        <Button
          v-for="btn in quickButtons"
          :key="btn.value"
          size="sm"
          :type="quickType === btn.value ? 'primary' : 'default'"
          @click="$emit('quick-select', btn.value)"
        >
          {{ btn.label }}
        </Button>
      </div>

      <!-- 导出按钮 -->
      <Button
        type="primary"
        icon="download"
        class="ml-auto"
        @click="$emit('export')"
      >
        导出
      </Button>
    </div>

    <!-- 当前信息 -->
    <div class="flex items-center gap-4 mt-3 pt-3 border-t border-gray-100">
      <span class="text-lg font-semibold text-primary-700">{{ currentStationName }}</span>
      <span class="text-sm text-gray-500">采集时间：{{ latestCollectTime || '-' }}</span>
    </div>
  </div>
</template>
