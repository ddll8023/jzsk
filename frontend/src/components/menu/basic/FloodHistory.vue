<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">历史洪水灾害情况</div>
      <el-form :inline="true" :model="searchForm" class="search-form" style="margin-bottom: 16px;">
        <el-form-item label="日期">
          <el-date-picker v-model="searchForm.date" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 180px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="filteredTableData" border stripe style="width: 100%">
        <el-table-column prop="date" label="日期" width="120" align="center" />
        <el-table-column prop="year" label="年份" width="100" align="center" />
        <el-table-column prop="peak" label="洪峰流量(m³/s)" width="140" align="center" />
        <el-table-column prop="rainfall" label="降雨量(mm)" width="120" align="center" />
        <el-table-column prop="loss" label="受灾情况" width="200" align="center" />
        <el-table-column prop="economicLoss" label="经济损失" width="120" align="center" />
        <el-table-column prop="evacuated" label="转移安置人数" width="120" align="center" />
        <el-table-column prop="deathCount" label="死亡人数" width="100" align="center" />
        <el-table-column prop="remark" label="备注" width="180" align="center" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'FloodHistory',
  data() {
    return {
      tableData: [
        { date: '2021-07-15', year: '2021', peak: 3200, rainfall: 180, loss: '农田2万亩被淹', economicLoss: '1.2亿', evacuated: 1500, deathCount: 2, remark: '有预警' },
        { date: '2020-08-10', year: '2020', peak: 2800, rainfall: 150, loss: '房屋受损120间', economicLoss: '0.9亿', evacuated: 800, deathCount: 1, remark: '' },
        { date: '2019-06-22', year: '2019', peak: 2500, rainfall: 120, loss: '道路中断3天', economicLoss: '0.7亿', evacuated: 600, deathCount: 0, remark: '' },
        { date: '2018-09-05', year: '2018', peak: 2100, rainfall: 100, loss: '农田被淹', economicLoss: '0.5亿', evacuated: 300, deathCount: 0, remark: '' },
        { date: '2017-07-18', year: '2017', peak: 1800, rainfall: 90, loss: '房屋受损', economicLoss: '0.3亿', evacuated: 200, deathCount: 0, remark: '' },
        { date: '2016-08-12', year: '2016', peak: 1600, rainfall: 80, loss: '农田被淹', economicLoss: '0.2亿', evacuated: 100, deathCount: 0, remark: '' },
        { date: '2015-07-20', year: '2015', peak: 1400, rainfall: 70, loss: '农田被淹', economicLoss: '0.1亿', evacuated: 80, deathCount: 0, remark: '' },
        { date: '2014-08-15', year: '2014', peak: 1200, rainfall: 60, loss: '道路中断', economicLoss: '0.08亿', evacuated: 60, deathCount: 0, remark: '' },
        { date: '2013-09-10', year: '2013', peak: 1000, rainfall: 50, loss: '房屋受损', economicLoss: '0.05亿', evacuated: 40, deathCount: 0, remark: '' }
      ],
      searchForm: {
        date: ''
      },
      filteredTableData: []
    }
  },
  methods: {
    handleSearch() {
      this.filteredTableData = this.tableData.filter(item => {
        if (this.searchForm.date) {
          return item.date === this.searchForm.date;
        }
        return true;
      });
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
</style> 