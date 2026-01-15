<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header">监测站预警指标</div>
      <el-list>
        <el-list-item v-for="(item, idx) in tableData" :key="idx">
          <span class="item-label">{{ item.station }}</span>
          <span class="item-value">{{ item.index }}：{{ item.value }}</span>
        </el-list-item>
      </el-list>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'WarningIndex',
  data() {
    return {
      tableData: [
        { station: '荆竹站', index: '水位', value: '80.0m' },
        { station: '李家站', index: '流量', value: '1500m³/s' },
        { station: '南河站', index: '降雨量', value: '100mm/h' }
      ]
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
.el-list-item { padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
.item-label { font-weight: bold; color: #333; margin-right: 12px; }
.item-value { color: #666; }
</style> 