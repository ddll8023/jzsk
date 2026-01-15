<template>
  <div id="map-container">
    <!-- 加载状态指示 -->
    <div v-if="isLoading" class="loading-overlay">
      <div class="loading-spinner">
        <i class="el-icon-loading"></i>
        <p>正在加载地图数据...</p>
      </div>
    </div>
    <div id="map" class="map"></div>
    <!-- 工程简介按钮 -->
    <div class="project-info" @click="toggleProjectInfo">
      <i class="el-icon-document"></i>
      <span class="project-info-text">工程简介</span>
    </div>
    <!-- 顶部预警信息按钮 -->
    <div class="warning-info-btn">
      <el-button :type="warningLevel" icon="el-icon-warning" @click="showWarningDialog = true">预警信息</el-button>
      </div>
    <!-- 图例分组 -->
    <div class="map-legend small-legend legend-top-left">
      <div class="legend-title">工程站点</div>
      <div class="legend-item">
        <div class="legend-marker">
          <i class="el-icon-location" style="color:#1890ff;font-size:22px;"></i>
        </div>
        <div class="legend-text">GNSS测站/基准点</div>
        <input type="checkbox" v-model="showGnss" style="margin-left:8px;vertical-align:middle;" />
      </div>
      <div class="legend-item">
        <div class="legend-marker">
          <img :src="legend.project[1].icon" :alt="legend.project[1].name" style="width: 18px; height: 26px;">
        </div>
        <div class="legend-text">坝前雨量水位站</div>
        <input type="checkbox" v-model="showRain" style="margin-left:8px;vertical-align:middle;" />
      </div>
      <div class="legend-item">
        <div class="legend-marker">
          <img :src="legend.project[2].icon" :alt="legend.project[2].name" style="width: 18px; height: 26px;">
        </div>
        <div class="legend-text">mcu测站</div>
        <input type="checkbox" v-model="showMcu" style="margin-left:8px;vertical-align:middle;" />
      </div>
      <div class="legend-title" style="margin-top: 12px;">预警站点</div>
      <div v-for="item in legend.warning" :key="item.name" class="legend-item">
        <div class="legend-marker">
          <img :src="item.icon" :alt="item.name" style="width: 18px; height: 26px;">
        </div>
        <div class="legend-text">{{ item.name }}</div>
      </div>
        </div>
    <!-- 图层切换控件（右上角） -->
    <div class="layer-switch legend-top-right">
      <label><input type="checkbox" v-model="showTdtImg" @change="toggleTdtImg"> 卫星影像</label>
      <label><input type="checkbox" v-model="showTdtAnno" @change="toggleTdtAnno"> 注记</label>
      </div>
    <!-- 动态经纬度显示 -->
    <div class="map-coord" v-if="mouseLngLat">
      经度：{{ mouseLngLat.lng.toFixed(6) }}° 纬度：{{ mouseLngLat.lat.toFixed(6) }}°
    </div>
    <!-- 预警信息弹窗 -->
    <el-dialog :visible.sync="showWarningDialog" title="预警信息" width="90vw" top="5vh" :close-on-click-modal="false">
      <PrewarningInformation ref="warningTable" :onlyUnprocessed="true" @warning-changed="onWarningChanged" />
    </el-dialog>
    
    <!-- 渗流量预览弹窗（点击 UP* 测站） -->
    <el-dialog :visible.sync="showUpPreviewDialog" :title="`${selectedUpPreview.name}（${selectedUpPreview.piezometerId}）渗流量预览`" width="70vw" top="8vh" :close-on-click-modal="false">
      <div ref="upPreviewChart" style="width: 100%; height: 420px;"></div>
    </el-dialog>
    
    <!-- 测站详情弹窗 -->
    <el-dialog :visible.sync="showStationDetailDialog" :title="selectedStation.name" width="600px" :close-on-click-modal="false">
      <div class="station-detail-content">
        <div class="station-info">
          <div class="info-row">
            <span class="label">测站名称：</span>
            <span class="value">{{ selectedStation.name }}</span>
          </div>
          <div class="info-row">
            <span class="label">测站类型：</span>
            <span class="value">{{ getStationTypeName(selectedStation.type) }}</span>
          </div>
          <div class="info-row">
            <span class="label">坐标位置：</span>
            <span class="value">{{ selectedStation.position ? `${selectedStation.position[0].toFixed(6)}, ${selectedStation.position[1].toFixed(6)}` : '未知' }}</span>
          </div>
        </div>
        
        <!-- GNSS测站数据 -->
        <div v-if="selectedStation.type === 'gnss' && selectedStation.latest" class="data-section">
          <h4>GNSS监测数据</h4>
          <div class="data-grid">
            <div class="data-item">
              <span class="data-label">Z位移：</span>
              <span class="data-value" :class="getDataStatusClass(selectedStation.latest.Z, -10, 10)">
                {{ selectedStation.latest.Z }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">合位移：</span>
              <span class="data-value" :class="getDataStatusClass(selectedStation.latest.H, -15, 15)">
                {{ selectedStation.latest.H }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">X位移：</span>
              <span class="data-value" :class="getDataStatusClass(selectedStation.latest.X, -8, 8)">
                {{ selectedStation.latest.X }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">Y位移：</span>
              <span class="data-value" :class="getDataStatusClass(selectedStation.latest.Y, -8, 8)">
                {{ selectedStation.latest.Y }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">监测时间：</span>
              <span class="data-value">{{ selectedStation.latest.time }}</span>
            </div>
          </div>
        </div>
        
        <!-- 雨量水位站数据 -->
        <div v-if="selectedStation.type === 'rain' && selectedStation.latest" class="data-section">
          <h4>雨量水位监测数据</h4>
          <div class="data-grid">
            <div class="data-item">
              <span class="data-label">当前水位：</span>
              <span class="data-value" :class="getWaterLevelClass(selectedStation.latest.water)">
                {{ selectedStation.latest.water }} m
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">降雨量：</span>
              <span class="data-value" :class="getRainfallClass(selectedStation.latest.rain)">
                {{ selectedStation.latest.rain }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">监测时间：</span>
              <span class="data-value">{{ selectedStation.latest.time }}</span>
            </div>
          </div>
        </div>
        
        <!-- MCU测站数据 -->
        <div v-if="selectedStation.type === 'mcu' && mcuLatestDataMap[selectedStation.name]" class="data-section">
          <h4>MCU监测数据</h4>
          <div class="data-grid">
            <div class="data-item">
              <span class="data-label">水位高程：</span>
              <span class="data-value" :class="getWaterLevelClass(mcuLatestDataMap[selectedStation.name].value)">
                {{ mcuLatestDataMap[selectedStation.name].value.toFixed(2) }} m
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">设备状态：</span>
              <span class="data-value status-online">在线</span>
            </div>
            <div class="data-item">
              <span class="data-label">检测时间：</span>
              <span class="data-value">{{ mcuLatestDataMap[selectedStation.name].time }}</span>
            </div>
          </div>
        </div>
        
        <!-- UPB测站渗流量数据 -->
        <div v-if="selectedStation.type === 'upb' && upbDataMap[selectedStation.name]" class="data-section">
          <h4>渗流量监测数据</h4>
          <div class="data-grid">
            <div class="data-item">
              <span class="data-label">渗压计编号：</span>
              <span class="data-value">{{ selectedStation.piezometerId }}</span>
            </div>
            <div class="data-item">
              <span class="data-label">渗压监测点：</span>
              <span class="data-value">{{ selectedStation.name }}</span>
            </div>
            <div class="data-item">
              <span class="data-label">水位高程：</span>
              <span class="data-value" :class="getWaterLevelClass(upbDataMap[selectedStation.name].waterLevelElevation)">
                {{ upbDataMap[selectedStation.name].waterLevelElevation.toFixed(2) }} m
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">水位：</span>
              <span class="data-value" :class="getWaterLevelClass(upbDataMap[selectedStation.name].waterLevel)">
                {{ upbDataMap[selectedStation.name].waterLevel.toFixed(2) }} mm
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">温度：</span>
              <span class="data-value" :class="getTemperatureClass(upbDataMap[selectedStation.name].temperature)">
                {{ upbDataMap[selectedStation.name].temperature.toFixed(2) }} °C
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">水压：</span>
              <span class="data-value" :class="getPressureClass(upbDataMap[selectedStation.name].pressure)">
                {{ upbDataMap[selectedStation.name].pressure.toFixed(2) }} MPa
              </span>
            </div>
            <div class="data-item">
              <span class="data-label">检测时间：</span>
              <span class="data-value">{{ formatDisplayTime(upbDataMap[selectedStation.name].time) }}</span>
            </div>
          </div>
        </div>
        
        <!-- 基准点信息 -->
        <div v-if="selectedStation.type === 'benchmark'" class="data-section">
          <h4>基准点信息</h4>
          <div class="data-grid">
            <div class="data-item">
              <span class="data-label">基准点类型：</span>
              <span class="data-value">GNSS基准点</span>
            </div>
            <div class="data-item">
              <span class="data-label">用途：</span>
              <span class="data-value">为GNSS测站提供坐标参考</span>
            </div>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="showStationDetailDialog = false">关闭</el-button>
        <el-button type="primary" @click="refreshStationData">刷新数据</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader';
import axios from 'axios';
// 本地icon
// const blueIcon = require('@/icon/mark_b.png'); // 蓝色水滴
import flowIcon from '@/icon/流量站点.png';
import mcuIcon from '@/icon/水厂.png';
import warnNormalPng from '@/icon/一般预警.png';
import warnSeriousPng from '@/icon/严重预警.png';
// 只引入表格部分的简化版预警信息表
import PrewarningInformation from './PrewarningManage/PrewarningInformation.vue';

export default {
  name: 'SimpleMap',
  components: { PrewarningInformation },
  data() {
    return {
      map: null,
      mouseLngLat: null,
      tdtImg: null,
      tdtAnno: null,
      showTdtImg: true,
      showTdtAnno: true,
      showWarningDialog: false,
      isLoading: true, // 添加加载状态
      // 1. GNSS测站和基准点（mock最新数据）
      gnssStations: [
        // 坐标来源：KML (LT1-*, LT2-*)，名称与现有一致（LJ1-* 对应 LT1-*）
        { name: 'LJ1-1', stationId: 33210, position: [115.692970645, 30.12990538], type: 'gnss', latest: {} },
        { name: 'LJ1-2', stationId: 33214, position: [115.692507964, 30.129352846], type: 'gnss', latest: {} },
        { name: 'LJ1-3', stationId: 33216, position: [115.691869598, 30.128584393], type: 'gnss', latest: {} },
        { name: 'LJ1-4', stationId: 33212, position: [115.691246, 30.12784], type: 'gnss', latest: {} },
        { name: 'LT2-1', stationId: 33215, position: [115.69324205, 30.129704684], type: 'gnss', latest: {} },
        { name: 'LT2-2', stationId: 33211, position: [115.692781, 30.129157], type: 'gnss', latest: {} },
        { name: 'LT2-3', stationId: 33217, position: [115.692131615, 30.12838906], type: 'gnss', latest: {} },
        { name: 'LT2-4', stationId: 33213, position: [115.691513365791, 30.127631336], type: 'gnss', latest: {} }
      ],
      benchmarks: [
        { name: '管理处基准点', position: [115.693889, 30.131389], type: 'benchmark' }
      ],
      // 2. 坝前雨量水位站（坐标大致在LJ1-4左下方）
      rainStation: {
        name: '坝前雨量水位站',
        position: [115.693058, 30.129979],
        type: 'rain',
        latest: {} // mock，后续可接口获取
      },
      // 3. mcu测站（坐标大致在雨量水位站左下方）
      mcuStation: {
        name: 'mcu测站',
        position: [115.6900, 30.1260],
        type: 'mcu',
        latest: { deviceName: 'MCU-40-1932', status: '在线', time: '2025-06-18 16:30:00' } // mock
      },
      // 4. 图例配置
      legend: {
        project: [
          { name: 'GNSS测站/基准点', icon: '' },
          { name: '坝前雨量水位站', icon: flowIcon },
          { name: 'mcu测站', icon: mcuIcon }
        ],
        warning: [
          { name: '一般预警', icon: warnNormalPng },
          { name: '严重预警', icon: warnSeriousPng }
        ]
      },
      projectInfoWindow: null,
      warningList: [], // 当前未解除的预警
      stationMarkers: {}, // 站点名: marker实例
      // 将 MCU 测站替换为 KML 中的 UP* 渗压/渗流测站坐标
      mcuPressureStationsOnlyLine: (function() {
        // 渗压计编号映射（名称大小写不敏感，统一用大写匹配）
        const idMapUpper = {
          'UPB1-1': 'P0108248','UPB1-2': 'P0108234','UPB1-3': 'P0108376','UPB1-4': 'P0108173','UPB1-5': 'P0108236',
          'UPA1-1': 'P0108190','UPA1-2': 'P0108050','UPA1-3': 'P0108242','UPA1-4': 'P0108345','UPA1-5': 'P0108154',
          'UPB2-1': 'P0108310','UPB2-2': 'P0108046','UPB2-3': 'P0108235','UPB2-4': 'P0108066','UPB2-5': 'P0108043',
          'UPB3-1': 'P0108267','UPB3-2': 'P0108282','UPB3-3': 'P0108033','UPB3-4': 'P0108100','UPB3-5': 'P0108377',
          'UPB4-1': 'P0108174','UPB4-2': 'P0108273','UPB4-3': 'P0108198','UPB4-4': 'P0108181','UPB4-5': 'P0108056',
          'UPR1-1': 'P0108118','UPR1-2': 'P0108148','UPR2-1': 'P0108200','UPR2-2': 'P0108311'
        };
        const coord = (name, lng, lat) => ({
          name,
          position: [lng, lat],
          piezometerId: idMapUpper[name.toUpperCase()],
            type: 'upb'
          });
        return [
          // 来自 KML 的坐标（渗压渗流）
          coord('UPR1-1', 115.693391, 30.130486),
          coord('UPB1-1', 115.692855899, 30.129744369),
          coord('UPB2-1', 115.692551468314, 30.1293836116865),
          coord('UPA1-1', 115.692538058, 30.129391659),
          coord('UPB3-1', 115.691938261, 30.128661055),
          coord('UPB4-1', 115.691328058, 30.127916742),
          coord('UPB4-4', 115.691895507, 30.127457935),
          coord('UPB4-2', 115.691407345, 30.12786429),
          coord('UPB4-3', 115.691585712, 30.127718109),
          coord('UPB4-5', 115.692138247, 30.127284933),
          coord('UPB3-2', 115.692014864906, 30.1286018975696),
          coord('UPB3-4', 115.692505709, 30.12825321),
          coord('UPB3-3', 115.692210666, 30.128486562),
          coord('UPB2-2', 115.69261568, 30.129344869),
          coord('UPA1-2', 115.692626409, 30.12933414),
          coord('UPB2-3', 115.692808799, 30.129206735),
          coord('UPA1-3', 115.692826233, 30.129193324),
          coord('UPB2-4', 115.693127982, 30.128990818),
          coord('UPA1-4', 115.693152122, 30.12896936),
          coord('UPB2-5', 115.693342559, 30.128851343),
          coord('UPA1-5', 115.693366698, 30.128827203),
          coord('UPB1-5', 115.693602733, 30.12917589),
          coord('UPB1-4', 115.693404249, 30.129312683),
          coord('UPB1-3', 115.693117252955, 30.1295594459549),
          coord('UPB1-2', 115.692937545, 30.12968551),
          coord('UPR1-2', 115.693592003701, 30.1300932054631),
          coord('UPB3-5', 115.692727826, 30.12812076)
        ];
      })(),
      showGnss: true,
      showRain: true,
      showMcu: true,
      mcuLatestDataMap: {}, // 存储所有mcu测站的最新数据
      showStationDetailDialog: false, // 测站详情弹窗显示状态
      selectedStation: {}, // 当前选中的测站
      upbDataMap: {}, // 存储UPB测站的渗流量数据
      // 渗流量预览弹窗
      showUpPreviewDialog: false,
      selectedUpPreview: { name: '', piezometerId: '' },
      upPreviewChart: null,
      upPreviewSeries: [],
      // 渗压计编号 -> 渗压监测点 名称映射（用于展示）
      piezometerIdToUpName: {
        'P0108248':'UPb1-1','P0108234':'UPb1-2','P0108376':'UPb1-3','P0108173':'UPb1-4','P0108236':'UPb1-5',
        'P0108190':'UPa1-1','P0108050':'UPa1-2','P0108345':'UPa1-4','P0108154':'UPa1-5',
        'P0108050':'UPa1-2','P0108242':'UPa1-3','P0108310':'UPa1-5',
        'P0108310':'UPa1-5','P0108242':'UPa1-3',
        'P0108310':'UPa1-5','P0108050':'UPa1-2',
        'P0108310':'UPa1-5',
        'P0108310':'UPa1-5',
        'P0108230':'UPb2-1','P0108310x':'',
        'P0108310_y':''
      }
    }
  },
  computed: {
    warningLevel() {
      // 优先级：严重 > 一般 > 无
      if (this.warningList.some(w => w.level === '严重预警')) return 'danger';
      if (this.warningList.some(w => w.level === '一般预警')) return 'warning';
      return 'primary';
    }
  },
  methods: {
    formatDisplayTime(t) {
      if (!t) return '';
      const s = t.toString();
      // 纯数字：10位当秒，13位当毫秒
      if (/^\d{10}$/.test(s)) {
        return new Date(parseInt(s, 10) * 1000).toLocaleString('zh-CN', { hour12: false });
      }
      if (/^\d{13}$/.test(s)) {
        return new Date(parseInt(s, 10)).toLocaleString('zh-CN', { hour12: false });
      }
      const d = new Date(s.replace(/-/g,'/'));
      if (isNaN(d.getTime())) return t;
      // 与 GNSS 显示保持一致（中文本地格式、含秒、24小时制）
      return d.toLocaleString('zh-CN', { hour12: false });
    },
    initAMap() {
      AMapLoader.load({
        key: "b29bbc8bcb421044d560756fb3d2c828",
        version: "2.0",
        plugins: ['AMap.MapType', 'AMap.Marker', 'AMap.InfoWindow']
      }).then((AMap) => {
        this.map = new AMap.Map("map", {
          viewMode: "3D",
          zoom: 18,
          center: [115.691846443, 30.128530098],
          mapStyle: 'amap://styles/normal',
          maxZoom: 18,
          // 优化地图性能
          enableHighAccuracy: false,
          timeout: 10000,
          maximumAge: 0,
          convert: true
        });
        
        // 监听地图缩放事件，动态调整标记大小
        this.map.on('zoomend', () => {
          this.updateMarkerSizes();
        });
        
        // 添加鼠标移动事件
        this.map.on('mousemove', (e) => {
          this.mouseLngLat = e.lnglat;
        });
        
        // 异步添加图层和站点，不阻塞地图显示
        this.$nextTick(() => {
          this.addTianDiTuLayer(AMap);
          this.addAllStations(AMap);
          this.initInfoWindows(AMap);
        });
      }).catch((e) => {
        console.error('地图加载失败:', e);
      });
    },
    addTianDiTuLayer(AMap) {
      this.tdtImg = new AMap.TileLayer({
        getTileUrl: function(x, y, z) {
          return `http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=018a619987a8ca968e107e98f06e6250&TILEMATRIX=${z}&TILEROW=${y}&TILECOL=${x}`;
        },
        zIndex: 2
      });
      this.tdtAnno = new AMap.TileLayer({
        getTileUrl: function(x, y, z) {
          return `http://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=018a619987a8ca968e107e98f06e6250&TILEMATRIX=${z}&TILEROW=${y}&TILECOL=${x}`;
        },
        zIndex: 3
      });
      this.map.add([this.tdtImg, this.tdtAnno]);
    },
    toggleTdtImg() {
      if (this.tdtImg) {
        this.showTdtImg ? this.tdtImg.show() : this.tdtImg.hide();
      }
    },
    toggleTdtAnno() {
      if (this.tdtAnno) {
        this.showTdtAnno ? this.tdtAnno.show() : this.tdtAnno.hide();
      }
    },
    // 渲染所有站点 - 优化版本
    addAllStations(AMap) {
      // 批量创建所有marker，减少DOM操作
      const allMarkers = [];
      
      // GNSS测站和基准点
      [...this.gnssStations, ...this.benchmarks].forEach(station => {
        const size = this.getMarkerSize();
        const marker = new AMap.Marker({
          position: station.position,
          title: station.name,
          icon: new AMap.Icon({
            image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
            size: new AMap.Size(size, size)
          }),
          offset: new AMap.Pixel(-size/2, -size/2)
        });
        marker.on('mouseover', () => {
          // 统一为与 UPB 相同的浮窗风格
          let content = `<div style="padding: 8px; min-width: 150px;">
            <div style=\"font-weight: bold; color: #1890ff; margin-bottom: 4px;\">${station.name}</div>`;
          if (station.type === 'gnss' && station.latest) {
            content += `<div style=\"font-size: 14px; color: #52c41a; margin-bottom: 4px;\">合位移: ${station.latest.H} mm</div>`;
            content += `<div style=\"font-size: 14px; color: #1890ff; margin-bottom: 4px;\">Z位移: ${station.latest.Z} mm</div>`;
            content += `<div style=\"font-size: 12px; color: #999;\">时间: ${station.latest.time}</div>`;
          } else if (station.type === 'benchmark') {
            content += `<div style=\"font-size: 12px; color: #666;\">类型：基准点</div>`;
          }
          content += `</div>`;
          new AMap.InfoWindow({ content, offset: new AMap.Pixel(0, -30) }).open(this.map, marker.getPosition());
        });
        marker.on('click', () => {
          this.showStationDetail(station);
        });
        this.stationMarkers[station.name] = marker;
        allMarkers.push(marker);
      });
      
      // 坝前雨量水位站
      const size = this.getMarkerSize();
      const rainMarker = new AMap.Marker({
        position: this.rainStation.position,
        title: this.rainStation.name,
        icon: new AMap.Icon({
          image: flowIcon,
          size: new AMap.Size(size, size)
        }),
        offset: new AMap.Pixel(-size/2, -size/2)
      });
      rainMarker.on('mouseover', () => {
        let content = `<div style="padding: 8px; min-width: 150px;">
          <div style=\"font-weight: bold; color: #1890ff; margin-bottom: 4px;\">${this.rainStation.name}</div>
          <div style=\"font-size: 14px; color: #52c41a; margin-bottom: 4px;\">水位: ${this.rainStation.latest.water} m</div>
          <div style=\"font-size: 14px; color: #1890ff; margin-bottom: 4px;\">降雨量: ${this.rainStation.latest.rain} mm</div>
          <div style=\"font-size: 12px; color: #999;\">监测时间: ${this.rainStation.latest.time}</div>
        </div>`;
        new AMap.InfoWindow({ content, offset: new AMap.Pixel(0, -30) }).open(this.map, rainMarker.getPosition());
      });
      rainMarker.on('click', () => {
        this.showStationDetail(this.rainStation);
      });
      this.stationMarkers[this.rainStation.name] = rainMarker;
      allMarkers.push(rainMarker);
      
      // MCU测站 - 批量创建
      this.mcuPressureStationsOnlyLine.forEach(station => {
        const size = this.getMarkerSize();
        const marker = new AMap.Marker({
          position: station.position,
          title: station.name,
          icon: new AMap.Icon({
            image: mcuIcon,
            size: new AMap.Size(size, size)
          }),
          offset: new AMap.Pixel(-size/2, -size/2)
        });
        marker.on('mouseover', () => {
          const data = this.mcuLatestDataMap[station.name] || {};
          const upbData = this.upbDataMap[station.name] || {};
          
          let content = `<div style="padding: 8px; min-width: 150px;">
            <div style="font-weight: bold; color: #1890ff; margin-bottom: 4px;">${station.name}</div>`;
          
          if (station.type === 'upb' && upbData.piezometerId) {
            content += `<div style="font-size: 12px; color: #666; margin-bottom: 4px;">渗压计: ${upbData.piezometerId}</div>`;
            if (upbData.waterLevelElevation !== undefined) {
              content += `<div style="font-size: 14px; color: #52c41a; margin-bottom: 4px;">水位高程: ${upbData.waterLevelElevation.toFixed(2)} m</div>`;
            }
            if (upbData.temperature !== undefined) {
              content += `<div style="font-size: 14px; color: #1890ff; margin-bottom: 4px;">温度: ${upbData.temperature.toFixed(2)} °C</div>`;
            }
            if (upbData.pressure !== undefined) {
              content += `<div style="font-size: 14px; color: #1890ff; margin-bottom: 4px;">水压: ${upbData.pressure.toFixed(2)} MPa</div>`;
            }
            if (upbData.time) {
              content += `<div style=\"font-size: 12px; color: #999;\">检测时间: ${this.formatDisplayTime(upbData.time)}</div>`;
            }
          } else {
            if (data.value !== undefined) {
              content += `<div style="font-size: 14px; color: #1890ff; margin-bottom: 4px;">水位高程: ${data.value.toFixed(2)} m</div>`;
            } else {
              content += `<div style="font-size: 14px; color: #1890ff; margin-bottom: 4px;">水位高程: 暂无数据</div>`;
            }
            if (data.time) {
              const t = new Date((data.time || '').toString().replace(/-/g,'/'));
              const pad = n => (n<10?('0'+n):n);
              const tStr = isNaN(t.getTime()) ? data.time : `${t.getFullYear()}-${pad(t.getMonth()+1)}-${pad(t.getDate())} ${pad(t.getHours())}:${pad(t.getMinutes())}`;
              content += `<div style="font-size: 12px; color: #999;">检测时间: ${tStr}</div>`;
            } else {
              content += `<div style="font-size: 12px; color: #ff4d4f;">检测时间: 暂无数据</div>`;
            }
          }
          content += `</div>`;
          new AMap.InfoWindow({ content, offset: new AMap.Pixel(0, -30) }).open(this.map, marker.getPosition());
        });
        marker.on('click', async () => {
          if (station.piezometerId) {
            await this.loadUpbLatest(station);
            this.showStationDetail({ ...station, type: 'upb' });
          } else {
          this.showStationDetail(station);
          }
        });
        this.stationMarkers[station.name] = marker;
        allMarkers.push(marker);
      });
      
      // 批量添加到地图
      this.map.add(allMarkers);
      
      // 延迟更新图标，避免阻塞渲染
      this.$nextTick(() => {
        this.updateAllStationMarkerIcons();
        this.updateMarkerSizes(); // 初始化标记大小
      });
    },
    async loadUpbLatest(station) {
      // 获取该渗压计最新一条记录并填充到 upbDataMap 供对话框展示
      const end = new Date();
      const start = new Date(end.getTime() - 7*24*60*60*1000);
      const fmt = d => `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`;
      try {
        const params = {
          pointIds: station.piezometerId,
          startTime: fmt(start),
          endTime: fmt(end),
          size: 100,
          current: 1
        };
        const res = await axios.get('/data-new/page', { params });
        const records = Array.isArray(res.data && res.data.records) ? res.data.records : [];
        let latest = null;
        for (const r of records) {
          if (!latest || new Date(r.time) > new Date(latest.time)) latest = r;
        }
        if (latest) {
          let elev = null, wl = null, temp = null, pres = null;
          try { const rd = JSON.parse(latest.resultData || '{}'); elev = rd['水位高程']; wl = rd['水位']; pres = rd['水压']; } catch(e){}
          try { const od = JSON.parse(latest.originalData || '{}'); if (od['温度'] != null) temp = od['温度']; } catch(e){}
          this.$set(this.upbDataMap, station.name, {
            piezometerId: station.piezometerId,
            waterLevelElevation: typeof elev === 'number' ? elev : Number(elev),
            waterLevel: typeof wl === 'number' ? wl : Number(wl),
            temperature: typeof temp === 'number' ? temp : Number(temp),
            pressure: typeof pres === 'number' ? pres : Number(pres),
            time: latest.time
          });
        } else {
          this.$set(this.upbDataMap, station.name, { piezometerId: station.piezometerId });
        }
      } catch(e) {
        this.$set(this.upbDataMap, station.name, { piezometerId: station.piezometerId });
      }
    },
     async loadProjectData() {
      // 直接设置工程数据，避免网络请求阻塞
      this.projectData = {
        name: '荆竹水库',
        description: '荆竹水库是武穴市余川镇大坝村的重要水利设施...'
      };
    },
       generateProjectContent() {
      return `
        <div class="marker-info">
          <h3>工程简介</h3>
          <div class="info-detail">
            <p><i class="el-icon-office-building"></i> 荆竹水库</p>
            <p><i class="el-icon-document"></i> 荆竹水库是武穴市余川镇大坝村的重要水利设施，距武穴市43公里，余川镇7公里，地理位置为东经115°41′28″，北纬30°07′45″。水库始建于1958年11月，1963年1月基本竣工，1975年冬季对主坝进行了加高培厚，2005年至2009年完成除险加固工程。水库控制流域面积56.7平方公里，多年平均降雨量1489.0毫米，总库容7710万立方米，正常蓄水位71.89米，死水位47.89米。枢纽工程包括主坝、副坝、溢洪道、输水隧洞、输水管、电站及水厂等主要建筑物。主坝为粘土心墙代料坝，最大坝高39.19米，副坝为粘土斜墙代料坝，最大坝高8.1米。主要建筑物防洪标准为100年一遇洪水设计，校核洪水位76.02米。荆竹水库设计灌溉面积7.5万亩，供水涉及3个乡镇、8万人口，电站装机2台1260千瓦，年发电量130万度。近年来，水库实施了信息化建设，包括水雨情测报、大坝安全监测、视频监视等系统，提升了管理水平和运行安全性。</p>
            <p class="info-tip"><i class="el-icon-info"></i> 点击地图任意位置关闭</p>
          </div>
        </div>
      `;
    },
    initInfoWindows(AMap) {
    this.projectInfoWindow = new AMap.InfoWindow({
      content: this.generateProjectContent(),
      offset: new AMap.Pixel(20, -20),
      closeWhenClickMap: true
    });
    },
   toggleProjectInfo() {
    if (!this.projectInfoWindow || !this.map) return;
    const center = this.map.getCenter();
      this.projectInfoWindow.setContent(this.generateProjectContent());
      this.projectInfoWindow.open(this.map, center);
      this.map.setCenter(center);
    },
    async fetchWarningList() {
      // 直接设置空预警列表，避免网络请求阻塞
        this.warningList = [];
    },
    updateAllStationMarkerIcons() {
      const warningMap = {};
      this.warningList.forEach(w => {
        if (!warningMap[w.position]) warningMap[w.position] = [];
        warningMap[w.position].push(w);
      });
      
      const size = this.getMarkerSize();
      const iconSize = new window.AMap.Size(size, size);
      const offset = new window.AMap.Pixel(-size/2, -size/2);
      
      // GNSS测站
      this.gnssStations.forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) {
          if (warningMap[station.name] && warningMap[station.name].length > 0) {
            const hasSerious = warningMap[station.name].some(w => w.level === '严重预警');
            marker.setIcon(new window.AMap.Icon({
              image: hasSerious ? warnSeriousPng : warnNormalPng,
              size: iconSize
            }));
            marker.setOffset(offset);
            marker.off('click');
            marker.on('click', () => this.handleMarkerWarningClick(station.name));
          } else {
            marker.setIcon(new window.AMap.Icon({
              image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
              size: iconSize
            }));
            marker.setOffset(offset);
            marker.off('click');
          }
        }
      });
      
      // 坝前雨量水位站
      const rainMarker = this.stationMarkers[this.rainStation.name];
      if (rainMarker) {
        if (warningMap[this.rainStation.name] && warningMap[this.rainStation.name].length > 0) {
          const hasSerious = warningMap[this.rainStation.name].some(w => w.level === '严重预警');
          rainMarker.setIcon(new window.AMap.Icon({
            image: hasSerious ? warnSeriousPng : warnNormalPng,
            size: iconSize
          }));
          rainMarker.setOffset(offset);
          rainMarker.off('click');
          rainMarker.on('click', () => this.handleMarkerWarningClick(this.rainStation.name));
        } else {
          rainMarker.setIcon(new window.AMap.Icon({
            image: flowIcon,
            size: iconSize
          }));
          rainMarker.setOffset(offset);
          rainMarker.off('click');
        }
      }
      
      // mcu测站
      this.mcuPressureStationsOnlyLine.forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) {
          if (warningMap[station.name] && warningMap[station.name].length > 0) {
            const hasSerious = warningMap[station.name].some(w => w.level === '严重预警');
            marker.setIcon(new window.AMap.Icon({
              image: hasSerious ? warnSeriousPng : warnNormalPng,
              size: iconSize
            }));
            marker.setOffset(offset);
            marker.off('click');
            marker.on('click', () => this.handleMarkerWarningClick(station.name));
          } else {
            marker.setIcon(new window.AMap.Icon({
              image: mcuIcon,
              size: iconSize
            }));
            marker.setOffset(offset);
            // 重新绑定点击，打开测站详情（与主绑定保持一致）
            marker.off('click');
            marker.on('click', async () => {
              if (station.piezometerId) {
                await this.loadUpbLatest(station);
                this.showStationDetail({ ...station, type: 'upb' });
              } else {
                this.showStationDetail(station);
              }
            });
          }
        }
      });
    },
    async handleMarkerWarningClick(stationName) {
      try {
        await this.$confirm('是否解除该站点的所有未解除预警？', '解除预警', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        });
        // 找到所有未解除的预警
        const toRelieve = this.warningList.filter(w => w.position === stationName);
        for (const w of toRelieve) {
          await this.$http.post('/warning-information/update', { ...w, status: '已解除' });
        }
        this.$message.success('解除成功');
        await this.fetchWarningList();
        // 刷新弹窗表格
        if (this.$refs.warningTable && this.$refs.warningTable.refresh) {
          this.$refs.warningTable.refresh();
        }
      } catch (e) {
        // 用户取消
      }
    },
    async onWarningChanged() {
      await this.fetchWarningList();
    },
    async fetchLatestGnssData() {
      // 从后端获取最近一段时间内的真实数据，取各测站最新一条
      const end = new Date();
      const start = new Date(end.getTime() - 15 * 24 * 60 * 60 * 1000);
      const fmt = d => `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`;
      const stationIds = this.gnssStations.map(s => s.stationId).join(',');
      try {
        const params = {
          startTime: fmt(start),
          endTime: fmt(end),
          sensor: 'L1_GP',
          stationIds,
          projectId: 1681,
          page: 1,
          size: 2000
        };
        const res = await axios.get('/external-data/displacement-history', { params });
        const records = (res && res.data && res.data.records) ? res.data.records : [];
        // 聚合：每个 stationId 取 collectTime 最新的一条
        const latestByStation = {};
        const toDate = (t) => new Date((t || '').toString().replace(/-/g,'/'));
        for (const r of records) {
          const sid = r.stationId || (r.station && r.station.id);
          if (!sid) continue;
          if (!latestByStation[sid] || toDate(r.collectTime) > toDate(latestByStation[sid].collectTime)) {
            latestByStation[sid] = r;
          }
        }
        // 写回各测站 latest
        this.gnssStations.forEach(station => {
          const rec = latestByStation[station.stationId];
          if (!rec) return; // 无数据则保持原值，避免跳变
          const kv = Array.isArray(rec.keyValues) ? rec.keyValues : [];
          const pick = key => {
            const item = kv.find(i => i.key === key);
            return item ? Number(item.value) : undefined;
          };
          const x = pick('gpsTotalX');
          const y = pick('gpsTotalY');
          const z = pick('gpsTotalZ');
          const d3 = pick('displacement3d');
          // 与页面字段对应：Z、H、X、Y
          station.latest = {
            Z: (typeof z === 'number' && !isNaN(z)) ? z.toFixed(2) : '-',
            H: (typeof d3 === 'number' && !isNaN(d3)) ? d3.toFixed(2) : '-',
            X: (typeof x === 'number' && !isNaN(x)) ? x.toFixed(2) : '-',
            Y: (typeof y === 'number' && !isNaN(y)) ? y.toFixed(2) : '-',
            time: rec.collectTime || ''
          };
        });
      } catch (e) {
        // 后端不可用时不再生成随机数，维持上次数据显示，避免刷新跳变
      }
    },
    // ... existing code ...
    async fetchLatestRainStationData() {
      // 改为从后端获取最新一条有效水位数据（与 Seepage 使用规则一致）
      try {
        const res = await axios.get('/st-rivers-r/list');
        const arr = Array.isArray(res.data) ? res.data : [];
        const valid = arr.filter(it => Number(it.z1) > 0);
        let latest = null;
        const toDate = (tm) => {
          if (!tm || !Array.isArray(tm) || tm.length < 3) return new Date(0);
          const [y, m, d] = tm; return new Date(y, m - 1, d);
        };
        for (const it of valid) {
          if (!latest || toDate(it.tm) > toDate(latest.tm)) latest = it;
        }
        let latestWater = null; let waterTime = '';
        if (latest) {
          const water = Number(latest.z1);
          latestWater = isNaN(water) ? null : water;
          if (latest.tm && Array.isArray(latest.tm) && latest.tm.length >= 3) {
            const [y, m, d] = latest.tm;
            waterTime = `${y}-${String(m).padStart(2,'0')}-${String(d).padStart(2,'0')}`;
          }
        }
        
        // 修改：同步获取最新一条降雨量（小时雨量）
        let latestRain = null; let rainTime = '';
        try {
          const r = await axios.get('/st-pptn-hour/list');
          const lst = Array.isArray(r.data) ? r.data : (r.data && r.data.records) || [];
          
          // 确保数据是数组
          if (!Array.isArray(lst)) {
            console.error('降雨数据不是数组格式:', lst);
            throw new Error('数据格式错误');
          }
          
          // 找到最新的数据记录
          let last = null;
          const toT = (tm) => {
            if (!tm) return new Date(0);
            
            // 处理不同格式的时间
            if (Array.isArray(tm) && tm.length >= 5) {
              // 处理数组格式的时间 [年,月,日,时,分]
              return new Date(tm[0], tm[1]-1, tm[2], tm[3], tm[4]);
            }
            
            // 处理字符串格式的时间
            if (typeof tm === 'string') {
              // 尝试解析日期字符串
              const date = new Date(tm.replace(/-/g,'/'));
              if (!isNaN(date.getTime())) {
                return date;
              }
            }
            
            // 如果无法解析，返回当前时间
            return new Date();
          };
          
          for (const it of lst) {
            // 确保数据包含必要的字段
            if (!it || !it.tm && !it.time) continue;
            
            // 获取时间戳
            const itemTime = toT(it.tm || it.time);
            
            // 比较时间，找到最新的记录
            if (!last || itemTime > toT(last.tm || last.time)) {
              last = it;
            }
          }
          
          // 提取降雨量数据
          if (last) {
            // 优先使用 drp 字段，如果没有则使用 rain 或 value
            let rainValue = last.drp;
            if (rainValue === undefined || rainValue === null) {
              rainValue = last.rain;
            }
            if (rainValue === undefined || rainValue === null) {
              rainValue = last.value;
            }
            
            // 转换为数字
            latestRain = Number(rainValue);
            
            // 设置时间
            if (last.tm) {
              if (Array.isArray(last.tm) && last.tm.length >= 5) {
                const [y, m, d, h, min] = last.tm;
                rainTime = `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`;
              } else {
                rainTime = last.tm.toString();
              }
            } else if (last.time) {
              rainTime = last.time.toString();
            }
          }
        } catch(e) {
          console.error('获取降雨数据出错:', e);
        }

        this.rainStation.latest = {
          rain: latestRain != null && !isNaN(latestRain) ? latestRain.toFixed(1) : '-',
          water: latestWater != null ? latestWater.toFixed(2) : '-',
          time: waterTime || rainTime || ''
        };
      } catch(e) {
        // 保持上次可用值，不再随机造数据
      }
    },
