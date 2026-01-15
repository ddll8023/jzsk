<template>
  <div class="basicinfo-container">
    <el-card>
      <div slot="header" class="header-row">
        <span>洪水防御预案</span>
        <el-button type="primary" size="mini" @click="addStep" icon="el-icon-plus" style="float:right;">新增步骤</el-button>
      </div>
      <el-form :model="editList" label-width="60px" class="plan-form">
        <div v-for="(item, idx) in editList" :key="idx" class="plan-step-row">
          <el-form-item :label="'步骤' + (idx + 1)" class="step-label">
            <el-input v-model="item.time" placeholder="阶段/时间点，如汛前、汛期、洪水发生时等" style="width:120px; margin-right:10px;" />
            <el-input v-model="item.content" placeholder="请输入具体内容" style="width:60%; margin-right:10px;" />
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeStep(idx)" v-if="editList.length > 1"></el-button>
          </el-form-item>
        </div>
      </el-form>
      <div class="btn-row">
        <el-button type="primary" @click="savePlan" icon="el-icon-check">保存</el-button>
        <el-button @click="resetPlan" icon="el-icon-refresh">重置</el-button>
      </div>
      <el-divider></el-divider>
      <div class="preview-title">预案预览：</div>
      <el-timeline>
        <el-timeline-item v-for="(item, idx) in editList" :key="'preview'+idx" :timestamp="item.time" placement="top">
          {{ item.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'FloodPlan',
  data() {
    return {
      defaultList: [
        { time: '汛前', content: '检查大坝、闸门、通讯等设施，完善应急物资储备' },
        { time: '汛期', content: '加强监测，及时发布预警，科学调度水库' },
        { time: '洪水发生时', content: '启动应急响应，组织人员转移，保障群众安全' },
        { time: '洪水后', content: '开展灾后评估和恢复重建' }
      ],
      editList: []
    }
  },
  created() {
    this.resetPlan();
  },
  methods: {
    addStep() {
      this.editList.push({ time: '', content: '' });
    },
    removeStep(idx) {
      this.editList.splice(idx, 1);
    },
    savePlan() {
      this.$message.success('预案已保存（仅本地，刷新页面会丢失）');
    },
    resetPlan() {
      // 深拷贝默认数据
      this.editList = this.defaultList.map(item => ({ ...item }));
    }
  }
}
</script>
<style scoped>
.basicinfo-container { padding: 24px; }
.header-row { display: flex; align-items: center; justify-content: space-between; font-size: 18px; font-weight: bold; }
.plan-form { margin-top: 18px; }
.plan-step-row { margin-bottom: 10px; display: flex; align-items: center; }
.step-label { width: 100%; }
.btn-row { margin: 18px 0 10px 0; }
.preview-title { font-size: 15px; font-weight: bold; margin: 10px 0 8px 0; color: #409EFF; }
</style> 