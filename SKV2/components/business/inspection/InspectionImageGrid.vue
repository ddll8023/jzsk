<template>
  <view v-if="imageList.length > 0" class="flex gap-2 mb-3">
    <image
      v-for="(img, index) in imageList.slice(0, maxPreview)"
      :key="index"
      :src="getImageUrl(img)"
      mode="aspectFill"
      class="w-16 h-16 rounded bg-gray-100"
      @click="handlePreview(index)"
    />
    <view
      v-if="imageList.length > maxPreview"
      class="w-16 h-16 rounded bg-gray-100 flex items-center justify-center"
    >
      <text class="text-sm text-gray-500">+{{ imageList.length - maxPreview }}</text>
    </view>
  </view>
</template>

<script setup>
/**
 * InspectionImageGrid 巡检图片展示组件
 * 功能：巡检图片缩略图网格展示、预览
 */
import { computed } from 'vue'
import config from '@/config/index.js'

const props = defineProps({
  imageStr: { type: String, default: '' },
  maxPreview: { type: Number, default: 3 },
})

const emit = defineEmits(['preview'])

const imageList = computed(() => {
  if (!props.imageStr) return []
  return props.imageStr.split(';').filter((img) => img.trim())
})

const getImageUrl = (img) => {
  if (!img) return ''
  if (img.startsWith('http')) return img
  return `${config.baseUrl}/photo/${img}`
}

const handlePreview = (index) => {
  const urls = imageList.value.map((img) => getImageUrl(img))
  uni.previewImage({ urls, current: index })
  emit('preview', index)
}
</script>
