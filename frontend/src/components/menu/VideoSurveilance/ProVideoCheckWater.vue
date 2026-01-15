<template>
    <div class="container">
        <div class="left">
            <div class="top-left">
                <fieldset class="operate">
                    <legend>操作信息</legend>
                    <div id="opinfo" class="opinfo">
                        <div v-for="info in opInfo" :key="info">{{ info }}</div>
                    </div>
                </fieldset>
                <fieldset class="callback">
                    <legend>事件回调信息</legend>
                    <div id="cbinfo" class="cbinfo">
                        <div v-for="info in cbInfo" :key="info">{{ info }}</div>
                    </div>
                </fieldset>
            </div>
            <div id="divPlugin" class="plugin"></div>
        </div>
        <div class="right">
            <fieldset class="login">
                <legend>登录</legend>
                <table cellpadding="0" cellspacing="3" border="0">
                    <!-- <tr>
                        <td class="tt">IP地址</td>
                        <td>
                            <input id="loginip" type="password" class="txt" v-model="loginIp" />
                        </td>
                        <td class="tt">端口号</td>
                        <td>
                            <input id="port" type="password" class="txt" v-model="port" />
                        </td>
                    </tr>
                    <tr>
                        <td class="tt">用户名</td>
                        <td>
                            <input id="username" type="password" class="txt" v-model="username" />
                        </td>
                        <td class="tt">密码</td>
                        <td>
                            <input id="password" type="password" class="txt" v-model="password" />
                        </td>
                    </tr> -->
                    <tr>
                        <td>窗口分割数</td>
                        <td>
                            <select class="sel2" v-model="wndNum" @change="changeWndNum">
                                <option value="1" selected>1x1</option>
                                <option value="2">2x2</option>
                                <option value="3">3x3</option>
                                <option value="4">4x4</option>
                                <option value="1*2">1x2</option>
                                <option value="2*1">2x1</option>
                            </select>
                        </td>
                        <td class="tt">通道列表</td>
                        <td>
                            <select id="channels" class="sel" v-model="selectedChannel" @change="onChannelChange">
                                <option v-for="channel in channels" :key="channel.id" :value="channel.id">
                                    {{ channel.name }}
                                </option>
                            </select>
                        </td>
                    </tr>
                    <!-- <tr>
                        <td colspan="4">
                            <input type="button" class="btn" value="登录" @click="clickLogin" />
                            <input type="button" class="btn" value="退出" @click="clickLogout" />
                        </td>
                    </tr> -->
                    <!-- <tr>
                        <td class="tt">已登录设备</td>
                        <td>
                            <select id="ip" class="sel" v-model="selectedDevice" @change="onDeviceChange">
                                <option v-for="device in devices" :key="device" :value="device">
                                    {{ device }}
                                </option>
                            </select>
                        </td>
                    </tr> -->
                    <!-- <tr> -->
                        <!-- <td>
                            <input type="button" class="btn2" value="开始预览" @click="clickStartRealPlay" />
                        </td> -->
                        <!-- <td>
                            <input type="button" class="btn2" value="停止预览" @click="clickStopRealPlay" />
                        </td>
                        <td>
                            <input type="button" class="btn2" value="销毁插件" @click="destroyPlugin" />
                        </td> -->
                        <!-- <td>
                            <input type="button" class="btn2" value="加载插件" @click="InitPlugin" />
                        </td> -->
                    <!-- </tr> -->
                </table>
            </fieldset>

            <fieldset class="ipchannel">
                <legend>数字通道</legend>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="button" class="btn" value="获取数字通道列表" @click="clickGetDigitalChannelInfo" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="digitaltdiv">
                                <table id="digitalchannellist" class="digitalchannellist" cellpadding="0"
                                    cellspacing="0" border="0">
                                    <tr v-for="channel in digitalChannels" :key="channel.id">
                                        <td>{{ channel.id }}</td>
                                        <td>{{ channel.ipAddress }}</td>
                                        <td>{{ channel.srcInputPort }}</td>
                                        <td>{{ channel.managePortNo }}</td>
                                        <td>{{ channel.online }}</td>
                                        <td>{{ channel.proxyProtocol }}</td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </fieldset>

            <fieldset class="preview">
                <legend>预览</legend>
                <table cellpadding="0" cellspacing="3" border="0">
                    <tr>
                        <td>码流类型</td>
                        <td>
                            <select style="margin-left: 20px;" id="streamtype" class="sel" v-model="streamType">
                                <option value="1">主码流</option>
                                <option value="2">子码流</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <div class="download-plugin">
                <a href="http://111.4.68.108:8081/shipin/HCWebSDKPlugin.exe" target="_blank">请在此处下载视频插件并安装</a>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loginIp: "192.168.32.124",
            port: "80",
            username: "admin",
            password: "Dse@147258",
            devicePort: "",
            rtspPort: "",
            wndNum: "1",
            selectedDevice: "",
            selectedChannel: "",
            devices: [],
            channels: [],
            digitalChannels: [],
            streamType: "2",
            opInfo: [],
            cbInfo: [],
            currentWindowIndex: 0, // 当前窗口索引
            currentChannelIndex: 0, // 当前通道索引
            isAutoLoading: false, // 是否正在自动加载
        };
    },
    methods: {
        showOPInfo(info, status, xmlDoc) {
            const timestamp = this.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
            let message = `${timestamp} ${info}`;
            if (status && status !== 200) {
                const statusString = xmlDoc ? xmlDoc.querySelector("statusString").textContent : "";
                const subStatusCode = xmlDoc ? xmlDoc.querySelector("subStatusCode").textContent : "";
                message += ` (${status}, ${subStatusCode})`;
            }
            this.opInfo.unshift(message);
        },
        showCBInfo(info) {
            const timestamp = this.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
            this.cbInfo.unshift(`${timestamp} ${info}`);
        },
        dateFormat(date, fmt) {
            const o = {
                "M+": date.getMonth() + 1,
                "d+": date.getDate(),
                "h+": date.getHours(),
                "m+": date.getMinutes(),
                "s+": date.getSeconds(),
                "q+": Math.floor((date.getMonth() + 3) / 3),
                "S": date.getMilliseconds(),
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (const k in o) {
                if (new RegExp(`(${k})`).test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return fmt;
        },
        clickLogout() {
            if (!this.selectedDevice) return;
            WebVideoCtrl.I_Logout(this.selectedDevice).then(() => {
                this.showOPInfo(`${this.selectedDevice} 退出成功！`);
                this.selectedDevice = "";
            }).catch(() => {
                this.showOPInfo(`${this.selectedDevice} 退出失败！`);
            });
        },
        clickGetDeviceInfo() {
            const szDeviceIdentify = this.selectedDevice;
            if (!szDeviceIdentify) return;

            WebVideoCtrl.I_GetDeviceInfo(szDeviceIdentify, {
                success: (xmlDoc) => {
                    // 将 xmlDoc 转换为可操作的 DOM 对象
                    let xmlDocParsed;
                    if (typeof xmlDoc === "string") {
                        xmlDocParsed = new DOMParser().parseFromString(xmlDoc, "application/xml");
                    } else {
                        xmlDocParsed = xmlDoc;
                    }

                    // 解析设备信息
                    const deviceInfo = [
                        `设备名称：${xmlDocParsed.querySelector("deviceName").textContent}`,
                        `设备ID：${xmlDocParsed.querySelector("deviceID").textContent}`,
                        `型号：${xmlDocParsed.querySelector("model").textContent}`,
                        `设备序列号：${xmlDocParsed.querySelector("serialNumber").textContent}`,
                        `MAC地址：${xmlDocParsed.querySelector("macAddress").textContent}`,
                        `主控版本：${xmlDocParsed.querySelector("firmwareVersion").textContent} ${xmlDocParsed.querySelector("firmwareReleasedDate").textContent}`,
                        `编码版本：${xmlDocParsed.querySelector("encoderVersion").textContent} ${xmlDocParsed.querySelector("encoderReleasedDate").textContent}`,
                    ];

                    this.showOPInfo(`${szDeviceIdentify} 获取设备信息成功！`);
                    alert(deviceInfo.join("\n"));
                },
                error: (oError) => {
                    this.showOPInfo(`${szDeviceIdentify} 获取设备信息失败！`, oError.errorCode, oError);
                }
            });
        },
        // 窗口分割
        changeWndNum() {
            const szDeviceIdentify = this.selectedDevice;
            if (!szDeviceIdentify) {
                this.showOPInfo("未选择设备！");
                return;
            }

            const wndType = this.wndNum;
            // 普通窗口数量
            const wndNum = parseInt(wndType, 10);
            WebVideoCtrl.I_ChangeWndNum(wndNum).then(() => {
                this.showOPInfo("窗口分割成功！");
            }).catch((oError) => {
                this.showOPInfo("窗口分割失败！", oError.errorCode, oError);
            });

        },
        onDeviceChange() {
            this.getChannelInfo();
            this.getDevicePort();
        },
        getChannelInfo() {
            const szDeviceIdentify = this.selectedDevice;
            if (!szDeviceIdentify) {
                this.showOPInfo("未选择设备！");
                return;
            }

            // 获取模拟通道信息
            WebVideoCtrl.I_GetAnalogChannelInfo(szDeviceIdentify, {
                success: (xmlDoc) => {
                    this.parseChannelInfo(xmlDoc, szDeviceIdentify);
                },
                error: (oError) => {
                    this.showOPInfo(`${szDeviceIdentify} 获取模拟通道信息失败！`, oError.errorCode, oError);
                }
            });

            // 获取数字通道信息
            WebVideoCtrl.I_GetDigitalChannelInfo(szDeviceIdentify, {
                success: (xmlDoc) => {
                    this.parseChannelInfo(xmlDoc, szDeviceIdentify);
                },
                error: (oError) => {
                    this.showOPInfo(`${szDeviceIdentify} 获取数字通道信息失败！`, oError.errorCode, oError);
                }
            });
        },
        // 解析通道信息
        parseChannelInfo(xmlDoc, szDeviceIdentify) {
            // 将 xmlDoc 转换为可操作的 DOM 对象
            let xmlDocParsed;
            if (typeof xmlDoc === "string") {
                xmlDocParsed = new DOMParser().parseFromString(xmlDoc, "application/xml");
            } else {
                xmlDocParsed = xmlDoc;
            }

            // 解析通道信息
            const channels = Array.from(xmlDocParsed.querySelectorAll("VideoInputChannel, InputProxyChannelStatus")).map((channel) => ({
                id: channel.querySelector("id").textContent,
                name: channel.querySelector("name").textContent || `Camera ${channel.querySelector("id").textContent}`
            }));

            this.channels = channels; // 更新通道列表
            if (channels.length > 0) {
                this.selectedChannel = channels[0].id; // 自动选择第一个通道
            }
            this.showOPInfo(`${szDeviceIdentify} 获取通道列表成功！`);
        },
        getDevicePort() {
            // 示例：获取设备端口逻辑
            this.devicePort = "8080";
        },
        onChannelChange() {
            // 先停止当前的预览
            this.clickStopRealPlay();
            // 延迟1秒后开始新的预览
            setTimeout(() => {
                this.clickStartRealPlay();
            }, 1000); // 延迟1000毫秒（1秒）
        },
        clickStopRealPlay() {
            if (!this.selectedDevice) return;
            WebVideoCtrl.I_Stop().then(() => {
                this.showOPInfo(`${this.selectedDevice} 停止预览成功！`);
            }).catch((oError) => {
                this.showOPInfo(`${this.selectedDevice} 停止预览失败！`, oError.errorCode, oError);
            });
        },
        clickGetDigitalChannelInfo() {
            if (!this.selectedDevice) return;
            WebVideoCtrl.I_GetDigitalChannelInfo(this.selectedDevice, {
                success: (xmlDoc) => {
                    this.digitalChannels = Array.from(xmlDoc.querySelectorAll("InputProxyChannelStatus")).map((channel) => ({
                        id: channel.querySelector("id").textContent,
                        ipAddress: channel.querySelector("ipAddress").textContent,
                        srcInputPort: channel.querySelector("srcInputPort").textContent,
                        managePortNo: channel.querySelector("managePortNo").textContent,
                        online: channel.querySelector("online").textContent === "true" ? "在线" : "离线",
                        proxyProtocol: channel.querySelector("proxyProtocol").textContent,
                    }));
                    this.showOPInfo(`${this.selectedDevice} 获取数字通道成功！`);
                },
                error: (oError) => {
                    this.showOPInfo(`${this.selectedDevice} 获取数字通道失败！`, oError.errorCode, oError);
                },
            });
        },
        destroyPlugin() {
            const confirmMessage = "是否确定要销毁插件？再次获取插件需要刷新页面。";
            const userConfirmed = confirm(confirmMessage);

            if (userConfirmed) {
                WebVideoCtrl.I_DestroyPlugin().then(() => {
                    // this.showOPInfo("销毁插件成功！");
                }).catch((oError) => {
                    this.showOPInfo("销毁插件成功，请刷新页面再次加载插件！", oError.errorCode, oError);
                });
            } else {
                this.showOPInfo("销毁插件操作已取消。");
            }
        },
        deepDestroyPlugin() {
            WebVideoCtrl.I_DestroyPlugin().then(() => {
                // this.showOPInfo("销毁插件成功！");
            }).catch((oError) => {
                this.showOPInfo("销毁插件成功，请刷新页面再次加载插件！", oError.errorCode, oError);
            });
        },
        InitPlugin() {
            WebVideoCtrl.I_InitPlugin({
                bWndFull: true,
                iWndowType: 4,
                cbSelWnd: (xmlDoc) => {
                    const wndIndex = parseInt(xmlDoc.querySelector("SelectWnd").textContent, 10);
                    this.showCBInfo(`当前选择的窗口编号：${wndIndex}`);
                },
                cbDoubleClickWnd: (iWndIndex, bFullScreen) => {
                    this.showCBInfo(`当前放大的窗口编号：${iWndIndex}`);
                },
                cbEvent: (iEventType, iParam1, iParam2) => {
                    if (iEventType === 2) {
                        this.showCBInfo(`窗口${iParam1}回放结束！`);
                    } else if (iEventType === -1) {
                        this.showCBInfo(`设备${iParam1}网络错误！`);
                    }
                },
                cbInitPluginComplete: () => {
                    WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin").then(() => {
                        WebVideoCtrl.I_CheckPluginVersion().then((bFlag) => {
                            if (bFlag) {
                                alert("检测到新的插件版本，请升级！");
                            }
                        });
                    }).catch(() => {
                        alert("插件初始化失败，请检查！");
                    });
                },
            });
        },
        hidPlugin() {
            this.showOPInfo("隐藏插件成功！");
        },
        showPlugin() {
            this.showOPInfo("展示插件成功！");
        },
        clickLogin() {
            const szIP = this.loginIp;
            const szPort = this.port;
            const szUsername = this.username;
            const szPassword = this.password;

            if (!szIP || !szPort) {
                this.showOPInfo("IP地址或端口号不能为空！");
                return;
            }

            const szDeviceIdentify = `${szIP}_${szPort}`;

            console.log(this.password)
            WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
                timeout: 3000,
                success: (xmlDoc) => {
                    this.showOPInfo(`${szDeviceIdentify} 登录成功！`);
                    this.devices.push(szDeviceIdentify);
                    this.selectedDevice = szDeviceIdentify;

                    // 登录成功后获取通道列表
                    this.getChannelInfo();
                    this.getDevicePort(); // 如果需要获取设备端口，也可以在这里调用
                    // 自动获取通道信息
                    this.clickGetDigitalChannelInfo();

                    // // 更新下拉框的值为3×3
                    this.wndNum = "4";
                    // 延迟1秒后开始自动加载通道视频
                    setTimeout(() => {
                        this.startAutoLoad();
                    }, 1000); // 延迟1秒，确保窗口分割完成
                },
                error: (oError) => {
                    this.showOPInfo(`${szDeviceIdentify} 登录失败！`, oError.errorCode, oError);
                }
            });
        },
        clickStartRealPlay() {
            if (!this.selectedDevice || !this.selectedChannel) return;
            console.log(this.selectedChannel)
            // 开始预览（自动加载到当前窗口）
            WebVideoCtrl.I_StartRealPlay(this.selectedDevice, {
                iStreamType: parseInt(this.streamType, 10),
                iChannelID: parseInt(this.selectedChannel, 10),
                success: () => {
                    this.showOPInfo(`${this.selectedDevice} 通道 ${this.selectedChannel} 开始预览成功！`);
                },
                error: (oError) => {
                    this.showOPInfo(`${this.selectedDevice} 通道 ${this.selectedChannel} 开始预览失败！`, oError.errorCode, oError);
                },
            });
        },
        startAutoLoad() {
            if (this.isAutoLoading) return; // 如果已经在自动加载，则不再执行
            console.log("我开始自动加载了")
            this.isAutoLoading = true; // 标记为正在自动加载
            this.currentWindowIndex = 0; // 重置窗口索引
            this.currentChannelIndex = 0; // 重置通道索引
            this.autoStartRealPlay(); // 开始加载第一个通道
        },
        // autoLoadNextChannel() {
        //     if (this.currentChannelIndex >= this.channels.length) {
        //         // 所有通道加载完毕
        //         this.isAutoLoading = false;
        //         this.showOPInfo("所有通道已加载完毕！");
        //         return;
        //     }

        //     const channel = this.channels[this.currentChannelIndex];
        //     const digitalChannel = this.digitalChannels.find((dc) => dc.id === channel.id);
        //     this.selectedChannel = channel.id; // 选择当前通道
        //     if (!digitalChannel || digitalChannel.online !== "在线") {
        //         // 如果通道不存在或设备离线，跳过当前通道
        //         this.showOPInfo(`通道 ${channel.id} 设备离线，跳过加载！`);
        //         this.loadNextChannel();
        //         return;
        //     }

        //     // 开始预览（自动加载到当前窗口）
        //     this.autoStartRealPlay();
        // },
        
        // 可以连续加载
        autoStartRealPlay() {
            if (!this.selectedDevice || !this.selectedChannel) return;

            const channel = this.channels[this.currentChannelIndex];
            const digitalChannel = this.digitalChannels.find((dc) => dc.id === channel.id);
            this.selectedChannel = channel.id; // 选择当前通道
            if (!digitalChannel || digitalChannel.online !== "在线") {
                // 如果通道不存在或设备离线，跳过当前通道
                this.showOPInfo(`通道 ${channel.id} 设备离线，跳过加载！`);
                this.loadNextChannel();
                return;
            }
            console.log(this.currentChannelIndex);
            // 开始预览（自动加载到当前窗口）
            WebVideoCtrl.I_StartRealPlay(this.selectedDevice, {
                iStreamType: parseInt(this.streamType, 10),
                iChannelID: parseInt(this.selectedChannel, 10),
                iWndIndex: this.currentWindowIndex, // 指定加载到当前窗口
                success: () => {
                    this.showOPInfo(`${this.selectedDevice} 通道 ${this.selectedChannel} 开始预览成功！`);
                    this.loadNextChannel(); // 加载下一个通道
                    // setTimeout(() => {
                    //     this.loadNextChannel(); // 加载下一个通道
                    // }, 1000);
                },
                error: (oError) => {
                    this.showOPInfo(`${this.selectedDevice} 通道 ${this.selectedChannel} 开始预览失败！`, oError.errorCode, oError);
                    this.loadNextChannel(); // 加载下一个通道
                    // setTimeout(() => {
                    //     this.loadNextChannel(); // 加载下一个通道
                    // }, 1000);
                },
            });
        },
        loadNextChannel() {
            this.currentWindowIndex++; // 切换到下一个窗口
            this.currentChannelIndex++; // 切换到下一个通道

            console.log(this.channels.length);
            if (this.currentChannelIndex < this.channels.length) {
                // 如果还有未加载的通道，继续加载
                const channel = this.channels[this.currentChannelIndex];
                this.selectedChannel = channel.id; // 选择当前通道
                this.autoStartRealPlay(); // 开始加载
            } else {
                // 所有通道加载完毕
                this.isAutoLoading = false;
                this.showOPInfo("所有通道已加载完毕！");
            }
        },

    },
    mounted() {
        this.InitPlugin();
        setTimeout(() => {
            this.clickLogin();
        }, 2000);
    },
    beforeDestroy() { // Vue 2 使用 beforeDestroy
        this.deepDestroyPlugin();
    },
};
</script>


