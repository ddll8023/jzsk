<template>
  <div class="list-panel">
    <ul>
      <li v-for="item in mockList" :key="item.time">
        <span class="alarm-time">{{item.time}}</span>
        <span class="alarm-msg">{{item.msg}}</span>
        <span class="alarm-type" :class="item.level">{{item.level}}</span>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'ListPanel',
  data() {
    return {
      mockList: [
        { time: '10:01', msg: '荆竹水库水位超限', level: '高' },
        { time: '10:03', msg: '南湖水库设备离线', level: '中' },
        { time: '10:05', msg: '北湖水库流量异常', level: '高' },
        { time: '10:08', msg: '东湖水库通信异常', level: '低' }
      ]
    }
  }
}
</script>
<style scoped>
.list-panel { background: transparent; }
ul { list-style: none; padding: 0; margin: 0; }
li { display: flex; align-items: center; padding: 4px 0; border-bottom: 1px solid #1a2a3a; }
.alarm-time { color: #00eaff; width: 60px; font-size: 14px; }
.alarm-msg { color: #fff; flex: 1; font-size: 14px; }
.alarm-type { font-size: 14px; font-weight: bold; margin-left: 8px; padding: 2px 8px; border-radius: 8px; }
.alarm-type.高 { background: #ff4d4f; color: #fff; }
.alarm-type.中 { background: #faad14; color: #fff; }
.alarm-type.低 { background: #1890ff; color: #fff; }
</style> 