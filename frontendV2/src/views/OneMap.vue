<template>
  <div class="h-full bg-white rounded-lg shadow">
    <!-- 地图容器 -->
    <div ref="mapContainer" class="w-full h-full min-h-[600px] rounded-lg">
      <!-- 地图加载中 -->
      <div v-if="loading" class="flex items-center justify-center h-full">
        <div class="text-center">
          <i class="fa fa-spinner fa-spin text-4xl text-primary-600" aria-hidden="true"></i>
          <p class="mt-4 text-gray-500">地图加载中...</p>
        </div>
      </div>
      
      <!-- 地图加载失败 -->
      <div v-if="error" class="flex items-center justify-center h-full">
        <div class="text-center">
          <i class="fa fa-exclamation-triangle text-4xl text-yellow-500" aria-hidden="true"></i>
          <p class="mt-4 text-gray-500">{{ error }}</p>
          <button
            @click="initMap"
            class="mt-4 px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
          >
            重新加载
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 一张图页面
 * 功能：地图展示（高德地图）
 * 依赖：@amap/amap-jsapi-loader
 */
import { ref, onMounted, onUnmounted } from 'vue'
import AMapLoader from '@amap/amap-jsapi-loader'

// 地图容器
const mapContainer = ref(null)

// 地图实例
let map = null

// 状态
const loading = ref(true)
const error = ref('')

/**
 * 初始化地图
 */
const initMap = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const AMap = await AMapLoader.load({
      key: 'your-amap-key', // 替换为实际的高德地图 Key
      version: '2.0',
      plugins: ['AMap.Scale', 'AMap.ToolBar']
    })
    
    map = new AMap.Map(mapContainer.value, {
      zoom: 10,
      center: [116.397428, 39.90923], // 默认中心点，按需修改
      mapStyle: 'amap://styles/normal'
    })
    
    // 添加控件
    map.addControl(new AMap.Scale())
    map.addControl(new AMap.ToolBar())
    
    loading.value = false
  } catch (e) {
    console.error('地图加载失败:', e)
    error.value = '地图加载失败，请检查网络连接'
    loading.value = false
  }
}

onMounted(() => {
  initMap()
})

onUnmounted(() => {
  // 销毁地图实例
  if (map) {
    map.destroy()
    map = null
  }
})
</script>
