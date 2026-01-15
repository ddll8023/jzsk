import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login.vue'
import Main from '../views/Main.vue'
import UserList from '../views/user/List.vue'
import UserProfile from '../views/user/Profile.vue'
import Onemap from '@/components/menu/OneMap.vue'
import NotFound from '../views/NotFound.vue'
import Home from '@/components/Home.vue'
import ProjectIntro from '@/components/menu/ProjectIntro.vue'
import Onemaps from '@/components/menu/OneMaps.vue'
import Monitor from '@/components/menu/Monitor.vue'
import ApiTest from '@/views/ApiTest.vue'
import PrewarningIndicatorSetting from '@/components/menu/PrewarningManage/PrewarningIndicatorSetting.vue'
import PrewarningIndicatorSettingRead from '@/components/menu/PrewarningManage/PrewarningIndicatorSettingRead.vue'
import PrewarningInformationRead from '@/components/menu/PrewarningManage/PrewarningInformationRead.vue'
import PrewarningInformation from '@/components/menu/PrewarningManage/PrewarningInformation.vue'
import EventStudy from '@/components/menu/EngineeringPolling/EventStudy.vue'
import EventStudyRead from '@/components/menu/EngineeringPolling/EventStudyRead.vue'
import PollingRecord from '@/components/menu/EngineeringPolling/PollingRecord.vue'
import PollingRecordRead from '@/components/menu/EngineeringPolling/PollingRecordRead.vue'
import MaintenceRecord from '@/components/menu/EngineeringPolling/MaintenceRecord.vue'
import MaintenceRecordRead from '@/components/menu/EngineeringPolling/MaintenceRecordRead.vue'
import ManageOrganization from '@/components/menu/ManageInformation/ManageOrganization.vue'
import ManageOrganizationRead from '@/components/menu/ManageInformation/ManageOrganizationRead.vue'
import ManagePerson from '@/components/menu/ManageInformation/ManagePerson.vue'
import ManagePersonRead from '@/components/menu/ManageInformation/ManagePersonRead.vue'
import MonitorSite from '@/components/menu/EngineeringInformation/MonitorSite.vue'
import MonitorSiteRead from '@/components/menu/EngineeringInformation/MonitorSiteRead.vue'
import MonitorItem from '@/components/menu/EngineeringInformation/MonitorItem.vue'
import MonitorItemRead from '@/components/menu/EngineeringInformation/MonitorItemRead.vue'
import GroundSourceWater from '@/components/menu/EngineeringInformation/EngineeringSites/GroundSourceWater.vue'
import GroundSourceWaterRead from '@/components/menu/EngineeringInformation/EngineeringSites/GroundSourceWaterRead.vue'
import Impoundment from '@/components/menu/EngineeringInformation/EngineeringSites/Impoundment.vue'
import ImpoundmentRead from '@/components/menu/EngineeringInformation/EngineeringSites/ImpoundmentRead.vue'
import IndividualFlowSites from '@/components/menu/EngineeringInformation/EngineeringSites/IndividualFlowSites.vue'
import IndividualFlowSitesRead from '@/components/menu/EngineeringInformation/EngineeringSites/IndividualFlowSitesRead.vue'
import IndividualPressureSites from '@/components/menu/EngineeringInformation/EngineeringSites/IndividualPressureSites.vue'
import IndividualPressureSitesRead from '@/components/menu/EngineeringInformation/EngineeringSites/IndividualPressureSitesRead.vue'
import PumpStation from '@/components/menu/EngineeringInformation/EngineeringSites/PumpStation.vue'
import PumpStationRead from '@/components/menu/EngineeringInformation/EngineeringSites/PumpStationRead.vue'
import Reservoir from '@/components/menu/EngineeringInformation/EngineeringSites/Reservoir.vue'
import ReservoirRead from '@/components/menu/EngineeringInformation/EngineeringSites/ReservoirRead.vue'
import SurfaceWaterSources from '@/components/menu/EngineeringInformation/EngineeringSites/SurfaceWaterSources.vue'
import SurfaceWaterSourcesRead from '@/components/menu/EngineeringInformation/EngineeringSites/SurfaceWaterSourcesRead.vue'
import WaterDistributor from '@/components/menu/EngineeringInformation/EngineeringSites/WaterDistributor.vue'
import WaterDistributorRead from '@/components/menu/EngineeringInformation/EngineeringSites/WaterDistributorRead.vue'
import WaterWorks from '@/components/menu/EngineeringInformation/EngineeringSites/WaterWorks.vue'
import WaterWorksRead from '@/components/menu/EngineeringInformation/EngineeringSites/WaterWorksRead.vue'
import Lines from '@/components/menu/EngineeringInformation/EngineeringSites/Lines.vue'
import LinesRead from '@/components/menu/EngineeringInformation/EngineeringSites/LinesRead.vue'
import Herb from '@/components/menu/EngineeringInformation/EngineeringSites/Herb.vue'
import HerbRead from '@/components/menu/EngineeringInformation/EngineeringSites/HerbRead.vue'
import FloatingBoat from '@/components/menu/EngineeringInformation/EngineeringSites/FloatingBoat.vue'
import FloatingBoatRead from '@/components/menu/EngineeringInformation/EngineeringSites/FloatingBoatRead.vue'
import Town from '@/components/menu/EngineeringInformation/EngineeringSites/Town.vue'
import TownRead from '@/components/menu/EngineeringInformation/EngineeringSites/TownRead.vue'
import WaterSupplyEngineering from '@/components/menu/EngineeringInformation/WaterSupplyEngineering.vue'
import WaterSupplyEngineeringRead from '@/components/menu/EngineeringInformation/WaterSupplyEngineeringRead.vue'
import AdministrativeDivision from '@/components/menu/SystemServe/AdministrativeDivision.vue'
import AdministrativeDivisionRead from '@/components/menu/SystemServe/AdministrativeDivisionRead.vue'
import CharacterManage from '@/components/menu/SystemServe/CharacterManage.vue'
import UserManage from '@/components/menu/SystemServe/UserManage.vue'
import UserManageData from '@/components/menu/SystemServe/UserManageData.vue'
import DepartmentManage from '@/components/menu/SystemServe/DepartmentManage.vue'
import DepartmentManageRead from '@/components/menu/SystemServe/DepartmentManageread.vue'
import DictManage from '@/components/menu/SystemServe/DictManage.vue'
import Videocheck from '@/components/menu/VideoSurveilance/VideoCheck.vue'
import VideocheckRead from '@/components/menu/VideoSurveilance/VideoCheckRead.vue'
import Videoconfiguration from '@/components/menu/VideoSurveilance/VideoConfiguration.vue'
import VideoconfigurationRead from '@/components/menu/VideoSurveilance/VideoConfigurationRead.vue'
import ProVideoCheck from '@/components/menu/VideoSurveilance/ProVideoCheck.vue'
import ProVideoCheckWater from '@/components/menu/VideoSurveilance/ProVideoCheckWater.vue'
import WaterLevel from '@/components/menu/VideoSurveilance/WaterLevel.vue'
import WaterLevelRead from '@/components/menu/VideoSurveilance/WaterLevelRead.vue'
import Flow from '@/components/menu/VideoSurveilance/Flow.vue'
import FlowRead from '@/components/menu/VideoSurveilance/FlowRead.vue'
import AppDownload from '@/components/menu/ResourceDownload/AppDownload.vue'
import ShouyeWeihu from '@/components/menu/SystemServe/ShouyeWeihu.vue'
import Station from '@/components/menu/waterinformation/station.vue'
import StpExtremumb from '@/components/menu/waterinformation/stpextremumb.vue'
import GateStatus from '@/components/menu/GateSurveilance/GateStatus.vue'
import GateControl from '@/components/menu/GateSurveilance/GateControl.vue'
import GateReport from '@/components/menu/GateSurveilance/GateReport.vue'
import BigScreen from '@/components/menu/BigScreen.vue'
import Jcsj from '@/views/dam/jcsj.vue'

