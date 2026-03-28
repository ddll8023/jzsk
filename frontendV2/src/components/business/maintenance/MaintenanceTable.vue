<template>
  <Table
    :columns="columns"
    :data="data"
    :loading="loading"
    :total="total"
    :current-page="currentPage"
    :page-size="pageSize"
    :show-pagination="true"
    @page-change="$emit('page-change', $event)"
    @update:page-size="$emit('update:page-size', $event)"
  >
    <!-- 备注列 -->
    <template #note="{ row }">
      <div class="text-left break-words hyphens-auto whitespace-pre-wrap">
        {{ row.note || '-' }}
      </div>
    </template>

    <!-- 操作列 -->
    <template #actions="{ row }">
      <div class="flex items-center justify-center gap-2">
        <Button size="sm" type="default" @click="$emit('edit', row)">
          编辑
        </Button>
        <Button size="sm" type="danger" @click="$emit('delete', row)">
          删除
        </Button>
      </div>
    </template>
  </Table>
</template>

<script setup>
/**
 * 维护记录表格
 * 功能：数据展示、操作按钮
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { computed } from 'vue'
import Table from '@/components/basic/Table.vue'
import Button from '@/components/basic/Button.vue'

defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  total: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 }
})

defineEmits(['page-change', 'update:page-size', 'edit', 'delete'])

// 表格列定义
const columns = computed(() => [
  { key: 'index', title: '序号', width: '60px', minWidth: '60px', noWrap: true },
  { key: 'name', title: '工程名称', minWidth: '120px', noWrap: true },
  { key: 'code', title: '工程编码', minWidth: '120px', noWrap: true },
  { key: 'note', title: '备注', minWidth: '200px', noEllipsis: true },
  { key: 'responsiblePerson', title: '负责人', width: '80px', minWidth: '80px', noWrap: true },
  { key: 'phone', title: '负责人电话', width: '130px', minWidth: '130px', noWrap: true },
  { key: 'startTime', title: '开始维护时间', width: '160px', minWidth: '160px', noWrap: true },
  { key: 'overTime', title: '结束维护时间', width: '160px', minWidth: '160px', noWrap: true },
  { key: 'actions', title: '操作', width: '140px', minWidth: '140px', noWrap: true }
])
</script>
