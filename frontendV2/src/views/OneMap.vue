<template>
  <div class="relative h-full bg-gray-900 rounded-lg overflow-hidden">
    <!-- 地图容器 -->
    <MapContainer
      ref="mapContainerRef"
      :center="[115.691846443, 30.128530098]"
      :zoom="18"
      @map-ready="handleMapReady"
      class="w-full h-full"
    />

    <!-- 测站弹窗容器 -->
    <div ref="stationPopupRef" class="ol-popup">
      <button
        @click="hideStationPopup"
        class="popup-close"
        aria-label="关闭"
      >
        ×
      </button>
    </div>

    <!-- 工程简介按钮 -->
    <ProjectIntroButton />

    <!-- 预警信息面板 -->
    <WarningPanel @open-modal="warningModalVisible = true" />

    <!-- 预警信息弹窗 -->
    <WarningModal v-model="warningModalVisible" />

    <!-- 地图控件（底图切换） -->
    <transition
      enter-active-class="transition-opacity duration-300"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
    >
      <MapLayerControl
        v-if="mapReady"
        :current-base-map="currentBaseMap"
        @base-map-change="handleBaseMapChange"
      />
    </transition>

    <!-- 测站图例（支持折叠） -->
    <transition
      enter-active-class="transition-opacity duration-300"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
    >
      <MapLegend
        v-if="mapReady"
        v-model="stationVisibility"
        @change="handleStationToggle"
      />
    </transition>
  </div>
</template>

<script setup>
/**
 * 一张图页面
 * 功能：OpenLayers地图集成、预警信息展示、测站标注
 * 依赖：MapContainer, MapLayerControl, WarningPanel, WarningModal, ProjectIntroButton, MapLegend
 * Source: 基于旧项目 frontend/src/components/menu/OneMap.vue 重构
 * 优化：
 * 1. 移除业务图层功能（遵循YAGNI原则）
 * 2. 修复底图切换bug（传入实际map实例）
 * 3. 简化组件依赖关系
 * 4. 数据面板改造为预警信息展示（2025-01-19）
 */
import { ref, computed, reactive, nextTick } from 'vue'
import { useMap } from '@/composables/useMap'
import { useStationMarkers } from '@/composables/useStationMarkers'
import MapContainer from '@/components/business/map/MapContainer.vue'
import MapLayerControl from '@/components/business/map/MapLayerControl.vue'
import MapLegend from '@/components/business/map/MapLegend.vue'
import WarningPanel from '@/components/business/map/WarningPanel.vue'
import WarningModal from '@/components/business/map/WarningModal.vue'
import ProjectIntroButton from '@/components/business/map/ProjectIntroButton.vue'

// 地图容器引用
const mapContainerRef = ref(null)
const stationPopupRef = ref(null)

// 地图就绪状态
const mapReady = ref(false)

// 地图实例（从MapContainer获取）
const map = computed(() => mapContainerRef.value?.map)

// 底图管理
const { currentBaseMap, changeBaseMap } = useMap()

// 测站标注管理器
let stationMarkerManager = null

// 测站显示状态
const stationVisibility = reactive({
  gnss: true,
  rain: true,
  seepage: true
})

// 预警弹窗可见性
const warningModalVisible = ref(false)

/**
 * 地图准备完成回调
 * 优化：地图实例创建后立即显示控件，测站数据异步加载不阻塞UI
 * 修复：使用 watch 实现弹窗内容响应式绑定
 * 修复：添加鼠标样式切换（悬浮测站图标时显示手型）
 */
const handleMapReady = async (mapInstance) => {
  mapReady.value = true
  stationMarkerManager = useStationMarkers(map)

  await nextTick()

  if (stationPopupRef.value) {
    stationMarkerManager.createStationMarkers(stationPopupRef.value)
    stationMarkerManager.updateStationData().catch(error => {
      console.error('[OneMap] 测站数据加载失败:', error)
    })
  }

  if (mapInstance) {
    mapInstance.on('pointermove', (evt) => {
      const pixel = mapInstance.getEventPixel(evt.originalEvent)
      const hit = mapInstance.hasFeatureAtPixel(pixel)
      mapInstance.getTargetElement().style.cursor = hit ? 'pointer' : ''
    })
  }
}

/**
 * 切换底图
 * 修复：传入实际的地图实例
 */
const handleBaseMapChange = (type) => {
  if (!map.value) {
    console.warn('[OneMap] 地图实例未就绪，无法切换底图')
    return
  }
  changeBaseMap(map.value, type)
}

/**
 * 切换测站显示
 */
const handleStationToggle = (stationType, visible) => {
  if (stationMarkerManager) {
    stationMarkerManager.toggleLayer(stationType, visible)
  }
}

/**
 * 隐藏测站弹窗
 */
const hideStationPopup = () => {
  if (stationMarkerManager) {
    stationMarkerManager.hidePopup()
  }
}
</script>

<style scoped>
/**
 * 测站弹窗样式
 */
.ol-popup {
  position: absolute;
  background-color: white;
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
  padding: 0;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  bottom: 12px;
  left: -50px;
  min-width: 200px;
  max-width: 400px;
  z-index: 1000;
}

.ol-popup::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -8px;
  border-width: 8px;
  border-style: solid;
  border-color: white transparent transparent transparent;
}

.popup-close {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  color: #999;
  font-size: 20px;
  line-height: 1;
  cursor: pointer;
  transition: color 0.2s;
  z-index: 1;
}

.popup-close:hover {
  color: #666;
}
</style>
