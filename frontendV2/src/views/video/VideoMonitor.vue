<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900 tracking-tight">视频实时监测</h1>
      <p class="mt-1 text-sm text-gray-500">实时查看监控视频，支持多窗口预览和云台控制</p>
    </header>

    <!-- 公网环境提示 -->
    <div v-if="!isIntranet" class="mb-6">
      <Card variant="default" shadow="sm" rounded="xl" padding="lg">
        <div class="flex flex-col items-center justify-center py-12 text-center">
          <i class="fa fa-exclamation-triangle text-5xl text-amber-500 mb-4" aria-hidden="true"></i>
          <h2 class="text-xl font-bold text-gray-800 mb-2">视频监控仅支持内网访问</h2>
          <p class="text-gray-500 max-w-md">
            由于网络安全限制，视频监控功能需要在内网环境下使用。<br>
            请连接到内网（192.168.20.x 网段）后访问本页面。
          </p>
        </div>
      </Card>
    </div>

    <!-- 内网环境：显示视频监控功能 -->
    <div v-else class="flex flex-col xl:flex-row gap-6">
      <!-- 左侧：视频播放区 -->
      <div class="flex-1">
        <Card variant="default" shadow="sm" rounded="xl" padding="none" class="overflow-hidden">
          <!-- 视频区域头部 -->
          <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/30">
            <div class="flex items-center gap-2">
              <i class="fa fa-video-camera text-primary-500" aria-hidden="true"></i>
              <h2 class="text-base font-bold text-gray-800">实时预览</h2>
            </div>
            <span :class="['text-sm px-2 py-1 rounded', isLoggedIn ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500']">
              {{ isLoggedIn ? '已连接' : '未连接' }}
            </span>
          </div>
          <!-- 视频插件容器 -->
          <div class="relative bg-black aspect-video">
            <div id="divPlugin" class="w-full h-full"></div>
            <!-- 未连接提示 -->
            <div v-if="!isLoggedIn" class="absolute inset-0 flex items-center justify-center bg-gray-900/80">
              <div class="text-center text-gray-400">
                <i class="fa fa-video-camera text-4xl mb-3" aria-hidden="true"></i>
                <p>请先登录设备开始预览</p>
              </div>
            </div>
          </div>
        </Card>
      </div>

      <!-- 右侧：控制面板 -->
      <div class="w-full xl:w-80 space-y-6">
        <!-- 设备控制卡片 -->
        <Card variant="default" shadow="sm" rounded="xl" padding="md">
          <div class="flex items-center gap-2 mb-4">
            <i class="fa fa-cog text-primary-500" aria-hidden="true"></i>
            <h3 class="text-base font-bold text-gray-800">设备控制</h3>
          </div>
          
          <!-- 窗口分割 -->
          <div class="mb-4">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2 block">窗口分割</label>
            <select 
              v-model="wndNum" 
              @change="changeWndNum"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            >
              <option v-for="layout in windowLayouts" :key="layout.value" :value="layout.value">
                {{ layout.label }}
              </option>
            </select>
          </div>

          <!-- 通道选择 -->
          <div class="mb-4">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2 block">通道选择</label>
            <select 
              v-model="selectedChannel" 
              @change="onChannelChange"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            >
              <option value="">请选择通道</option>
              <option v-for="channel in channels" :key="channel.id" :value="channel.id">
                {{ channel.name }} (ID: {{ channel.id }})
              </option>
            </select>
          </div>

          <!-- 码流类型 -->
          <div class="mb-4">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2 block">码流类型</label>
            <select 
              v-model="streamType"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            >
              <option v-for="type in streamTypes" :key="type.value" :value="type.value">
                {{ type.label }}
              </option>
            </select>
          </div>

          <!-- 登录/退出按钮 -->
          <div class="flex gap-2 mb-4">
            <Button type="primary" class="flex-1" :loading="loginLoading" @click="handleLogin">
              {{ isLoggedIn ? '重新登录' : '登录设备' }}
            </Button>
            <Button type="default" class="flex-1" :disabled="!isLoggedIn" @click="handleLogout">
              退出
            </Button>
          </div>

          <!-- 预览控制按钮 -->
          <div class="flex gap-2">
            <Button type="success" class="flex-1" :disabled="!isLoggedIn" @click="startPreview">
              开始预览
            </Button>
            <Button type="danger" class="flex-1" :disabled="!isLoggedIn" @click="stopPreview">
              停止预览
            </Button>
          </div>
        </Card>

        <!-- 历史回放卡片 -->
        <Card variant="default" shadow="sm" rounded="xl" padding="md">
          <div class="flex items-center gap-2 mb-4">
            <i class="fa fa-history text-primary-500" aria-hidden="true"></i>
            <h3 class="text-base font-bold text-gray-800">历史回放</h3>
          </div>

          <!-- 开始时间 -->
          <div class="mb-4">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2 block">开始时间</label>
            <Input v-model="playbackStartTime" type="datetime-local" />
          </div>

          <!-- 结束时间 -->
          <div class="mb-4">
            <label class="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2 block">结束时间</label>
            <Input v-model="playbackEndTime" type="datetime-local" />
          </div>

          <!-- 回放控制按钮 -->
          <div class="flex gap-2 mb-3">
            <Button type="primary" class="flex-1" :disabled="!isLoggedIn || !selectedChannel" @click="startPlayback">
              开始回放
            </Button>
            <Button type="danger" class="flex-1" :disabled="!isLoggedIn" @click="stopPlayback">
              停止回放
            </Button>
          </div>
          <div class="flex gap-2">
            <Button type="default" class="flex-1" :disabled="!isLoggedIn" @click="pausePlayback">
              暂停
            </Button>
            <Button type="default" class="flex-1" :disabled="!isLoggedIn" @click="resumePlayback">
              继续
            </Button>
            <Button type="default" class="flex-1" icon="camera" :disabled="!isLoggedIn" @click="captureImage">
              抓图
            </Button>
          </div>
        </Card>

        <!-- 云台控制卡片 -->
        <Card variant="default" shadow="sm" rounded="xl" padding="md">
          <div class="flex items-center gap-2 mb-4">
            <i class="fa fa-arrows text-primary-500" aria-hidden="true"></i>
            <h3 class="text-base font-bold text-gray-800">云台控制</h3>
          </div>

          <!-- 方向控制盘 -->
          <div class="flex justify-center mb-4">
            <div class="relative w-32 h-32">
              <!-- 上 -->
              <button 
                class="absolute top-0 left-1/2 -translate-x-1/2 w-10 h-10 bg-gray-100 hover:bg-primary-100 rounded-full flex items-center justify-center transition-colors cursor-pointer"
                :disabled="!isLoggedIn || !selectedChannel"
                @mousedown="ptzControl(PTZ.UP)" 
                @mouseup="ptzStop(PTZ.UP)" 
                @mouseleave="ptzStop(PTZ.UP)"
              >
                <i class="fa fa-chevron-up text-gray-600" aria-hidden="true"></i>
              </button>
              <!-- 下 -->
              <button 
                class="absolute bottom-0 left-1/2 -translate-x-1/2 w-10 h-10 bg-gray-100 hover:bg-primary-100 rounded-full flex items-center justify-center transition-colors cursor-pointer"
                :disabled="!isLoggedIn || !selectedChannel"
                @mousedown="ptzControl(PTZ.DOWN)" 
                @mouseup="ptzStop(PTZ.DOWN)" 
                @mouseleave="ptzStop(PTZ.DOWN)"
              >
                <i class="fa fa-chevron-down text-gray-600" aria-hidden="true"></i>
              </button>
              <!-- 左 -->
              <button 
                class="absolute left-0 top-1/2 -translate-y-1/2 w-10 h-10 bg-gray-100 hover:bg-primary-100 rounded-full flex items-center justify-center transition-colors cursor-pointer"
                :disabled="!isLoggedIn || !selectedChannel"
                @mousedown="ptzControl(PTZ.LEFT)" 
                @mouseup="ptzStop(PTZ.LEFT)" 
                @mouseleave="ptzStop(PTZ.LEFT)"
              >
                <i class="fa fa-chevron-left text-gray-600" aria-hidden="true"></i>
              </button>
              <!-- 右 -->
              <button 
                class="absolute right-0 top-1/2 -translate-y-1/2 w-10 h-10 bg-gray-100 hover:bg-primary-100 rounded-full flex items-center justify-center transition-colors cursor-pointer"
                :disabled="!isLoggedIn || !selectedChannel"
                @mousedown="ptzControl(PTZ.RIGHT)" 
                @mouseup="ptzStop(PTZ.RIGHT)" 
                @mouseleave="ptzStop(PTZ.RIGHT)"
              >
                <i class="fa fa-chevron-right text-gray-600" aria-hidden="true"></i>
              </button>
              <!-- 中心圆 -->
              <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-12 h-12 bg-gray-200 rounded-full"></div>
            </div>
          </div>

          <!-- 变焦控制 -->
          <div class="flex gap-3 justify-center">
            <button 
              class="flex items-center gap-2 px-4 py-2 bg-gray-100 hover:bg-primary-100 rounded-lg transition-colors cursor-pointer"
              :disabled="!isLoggedIn || !selectedChannel"
              @mousedown="ptzControl(PTZ.ZOOM_IN)" 
              @mouseup="ptzStop(PTZ.ZOOM_IN)" 
              @mouseleave="ptzStop(PTZ.ZOOM_IN)"
            >
              <i class="fa fa-search-plus text-gray-600" aria-hidden="true"></i>
              <span class="text-sm text-gray-600">放大</span>
            </button>
            <button 
              class="flex items-center gap-2 px-4 py-2 bg-gray-100 hover:bg-primary-100 rounded-lg transition-colors cursor-pointer"
              :disabled="!isLoggedIn || !selectedChannel"
              @mousedown="ptzControl(PTZ.ZOOM_OUT)" 
              @mouseup="ptzStop(PTZ.ZOOM_OUT)" 
              @mouseleave="ptzStop(PTZ.ZOOM_OUT)"
            >
              <i class="fa fa-search-minus text-gray-600" aria-hidden="true"></i>
              <span class="text-sm text-gray-600">缩小</span>
            </button>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>


