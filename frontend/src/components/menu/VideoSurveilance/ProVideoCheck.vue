<template>
  <div class="provideo-container">
    <!-- 视频预览区域 -->
    <div class="provideo-left">
      <div id="divPlugin" class="provideo-video-plugin"></div>
    </div>

    <!-- 右侧控制区域 -->
    <div class="provideo-right">
      <!-- 设备登录与基础控制 -->
      <div class="provideo-card">
        <div class="provideo-card-title">
          <i class="el-icon-video-camera"></i> 设备控制
        </div>
        <!-- 隐藏设备IP和端口输入框 -->
        <!-- <div class="provideo-form-row">
          <label>设备IP：</label>
          <input 
            type="text" 
            v-model="loginIp" 
            placeholder="输入设备IP"
            class="provideo-input"
          >
        </div>
        <div class="provideo-form-row">
          <label>端口：</label>
          <input 
            type="text" 
            v-model="port" 
            placeholder="默认80"
            class="provideo-input"
          >
        </div> -->
        <div class="provideo-form-row">
          <label>窗口分割：</label>
          <select v-model="wndNum" @change="changeWndNum">
            <option value="1">1x1</option>
            <option value="2">2x2</option>
            <option value="3">3x3</option>
            <option value="4">4x4</option>
          </select>
        </div>
        <div class="provideo-form-row">
          <label>通道：</label>
          <select v-model="selectedChannel" @change="onChannelChange">
            <option value="">请选择通道</option>
            <option 
              v-for="channel in channels" 
              :key="channel.id" 
              :value="channel.id"
            >
              {{ channel.name }} (ID: {{ channel.id }})
            </option>
          </select>
        </div>
        <div class="provideo-form-row">
          <label>码流类型：</label>
          <select v-model="streamType">
            <option value="1">高清</option>
            <option value="2">标清</option>
          </select>
        </div>
        <div class="provideo-btn-row">
          <button class="provideo-btn primary" @click="clickLogin">登录</button>
          <button class="provideo-btn primary" @click="clickLogout">退出</button>
        </div>
        <div class="provideo-btn-row">
          <button class="provideo-btn success" @click="clickStartRealPlay">开始预览</button>
          <button class="provideo-btn danger" @click="clickStopRealPlay">停止预览</button>
        </div>
      </div>

      <!-- 历史视频回看 -->
      <div class="provideo-card">
        <div class="provideo-card-title">
          <i class="el-icon-time"></i> 历史视频回看
        </div>
        <div class="provideo-form-row">
          <label>开始时间：</label>
          <input 
            type="datetime-local" 
            v-model="playbackStartTime" 
            class="provideo-input"
          >
        </div>
        <div class="provideo-form-row">
          <label>结束时间：</label>
          <input 
            type="datetime-local" 
            v-model="playbackEndTime" 
            class="provideo-input"
          >
        </div>
        <div class="provideo-form-row">
          <label>码流类型：</label>
          <select v-model="playbackStreamType">
            <option value="1">高清</option>
            <option value="2">标清</option>
          </select>
        </div>
        <div class="provideo-btn-row">
          <button class="provideo-btn primary" @click="startPlayback">开始回放</button>
          <button class="provideo-btn danger" @click="stopPlayback">停止回放</button>
        </div>
        <div class="provideo-btn-row">
          <button class="provideo-btn" @click="playbackPause">暂停</button>
          <button class="provideo-btn" @click="playbackResume">继续</button>
          <button class="provideo-btn" @click="capturePlaybackPic">抓图</button>
        </div>
      </div>

      <!-- 云台控制 -->
      <div class="provideo-card ptz-card">
        <div class="provideo-card-title">
          <i class="el-icon-s-operation"></i> 云台控制
        </div>
        
        <!-- 隐藏状态显示 -->
        <!-- <div class="ptz-status">
          <div class="status-item">
            <span class="status-label">设备：</span>
            <span class="status-value">{{ selectedDevice || '未连接' }}</span>
          </div>
          <div class="status-item">
            <span class="status-label">通道：</span>
            <span class="status-value">{{ selectedChannel || '未选择' }}</span>
          </div>
        </div> -->

        <!-- 遥控器式控制面板 -->
        <div class="ptz-remote-control">
          <!-- 方向控制盘 -->
          <div class="direction-pad">
            <div class="pad-container">
              <!-- 上 -->
              <button 
                @mousedown="ptzControl(1)" 
                @mouseup="ptzStop(1)" 
                @mouseleave="ptzStop(1)"
                class="direction-btn up-btn"
              >
                <i class="el-icon-arrow-up"></i>
              </button>
              
              <!-- 左 -->
              <button 
                @mousedown="ptzControl(3)" 
                @mouseup="ptzStop(3)" 
                @mouseleave="ptzStop(3)"
                class="direction-btn left-btn"
              >
                <i class="el-icon-arrow-left"></i>
              </button>
              
              <!-- 右 -->
              <button 
                @mousedown="ptzControl(4)" 
                @mouseup="ptzStop(4)" 
                @mouseleave="ptzStop(4)"
                class="direction-btn right-btn"
              >
                <i class="el-icon-arrow-right"></i>
              </button>
              
              <!-- 下 -->
              <button 
                @mousedown="ptzControl(2)" 
                @mouseup="ptzStop(2)" 
                @mouseleave="ptzStop(2)"
                class="direction-btn down-btn"
              >
                <i class="el-icon-arrow-down"></i>
              </button>
            </div>
          </div>

          <!-- 功能按钮区域 -->
          <div class="function-buttons">
            <!-- 变焦控制 -->
            <div class="zoom-section">
              <div class="zoom-btn zoom-in" 
                   @mousedown="ptzControl(10)" 
                   @mouseup="ptzStop(10)" 
                   @mouseleave="ptzStop(10)">
                <i class="el-icon-zoom-in"></i>
                <span>放大</span>
              </div>
              <div class="zoom-btn zoom-out"
                   @mousedown="ptzControl(11)" 
                   @mouseup="ptzStop(11)" 
                   @mouseleave="ptzStop(11)">
                <i class="el-icon-zoom-out"></i>
                <span>缩小</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getVideoApiConfig } from '../../../config/api'

