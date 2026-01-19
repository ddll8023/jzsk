<template>
  <span :class="badgeClasses">
    <i :class="`fa fa-${iconName}`" aria-hidden="true" class="mr-1"></i>
    {{ levelText }}
  </span>
</template>

<script setup>
/**
 * LevelBadge 组件
 * 功能：预警等级徽章
 * 业务场景：预警信息管理
 * 遵循原则：KISS - 简洁实现单一功能
 */
import { computed } from 'vue'

const props = defineProps({
  level: {
    type: String,
    required: true
  }
})

// 等级映射配置
const levelConfig = {
  '一般': {
    text: '一般',
    icon: 'info-circle',
    classes: 'bg-blue-50 text-blue-700 border-blue-200'
  },
  '较重': {
    text: '较重',
    icon: 'exclamation-triangle',
    classes: 'bg-yellow-50 text-yellow-700 border-yellow-200'
  },
  '严重': {
    text: '严重',
    icon: 'exclamation-circle',
    classes: 'bg-orange-50 text-orange-700 border-orange-200'
  },
  '特别严重': {
    text: '特别严重',
    icon: 'times-circle',
    classes: 'bg-red-50 text-red-700 border-red-200'
  }
}

// 计算等级文本
const levelText = computed(() => {
  return levelConfig[props.level]?.text || props.level
})

// 计算图标名称
const iconName = computed(() => {
  return levelConfig[props.level]?.icon || 'circle'
})

// 计算徽章样式
const badgeClasses = computed(() => {
  const baseClasses = 'inline-flex items-center px-2.5 py-0.5 rounded text-xs font-medium border'
  const stateClasses = levelConfig[props.level]?.classes || 'bg-gray-50 text-gray-700 border-gray-200'
  return `${baseClasses} ${stateClasses}`
})
</script>
