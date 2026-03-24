<template>
  <!-- 测项信息管理页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">测项信息管理</h1>
      <p class="mt-1 text-sm text-gray-500">管理测项基础信息，包括测项编号、名称、单位等</p>
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
        <div class="flex-1">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <!-- 测项名称 -->
            <div>
              <Input
                v-model="query.name"
                label="测项名称"
                placeholder="输入测项名称搜索"
                :disabled="loading"
              />
            </div>
          </div>
        </div>

        <!-- 右侧：操作按钮 -->
        <div class="flex items-end gap-3 pt-4 lg:pt-0 border-t lg:border-t-0 border-gray-100">
          <Button type="primary" icon="search" class="w-full lg:w-auto shadow-md shadow-primary-500/20" :disabled="loading" @click="search">搜索</Button>
          <Button icon="refresh" class="w-full lg:w-auto" :disabled="loading" @click="resetFilters">重置</Button>
          <Button type="success" icon="plus" class="w-full lg:w-auto" :disabled="loading" @click="handleAdd">新增</Button>
          <Button icon="download" class="w-full lg:w-auto" :disabled="loading" @click="handleExport">导出</Button>
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <h2 class="text-base font-bold text-gray-800">测项信息列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ total }} 条记录</span>
      </div>
      
      <div class="p-0">
        <Table
          :columns="tableColumns"
          :data="itemList"
          :loading="loading"
          :show-pagination="true"
          :total="total"
          :current-page="query.currentPage"
          :page-size="query.pageSize"
          @page-change="handlePageChange"
          @update:pageSize="handleSizeChange"
        >
          <!-- 测项编号 -->
          <template #number="{ row }">
            <span class="text-gray-900 font-medium">{{ row.number }}</span>
          </template>

          <!-- 测项名称 -->
          <template #name="{ row }">
            <span class="text-gray-900">{{ row.name }}</span>
          </template>

          <!-- 测项单位 -->
          <template #unit="{ row }">
            <span class="text-gray-700">{{ row.unit || '-' }}</span>
          </template>

          <!-- 操作 -->
          <template #actions="{ row }">
            <div class="flex items-center gap-2">
              <Button
                type="primary"
                size="sm"
                @click="handleEdit(row)"
              >
                编辑
              </Button>
              <Button
                type="danger"
                size="sm"
                @click="handleDelete(row)"
              >
                删除
              </Button>
            </div>
          </template>
        </Table>
      </div>
    </Card>

    <!-- 新增/编辑弹窗 -->
    <Modal
      v-model="formModalVisible"
      :title="formData.id ? '编辑测项信息' : '新增测项信息'"
      width="lg"
    >
      <div class="py-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- 测项编号 -->
          <div>
            <Input
              v-model="formData.number"
              label="测项编号"
              placeholder="请输入测项编号"
              :required="true"
            />
          </div>

          <!-- 测项名称 -->
          <div>
            <Input
              v-model="formData.name"
              label="测项名称"
              placeholder="请输入测项名称"
              :required="true"
            />
          </div>

          <!-- 测项单位 -->
          <div class="md:col-span-2">
            <Input
              v-model="formData.unit"
              label="测项单位"
              placeholder="请输入测项单位"
            />
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="formModalVisible = false">取消</Button>
        <Button type="primary" :loading="loading" @click="confirmSave">确认保存</Button>
      </template>
    </Modal>

    <!-- 删除确认弹窗 -->
    <Modal
      v-model="deleteModalVisible"
      title="删除确认"
      width="md"
    >
      <div class="py-4">
        <p class="text-gray-700 mb-4">确认删除该测项信息吗？此操作不可恢复。</p>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">测项编号：</span><span class="text-gray-900">{{ currentItem?.number }}</span></div>
          <div><span class="text-gray-500">测项名称：</span><span class="text-gray-900">{{ currentItem?.name }}</span></div>
          <div><span class="text-gray-500">测项单位：</span><span class="text-gray-900">{{ currentItem?.unit || '-' }}</span></div>
        </div>
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
 * 测项信息管理页面
 * 功能：测项信息的增删改查、导出
 * 依赖组件：Table, Modal, Button, Select, Input, Card
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, onMounted } from 'vue'
import { useMonitorItem } from '@/composables/useMonitorItem'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'

// 使用 Composable
const {
  loading,
  itemList,
  total,
  query,
  formData,
  loadItemList,
  loadItemInfo,
  saveItem,
  deleteItem,
  search,
  resetFilters,
  handlePageChange,
  handleSizeChange,
  resetForm,
  exportData
} = useMonitorItem()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'number', title: '测项编号', width: '150px' },
  { key: 'name', title: '测项名称', width: '200px' },
  { key: 'unit', title: '测项单位', width: '150px' },
  { key: 'actions', title: '操作', width: '150px' }
]

// 弹窗状态
const formModalVisible = ref(false)
const deleteModalVisible = ref(false)
const currentItem = ref(null)

/**
 * 处理新增
 */
const handleAdd = () => {
  resetForm()
  formModalVisible.value = true
}

/**
 * 处理编辑
 */
const handleEdit = async (item) => {
  try {
    await loadItemInfo(item.id)
    formModalVisible.value = true
  } catch (error) {
    showToast('加载测项信息失败', 'error')
  }
}

/**
 * 处理删除
 */
const handleDelete = (item) => {
  currentItem.value = item
  deleteModalVisible.value = true
}

/**
 * 确认保存
 */
const confirmSave = async () => {
  // 简单验证
  if (!formData.number || !formData.name) {
    showToast('请填写必填项', 'error')
    return
  }

  const result = await saveItem(formData)
  if (result.success) {
    showToast(result.message, 'success')
    formModalVisible.value = false
    loadItemList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 确认删除
 */
const confirmDelete = async () => {
  const result = await deleteItem(currentItem.value.id)
  if (result.success) {
    showToast(result.message, 'success')
    deleteModalVisible.value = false
    currentItem.value = null
    loadItemList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 处理导出
 */
const handleExport = async () => {
  const result = await exportData()
  if (result.success) {
    showToast(result.message, 'success')
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadItemList()
})
</script>
