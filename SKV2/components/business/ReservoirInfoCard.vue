<template>
  <view class="mx-4 bg-white rounded-2xl overflow-hidden shadow-sm">
    <!-- 图片轮播 -->
    <swiper
      class="w-full"
      style="height: 400rpx"
      indicator-dots
      indicator-color="rgba(255, 255, 255, 0.5)"
      indicator-active-color="#00b783"
      autoplay
      circular
    >
      <swiper-item v-for="(img, index) in images" :key="index">
        <image :src="img" mode="aspectFill" class="w-full h-full"></image>
      </swiper-item>
    </swiper>

    <!-- 水库基础信息 -->
    <view class="p-5">
      <text class="block text-xl font-bold text-gray-900 mb-4">{{
        info.name
      }}</text>
      <view class="grid grid-cols-2 gap-3">
        <view class="flex flex-col">
          <text class="text-xs text-gray-500 mb-1">地理坐标</text>
          <text class="text-sm text-gray-800">{{ info.location }}</text>
        </view>
        <view class="flex flex-col">
          <text class="text-xs text-gray-500 mb-1">库容水量</text>
          <text class="text-sm text-gray-800">{{ info.capacity }}</text>
        </view>
        <view class="flex flex-col">
          <text class="text-xs text-gray-500 mb-1">灌溉面积</text>
          <text class="text-sm text-gray-800">{{ info.irrigationArea }}</text>
        </view>
        <view class="flex flex-col">
          <text class="text-xs text-gray-500 mb-1">建成时间</text>
          <text class="text-sm text-gray-800">{{ info.buildYear }}</text>
        </view>
        <view class="flex flex-col col-span-2">
          <text class="text-xs text-gray-500 mb-1">主要功能</text>
          <text class="text-sm text-gray-800">{{ info.functions }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * ReservoirInfoCard 水库信息卡片
 * 功能：图片轮播 + 水库基础信息展示
 */
import { RESERVOIR_INFO } from '@/config/map.js'

defineProps({
  images: {
    type: Array,
    default: () => ['/static/img/sk.jpg', '/static/img/sk2.jpg'],
  },
  info: {
    type: Object,
    default: () => RESERVOIR_INFO,
  },
})
</script>
