<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">洪水影响情况</div>
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
        <el-table-column prop="region" label="影响区域" width="180" align="center" />
        <el-table-column prop="maxFlood" label="历史最大洪水" width="180" align="center" />
        <el-table-column prop="population" label="影响人口" width="120" align="center" />
        <el-table-column prop="economicLoss" label="经济损失" width="120" align="center" />
        <el-table-column prop="disasterType" label="主要灾害类型" width="200" align="center" />
        <el-table-column prop="rescueCount" label="救援次数" width="100" align="center" />
        <el-table-column prop="evacuated" label="转移安置人数" width="120" align="center" />
        <el-table-column prop="remark" label="备注" width="180" align="center" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'FloodImpact',
  data() {
    return {
      searchForm: {
        date: ''
      },
      tableData: [
        { date: '2021-07-15', region: '荆竹镇', maxFlood: '3200m³/s', population: '3万人', economicLoss: '1.5亿元', disasterType: '农田被淹、道路中断', rescueCount: 5, evacuated: 1200, remark: '及时预警' },
        { date: '2020-08-10', region: '李家村', maxFlood: '2800m³/s', population: '2万人', economicLoss: '1.1亿元', disasterType: '房屋受损、道路中断', rescueCount: 3, evacuated: 800, remark: '部分村庄受灾' },
        { date: '2019-06-22', region: '南河村', maxFlood: '2500m³/s', population: '1.5万人', economicLoss: '0.8亿元', disasterType: '农田被淹', rescueCount: 2, evacuated: 500, remark: '' },
        { date: '2018-09-05', region: '荆竹镇', maxFlood: '2100m³/s', population: '1万人', economicLoss: '0.5亿元', disasterType: '道路中断', rescueCount: 1, evacuated: 300, remark: '' },
        { date: '2017-07-18', region: '李家村', maxFlood: '1800m³/s', population: '0.8万人', economicLoss: '0.3亿元', disasterType: '房屋受损', rescueCount: 1, evacuated: 200, remark: '' },
        { date: '2016-08-12', region: '南河村', maxFlood: '1600m³/s', population: '0.5万人', economicLoss: '0.2亿元', disasterType: '农田被淹', rescueCount: 1, evacuated: 100, remark: '' },
        { date: '2015-07-20', region: '荆竹镇', maxFlood: '1400m³/s', population: '0.3万人', economicLoss: '0.1亿元', disasterType: '农田被淹', rescueCount: 1, evacuated: 50, remark: '' },
        { date: '2014-08-15', region: '李家村', maxFlood: '1200m³/s', population: '0.2万人', economicLoss: '0.08亿元', disasterType: '道路中断', rescueCount: 1, evacuated: 30, remark: '' },
        { date: '2013-09-10', region: '南河村', maxFlood: '1000m³/s', population: '0.1万人', economicLoss: '0.05亿元', disasterType: '房屋受损', rescueCount: 1, evacuated: 20, remark: '' }
      ]
    }
  },
  computed: {
    filteredTableData() {
      if (this.searchForm.date) {
        return this.tableData.filter(item => item.date === this.searchForm.date);
      }
      return this.tableData;
    }
  },
  methods: {
    handleSearch() {
      // Implementation of handleSearch method
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
</style> 