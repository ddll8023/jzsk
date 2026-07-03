<template>
  <div
    class="absolute top-20 left-4 z-10 transition-all duration-300 ease-in-out"
    :class="[isCollapsed ? 'w-10' : 'w-56']"
  >
    <div class="bg-white/90 backdrop-blur-md shadow-lg rounded-lg overflow-hidden border border-white/50">
      <!-- 标题栏 / 折叠按钮 -->
      <div
        class="flex items-center justify-between p-2 cursor-pointer hover:bg-gray-50/80 transition-colors"
        @click="toggleCollapse"
        :title="isCollapsed ? '展开图例' : '收起图例'"
      >
        <div v-if="!isCollapsed" class="flex items-center gap-2 text-sm font-bold text-gray-700">
          <svg class="w-4 h-4 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
          <span>图例说明</span>
        </div>
        <div v-else class="w-full flex justify-center">
          <svg class="w-4 h-4 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </div>

        <svg
          v-if="!isCollapsed"
          class="w-3 h-3 text-gray-400 transition-transform duration-300"
          :class="{'rotate-180': isCollapsed}"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
      </div>

      <!-- 图例内容列表 -->
      <transition
        enter-active-class="transition-all duration-300 ease-out"
        enter-from-class="max-h-0 opacity-0"
        enter-to-class="max-h-96 opacity-100"
        leave-active-class="transition-all duration-200 ease-in"
        leave-from-class="max-h-96 opacity-100"
        leave-to-class="max-h-0 opacity-0"
      >
        <div v-show="!isCollapsed" class="p-3 pt-0 space-y-3 border-t border-gray-100">
          <!-- 测站类型列表 -->
          <label
            v-for="item in legendItems"
            :key="item.key"
            class="flex items-center gap-3 cursor-pointer group hover:bg-gray-50/80 rounded p-1.5 -mx-1 transition-colors"
          >
            <input
              type="checkbox"
              :checked="modelValue[item.key]"
              @change="handleChange(item.key, $event.target.checked)"
              class="w-3.5 h-3.5 text-blue-600 rounded border-gray-300 focus:ring-blue-500 focus:ring-offset-0 cursor-pointer"
            />
            <div class="flex items-center gap-2">
              <!-- GNSS测站 - 使用地图实际图标（已到报状态） -->
              <img v-if="item.key === 'gnss'" src="https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png" class="w-5 h-5 object-contain" alt="GNSS" />
              <!-- 雨量水位站 - 使用地图实际图标（已到报状态） -->
              <img v-else-if="item.key === 'rain'" src="/icons/流量站点.png" class="w-5 h-5 object-contain" alt="雨量水位站" />
              <!-- 渗压测站 - 使用地图实际图标（已到报状态） -->
              <img v-else-if="item.key === 'seepage'" src="/icons/水厂.png" class="w-5 h-5 object-contain" alt="渗压测站" />
              <span class="text-xs text-gray-600 font-medium group-hover:text-gray-800">{{ item.label }}</span>
            </div>
          </label>

          <!-- 图标示例说明 -->
          <div class="space-y-2 pt-2 border-t border-gray-100">
            <div class="text-xs font-medium text-gray-600">测站状态示例：</div>

            <!-- GNSS测站 -->
            <div class="flex items-center gap-4">
              <div class="flex items-center gap-2">
                <img src="https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png" class="w-5 h-5 object-contain" alt="GNSS已到报" />
                <span class="text-xs text-gray-500">已到报</span>
              </div>
              <div class="flex items-center gap-2">
                <img src="https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png" class="w-5 h-5 object-contain grayscale opacity-50" alt="GNSS未到报" />
                <span class="text-xs text-gray-500">未到报</span>
              </div>
              <span class="text-xs text-gray-400 ml-auto">GNSS</span>
            </div>

            <!-- 雨量水位站 -->
            <div class="flex items-center gap-4">
              <div class="flex items-center gap-2">
                <img src="/icons/流量站点.png" class="w-5 h-5 object-contain" alt="雨量水位站已到报" />
                <span class="text-xs text-gray-500">已到报</span>
              </div>
              <div class="flex items-center gap-2">
                <img src="/icons/流量站点.png" class="w-5 h-5 object-contain grayscale opacity-50" alt="雨量水位站未到报" />
                <span class="text-xs text-gray-500">未到报</span>
              </div>
              <span class="text-xs text-gray-400 ml-auto">雨量水位</span>
            </div>

            <!-- 渗压测站 -->
            <div class="flex items-center gap-4">
              <div class="flex items-center gap-2">
                <img src="/icons/水厂.png" class="w-5 h-5 object-contain" alt="渗压已到报" />
                <span class="text-xs text-gray-500">已到报</span>
              </div>
              <div class="flex items-center gap-2">
                <img src="/icons/水厂.png" class="w-5 h-5 object-contain grayscale opacity-50" alt="渗压未到报" />
                <span class="text-xs text-gray-500">未到报</span>
              </div>
              <span class="text-xs text-gray-400 ml-auto">渗压测站</span>
            </div>
          </div>

          <!-- 超时阈值说明 -->
          <div class="text-xs text-gray-400 pt-1 border-t border-gray-100 space-y-0.5">
            <div>到报阈值：GNSS 60min | 雨量水位 5min | 渗压 60min</div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
/**
 * 地图图例组件
 * 功能：测站图层显示/隐藏控制
 * 优化：
 * 1. 使用地图实际图片作为图标（保持一致性）
 * 2. 支持折叠/展开
 * 3. 立即可用，不依赖数据加载
 * Source: 基于旧版本优化
 */
import { ref } from 'vue'

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

const isCollapsed = ref(false)

const legendItems = [
  { key: 'gnss', label: 'GNSS测站' },
  { key: 'rain', label: '雨量水位站' },
  { key: 'seepage', label: '渗压测站' }
]

/**
 * 切换折叠状态
 */
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

/**
 * 处理图层显示变更
 * @param {string} key - 图层键名
 * @param {boolean} checked - 是否显示
 */
const handleChange = (key, checked) => {
  const newValue = { ...props.modelValue, [key]: checked }
  emit('update:modelValue', newValue)
  // 同时触发 change 事件，传递具体的变更项
  emit('change', key, checked)
}
</script>
