<template>
  <Modal
    :model-value="modelValue"
    title="历史到报情况"
    width="full"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <div class="min-h-[600px]">
    <!-- 筛选条件 -->
    <Card variant="default" padding="md" shadow="sm" class="mb-4">
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-3">
        <Select
          v-model="query.deviceType"
          :options="deviceTypeOptions"
          placeholder="全部类型"
          size="sm"
          @change="handleQueryChange"
        />
        <Select
          v-model="query.faultStatus"
          :options="faultStatusOptions"
          placeholder="全部状态"
          size="sm"
          @change="handleQueryChange"
        />
        <Select
          v-model="query.processStatus"
          :options="processStatusOptions"
          placeholder="全部处理"
          size="sm"
          @change="handleQueryChange"
        />
        <div>
          <input
            v-model="query.startTime"
            type="datetime-local"
            class="w-full px-3 py-1.5 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500 focus:border-primary-500"
            @change="handleQueryChange"
          />
        </div>
        <div>
          <input
            v-model="query.endTime"
            type="datetime-local"
            class="w-full px-3 py-1.5 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500 focus:border-primary-500"
            @change="handleQueryChange"
          />
        </div>
        <Input
          v-model="query.keyword"
          placeholder="设备名称/编码"
          prefix-icon="search"
          size="sm"
          @keyup.enter="handleQueryChange"
        />
      </div>
        <div class="flex gap-2 mt-4">
          <Button type="primary" size="sm" @click="handleQueryChange">
            <i class="fa fa-search mr-2"></i>查询
          </Button>
          <Button type="secondary" size="sm" @click="resetQuery">
            <i class="fa fa-redo mr-2"></i>重置
          </Button>
        </div>
    </Card>

    <!-- 表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-3">
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <i class="fa fa-history text-gray-400"></i>
          <h2 class="text-base font-bold text-gray-800">到报情况列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ total }} 条记录</span>
      </div>
      <div class="p-0">
    <Table
      :columns="columns"
      :data="records"
      :loading="loading"
      :show-pagination="true"
      :total="total"
      :current-page="query.page"
      :page-size="query.size"
      :page-sizes="[10, 20, 50]"
      row-key="id"
      @page-change="handlePageChange"
      @update:page-size="handleSizeChange"
      @update:current-page="handleCurrentPageChange"
    >
      <!-- 设备名称 -->
      <template #deviceName="{ row }">
        <span class="font-medium text-gray-800">{{ row.deviceName }}</span>
      </template>

      <!-- 设备类型 -->
      <template #deviceType="{ row }">
        <span
          class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium"
          :class="deviceTypeBadgeClass(row.deviceType)"
        >
          {{ deviceTypeLabel(row.deviceType) }}
        </span>
      </template>

      <!-- 故障状态 -->
      <template #currentFaultStatus="{ row }">
        <span
          class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium"
          :class="faultStatusBadgeClass(row.currentFaultStatus)"
        >
          {{ faultStatusLabel(row.currentFaultStatus) }}
        </span>
      </template>

      <!-- 开始时间 -->
      <template #startTime="{ row }">
        <span class="text-sm text-gray-600">{{ formatTime(row.startTime) }}</span>
      </template>

      <!-- 恢复时间 -->
      <template #endTime="{ row }">
        <span class="text-sm text-gray-600">{{ row.endTime ? formatTime(row.endTime) : '--' }}</span>
      </template>

      <!-- 持续时长 -->
      <template #durationMinutes="{ row }">
        <span v-if="row.processStatus === 'active'" class="text-xs font-medium text-amber-600">
          持续中
        </span>
        <span v-else class="text-sm text-gray-600">{{ formatDuration(row.durationMinutes) }}</span>
      </template>

      <!-- 处理状态 -->
      <template #processStatus="{ row }">
        <span
          class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-medium"
          :class="processStatusBadgeClass(row.processStatus)"
        >
          <span v-if="row.processStatus === 'active'" class="w-1.5 h-1.5 rounded-full bg-amber-500 animate-pulse" />
          {{ processStatusLabel(row.processStatus) }}
        </span>
      </template>

      <!-- 到报详情 -->
      <template #faultDetail="{ row }">
        <span class="text-sm text-gray-600 block max-w-[150px] break-all line-clamp-2" :title="row.faultDetail || ''">
          {{ row.faultDetail || '--' }}
        </span>
      </template>

      <!-- 操作 -->
      <template #action="{ row }">
        <div class="flex items-center">
          <button
            class="text-sm text-red-500 hover:text-red-700 font-medium transition-colors"
            @click="confirmDelete(row)"
          >
            删除
          </button>
        </div>
      </template>
    </Table>
      </div>
    </Card>

    <!-- 删除确认弹窗 -->
    <Modal
      v-model="deleteModalVisible"
      title="确认删除"
      width="sm"
    >
      <div class="py-4">
        <p class="text-gray-700 mb-4">确认删除该到报情况记录吗？删除后不可恢复。</p>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">设备名称：</span><span class="text-gray-900">{{ deleteTarget?.deviceName }}</span></div>
          <div><span class="text-gray-500">设备类型：</span><span class="text-gray-900">{{ deviceTypeLabel(deleteTarget?.deviceType) }}</span></div>
          <div><span class="text-gray-500">开始时间：</span><span class="text-gray-900">{{ formatTime(deleteTarget?.startTime) }}</span></div>
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end gap-2">
          <Button type="secondary" size="sm" @click="deleteModalVisible = false">取消</Button>
          <Button type="danger" size="sm" @click="handleDelete">确认删除</Button>
        </div>
      </template>
    </Modal>
    </div>
  </Modal>