// ... existing code ...
    setMcuStationTimeToNearestHalfHour() {
      const now = new Date();
      let hour = now.getHours();
      let minute = now.getMinutes();
      let nearestMinute = minute < 30 ? 0 : 30;
      // 如果minute>=30, nearestMinute=30，否则=0
      const pad = n => n < 10 ? '0' + n : n;
      const timeStr = `${now.getFullYear()}-${pad(now.getMonth()+1)}-${pad(now.getDate())} ${pad(hour)}:${pad(nearestMinute)}:00`;
      this.mcuStation.latest = {
        deviceName: 'MCU-40-1932',
        status: '在线',
        time: timeStr
      };
    },
    async fetchLatestMcuStationData() {
      // 不再随机生成，保持已有数据；若需要，可在此接真实接口
      return;
    },
    
    // 获取UPB测站渗流量数据
    async fetchLatestUpbStationData() {
      // 初始化阶段批量加载真实最新数据
      for (const station of this.mcuPressureStationsOnlyLine) {
        if (station.type === 'upb' && station.piezometerId) {
          await this.loadUpbLatest(station);
        }
      }
    },
    
    // 根据缩放级别计算标记大小
    getMarkerSize() {
      const zoom = this.map ? this.map.getZoom() : 18;
      // 缩放级别与标记大小的映射关系
      if (zoom >= 19) return 44;
      if (zoom >= 18) return 36;
      if (zoom >= 17) return 30;
      if (zoom >= 16) return 26;
      if (zoom >= 15) return 22;
      return 18;
    },
    
    // 更新所有标记的大小
    updateMarkerSizes() {
      if (!this.map) return;
      
      const size = this.getMarkerSize();
      const iconSize = new window.AMap.Size(size, size);
      const offset = new window.AMap.Pixel(-size/2, -size/2);
      
      // 更新GNSS测站和基准点标记
      [...this.gnssStations, ...this.benchmarks].forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) {
          marker.setIcon(new window.AMap.Icon({
            image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
            size: iconSize
          }));
          marker.setOffset(offset);
        }
      });
      
      // 更新雨量水位站标记
      const rainMarker = this.stationMarkers[this.rainStation.name];
      if (rainMarker) {
        rainMarker.setIcon(new window.AMap.Icon({
          image: flowIcon,
          size: iconSize
        }));
        rainMarker.setOffset(offset);
      }
      
      // 更新MCU测站标记
      this.mcuPressureStationsOnlyLine.forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) {
          marker.setIcon(new window.AMap.Icon({
            image: mcuIcon,
            size: iconSize
          }));
          marker.setOffset(offset);
        }
      });
    },
    toggleGnssMarkers(show) {
      [...this.gnssStations, ...this.benchmarks].forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) show ? marker.show() : marker.hide();
      });
    },
    toggleRainMarker(show) {
      const marker = this.stationMarkers[this.rainStation.name];
      if (marker) show ? marker.show() : marker.hide();
    },
    toggleMcuMarkers(show) {
      this.mcuPressureStationsOnlyLine.forEach(station => {
        const marker = this.stationMarkers[station.name];
        if (marker) show ? marker.show() : marker.hide();
      });
    },
    
    // 显示测站详情弹窗
    showStationDetail(station) {
      this.selectedStation = station;
      this.showStationDetailDialog = true;
    },
    
    // 获取测站类型名称
    getStationTypeName(type) {
      const typeMap = {
        'gnss': 'GNSS测站',
        'rain': '雨量水位站',
        'mcu': 'MCU测站',
        'upb': 'UPB渗压测站',
        'benchmark': '基准点'
      };
      return typeMap[type] || '未知类型';
    },
    
    // 获取数据状态样式类
    getDataStatusClass(value, min, max) {
      if (value === undefined || value === null) return 'data-error';
      const numValue = parseFloat(value);
      if (numValue < min || numValue > max) return 'data-warning';
      return 'data-normal';
    },
    
    // 获取水位状态样式类
    getWaterLevelClass(value) {
      if (value === undefined || value === null) return 'data-error';
      const numValue = parseFloat(value);
      if (numValue > 60) return 'data-warning';
      if (numValue > 55) return 'data-normal';
      return 'data-info';
    },
    
    // 获取降雨量状态样式类
    getRainfallClass(value) {
      if (value === undefined || value === null) return 'data-error';
      const numValue = parseFloat(value);
      if (numValue > 50) return 'data-warning';
      if (numValue > 25) return 'data-normal';
      return 'data-info';
    },
    
    // 获取温度状态样式类
    getTemperatureClass(value) {
      if (value === undefined || value === null) return 'data-error';
      const numValue = parseFloat(value);
      if (numValue > 30 || numValue < 0) return 'data-warning';
      if (numValue > 25 || numValue < 5) return 'data-normal';
      return 'data-info';
    },
    
    // 获取水压状态样式类
    getPressureClass(value) {
      if (value === undefined || value === null) return 'data-error';
      const numValue = parseFloat(value);
      if (numValue > 1.5) return 'data-warning';
      if (numValue > 0.8) return 'data-normal';
      return 'data-info';
    },
    
    // 刷新测站数据
    async refreshStationData() {
      try {
        if (this.selectedStation.type === 'gnss') {
          await this.fetchLatestGnssData();
        } else if (this.selectedStation.type === 'rain') {
          await this.fetchLatestRainStationData();
        } else if (this.selectedStation.type === 'mcu') {
          await this.fetchLatestMcuStationData();
        } else if (this.selectedStation.type === 'upb') {
          await this.loadUpbLatest(this.selectedStation);
        }
        this.$message.success('数据刷新成功');
      } catch (error) {
        console.error('刷新数据失败:', error);
        this.$message.error('数据刷新失败');
      }
    },
},
async mounted() {
    try {
      // 先初始化地图，让用户看到界面
      await this.initAMap();
      
      // 快速加载模拟数据
      this.loadProjectData();
      this.fetchLatestGnssData();
      this.fetchLatestRainStationData();
      this.fetchLatestMcuStationData();
      this.fetchLatestUpbStationData();
      this.fetchWarningList();
    this.setMcuStationTimeToNearestHalfHour();
      
    } catch (error) {
      console.error('地图初始化失败:', error);
    } finally {
      // 延迟关闭加载状态，确保地图完全加载
      setTimeout(() => {
        this.isLoading = false;
      }, 1000);
    }
  },
  watch: {
    showWarningDialog(val) {
      if (!val) this.fetchWarningList();
    },
    showGnss(val) { this.toggleGnssMarkers(val); },
    showRain(val) { this.toggleRainMarker(val); },
    showMcu(val) { this.toggleMcuMarkers(val); },
}
}
</script>