<script setup>
/**
 * 视频实时监测页面
 * 功能：海康SDK视频预览、历史回放、云台控制
 * 依赖：海康 WebVideoCtrl SDK
 */
import { ref, onMounted, onBeforeUnmount } from 'vue'

// 组件导入
import Button from '@/components/basic/Button.vue'
import Card from '@/components/basic/Card.vue'
import Input from '@/components/basic/Input.vue'

// 配置导入
import { 
  getVideoConfig, 
  isIntranetEnvironment,
  WINDOW_LAYOUTS, 
  STREAM_TYPES, 
  PTZ_DIRECTIONS 
} from '@/config/video'

// ==================== 常量定义 ====================
const PTZ = PTZ_DIRECTIONS
const windowLayouts = WINDOW_LAYOUTS

const streamTypes = STREAM_TYPES
let resizeObserver = null // ResizeObserver 实例

// ==================== 状态定义 ====================
// 网络环境检测
const isIntranet = ref(isIntranetEnvironment())

// 设备连接状态
const isLoggedIn = ref(false)
const loginLoading = ref(false)
const deviceIdentify = ref('')
const isPluginReady = ref(false) // 插件初始化完成标记

// 视频参数
const wndNum = ref(2)
const channels = ref([])
const selectedChannel = ref('')
const streamType = ref(1)

