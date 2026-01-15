<template>
  <div>
    <el-table :data="PIList" border stripe style="width: 100%" :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index" />
      <el-table-column prop="position" label="预警地点" align="center" />
      <el-table-column prop="type" label="预警类型" align="center" />
      <el-table-column prop="level" label="预警等级" align="center" />
      <el-table-column prop="content" label="预警内容" align="center" />
      <el-table-column prop="status" label="预警状态" align="center" />
      <el-table-column prop="project" label="所属工程" align="center" />
      <el-table-column prop="startTime" label="发生时间" align="center" width="150" />
      <el-table-column prop="overTime" label="解除时间" align="center" width="150" />
      <el-table-column prop="stayTime" label="持续时长" align="center" />
      <el-table-column label="操作" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === '未解除'"
            size="mini"
            type="danger"
            @click="handleRelieve(scope.row)"
          >解除预警</el-button>
          <span v-else style="color: #aaa;">已解除</span>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 10px;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PrewarningTableForMap',
  data() {
    return {
      PIList: [],
      total: 0,
      queryinfo: {
        currentPage: 1,
        pageSize: 10
      }
    }
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      const { data: res } = await this.$http.get('/warning-information/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize
        }
      });
      if (res.code === 200 && res.data && res.data.records) {
        this.PIList = res.data.records;
        this.total = res.data.total;
      } else {
        this.PIList = [];
        this.total = 0;
      }
    },
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize;
      this.fetchData();
    },
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage;
      this.fetchData();
    },
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1;
    },
    async handleRelieve(row) {
      const { data: res } = await this.$http.post('/warning-information/update', {
        ...row,
        status: '已解除'
      });
      if (res.code === 200) {
        this.$message.success('解除成功');
        this.fetchData();
        this.$emit('warning-changed');
      } else {
        this.$message.error('解除失败');
      }
    }
  }
}
</script>

<style scoped>
.el-table th, .el-table td {
  font-size: 15px;
  padding: 6px 0;
}
</style> 