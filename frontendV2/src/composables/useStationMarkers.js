/**
 * 测站标注管理 Composable
 * 功能：OpenLayers 测站点位标注、弹窗、显隐控制
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 参照旧项目 frontend/src/components/menu/OneMaps.vue
 * 修复：移除 fromLonLat 转换，直接使用 EPSG:4326 坐标
 * 优化：使用 Vue 组件替代 HTML 字符串拼接
 */

import { ref, reactive, watch, createApp } from 'vue'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Feature from 'ol/Feature'
import Point from 'ol/geom/Point'
import { Style, Icon } from 'ol/style'
import Overlay from 'ol/Overlay'
import { useStationData } from './useStationData'
import StationPopup from '@/components/business/map/StationPopup.vue'

/**
 * GNSS测站配置
 * Source: frontend/src/components/menu/OneMaps.vue
 */
const GNSS_STATIONS = [
  { name: 'LJ1-1', stationId: 33210, position: [115.692970645, 30.12990538], type: 'gnss' },
  { name: 'LJ1-2', stationId: 33214, position: [115.692507964, 30.129352846], type: 'gnss' },
  { name: 'LJ1-3', stationId: 33216, position: [115.691869598, 30.128584393], type: 'gnss' },
  { name: 'LJ1-4', stationId: 33212, position: [115.691246, 30.12784], type: 'gnss' },
  { name: 'LT2-1', stationId: 33215, position: [115.69324205, 30.129704684], type: 'gnss' },
  { name: 'LT2-2', stationId: 33211, position: [115.692781, 30.129157], type: 'gnss' },
  { name: 'LT2-3', stationId: 33217, position: [115.692131615, 30.12838906], type: 'gnss' },
  { name: 'LT2-4', stationId: 33213, position: [115.691513365791, 30.127631336], type: 'gnss' }
]

/**
 * 基准点配置
 */
const BENCHMARKS = [
  { name: '管理处基准点', position: [115.693889, 30.131389], type: 'benchmark' }
]

/**
 * 雨量水位站配置
 */
const RAIN_STATION = {
  name: '坝前雨量水位站',
  position: [115.693058, 30.129979],
  type: 'rain'
}

/**
 * 渗压测站配置（UPB系列）
 * Source: frontend/src/components/menu/OneMaps.vue mcuPressureStationsOnlyLine
 */
const SEEPAGE_STATIONS = [
  { name: 'UPR1-1', position: [115.693391, 30.130486], piezometerId: 'P0108118', type: 'upb' },
  { name: 'UPB1-1', position: [115.692855899, 30.129744369], piezometerId: 'P0108248', type: 'upb' },
  { name: 'UPB2-1', position: [115.692551468314, 30.1293836116865], piezometerId: 'P0108310', type: 'upb' },
  { name: 'UPA1-1', position: [115.692538058, 30.129391659], piezometerId: 'P0108190', type: 'upb' },
  { name: 'UPB3-1', position: [115.691938261, 30.128661055], piezometerId: 'P0108267', type: 'upb' },
  { name: 'UPB4-1', position: [115.691328058, 30.127916742], piezometerId: 'P0108174', type: 'upb' },
  { name: 'UPB4-4', position: [115.691895507, 30.127457935], piezometerId: 'P0108181', type: 'upb' },
  { name: 'UPB4-2', position: [115.691407345, 30.12786429], piezometerId: 'P0108273', type: 'upb' },
  { name: 'UPB4-3', position: [115.691585712, 30.127718109], piezometerId: 'P0108198', type: 'upb' },
  { name: 'UPB4-5', position: [115.692138247, 30.127284933], piezometerId: 'P0108056', type: 'upb' },
  { name: 'UPB3-2', position: [115.692014864906, 30.1286018975696], piezometerId: 'P0108282', type: 'upb' },
  { name: 'UPB3-4', position: [115.692505709, 30.12825321], piezometerId: 'P0108100', type: 'upb' },
  { name: 'UPB3-3', position: [115.692210666, 30.128486562], piezometerId: 'P0108033', type: 'upb' },
  { name: 'UPB2-2', position: [115.69261568, 30.129344869], piezometerId: 'P0108046', type: 'upb' },
  { name: 'UPA1-2', position: [115.692626409, 30.12933414], piezometerId: 'P0108050', type: 'upb' },
  { name: 'UPB2-3', position: [115.692808799, 30.129206735], piezometerId: 'P0108235', type: 'upb' },
  { name: 'UPA1-3', position: [115.692826233, 30.129193324], piezometerId: 'P0108242', type: 'upb' },
  { name: 'UPB2-4', position: [115.693127982, 30.128990818], piezometerId: 'P0108066', type: 'upb' },
  { name: 'UPA1-4', position: [115.693152122, 30.12896936], piezometerId: 'P0108345', type: 'upb' },
  { name: 'UPB2-5', position: [115.693342559, 30.128851343], piezometerId: 'P0108043', type: 'upb' },
  { name: 'UPA1-5', position: [115.693366698, 30.128827203], piezometerId: 'P0108154', type: 'upb' },
  { name: 'UPB1-5', position: [115.693602733, 30.12917589], piezometerId: 'P0108236', type: 'upb' },
  { name: 'UPB1-4', position: [115.693404249, 30.129312683], piezometerId: 'P0108173', type: 'upb' },
  { name: 'UPB1-3', position: [115.693117252955, 30.1295594459549], piezometerId: 'P0108376', type: 'upb' },
  { name: 'UPB1-2', position: [115.692937545, 30.12968551], piezometerId: 'P0108234', type: 'upb' },
  { name: 'UPR1-2', position: [115.693592003701, 30.1300932054631], piezometerId: 'P0108148', type: 'upb' },
  { name: 'UPB3-5', position: [115.692727826, 30.12812076], piezometerId: 'P0108377', type: 'upb' }
]

