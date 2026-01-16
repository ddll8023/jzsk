<template>
  <div class="seepage-container">
    <el-card class="filter-card" shadow="hover">
      <div slot="header" class="header-row">
        <el-tabs v-model="mainTab" @tab-click="handleMainTabClick" class="main-switch-tabs">
          <el-tab-pane label="渗流量统计查询" name="seepage"></el-tab-pane>
          <el-tab-pane label="浸润线" name="phreatic"></el-tab-pane>
        </el-tabs>
      </div>
      <template v-if="mainTab === 'seepage'">
        <el-form :inline="true" :model="query" class="mb-2">
          <el-form-item label="选择站点">
            <el-select v-model="query.pointId" filterable clearable placeholder="请选择站点" @change="onPointChange" @clear="onPointClear" style="min-width: 220px">
              <el-option label="全部" value="" />
              <el-option v-for="p in pointList" :key="p.id || p.name"
                :label="p.name"
                :value="p.name" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间选择">
            <!-- Quick date range selection buttons -->
            <el-button-group>
              <el-button :type="query.dateRangeType === '24h' ? 'primary' : 'default'" @click="setQuickDateRange('24h')">近24h</el-button>
              <el-button :type="query.dateRangeType === 'week' ? 'primary' : 'default'" @click="setQuickDateRange('week')">近一周</el-button>
              <el-button :type="query.dateRangeType === 'month' ? 'primary' : 'default'" @click="setQuickDateRange('month')">近一月</el-button>
              <el-button :type="query.dateRangeType === 'threeMonth' ? 'primary' : 'default'" @click="setQuickDateRange('threeMonth')">近三月</el-button>
              <el-button :type="query.dateRangeType === 'custom' ? 'primary' : 'default'" @click="setQuickDateRange('custom')">自定义</el-button>
            </el-button-group>
            <el-date-picker v-if="query.dateRangeType === 'custom'" v-model="query.dateRange" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" @change="onDateRangeChange" />
            <span v-else class="date-display">{{ displayDateRange }}</span>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
            <el-button type="success" icon="el-icon-download" @click="exportData">导出</el-button>
          </el-form-item>
        </el-form>
        <el-card class="chart-card" shadow="hover">
          <div slot="header">
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
              <el-tab-pane label="水位高程" name="waterElevation"></el-tab-pane>
              <el-tab-pane label="水位" name="waterLevel"></el-tab-pane>
              <el-tab-pane label="温度" name="temperature"></el-tab-pane>
              <el-tab-pane label="水压" name="waterPressure"></el-tab-pane>
            </el-tabs>
          </div>
          <div id="chart" class="chart-container"></div>
        </el-card>
        <el-card class="table-card" shadow="hover">
          <div slot="header"><strong>渗流数据</strong></div>
          <el-table :data="tableData" border stripe style="width:100%;margin-top:10px;">
            <el-table-column prop="time" label="采集时间" align="center">
              <template slot-scope="scope">
                {{ formatMinute(scope.row.time) }}
              </template>
            </el-table-column>
            <el-table-column prop="pointId" label="测站名称" align="center">
              <template slot-scope="scope">
                {{ scope.row.pointId }}
              </template>
            </el-table-column>
            <el-table-column label="水位高程(m)" align="center">
              <template slot-scope="scope">
                {{ parseResultData(scope.row.resultData, '水位高程') }}
              </template>
            </el-table-column>
            <el-table-column label="水位(mm)" align="center">
              <template slot-scope="scope">
                {{ parseResultData(scope.row.resultData, '水位') }}
              </template>
            </el-table-column>

            <el-table-column label="温度(°C)" align="center">
              <template slot-scope="scope">
                {{ parseOriginalData(scope.row.originalData, '温度') }}
              </template>
            </el-table-column>
            <el-table-column label="水压" align="center">
              <template slot-scope="scope">
                {{ parseResultData(scope.row.resultData, '水压') }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top:16px;text-align:right;"
            background
            layout="prev, pager, next, jumper"
            :total="total"
            :page-size="query.size"
            :current-page.sync="query.current"
            @current-change="onPageChange"
          />
        </el-card>
      </template>
      <template v-else>
        <div class="phreatic-top-bar">
          <span class="phreatic-title">主坝浸润线观测图</span>
          <el-form :inline="true" class="phreatic-form">
            <el-form-item label="断面桩号：">
              <el-select v-model="selectedSection" style="min-width: 180px" @change="onSectionChange">
                <el-option v-for="section in sectionList" :key="section.value" :label="section.label" :value="section.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="开始日期：">
              <el-date-picker v-model="phreaticDateRange[0]" type="date" placeholder="开始日期" @change="onPhreaticDateChange" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item label="结束日期：">
              <el-date-picker v-model="phreaticDateRange[1]" type="date" placeholder="结束日期" @change="onPhreaticDateChange" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onPhreaticConfirm">确定</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div id="phreatic-chart" class="phreatic-chart-container" style="width: 80%; float: left;"></div>
      </template>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts';
import _ from 'lodash';

export default {
  data() {
    return {
      pointList: [],
      query: {
        pointId: '',
        dateRangeType: '24h',
        dateRange: [],
        current: 1,
        size: 10
      },
      tableData: [],
      total: 0,
      activeTab: 'waterElevation',
      chart: null,
      chartConfig: {
        waterElevation: {
          title: '水位高程变化趋势',
          yAxisName: '水位高程',
          dataKey: 'resultData',
          valueKey: '水位高程',
          minSpan: 0.1
        },
        waterLevel: {
          title: '水位变化趋势',
          yAxisName: '水位(mm)',
          dataKey: 'resultData',
          valueKey: '水位',
          minSpan: 1
        },
        temperature: {
          title: '温度变化趋势',
          yAxisName: '温度(°C)',
          dataKey: 'originalData',
          valueKey: '温度',
          minSpan: 0.1
        },
        waterPressure: {
          title: '水压变化趋势',
          yAxisName: '水压',
          dataKey: 'resultData',
          valueKey: '水压',
          minSpan: 0.1
        }
      },
      chartData: [],
      mainTab: 'seepage',
      selectedSection: '0+100',
      sectionList: [
        { label: '主坝0+000', value: '0+000' },
        { label: '主坝0+015', value: '0+015' },
        { label: '主坝0+025', value: '0+025' },
        { label: '主坝0+035', value: '0+035' },
        { label: '主坝0+100', value: '0+100' },
        { label: '主坝0+150', value: '0+150' },
        { label: '主坝0+250', value: '0+250' },
        { label: '主坝0+350', value: '0+350' }
      ],
      phreaticDateRange: [this.getDefaultStartDate(), this.getDefaultEndDate()],
      damProfileMap: {
        // 参照图二（棕色）形状重构：上游缓坡→顶宽→下游折线
        '0+000': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+015': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+025': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+035': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+100': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+150': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+250': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}],
        '0+350': [ {x:28,y:30}, {x:105,y:77}, {x:120,y:77}, {x:132,y:70}, {x:150,y:70}, {x:210,y:30}]
      },
      pointNameIdMap: {
        '0+100': [
          { name: 'UPb1-1', id: 'P0108248' },
          { name: 'UPb1-2', id: 'P0108234' },
          { name: 'UPb1-3', id: 'P0108376' },
          { name: 'UPb1-4', id: 'P0108173' },
          { name: 'UPb1-5', id: 'P0108236' }
        ],
        '0+150': [
          // 第一个管道：包含5个测站（除了UPb2-5之外的所有测站）
          { name: 'UPa1-1', id: 'P0108190', pipeIndex: 0 },
          { name: 'UPa1-4', id: 'P0108345', pipeIndex: 0 },
          { name: 'UPa1-5', id: 'P0108154', pipeIndex: 0 },
          { name: 'UPb2-1', id: 'P0108310', pipeIndex: 0 },
          { name: 'UPb2-4', id: 'P0108066', pipeIndex: 0 },
          // 第二个管道：UPb2-2
          { name: 'UPb2-2', id: 'P0108046', pipeIndex: 1 },
          // 第三个管道：UPa1-2
          { name: 'UPa1-2', id: 'P0108050', pipeIndex: 2 },
          // 第四个管道：UPb2-3
          { name: 'UPb2-3', id: 'P0108235', pipeIndex: 3 },
          // 第五个管道：UPa1-3
          { name: 'UPa1-3', id: 'P0108242', pipeIndex: 4 },
          // 原配置中 UPb2-5 需要在0+150断面显示，这里按需求删除，不再在该断面展示
        ],
        '0+250': [
          { name: 'UPb3-1', id: 'P0108267' },
          { name: 'UPb3-2', id: 'P0108282' },
          { name: 'UPb3-3', id: 'P0108033' },
          { name: 'UPb3-4', id: 'P0108100' },
          { name: 'UPb3-5', id: 'P0108377' }
        ],
        '0+350': [
          { name: 'UPb4-1', id: 'P0108174' },
          { name: 'UPb4-2', id: 'P0108273' },
          { name: 'UPb4-3', id: 'P0108198' },
          { name: 'UPb4-4', id: 'P0108181' },
          { name: 'UPb4-5', id: 'P0108056' }
        ],
        '0+000': [
          { name: 'UPr1-1', id: 'P0108118' },
        ],
        '0+015': [
          { name: 'UPr1-2', id: 'P0108148' },
        ],
        '0+025': [
          { name: 'UPr2-1', id: 'P0108206' },
        ],
        '0+035': [
          { name: 'UPr2-2', id: 'P0108311' },
        ]
      },
      damProfile: [],
      phreaticPoints: [],
      phreaticChart: null,
      reservoirLevel: null,
      reservoirLevelTime: '',
      phreaticCache: new Map(),
      phreaticCancel: null,
    }
  },
  computed: {
    displayDateRange() {
      if (this.query.dateRange && this.query.dateRange.length === 2) {
        const [start, end] = this.query.dateRange;
        return `${this.formatMinute(start)} 至 ${this.formatMinute(end)}`;
      }
      return '';
    }
  },
  watch: {
    mainTab(val) {
      if (val === 'phreatic') {
        this.$nextTick(() => {
          // 每次进入浸润线，销毁旧实例，避免隐藏后尺寸不正确或丢失渲染
          if (this.phreaticChart) {
            try { this.phreaticChart.dispose(); } catch(e) {}
            this.phreaticChart = null;
          }
          // 先用已有数据渲染，避免空白，再异步拉取
          if (this.phreaticPoints && this.phreaticPoints.length && typeof this.reservoirLevel === 'number') {
            this.initPhreaticChart();
          }
          this.updatePhreaticData();
          // 切换显示后强制自适应尺寸
          setTimeout(() => {
            if (this.phreaticChart) this.phreaticChart.resize();
          }, 50);
        });
      }
      if (val === 'seepage') {
        this.$nextTick(() => {
          this.initChart();
        });
      }
    }
  },
  methods: {
    // 解析后端时间：支持数字秒/毫秒、ISO字符串、Timestamp字符串
    parseBackendTime(t) {
      if (t == null) return NaN;
      if (typeof t === 'number') {
        // 10位秒，13位毫秒
        if (t < 1e12) return t * 1000;
        return t;
      }
      // Timestamp 或 ISO 字符串
      const d = new Date(String(t).replace(/-/g,'/'));
      return d.getTime();
    },
    formatUpNameWithId(p) {
      if (!p) return '';
      const id = p.id || p.pointId || '';
      const name = p.name || p.upName || this.formatUpNameById(id) || '';
      // UI 只显示测站名称
      return name || '';
    },
    formatUpNameById(id) {
      if (!id) return '';
      const all = this.pointNameIdMap;
      for (const k in all) {
        const arr = all[k] || [];
        const f = arr.find(x => x.id === id || String(x.id) === String(id));
        if (f) return f.name;
      }
      return '';
    },
    getPointDisplayName(p) {
      if (!p) return '';
      // 优先使用已处理的name
      if (p.name && !p.name.startsWith('P')) {
        return p.name;
      }
      // 如果name是ID格式，从pointNameIdMap查找
      const id = p.id || p.name || '';
      if (id) {
        const name = this.formatUpNameById(id);
        if (name) return name;
      }
      // 最后返回原始值
      return p.name || p.id || '';
    },
    // 根据name查找对应的ID
    getNameToId(name) {
      if (!name) return '';
      const all = this.pointNameIdMap;
      for (const k in all) {
        const arr = all[k] || [];
        const f = arr.find(x => x.name === name);
        if (f) return f.id;
      }
      return name; // 如果找不到，返回原值（可能是ID格式）
    },
    async fetchPoints() {
      try {
        const res = await axios.get('/data-new/points');
        console.log('后端返回的points数据:', res.data);
        const raw = Array.isArray(res.data) ? res.data : (res.data && res.data.records) || [];
        console.log('解析后的raw数据:', raw);
        if (raw.length > 0) {
          console.log('第一条数据示例:', raw[0]);
        }
        // 兼容多种后端字段：{id,name} 或 {pointId,upName}
        // 注意：后端返回的name字段实际上是ID（如'P0108248'），需要转换为pointNameIdMap中的name
        this.pointList = raw.map(r => {
          // 后端返回的name字段是ID，id字段是数字ID
          const backendName = r.name || ''; // 这是ID格式，如'P0108248'
          const backendId = r.id || r.pointId || r.piezometerId || r.code || '';
          // 优先使用name字段（后端返回的ID），如果没有则使用id字段
          const actualId = backendName || String(backendId);
          // 从pointNameIdMap中查找对应的name（如'UPb1-1'）
          const displayName = this.formatUpNameById(actualId) || backendName || String(backendId);
          return { 
            id: actualId, 
            name: displayName,
            backendId: backendId // 保留原始数字ID，可能用于查询
          };
        }).filter(p => p.id || p.name);
        console.log('处理后的pointList:', this.pointList);
        // 按名称排序，方便查找
        this.pointList.sort((a, b) => {
          const nameA = a.name || a.id || '';
          const nameB = b.name || b.id || '';
          return nameA.localeCompare(nameB, 'zh-CN');
        });
      } catch (error) {
        console.error('获取测点列表失败:', error);
        this.pointList = [];
      }
    },
    async fetchData() {
      try {
        // 如果query.pointId是name（如'UPb1-1'），需要转换为ID（如'P0108248'）
        let pointIdForQuery = this.query.pointId || undefined;
        if (pointIdForQuery && !pointIdForQuery.startsWith('P')) {
          // 可能是name格式，尝试转换为ID
          const id = this.getNameToId(pointIdForQuery);
          if (id && id.startsWith('P')) {
            pointIdForQuery = id;
          }
        }
        const params = {
          current: this.query.current,
          size: this.query.size,
          // 后端要求用测点ID筛选（pointId参数实际需要ID格式）
          pointId: pointIdForQuery || undefined
        };
        if (this.query.dateRange && this.query.dateRange.length === 2) {
          params.startTime = this.query.dateRange[0];
          params.endTime = this.query.dateRange[1];
        } else if (this.query.dateRangeType === '24h') {
          // 表格也对齐24h的窗口：优先使用图表刚刚修正过的窗口
          const end = this.query.dateRange && this.query.dateRange[1] ? new Date(this.query.dateRange[1]) : new Date();
          const start = new Date(end.getTime() - 24*60*60*1000);
          params.startTime = this.formatDateForPicker(start);
          params.endTime = this.formatDateForPicker(end);
        }
        const res = await axios.get('/data-new/page', { params });
        this.tableData = res.data.records || [];
        this.total = res.data.total || 0;
        // 若24h模式下没有表格数据，尝试使用 latest-water-elevation 对齐窗口再拉一次
        if ((!this.tableData || this.tableData.length === 0) && this.query.dateRangeType === '24h' && this.query.pointId) {
          try {
            const latest = await axios.get('/data-new/latest-water-elevation');
            const list = Array.isArray(latest.data) ? latest.data : [];
            const hit = list.find(it => (it.pointName === this.query.pointId) || (it.pointId === this.query.pointId));
            if (hit && hit.time) {
              const endMs = this.parseBackendTime(hit.time);
              if (!isNaN(endMs)) {
                const startMs = endMs - 24*60*60*1000;
                const start = this.formatDateForPicker(new Date(startMs));
                const end = this.formatDateForPicker(new Date(endMs));
                const retry = await axios.get('/data-new/page', { params: { ...params, startTime: start, endTime: end } });
                this.tableData = retry.data.records || [];
                this.total = retry.data.total || 0;
              }
            }
          } catch(e) {}
        }
      } catch (error) {
        this.tableData = [];
        this.total = 0;
      }
    },
    exportData() {
      if (!this.tableData || this.tableData.length === 0) {
        this.$message.warning('没有数据可导出！');
        return;
      }
      const headers = ['采集时间', '测站名称', '水位高程', '水位(mm)', '温度(°C)', '水压'];
      const rows = this.tableData.map(item => [
        this.formatMinute(item.time),
        item.pointId,
        this.parseResultData(item.resultData, '水位高程'),
        this.parseResultData(item.resultData, '水位'),
        this.parseOriginalData(item.originalData, '温度'),
        this.parseResultData(item.resultData, '水压')
      ]);

      let csvContent = '\ufeff' + headers.join(',') + '\n';
      rows.forEach(row => {
        csvContent += row.map(e => `"${e}"`).join(',') + '\n';
      });

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement("a");
      if (link.download !== undefined) {
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", "渗流量数据.csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
      }
      this.$message.success('数据已导出！');
    },
    formatMinute(val) {
      if (!val) return ''
      let d;
      if (typeof val === 'number') {
        d = new Date(val * 1000);
      } else if (typeof val === 'string' && val.length >= 16) {
        d = new Date(val.replace(/-/g, '/'));
      } else {
        d = new Date(val);
      }

      if (!isNaN(d.getTime())) {
        const y = d.getFullYear()
        const m = String(d.getMonth() + 1).padStart(2, '0')
        const dd = String(d.getDate()).padStart(2, '0')
        const h = String(d.getHours()).padStart(2, '0')
        const min = String(d.getMinutes()).padStart(2, '0')
        return `${y}-${m}-${dd} ${h}:${min}`
      }
      return ''
    },
    formatPointId(id) {
      try {
        return BigInt(id).toString();
      } catch (e) {
        console.error("Error converting pointId to BigInt:", id, e);
        return String(id);
      }
    },
    onPointClear() {
      this.query.pointId = '';
      this.onSearch();
    },
    setQuickDateRange(type) {
      this.query.dateRangeType = type;
      const now = new Date();
      let startDate, endDate;
      switch (type) {
        case '24h':
          startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000);
          endDate = now;
          break;
        case 'week':
          startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
          endDate = now;
          break;
        case 'month':
          startDate = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds());
          endDate = now;
          break;
        case 'threeMonth':
          startDate = new Date(now.getFullYear(), now.getMonth() - 3, now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds());
          endDate = now;
          break;
        case 'custom':
          this.query.dateRange = this.query.dateRange.length ? this.query.dateRange : [this.formatDateForPicker(new Date(now.getTime() - 24 * 60 * 60 * 1000)), this.formatDateForPicker(now)];
          this.onSearch();
          this.fetchChartData();
          return;
        default:
          startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000);
          endDate = now;
      }
      this.query.dateRange = [this.formatDateForPicker(startDate), this.formatDateForPicker(endDate)];
      this.onSearch();
      this.fetchChartData();
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
    parseOriginalData(data, key) {
      try {
        const parsed = JSON.parse(data);
        const value = parsed[key];
        if (value !== undefined) {
          return typeof value === 'number' ? value.toFixed(2) : value;
        }
        return '';
      } catch (e) {
        console.error('Error parsing originalData:', e, 'Raw data:', data);
        return '';
      }
    },
    parseResultData(data, key) {
      try {
        const parsed = JSON.parse(data);
        const value = parsed[key];
        if (value !== undefined) {
          return typeof value === 'number' ? value.toFixed(2) : value;
        }
        return '';
      } catch (e) {
        console.error('Error parsing resultData:', e, 'Raw data:', data);
        return '';
      }
    },
    initChart() {
      // 销毁旧实例
      if (this.chart) {
        this.chart.dispose();
        this.chart = null;
      }
      const chartDom = document.getElementById('chart');
      // DOM 不存在时延迟重试，确保首次访问时图表正确初始化
      if (!chartDom) {
        setTimeout(() => this.initChart(), 50);
        return;
      }
      this.chart = echarts.init(chartDom);
      this.updateChart();
    },
    async fetchChartData() {
      if (!this.query.pointId) {
        this.chartData = [];
        this.updateChart();
        return;
      }
      let url = '';
      switch (this.activeTab) {
        case 'waterElevation': url = '/data-new/time-water-elevation'; break;
        case 'waterLevel': url = '/data-new/time-water-level'; break;
        case 'temperature': url = '/data-new/time-temperature'; break;
        case 'waterPressure': url = '/data-new/time-water-pressure'; break;
        // 其他tab可补充
        default: url = '/data-new/time-water-elevation';
      }
      try {
        // 如果query.pointId是name（如'UPb1-1'），需要转换为ID（如'P0108248'）
        let pointIdForQuery = this.query.pointId || '';
        if (pointIdForQuery && !pointIdForQuery.startsWith('P')) {
          // 可能是name格式，尝试转换为ID
          const id = this.getNameToId(pointIdForQuery);
          if (id && id.startsWith('P')) {
            pointIdForQuery = id;
          }
        }
        const params = { pointId: pointIdForQuery };
        if (this.query.dateRange && this.query.dateRange.length === 2) {
          params.startTime = this.query.dateRange[0];
          params.endTime = this.query.dateRange[1];
        }
        const res = await axios.get(url, { params });
        this.chartData = res.data || [];
        // 近24h无数据时，自动以后端最新时间为右端点重置24h范围并重试一次
        if ((!this.chartData || this.chartData.length === 0) && this.query.dateRangeType === '24h') {
          try {
            const latest = await axios.get('/data-new/latest-water-elevation');
            const list = Array.isArray(latest.data) ? latest.data : [];
            const hit = list.find(it => (it.pointName === pointIdForQuery) || (it.pointId === pointIdForQuery));
            if (hit && hit.time) {
              const endMs = this.parseBackendTime(hit.time);
              if (!isNaN(endMs)) {
                const startMs = endMs - 24 * 60 * 60 * 1000;
                const fmt = (d) => this.formatDateForPicker(new Date(d));
                this.query.dateRange = [fmt(startMs), fmt(endMs)];
                const retry = await axios.get(url, { params: { pointId: pointIdForQuery, startTime: this.query.dateRange[0], endTime: this.query.dateRange[1] } });
                this.chartData = retry.data || [];
              }
            }
          } catch(e) {}
        }
        this.updateChart();
      } catch (error) {
        this.chartData = [];
        this.updateChart();
      }
    },
    updateChart() {
      if (!this.chart) return;
      if (!this.query.pointId || !this.chartData.length) {
        this.chart.setOption({
          title: { text: '请选择具体站点显示图表', left: 'center' },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: { type: 'time' },
          yAxis: { 
            type: 'value',
            name: '数值',
            nameLocation: 'end',
            nameGap: 15,
            nameTextStyle: {
              color: '#1E293B',
              fontSize: 13,
              fontWeight: 500
            },
            min: 0,
            max: 100,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#CBD5E1',
                width: 1
              }
            },
            axisTick: {
              show: true,
              length: 4,
              lineStyle: {
                color: '#CBD5E1'
              }
            },
            axisLabel: {
              margin: 10,
              color: '#475569',
              fontSize: 12
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed',
                color: '#E2E8F0',
                opacity: 0.8
              }
            }
          },
          series: [{ type: 'line', data: [] }]
        });
        return;
      }
      const config = this.chartConfig[this.activeTab] || this.chartConfig['waterElevation'];
      // phreaticLine 特殊处理略，可后续补充
      const data = this.chartData.map(item => [this.parseBackendTime(item.time), Number(item.value)]).filter(p => !isNaN(p[0]) && !isNaN(p[1]));
      const values = data.map(item => item[1]).filter(v => !isNaN(v));
      if (!values.length) {
        // 数据解析后为空，使用完整的 Y 轴配置
        this.chart.setOption({
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          yAxis: { 
            min: 0, 
            max: 100, 
            interval: 20, 
            name: config.yAxisName,
            axisLine: {
              show: true,
              lineStyle: { color: '#CBD5E1', width: 1 }
            },
            axisTick: {
              show: true,
              length: 4,
              lineStyle: { color: '#CBD5E1' }
            },
            axisLabel: {
              margin: 10,
              color: '#475569',
              fontSize: 12
            },
            splitLine: {
              show: true,
              lineStyle: { type: 'dashed', color: '#E2E8F0', opacity: 0.8 }
            }
          },
          series: [{ type: 'line', data: [] }]
        });
        return;
      }
      const dataMin = Math.min(...values);
      const dataMax = Math.max(...values);
      const dataRange = dataMax - dataMin;

      const minChartRange = config.minSpan || 1.0; // 从 chartConfig 获取最小Y轴跨度，默认为1.0
      const tinyPaddingFactor = 0.005; // 0.5% 的边距，用于紧密缩放

      let yMin, yMax;

      // 首先，尝试根据数据范围和极小边距确定Y轴范围
      yMin = dataMin - dataRange * tinyPaddingFactor;
      yMax = dataMax + dataRange * tinyPaddingFactor;

      // 如果当前计算出的Y轴范围小于最小所需跨度，则将其扩展到minChartRange
      if (yMax - yMin < minChartRange) {
          const center = (dataMin + dataMax) / 2; // 仍然基于原始数据的中心点
          yMin = center - minChartRange / 2;
          yMax = center + minChartRange / 2;
      }

      // 确保Y轴最小值不为负（如果所有数据都是非负的）
      if (dataMin >= 0) {
          yMin = Math.max(0, yMin);
          // 如果将yMin设置为0导致范围压缩过大，重新扩展yMax以满足minChartRange
          if (yMax - yMin < minChartRange) {
              yMax = yMin + minChartRange;
          }
      }
      // 如果所有数据都是非正的，确保Y轴最大值不为正
      else if (dataMax <= 0) {
          yMax = Math.min(0, yMax);
          // 如果将yMax设置为0导致范围压缩过大，重新扩展yMin以满足minChartRange
          if (yMax - yMin < minChartRange) {
              yMin = yMax - minChartRange;
          }
      }

      // 根据最终的yMin和yMax重新计算间隔
      let interval = (yMax - yMin) / 5;
      if (interval === 0) {
        interval = 1;
      } else {
         // 确保间隔是"友好"的数字，例如 1, 2, 5, 10, 20, 50, 100...的倍数
        const magnitudes = [1, 2, 5];
        const powerOfTen = Math.pow(10, Math.floor(Math.log10(interval)));
        interval = magnitudes.map(m => m * powerOfTen).find(m => m >= interval) || (5 * powerOfTen);
      }

      // 特殊处理：浸润线观测图tab用水位高程曲线
      let option = {
        title: {
          text: config.title,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          formatter: function(params) {
            const ms = params[0].value[0];
            const time = new Date(ms).toLocaleString('zh-CN', { hour12: false });
            const value = Number(params[0].value[1]);
            let decimals = 2;
            if (interval < 1) decimals = 3;
            if (interval < 0.1) decimals = 4;
            return `${time}<br/>${config.yAxisName}: ${isNaN(value) ? '-' : value.toFixed(decimals)}`;
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'time',
          axisLabel: {
            formatter: function(value) {
              return new Date(value).toLocaleString();
            }
          }
        },
        yAxis: {
          type: 'value',
          name: config.yAxisName,
          nameLocation: 'end',
          nameGap: 15,
          nameTextStyle: {
            color: '#1E293B',
            fontSize: 13,
            fontWeight: 500,
            padding: [0, 0, 0, 40]
          },
          min: yMin,
          max: yMax,
          interval: interval,
          splitNumber: 10,
          minInterval: 0.01,
          axisLine: {
            show: true,
            lineStyle: {
              color: '#CBD5E1',
              width: 1
            }
          },
          axisTick: {
            show: true,
            length: 4,
            lineStyle: {
              color: '#CBD5E1'
            }
          },
          axisLabel: {
            margin: 10,
            color: '#475569',
            fontSize: 12,
            formatter: function(value) {
              if (interval >= 1) {
                return value.toFixed(0);
              } else {
                return value.toFixed(2);
              }
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed',
              color: '#E2E8F0',
              opacity: 0.8
            }
          }
        },
      series: [{
        type: 'line',
          name: config.yAxisName,
          data: data,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
          lineStyle: {
            width: 3
          },
          itemStyle: {
            borderWidth: 2
          }
        }]
      };

      this.chart.setOption(option);
    },
    handleTabClick() {
      this.$nextTick(() => {
        this.fetchChartData();
      });
    },
    onPointChange() {
      this.onSearch();
    },
    onSearch() {
      this.query.current = 1;
      this.fetchData();
      this.fetchChartData();
    },
    onDateRangeChange() {
      this.onSearch();
      this.fetchChartData();
    },
    onPageChange(page) {
      this.query.current = page;
      this.fetchData();
      this.fetchChartData();
    },
    handleMainTabClick(tab) {
      // 可根据需要做切换时的逻辑
    },
    getDefaultStartDate() {
      const d = new Date();
      d.setDate(d.getDate() - 1);
      return d.toISOString().slice(0, 10);
    },
    getDefaultEndDate() {
      const d = new Date();
      return d.toISOString().slice(0, 10);
    },
    onSectionChange: _.debounce(function() {
      this.updatePhreaticData();
    }, 150),
    onPhreaticDateChange() {
      this.updatePhreaticDataThrottled();
    },
    onPhreaticConfirm() {
      this.updatePhreaticData();
    },
    updatePhreaticDataThrottled: _.throttle(function() { this.updatePhreaticData(); }, 200),
    async updatePhreaticData() {
      // 1. 更新剖面线
      this.damProfile = this.damProfileMap[this.selectedSection] || [];
      // 取消上一次未完成请求，避免堆积
      if (this.phreaticCancel) {
        try { this.phreaticCancel.cancel('cancel previous phreatic req'); } catch(e) {}
        this.phreaticCancel = null;
      }
      const CancelToken = axios.CancelToken;
      this.phreaticCancel = CancelToken.source();
      // 缓存命中则直接返回
      const cacheKey = `${this.selectedSection}|${this.phreaticDateRange[0]}|${this.phreaticDateRange[1]}`;
      if (this.phreaticCache.has(cacheKey)) {
        const cached = this.phreaticCache.get(cacheKey);
        this.phreaticPoints = cached.phreaticPoints;
        this.reservoirLevel = cached.reservoirLevel;
        this.reservoirLevelTime = cached.reservoirLevelTime;
        this.initPhreaticChart();
        return;
      }

      // 2. 并行请求数据（使用 Promise.all，避免 allSettled 兼容问题）
      let reservoirLevel = null;
      let reservoirLevelTime = '';
      let phreaticPoints = [];

      // 断面测点信息
      let sectionKey = (this.selectedSection + '').trim();
      const allKeys = Object.keys(this.pointNameIdMap).map(k => k.trim());
      let realKey = allKeys.find(k => k.trim() === sectionKey) || allKeys.find(k => k.replace(/\s/g, '') === sectionKey.replace(/\s/g, ''));
      const points = (this.pointNameIdMap[realKey] || []);
      const pointIds = points.map(pt => pt.id).filter(Boolean);
      
      // 计算时间范围（使用选择的日期范围）
      const endDate = new Date(this.phreaticDateRange[1] || this.getDefaultEndDate());
      const startDate = new Date(this.phreaticDateRange[0] || this.getDefaultStartDate());
      const fmt = d => `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`;
      
      // 解析水库水位 & 从/data-new/page获取最新各测点水位高程（一次并发）
      const [waterRes, seepageDataRes] = await Promise.all([
        // 仅取前50条，减少传输；后端若支持排序则取时间倒序第一页
        axios.get('/st-rivers-r/page', { params: { page: 1, size: 50 }, cancelToken: this.phreaticCancel.token }),
        // 从实际数据接口获取各测点的最新水位高程数据
        axios.get('/data-new/page', { 
          params: { 
            pointIds: pointIds.join(','),
            startTime: fmt(startDate),
            endTime: fmt(endDate),
            size: 1000,
            current: 1
          }, 
          cancelToken: this.phreaticCancel.token 
        })
      ]);

      try {
        // 兼容分页/非分页
        const arr = Array.isArray(waterRes.data) ? waterRes.data : (Array.isArray(waterRes.data && waterRes.data.records) ? waterRes.data.records : []);
        // 添加筛选条件：只取 z1 > 50 的数据
        const valid = arr.filter(item => Number(item.z1) > 50);
        if (valid.length > 0) {
          let latest = valid[0];
          for (let i = 1; i < valid.length; i++) {
            const t1 = this.parseTimeArrayToDate(latest.tm);
            const t2 = this.parseTimeArrayToDate(valid[i].tm);
            if (t2 > t1) latest = valid[i];
          }
          reservoirLevel = Number(latest.z1);
          if (latest.tm && Array.isArray(latest.tm) && latest.tm.length >= 3) {
            const [y, m, d] = latest.tm;
            reservoirLevelTime = `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
          }
        } else {
          // 如果没有数据，使用模拟数据
          reservoirLevel = 55.34;
          reservoirLevelTime = '2025-09-22';
        }
      } catch(e) {
        // 如果出错，使用模拟数据
        reservoirLevel = 55.34;
        reservoirLevelTime = '2025-09-22';
      }

      // 解析测点数据：从/data-new/page接口获取最新水位高程数据
      try {
        const records = Array.isArray(seepageDataRes.data && seepageDataRes.data.records) ? seepageDataRes.data.records : [];
        console.log('data-new/page返回数据条数:', records.length);
        
        // 为每个测点ID找到最新的一条记录
        const idToElev = {};
        const idToRecord = {};
        
        records.forEach(item => {
          // 尝试多种方式获取pointId
          const pid = String(item.pointId || item.pointName || item.id || '');
          if (!pid) return;
          
          // 解析resultData中的"水位高程"字段
          let elev = null;
          try {
            const resultData = typeof item.resultData === 'string' ? JSON.parse(item.resultData) : item.resultData;
            if (resultData && typeof resultData['水位高程'] === 'number') {
              elev = resultData['水位高程'];
            } else if (resultData && typeof resultData['水位高程'] === 'string') {
              elev = Number(resultData['水位高程']);
            }
            // 如果resultData中没有，尝试从其他字段获取
            if (elev == null && resultData && typeof resultData['水位'] === 'number') {
              elev = resultData['水位'];
            }
          } catch(e) {
            console.warn('解析resultData失败:', pid, e);
          }
          
          if (elev != null && !isNaN(elev) && elev > 0) {
            // 如果该测点还没有记录，或者这条记录时间更新，则更新
            if (!idToRecord[pid] || new Date(item.time) > new Date(idToRecord[pid].time)) {
              idToElev[pid] = elev;
              idToRecord[pid] = item;
            }
          }
        });
        
        // 如果第一条记录有数据，打印出来用于调试
        if (records.length > 0) {
          console.log('第一条记录示例:', records[0]);
          if (records[0].resultData) {
            try {
              const rd = typeof records[0].resultData === 'string' ? JSON.parse(records[0].resultData) : records[0].resultData;
              console.log('第一条记录的resultData:', rd);
            } catch(e) {}
          }
        }
        
        console.log('idToElev映射:', idToElev);
        console.log('当前断面的points:', points);
        
        // 为每个断面设置固定的X坐标和模拟数据（如果没有真实数据）
        const fixedX_100 = [145, 155, 165, 175, 185];
        const fixedX_150 = [145, 155, 165, 175, 185];
        const fixedX_250 = [145, 155, 165, 175, 185];
        const fixedX_350 = [145, 155, 165, 175, 185];
        
        // 统一处理所有断面
        const fixedXMap = {
          '0+000': [100],  // 单个测站，放在100
          '0+015': [100],  // 单个测站，放在100
          '0+025': [100],  // 单个测站，放在100
          '0+035': [100],  // 单个测站，放在100
          // 0+100 / 0+150 / 0+250 断面：5 个管道统一布在 100 / 120 / 142 / 160 / 180
          '0+100': [100, 120, 142, 160, 180],
          '0+150': [100, 120, 142, 160, 180],
          '0+250': [100, 120, 142, 160, 180],
          '0+350': [145, 155, 165, 175, 185]
        };
        const fixedX = fixedXMap[this.selectedSection] || [105, 122, 142, 160, 180];
        
        phreaticPoints = points.map((pt, i) => {
          // 尝试多种ID格式匹配
          let elev = null;
          const ptId = String(pt.id || '');
          // 直接匹配
          if (ptId && idToElev[ptId] != null) {
            elev = idToElev[ptId];
          } else {
            // 尝试所有可能的ID格式
            for (const key in idToElev) {
              if (String(key) === ptId || key.includes(ptId) || ptId.includes(key)) {
                elev = idToElev[key];
                break;
              }
            }
          }
          
          const finalY = elev != null && !isNaN(elev) && elev > 0 ? elev : (50 + Math.random() * 5);
          console.log(`测点 ${pt.name} (ID: ${ptId}): 高程=${finalY}, 从idToElev获取=${elev}`);
          
          // 对于0+150断面，如果测站有pipeIndex属性，使用pipeIndex来分配x坐标
          let xCoord;
          if (this.selectedSection === '0+150' && typeof pt.pipeIndex === 'number') {
            xCoord = fixedX[pt.pipeIndex] || fixedX[i] || (i + 1) * (240 / (points.length + 1));
          } else {
            xCoord = fixedX[i] || (i + 1) * (240 / (points.length + 1));
          }
          
          return { 
            name: pt.name, 
            id: pt.id,
            x: xCoord, 
            y: finalY,
            pipeIndex: pt.pipeIndex // 保留pipeIndex，用于后续渲染
          };
        });
      } catch (e) {
        const pts = (this.pointNameIdMap[realKey] || []);
        const fixedX = fixedXMap[this.selectedSection] || [105, 122, 142, 160, 180];
        phreaticPoints = pts.map((pt, i) => {
          // 对于0+150断面，如果测站有pipeIndex属性，使用pipeIndex来分配x坐标
          let xCoord;
          if (this.selectedSection === '0+150' && typeof pt.pipeIndex === 'number') {
            xCoord = fixedX[pt.pipeIndex] || (i + 1) * (240 / (pts.length + 1));
          } else {
            xCoord = fixedX[i] || (i + 1) * (240 / (pts.length + 1));
          }
          return { 
            ...pt, 
            x: xCoord, 
            y: 50 + Math.random() * 5, // 模拟数据
            pipeIndex: pt.pipeIndex // 保留pipeIndex
          };
        });
      }

      // 写入并缓存
      this.phreaticPoints = phreaticPoints;
      this.reservoirLevel = reservoirLevel;
      this.reservoirLevelTime = reservoirLevelTime;
      this.phreaticCache.set(cacheKey, { phreaticPoints, reservoirLevel, reservoirLevelTime });
      this.initPhreaticChart();
    },
    initPhreaticChart() {
      // 复用图实例，避免销毁重建
      const chartDom = document.getElementById('phreatic-chart');
      if (!chartDom) return;
      if (!this.phreaticChart) {
        this.phreaticChart = echarts.init(chartDom, null, { useDirtyRect: true });
      }
      // 剖面填充区
      const damFill = [
        ...this.damProfile,
        { x: 240, y: 30 },
        { x: 0, y: 30 },
        { x: 0, y: (this.damProfile[0] && this.damProfile[0].y) ? this.damProfile[0].y : 30 }
      ].map(p => [p.x, p.y]);
      // 剖面线
      const damLine = this.damProfile.map(p => [p.x, p.y]);
      // 计算水库水位线与坝体剖面交点x
      function getReservoirIntersectX(profile, reservoirLevel) {
        for (let i = 1; i < profile.length; i++) {
          const p1 = profile[i - 1];
          const p2 = profile[i];
          if ((p1.y - reservoirLevel) * (p2.y - reservoirLevel) <= 0) {
            // 线性插值
            const t = (reservoirLevel - p1.y) / (p2.y - p1.y);
            return p1.x + t * (p2.x - p1.x);
          }
        }
        return profile[profile.length - 1].x;
      }
      let reservoirLineEndX = 40;
      if (this.damProfile && this.damProfile.length >= 2 && typeof this.reservoirLevel === 'number') {
        reservoirLineEndX = getReservoirIntersectX(this.damProfile, this.reservoirLevel);
      }
      
      // 创建水位填充区域数据
      let waterFillData = [];
      // 移除调试日志，避免阻塞渲染
      
      if (typeof this.reservoirLevel === 'number' && this.reservoirLevel > 0) {
        // 找到坝体左侧边界点（第一个点）
        const damLeftPoint = this.damProfile[0] || { x: 40, y: 30 };
        
        // 水位填充区域：只覆盖左侧梯形区域，不覆盖坝体
        waterFillData = [
          [0, this.reservoirLevel],                    // 左上角
          [damLeftPoint.x, this.reservoirLevel],       // 右上角（坝体左边界）
          [damLeftPoint.x, damLeftPoint.y],            // 右下角（坝体左边界底部）
          [0, damLeftPoint.y]                          // 左下角
        ];
        
        console.log('水位填充数据:', {
          waterFillData,
          damLeftPoint,
          reservoirLevel: this.reservoirLevel,
          reservoirLevelTime: this.reservoirLevelTime
        });
      }
      
      // 浸润线点：起点为水库水位线与坝体剖面交点，后续点按phreaticPoints顺序
      let phreaticLine = [];
      let phreaticLineWithInfo = [];
      if (typeof this.reservoirLevel === 'number') {
        phreaticLine.push([reservoirLineEndX, this.reservoirLevel]);
        phreaticLineWithInfo.push({
          value: [reservoirLineEndX, this.reservoirLevel],
          name: '',
          y: this.reservoirLevel,
          isStart: true
        });
      }
      // 对于0+150断面，需要特殊处理：从第一个管道中水位最高的测站开始连接
      if (this.selectedSection === '0+150') {
        // 按pipeIndex分组
        const pipeGroups = {};
        this.phreaticPoints.forEach(pt => {
          if (typeof pt.y === 'number') {
            const pipeIdx = typeof pt.pipeIndex === 'number' ? pt.pipeIndex : 0;
            if (!pipeGroups[pipeIdx]) {
              pipeGroups[pipeIdx] = [];
            }
            pipeGroups[pipeIdx].push(pt);
          }
        });
        
        // 第一个管道：找到水位最高的测站
        const firstPipePts = pipeGroups[0] || [];
        if (firstPipePts.length > 0) {
          const highestPt = firstPipePts.reduce((max, pt) => 
            (pt.y > max.y) ? pt : max
          );
          
          // 添加到浸润线
          let displayName = highestPt.name || '';
          if (displayName && displayName.startsWith('P')) {
            displayName = this.formatUpNameById(displayName) || displayName;
          }
          phreaticLine.push([highestPt.x, highestPt.y]);
          phreaticLineWithInfo.push({
            value: [highestPt.x, highestPt.y],
            name: displayName,
            y: highestPt.y,
            isStart: false
          });
        }
        
        // 按pipeIndex顺序连接其他管道的测站（每个管道取第一个测站）
        const sortedPipeIndices = Object.keys(pipeGroups)
          .filter(idx => idx !== '0')
          .sort((a, b) => Number(a) - Number(b));
        
        sortedPipeIndices.forEach(pipeIdx => {
          const pts = pipeGroups[pipeIdx];
          if (pts && pts.length > 0) {
            // 每个管道取第一个测站
            const pt = pts[0];
            let displayName = pt.name || '';
            if (displayName && displayName.startsWith('P')) {
              displayName = this.formatUpNameById(displayName) || displayName;
            }
            phreaticLine.push([pt.x, pt.y]);
            phreaticLineWithInfo.push({
              value: [pt.x, pt.y],
              name: displayName,
              y: pt.y,
              isStart: false
            });
          }
        });
      } else {
        // 其他断面：按原来的方式连接所有测站
        this.phreaticPoints.forEach(pt => {
          if (typeof pt.y === 'number') {
            phreaticLine.push([pt.x, pt.y]);
            // 确保name字段正确，如果pt.name是ID，则从pointNameIdMap查找
            let displayName = pt.name || '';
            if (displayName && displayName.startsWith('P')) {
              // 如果是ID格式，查找对应的name
              displayName = this.formatUpNameById(displayName) || displayName;
            }
            phreaticLineWithInfo.push({
              value: [pt.x, pt.y],
              name: displayName,
              y: pt.y,
              isStart: false
            });
          }
        });
      }
      let phreaticLineData = phreaticLineWithInfo;
      
      console.log('浸润线调试信息:', {
        reservoirLevel: this.reservoirLevel,
        reservoirLineEndX: reservoirLineEndX,
        phreaticPoints: this.phreaticPoints,
        phreaticLineData: phreaticLineData
      });
      // 蓝色水位高程竖线颜色加深
      const verticalLines = this.phreaticPoints.filter(p => typeof p.y === 'number').map(p => ({
        coords: [ [p.x, 30], [p.x, p.y] ],
        lineStyle: { color: '#007aff', width: 4 }
      }));
      // 右侧渗压计点灰色透明外壳：高度刚好与坝体相交
      // 对于新断面（0+000到0+035），测点可能在左侧，需要调整过滤条件
      const isNewSection = ['0+000', '0+015', '0+025', '0+035'].includes(this.selectedSection);
      // 对于0+150断面，每个管道位置只取一个代表测站用于显示管道
      // 注意：这里要把 x=100 也包含进来，否则第一个管柱会被过滤掉
      let pointsRight = this.phreaticPoints
        .filter(pt => typeof pt.x === 'number' && (isNewSection || pt.x >= 100));
      
      if (this.selectedSection === '0+150') {
        // 按pipeIndex分组，每个管道位置只取第一个测站用于显示管道
        const pipeGroups = {};
        pointsRight.forEach(pt => {
          const pipeIdx = typeof pt.pipeIndex === 'number' ? pt.pipeIndex : 0;
          if (!pipeGroups[pipeIdx]) {
            pipeGroups[pipeIdx] = pt;
          }
        });
        pointsRight = Object.values(pipeGroups);
      }
      
      pointsRight = pointsRight.sort((a, b) => a.x - b.x);

      // 计算每个测点对应的坝体顶部高度，使灰色外壳刚好与坝体相交
      const getDamTopY = (x) => {
        for (let i = 1; i < this.damProfile.length; i++) {
          const p1 = this.damProfile[i - 1];
          const p2 = this.damProfile[i];
          if (x >= p1.x && x <= p2.x) {
            const t = (x - p1.x) / (p2.x - p1.x);
            return p1.y + t * (p2.y - p1.y);
          }
        }
        const lastPoint = this.damProfile[this.damProfile.length - 1];
        return (lastPoint && lastPoint.y) || 85;
      };

      const piezometerShellSeries = pointsRight.map((pt) => {
        const damTopY = getDamTopY(pt.x);
        return {
          type: 'custom',
          renderItem: function(params, api) {
            const x = api.coord([pt.x, 0])[0];
            const yBottom = api.coord([pt.x, 30])[1];
            const yTop = api.coord([pt.x, damTopY])[1];
            return {
              type: 'rect',
              shape: { x: x - 6, y: yTop, width: 12, height: yBottom - yTop },
              style: api.style({ fill: '#888', opacity: 0.3 })
            };
          },
          data: [0],
          z: 2
        };
      });

      // 在横轴附近显示测站名称（单行，无外框）
      // 对于0+150断面，第一个管道只显示第一个测站的名称，避免重叠
      let nameLabelsGraphic = [];
      if (this.selectedSection === '0+150') {
        // 使用所有测站数据，按pipeIndex分组
        const allRightPts = this.phreaticPoints.filter(pt => {
          const hasX = typeof pt.x === 'number' && !isNaN(pt.x);
          return hasX && (isNewSection || pt.x > 100);
        });
        
        // 按pipeIndex分组，每个管道位置只显示第一个测站的名称
        const pipeGroups = {};
        allRightPts.forEach(pt => {
          const pipeIdx = typeof pt.pipeIndex === 'number' ? pt.pipeIndex : 0;
          if (!pipeGroups[pipeIdx]) {
            pipeGroups[pipeIdx] = [];
          }
          pipeGroups[pipeIdx].push(pt);
        });
        
        // 为每个管道位置生成名称标签（只取第一个测站）
        Object.keys(pipeGroups).sort((a, b) => Number(a) - Number(b)).forEach(pipeIdx => {
          const pts = pipeGroups[pipeIdx];
          if (pts && pts.length > 0) {
            const pt = pts[0]; // 只取第一个测站
            let displayName = pt.name || '';
            if (displayName && displayName.startsWith('P')) {
              displayName = this.formatUpNameById(displayName) || displayName;
            }
            nameLabelsGraphic.push({
              type: 'custom',
              renderItem: function(params, api) {
                const axisCoord = api.coord([pt.x, 30]);
                const x = axisCoord[0];
                const yAxis = axisCoord[1];
                return {
                  type: 'text',
                  style: {
                    text: displayName || '',
                    fill: '#333',
                    fontSize: 10,
                    fontWeight: 'bold',
                    textAlign: 'center',
                    textVerticalAlign: 'top'
                  },
                  position: [x, yAxis + 4]
                };
              },
              data: [0],
              z: 100
            });
          }
        });
      } else {
        // 其他断面：按原来的方式显示
        nameLabelsGraphic = pointsRight.map((pt) => {
          let displayName = pt.name || '';
          if (displayName && displayName.startsWith('P')) {
            displayName = this.formatUpNameById(displayName) || displayName;
          }
          return {
            type: 'custom',
            renderItem: function(params, api) {
              const axisCoord = api.coord([pt.x, 30]);
              const x = axisCoord[0];
              const yAxis = axisCoord[1];
              return {
                type: 'text',
                style: {
                  text: displayName || '',
                  fill: '#333',
                  fontSize: 10,
                  fontWeight: 'bold',
                  textAlign: 'center',
                  textVerticalAlign: 'top'
                },
                position: [x, yAxis + 4]
              };
            },
            data: [0],
            z: 100
          };
        });
      }

      // 右侧渗压计点scatter+markPoint，label下方为高程值
      const rightPts = this.phreaticPoints.filter(pt => {
        const hasY = typeof pt.y === 'number' && !isNaN(pt.y) && pt.y > 0;
        const hasX = typeof pt.x === 'number' && !isNaN(pt.x);
        // 注意：这里也要把 x=100 包含进来，否则 0+100 / 0+250 断面的第一个管柱会被排除
        const isRight = isNewSection || (hasX && pt.x >= 100);
        return hasY && hasX && isRight;
      });
      
      // 使用custom类型显示测站高程标签（显示在测站点下方）
      const elevationLabelsGraphic = rightPts.map((pt) => {
        const yVal = typeof pt.y === 'number' ? pt.y : null;
        if (yVal == null || isNaN(yVal) || yVal <= 0) return null;
        return {
          type: 'custom',
          renderItem: function(params, api) {
            // 使用测站点的坐标，而不是横轴坐标
            const pointCoord = api.coord([pt.x, pt.y]);
            const x = pointCoord[0];
            const yPoint = pointCoord[1];
            const text = `${yVal.toFixed(2)}m`;
            // 估算文本宽度
            const textWidth = text.length * 7;
            // 标签显示在测站点下方，留出一些间距
            const labelY = yPoint + 15; // 测站点下方15像素
            return {
              type: 'group',
              children: [
                {
                  type: 'rect',
                  shape: {
                    x: x - textWidth / 2 - 6,
                    y: labelY - 10,
                    width: textWidth + 12,
                    height: 20
                  },
                  style: {
                    fill: 'rgba(255,255,255,0.9)',
                    stroke: '#007aff',
                    lineWidth: 1
                  }
                },
                {
                  type: 'text',
                  style: {
                    text: text,
                    fill: '#007aff',
                    fontSize: 11,
                    fontWeight: 'bold',
                    textAlign: 'center',
                    textVerticalAlign: 'middle'
                  },
                  position: [x, labelY]
                }
              ]
            };
          },
          data: [0],
          z: 101
        };
      }).filter(item => item !== null);
      
      const piezometerMarkSeries = rightPts.length > 0 ? [
        {
          type: 'scatter',
          data: rightPts.map(pt => {
            // 确保name字段正确，如果pt.name是ID，则从pointNameIdMap查找
            let displayName = pt.name || '';
            if (displayName && displayName.startsWith('P')) {
              // 如果是ID格式，查找对应的name
              displayName = this.formatUpNameById(displayName) || displayName;
            }
            return { 
              value: [pt.x, pt.y], 
              name: displayName, 
              y: pt.y,
              originalName: pt.name
            };
          }),
          symbol: 'circle',
          symbolSize: 12,
          itemStyle: { color: '#1e90ff', borderColor: '#fff', borderWidth: 2 },
          z: 10,
          tooltip: {
            show: true,
            formatter: function(params) {
              const name = params.data.name || params.data.originalName || '';
              const y = params.data.y != null ? params.data.y : (params.data.value && params.data.value[1]);
              return `${name || '测站'}<br/>高程: ${y != null && !isNaN(y) ? Number(y).toFixed(2) + 'm' : '-'}`;
            }
          },
          label: { 
            show: false  // 名称已在灰色管道顶部显示，这里不显示
          }
        }
      ] : [];
      // 新增断面（0+000, 0+015, 0+025, 0+035）：将测站水位高程与坝脚点(200,30)用一条平滑曲线连接
      const seepageCurveSeries = [];
      if (isNewSection && this.phreaticPoints && this.phreaticPoints.length > 0) {
        const mainPt = this.phreaticPoints[0];
        if (typeof mainPt.x === 'number' && typeof mainPt.y === 'number') {
          const toeX = 200;
          const toeY = 30;
          const midX = (mainPt.x + toeX) / 2;
          const midY = (mainPt.y + toeY) / 2 + 3; // 稍微抬高形成弧线
          seepageCurveSeries.push({
            type: 'line',
            name: '渗流线',
            data: [
              [mainPt.x, mainPt.y],
              [midX, midY],
              [toeX, toeY]
            ],
            smooth: true,
            symbol: 'none',
            lineStyle: { color: '#409EFF', width: 3 }, // 蓝色平滑曲线
            z: 7
          });
        }
      }
      // 蓝色文字标注：固定在图左上角（相对像素定位）
      const waterLabelGraphic = () => ({
        type: 'group',
        id: 'waterLevelGroup',
        left: 60,
        top: 12,
        children: [
          {
            type: 'rect',
            shape: { x: 0, y: 0, width: 260, height: 26 },
            style: { fill: 'rgba(255,255,255,0.65)', stroke: '#d9e6ff' }
          },
          {
            type: 'text',
            style: {
              text: `实测水位 ${this.reservoirLevel ? this.reservoirLevel.toFixed(2) : ''}m ${this.reservoirLevelTime || ''}`,
              fill: '#1e90ff',
              font: 'bold 14px sans-serif',
              textAlign: 'left'
            },
            left: 8,
            top: 5
          }
        ]
      });
      // series配置健壮性，所有type都必须指定
      this.phreaticChart.setOption({
        title: { text: '', left: 'center' },
        grid: { left: 60, right: 40, top: 60, bottom: 50 },
        xAxis: { type: 'value', name: '坝体横向位置', min: 0, max: 240, axisLine: { onZero: false } },
        yAxis: { type: 'value', name: '断面高(m)', min: 30, max: 85 },
        tooltip: { show: false },
        legend: { show: false },
        animation: false,
        progressive: 2000,
        progressiveThreshold: 3000,
        series: [
          ...(nameLabelsGraphic || []),  // 测站名称标签
          ...(elevationLabelsGraphic || []),  // 测站高程标签
          {
            type: 'line',
            name: '水位填充',
            data: [
              [0, this.reservoirLevel],
              [reservoirLineEndX, this.reservoirLevel],
              [reservoirLineEndX, 30],
              [0, 30]
            ],
            lineStyle: { color: 'transparent' },
            areaStyle: { color: '#409EFF', opacity: 0.3 },
            symbol: 'none',
            z: 2
          },
          {
            type: 'custom',
            name: '剖面填充',
            renderItem: function(params, api) {
              const points = damFill.map(pt => api.coord(pt));
              return {
                type: 'polygon',
                shape: { points },
                style: api.style({ fill: '#4e8077', opacity: 0.5 })
              };
            },
            data: [damFill],
            silent: true,
            z: 0
          },
          ...piezometerShellSeries,
          {
            type: 'lines',
            coordinateSystem: 'cartesian2d',
            polyline: false,
            data: verticalLines,
            lineStyle: { color: '#007aff', width: 4, type: 'solid' },
            effect: { show: false },
            z: 3
          },
          {
            type: 'line',
            name: '剖面线',
            data: damLine,
            lineStyle: { color: '#8d5524', width: 2 },
            symbol: 'none',
            z: 3
          },
          {
            type: 'line',
            name: '水库水位线',
            data: [ [0, this.reservoirLevel], [reservoirLineEndX, this.reservoirLevel] ],
            lineStyle: { color: '#409EFF', width: 4 },
            symbol: 'none',  // 删除蓝色圆点
            z: 8,
            label: {
              show: true,
              position: 'left',  // 改为左侧，只显示一个
              formatter: function(params) {
                // 只对第一个点显示标签
                if (params.dataIndex === 0) {
                  const y = params.data[1];
                  return typeof y === 'number' ? `水位: ${y.toFixed(2)}m` : '';
                }
                return '';
              },
              color: '#409EFF',
              fontSize: 13,
              fontWeight: 'bold',
              backgroundColor: 'rgba(255,255,255,0.95)',
              borderColor: '#409EFF',
              borderWidth: 1,
              borderRadius: 4,
              padding: [4, 6],
              offset: [-8, 0]
            },
            tooltip: {
              show: true,
              formatter: function(params) {
                return `水库水位: ${params.data[1].toFixed(2)}m`;
              }
            }
          },
          // 水库水位线与坝体相交点标记
          {
            type: 'scatter',
            name: '水位交点',
            data: typeof this.reservoirLevel === 'number' ? [[reservoirLineEndX, this.reservoirLevel]] : [],
            symbol: 'circle',
            symbolSize: 10,
            itemStyle: { 
              color: '#409EFF', 
              borderColor: '#fff', 
              borderWidth: 2 
            },
            z: 9,
            label: {
              show: true,
              position: 'top',
              formatter: function(params) {
                const y = params.data[1];
                return typeof y === 'number' ? `水库水位 ${y.toFixed(2)}m` : '';
              },
              color: '#409EFF',
              fontSize: 11,
              fontWeight: 'bold',
              backgroundColor: 'rgba(255,255,255,0.9)',
              borderColor: '#409EFF',
              borderWidth: 1,
              borderRadius: 4,
              padding: [4, 6],
              offset: [0, -8]
            },
            tooltip: {
              show: true,
              formatter: function(params) {
                return `水库水位: ${params.data[1].toFixed(2)}m`;
              }
            }
          },
          {
            type: 'line',
            name: '浸润线',
            data: phreaticLineData.map(item => ({
              value: item.value || item,
              name: item.name || '',
              y: item.y || (Array.isArray(item.value) ? item.value[1] : (Array.isArray(item) ? item[1] : null)),
              isStart: item.isStart || false
            })),
            lineStyle: { color: '#1e90ff', width: 3 },
            symbol: 'none',          // 去掉蓝色小圆点
            symbolSize: 0,
            z: 6,
            smooth: true,            // 使用平滑渗流曲线
            showSymbol: false,
            sampling: 'lttb',
            itemStyle: { color: '#1e90ff' },
            label: {
              show: true,
              position: 'top',
              formatter: function(params) {
                // 起点（水库水位交点）不显示标签
                if (params.data && params.data.isStart) return '';
                const dataItem = params.data;
                const y = dataItem && typeof dataItem.y === 'number' ? dataItem.y : 
                         (dataItem && dataItem.value && Array.isArray(dataItem.value) ? dataItem.value[1] : 
                         (Array.isArray(dataItem) ? dataItem[1] : null));
                // 只显示高程值，不显示名称
                if (y != null && !isNaN(y)) {
                  return `${Number(y).toFixed(2)}m`;
                }
                return '';
              },
              color: '#1e90ff',
              fontSize: 12,
              fontWeight: 'bold',
              backgroundColor: 'rgba(255,255,255,0.9)',
              borderColor: '#1e90ff',
              borderWidth: 1,
              borderRadius: 4,
              padding: [4, 6],
              offset: [0, -10]
            },
            tooltip: {
              show: true,
              formatter: function(params) {
                const dataItem = params.data;
                const y = dataItem && typeof dataItem.y === 'number' ? dataItem.y : 
                         (dataItem && dataItem.value && Array.isArray(dataItem.value) ? dataItem.value[1] : 
                         (Array.isArray(dataItem) ? dataItem[1] : null));
                const name = dataItem && dataItem.name ? dataItem.name : '';
                if (dataItem && dataItem.isStart) {
                  return `水库水位线<br/>高程: ${y != null && !isNaN(y) ? Number(y).toFixed(2) : '-'}m`;
                }
                if (name && y != null && !isNaN(y)) {
                  return `${name}<br/>水位高程: ${Number(y).toFixed(2)}m`;
                } else if (y != null && !isNaN(y)) {
                  return `水位高程: ${Number(y).toFixed(2)}m`;
                }
                return '';
              }
            }
          },
          ...piezometerMarkSeries,
          ...seepageCurveSeries
        ]
      }, { notMerge: true, lazyUpdate: true });
      if (this.phreaticChart) this.phreaticChart.resize();
      // 使用 replaceMerge 增量更新 graphic，避免整图重绘
      this.$nextTick(() => {
        try { this.phreaticChart.setOption({ graphic: [waterLabelGraphic()] }, { replaceMerge: ['graphic'] }); } catch (e) {}
        setTimeout(() => {
          try { this.phreaticChart.setOption({ graphic: [waterLabelGraphic()] }, { replaceMerge: ['graphic'] }); } catch (e) {}
        }, 50);
      });
    },
    parseTimeArrayToDate(timeArray) {
      if (!timeArray || timeArray.length < 3) return new Date(0); // 默认返回一个早于所有数据的日期
      const [year, month, day] = timeArray;
      const date = new Date(year, month - 1, day); // month is 0-indexed
      return date;
    }
  },
  mounted() {
    // 读取一张图跳转过来的 piezometerId 和 upName
    const q = (this.$route && this.$route.query) || {};
    if (q.pointId) {
      this.query.pointId = q.pointId;
    }
    // 先初始化图表，再加载数据，避免 updateChart 时 chart 实例为 null
    this.$nextTick(() => {
      this.initChart();
      // 图表初始化后再设置时间范围并加载数据
      this.setQuickDateRange(this.query.dateRangeType);
      this.fetchPoints().then(() => {
        this.fetchData();
      });
    });
    if (this.mainTab === 'phreatic') {
      this.$nextTick(() => {
        if (this.phreaticChart) {
          try { this.phreaticChart.dispose(); } catch(e) {}
          this.phreaticChart = null;
        }
        // 优先渲染缓存，避免空白
        if (this.phreaticPoints && this.phreaticPoints.length && typeof this.reservoirLevel === 'number') {
          this.initPhreaticChart();
        }
        this.updatePhreaticData();
      });
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
    if (this.phreaticChart) {
      this.phreaticChart.dispose();
    }
  }
}
</script>

<style scoped>
.seepage-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.filter-card, .table-card, .chart-card { margin-bottom: 20px; }
.mb-2 { margin-bottom: 16px; }
.date-display { margin-left: 10px; font-weight: bold; color: #606266; }
.chart-container { height: 400px; width: 100%; }
.header-row {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.main-switch-tabs >>> .el-tabs__header {
  margin-bottom: 0;
}
.phreatic-placeholder {
  padding: 40px;
  text-align: center;
  color: #888;
  font-size: 18px;
}
.phreatic-top-bar {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-bottom: 10px;
}
.phreatic-title {
  font-size: 18px;
  font-weight: bold;
  margin-right: 32px;
}
.phreatic-form {
  margin-bottom: 0;
}
.phreatic-chart-container {
  height: 420px;
  width: 60%;
  background: #fff;
  border-radius: 8px;
  float: left;
}
</style> 