<style scoped>
#map-container {
  width: 100%;
  height: 100vh;
  position: relative;
}
.map {
  width: 100%;
  height: 100%;
}
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}
.loading-spinner {
  text-align: center;
  color: #1890ff;
}
.loading-spinner i {
  font-size: 32px;
  margin-bottom: 10px;
}
.loading-spinner p {
  margin: 0;
  font-size: 14px;
}
.project-info {
  position: absolute;
  right: 30px;
  bottom: 50%;
  transform: translateY(50%);
  z-index: 1000;
  cursor: pointer;
  background: linear-gradient(135deg, #00B2D5, #0089b9);
  border-radius: 20px;
  width: auto;
  height: 40px;
  box-shadow: 0 4px 15px rgba(0, 90, 158, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 18px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid rgba(255,255,255,0.2);
  will-change: transform;
  backface-visibility: hidden;
}
.project-info i {
  color: #fff;
  font-size: 18px;
  margin-right: 8px;
}
.project-info-text {
  color: #fff;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 1px;
}
.warning-info-btn {
  position: absolute;
  left: 50%;
  top: 18px;
  transform: translateX(-50%);
  z-index: 1100;
}
.small-legend {
  min-width: 80px;
  padding: 6px;
  font-size: 11px;
}
.small-legend .legend-title {
  font-size: 12px;
  margin-bottom: 4px;
  padding-bottom: 4px;
}
.small-legend .legend-item {
  margin-bottom: 2px;
}
.small-legend .legend-color,
.small-legend .legend-marker img {
  width: 12px !important;
  height: 16px !important;
}
.small-legend .legend-text {
  font-size: 10px;
}
.legend-top-left {
  position: absolute;
  top: 24px;
  left: 24px;
  right: auto;
  z-index: 1000;
  background: rgba(255,255,255,0.98);
  border: 2px solid #1890FF;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(24,144,255,0.10);
  padding: 18px 22px 14px 22px;
  min-width: 120px;
  font-size: 15px;
  font-weight: bold;
  color: #222;
  letter-spacing: 1px;
  transition: all 0.2s;
}
.legend-top-left .legend-title {
  font-size: 18px;
  font-weight: 700;
  color: #1890FF;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e6f7ff;
}
.legend-top-left .legend-item {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}
.legend-top-left .legend-item:last-child {
  margin-bottom: 0;
}
.legend-top-left .legend-marker img {
  width: 18px !important;
  height: 26px !important;
  margin-right: 10px;
}
.legend-top-left .legend-marker i {
  margin-right: 10px;
}
.legend-top-left .legend-text {
  font-size: 15px;
  color: #333;
  font-weight: 600;
}
.layer-switch.legend-top-right {
  position: absolute;
  top: 24px;
  right: 24px;
  left: auto;
  z-index: 1100;
  background: rgba(255,255,255,0.95);
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  padding: 8px 12px;
  font-size: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.layer-switch label {
  cursor: pointer;
  user-select: none;
}
.layer-switch input[type="checkbox"] {
  margin-right: 6px;
}
.map-coord {
  position: absolute;
  right: 24px;
  bottom: 24px;
  background: rgba(0,0,0,0.7);
  color: #fff;
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  z-index: 1001;
  pointer-events: none;
  font-family: monospace;
}

/* 测站详情弹窗样式 */
.station-detail-content {
  padding: 20px 0;
}

.station-info {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  border-left: 4px solid #1890ff;
}

.info-row {
  display: flex;
  margin-bottom: 8px;
  align-items: center;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-row .label {
  font-weight: 600;
  color: #333;
  min-width: 80px;
  margin-right: 10px;
}

.info-row .value {
  color: #666;
  flex: 1;
}

.data-section {
  margin-bottom: 25px;
}

.data-section h4 {
  color: #1890ff;
  font-size: 16px;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e6f7ff;
}

.data-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.data-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #e8e8e8;
}

.data-label {
  font-weight: 500;
  color: #333;
}

.data-value {
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.data-normal {
  color: #1890ff;
  background: #f0f9ff;
  border: 1px solid #91d5ff;
}

.data-warning {
  color: #1890ff;
  background: #f0f9ff;
  border: 1px solid #91d5ff;
}

.data-error {
  color: #ff4d4f;
  background: #fff2f0;
  border: 1px solid #ffccc7;
}

.data-info {
  color: #1890ff;
  background: #f0f9ff;
  border: 1px solid #91d5ff;
}

.status-online {
  color: #52c41a;
  background: #f6ffed;
  border: 1px solid #b7eb8f;
}

.dialog-footer {
  text-align: right;
  padding-top: 20px;
  border-top: 1px solid #e8e8e8;
}
</style>
