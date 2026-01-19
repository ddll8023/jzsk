<script setup>
/**
 * 闸门报表表格组件
 * 功能：展示闸门历史数据，支持分页
 * 依赖组件：Card、Table、Pagination
 * 遵循原则：KISS - 简洁实现，复用基础组件
 */
import { computed } from 'vue'
import Card from '@/components/basic/Card.vue'
import Table from '@/components/basic/Table.vue'
import Pagination from '@/components/basic/Pagination.vue'

const props = defineProps({
  columns: { type: Array, default: () => [] },
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  pagination: {
    type: Object,
    default: () => ({ current: 1, size: 10, total: 0 })
  }
})

const emit = defineEmits(['update:pagination'])

/**
 * 格式化数值：保留3位小数
 * @param {any} val - 原始值
 * @returns {string|any} - 格式化后的值
 */
function formatValue(val) {
  if (val === null || val === undefined || val === '') return '-'
  if (typeof val === 'number') {
    return Number(val.toFixed(3))
  }
  const num = parseFloat(val)
  if (!isNaN(num) && val.toString().includes('.')) {
    return Number(num.toFixed(3))
  }
  return val
}

// 格式化后的数据
const formattedData = computed(() => {
  return props.data.map(row => {
    const newRow = {}
    for (const key in row) {
      newRow[key] = formatValue(row[key])
    }
    return newRow
  })
})

// 处理分页变化
function handlePageChange({ page, pageSize }) {
  emit('update:pagination', {
    ...props.pagination,
    current: page,
    size: pageSize
  })
}
</script>

<template>
  <Card>
    <template #header>
      <div class="flex items-center justify-between">
        <span class="text-lg font-semibold">闸门历史数据</span>
        <span class="text-sm text-gray-500">共 {{ pagination.total }} 条记录</span>
      </div>
    </template>

    <!-- 数据表格 -->
    <Table
      :columns="columns"
      :data="formattedData"
      :loading="loading"
      :show-pagination="false"
      row-key="tm"
    />

    <!-- 分页组件 -->
    <div v-if="pagination.total > 0" class="mt-4">
      <Pagination
        :total="pagination.total"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        @change="handlePageChange"
      />
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && formattedData.length === 0" class="flex flex-col items-center justify-center py-12 text-gray-400">
      <i class="fa fa-inbox text-4xl mb-3" aria-hidden="true"></i>
      <p class="text-sm">暂无数据</p>
    </div>
  </Card>
</template>
