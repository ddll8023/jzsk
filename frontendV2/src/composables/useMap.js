/**
 * 地图初始化与操作 Composable
 * 功能：OpenLayers地图实例管理、视图配置、控件添加
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 基于旧项目 frontend/src/components/menu/OneMap.vue 重构
 */

import { ref, onUnmounted } from 'vue'
import Map from 'ol/Map'
import View from 'ol/View'
import { FullScreen } from 'ol/control'
import { Tile as TileLayer } from 'ol/layer'
import XYZ from 'ol/source/XYZ'

/**
 * 创建天地图图层
 * @param {string} type - 图层类型：vec(矢量)、img(影像)、ter(地形)
 * @param {string} layer - 图层名称：底图或注记
 * @param {number} zIndex - 图层层级
 * 修复：使用HTTP协议与GeoServer保持一致，避免Mixed Content错误
 * Source: 参照旧项目 frontend/src/components/menu/OneMap.vue
 */
const createTiandituLayer = (type, layer, zIndex = 0) => {
  const token = import.meta.env.VITE_TIANDITU_TOKEN || '53e8dc8dc6768eae964528e059c5bca3'
  return new TileLayer({
    source: new XYZ({
      url: `http://t0.tianditu.gov.cn/DataServer?T=${type}_${layer}&x={x}&y={y}&l={z}&tk=${token}`,
      projection: 'EPSG:4326'
    }),
    zIndex
  })
}

/**
 * 地图初始化 Composable
 * @param {Object} options - 配置选项
 * @returns {Object} 地图实例和操作方法
 * Source: 参照旧项目 frontend/src/components/menu/OneMaps.vue
 */
export function useMap(options = {}) {
  const {
    center = [115.691846443, 30.128530098],  // 荆竹水库中心点（参照OneMaps.vue）
    zoom = 16,
    minZoom = 5,
    maxZoom = 18
  } = options

  // 地图实例
  const map = ref(null)
  const error = ref('')
  // 加载状态
  const loading = ref(true)

  // 底图图层
  const baseLayers = {
    vec: {
      base: createTiandituLayer('vec', 'c', 0),
      label: createTiandituLayer('cva', 'c', 1)
    },
    img: {
      base: createTiandituLayer('img', 'c', 0),
      label: createTiandituLayer('cia', 'c', 1)
    },
    ter: {
      base: createTiandituLayer('ter', 'c', 0),
      label: createTiandituLayer('cta', 'c', 1)
    }
  }

  // 当前底图类型（默认卫星图）
  const currentBaseMap = ref('img')

  /**
   * 初始化地图
   * @param {HTMLElement} container - 地图容器DOM元素
   */
  const initMap = (container) => {
    if (!container) {
      error.value = '地图容器未找到'
      loading.value = false
      return
    }

    try {
      // 创建地图实例
      map.value = new Map({
        target: container,
        view: new View({
          center,
          zoom,
          minZoom,
          maxZoom,
          projection: 'EPSG:4326'
        })
      })

      // 添加默认底图（卫星影像图）
      const baseLayer = baseLayers.img.base
      const labelLayer = baseLayers.img.label

      map.value.addLayer(baseLayer)
      map.value.addLayer(labelLayer)

      // 添加全屏控件
      map.value.addControl(new FullScreen())

      // 简化加载状态：地图实例创建完成即可交互
      // 瓦片加载是异步的，不阻塞地图功能
      loading.value = false
      console.log('地图实例初始化完成，瓦片加载中...')

    } catch (e) {
      console.error('地图初始化失败:', e)
      error.value = '地图初始化失败，请刷新页面重试'
      loading.value = false
    }
  }

  /**
   * 切换底图
   * @param {Object} mapInstance - 地图实例（从外部传入）
   * @param {string} type - 底图类型：vec、img、ter
   * 修复：改为接收map参数，避免闭包中map引用为null的问题
   */
  const changeBaseMap = (mapInstance, type) => {
    if (!mapInstance || !baseLayers[type]) {
      console.warn('[useMap] 切换底图失败：地图实例或底图类型无效', { mapInstance, type })
      return
    }

    // 移除当前底图
    const currentLayers = baseLayers[currentBaseMap.value]
    mapInstance.removeLayer(currentLayers.base)
    mapInstance.removeLayer(currentLayers.label)

    // 添加新底图
    const newLayers = baseLayers[type]
    mapInstance.addLayer(newLayers.base)
    mapInstance.addLayer(newLayers.label)

    currentBaseMap.value = type
    console.log(`[useMap] 底图已切换至: ${type}`)
  }

  /**
   * 销毁地图实例
   */
  const destroyMap = () => {
    if (map.value) {
      map.value.setTarget(null)
      map.value = null
    }
  }

  // 组件卸载时销毁地图
  onUnmounted(() => {
    destroyMap()
  })

  return {
    map,
    error,
    loading,
    currentBaseMap,
    initMap,
    changeBaseMap,
    destroyMap
  }
}
