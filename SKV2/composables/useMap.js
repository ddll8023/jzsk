/**
 * 地图初始化 Composable
 * 功能：封装天地图 H5 加载和 APP 原生地图配置
 */

import { ref } from 'vue'
import { TIANDITU_TOKEN, MAP_CONFIG, RESERVOIR_MARKER } from '@/config/map.js'

export function useMap() {
  const mapLoaded = ref(false)
  const mapError = ref('')

  // #ifdef H5
  function loadTiandituScript() {
    return new Promise((resolve, reject) => {
      if (window.T) {
        resolve(window.T)
        return
      }

      const script = document.createElement('script')
      script.src = `https://api.tianditu.gov.cn/api?v=4.0&tk=${TIANDITU_TOKEN}`
      script.onload = () => {
        if (window.T) {
          resolve(window.T)
        } else {
          reject(new Error('天地图 API 加载失败'))
        }
      }
      script.onerror = () => reject(new Error('天地图脚本加载失败'))
      document.head.appendChild(script)
    })
  }

  async function initH5Map(containerId) {
    try {
      const T = await loadTiandituScript()
      const map = new T.Map(containerId)
      map.centerAndZoom(
        new T.LngLat(MAP_CONFIG.center.longitude, MAP_CONFIG.center.latitude),
        MAP_CONFIG.zoom,
      )

      const marker = new T.Marker(
        new T.LngLat(RESERVOIR_MARKER.longitude, RESERVOIR_MARKER.latitude),
      )
      map.addOverLay(marker)

      const infoWindow = new T.InfoWindow()
      infoWindow.setContent(
        `<div style="padding:8px;">${RESERVOIR_MARKER.title}</div>`,
      )
      marker.addEventListener('click', () => marker.openInfoWindow(infoWindow))

      mapLoaded.value = true
      return map
    } catch (error) {
      mapError.value = error.message
      throw error
    }
  }
  // #endif

  // #ifdef APP-PLUS
  function getAppMapConfig() {
    return {
      latitude: MAP_CONFIG.center.latitude,
      longitude: MAP_CONFIG.center.longitude,
      scale: MAP_CONFIG.zoom,
      markers: [RESERVOIR_MARKER],
    }
  }
  // #endif

  return {
    mapLoaded,
    mapError,
    // #ifdef H5
    initH5Map,
    // #endif
    // #ifdef APP-PLUS
    getAppMapConfig,
    // #endif
  }
}
