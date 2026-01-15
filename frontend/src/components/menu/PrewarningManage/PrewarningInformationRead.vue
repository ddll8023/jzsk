<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>预警管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/prewarninginformation' }">预警信息处理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <span style="align-self:center; font-size:14px">发生时间：</span>
      <el-date-picker v-model="datepickers" type="datetimerange" range-separator="-" start-placeholder="开始日期"
        end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss" style="align-self:center;"
        @change="selectStatusLevelDateTypeRange" @clear="selectStatusLevelDateTypeRange">
      </el-date-picker>

      <span style="align-self:center; margin-left:20px;font-size:14px">预警地点：</span>
      <el-select v-model="position" clearable placeholder="请选择" style="width:150px; align-self:center;"
        @change="selectStatusLevelDateTypeRange" @clear="selectStatusLevelDateTypeRange">
        <el-option v-for="item in positionList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">预警类型：</span>
      <el-select v-model="type" clearable placeholder="请选择" style="width:150px; align-self:center;"
        @change="selectStatusLevelDateTypeRange" @clear="selectStatusLevelDateTypeRange">
        <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">预警等级：</span>
      <el-select v-model="level" clearable placeholder="请选择" style="width:150px; align-self:center;"
        @change="selectStatusLevelDateTypeRange" @clear="selectStatusLevelDateTypeRange">
        <el-option v-for="item in levelList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">预警状态：</span>
      <el-select v-model="status" clearable placeholder="请选择" style="width:150px; align-self:center;"
        @change="selectStatusLevelDateTypeRange" @clear="selectStatusLevelDateTypeRange">
        <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <download-excel class="export-excel-wrapper" :data="PIList" :fields="json_fields" name="预警信息报告.xls"
        style="margin-left:auto;align-self:center; margin-right:10px">
        <el-button type="primary" round icon="iconfont icon-icon-test"
          style="align-self:center; margin-right:10px">导出</el-button>
      </download-excel>

    </div>
    <div id="div-main">
      <el-table :data="PIList" border stripe style="width: 100%" height="100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="position" label="预警地点" align="center">
        </el-table-column>
        <el-table-column prop="type" label="预警类型" align="center">
        </el-table-column>
        <el-table-column prop="level" label="预警等级" align="center">
        </el-table-column>
        <el-table-column prop="content" label="预警内容" align="center">
        </el-table-column>
        <el-table-column prop="status" label="预警状态" align="center">
        </el-table-column>
        <el-table-column prop="project" label="所属工程" align="center">
        </el-table-column>
        <el-table-column width="150px" prop="startTime" label="发生时间" align="center">
        </el-table-column>
        <el-table-column width="150px" prop="overTime" label="解除时间" align="center">
        </el-table-column>
        <el-table-column prop="stayTime" label="持续时长" align="center">
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
    <el-dialog title="新增预警信息" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警地点:" prop="position">
              <el-input v-model="addForm.position"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属工程:" prop="project">
              <el-input v-model="addForm.project"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="addForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="addForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警内容:" prop="content">
              <el-input v-model="addForm.content"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警类型:" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择预警类型">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警等级:" prop="level">
              <el-select v-model="addForm.level" placeholder="请选择预警等级">
                <el-option v-for="item in levelList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警状态:" prop="status">
              <el-select v-model="addForm.status" placeholder="请选择预警状态">
                <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发生时间:" prop="startTime">
              <el-date-picker v-model="addForm.startTime" type="datetime" placeholder="选择日期时间" align="right"
                :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="解除时间:" prop="overTime">
              <el-date-picker v-model="addForm.overTime" type="datetime" placeholder="选择日期时间" align="right"
                :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
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
    <el-dialog title="编辑预警信息" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警地点:" prop="position">
              <el-input v-model="editForm.position"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属工程:" prop="project">
              <el-input v-model="editForm.project"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="editForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="editForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警内容:" prop="content">
              <el-input v-model="editForm.content"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警类型:" prop="type">
              <el-select v-model="editForm.type" placeholder="请选择预警类型">
                <el-option label="水位" value="水位"></el-option>
                <el-option label="流量" value="流量"></el-option>
                <el-option label="水质" value="水质"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警等级:" prop="level">
              <el-select v-model="editForm.level" placeholder="请选择预警等级">
                <el-option label="一般预警" value="一般预警"></el-option>
                <el-option label="严重预警" value="严重预警"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警状态:" prop="status">
              <el-select v-model="editForm.status" placeholder="请选择预警状态">
                <el-option label="未解除" value="未解除"></el-option>
                <el-option label="已解除" value="已解除"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发生时间:" prop="startTime">
              <el-date-picker v-model="editForm.startTime" type="datetime" placeholder="选择日期时间" align="right"
                :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="解除时间:" prop="overTime">
              <el-date-picker v-model="editForm.overTime" type="datetime" placeholder="选择日期时间" align="right"
                :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
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

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="position" label="预警地点" align="center">
      </el-table-column>
      <el-table-column prop="project" label="所属工程" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="content" label="预警内容" align="center">
      </el-table-column>
      <el-table-column prop="status" label="预警状态" align="center">
      </el-table-column>
      <el-table-column prop="level" label="预警等级" align="center">
      </el-table-column>
      <el-table-column prop="type" label="预警类型" align="center">
      </el-table-column>
      <el-table-column prop="startTime" label="发生时间" align="center">
      </el-table-column>
      <el-table-column prop="overTime" label="解除时间" align="center">
      </el-table-column>
      <el-table-column prop="stayTime" label="持续时长" align="center">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'
