<template>
  <Card padding="none" shadow="md">
    <!-- 表格标题栏 -->
    <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
      <div class="flex items-center gap-3">
        <div class="h-6 w-1 rounded-full bg-primary-500" />
        <h3 class="text-base font-semibold text-gray-800">设备列表</h3>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">
          共 {{ devices.length }} 台设备
        </span>
      </div>

      <!-- 状态筛选 -->
      <div class="flex items-center gap-2">
        <button
          v-for="item in statusFilters"
          :key="item.value"
          class="px-3 py-1.5 text-xs font-medium rounded-lg border transition-colors"
          :class="activeStatus === item.value
            ? `bg-${item.color}-50 text-${item.color}-700 border-${item.color}-200`
            : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-50'"
          @click="$emit('status-filter', item.value)"
        >
          {{ item.label }}
        </button>
      </div>
    </div>

    <!-- 表格 -->
    <Table
      :columns="columns"
      :data="devices"
      :loading="loading"
      :show-pagination="false"
      row-key="name"
    >
      <!-- 设备名称 -->
      <template #name="{ row }">
        <div class="flex items-center gap-2">
          <i :class="typeIcon(row.type)" class="text-gray-400 text-sm w-4 text-center" />
          <span class="font-medium text-gray-800">{{ row.name }}</span>
        </div>
      </template>

      <!-- 设备类型 -->
      <template #type="{ row }">
        <span
          class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium"
          :class="typeBadgeClass(row.type)"
        >
          {{ typeLabel(row.type) }}
        </span>
      </template>

      <!-- 状态 -->
      <template #status="{ row }">
        <div class="flex items-center gap-2">
          <span
            class="w-2 h-2 rounded-full"
            :class="statusDotClass(row.status)"
          />
          <span
            class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium"
            :class="statusBadgeClass(row.status)"
          >
            {{ statusLabel(row.status) }}
          </span>
        </div>
      </template>

      <!-- 最后采集时间 -->
      <template #lastCollectTime="{ row }">
        <span class="text-sm text-gray-600">
          {{ row.lastCollectTime ? formatTime(row.lastCollectTime) : '--' }}
        </span>
      </template>

      <!-- 详情 -->
      <template #detail="{ row }">
        <span class="text-sm text-gray-500">
          {{ row.detail || '--' }}
        </span>
      </template>
    </Table>
  </Card>
</template>

<script setup>
/**
 * 设备状态列表组件
 * 功能: 展示所有设备的详细状态信息，支持按状态筛选
 * 依赖: Card, Table 基础组件
 */
import { computed } from 'vue'
import Card from '@/components/basic/Card.vue'
import Table from '@/components/basic/Table.vue'
import { DEVICE_TYPES, STATUS_CONFIG } from '@/composables/useDeviceMonitor'

const props = defineProps({
  devices: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  activeStatus: {
    type: String,
    default: null
  }
})

defineEmits(['status-filter'])

const columns = [
  { key: 'name', title: '设备名称', width: '200px' },
  { key: 'type', title: '设备类型', width: '130px' },
  { key: 'status', title: '状态', width: '120px' },
  { key: 'lastCollectTime', title: '最后采集时间', width: '180px' },
  { key: 'detail', title: '采集详情' }
]

const statusFilters = [
  { value: 'online', label: '在线', color: 'emerald' },
  { value: 'offline', label: '离线', color: 'red' },
  { value: 'abnormal', label: '采集异常', color: 'amber' }
]

const typeLabel = (type) => DEVICE_TYPES[type]?.label || type
const typeIcon = (type) => DEVICE_TYPES[type]?.icon || 'fa-microchip'
const typeBadgeClass = (type) => {
  const colorMap = { gnss: 'blue', rain: 'cyan', seepage: 'teal' }
  const c = colorMap[type] || 'gray'
  return `bg-${c}-50 text-${c}-700`
}

const statusLabel = (status) => STATUS_CONFIG[status]?.label || status
const statusDotClass = (status) => STATUS_CONFIG[status]?.dotClass || 'bg-gray-400'
const statusBadgeClass = (status) => {
  const colorMap = { online: 'emerald', offline: 'red', abnormal: 'amber' }
  const c = colorMap[status] || 'gray'
  return `bg-${c}-50 text-${c}-700`
}

const formatTime = (time) => {
  if (!time) return '--'
  const d = new Date(time)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
</script>
