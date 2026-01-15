<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>预警管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/prewarningindicatorsetting' }">预警指标设定</el-breadcrumb-item>
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

      <el-button type="primary" round icon="el-icon-plus" style="margin-left:auto; align-self: center; margin-right:20px;"
        @click="addDialogVisible = true">新增</el-button>
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
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
          </template>
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
              <el-select v-model="addForm.position" placeholder="请选择测点名称" @change="onPointChange('add')">
                <el-option v-for="item in pointOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测项:" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择监测项">
                <el-option v-for="item in getMonitorItems(addForm.position)" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
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
              <el-select v-model="editForm.position" placeholder="请选择测点名称" @change="onPointChange('edit')">
                <el-option v-for="item in pointOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测项:" prop="type">
              <el-select v-model="editForm.type" placeholder="请选择监测项">
                <el-option v-for="item in getMonitorItems(editForm.position)" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
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
      },
      // 新增：测点和监测项映射
      pointOptions: [
        { value: 'LJ1-1', label: 'LJ1-1', type: 'gnss' },
        { value: 'LJ1-2', label: 'LJ1-2', type: 'gnss' },
        { value: 'LJ1-3', label: 'LJ1-3', type: 'gnss' },
        { value: 'LJ1-4', label: 'LJ1-4', type: 'gnss' },
        { value: 'LT2-1', label: 'LT2-1', type: 'gnss' },
        { value: 'LT2-2', label: 'LT2-2', type: 'gnss' },
        { value: 'LT2-3', label: 'LT2-3', type: 'gnss' },
        { value: 'LT2-4', label: 'LT2-4', type: 'gnss' },
        { value: '坝前雨量水位站', label: '坝前雨量水位站', type: 'rain' },
        { value: 'mcu测站', label: 'mcu测站', type: 'mcu' }
      ],
      monitorItemMap: {
        gnss: [
          { value: 'x位移', label: 'x位移' },
          { value: 'y位移', label: 'y位移' },
          { value: 'z位移', label: 'z位移' },
          { value: '合位移', label: '合位移' },
          { value: '水平位移', label: '水平位移' }
        ],
        rain: [
          { value: '雨量', label: '雨量' },
          { value: '水位', label: '水位' }
        ],
        mcu: [
          { value: '模数', label: '模数' },
          { value: '温度', label: '温度' },
          { value: '水位', label: '水位' },
          { value: '水压', label: '水压' },
          { value: '水位高程', label: '水位高程' }
        ]
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
      // 从后端接口获取所有实际存在的监测项
      const { data: res } = await this.$http.get('/warning-indicator-setting/types');
      if (res.code !== 200 || !res.data) {
        this.typeList = [];
        return;
      }
      this.typeList = res.data.map(type => ({ value: type, label: type }));
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
    // 新增：根据测点名称获取监测项
    getMonitorItems(point) {
      const found = this.pointOptions.find(item => item.value === point);
      if (!found) return [];
      return this.monitorItemMap[found.type] || [];
    },
    // 新增：测点名称变化时清空监测项
    onPointChange(formType) {
      if (formType === 'add') {
        this.addForm.type = '';
      } else if (formType === 'edit') {
        this.editForm.type = '';
      }
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
