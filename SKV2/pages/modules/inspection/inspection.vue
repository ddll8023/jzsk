<template>
  <view class="min-h-screen bg-gray-50 p-4" style="padding-bottom: calc(24rpx + env(safe-area-inset-bottom))">
    <!-- 筛选面板 -->
    <SkCard title="巡检筛选">
      <InspectionFilter
        :project-options="options.projectList"
        :abnormal-options="options.abnormalList"
        :solve-options="options.solveList"
        :person-options="options.personList"
        @search="handleSearch"
        @reset="handleReset"
      />
    </SkCard>

    <!-- 列表 -->
    <SkCard title="巡检记录">
      <InspectionList
        :data="tableData"
        :loading="loading"
        :total="total"
        @load-more="handleLoadMore"
        @item-click="handleView"
        @edit="handleEdit"
        @delete="handleDelete"
        @solve="handleSolve"
      />
    </SkCard>

    <!-- 悬浮新增按钮 -->
    <view
      class="fixed right-5 w-14 h-14 bg-primary rounded-full shadow-lg flex items-center justify-center active:scale-95 transition-transform z-50"
      style="bottom: calc(120rpx + env(safe-area-inset-bottom))"
      @click="handleAdd"
    >
      <text class="text-white text-2xl font-light leading-none">+</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 巡检记录列表页
 * 功能：巡检记录列表、筛选、新增/编辑/删除/标记处理
 */
import { onShow, onPullDownRefresh } from '@dcloudio/uni-app'
import { useInspection } from '@/composables/useInspection.js'
import SkCard from '@/components/common/SkCard.vue'
import InspectionFilter from '@/components/business/inspection/InspectionFilter.vue'
import InspectionList from '@/components/business/inspection/InspectionList.vue'

const {
  loading,
  tableData,
  total,
  query,
  options,
  fetchData,
  loadMore,
  refresh,
  removeData,
  solveData,
  resetQuery,
} = useInspection()

onShow(() => {
  refresh()
})

onPullDownRefresh(() => {
  refresh().then(() => uni.stopPullDownRefresh())
})

const handleSearch = (filters) => {
  query.project = filters.project
  query.startTime = filters.startTime
  query.endTime = filters.endTime
  query.abnormal = filters.abnormal
  query.solve = filters.solve
  query.person = filters.person
  query.page = 1
  fetchData()
}

const handleReset = () => {
  resetQuery()
  fetchData()
}

const handleLoadMore = () => {
  loadMore()
}

const handleView = (item) => {
  uni.navigateTo({
    url: `/pages/modules/inspection/inspection-form?id=${item.id}`,
  })
}

const handleEdit = (item) => {
  uni.navigateTo({
    url: `/pages/modules/inspection/inspection-form?id=${item.id}`,
  })
}

const handleDelete = async (item) => {
  await removeData(item.id)
}

const handleSolve = async (item) => {
  await solveData(item.id)
}

const handleAdd = () => {
  uni.navigateTo({
    url: '/pages/modules/inspection/inspection-form',
  })
}
</script>
