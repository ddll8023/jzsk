<script setup>
/**
 * 闸门数据表格组件
 * 功能：展示闸门实时数据（仅最新一条）
 * 依赖组件：Card、Table
 */
import { computed } from 'vue'
import Card from '@/components/basic/Card.vue'
import Table from '@/components/basic/Table.vue'

const props = defineProps({
  columns: { type: Array, default: () => [] },
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

/**
 * 格式化数值：保留3位有效数字
 * @param {any} val - 原始值
 * @returns {string|any} - 格式化后的值
 */
function formatValue(val) {
  if (val === null || val === undefined || val === '') return '-'
  if (typeof val === 'number') {
    // 保留3位小数
    return Number(val.toFixed(3))
  }
  // 尝试解析字符串数字
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
</script>

<template>
  <Card>
    <template #header>
      <span class="text-lg font-semibold">闸门实时数据</span>
    </template>

    <Table
      :columns="columns"
      :data="formattedData"
      :loading="loading"
      :show-pagination="false"
      row-key="tm"
    />
  </Card>
</template>
