<template>
  <Modal
    v-model="isVisible"
    title="预警信息管理"
    width="full"
    @close="handleClose"
  >
    <div class="min-h-[600px]">
      <!-- 筛选区域 -->
      <Card variant="default" padding="md" shadow="sm" class="mb-4">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- 预警地点 -->
          <div>
            <Select
              v-model="filters.position"
              label="预警地点"
              :options="[{ label: '全部', value: '' }, ...dictData.positions]"
              :disabled="modalLoading"
            />
          </div>

          <!-- 预警类型 -->
          <div>
            <Select
              v-model="filters.type"
              label="预警类型"
              :options="[{ label: '全部', value: '' }, ...dictData.types]"
              :disabled="modalLoading"
            />
          </div>

          <!-- 预警等级 -->
          <div>
            <Select
              v-model="filters.level"
              label="预警等级"
              :options="[{ label: '全部', value: '' }, ...dictData.levels]"
              :disabled="modalLoading"
            />
          </div>

          <!-- 预警状态 -->
          <div>
            <Select
              v-model="filters.status"
              label="预警状态"
              :options="[{ label: '全部', value: '' }, ...dictData.statuses]"
              :disabled="modalLoading"
            />
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex gap-2 mt-4">
          <Button type="primary" @click="handleSearch" :disabled="modalLoading">
            <i class="fa fa-search mr-2"></i>查询
          </Button>
          <Button type="secondary" @click="resetFilters" :disabled="modalLoading">
            <i class="fa fa-redo mr-2"></i>重置
          </Button>
        </div>
      </Card>

      <!-- 预警列表 -->
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
            :loading="modalLoading"
            :show-pagination="true"
            :current-page="pagination.currentPage"
            :page-size="pagination.pageSize"
            :total="pagination.total"
            @update:currentPage="handlePageChange"
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

          <!-- 预警内容（添加文本截断和tooltip） -->
          <template #content="{ row }">
            <span class="truncate-text" :title="row.content">{{ row.content }}</span>
          </template>

          <!-- 预警状态 -->
          <template #status="{ row }">
            <StatusBadge :status="row.status" />
          </template>

          <!-- 所属工程（添加文本截断和tooltip） -->
          <template #project="{ row }">
            <span class="truncate-text" :title="row.project">{{ row.project }}</span>
          </template>

          <!-- 操作列 -->
          <template #actions="{ row }">
            <Button
              v-if="row.status === '未解除'"
              type="danger"
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
    </div>

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
        <div class="flex justify-end gap-2">
          <Button type="secondary" @click="resolveModalVisible = false">取消</Button>
          <Button type="danger" @click="confirmResolve">确认解除</Button>
        </div>
      </template>
    </Modal>
  </Modal>
</template>

<script setup>
/**
 * 预警信息弹窗
 * 功能：在地图页面以弹窗形式展示预警信息列表
 * 依赖：Modal、Table、Button、Select、LevelBadge、StatusBadge、usePrewarning
 * 遵循原则：KISS, YAGNI, SOLID-SRP
 * Source: 基于 PrewarningInformation.vue 改造
 */
import { ref, watch, onMounted } from 'vue'
import { usePrewarning } from '@/composables/usePrewarning'
import { useToast } from '@/composables/useToast'
import Modal from '@/components/basic/Modal.vue'
import Table from '@/components/basic/Table.vue'
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'
import Card from '@/components/basic/Card.vue'
import StatusBadge from '@/components/business/warning/StatusBadge.vue'
import LevelBadge from '@/components/business/warning/LevelBadge.vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 内部可见性状态
const isVisible = ref(props.modelValue)

// 独立的加载状态（不使用composable的loading，避免状态共享冲突）
const modalLoading = ref(false)

// 监听外部变化
watch(() => props.modelValue, async (newVal) => {
  isVisible.value = newVal
  if (newVal) {
    // 弹窗打开时加载数据
    modalLoading.value = true
    try {
      await loadWarningList()
      if (dictData.positions.length === 0) {
        await loadDictData()
      }
    } finally {
      modalLoading.value = false
    }
  }
})

// 监听内部变化
watch(isVisible, (newVal) => {
  emit('update:modelValue', newVal)
})

// 使用 Composable
const {
  warningList,
  loading,  // 保留但不使用，避免解构错误
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

// 表格列配置（优化列宽和文本截断）
const tableColumns = [
  { key: 'position', title: '预警地点', width: '100px' },
  { key: 'type', title: '预警类型', width: '80px' },
  { key: 'level', title: '预警等级', width: '100px' },
  { key: 'content', title: '预警内容', width: '220px' },
  { key: 'status', title: '预警状态', width: '100px' },
  { key: 'project', title: '所属工程', width: '150px' },
  { key: 'startTime', title: '发生时间', width: '150px' },
  { key: 'actions', title: '操作', width: '120px' }
]

// 解除预警弹窗
const resolveModalVisible = ref(false)
const currentWarning = ref(null)

/**
 * 搜索（包装search方法，添加loading控制）
 */
const handleSearch = async () => {
  modalLoading.value = true
  try {
    await search()
  } finally {
    modalLoading.value = false
  }
}

/**
 * 打开解除预警弹窗
 */
const handleResolve = (warning) => {
  currentWarning.value = warning
  resolveModalVisible.value = true
}

/**
 * 确认解除预警
 */
const confirmResolve = async () => {
  modalLoading.value = true
  try {
    const result = await resolveWarning(currentWarning.value)
    
    if (result.success) {
      showToast(result.message, 'success')
      resolveModalVisible.value = false
      currentWarning.value = null
      // 重新加载列表
      await loadWarningList()
    } else {
      showToast(result.message, 'error')
    }
  } finally {
    modalLoading.value = false
  }
}

/**
 * 关闭弹窗
 */
const handleClose = () => {
  isVisible.value = false
}

// 组件挂载时加载字典数据
onMounted(async () => {
  await loadDictData()
})
</script>

<style scoped>
/**
 * 文本截断样式
 * 功能：长文本显示省略号，鼠标悬浮显示完整内容
 */
.truncate-text {
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}
</style>
