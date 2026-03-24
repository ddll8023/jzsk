<template>
  <div>
    <!-- 表格头部 -->
    <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
      <div class="flex items-center gap-3">
        <!-- 视觉锚点装饰 -->
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
        </svg>
        <h2 class="text-base font-bold text-gray-800">值班安排列表</h2>
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
      <!-- 值班时间列 -->
      <template #dutyTime="{ row }">
        {{ formatDateTime(row.dutyTime) }}
      </template>

      <!-- 创建时间列 -->
      <template #createTime="{ row }">
        {{ formatDateTime(row.createTime) }}
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
 * 值班安排表格组件
 * 功能：展示值班安排列表，支持编辑、删除操作
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
  formatDateTime: {
    type: Function,
    required: true
  }
})

defineEmits(['page-change', 'update:pageSize', 'edit', 'delete'])

// 表格列配置
const columns = [
  { key: 'dutyScheduleId', title: '值班安排ID', width: '100px' },
  { key: 'dutyPerson', title: '值班人员' },
  { key: 'leader', title: '带班领导' },
  { key: 'dutyTime', title: '值班时间' },
  { key: 'dutyPost', title: '值班岗位' },
  { key: 'createTime', title: '创建时间' },
  { key: 'actions', title: '操作', width: '180px' }
]
</script>
