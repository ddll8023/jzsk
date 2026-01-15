<template>
  <div class="bigscreen-root">
    <!-- 全屏地图底图作为背景 -->
    <div class="bigscreen-bgmap">
      <one-maps :simple="true" class="fullscreen-map-bg" />
    </div>
    <!-- 顶部标题栏 -->
    <div class="bigscreen-header">
      <div class="header-left">
        <img :src="require('@/assets/img/sea2.jpg')" class="header-logo" />
        <span class="header-title">智慧荆竹水库管理平台 可视化大屏</span>
      </div>
      <div class="header-center">
        <span class="header-time">{{ nowTime }}</span>
      </div>
    </div>
    <!-- 顶部数字卡片区 -->
    <div class="bigscreen-stats">
      <div class="stat-card"><div class="stat-value">125,811</div><div class="stat-label">总监测人数</div></div>
      <div class="stat-card"><div class="stat-value">10,456</div><div class="stat-label">实时监测数</div></div>
      <div class="stat-card"><div class="stat-value">3</div><div class="stat-label">水库数量</div></div>
      <div class="stat-card"><div class="stat-value">99.9%</div><div class="stat-label">设备在线率</div></div>
    </div>
    <div class="bigscreen-main-overlay">
      <!-- 左栏卡片 -->
      <div class="main-side main-side-left">
        <div class="card-panel">
          <div class="panel-title">水库容量统计</div>
          <water-level-bar :mock="true" />
        </div>
        <div class="card-panel">
          <div class="panel-title">水库类型分布</div>
          <pie-chart-panel />
        </div>
        <div class="card-panel">
          <div class="panel-title">年内水位变化</div>
          <line-chart-panel />
        </div>
      </div>
      <!-- 中间栏地图标题 -->
      <div class="main-center">
        <div class="center-title">荆竹水库工程分布图</div>
      </div>
      <!-- 右栏卡片 -->
      <div class="main-side main-side-right">
        <div class="card-panel">
          <div class="panel-title">设备运行仪表盘</div>
          <gauge-panel />
        </div>
        <div class="card-panel">
          <div class="panel-title">报警信息</div>
          <list-panel />
        </div>
        <div class="card-panel">
          <div class="panel-title">流量趋势</div>
          <line-chart-panel :mockType="2" />
        </div>
        <div class="card-panel">
          <div class="panel-title">区域分布</div>
          <pie-chart-panel :mockType="2" />
        </div>
      </div>
    </div>
    <!-- 底部水位雨量过程线 -->
    <div class="bottom-waterlevelrain">
      <div class="card-panel waterlevelrain-card">
        <water-level-rain-line style="width: 1100px; height: 320px;" />
      </div>
    </div>
  </div>
