<script setup>
/**
 * 渗流量查询面板
 * 功能：测站选择、时间范围筛选、查询/导出操作
 */
import { computed } from 'vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'

const props = defineProps({
  stationList: { type: Array, default: () => [] },
  stationId: { type: String, default: '' },
  dateRangeType: { type: String, default: '24h' },
  dateRange: { type: Array, default: () => [] },
  displayDateRange: { type: String, default: '' }
})

const emit = defineEmits([
  'update:stationId',
  'update:dateRangeType',
  'update:dateRange',
  'search',
  'export'
])

// 双向绑定测站ID
const modelStationId = computed({
  get: () => props.stationId,
  set: (val) => emit('update:stationId', val)
})

// 测站选项映射
const stationOptions = computed(() => {
  return props.stationList.map(s => ({
    label: s.name,
    value: s.id
  }))
})

// 时间按钮配置
const timeButtons = [
  { label: '近24h', value: '24h' },
  { label: '近一周', value: 'week' },
  { label: '近一月', value: 'month' },
  { label: '自定义', value: 'custom' }
]

// 时间按钮点击
function onTimeButtonClick(type) {
  emit('update:dateRangeType', type)
}

// 格式化为input datetime-local格式
function formatForInput(dateStr) {
  if (!dateStr) return ''
  return dateStr.replace(' ', 'T').slice(0, 16)
}

// 开始日期变化
function onStartDateChange(val) {
  if (!val) return
  const dateTime = val.replace('T', ' ') + ':00'
  emit('update:dateRange', [dateTime, props.dateRange[1] || ''])
}

// 结束日期变化
function onEndDateChange(val) {
  if (!val) return
  const dateTime = val.replace('T', ' ') + ':00'
  emit('update:dateRange', [props.dateRange[0] || '', dateTime])
}
</script>

<template>
  <!-- 渗流量查询面板 -->
  <div class="bg-white rounded-lg shadow p-4 mb-4">
    <div class="flex flex-wrap items-center gap-4">
      <!-- 测站选择 -->
      <div class="flex items-center gap-2 w-72">
        <Select
          label="选择测站"
          v-model="modelStationId"
          :options="stationOptions"
        />
      </div>

      <!-- 时间选择 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700">时间选择</label>
        <div class="flex gap-1">
          <Button
            v-for="btn in timeButtons"
            :key="btn.value"
            size="sm"
            :type="dateRangeType === btn.value ? 'primary' : 'default'"
            @click="onTimeButtonClick(btn.value)"
          >
            {{ btn.label }}
          </Button>
        </div>
      </div>

      <!-- 自定义日期范围 -->
      <div v-if="dateRangeType === 'custom'" class="flex items-center gap-2">
        <Input
          type="datetime-local"
          :model-value="formatForInput(dateRange[0])"
          @update:model-value="onStartDateChange"
          class="w-48"
        />
        <span class="text-gray-500">至</span>
        <Input
          type="datetime-local"
          :model-value="formatForInput(dateRange[1])"
          @update:model-value="onEndDateChange"
          class="w-48"
        />
      </div>

      <!-- 日期显示 -->
      <span v-else class="text-sm font-medium text-gray-600">
        {{ displayDateRange }}
      </span>

      <!-- 操作按钮 -->
      <div class="flex gap-2 ml-auto">
        <Button
          type="primary"
          icon="search"
          @click="$emit('search')"
        >
          查询
        </Button>
        <Button
          type="success"
          icon="download"
          @click="$emit('export')"
        >
          导出
        </Button>
      </div>
    </div>
  </div>
</template>
