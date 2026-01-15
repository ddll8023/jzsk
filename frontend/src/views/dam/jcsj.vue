<template>
  <div class="jcsj-container">
    <div class="jcsj-top-section">
      <div class="jcsj-top-left">
        <div class="station-selector">
          <span>站点选择：</span>
          <el-select v-model="selectedStationId" placeholder="请选择站点" @change="fetchData">
            <el-option v-for="station in stationOptions" :key="station.stationId" :label="station.name" :value="station.stationId"></el-option>
          </el-select>
        </div>
        <div class="time-selector">
          <span>时间选择：</span>
          <el-date-picker v-model="dateRange" type="datetimerange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" @change="fetchData" />
          <el-button-group style="margin-left:10px">
            <el-button :type="quickType==='today'?'primary':'default'" @click="setQuick('today')">今天</el-button>
            <el-button :type="quickType==='yesterday'?'primary':'default'" @click="setQuick('yesterday')">昨天</el-button>
            <el-button :type="quickType==='last15'?'primary':'default'" @click="setQuick('last15')">近15天</el-button>
            <el-button :type="quickType==='month'?'primary':'default'" @click="setQuick('month')">本月</el-button>
          </el-button-group>
        </div>
        <div class="display-mode-group">
          <el-button-group>
            <el-button :type="displayMode==='chart'?'primary':'default'" @click="displayMode='chart'">图表</el-button>
            <el-button :type="displayMode==='table'?'primary':'default'" @click="displayMode='table'">表格</el-button>
          </el-button-group>
        </div>
        <div class="current-info">
          <span class="current-station-name">{{ stationName }}</span>
          <span class="current-collect-time">采集时间：{{ collectTime }}</span>
        </div>
      </div>
      <div class="jcsj-top-right">
        <el-button type="primary" size="small" @click="exportTable">导出</el-button>
      </div>
    </div>

    <div class="jcsj-cards-section">
      <div class="jcsj-card" v-for="item in attrs" :key="item.label">
        <div class="jcsj-card-label">{{ item.label }}</div>
        <div class="jcsj-card-value">{{ item.value }}</div>
        <div class="jcsj-card-unit">{{ item.unit }}</div>
      </div>
    </div>

    <div v-if="displayMode==='chart'" class="jcsj-chart-section">
      <div ref="lineChart" style="width: 100%; height: 380px;"></div>
      <div class="stat-table-section">
        <div class="table-header">
          <h3>均值统计</h3>
        </div>
        <el-table :data="statRows" border style="width: 100%">
          <el-table-column prop="attr" label="监测属性"></el-table-column>
          <el-table-column prop="mean" label="均值"></el-table-column>
          <el-table-column prop="max" label="最大值"></el-table-column>
          <el-table-column prop="min" label="最小值"></el-table-column>
          <el-table-column prop="std" label="标准差"></el-table-column>
        </el-table>
      </div>
    </div>
    <div v-else class="jcsj-table-section">
      <div class="table-header">
        <h3>监测数据</h3>
      </div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="collectTime" label="时间" width="180"></el-table-column>
        <el-table-column prop="stationName" label="站点名称" width="120"></el-table-column>
        <el-table-column prop="deviceSn" label="设备SN" width="120"></el-table-column>
        <el-table-column prop="gpsTotalX" label="X位移(mm)"></el-table-column>
        <el-table-column prop="gpsTotalY" label="Y位移(mm)"></el-table-column>
        <el-table-column prop="gpsTotalZ" label="Z位移(mm)"></el-table-column>
        <el-table-column prop="displacement3d" label="合位移(mm)"></el-table-column>
        <el-table-column prop="displacement2d" label="水平位移(mm)"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts';
import * as XLSX from 'xlsx';

function calcStats(arr) {
  if (!arr.length) return { mean: '-', max: '-', min: '-', std: '-' };
  const n = arr.length;
  const mean = arr.reduce((a, b) => a + b, 0) / n;
  const max = Math.max(...arr);
  const min = Math.min(...arr);
  const std = Math.sqrt(arr.reduce((a, b) => a + Math.pow(b - mean, 2), 0) / n);
  return {
    mean: mean.toFixed(4),
    max: max,
    min: min,
    std: std.toFixed(4)
  };
}

