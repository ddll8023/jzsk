<template>
  <!-- 预警指标设定页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">预警指标设定</h1>
      <p class="mt-1 text-sm text-gray-500">配置和管理各测点的预警阈值指标</p>
    </header>

    <!-- 筛选面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <div class="flex flex-col lg:flex-row lg:items-center gap-6 justify-between">
        <!-- 左侧：筛选条件 -->
        <div class="flex flex-col md:flex-row gap-6 md:items-center flex-1">
          <!-- 测点名称搜索 -->
          <div class="flex-1">
            <Input
              v-model="filters.position"
              label="测点名称"
              placeholder="请输入测点名称搜索"
              :disabled="loading"
              @keyup.enter="search"
            />
          </div>

          <!-- 监测项 -->
          <div class="w-full md:w-48">
            <Select
              v-model="filters.type"
              label="监测项"
              :options="[{ label: '全部', value: '' }, ...dictData.types]"
              :disabled="loading"
              @change="search"
            />
          </div>
        </div>

        <!-- 右侧：操作按钮 -->
        <div class="flex items-end gap-3 pt-4 lg:pt-0 border-t lg:border-t-0 border-gray-100">
          <Button type="primary" icon="search" class="w-full lg:w-auto" :disabled="loading" @click="search">搜索</Button>
          <Button icon="refresh" class="w-full lg:w-auto" :disabled="loading" @click="resetFilters">重置</Button>
          <Button type="primary" icon="plus" class="w-full lg:w-auto shadow-md shadow-primary-500/20" :disabled="loading" @click="handleAdd">新增</Button>
        </div>
      </div>
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-3">
          <!-- 视觉锚点装饰 -->
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          <h2 class="text-base font-bold text-gray-800">预警指标列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ pagination.total }} 条记录</span>
      </div>
      
      <div class="p-0">
        <Table
        :columns="tableColumns"
        :data="indicatorList"
        :loading="loading"
        :show-pagination="true"
        :total="pagination.total"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        @page-change="handlePageChange"
        @update:pageSize="handleSizeChange"
        @update:currentPage="handleCurrentPageUpdate"
      >
        <!-- 测点名称 -->
        <template #position="{ row }">
          <span class="text-gray-900 font-medium">{{ row.position }}</span>
        </template>

        <!-- 监测项 -->
        <template #type="{ row }">
          <span class="text-gray-700">{{ row.type }}</span>
        </template>

        <!-- 上上限 -->
        <template #upUpLimit="{ row }">
          <span class="text-red-600 font-medium">{{ row.upUpLimit }}</span>
        </template>

        <!-- 上限 -->
        <template #upLimit="{ row }">
          <span class="text-orange-600 font-medium">{{ row.upLimit }}</span>
        </template>

        <!-- 下限 -->
        <template #lowLimit="{ row }">
          <span class="text-blue-600 font-medium">{{ row.lowLimit }}</span>
        </template>

        <!-- 下下限 -->
        <template #lowerLimit="{ row }">
          <span class="text-purple-600 font-medium">{{ row.lowerLimit }}</span>
        </template>

        <!-- 单位 -->
        <template #unit="{ row }">
          <span class="text-gray-600">{{ row.unit }}</span>
        </template>

        <!-- 操作 -->
        <template #actions="{ row }">
          <div class="flex gap-2">
            <Button size="sm" @click="handleEdit(row)">编辑</Button>
            <Button size="sm" type="danger" @click="handleDelete(row)">删除</Button>
          </div>
        </template>
      </Table>
      </div>
    </Card>

    <!-- 新增/编辑弹窗 -->
    <Modal
      v-model="formModalVisible"
      :title="isEdit ? '编辑预警指标' : '新增预警指标'"
      width="lg"
    >
      <div class="py-4">
        <div class="grid grid-cols-2 gap-4">
          <!-- 测点名称 -->
          <div>
            <Select
              v-model="formData.position"
              label="测点名称"
              :options="[{ label: '请选择', value: '' }, ...pointOptions]"
              :required="true"
              @change="handlePointChange"
            />
          </div>

          <!-- 监测项 -->
          <div>
            <Select
              v-model="formData.type"
              label="监测项"
              :options="[{ label: '请选择', value: '' }, ...currentMonitorItems]"
              :required="true"
              :disabled="!formData.position"
            />
          </div>

          <!-- 上上限 -->
          <div>
            <Input
              v-model="formData.upUpLimit"
              type="number"
              label="上上限"
              placeholder="请输入上上限"
              :required="true"
            />
          </div>

          <!-- 上限 -->
          <div>
            <Input
              v-model="formData.upLimit"
              type="number"
              label="上限"
              placeholder="请输入上限"
              :required="true"
            />
          </div>

          <!-- 下限 -->
          <div>
            <Input
              v-model="formData.lowLimit"
              type="number"
              label="下限"
              placeholder="请输入下限"
              :required="true"
            />
          </div>

          <!-- 下下限 -->
          <div>
            <Input
              v-model="formData.lowerLimit"
              type="number"
              label="下下限"
              placeholder="请输入下下限"
              :required="true"
            />
          </div>

          <!-- 单位 -->
          <div class="col-span-2">
            <Input
              v-model="formData.unit"
              label="单位"
              placeholder="请输入单位"
              :required="true"
            />
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="formModalVisible = false">取消</Button>
        <Button type="primary" :loading="loading" @click="handleSubmit">确定</Button>
      </template>
    </Modal>

    <!-- 删除确认弹窗 -->
    <Modal
      v-model="deleteModalVisible"
      title="删除确认"
      width="sm"
    >
      <div class="py-4">
        <p class="text-gray-700">确认删除该预警指标吗？此操作不可恢复。</p>
      </div>
      <template #footer>
        <Button @click="deleteModalVisible = false">取消</Button>
        <Button type="danger" :loading="loading" @click="confirmDelete">确认删除</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 预警指标设定页面
 * 功能：预警指标列表、筛选、CRUD操作
 * 依赖组件：Table, Modal, Button, Select, Input
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, computed, onMounted } from 'vue'
import { useIndicator } from '@/composables/useIndicator'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'

