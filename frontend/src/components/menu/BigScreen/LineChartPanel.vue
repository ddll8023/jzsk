<template>
  <div class="line-panel">
    <div ref="chart" style="width:100%;height:160px;"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'LineChartPanel',
  props: { mockType: { type: Number, default: 1 } },
  mounted() { this.renderChart() },
  methods: {
    renderChart() {
      const data1 = {
        x: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
        y: [120, 132, 101, 134, 90, 230, 210, 180, 160, 170, 150, 140]
      }
      const data2 = {
        x: Array.from({length: 30}, (_, i) => (i+1).toString()),
        y: Array.from({length: 30}, () => Math.floor(Math.random()*80+20))
      }
      const data = this.mockType === 2 ? data2 : data1
      const chart = echarts.init(this.$refs.chart)
      chart.setOption({
        grid: { left: 30, right: 10, top: 30, bottom: 30 },
        xAxis: {
          type: 'category',
          data: data.x,
          axisLabel: { color: '#00eaff', fontSize: 14 },
          axisLine: { lineStyle: { color: '#00eaff' } }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#00eaff', fontSize: 14 },
          splitLine: { lineStyle: { color: '#1a2a3a' } }
        },
        series: [{
          type: 'line',
          data: data.y,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: { color: '#00eaff', width: 3, shadowColor: '#00eaff', shadowBlur: 8 },
          itemStyle: { color: '#00eaff', borderColor: '#fff', borderWidth: 2 }
        }]
      })
    }
  }
}
</script>
<style scoped>
.line-panel { background: transparent; }
</style> 