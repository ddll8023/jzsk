<template>
  <view class="min-h-screen bg-gray-50">
    <view class="p-4 space-y-4">
      <!-- 基本信息 -->
      <SkCard title="基本信息">
        <view class="space-y-4 py-2">
          <SkSelect
            v-model="formData.project"
            label="巡检站点"
            :options="options.projectList"
            placeholder="请选择巡检站点"
          />
          <SkSelect
            v-model="formData.type"
            label="巡检类型"
            :options="options.typeList"
            placeholder="请选择巡检类型"
          />
          <SkInput
            v-model="formData.person"
            label="负责人"
            placeholder="请输入负责人"
            clearable
          />
          <SkInput
            v-model="formData.date"
            label="巡检日期"
            type="datetime"
            placeholder="请选择日期"
          />
        </view>
      </SkCard>

      <!-- 位置信息 -->
      <SkCard title="位置信息">
        <view class="space-y-4 py-2">
          <SkInput
            v-model="formData.longitude"
            label="经度"
            type="digit"
            placeholder="请输入经度"
            clearable
          />
          <SkInput
            v-model="formData.latitude"
            label="纬度"
            type="digit"
            placeholder="请输入纬度"
            clearable
          />
          <view class="pt-1">
            <SkButton text="获取当前位置" type="default" size="small" @click="handleGetLocation" />
          </view>
        </view>
      </SkCard>

      <!-- 巡检情况 -->
      <SkCard title="巡检情况">
        <view class="space-y-4 py-2">
          <SkSelect
            v-model="formData.abnormal"
            label="异常情况"
            :options="options.abnormalList"
            placeholder="请选择"
          />
          <SkSelect
            v-model="formData.solve"
            label="处理状态"
            :options="options.solveList"
            placeholder="请选择"
          />
          <view>
            <text class="text-sm font-medium text-gray-700 mb-2 block">巡检情况</text>
            <textarea
              v-model="formData.situation"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-base text-gray-900 bg-gray-50 focus:bg-white focus:border-primary"
              placeholder="请输入巡检情况描述（最多255字）"
              maxlength="255"
              :rows="4"
            />
            <text class="text-xs text-gray-400 text-right block mt-1">
              {{ (formData.situation && formData.situation.length) || 0 }}/255
            </text>
          </view>
        </view>
      </SkCard>

      <!-- 现场图片 -->
      <SkCard title="现场图片">
        <view class="py-2">
          <SkImageUpload v-model="imageList" :max="6" />
        </view>
      </SkCard>
    </view>

    <!-- 底部按钮 -->
    <view class="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-100 p-4 flex gap-3">
      <SkButton text="取消" type="default" size="large" class="flex-1" @click="handleCancel" />
      <SkButton text="保存" type="primary" size="large" class="flex-1" :loading="saving" @click="handleSubmit" />
    </view>

    <!-- 底部按钮占位 -->
    <view class="h-20"></view>
  </view>
</template>

<script setup>
/**
 * 巡检记录表单页
 * 功能：新增/编辑巡检记录
 */
import { ref, reactive } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { formatDate } from '@/utils/format.js'
import { useInspection } from '@/composables/useInspection.js'
import SkCard from '@/components/common/SkCard.vue'
import SkInput from '@/components/common/SkInput.vue'
import SkSelect from '@/components/common/SkSelect.vue'
import SkButton from '@/components/common/SkButton.vue'
import SkImageUpload from '@/components/common/SkImageUpload.vue'

const { getDetail, saveData, options } = useInspection()

const isEdit = ref(false)
const saving = ref(false)
const imageList = ref([])

const formData = reactive({
  id: null,
  project: '',
  type: '',
  person: '',
  date: '',
  longitude: '',
  latitude: '',
  abnormal: '正常',
  solve: '未处理',
  situation: '',
  image: '',
})

onLoad(async (query) => {
  if (query?.id) {
    isEdit.value = true
    uni.setNavigationBarTitle({ title: '编辑巡检记录' })
    await loadDetail(query.id)
  } else {
    uni.setNavigationBarTitle({ title: '新增巡检记录' })
    formData.date = formatDate(new Date(), 'YYYY-MM-DD')
  }
})



const loadDetail = async (id) => {
  const data = await getDetail(id)
  if (data) {
    Object.assign(formData, data)
    if (data.longitude != null) formData.longitude = String(data.longitude)
    if (data.latitude != null) formData.latitude = String(data.latitude)
    if (data.image) {
      imageList.value = data.image.split(';').filter((img) => img.trim())
    }
  }
}

const handleGetLocation = () => {
  uni.showLoading({ title: '获取位置中...' })
  uni.getLocation({
    type: 'gcj02',
    success: (res) => {
      formData.longitude = res.longitude.toFixed(6)
      formData.latitude = res.latitude.toFixed(6)
      uni.showToast({ title: '获取成功', icon: 'success' })
    },
    fail: () => {
      uni.showToast({ title: '获取位置失败', icon: 'none' })
    },
    complete: () => {
      uni.hideLoading()
    },
  })
}

const validate = () => {
  if (!formData.project) {
    uni.showToast({ title: '请选择巡检站点', icon: 'none' })
    return false
  }
  if (!formData.type) {
    uni.showToast({ title: '请选择巡检类型', icon: 'none' })
    return false
  }
  if (!formData.person) {
    uni.showToast({ title: '请输入负责人', icon: 'none' })
    return false
  }
  if (!formData.date) {
    uni.showToast({ title: '请选择巡检日期', icon: 'none' })
    return false
  }
  return true
}

const handleSubmit = async () => {
  if (!validate()) return

  saving.value = true
  try {
    formData.image = imageList.value.join(';')
    const success = await saveData({ ...formData })
    if (success) {
      uni.navigateBack()
    }
  } finally {
    saving.value = false
  }
}

const handleCancel = () => {
  uni.navigateBack()
}
</script>

<style scoped>
textarea {
  resize: none;
}
</style>
