<template>
  <div>
    <!-- 工程简介按钮 -->
    <button
      @click="showModal = true"
      class="absolute top-4 left-20 z-10 flex items-center gap-2 px-4 py-2 bg-white/90 backdrop-blur-sm rounded-lg shadow-lg hover:bg-white hover:shadow-xl transition-all duration-200"
    >
      <i class="fa fa-info-circle text-primary-600 text-lg" aria-hidden="true"></i>
      <span class="text-sm font-medium text-gray-900">工程简介</span>
    </button>

    <!-- 工程简介弹窗 -->
    <Modal v-model="showModal" title="荆竹水库工程简介" width="lg">
      <div class="prose prose-sm max-w-none">
        <p class="text-gray-700 leading-relaxed text-justify indent-8">
          荆竹水库是武穴市余川镇大坝村的重要水利设施，距武穴市43公里，余川镇7公里，地理位置为东经115°41′28″，北纬30°07′45″。水库始建于1958年11月，1963年1月基本竣工，1975年冬季对主坝进行了加高培厚，2005年至2009年完成除险加固工程。
        </p>
        <p class="text-gray-700 leading-relaxed text-justify indent-8">
          水库控制流域面积56.7平方公里，多年平均降雨量1489.0毫米，总库容7710万立方米，正常蓄水位71.89米，死水位47.89米。枢纽工程包括主坝、副坝、溢洪道、输水隧洞、输水管、电站及水厂等主要建筑物。主坝为粘土心墙代料坝，最大坝高39.19米，副坝为粘土斜墙代料坝，最大坝高8.1米。主要建筑物防洪标准为100年一遇洪水设计，校核洪水位76.02米。
        </p>
        <p class="text-gray-700 leading-relaxed text-justify indent-8">
          荆竹水库设计灌溉面积7.5万亩，供水涉及3个乡镇、8万人口，电站装机2台1260千瓦，年发电量130万度。近年来，水库实施了信息化建设，包括水雨情测报、大坝安全监测、视频监视等系统，提升了管理水平和运行安全性。
        </p>
      </div>

      <template #footer>
        <Button @click="showModal = false">关闭</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 工程简介按钮组件
 * 功能：显示荆竹水库工程简介弹窗
 * 依赖：Modal、Button组件
 */
import { ref } from 'vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'

const showModal = ref(false)
</script>
