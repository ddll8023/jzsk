<template>
  <div class="mt-4">
    <!-- 加载中 -->
    <div v-if="loading" class="flex items-center justify-center py-6">
      <i class="fa fa-spinner fa-spin text-gray-400 mr-2" aria-hidden="true" />
      <span class="text-sm text-gray-400">加载事件明细...</span>
    </div>

    <!-- 空状态 -->
    <div v-else-if="events.length === 0" class="flex flex-col items-center justify-center py-6">
      <i class="fa fa-stream text-gray-300 text-2xl mb-2" aria-hidden="true" />
      <span class="text-sm text-gray-400">暂无事件明细</span>
    </div>

    <!-- 时间线 -->
    <div v-else class="relative pl-6">
      <!-- 时间线竖线 -->
      <div class="absolute left-[11px] top-2 bottom-2 w-px bg-gray-200" />

      <div v-for="event in events" :key="event.id" class="relative pb-4 last:pb-0">
        <!-- 时间线节点 -->
        <div
          class="absolute -left-6 top-1 w-[22px] h-[22px] rounded-full border-2 flex items-center justify-center"
          :class="nodeClass(event.eventType)"
        >
          <div class="w-2 h-2 rounded-full" :class="dotClass(event.eventType)" />
        </div>

        <!-- 事件内容 -->
        <div class="ml-2">
          <div class="flex items-center gap-2">
            <span class="text-sm font-medium" :class="statusTextColor(event.eventStatus)">
              {{ statusLabel(event.eventStatus) }}
            </span>
            <span
              class="inline-flex items-center px-1.5 py-0.5 rounded text-xs font-medium"
              :class="typeBadgeClass(event.eventType)"
            >
              {{ typeLabel(event.eventType) }}
            </span>
          </div>
          <div class="text-xs text-gray-400 mt-0.5">
            {{ formatTime(event.eventTime) }}
          </div>
          <div v-if="event.eventDetail" class="text-xs text-gray-500 mt-1">
            {{ event.eventDetail }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 到报事件时间线组件
 * 功能：根据到报情况主记录ID加载并展示事件时间线
 * 依赖：getDeviceFaultEvents API
 * 特性：加载状态、空状态、时间线可视化
 */
import { ref, watch } from 'vue'
import { getDeviceFaultEvents } from '@/api/deviceMonitor'

const props = defineProps({
  faultRecordId: {
    type: Number,
    default: null
  }
})

const loading = ref(false)
const events = ref([])

async function fetchEvents() {
  if (!props.faultRecordId) {
    events.value = []
    return
  }
  loading.value = true
  try {
    const res = await getDeviceFaultEvents(props.faultRecordId)
    events.value = res.data?.data || []
  } catch (e) {
    console.error('[DeviceFaultEventTimeline] 加载事件失败:', e)
    events.value = []
  } finally {
    loading.value = false
  }
}

watch(() => props.faultRecordId, (val) => {
  if (val) {
    fetchEvents()
  } else {
    events.value = []
  }
}, { immediate: true })

const STATUS_LABEL_MAP = {
  online: '已到报',
  offline: '未到报',
  abnormal: '采集异常'
}

const TYPE_LABEL_MAP = {
  fault_start: '异常开始',
  status_change: '状态变化',
  fault_recover: '恢复到报'
}

const statusLabel = (status) => STATUS_LABEL_MAP[status] || status
const typeLabel = (type) => TYPE_LABEL_MAP[type] || type

const statusTextColor = (status) => {
  if (status === 'online') return 'text-emerald-600'
  if (status === 'offline') return 'text-red-600'
  return 'text-amber-600'
}

const typeBadgeClass = (type) => {
  if (type === 'fault_start') return 'bg-red-50 text-red-600'
  if (type === 'status_change') return 'bg-amber-50 text-amber-600'
  return 'bg-emerald-50 text-emerald-600'
}

const nodeClass = (type) => {
  if (type === 'fault_start') return 'border-red-300 bg-red-50'
  if (type === 'status_change') return 'border-amber-300 bg-amber-50'
  return 'border-emerald-300 bg-emerald-50'
}

const dotClass = (type) => {
  if (type === 'fault_start') return 'bg-red-400'
  if (type === 'status_change') return 'bg-amber-400'
  return 'bg-emerald-400'
}

function formatTime(time) {
  if (!time) return '--'
  const d = new Date(time)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
</script>
