<template>
  <Modal
    v-model="visible"
    :title="title"
    width="md"
    @close="handleClose"
  >
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- 值班人员 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          值班人员 <span class="text-red-500">*</span>
        </label>
        <Input
          v-model="formData.值班人员"
          placeholder="请输入值班人员"
          :disabled="loading"
        />
      </div>

      <!-- 带班领导 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          带班领导 <span class="text-red-500">*</span>
        </label>
        <Input
          v-model="formData.带班领导"
          placeholder="请输入带班领导"
          :disabled="loading"
        />
      </div>

      <!-- 值班时间 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          值班时间 <span class="text-red-500">*</span>
        </label>
        <input
          type="datetime-local"
          v-model="formData.值班时间"
          :disabled="loading"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
        />
      </div>

      <!-- 值班岗位 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          值班岗位 <span class="text-red-500">*</span>
        </label>
        <Input
          v-model="formData.值班岗位"
          placeholder="请输入值班岗位"
          :disabled="loading"
        />
      </div>
    </form>

    <template #footer>
      <div class="flex justify-end gap-3">
        <Button @click="handleClose" :disabled="loading">
          取消
        </Button>
        <Button type="primary" :loading="loading" @click="handleSubmit">
          保存
        </Button>
      </div>
    </template>
  </Modal>
</template>

<script setup>
/**
 * 值班安排弹窗组件
 * 功能：新增/编辑值班安排
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, watch, computed } from 'vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Button from '@/components/basic/Button.vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  data: {
    type: Object,
    default: null
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

// 弹窗显示状态
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 表单数据
const formData = ref({
  值班人员: '',
  带班领导: '',
  值班时间: '',
  值班岗位: ''
})

// 弹窗标题
const title = computed(() => {
  return props.data?.值班安排id ? '编辑值班安排' : '新增值班安排'
})

/**
 * 格式化日期时间为datetime-local格式
 * @param {*} value - 日期时间值
 * @returns {String}
 */
const formatToDatetimeLocal = (value) => {
  if (!value) return ''
  
  let d
  if (Array.isArray(value)) {
    const [y, M, D, h = 0, m = 0] = value
    d = new Date(y, M - 1, D, h, m)
  } else if (typeof value === 'string') {
    d = new Date(value)
  } else {
    return ''
  }

  if (isNaN(d.getTime())) return ''

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')

  return `${year}-${month}-${day}T${hours}:${minutes}`
}

/**
 * 重置表单
 */
const resetForm = () => {
  formData.value = {
    值班人员: '',
    带班领导: '',
    值班时间: '',
    值班岗位: ''
  }
}

/**
 * 监听数据变化，初始化表单
 */
watch(() => props.data, (newData) => {
  if (newData) {
    formData.value = {
      值班安排id: newData.值班安排id,
      值班人员: newData.值班人员 || '',
      带班领导: newData.带班领导 || '',
      值班时间: formatToDatetimeLocal(newData.值班时间) || '',
      值班岗位: newData.值班岗位 || ''
    }
  } else {
    resetForm()
  }
}, { immediate: true })

/**
 * 表单验证
 * @returns {Boolean}
 */
const validateForm = () => {
  if (!formData.value.值班人员?.trim()) {
    alert('请输入值班人员')
    return false
  }
  if (!formData.value.带班领导?.trim()) {
    alert('请输入带班领导')
    return false
  }
  if (!formData.value.值班时间) {
    alert('请选择值班时间')
    return false
  }
  if (!formData.value.值班岗位?.trim()) {
    alert('请输入值班岗位')
    return false
  }
  return true
}

/**
 * 处理提交
 */
const handleSubmit = () => {
  if (!validateForm()) return
  
  // 转换datetime-local格式为标准格式
  const submitData = {
    ...formData.value,
    值班时间: formData.value.值班时间.replace('T', ' ') + ':00'
  }
  
  emit('submit', submitData)
}

/**
 * 处理关闭
 */
const handleClose = () => {
  visible.value = false
  resetForm()
}
</script>
