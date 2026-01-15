<template>
  <!-- 卡片容器：支持多种变体和尺寸 -->
  <div :class="cardClasses">
    <!-- 标题区域（可选） -->
    <header v-if="$slots.header || title" class="card-header">
      <slot name="header">
        <h3 :class="titleClasses">{{ title }}</h3>
      </slot>
    </header>
    
    <!-- 内容区域 -->
    <div :class="contentClasses">
      <slot></slot>
    </div>
    
    <!-- 底部区域（可选） -->
    <footer v-if="$slots.footer" class="card-footer">
      <slot name="footer"></slot>
    </footer>
  </div>
</template>

<script setup>
/**
 * Card 卡片组件
 * 功能：通用卡片容器，支持毛玻璃、深色、默认三种变体
 * 遵循 KISS 原则：简洁实现，只包含必需功能
 */
import { computed } from 'vue'

const props = defineProps({
  // 卡片变体：default（白色）、glass（毛玻璃）、dark（深色）、shadow（多层阴影）
  variant: {
    type: String,
    default: 'default',
    validator: (v) => ['default', 'glass', 'dark', 'shadow'].includes(v)
  },
  // 内边距尺寸
  padding: {
    type: String,
    default: 'md',
    validator: (v) => ['none', 'sm', 'md', 'lg'].includes(v)
  },
  // 圆角尺寸
  rounded: {
    type: String,
    default: 'lg',
    validator: (v) => ['none', 'sm', 'md', 'lg', 'xl', '2xl'].includes(v)
  },
  // 阴影尺寸
  shadow: {
    type: String,
    default: 'md',
    validator: (v) => ['none', 'sm', 'md', 'lg', 'xl', '2xl'].includes(v)
  },
  // 卡片标题（可选）
  title: {
    type: String,
    default: ''
  }
})

// 变体样式映射
const variantClasses = {
  default: 'bg-white border border-gray-200',
  glass: 'backdrop-blur-xl bg-white/10 border border-white/20',
  dark: 'bg-gray-900/90 border border-gray-700/50',
  shadow: 'bg-gray-900/90 border border-gray-700/50'
}

// 内边距映射
const paddingClasses = {
  none: '',
  sm: 'p-4',
  md: 'p-6',
  lg: 'p-8'
}

// 圆角映射
const roundedClasses = {
  none: '',
  sm: 'rounded-sm',
  md: 'rounded-md',
  lg: 'rounded-lg',
  xl: 'rounded-xl',
  '2xl': 'rounded-2xl'
}

// 阴影映射
const shadowClasses = {
  none: '',
  sm: 'shadow-sm',
  md: 'shadow-md',
  lg: 'shadow-lg',
  xl: 'shadow-xl',
  '2xl': 'shadow-2xl'
}

// 计算卡片类名
const cardClasses = computed(() => [
  'card',
  variantClasses[props.variant],
  roundedClasses[props.rounded],
  shadowClasses[props.shadow]
])

// 计算内容区域类名
const contentClasses = computed(() => [
  'card-content',
  paddingClasses[props.padding]
])

// 计算标题类名（根据变体调整颜色）
const titleClasses = computed(() => [
  'text-lg font-semibold',
  props.variant === 'default' ? 'text-gray-900' : 'text-white'
])
</script>

<style scoped>
/**
 * 卡片组件样式
 * 使用 Tailwind 工具类，仅保留必要的自定义样式
 */
.card {
  /* 过渡动画 */
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

/* 毛玻璃效果增强（Safari 兼容） */
.card:where([class*="backdrop-blur"]) {
  -webkit-backdrop-filter: blur(24px);
}
</style>
