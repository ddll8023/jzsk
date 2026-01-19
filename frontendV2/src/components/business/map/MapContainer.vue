<template>
  <div class="relative w-full h-full">
    <!-- 地图容器 -->
    <div ref="mapContainer" class="w-full h-full"></div>

    <!-- 坐标弹窗 -->
    <transition
      enter-active-class="transition-all duration-200 ease-out"
      enter-from-class="opacity-0 scale-95"
      enter-to-class="opacity-100 scale-100"
      leave-active-class="transition-all duration-150 ease-in"
      leave-from-class="opacity-100 scale-100"
      leave-to-class="opacity-0 scale-95"
    >
      <div 
        v-show="popupContent"
        ref="popupContainer" 
        class="absolute z-20 min-w-[280px] max-w-[400px] bg-white/90 backdrop-blur-sm shadow-xl rounded-lg border border-white/50 p-4"
      >
        <!-- 关闭按钮 -->
        <button
          @click="hidePopup"
          class="absolute top-2 right-2 p-1 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded transition-colors"
          aria-label="关闭"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        
        <!-- 弹窗内容 -->
        <div v-html="popupContent" class="popup-content pr-6"></div>
        
        <!-- 弹窗箭头 -->
        <div class="absolute top-full left-1/2 -translate-x-1/2 -mt-px">
          <svg class="w-4 h-2 text-white/90 drop-shadow-sm" viewBox="0 0 16 8" fill="currentColor">
            <path d="M8 8L0 0h16L8 8z" />
          </svg>
        </div>
      </div>
    </transition>

    <!-- 错误提示 -->
    <transition
      enter-active-class="transition-opacity duration-300"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition-opacity duration-200"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div 
        v-if="error" 
        class="absolute inset-0 flex items-center justify-center bg-gray-900/20 backdrop-blur-sm z-50"
      >
        <div class="bg-white/95 backdrop-blur-md rounded-xl shadow-2xl border border-white/60 p-8 max-w-md mx-4 text-center">
          <!-- 错误图标 -->
          <div class="mb-4 flex justify-center">
            <div class="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center">
              <svg class="w-8 h-8 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
          </div>
          
          <!-- 错误信息 -->
          <h3 class="text-lg font-semibold text-gray-900 mb-2">加载失败</h3>
          <p class="text-gray-600 mb-6">{{ error }}</p>
          
          <!-- 重试按钮 -->
          <Button type="primary" @click="handleRetry" class="min-w-[120px]">
            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            重新加载
          </Button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
/**
 * 地图容器组件
 * 功能：OpenLayers地图渲染、弹窗管理、错误处理
 * 依赖：useMap、useMapPopup、Button组件
 * 优化：
 * 1. 统一样式风格（backdrop-blur、transition动画）
 * 2. 使用SVG图标替代FontAwesome
 * 3. 统一z-index层级（弹窗z-20，错误z-50）
 * 4. 添加淡入淡出动画
 * 5. 支持父组件传入地图配置参数（center、zoom）- 2025-01-19修复
 * Source: 基于旧项目重构，遵循KISS/SOLID原则
 */
import { ref, onMounted, watch } from 'vue'
import { useMap } from '@/composables/useMap'
import { useMapPopup } from '@/composables/useMapPopup'
import Button from '@/components/basic/Button.vue'

// 定义组件属性：接收父组件传入的地图配置
const props = defineProps({
  // 地图中心点坐标 [经度, 纬度]
  center: {
    type: Array,
    default: () => [115.691846443, 30.128530098]
  },
  // 地图初始缩放级别（1-20）
  zoom: {
    type: Number,
    default: 16
  }
})

const emit = defineEmits(['map-ready'])

// DOM引用
const mapContainer = ref(null)
const popupContainer = ref(null)

// 地图实例（传入父组件配置）
const { map, error, initMap } = useMap({
  center: props.center,
  zoom: props.zoom
})

// 弹窗管理
const { popupContent, hidePopup, initPopup } = useMapPopup(map)

/**
 * 组件挂载时初始化地图
 */
onMounted(() => {
  initMap(mapContainer.value)
  
  // 监听地图实例创建，初始化弹窗并通知父组件
  watch(map, (newMap) => {
    if (newMap && popupContainer.value) {
      initPopup(popupContainer.value)
      emit('map-ready', newMap)
    }
  }, { immediate: true })
})

/**
 * 重试加载地图
 * 清除错误状态并重新初始化
 */
const handleRetry = () => {
  error.value = ''
  initMap(mapContainer.value)
}

// 暴露地图实例给父组件
defineExpose({
  map
})
</script>

<style scoped>
/**
 * 弹窗内容样式
 * 限制最大宽度，确保内容不溢出
 */
.popup-content {
  max-width: 100%;
  word-wrap: break-word;
}

/**
 * OpenLayers默认控件样式覆盖
 * 统一控件外观，与整体设计风格保持一致
 */
:deep(.ol-control) {
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

:deep(.ol-control button) {
  background-color: rgba(255, 255, 255, 0.9);
  color: #374151;
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.ol-control button:hover) {
  background-color: white;
  transform: scale(1.05);
}

:deep(.ol-control button:active) {
  transform: scale(0.95);
}
</style>
