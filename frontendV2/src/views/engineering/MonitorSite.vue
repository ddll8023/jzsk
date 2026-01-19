<template>
  <!-- 监测站点管理页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">监测站点管理</h1>
      <p class="mt-1 text-sm text-gray-500">管理监测站点基础信息，包括站码、站名、经纬度等</p>
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
            <!-- 站点名称 -->
            <div>
              <Select
                v-model="query.name"
                label="站点名称"
                :options="[{ label: '全部', value: '' }, ...siteNames]"
                :disabled="loading"
                @change="search"
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
          <h2 class="text-base font-bold text-gray-800">监测站点列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ total }} 条记录</span>
      </div>
      
      <div class="p-0">
        <Table
          :columns="tableColumns"
          :data="siteList"
          :loading="loading"
          :show-pagination="true"
          :total="total"
          :current-page="query.currentPage"
          :page-size="query.pageSize"
          @page-change="handlePageChange"
          @update:pageSize="handleSizeChange"
        >
          <!-- 站码 -->
          <template #code="{ row }">
            <span class="text-gray-900 font-medium">{{ row.code }}</span>
          </template>

          <!-- 站名 -->
          <template #name="{ row }">
            <span class="text-gray-900">{{ row.name }}</span>
          </template>

          <!-- 水系名称 -->
          <template #waterName="{ row }">
            <span class="text-gray-700">{{ row.waterName || '-' }}</span>
          </template>

          <!-- 河流名称 -->
          <template #riverName="{ row }">
            <span class="text-gray-700">{{ row.riverName || '-' }}</span>
          </template>

          <!-- 施测项目码 -->
          <template #monitorCode="{ row }">
            <span class="text-gray-700">{{ row.monitorCode || '-' }}</span>
          </template>

          <!-- 行政区划码 -->
          <template #addressCode="{ row }">
            <span class="text-gray-700">{{ row.addressCode || '-' }}</span>
          </template>

          <!-- 设站年月 -->
          <template #establishDate="{ row }">
            <span class="text-gray-600 text-sm">{{ row.establishDate || '-' }}</span>
          </template>

          <!-- 经度 -->
          <template #longitude="{ row }">
            <span class="text-gray-700">{{ row.longitude }}</span>
          </template>

          <!-- 纬度 -->
          <template #latitude="{ row }">
            <span class="text-gray-700">{{ row.latitude }}</span>
          </template>

          <!-- 备注 -->
          <template #note="{ row }">
            <span class="text-gray-600 text-sm">{{ row.note || '-' }}</span>
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
      :title="formData.id ? '编辑监测站点' : '新增监测站点'"
      width="lg"
    >
      <div class="py-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- 站码 -->
          <div>
            <Input
              v-model="formData.code"
              label="站码"
              placeholder="请输入8位站码"
              :required="true"
            />
          </div>

          <!-- 站名 -->
          <div>
            <Input
              v-model="formData.name"
              label="站名"
              placeholder="请输入站名"
              :required="true"
            />
          </div>

          <!-- 水系名称 -->
          <div>
            <Input
              v-model="formData.waterName"
              label="水系名称"
              placeholder="请输入水系名称"
            />
          </div>

          <!-- 河流名称 -->
          <div>
            <Input
              v-model="formData.riverName"
              label="河流名称"
              placeholder="请输入河流名称"
            />
          </div>

          <!-- 施测项目码 -->
          <div>
            <Input
              v-model="formData.monitorCode"
              label="施测项目码"
              placeholder="Z-水位/Q-流量/W-水质"
            />
          </div>

          <!-- 行政区划码 -->
          <div>
            <Input
              v-model="formData.addressCode"
              label="行政区划码"
              placeholder="请输入行政区划码"
            />
          </div>

          <!-- 设站年月 -->
          <div>
            <Input
              v-model="formData.establishDate"
              type="month"
              label="设站年月"
              placeholder="选择月份"
            />
          </div>

          <!-- 经度 -->
          <div>
            <Input
              v-model="formData.longitude"
              label="经度"
              placeholder="-180到180"
              :required="true"
            />
          </div>

          <!-- 纬度 -->
          <div>
            <Input
              v-model="formData.latitude"
              label="纬度"
              placeholder="-90到90"
              :required="true"
            />
          </div>

          <!-- 备注 -->
          <div class="md:col-span-2">
            <Input
              v-model="formData.note"
              label="备注"
              placeholder="请输入备注信息"
              type="textarea"
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
        <p class="text-gray-700 mb-4">确认删除该监测站点吗？此操作不可恢复。</p>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">站码：</span><span class="text-gray-900">{{ currentSite?.code }}</span></div>
          <div><span class="text-gray-500">站名：</span><span class="text-gray-900">{{ currentSite?.name }}</span></div>
          <div><span class="text-gray-500">经度：</span><span class="text-gray-900">{{ currentSite?.longitude }}</span></div>
          <div><span class="text-gray-500">纬度：</span><span class="text-gray-900">{{ currentSite?.latitude }}</span></div>
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
 * 监测站点管理页面
 * 功能：监测站点的增删改查、导出
 * 依赖组件：Table, Modal, Button, Select, Input, Card
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, onMounted } from 'vue'
import { useMonitorSite } from '@/composables/useMonitorSite'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'

// 使用 Composable
const {
  loading,
  siteList,
  siteNames,
  total,
  query,
  formData,
  loadSiteList,
  loadSiteNames,
  loadSiteInfo,
  saveSite,
  deleteSite,
  search,
  resetFilters,
  handlePageChange,
  handleSizeChange,
  resetForm,
  exportData
} = useMonitorSite()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'code', title: '站码', width: '100px' },
  { key: 'name', title: '站名', width: '120px' },
  { key: 'waterName', title: '水系名称', width: '120px' },
  { key: 'riverName', title: '河流名称', width: '120px' },
  { key: 'monitorCode', title: '施测项目码', width: '100px' },
  { key: 'addressCode', title: '行政区划码', width: '120px' },
  { key: 'establishDate', title: '设站年月', width: '100px' },
  { key: 'longitude', title: '经度', width: '100px' },
  { key: 'latitude', title: '纬度', width: '100px' },
  { key: 'note', title: '备注' },
  { key: 'actions', title: '操作', width: '150px' }
]

// 弹窗状态
const formModalVisible = ref(false)
const deleteModalVisible = ref(false)
const currentSite = ref(null)

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
const handleEdit = async (site) => {
  try {
    await loadSiteInfo(site.id)
    formModalVisible.value = true
  } catch (error) {
    showToast('加载站点信息失败', 'error')
  }
}

/**
 * 处理删除
 */
const handleDelete = (site) => {
  currentSite.value = site
  deleteModalVisible.value = true
}

/**
 * 确认保存
 */
const confirmSave = async () => {
  // 简单验证
  if (!formData.code || !formData.name || !formData.longitude || !formData.latitude) {
    showToast('请填写必填项', 'error')
    return
  }

  const result = await saveSite(formData)
  if (result.success) {
    showToast(result.message, 'success')
    formModalVisible.value = false
    loadSiteList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 确认删除
 */
const confirmDelete = async () => {
  const result = await deleteSite(currentSite.value.id)
  if (result.success) {
    showToast(result.message, 'success')
    deleteModalVisible.value = false
    currentSite.value = null
    loadSiteList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 处理导出
 */
const handleExport = () => {
  const result = exportData()
  if (result.success) {
    showToast(result.message, 'success')
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadSiteNames()
  await loadSiteList()
})
</script>
