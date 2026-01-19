<template>
  <!-- 巡检记录页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">巡检记录管理</h1>
      <p class="mt-1 text-sm text-gray-500">记录和管理工程巡检信息</p>
    </header>

    <!-- 查询面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <InspectionQueryPanel
        v-model:project="filters.project"
        v-model:start-date="filters.startDate"
        v-model:end-date="filters.endDate"
        v-model:abnormal="filters.abnormal"
        v-model:solve="filters.solve"
        v-model:person="filters.person"
        :project-options="dictOptions.projectList"
        :abnormal-options="dictOptions.abnormalList"
        :solve-options="dictOptions.solveList"
        :person-options="dictOptions.personList"
        :loading="loading"
        @search="handleSearch"
        @reset="handleReset"
        @export="handleExport"
        @add="handleAdd"
      />
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <InspectionTable
        :data="tableData"
        :loading="loading"
        :total="total"
        :current-page="query.current"
        :page-size="query.size"
        @page-change="onPageChange"
        @update:page-size="onSizeChange"
        @view="handleView"
        @edit="handleEdit"
        @delete="handleDelete"
        @preview-image="handlePreviewImage"
      />
    </Card>

    <!-- 新增/编辑弹窗 -->
    <InspectionModal
      v-model="modalVisible"
      :data="currentRow"
      :loading="modalLoading"
      :project-options="dictOptions.projectList"
      :type-options="dictOptions.typeList"
      :solve-options="dictOptions.solveList"
      @submit="handleSubmit"
    />

    <!-- 图片预览弹窗 -->
    <Modal
      v-model="imagePreviewVisible"
      title="图片预览"
      width="xl"
    >
      <div class="flex items-center justify-center gap-4">
        <Button
          type="default"
          icon="angle-left"
          :disabled="currentImageIndex === 0"
          @click="previousImage"
        />
        <img
          v-if="currentImageUrl"
          :src="currentImageUrl"
          class="max-w-full max-h-[500px] object-contain"
        />
        <Button
          type="default"
          icon="angle-right"
          :disabled="currentImageIndex === previewImages.length - 1"
          @click="nextImage"
        />
      </div>
      <div class="text-center text-sm text-gray-500 mt-4">
        {{ currentImageIndex + 1 }} / {{ previewImages.length }}
      </div>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 巡检记录页面
 * 功能：巡检记录的增删改查
 * 依赖组件：InspectionQueryPanel, InspectionTable, InspectionModal
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive, onMounted, computed } from 'vue'
import { useInspection } from '@/composables/useInspection'
import { useDict } from '@/composables/useDict'
import Card from '@/components/basic/Card.vue'
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import InspectionQueryPanel from '@/components/business/inspection/InspectionQueryPanel.vue'
import InspectionTable from '@/components/business/inspection/InspectionTable.vue'
import InspectionModal from '@/components/business/inspection/InspectionModal.vue'
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
} = useInspection()

// 使用字典Composable
const { getDictOptions } = useDict()

// 筛选条件
const filters = reactive({
  project: '',
  startDate: '',
  endDate: '',
  abnormal: '',
  solve: '',
  person: ''
})

// 字典选项
const dictOptions = reactive({
  projectList: [],
  typeList: [],
  abnormalList: [],
  solveList: [],
  personList: []
})

// 弹窗状态
const modalVisible = ref(false)
const modalLoading = ref(false)
const currentRow = ref(null)

// 图片预览
const imagePreviewVisible = ref(false)
const previewImages = ref([])
const currentImageIndex = ref(0)
const currentImageUrl = computed(() => {
  if (previewImages.value.length === 0) return ''
  const baseUrl = 'http://111.4.68.108:8081/photo/'
  return `${baseUrl}${previewImages.value[currentImageIndex.value]}`
})

/**
 * 初始化字典数据
 */
const initDictData = async () => {
  try {
    dictOptions.projectList = await getDictOptions('巡检站点')
    dictOptions.typeList = await getDictOptions('巡检类型')
    dictOptions.abnormalList = await getDictOptions('异常情况')
    dictOptions.solveList = await getDictOptions('处理类型')
    dictOptions.personList = await getDictOptions('负责人')
  } catch (error) {
    console.error('加载字典数据失败:', error)
  }
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  // 格式化日期时间
  const startTime = filters.startDate ? filters.startDate.replace('T', ' ') + ':00' : ''
  const endTime = filters.endDate ? filters.endDate.replace('T', ' ') + ':00' : ''
  
  query.project = filters.project
  query.abnormal = filters.abnormal
  query.solve = filters.solve
  query.person = filters.person
  query.startTime = startTime
  query.endTime = endTime
  query.current = 1
  fetchData()
}

/**
 * 处理重置
 */
const handleReset = () => {
  filters.project = ''
  filters.startDate = ''
  filters.endDate = ''
  filters.abnormal = ''
  filters.solve = ''
  filters.person = ''
  query.project = ''
  query.abnormal = ''
  query.solve = ''
  query.person = ''
  query.startTime = ''
  query.endTime = ''
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
    XLSX.utils.book_append_sheet(wb, ws, '巡检记录')
    const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
    FileSaver.saveAs(
      new Blob([wbout], { type: 'application/octet-stream' }),
      '工程巡检记录.xlsx'
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
 * 处理查看
 */
const handleView = (row) => {
  currentRow.value = { ...row }
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
  if (!confirm('确定删除该巡检记录吗？')) return

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
 * 处理图片预览
 */
const handlePreviewImage = (imageStr, index) => {
  if (!imageStr) return
  previewImages.value = imageStr.split(';').filter(img => img.trim())
  currentImageIndex.value = index || 0
  imagePreviewVisible.value = true
}

/**
 * 上一张图片
 */
const previousImage = () => {
  if (currentImageIndex.value > 0) {
    currentImageIndex.value--
  }
}

/**
 * 下一张图片
 */
const nextImage = () => {
  if (currentImageIndex.value < previewImages.value.length - 1) {
    currentImageIndex.value++
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
    await initDictData()
    await fetchData(false) // 不在fetchData内部管理loading，由外部统一管理
    console.log('🔍 [PollingRecord] 表格数据:', tableData.value)
    console.log('🔍 [PollingRecord] 数据总数:', total.value)
  } catch (error) {
    console.error('初始化失败:', error)
    showToast('初始化失败', 'error')
  } finally {
    loading.value = false
  }
})
</script>
