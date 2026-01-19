<!-- 预警设施页面 -->
<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">预警设施</h1>
      <p class="mt-1 text-sm text-gray-500">管理预警设施的基本信息和维护记录</p>
    </header>

    <!-- 操作面板 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="sm"
      class="mb-6 transition-shadow duration-300 hover:shadow-md"
    >
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <h2 class="text-base font-bold text-gray-800">设施管理</h2>
        </div>
        <Button type="primary" icon="plus" :disabled="loading" @click="handleAdd">新增设施</Button>
      </div>
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-3">
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <i class="fa fa-list text-gray-400" aria-hidden="true"></i>
          <h2 class="text-base font-bold text-gray-800">设施列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ pagination.total }} 条记录</span>
      </div>
      
      <div class="p-0">
        <Table
          :columns="tableColumns"
          :data="facilityList"
          :loading="loading"
          :show-pagination="true"
          :total="pagination.total"
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          @page-change="handlePageChange"
          @update:pageSize="handleSizeChange"
        >
          <!-- 设施名称 -->
          <template #facilityName="{ row }">
            <span class="text-gray-900 font-medium">{{ row.facilityName }}</span>
          </template>

          <!-- 类型 -->
          <template #type="{ row }">
            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
              {{ row.type }}
            </span>
          </template>

          <!-- 位置 -->
          <template #location="{ row }">
            <span class="text-gray-700">{{ row.location }}</span>
          </template>

          <!-- 状态 -->
          <template #status="{ row }">
            <span 
              class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
              :class="getStatusClass(row.status)"
            >
              {{ row.status }}
            </span>
          </template>

          <!-- 负责人 -->
          <template #manager="{ row }">
            <span class="text-gray-700">{{ row.manager }}</span>
          </template>

          <!-- 最后维护时间 -->
          <template #lastUpdate="{ row }">
            <span class="text-gray-600 text-xs">{{ formatDateTime(row.lastUpdate) }}</span>
          </template>

          <!-- 建档时间 -->
          <template #recordTime="{ row }">
            <span class="text-gray-600 text-xs">{{ formatDateTime(row.recordTime) }}</span>
          </template>

          <!-- 操作 -->
          <template #actions="{ row }">
            <div class="flex items-center justify-center gap-2">
              <Button size="sm" type="primary" @click="handleEdit(row)">编辑</Button>
              <Button size="sm" type="danger" @click="handleDelete(row)">删除</Button>
            </div>
          </template>
        </Table>
      </div>
    </Card>

    <!-- 新增/编辑弹窗 -->
    <Modal
      v-model="formModalVisible"
      :title="formData.id ? '编辑预警设施' : '新增预警设施'"
      width="lg"
    >
      <div class="py-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- 设施名称 -->
          <div>
            <Input
              v-model="formData.facilityName"
              label="设施名称"
              placeholder="请输入设施名称"
              :required="true"
            />
          </div>

          <!-- 类型 -->
          <div>
            <Input
              v-model="formData.type"
              label="类型"
              placeholder="请输入设施类型"
              :required="true"
            />
          </div>

          <!-- 位置 -->
          <div>
            <Input
              v-model="formData.location"
              label="位置"
              placeholder="请输入设施位置"
              :required="true"
            />
          </div>

          <!-- 状态 -->
          <div>
            <Input
              v-model="formData.status"
              label="状态"
              placeholder="请输入设施状态"
              :required="true"
            />
          </div>

          <!-- 负责人 -->
          <div>
            <Input
              v-model="formData.manager"
              label="负责人"
              placeholder="请输入负责人姓名"
              :required="true"
            />
          </div>

          <!-- 最后维护时间 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              最后维护时间 <span class="text-red-500">*</span>
            </label>
            <input
              v-model="formData.lastUpdate"
              type="datetime-local"
              class="w-full px-4 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 bg-white transition-all duration-200"
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
      width="md"
    >
      <div class="py-4">
        <div class="flex items-start gap-3 mb-4">
          <div class="flex-shrink-0 w-10 h-10 rounded-full bg-red-100 flex items-center justify-center">
            <i class="fa fa-exclamation-triangle text-red-600" aria-hidden="true"></i>
          </div>
          <div>
            <p class="text-gray-900 font-medium mb-1">确认删除该预警设施吗？</p>
            <p class="text-sm text-gray-500">此操作不可恢复</p>
          </div>
        </div>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">设施名称：</span><span class="text-gray-900 font-medium">{{ currentItem?.facilityName }}</span></div>
          <div><span class="text-gray-500">类型：</span><span class="text-gray-900">{{ currentItem?.type }}</span></div>
          <div><span class="text-gray-500">位置：</span><span class="text-gray-900">{{ currentItem?.location }}</span></div>
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
 * 预警设施页面
 * 功能：预警设施的增删改查、列表展示
 * 依赖组件：Table, Modal, Button, Input, Card
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, onMounted } from 'vue'
import { useWarningFacility } from '@/composables/useWarningFacility'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'

// 使用 Composable
const {
  loading,
  facilityList,
  pagination,
  formData,
  loadFacilityList,
  loadFacilityInfo,
  saveFacility,
  deleteFacility,
  resetForm,
  handlePageChange,
  handleSizeChange,
  formatDateTime
} = useWarningFacility()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'facilityName', title: '设施名称' },
  { key: 'type', title: '类型', width: '120px' },
  { key: 'location', title: '位置' },
  { key: 'status', title: '状态', width: '100px' },
  { key: 'manager', title: '负责人', width: '120px' },
  { key: 'lastUpdate', title: '最后维护时间', width: '180px' },
  { key: 'recordTime', title: '建档时间', width: '180px' },
  { key: 'actions', title: '操作', width: '180px' }
]

// 弹窗状态
const formModalVisible = ref(false)
const deleteModalVisible = ref(false)
const currentItem = ref(null)

/**
 * 获取状态样式类名
 */
const getStatusClass = (status) => {
  const statusMap = {
    '正常': 'bg-green-100 text-green-800',
    '维护中': 'bg-yellow-100 text-yellow-800',
    '故障': 'bg-red-100 text-red-800',
    '停用': 'bg-gray-100 text-gray-800'
  }
  return statusMap[status] || 'bg-gray-100 text-gray-800'
}

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
    await loadFacilityInfo(item.id)
    formModalVisible.value = true
  } catch (error) {
    showToast('加载设施信息失败', 'error')
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
 * 确认删除
 */
const confirmDelete = async () => {
  const result = await deleteFacility(currentItem.value.id)
  if (result.success) {
    showToast(result.message, 'success')
    deleteModalVisible.value = false
    currentItem.value = null
    await loadFacilityList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  // 简单验证
  if (!formData.facilityName || !formData.type || !formData.location || 
      !formData.status || !formData.manager || !formData.lastUpdate) {
    showToast('请填写完整信息', 'warning')
    return
  }

  const result = await saveFacility(formData)
  if (result.success) {
    showToast(result.message, 'success')
    formModalVisible.value = false
    await loadFacilityList()
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadFacilityList()
})
</script>
