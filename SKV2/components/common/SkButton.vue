<template>
  <view
    class="inline-flex items-center justify-center transition-all duration-300"
    :class="[
      sizeClasses,
      typeClasses,
      { 'rounded-full': round, 'rounded-lg': !round },
      { 'opacity-60 cursor-not-allowed': disabled },
      { 'active:scale-95': !disabled && !loading },
    ]"
    @click="handleClick"
  >
    <view
      v-if="loading"
      class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"
    ></view>
    <text class="font-medium">{{ text }}</text>
  </view>
</template>

<script setup>
/**
 * SkButton 按钮组件
 * 功能：通用按钮，支持多种样式变体
 */
import { computed } from 'vue'

const props = defineProps({
  text: { type: String, default: '按钮' },
  type: { type: String, default: 'primary' },
  size: { type: String, default: 'large' },
  plain: { type: Boolean, default: false },
  round: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  loading: { type: Boolean, default: false },
})

const emit = defineEmits(['click'])

const sizeClasses = computed(() => {
  const map = {
    large: 'text-base px-4 py-2.5 min-w-[128rpx]',
    small: 'text-sm px-3 py-1.5 min-w-[96rpx]',
  }
  return map[props.size] || map.large
})

const typeClasses = computed(() => {
  const map = {
    primary: props.plain
      ? 'border-2 border-primary text-primary bg-transparent'
      : 'bg-primary text-white',
    default: props.plain
      ? 'border-2 border-gray-300 text-gray-700 bg-transparent'
      : 'bg-gray-200 text-gray-900',
    success: props.plain
      ? 'border-2 border-green-500 text-green-600 bg-transparent'
      : 'bg-green-500 text-white',
    warning: props.plain
      ? 'border-2 border-orange-500 text-orange-600 bg-transparent'
      : 'bg-orange-500 text-white',
    error: props.plain
      ? 'border-2 border-red-500 text-red-600 bg-transparent'
      : 'bg-red-500 text-white',
  }
  return map[props.type] || map.primary
})

const handleClick = (e) => {
  if (props.disabled || props.loading) return
  emit('click', e)
}
</script>
