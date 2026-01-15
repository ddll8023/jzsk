<template>
    <div ref="divPlugin" :id="divPlugin" class="plugin" style="width: 100%;height:100%;"></div>
</template>


<script>
export default {
    props: ['sysParams', 'width', 'height'],
    data() {
        return {
            divPlugin: 'video_' + this.generateUUID(),
            g_iWndIndex: null,
            szDeviceIdentify: ''
        };
    },
    created() {
    },
    mounted() {
        this.$nextTick(() => {
            setTimeout(() => {
                this.linkVideo();
            }, 500);
        })
    },
    beforeDestroy() {
        this.hideVideo();
    },
    methods: {
        // 初始化
        linkVideo() {
            let that = this;
            WebVideoCtrl.I_InitPlugin({
                bWndFull: true, //是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
                iWndowType: 1, //表示视频组件窗口数，默认是1，我这里需要四个窗口改为了2
                cbSelWnd: function (xmlDoc) {
                    that.g_iWndIndex = parseInt(
                        $(xmlDoc).find('SelectWnd').eq(0).text(),
                        10
                    );
                    console.log('选中窗口');
                    if (that.sysParams[that.g_iWndIndex]) {
                        that.clickStartRealPlay();
                    } else {
                        that.$message.error('当前窗口无设备');
                    }
                },
                cbDoubleClickWnd: function (iWndIndex, bFullScreen) {
                    if (!bFullScreen) {
                    }
                },
                cbEvent: function (iEventType, iParam1, iParam2) {
                    if (2 == iEventType) {
                        // 回放正常结束
                        showCBInfo('窗口' + iParam1 + '回放结束！');
                    } else if (-1 == iEventType) {
                        showCBInfo('设备' + iParam1 + '网络错误！');
                    } else if (3001 == iEventType) {
                        clickStopRecord(g_szRecordType, iParam1);
                    }
                },
                cbInitPluginComplete: function () {
                    WebVideoCtrl.I_InsertOBJECTPlugin(that.divPlugin).then(
                        () => {
                            that.sysParams.map((item, index) => {
                                setTimeout(() => {
                                    that.g_iWndIndex = index;
                                    that.clickStartRealPlay();
                                }, index * 1000);
                            });
                        },
                        () => {
                            alert("插件初始化失败，请确认是否已安装插件；如果未安装，请双击开发包目录里的HCWebSDKPlugin.exe安装！")
                            console.log("插件初始化失败，请确认是否已安装插件；如果未安装，请双击开发包目录里的HCWebSDKPlugin.exe安装！");
                        }
                    );
                }
            });
        },
        // 登录
        async clickStartRealPlay() {
            let that = this;
            var oWndInfo = WebVideoCtrl.I_GetWindowStatus(that.g_iWndIndex);
            let { szIP, szPort, szUsername, szPassword } =
                this.sysParams[that.g_iWndIndex];
            that.szDeviceIdentify = szIP + '_' + szPort;
            if (oWndInfo == null) {
                WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
                    success: function (xmlDoc) {
                        //成功的回调函数
                        that.getVideo();
                    },
                    error: function (oError) {
                        if (oError.errorCode == '2001') {
                            that.getVideo();
                        }
                        //失败的回调函数
                    }
                });
            } else {
                await WebVideoCtrl.I_Stop(that.g_iWndIndex);
                that.getVideo();
            }
        },
        // 打开预览视频
        getVideo() {
            console.log('渲染第' + this.g_iWndIndex + '窗口');
            WebVideoCtrl.I_StartRealPlay(this.szDeviceIdentify, {
                iWndIndex: this.g_iWndIndex,
                success: function () { },
                error: function (oError) { }
            });
        },
        // 关闭销毁
        async hideVideo() {
            if (WebVideoCtrl && typeof WebVideoCtrl.I_StopAllPlay === 'function') {
                await WebVideoCtrl.I_StopAllPlay();
            }
            if (WebVideoCtrl && typeof WebVideoCtrl.I_Logout === 'function' && this.szDeviceIdentify) {
                await WebVideoCtrl.I_Logout(this.szDeviceIdentify);
            }
            if (WebVideoCtrl && typeof WebVideoCtrl.I_DestroyPlugin === 'function') {
                await WebVideoCtrl.I_DestroyPlugin();
            }
        },
        // 生成uuid
        generateUUID() {
            let d = new Date().getTime();
            let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(
                /[xy]/g,
                (c) => {
                    let r = (d + Math.random() * 16) % 16 | 0;
                    d = Math.floor(d / 16);
                    return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16);
                }
            );
            return uuid;
        }
    },
    watch: {
        sysParams: {
            handler(newVal, oldVal) {
                // 当items数组发生变化时，执行这里的逻辑
                console.log('items 数组发生变化：', newVal, oldVal);
                // this.hideVideo();
                WebVideoCtrl.I_StopAllPlay();
                // this.linkVideo();
                this.sysParams.map((item, index) => {
                    setTimeout(() => {
                        this.g_iWndIndex = index;
                        this.clickStartRealPlay();
                    }, index * 400);
                });
            },
            deep: true
        }
    }
};
</script>

<style lang='scss' scoped>

</style>