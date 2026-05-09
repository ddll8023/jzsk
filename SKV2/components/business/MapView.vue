<template>
  <view class="relative w-full" style="height: 55vh">
    <!-- #ifdef H5 -->
    <view id="tianditu-map" class="w-full h-full"></view>
    <!-- #endif -->

    <!-- #ifdef APP-PLUS -->
    <map
      class="w-full h-full"
      :latitude="appConfig.latitude"
      :longitude="appConfig.longitude"
      :scale="appConfig.scale"
      :markers="appConfig.markers"
    ></map>
    <!-- #endif -->

    <!-- 加载状态 -->
    <view
      v-if="!mapLoaded && !mapError"
      class="absolute inset-0 flex items-center justify-center bg-white/90"
    >
      <view
        class="w-10 h-10 border-4 border-gray-200 border-t-primary rounded-full animate-spin"
      ></view>
    </view>

    <!-- 错误状态 -->
    <view
      v-if="mapError"
      class="absolute inset-0 flex items-center justify-center bg-white/90"
    >
      <text class="text-sm text-error">{{ mapError }}</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 地图展示组件
 * 功能：双平台地图（H5 天地图 + APP 原生 map）
 */
import { onMounted } from 'vue'
import { useMap } from '@/composables/useMap.js'

const { mapLoaded, mapError, initH5Map, getAppMapConfig } = useMap()

// #ifdef APP-PLUS
const appConfig = getAppMapConfig()
// #endif

onMounted(() => {
  // #ifdef H5
  initH5Map('tianditu-map').catch((err) => {
    console.error('地图初始化失败:', err)
  })
  // #endif

  // #ifdef APP-PLUS
  mapLoaded.value = true
  // #endif
})
</script>
