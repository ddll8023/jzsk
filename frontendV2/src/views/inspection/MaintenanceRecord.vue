<template>
  <!-- 维护记录页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">维护记录管理</h1>
      <p class="mt-1 text-sm text-gray-500">记录和管理工程维护信息</p>
    </header>

    <!-- 查询面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <MaintenanceQueryPanel
        v-model:name="filters.name"
        v-model:start-date="filters.startDate"
        v-model:end-date="filters.endDate"
        :loading="loading"
        @search="handleSearch"
        @reset="handleReset"
        @export="handleExport"
        @add="handleAdd"
      />
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="w-full">
      <MaintenanceTable
        :data="tableData"
        :loading="loading"
        :total="total"
        :current-page="query.current"
        :page-size="query.size"
        @page-change="onPageChange"
        @update:page-size="onSizeChange"
        @edit="handleEdit"
        @delete="handleDelete"
      />
    </Card>

    <!-- 新增/编辑弹窗 -->
    <MaintenanceModal
      v-model="modalVisible"
      :data="currentRow"
      :loading="modalLoading"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
/**
 * 维护记录页面
 * 功能：维护记录的增删改查
 * 依赖组件：MaintenanceQueryPanel, MaintenanceTable, MaintenanceModal
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive, onMounted } from 'vue'
import { useMaintenance } from '@/composables/useMaintenance'
import Card from '@/components/basic/Card.vue'
import MaintenanceQueryPanel from '@/components/business/maintenance/MaintenanceQueryPanel.vue'
import MaintenanceTable from '@/components/business/maintenance/MaintenanceTable.vue'
import MaintenanceModal from '@/components/business/maintenance/MaintenanceModal.vue'
import FileSaver from 'file-saver'
import * as XLSX from 'xlsx'

// 使用Composable
const {
  loading,
  tableData,
  total,
  query,
  fetchData,
  saveData,
  deleteData,
  exportData,
  onPageChange,
  onSizeChange
} = useMaintenance()

// 筛选条件
const filters = reactive({
  name: '',
  startDate: '',
  endDate: ''
})

// 弹窗状态
const modalVisible = ref(false)
const modalLoading = ref(false)
const currentRow = ref(null)

/**
 * 处理搜索
 */
const handleSearch = () => {
  // 格式化日期时间
  const startTime = filters.startDate ? filters.startDate.replace('T', ' ') + ':00' : ''
  const endTime = filters.endDate ? filters.endDate.replace('T', ' ') + ':00' : ''
  
  query.name = filters.name
  query.startTime = startTime
  query.overTime = endTime
  query.current = 1
  fetchData()
}

/**
 * 处理重置
 */
const handleReset = () => {
  filters.name = ''
  filters.startDate = ''
  filters.endDate = ''
  query.name = ''
  query.startTime = ''
  query.overTime = ''
  query.current = 1
  fetchData()
}

/**
 * 处理导出
 */
const handleExport = async () => {
  try {
    const data = await exportData()
    if (!data || data.length === 0) {
      showToast('暂无数据可导出', 'warning')
      return
    }
    
    // 使用XLSX导出
    const ws = XLSX.utils.json_to_sheet(data)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, '维护记录')
    const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
    FileSaver.saveAs(
      new Blob([wbout], { type: 'application/octet-stream' }),
      '工程维护记录.xlsx'
    )
    showToast('导出成功', 'success')
  } catch (error) {
    showToast('导出失败', 'error')
  }
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
 */
const handleEdit = (row) => {
  currentRow.value = { ...row }
  modalVisible.value = true
}

/**
 * 处理删除
 */
const handleDelete = async (row) => {
  if (!confirm('确定删除该维护记录吗？')) return

  try {
    await deleteData(row.id)
    showToast('删除成功', 'success')
  } catch (error) {
    showToast('删除失败', 'error')
  }
}

/**
 * 处理表单提交
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

/**
 * 初始化
 * 设置loading状态，确保初始加载时显示动画
 */
onMounted(async () => {
  loading.value = true
  try {
    await fetchData(false) // 不在fetchData内部管理loading，由外部统一管理
  } catch (error) {
    console.error('初始化失败:', error)
    showToast('初始化失败', 'error')
  } finally {
    loading.value = false
  }
})
</script>