Vue.use(Router)

//初始化路由
const router = new Router({
  mode: 'history',
  routes: [
    { path: '/login', name: 'login', component: Login },
    { path: '/api-test', name: 'api-test', component: ApiTest },
    {
      path: '/home',
      component: Home,
      redirect: '/home/onemap',
      children: [
        { path: 'onemap', component: Onemaps },
        { path: 'monitor', component: ProVideoCheck }, //子路由不带/表示http://xxxxxx/home/monitor
        { path: 'videocheck', component: Videocheck },
        { path: 'videocheckread', component: VideocheckRead },
        { path: 'videoconfiguration', component: Videoconfiguration },
        { path: 'videoconfigurationread', component: VideoconfigurationRead },
        { path: 'provideocheck', component: ProVideoCheck },
        { path: 'provideocheckwater', component: ProVideoCheckWater },
        { path: 'waterlevel', component: WaterLevel },
        { path: 'waterlevelread', component: WaterLevelRead },
        { path: 'flow', component: Flow },
        { path: 'flowread', component: FlowRead },
        { path: 'prewarningindicatorsetting', component: PrewarningIndicatorSetting },
        { path: 'prewarningindicatorsettingread', component: PrewarningIndicatorSettingRead },
        { path: 'prewarninginformation', component: PrewarningInformation },
        { path: 'prewarninginformationread', component: PrewarningInformationRead },
        { path: 'eventstudy', component: EventStudy },
        { path: 'eventstudyread', component: EventStudyRead },
        { path: 'pollingrecord', component: PollingRecord },
        { path: 'pollingrecordread', component: PollingRecordRead },
        { path: 'maintencerecord', component: MaintenceRecord },
        { path: 'maintencerecordread', component: MaintenceRecordRead },
        { path: 'manageorganization', component: ManageOrganization },
        { path: 'manageorganizationread', component: ManageOrganizationRead },
        { path: 'manageperson', component: ManagePerson },
        { path: 'managepersonread', component: ManagePersonRead },
        { path: 'watersupplyengineering', component: WaterSupplyEngineering },
        { path: 'watersupplyengineeringread', component: WaterSupplyEngineeringRead },
        { path: 'monitorsite', component: MonitorSite, meta: { title: '监测站点' } },
        { path: 'monitorsiteread', component: MonitorSiteRead, meta: { title: '监测站点' } },
        { path: 'monitoritem', component: MonitorItem, meta: { title: '测项信息' } },
        { path: 'monitoritemread', component: MonitorItemRead, meta: { title: '测项信息' } },
        { path: 'groundsourcewater', component: GroundSourceWater },
        { path: 'groundsourcewaterread', component: GroundSourceWaterRead },
        { path: 'impoundment', component: Impoundment },
        { path: 'impoundmentread', component: ImpoundmentRead },
        { path: 'individualflowsites', component: IndividualFlowSites },
        { path: 'individualflowsitesread', component: IndividualFlowSitesRead },
        { path: 'individualpressuresites', component: IndividualPressureSites },
        { path: 'individualpressuresitesread', component: IndividualPressureSitesRead },
        { path: 'pumpstation', component: PumpStation },
        { path: 'pumpstationread', component: PumpStationRead },
        { path: 'reservoir', component: Reservoir, meta: { title: '水库基本情况' } },
        { path: 'reservoirread', component: ReservoirRead, meta: { title: '水库基本情况' } },
        { path: 'surfacewatersources', component: SurfaceWaterSources },
        { path: 'surfacewatersourcesread', component: SurfaceWaterSourcesRead },
        { path: 'waterdistributor', component: WaterDistributor },
        { path: 'waterdistributorread', component: WaterDistributorRead },
        { path: 'waterworks', component: WaterWorks },
        { path: 'waterworksread', component: WaterWorksRead },
        { path: 'lines', component: Lines },
        { path: 'linesread', component: LinesRead },
        { path: 'herb', component: Herb },
        { path: 'herbread', component: HerbRead },
        { path: 'floatingboat', component: FloatingBoat },
        { path: 'floatingboatread', component: FloatingBoatRead },
        { path: 'town', component: Town },
        { path: 'townread', component: TownRead },
        { path: 'administrativedivision', component: AdministrativeDivision },
        { path: 'administrativedivisionread', component: AdministrativeDivisionRead },
        { path: 'dictmanage', component: DictManage },
        { path: 'charactermanage', component: CharacterManage },
        { path: 'usermanage', component: UserManage },
        { path: 'usermanagedata', component: UserManageData },
        { path: 'departmentmanage', component: DepartmentManage },
        { path: 'departmentmanageread', component: DepartmentManageRead },
        { path: 'appdownload', component: AppDownload, meta: { title: 'app下载' } },
        { path: 'shouyeweihu', component: ShouyeWeihu },
        {
          path: 'waterinfo',
          component: { render: h => h('router-view') },
          children: [
            { path: 'waterstorage', component: () => import('@/components/menu/waterinformation/waterstorage.vue') },
            { path: 'station', component: () => import('@/components/menu/waterinformation/station.vue') },
            { path: 'stpextremumb', component: StpExtremumb },
            { path: 'dailyrainfall', component: () => import('@/components/menu/waterinformation/dailyrainfall.vue') },
            { path: 'rainbaobiao', component: () => import('@/components/menu/waterinformation/rainbaobiao.vue') },
            { path: 'riverstation', component: () => import('@/components/menu/waterinformation/riverstation.vue') },
            { path: 'annualwaterstation', component: () => import('@/components/menu/waterinformation/annualwaterstation.vue') },
            { path: 'waterreport', component: () => import('@/components/menu/waterinformation/waterreport.vue') },
          ]
        },
        { path: 'dam/seepage', component: () => import(/* webpackChunkName: "dam-seepage" */ '@/views/dam/Seepage.vue') },
        { path: 'dam/seepagewater', component: () => import(/* webpackChunkName: "dam-seepagewater" */ '@/views/dam/Seepagewater.vue') },
        { path: 'dam/seepageflowmonitor', component: () => import(/* webpackChunkName: "dam-seepageflowmonitor" */ '@/views/dam/SeepageFlowMonitor.vue'), meta: { title: '渗流量监测分析' } },
        { path: 'dam/sllbaobiao', component: () => import(/* webpackChunkName: "dam-sllbaobiao" */ '@/views/dam/Sllbaobiao.vue') },
        { path: 'dam/horizontal', component: () => import(/* webpackChunkName: "dam-horizontal" */ '@/views/dam/Horizontal.vue') },
        { path: 'dam/vertical', component: () => import(/* webpackChunkName: "dam-vertical" */ '@/views/dam/Vertical.vue') },
        { path: 'dam/bxbaobiao', component: () => import(/* webpackChunkName: "dam-bxbaobiao" */ '@/views/dam/Bxbaobiao.vue') },
        { path: 'dam/surfacedisplacement', component: Jcsj, meta: { title: '地表位移' } },
        { path: 'gate_status', component: () => import('@/components/menu/GateSurveilance/GateStatus.vue') },
        { path: 'gatecontrol', component: () => import('@/components/menu/GateSurveilance/GateControl.vue') },
        { path: 'gatereport', component: () => import('@/components/menu/GateSurveilance/GateReport.vue') },
        { path: 'baseinfo/floodimpact', component: () => import('@/components/menu/basic/FloodImpact.vue') },
        { path: 'baseinfo/floodhistory', component: () => import('@/components/menu/basic/FloodHistory.vue') },
        { path: 'baseinfo/basininfo', component: () => import('@/components/menu/basic/BasinInfo.vue') },
        { path: 'baseinfo/floodplan', component: () => import('@/components/menu/basic/FloodPlan.vue') },
        { path: 'baseinfo/warningindex', component: () => import('@/components/menu/basic/WarningIndex.vue') },
        { path: 'baseinfo/warningfacility', component: () => import('@/components/menu/basic/WarningFacility.vue') },
        { path: 'baseinfo/dangerzone', component: () => import('@/components/menu/basic/DangerZone.vue') },
        { path: 'baseinfo/transferroute', component: () => import('@/components/menu/basic/TransferRoute.vue') },
        { path: 'baseinfo/responsibility', component: () => import('@/components/menu/basic/Responsibility.vue') },
        { path: 'bigscreen', component: BigScreen },
        { path: 'system/organization', component: () => import('@/components/menu/ManageInformation/ManageOrganization.vue') },
        { path: 'system/person', component: () => import('@/components/menu/ManageInformation/ManagePerson.vue') },
        {
          path: 'yjgl',
          component: { render: h => h('router-view') },
          meta: { title: '预警管理' },
        },
        {
          path: '/home/zhbb',
          component: { render: h => h('router-view') },
          meta: { title: '综合报表' },
          redirect: '/home/zhbb/zbgl',
          children: [
            {
              path: 'zbgl',
              component: { render: h => h('router-view') },
              meta: { title: '防汛值班管理' },
              redirect: '/home/zhbb/zbgl/zbap',
              children: [
                {
                  path: 'zbap',
                  component: () => import('@/components/ComprehensiveReport/DutySchedule.vue'),
                  meta: { title: '值班安排' }
                },
                {
                  path: 'zbrz',
                  component: () => import('@/components/ComprehensiveReport/DutyLog.vue'),
                  meta: { title: '值班日志' }
                },
                {
                  path: 'fxsb',
                  component: () => import('@/components/ComprehensiveReport/Equipment.vue'),
                  meta: { title: '防汛设备管理' }
                }
              ]
            },
            {
              path: 'gxsb',
              component: { render: h => h('router-view') },
              meta: { title: '信息共享上报' },
              redirect: '/home/zhbb/gxsb/yjsb',
              children: [
                {
                  path: 'yjsb',
                  component: () => import('@/components/ComprehensiveReport/InfoReportYJ.vue'),
                  meta: { title: '预警信息上报' }
                },
                {
                  path: 'xysb',
                  component: () => import('@/components/ComprehensiveReport/InfoReportXY.vue'),
                  meta: { title: '响应信息上报' }
                },
                {
                  path: 'zqxx',
                  component: () => import('@/components/ComprehensiveReport/InfoReportZQ.vue'),
                  meta: { title: '灾情信息上报' }
                }
              ]
            }
          ]
        },
        { path: '*', component: NotFound }
      ]
    },
    {
      path: '/waterinformation/riverstation',
      name: 'RiverStation',
      component: () => import('@/components/menu/waterinformation/riverstation.vue')
    },
    {
      path: '/waterstorage',
      redirect: '/home/waterinfo/waterstorage'
    },
    { path: 'baseinfo/floodimpact', component: () => import('@/components/menu/basic/FloodImpact.vue') },
    { path: 'baseinfo/floodhistory', component: () => import('@/components/menu/basic/FloodHistory.vue') },
    { path: 'baseinfo/basininfo', component: () => import('@/components/menu/basic/BasinInfo.vue') },
    { path: 'baseinfo/monitorstation', component: MonitorSiteRead },
    { path: 'baseinfo/cexiang', component: MonitorItemRead },
    { path: 'baseinfo/reservoir', component: ReservoirRead },
    { path: 'baseinfo/floodplan', component: () => import('@/components/menu/basic/FloodPlan.vue') },
    { path: 'baseinfo/warningindex', component: () => import('@/components/menu/basic/WarningIndex.vue') },
    { path: 'baseinfo/warningfacility', component: () => import('@/components/menu/basic/WarningFacility.vue') },
    { path: 'baseinfo/dangerzone', component: () => import('@/components/menu/basic/DangerZone.vue') },
    { path: 'baseinfo/transferroute', component: () => import('@/components/menu/basic/TransferRoute.vue') },
    { path: 'baseinfo/floodplan', component: NotFound },
    { path: 'baseinfo/warningindex', component: NotFound },
    { path: 'baseinfo/warningfacility', component: NotFound },
    { path: 'baseinfo/dangerzone', component: NotFound },
    { path: 'baseinfo/transferroute', component: NotFound },
    { path: 'baseinfo/responsibility', component: NotFound },
  ]
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  if (to.path === '/login' || to.path === '/api-test') return next()
  //获取token
  const tokenstr = window.sessionStorage.getItem('token')
  if (!tokenstr) return next('/login')
  next()
})
export default router
