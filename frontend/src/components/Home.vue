<template>
  <div class="home-container">
    <div v-if="$route.path !== '/home/bigscreen'">
      <!-- 侧边栏和主内容区域 -->
      <div class="home-main-layout">
        <!-- 侧边栏 -->
        <aside class="sidebar" :class="{ collapsed: isCollapsed }">
          <div class="logo-box">
            <img src="../assets/img/sea2.jpg" class="logo-img" />
            <span class="logo-title">智慧荆竹水库</span>
        </div>
          <div class="sidebar-scroll">
            <el-menu
              :default-active="activePath"
              class="el-menu-vertical-demo"
              mode="vertical"
              :collapse="isCollapsed"
              background-color="#263445"
              text-color="#fff"
              active-text-color="#ffd04b"
              router
            >
              <!-- 静态主业务菜单 -->
              <el-menu-item index="/home/onemap"><i class="el-icon-map-location"></i> <span slot="title">一张图</span></el-menu-item>
              <el-submenu index="waterinfo">
                <template slot="title"><i class="el-icon-menu"></i> 水雨情管理</template>
                <el-menu-item index="/home/waterinfo/station">降雨查询</el-menu-item>
                <el-menu-item index="/home/waterinfo/waterstorage">水库水位查询</el-menu-item>
                </el-submenu>
              <el-menu-item index="/home/monitor">
                <i class="el-icon-view"></i> <span slot="title">视频监测</span>
              </el-menu-item>
              <el-submenu index="dam">
                <template slot="title"><i class="el-icon-s-data"></i> 大坝安全监测</template>
                <el-menu-item index="/home/dam/seepage">渗流压力监测分析</el-menu-item>
                <el-menu-item index="/home/dam/seepageflowmonitor">渗流量监测分析</el-menu-item>
                <el-menu-item index="/home/dam/surfacedisplacement">地表位移</el-menu-item>
              </el-submenu>
              <el-submenu index="gate">
                <template slot="title"><i class="el-icon-lock"></i> 闸门监控管理</template>
                <el-menu-item index="/home/gate_status">闸门实时状态查询</el-menu-item>
                <el-menu-item index="/home/gatereport">闸门报表</el-menu-item>
              </el-submenu>
              <el-submenu index="yjgl">
                <template slot="title"><i class="el-icon-warning"></i> 预警管理</template>
                <el-menu-item index="/home/prewarninginformation">预警信息处理</el-menu-item>
                <el-menu-item index="/home/prewarningindicatorsetting">预警指标设定</el-menu-item>
              </el-submenu>

              <!-- 静态综合报表菜单 -->
              <el-submenu index="zhbb">
                <template slot="title"><i class="el-icon-s-data"></i> 综合报表</template>
                  <el-menu-item index="/home/zhbb/zbgl/zbap">值班安排</el-menu-item>
                  <el-menu-item index="/home/zhbb/zbgl/zbrz">值班日志</el-menu-item>
              </el-submenu>
              <!-- 系统工具 -->
              <!-- 动态菜单，去除与静态主业务菜单重复的项 -->
              <template v-for="item in filteredMenuList">
                <el-menu-item
                  v-if="item.children.length === 0"
                  :index="item.path.startsWith('/') ? item.path : '/home/' + item.path"
                  :key="'menuitem-' + item.id"
                >
                  <i :class="menuIconClass(item.name)"></i>
            <span slot="title">{{ removeReadOnly(item.name) }}</span>
          </el-menu-item>
                <el-submenu
                  v-else
                  :index="item.id + ''"
                  :key="'submenu-' + item.id"
                >
            <template slot="title">
                    <i :class="menuIconClass(item.name)"></i>
              <span>{{ removeReadOnly(item.name) }}</span>
            </template>
                  <el-menu-item v-for="subItem in item.children" :index="subItem.path" :key="subItem.id">
                    <i class="el-icon-caret-right"></i>
              <span>{{ removeReadOnly(subItem.name) }}</span>
            </el-menu-item>
          </el-submenu>
        </template>
      </el-menu>
    </div>
          <!-- 渐变悬浮收缩按钮，仅在未收缩时显示 -->
          <div v-if="!isCollapsed" class="sidebar-collapse-float" @click="isCollapsed = true">
            <i class="el-icon-s-fold"></i>
          </div>
        </aside>
        <!-- 侧边栏收缩后显示的展开按钮 -->
        <div v-if="isCollapsed" class="sidebar-expand-float" @click="isCollapsed = false">
          <i class="el-icon-s-unfold"></i>
        </div>
        <!-- 主体区域 -->
        <div class="main-content">
          <!-- 顶部栏：面包屑+用户栏 -->
          <div class="topbar">
            <el-breadcrumb separator="/" class="breadcrumb-bar">
              <el-breadcrumb-item v-for="(item, idx) in breadcrumbList" :key="idx">
                <span v-if="idx === breadcrumbList.length - 1">{{ item.label }}</span>
                <router-link v-else :to="item.path">{{ item.label }}</router-link>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <div class="userbar">
              <el-dropdown>
                <span class="el-dropdown-link">
                  <i class="el-icon-user"></i> {{ UserList.username || '用户' }} <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="usersDialogVisible = true">个人信息</el-dropdown-item>
                  <el-dropdown-item @click.native="changePasswordDialogVisible = true">修改密码</el-dropdown-item>
                  <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <!-- 内容区 -->
          <div class="content-area">
            <keep-alive>
      <router-view></router-view>
            </keep-alive>
    </div>
        </div>
      </div>
    </div>
    <router-view v-else />
      <!-- 个人信息/修改密码弹窗等保留 -->
    <el-dialog :visible.sync="usersDialogVisible" width="900px" center>
      <template slot="title">
        <div class="userTitle">个人信息</div>
      </template>
      <el-descriptions class="margin-top" :column="2" border>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <span>{{ UserList.username }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            专业
          </template>
          <span>{{ UserList.major }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            姓名
          </template>
          <span>{{ UserList.name }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-tickets"></i>
            电子邮件
          </template>
          <span>{{ UserList.email }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            身份证号
          </template>
          <span>
            {{ UserList.idNumber }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            家庭住址
          </template>
          <span>
            {{ UserList.address }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            性别
          </template>
          <span>
            {{ UserList.gender }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            开始工作时间
          </template>
          <span>
            {{ UserList.workingTime }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            技术职称
          </template>
          <span>
            {{ UserList.technicalTitle }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            出生年月
          </template>
          <span>
            {{ UserList.birthday }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            学历
          </template>
          <span>
            {{ UserList.academicQualifications }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            政治面貌
          </template>
          <span>
            {{ UserList.politicalAppearance }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            毕业院校
          </template>
          <span>
            {{ UserList.graduationInstitution }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:30%;">
          <template slot="label">
            <i class="el-icon-tickets"></i>
            备注
          </template>
          <span style="width:100px">
            {{ UserList.note }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-style="width:20%;" content-style="width:80%;">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号码
          </template>
          <span>
            {{ UserList.phoneNumber }}
          </span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="changePasswordDialogVisible" width="400px"
      @close="changePasswordDialogClosed" center :close-on-press-escape="false">
      <el-form :model="passwordForm" :rules="passwordFormRules" ref="passwordFormRef" label-width="120px" size="small">
        <el-form-item label="原密码：" prop="currentPassword">
          <el-input v-model="passwordForm.currentPassword" style="width: 200px;" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码：" prop="password">
          <el-input v-model="passwordForm.password" type="password" style="width: 200px;"></el-input>
        </el-form-item>
          <el-form-item label="确认密码：" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" style="width: 200px;"></el-input>
        </el-form-item>
      </el-form>
        <div slot="footer" class="dialog-footer">
        <el-button @click="changePasswordDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitPasswordForm">确 定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
const breadcrumbMap = {
  '/home': '首页',
  '/home/onemap': '一张图',
  '/home/bigscreen': '可视化大屏',
  '/home/waterinfo': '水雨情管理',
  '/home/dam': '大坝安全监测',
  '/home/gate': '闸门监控管理',
  '/home/monitor': '实时监测',
  '/home/yjgl': '预警管理',
  '/home/szqx': '水质气象数据集成',
  '/home/prewarninginformation': '预警信息处理',
  '/home/prewarningindicatorsetting': '预警指标设定',
  '/home/szqx/statistics': '水质监测数据统计',
  '/home/szqx/evaluation': '水质评测和预测分析',
  '/home/szqx/status': '水质监测点工作状态统计',
  '/home/szqx/overlimit': '超标数据时段分析',
  '/home/szqx/trace': '水质异常溯源分析',
  '/home/szqx/history': '历史数据查询',
  '/home/szqx/weather': '查询天气预报',
  '/home/szqx/warning': '气象信息预警',
  '/home/szqx/geowarning': '地质灾害预警信息',
  '/home/dam/seepage': '渗流量查看',
  '/home/dam/seepagewater': '渗流量趋势分析',
  '/home/dam/seepageflowmonitor': '渗流量监测分析',
  '/home/dam/sllbaobiao': '渗流量报表',
  '/home/dam/horizontal': '水平位移查看',
  '/home/dam/vertical': '垂直位移查看',
  '/home/dam/bxbaobiao': '变形报表',
  '/home/dam/surfacedisplacement': '地表位移',
  '/home/waterinfo/waterstorage': '水库水位',
  '/home/waterinfo/riverstation': '河道站水位',
  '/home/waterinfo/annualwaterstation': '单站历年水情',
  '/home/waterinfo/waterreport': '水情报表',
  '/home/waterinfo/station': '水库实时雨情',
  '/home/waterinfo/stpextremumb': '测站极值信息',
  '/home/waterinfo/dailyrainfall': '逐日时段雨量',
  '/home/waterinfo/rainbaobiao': '雨情报表',
  '/home/baseinfo/floodimpact': '洪水影响情况',
  '/home/baseinfo/floodhistory': '历史洪水灾害情况',
  '/home/baseinfo/basininfo': '库区基本情况',
  '/home/baseinfo/floodplan': '洪水防御预案',
  '/home/baseinfo/warningindex': '预警指标',
  '/home/baseinfo/warningfacility': '预警设施',
  '/home/baseinfo/dangerzone': '危险区',
  '/home/baseinfo/transferroute': '安置点及转移路线',
  '/home/baseinfo/responsibility': '应急单位和责任人',
  '/home/system': '系统管理',
  '/home/system/organization': '机构信息',
  '/home/system/person': '人员信息',
  // 综合报表新增路径
  '/home/zhbb/zbgl/zbap': '值班安排',
  '/home/zhbb/zbgl/zbrz': '值班日志',
  '/home/zhbb/zbgl/fxsb': '防汛设备管理',
  '/home/appdownload': 'app下载',
  '/home/engineeringpolling': '工程巡检',
  '/home/pollingrecord': '维护记录',
  '/home/maintencerecord': '维护记录',
  // ... 可继续补充其它路径
};
const breadcrumbGroup = [
  {
    groupPath: '/home/gate',
    groupLabel: '闸门监控管理',
    children: [
      '/home/gate_status',
      '/home/gatecontrol',
      '/home/gatereport'
    ]
  },
  {
    groupPath: '/home/baseinfo',
    groupLabel: '基础工情查询',
    children: [
      '/home/monitorsite',
      '/home/monitorsiteread',
      '/home/monitoritem',
      '/home/monitoritemread',
      '/home/reservoir',
      '/home/reservoirread',
      '/home/baseinfo/floodimpact',
      '/home/baseinfo/floodhistory',
      '/home/baseinfo/basininfo',
      '/home/baseinfo/floodplan',
      '/home/baseinfo/warningfacility',
      '/home/baseinfo/dangerzone',
      '/home/baseinfo/responsibility'
    ]
  },
  // 可继续添加其他分组
];
export default {
  name: 'Home',
  data() {
    return {
      windowwid: '',
      menulist: [],
      dialogFormVisible: false,
      activePath: '',
      usersDialogVisible: false,
      changePasswordDialogVisible: false,
      UserList: [],
      passwordForm: {
        password: '',
        currentPassword: '',
        confirmPassword: '',
      },
      passwordFormRules: {
        password: [
          { required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        currentPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { pattern: /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/, message: '密码必须包含大写字母、小写字母、数字和特殊符号，且至少8位', trigger: 'blur' }
        ]
      },
      isCollapsed: false
    }
  },
  computed: {
    processedMenuList() {
      let list = JSON.parse(JSON.stringify(this.menulist || []));

      // 确保移除可能存在的"安全监控"菜单项
      list = list.filter(item => item.name !== '安全监控');
      
      // 移除"水质气象数据集成"菜单
      list = list.filter(item => item.name !== '水质气象数据集成');

      // 移除"管理信息服务"菜单
      list = list.filter(item => item.name !== '管理信息服务');

      // 处理重复子菜单的函数
      const deduplicateChildren = (menuItem) => {
        if (menuItem && menuItem.children) {
          const seenNames = new Set();
          menuItem.children = menuItem.children.filter(child => {
            // 移除可能存在的只读用户/数据人员后缀，再进行去重判断
            const cleanedChildName = this.removeReadOnly(child.name);
            if (seenNames.has(cleanedChildName)) {
              return false;
            }
            seenNames.add(cleanedChildName);
            return true;
          });
        }
      };

      // 对特定主菜单的子菜单进行去重
      list.forEach(item => {
        const itemName = this.removeReadOnly(item.name);
        if (itemName === '工程巡检' || itemName === '现场检查' || item.name === '工程巡检' || item.name === '现场检查' || 
            item.name === '系统管理' || 
            item.name === '基础工情查询' || item.name === '基础信息查询') {
          deduplicateChildren(item);
        }
      });

      // 过滤"基础信息查询"或"基础工情查询"的子菜单
      const baseInfoMenu = list.find(item => 
        item.name === '基础工情查询' || item.name === '基础信息查询'
      );
      if (baseInfoMenu && baseInfoMenu.children) {
        // 允许显示的菜单项（恢复原始配置）
        const allowedBaseInfoChildrenNames = [
          '监测站点',
          '测项信息',
          '洪水防御预案',
          '预警设施',
          '应急单位和责任人', // 对应"相应单位"
          '库区基本情况'
        ];
        baseInfoMenu.children = baseInfoMenu.children.filter(child =>
          allowedBaseInfoChildrenNames.includes(this.removeReadOnly(child.name))
        );
      }

      // 过滤"系统管理"的子菜单
      const systemManageMenu = list.find(item => item.name === '系统管理');
      if (systemManageMenu && systemManageMenu.children) {
        const allowedSystemManageChildrenNames = [
          '角色管理',
          '机构信息',
          '用户管理',
          '人员信息',
          '部门管理',
          '字典管理'
        ];
        systemManageMenu.children = systemManageMenu.children.filter(child =>
          allowedSystemManageChildrenNames.includes(this.removeReadOnly(child.name))
        );
      }

      return list;
    },
    filteredMenuList() {
      const staticNames = [
        '一张图', '可视化大屏', '水雨情管理', '实时监测', '大坝安全监测', '闸门监控管理', '预警管理', '综合报表'
      ];
      const list = this.processedMenuList.filter(item => !staticNames.includes(item.name));
      // 若后端已返回"基础信息查询"或"基础工情查询"，在其 children 下追加一个"库区基本情况"项（若不存在）
      const baseInfo = list.find(i => 
        this.removeReadOnly(i.name) === '基础工情查询' || 
        this.removeReadOnly(i.name) === '基础信息查询'
      );
      if (baseInfo && Array.isArray(baseInfo.children)) {
        const exists = baseInfo.children.some(c => c.path === '/home/baseinfo/basininfo' || this.removeReadOnly(c.name) === '库区基本情况');
        if (!exists) {
          baseInfo.children.push({ id: 'static-basininfo', name: '库区基本情况', path: '/home/baseinfo/basininfo', children: [] });
        }
      }
      return list;
    },
    breadcrumbList() {
      const matched = this.$route.matched;
      const breadcrumbs = matched.map(route => {
        const path = route.path;
        // 优先用 breadcrumbMap 的中文名
        let label = (route.meta && route.meta.title) || breadcrumbMap[path];
        if (!label && path.includes('/')) {
          label = path.split('/').pop();
          // 强制兜底appdownload为中文
          if (label === 'appdownload') label = 'app下载';
          if (label === '') label = breadcrumbMap[path];
        }
        // 若label为英文，尝试用pathToCn转换
        if (label && /[a-zA-Z_]/.test(label) && this.pathToCn) {
          label = this.pathToCn(path);
        }
        // 再次兜底
        if (label === 'appdownload') label = 'app下载';
        return { path, label: label || '未知' };
      });
      // 过滤无效label
      return breadcrumbs.filter(b => b.label !== '未知' && b.label !== '');
    }
  },
  methods: {
    removeReadOnly(name) {
      let cleanedName = name.replace(/只读用户/g, '');
      cleanedName = cleanedName.replace(/数据人员/g, '');
      return cleanedName;
    },
    pathToCn(path) {
      const map = {
        '/home/waterinfo/waterbaobiao/overview': '水库水情总览',
        '/home/waterinfo/waterbaobiao/waterlevel': '水位过程线图',
        '/home/waterinfo/waterbaobiao/flow': '出入库流量过程线图',
        '/home/waterinfo/waterbaobiao/stationwater': '水位站水位报表',
        '/home/waterinfo/waterbaobiao/stationflow': '流量站流量报表',
        '/home/waterinfo/waterbaobiao/characteristic': '水库特征曲线图',
        '/home/waterinfo/waterbaobiao/capacity': '水位-库容曲线',
        '/home/waterinfo/waterbaobiao/history': '历史比较分析',
        '/home/onemap': '一张图',
        '/home/bigscreen': '可视化大屏',
        '/home/monitor': '视频监控',
        '/home/waterlevel': '水位监测',
        '/home/flow': '流量监测',
        '/home/waterquality': '水质监测',
        '/home/gate_status': '闸门实时状态查询',
        '/home/gatecontrol': '闸门控制',
        '/home/gatereport': '闸门报表',
        '/home/prewarninginformation': '预警信息处理',
        '/home/prewarningindicatorsetting': '预警指标设定',
        '/home/szqx/statistics': '水质监测数据统计',
        '/home/szqx/evaluation': '水质评测和预测分析',
        '/home/szqx/status': '水质监测点工作状态统计',
        '/home/szqx/overlimit': '超标数据时段分析',
        '/home/szqx/trace': '水质异常溯源分析',
        '/home/szqx/history': '历史数据查询',
        '/home/szqx/weather': '查询天气预报',
        '/home/szqx/warning': '气象信息预警',
        '/home/szqx/geowarning': '地质灾害预警信息',
        '/home/dam/seepage': '渗流量查看',
        '/home/dam/seepagewater': '渗流量趋势分析',
        '/home/dam/seepageflowmonitor': '渗流量监测分析',
        '/home/dam/sllbaobiao': '渗流量报表',
        '/home/dam/horizontal': '水平位移查看',
        '/home/dam/vertical': '垂直位移查看',
        '/home/dam/bxbaobiao': '变形报表',
        '/home/dam/surfacedisplacement': '地表位移',
        // 动态菜单常见项
        '/home/engineeringpolling': '工程巡检',
        '/home/systemtools': '系统工具',
        '/home/resource': '资源下载',
        '/home/usermanage': '用户管理',
        '/home/departmentmanage': '部门管理',
        '/home/charactermanage': '角色管理',
        '/home/dictmanage': '字典管理',
        '/home/notice': '通知公告',
        '/home/logmanage': '日志管理',
      };
      return map[path] || path.split('/').pop();
    },
    async getMenuList() {
      try {
        console.log('开始获取菜单数据...');
        const { data: res } = await this.$http.get('/menu/nav')
        console.log('菜单API响应:', res);
        
        if (res.code != 200) {
          console.error('菜单获取失败:', res.message);
          this.$message.error('菜单加载失败，使用备用菜单');
          this.useStaticMenu();
          return;
        }
        
        let menuList = res.data.nav
        console.log('成功获取菜单数据:', menuList);

        // Helper function to ensure path is a string for menu items and their children
        const ensurePathString = (item) => {
          if (item) {
            if (item.path === null || item.path === undefined) {
              item.path = ''; // Default to empty string if null/undefined
            }
            if (item.children && Array.isArray(item.children)) {
              item.children.forEach(child => ensurePathString(child));
            }
          }
        };

        // 处理综合报表三级菜单结构
        const zhbb = menuList.find(item => item.name === '综合报表')
        if (zhbb && zhbb.children) {
          // 防汛值班管理
          const zbgl = zhbb.children.find(item => item.name === '防汛值班管理')
          if (zbgl && zbgl.children) {
            // 确保三级菜单都在 children 下
            zbgl.children = zbgl.children.map(child => {
              if (child.name === '值班安排') {
                child.path = '/home/zhbb/zbgl/zbap'
              } else if (child.name === '值班日志') {
                child.path = '/home/zhbb/zbgl/zbrz'
              }
              ensurePathString(child); // Ensure path is string after assignment
              return child;
            })
          }
          // 信息共享上报
          const gxsb = zhbb.children.find(item => item.name === '信息共享上报')
          if (gxsb && gxsb.children) {
            gxsb.children = gxsb.children.map(child => {
              if (child.name === '预警信息上报') {
                child.path = '/home/zhbb/gxsb/yjsb'
              } else if (child.name === '响应信息上报') {
                child.path = '/home/zhbb/gxsb/xysb'
              } else if (child.name === '灾情信息上报') {
                child.path = '/home/zhbb/gxsb/zqxx'
              }
              ensurePathString(child); // Ensure path is string after assignment
              return child;
            })
          }
        }

        // Apply path string enforcement to all top-level menu items
        menuList.forEach(item => ensurePathString(item));

        this.menulist = menuList
      } catch (error) {
        console.error('菜单API调用失败:', error);
        this.$message.error('菜单加载失败，使用备用菜单');
        this.useStaticMenu();
      }
    },
    
    // 使用静态菜单作为备用方案
    useStaticMenu() {
      console.log('使用静态备用菜单');
      this.menulist = [
        {
          name: '一张图',
          path: '/home/onemap',
          children: []
        },
        {
          name: '水雨情管理',
          path: '/home/waterinfo',
          children: [
            { name: '降雨查询', path: '/home/waterinfo/station' },
            { name: '水库水位查询', path: '/home/waterinfo/waterstorage' }
          ]
        },
        {
          name: '视频监测',
          path: '/home/monitor',
          children: []
        },
        {
          name: '大坝安全监测',
          path: '/home/dam',
          children: [
            { name: '渗流压力监测分析', path: '/home/dam/seepage' },
            { name: '渗流量监测分析', path: '/home/dam/seepageflowmonitor' },
            { name: '地表位移', path: '/home/dam/surfacedisplacement' }
          ]
        },
        {
          name: '闸门监控管理',
          path: '/home/gate',
          children: [
            { name: '闸门实时状态查询', path: '/home/gate_status' },
            { name: '闸门报表', path: '/home/gatereport' }
          ]
        },
        {
          name: '预警管理',
          path: '/home/yjgl',
          children: [
            { name: '预警信息处理', path: '/home/prewarninginformation' },
            { name: '预警指标设定', path: '/home/prewarningindicatorsetting' }
          ]
        },
        {
          name: '综合报表',
          path: '/home/zhbb',
          children: [
            { name: '值班安排', path: '/home/zhbb/zbgl/zbap' },
            { name: '值班日志', path: '/home/zhbb/zbgl/zbrz' }
          ]
        }
      ];
    },
    async getPersonInformation() {
      const { data: res } = await this.$http.get('/user/userInfo')
      if (res.code != 200) return this.$message.error('获取个人信息错误')
      this.UserList = res.data
    },
    changePasswordDialogClosed() {
      this.$refs.passwordFormRef.resetFields()
    },
    saveNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath)
      this.activePath = activePath
    },
    submitPasswordForm() {
      if (this.passwordForm.password != this.passwordForm.confirmPassword) {
        this.$message.error('两次密码输入不一致')
        return
      }
      this.$refs.passwordFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/user/updatePass',
          this.passwordForm
        )
        if (res.code == 200) {
          this.$message.success('修改密码成功')
          sessionStorage.removeItem('token')
          sessionStorage.removeItem('activePath')
          this.$router.push('/login')
          this.changePasswordDialogVisible = false
        }
        else {
          this.$message.error('原密码不对')
        }
      })
    },
    logout() {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('activePath')
      this.$router.push('/login')
    },
    checkWindowWidth() {
      this.windowwid = window.innerWidth > 1350;
    },
    goWaterInfo() {
      this.activePath = '/home/waterinfo'
      window.sessionStorage.setItem('activePath', '/home/waterinfo')
      this.$router.push('/home/waterinfo')
    },
    goDamSafety() {
      this.activePath = '/home/dam'
      window.sessionStorage.setItem('activePath', '/home/dam')
      this.$router.push('/home/dam')
    },
    menuIconClass(name) {
      // 主菜单唯一图标
      switch (name) {
        case '水质气象数据集成': return 'el-icon-cloudy';
        case '预警管理': return 'el-icon-warning';
        case '基础工情查询':
        case '基础信息查询': return 'el-icon-collection';
        case '工程巡检': return 'el-icon-suitcase';
        case '资源下载': return 'el-icon-download';
        case '系统管理': return 'el-icon-setting';
        case '系统工具': return 'el-icon-tool';
        default: return 'el-icon-folder';
      }
    },
  },
  created() {
    this.getMenuList();
    (this.activePath = window.sessionStorage.getItem('activePath'))
    this.getPersonInformation()
    this.$nextTick(() => {
      this.menulist.forEach(item => {
        if(item.name && item.name.includes('水雨情管理')) {
          item.children.forEach(sub => {
            if(sub.name && (sub.name.includes('雨量极值') || sub.name.includes('雨量极值信息'))) {
              sub.path = '/home/rainfallextremum'
            }
          })
        }
      })
    })
  },
  mounted() {
    this.checkWindowWidth()
    window.addEventListener('resize', this.checkWindowWidth);
  },
  components: {}
}
</script>

<style lang="less" scoped>
.home-container {
  width: 100vw;
  height: 100vh;
  background: #f5f6fa;
  overflow: hidden;
}
.home-main-layout {
  display: flex;
  height: 100vh;
  background: #f5f6fa;
  position: relative;
}
.sidebar {
  width: 220px;
  min-width: 0;
  background: #263445;
  color: #fff;
  min-height: 100vh;
  box-shadow: 2px 0 8px #e4e7ed22;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  transition: width 0.3s;
  z-index: 10;
  position: relative;
}
.sidebar.collapsed {
  width: 0;
  overflow: hidden;
}
.logo-box {
  display: flex;
  align-items: center;
  height: 60px;
  padding: 10px 0 10px 20px;
  background: #263445;
  margin-bottom: 0;
}
.logo-img {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  box-shadow: 0 2px 8px #2222;
}
.logo-title {
  margin-left: 10px;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 2px;
  white-space: nowrap;
  transition: opacity 0.2s;
}
.sidebar.collapsed .logo-title {
  opacity: 0;
  width: 0;
}
.collapse-btn {
  display: none;
}
.sidebar-collapse-float {
  position: absolute;
  top: 50%;
  right: -16px;
  transform: translateY(-50%);
  width: 32px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to left, #263445 60%, #263445cc 100%, transparent 100%);
  border-radius: 0 16px 16px 0;
  box-shadow: 0 2px 8px #2222;
  cursor: pointer;
  z-index: 20;
  transition: right 0.2s;
}
.sidebar.collapsed .sidebar-collapse-float {
  right: -16px;
}
.sidebar-collapse-float i {
  color: #fff;
  font-size: 22px;
  transition: transform 0.2s;
}
.sidebar-scroll {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}
.el-menu-vertical-demo {
  border-right: none;
  background: #263445;
  flex: 1;
}
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 100vh;
  background: #f5f6fa;
}
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  height: 56px;
  padding: 0 24px;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 8px #e4e7ed11;
  z-index: 5;
}
.breadcrumb-bar {
  font-size: 15px;
  color: #333;
  background: transparent;
  padding: 0;
}
.userbar {
  display: flex;
  align-items: center;
  font-size: 15px;
  color: #333;
}
.content-area {
  flex: 1;
  padding: 16px 16px 0 16px;
  background: #f5f6fa;
  overflow: auto;
}
@media (max-width: 900px) {
  .sidebar {
    width: 60px !important;
  }
  .main-content {
    padding-left: 0 !important;
  }
}
/deep/ .el-menu,
/deep/ .el-submenu__title,
/deep/ .el-menu-item {
  background: #263445 !important;
  color: #fff !important;
}
/deep/ .el-menu-item.is-active,
/deep/ .el-menu-item:hover,
/deep/ .el-submenu__title:hover {
  background: #1a2233 !important;
  color: #ffd04b !important;
}
.sidebar-expand-float {
  position: fixed;
  top: 60px;
  left: 0;
  width: 32px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to right, #263445 60%, #263445cc 100%, transparent 100%);
  border-radius: 0 16px 16px 0;
  box-shadow: 0 2px 8px #2222;
  cursor: pointer;
  z-index: 100;
}
.sidebar-expand-float i {
  color: #fff;
  font-size: 22px;
}
</style>
