<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/charactermanage' }">角色管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入角色名称搜索" v-model="input" class="input-with-select" clearable @clear="getCharacterList">
          <el-button slot="append" icon="el-icon-search" @click="searchCharacter"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus" style="align-self:center;margin-left:auto;margin-right:10px"
        @click="addDialogVisible = true">新增</el-button>
    </div>

    <div id="div-main">
      <el-table :data="CharacterList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="name" label="角色名称" width="200" align="center">
        </el-table-column>
        <el-table-column prop="code" label="角色代码" align="center" width="200">
        </el-table-column>
        <el-table-column prop="type" label="角色类型" align="center" width="200">
        </el-table-column>
        <!-- <el-table-column prop="status" label="启用状态" align="center" width="200">
        </el-table-column> -->
        <el-table-column prop="note" label="说明" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增角色" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色代码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色类型:" prop="type">
              <el-input v-model="addForm.type"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="说明:" prop="note">
              <el-input type="textarea" v-model="addForm.note"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="启用状态:" prop="status">
              <el-select v-model="addForm.status" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="启用" value="启用">
                </el-option>
                <el-option label="禁用" value="禁用">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addCharacter">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑角色信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色代码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色类型:" prop="type">
              <el-input v-model="editForm.type"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="说明:" prop="note">
              <el-input type="textarea" v-model="editForm.note"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="启用状态:" prop="status">
              <el-select v-model="editForm.status" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="启用" value="启用">
                </el-option>
                <el-option label="禁用" value="禁用">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editCharacter">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    //自定义表单验证规则
    var checktownCode = (rule, value, cb) => {
      const regtownCode = /^\d{9}$/
      if (regtownCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的乡镇行政区划代码'))
    }
    var checkvillageCode = (rule, value, cb) => {
      const regvillageCode = /^\d{9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的村行政区划代码'))
    }
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
      CharacterList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加表单数据
      addForm: {
        name: '',
        code: '',
        type: '',
        status: '',
        note: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入角色代码', trigger: 'blur' }],
        type: [{ required: true, message: '请输入角色类型', trigger: 'blur' }],
        status: [
          { required: true, message: '请输入启用状态', trigger: 'blur' }
        ],
        note: [{ required: true, message: '请输入说明', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入角色代码', trigger: 'blur' }],
        type: [{ required: true, message: '请输入角色类型', trigger: 'blur' }],
        status: [
          { required: true, message: '请输入启用状态', trigger: 'blur' }
        ],
        note: [{ required: true, message: '请输入说明', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getCharacterList() {
      const { data: res } = await this.$http.get('/role/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.CharacterList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/role/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getOrganizazitonList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getOrganizazitonList()
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
    //新增角色
    addCharacter() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post('/role/save', this.addForm)
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getCharacterList()
      })
    },
    //编辑角色信息表单
    editCharacter() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/role/update', {
          id: this.editForm.id,
          name: this.editForm.name,
          code: this.editForm.code,
          type: this.editForm.type,
          status: this.editForm.status,
          note: this.editForm.note
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getCharacterList()
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
      const { data: res } = await this.$http.post('/role/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getCharacterList()
    },
    //搜索
    async searchCharacter() {
      const { data: res } = await this.$http.get('/role/list-by-name', {
        params: { name: this.input }
      })
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      console.log(this.input)
      this.CharacterList = res.data.records
      this.total = res.data.total
    }
  },
  created() {
    this.getCharacterList()
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#bread {
  height: 25px;
  width: 100%;
}

#div-header {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  // border-top: 1px solid rgb(12, 12, 12);
  // border-bottom: 1px solid rgb(12, 12, 12);
  background-color: rgb(253, 242, 228);
}

#div-main {
  height: calc(100% - 120px);
  width: 100%;
}

#div-footer {
  height: 35px;
  width: 100%;
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}

.el-select .el-input {
  width: 130px;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
</style>