</template>
<script>
import OneMaps from '../menu/OneMaps.vue'
import WaterLevelBar from './BigScreen/WaterLevelBar.vue'
import PieChartPanel from './BigScreen/PieChartPanel.vue'
import LineChartPanel from './BigScreen/LineChartPanel.vue'
import GaugePanel from './BigScreen/GaugePanel.vue'
import ListPanel from './BigScreen/ListPanel.vue'
import WaterLevelRainLine from './BigScreen/WaterLevelRainLine.vue'
export default {
  name: 'BigScreen',
  components: {
    OneMaps,
    WaterLevelBar,
    PieChartPanel,
    LineChartPanel,
    GaugePanel,
    ListPanel,
    WaterLevelRainLine
  },
  data() {
    return {
      nowTime: '',
      username: 'admin01',
      projectImgs: [
        require('@/assets/img/sea.jpg'),
        require('@/assets/img/sea2.jpg'),
        require('@/assets/img/jingzhu1.png')
      ]
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.enterFullscreen()
    window.addEventListener('keydown', this.handleEsc)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    window.removeEventListener('keydown', this.handleEsc)
    this.exitFullscreen()
  },
  methods: {
    updateTime() {
      const now = new Date()
      const y = now.getFullYear()
      const m = (now.getMonth() + 1).toString().padStart(2, '0')
      const d = now.getDate().toString().padStart(2, '0')
      const h = now.getHours().toString().padStart(2, '0')
      const min = now.getMinutes().toString().padStart(2, '0')
      const s = now.getSeconds().toString().padStart(2, '0')
      this.nowTime = `${y}-${m}-${d} ${h}:${min}:${s}`
    },
    enterFullscreen() {
      const el = document.documentElement
      if (el.requestFullscreen) el.requestFullscreen()
      else if (el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      else if (el.mozRequestFullScreen) el.mozRequestFullScreen()
      else if (el.msRequestFullscreen) el.msRequestFullscreen()
    },
    exitFullscreen() {
      if (document.exitFullscreen) document.exitFullscreen()
      else if (document.webkitExitFullscreen) document.webkitExitFullscreen()
      else if (document.mozCancelFullScreen) document.mozCancelFullScreen()
      else if (document.msExitFullscreen) document.msExitFullscreen()
    },
    handleEsc(e) {
      if (e.key === 'Escape') {
        this.$router.push('/home/onemap')
      }
    }
  }
}
</script>
<style scoped>
.bigscreen-root {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
  font-family: 'DIN', '微软雅黑', Arial, sans-serif;
  min-width: 1440px;
  min-height: 900px;
}
.bigscreen-bgmap {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  width: 100vw;
  height: 100vh;
  z-index: 0;
}
.fullscreen-map-bg {
  width: 100vw !important;
  height: 100vh !important;
  position: absolute;
  left: 0; top: 0;
  z-index: 0;
}
.bigscreen-header,
.bigscreen-stats,
.bigscreen-main-overlay,
.bottom-waterlevelrain {
  position: relative;
  z-index: 2;
}
.bigscreen-header {
  height: 80px;
  background: linear-gradient(90deg, #0a1a2a 60%, #005fa3 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 60px;
  color: #fff;
  font-size: 28px;
  box-shadow: 0 4px 24px #00eaff33;
  letter-spacing: 4px;
  border-bottom: 3px solid #00eaff88;
}
.header-left { display: flex; align-items: center; }
.header-logo { width: 60px; height: 60px; border-radius: 12px; margin-right: 24px; box-shadow: 0 0 18px #00eaff77; }
.header-title { font-size: 36px; font-weight: bold; color: #00eaff; text-shadow: 0 0 18px #00eaff, 0 0 2px #fff; letter-spacing: 8px; }
.header-center { flex: 1; text-align: center; font-size: 24px; color: #ffd700; font-weight: bold; text-shadow: 0 0 8px #ffd700; }
.bigscreen-stats {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 60px;
  margin: 24px 0 0 0;
}
.stat-card {
  background: linear-gradient(135deg, #0a1a2a 60%, #005fa3 100%);
  border: 3px solid #00eaffcc;
  border-radius: 18px;
  box-shadow: 0 0 32px #00eaff55, 0 4px 16px #2228;
  padding: 24px 48px 12px 48px;
  min-width: 180px;
  text-align: center;
  position: relative;
  transition: box-shadow 0.2s;
}
.stat-card:hover {
  box-shadow: 0 0 48px #00eaffcc, 0 8px 32px #222a;
}
.stat-value {
  font-size: 48px;
  font-weight: bold;
  color: #00eaff;
  text-shadow: 0 0 24px #00eaff, 0 0 4px #fff;
  letter-spacing: 4px;
}
.stat-label {
  font-size: 18px;
  color: #fff;
  margin-top: 8px;
  letter-spacing: 3px;
  font-weight: 600;
}
.bigscreen-main-overlay {
  flex: 1;
  display: flex;
  height: calc(100vh - 320px - 180px);
  gap: 24px;
  padding: 0 24px;
  position: relative;
  z-index: 2;
}
.main-side {
  width: 360px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 12px 0;
  justify-content: flex-start;
}
.card-panel {
  background: rgba(10,26,42,0.85);
  border-radius: 18px;
  box-shadow: 0 0 24px #00eaff55;
  border: 2px solid #00eaff55;
  padding: 18px 18px 10px 18px;
  margin-bottom: 12px;
}
.main-side-right { padding-left: 8px; }
.main-side-left { padding-right: 8px; }
.main-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 12px 0;
  min-width: 600px;
}
.center-title {
  color: #00eaff;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 16px;
  text-shadow: 0 0 16px #00eaff;
  letter-spacing: 4px;
}
.bottom-waterlevelrain {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  z-index: 10;
  pointer-events: none;
  padding-bottom: 18px;
}
.bottom-waterlevelrain > * {
  pointer-events: auto;
}
.waterlevelrain-card {
  background: rgba(10,26,42,0.92);
  border-radius: 18px;
  box-shadow: 0 0 24px #00eaff55;
  border: 2px solid #00eaff55;
  padding: 18px 18px 10px 18px;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
.panel-title {
  color: #00eaff;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 8px;
  text-shadow: 0 0 10px #00eaff;
  letter-spacing: 3px;
  border-left: 4px solid #00eaff;
  padding-left: 12px;
  margin-top: 8px;
}
</style> 