<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程巡检</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/pollingrecord' }">巡检记录</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <span style="align-self:center; margin-left:20px;font-size:14px">巡检站点：</span>
      <el-select v-model="project" clearable placeholder="请选择" style="align-self:center;width:150px;" @change="getPRList"
        @clear="getPRList">
        <el-option v-for="item in projectList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">巡检时间：</span>
      <el-date-picker v-model="datepickers" type="datetimerange" range-separator="-" start-placeholder="开始日期"
        end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss" style="align-self:center;"
        @change="getPRList" @clear="getPRList">
      </el-date-picker>

      <span style="align-self:center; margin-left:20px;font-size:14px">异常情况：</span>
      <el-select v-model="abnormal" clearable placeholder="请选择" style="align-self:center;width:150px;" @change="getPRList"
        @clear="getPRList">
        <el-option v-for="item in abnormalList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">处理状态：</span>
      <el-select v-model="solve" clearable placeholder="请选择" style="align-self:center;width:150px;" @change="getPRList"
        @clear="getPRList">
        <el-option v-for="item in solveList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="align-self:center; margin-left:20px;font-size:14px">负责人：</span>
      <el-select v-model="person" clearable placeholder="请选择" style="align-self:center;width:150px;" @change="getPRList"
        @clear="getPRList">
        <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <el-button type="primary" round icon="iconfont icon-icon-test"
        style="align-self:center;margin-right:10px;margin-left: auto;" @click="exportExcel">导出</el-button>
    </div>

    <div id="div-main">
      <el-table :data="PRList" border stripe style="width: 100%" height="100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="project" label="巡检站点" align="center">
        </el-table-column>
        <!-- <el-table-column prop="longitude" label="经度" align="center">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" align="center">
        </el-table-column> -->
        <el-table-column prop="type" label="巡检类型" align="center">
        </el-table-column>
        <el-table-column prop="abnormal" label="异常情况" align="center">
        </el-table-column>
        <el-table-column prop="situation" label="巡检情况" align="center" :formatter="formatSituation">
        </el-table-column>
        <el-table-column prop="solve" label="处理状态" align="center">
        </el-table-column>
        <el-table-column prop="image" label="图片" width="250px" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.image">
              <div v-for="(img, index) in getLimitedImages(scope.row.image)" :key="index"
                style="display: inline-block;">
                <img :src="`${photo_url}${img}`" style="width: auto; height: 50px; margin-right: 5px;" alt="图片">
              </div>
              <span v-if="getImageCount(scope.row.image) > 3">...</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="person" label="负责人" align="center">
        </el-table-column>
        <el-table-column prop="date" label="日期" align="center" width="200">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="300">
          <template slot-scope="scope">
            <!-- <el-button v-if="scope.row.solve === '未处理'" size="mini" type="primary" @click="solvePolling(scope.row.id)">
              处理巡检信息
            </el-button> -->
            <el-button size="mini" @click="showEditDialog(scope.row.id)">查看</el-button>
            <!-- <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 1000000]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增巡检记录" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="巡检站点:" prop="project">
              <!-- <el-input v-model="addForm.project"></el-input> -->
              <el-select v-model="addForm.project">
                <el-option v-for="item in projectList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
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
            <el-form-item label="巡检类型:" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择巡检类型">
                <el-option label="日常巡检" value="日常巡检"></el-option>
                <el-option label="定期巡检" value="定期巡检"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="异常情况:" prop="abnormal">
              <el-input v-model="addForm.abnormal"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="巡检情况:" prop="situation">
              <el-input v-model="addForm.situation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人:" prop="person">
              <el-input v-model="addForm.person"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="日期:" prop="date">
              <el-date-picker v-model="addForm.date" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="上传图片:" prop="image">
              <el-upload ref="upload" action="/inspection-records/upload2" :file-list="fileList"
                :before-upload="beforeUpload" :on-change="handleChange" :on-remove="handleRemove"
                :http-request="httpRequest" list-type="picture-card" multiple
                :style="{ display: 'flex', flexWrap: 'wrap', justifyContent: 'flex-start' }">
                <!-- 自定义触发上传的按钮 -->
                <div style="width: 100px; height: 100px; line-height: 100px; text-align: center;">
                  <i class="el-icon-plus"></i>
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPR">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="查看巡检记录" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="巡检站点:" prop="project">
              <el-select v-model="editForm.project" style="width: 100%;" disabled>
                <el-option v-for="item in projectList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="处理情况:" prop="solve">
              <el-select v-model="editForm.solve" style="width: 100%;" disabled>
                <el-option v-for="item in solveList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="editForm.longitude" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="editForm.latitude" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="巡检类型:" prop="type">
              <el-select v-model="editForm.type" placeholder="请选择巡检类型" style="width: 100%;" disabled>
                <el-option label="日常巡检" value="日常巡检"></el-option>
                <el-option label="定期巡检" value="定期巡检"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="异常情况:" prop="abnormal">
              <el-input v-model="editForm.abnormal" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="巡检情况:" prop="situation">
              <div class="textarea-container">
                <textarea v-model="editForm.situation" maxlength="200" rows="4" class="custom-textarea"
                  @input="updateCharacterCount" disabled></textarea>
                <!-- <div class="char-count">{{ characterCount }}/200</div> -->
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="负责人:" prop="person">
              <el-input v-model="editForm.person" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="日期:" prop="date">
              <el-date-picker v-model="editForm.date" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }" disabled>
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="图片:" prop="image">
              <div style="display: flex; flex-wrap: wrap; align-items: center;">
                <div v-for="(url, index) in imageList2" :key="index" class="image-container"
                  style="position: relative; display: inline-block;">
                  <img :src="`${photo_url}${url}`" @click="showBigImage(index)"
                    style="width: auto; height: 145px; cursor: pointer;">
                  <!-- 删除图标 -->
                  <!-- <el-button class="close-icon" type="text" @click="removeImage(index)" disabled>
                    <i class="el-icon-close"></i>
                  </el-button> -->
                </div>

                <!-- 放大的图片模态框 -->
                <el-dialog ref="elDialog" :visible.sync="dialogVisible" width="70%" custom-class="my-custom-dialog">
                  <div class="dialog-body">
                    <el-button @click="previousImage" icon="el-icon-arrow-left"></el-button>
                    <img :src="`${photo_url}${currentBigImageUrl}`" style="width: 100%; max-height: 600px;">
                    <el-button @click="nextImage" icon="el-icon-arrow-right"></el-button>
                  </div>
                </el-dialog>
                <!-- 图片上传区域 -->
                <!-- <div style="margin-bottom: 20px;">
                  <el-upload action="/inspection-records/upload2" :before-upload="beforeUpload"
                    :file-list="uploadFileList" list-type="picture-card" :show-file-list="false"
                    :http-request="httpRequest2" disabled>
                    <i class="el-icon-plus"></i>
                  </el-upload>
                </div> -->
                <!-- 暂无图片提示 -->
                <div v-if="imageList2.length === 0" style="width: 100%; text-align: center;">暂无图片</div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editPR" disabled>确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导出表格 -->
    <el-table id="table" :data="ALLPRList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="project" label="巡检站点" align="center">
      </el-table-column>
      <!-- <el-table-column prop="position" label="位置" align="center">
      </el-table-column> -->
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="type" label="巡检类型" align="center">
      </el-table-column>
      <el-table-column prop="abnormal" label="异常情况" align="center">
      </el-table-column>
      <el-table-column prop="situation" label="巡检情况" align="center">
      </el-table-column>
      <el-table-column prop="solve" label="处理状态" align="center">
      </el-table-column>
      <el-table-column prop="image" label="图片" align="center">
      </el-table-column>
      <el-table-column prop="person" label="负责人" align="center">
      </el-table-column>
      <el-table-column prop="date" label="日期" align="center">
      </el-table-column>
    </el-table>
    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>

      <!-- </el-table-column> -->
      <el-table-column prop="project" label="巡检站点" align="center">
      </el-table-column>
      <!-- <el-table-column prop="position" label="位置" align="center">
      </el-table-column> -->
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="type" label="巡检类型" align="center">
      </el-table-column>
      <el-table-column prop="abnormal" label="异常情况" align="center">
      </el-table-column>
      <el-table-column prop="situation" label="巡检情况" align="center">
      </el-table-column>
      <el-table-column prop="person" label="负责人" align="center">
      </el-table-column>
      <el-table-column prop="date" label="日期" align="center">
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
      photo_url: "http://111.4.68.108:8081/photo/",
      characterCount: 0,
      inputWord: '',
      //巡检类型字段
      type: '',
      types: '',
      typeList: [],
      datepickers: ['', ''],
      status: '',
      statusList: [
        {
          value: '未审核',
          label: '未审核'
        },
        {
          value: '已审核',
          label: '已审核'
        }
      ],
      project: '',
      projects: [],
      projectList: [],
      //异常情况
      abnormal: '',
      abnormals: [],
      abnormalList: [],
      //是否处理
      solve: '',
      solves: [],
      solveList: [],
      //负责人
      person: '',
      persons: [],
      personList: [],
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      notReviewedNum: '',
      //数据列表
      PRList: [],
      ALLPRList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //图片文件
      fileList: [],
      imageList: [],
      imageList2: [],
      currentBigImageUrl: '', // 当前被放大的图片URL
      currentImageIndex: -1, // 当前图片的索引
      dialogVisible: false,
      uploadFileList: [],
      //数据列表
      ExamplePI: [
        {
          project: "水厂1",
          longitude: '123.5',
          latitude: '30.9',
          type: "定期巡检",
          situation: "无异常",
          person: "张三",
          solve: "未处理",
          date: '2023-02-24 10:43:17',
        }
      ],
      //添加表单数据
      addForm: {
        project: '',
        longitude: '',
        latitude: '',
        type: '',
        abnormal: '',
        situation: '',
        solve: '',
        image: '',
        person: '',
        date: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        project: [{ required: true, message: '请输入巡检站点', trigger: 'blur' }],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }, {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度',
          trigger: 'blur'
        }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }, {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度',
          trigger: 'blur'
        }],
        type: [{ required: true, message: '请输入巡检类型', trigger: 'blur' }],
        abnormal: [
          { required: false, message: '请选择异常情况', trigger: 'blur' }
        ],
        situation: [
          { required: false, message: '请输入巡检情况', trigger: 'blur' }
        ],
        solve: [
          { required: false }
        ],
        person: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        date: [{ required: true, message: '请输入日期', trigger: 'blur' }],
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        project: [{ required: true, message: '请输入巡检站点', trigger: 'blur' }],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }, {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度',
          trigger: 'blur'
        }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }, {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度',
          trigger: 'blur'
        }],
        type: [{ required: true, message: '请输入巡检类型', trigger: 'blur' }],
        abnormal: [
          { required: true, message: '请选择异常情况', trigger: 'blur' }
        ],
        situation: [
          { required: false, message: '请输入巡检情况', trigger: 'blur' }
        ],
        solve: [
          { required: false }
        ],
        person: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        date: [{ required: true, message: '请输入日期', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 处理巡检记录
    async solvePolling(id) {
      console.log(111)
      await this.getPolling(id);
      console.log(222)
      this.editSolvePolling();
      console.log(333)
    },
    // 获取巡检记录数据
    async getPolling(id) {
      const { data: res } = await this.$http.get(
        '/inspection-records/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.imageList2 = this.editForm.image ? this.editForm.image.split(';') : []
    },
    //编辑巡检记录
    async editSolvePolling() {
      const { data: res } = await this.$http.post(
        '/inspection-records/solveRecords',
        {
          id: this.editForm.id,
          project: this.editForm.project,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          type: this.editForm.type,
          abnormal: this.editForm.abnormal,
          situation: this.editForm.situation,
          person: this.editForm.person,
          image: this.editForm.image,
          date: this.editForm.date
        }
      )
      if (res.code !== 200) {
        return this.$message.error('更新数据失败')
      }
      this.$message.success('更新数据成功')
      this.editDialogVisible = false
      this.getPRList()
    },
    //获取数据列表
    async getPRList() {
      if (this.datepickers === null) {
        this.datepickers = ['', '']
      }
      const { data: res } = await this.$http.get(
        '/inspection-records/list',
        {
          params: {
            pageSize: this.queryinfo.pageSize,
            currentPage: this.queryinfo.currentPage,
            project: this.project,
            abnormal: this.abnormal,
            person: this.person,
            solve: this.solve,
            startTime: this.datepickers[0],
            endTime: this.datepickers[1],
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.PRList = res.data.records
      this.total = res.data.total
      console.log(this.PRList);
    },
    async getALLPRList() {
      const { data: res } = await this.$http.get('/inspection-records/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ALLPRList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/inspection-records/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.imageList2 = this.editForm.image ? this.editForm.image.split(';') : [],
        this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getPRList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getPRList()
    },
    //序号连续
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
      this.imageList = []
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
      this.uploadFileList = []
    },
    getLimitedImages(imageString) {
      // 将图片路径字符串分割成数组，并返回前三个元素
      const imagesArray = imageString.split(';').map(img => img.trim());
      console.log(imagesArray)
      return imagesArray.slice(0, 3);
    },
    getImageCount(imageString) {
      // 将图片路径字符串分割成数组，并返回数组的长度
      return imageString.split(';').length;
    },
    //新增枢纽供水工程
    addPR() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/inspection-records/save',
          this.addForm,
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.fileList = []
        this.getPRList()
      })
    },
    //编辑角色信息表单
    editPR() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/inspection-records/update',
          {
            id: this.editForm.id,
            project: this.editForm.project,
            longitude: this.editForm.longitude,
            latitude: this.editForm.latitude,
            type: this.editForm.type,
            abnormal: this.editForm.abnormal,
            situation: this.editForm.situation,
            solve: this.editForm.solve,
            person: this.editForm.person,
            image: this.editForm.image,
            date: this.editForm.date
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getPRList()
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
        '/inspection-records/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getPRList()
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
          '工程巡检记录数据列表模板.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
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
          '工程巡检报告.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
    },
    //查询所有巡检站点
    async selectProjects() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '巡检站点'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.projects = res.data
      this.projectList = this.projects.map(project => ({
        value: project,
        label: project
      }))
    },
    //查询所有巡检站点
    async selectExcepts() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '异常情况'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.abnormals = res.data
      this.abnormalList = this.abnormals.map(abnormal => ({
        value: abnormal,
        label: abnormal
      }))
    },
    //查询所有巡检类型
    async selectTypes() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '巡检类型'
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
    //查询所有处理类型
    async selectSolves() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '处理类型'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.solves = res.data

      this.solveList = this.solves.map(solve => ({
        value: solve,
        label: solve
      }))
    },
    //查询所有巡检站点
    async selectPersons() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '负责人'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.persons = res.data
      this.personList = this.persons.map(person => ({
        value: person,
        label: person
      }))
    },
    // 新增图片的函数
    beforeUpload(file) {
      // 允许上传的图片文件格式列表
      let acceptList = ['jpg', 'jpeg', 'png', 'gif', 'bmp'];
      // 根据文件名获取文件的后缀名
      let fileType = file.name
        .split('.')
        .pop()
        .toLowerCase();
      // 判断文件格式是否符合要求
      if (acceptList.indexOf(fileType) === -1) {
        this.$message.error('只能上传 jpg/jpeg/png/gif/bmp 格式的图片文件 !');
        return false;
      }
      return true;
    },
    async httpRequest(param) {
      let fileObj = param.file;
      let formData = new FormData();
      formData.append('image', fileObj);
      let url = '/inspection-records/upload2';
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      };
      try {
        const response = await this.$http.post(url, formData, config);
        console.log(response)
        if (response.data.code === 200) {
          this.$message.success('上传成功');
          let fileItem = this.$refs.upload.fileList.find(f => f.uid === param.file.uid);
          if (fileItem) {
            this.imageList.push({
              uid: fileItem.uid,
              url: response.data.data
            });
            this.updateImageString();
          }
        } else {
          this.$message.error(response.data.message || '图片上传失败');
        }
      } catch (error) {
        console.error('上传失败:', error);
        this.$message.error('图片上传失败，请重试！');
      }
    },
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    handleRemove(file) {
      this.imageList = this.imageList.filter(item => item.uid !== file.uid);
      this.updateImageString()
      this.$message.success('图片已删除');
    },
    updateImageString() {
      this.addForm.image = this.imageList.map(item => item.url).join(';');
    },

    // 编辑图片的函数
    async httpRequest2(param) {
      let fileObj = param.file
      let formData = new FormData()
      formData.append('image', fileObj)
      let url = '/inspection-records/upload2'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      try {
        const response = await this.$http.post(url, formData, config);
        if (response.data.code === 200) {
          this.$message.success('上传成功');
          console.log(response.data);
          if (this.editForm.image) {
            this.editForm.image += ';' + response.data.data;
          } else {
            this.editForm.image = response.data.data;
          }
          this.imageList2 = this.editForm.image ? this.editForm.image.split(';') : [];
        } else {
          this.$message.error(response.data.message || '图片上传失败');
        }
      } catch (error) {
        console.error('上传失败:', error);
        this.$message.error('图片上传失败，请重试！');
      }
    },
    removeImage(index) {
      this.$confirm('确定要删除这张图片吗？')
        .then(() => {
          this.imageList2.splice(index, 1);
          this.editForm.image = this.imageList2.join(';');
          this.$message.success('图片已删除');
        })
        .catch(() => {
          // 取消操作
        });
    },
    showBigImage(index) {
      this.$refs.elDialog.$el.firstChild.style.height = '700px';
      this.currentImageIndex = index;
      this.currentBigImageUrl = this.imageList2[index];
      this.dialogVisible = true;
    },
    previousImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--;
        this.currentBigImageUrl = this.imageList2[this.currentImageIndex];
      }
    },
    nextImage() {
      if (this.currentImageIndex < this.imageList2.length - 1) {
        this.currentImageIndex++;
        this.currentBigImageUrl = this.imageList2[this.currentImageIndex];
      }
    },
    formatSituation(row, column, cellValue, index) {
      console.log(this.cellValue);
      if (cellValue) {
        return cellValue.length > 30 ? cellValue.substring(0, 30) + '...' : cellValue;
      }
      return '';
    },
    updateCharacterCount() {
      this.characterCount = this.editForm.situation.length;
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
    this.getPRList()
    this.getALLPRList()
    this.selectProjects()
    this.selectTypes()
    this.selectSolves()
    this.selectPersons()
    this.selectExcepts()
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
  align-items: center;
  background-color: rgb(253, 242, 228);
}

