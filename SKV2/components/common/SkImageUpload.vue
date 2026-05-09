<template>
  <view class="w-full">
    <text v-if="label" class="text-sm font-medium text-gray-700 mb-2 block">
      {{ label }}
    </text>

    <view class="flex flex-wrap gap-2">
      <!-- 已上传图片 -->
      <view
        v-for="(img, index) in modelValue"
        :key="index"
        class="relative w-20 h-20 rounded-lg overflow-hidden bg-gray-100"
      >
        <image
          :src="getFullUrl(img)"
          mode="aspectFill"
          class="w-full h-full"
          @click="handlePreview(index)"
        />
        <view
          v-if="!disabled"
          class="absolute top-1 right-1 w-5 h-5 bg-black/50 rounded-full flex items-center justify-center"
          @click.stop="handleRemove(index)"
        >
          <text class="text-white text-xs leading-none">×</text>
        </view>
        <view
          v-if="uploadingIndex === index"
          class="absolute inset-0 bg-black/50 flex items-center justify-center"
        >
          <view class="w-6 h-6 border-2 border-white border-t-transparent rounded-full animate-spin"></view>
        </view>
      </view>

      <!-- 添加按钮 -->
      <view
        v-if="modelValue.length < max && !disabled"
        class="w-20 h-20 border-2 border-dashed border-gray-300 rounded-lg flex flex-col items-center justify-center bg-gray-50 active:bg-gray-100"
        @click="handleChoose"
      >
        <text class="text-2xl text-gray-400 leading-none mb-1">+</text>
        <text class="text-xs text-gray-400">上传</text>
      </view>
    </view>

    <text v-if="tip" class="text-xs text-gray-400 mt-2 block">{{ tip }}</text>
  </view>
</template>

<script setup>
/**
 * SkImageUpload 图片上传组件
 * 功能：多图选择、上传、预览、删除
 */
import { ref } from 'vue'
import { getStorage } from '@/utils/storage.js'
import { uploadInspectionImage } from '@/services/inspection.js'
import config from '@/config/index.js'

const props = defineProps({
  modelValue: { type: Array, default: () => [] },
  label: { type: String, default: '' },
  max: { type: Number, default: 6 },
  disabled: { type: Boolean, default: false },
  tip: { type: String, default: '支持jpg/png格式，最多上传6张' },
})

const emit = defineEmits(['update:modelValue', 'change'])

const uploadingIndex = ref(-1)

const getFullUrl = (img) => {
  if (!img) return ''
  if (img.startsWith('http') || img.startsWith('blob') || img.startsWith('data:')) return img
  return `${config.baseUrl}/photo/${img}`
}

const handleChoose = () => {
  if (props.disabled) return
  const remaining = props.max - props.modelValue.length
  if (remaining <= 0) {
    uni.showToast({ title: `最多上传${props.max}张`, icon: 'none' })
    return
  }

  uni.chooseImage({
    count: remaining,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: async (res) => {
      for (const filePath of res.tempFilePaths) {
        await uploadImage(filePath)
      }
    },
  })
}

const uploadImage = async (filePath) => {
  const currentList = [...props.modelValue]
  const tempIndex = currentList.length

  currentList.push(filePath)
  emit('update:modelValue', currentList)
  uploadingIndex.value = tempIndex

  try {
    const res = await uploadInspectionImage(filePath)
    const newList = [...props.modelValue]
    newList[tempIndex] = res.data
    emit('update:modelValue', newList)
    emit('change', newList)
  } catch {
    uni.showToast({ title: '上传失败', icon: 'none' })
    const newList = props.modelValue.filter((_, i) => i !== tempIndex)
    emit('update:modelValue', newList)
  } finally {
    uploadingIndex.value = -1
  }
}

const handlePreview = (index) => {
  const urls = props.modelValue.map((img) => getFullUrl(img))
  uni.previewImage({ urls, current: index })
}

const handleRemove = (index) => {
  uni.showModal({
    title: '提示',
    content: '确定删除这张图片吗？',
    success: (res) => {
      if (res.confirm) {
        const newList = props.modelValue.filter((_, i) => i !== index)
        emit('update:modelValue', newList)
        emit('change', newList)
      }
    },
  })
}
</script>
