<template>
  <div>
    <el-card>
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="openDialog">新增</el-button>
      </div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="上报时间" label="上报时间"/>
        <el-table-column prop="上报人" label="上报人"/>
        <el-table-column prop="内容" label="内容"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" @click="editRow(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form :model="form" label-width="80px">
        <el-form-item label="上报时间">
          <el-date-picker v-model="form.上报时间" type="datetime" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="上报人">
          <el-input v-model="form.上报人" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.内容" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增',
      form: {}
    }
  },
  methods: {
    openDialog() {
      this.dialogTitle = '新增'
      this.form = {}
      this.dialogVisible = true
    },
    editRow(row) {
      this.dialogTitle = '编辑'
      this.form = { ...row }
      this.dialogVisible = true
    },
    submitForm() {
      if (this.form.id) {
        // 编辑
        const idx = this.tableData.findIndex(i => i.id === this.form.id)
        if (idx !== -1) this.$set(this.tableData, idx, { ...this.form })
      } else {
        // 新增
        this.form.id = Date.now()
        this.tableData.push({ ...this.form })
      }
      this.dialogVisible = false
    },
    deleteRow(row) {
      this.$confirm('确定删除？').then(() => {
        this.tableData = this.tableData.filter(i => i.id !== row.id)
      })
    }
  }
}
</script> 