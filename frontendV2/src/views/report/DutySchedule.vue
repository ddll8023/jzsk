<template>
  <!-- 值班安排页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">值班安排管理</h1>
      <p class="mt-1 text-sm text-gray-500">安排和管理值班人员及时间</p>
    </header>

    <!-- 查询面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <DutyScheduleQueryPanel
        v-model:start-date="startDate"
        v-model:end-date="endDate"
        :has-selection="selectedRows.length > 0"
        @search="handleSearch"
        @reset="handleReset"
        @add="handleAdd"
        @batch-delete="handleBatchDelete"
      />
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <DutyScheduleTable
        :data="tableData"
        :loading="loading"
        :total="total"
        :current-page="query.current"
        :page-size="query.size"
        :format-date-time="formatDateTime"
        @page-change="onPageChange"
        @update:page-size="onSizeChange"
        @edit="handleEdit"
        @delete="handleDelete"
      />
    </Card>

    <!-- 新增/编辑弹窗 -->
    <DutyScheduleModal
      v-model="modalVisible"
      :data="currentRow"
      :loading="modalLoading"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
/**
 * 值班安排页面
 * 功能：值班安排的增删改查
 * 依赖组件：DutyScheduleQueryPanel, DutyScheduleTable, DutyScheduleModal
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, onMounted } from 'vue'
import { useDutySchedule } from '@/composables/useDutySchedule'
import Card from '@/components/basic/Card.vue'
import DutyScheduleQueryPanel from '@/components/business/report/DutyScheduleQueryPanel.vue'
import DutyScheduleTable from '@/components/business/report/DutyScheduleTable.vue'
import DutyScheduleModal from '@/components/business/report/DutyScheduleModal.vue'

// 使用Composable
const {
  loading,
  tableData,
  total,
  selectedRows,
  query,
  fetchData,
  saveData,
  deleteData,
  batchDelete,
  onPageChange,
  onSizeChange,
  onSearch,
  resetSearch,
  formatDateTime
} = useDutySchedule()

// 搜索条件
const startDate = ref('')
const endDate = ref('')

// 弹窗状态
const modalVisible = ref(false)
const modalLoading = ref(false)
const currentRow = ref(null)

/**
 * 处理搜索
 */
const handleSearch = () => {
  onSearch(startDate.value, endDate.value)
}

/**
 * 处理重置
 */
const handleReset = () => {
  startDate.value = ''
  endDate.value = ''
  resetSearch()
}

/**
 * 处理新增
 */
const handleAdd = () => {
  currentRow.value = null
  modalVisible.value = true
}

/**
 * 处理编辑
 * @param {Object} row - 行数据
 */
const handleEdit = (row) => {
  currentRow.value = { ...row }
  modalVisible.value = true
}

/**
 * 处理删除
 * @param {Object} row - 行数据
 */
const handleDelete = async (row) => {
  if (!confirm('确定删除该值班安排吗？')) return

  try {
    await deleteData(row.dutyScheduleId)
    showToast('删除成功', 'success')
  } catch (error) {
    showToast('删除失败', 'error')
  }
}

/**
 * 处理批量删除
 */
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    showToast('请选择要删除的数据', 'warning')
    return
  }

  if (!confirm(`确定删除选中的 ${selectedRows.value.length} 条数据吗？`)) return

  try {
    const ids = selectedRows.value.map(row => row.dutyScheduleId)
    await batchDelete(ids)
    showToast('批量删除成功', 'success')
    selectedRows.value = []
  } catch (error) {
    showToast('批量删除失败', 'error')
  }
}

/**
 * 处理表单提交
 * @param {Object} formData - 表单数据
 */
const handleSubmit = async (formData) => {
  modalLoading.value = true
  try {
    await saveData(formData)
    showToast('保存成功', 'success')
    modalVisible.value = false
  } catch (error) {
    showToast('保存失败', 'error')
  } finally {
    modalLoading.value = false
  }
}

/**
 * 显示提示信息
 * @param {String} message - 提示内容
 * @param {String} type - 类型 success/error/warning
 */
const showToast = (message, type = 'info') => {
  const bgColor = {
    success: 'bg-green-500',
    error: 'bg-red-500',
    warning: 'bg-yellow-500',
    info: 'bg-blue-500'
  }[type] || 'bg-gray-500'

  const toast = document.createElement('div')
  toast.className = `fixed bottom-4 right-4 ${bgColor} text-white px-4 py-2 rounded-lg shadow-lg z-50 transition-opacity duration-300`
  toast.textContent = message
  document.body.appendChild(toast)

  requestAnimationFrame(() => {
    toast.classList.add('opacity-100')
  })

  setTimeout(() => {
    toast.classList.remove('opacity-100')
    toast.classList.add('opacity-0')
    setTimeout(() => toast.remove(), 300)
  }, 3000)
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>
