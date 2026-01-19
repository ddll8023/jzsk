<template>
  <div class="absolute top-20 right-4 z-40 font-sans">
    <!-- 主触发按钮 -->
    <div 
      class="bg-white/90 hover:bg-white text-gray-700 shadow-xl backdrop-blur-md rounded-lg cursor-pointer transition-all duration-300 w-10 h-10 flex items-center justify-center border border-white/50 hover:scale-105 active:scale-95"
      @click="togglePanel"
      title="底图切换"
    >
      <svg class="w-5 h-5" :class="{'text-blue-600': isOpen}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
      </svg>
    </div>

    <!-- 底图选择面板 -->
    <transition
      enter-active-class="transition ease-out duration-200"
      enter-from-class="opacity-0 translate-y-2 scale-95"
      enter-to-class="opacity-100 translate-y-0 scale-100"
      leave-active-class="transition ease-in duration-150"
      leave-from-class="opacity-100 translate-y-0 scale-100"
      leave-to-class="opacity-0 translate-y-2 scale-95"
    >
      <div 
        v-if="isOpen"
        class="absolute right-0 top-12 w-72 bg-white/95 backdrop-blur-xl rounded-xl shadow-2xl overflow-hidden border border-white/60"
      >
        <!-- 标题 -->
        <div class="px-4 py-3 border-b border-gray-100 bg-gray-50/50">
          <h3 class="text-sm font-semibold text-gray-700">底图风格</h3>
        </div>

        <!-- 底图选择 -->
        <div class="p-4">
          <div class="grid grid-cols-2 gap-3">
            <div
              v-for="map in baseMaps"
              :key="map.value"
              @click="changeBaseMap(map.value)"
              class="relative group cursor-pointer"
            >
              <!-- 预览卡片 -->
              <div 
                class="aspect-video rounded-lg overflow-hidden border-2 transition-all duration-200 shadow-sm"
                :class="currentBaseMap === map.value ? 'border-blue-500 ring-2 ring-blue-100' : 'border-gray-200 group-hover:border-blue-300'"
              >
                <!-- 模拟缩略图背景 -->
                <div 
                  class="w-full h-full bg-cover bg-center transition-transform duration-500 group-hover:scale-110"
                  :style="getBaseMapPreview(map.value)"
                ></div>
                
                <!-- 选中标记 -->
                <div 
                  v-if="currentBaseMap === map.value"
                  class="absolute inset-0 bg-blue-600/10 flex items-center justify-center backdrop-blur-[1px]"
                >
                  <div class="bg-blue-600 text-white rounded-full w-6 h-6 flex items-center justify-center shadow-lg">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                    </svg>
                  </div>
                </div>
              </div>
              <div 
                class="mt-2 text-center text-xs font-medium transition-colors"
                :class="currentBaseMap === map.value ? 'text-blue-600' : 'text-gray-600'"
              >
                {{ map.label }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
/**
 * 地图底图切换控件
 * 功能：天地图底图风格切换（矢量、影像、地形）
 * 优化：
 * 1. 移除业务图层功能（遵循YAGNI原则）
 * 2. 简化为单一职责组件
 * 3. 使用SVG图标替代FontAwesome
 * Source: 基于旧版本简化重构
 */
import { ref } from 'vue'

const props = defineProps({
  currentBaseMap: {
    type: String,
    default: 'img' // 默认显示卫星图
  }
})

const emit = defineEmits(['base-map-change'])

// 面板显示状态
const isOpen = ref(false)

// 底图配置（移除地形地貌选项，遵循YAGNI原则）
const baseMaps = [
  { value: 'vec', label: '矢量地图' },
  { value: 'img', label: '影像卫星' }
]

/**
 * 获取底图预览样式
 * 使用渐变色模拟不同底图风格
 */
const getBaseMapPreview = (type) => {
  const colors = {
    vec: 'linear-gradient(135deg, #f3f4f6 0%, #d1d5db 100%)', // 浅色
    img: 'linear-gradient(135deg, #1f2937 0%, #111827 100%)', // 深色
    ter: 'linear-gradient(135deg, #d1d5db 0%, #9ca3af 100%)'  // 中性色
  }
  return { background: colors[type] || '#eee' }
}

/**
 * 切换面板显示/隐藏
 */
const togglePanel = () => {
  isOpen.value = !isOpen.value
}

/**
 * 切换底图
 * @param {string} type - 底图类型
 */
const changeBaseMap = (type) => {
  emit('base-map-change', type)
  // 切换后自动关闭面板
  isOpen.value = false
}
</script>

<style scoped>
/**
 * 组件样式
 * 使用Tailwind类名，无需额外样式
 */
</style>
