<template>
  <div class="waterlevelrain-panel">
    <div class="panel-title">荆竹水库水位/雨量过程线</div>
    <div ref="chart" style="width:100%;height:280px;"></div>
  </div>
</template>
<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  name: 'WaterLevelRainLine',
  data() {
    return {
      chart: null,
      xData: [],
      waterLevelData: [],
      rainfallData: []
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      // 假设接口返回为 [{reportTime: '2024-06-11 10:00', waterLevel: 53.2, rainfall: 2.1}, ...]
      const res = await axios.get('/rainfall-stations')
      const data = res.data || []
      this.xData = data.map(function(i) {
        if (i.reportTime && typeof i.reportTime === 'string' && i.reportTime.length >= 16) {
          return i.reportTime.slice(11, 16)
        } else {
          return i.reportTime
        }
      })
      this.waterLevelData = data.map(function(i) { return i.waterLevel })
      this.rainfallData = data.map(function(i) { return i.rainfall })
      this.renderChart()
    },
    renderChart() {
      if (!this.$refs.chart) return
      if (!this.chart) this.chart = echarts.init(this.$refs.chart)
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['水位', '雨量'], top: 10, textStyle: { color: '#00eaff', fontSize: 16 } },
        grid: { left: 40, right: 40, top: 50, bottom: 40 },
        xAxis: {
          type: 'category',
          data: this.xData,
          axisLabel: { color: '#00eaff', fontSize: 14 },
          axisLine: { lineStyle: { color: '#00eaff' } }
        },
        yAxis: [
          {
            type: 'value',
            name: '水位(m)',
            position: 'left',
            axisLabel: { color: '#00eaff', fontSize: 14 },
            splitLine: { lineStyle: { color: '#1a2a3a' } }
          },
          {
            type: 'value',
            name: '雨量(mm)',
            position: 'right',
            axisLabel: { color: '#ffd700', fontSize: 14 },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '水位',
            type: 'line',
            yAxisIndex: 0,
            data: this.waterLevelData,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: { color: '#00eaff', width: 3, shadowColor: '#00eaff', shadowBlur: 8 },
            itemStyle: { color: '#00eaff', borderColor: '#fff', borderWidth: 2 }
          },
          {
            name: '雨量',
            type: 'line',
            yAxisIndex: 1,
            data: this.rainfallData,
            symbol: 'rect',
            symbolSize: 8,
            lineStyle: { color: '#ffd700', width: 3, shadowColor: '#ffd700', shadowBlur: 8, type: 'dashed' },
            itemStyle: { color: '#ffd700', borderColor: '#fff', borderWidth: 2 }
          }
        ]
      })
    }
  }
}
</script>
<style scoped>
.waterlevelrain-panel {
  width: 100%;
  background: transparent;
  color: #fff;
  margin: 0 auto;
  padding: 0;
}
.panel-title {
  color: #00eaff;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
  text-shadow: 0 0 8px #00eaff;
  letter-spacing: 2px;
}
</style> 