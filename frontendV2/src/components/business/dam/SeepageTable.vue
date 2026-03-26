<script setup>
/**
 * 渗流数据表格组件
 * 功能：展示渗流监测数据列表，支持分页
 */
import { computed } from 'vue'
import Table from '@/components/basic/Table.vue'
import { formatMinute, parseJsonField, formatUpNameById } from '@/composables/useSeepage'

const props = defineProps({
  tableData: { type: Array, default: () => [] },
  total: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['page-change'])

// 表格列配置
const columns = [
  { title: '采集时间', key: 'time', width: '20%' },
  { title: '测站名称', key: 'pointId', width: '20%' },
  { title: '水位高程(m)', key: 'waterElevation', width: '15%' },
  { title: '水位(mm)', key: 'waterLevel', width: '15%' },
  { title: '温度(°C)', key: 'temperature', width: '15%' },
  { title: '水压', key: 'pressure', width: '15%' }
]

// 分页变化
function onPageChange(page) {
  emit('page-change', page)
}

// 获取测点显示名称
function getPointName(pointId) {
  if (!pointId) return ''
  const name = formatUpNameById(pointId)
  return name || pointId
}
</script>

<template>
  <!-- 渗流数据表格 -->
  <div class="bg-white rounded-lg shadow p-4">
    <div class="border-b border-gray-200 pb-3 mb-2">
      <h3 class="text-base font-semibold text-gray-900">渗流数据</h3>
    </div>

    <Table
      :columns="columns"
      :data="tableData"
      :loading="loading"
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      @page-change="onPageChange"
      @update:current-page="onPageChange"
    >
      <!-- 自定义列渲染 -->
      <template #time="{ row }">
        {{ formatMinute(row.time) }}
      </template>

      <template #pointId="{ row }">
        {{ getPointName(row.pointName) }}
      </template>

      <template #waterElevation="{ row }">
        <div class="text-center">{{ parseJsonField(row.resultData, '水位高程') }}</div>
      </template>

      <template #waterLevel="{ row }">
        <div class="text-center">{{ parseJsonField(row.resultData, '水位') }}</div>
      </template>

      <template #temperature="{ row }">
        <div class="text-center">{{ parseJsonField(row.originalData, '温度') }}</div>
      </template>

      <template #pressure="{ row }">
        <div class="text-center">{{ parseJsonField(row.resultData, '水压') }}</div>
      </template>
    </Table>
  </div>
</template>

