/**
 * 地图图层管理 Composable
 * 功能：WMS图层创建、显隐控制、状态管理
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 基于旧项目 frontend/src/components/menu/OneMap.vue 重构
 * 修复：添加GeoServer连接检测和错误处理
 */

import { reactive, toRefs } from 'vue'
import { Tile as TileLayer } from 'ol/layer'
import TileWMS from 'ol/source/TileWMS'

/**
 * 图层配置
 */
const LAYER_CONFIG = {
  pumpStation: {
    name: 'szy:pump_station',
    label: '泵站'
  },
  groundWater: {
    name: 'szy:ground_source_water',
    label: '地下水'
  },
  flowSites: {
    name: 'szy:individual_flow_sites',
    label: '单独流量测站'
  },
  impoundment: {
    name: 'szy:impoundment',
    label: '蓄水池'
  },
  pressureSites: {
    name: 'szy:individual_pressure_sites',
    label: '单独压力测站'
  },
  inspection: {
    name: 'szy:inspection_records',
    label: '巡检'
  },
  reservoir: {
    name: 'szy:reservoir',
    label: '水库'
  },
  lines: {
    name: 'szy:lines',
    label: '管道'
  }
}

/**
 * 创建WMS图层
 * @param {string} layerName - GeoServer图层名称
 * @returns {TileLayer} OpenLayers图层实例
 * 修复：添加serverType参数，对齐旧项目配置
 * Source: 参照旧项目 frontend/src/components/menu/OneMap.vue
 */
const createWMSLayer = (layerName) => {
  const geoserverUrl = import.meta.env.VITE_GEOSERVER_URL || 'http://172.27.25.88:8083/geoserver/szy/wms'
  
  const source = new TileWMS({
    url: geoserverUrl,
    wrapX: false,
    params: {
      FORMAT: 'image/png',
      VERSION: '1.1.1',
      tiled: true,
      STYLES: '',
      LAYERS: layerName,
      exceptions: 'application/vnd.ogc.se_inimage',
      tilesOrigin: '73.33,3.51'
    },
    serverType: 'geoserver' // 添加GeoServer服务器类型标识
  })

  // 监听图层加载错误
  source.on('tileloaderror', (event) => {
    console.warn(`图层 ${layerName} 加载失败，可能是GeoServer服务不可用`, event)
  })

  return new TileLayer({
    source,
    zIndex: 9999
  })
}

/**
 * 地图图层管理 Composable
 * @param {Ref} map - 地图实例引用
 * @returns {Object} 图层状态和操作方法
 */
export function useMapLayers(map) {
  // 图层显示状态
  const layerStates = reactive({
    pumpStation: false,
    groundWater: false,
    flowSites: false,
    impoundment: false,
    pressureSites: false,
    inspection: false,
    reservoir: false,
    lines: false
  })

  // 图层实例缓存
  const layerInstances = {}

  /**
   * 切换图层显示/隐藏
   * @param {string} layerKey - 图层键名
   * @param {boolean} visible - 是否显示
   */
  const toggleLayer = (layerKey, visible) => {
    if (!map.value) return

    const config = LAYER_CONFIG[layerKey]
    if (!config) {
      console.warn(`未找到图层配置: ${layerKey}`)
      return
    }

    if (visible) {
      // 创建并添加图层
      if (!layerInstances[layerKey]) {
        layerInstances[layerKey] = createWMSLayer(config.name)
      }
      map.value.addLayer(layerInstances[layerKey])
    } else {
      // 移除图层
      if (layerInstances[layerKey]) {
        map.value.removeLayer(layerInstances[layerKey])
      }
    }

    layerStates[layerKey] = visible
  }

  /**
   * 获取图层配置列表
   * @returns {Array} 图层配置数组
   */
  const getLayerConfigs = () => {
    return Object.entries(LAYER_CONFIG).map(([key, config]) => ({
      key,
      ...config,
      visible: layerStates[key]
    }))
  }

  /**
   * 清除所有图层
   */
  const clearAllLayers = () => {
    if (!map.value) return

    Object.keys(layerInstances).forEach(key => {
      if (layerInstances[key]) {
        map.value.removeLayer(layerInstances[key])
        layerStates[key] = false
      }
    })
  }

  return {
    ...toRefs(layerStates),
    toggleLayer,
    getLayerConfigs,
    clearAllLayers,
    LAYER_CONFIG
  }
}