</template>

<script setup>
/**
 * 历史到报情况弹窗组件
 * 功能：筛选、分页查询、展示设备到报情况主记录列表，支持删除记录
 * 依赖：Modal, Table, Select, Input 组件
 * 特性：状态管理内置，弹窗打开自动加载，筛选变更重置分页
 */
import { ref, watch } from 'vue'
import Modal from '@/components/basic/Modal.vue'
import Card from '@/components/basic/Card.vue'
import Button from '@/components/basic/Button.vue'
import Table from '@/components/basic/Table.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'
import { getDeviceFaultRecordPage, deleteDeviceFaultRecord } from '@/api/deviceMonitor'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

defineEmits(['update:modelValue'])

// ==================== 筛选参数 ====================
const query = ref({
  page: 1,
  size: 10,
  deviceType: '',
  faultStatus: '',
  processStatus: 'active',
  keyword: '',
  startTime: '',
  endTime: ''
})

// ==================== 下拉选项 ====================
const deviceTypeOptions = [
  { label: 'GNSS 地表位移', value: 'gnss' },
  { label: '水雨情', value: 'rain' },
  { label: '渗流渗压', value: 'seepage' }
]

const faultStatusOptions = [
  { label: '未到报', value: 'offline' },
  { label: '采集异常', value: 'abnormal' }
]

const processStatusOptions = [
  { label: '未恢复', value: 'active' },
  { label: '已恢复', value: 'resolved' }
]

// ==================== 表格列定义 ====================
const columns = [
  { key: 'deviceName', title: '设备名称', width: '120px' },
  { key: 'deviceType', title: '设备类型', width: '100px' },
  { key: 'currentFaultStatus', title: '故障状态', width: '90px' },
  { key: 'startTime', title: '开始时间', width: '145px' },
  { key: 'endTime', title: '恢复时间', width: '145px' },
  { key: 'durationMinutes', title: '持续时长', width: '85px' },
  { key: 'processStatus', title: '处理状态', width: '85px' },
  { key: 'faultDetail', title: '到报详情', width: '155px' },
  { key: 'action', title: '操作', width: '70px' }
]

// ==================== 数据状态 ====================
const loading = ref(false)
const records = ref([])
const total = ref(0)
const deleteModalVisible = ref(false)
const deleteTarget = ref(null)

// ==================== 数据加载 ====================

/**
 * 加载到报情况分页数据
 */
