<template>
  <div id="div1">
    <div id="div-main">
      <div id="div-video" ref="divVideo">
        <!-- 根据媒体类型显示视频或图片 -->
        <img v-if="isImage" :src="videoUrl" alt="封面图" @click="toggleFullScreen" />
        <video v-else ref="videoPlayer" class="video-js vjs-default-skin" playsinline controls>
          <source :src="videoUrl" type="video/mp4" />
        </video>
        <!-- 全屏按钮 -->
        <button v-if="isImage" id="full-screen-button" @click="toggleFullScreen">
          <i class="el-icon-full-screen"></i>
        </button>
      </div>
      <div id="div-description">
        <h1>工程简介</h1>
        <p style="white-space: pre-line;">{{ projectInfo.text }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import videojs from "video.js";
import "video.js/dist/video-js.css";

export default {
  data() {
    return {
      player: null,
      projectInfo: [],
      baseUrl: 'http://111.4.68.108:8081/shipin/',
      videoUrl: '',
      dialogVisible: false,
      editForm: { text: '' },
      characterCount: 0, // 当前输入的字数
      maxCharacters: 450, // 最大允许的字数
      isImage: false, // 用于标记是否为图片
      isFullScreen: false // 用于标记是否全屏
    }
  },
  async mounted() {
    await this.getProjectInfo();
    this.checkMediaType(); // 检查媒体类型
  },
  methods: {
    async getProjectInfo() {
      const { data: res } = await this.$http.get('/icon/getByName');
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败');
      }
      this.projectInfo = res.data;
      this.videoUrl = this.baseUrl + this.projectInfo.img;
      console.log(this.videoUrl);
    },
    checkMediaType() {
      // 假设图片文件的扩展名为.jpg, .jpeg, .png等
      const imageExtensions = ['.jpg', '.jpeg', '.png'];
      const extension = this.videoUrl.slice(((this.videoUrl.lastIndexOf(".") - 1) >>> 0) + 2);
      if (imageExtensions.includes('.' + extension.toLowerCase())) {
        this.isImage = true;
      } else {
        this.isImage = false;
        this.player = videojs(this.$refs.videoPlayer, {
          controls: true,
          autoplay: false,
          preload: "auto",
          techOrder: ["html5"],
        });

        this.player.src({
          type: "video/mp4",
          src: this.videoUrl
        });
      }
    },
    toggleFullScreen() {
      if (this.isFullScreen) {
        this.exitFullScreen();
      } else {
        this.enterFullScreen();
      }
    },
    enterFullScreen() {
      const elem = this.$refs.divVideo;
      if (elem.requestFullscreen) {
        elem.requestFullscreen();
      } else if (elem.mozRequestFullScreen) {
        elem.mozRequestFullScreen();
      } else if (elem.webkitRequestFullscreen) {
        elem.webkitRequestFullscreen();
      } else if (elem.msRequestFullscreen) {
        elem.msRequestFullscreen();
      }
      this.isFullScreen = true;
    },
    exitFullScreen() {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      }
      this.isFullScreen = false;
    },
  },
  beforeDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  },
};
</script>

<style lang="less" scoped>
#div1 {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  background-color: #b3d8e7;
}

#div-main {
  display: flex;
  width: 90%;
  height: 75%;
  max-width: 1400px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 16px;
  overflow: hidden;
  align-items: center;
  margin: 20px;
}

#div-video {
  width: 70%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #000;
  height: 100%;
  position: relative;
}

#div-description {
  width: 30%;
  padding: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
}

h1 {
  color: #333;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 20px;
}

p {
  color: #666;
  line-height: 1.8;
  font-size: 16px;
}

img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 修改此行 */
  cursor: pointer;
}

video {
  width: 100%;
  height: 100%;
  cursor: pointer;
}

#full-screen-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.char-count {
  position: absolute;
  bottom: 5px;
  right: 5px;
  color: #909399;
  font-size: 12px;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2px 5px;
  border-radius: 4px;
  pointer-events: none;
}

@media (max-width: 768px) {
  #div-main {
    flex-direction: column;
  }

  #div-video,
  #div-description {
    flex: none;
  }

  #div-video {
    height: 300px;
  }

  #div-description {
    padding: 20px;
  }
}
</style>