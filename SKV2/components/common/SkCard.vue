<template>
  <view
    class="bg-white rounded-xl mb-4 overflow-hidden"
    :class="[
      shadow === 'always'
        ? 'shadow-sm'
        : shadow === 'hover'
          ? 'shadow-sm active:shadow-md active:-translate-y-0.5'
          : '',
      border ? 'border border-gray-200' : '',
    ]"
    @click="handleClick"
  >
    <view
      v-if="title || $slots.header"
      class="flex items-center justify-between px-4 py-2 border-b border-gray-100"
    >
      <view v-if="title" class="flex items-center">
        <view class="w-1 h-5 bg-primary rounded mr-3"></view>
        <text class="text-base font-semibold text-gray-900">{{ title }}</text>
      </view>
      <slot name="header"></slot>
    </view>

    <view class="px-4 py-2">
      <slot></slot>
    </view>

    <view v-if="$slots.footer" class="px-4 py-3 border-t border-gray-50">
      <slot name="footer"></slot>
    </view>
  </view>
</template>

<script setup>
/**
 * SkCard 通用卡片
 * 功能：基础内容容器，支持标题、阴影、边框
 */
defineProps({
  title: { type: String, default: '' },
  shadow: { type: String, default: 'always' },
  border: { type: Boolean, default: false },
})

const emit = defineEmits(['click'])

const handleClick = (e) => {
  emit('click', e)
}
</script>