// 回放参数
const playbackStartTime = ref('')
const playbackEndTime = ref('')

// ==================== 方法定义 ====================

/**
 * 初始化SDK插件
 * 注意：I_InitPlugin 是纯回调式API，不返回Promise
 */
const initPlugin = () => {
  return new Promise((resolve, reject) => {
    // 检查 WebVideoCtrl 是否已加载
    if (typeof WebVideoCtrl === 'undefined') {
      console.error('WebVideoCtrl SDK 未加载')
      reject(new Error('SDK未加载'))
      return
    }

    // I_InitPlugin 返回 undefined，通过 cbInitPluginComplete 回调处理
    WebVideoCtrl.I_InitPlugin({
      iWndowType: wndNum.value,
      bWndFull: true,
      bDebugMode: false,
      cbInitPluginComplete: () => {
        // I_InsertOBJECTPlugin 返回 Promise，可使用链式调用
        WebVideoCtrl.I_InsertOBJECTPlugin('divPlugin')
          .then(() => {
            console.log('SDK插件初始化完成')
            isPluginReady.value = true
            resolve()
          })
          .catch((err) => {
            console.error('插件嵌入失败:', err?.message || err)
            reject(err)
          })
      },
      cbEvent: (iEventType, iParam1) => {
        // 异常事件处理
        if (iEventType === -1) {
          console.log(`设备${iParam1}网络错误`)
        } else if (iEventType === 2) {
          console.log(`窗口${iParam1}回放结束`)
        }
      },
      cbSelWnd: (xmlDoc) => {
        const wndIndex = xmlDoc.querySelector('SelectWnd')?.textContent
        console.log(`选中窗口: ${wndIndex}`)
      }
    })
  })
}


