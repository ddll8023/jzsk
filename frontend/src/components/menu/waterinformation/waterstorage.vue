<template>
  <div id="div1">
    <el-row>
      <!-- 筛选区 -->
      <el-col :span="24" class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
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
          <div slot="header" class="chart-title">水库水位过程线</div>
          <div ref="chart" style="width: 100%; height: 320px;"></div>
        </el-card>
        <el-card shadow="hover" class="table-card">
          <div slot="header" class="table-title">水库水位数据</div>
          <el-table :data="pagedData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
            <el-table-column prop="stationName" label="测站名称" align="center"></el-table-column>
            <el-table-column prop="formattedTime" label="时间" align="center"></el-table-column>
            <el-table-column prop="z1" label="水位(m)" align="center"></el-table-column>
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
      searchForm: {
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
      // 仅保留每个时间点中 z1 > 50 的那条记录
      const withinRange = (item) => {
        if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) return true;
        const [startDateStr, endDateStr] = this.searchForm.dateRange;
        const startDate = new Date(startDateStr);
        const endDate = new Date(endDateStr);
        const itemTime = this.parseTimeArrayToDate(item.tm);
        return itemTime >= startDate && itemTime <= endDate;
      };

      const mapByTime = new Map();
      this.allTableData.forEach(item => {
        if (!withinRange(item)) return;
        const timeKey = this.formatTime(item.tm);
        const z1Val = Number(item.z1);
        if (z1Val <= 50) return;
        if (!mapByTime.has(timeKey)) {
          mapByTime.set(timeKey, item);
        }
      });
      return Array.from(mapByTime.values());
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
    async fetchData(page = this.currentPage, size = this.pageSize) {
      // 拉取最近一天（或所选范围）需要的完整数据用于图表；分页仅影响表格切片，不再触发重新请求
      this.loading = true;
      try {
        const fetchSize = 200; // 单次抓取条数
        let total = Infinity;
        let accumulatedAll = [];
        let currentFetchPage = 1;

        const withinRange = (item) => {
          if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) return true;
          const [startDateStr, endDateStr] = this.searchForm.dateRange;
          const startDate = new Date(startDateStr);
          const endDate = new Date(endDateStr);
          const itemTime = this.parseTimeArrayToDate(item.tm);
          return itemTime >= startDate && itemTime <= endDate;
        };

        const mapByTime = new Map();
        const pushWithFilter = (list) => {
          list.forEach(raw => {
            if (!withinRange(raw)) return;
            const timeKey = this.formatTime(raw.tm);
            const z1Val = Number(raw.z1);
            if (z1Val <= 50) return;
            if (!mapByTime.has(timeKey)) {
              mapByTime.set(timeKey, {
                ...raw,
                formattedTime: this.formatTime(raw.tm),
                stationName: '坝前雨量水位站（新站）'
              });
            }
          });
          accumulatedAll = Array.from(mapByTime.values());
        };

        // 期望覆盖“最近一天”范围的点位（每半小时一个点，约 49 个）
        const expectedPoints = 49;
        // 循环抓取直到覆盖一天所需点位或没有更多数据
        while (accumulatedAll.length < expectedPoints && (currentFetchPage - 1) * fetchSize < total) {
          const res = await axios.get('/st-rivers-r/page', { params: { page: currentFetchPage, size: fetchSize } });
          const pageData = res && res.data ? res.data : {};
          const records = pageData.records || [];
          total = Number(pageData.total || 0);
          this.serverTotal = total; // 显示后端总记录数（未过滤）
          pushWithFilter(records);
          currentFetchPage += 1;
        }

        // 设置组件状态
        this.currentPage = page;
        this.pageSize = size;
        this.allTableData = accumulatedAll; // 用完整一天的数据作为表格过滤源
        this.chartData = [...accumulatedAll].sort((a, b) => this.parseTimeArrayToDate(a.tm) - this.parseTimeArrayToDate(b.tm));
        this.tableData = this.pagedData;
        this.$nextTick(() => this.renderChart(this.chartData));
      } catch (error) {
        console.error("Error fetching river data:", error);
        this.allTableData = [];
        this.tableData = [];
        this.$nextTick(() => this.renderChart([]));
        this.$message.error('数据加载失败，请检查网络连接');
      } finally {
        this.loading = false;
      }
    },
    

    handleSearch() {
      this.currentPage = 1;
      // 重新拉取覆盖查询范围（默认一天）的完整数据
      this.fetchData(1, this.pageSize);
    },
    exportData() {
      if (this.filteredData.length === 0) {
        this.$message.warning('没有数据可导出！');
        return;
      }

      const headers = ['测站名称', '时间', '水位(m)'];
      const rows = this.filteredData.map(item => [
        item.stationName,
        item.formattedTime,
        item.z1
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
        link.setAttribute("download", "河道水情数据.csv");
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
      
      // 使用传入的过滤后数据
      const chartData = data;
      
      // 生成等间隔时间轴（每半小时一个点）
      const timeAxis = this.generateTimeAxis(chartData);
      const waterLevelData = this.generateWaterLevelData(chartData, timeAxis);
      this.chart.setOption({
        title: { text: '', left: 'center' },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (!params.length) return '';
            const item = params[0];
            const time = item.axisValue;
            const val = item.data !== null ? item.data + 'm' : '-';
            return `${time}<br/>水位: ${val}`;
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
          name: '水位(m)',
          nameTextStyle: { fontSize: 14 },
          axisLabel: { fontSize: 12 },
          min: 0,
        },
        series: [{
          name: '水位',
          type: 'line',
          data: waterLevelData,
          smooth: true,
          symbol: 'circle',
          symbolSize: 8, 
          lineStyle: { width: 3, color: '#409EFF' }, 
          itemStyle: { color: '#409EFF' },
          areaStyle: { color: 'rgba(64,158,255,0.1)' },
          connectNulls: true // 断点连线，保证图线存在
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
    
    // 生成等间隔时间轴（每半小时一个点），默认“当前时刻往前推24小时”，起止对齐整点/半点
    generateTimeAxis(data) {
      // 优先使用选择的时间范围，否则使用最近一天
      let endTime, startTime;
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        startTime = new Date(this.searchForm.dateRange[0]);
        endTime = new Date(this.searchForm.dateRange[1]);
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
    
    // 生成对应的水位数据
    generateWaterLevelData(data, timeAxis) {
      if (!data.length || !timeAxis.length) return [];
      
      // 创建数据映射
      const dataMap = {};
      data.forEach(item => {
        const timeKey = item.formattedTime;
        dataMap[timeKey] = Number(item.z1);
      });
      
      // 生成等间隔的水位数据
      const waterLevelData = [];
      let lastValue = null;
      
      timeAxis.forEach(timeKey => {
        if (dataMap[timeKey] !== undefined) {
          lastValue = dataMap[timeKey];
          waterLevelData.push(lastValue);
        } else {
          // 没有数据时使用前一个值，或者null
          waterLevelData.push(lastValue);
        }
      });

      // 若前段仍为 null，用后面首次出现的值回填，保证整天连续
      let firstNonNull = null;
      for (let i = 0; i < waterLevelData.length; i++) {
        if (waterLevelData[i] != null) { firstNonNull = waterLevelData[i]; break; }
      }
      if (firstNonNull != null) {
        for (let i = 0; i < waterLevelData.length && waterLevelData[i] == null; i++) {
          waterLevelData[i] = firstNonNull;
        }
      }
      
      return waterLevelData;
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      // 改为前端分页：不重新请求，仅调整切片
    },
    handleCurrentChange(page) {
      this.currentPage = page
      // 改为前端分页：不重新请求
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