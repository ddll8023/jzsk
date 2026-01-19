<template>
  <!-- 预警信息处理页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">预警信息处理</h1>
      <p class="mt-1 text-sm text-gray-500">查询和处理各类预警信息，及时解除已处理的预警</p>
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
            <!-- 开始时间 -->
            <div class="relative group">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10" style="top: 28px;">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <Input
                v-model="filters.dateRange[0]"
                type="datetime-local"
                label="开始时间"
                inputClass="pl-10"
                :disabled="loading"
                @change="search"
              />
            </div>

            <!-- 结束时间 -->
            <div class="relative group">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10" style="top: 28px;">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <Input
                v-model="filters.dateRange[1]"
                type="datetime-local"
                label="结束时间"
                inputClass="pl-10"
                :disabled="loading"
                @change="search"
              />
            </div>

            <!-- 预警地点 -->
            <div>
              <Select
                v-model="filters.position"
                label="预警地点"
                :options="[{ label: '全部', value: '' }, ...dictData.positions]"
                :disabled="loading"
                @change="search"
              />
            </div>

            <!-- 预警类型 -->
            <div>
              <Select
                v-model="filters.type"
                label="预警类型"
                :options="[{ label: '全部', value: '' }, ...dictData.types]"
                :disabled="loading"
                @change="search"
              />
            </div>

            <!-- 预警等级 -->
            <div>
              <Select
                v-model="filters.level"
                label="预警等级"
                :options="[{ label: '全部', value: '' }, ...dictData.levels]"
                :disabled="loading"
                @change="search"
              />
            </div>

            <!-- 预警状态 -->
            <div>
              <Select
                v-model="filters.status"
                label="预警状态"
                :options="[{ label: '全部', value: '' }, ...dictData.statuses]"
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
          <h2 class="text-base font-bold text-gray-800">预警信息列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ pagination.total }} 条记录</span>
      </div>
      
      <div class="p-0">
        <Table
        :columns="tableColumns"
        :data="warningList"
        :loading="loading"
        :show-pagination="true"
        :total="pagination.total"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        @page-change="handlePageChange"
        @update:pageSize="handleSizeChange"
      >
        <!-- 预警地点 -->
        <template #position="{ row }">
          <span class="text-gray-900">{{ row.position }}</span>
        </template>

        <!-- 预警类型 -->
        <template #type="{ row }">
          <span class="text-gray-700">{{ row.type }}</span>
        </template>

        <!-- 预警等级 -->
        <template #level="{ row }">
          <LevelBadge :level="row.level" />
        </template>

        <!-- 预警内容 -->
        <template #content="{ row }">
          <span class="text-gray-700">{{ row.content }}</span>
        </template>

        <!-- 预警状态 -->
        <template #status="{ row }">
          <StatusBadge :status="row.status" />
        </template>

        <!-- 所属工程 -->
        <template #project="{ row }">
          <span class="text-gray-700">{{ row.project }}</span>
        </template>

        <!-- 发生时间 -->
        <template #startTime="{ row }">
          <span class="text-gray-600 text-sm">{{ row.startTime }}</span>
        </template>

        <!-- 解除时间 -->
        <template #overTime="{ row }">
          <span class="text-gray-600 text-sm">{{ row.overTime || '-' }}</span>
        </template>

        <!-- 持续时长 -->
        <template #stayTime="{ row }">
          <span class="text-gray-600 text-sm">{{ row.stayTime || '-' }}</span>
        </template>

        <!-- 操作 -->
        <template #actions="{ row }">
          <Button
            v-if="row.status === '未解除'"
            type="primary"
            size="sm"
            @click="handleResolve(row)"
          >
            解除预警
          </Button>
          <span v-else class="text-gray-400 text-sm">已解除</span>
        </template>
        </Table>
      </div>
    </Card>

    <!-- 解除预警确认弹窗 -->
    <Modal
      v-model="resolveModalVisible"
      title="解除预警"
      width="md"
    >
      <div class="py-4">
        <p class="text-gray-700 mb-4">确认解除该预警信息吗？</p>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">预警地点：</span><span class="text-gray-900">{{ currentWarning?.position }}</span></div>
          <div><span class="text-gray-500">预警类型：</span><span class="text-gray-900">{{ currentWarning?.type }}</span></div>
          <div><span class="text-gray-500">预警等级：</span><LevelBadge v-if="currentWarning" :level="currentWarning.level" /></div>
          <div><span class="text-gray-500">预警内容：</span><span class="text-gray-900">{{ currentWarning?.content }}</span></div>
        </div>
      </div>
      <template #footer>
        <Button @click="resolveModalVisible = false">取消</Button>
        <Button type="primary" :loading="loading" @click="confirmResolve">确认解除</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 预警信息处理页面
 * 功能：预警信息列表、筛选、解除预警
 * 依赖组件：Table, Modal, Button, Select, Input, StatusBadge, LevelBadge
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, onMounted } from 'vue'
import { usePrewarning } from '@/composables/usePrewarning'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'
import StatusBadge from '@/components/business/warning/StatusBadge.vue'
import LevelBadge from '@/components/business/warning/LevelBadge.vue'

// 使用 Composable
const {
  warningList,
  loading,
  pagination,
  filters,
  dictData,
  loadWarningList,
  resolveWarning,
  loadDictData,
  search,
  resetFilters,
  handlePageChange,
  handleSizeChange
} = usePrewarning()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'position', title: '预警地点' },
  { key: 'type', title: '预警类型' },
  { key: 'level', title: '预警等级' },
  { key: 'content', title: '预警内容' },
  { key: 'status', title: '预警状态' },
  { key: 'project', title: '所属工程' },
  { key: 'startTime', title: '发生时间', width: '150px' },
  { key: 'overTime', title: '解除时间', width: '150px' },
  { key: 'stayTime', title: '持续时长' },
  { key: 'actions', title: '操作', width: '120px' }
]

// 解除预警弹窗
const resolveModalVisible = ref(false)
const currentWarning = ref(null)

/**
 * 处理解除预警
 */
const handleResolve = (warning) => {
  currentWarning.value = warning
  resolveModalVisible.value = true
}

/**
 * 确认解除预警
 */
const confirmResolve = async () => {
  const result = await resolveWarning(currentWarning.value)
  if (result.success) {
    showToast(result.message, 'success')
    resolveModalVisible.value = false
    currentWarning.value = null
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadDictData()
  await loadWarningList()
})
</script>
