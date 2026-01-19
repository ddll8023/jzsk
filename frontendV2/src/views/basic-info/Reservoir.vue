<template>
  <!-- 库区基本情况页面 -->
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-8">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">库区基本情况</h1>
      <p class="mt-1 text-sm text-gray-500">武穴市荆竹水库工程概况（简版）</p>
    </header>

    <!-- 核心指标卡片 -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <Card
        v-for="metric in metrics"
        :key="metric.label"
        variant="default"
        shadow="sm"
        rounded="xl"
        padding="md"
        class="text-center hover:shadow-md transition-shadow duration-300"
      >
        <div class="text-3xl font-bold text-primary-600 mb-2">{{ metric.value }}</div>
        <div class="text-sm text-gray-500 font-medium">{{ metric.label }}</div>
      </Card>
    </div>

    <!-- 工程概况 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="md"
      class="mb-6"
    >
      <div class="flex items-center gap-3 mb-6">
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <i class="fa fa-info-circle text-gray-400" aria-hidden="true"></i>
        <h2 class="text-base font-bold text-gray-800">工程概况</h2>
      </div>
      <div class="space-y-4 text-gray-700 leading-relaxed">
        <p>
          荆竹水库位于湖北省武穴市，是以防洪、供水为主，兼顾灌溉、生态等综合利用的中型水库工程。
          枢纽主体由坝体、溢洪与放水建筑物等组成。工程等别为Ⅲ等，主要建筑物级别为3级。
        </p>
        <p>
          水库正常蓄水位 <span class="font-semibold text-primary-600">{{ design.normalLevel }}m</span>，
          校核洪水位 <span class="font-semibold text-primary-600">{{ design.checkLevel }}m</span>，
          总库容约 <span class="font-semibold text-primary-600">{{ design.totalStorage }} 万m³</span>，
          兴利库容约 <span class="font-semibold text-primary-600">{{ design.usefulStorage }} 万m³</span>。
          枢纽布置紧凑，兼顾运行安全与运维便利。
        </p>
        <p>
          水库承担武穴城区及沿线居民生活与工业供水保障任务，同时通过生态下泄维持下游河道基本生态需水，
          对区域防洪减灾与水资源调控具有重要意义。
        </p>
      </div>
    </Card>

    <!-- 枢纽布置与形象 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="md"
      class="mb-6"
    >
      <div class="flex items-center gap-3 mb-6">
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <i class="fa fa-image text-gray-400" aria-hidden="true"></i>
        <h2 class="text-base font-bold text-gray-800">枢纽布置与形象</h2>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
        <!-- 平面布置图占位 -->
        <div class="aspect-video bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg flex flex-col items-center justify-center border border-blue-200">
          <i class="fa fa-map text-4xl text-blue-400 mb-2" aria-hidden="true"></i>
          <span class="text-blue-600 font-medium">平面布置图</span>
          <span class="text-xs text-blue-400 mt-1">待上传</span>
        </div>
        <!-- 地形图占位 -->
        <div class="aspect-video bg-gradient-to-br from-green-50 to-green-100 rounded-lg flex flex-col items-center justify-center border border-green-200">
          <i class="fa fa-globe text-4xl text-green-400 mb-2" aria-hidden="true"></i>
          <span class="text-green-600 font-medium">地形图</span>
          <span class="text-xs text-green-400 mt-1">待上传</span>
        </div>
      </div>
      <p class="text-xs text-gray-400 text-center">
        注：图示为示意图，具体以设计与竣工资料为准。
      </p>
    </Card>

    <!-- 运行与管理要点 -->
    <Card 
      variant="default" 
      shadow="sm" 
      rounded="xl" 
      padding="md"
    >
      <div class="flex items-center gap-3 mb-6">
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <i class="fa fa-tasks text-gray-400" aria-hidden="true"></i>
        <h2 class="text-base font-bold text-gray-800">运行与管理要点</h2>
      </div>
      <ul class="space-y-3">
        <li 
          v-for="(tip, index) in tips" 
          :key="index"
          class="flex items-start gap-3 text-gray-700"
        >
          <span class="flex-shrink-0 w-6 h-6 rounded-full bg-primary-100 text-primary-700 flex items-center justify-center text-sm font-semibold">
            {{ index + 1 }}
          </span>
          <span class="leading-relaxed">{{ tip }}</span>
        </li>
      </ul>
    </Card>
  </div>
</template>

<script setup>
/**
 * 库区基本情况页面
 * 功能：展示荆竹水库的基本信息、工程概况、枢纽布置及运行要点
 * 依赖组件：Card
 * 遵循原则：KISS, YAGNI, SOLID
 * 数据来源：静态硬编码（与旧项目保持一致）
 */
import { reactive } from 'vue'
import Card from '@/components/basic/Card.vue'

// 核心指标数据
const metrics = reactive([
  { label: '集雨面积', value: '38.6 km²' },
  { label: '坝高', value: '42.5 m' },
  { label: '坝长', value: '210 m' },
  { label: '建设时间', value: '1972年' }
])

// 设计参数
const design = reactive({
  normalLevel: 56.5,    // 正常蓄水位
  checkLevel: 58.1,     // 校核洪水位
  totalStorage: 1738,   // 总库容（万m³）
  usefulStorage: 850    // 兴利库容（万m³）
})

// 运行管理要点
const tips = reactive([
  '汛期严格执行调度令，确保防洪库容及时腾挪；',
  '非汛期保障城镇生活及工业供水，维持生态基流；',
  '重点巡查大坝迎水面、廊道与消力池等关键部位；',
  '完善视频监控、在线监测与预警联动，提高运行安全。'
])
</script>
