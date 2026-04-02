<template>
  <div class="station-popup">
    <!-- GNSS测站 -->
    <template v-if="stationType === 'gnss' || stationType === 'benchmark'">
      <div class="popup-title">{{ stationName }}</div>
      <template v-if="stationType === 'gnss' && data">
        <div v-if="data.displacement3d" class="popup-item success">
          合位移: {{ formatValue(data.displacement3d) }} mm
        </div>
        <div v-if="data.gpsTotalZ" class="popup-item primary">
          Z位移: {{ formatValue(data.gpsTotalZ) }} mm
        </div>
        <div v-if="data.gpsTotalX" class="popup-item">
          X位移: {{ formatValue(data.gpsTotalX) }} mm
        </div>
        <div v-if="data.gpsTotalY" class="popup-item">
          Y位移: {{ formatValue(data.gpsTotalY) }} mm
        </div>
        <div v-if="data.collectTime" class="popup-time">
          时间: {{ formatTime(data.collectTime) }}
        </div>
        <div v-if="!hasData" class="popup-empty">暂无监测数据</div>
      </template>
      <template v-else-if="stationType === 'benchmark'">
        <div class="popup-item">类型：基准点</div>
      </template>
    </template>

    <!-- 雨量水位站 -->
    <template v-else-if="stationType === 'rain'">
      <div class="popup-title">{{ stationName }}</div>
      <div v-if="data?.waterLevel" class="popup-item success">
        水位: {{ formatValue(data.waterLevel) }} m
      </div>
      <div v-if="data?.rainfall" class="popup-item primary">
        降雨量: {{ formatValue(data.rainfall) }} mm
      </div>
      <div v-if="data?.time" class="popup-time">
        时间: {{ formatTime(data.time) }}
      </div>
      <div v-if="!hasData" class="popup-empty">暂无监测数据</div>
    </template>

    <!-- 渗压测站 (UPB) -->
    <template v-else-if="stationType === 'upb'">
      <div class="popup-title">{{ stationName }}</div>
      <div v-if="piezometerId" class="popup-subtitle">
        渗压计: {{ piezometerId }}
      </div>
      <div v-if="data?.waterLevelElevation != null" class="popup-item success">
        水位高程: {{ formatValue(data.waterLevelElevation, 2) }} m
      </div>
      <div v-if="data?.pressure != null" class="popup-item primary">
        水压: {{ formatValue(data.pressure, 2) }} MPa
      </div>
      <div v-if="data?.temperature != null" class="popup-item">
        温度: {{ formatValue(data.temperature, 2) }} °C
      </div>
      <div v-if="data?.time" class="popup-time">
        时间: {{ formatTime(data.time) }}
      </div>
      <div v-if="!hasData" class="popup-empty">暂无监测数据</div>
    </template>
  </div>
</template>

<script setup>
/**
 * 测站弹窗组件
 * 功能：统一展示不同类型测站的监测数据
 * 遵循原则：KISS、YAGNI、SOLID（单一职责）
 * Source: 重构自 useStationMarkers.js 的 HTML 字符串
 */
import { computed } from 'vue'
import { formatMinute } from '@/utils/time'

const props = defineProps({
  stationType: {
    type: String,
    required: true,
    validator: (value) => ['gnss', 'rain', 'upb', 'benchmark'].includes(value)
  },
  stationName: {
    type: String,
    required: true
  },
  piezometerId: {
    type: String,
    default: ''
  },
  data: {
    type: Object,
    default: null
  }
})

/**
 * 判断是否有数据
 */
const hasData = computed(() => {
  if (!props.data) return false
  
  if (props.stationType === 'gnss') {
    return props.data.displacement3d || props.data.gpsTotalZ
  }
  if (props.stationType === 'rain') {
    return props.data.waterLevel || props.data.rainfall
  }
  if (props.stationType === 'upb') {
    return props.data.waterLevelElevation != null || props.data.pressure != null
  }
  
  return false
})

/**
 * 格式化数值显示
 */
const formatValue = (value, decimals = 2) => {
  if (value == null) return '-'
  const num = Number(value)
  return isNaN(num) ? '-' : num.toFixed(decimals)
}

/**
 * 格式化时间显示
 * - GNSS、渗压测站：分钟个位数四舍五入（与 seepage 模块一致）
 * - 雨量水位站：直接显示原始时间
 */
const formatTime = (time) => {
  if (!time) return '暂无数据'

  // 雨量水位站不做四舍五入
  if (props.stationType === 'rain') {
    const d = new Date(String(time).replace(/-/g, '/'))
    if (isNaN(d.getTime())) return '暂无数据'
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const dd = String(d.getDate()).padStart(2, '0')
    const h = String(d.getHours()).padStart(2, '0')
    const min = String(d.getMinutes()).padStart(2, '0')
    return `${y}-${m}-${dd} ${h}:${min}`
  }

  return formatMinute(time) || '暂无数据'
}
</script>

<style scoped>
.station-popup {
  padding: 16px;
  min-width: 200px;
  max-width: 400px;
}

.popup-title {
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 8px;
  font-size: 15px;
}

.popup-subtitle {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.popup-item {
  font-size: 14px;
  color: #666;
  margin-bottom: 6px;
  line-height: 1.5;
}

.popup-item.success {
  color: #52c41a;
  font-weight: 500;
}

.popup-item.primary {
  color: #1890ff;
  font-weight: 500;
}

.popup-time {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.popup-empty {
  font-size: 14px;
  color: #ccc;
  font-style: italic;
}
</style>
