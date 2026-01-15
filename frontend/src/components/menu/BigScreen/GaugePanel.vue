<template>
  <div class="gauge-panel">
    <div ref="chart" style="width:100%;height:160px;"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'GaugePanel',
  mounted() { this.renderChart() },
  methods: {
    renderChart() {
      const chart = echarts.init(this.$refs.chart)
      chart.setOption({
        series: [{
          type: 'gauge',
          startAngle: 200,
          endAngle: -20,
          min: 0,
          max: 100,
          progress: { show: true, width: 18 },
          axisLine: { lineStyle: { width: 18, color: [[1, '#00eaff']] } },
          pointer: { show: true, length: '70%', width: 6 },
          axisTick: { show: false },
          splitLine: { show: false },
          axisLabel: { show: false },
          detail: {
            valueAnimation: true,
            fontSize: 32,
            color: '#00eaff',
            offsetCenter: [0, '60%'],
            formatter: '{value}%'
          },
          data: [{ value: 86, name: '运行率' }],
          title: { show: true, offsetCenter: [0, '-30%'], color: '#fff', fontSize: 16 }
        }]
      })
    }
  }
}
</script>
<style scoped>
.gauge-panel { background: transparent; }
</style> 