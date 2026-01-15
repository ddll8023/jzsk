<template>
  <div class="center-map-panel">
    <one-map :mapId="'bigscreen-map'" :divId="'bigscreen-div1'" :popupId="'bigscreen-popup'" :closerId="'bigscreen-popup-closer'" :contentId="'bigscreen-popup-content'" style="width:100%;height:100%;" />
  </div>
</template>
<script>
import OneMap from '../OneMap.vue'
export default {
  name: 'CenterMapPanel',
  components: { OneMap }
}
</script>
<style scoped>
.center-map-panel { width: 100%; height: 100%; background: #0a1a2a; }
</style> 