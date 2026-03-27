<template>
  <div>
    <!-- 表格头部 -->
    <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
      <div class="flex items-center gap-3">
        <!-- 视觉锚点装饰 -->
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
        <h2 class="text-base font-bold text-gray-800">值班日志列表</h2>
      </div>
      <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ total }} 条记录</span>
    </div>
    
    <!-- 表格内容 -->
    <div class="p-0">
      <Table
        :columns="columns"
        :data="data"
        :loading="loading"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        :show-pagination="true"
        @page-change="$emit('page-change', $event)"
        @update:pageSize="$emit('update:pageSize', $event)"
      >
      <!-- 值班日期列 -->
      <template #dutyDate="{ row }">
        {{ formatDate(row.dutyDate) }}
      </template>

      <!-- 日志填写时间列 -->
      <template #fillTime="{ row }">
        {{ formatDateTime(row.fillTime) }}
      </template>

      <!-- 日志内容列 -->
      <template #logContent="{ row }">
        <div class="line-clamp-3 text-left" :title="row.logContent">
          {{ row.logContent }}
        </div>
      </template>

      <!-- 操作列 -->
      <template #actions="{ row }">
        <div class="flex items-center justify-center gap-2">
          <Button size="sm" icon="edit" @click="$emit('edit', row)">
            编辑
          </Button>
          <Button size="sm" type="danger" icon="trash" @click="$emit('delete', row)">
            删除
          </Button>
        </div>
      </template>
      </Table>
    </div>
  </div>
</template>

<script setup>
/**
 * 值班日志表格组件
 * 功能：展示值班日志列表，支持编辑、删除操作
 * 遵循原则：KISS, YAGNI, SOLID
 */
import Table from '@/components/basic/Table.vue'
import Button from '@/components/basic/Button.vue'

defineProps({
  data: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  total: {
    type: Number,
    default: 0
  },
  currentPage: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 10
  },
  formatDate: {
    type: Function,
    required: true
  },
  formatDateTime: {
    type: Function,
    required: true
  }
})

defineEmits(['page-change', 'update:pageSize', 'edit', 'delete'])

// 表格列配置
const columns = [
  { key: 'dutyDate', title: '值班日期', width: '100px' },
  { key: 'weather', title: '天气', width: '80px' },
  { key: 'rainfall', title: '雨量', width: '80px' },
  { key: 'leader', title: '带班领导', width: '100px' },
  { key: 'dayShiftPerson', title: '白班值班人员', width: '100px' },
  { key: 'nightShiftPerson', title: '晚班值班人员', width: '100px' },
  { key: 'logContent', title: '日志内容', width: '200px', noEllipsis: true },
  { key: 'fillTime', title: '填写时间', width: '140px' },
  { key: 'logStatus', title: '状态', width: '80px' },
  { key: 'actions', title: '操作', width: '140px', noEllipsis: true }
]
</script>
