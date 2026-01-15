<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/administrativedivision' }">行政区划</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <div style="width:250px;align-self:center;display: flex;margin-left: 5px;">
        <el-input placeholder="请输入村子名称搜索" v-model="administrative" class="input-with-select" clearable
          style="align-self:center;" @clear="getAdministrativeDivisionList">
          <el-button slot="append" icon="el-icon-search" @click="getAdministrativeDivisionList"></el-button>
        </el-input>
      </div>
      <el-button type="primary" round icon="el-icon-plus"
        style="margin-left:auto; align-self: center;margin-right:10px;" @click="addDialogVisible = true">新增</el-button>
    </div>

    <div id="div-main">
      <el-table :data="AdministrativeDivisionList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="waterSupply" label="供水管理单位" width="180" align="center">
        </el-table-column>
        <el-table-column prop="townBelong" label="所属乡镇" width="180" align="center">
        </el-table-column>
        <el-table-column prop="townCode" label="乡镇行政区划代码" align="center">
        </el-table-column>
        <el-table-column prop="villageBelong" label="所属村" align="center">
        </el-table-column>
        <el-table-column prop="villageCode" label="村行政区划代码" align="center">
        </el-table-column>
        <el-table-column prop="households" label="户数" align="center">
        </el-table-column>
        <el-table-column prop="population" label="人口" align="center">
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column fixed="right" label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">
              编辑</el-button>
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
    <el-dialog title="新增行政规划" :visible.sync="addDialogVisible" width="30%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-form-item label="供水管理单位:" prop="waterSupply">
          <el-input v-model="addForm.waterSupply"></el-input>
        </el-form-item>
        <el-form-item label="所属乡镇:" prop="townBelong">
          <el-input v-model="addForm.townBelong"></el-input>
          <!-- <el-select v-model="addForm.townBelong" clearable placeholder="请选择" style="align-self:center;"
            @change="adjustTownVilligeadd" @clear="adjustTownVilligeadd">
            <el-option v-for="item in administrativeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select> -->
        </el-form-item>
        <el-form-item label="乡镇行政区划代码:" prop="townCode">
          <el-input v-model="addForm.townCode"></el-input>
        </el-form-item>
        <el-form-item label="所属村:" prop="villageBelong">
          <el-input v-model="addForm.villageBelong"></el-input>
          <!-- <el-select v-model="addForm.villageBelong" clearable placeholder="请选择" style="align-self:center;"
            @change="adjustTownVilligeadd" @clear="adjustTownVilligeadd">
            <el-option v-for="item in villigeSelectList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select> -->
        </el-form-item>
        <el-form-item label="村行政区划代码:" prop="villageCode">
          <el-input v-model="addForm.villageCode"></el-input>
        </el-form-item>
        <el-form-item label="户数:" prop="households">
          <el-input v-model="addForm.households"></el-input>
        </el-form-item>
        <el-form-item label="人口:" prop="population">
          <el-input v-model="addForm.population"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addAdministrativeDivision">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑行政区划" :visible.sync="editDialogVisible" width="30%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-form-item label="供水管理单位:" prop="waterSupply">
          <el-input v-model="editForm.waterSupply"></el-input>
        </el-form-item>
        <el-form-item label="所属乡镇:" prop="townBelong">
          <el-input v-model="editForm.townBelong"></el-input>
          <!-- <el-select v-model="editForm.townBelong" clearable placeholder="请选择" style="align-self:center;"
            @change="adjustTownVilligeedit" @clear="adjustTownVilligeedit">
            <el-option v-for="item in administrativeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select> -->
        </el-form-item>
        <el-form-item label="乡镇行政区划代码:" prop="townCode">
          <el-input v-model="editForm.townCode"></el-input>
        </el-form-item>
        <el-form-item label="所属村:" prop="villageBelong">
          <el-input v-model="editForm.villageBelong"></el-input>
          <!-- <el-select v-model="editForm.villageBelong" clearable placeholder="请选择" style="align-self:center;"
            @change="adjustTownVilligeedit" @clear="adjustTownVilligeedit">
            <el-option v-for="item in villigeSelectList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select> -->
        </el-form-item>
        <el-form-item label="村行政区划代码:" prop="villageCode">
          <el-input v-model="editForm.villageCode"></el-input>
        </el-form-item>
        <el-form-item label="户数:" prop="households">
          <el-input v-model="editForm.households"></el-input>
        </el-form-item>
        <el-form-item label="人口:" prop="population">
          <el-input v-model="editForm.population"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editAdministrativeDivision">确 定</el-button>
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
      //选择行政区域
      administrative: '',
      administrativeList: [
        {
          value: '乡镇1',
          label: '乡镇1',
          code: '123000',
          villigeList: [
            {
              value: '村子1',
              label: '村子1',
              code: '123001'
            },
            {
              value: '村子2',
              label: '村子2',
              code: '123002'
            },
            {
              value: '村子3',
              label: '村子3',
              code: '123003'
            }
          ]
        },
        {
          value: '乡镇2',
          label: '乡镇2',
          code: '124000',
          villigeList: [
            {
              value: '村子1',
              label: '村子1',
              code: '124001'
            },
            {
              value: '村子2',
              label: '村子2',
              code: '124002'
            },
            {
              value: '村子3',
              label: '村子3',
              code: '124003'
            }
          ]
        },
        {
          value: '乡镇3',
          label: '乡镇3',
          code: '125000',
          villigeList: [
            {
              value: '村子1',
              label: '村子1',
              code: '125001'
            },
            {
              value: '村子2',
              label: '村子2',
              code: '125002'
            },
            {
              value: '村子3',
              label: '村子3',
              code: '125003'
            }
          ]

        }
      ],
      villigeSelectList: [],
      //获取行政区划列表的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      AdministrativeDivisionList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加行政规划的表单数据
      addForm: {
        waterSupply: '',
        townBelong: '',
        townCode: '',
        villageBelong: '',
        villageCode: '',
        households: '',
        population: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        waterSupply: [
          { required: true, message: '请输入供水管理单位', trigger: 'blur' },
        ],
        townBelong: [
          { required: true, message: '请输入所属乡镇', trigger: 'blur' }
        ],
        townCode: [
          {
            required: true,
            message: '请输入6位乡镇行政区划代码',
            trigger: 'blur'
          },
          {
            pattern: /^\d{6}$/,
            message: '请输入6位乡镇行政区划代码',
            trigger: 'blur'
          }
        ],
        villageBelong: [
          { required: true, message: '请输入所属村', trigger: 'blur' }
        ],
        villageCode: [
          { required: true, message: '请输入6位村行政区划代码', trigger: 'blur' },
          {
            pattern: /^\d{6}$/,
            message: '请输入6位村行政区划代码',
            trigger: 'blur'
          }
        ],
        households: [
          { required: true, message: '请输入户数', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        population: [{ required: true, message: '请输入人口', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        waterSupply: [
          { required: true, message: '请输入供水管理单位', trigger: 'blur' },
        ],
        townBelong: [
          { required: true, message: '请输入所属乡镇', trigger: 'blur' }
        ],
        townCode: [
          {
            required: true,
            message: '请输入6位乡镇行政区划代码',
            trigger: 'blur'
          },
          {
            pattern: /^\d{3}000$/,
            message: '请输入6位乡镇行政区划代码',
            trigger: 'blur'

          }

        ],
        villageBelong: [
          { required: true, message: '请输入所属村', trigger: 'blur' }
        ],
        villageCode: [
          { required: true, message: '请输入6位村行政区划代码', trigger: 'blur' },
          {
            pattern: /^\d{6}$/,
            message: '请输入6位村行政区划代码',
            trigger: 'blur'

          }

        ],
        households: [
          { required: true, message: '请输入户数', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        population: [{ required: true, message: '请输入人口', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getAdministrativeDivisionList() {
      if (this.administrative == null) {
        this.administrative = ''
      }
      const { data: res } = await this.$http.get(
        '/administration-division/list',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            administrative: this.administrative
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('获取用户列表失败')
      }
      this.AdministrativeDivisionList = res.data.records
      this.total = res.data.total
    },
    //编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/administration-division/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询用户信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getAdministrativeDivisionList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getAdministrativeDivisionList()
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
    //根据选择的乡镇调整村的下拉菜单
    adjustTownVilligeadd() {
      //乡镇下拉框不为空时，展示第二个下拉框内容
      if (this.addForm.townBelong !== '') {
        //乡镇代码自动选取了
        this.addForm.townCode = this.administrativeList.filter((item) => item.value == this.addForm.townBelong)[0].code
        this.villigeSelectList = this.administrativeList.filter((item) => item.value == this.addForm.townBelong)[0].villigeList
        if (this.addForm.villageBelong !== '') {
          this.addForm.villageCode = this.villigeSelectList.filter((item) => item.value == this.addForm.villageBelong)[0].code
        } else {
          this.addForm.villageCode = ''
        }
        const findlevel = (item) => item.label == this.addForm.villageBelong
        if (this.addForm.villageBelong !== '' && !this.villigeSelectList.some(findlevel)) {
          this.addForm.villageBelong = ''
        }
      } else {
        this.addForm.townCode = ''
        this.addForm.villageBelong = ''
        this.villigeSelectList = []
        this.addForm.villageCode = ''
      }

    },
    adjustTownVilligeedit() {
      //乡镇下拉框不为空时，展示第二个下拉框内容
      if (this.editForm.townBelong !== '') {
        //乡镇代码自动选取了
        this.editForm.townCode = this.administrativeList.filter((item) => item.value == this.editForm.townBelong)[0].code
        this.villigeSelectList = this.administrativeList.filter((item) => item.value == this.editForm.townBelong)[0].villigeList
        if (this.editForm.villageBelong !== '') {
          this.editForm.villageCode = this.villigeSelectList.filter((item) => item.value == this.editForm.villageBelong)[0].code
        } else {
          this.editForm.villageCode = ''
        }
        const findlevel = (item) => item.label == this.editForm.villageBelong
        if (this.editForm.villageBelong !== '' && !this.villigeSelectList.some(findlevel)) {
          this.editForm.villageBelong = ''
        }
      } else {
        this.editForm.townCode = ''
        this.editForm.villageBelong = ''
        this.villigeSelectList = []
        this.editForm.villageCode = ''
      }

    },
    //新增行政区划
    addAdministrativeDivision() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/administration-division/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getAdministrativeDivisionList()
      })
    },
    //编辑行政区划信息表单
    editAdministrativeDivision() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/administration-division/update',
          {
            id: this.editForm.id,
            waterSupply: this.editForm.waterSupply,
            townBelong: this.editForm.townBelong,
            townCode: this.editForm.townCode,
            villageBelong: this.editForm.villageBelong,
            villageCode: this.editForm.villageCode,
            households: this.editForm.households,
            population: this.editForm.population
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getAdministrativeDivisionList()
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
        '/administration-division/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getAdministrativeDivisionList()
    }
  },
  created() {
    this.getAdministrativeDivisionList()
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

.el-input {
  font-size: 14px;
}
</style>
