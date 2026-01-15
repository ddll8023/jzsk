<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
        
        <el-breadcrumb-item :to="{ path: '/home/pumpstation' }">泵站</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入泵站名称搜索" v-model="input" clearable @clear="getPSList">
          <el-button slot="append" icon="el-icon-search" @click="searchPS"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="margin-left: auto; align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>
    </div>


    <div id="div-main">
      <el-table :data="PSList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="name" label="泵站名称" align="center">
        </el-table-column>
        <el-table-column prop="code" label="泵站编码" align="center">
        </el-table-column>
        <el-table-column prop="type" label="泵站类型" align="center">
        </el-table-column>
        <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
        </el-table-column>
        <el-table-column prop="company" label="所属供水公司" align="center">
        </el-table-column>
        <el-table-column prop="longitude" label="经度" align="center">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" align="center">
        </el-table-column>
        <el-table-column prop="address" label="地址" align="center">
        </el-table-column>
        <el-table-column prop="operationMode" label="运行方式(几备几用)" align="center">
        </el-table-column>
        <el-table-column prop="number" label="机组数量(台)" align="center">
        </el-table-column>
        <el-table-column prop="designScale" label="设计规模(m³/天)" align="center">
        </el-table-column>
        <el-table-column prop="installedCapacity" label="装机容量(kw)" align="center">
        </el-table-column>
        <el-table-column prop="lift" label="扬程" align="center">
        </el-table-column>
        <el-table-column prop="date" label="建站年月" align="center">
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增泵站信息" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="泵站名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="泵站编码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="泵站类型:" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择泵站类型">
                <el-option label="抽水泵站" value="抽水泵站"></el-option>
                <el-option label="加压泵站" value="加压泵站"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupplyProject">
              <el-input v-model="addForm.waterSupplyProject"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属供水公司:" prop="company">
              <el-input v-model="addForm.company"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="addForm.longitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="addForm.latitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="addForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运行方式(几备几用):" prop="operationMode">
              <el-input v-model="addForm.operationMode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机组数量(台):" prop="number">
              <el-input v-model="addForm.number"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计规模(m³/天):" prop="designScale">
              <el-input v-model="addForm.designScale"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="装机容量(kw):" prop="installedCapacity">
              <el-input v-model="addForm.installedCapacity"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="扬程:" prop="lift">
              <el-input v-model="addForm.lift"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="addForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPS">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑泵站信息" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="泵站名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="泵站编码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="泵站类型:" prop="type">
              <el-select v-model="editForm.type" placeholder="请选择泵站类型">
                <el-option label="抽水泵站" value="抽水泵站"></el-option>
                <el-option label="加压泵站" value="加压泵站"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupplyProject">
              <el-input v-model="editForm.waterSupplyProject"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属供水公司:" prop="company">
              <el-input v-model="editForm.company"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="editForm.longitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="editForm.latitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="editForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运行方式(几备几用):" prop="operationMode">
              <el-input v-model="editForm.operationMode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机组数量(台):" prop="number">
              <el-input v-model="editForm.number"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设计规模(m³/天):" prop="designScale">
              <el-input v-model="editForm.designScale"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="装机容量(kw):" prop="installedCapacity">
              <el-input v-model="editForm.installedCapacity"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="扬程:" prop="lift">
              <el-input v-model="editForm.lift"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="editForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editPS">确 定</el-button>
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
        action="/pump-station/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
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

    <!-- 导出表格 -->
    <el-table id="table" :data="AllPSList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="泵站名称" align="center">
      </el-table-column>
      <el-table-column prop="code" label="泵站编码" align="center">
      </el-table-column>
      <el-table-column prop="type" label="泵站类型" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
      </el-table-column>
      <el-table-column prop="company" label="所属供水公司" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="address" label="地址" align="center">
      </el-table-column>
      <el-table-column prop="operationMode" label="运行方式(几备几用)" align="center">
      </el-table-column>
      <el-table-column prop="number" label="机组数量(台)" align="center">
      </el-table-column>
      <el-table-column prop="designScale" label="设计规模(m³/天)" align="center">
      </el-table-column>
      <el-table-column prop="installedCapacity" label="装机容量(kw)" align="center">
      </el-table-column>
      <el-table-column prop="lift" label="扬程" align="center">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePS" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="泵站名称" align="center">
      </el-table-column>
      <el-table-column prop="code" label="泵站编码" align="center">
      </el-table-column>
      <el-table-column prop="type" label="泵站类型" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
      </el-table-column>
      <el-table-column prop="company" label="所属供水公司" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="address" label="地址" align="center">
      </el-table-column>
      <el-table-column prop="operationMode" label="运行方式(几备几用)" align="center">
      </el-table-column>
      <el-table-column prop="number" label="机组数量(台)" align="center">
      </el-table-column>
      <el-table-column prop="designScale" label="设计规模(m³/天)" align="center">
      </el-table-column>
      <el-table-column prop="installedCapacity" label="装机容量(kw)" align="center">
      </el-table-column>
      <el-table-column prop="lift" label="扬程" align="center">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center">
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
    //自定义表单验证规则
    var checkname = (rule, value, cb) => {
      const regtownCode = /^[\u4e00-\u9fa5_a-zA-Z0-9]{0,99}$/
      if (regtownCode.test(value)) {
        return cb()
      }
      cb(new Error('请勿包含空格，标点符号等特殊字符'))
    }
    var checkCode = (rule, value, cb) => {
      const regvillageCode = /^\d{9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('泵站编码必须为9位阿拉伯数字'))
    }
    var checklongitude = (rule, value, cb) => {
      const regvillageCode = /^([0-9]{1,3}[.][0-9]*)$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的经纬度'))
    }
    var checknumber = (rule, value, cb) => {
      const regvillageCode = /^[0-9]{0,9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的数字'))
    }
    return {
      file: '',
      fileList: [],
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      ExamplePS: [
        {
          waterSupplyProject: '长度小于99，不包含特殊字符',
          name: '长度小于99，不包含特殊字符',
          code: '长度为9的阿拉伯数字',
          type: '抽水泵站/加压泵站/其他',
          company: '长度小于99，不包含特殊字符',
          longitude: '-180到180,小数点后不超过7位',
          latitude: '-90到90,小数点后不超过7位',
          address: 'XX省XX市XX县XX镇XX村XX地方',
          operationMode: '1备2用',
          number: '3',
          designScale: '6000',
          installedCapacity: '1500',
          lift: '132.5',
          date: '2012-08'
        }
      ],
      PSList: [],
      AllPSList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //添加表单数据
      addForm: {
        waterSupplyProject: '',
        name: '',
        code: '',
        type: '',
        company: '',
        longitude: '',
        latitude: '',
        address: '',
        operationMode: '',
        number: '',
        designScale: '',
        installedCapacity: '',
        lift: '',
        date: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        waterSupplyProject: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入泵站编码', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入泵站名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请输入泵站类型', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        company: [
          { required: true, message: '请输入所属供水公司', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '请输入经度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
            message: '请输入正确的经度',
            trigger: 'blur'
          }
        ],
        latitude: [
          { required: true, message: '请输入纬度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        operationMode: [
          {
            required: true,
            message: '请输入运行方式（几备几用）',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        number: [
          { required: true, message: '请输入机组数量', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        designScale: [
          { required: true, message: '请输入设计规模', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        installedCapacity: [
          { required: true, message: '请输入装机容量', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        lift: [{ required: true, message: '请输入扬程', trigger: 'blur' },
        { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        waterSupplyProject: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入泵站编码', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入泵站名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请输入泵站类型', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        company: [
          { required: true, message: '请输入所属供水公司', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '请输入经度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
            message: '请输入正确的经度',
            trigger: 'blur'
          }
        ],
        latitude: [
          { required: true, message: '请输入纬度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        operationMode: [
          {
            required: true,
            message: '请输入运行方式（几备几用）',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        number: [
          { required: true, message: '请输入机组数量', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        designScale: [
          { required: true, message: '请输入设计规模', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        installedCapacity: [
          { required: true, message: '请输入装机容量', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        lift: [{ required: true, message: '请输入扬程', trigger: 'blur' },
        { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getPSList() {
      const { data: res } = await this.$http.get('/pump-station/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.PSList = res.data.records
      this.total = res.data.total
    },
    //获取不分页数据
    async getAllPSList() {
      const { data: res } = await this.$http.get('/pump-station/export-excel')

      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllPSList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/pump-station/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getPSList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getPSList()
    },
    //序号连续
    table_index(index) {
      return (
        (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
      )
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
    addPS() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/pump-station/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getPSList()
        this.getAllPSList()
      })
    },
    //编辑泵站信息表单
    editPS() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/pump-station/update', {
          id: this.editForm.id,
          waterSupplyProject: this.editForm.waterSupplyProject,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          code: this.editForm.code,
          name: this.editForm.name,
          type: this.editForm.type,
          company: this.editForm.company,
          address: this.editForm.address,
          operationMode: this.editForm.operationMode,
          number: this.editForm.number,
          designScale: this.editForm.designScale,
          installedCapacity: this.editForm.installedCapacity,
          lift: this.editForm.lift,
          date: this.editForm.date
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getPSList()
        this.getAllPSList()
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
      const { data: res } = await this.$http.post('/pump-station/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getPSList()
      this.getAllPSList()
    },
    //搜索
    async searchPS() {
      const { data: res } = await this.$http.get(
        '/pump-station/name',
        {
          params: {
            name: this.input,
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.PSList = res.data.records
      this.total = res.data.total
    },
    //导出Excel
    exportExcel() {
      var xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(
        document.querySelector('#table'),
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
          '泵站数据列表报告.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
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
          '泵站数据列表模板.xlsx'
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
        this.getPSList()
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
      let url = '/pump-station/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllPSList()
      } else {
        this.$message.error(res.message)
      }
    },
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
    this.getPSList()
    this.getAllPSList()
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

#div-header1 {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

#div-header2 {
  height: 6%;
  width: 100%;
  margin-top: 0px;
  display: flex;
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
