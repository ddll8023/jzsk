<template>
  <Modal
    v-model="visible"
    :title="title"
    width="lg"
    @close="handleClose"
  >
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <div class="grid grid-cols-2 gap-4">
        <!-- 值班日期 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            值班日期 <span class="text-red-500">*</span>
          </label>
          <input
            type="date"
            v-model="formData.dutyDate"
            :disabled="loading"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>

        <!-- 天气 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            天气 <span class="text-red-500">*</span>
          </label>
          <Input
            v-model="formData.weather"
            placeholder="请输入天气"
            :disabled="loading"
          />
        </div>

        <!-- 雨量 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            雨量
          </label>
          <Input
            v-model="formData.rainfall"
            type="number"
            placeholder="请输入雨量"
            :disabled="loading"
          />
        </div>

        <!-- 带班领导 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            带班领导 <span class="text-red-500">*</span>
          </label>
          <Input
            v-model="formData.leader"
            placeholder="请输入带班领导"
            :disabled="loading"
          />
        </div>

        <!-- 白班值班人员 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            白班值班人员 <span class="text-red-500">*</span>
          </label>
          <Input
            v-model="formData.dayShiftPerson"
            placeholder="请输入白班值班人员"
            :disabled="loading"
          />
        </div>

        <!-- 晚班值班人员 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            晚班值班人员 <span class="text-red-500">*</span>
          </label>
          <Input
            v-model="formData.nightShiftPerson"
            placeholder="请输入晚班值班人员"
            :disabled="loading"
          />
        </div>

        <!-- 日志状态 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            日志状态
          </label>
          <Input
            v-model="formData.logStatus"
            placeholder="请输入日志状态"
            :disabled="loading"
          />
        </div>
      </div>

      <!-- 日志内容 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          日志内容 <span class="text-red-500">*</span>
        </label>
        <textarea
          v-model="formData.logContent"
          rows="4"
          placeholder="请输入日志内容"
          :disabled="loading"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
        ></textarea>
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
 * 值班日志弹窗组件
 * 功能：新增/编辑值班日志
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
  dutyDate: '',
  weather: '',
  rainfall: '',
  leader: '',
  dayShiftPerson: '',
  nightShiftPerson: '',
  logContent: '',
  logStatus: ''
})

// 弹窗标题
const title = computed(() => {
  return props.data?.dutyLogId ? '编辑值班日志' : '新增值班日志'
})

/**
 * 格式化日期为date格式
 * @param {*} value - 日期值
 * @returns {String}
 */
const formatToDate = (value) => {
  if (!value) return ''
  
  let d
  if (Array.isArray(value)) {
    const [y, M, D] = value
    d = new Date(y, M - 1, D)
  } else if (typeof value === 'string') {
    d = new Date(value)
  } else {
    return ''
  }

  if (isNaN(d.getTime())) return ''

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}

/**
 * 重置表单
 */
const resetForm = () => {
  formData.value = {
    dutyDate: '',
    weather: '',
    rainfall: '',
    leader: '',
    dayShiftPerson: '',
    nightShiftPerson: '',
    logContent: '',
    logStatus: ''
  }
}

/**
 * 监听数据变化，初始化表单
 */
watch(() => props.data, (newData) => {
  if (newData) {
    formData.value = {
      dutyLogId: newData.dutyLogId,
      dutyDate: formatToDate(newData.dutyDate) || '',
      weather: newData.weather || '',
      rainfall: newData.rainfall ?? '',
      leader: newData.leader || '',
      dayShiftPerson: newData.dayShiftPerson || '',
      nightShiftPerson: newData.nightShiftPerson || '',
      logContent: newData.logContent || '',
      logStatus: newData.logStatus || ''
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
  if (!formData.value.dutyDate) {
    alert('请选择值班日期')
    return false
  }
  if (!formData.value.weather?.trim()) {
    alert('请输入天气')
    return false
  }
  if (!formData.value.leader?.trim()) {
    alert('请输入带班领导')
    return false
  }
  if (!formData.value.dayShiftPerson?.trim()) {
    alert('请输入白班值班人员')
    return false
  }
  if (!formData.value.nightShiftPerson?.trim()) {
    alert('请输入晚班值班人员')
    return false
  }
  if (!formData.value.logContent?.trim()) {
    alert('请输入日志内容')
    return false
  }
  return true
}

/**
 * 处理提交
 */
const handleSubmit = () => {
  if (!validateForm()) return
  emit('submit', formData.value)
}

/**
 * 处理关闭
 */
const handleClose = () => {
  visible.value = false
  resetForm()
}
</script>
