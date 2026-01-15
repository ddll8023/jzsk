<template>
  <div class="warning-table-for-map">
    <el-table :data="tableData" border stripe style="width: 100%" :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="position" label="预警地点" align="center" />
      <el-table-column prop="type" label="预警类型" align="center" />
      <el-table-column prop="level" label="预警等级" align="center" />
      <el-table-column prop="content" label="预警内容" align="center" />
      <el-table-column prop="status" label="预警状态" align="center" />
      <el-table-column prop="startTime" label="发生时间" align="center" width="150" />
      <el-table-column prop="overTime" label="解除时间" align="center" width="150" />
      <el-table-column prop="stayTime" label="持续时长" align="center" />
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'WarningTableForMap',
  data() {
    return {
      tableData: []
    }
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const { data: res } = await this.$http.get('/warning-information/list', {
          params: { currentPage: 1, pageSize: 100 }
        });
        if (res.code === 200 && res.data && res.data.records) {
          this.tableData = res.data.records;
        } else {
          this.tableData = [];
        }
      } catch (e) {
        this.tableData = [];
      }
    }
  }
}
</script>

<style scoped>
.warning-table-for-map {
  width: 100%;
  padding: 0 0 10px 0;
}
.el-table th, .el-table td {
  font-size: 15px;
  padding: 6px 0;
}
</style> 