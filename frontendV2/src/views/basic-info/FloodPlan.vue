<template>
  <!-- 洪水防御预案页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">洪水防御预案</h1>
      <p class="mt-1 text-sm text-gray-500">管理洪水防御各阶段的应急预案和响应措施</p>
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
          <h2 class="text-base font-bold text-gray-800">预案管理</h2>
        </div>
        <div class="flex items-center gap-3">
          <Button type="success" icon="plus" :disabled="loading" @click="handleAdd">新增步骤</Button>
          <Button type="secondary" icon="refresh" :disabled="loading" @click="handleReset">重置为默认</Button>
        </div>
      </div>
    </Card>

    <!-- 数据表格 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden mb-6">
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
        <div class="flex items-center gap-3">
          <div class="h-6 w-1 rounded-full bg-primary-500"></div>
          <i class="fa fa-list-ol text-gray-400" aria-hidden="true"></i>
          <h2 class="text-base font-bold text-gray-800">预案步骤列表</h2>
        </div>
        <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">共 {{ planList.length }} 个步骤</span>
      </div>
      
      <div class="p-0">
        <Table
          :columns="tableColumns"
          :data="planList"
          :loading="loading"
          :show-pagination="false"
          row-key="id"
        >
          <!-- 步骤序号 -->
          <template #ordernum="{ row, index }">
            <div class="flex items-center justify-center">
              <span class="inline-flex items-center justify-center w-8 h-8 rounded-full bg-primary-100 text-primary-700 font-semibold text-sm">
                {{ index + 1 }}
              </span>
            </div>
          </template>

          <!-- 阶段/时间点 -->
          <template #time="{ row }">
            <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
              {{ row.time }}
            </span>
          </template>

          <!-- 具体内容 -->
          <template #content="{ row }">
            <p class="text-gray-700 text-left">{{ row.content }}</p>
          </template>

          <!-- 操作 -->
          <template #actions="{ row }">
            <div class="flex items-center justify-center gap-2">
              <Button
                type="primary"
                size="sm"
                icon="edit"
                @click="handleEdit(row)"
              >
                编辑
              </Button>
              <Button
                type="danger"
                size="sm"
                icon="trash"
                :disabled="planList.length <= 1"
                @click="handleDelete(row)"
              >
                删除
              </Button>
            </div>
          </template>
        </Table>
      </div>
    </Card>

    <!-- 预案预览 -->
    <Card variant="default" shadow="sm" rounded="xl" padding="md" class="overflow-hidden">
      <div class="flex items-center gap-3 mb-6">
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <i class="fa fa-eye text-gray-400" aria-hidden="true"></i>
        <h2 class="text-base font-bold text-gray-800">预案预览</h2>
      </div>
      
      <!-- 时间轴 -->
      <div class="relative pl-8">
        <div 
          v-for="(item, index) in planList" 
          :key="item.id"
          class="relative pb-8 last:pb-0"
        >
          <!-- 连接线 -->
          <div 
            v-if="index < planList.length - 1"
            class="absolute left-0 top-6 w-0.5 h-full bg-gray-200"
            style="margin-left: -1.75rem;"
          ></div>
          
          <!-- 圆点 -->
          <div 
            class="absolute left-0 top-1 w-4 h-4 rounded-full border-2 border-primary-500 bg-white"
            style="margin-left: -2rem;"
          ></div>
          
          <!-- 内容 -->
          <div class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
            <div class="flex items-center gap-2 mb-2">
              <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-primary-100 text-primary-800">
                {{ item.time }}
              </span>
              <span class="text-xs text-gray-400">步骤 {{ index + 1 }}</span>
            </div>
            <p class="text-sm text-gray-700">{{ item.content }}</p>
          </div>
        </div>
      </div>
    </Card>

    <!-- 新增/编辑弹窗 -->
    <Modal
      v-model="formModalVisible"
      :title="formData.id ? '编辑预案步骤' : '新增预案步骤'"
      width="lg"
    >
      <div class="py-4">
        <div class="space-y-4">
          <!-- 阶段/时间点 -->
          <div>
            <Input
              v-model="formData.time"
              label="阶段/时间点"
              placeholder="如：汛前、汛期、洪水发生时等"
              :required="true"
              maxlength="20"
            />
            <p class="mt-1 text-xs text-gray-500">建议不超过20个字符</p>
          </div>

          <!-- 具体内容 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              具体内容 <span class="text-red-500">*</span>
            </label>
            <textarea
              v-model="formData.content"
              rows="4"
              maxlength="200"
              placeholder="请输入该阶段的具体应急措施和响应内容"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
            ></textarea>
            <p class="mt-1 text-xs text-gray-500 text-right">{{ formData.content.length }}/200</p>
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
        <div class="flex items-start gap-3 mb-4">
          <div class="flex-shrink-0 w-10 h-10 rounded-full bg-red-100 flex items-center justify-center">
            <i class="fa fa-exclamation-triangle text-red-600" aria-hidden="true"></i>
          </div>
          <div>
            <p class="text-gray-900 font-medium mb-1">确认删除该预案步骤吗？</p>
            <p class="text-sm text-gray-500">此操作不可恢复</p>
          </div>
        </div>
        <div class="bg-gray-50 rounded-lg p-4 space-y-2 text-sm">
          <div><span class="text-gray-500">阶段：</span><span class="text-gray-900 font-medium">{{ currentItem?.time }}</span></div>
          <div><span class="text-gray-500">内容：</span><span class="text-gray-900">{{ currentItem?.content }}</span></div>
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
 * 洪水防御预案页面
 * 功能：洪水防御预案的增删改查、预览
 * 依赖组件：Table, Modal, Button, Input, Card
 * 遵循原则：KISS, YAGNI, SOLID
 * 支持本地存储降级方案
 */
