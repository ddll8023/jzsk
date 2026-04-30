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
          :class="activeStatus === item.value ? item.activeClass : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-50'"
          @click="$emit('status-filter', item.value)"
        >
          {{ item.label }}
        </button>
      </div>
    </div>

    <!-- 表格 -->
    <Table
      :columns="activeColumns"
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

      <!-- 设备类型（全部模式下显示） -->
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

      <!-- 关键指标（全部模式下显示，按类型摘要） -->
      <template #summary="{ row }">
        <span class="text-sm text-gray-500">
          {{ formatSummary(row) }}
        </span>
      </template>

      <!-- 2D合位移 -->
      <template #displacement2d="{ row }">
        <span class="text-sm font-mono" :class="valueColor(parseDetailValue(row.detail, 'displacement2d'), 10)">
          {{ formatValue(parseDetailValue(row.detail, 'displacement2d'), 'mm') }}
        </span>
      </template>

      <!-- 3D合位移 -->
      <template #displacement3d="{ row }">
        <span class="text-sm font-mono" :class="valueColor(parseDetailValue(row.detail, 'displacement3d'), 10)">
          {{ formatValue(parseDetailValue(row.detail, 'displacement3d'), 'mm') }}
        </span>
      </template>

      <!-- X位移 -->
      <template #gpsTotalX="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, 'gpsTotalX'), 'mm') }}
        </span>
      </template>

      <!-- Y位移 -->
      <template #gpsTotalY="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, 'gpsTotalY'), 'mm') }}
        </span>
      </template>

      <!-- Z位移 -->
      <template #gpsTotalZ="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, 'gpsTotalZ'), 'mm') }}
        </span>
      </template>

      <!-- 水位 -->
      <template #waterLevel="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ row.type === 'seepage' ? formatValue(parseDetailValue(row.detail, '水位'), 'm', 3) : formatValue(parseDetailValue(row.detail, '水位'), 'm') }}
        </span>
      </template>

      <!-- 雨量 -->
      <template #rainfall="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, '雨量'), 'mm') }}
        </span>
      </template>

      <!-- 水位高程 -->
      <template #waterElevation="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, '水位高程'), 'm', 3) }}
        </span>
      </template>

      <!-- 渗压水压 -->
      <template #waterPressure="{ row }">
        <span class="text-sm font-mono text-gray-700">
          {{ formatValue(parseDetailValue(row.detail, '水压'), 'kPa', 3) }}
        </span>
      </template>
    </Table>
  </Card>
</template>

<script setup>
/**
 * 设备状态列表组件
 * 功能: 展示所有设备的详细状态信息，支持按状态筛选
 * 特性: 根据当前设备类型动态切换表格列，展示对应的结构化数据
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
  },
  activeType: {
    type: String,
    default: null
  }
})

defineEmits(['status-filter'])

/**
 * 根据设备类型动态定义列
 */
const COLUMNS_ALL = [
  { key: 'name', title: '设备名称', width: '180px' },
  { key: 'type', title: '设备类型', width: '120px' },
  { key: 'status', title: '状态', width: '110px' },
  { key: 'lastCollectTime', title: '最后采集时间', width: '170px' },
  { key: 'summary', title: '关键指标' }
]

const COLUMNS_GNSS = [
  { key: 'name', title: '设备名称', width: '100px' },
  { key: 'status', title: '状态', width: '100px' },
  { key: 'lastCollectTime', title: '最后采集时间', width: '160px' },
  { key: 'displacement2d', title: '2D合位移', width: '100px' },
  { key: 'displacement3d', title: '3D合位移', width: '100px' },
  { key: 'gpsTotalX', title: 'X位移', width: '100px' },
  { key: 'gpsTotalY', title: 'Y位移', width: '100px' },
  { key: 'gpsTotalZ', title: 'Z位移', width: '100px' }
]

const COLUMNS_RAIN = [
  { key: 'name', title: '设备名称', width: '160px' },
  { key: 'status', title: '状态', width: '110px' },
  { key: 'lastCollectTime', title: '最后采集时间', width: '170px' },
  { key: 'waterLevel', title: '水位', width: '120px' },
  { key: 'rainfall', title: '雨量', width: '120px' }
]

