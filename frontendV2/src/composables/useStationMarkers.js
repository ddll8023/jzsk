/**
 * 测站标注管理 Composable
 * 功能：OpenLayers 测站点位标注、弹窗、显隐控制
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 参照旧项目 frontend/src/components/menu/OneMaps.vue
 * 修复：移除 fromLonLat 转换，直接使用 EPSG:4326 坐标
 * 优化：使用 Vue 组件替代 HTML 字符串拼接
 * 修复：正确映射后端返回的字段名（z1/drp/tm）
 */

import { ref, reactive, watch, createApp } from 'vue'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Feature from 'ol/Feature'
import Point from 'ol/geom/Point'
import { Style, Icon } from 'ol/style'
import Overlay from 'ol/Overlay'
import { useStationData } from './useStationData'
import { useStationStatus, STATION_STATUS } from './useStationStatus'
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
 * 渗压测站配置（UPB/UPR系列）
 * Source: frontend/src/components/menu/OneMaps.vue mcuPressureStationsOnlyLine
 */
const SEEPAGE_STATIONS = [
  { name: 'UPR1-1', position: [115.693391, 30.130486], piezometerId: 'P0108118', type: 'upb' },
  { name: 'UPB1-1', position: [115.692855899, 30.129744369], piezometerId: 'P0108248', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPB2-1', position: [115.692551468314, 30.1293836116865], piezometerId: 'P0108310', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPA1-1', position: [115.692538058, 30.129391659], piezometerId: 'P0108190', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPB3-1', position: [115.691938261, 30.128661055], piezometerId: 'P0108267', type: 'upb' },
  { name: 'UPB4-1', position: [115.691328058, 30.127916742], piezometerId: 'P0108174', type: 'upb' },
  { name: 'UPB4-4', position: [115.691895507, 30.127457935], piezometerId: 'P0108181', type: 'upb' },
  { name: 'UPB4-2', position: [115.691407345, 30.12786429], piezometerId: 'P0108273', type: 'upb' },
  { name: 'UPB4-3', position: [115.691585712, 30.127718109], piezometerId: 'P0108198', type: 'upb' },
  { name: 'UPB4-5', position: [115.692138247, 30.127284933], piezometerId: 'P0108056', type: 'upb' },
  { name: 'UPB3-2', position: [115.692014864906, 30.1286018975696], piezometerId: 'P0108282', type: 'upb' },
  { name: 'UPB3-4', position: [115.692505709, 30.12825321], piezometerId: 'P0108100', type: 'upb' },
  { name: 'UPB3-3', position: [115.692210666, 30.128486562], piezometerId: 'P0108033', type: 'upb' },
  { name: 'UPB2-2', position: [115.69261568, 30.129344869], piezometerId: 'P0108046', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPA1-2', position: [115.692626409, 30.12933414], piezometerId: 'P0108050', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPB2-3', position: [115.692808799, 30.129206735], piezometerId: 'P0108235', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPA1-3', position: [115.692826233, 30.129193324], piezometerId: 'P0108242', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPB2-4', position: [115.693127982, 30.128990818], piezometerId: 'P0108066', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPA1-4', position: [115.693152122, 30.12896936], piezometerId: 'P0108345', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPB2-5', position: [115.693342559, 30.128851343], piezometerId: 'P0108043', type: 'upb', offset: [-0.000055, 0.000055] },
  { name: 'UPA1-5', position: [115.693366698, 30.128827203], piezometerId: 'P0108154', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPB1-5', position: [115.693602733, 30.12917589], piezometerId: 'P0108236', type: 'upb' },
  { name: 'UPB1-4', position: [115.693404249, 30.129312683], piezometerId: 'P0108173', type: 'upb' },
  { name: 'UPB1-3', position: [115.693117252955, 30.1295594459549], piezometerId: 'P0108376', type: 'upb' },
  { name: 'UPB1-2', position: [115.692937545, 30.12968551], piezometerId: 'P0108234', type: 'upb', offset: [0.000055, -0.000055] },
  { name: 'UPR1-2', position: [115.693592003701, 30.1300932054631], piezometerId: 'P0108148', type: 'upb' },
  { name: 'UPR2-1', position: [115.693726, 30.129831], piezometerId: 'P0108206', type: 'upb' },
  { name: 'UPR2-2', position: [115.69386, 30.129569], piezometerId: 'P0108311', type: 'upb' },
  { name: 'UPB3-5', position: [115.692727826, 30.12812076], piezometerId: 'P0108377', type: 'upb' }
]

/**
 * 图标路径配置
 * 已到报：正常颜色
 * 未到报：使用 CSS filter 实现灰度效果（所有类型统一用灰度）
 */
const ICON_PATHS = {
  gnss: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png', // 蓝色标记（已到报）
  gnssOffline: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png', // 蓝色标记（未到报用灰度滤镜）
  rain: '/icons/流量站点.png',
  rainOffline: '/icons/流量站点.png', // 使用CSS滤镜实现灰度
  seepage: '/icons/水厂.png',
  seepageOffline: '/icons/水厂.png',   // 使用CSS滤镜实现灰度
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
  const popupApp = ref(null) // Vue 应用实例

  // 数据管理
  const stationData = useStationData()

  // 状态管理（已到报/未到报）
  const stationStatus = useStationStatus()

  /**
   * 创建图标样式
   * @param {string} iconUrl - 图标URL
   * @param {number} scale - 缩放比例
   * @param {string} status - 状态：online/offline/hidden
   */
  const createIconStyle = (iconUrl, scale = 0.8, status = 'online') => {
    return new Style({
      image: new Icon({
        src: iconUrl,
        scale,
        anchor: [0.5, 1], // 底部中心对齐
        color: status === 'offline' || status === 'hidden' ? 'gray' : undefined,
        opacity: status === 'offline' || status === 'hidden' ? 0.5 : 1
      })
    })
  }

  /**
   * 获取测站当前状态（online/offline/hidden）
   * @param {Object} station - 测站配置
   * @returns {string} 状态
   */
  const getStationDisplayStatus = (station) => {
    // 基准点始终按已到报展示
    if (station.type === 'benchmark') return STATION_STATUS.ONLINE

    // 从已有状态中获取状态
    const status = stationStatus.getStationStatus(station.type, station.stationId || station.piezometerId)

    // 兼容旧隐藏状态，无数据时按未到报显示
    if (status.status === STATION_STATUS.HIDDEN) {
      return STATION_STATUS.OFFLINE
    }

    // 根据采集时间判断是否已到报
    const now = Date.now()
    const thresholds = {
      gnss: 61 * 60 * 1000,   // 61分钟
      rain: 6 * 60 * 1000,   // 6分钟
      upb: 11 * 60 * 1000     // 11分钟
    }
    const threshold = thresholds[station.type] || thresholds.gnss

    if (status.lastCollectTime) {
      const collectTime = new Date(status.lastCollectTime).getTime()
      return (now - collectTime) <= threshold ? STATION_STATUS.ONLINE : STATION_STATUS.OFFLINE
    }

    return STATION_STATUS.OFFLINE
  }

  /**
   * 获取测站对应的图标路径
   * @param {string} type - 测站类型
   * @param {string} status - 状态
   * @returns {string} 图标URL
   */
  const getIconPath = (type, status) => {
    const iconMap = {
      gnss: { online: ICON_PATHS.gnss, offline: ICON_PATHS.gnssOffline, hidden: ICON_PATHS.gnss },
      rain: { online: ICON_PATHS.rain, offline: ICON_PATHS.rainOffline, hidden: ICON_PATHS.rain },
      upb: { online: ICON_PATHS.seepage, offline: ICON_PATHS.seepageOffline, hidden: ICON_PATHS.seepage },
      benchmark: { online: ICON_PATHS.benchmark, offline: ICON_PATHS.benchmark, hidden: ICON_PATHS.benchmark }
    }
    return iconMap[type]?.[status] || ICON_PATHS.gnss
  }

  /**
   * 创建测站 Feature
   * @param {Object} station - 测站配置
   * 优化：创建时使用 loading 状态，数据加载后逐步更新
   */
  const createStationFeature = (station) => {
    // 创建时默认使用 loading 状态
    const status = getStationDisplayStatus(station)
    const iconUrl = getIconPath(station.type, status)

    const feature = new Feature({
      geometry: new Point(getDisplayPosition(station)), // 直接使用原始坐标（EPSG:4326），密集点增加轻微偏移
      name: station.name,
      type: station.type,
      stationId: station.stationId,
      piezometerId: station.piezometerId,
      stationData: station,
      status: status
    })

    feature.setStyle(createIconStyle(iconUrl, getIconScale(station.type), status))
    return feature
  }

  /**
   * 获取展示坐标，针对坐标密集点增加小偏移，避免图标完全重叠。
   * @param {Object} station - 测站配置
   * @returns {number[]} 展示坐标
   */
  const getDisplayPosition = (station) => {
    if (!station.offset) return station.position
    return [
      station.position[0] + station.offset[0],
      station.position[1] + station.offset[1]
    ]
  }

  /**
   * 获取图标缩放比例
   * @param {string} type - 测站类型
   * @returns {number} 缩放比例
   */
  const getIconScale = (type) => {
    return type === 'upb' ? 0.65 : 0.8
  }

  /**
   * 创建 GNSS 测站图层
   */
  const createGnssLayer = () => {
    const features = [...GNSS_STATIONS, ...BENCHMARKS].map(station =>
      createStationFeature(station)
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
    const feature = createStationFeature(RAIN_STATION)

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
      createStationFeature(station)
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
   * 修复：正确映射后端返回的字段名
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
        // 修复：正确映射字段名（useStationData 存储时已做映射 z1→waterLevel, drp→rainfall）
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

    const layers = [
      createGnssLayer(),
      createRainLayer(),
      createSeepageLayer()
    ]

    layers.forEach(layer => {
      if (layer) {
        map.value.addLayer(layer)
      }
    })

    if (popupContainer) {
      createPopup(popupContainer)
    }

    map.value.on('click', handleMapClick)
  }

  /**
   * 更新测站数据
   * 修复：传入渗压测站配置，批量加载所有测站数据
   * 优化：改为渐进式加载，收到数据后立即显示对应测站图标
   * Source: 对齐旧项目 fetchLatestUpbStationData 行为
   */
  const updateStationData = async ({ initial = false } = {}) => {
    // 首次加载时初始化占位状态；定时刷新保留旧状态，避免请求慢或失败导致在线图标变灰。
    if (initial) {
      stationStatus.initLoadingStatus()
      updateMarkerStyles()
    }

    // 2. 渐进式加载数据，每个请求完成后立即显示对应测站
    // GNSS 数据
    stationData.fetchGnssData().then(gnssData => {
      stationStatus.updateGnssStatus(gnssData)
      updateMarkerStyles()
    }).catch(err => {
      console.warn('[测站标注] GNSS数据加载失败:', err)
    })

    // 水位数据
    stationData.fetchWaterLevelData().then(data => {
      stationStatus.updateRainStatus(data, stationData.rainfallData.value)
      updateMarkerStyles()
    }).catch(err => {
      console.warn('[测站标注] 水位数据加载失败:', err)
    })

    // 雨量数据
    stationData.fetchRainfallData().then(data => {
      stationStatus.updateRainStatus(stationData.waterLevelData.value, data)
      updateMarkerStyles()
    }).catch(err => {
      console.warn('[测站标注] 雨量数据加载失败:', err)
    })

    // 渗压数据
    stationData.fetchSeepageData(SEEPAGE_STATIONS).then(seepageData => {
      stationStatus.updateSeepageStatus(seepageData)
      updateMarkerStyles()
    }).catch(err => {
      console.warn('[测站标注] 渗压数据加载失败:', err)
    })
  }

  /**
   * 更新所有测站图标的样式（根据状态）
   * online: 正常颜色和透明度
   * offline: 灰色 + 50%透明度
   * hidden: 兼容旧状态，按未到报样式显示
   */
  const updateMarkerStyles = () => {
    const updateLayerStyles = (layer, stationType) => {
      if (!layer) return

      const source = layer.getSource()
      if (!source) return

      source.getFeatures().forEach(feature => {
        const type = feature.get('type')
        if (type !== stationType) return

        let stationId = feature.get('stationId')
        const piezometerId = feature.get('piezometerId')
        const id = piezometerId || stationId

        // 获取当前状态
        const displayStatus = getStationDisplayStatus({
          type,
          stationId,
          piezometerId
        })

        // 获取对应的图标
        const iconUrl = getIconPath(type, displayStatus)

        // 更新 feature 的样式和状态
        feature.setStyle(createIconStyle(iconUrl, getIconScale(type), displayStatus))
        feature.set('status', displayStatus)
      })
    }

    // 更新 GNSS 图层
    updateLayerStyles(gnssLayer.value, 'gnss')
    // 更新雨量水位图层
    updateLayerStyles(rainLayer.value, 'rain')
    // 更新渗压测站图层
    updateLayerStyles(seepageLayer.value, 'upb')
  }

  /**
   * 启动定时刷新测站状态
   * 每分钟重新请求数据并更新样式
   */
  const startStatusRefresh = () => {
    stationStatus.startAutoRefresh(() => {
      updateStationData({ initial: false }).catch(err => {
        console.warn('[测站标注] 定时刷新数据失败:', err)
      })
    }, 60000) // 1分钟
  }

  /**
   * 停止定时刷新
   */
  const stopStatusRefresh = () => {
    stationStatus.stopAutoRefresh()
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
    // 停止定时刷新
    stopStatusRefresh()

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
    cleanup,
    startStatusRefresh,
    stopStatusRefresh,
    stationStatus
  }
}
