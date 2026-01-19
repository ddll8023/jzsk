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
      <template #值班日期="{ row }">
        {{ formatDate(row.值班日期) }}
      </template>

      <!-- 日志填写时间列 -->
      <template #日志填写时间="{ row }">
        {{ formatDateTime(row.日志填写时间) }}
      </template>

      <!-- 日志内容列 -->
      <template #日志内容="{ row }">
        <div class="max-w-xs truncate" :title="row.日志内容">
          {{ row.日志内容 }}
        </div>
      </template>

      <!-- 操作列 -->
      <template #操作="{ row }">
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
  { key: '值班日志id', title: '值班日志ID', width: '100px' },
  { key: '值班日期', title: '值班日期' },
  { key: '天气', title: '天气' },
  { key: '雨量', title: '雨量' },
  { key: '带班领导', title: '带班领导' },
  { key: '白班值班人员', title: '白班值班人员' },
  { key: '晚班值班人员', title: '晚班值班人员' },
  { key: '日志内容', title: '日志内容' },
  { key: '日志填写时间', title: '日志填写时间' },
  { key: '日志状态', title: '日志状态' },
  { key: '操作', title: '操作', width: '180px' }
]
</script>
