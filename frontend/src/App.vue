<template>
  <div id="app">
    <!--占位符 -->
    <router-view></router-view>
    <!-- API状态指示器 -->
    <ApiStatusIndicator />
  </div>
</template>

<script>
import ApiStatusIndicator from './components/ApiStatusIndicator.vue';

export default {
  name: 'App',
  components: {
    ApiStatusIndicator
  }
};
</script>



<style>
* {
  margin: 0;
  padding: 0;
}
body {
  height: 100%;
  width: 100%;
}
@keyframes marker-blink {
  0%, 100% {
    filter: drop-shadow(0 0 0px #ff2222) brightness(1.2);
    box-shadow: 0 0 0px 0px #ff2222;
    background-color: transparent;
  }
  50% {
    filter: drop-shadow(0 0 24px #ff2222) brightness(2.2);
    box-shadow: 0 0 32px 12px #ff2222;
    background-color: rgba(255, 0, 0, 0.18);
  }
}
.marker-danger {
  animation: marker-blink 0.6s infinite;
  filter: drop-shadow(0 0 16px #ff2222) brightness(1.5) !important;
  box-shadow: 0 0 32px 12px #ff2222 !important;
  border-radius: 50% !important;
  z-index: 9999 !important;
}
@keyframes marker-blink-warning {
  0%, 100% {
    filter: drop-shadow(0 0 0px orange);
    background-color: transparent;
  }
  50% {
    filter: drop-shadow(0 0 12px orange) brightness(1.5);
    background-color: rgba(255, 165, 0, 0.18);
  }
}
.marker-warning {
  animation: marker-blink-warning 1s infinite !important;
  filter: drop-shadow(0 0 6px orange) !important;
}
</style>
