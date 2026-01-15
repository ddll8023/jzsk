<template>
  <div class="bar-panel">
    <div ref="chart" style="width:100%;height:180px;"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'WaterLevelBar',
  props: { mock: { type: Boolean, default: false } },
  data() {
    return {
      mockData: [
        { name: '荆竹水库', value: 1200 },
        { name: '南湖水库', value: 980 },
        { name: '北湖水库', value: 860 },
        { name: '东湖水库', value: 1500 },
        { name: '西湖水库', value: 1100 }
      ]
    }
  },
  mounted() {
    this.renderChart()
  },
  methods: {
    renderChart() {
      const data = this.mockData
      if (!this.$refs.chart) return
      const chart = echarts.init(this.$refs.chart)
      chart.setOption({
        grid: { left: 30, right: 10, top: 30, bottom: 30 },
        xAxis: {
          type: 'category',
          data: data.map(i => i.name),
          axisLabel: { color: '#00eaff', fontSize: 14 },
          axisLine: { lineStyle: { color: '#00eaff' } }
        },
        yAxis: {
          type: 'value',
          name: '容量(万m³)',
          nameTextStyle: { color: '#00eaff', fontSize: 14 },
          axisLabel: { color: '#00eaff', fontSize: 14 },
          splitLine: { lineStyle: { color: '#1a2a3a' } }
        },
        series: [{
          type: 'bar',
          data: data.map(i => i.value),
          barWidth: 28,
          itemStyle: {
            color: {
              type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: '#00eaff' },
                { offset: 1, color: '#0a1a2a' }
              ]
            },
            shadowColor: '#00eaff', shadowBlur: 10
          }
        }]
      })
    }
  }
}
</script>
<style scoped>
.bar-panel { background: transparent; }
</style> 