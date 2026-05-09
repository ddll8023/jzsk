<template>
  <view class="min-h-screen bg-gray-50">
    <!-- 自定义导航栏 -->
    <view
      class="fixed top-0 left-0 right-0 z-10"
      :style="{ paddingTop: statusBarHeight + 'px' }"
    >
      <view class="flex items-center px-4 py-3">
        <text class="text-white text-lg font-bold">武穴市荆竹水库</text>
      </view>
    </view>

    <!-- 地图区域 -->
    <view class="relative">
      <MapView />

      <!-- 底部渐变遮罩 -->
      <view class="absolute bottom-0 left-0 right-0 pointer-events-none" style="height: 120rpx;">
      </view>
    </view>

    <!-- 信息卡片浮层 -->
    <view class="relative z-10 -mt-6 rounded-t-3xl bg-gray-50 pb-8">
      <!-- 面板把手 -->
      <view
        class="w-12 h-1.5 rounded-full bg-gray-300 mx-auto mt-4 mb-4"
      ></view>

      <ReservoirInfoCard />
    </view>
  </view>
</template>

<script setup>
/**
 * 首页
 * 功能：水库地图、水库图片轮播、水库基础信息
 */
import { ref } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import MapView from '@/components/business/MapView.vue'
import ReservoirInfoCard from '@/components/business/ReservoirInfoCard.vue'

const statusBarHeight = ref(0)

onLoad(() => {
  const systemInfo = uni.getSystemInfoSync()
  statusBarHeight.value = systemInfo.statusBarHeight || 0
})
</script>

<style scoped>
.fixed {
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.3),
    transparent
  );
}
</style>
