<template>
  <view class="min-h-screen bg-gray-50 p-4">
    <!-- 监测模块 -->
    <SkCard title="监测模块">
      <view class="grid grid-cols-4 gap-3 py-2">
        <view
          v-for="item in monitorMenus"
          :key="item.name"
          class="flex flex-col items-center py-2 active:opacity-70"
          @click="handleNavigation(item)"
        >
          <view
            class="w-12 h-12 rounded-xl bg-primary/10 flex items-center justify-center mb-2"
          >
            <image :src="item.icon" mode="aspectFit" class="w-7 h-7"></image>
          </view>
          <text class="text-xs text-gray-700">{{ item.name }}</text>
        </view>
      </view>
    </SkCard>

    <!-- 巡检模块 -->
    <SkCard title="巡检模块">
      <view class="grid grid-cols-4 gap-3 py-2">
        <view
          v-for="item in inspectionMenus"
          :key="item.name"
          class="flex flex-col items-center py-2 active:opacity-70"
          @click="handleNavigation(item)"
        >
          <view
            class="w-12 h-12 rounded-xl bg-primary/10 flex items-center justify-center mb-2"
          >
            <image :src="item.icon" mode="aspectFit" class="w-7 h-7"></image>
          </view>
          <text class="text-xs text-gray-700">{{ item.name }}</text>
        </view>
      </view>
    </SkCard>
  </view>
</template>

<script setup>
/**
 * 功能中心
 * 功能：监测模块、巡检模块业务入口
 */
import SkCard from '@/components/common/SkCard.vue'

const monitorMenus = [
  {
    name: '水位监测',
    icon: '/static/icons/monitor/water.svg',
    path: '/pages/modules/water-level/water-level',
  },
  {
    name: '雨量监测',
    icon: '/static/icons/monitor/rain.svg',
    path: '/pages/modules/rainfall/rainfall',
  },
  {
    name: '渗压监测',
    icon: '/static/icons/monitor/pressure.svg',
    path: '/pages/modules/seepage/seepage',
  },
  {
    name: '渗流量监测',
    icon: '/static/icons/monitor/seepage.svg',
    path: '/pages/modules/seepage/seepage-flow',
  },
]

const inspectionMenus = [
  {
    name: '巡检记录',
    icon: '/static/icons/inspection/record.svg',
    path: '/pages/modules/inspection/inspection',
  },
]

const handleNavigation = (item) => {
  uni.navigateTo({ url: item.path })
}
</script>
