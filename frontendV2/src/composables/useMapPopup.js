/**
 * 地图弹窗管理 Composable
 * 功能：Overlay弹窗创建（坐标显示功能已禁用）
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 基于旧项目 frontend/src/components/menu/OneMap.vue 重构
 * 修复：移除坐标弹窗功能，避免与测站弹窗冲突
 */

import { ref, onUnmounted } from 'vue'
import Overlay from 'ol/Overlay'
import { toStringHDMS } from 'ol/coordinate'
import { toLonLat } from 'ol/proj'

/**
 * 地图弹窗管理 Composable
 * @param {Ref} map - 地图实例引用
 * @returns {Object} 弹窗实例和操作方法
 */
export function useMapPopup(map) {
  // 弹窗实例
  const overlay = ref(null)
  
  // 弹窗内容
  const popupContent = ref('')
  
  // 弹窗是否显示
  const popupVisible = ref(false)

  /**
   * 初始化弹窗
   * @param {HTMLElement} container - 弹窗容器DOM元素
   * 修复：移除坐标弹窗功能（遵循YAGNI原则，避免与测站弹窗冲突）
   */
  const initPopup = (container) => {
    if (!map.value || !container) return

    // 创建Overlay（保留实例供未来扩展）
    overlay.value = new Overlay({
      element: container,
      autoPan: true,
      autoPanAnimation: {
        duration: 250
      }
    })

    // 添加到地图
    map.value.addOverlay(overlay.value)

    // 移除地图点击事件监听，避免与测站弹窗冲突
    // map.value.on('singleclick', handleMapClick)
  }

  /**
   * 处理地图点击事件
   * @param {Event} e - 点击事件对象
   */
  const handleMapClick = (e) => {
    const coordinate = e.coordinate
    const hdms = toStringHDMS(toLonLat(coordinate))

    // 构建弹窗内容
    popupContent.value = `
      <div class="text-sm">
        <p class="font-medium text-gray-900 mb-2">点击位置信息</p>
        <div class="space-y-1">
          <div class="flex items-start">
            <span class="text-gray-500 w-16 shrink-0">经纬度:</span>
            <code class="text-xs bg-gray-100 px-2 py-1 rounded">${hdms}</code>
          </div>
          <div class="flex items-start">
            <span class="text-gray-500 w-16 shrink-0">坐标:</span>
            <div class="text-xs">
              <div>X: ${coordinate[0].toFixed(6)}</div>
              <div>Y: ${coordinate[1].toFixed(6)}</div>
            </div>
          </div>
        </div>
      </div>
    `

    // 显示弹窗
    showPopup(coordinate)
  }

  /**
   * 显示弹窗
   * @param {Array} coordinate - 坐标位置
   */
  const showPopup = (coordinate) => {
    if (overlay.value) {
      overlay.value.setPosition(coordinate)
      popupVisible.value = true
    }
  }

  /**
   * 隐藏弹窗
   */
  const hidePopup = () => {
    if (overlay.value) {
      overlay.value.setPosition(undefined)
      popupVisible.value = false
    }
  }

  /**
   * 销毁弹窗
   */
  const destroyPopup = () => {
    if (map.value && overlay.value) {
      map.value.un('singleclick', handleMapClick)
      map.value.removeOverlay(overlay.value)
      overlay.value = null
    }
  }

  // 组件卸载时销毁弹窗
  onUnmounted(() => {
    destroyPopup()
  })

  return {
    overlay,
    popupContent,
    popupVisible,
    initPopup,
    showPopup,
    hidePopup,
    destroyPopup
  }
}
