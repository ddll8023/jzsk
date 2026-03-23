<template>
  <div class="w-full overflow-hidden">
    <!-- 表格容器 -->
    <div class="overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200">
        <!-- 表头 -->
        <thead class="bg-gray-50">
          <tr>
            <th
              v-for="column in columns"
              :key="column.key"
              :style="{ width: column.width }"
              class="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              {{ column.title }}
            </th>
          </tr>
        </thead>
        
        <!-- 表体 -->
        <tbody class="bg-white divide-y divide-gray-200">
          <!-- 加载状态 -->
          <tr v-if="loading">
            <td :colspan="columns.length" class="px-4 py-8 text-center">
              <i class="fa fa-spinner fa-spin text-primary-600 text-xl" aria-hidden="true"></i>
              <p class="mt-2 text-gray-500">加载中...</p>
            </td>
          </tr>
          
          <!-- 空数据 -->
          <tr v-else-if="!data || data.length === 0">
            <td :colspan="columns.length" class="px-4 py-8 text-center text-gray-500">
              <slot name="empty">
                <i class="fa fa-inbox text-4xl text-gray-300" aria-hidden="true"></i>
                <p class="mt-2">暂无数据</p>
              </slot>
            </td>
          </tr>
          
          <!-- 数据行 -->
          <tr
            v-else
            v-for="(row, rowIndex) in data"
            :key="rowKey ? row[rowKey] : rowIndex"
            class="hover:bg-gray-50 transition-colors"
          >
            <td
              v-for="column in columns"
              :key="column.key"
              class="px-4 py-3 text-sm text-gray-900 text-center"
            >
              <slot :name="column.key" :row="row" :index="rowIndex">
                {{ row[column.key] }}
              </slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 分页 -->
    <div v-if="showPagination && total > 0" class="flex items-center justify-center px-4 pt-4 pb-2 border-t border-gray-200">
      <Pagination
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        class="border-0 shadow-none px-0 py-0"
        @change="handlePageChange"
        @update:page-size="handleSizeChange"
        @update:current-page="handleCurrentPageChange"
      />
    </div>
  </div>
</template>

<script setup>
/**
 * Table 组件
 * 功能：表格展示组件
 * 遵循 KISS 原则：简洁实现
 */
import { computed } from 'vue'
import Pagination from './Pagination.vue'

const props = defineProps({
  columns: {
    type: Array,
    required: true
  },
  data: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  rowKey: {
    type: String,
    default: 'id'
  },
  showPagination: {
    type: Boolean,
    default: true
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
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50]
  }
})

const emit = defineEmits(['page-change', 'update:pageSize', 'update:currentPage'])

const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

/**
 * 处理分页变化 - 兼用两种事件格式以保持兼容性
 * Pagination 组件发出 ({ page, pageSize }) 
 * 同时也发出 update:currentPage 和 update:pageSize
 */
const handlePageChange = (payload) => {
  // 如果是对象（来自 Pagination 组件）
  if (typeof payload === 'object') {
    emit('page-change', payload.page)
    emit('update:currentPage', payload.page)
    emit('update:pageSize', payload.pageSize)
  } else {
    // 兼容旧的数字调用方式
    emit('page-change', payload)
    emit('update:currentPage', payload)
  }
}

/**
 * 处理每页条数变化
 */
const handleSizeChange = (size) => {
  emit('update:pageSize', size)
  // 当改变页大小时，通常重置到第一页，但这由 Pagination 组件控制触发 change 事件
}

/**
 * 处理当前页码变化（直接响应 Pagination 组件的 update:currentPage 事件）
 * 确保分页条 UI 能够及时同步更新
 */
const handleCurrentPageChange = (page) => {
  emit('update:currentPage', page)
}
</script>
