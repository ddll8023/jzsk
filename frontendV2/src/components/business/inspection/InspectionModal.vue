<template>
  <Modal
    :model-value="modelValue"
    :title="isEdit ? '编辑巡检记录' : '新增巡检记录'"
    width="lg"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- 第一行：巡检站点、巡检类型 -->
      <div class="grid grid-cols-2 gap-4">
        <Select
          v-model="formData.project"
          :options="projectOptions"
          label="巡检站点"
          placeholder="请选择"
          required
          :error="errors.project"
        />
        <Select
          v-model="formData.type"
          :options="typeOptions"
          label="巡检类型"
          placeholder="请选择"
          required
          :error="errors.type"
        />
      </div>

      <!-- 第二行：经度、纬度 -->
      <div class="grid grid-cols-2 gap-4">
        <Input
          v-model="formData.longitude"
          label="经度"
          placeholder="请输入经度"
          required
          :error="errors.longitude"
        />
        <Input
          v-model="formData.latitude"
          label="纬度"
          placeholder="请输入纬度"
          required
          :error="errors.latitude"
        />
      </div>

      <!-- 第三行：异常情况、处理状态 -->
      <div class="grid grid-cols-2 gap-4">
        <Input
          v-model="formData.abnormal"
          label="异常情况"
          placeholder="请输入异常情况"
        />
        <Select
          v-model="formData.solve"
          :options="solveOptions"
          label="处理状态"
          placeholder="请选择"
          :disabled="!isEdit"
        />
      </div>

      <!-- 第四行：巡检情况 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          巡检情况
        </label>
        <textarea
          v-model="formData.situation"
          rows="4"
          maxlength="200"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
          placeholder="请输入巡检情况（最多200字）"
        ></textarea>
        <div class="text-xs text-gray-500 text-right mt-1">
          {{ formData.situation?.length || 0 }}/200
        </div>
      </div>

      <!-- 第五行：负责人、日期 -->
      <div class="grid grid-cols-2 gap-4">
        <Input
          v-model="formData.person"
          label="负责人"
          placeholder="请输入负责人"
          required
          :error="errors.person"
        />
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            日期 <span class="text-red-500">*</span>
          </label>
          <input
            v-model="formData.date"
            type="datetime-local"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            required
          />
        </div>
      </div>

      <!-- 第六行：图片上传 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">
          上传图片
        </label>
        <div class="flex flex-wrap gap-2">
          <!-- 已上传图片预览 -->
          <div
            v-for="(img, index) in imageList"
            :key="index"
            class="relative w-24 h-24 border border-gray-200 rounded-lg overflow-hidden group"
          >
            <img
              :src="getImageUrl(img)"
              class="w-full h-full object-cover"
            />
            <button
              type="button"
              class="absolute top-1 right-1 w-6 h-6 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center"
              @click="removeImage(index)"
            >
              <i class="fa fa-times" aria-hidden="true"></i>
            </button>
          </div>

          <!-- 上传按钮 -->
          <label
            v-if="imageList.length < 6"
            class="w-24 h-24 border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center cursor-pointer hover:border-primary-500 transition-colors"
          >
            <input
              type="file"
              accept="image/*"
              multiple
              class="hidden"
              @change="handleImageUpload"
            />
            <i class="fa fa-plus text-2xl text-gray-400" aria-hidden="true"></i>
          </label>
        </div>
        <p class="text-xs text-gray-500 mt-2">支持jpg/png/gif格式，最多上传6张</p>
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
 * 巡检记录弹窗
 * 功能：新增/编辑巡检记录表单
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, computed, watch } from 'vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'
import Select from '@/components/basic/Select.vue'
import { uploadInspectionImage } from '@/api/inspection'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  data: { type: Object, default: null },
  loading: { type: Boolean, default: false },
  projectOptions: { type: Array, default: () => [] },
  typeOptions: { type: Array, default: () => [] },
  solveOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'submit'])

// 表单数据
const formData = ref({
  id: null,
  project: '',
  longitude: '',
  latitude: '',
  type: '',
  abnormal: '',
  situation: '',
  solve: '未处理',
  person: '',
  date: '',
  image: ''
})

// 图片列表
const imageList = ref([])

// 错误信息
const errors = ref({})

// 是否编辑模式
const isEdit = computed(() => !!props.data?.id)

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
    project: '',
    longitude: '',
    latitude: '',
    type: '',
    abnormal: '',
    situation: '',
    solve: '未处理',
    person: '',
    date: getCurrentDateTime(),
    image: ''
  }
  imageList.value = []
  errors.value = {}
}

/**
 * 表单验证
 */
const validate = () => {
  errors.value = {}
  
  if (!formData.value.project) {
    errors.value.project = '请选择巡检站点'
  }
  if (!formData.value.type) {
    errors.value.type = '请选择巡检类型'
  }
  if (!formData.value.longitude) {
    errors.value.longitude = '请输入经度'
  } else if (!/^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/.test(formData.value.longitude)) {
    errors.value.longitude = '请输入正确的经度'
  }
  if (!formData.value.latitude) {
    errors.value.latitude = '请输入纬度'
  } else if (!/^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/.test(formData.value.latitude)) {
    errors.value.latitude = '请输入正确的纬度'
  }
  if (!formData.value.person) {
    errors.value.person = '请输入负责人'
  }
  
  return Object.keys(errors.value).length === 0
}

/**
 * 处理图片上传
 */
const handleImageUpload = async (event) => {
  const files = Array.from(event.target.files)
  
  for (const file of files) {
    // 验证文件类型
    const acceptList = ['jpg', 'jpeg', 'png', 'gif', 'bmp']
    const fileType = file.name.split('.').pop().toLowerCase()
    if (!acceptList.includes(fileType)) {
      alert('只能上传 jpg/jpeg/png/gif/bmp 格式的图片文件！')
      continue
    }
    
    // 上传图片
    try {
      const formData = new FormData()
      formData.append('image', file)
      const res = await uploadInspectionImage(formData)
      if (res.code === 200) {
        imageList.value.push(res.data)
      }
    } catch (error) {
      console.error('图片上传失败:', error)
      alert('图片上传失败，请重试！')
    }
  }
  
  // 清空input
  event.target.value = ''
}

/**
 * 删除图片
 */
const removeImage = (index) => {
  imageList.value.splice(index, 1)
}

/**
 * 获取图片URL
 */
const getImageUrl = (img) => {
  const baseUrl = 'http://111.4.68.108:8081/photo/'
  return `${baseUrl}${img}`
}

/**
 * 提交表单
 */
const handleSubmit = () => {
  if (!validate()) return
  
  // 更新图片字段
  formData.value.image = imageList.value.join(';')
  
  // 格式化日期
  if (formData.value.date) {
    formData.value.date = formData.value.date.replace('T', ' ') + ':00'
  }
  
  emit('submit', { ...formData.value })
}

/**
 * 监听数据变化，初始化表单
 */
watch(() => props.data, (newData) => {
  if (newData) {
    formData.value = { ...newData }
    imageList.value = newData.image ? newData.image.split(';').filter(img => img.trim()) : []
  } else {
    resetForm()
  }
}, { immediate: true })
</script>
