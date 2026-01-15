<template>
  <div>
    <el-card>
      <div style="margin-bottom: 16px; display: flex; align-items: center;">
        <span style="font-size: 18px; font-weight: bold;">预警设施</span>
        <el-button type="primary" size="small" @click="showAddDialog = true" style="margin-left: auto;">新增</el-button>
      </div>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column prop="facilityName" label="设施名称" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="manager" label="负责人" />
        <el-table-column prop="lastUpdate" label="最后维护时间">
          <template slot-scope="scope">{{ formatDateTime(null, null, scope.row.lastUpdate) }}</template>
        </el-table-column>
        <el-table-column prop="recordTime" label="建档时间">
          <template slot-scope="scope">{{ formatDateTime(null, null, scope.row.recordTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 16px; text-align:right;"
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="currentPage"
        @current-change="fetchData"
        @size-change="handleSizeChange"
      />
    </el-card>
    <!-- 新增弹窗 -->
    <el-dialog title="新增预警设施" :visible.sync="showAddDialog" width="500px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="设施名称"><el-input v-model="addForm.facilityName" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="addForm.type" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="addForm.location" /></el-form-item>
        <el-form-item label="状态"><el-input v-model="addForm.status" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="addForm.manager" /></el-form-item>
        <el-form-item label="最后维护时间">
          <el-date-picker v-model="addForm.lastUpdate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">确定</el-button>
      </div>
    </el-dialog>
    <!-- 编辑弹窗 -->
    <el-dialog title="编辑预警设施" :visible.sync="showEditDialog" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="设施名称"><el-input v-model="editForm.facilityName" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="editForm.type" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="editForm.location" /></el-form-item>
        <el-form-item label="状态"><el-input v-model="editForm.status" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="editForm.manager" /></el-form-item>
        <el-form-item label="最后维护时间">
          <el-date-picker v-model="editForm.lastUpdate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleEdit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'WarningFacility',
  data() {
    return {
      tableData: [],
      showAddDialog: false,
      showEditDialog: false,
      addForm: {
        facilityName: '',
        type: '',
        location: '',
        status: '',
        manager: '',
        lastUpdate: ''
      },
      editForm: {
        facilityName: '',
        type: '',
        location: '',
        status: '',
        manager: '',
        lastUpdate: ''
      },
      total: 0,
      pageSize: 10,
      currentPage: 1
    }
  },
  methods: {
    async fetchData(page = this.currentPage) {
      const { data: res } = await this.$http.get('/warning-facilities/list', {
        params: { current: page, pageSize: this.pageSize }
      });
      if (res && res.records) {
        this.tableData = res.records;
        this.total = res.total;
      } else if (Array.isArray(res)) {
        this.tableData = res;
        this.total = res.length;
      } else if (res && Array.isArray(res.data)) {
        this.tableData = res.data;
        this.total = res.data.length;
      } else {
        this.tableData = [];
        this.total = 0;
      }
      this.currentPage = page;
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.fetchData(1);
    },
    formatDateTime(row, column, cellValue) {
      const val = cellValue !== undefined ? cellValue : (row && row[column && column.property]);
      if (!val) return '-';
      let d;
      if (Array.isArray(val)) {
        const [y, M, D, h = 0, m = 0, s = 0] = val;
        d = new Date(y, M - 1, D, h, m, s);
      } else if (typeof val === 'object' && val && val.year) {
        const { year, monthValue, dayOfMonth, hour = 0, minute = 0, second = 0 } = val;
        d = new Date(year, monthValue - 1, dayOfMonth, hour, minute, second);
      } else if (typeof val === 'string') {
        d = new Date(val.replace('T', ' '));
      } else if (typeof val === 'number') {
        d = new Date(val);
      } else {
        return String(val);
      }
      if (isNaN(d.getTime())) return String(val);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    async handleAdd() {
      const { data: res } = await this.$http.post('/warning-facilities/add', this.addForm);
      if (res === true || (res && res.code === 200)) {
        this.$message.success('新增成功');
        this.showAddDialog = false;
        this.fetchData(1);
        this.addForm = { facilityName: '', type: '', location: '', status: '', manager: '', lastUpdate: '' };
      } else {
        this.$message.error('新增失败');
      }
    },
    openEditDialog(row) {
      this.editForm = {
        facilityName: '',
        type: '',
        location: '',
        status: '',
        manager: '',
        lastUpdate: '',
        ...row
      };
      this.showEditDialog = true;
    },
    async handleEdit() {
      const { data: res } = await this.$http.put('/warning-facilities/update', this.editForm);
      if (res === true || (res && res.code === 200)) {
        this.$message.success('编辑成功');
        this.showEditDialog = false;
        this.fetchData(1);
      } else {
        this.$message.error('编辑失败');
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该设施吗？', '提示', { type: 'warning' });
        const { data: res } = await this.$http.delete(`/warning-facilities/delete/${row.id}`);
        if (res === true || (res && res.code === 200)) {
          this.$message.success('删除成功');
          this.fetchData(1);
        } else {
          this.$message.error('删除失败');
        }
      } catch (e) {}
    }
  },
  mounted() {
    this.fetchData();
  }
}
</script>
<style scoped>
.el-table th, .el-table td { font-size: 15px; }
</style> 