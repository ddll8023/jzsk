<template>
  <span :class="badgeClasses">
    <i :class="`fa fa-${iconName}`" aria-hidden="true" class="mr-1"></i>
    {{ statusText }}
  </span>
</template>

<script setup>
/**
 * StatusBadge 组件
 * 功能：预警状态徽章
 * 业务场景：预警信息管理
 * 遵循原则：KISS - 简洁实现单一功能
 */
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true
  }
})

// 状态映射配置
const statusConfig = {
  '未解除': {
    text: '未解除',
    icon: 'exclamation-circle',
    classes: 'bg-red-50 text-red-700 border-red-200'
  },
  '已解除': {
    text: '已解除',
    icon: 'check-circle',
    classes: 'bg-green-50 text-green-700 border-green-200'
  }
}

// 计算状态文本
const statusText = computed(() => {
  return statusConfig[props.status]?.text || props.status
})

// 计算图标名称
const iconName = computed(() => {
  return statusConfig[props.status]?.icon || 'circle'
})

// 计算徽章样式
const badgeClasses = computed(() => {
  const baseClasses = 'inline-flex items-center px-2.5 py-0.5 rounded text-xs font-medium border'
  const stateClasses = statusConfig[props.status]?.classes || 'bg-gray-50 text-gray-700 border-gray-200'
  return `${baseClasses} ${stateClasses}`
})
</script>