<style lang="less" scoped>
* {
    margin: 0;
    padding: 0;
}

html {
    width: 100%;
    height: 100%;
    font-size: 12px;
    font-family: Arial, Helvetica, sans-serif;
    -webkit-text-size-adjust: none;
    background: #FFFFFF;
}

body {
    padding: 5px;
}

.container {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    flex-direction: row;
}

select {
    height: 20px;
    line-height: 20px;
}

// .left {
//     float: left;
// }
.top-left {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.freeze {
    position: absolute;
    text-align: center;
    background: #343434;
    color: #FFFFFF;
    font-size: 26px;
    font-weight: bold;
    filter: alpha(opacity=60);
    opacity: 0.6;
}

.vtop {
    vertical-align: middle;
    margin-top: -1px;
}

/*插件*/
.plugin {
    width: 1000px;
    height: 600px;
}

// fieldset {
//     display: block;
// }


/*登录*/
.login {
    width: 480px;
    padding: 10px;
    border: 1px solid #7F9DB9;
}

.login .tt {
    width: 100px;
}

.login .txt {
    width: 130px;
}

.login .btn {
    width: 45px;
    height: 22px;
    line-height: 18px;
}

.login .btn2 {
    width: 100px;
    height: 22px;
    line-height: 18px;
}

.login .sel {
    width: 130px;
}

.login .sel2 {
    width: 65px;
}

/*数字通道*/
.ipchannel {
    width: 480px;
    padding: 10px;
    border: 1px solid #7F9DB9;
}

.ipchannel .digitaltdiv {
    width: 100%;
    /* 确保表格容器宽度为 100% */
    height: 200px;
    overflow: hidden;
    overflow-y: auto;
    border: 1px solid #7F9DB9;
    font-size: 11px;
}

.ipchannel .digitalchannellist {
    width: 100%;
    /* 确保表格宽度为 100% */
    border-collapse: collapse;
}

.ipchannel .digitalchannellist th,
.ipchannel .digitalchannellist td {
    padding: 2px;
    border: 1px solid #7F9DB9;
    white-space: nowrap;
    text-align: center;
    /* 居中对齐内容 */
}

/*预览*/
.preview {
    width: 480px;
    padding: 10px;
    padding-top: 0;
    border: 1px solid #7F9DB9;
}

.preview .tt {
    width: 60px;
}

.preview .txt {
    width: 30px;
}

.preview .btn {
    width: 70px;
    height: 22px;
    line-height: 18px;
}

.preview .btn2 {
    width: 90px;
    height: 22px;
    line-height: 18px;
}

.preview .sel {
    width: 105px;
}

/*操作信息*/
.operate {
    width: 450px;
    padding: 10px;
    border: 1px solid #7F9DB9;
}

.operate .opinfo {
    height: 220px;
    border: 1px solid #7F9DB9;
    overflow: auto;
}

/*事件回调*/
.callback {
    width: 450px;
    padding: 10px;
    border: 1px solid #7F9DB9;
}

.callback .cbinfo {
    height: 220px;
    border: 1px solid #7F9DB9;
    overflow: auto;
}
</style>