<script setup>
/**
 * 闸门查询面板组件
 * 功能：闸门选择、时间范围、快捷按钮、导出、刷新
 * 依赖组件：Button、Select
 */
import { computed } from 'vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'

const props = defineProps({
  gateOptions: { type: Array, required: true },
  selectedGate: { type: String, required: true },
  dateRange: { type: Array, default: () => [] },
  quickType: { type: String, default: 'day' },
  currentGateName: { type: String, default: '' },
  latestCollectTime: { type: String, default: '' },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['update:selectedGate', 'update:dateRange', 'quick-select', 'export', 'refresh'])

// 双向绑定闸门选择
const modelGate = computed({
  get: () => props.selectedGate,
  set: (val) => emit('update:selectedGate', val)
})

// 闸门选项映射为 Select 组件格式
const selectOptions = computed(() => {
  return props.gateOptions.map(g => ({
    label: g.name,
    value: g.code
  }))
})

// 快捷按钮配置
const quickButtons = [
  { label: '全部', value: 'all' },
  { label: '最近一天', value: 'day' },
  { label: '最近一周', value: 'week' },
  { label: '最近一月', value: 'month' }
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
  <!-- 闸门查询面板 -->
  <div class="bg-white rounded-lg shadow p-4 mb-4">
    <div class="flex flex-wrap items-center gap-4">
      <!-- 闸门选择 -->
      <div class="flex items-center gap-2 w-48">
        <Select
          label="选择闸门"
          v-model="modelGate"
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

      <!-- 操作按钮 -->
      <div class="flex items-center gap-2 ml-auto">
        <Button 
          type="default" 
          :icon="loading ? 'spinner fa-spin' : 'refresh'" 
          :disabled="loading"
          @click="$emit('refresh')"
        >
          {{ loading ? '加载中' : '刷新' }}
        </Button>
        <Button type="primary" icon="download" :disabled="loading" @click="$emit('export')">
          导出
        </Button>
      </div>
    </div>

    <!-- 当前信息 -->
    <div class="flex items-center gap-4 mt-3 pt-3 border-t border-gray-100">
      <span class="text-lg font-semibold text-primary-700">{{ currentGateName }}</span>
      <span class="text-sm text-gray-500">采集时间：{{ latestCollectTime || '-' }}</span>
    </div>
  </div>
</template>