/**
 * 图标路径配置
 */
const ICON_PATHS = {
  gnss: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png', // 蓝色标记
  rain: '/icons/流量站点.png',
  seepage: '/icons/水厂.png',
  benchmark: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'
}

/**
 * 测站标注管理
 * @param {Ref} map - 地图实例引用
 * @returns {Object} 标注状态和操作方法
 */
export function useStationMarkers(map) {
  // 图层实例
  const gnssLayer = ref(null)
  const rainLayer = ref(null)
  const seepageLayer = ref(null)

  // 显示状态
  const visibility = reactive({
    gnss: true,
    rain: true,
    seepage: true
  })

  // 弹窗实例
  const popup = ref(null)
  const popupApp = ref(null)  // Vue 应用实例

  // 数据管理
  const stationData = useStationData()

  /**
   * 创建图标样式
   * @param {string} iconUrl - 图标URL
   * @param {number} scale - 缩放比例
   */
  const createIconStyle = (iconUrl, scale = 0.8) => {
    return new Style({
      image: new Icon({
        src: iconUrl,
        scale,
        anchor: [0.5, 1] // 底部中心对齐
      })
    })
  }

  /**
   * 创建测站 Feature
   * @param {Object} station - 测站配置
   * @param {string} iconUrl - 图标URL
   * 修复：直接使用 EPSG:4326 坐标，不进行投影转换
   */
  const createStationFeature = (station, iconUrl) => {
    const feature = new Feature({
      geometry: new Point(station.position), // 直接使用原始坐标（EPSG:4326）
      name: station.name,
      type: station.type,
      stationId: station.stationId,
      piezometerId: station.piezometerId,
      stationData: station
    })

    feature.setStyle(createIconStyle(iconUrl))
    return feature
  }

  /**
   * 创建 GNSS 测站图层
   */
  const createGnssLayer = () => {
    const features = [...GNSS_STATIONS, ...BENCHMARKS].map(station =>
      createStationFeature(station, ICON_PATHS.gnss)
    )

    const source = new VectorSource({ features })
    gnssLayer.value = new VectorLayer({
      source,
      zIndex: 100,
      visible: visibility.gnss
    })

    return gnssLayer.value
  }

  /**
   * 创建雨量水位站图层
   */
  const createRainLayer = () => {
    const feature = createStationFeature(RAIN_STATION, ICON_PATHS.rain)

    const source = new VectorSource({ features: [feature] })
    rainLayer.value = new VectorLayer({
      source,
      zIndex: 100,
      visible: visibility.rain
    })

    return rainLayer.value
  }

  /**
   * 创建渗压测站图层
   */
  const createSeepageLayer = () => {
    const features = SEEPAGE_STATIONS.map(station =>
      createStationFeature(station, ICON_PATHS.seepage)
    )

    const source = new VectorSource({ features })
    seepageLayer.value = new VectorLayer({
      source,
      zIndex: 100,
      visible: visibility.seepage
    })

    return seepageLayer.value
  }

  /**
   * 渲染 Vue 组件到弹窗
   * 优化：使用 Vue 组件替代 HTML 字符串拼接
   */
  const renderPopupComponent = (container, props) => {
    // 清理旧的应用实例
    if (popupApp.value) {
      popupApp.value.unmount()
      popupApp.value = null
    }
    
    // 创建新的 Vue 应用实例
    popupApp.value = createApp(StationPopup, props)
    popupApp.value.mount(container)
  }

  /**
   * 创建弹窗
   */
  const createPopup = (container) => {
    popup.value = new Overlay({
      element: container,
      positioning: 'bottom-center',
      stopEvent: false,
      offset: [0, -10]
    })

    if (map.value) {
      map.value.addOverlay(popup.value)
    }
  }

  /**
   * 显示弹窗（使用 Vue 组件）
   * 优化：传入组件 props 而非 HTML 字符串
   */
  const showPopup = (coordinate, station, data) => {
    if (!popup.value) return

    const container = popup.value.getElement()
    if (!container) return

    // 准备组件 props
    const props = {
      stationType: station.type,
      stationName: station.name,
      piezometerId: station.piezometerId || '',
      data: data
    }

    // 渲染 Vue 组件
    renderPopupComponent(container, props)
    
    // 设置弹窗位置
    popup.value.setPosition(coordinate)
  }

  /**
   * 隐藏弹窗
   */
  const hidePopup = () => {
    if (popup.value) {
      popup.value.setPosition(undefined)
    }
    
    // 清理 Vue 应用实例
    if (popupApp.value) {
      popupApp.value.unmount()
      popupApp.value = null
    }
  }

  /**
   * 处理地图点击事件
   * 优化：传递数据对象而非 HTML 字符串
   */
  const handleMapClick = (evt) => {
    if (!map.value) return

    const feature = map.value.forEachFeatureAtPixel(evt.pixel, (feature) => feature)

    if (feature) {
      const station = feature.get('stationData')
      const type = feature.get('type')
      let data = null

      // 根据测站类型获取数据
      if (type === 'gnss' || type === 'benchmark') {
        data = stationData.gnssData.value.find(
          d => d.stationId === station.stationId
        )
      } else if (type === 'rain') {
        // 合并水位和降雨量数据
        data = {
          waterLevel: stationData.waterLevelData.value?.waterLevel,
          rainfall: stationData.rainfallData.value?.rainfall,
          time: stationData.waterLevelData.value?.time || stationData.rainfallData.value?.time
        }
      } else if (type === 'upb') {
        data = stationData.seepageData.value.find(
          d => d.piezometerId === station.piezometerId
        )
      }

      showPopup(evt.coordinate, station, data)
    } else {
      hidePopup()
    }
  }

  /**
   * 初始化测站标注
   * 修复：添加详细日志，验证图层添加
   */
  const createStationMarkers = (popupContainer) => {
    if (!map.value) {
      console.warn('[测站标注] 地图实例未就绪')
      return
    }

    console.log('[测站标注] 开始初始化，地图实例:', map.value)

    // 创建图层
    const layers = [
      createGnssLayer(),
      createRainLayer(),
      createSeepageLayer()
    ]

    // 添加到地图
    layers.forEach((layer, index) => {
      if (layer) {
        map.value.addLayer(layer)
        const source = layer.getSource()
        const featureCount = source.getFeatures().length
        console.log(`[测站标注] 图层${index + 1}已添加，包含 ${featureCount} 个要素`)
      }
    })

    // 创建弹窗
    if (popupContainer) {
      createPopup(popupContainer)
      console.log('[测站标注] 弹窗已创建')
    }

    // 绑定点击事件
    map.value.on('click', handleMapClick)

    console.log('[测站标注] 初始化完成')
  }

  /**
   * 更新测站数据
   * 修复：传入渗压测站配置，批量加载所有测站数据
   * Source: 对齐旧项目 fetchLatestUpbStationData 行为
   */
  const updateStationData = async () => {
    console.log('[测站标注] 开始加载测站数据')
    
    // 传入渗压测站配置，批量加载
    await stationData.fetchAllStationData(SEEPAGE_STATIONS)
    
    console.log('[测站标注] 数据加载完成:', {
      gnss: stationData.gnssData.value.length,
      waterLevel: stationData.waterLevelData.value ? '有数据' : '无数据',
      rainfall: stationData.rainfallData.value ? '有数据' : '无数据',
      seepage: stationData.seepageData.value.length
    })
  }

  /**
   * 切换图层显示
   */
  const toggleLayer = (layerType, visible) => {
    visibility[layerType] = visible

    const layerMap = {
      gnss: gnssLayer.value,
      rain: rainLayer.value,
      seepage: seepageLayer.value
    }

    const layer = layerMap[layerType]
    if (layer) {
      layer.setVisible(visible)
    }
  }

  /**
   * 清理资源
   */
  const cleanup = () => {
    if (map.value) {
      map.value.un('click', handleMapClick)

      if (gnssLayer.value) map.value.removeLayer(gnssLayer.value)
      if (rainLayer.value) map.value.removeLayer(rainLayer.value)
      if (seepageLayer.value) map.value.removeLayer(seepageLayer.value)
      if (popup.value) map.value.removeOverlay(popup.value)
    }
    
    // 清理 Vue 应用实例
    if (popupApp.value) {
      popupApp.value.unmount()
      popupApp.value = null
    }
  }

  return {
    visibility,
    createStationMarkers,
    updateStationData,
    toggleLayer,
    hidePopup,
    cleanup
  }
}
