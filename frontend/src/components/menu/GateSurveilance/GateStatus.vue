<template>
  <div class="gate-status-container">
    <el-row>
      <!-- 筛选区 -->
      <el-col :span="24" class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="选择闸门">
            <el-select v-model="selectedGate" placeholder="请选择闸门" filterable style="width:180px" @change="handleGateChange">
              <el-option v-for="gate in gateList" :key="gate.code" :label="gate.name" :value="gate.code" />
          </el-select>
          </el-form-item>
          <el-form-item label="选择时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期时间"
              end-placeholder="结束日期时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              align="right"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
            <el-button type="success" icon="el-icon-download" @click="exportData">导出</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <!-- 实时状态+SVG -->
      <el-col :span="24" class="right-panel">
        <el-card shadow="hover" class="status-card">
          <div slot="header" class="status-title">闸门实时状态</div>
          <div class="gate-status-box">
            <div v-if="latestData">
              <div style="font-size:20px;font-weight:bold;">{{ currentGateName }}</div>
              <div style="font-size:14px;color:#666;margin:5px 0;">
                状态: <span :style="{color: getStatusColor(), fontWeight: 'bold'}">{{ getStatusText() }}</span>
                | 开度: <span style="color:#409EFF;font-weight:bold;">{{ displayKD }}%</span>
                | 水位: <span style="color:#0288d1;font-weight:bold;">{{ displayWaterPercent }}%</span>
              </div>
              <svg width="600" height="350" style="display:block;margin:30px auto;">
                <!-- 水池背景 -->
                <rect x="60" y="100" width="480" height="180" fill="#b3e5fc" rx="18" />
                <!-- 水面波浪（随开度动态变化） -->
                <path :d="wavePathD" stroke="#4FC3F7" stroke-width="3" fill="none" />
                <!-- 水位填充区域 -->
                <path :d="waterFillPath" fill="#4FC3F7" opacity="0.3" />
                <!-- 水位数值显示 -->
                <text x="80" :y="waterLevelY" font-size="14" fill="#0288d1" font-weight="bold">水位: {{ displayWaterPercent }}%</text>
                <!-- 闸门轨道 -->
                <rect x="220" y="60" width="20" height="220" fill="#bbb" rx="6" />
                <rect x="360" y="60" width="20" height="220" fill="#bbb" rx="6" />
                <!-- 闸门本体 -->
                <rect x="240" :y="gateYBig" width="120" :height="gateHeightBig" fill="#607d8b" rx="8" style="filter:url(#shadow)" />
                <!-- 闸门开度指示器 -->
                <rect x="245" :y="gateYBig - 5" width="110" height="8" fill="#ff9800" rx="4" />
                <text x="300" :y="gateYBig + 2" font-size="12" fill="#fff" text-anchor="middle" font-weight="bold">{{ displayKD }}%</text>
                <!-- 闸门刻度线（精细刻度，0-10% 区间密集） -->
                <g>
                  <line v-for="tick in scaleTicks" :key="'l'+tick.y" :x1="370" :y1="tick.y" :x2="380" :y2="tick.y" :stroke="tick.major ? '#666' : '#aaa'" :stroke-width="tick.major ? 2 : 1" />
                                     <text v-for="tick in scaleTicksWithLabel" :key="'t'+tick.y" x="385" :y="tick.y + 5" font-size="12" fill="#888">{{ tick.label }}</text>
                </g>
                <!-- 阴影滤镜 -->
                <defs>
                  <filter id="shadow" x="-20%" y="-20%" width="140%" height="140%">
                    <feDropShadow dx="0" dy="4" stdDeviation="4" flood-color="#222" flood-opacity="0.2"/>
                  </filter>
                </defs>
                                                  <!-- 开度文字 -->
                  <text x="300" y="295" font-size="20" fill="#222" text-anchor="middle">开度: {{ displayKD }}%</text>
                  <!-- 开度状态指示器 -->
                  <circle :cx="300" :cy="310" :r="15" :fill="getStatusColor()" />
                  <text x="300" y="315" font-size="12" fill="#fff" text-anchor="middle" font-weight="bold">{{ getStatusText() }}</text>
                  <!-- 时间显示 - 调整位置避免重叠 -->
                  <text x="80" y="40" font-size="14" fill="#0288d1">时间: {{ (latestData && (latestData.TM || latestData.tm)) || '' }}</text>
            </svg>
            </div>
            <div v-else style="color:#888;text-align:center;padding:40px;">
              <div style="font-size:16px;margin-bottom:10px;">暂无数据</div>
              <div style="font-size:12px;color:#999;">请检查网络连接或稍后重试</div>
              <el-button type="primary" size="small" @click="forceRefresh" style="margin-top:10px;">重新加载</el-button>
            </div>
          </div>
        </el-card>
        <!-- 表格区 -->
        <el-card shadow="hover" class="table-card">
          <div slot="header" class="table-title">
            闸门实时数据
            <el-button type="text" size="small" @click="forceRefresh" style="float: right; margin-top: -5px;">
              <i class="el-icon-refresh"></i> 刷新
            </el-button>
          </div>
          <el-table :data="realtimeData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
            <el-table-column v-for="col in tableColumns" :key="col.prop" :prop="col.prop" :label="fieldToLabel(col.prop)" align="center" />
          </el-table>
          <div v-if="!realtimeData.length" style="text-align: center; padding: 40px; color: #999;">
            <div>暂无数据</div>
            <div style="font-size: 12px; margin-top: 10px;">
              调试信息: allTableData.length = {{ allTableData.length }}, 
              filteredData.length = {{ filteredData.length }}, 
              realtimeData.length = {{ realtimeData.length }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  data() {
    return {
      gateList: [
        { code: 'dgq', name: '东干渠' },
        { code: 'dzdf', name: '电站蝶阀' },
        { code: 'qst', name: '取水塔' },
        { code: 'xgq', name: '西干渠' },
        { code: 'yhd', name: '溢洪道' }
      ],
      selectedGate: 'dgq',
      allTableData: [],
      pageSize: 10,
      currentPage: 1,
      tableData: [],
      tableColumns: [],
      latestData: null,
      searchForm: {
        dateRange: []
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一天',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      chart: null,
      refreshTimer: null
    }
  },
  computed: {
    currentGateName() {
      const g = this.gateList.find(g => g.code === this.selectedGate)
      return g ? g.name : ''
    },
    filteredData() {
      if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) {
        return this.allTableData;
      }
      const [startDateStr, endDateStr] = this.searchForm.dateRange;
      const startDate = new Date(startDateStr);
      const endDate = new Date(endDateStr);
      return this.allTableData.filter(item => {
        const itemTime = this.parseTime(item.TM);
        return itemTime >= startDate && itemTime <= endDate;
      });
    },
        pagedData() {
      const sortedData = [...this.filteredData].sort((a, b) => this.parseTime(b.TM || b.tm) - this.parseTime(a.TM || a.tm));
      const start = (this.currentPage - 1) * this.pageSize;
      return sortedData.slice(start, start + this.pageSize);
    },
    // 实时数据（仅最新一条）
    realtimeData() {
      if (!this.allTableData.length) return [];
      const sorted = [...this.allTableData].sort((a, b) => this.parseTime(b.TM || b.tm) - this.parseTime(a.TM || a.tm));
      return [sorted[0]];
    },
    latestKD() {
      if (!this.latestData) return 0;
      const d = this.latestData;
      // 根据不同闸门类型获取开度数据
      let kd = 0;
      if (this.selectedGate === 'dgq') {
        kd = d.KD || d.dgq_M1_KD || 0;
      } else if (this.selectedGate === 'qst') {
        kd = d.qst_M1_KD || d.qst_M2_KD || 0;
      } else if (this.selectedGate === 'dzdf') {
        // 电站蝶阀可能没有开度字段，使用液位或其他字段
        kd = d.dzdf_M1_KD || d.dzdf_M1_YW || 0;
      } else if (this.selectedGate === 'xgq') {
        kd = d.xgq_M1_KD || d.xgq_M2_KD || 0;
      } else if (this.selectedGate === 'yhd') {
        kd = d.yhd_M1_KD || d.yhd_M2_KD || d.yhd_M3_KD || 0;
      }
      return Number(kd) || 0;
    },
    // 显示用：统一两位小数
    displayKD() {
      return Number(this.latestKD).toFixed(2);
    },
    gateHeightBig() {
      // SVG闸门高度，最大180，最小36；dgq开度来自 dgq_M1_KD
      const kd = this.latestKD;
      return 180 - (kd / 100) * 144;
    },
    gateYBig() {
      // SVG闸门Y坐标
      return 100 + (180 - this.gateHeightBig);
    },
         filteredLatestData() {
      if (!this.latestData) return [];
      return Object.keys(this.latestData)
        .filter(key => key !== 'TM' && key !== 'tm')
        .map(key => ({ key, val: this.latestData[key] }));
    },
    // 动态刻度（0-10% 更密集，其余为大刻度）
    scaleTicks() {
      const ticks = [];
      // 水池可视区域 Y: 100 -> 280，高度 180
      const baseY = 100; const height = 180;
      // 0-10%：每 1% 一个刻度
      for (let p = 0; p <= 10; p += 1) {
        const y = baseY + height - (p / 100) * height;
        ticks.push({ y, major: p % 5 === 0, label: p % 5 === 0 ? `${p}%` : '' });
      }
      // 20% 到 100%：每 10% 一个刻度
      for (let p = 20; p <= 100; p += 10) {
        const y = baseY + height - (p / 100) * height;
        ticks.push({ y, major: true, label: `${p}%` });
      }
      return ticks;
    },
    scaleTicksWithLabel() {
      return this.scaleTicks.filter(t => t.label);
    },
    // 动态水位波浪路径，水位与开度成反比，仅用于视觉提示
    wavePathD() {
      const baseY = 100; const height = 180;
      const kd = this.latestKD; // 0-100
      const waterPercent = Math.max(0, Math.min(100, 100 - kd));
      const y = baseY + height - (waterPercent / 100) * height;
      // 生成简易波浪
      return `M60 ${y} Q90 ${y-10} 120 ${y} T180 ${y} T240 ${y} T300 ${y} T360 ${y} T420 ${y} T480 ${y} T540 ${y}`;
    },
    // 水位填充区域路径
    waterFillPath() {
      const baseY = 100; const height = 180;
      const kd = this.latestKD;
      const waterPercent = Math.max(0, Math.min(100, 100 - kd));
      const y = baseY + height - (waterPercent / 100) * height;
      return `M60 ${y} L540 ${y} L540 280 L60 280 Z`;
    },
    // 水位百分比
    waterLevelPercent() {
      const kd = this.latestKD;
      return Math.max(0, Math.min(100, 100 - kd));
    },
    displayWaterPercent() {
      return Number(this.waterLevelPercent).toFixed(2);
    },
    // 水位Y坐标
    waterLevelY() {
      const baseY = 100; const height = 180;
      const kd = this.latestKD;
      const waterPercent = Math.max(0, Math.min(100, 100 - kd));
      return baseY + height - (waterPercent / 100) * height - 10;
    }
  },
  mounted() {
    this.setQuickDateRange('最近一天');
    this.fetchGateData();
    // 每30秒自动刷新数据
    this.refreshTimer = setInterval(() => {
      this.fetchGateData();
    }, 30000);
  },
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
    }
  },
  methods: {
    setQuickDateRange(type) {
      const end = new Date();
      const start = new Date();
      if (type === '最近一天') {
        start.setTime(start.getTime() - 3600 * 1000 * 24);
      } else if (type === '最近一周') {
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      } else if (type === '最近一个月') {
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      }
      this.searchForm.dateRange = [
        this.formatDateForPicker(start),
        this.formatDateForPicker(end)
      ];
    },
    formatDateForPicker(date) {
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const d = String(date.getDate()).padStart(2, '0');
      const h = String(date.getHours()).padStart(2, '0');
      const min = String(date.getMinutes()).padStart(2, '0');
      const s = String(date.getSeconds()).padStart(2, '0');
      return `${y}-${m}-${d} ${h}:${min}:${s}`;
    },
    async fetchGateData() {
      if (!this.selectedGate) return
      const url = `/zkxt/${this.selectedGate}`
      console.log('获取闸门数据:', { selectedGate: this.selectedGate, url })
      try {
        const { data: res } = await axios.get(url)
        console.log('接口响应:', res)
        const records = Array.isArray(res) ? res : (res && res.records) || []
        console.log('解析后的记录:', records)
        this.allTableData = records
        this.currentPage = 1
      } catch (error) {
        console.error('获取闸门数据失败:', error)
        this.allTableData = []
        this.latestData = null
        this.tableColumns = []
        return
      }
       // 针对不同闸门按固定字段顺序渲染表头
       if (this.allTableData.length > 0) {
         const records = this.allTableData
         let keys
         const has = Object.keys(records[0])
         
         if (this.selectedGate === 'dgq') {
           // 东干渠固定顺序
           const fixed = ['TM','tm','dgq_M1_Ua','dgq_M1_Ub','dgq_M1_Uc','dgq_M1_Uab','dgq_M1_Ubc','dgq_M1_Uca','dgq_M1_Ia','dgq_M1_Ib','dgq_M1_Ic','dgq_M1_KD','dgq_M1_KDSD']
           keys = fixed.filter(k => has.includes(k))
         } else if (this.selectedGate === 'qst') {
           // 取水塔固定顺序
           const fixed = ['tm','qst_M1_Ua','qst_M1_Ub','qst_M1_Uc','qst_M1_Uab','qst_M1_Ubc','qst_M1_Uca','qst_M1_Ia','qst_M1_Ib','qst_M1_Ic','qst_M1_KD','qst_M1_KDSD','qst_M2_Ua','qst_M2_Ub','qst_M2_Uc','qst_M2_Uab','qst_M2_KD','qst_M2_KDSD','qst_M2_Ia','qst_M2_Ib','qst_M2_Ic']
           keys = fixed.filter(k => has.includes(k))
         } else if (this.selectedGate === 'dzdf') {
           // 电站蝶阀固定顺序
           const fixed = ['tm','dzdf_M1_Ua','dzdf_M1_Ub','dzdf_M1_Uc','dzdf_M1_Uab','dzdf_M1_Ubc','dzdf_M1_Uca','dzdf_M1_Ia','dzdf_M1_Ib','dzdf_M1_Ic','dzdf_M1_FIT','dzdf_M1_FIT_TOL','dzdf_M1_YW']
           keys = fixed.filter(k => has.includes(k))
         } else if (this.selectedGate === 'xgq') {
           // 西干渠固定顺序
           const fixed = ['tm','xgq_M1_Ua','xgq_M1_Ub','xgq_M1_Uc','xgq_M1_Uab','xgq_M1_Ubc','xgq_M1_Uca','xgq_M1_Ia','xgq_M1_Ib','xgq_M1_Ic','xgq_M1_KD','xgq_M1_KDSD','xgq_M2_Ua','xgq_M2_Ub','xgq_M2_Uc','xgq_M2_Uab','xgq_M2_KD','xgq_M2_KDSD','xgq_M2_Ia','xgq_M2_Ib','xgq_M2_Ic']
           keys = fixed.filter(k => has.includes(k))
         } else if (this.selectedGate === 'yhd') {
           // 溢洪道固定顺序
           const fixed = ['tm','yhd_M1_Ua','yhd_M1_Ub','yhd_M1_Uc','yhd_M1_Uab','yhd_M1_Ubc','yhd_M1_Uca','yhd_M1_Ia','yhd_M1_Ib','yhd_M1_Ic','yhd_M1_KD','yhd_M1_KDSD','yhd_M2_Ua','yhd_M2_Ub','yhd_M2_Uc','yhd_M2_Uab','yhd_M2_KD','yhd_M2_KDSD','yhd_M2_Ia','yhd_M2_Ib','yhd_M2_Ic','yhd_M3_Ua','yhd_M3_Ub','yhd_M3_Uc','yhd_M3_Uab','yhd_M3_Ubc','yhd_M3_Uca','yhd_M3_Ia','yhd_M3_Ib','yhd_M3_Ic','yhd_M3_KD','yhd_M3_KDSD']
           keys = fixed.filter(k => has.includes(k))
         } else {
           // 其他闸门保持动态
           keys = Object.keys(records[0])
         }
         
         // 把时间字段放到最左侧
         const timeKey = keys.find(k => k.toLowerCase() === 'tm')
         if (timeKey) {
           keys = [timeKey, ...keys.filter(k => k !== timeKey)]
         }
         
         this.tableColumns = keys.map(key => ({
           prop: key,
           label: this.fieldToLabel(key)
         }))
         this.latestData = records[0] // 默认最新一条为第一条
         console.log('表格列配置:', this.tableColumns)
         console.log('最新数据:', this.latestData)
       } else {
         this.tableColumns = []
         this.latestData = null
       }
       this.$nextTick(() => this.renderChart(this.filteredData));
    },
    handleGateChange() {
      this.fetchGateData();
    },
    // 强制刷新数据
    forceRefresh() {
      this.allTableData = [];
      this.latestData = null;
      this.tableColumns = [];
      this.fetchGateData();
    },
    handleSearch() {
      this.currentPage = 1;
      this.$nextTick(() => this.renderChart(this.filteredData));
    },
    exportData() {
      if (this.filteredData.length === 0) {
        this.$message.warning('没有数据可导出！');
        return;
      }
      const headers = this.tableColumns.map(col => col.label);
      const rows = this.filteredData.map(item => this.tableColumns.map(col => item[col.prop]));
      let csvContent = '\ufeff' + headers.join(',') + '\n';
      rows.forEach(row => {
        csvContent += row.map(e => `"${e}"`).join(',') + '\n';
      });
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement("a");
      if (link.download !== undefined) {
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", "闸门数据.csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
      }
      this.$message.success('数据已导出！');
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1;
    },
    handleCurrentChange(page) {
      this.currentPage = page;
    },
    parseTime(tm) {
      // 支持字符串/Date/时间数组
      if (Array.isArray(tm) && tm.length >= 5) {
        return new Date(tm[0], tm[1] - 1, tm[2], tm[3], tm[4], tm.length > 5 ? tm[5] : 0);
      }
      if (typeof tm === 'string') {
        return new Date(tm);
      }
      return new Date(tm);
    },
    renderChart(data) {
      if (!this.$refs.chart) return;
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart);
      }
      if (!data.length) {
        this.chart.clear();
        return;
      }
      // 取常见监控字段
      const chartData = [...data].sort((a, b) => this.parseTime(a.TM) - this.parseTime(b.TM));
      const times = chartData.map(item => {
        if (Array.isArray(item.TM) && item.TM.length >= 5) {
          const [y, m, d, h, min] = item.TM;
          return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`;
        }
        return item.TM;
      });
      // 动态检测字段
             // 兼容不同闸门字段命名，dgq 使用 dgq_M1_ 前缀
       const kd = chartData.map(i => Number(i.KD || i.dgq_M1_KD || 0));
       const ia = chartData.map(i => Number(i.IA_ZM1 || i.IA_ZM2 || i.IA_ZM3 || i.dgq_M1_Ia || 0));
       const ua = chartData.map(i => Number(i.UA_ZM1 || i.UA_ZM2 || i.UA_ZM3 || i.dgq_M1_Ua || 0));
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['开度(%)', 'A相电流(A)', 'A相电压(V)'], top: 10 },
        grid: { left: 40, right: 30, top: 40, bottom: 40 },
        xAxis: { type: 'category', data: times, axisLabel: { fontSize: 12 } },
        yAxis: [{ type: 'value', name: '开度(%)', min: 0, max: 100, position: 'left', axisLabel: { fontSize: 12 } },
                { type: 'value', name: '电流/电压', position: 'right', axisLabel: { fontSize: 12 } }],
        series: [
          { name: '开度(%)', type: 'line', yAxisIndex: 0, data: kd, smooth: true, symbol: 'circle', lineStyle: { width: 3, color: '#409EFF' } },
          { name: 'A相电流(A)', type: 'line', yAxisIndex: 1, data: ia, smooth: true, symbol: 'circle', lineStyle: { width: 2, color: '#67C23A' } },
          { name: 'A相电压(V)', type: 'line', yAxisIndex: 1, data: ua, smooth: true, symbol: 'circle', lineStyle: { width: 2, color: '#E6A23C' } }
        ]
      });
    },
    getStatusColor() {
      const kd = this.latestKD;
      if (kd === 0) return '#f44336'; // 红色 - 关闭
      if (kd < 30) return '#ff9800'; // 橙色 - 部分开启
      if (kd < 70) return '#ffeb3b'; // 黄色 - 中等开启
      return '#4caf50'; // 绿色 - 完全开启
    },
    getStatusText() {
      const kd = this.latestKD;
      if (kd === 0) return '关闭';
      if (kd < 30) return '微开';
      if (kd < 70) return '半开';
      return '全开';
    },
    fieldToLabel(key) {
      const map = {
        // 东干渠M1
        'dgq_M1_Ua': 'M闸门1A相电压',
        'dgq_M1_Ub': 'M闸门1B相电压',
        'dgq_M1_Uc': 'M闸门1C相电压',
        'dgq_M1_Uab': 'M闸门1AB线电压',
        'dgq_M1_Ubc': 'M闸门1BC线电压',
        'dgq_M1_Uca': 'M闸门1CA线电压',
        'dgq_M1_Ia': 'M闸门1A相电流',
        'dgq_M1_Ib': 'M闸门1B相电流',
        'dgq_M1_Ic': 'M闸门1C相电流',
        'dgq_M1_KD': 'M闸门1开度',
        'dgq_M1_KDSD': 'M闸门1开度设定',
        // 电站蝶阀M1
        'dzdf_M1_Ua': '电站蝶阀A相电压',
        'dzdf_M1_Ub': '电站蝶阀B相电压',
        'dzdf_M1_Uc': '电站蝶阀C相电压',
        'dzdf_M1_Uab': '电站蝶阀AB线电压',
        'dzdf_M1_Ubc': '电站蝶阀BC线电压',
        'dzdf_M1_Uca': '电站蝶阀CA线电压',
        'dzdf_M1_Ia': '电站蝶阀A相电流',
        'dzdf_M1_Ib': '电站蝶阀B相电流',
        'dzdf_M1_Ic': '电站蝶阀C相电流',
        'dzdf_M1_FIT': '电站蝶阀流量',
        'dzdf_M1_FIT_TOL': '电站蝶阀累计流量',
        'dzdf_M1_YW': '电站蝶阀液位',
        // 取水塔M1
        'qst_M1_Ia': '取水塔M1A相电流',
        'qst_M1_Ib': '取水塔M1B相电流',
        'qst_M1_Ic': '取水塔M1C相电流',
        'qst_M1_Ua': '取水塔M1A相电压',
        'qst_M1_Ub': '取水塔M1B相电压',
        'qst_M1_Uc': '取水塔M1C相电压',
        'qst_M1_Uab': '取水塔M1AB线电压',
        'qst_M1_Ubc': '取水塔M1BC线电压',
        'qst_M1_Uca': '取水塔M1CA线电压',
        'qst_M1_KD': '取水塔M1开度',
        'qst_M1_KDSD': '取水塔M1开度设定',
        // 取水塔M2
        'qst_M2_Ia': '取水塔M2A相电流',
        'qst_M2_Ib': '取水塔M2B相电流',
        'qst_M2_Ic': '取水塔M2C相电流',
        'qst_M2_Ua': '取水塔M2A相电压',
        'qst_M2_Ub': '取水塔M2B相电压',
        'qst_M2_Uc': '取水塔M2C相电压',
        'qst_M2_Uab': '取水塔M2AB线电压',
        'qst_M2_KD': '取水塔M2开度',
        'qst_M2_KDSD': '取水塔M2开度设定',
        // 西干渠M1
        'xgq_M1_Ia': '西干渠M1A相电流',
        'xgq_M1_Ib': '西干渠M1B相电流',
        'xgq_M1_Ic': '西干渠M1C相电流',
        'xgq_M1_Ua': '西干渠M1A相电压',
        'xgq_M1_Ub': '西干渠M1B相电压',
        'xgq_M1_Uc': '西干渠M1C相电压',
        'xgq_M1_Uab': '西干渠M1AB线电压',
        'xgq_M1_Ubc': '西干渠M1BC线电压',
        'xgq_M1_Uca': '西干渠M1CA线电压',
        'xgq_M1_KD': '西干渠M1开度',
        'xgq_M1_KDSD': '西干渠M1开度设定',
        // 西干渠M2
        'xgq_M2_Ia': '西干渠M2A相电流',
        'xgq_M2_Ib': '西干渠M2B相电流',
        'xgq_M2_Ic': '西干渠M2C相电流',
        'xgq_M2_Ua': '西干渠M2A相电压',
        'xgq_M2_Ub': '西干渠M2B相电压',
        'xgq_M2_Uc': '西干渠M2C相电压',
        'xgq_M2_Uab': '西干渠M2AB线电压',
        'xgq_M2_KD': '西干渠M2开度',
        'xgq_M2_KDSD': '西干渠M2开度设定',
        // 溢洪道M1
        'yhd_M1_Ia': '溢洪道M1A相电流',
        'yhd_M1_Ib': '溢洪道M1B相电流',
        'yhd_M1_Ic': '溢洪道M1C相电流',
        'yhd_M1_Ua': '溢洪道M1A相电压',
        'yhd_M1_Ub': '溢洪道M1B相电压',
        'yhd_M1_Uc': '溢洪道M1C相电压',
        'yhd_M1_Uab': '溢洪道M1AB线电压',
        'yhd_M1_Ubc': '溢洪道M1BC线电压',
        'yhd_M1_Uca': '溢洪道M1CA线电压',
        'yhd_M1_KD': '溢洪道M1开度',
        'yhd_M1_KDSD': '溢洪道M1开度设定',
        // 溢洪道M2
        'yhd_M2_Ia': '溢洪道M2A相电流',
        'yhd_M2_Ib': '溢洪道M2B相电流',
        'yhd_M2_Ic': '溢洪道M2C相电流',
        'yhd_M2_Ua': '溢洪道M2A相电压',
        'yhd_M2_Ub': '溢洪道M2B相电压',
        'yhd_M2_Uc': '溢洪道M2C相电压',
        'yhd_M2_Uab': '溢洪道M2AB线电压',
        'yhd_M2_Ubc': '溢洪道M2BC线电压',
        'yhd_M2_Uca': '溢洪道M2CA线电压',
        'yhd_M2_KD': '溢洪道M2开度',
        'yhd_M2_KDSD': '溢洪道M2开度设定',
        // 溢洪道M3
        'yhd_M3_Ia': '溢洪道M3A相电流',
        'yhd_M3_Ib': '溢洪道M3B相电流',
        'yhd_M3_Ic': '溢洪道M3C相电流',
        'yhd_M3_Ua': '溢洪道M3A相电压',
        'yhd_M3_Ub': '溢洪道M3B相电压',
        'yhd_M3_Uc': '溢洪道M3C相电压',
        'yhd_M3_Uab': '溢洪道M3AB线电压',
        'yhd_M3_Ubc': '溢洪道M3BC线电压',
        'yhd_M3_Uca': '溢洪道M3CA线电压',
        'yhd_M3_KD': '溢洪道M3开度',
        'yhd_M3_KDSD': '溢洪道M3开度设定',
      };
      if (key === 'tm' || key === 'TM') return '时间';
      return map[key] || key;
    }
  }
}
</script>

<style scoped>
.gate-status-container { height: 100%; width: 100%; background: #f4f6fa; padding: 20px; box-sizing: border-box; }
.search-panel {
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}
.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}
.right-panel { height: calc(100% - 100px); overflow: auto; background: #f4f6fa; width: 100%; }
.status-card, .chart-card, .table-card { margin-bottom: 20px; box-shadow: 0 2px 8px #e4e7ed22; border-radius: 8px;}
.status-title, .chart-title, .table-title { font-size: 16px; font-weight: bold; color: #222; padding: 10px 0; border-bottom: 1px solid #eee; margin-bottom: 10px;}
.status-card .gate-status-box { padding: 30px 0 30px 0; }
.realtime-info { font-size: 15px; color: #333; margin-top: 10px; }
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
</style> 
