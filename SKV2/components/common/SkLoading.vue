<template>
  <view class="flex flex-col items-center justify-center py-25">
    <view
      class="w-10 h-10 border-4 border-primary border-t-transparent rounded-full animate-spin"
    ></view>
    <text class="text-base text-gray-500 mt-5">{{ text }}</text>
  </view>
</template>

<script setup>
/**
 * SkLoading 加载状态组件
 * 功能：统一加载中展示
 */
defineProps({
  text: { type: String, default: '加载中...' },
})
</script>
