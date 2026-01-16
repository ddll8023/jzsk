<script setup>
/**
 * 渗流量数据表格组件
 * 功能：展示渗流量监测数据列表，支持分页
 */
import Table from '@/components/basic/Table.vue'

const props = defineProps({
  tableData: { type: Array, default: () => [] },
  total: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['page-change', 'size-change'])

// 表格列配置
const columns = [
  { title: '测站名称', key: 'stationName', width: '40%' },
  { title: '时间', key: 'formattedTime', width: '35%' },
  { title: '流量(L/s)', key: 'q1', width: '25%' }
]

// 分页变化
function onPageChange(page) {
  emit('page-change', page)
}

// 每页条数变化
function onSizeChange(size) {
  emit('size-change', size)
}

// 格式化流量值（转换为L/s）
function formatFlow(q1) {
  const val = Number(q1)
  if (isNaN(val)) return '-'
  return (val * 1000).toFixed(3)
}
</script>

<template>
  <!-- 渗流量数据表格 -->
  <div class="bg-white rounded-lg shadow p-4">
    <div class="border-b border-gray-200 pb-3 mb-2">
      <h3 class="text-base font-semibold text-gray-900">渗流量数据</h3>
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
      @update:page-size="onSizeChange"
    >
      <!-- 自定义列渲染 -->
      <template #stationName="{ row }">
        {{ row.stationName }}
      </template>

      <template #formattedTime="{ row }">
        {{ row.formattedTime }}
      </template>

      <template #q1="{ row }">
        <div class="text-center font-medium text-green-600">
          {{ formatFlow(row.q1) }}
        </div>
      </template>
    </Table>
  </div>
</template>