/**
 * 登录设备
 */
const handleLogin = async () => {
  const config = getVideoConfig()
  
  // 公网环境不支持登录
  if (!config) {
    console.error('当前为公网环境，不支持视频监控')
    return
  }
  
  const szDeviceIdentify = `${config.loginIp}_${config.port}`

  // 检查是否已登录
  if (isLoggedIn.value && deviceIdentify.value === szDeviceIdentify) {
    console.log('设备已登录，获取通道信息')
    getChannels()
    return
  }

  loginLoading.value = true

  try {
    await new Promise((resolve, reject) => {
      WebVideoCtrl.I_Login(config.loginIp, 1, config.port, config.username, config.password, {
        success: () => {
          console.log(`设备${szDeviceIdentify}登录成功`)
          deviceIdentify.value = szDeviceIdentify
          isLoggedIn.value = true
          // 延迟调用，避免回调返回值被 SDK 内部 JSON.parse
          setTimeout(() => getChannels(), 0)
          resolve()
        },
        error: (oError) => {
          // 错误码 2001 表示设备已登录
          if (oError.errorCode === 2001) {
            console.log('设备已登录，更新状态')
            deviceIdentify.value = szDeviceIdentify
            isLoggedIn.value = true
            setTimeout(() => getChannels(), 0)
            resolve()
          } else {
            console.error(`登录失败[${oError.errorCode}]: ${oError.errorString}`)
            reject(oError)
          }
        }
      })
    })
  } catch (err) {
    console.error('登录异常:', err)
  } finally {
    loginLoading.value = false
  }
}

/**
 * 退出登录
 */
const handleLogout = async () => {
  if (!deviceIdentify.value) return

  try {
    await WebVideoCtrl.I_Logout(deviceIdentify.value)
    console.log('设备退出成功')
    
    // 重置状态
    deviceIdentify.value = ''
    isLoggedIn.value = false
    channels.value = []
    selectedChannel.value = ''
    
    // 停止所有播放
    await WebVideoCtrl.I_StopAllPlay()
  } catch (err) {
    console.error('退出失败:', err)
  }
}

/**
 * 获取通道信息
 */
const getChannels = () => {
  if (!deviceIdentify.value) return

  // 获取模拟通道
  // 注意：回调中不返回任何值，避免 SDK 内部误将返回值进行 JSON.parse
  WebVideoCtrl.I_GetAnalogChannelInfo(deviceIdentify.value, {
    success: (xmlDoc) => {
      if (xmlDoc) parseChannelXml(xmlDoc, false)
    },
    error: (err) => console.log('获取模拟通道失败:', err.errorCode)
  })

  // 获取数字通道
  WebVideoCtrl.I_GetDigitalChannelInfo(deviceIdentify.value, {
    success: (xmlDoc) => {
      if (xmlDoc) parseChannelXml(xmlDoc, true)
    },
    error: (err) => console.log('获取数字通道失败:', err.errorCode)
  })
}

/**
 * 解析通道XML数据
 */
const parseChannelXml = (xmlDoc, isDigital) => {
  // 防御性校验：避免 SDK 回调传入 undefined 导致 JSON.parse 报错
  if (!xmlDoc) {
    console.warn('parseChannelXml: xmlDoc 为空，跳过解析')
    return
  }
  
  let doc = typeof xmlDoc === 'string' 
    ? new DOMParser().parseFromString(xmlDoc, 'application/xml') 
    : xmlDoc

  const nodes = isDigital
    ? doc.querySelectorAll('InputProxyChannelStatus')
    : doc.querySelectorAll('VideoInputChannel')

  nodes.forEach((node) => {
    const id = node.querySelector('id')?.textContent
    const name = node.querySelector('name')?.textContent || `通道${id}`
    
    // 去重添加
    if (id && !channels.value.some((c) => c.id === id)) {
      channels.value.push({ id, name })
    }
  })

  // 按ID排序
  channels.value.sort((a, b) => parseInt(a.id) - parseInt(b.id))
  console.log(`共获取${channels.value.length}个通道`)

  // 自动开始多窗口预览
  if (channels.value.length > 0) {
    setTimeout(() => startMultiWindowPreview(), 1000)
  }
}

