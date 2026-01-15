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
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
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
              class="px-4 py-3 text-sm text-gray-900"
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
    <div v-if="showPagination && total > 0" class="flex items-center justify-between px-4 py-3 border-t border-gray-200">
      <div class="text-sm text-gray-500">
        共 {{ total }} 条
      </div>
      <div class="flex items-center gap-2">
        <button
          :disabled="currentPage <= 1"
          class="px-3 py-1 text-sm border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          @click="handlePageChange(currentPage - 1)"
        >
          <i class="fa fa-angle-left" aria-hidden="true"></i>
        </button>
        <span class="text-sm text-gray-700">{{ currentPage }} / {{ totalPages }}</span>
        <button
          :disabled="currentPage >= totalPages"
          class="px-3 py-1 text-sm border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          @click="handlePageChange(currentPage + 1)"
        >
          <i class="fa fa-angle-right" aria-hidden="true"></i>
        </button>
      </div>
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
  }
})

const emit = defineEmits(['page-change'])

// 计算总页数
const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

/**
 * 处理分页变化
 */
const handlePageChange = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    emit('page-change', page)
  }
}
</script>
