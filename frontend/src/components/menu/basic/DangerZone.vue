<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">危险区</div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="name" label="危险区名称" width="180" />
        <el-table-column prop="area" label="范围" />
        <el-table-column prop="population" label="人口" />
        <el-table-column prop="level" label="风险等级" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'DangerZone',
  data() {
    return {
      tableData: [
        { name: '荆竹村', area: '河道两侧', population: '1200', level: '高' },
        { name: '李家村', area: '低洼地带', population: '800', level: '中' },
        { name: '南河村', area: '库区下游', population: '600', level: '高' }
      ]
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
</style> 