/**
 * 切换窗口分割
 */
const changeWndNum = () => {
  WebVideoCtrl.I_ChangeWndNum(wndNum.value)
    .then(() => {
      console.log(`窗口分割为${wndNum.value}x${wndNum.value}`)
      if (isLoggedIn.value && channels.value.length > 0) {
        setTimeout(() => startMultiWindowPreview(), 500)
      }
    })
    .catch((err) => console.error('窗口分割失败:', err))
}

/**
 * 多窗口预览
 */
const startMultiWindowPreview = async () => {
  if (!deviceIdentify.value || channels.value.length === 0) return

  const maxWindows = wndNum.value * wndNum.value
  const channelsToShow = channels.value.slice(0, maxWindows)

  console.log(`开始${wndNum.value}x${wndNum.value}多窗口预览`)

  try {
    await WebVideoCtrl.I_StopAllPlay()
    
    channelsToShow.forEach((channel, index) => {
      setTimeout(() => startChannelPreview(channel.id, index), index * 200)
    })
  } catch (err) {
    console.error('停止播放失败:', err)
  }
}

/**
 * 启动单个通道预览
 */
const startChannelPreview = (channelId, windowIndex) => {
  WebVideoCtrl.I_StartRealPlay(deviceIdentify.value, {
    iWndIndex: windowIndex,
    iChannelID: parseInt(channelId),
    iStreamType: streamType.value,
    success: () => console.log(`通道${channelId}在窗口${windowIndex}开始预览`),
    error: (err) => console.log(`通道${channelId}预览失败:`, err.errorCode)
  })
}

/**
 * 开始预览（当前选中通道）
 */
const startPreview = () => {
  if (!deviceIdentify.value || !selectedChannel.value) {
    console.log('请先选择通道')
    return
  }

  WebVideoCtrl.I_StartRealPlay(deviceIdentify.value, {
    iWndIndex: 0,
    iChannelID: parseInt(selectedChannel.value),
    iStreamType: streamType.value,
    success: () => {
      console.log(`通道${selectedChannel.value}预览开始`)
      WebVideoCtrl.I_OpenSound(0).catch(() => {})
    },
    error: (err) => console.error('预览失败:', err)
  })
}

/**
 * 停止预览
 */
const stopPreview = () => {
  WebVideoCtrl.I_Stop({ iWndIndex: 0 })
    .then(() => console.log('预览已停止'))
    .catch((err) => console.error('停止预览失败:', err))
}

/**
 * 通道切换处理
 */
const onChannelChange = () => {
  if (selectedChannel.value) {
    console.log(`已选择通道${selectedChannel.value}`)
  }
}

/**
 * 开始回放
 */
const startPlayback = async () => {
  if (!deviceIdentify.value || !selectedChannel.value) {
    console.log('请先选择通道')
    return
  }

  if (!playbackStartTime.value || !playbackEndTime.value) {
    console.log('请选择时间范围')
    return
  }

  // 格式化时间
  const formatTime = (dateStr) => {
    const date = new Date(dateStr)
    const pad = (n) => String(n).padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
  }

  const startTime = formatTime(playbackStartTime.value)
  const endTime = formatTime(playbackEndTime.value)

  // 先停止当前预览
  stopPreview()

  setTimeout(() => {
    WebVideoCtrl.I_StartPlayback(deviceIdentify.value, {
      iWndIndex: 0,
      iChannelID: parseInt(selectedChannel.value),
      szStartTime: startTime,
      szEndTime: endTime,
      iStreamType: streamType.value,
      success: () => {
        console.log(`开始回放: ${startTime} 至 ${endTime}`)
        WebVideoCtrl.I_OpenSound(0).catch(() => {})
      },
      error: (err) => console.error('回放失败:', err)
    })
  }, 500)
}

/**
 * 停止回放
 */
const stopPlayback = () => {
  WebVideoCtrl.I_Stop({ iWndIndex: 0 })
    .then(() => console.log('回放已停止'))
    .catch((err) => console.error('停止回放失败:', err))
}