import { baseURL } from '@/main.js'


export default {
  name: '',
  data() {
    return {
      //时间快捷选项
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      //自定义导出字段
      json_fields: {
        预警地点: 'position',
        所属工程: 'project',
        经度: 'longitude',
        纬度: 'latitude',
        预警内容: 'content',
        预警类型: 'type',
        预警等级: 'level',
        预警状态: 'status',
        预警时间: 'startTime',
        结束时间: 'overTime',
        持续时长: 'stayTime'
      },
      json_meta: [
        [
          {
            ' key ': ' charset ',
            ' value ': ' utf- 8 '
          }
        ]
      ],
      position: '',
      positions: [],
      positionList: [],
      status: '',
      statuss: [],
      statusList: [],
      level: '',
      levels: [],
      levelList: [],
      type: '',
      types: [],
      typeList: [],
      value: '',
      // 时间选择器
      datepickers: ['', ''],
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 5
      },
      //数据列表
      PIList: [],
      //数据列表
      ExamplePI: [
        {
          position: "预警地点",
          project: '所属工程',
          content: "预警内容",
          type: "预警类型",
          level: "预警等级",
          status: "预警状态",
          startTime: "发生时间",
          overTime: "解除时间",
          stayTime: "持续时长"
        }
      ],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //上传的文件
      fileList: [],
      //添加表单数据
      addForm: {
        position: '',
        project: '',
        content: '',
        type: '',
        level: '',
        status: '',
        longitude: '',
        latitude: '',
        startTime: '',
        overTime: '',
        stayTime: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        position: [
          { required: true, message: '请输入预警地点', trigger: 'blur' }
        ],
        project: [
          { required: true, message: '请输入所属工程', trigger: 'blur' }
        ],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }, {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度:-180到180之间',
          trigger: 'blur'
        }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }, {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度:-90到90之间',
          trigger: 'blur'
        }],
        content: [
          { required: true, message: '请输入预警内容', trigger: 'blur' }
        ],
        type: [{ required: true, message: '请输入预警类型', trigger: 'blur' }],
        level: [{ required: true, message: '请输入预警等级', trigger: 'blur' }],
        status: [
          { required: true, message: '请输入预警状态', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请输入发生时间', trigger: 'blur' }
        ],
        overTime: [
          { required: false, message: '请输入解除时间', trigger: 'blur' }
        ]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        position: [
          { required: true, message: '请输入预警地点', trigger: 'blur' }
        ],
        project: [
          { required: true, message: '请输入所属工程', trigger: 'blur' }
        ],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }, {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度:-180到180之间',
          trigger: 'blur'
        }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }, {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度:-90到90之间',
          trigger: 'blur'
        }],
        content: [
          { required: true, message: '请输入预警内容', trigger: 'blur' }
        ],
        type: [{ required: true, message: '请输入预警类型', trigger: 'blur' }],
        level: [{ required: true, message: '请输入预警等级', trigger: 'blur' }],
        status: [
          { required: true, message: '请输入预警状态', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请输入发生时间', trigger: 'blur' }
        ],
        overTime: [
          { required: false, message: '请输入解除时间', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/warning-information/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editPI()
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.selectStatusLevelDateTypeRange()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.selectStatusLevelDateTypeRange()
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
    //得到所有预警状态
    async getAllStatuss() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '预警状态'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.statuss = res.data
      this.statusList = this.statuss.map(status => ({
        value: status,
        label: status
      }))
    },
    //得到所有预警等级
    async getAllLevels() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '预警等级'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.levels = res.data
      this.levelList = this.levels.map(level => ({
        value: level,
        label: level
      }))
    },
    //得到所有预警类型
    async getAllTypes() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '预警类型'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.types = res.data
      this.typeList = this.types.map(type => ({
        value: type,
        label: type
      }))
    },
    //得到所有预警地点
    async getAllPositions() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '预警地点'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.positions = res.data
      this.positionList = this.positions.map(position => ({
        value: position,
        label: position
      }))
    },
    //新增预警信息
    addPI() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/warning-information/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.selectStatusLevelDateTypeRange()
      })
    },
    //编辑角色信息表单
    editPI() {
      // 直接发送请求，不进行表单验证
      const { data: res } = this.$http.post(
        '/warning-information/update',
        {
          id: this.editForm.id,
          position: this.editForm.position,
          project: this.editForm.project,
          content: this.editForm.content,
          type: this.editForm.type,
          level: this.editForm.level,
          status: this.editForm.status,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          startTime: this.editForm.startTime,
          overTime: this.editForm.overTime
        }
      ).then(res => {
        console.log(res)
        // 检查响应代码
        if (res.data.code !== 200) {
          // 如果响应代码不是200，则显示错误信息
          this.$message.error('解除失败');
        } else {
          // 如果更新成功，显示成功信息
          this.$message.success('预警已解除');
        }
      }).catch(error => {
        // 处理请求失败的情况
        console.error('请求失败:', error);
        this.$message.error('请求失败');
      }).finally(() => {
        // 无论成功或失败都会执行的代码
        this.editDialogVisible = false;
        this.selectStatusLevelDateTypeRange();
      });

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
        '/warning-information/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.selectStatusLevelDateTypeRange()
    },
    //根据监测站点搜索预警信息
    async searchInputPosition() {
      if (this.position === null) {
        this.position = ''
      }
      const { data: res } = await this.$http.get(
        '/warning-information/position',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            position: this.position,
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.PIList = res.data.records
      this.total = res.data.total
    },
    //搜索 预警时间 预警状态 预警等级 预警类型
    async selectStatusLevelDateTypeRange() {
      if (this.datepickers === null) {
        this.datepickers = ['', '']
      }
      if (this.status == null) {
        this.status = ''
      }
      if (this.level == null) {
        this.level = ''
      }
      if (this.type == null) {
        this.type = ''
      }
      if (this.position == null) {
        this.position = ''
      }
      const { data: res } = await this.$http.get(
        '/warning-information/list',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            status: this.status,
            level: this.level,
            type: this.type,
            position: this.position,
            startTime: this.datepickers[0],
            endTime: this.datepickers[1],
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.PIList = res.data.records
      console.log(this.PIList)
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
          '预警信息数据列表模板.xlsx'
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
        this.selectStatusLevelDateTypeRange()
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
      let url = `${baseURL}/warning-information/import-excel`
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
    },
    // 初始化日期
    initDate() {
      const now = new Date();
      const twoDaysAgo = new Date(now.getTime() - 2 * 24 * 60 * 60 * 1000);

      // 定义一个函数来格式化日期和时间  
      function formatDateTime(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
      this.datepickers = [formatDateTime(twoDaysAgo), formatDateTime(now)];
    },
  },
  created() {
    this.initDate()
    this.selectStatusLevelDateTypeRange()
    this.getAllTypes()
    this.getAllStatuss()
    this.getAllLevels()
    this.getAllPositions()
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

#div-header1 {
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

.block {
  align-self: center;
  margin-left: 10px;
  font-size: 14px;
}

.upload-demo {
  margin: 10px 200px;
}
</style>