async function fetchData() {
  loading.value = true
  try {
    const params = { ...query.value }
    // 转换 datetime-local 为后端格式
    if (params.startTime) {
      params.startTime = params.startTime.replace('T', ' ') + ':00'
    }
    if (params.endTime) {
      params.endTime = params.endTime.replace('T', ' ') + ':59'
    }
    // 清除空值
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })
    const res = await getDeviceFaultRecordPage(params)
    const pageData = res.data?.data
    if (pageData) {
      records.value = pageData.records || pageData.list || []
      total.value = pageData.total || 0
    }
  } catch (e) {
    console.error('[DeviceFaultHistoryModal] 数据加载失败:', e)
    records.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/**
 * 筛选条件变更，重置到第一页重新查询
 */
function handleQueryChange() {
  query.value.page = 1
  fetchData()
}

/**
 * 分页切换
 */
function handlePageChange(page) {
  query.value.page = page
  fetchData()
}

function handleCurrentPageChange(page) {
  query.value.page = page
  fetchData()
}

function handleSizeChange(size) {
  query.value.size = size
  query.value.page = 1
  fetchData()
}

/**
 * 重置筛选条件
 */
function resetQuery() {
  query.value = {
    page: 1,
    size: query.value.size,
    deviceType: '',
    faultStatus: '',
    processStatus: 'active',
    keyword: '',
    startTime: '',
    endTime: ''
  }
  fetchData()
}

/**
 * 打开删除确认弹窗
 */
function confirmDelete(row) {
  deleteTarget.value = row
  deleteModalVisible.value = true
}

/**
 * 执行删除
 */
async function handleDelete() {
  try {
    await deleteDeviceFaultRecord(deleteTarget.value.id)
    deleteModalVisible.value = false
    deleteTarget.value = null
    fetchData()
  } catch (e) {
    console.error('[DeviceFaultHistoryModal] 删除失败:', e)
  }
}

// ==================== 显示映射 ====================

const DEVICE_TYPE_MAP = {
  gnss: 'GNSS 地表位移',
  rain: '水雨情',
  seepage: '渗流渗压'
}

const FAULT_STATUS_MAP = {
  offline: '未到报',
  abnormal: '采集异常'
}

const PROCESS_STATUS_MAP = {
  active: '未恢复',
  resolved: '已恢复'
}

const DEVICE_TYPE_BADGE = {
  gnss: 'bg-blue-50 text-blue-700',
  rain: 'bg-cyan-50 text-cyan-700',
  seepage: 'bg-teal-50 text-teal-700'
}

const FAULT_STATUS_BADGE = {
  offline: 'bg-amber-50 text-amber-700',
  abnormal: 'bg-red-50 text-red-700'
}

const PROCESS_STATUS_BADGE = {
  active: 'bg-amber-50 text-amber-700',
  resolved: 'bg-emerald-50 text-emerald-700'
}

const deviceTypeLabel = (type) => DEVICE_TYPE_MAP[type] || type
const deviceTypeBadgeClass = (type) => DEVICE_TYPE_BADGE[type] || 'bg-gray-50 text-gray-700'
const faultStatusLabel = (status) => FAULT_STATUS_MAP[status] || status
const faultStatusBadgeClass = (status) => FAULT_STATUS_BADGE[status] || 'bg-gray-50 text-gray-700'
const processStatusLabel = (status) => PROCESS_STATUS_MAP[status] || status
const processStatusBadgeClass = (status) => PROCESS_STATUS_BADGE[status] || 'bg-gray-50 text-gray-700'

/**
 * 格式化持续时间（分钟 → 可读文本）
 */
function formatDuration(minutes) {
  if (minutes === null || minutes === undefined) return '--'
  if (minutes < 60) return `${minutes}分钟`
  if (minutes < 1440) {
    const h = Math.floor(minutes / 60)
    const m = minutes % 60
    return m > 0 ? `${h}小时${m}分钟` : `${h}小时`
  }
  const d = Math.floor(minutes / 1440)
  const remaining = minutes % 1440
  const h = Math.floor(remaining / 60)
  return h > 0 ? `${d}天${h}小时` : `${d}天`
}

/**
 * 格式化时间
 */
function formatTime(time) {
  if (!time) return '--'
  const d = new Date(time)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

// ==================== 弹窗打开时自动加载 ====================
watch(() => props.modelValue, (val) => {
  if (val) {
    query.value.page = 1
    deleteModalVisible.value = false
    deleteTarget.value = null
    fetchData()
  }
})
</script>