export default {
  data() {
    const videoConfig = getVideoApiConfig();
    return {
      // 设备连接参数
      loginIp: videoConfig.loginIp,
      port: videoConfig.port,
      username: 'admin',
      password: 'wx147369',
      selectedDevice: '', // 设备标识（IP_Port）
      
      // 视频参数
      wndNum: '2', // 默认2x2窗口
      channels: [], // 通道列表
      selectedChannel: '', // 当前选中通道
      streamType: '1', // 码流类型：1-主码流，2-子码流
      
      // 历史视频回放参数
      playbackStartTime: '',
      playbackEndTime: '',
      playbackStreamType: '1', // 1-主码流，2-子码流
    };
  },

  methods: {
    // ============== 插件初始化（文档5.1节） ==============
    initPlugin() {
      return new Promise((resolve, reject) => {
        WebVideoCtrl.I_InitPlugin({
          iWndowType: parseInt(this.wndNum), // 窗口分割类型
          bWndFull: true, // 支持双击全屏
          bDebugMode: false, // 关闭调试模式
          cbInitPluginComplete: () => {
            // 嵌入插件到DOM（文档5.1.2节）
            WebVideoCtrl.I_InsertOBJECTPlugin('divPlugin').then(() => {
              // 检查插件版本（文档5.10.1节）
              WebVideoCtrl.I_CheckPluginVersion().then((bNeedUpdate) => {
                if (bNeedUpdate) {
                  console.log('检测到新插件版本，请升级');
                }
                console.log('插件初始化完成');
                resolve();
              }).catch(err => {
                console.log(`版本检查失败: ${err.message}`);
                resolve(); // 版本检查失败不阻断初始化
              });
            }).catch(err => {
              console.log(`插件嵌入失败: ${err.message}`);
              reject(err);
            });
          },
          cbEvent: (iEventType, iParam1) => {
            // 异常事件处理（文档5.1.1节）
            if (iEventType === -1) {
              console.log(`设备${iParam1}网络错误`);
            } else if (iEventType === 2) {
              console.log(`窗口${iParam1}回放结束`);
            }
          },
          cbSelWnd: (xmlDoc) => {
            // 窗口选择回调（文档5.1.1节）
            const wndIndex = xmlDoc.querySelector('SelectWnd').textContent;
            console.log(`选中窗口: ${wndIndex}`);
          }
        }).catch(err => {
          console.log(`插件初始化失败: ${err.message}`);
          reject(err);
        });
      });
    },

    // ============== 设备登录（文档5.2.1节） ==============
    clickLogin() {
      const szIP = this.loginIp;
      const iPort = parseInt(this.port);
      const szUsername = this.username;
      const szPassword = this.password;

      if (!szIP || !iPort) {
        console.log('IP和端口不能为空');
        return;
      }

      const szDeviceIdentify = `${szIP}_${iPort}`;

      // 检查设备是否已经登录
      if (this.selectedDevice === szDeviceIdentify) {
        console.log(`设备${szDeviceIdentify}已经登录，无需重复登录`);
        // 如果已登录但通道列表为空，重新获取通道
        if (this.channels.length === 0) {
          this.getChannels();
        }
        return;
      }

      WebVideoCtrl.I_Login(szIP, 1, iPort, szUsername, szPassword, {
        success: (xmlDoc) => {
          console.log(`设备${szDeviceIdentify}登录成功`);
          this.selectedDevice = szDeviceIdentify;
          // 获取设备端口信息（文档5.2.9节）
          WebVideoCtrl.I_GetDevicePort(szDeviceIdentify).then(portInfo => {
            console.log(`设备端口: HTTP=${portInfo.iHttpPort}, RTSP=${portInfo.iRtspPort}`);
          }).catch(err => {
            console.log(`获取端口信息失败: ${err.message}`);
          });
          // 获取通道信息
          this.getChannels();
        },
        error: (oError) => {
          console.log(`登录失败[${oError.errorCode}]: ${oError.errorString}`);
          // 如果是设备已登录错误，更新状态
          if (oError.errorCode === 2001) {
            console.log('设备已登录，更新状态');
            this.selectedDevice = szDeviceIdentify;
            this.getChannels();
          }
        }
      });
    },

    // ============== 设备退出（文档5.2.2节） ==============
    clickLogout() {
      if (!this.selectedDevice) {
        console.log('未登录设备');
        return;
      }

      WebVideoCtrl.I_Logout(this.selectedDevice).then(() => {
        console.log(`设备${this.selectedDevice}退出成功`);
        this.selectedDevice = '';
        this.channels = [];
        this.selectedChannel = '';
        // 停止所有播放（文档5.3.6节）
        WebVideoCtrl.I_StopAllPlay().then(() => {
          console.log('所有播放已停止');
        }).catch(err => {
          console.log(`停止播放失败: ${err.message}`);
        });
      }).catch(oError => {
        console.log(`退出失败[${oError.errorCode}]: ${oError.errorString}`);
      });
    },

    // ============== 获取通道信息（文档5.2.4/5.2.5节） ==============
    getChannels() {
      if (!this.selectedDevice) return;

      // 获取模拟通道
      WebVideoCtrl.I_GetAnalogChannelInfo(this.selectedDevice, {
        success: (xmlDoc) => {
          this.parseChannelXml(xmlDoc, false);
        },
        error: (oError) => {
          console.log(`获取模拟通道失败[${oError.errorCode}]`);
        }
      });

      // 获取数字通道
      WebVideoCtrl.I_GetDigitalChannelInfo(this.selectedDevice, {
        success: (xmlDoc) => {
          this.parseChannelXml(xmlDoc, true);
        },
        error: (oError) => {
          console.log(`获取数字通道失败[${oError.errorCode}]`);
        }
      });
    },

    // 解析通道XML数据
    parseChannelXml(xmlDoc, isDigital) {
      let doc;
      if (typeof xmlDoc === 'string') {
        doc = new DOMParser().parseFromString(xmlDoc, 'application/xml');
      } else {
        doc = xmlDoc;
      }

      const nodes = isDigital 
        ? doc.querySelectorAll('InputProxyChannelStatus')
        : doc.querySelectorAll('VideoInputChannel');

      nodes.forEach(node => {
        const id = node.querySelector('id').textContent;
        const nameNode = node.querySelector('name');
        const name = nameNode.textContent || '通道' + id;
        // 去重添加
        if (!this.channels.some(c => c.id === id)) {
          this.channels.push({ id, name });
        }
      });

      // 按ID排序
      this.channels.sort((a, b) => parseInt(a.id) - parseInt(b.id));
      console.log(`共获取${this.channels.length}个通道`);
      
      // 自动开始多窗口预览
      if (this.channels.length > 0) {
        setTimeout(() => {
          this.startMultiWindowPreview();
        }, 1000);
      }
    },

    // ============== 窗口分割（文档5.3.18节） ==============
    changeWndNum() {
      const iWndType = parseInt(this.wndNum);
      WebVideoCtrl.I_ChangeWndNum(iWndType).then(() => {
        console.log(`窗口分割为${iWndType}x${iWndType}`);
        // 窗口分割后重新开始预览
        if (this.selectedDevice && this.channels.length > 0) {
          setTimeout(() => {
            this.startMultiWindowPreview();
          }, 500);
        }
      }).catch(oError => {
        console.log(`窗口分割失败[${oError.errorCode}]`);
      });
    },

    // ============== 多窗口预览 ==============
    startMultiWindowPreview() {
      if (!this.selectedDevice || this.channels.length === 0) {
        console.log('设备未登录或没有可用通道');
        return;
      }

      const maxWindows = parseInt(this.wndNum) * parseInt(this.wndNum);
      const channelsToShow = this.channels.slice(0, maxWindows);

      console.log(`开始${this.wndNum}x${this.wndNum}多窗口预览，显示${channelsToShow.length}个通道`);

      // 停止所有播放
      WebVideoCtrl.I_StopAllPlay().then(() => {
        // 逐个启动通道预览
        channelsToShow.forEach((channel, index) => {
          setTimeout(() => {
            this.startChannelPreview(channel.id, index);
          }, index * 200); // 每个通道间隔200ms启动
        });
      }).catch(err => {
        console.log(`停止播放失败: ${err.message}`);
      });
    },

    // 启动单个通道预览
    startChannelPreview(channelId, windowIndex) {
      WebVideoCtrl.I_StartRealPlay(this.selectedDevice, {
        iWndIndex: windowIndex,
        iChannelID: parseInt(channelId),
        iStreamType: parseInt(this.streamType),
        success: () => {
          console.log(`通道${channelId}在窗口${windowIndex}开始预览，码流类型: ${this.streamType === '1' ? '高清' : '标清'}`);
        },
        error: (oError) => {
          console.log(`通道${channelId}在窗口${windowIndex}预览失败[${oError.errorCode}]`);
        }
      });
    },

    // ============== 开始预览（文档5.3.2节） ==============
    clickStartRealPlay() {
      if (!this.selectedDevice || !this.selectedChannel) {
        console.log('请先选择设备和通道');
        return;
      }

      WebVideoCtrl.I_StartRealPlay(this.selectedDevice, {
        iWndIndex: 0, // 窗口索引
        iChannelID: parseInt(this.selectedChannel), // 通道ID
        iStreamType: parseInt(this.streamType), // 使用选择的码流类型
        success: () => {
          console.log(`通道${this.selectedChannel}预览开始，码流类型: ${this.streamType === '1' ? '高清' : '标清'}`);
          // 打开声音（文档5.3.13节）
          WebVideoCtrl.I_OpenSound(0).catch(err => {
            console.log(`开启声音失败: ${err.message}`);
          });
        },
        error: (oError) => {
          console.log(`预览失败[${oError.errorCode}]: ${oError.errorString}`);
        }
      });
    },

    // ============== 停止预览（文档5.3.5节） ==============
    clickStopRealPlay() {
      WebVideoCtrl.I_Stop({ iWndIndex: 0 }).then(() => {
        console.log('预览已停止');
      }).catch(oError => {
        console.log(`停止预览失败[${oError.errorCode}]`);
      });
    },

    // ============== 云台控制（文档5.7节） ==============
    /**
     * 云台操作
     * @param {Number} iPTZIndex 操作类型（1-上，2-下，3-左，4-右，10-变焦+，11-变焦-）
     */
    ptzControl(iPTZIndex) {
      if (!this.selectedDevice || !this.selectedChannel) {
        console.log('请先选择设备和通道');
        return;
      }

      WebVideoCtrl.I_PTZControl(iPTZIndex, false, {
        iWndIndex: 0,
        iPTZSpeed: 4 // 速度（1-8，默认4）
      }).catch(oError => {
        console.log(`云台操作失败[${oError.errorCode}]`);
      });
    },

    /**
     * 停止云台操作
     * @param {Number} iPTZIndex 对应操作类型
     */
    ptzStop(iPTZIndex) {
      if (!this.selectedDevice) return;

      WebVideoCtrl.I_PTZControl(iPTZIndex, true, {
        iWndIndex: 0
      }).catch(oError => {
        console.error('停止云台操作失败', oError);
      });
    },

    // ============== 开始回放（文档5.3.3节） ==============
    async startPlayback() {
      if (!this.selectedDevice || !this.selectedChannel) {
        console.log('请先选择设备和通道');
        return;
      }

      // 检查设备是否支持回放
      const supportsPlayback = await this.checkPlaybackSupport();
      if (!supportsPlayback) {
        console.log('设备可能不支持回放功能，但仍将尝试');
      }

      // 获取RTSP端口（文档5.2.9节）
      let rtspPort;
      try {
        const portInfo = await WebVideoCtrl.I_GetDevicePort(this.selectedDevice);
        rtspPort = portInfo.iRtspPort;
      } catch (err) {
        console.log('获取RTSP端口失败，使用默认值554');
        rtspPort = 554;
      }

      // 检查时间格式
      if (!this.playbackStartTime || !this.playbackEndTime) {
        console.log('请选择开始时间和结束时间');
        return;
      }

      // 检查时间有效性
      const startDate = new Date(this.playbackStartTime);
      const endDate = new Date(this.playbackEndTime);
      const now = new Date();
      
      if (startDate >= endDate) {
        console.log('开始时间必须早于结束时间');
        return;
      }
      
      if (endDate > now) {
        console.log('结束时间不能晚于当前时间');
        return;
      }

      // 格式化时间为插件要求的格式（文档5.3.3节）
      const formatTime = (dateString) => {
        try {
          const date = new Date(dateString);
          if (isNaN(date.getTime())) {
            throw new Error('无效的日期格式');
          }
          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, '0');
          const day = String(date.getDate()).padStart(2, '0');
          const hours = String(date.getHours()).padStart(2, '0');
          const minutes = String(date.getMinutes()).padStart(2, '0');
          const seconds = String(date.getSeconds()).padStart(2, '0');
          return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        } catch (error) {
          console.log('时间格式化失败:', error.message);
          return null;
        }
      };

      const startTime = formatTime(this.playbackStartTime);
      const endTime = formatTime(this.playbackEndTime);
      
      if (!startTime || !endTime) {
        console.log('时间格式错误，请重新选择时间');
        return;
      }

      console.log(`准备开始回放: ${startTime} 至 ${endTime}`);

      // 先停止当前预览
      this.clickStopRealPlay();

      // 添加延迟确保停止操作完成
      setTimeout(() => {
        try {
          WebVideoCtrl.I_StartPlayback(this.selectedDevice, {
            iWndIndex: 0, // 窗口索引
            iChannelID: parseInt(this.selectedChannel), // 通道ID
            szStartTime: startTime,
            szEndTime: endTime,
            iStreamType: parseInt(this.playbackStreamType),
            iPort: rtspPort, // RTSP端口
            success: () => {
              console.log(`开始回放成功: ${startTime} 至 ${endTime}`);
              // 打开声音（文档5.3.13节）
              WebVideoCtrl.I_OpenSound(0).catch(err => {
                console.log(`开启声音失败: ${err.message}`);
              });
            },
            error: (oError) => {
              console.log(`回放失败[${oError.errorCode}]: ${oError.errorString || '未知错误'}`);
              // 错误码处理（文档3.2节）
              switch (oError.errorCode) {
                case 16: // 网络连接失败
                  console.log('建议: 检查设备网络连接是否正常');
                  break;
                case 25: // URL解析失败
                  console.log('建议: 检查时间格式是否正确');
                  break;
                case 33: // 磁盘空间不足
                  console.log('建议: 清理本地磁盘空间');
                  break;
                default:
                  console.log('建议: 参考错误码说明排查问题');
              }
            }
          });
        } catch (error) {
          console.log('回放调用异常:', error.message);
        }
      }, 500);
    },

    // ============== 停止回放（文档5.3.5节） ==============
    stopPlayback() {
      try {
        WebVideoCtrl.I_Stop({ iWndIndex: 0 }).then(() => {
          console.log('回放已停止');
        }).catch(oError => {
          console.log(`停止回放失败[${oError.errorCode}]: ${oError.errorString || '未知错误'}`);
        });
      } catch (error) {
        console.log('停止回放调用异常:', error.message);
      }
    },

    // ============== 暂停回放（文档5.3.8节） ==============
    playbackPause() {
      try {
        WebVideoCtrl.I_Pause({ iWndIndex: 0 }).then(() => {
          console.log('回放已暂停');
        }).catch(oError => {
          console.log(`暂停回放失败[${oError.errorCode}]: ${oError.errorString || '未知错误'}`);
        });
      } catch (error) {
        console.log('暂停回放调用异常:', error.message);
      }
    },

    // ============== 恢复回放（文档5.3.9节） ==============
    playbackResume() {
      try {
        WebVideoCtrl.I_Resume({ iWndIndex: 0 }).then(() => {
          console.log('回放已恢复');
        }).catch(oError => {
          console.log(`恢复回放失败[${oError.errorCode}]: ${oError.errorString || '未知错误'}`);
        });
      } catch (error) {
        console.log('恢复回放调用异常:', error.message);
      }
    },

    // ============== 回放抓图（文档5.3.16节） ==============
    capturePlaybackPic() {
      try {
        const picName = `playback_capture_${new Date().getTime()}`;
        WebVideoCtrl.I_CapturePic(picName, { iWndIndex: 0 }).then(() => {
          console.log(`回放抓图成功: ${picName}`);
          // 获取保存路径（文档5.10.2节）
          WebVideoCtrl.I_GetLocalCfg().then(cfg => {
            console.log(`图片保存路径: ${cfg.playbackPicPath}`);
          }).catch(err => {
            console.log(`获取配置失败: ${err.message}`);
          });
        }).catch(oError => {
          console.log(`抓图失败[${oError.errorCode}]: ${oError.errorString || '未知错误'}`);
        });
      } catch (error) {
        console.log('抓图调用异常:', error.message);
      }
    },

    // 检查设备是否支持回放功能（文档5.2.3节）
    async checkPlaybackSupport() {
      if (!this.selectedDevice) {
        console.log('设备未登录，无法检查回放支持');
        return false;
      }

      try {
        // 调用获取设备信息接口（文档5.2.3节）
        await WebVideoCtrl.I_GetDeviceInfo(this.selectedDevice, {
          success: (xmlDoc) => {
            console.log('设备信息获取成功，支持回放检查通过');
          },
          error: (err) => {
            console.log(`设备信息获取失败: ${err.message}`);
          }
        });
        return true;
      } catch (error) {
        console.log('检查设备回放支持失败:', error.message);
        return false;
      }
    },

    // 通道切换处理
    onChannelChange() {
      if (this.selectedChannel) {
        console.log(`已选择通道${this.selectedChannel}`);
      }
    },

    // 重新初始化插件
    async reinitializePlugin() {
      try {
        // 先销毁现有插件
        await this.destroyPlugin();
        
        // 等待一小段时间确保完全销毁
        await new Promise(resolve => setTimeout(resolve, 500));
        
        // 重新初始化插件
        await this.initPlugin();
        
        // 如果之前有登录状态，自动重新登录
        if (this.loginIp && this.port) {
          console.log('自动重新登录设备');
          setTimeout(() => {
            this.clickLogin();
          }, 1000);
        }
      } catch (error) {
        console.error('重新初始化插件失败:', error);
      }
    },

    // 停止所有预览
    stopAllPreview() {
      if (typeof WebVideoCtrl !== 'undefined' && WebVideoCtrl.I_StopAllPlay) {
        WebVideoCtrl.I_StopAllPlay().then(() => {
          console.log('所有预览已停止');
        }).catch(err => {
          console.log(`停止预览失败: ${err.message}`);
        });
      }
    },

    // 销毁插件
    async destroyPlugin() {
      if (typeof WebVideoCtrl !== 'undefined' && WebVideoCtrl.I_DestroyPlugin) {
        try {
          // 停止对讲
          // if (this.isTalking) { // 移除此行
          //   await this.stopVoiceTalk(); // 移除此行
          // } // 移除此行
          await WebVideoCtrl.I_DestroyPlugin();
          console.log('插件已销毁');
        } catch (err) {
          console.log(`销毁插件失败: ${err.message}`);
        }
      }
    },

    // 检查插件状态并恢复
    checkAndRestorePlugin() {
      // 延迟检查，确保DOM已完全加载
      setTimeout(() => {
        this.restorePluginState();
      }, 300);
    },

    // 恢复插件状态
    async restorePluginState() {
      try {
        // 检查插件是否已初始化
        if (typeof WebVideoCtrl === 'undefined') {
          console.log('WebVideoCtrl未定义，重新初始化插件');
          await this.initPlugin();
          return;
        }

        // 检查插件是否正在运行
        const isRunning = await WebVideoCtrl.I_IsPluginRunning();
        if (!isRunning) {
          console.log('插件未运行，重新初始化');
          await this.initPlugin();
        } else {
          console.log('插件正在运行，检查登录状态');
        }

        // 检查登录状态并恢复
        if (this.loginIp && this.port) {
          // 检查是否已登录
          if (!this.selectedDevice) {
            console.log('设备未登录，尝试重新登录');
            setTimeout(() => {
              this.clickLogin();
            }, 500);
          } else {
            console.log('设备已登录，检查视频预览状态');
            // 检查是否有视频在播放
            this.checkVideoPreview();
          }
        }
      } catch (error) {
        console.error('恢复插件状态失败:', error);
        // 如果恢复失败，重新初始化
        await this.initPlugin();
      }
    },

    // 检查视频预览状态
    checkVideoPreview() {
      if (this.channels.length > 0 && this.selectedDevice) {
        console.log('检查视频预览状态，尝试恢复多窗口预览');
        setTimeout(() => {
          this.startMultiWindowPreview();
        }, 1000);
      }
    },

    // 强制重新初始化
    async forceReinitialize() {
      try {
        console.log('开始强制重新初始化');
        
        // 保存当前状态
        const savedDevice = this.selectedDevice;
        const savedChannels = [...this.channels];
        const savedSelectedChannel = this.selectedChannel;
        // const savedAudioChannels = [...this.audioChannels]; // 移除此行
        
        // 停止所有播放
        await this.stopAllPreview();
        // 停止对讲
        // if (this.isTalking) { // 移除此行
        //   await this.stopVoiceTalk(); // 移除此行
        // } // 移除此行
        
        // 等待一段时间确保清理完成
        await new Promise(resolve => setTimeout(resolve, 500));
        
        // 重新初始化插件
        await this.initPlugin();
        
        // 等待插件初始化完成
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // 恢复状态
        this.selectedDevice = savedDevice;
        this.channels = savedChannels;
        this.selectedChannel = savedSelectedChannel;
        // this.audioChannels = savedAudioChannels; // 移除此行
        
        // 如果有登录状态，直接获取通道信息
        if (this.selectedDevice) {
          console.log('恢复登录状态，重新获取通道信息');
          this.getChannels();
          // this.getAudioChannels(); // 移除此行
        } else if (this.loginIp && this.port) {
          // 如果没有登录状态，尝试登录
          console.log('尝试重新登录设备');
          this.clickLogin();
        }
        
        console.log('强制重新初始化完成');
      } catch (error) {
        console.error('强制重新初始化失败:', error);
      }
    },

    // 检查设备状态
    async checkDeviceStatus() {
      try {
        if (this.selectedDevice) {
          console.log('检查设备登录状态:', this.selectedDevice);
          
          // 尝试获取设备信息来验证登录状态
          if (typeof WebVideoCtrl !== 'undefined' && WebVideoCtrl.I_GetDevicePort) {
            try {
              await WebVideoCtrl.I_GetDevicePort(this.selectedDevice);
              console.log('设备登录状态正常，恢复视频预览');
              this.checkVideoPreview();
              return;
            } catch (error) {
              console.log('设备登录状态异常，重新登录');
              this.selectedDevice = '';
              // this.audioChannels = []; // 移除此行
            }
          }
        }
        
        // 如果没有登录状态或登录状态异常，尝试登录
        if (this.loginIp && this.port) {
          console.log('尝试登录设备');
          this.clickLogin();
        } else {
          console.log('未配置设备信息，请先登录');
        }
      } catch (error) {
        console.error('检查设备状态失败:', error);
      }
    },

    // 格式化日期时间用于input类型
    formatDateTimeForInput(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day}T${hours}:${minutes}`;
    },

    // 添加全局错误处理
    setupGlobalErrorHandling() {
      window.addEventListener('unhandledrejection', (event) => {
        console.error('未处理的Promise拒绝:', event.reason);
      });
      window.addEventListener('error', (event) => {
        console.error('JavaScript错误:', event.message, event.filename, event.lineno, event.colno);
      });
    }
  },

  mounted() {
    // 初始化插件
    this.initPlugin();
    
    // 设置默认回放时间（最近一小时）
    const endTime = new Date();
    const startTime = new Date(endTime.getTime() - 60 * 60 * 1000);
    this.playbackStartTime = this.formatDateTimeForInput(startTime);
    this.playbackEndTime = this.formatDateTimeForInput(endTime);
    
    // 添加全局错误处理
    this.setupGlobalErrorHandling();
    
    // 监听路由变化
    this.$watch('$route', (to, from) => {
      if (to.path === '/home/monitor' || to.path === '/home/provideocheck') {
        console.log('路由切换到视频页面，检查状态');
        setTimeout(() => {
          this.checkAndRestorePlugin();
        }, 500);
      }
    });
  },

  activated() {
    // 页面激活时检查设备状态
    console.log('页面激活，检查设备状态');
    this.checkDeviceStatus();
  },

  deactivated() {
    // 页面失活时停止预览但保持插件
    console.log('页面失活，停止预览');
    this.stopAllPreview();
    // 停止对讲
    // if (this.isTalking) { // 移除此行
    //   this.stopVoiceTalk(); // 移除此行
    // } // 移除此行
  },

  beforeDestroy() {
    // 销毁插件（文档5.12.6节）
    this.destroyPlugin();
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.provideo-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
  position: relative;
}

.provideo-left {
  flex: 3;
  min-width: 0; /* 允许收缩 */
}

.provideo-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  min-width: 400px; /* 设置最小宽度，防止被压缩 */
  max-width: 450px; /* 设置最大宽度，保持固定大小 */
  position: sticky;
  top: 20px;
  height: fit-content;
}

.provideo-video-plugin {
  width: 100%;
  height: 600px;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e9ecef;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .provideo-container {
    flex-direction: column;
    gap: 15px;
  }
  
  .provideo-left {
    flex: none;
    width: 100%;
  }
  
  .provideo-right {
    flex: none;
    width: 100%;
    min-width: auto;
    max-width: none;
    position: static;
  }
  
  .provideo-video-plugin {
    height: 500px;
  }
}

@media (max-width: 768px) {
  .provideo-container {
    padding: 10px;
  }
  
  .provideo-video-plugin {
    height: 400px;
  }
}

.provideo-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.provideo-card-title {
  font-size: 16px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.provideo-form-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  gap: 10px;
}

.provideo-form-row label {
  width: 80px;
  font-size: 14px;
  color: #666;
}

.provideo-form-row select,
.provideo-input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.provideo-btn-row {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.provideo-btn {
  flex: 1;
  padding: 8px 0;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.provideo-btn.primary {
  background-color: #ffffff;
  color: #333;
  border: 1px solid #dcdfe6;
}

.provideo-btn.success {
  background-color: #ffffff;
  color: #333;
  border: 1px solid #dcdfe6;
}

.provideo-btn.danger {
  background-color: #ffffff;
  color: #333;
  border: 1px solid #dcdfe6;
}

.provideo-btn:hover {
  opacity: 0.9;
}

/* 云台控制样式 */
.ptz-card {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  color: #333;
  border: 2px solid #e9ecef;
}

.ptz-card .provideo-card-title {
  color: #333;
  font-size: 18px;
  margin-bottom: 20px;
  text-align: center;
  font-weight: 600;
}

.ptz-status {
  margin-bottom: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.status-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.status-item:last-child {
  margin-bottom: 0;
}

.status-label {
  font-weight: 600;
  color: #666;
  margin-right: 10px;
  min-width: 50px;
}

.status-value {
  color: #333;
  font-weight: 500;
  background: #f8f9fa;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  border: 1px solid #e9ecef;
}

.ptz-remote-control {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  border: 2px solid #e9ecef;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.direction-pad {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(145deg, #f0f0f0, #e6e6e6);
  border-radius: 16px;
  position: relative;
  overflow: hidden;
  border: 3px solid #d1d5db;
  box-shadow: 
    inset 0 2px 4px rgba(255, 255, 255, 0.8),
    0 4px 8px rgba(0, 0, 0, 0.1);
}

.pad-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(3, 1fr);
  gap: 8px;
  align-items: center;
  justify-items: center;
  padding: 20px;
}

.direction-btn {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #d1d5db;
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.1),
    inset 0 1px 2px rgba(255, 255, 255, 0.8);
  color: #333;
  font-size: 20px;
}

.direction-btn:hover {
  background: linear-gradient(145deg, #f8f9fa, #e9ecef);
  color: #333;
  transform: translateY(-2px);
  box-shadow: 
    0 4px 8px rgba(0, 0, 0, 0.1),
    inset 0 1px 2px rgba(255, 255, 255, 0.8);
}

.direction-btn:active {
  transform: translateY(0);
  box-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.1),
    inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.direction-btn.up-btn {
  grid-area: 1 / 2;
}

.direction-btn.down-btn {
  grid-area: 3 / 2;
}

.direction-btn.left-btn {
  grid-area: 2 / 1;
}

.direction-btn.right-btn {
  grid-area: 2 / 3;
}

.function-buttons {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
  align-items: center;
}

.zoom-section {
  display: flex;
  gap: 15px;
  width: 100%;
  justify-content: center;
}

.zoom-btn {
  width: 80px;
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px solid #d1d5db;
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.1),
    inset 0 1px 2px rgba(255, 255, 255, 0.8);
  color: #333;
}

.zoom-btn:hover {
  background: linear-gradient(145deg, #f8f9fa, #e9ecef);
  color: #333;
  transform: translateY(-2px);
  box-shadow: 
    0 4px 8px rgba(0, 0, 0, 0.1),
    inset 0 1px 2px rgba(255, 255, 255, 0.8);
}

.zoom-btn:active {
  transform: translateY(0);
  box-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.1),
    inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.zoom-btn i {
  font-size: 20px;
  margin-bottom: 4px;
}

.zoom-btn span {
  font-size: 10px;
  font-weight: 500;
  color: inherit;
}
</style>