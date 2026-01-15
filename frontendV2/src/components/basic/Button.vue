<template>
  <button :class="buttonClasses" :disabled="disabled || loading" @click="handleClick">
    <i v-if="loading" class="fa fa-spinner fa-spin mr-2" aria-hidden="true"></i>
    <i v-else-if="icon" :class="`fa fa-${icon}`" aria-hidden="true" class="mr-2"></i>
    <slot></slot>
  </button>
</template>

<script setup>
/**
 * Button 组件
 * 功能：统一按钮样式与交互的基础组件
 * 遵循 KISS 原则：简洁实现，只包含必需功能
 * 遵循 YAGNI 原则：只实现实际使用的 props
 */
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'default'
  },
  size: {
    type: String,
    default: 'md'
  },
  loading: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  block: {
    type: Boolean,
    default: false
  },
  icon: {
    type: String,
    default: ''
  },
  circle: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['click'])

// 基础样式
const baseClasses = 'inline-flex items-center justify-center font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2'

// 尺寸映射
const sizeClasses = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-4 py-2 text-sm',
  lg: 'px-6 py-3 text-base'
}

// 圆形按钮尺寸
const circleSizeClasses = {
  sm: 'w-8 h-8 text-sm',
  md: 'w-10 h-10 text-sm',
  lg: 'w-12 h-12 text-base'
}

// 变体映射
const variantClasses = {
  default: 'border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 hover:border-gray-400 focus:ring-gray-500',
  primary: 'border border-transparent bg-primary-600 text-white hover:bg-primary-700 focus:ring-primary-500',
  // 次要按钮：灰色背景，用于重置等次要操作
  secondary: 'border border-gray-300 bg-gray-200 text-gray-900 hover:bg-gray-300 focus:ring-gray-500',
  success: 'border border-transparent bg-green-600 text-white hover:bg-green-700 focus:ring-green-500',
  danger: 'border border-transparent bg-red-600 text-white hover:bg-red-700 focus:ring-red-500',
  warning: 'border border-transparent bg-yellow-500 text-white hover:bg-yellow-600 focus:ring-yellow-500',
  text: 'bg-transparent text-gray-700 hover:bg-gray-50 focus:ring-gray-200'
}

// 计算按钮样式类名
const buttonClasses = computed(() => [
  baseClasses,
  props.circle ? circleSizeClasses[props.size] || circleSizeClasses.md : sizeClasses[props.size] || sizeClasses.md,
  variantClasses[props.type] || variantClasses.default,
  props.circle ? 'rounded-full p-0' : 'rounded-md',
  {
    'w-full justify-center': props.block,
    'opacity-50 cursor-not-allowed': props.disabled || props.loading
  }
])

/**
 * 处理点击事件
 * 避免在 loading 或 disabled 状态下触发
 */
const handleClick = (event) => {
  if (!props.loading && !props.disabled) {
    emit('click', event)
  }
}
</script>
