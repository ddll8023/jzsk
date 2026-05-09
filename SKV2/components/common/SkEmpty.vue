<template>
  <view class="flex flex-col items-center justify-center py-8">
    <text class="text-sm text-gray-500">{{ text }}</text>
  </view>
</template>

<script setup>
/**
 * SkEmpty 空状态组件
 * 功能：统一空数据展示
 */
defineProps({
  text: { type: String, default: '暂无数据' },
})
</script>
