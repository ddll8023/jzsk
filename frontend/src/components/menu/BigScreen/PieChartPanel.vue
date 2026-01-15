<template>
  <div class="pie-panel">
    <div ref="chart" style="width:100%;height:160px;"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'PieChartPanel',
  props: { mockType: { type: Number, default: 1 } },
  mounted() { this.renderChart() },
  methods: {
    renderChart() {
      const data1 = [
        { value: 40, name: '大型' },
        { value: 30, name: '中型' },
        { value: 20, name: '小型' },
        { value: 10, name: '微型' }
      ]
      const data2 = [
        { value: 50, name: '荆州' },
        { value: 30, name: '武汉' },
        { value: 20, name: '宜昌' },
        { value: 10, name: '襄阳' }
      ]
      const data = this.mockType === 2 ? data2 : data1
      const chart = echarts.init(this.$refs.chart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { color: '#00eaff' } },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: { show: false },
          labelLine: { show: false },
          data,
          itemStyle: {
            borderColor: '#0a1a2a', borderWidth: 2,
            shadowColor: '#00eaff', shadowBlur: 10
          }
        }]
      })
    }
  }
}
</script>
<style scoped>
.pie-panel { background: transparent; }
</style> 