// 使用 Composable
const {
  indicatorList,
  loading,
  pagination,
  filters,
  dictData,
  pointOptions,
  getMonitorItems,
  loadIndicatorOptions,
  loadIndicatorList,
  getDetail,
  save,
  update,
  remove,
  search,
  resetFilters,
  handlePageChange,
  handleSizeChange
} = useIndicator()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'position', title: '测点名称' },
  { key: 'type', title: '监测项' },
  { key: 'upUpLimit', title: '上上限', width: '100px' },
  { key: 'upLimit', title: '上限', width: '100px' },
  { key: 'lowLimit', title: '下限', width: '100px' },
  { key: 'lowerLimit', title: '下下限', width: '100px' },
  { key: 'unit', title: '单位', width: '80px' },
  { key: 'actions', title: '操作', width: '150px' }
]

const createEmptyFormData = () => ({
  id: null,
  position: '',
  type: '',
  upUpLimit: '',
  upLimit: '',
  lowLimit: '',
  lowerLimit: '',
  unit: ''
})

// 表单弹窗
const formModalVisible = ref(false)
const isEdit = ref(false)
const formData = ref(createEmptyFormData())

// 删除弹窗
const deleteModalVisible = ref(false)
const currentId = ref(null)

// 当前监测项列表
const currentMonitorItems = computed(() => {
  return getMonitorItems(formData.value.position)
})

/**
 * 处理分页条当前页码更新
 * 响应 Table 组件的 update:currentPage 事件，同步更新 Pagination 组件显示
 */
const handleCurrentPageUpdate = (page) => {
  pagination.currentPage = page
}

/**
 * 处理测点变化
 */
const handlePointChange = () => {
  formData.value.type = ''
}

/**
 * 处理新增
 */
const handleAdd = () => {
  isEdit.value = false
  formData.value = createEmptyFormData()
  formModalVisible.value = true
}

/**
 * 处理编辑
 */
const handleEdit = async (row) => {
  isEdit.value = true
  const detail = await getDetail(row.id)
  if (detail) {
    formData.value = {
      ...createEmptyFormData(),
      ...detail
    }
    formModalVisible.value = true
  }
}

/**
 * 处理删除
 */
const handleDelete = (row) => {
  currentId.value = row.id
  deleteModalVisible.value = true
}

/**
 * 确认删除
 */
const confirmDelete = async () => {
  const result = await remove(currentId.value)
  if (result.success) {
    showToast(result.message, 'success')
    deleteModalVisible.value = false
    currentId.value = null
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  const requiredFields = ['position', 'type', 'upUpLimit', 'upLimit', 'lowLimit', 'lowerLimit', 'unit']
  const hasEmptyField = requiredFields.some((field) => {
    const value = formData.value[field]
    if (value === null || value === undefined) {
      return true
    }
    if (typeof value === 'string') {
      return value.trim() === ''
    }
    return false
  })

  if (hasEmptyField) {
    showToast('请填写完整信息', 'warning')
    return
  }

  const result = isEdit.value 
    ? await update(formData.value)
    : await save(formData.value)

  if (result.success) {
    showToast(result.message, 'success')
    formModalVisible.value = false
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadIndicatorOptions()
  await loadIndicatorList()
})
</script>