const COLUMNS_SEEPAGE = [
  { key: 'name', title: '设备名称', width: '200px' },
  { key: 'status', title: '状态', width: '100px' },
  { key: 'lastCollectTime', title: '最后采集时间', width: '160px' },
  { key: 'waterElevation', title: '水位高程', width: '120px' },
  { key: 'waterLevel', title: '水位', width: '120px' },
  { key: 'waterPressure', title: '水压', width: '120px' }
]

const TYPE_COLUMNS_MAP = {
  gnss: COLUMNS_GNSS,
  rain: COLUMNS_RAIN,
  seepage: COLUMNS_SEEPAGE
}

const activeColumns = computed(() => {
  return TYPE_COLUMNS_MAP[props.activeType] || COLUMNS_ALL
})

const statusFilters = [
  { value: null, label: '全部', activeClass: 'bg-gray-100 text-gray-700 border-gray-300' },
  { value: 'online', label: '在线', activeClass: 'bg-emerald-50 text-emerald-700 border-emerald-200' },
  { value: 'offline', label: '离线', activeClass: 'bg-red-50 text-red-700 border-red-200' },
  { value: 'abnormal', label: '采集异常', activeClass: 'bg-amber-50 text-amber-700 border-amber-200' }
]

const TYPE_BADGE_MAP = {
  gnss: 'bg-blue-50 text-blue-700',
  rain: 'bg-cyan-50 text-cyan-700',
  seepage: 'bg-teal-50 text-teal-700'
}

const STATUS_BADGE_MAP = {
  online: 'bg-emerald-50 text-emerald-700',
  offline: 'bg-red-50 text-red-700',
  abnormal: 'bg-amber-50 text-amber-700'
}

const typeLabel = (type) => DEVICE_TYPES[type]?.label || type
const typeIcon = (type) => DEVICE_TYPES[type]?.icon || 'fa-microchip'
const typeBadgeClass = (type) => TYPE_BADGE_MAP[type] || 'bg-gray-50 text-gray-700'

const statusLabel = (status) => STATUS_CONFIG[status]?.label || status
const statusDotClass = (status) => STATUS_CONFIG[status]?.dotClass || 'bg-gray-400'
const statusBadgeClass = (status) => STATUS_BADGE_MAP[status] || 'bg-gray-50 text-gray-700'

/**
 * 解析 detail 字符串中的指定键值
 * detail 格式: "key1: value1; key2: value2"
 */
const parseDetailValue = (detail, key) => {
  if (!detail) return null
  const pair = detail.split('; ').find(s => s.startsWith(key + ': '))
  if (!pair) return null
  const val = parseFloat(pair.replace(key + ': ', ''))
  return isNaN(val) ? null : val
}

/**
 * 格式化数值 + 单位
 */
const formatValue = (val, unit, decimals = null) => {
  if (val === null) return '--'
  const formatted = decimals !== null ? val.toFixed(decimals) : val
  return `${formatted}${unit}`
}

/**
 * 超过阈值变红
 */
const valueColor = (val, threshold) => {
  if (val === null) return 'text-gray-400'
  return Math.abs(val) > threshold ? 'text-red-600 font-semibold' : 'text-gray-700'
}

/**
 * 全部模式下的摘要：按类型取关键指标
 */
const formatSummary = (row) => {
  if (!row.detail) return '--'
  if (row.type === 'gnss') {
    const d2 = parseDetailValue(row.detail, 'displacement2d')
    const d3 = parseDetailValue(row.detail, 'displacement3d')
    if (d2 === null) return row.detail
    return `2D: ${d2}mm / 3D: ${d3}mm`
  }
  if (row.type === 'rain') {
    return row.detail
  }
  if (row.type === 'seepage') {
    const wp = parseDetailValue(row.detail, '水压')
    const wl = parseDetailValue(row.detail, '水位')
    if (wp === null) return row.detail
    return `水位: ${wl.toFixed(3)}m / 水压: ${wp.toFixed(3)}kPa`
  }
  return row.detail
}

const formatTime = (time) => {
  if (!time) return '--'
  const d = new Date(time)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
</script>
