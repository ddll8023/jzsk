<template>
  <div class="div1">
    <div class="div-bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/departmentmanage' }">部门管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="div-body">
      <!-- <div class="div-body-aside">
        <el-aside width="100%">
          <el-menu :default-openeds="[1]" background-color="rgb(249,249,249)">
            <el-submenu index="1">
              <template slot="title"
                ><i class="el-icon-menu"></i>东部分公司</template>
<el-submenu index="1-1">
  <template slot="title">韦州镇</template>
  <el-menu-item index="1-1-1">甘沟移民村</el-menu-item>
  <el-menu-item index="1-1-2">马庄子村</el-menu-item>
  <el-menu-item index="1-1-3">停沟村</el-menu-item>
  <el-menu-item index="1-1-4">旧庄老村</el-menu-item>
  <el-menu-item index="1-1-5">旧庄三标</el-menu-item>
  <el-menu-item index="1-1-6">旧庄移民村</el-menu-item>
</el-submenu>
</el-submenu>
</el-menu>
</el-aside>
</div> -->

      <div class="div-body-main">
        <div class="div-body-main-header">
          <div style="align-self: center; margin-left: 10px">
            <el-input placeholder="请输入部门名称搜索" v-model="input" class="input-with-select" clearable
              @clear="getDepartmentList">
              <el-button slot="append" icon="el-icon-search" @click="searchDepartment"></el-button>
            </el-input>
          </div>

          <el-button type="primary" round icon="el-icon-plus"
            style="margin-left: auto; align-self: center; margin-right: 10px"
            @click="addDialogVisible = true">新增</el-button>
        </div>

        <div class="div-body-main-body">
          <el-table :data="DepartmentList" border stripe height="100%" style="width: 100%"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="100px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="departmentName" label="部门名称" align="center">
            </el-table-column>
            <el-table-column prop="departmentResponsibility" label="部门职责" align="center">
            </el-table-column>
            <el-table-column prop="level" label="部门级别" align="center">
            </el-table-column>
            <el-table-column prop="company" label="所属公司" align="center">
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center">
              <template slot-scope="scope">
                <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
                <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div id="div-body-main-footer">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增部门" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称:" prop="departmentName">
              <el-input v-model="addForm.departmentName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门职责:" prop="departmentResponsibility">
              <el-input v-model="addForm.departmentResponsibility"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门级别:" prop="level">
              <el-input v-model="addForm.level"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属公司:" prop="company">
              <el-input v-model="addForm.company"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addDepartment">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑部门信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称:" prop="departmentName">
              <el-input v-model="editForm.departmentName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门职责:" prop="departmentResponsibility">
              <el-input v-model="editForm.departmentResponsibility"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门级别:" prop="level">
              <el-input v-model="editForm.level"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属公司:" prop="company">
              <el-input v-model="editForm.company"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editDepartment">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    return {
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      DepartmentList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加表单数据
      addForm: {
        departmentName: '',
        departmentResponsibility: '',
        level: '',
        company: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        departmentName: [
          { required: true, message: '请输入部门名称', trigger: 'blur' }
        ],
        departmentResponsibility: [
          { required: true, message: '请输入部门职责', trigger: 'blur' }
        ]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        departmentName: [
          { required: true, message: '请输入部门名称', trigger: 'blur' }
        ],
        departmentResponsibility: [
          { required: true, message: '请输入部门职责', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //获取数据列表
    async getDepartmentList() {
      const { data: res } = await this.$http.get('/department/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.DepartmentList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/department/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getDepartmentList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getDepartmentList()
    },
    //序号连续
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    //新增部门
    addDepartment() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/department/save',
          this.addForm
        )
        console.log(this.addForm)
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getDepartmentList()
      })
    },
    //编辑角色信息表单
    editDepartment() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/department/update', {
          id: this.editForm.id,
          departmentName: this.editForm.departmentName,
          departmentResponsibility: this.editForm.departmentResponsibility,
          level: this.editForm.level,
          company: this.editForm.company
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getDepartmentList()
      })
    },
    //删除
    async removeById(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该数据, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      //如果确认删除，返回值为字符串confirm,如果取消删除，则返回值为cancel
      if (confirmResult !== 'confirm') return
      const { data: res } = await this.$http.post('/department/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getDepartmentList()
    },
    //搜索
    async searchDepartment() {
      const { data: res } = await this.$http.get('/department/list-by-name', {
        params: { name: this.input }
      })
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.DepartmentList = res.data.records
      this.total = res.data.total
    }
  },
  created() {
    this.getDepartmentList()
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
.div1 {
  height: 100%;
  width: 100%;
}

.div-bread {
  height: 25px;
  width: 100%;
}

.div-body {
  height: calc(100% - 25px);
  width: 100%;
  display: flex;
}

.div-body-aside {
  height: 100%;
  width: 11.5%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background-color: rgb(249, 249, 249);
}

.div-body-main {
  height: 100%;
  width: 100%;
}

.div-body-main-header {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

.div-body-main-body {
  height: calc(100% - 95px);
  width: 100%;
}

#div-body-main-footer {
  height: 35px;
  width: 100%;
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}
</style>