/**
 * 暂停回放
 */
const pausePlayback = () => {
  WebVideoCtrl.I_Pause({ iWndIndex: 0 })
    .then(() => console.log('回放已暂停'))
    .catch((err) => console.error('暂停失败:', err))
}

/**
 * 继续回放
 */
const resumePlayback = () => {
  WebVideoCtrl.I_Resume({ iWndIndex: 0 })
    .then(() => console.log('回放已恢复'))
    .catch((err) => console.error('恢复失败:', err))
}

/**
 * 抓图
 */
const captureImage = () => {
  const picName = `capture_${Date.now()}`
  WebVideoCtrl.I_CapturePic(picName, { iWndIndex: 0 })
    .then(() => console.log(`抓图成功: ${picName}`))
    .catch((err) => console.error('抓图失败:', err))
}

/**
 * 云台控制
 */
const ptzControl = (direction) => {
  if (!deviceIdentify.value || !selectedChannel.value) return

  WebVideoCtrl.I_PTZControl(direction, false, {
    iWndIndex: 0,
    iPTZSpeed: 4
  }).catch((err) => console.error('云台操作失败:', err))
}

/**
 * 停止云台操作
 */
const ptzStop = (direction) => {
  if (!deviceIdentify.value) return

  WebVideoCtrl.I_PTZControl(direction, true, {
    iWndIndex: 0
  }).catch(() => {})
}

/**
 * 初始化默认回放时间（最近1小时）
 */
const initPlaybackTime = () => {
  const now = new Date()
  const oneHourAgo = new Date(now.getTime() - 60 * 60 * 1000)
  
  const formatForInput = (date) => {
    const pad = (n) => String(n).padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`
  }
  
  playbackStartTime.value = formatForInput(oneHourAgo)
  playbackEndTime.value = formatForInput(now)
}

/**
 * 调整插件大小
 * 解决插件不随窗口缩放的问题（因SDK内部依赖jQuery未引入导致自动缩放失效）
 */
const resizePlugin = () => {
  if (typeof WebVideoCtrl === 'undefined' || !isPluginReady.value) return
  
  const el = document.getElementById('divPlugin')
  if (el) {
    const width = el.clientWidth
    const height = el.clientHeight
    // 只有当尺寸有效时才调整
    if (width > 0 && height > 0) {
      WebVideoCtrl.I_Resize(width, height)
    }
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  initPlaybackTime()
  
  // 公网环境不初始化插件
  if (!isIntranet.value) {
    console.log('公网环境，跳过视频插件初始化')
    return
  }
  
  // 延迟初始化插件，确保DOM已渲染
  setTimeout(() => {
    initPlugin()
      .then(() => {
        console.log('插件初始化成功')
        // 初始化后强制调整一次大小
        resizePlugin()
      })
      .catch((err) => console.error('插件初始化失败:', err))
  }, 500)
  
  // 1. 监听容器大小变化 (覆盖 resize 和布局变化)
  // 当侧边栏收缩或窗口变化导致容器尺寸变动时，ResizeObserver 会触发
  const el = document.getElementById('divPlugin')
  if (el) {
    resizeObserver = new ResizeObserver(() => {
       resizePlugin()
    })
    resizeObserver.observe(el)
  }
  
  // 2. 监听滚动事件 (重要：解决滚动时插件脱离原位的问题)
  // 插件通常是绝对定位覆盖在页面上的，页面滚动改变了DOM位置，必须通知插件更新
  // 使用 capture: true 捕获所有可能的滚动容器事件
  window.addEventListener('scroll', resizePlugin, true)

  // 3. 监听窗口 resize (作为兜底)
  window.addEventListener('resize', resizePlugin)
})

onBeforeUnmount(() => {
  isPluginReady.value = false
  // 清理监听器
  if (resizeObserver) {
    resizeObserver.disconnect()
    resizeObserver = null
  }
  window.removeEventListener('scroll', resizePlugin, true)
  window.removeEventListener('resize', resizePlugin)

  // 清理：停止所有播放并销毁插件
  if (typeof WebVideoCtrl !== 'undefined') {
    WebVideoCtrl.I_StopAllPlay?.().catch(() => {})
    WebVideoCtrl.I_DestroyPlugin?.().catch(() => {})
  }
})
</script>