.review-label {
  text-align: center;
  margin-left: 20px;
  font-size: 14px;
  color: #409eff;
  /* 蓝色字体 */
}

.review-count {
  text-align: center;
  margin-left: 20px;
  font-size: 14px;
  color: #409eff;
  /* 蓝色字体 */
  border: 1px solid #409eff;
  /* 蓝色边框 */
  border-radius: 10px;
  /* 圆角边框 */
  padding: 5px 10px;
  /* 内边距 */
  font-weight: bold;
  /* 字体加粗 */
  background-color: #ecf5ff;
  /* 浅色背景 */
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

.image-container {
  position: relative;
  margin-right: 10px;
  margin-bottom: 10px;
}

.image-container .close-icon {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 5px;
  border-radius: 50%;
  cursor: pointer;
}

.image-container .close-icon i {
  font-size: 16px;
}

.my-custom-dialog .el-dialog {
  display: flex;
  /* 使用Flexbox布局 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  width: 70% !important;
  /* 确保宽度不被覆盖 */
}

.my-custom-dialog .dialog-header {
  height: 0%;
}

.my-custom-dialog .dialog-body {
  width: 100%;
  height: 600px;
  display: flex;
  justify-content: space-between;
  /* 水平居中图片 */
  align-items: center;
}


/* 限制图片的最大高度，防止超出视口 */
.my-custom-dialog img {
  max-width: 60%;
  max-height: 500px;
  height: auto;
}

.textarea-container {
  position: relative;
}

.custom-textarea {
  width: 100%;
  padding: 10px 10px 10px 20px;
  box-sizing: border-box;
  /* 确保 padding 不影响整体宽度 */
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  resize: none;
  /* 禁止用户调整大小 */
}

.char-count {
  position: absolute;
  bottom: 5px;
  right: 13px;
  color: #909399;
  font-size: 10px;
}
</style>
