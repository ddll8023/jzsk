<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>预警管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/prewarningindicatorsettingread' }">预警指标设定</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <div style="align-self:center; margin-left:10px">
        <el-input placeholder="请输入测点名称搜索" v-model="inputWord" class="input-with-select" clearable
          @clear="selectMonitorWarningType">
          <el-button slot="append" icon="el-icon-search" @click="searchPI"></el-button>
        </el-input>
      </div>

      <span style="align-self:center;font-size:15px; margin-left:20px">监测项：</span>
      <el-select v-model="type" clearable placeholder="请选择" style="align-self:center;"
        @change="selectMonitorWarningType" @clear="selectMonitorWarningType">
        <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>

    <div id="div-main">
      <el-table :data="PIList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index"></el-table-column>
        <el-table-column prop="position" label="测点名称" align="center">
        </el-table-column>
        <el-table-column prop="type" label="监测项" align="center">
        </el-table-column>
        <el-table-column label="预警指标范围" class="column" align="center">
          <el-table-column prop="upUpLimit" label="上上限" width="180" align="center">
          </el-table-column>
          <el-table-column prop="upLimit" label="上限" width="180" align="center">
          </el-table-column>
          <el-table-column prop="lowLimit" label="下限" width="180" align="center">
          </el-table-column>
          <el-table-column prop="lowerLimit" label="下下限" width="180" align="center">
          </el-table-column>
        </el-table-column>
        <el-table-column prop="unit" label="单位" align="center">
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[5, 10, 20, 50]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增预警指标设定" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="110px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点名称:" prop="position">
              <el-input v-model="addForm.position"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测项:" prop="type">
              <el-input v-model="addForm.type"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上上限:" prop="upUpLimit">
              <el-input v-model="addForm.upUpLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上限:" prop="upLimit">
              <el-input v-model="addForm.upLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下限:" prop="lowLimit">
              <el-input v-model="addForm.lowLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下下限:" prop="lowerLimit">
              <el-input v-model="addForm.lowerLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位:" prop="unit">
              <el-input v-model="addForm.unit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPI">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑预警指标设定" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点名称:" prop="position">
              <el-input v-model="editForm.position"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测项:" prop="type">
              <el-input v-model="editForm.type"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上上限:" prop="upUpLimit">
              <el-input v-model="editForm.upUpLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上限:" prop="upLimit">
              <el-input v-model="editForm.upLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下限:" prop="lowLimit">
              <el-input v-model="editForm.lowLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下下限:" prop="lowerLimit">
              <el-input v-model="editForm.lowerLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位:" prop="unit">
              <el-input v-model="editForm.unit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editPI">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  position: '',
  data() {
    return {

      // 监测项类型列表
      type: '',
      types: [],
      typeList: [],
      queryinfo1: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
      },
      total: 0,//页面数据总条数
      PIList: [],//请求到的数据列表
      inputWord: '',//输入搜索的文字

      // ----------------------------
      input1: '',
      input2: '',
      input3: '',
      input4: '',
      input5: '',
      input6: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '泵站'
      },
      queryinfo2: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '水库'
      },
      queryinfo3: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '浮船'
      },
      queryinfo4: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '供水管线'
      },
      queryinfo5: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '水厂'
      },
      queryinfo6: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10,
        //工程类型
        projectType: '管道'
      },
      //数据列表
      PIList1: [],
      PIList2: [],
      PIList3: [],
      PIList4: [],
      PIList5: [],
      PIList6: [],
      total1: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加表单数据
      addForm: {
        position: '',
        type: '',
        upUpLimit: '',
        upLimit: '',
        lowLimit: '',
        lowerLimit: '',
        type: '',
        unit: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        position: [{ required: true, message: '请输入测点名称', trigger: 'blur' }],
        type: [
          { required: true, message: '请输入监测项', trigger: 'blur' }
        ],
        upUpLimit: [
          { required: true, message: '请输入上上限', trigger: 'blur' }
        ],
        upLimit: [{ required: true, message: '请输入上限', trigger: 'blur' }],
        lowLimit: [{ required: true, message: '请输入下限', trigger: 'blur' }],
        lowerLimit: [
          { required: true, message: '请输入下下限', trigger: 'blur' }
        ],
        unit: [{ required: true, message: '请输入单位', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        position: [{ required: true, message: '请输入测点名称', trigger: 'blur' }],
        type: [
          { required: true, message: '请输入监测项', trigger: 'blur' }
        ],
        upUpLimit: [
          { required: true, message: '请输入上上限', trigger: 'blur' }
        ],
        upLimit: [{ required: true, message: '请输入上限', trigger: 'blur' }],
        lowLimit: [{ required: true, message: '请输入下限', trigger: 'blur' }],
        lowerLimit: [
          { required: true, message: '请输入下下限', trigger: 'blur' }
        ],
        unit: [{ required: true, message: '请输入单位', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 根据监测类型搜索
    async selectMonitorWarningType() {
      // 请求数据
      const { data: res } = await this.$http.get(
        'warning-indicator-setting/search-list',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            type: this.type,
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.PIList = res.data.records
      this.total = res.data.total
    },
    async selectTypes() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params:{
          name: '监测项'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.types = res.data
      this.typeList = this.types.map(type => ({
        value: type,
        label: type
      }))
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.selectMonitorWarningType()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.selectMonitorWarningType()
    },
    //序号连续
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
    },
    // 根据搜索框输入数据进行搜索
    async searchPI() {
      const { data: res } = await this.$http.get(
        'warning-indicator-setting/search-position',
        {
          params:
          {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            position: this.inputWord
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.PIList = res.data.records
      this.total = res.data.total
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    //新增预警指标设定
    addPI() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/warning-indicator-setting/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.selectMonitorWarningType()
      })
    },
    //获取编辑数据对话框 //回显
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/warning-indicator-setting/info/' + id
      )
      console.log(res)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //编辑角色信息表单
    editPI() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/warning-indicator-setting/update',
          {
            id: this.editForm.id,
            position: this.editForm.position,
            type: this.editForm.type,
            upUpLimit: this.editForm.upUpLimit,
            upLimit: this.editForm.upLimit,
            lowLimit: this.editForm.lowLimit,
            lowerLimit: this.editForm.lowerLimit,
            unit: this.editForm.unit,
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.selectMonitorWarningType()
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
      const { data: res } = await this.$http.post(
        '/warning-indicator-setting/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.selectMonitorWarningType()
    },

  },
  created() {
    this.selectMonitorWarningType()
    this.selectTypes()
  },
  mounted() { }
}
</script>

<style scoped>
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
</style>