export default {
  name: 'Jcsj',
  data() {
    return {
      stationName: '',
      collectTime: '',
      attrs: [],
      chartData: [],
      selectedStationId: 33210,
      stationOptions: [
        { stationId: 33210, name: 'LJ1-1' },
        { stationId: 33214, name: 'LJ1-2' },
        { stationId: 33216, name: 'LJ1-3' },
        { stationId: 33212, name: 'LJ1-4' },
        { stationId: 33215, name: 'LT2-1' },
        { stationId: 33211, name: 'LT2-2' },
        { stationId: 33217, name: 'LT2-3' },
        { stationId: 33213, name: 'LT2-4' },
      ],
      tableData: [],
      timer: null,
      dateRange: [],
      quickType: 'today',
      displayMode: 'chart',
      statRows: []
    };
  },
  methods: {
    setQuick(type) {
      this.quickType = type;
      const now = new Date();
      let start, end;
      if (type === 'today') {
        start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        end = now;
      } else if (type === 'yesterday') {
        start = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1, 0, 0, 0);
        end = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1, 23, 59, 59);
      } else if (type === 'last15') {
        start = new Date(now.getTime() - 14 * 24 * 60 * 60 * 1000);
        end = now;
      } else if (type === 'month') {
        start = new Date(now.getFullYear(), now.getMonth(), 1, 0, 0, 0);
        end = now;
      }
      this.dateRange = [this.formatDate(start), this.formatDate(end)];
      this.fetchData();
    },
    formatDate(date) {
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const d = String(date.getDate()).padStart(2, '0');
      const h = String(date.getHours()).padStart(2, '0');
      const min = String(date.getMinutes()).padStart(2, '0');
      const s = String(date.getSeconds()).padStart(2, '0');
      return `${y}-${m}-${d} ${h}:${min}:${s}`;
    },
    async fetchData() {
      if (!this.dateRange || this.dateRange.length !== 2) return;
      const params = {
        startTime: this.dateRange[0],
        endTime: this.dateRange[1],
        sensor: 'L1_GP',
        stationIds: this.selectedStationId,
        projectId: 1681,
        page: 1,
        size: 1000
      };
      try {
        const res = await axios.get('/external-data/displacement-history', { params });
        const records = res.data.records || [];
        this.tableData = records.map(r => {
          const row = {
            collectTime: r.collectTime,
            stationName: r.stationName,
            deviceSn: r.deviceSn,
            gpsTotalX: null,
            gpsTotalY: null,
            gpsTotalZ: null,
            displacement3d: null,
            displacement2d: null
          };
          (r.keyValues || []).forEach(kv => {
            if (kv.key === 'gpsTotalX') row.gpsTotalX = parseFloat(kv.value);
            if (kv.key === 'gpsTotalY') row.gpsTotalY = parseFloat(kv.value);
            if (kv.key === 'gpsTotalZ') row.gpsTotalZ = parseFloat(kv.value);
            if (kv.key === 'displacement3d') row.displacement3d = parseFloat(kv.value);
            if (kv.key === 'displacement2d') row.displacement2d = parseFloat(kv.value);
          });
          return row;
        });
        const latest = this.tableData[0] || {};
        this.stationName = latest.stationName || '';
        this.collectTime = latest.collectTime || '';
        this.chartData = this.tableData.slice().reverse().map(d => ({
          time: d.collectTime,
          x: d.gpsTotalX,
          y: d.gpsTotalY,
          z: d.gpsTotalZ,
          d3: d.displacement3d,
          d2: d.displacement2d,
        }));
        this.calcStatsTable();
        if (this.quickType === 'today') {
          this.attrs = [
            { label: '最新X位移', value: (latest.gpsTotalX !== undefined && latest.gpsTotalX !== null) ? latest.gpsTotalX : '-', unit: 'mm' },
            { label: '最新Y位移', value: (latest.gpsTotalY !== undefined && latest.gpsTotalY !== null) ? latest.gpsTotalY : '-', unit: 'mm' },
            { label: '最新Z位移', value: (latest.gpsTotalZ !== undefined && latest.gpsTotalZ !== null) ? latest.gpsTotalZ : '-', unit: 'mm' },
            { label: '最新合位移', value: (latest.displacement3d !== undefined && latest.displacement3d !== null) ? latest.displacement3d : '-', unit: 'mm' },
            { label: '最新水平位移', value: (latest.displacement2d !== undefined && latest.displacement2d !== null) ? latest.displacement2d : '-', unit: 'mm' },
          ];
        } else {
          const stat = {};
          (this.statRows || []).forEach(row => {
            if (row.attr.indexOf('X位移') !== -1) stat.x = row.mean;
            if (row.attr.indexOf('Y位移') !== -1) stat.y = row.mean;
            if (row.attr.indexOf('Z位移') !== -1) stat.z = row.mean;
            if (row.attr.indexOf('合位移') !== -1) stat.d3 = row.mean;
            if (row.attr.indexOf('水平位移') !== -1) stat.d2 = row.mean;
          });
          this.attrs = [
            { label: '平均X位移', value: stat.x !== undefined ? Number(stat.x).toFixed(1) : '-', unit: 'mm' },
            { label: '平均Y位移', value: stat.y !== undefined ? Number(stat.y).toFixed(1) : '-', unit: 'mm' },
            { label: '平均Z位移', value: stat.z !== undefined ? Number(stat.z).toFixed(1) : '-', unit: 'mm' },
            { label: '平均合位移', value: stat.d3 !== undefined ? Number(stat.d3).toFixed(1) : '-', unit: 'mm' },
            { label: '平均水平位移', value: stat.d2 !== undefined ? Number(stat.d2).toFixed(1) : '-', unit: 'mm' },
          ];
        }
        if (this.displayMode === 'chart') this.$nextTick(this.renderLineChart);
      } catch (e) {
        this.stationName = '';
        this.collectTime = '';
        this.attrs = [];
        this.chartData = [];
        this.tableData = [];
        this.statRows = [];
        if (this.$refs.lineChart) {
          echarts.init(this.$refs.lineChart).clear();
        }
      }
    },
    calcStatsTable() {
      const arrX = this.tableData.map(d => d.gpsTotalX).filter(v => typeof v === 'number');
      const arrY = this.tableData.map(d => d.gpsTotalY).filter(v => typeof v === 'number');
      const arrZ = this.tableData.map(d => d.gpsTotalZ).filter(v => typeof v === 'number');
      const arr3d = this.tableData.map(d => d.displacement3d).filter(v => typeof v === 'number');
      const arr2d = this.tableData.map(d => d.displacement2d).filter(v => typeof v === 'number');
      this.statRows = [
        { attr: 'X位移(mm)', ...calcStats(arrX) },
        { attr: 'Y位移(mm)', ...calcStats(arrY) },
        { attr: 'Z位移(mm)', ...calcStats(arrZ) },
        { attr: '合位移(mm)', ...calcStats(arr3d) },
        { attr: '水平位移(mm)', ...calcStats(arr2d) },
      ];
    },
    renderLineChart() {
      const chart = echarts.init(this.$refs.lineChart);
      const data = this.chartData;
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: {
          data: ['X位移', 'Y位移', 'Z位移', '合位移', '水平位移'],
          top: '0',
          left: 'center',
          textStyle: { color: '#333' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.map(d => d.time),
          axisLabel: { color: '#666' },
          axisLine: { lineStyle: { color: '#ccc' } },
          splitLine: { show: false }
        },
        yAxis: {
          type: 'value',
          name: 'mm',
          axisLabel: { color: '#666' },
          axisLine: { lineStyle: { color: '#ccc' } },
          splitLine: { lineStyle: { type: 'dashed', color: '#eee' } }
        },
        series: [
          { name: 'X位移', type: 'line', data: data.map(d => d.x), smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { width: 2 }, itemStyle: { color: '#5470C6' } },
          { name: 'Y位移', type: 'line', data: data.map(d => d.y), smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { width: 2 }, itemStyle: { color: '#91CC75' } },
          { name: 'Z位移', type: 'line', data: data.map(d => d.z), smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { width: 2 }, itemStyle: { color: '#EE6666' } },
          { name: '合位移', type: 'line', data: data.map(d => d.d3), smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { width: 2 }, itemStyle: { color: '#FAC858' } },
          { name: '水平位移', type: 'line', data: data.map(d => d.d2), smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { width: 2 }, itemStyle: { color: '#73C0DE' } },
        ]
      });
    },
    exportTable() {
      const worksheet = XLSX.utils.json_to_sheet(this.tableData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, '监测数据');
      XLSX.writeFile(workbook, '监测数据.xlsx');
    },
    startAutoRefresh() {
      this.timer = setInterval(() => {
        this.fetchData();
      }, 60 * 1000);
    },
    stopAutoRefresh() {
      if (this.timer) clearInterval(this.timer);
    }
  },
  watch: {
    displayMode(val) {
      if (val === 'chart') {
        this.$nextTick(this.renderLineChart);
      }
    }
  },
  mounted() {
    this.setQuick('today');
    this.startAutoRefresh();
  },
  beforeDestroy() {
    this.stopAutoRefresh();
  }
};
</script>