import { ref, onMounted } from 'vue'
import { useFloodPlan } from '@/composables/useFloodPlan'
import { useToast } from '@/composables/useToast'
import Table from '@/components/basic/Table.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'
import Card from '@/components/basic/Card.vue'

// 使用 Composable
const {
  loading,
  planList,
  useLocalStorage,
  formData,
  loadPlanList,
  loadPlanInfo,
  savePlanItem,
  deletePlanItem,
  resetToDefault,
  resetForm
} = useFloodPlan()

// 使用 Toast
const { showToast } = useToast()

// 表格列配置
const tableColumns = [
  { key: 'ordernum', title: '步骤', width: '80px' },
  { key: 'time', title: '阶段/时间点', width: '150px' },
  { key: 'content', title: '具体内容' },
  { key: 'actions', title: '操作', width: '180px' }
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
  formData.ordernum = planList.value.length + 1
  formModalVisible.value = true
}

/**
 * 处理编辑
 */
const handleEdit = async (item) => {
  try {
    await loadPlanInfo(item.id)
    formModalVisible.value = true
  } catch (error) {
    showToast('加载预案信息失败', 'error')
  }
}

/**
 * 处理删除
 */
const handleDelete = (item) => {
  if (planList.value.length <= 1) {
    showToast('至少保留一个预案步骤', 'warning')
    return
  }
  currentItem.value = item
  deleteModalVisible.value = true
}

/**
 * 处理重置
 */
const handleReset = () => {
  if (confirm('确认重置为默认预案吗？当前数据将被覆盖。')) {
    const result = resetToDefault()
    showToast(result.message, result.success ? 'success' : 'error')
  }
}

/**
 * 确认保存
 */
const confirmSave = async () => {
  // 简单验证
  if (!formData.time || !formData.content) {
    showToast('请填写必填项', 'error')
    return
  }

  if (formData.time.length > 20) {
    showToast('阶段/时间点不能超过20个字符', 'error')
    return
  }

  if (formData.content.length > 200) {
    showToast('具体内容不能超过200个字符', 'error')
    return
  }

  const result = await savePlanItem(formData)
  if (result.success) {
    showToast(result.message, 'success')
    formModalVisible.value = false
    await loadPlanList()
  } else {
    showToast(result.message, 'error')
  }
}

/**
 * 确认删除
 */
const confirmDelete = async () => {
  const result = await deletePlanItem(currentItem.value.id)
  if (result.success) {
    showToast(result.message, 'success')
    deleteModalVisible.value = false
    currentItem.value = null
    await loadPlanList()
  } else {
    showToast(result.message, 'error')
  }
}

// 初始化
onMounted(async () => {
  await loadPlanList()
})
</script>
