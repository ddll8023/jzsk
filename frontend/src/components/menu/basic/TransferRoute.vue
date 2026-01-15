<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">安置点及转移路线</div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="site" label="安置点" width="180" />
        <el-table-column prop="capacity" label="容量(人)" />
        <el-table-column prop="route" label="转移路线" />
        <el-table-column prop="person" label="负责人" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'TransferRoute',
  data() {
    return {
      tableData: [
        { site: '荆竹小学', capacity: 500, route: '荆竹村-小学', person: '张三' },
        { site: '李家村委会', capacity: 300, route: '李家村-村委会', person: '李四' },
        { site: '南河活动中心', capacity: 200, route: '南河村-活动中心', person: '王五' }
      ]
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
</style> 