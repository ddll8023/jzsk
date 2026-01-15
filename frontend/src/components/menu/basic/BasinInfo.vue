<template>
  <div>
    <div class="overview-container">
      <el-card class="hero-card" shadow="hover">
        <div class="hero">
          <div class="title">
            <h2>库区基本情况</h2>
            <p>武穴市荆竹水库工程概况（简版）</p>
          </div>
        </div>
      </el-card>

      <el-row :gutter="16" class="metrics-row">
        <el-col :span="6" v-for="m in metrics" :key="m.label">
          <el-card shadow="hover" class="metric-card">
            <div class="metric-value">{{ m.value }}</div>
            <div class="metric-label">{{ m.label }}</div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="intro-card" shadow="hover">
        <div slot="header" class="section-title">工程概况</div>
        <div class="intro">
          <p>
            荆竹水库位于湖北省武穴市，是以防洪、供水为主，兼顾灌溉、生态等综合利用的中型水库工程。
            枢纽主体由坝体、溢洪与放水建筑物等组成。工程等别为Ⅲ等，主要建筑物级别为3级。
          </p>
          <p>
            水库正常蓄水位 {{ design.normalLevel }}m，校核洪水位 {{ design.checkLevel }}m，总库容约 {{ design.totalStorage }} 万m³，
            兴利库容约 {{ design.usefulStorage }} 万m³。枢纽布置紧凑，兼顾运行安全与运维便利。
          </p>
          <p>
            水库承担武穴城区及沿线居民生活与工业供水保障任务，同时通过生态下泄维持下游河道基本生态需水，对区域防洪减灾与
            水资源调控具有重要意义。
          </p>
        </div>
      </el-card>

      <el-card class="layout-card" shadow="hover">
        <div slot="header" class="section-title">枢纽布置与形象</div>
        <el-row :gutter="12" class="layout-row">
          <el-col :span="12">
            <div class="image-placeholder">
              <p>平面布置图</p>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="image-placeholder">
              <p>地形图</p>
            </div>
          </el-col>
        </el-row>
        <div class="layout-caption">注：图示为示意图，具体以设计与竣工资料为准。</div>
      </el-card>

      <el-card class="tips-card" shadow="hover">
        <div slot="header" class="section-title">运行与管理要点</div>
        <ul class="tips">
          <li>汛期严格执行调度令，确保防洪库容及时腾挪；</li>
          <li>非汛期保障城镇生活及工业供水，维持生态基流；</li>
          <li>重点巡查大坝迎水面、廊道与消力池等关键部位；</li>
          <li>完善视频监控、在线监测与预警联动，提高运行安全。</li>
        </ul>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BasinInfo',
  data() {
    return {
      metrics: [
        { label: '集雨面积', value: '38.6 km²' },
        { label: '坝高', value: '42.5 m' },
        { label: '坝长', value: '210 m' },
        { label: '建设时间', value: '1972年' }
      ],
      design: {
        normalLevel: 56.5,
        checkLevel: 58.1,
        totalStorage: 1738,
        usefulStorage: 850
      }
    }
  }
}
</script>

<style scoped>
.overview-container {
  padding: 20px;
  background-color: #f5f6fa;
  min-height: calc(100vh - 80px);
}

.hero-card {
  margin-bottom: 20px;
  background: linear-gradient(120deg, #e0f7fa, #bbdefb);
  border: none;
}

.hero {
  text-align: center;
  padding: 20px;
}

.title h2 {
  font-size: 28px;
  color: #1a237e;
  margin-bottom: 10px;
}

.title p {
  font-size: 16px;
  color: #4a148c;
}

.metrics-row {
  margin-bottom: 20px;
}

.metric-card {
  text-align: center;
  border-radius: 8px;
  overflow: hidden;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #1a237e;
  margin: 10px 0;
}

.metric-label {
  font-size: 14px;
  color: #666;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #1a237e;
}

.intro-card {
  margin-bottom: 20px;
}

.intro p {
  line-height: 1.8;
  margin-bottom: 15px;
  color: #333;
}

.layout-card {
  margin-bottom: 20px;
}

.layout-row {
  margin-bottom: 10px;
}

.image-placeholder {
  width: 100%;
  height: 200px;
  background-color: #e3f2fd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1976d2;
  font-weight: bold;
}

.layout-caption {
  font-size: 12px;
  color: #888;
  text-align: center;
}

.tips-card ul {
  padding-left: 20px;
}

.tips li {
  margin-bottom: 10px;
  line-height: 1.6;
  color: #333;
}
</style>