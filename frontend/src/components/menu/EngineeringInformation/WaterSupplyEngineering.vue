<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/watersupplyengineering' }">枢纽供水工程</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入供水工程搜索" v-model="input" class="input-with-select" clearable @clear="getWSEList">
          <el-button slot="append" icon="el-icon-search" @click="searchWSE"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus" style="margin-left:auto; align-self: center;"
        @click="addDialogVisible = true">新增</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
        @click="importDialogVisible = true">导入</el-button>

      <download-excel class="export-excel-wrapper" :data="WSEList" :fields="json_fields" name="枢纽供水工程信息.xls"
        style="align-self:center; margin-right:10px">
        <!-- 上面可以自定义自己的样式，还可以引用其他组件button -->
        <el-button type="primary" round icon="iconfont icon-icon-test"
          style="align-self:center; margin-right:10px">导出</el-button>
      </download-excel>
    </div>

    <div id="div-main">
      <el-table :data="WSEList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="projectName" label="供水工程名" align="center">
        </el-table-column>
        <el-table-column prop="code" label="供水工程代码" align="center">
        </el-table-column>
        <el-table-column prop="type" label="工程类型" align="center">
        </el-table-column>
        <el-table-column prop="zone" label="所属分区" align="center">
        </el-table-column>
        <el-table-column prop="designSupply" label="设计日供水规模(m³)" align="center">
        </el-table-column>
        <el-table-column prop="actualSupple" label="实际日供水规模(m³)" align="center">
        </el-table-column>
        <el-table-column prop="designPopulation" label="设计供水人口(万人)" align="center">
        </el-table-column>
        <el-table-column prop="village" label="受益行政村数量" align="center">
        </el-table-column>
        <el-table-column prop="population" label="受益人口(万人)" align="center">
        </el-table-column>
        <el-table-column prop="supplyNumber" label="供水户数(万人)" align="center">
        </el-table-column>
        <el-table-column prop="manageDepartment" label="工程主管部门" align="center">
        </el-table-column>
        <el-table-column prop="manageUnit" label="工程管理单位" align="center">
        </el-table-column>
        <el-table-column prop="area" label="供水范围" align="center">
        </el-table-column>
        <el-table-column prop="note" label="备注" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="160">
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
    <el-dialog title="新增供水工程" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="170px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水工程名:" prop="projectName">
              <el-input v-model="addForm.projectName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水工程代码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程类型:" prop="type">
              <el-input v-model="addForm.type"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分区:" prop="zone">
              <el-input v-model="addForm.zone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计日供水规模(m³):" prop="designSupply">
              <el-input v-model="addForm.designSupply"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际日供水规模(m³):" prop="actualSupple">
              <el-input v-model="addForm.actualSupple"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计供水人口(万人):" prop="designPopulation">
              <el-input v-model="addForm.designPopulation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="受益行政村数量:" prop="village">
              <el-input v-model="addForm.village"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="受益人口(万人):" prop="population">
              <el-input v-model="addForm.population"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水户数(万人):" prop="supplyNumber">
              <el-input v-model="addForm.supplyNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程主管部门:" prop="manageDepartment">
              <el-input v-model="addForm.manageDepartment"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程管理单位:" prop="manageUnit">
              <el-input v-model="addForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水范围:" prop="area">
              <el-input v-model="addForm.area"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="addForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addWSE">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑供水工程" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="170px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水工程名:" prop="projectName">
              <el-input v-model="editForm.projectName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水工程代码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程类型:" prop="type">
              <el-input v-model="editForm.type"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分区:" prop="zone">
              <el-input v-model="editForm.zone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计日供水规模(m³):" prop="designSupply">
              <el-input v-model="editForm.designSupply"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际日供水规模(m³):" prop="actualSupple">
              <el-input v-model="editForm.actualSupple"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计供水人口(万人):" prop="designPopulation">
              <el-input v-model="editForm.designPopulation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="受益行政村数量:" prop="village">
              <el-input v-model="editForm.village"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="受益人口(万人):" prop="population">
              <el-input v-model="editForm.population"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水户数(万人):" prop="supplyNumber">
              <el-input v-model="editForm.supplyNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程主管部门:" prop="manageDepartment">
              <el-input v-model="editForm.manageDepartment"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程管理单位:" prop="manageUnit">
              <el-input v-model="editForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水范围:" prop="area">
              <el-input v-model="editForm.area"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="editForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editWSE">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导入框 -->
    <el-dialog :visible.sync="importDialogVisible" width="800px" :modal="false" :close-on-press-escape="false"
      :close-on-click-modal="false">
      <template slot="title">
        <div style="border-bottom: 1px solid #ebebeb;font-size:20px;padding-bottom:10px">
          导入Excel
        </div>
      </template>
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file"
        action="/water-supply-project/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
        :on-remove="handleRemove" :before-upload="beforeUpload" :http-request="httpRequest" multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传xls/xlsx文件
        </div>
      </el-upload>
      <span slot="footer">
        <el-button type="info" icon="el-icon-download" style="margin-right:450px"
          @click="exportFormwork">下载模板</el-button>
        <el-button type="success" @click="submitUpload">上 传</el-button>
        <el-button @click="importDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="projectName" label="供水工程名" align="center">
      </el-table-column>
      <el-table-column prop="code" label="供水工程代码" align="center">
      </el-table-column>
      <el-table-column prop="type" label="工程类型" align="center">
      </el-table-column>
      <el-table-column prop="zone" label="所属分区" align="center">
      </el-table-column>
      <el-table-column prop="designSupply" label="设计日供水规模(m³)" align="center">
      </el-table-column>
      <el-table-column prop="actualSupple" label="实际日供水规模(m³)" align="center">
      </el-table-column>
      <el-table-column prop="designPopulation" label="设计供水人口(万人)" align="center">
      </el-table-column>
      <el-table-column prop="village" label="受益行政村数量" align="center">
      </el-table-column>
      <el-table-column prop="population" label="受益人口(万人)" align="center">
      </el-table-column>
      <el-table-column prop="supplyNumber" label="供水户数(万人)" align="center">
      </el-table-column>
      <el-table-column prop="manageDepartment" label="工程主管部门" align="center">
      </el-table-column>
      <el-table-column prop="manageUnit" label="工程管理单位" align="center">
      </el-table-column>
      <el-table-column prop="area" label="供水范围" align="center">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center">
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
  name: '',
  data() {
    return {
      //自定义导出字段
      json_fields: {
        "供水工程名": "projectName",
        '供水工程代码': "code",
        '供水工程类型': "type",
        '所属分区': "zone",
        "巡检类型": "type",
        "设计日供水规模": "designSupply",
        "实际日供水规模": "actualSupple",
        "设计供水人口(万人)": "designPopulation",
        "受益行政村数量": "village",
        "受益人口(万人)": "population",
        "供水户数(万人)": "supplyNumber",
        "工程主管部门": "manageDepartment",
      },
      json_meta: [
        [
          {
            " key ": " charset ",
            " value ": " utf- 8 "
          }
        ]
      ],
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      WSEList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //上传的文件
      fileList: [],
      //数据列表
      ExamplePI: [
        {
          projectName: "供水工程名",
          code: '供水工程代码',
          type: '供水工程类型',
          zone: '所属分区',
          type: "巡检类型",
          designSupply: "设计日供水规模",
          actualSupple: "实际日供水规模",
          designPopulation: "设计供水人口(万人)",
          village: "受益行政村数量",
          population: "受益人口(万人)",
          supplyNumber: "供水户数(万人)",
          manageDepartment: "工程主管部门",
          manageUnit: "工程管理单位",
          area: "供水范围",
          note: "备注"

        }
      ],
      //添加表单数据
      addForm: {
        projectName: '',
        code: '',
        type: '',
        zone: '',
        designSupply: '',
        actualSupple: '',
        designPopulation: '',
        village: '',
        population: '',
        supplyNumber: '',
        manageDepartment: '',
        manageUnit: '',
        area: '',
        note: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        projectName: [
          { required: true, message: "请输入供水工程名", trigger: 'blur' }
        ],
        code: [
          { required: true, message: "请输入供水工程代码", trigger: 'blur' }
        ],
        type: [
          { required: false, message: "请输入供水工程类型", trigger: 'blur' }
        ],
        zone: [
          { required: false, message: "请输入所属分区", trigger: 'blur' }
        ],
        designSupply: [
          { required: true, message: "请输入设计日供水规模", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        actualSupple: [
          { required: true, message: "请输入实际日供水规模", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        designPopulation: [
          { required: true, message: "请输入设计供水人口(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        village: [
          { required: true, message: "请输入受益行政村数量", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        population: [
          { required: true, message: "请输入受益人口(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        supplyNumber: [
          { required: true, message: "请输入供水户数(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        manageDepartment: [
          { required: false, message: "请输入工程主管部门", trigger: 'blur' }
        ],
        manageUnit: [
          { required: false, message: "请输入工程管理单位", trigger: 'blur' }
        ],
        area: [
          { required: false, message: "请输入工程管理范围", trigger: 'blur' }
        ],
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        projectName: [
          { required: true, message: "请输入供水工程名", trigger: 'blur' }
        ],
        code: [
          { required: true, message: "请输入供水工程代码", trigger: 'blur' }
        ],
        type: [
          { required: false, message: "请输入供水工程类型", trigger: 'blur' }
        ],
        zone: [
          { required: false, message: "请输入所属分区", trigger: 'blur' }
        ],
        designSupply: [
          { required: true, message: "请输入设计日供水规模", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        actualSupple: [
          { required: true, message: "请输入实际日供水规模", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        designPopulation: [
          { required: true, message: "请输入设计供水人口(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        village: [
          { required: true, message: "请输入受益行政村数量", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        population: [
          { required: true, message: "请输入受益人口(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        supplyNumber: [
          { required: true, message: "请输入供水户数(万人)", trigger: 'blur' },
          { pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: "请输入数字", trigger: 'blur' }
        ],
        manageDepartment: [
          { required: false, message: "请输入工程主管部门", trigger: 'blur' }
        ],
        manageUnit: [
          { required: false, message: "请输入工程管理单位", trigger: 'blur' }
        ],
        area: [
          { required: false, message: "请输入工程管理范围", trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    //获取数据列表
    async getWSEList() {
      const { data: res } = await this.$http.get('/water-supply-project/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.WSEList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/water-supply-project/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getWSEList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getWSEList()
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
    //新增枢纽供水工程
    addWSE() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/water-supply-project/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getWSEList()
      })
    },
    //编辑角色信息表单
    editWSE() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/water-supply-project/update',
          {
            id: this.editForm.id,
            projectName: this.editForm.projectName,
            code: this.editForm.code,
            type: this.editForm.type,
            zone: this.editForm.zone,
            designSupply: this.editForm.designSupply,
            actualSupple: this.editForm.actualSupple,
            designPopulation: this.editForm.designPopulation,
            village: this.editForm.village,
            population: this.editForm.population,
            supplyNumber: this.editForm.supplyNumber,
            manageDepartment: this.editForm.manageDepartment,
            manageUnit: this.editForm.manageUnit,
            area: this.editForm.area,
            note: this.editForm.note,
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getWSEList()
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
        '/water-supply-project/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getWSEList()
    },
    //搜索
    async searchWSE() {
      const { data: res } = await this.$http.get(
        '/water-supply-project/search-list-by-name',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            name: this.input,
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.WSEList = res.data.records
      this.total = res.data.total
    },
    //导出模板
    exportFormwork() {
      var xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(
        document.querySelector('#tableFormwork'),
        xlsxParam
      )
      var wbout = XLSX.write(wb, {
        bookType: 'xlsx',
        bookSST: true,
        type: 'array'
      })
      try {
        FileSaver.saveAs(
          new Blob([wbout], { type: 'application/octet-stream' }),
          '枢纽供水工程信息数据列表模板.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
    },
    //导入Excel
    submitUpload() {
      //关闭表单
      this.importDialogVisible = false
      this.$refs.upload.submit()
      setTimeout(() => {
        this.getWSEList()
      }, 1000)

    },
    fileChange(file, fileList) {
      this.fileList = fileList
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    async httpRequest(param) {
      let fileObj = param.file
      let formData = new FormData()
      formData.append('file', fileObj)
      let url = '/water-supply-project/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
      } else {
        this.$message.error(res.message)
      }
    },
    //上传文件之前进行文件类型判断
    beforeUpload(file) {
      // 允许上传的文件格式列表
      let acceptList = ['xlsx', 'xls']
      // 根据文件名获取文件的后缀名
      let fileType = file.name
        .split('.')
        .pop()
        .toLowerCase()
      // 判断文件格式是否符合要求
      if (acceptList.indexOf(fileType) === -1) {
        this.$message.error('只能上传 xlsx/xls 格式的文件 !')
        return false
      }
    }
  },
  created() {
    this.getWSEList()
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

.upload-demo {
  margin: 10px 200px;
}
</style>
