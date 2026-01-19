<template>
  <Modal
    :model-value="modelValue"
    :title="isEdit ? '编辑维护记录' : '新增维护记录'"
    width="lg"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- 第一行：工程名称、工程编码 -->
      <div class="grid grid-cols-2 gap-4">
        <Input
          v-model="formData.name"
          label="工程名称"
          placeholder="请输入工程名称"
          required
          :error="errors.name"
        />
        <Input
          v-model="formData.code"
          label="工程编码"
          placeholder="请输入工程编码"
        />
      </div>

      <!-- 第二行：备注 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          备注
        </label>
        <textarea
          v-model="formData.note"
          rows="3"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
          placeholder="请输入备注信息"
        ></textarea>
      </div>

      <!-- 第三行：负责人、负责人电话 -->
      <div class="grid grid-cols-2 gap-4">
        <Input
          v-model="formData.responsiblePerson"
          label="负责人"
          placeholder="请输入负责人"
        />
        <Input
          v-model="formData.phone"
          label="负责人电话"
          placeholder="请输入负责人电话"
        />
      </div>

      <!-- 第四行：开始维护时间、结束维护时间 -->
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            开始维护时间 <span class="text-red-500">*</span>
          </label>
          <input
            v-model="formData.startTime"
            type="datetime-local"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            required
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            结束维护时间
          </label>
          <input
            v-model="formData.overTime"
            type="datetime-local"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>
      </div>
    </form>

    <template #footer>
      <div class="flex items-center justify-end gap-3">
        <Button type="default" @click="$emit('update:modelValue', false)">
          取消
        </Button>
        <Button type="primary" :loading="loading" @click="handleSubmit">
          确定
        </Button>
      </div>
    </template>
  </Modal>
</template>

<script setup>
/**
 * 维护记录弹窗
 * 功能：新增/编辑维护记录表单
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, computed, watch } from 'vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  data: { type: Object, default: null },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue', 'submit'])

// 表单数据
const formData = ref({
  id: null,
  name: '',
  code: '',
  note: '',
  responsiblePerson: '',
  phone: '',
  startTime: '',
  overTime: ''
})

// 错误信息
const errors = ref({})

// 是否编辑模式
const isEdit = computed(() => !!props.data?.id)

/**
 * 格式化日期时间为input[type="datetime-local"]格式
 */
const formatDateTimeForInput = (dateStr) => {
  if (!dateStr) return ''
  // 将 "2023-02-24 10:43:17" 转换为 "2023-02-24T10:43"
  return dateStr.substring(0, 16).replace(' ', 'T')
}

/**
 * 获取当前日期时间
 */
const getCurrentDateTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day}T${hours}:${minutes}`
}

/**
 * 重置表单
 */
const resetForm = () => {
  formData.value = {
    id: null,
    name: '',
    code: '',
    note: '',
    responsiblePerson: '',
    phone: '',
    startTime: getCurrentDateTime(),
    overTime: ''
  }
  errors.value = {}
}

/**
 * 表单验证
 */
const validate = () => {
  errors.value = {}
  
  if (!formData.value.name) {
    errors.value.name = '请输入工程名称'
  }
  
  return Object.keys(errors.value).length === 0
}

/**
 * 提交表单
 */
const handleSubmit = () => {
  if (!validate()) return
  
  // 格式化日期时间
  const submitData = { ...formData.value }
  if (submitData.startTime) {
    submitData.startTime = submitData.startTime.replace('T', ' ') + ':00'
  }
  if (submitData.overTime) {
    submitData.overTime = submitData.overTime.replace('T', ' ') + ':00'
  }
  
  emit('submit', submitData)
}

/**
 * 监听数据变化，初始化表单
 */
watch(() => props.data, (newData) => {
  if (newData) {
    formData.value = { ...newData }
    // 格式化日期时间为datetime-local格式
    if (formData.value.startTime) {
      formData.value.startTime = formatDateTimeForInput(formData.value.startTime)
    }
    if (formData.value.overTime) {
      formData.value.overTime = formatDateTimeForInput(formData.value.overTime)
    }
  } else {
    resetForm()
  }
}, { immediate: true })
</script>