<style scoped>
.jcsj-container {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px #e4e7ed22;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow: auto;
}

.jcsj-top-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.jcsj-top-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.station-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.station-selector span {
  font-size: 16px;
  color: #333;
}

.time-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.time-selector span {
  font-size: 16px;
  color: #333;
}

.display-mode-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.display-mode-group .el-button-group {
  display: flex;
  align-items: center;
  gap: 5px;
}

.current-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.current-station-name {
  font-size: 20px;
  font-weight: bold;
  color: #2b4b6b;
}

.current-collect-time {
  font-size: 14px;
  color: #666;
}

.jcsj-cards-section {
  display: flex;
  gap: 18px;
  flex-wrap: wrap;
  justify-content: space-between;
}

.jcsj-card {
  background: #f5f8ff;
  border-radius: 8px;
  padding: 18px 28px;
  box-shadow: 0 1px 4px #e4e7ed22;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 120px;
  flex: 1;
  box-sizing: border-box;
}

.jcsj-card-label {
  font-size: 15px;
  color: #888;
  margin-bottom: 6px;
}

.jcsj-card-value {
  font-size: 22px;
  font-weight: bold;
  color: #2b4b6b;
}

.jcsj-card-unit {
  font-size: 13px;
  color: #aaa;
}

.jcsj-chart-section {
  background: #f5f8ff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 4px #e4e7ed22;
  flex: 1;
  min-height: 380px;
}

.jcsj-table-section {
  background: #f5f8ff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 4px #e4e7ed22;
  flex: 1;
}

.jcsj-table-section .table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.jcsj-table-section h3 {
  font-size: 20px;
  color: #2b4b6b;
  margin: 0;
}

.stat-table-section {
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 4px #e4e7ed22;
}
</style>
