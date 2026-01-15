<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">响应单位和责任人信息</div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="unit" label="单位名称" width="180" />
        <el-table-column prop="person" label="责任人" />
        <el-table-column prop="phone" label="联系方式" />
        <el-table-column prop="duty" label="职责" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'Responsibility',
  data() {
    return {
      tableData: [
        { unit: '荆竹水库管理局', person: '赵六', phone: '13800001111', duty: '水库调度与管理' },
        { unit: '荆竹镇政府', person: '钱七', phone: '13800002222', duty: '应急响应与群众转移' },
        { unit: '南河村委会', person: '孙八', phone: '13800003333', duty: '村民通知与安置' }
      ]
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
</style> 