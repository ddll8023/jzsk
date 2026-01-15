<template>
  <div id="div1">
    <el-row>
      <!-- 筛选区 -->
      <el-col :span="24" class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="选择测站">
            <el-select v-model="searchForm.stationId" placeholder="请选择测站" style="width: 220px" @change="handleStationChange">
              <el-option v-for="station in stationList" :key="station.id" :label="station.name" :value="station.id" />
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
      <!-- 右侧内容区 -->
      <el-col :span="24" class="right-panel">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-title">渗流量过程线</div>
          <div ref="chart" style="width: 100%; height: 320px;"></div>
        </el-card>
        <el-card shadow="hover" class="table-card">
          <div slot="header" class="table-title">渗流量数据</div>
          <el-table :data="pagedData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;" v-loading="loading">
            <el-table-column prop="stationName" label="测站名称" align="center"></el-table-column>
            <el-table-column prop="formattedTime" label="时间" align="center"></el-table-column>
            <el-table-column prop="q1" label="流量(L/s)" align="center">
              <template slot-scope="scope">
                {{ (Number(scope.row.q1) * 1000).toFixed(3) }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredData.length"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            style="margin-top: 16px; text-align: right;"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'

export default {
  name: 'SeepageFlowMonitor',
  data() {
    return {
      allTableData: [], // 存储所有从后端获取的原始数据
      tableData: [], // 存储当前过滤后的数据
      chart: null,
      pageSize: 10,
      currentPage: 1,
      serverTotal: 0,
      loading: false,
      chartData: [],
      stationList: [
        { id: '4211822043', name: '主坝0+400坝脚量水堰' },
        { id: '4211823043', name: '主坝0+200坝脚量水堰' }
      ],
      searchForm: {
        stationId: '4211823043', // 默认选择0+200测站
        dateRange: [] // 存储日期范围，格式 [开始时间, 结束时间]
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一天',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24); // 24小时前
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
      }
    }
  },
  computed: {
    filteredData() {
      // 根据选择的测站和时间范围筛选数据
      const withinRange = (item) => {
        if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) return true;
        const [startDateStr, endDateStr] = this.searchForm.dateRange;
        const startDate = new Date(startDateStr.replace(/-/g, '/'));
        const endDate = new Date(endDateStr.replace(/-/g, '/'));
        const itemTime = this.parseTimeArrayToDate(item.tm);
        return itemTime >= startDate && itemTime <= endDate;
      };

      // 根据测站ID筛选
      const matchStation = (item) => {
        if (!this.searchForm.stationId) return false; // 必须选择测站
        const itemId = String(item.id || item.stcd || item.stationId || '');
        return itemId === String(this.searchForm.stationId);
      };

      return this.allTableData.filter(item => {
        if (!withinRange(item)) return false;
        if (!matchStation(item)) return false;
        const q1Val = Number(item.q1);
        const itemId = String(item.id || item.stcd || item.stationId || '');
        // 对于0+400测站（ID: 4211822043），即使q1=0也要显示
        // 对于其他测站，只显示q1 > 0的数据
        if (itemId === '4211822043') {
          return !isNaN(q1Val) && q1Val >= 0; // 0+400测站显示q1>=0的数据
        } else {
          return !isNaN(q1Val) && q1Val > 0; // 其他测站只显示q1>0的数据
        }
      });
    },
    pagedData() {
      const sortedData = [...this.filteredData].sort((a, b) => this.parseTimeArrayToDate(b.tm) - this.parseTimeArrayToDate(a.tm));
      const start = (this.currentPage - 1) * this.pageSize
      return sortedData.slice(start, start + this.pageSize)
    }
  },
  mounted() {
    // 默认显示最近一天的数据
    this.setQuickDateRange('最近一天');
    this.fetchData();
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
    async fetchData() {
      // 使用 /st-rivers-r/page 分页接口获取流量数据
      this.loading = true;
      try {
        const fetchSize = 200; // 单次抓取条数
        let total = Infinity;
        let accumulatedAll = [];
        let currentFetchPage = 1;

        const withinRange = (item) => {
          if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) return true;
          const [startDateStr, endDateStr] = this.searchForm.dateRange;
          const startDate = new Date(startDateStr.replace(/-/g, '/'));
          const endDate = new Date(endDateStr.replace(/-/g, '/'));
          const itemTime = this.parseTimeArrayToDate(item.tm);
          return itemTime >= startDate && itemTime <= endDate;
        };

        const pushWithFilter = (list) => {
          list.forEach(raw => {
            if (!withinRange(raw)) return;
            const q1Val = Number(raw.q1);
            
            // 根据测站ID确定测站名称（支持多种字段名：id, stcd, stationId等）
            const stationId = String(raw.id || raw.stcd || raw.stationId || '');
            
            // 对于0+400测站（ID: 4211822043），即使q1=0也要添加
            // 对于其他测站，只添加q1 > 0的数据
            if (stationId === '4211822043') {
              // 0+400测站：显示q1>=0的数据
              if (isNaN(q1Val) || q1Val < 0) return;
            } else {
              // 其他测站：只显示q1>0的数据
              if (isNaN(q1Val) || q1Val <= 0) return;
            }
            
            let stationName = '';
            if (stationId) {
              const station = this.stationList.find(s => String(s.id) === stationId);
              if (station) {
                stationName = station.name;
              } else {
                // 如果找不到匹配的测站，根据ID判断
                if (stationId === '4211822043') {
                  stationName = '主坝0+400坝脚量水堰';
                } else if (stationId === '4211823043') {
                  stationName = '主坝0+200坝脚量水堰';
                } else {
                  stationName = `测站${stationId}`;
                }
              }
            } else {
              // 如果没有测站ID，使用默认名称
              stationName = '未知测站';
            }
            
            accumulatedAll.push({
              ...raw,
              formattedTime: this.formatTime(raw.tm),
              stationName: stationName,
              stationId: stationId
            });
          });
        };

        // 循环抓取数据，直到获取足够的数据或没有更多数据
        while ((currentFetchPage - 1) * fetchSize < total) {
          const res = await axios.get('/st-rivers-r/page', { params: { page: currentFetchPage, size: fetchSize } });
          const pageData = res && res.data ? res.data : {};
          const records = Array.isArray(pageData.records) ? pageData.records : [];
          total = Number(pageData.total || 0);
          this.serverTotal = total;
          
          if (records.length === 0) break;
          
          pushWithFilter(records);
          
          // 如果已经获取了足够的数据（覆盖一天），可以提前结束
          if (accumulatedAll.length >= 100) break;
          
          currentFetchPage += 1;
        }

        // 设置组件状态
        this.allTableData = accumulatedAll;
        this.updateChart();
        
        if (accumulatedAll.length > 0) {
          this.$message.success(`数据加载成功，共${this.filteredData.length}条渗流量数据`);
        } else {
          this.$message.warning('未找到符合条件的渗流量数据（q1 > 0）');
        }
      } catch (error) {
        console.error("Error fetching seepage flow data:", error);
        this.allTableData = [];
        this.tableData = [];
        this.$nextTick(() => this.renderChart([]));
        this.$message.error('数据加载失败，请检查网络连接');
      } finally {
        this.loading = false;
      }
    },

    handleStationChange() {
      this.currentPage = 1;
      // 测站改变时更新图表显示
      this.updateChart();
    },
    handleSearch() {
      this.currentPage = 1;
      this.fetchData();
    },
    updateChart() {
      // 根据筛选后的数据更新图表
      const sortedData = [...this.filteredData].sort((a, b) => this.parseTimeArrayToDate(a.tm) - this.parseTimeArrayToDate(b.tm));
      this.chartData = sortedData;
      this.$nextTick(() => this.renderChart(sortedData));
    },
    exportData() {
      if (this.filteredData.length === 0) {
        this.$message.warning('没有数据可导出！');
        return;
      }

      const headers = ['测站名称', '时间', '流量(L/s)'];
      const rows = this.filteredData.map(item => [
        item.stationName,
        item.formattedTime,
        (Number(item.q1) * 1000).toFixed(3)
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
        link.setAttribute("download", "渗流量监测数据.csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
      }
      this.$message.success('数据已导出！');
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
      
      // 生成等间隔时间轴（每半小时一个点）
      const timeAxis = this.generateTimeAxis(data);
      const flowData = this.generateFlowData(data, timeAxis);
      
      this.chart.setOption({
        title: { text: '', left: 'center' },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (!params.length) return '';
            const item = params[0];
            const time = item.axisValue;
            const val = item.data !== null && item.data !== undefined ? item.data + ' L/s' : '-';
            return `${time}<br/>流量: ${val}`;
          }
        },
        grid: { left: 40, right: 30, top: 40, bottom: 40 },
        xAxis: {
          type: 'category',
          data: timeAxis,
          axisLabel: { rotate: 0, fontSize: 12 }
        },
        yAxis: {
          type: 'value',
          name: '流量(L/s)',
          nameTextStyle: { fontSize: 14 },
          axisLabel: { fontSize: 12 },
          min: 0,
        },
        series: [{
          name: '流量',
          type: 'line',
          data: flowData,
          smooth: true,
          symbol: 'circle',
          symbolSize: 8, 
          lineStyle: { width: 3, color: '#67C23A' }, 
          itemStyle: { color: '#67C23A' },
          areaStyle: { color: 'rgba(103,194,58,0.1)' },
          connectNulls: true
        }]
      });
    },
    
    formatTime(timeArr) {
      if (Array.isArray(timeArr) && timeArr.length >= 5) {
        const [y, m, d, h, min] = timeArr
        return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`
      }
      return ''
    },
    parseTimeArrayToDate(timeArr) {
      if (Array.isArray(timeArr) && timeArr.length >= 5) {
        return new Date(timeArr[0], timeArr[1] - 1, timeArr[2], timeArr[3], timeArr[4], timeArr.length > 5 ? timeArr[5] : 0);
      }
      return new Date(0); 
    },
    
    // 生成等间隔时间轴（每半小时一个点）
    generateTimeAxis(data) {
      // 优先使用选择的时间范围，否则使用最近一天
      let endTime, startTime;
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        startTime = new Date(this.searchForm.dateRange[0].replace(/-/g, '/'));
        endTime = new Date(this.searchForm.dateRange[1].replace(/-/g, '/'));
      } else {
        endTime = new Date();
        startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000);
      }

      // 起点对齐：小于30分取0分，否则取30分
      startTime.setSeconds(0, 0);
      startTime.setMinutes(startTime.getMinutes() < 30 ? 0 : 30);

      // 终点对齐：小于30分取30分，否则进位到下个整点
      const endM = endTime.getMinutes();
      endTime.setSeconds(0, 0);
      if (endM < 30) {
        endTime.setMinutes(30);
      } else {
        endTime.setMinutes(0);
        endTime.setHours(endTime.getHours() + 1);
      }
      
      const timeAxis = [];
      const current = new Date(startTime);
      
      while (current <= endTime) {
        timeAxis.push(this.formatTime([
          current.getFullYear(),
          current.getMonth() + 1,
          current.getDate(),
          current.getHours(),
          current.getMinutes()
        ]));
        current.setMinutes(current.getMinutes() + 30);
      }
      
      return timeAxis;
    },
    
    // 生成对应的流量数据
    generateFlowData(data, timeAxis) {
      if (!data.length || !timeAxis.length) return [];
      
      // 创建数据映射
      const dataMap = {};
      data.forEach(item => {
        const timeKey = item.formattedTime;
        const q1 = Number(item.q1);
        // 对于0+400测站，即使q1=0也要显示；对于其他测站，只显示q1>0
        const stationId = String(item.stationId || item.id || item.stcd || '');
        if (!isNaN(q1)) {
            if (stationId === '4211822043') {
            // 0+400测站：显示q1>=0，转换为L/s
            if (q1 >= 0) {
              dataMap[timeKey] = q1 * 1000; // 转换为L/s
            }
          } else {
            // 其他测站：只显示q1>0，转换为L/s
            if (q1 > 0) {
              dataMap[timeKey] = q1 * 1000; // 转换为L/s
            }
          }
        }
      });
      
      // 生成等间隔的流量数据
      const flowData = [];
      let lastValue = null;
      
      timeAxis.forEach(timeKey => {
        if (dataMap[timeKey] !== undefined) {
          lastValue = dataMap[timeKey];
          flowData.push(lastValue);
        } else {
          // 没有数据时使用前一个值，或者null
          flowData.push(lastValue);
        }
      });

      // 若前段仍为 null，用后面首次出现的值回填，保证整天连续
      let firstNonNull = null;
      for (let i = 0; i < flowData.length; i++) {
        if (flowData[i] != null) { firstNonNull = flowData[i]; break; }
      }
      if (firstNonNull != null) {
        for (let i = 0; i < flowData.length && flowData[i] == null; i++) {
          flowData[i] = firstNonNull;
        }
      }
      
      return flowData;
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
    },
    handleCurrentChange(page) {
      this.currentPage = page
    }
  }
}
</script>

<style scoped>
#div1 { height: 100%; width: 100%; background: #f4f6fa; padding: 20px; box-sizing: border-box; }
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
.chart-card, .table-card { margin-bottom: 20px; box-shadow: 0 2px 8px #e4e7ed22; border-radius: 8px; }
.chart-title, .table-title { font-size: 16px; font-weight: bold; color: #222; padding: 10px 0; border-bottom: 1px solid #eee; margin-bottom: 10px; }
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
</style>

