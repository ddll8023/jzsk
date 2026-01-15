<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>实时监测</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/videocheck' }">图片监测</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="main">
      <div id="main-body">
        <!-- 上部分：日期选择器、监测点选择框、查询按钮 -->
        <div id="search-section">
          <el-date-picker v-model="queryDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
            style="margin-right: 10px;">
          </el-date-picker>
          <el-select v-model="code" clearable placeholder="请选择监测点" style="margin-right: 10px;">
            <el-option v-for="item in codeList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-button type="primary" @click="getPhotos">内容搜索</el-button>
        </div>

        <!-- 下部分：用于显示图片的大框框 -->
        <div id="photo-frame">
          <div v-if="imageList.length">
            <img v-for="(img, index) in imageList" :key="index" :src="`${video_photo_url}${img}`"
              @click="showEnlargedImage(index)" alt="Photo" class="photo-display" />
          </div>
          <div v-else>
            暂无图片
          </div>
        </div>

        <!-- 放大的图片模态框 -->
        <div v-if="currentEnlargedIndex !== -1" class="enlarged-image-modal">
          <img :src="`${video_photo_url}${imageList[currentEnlargedIndex]}`" alt="Enlarged Photo"
            class="enlarged-photo-display" />
          <button @click="previousImage" class="prev-button">&lt;</button>
          <button @click="nextImage" class="next-button">&gt;</button>
          <!-- 关闭按钮 -->
          <button @click="closeModal" class="close-button">X</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    return {
      video_photo_url: 'http://111.4.68.108:8081/pic/',
      code: '',  // 测站编码
      codeList: [],
      map: [],
      queryDate: '',
      imageList: [],
      currentEnlargedIndex: -1,
      type: '',
      types: [],
      typeList: [],
      town: '乡镇1',
      village: '乡镇1村1',
      //镇 村类型列表
      townVillageData: [],
      townList: [],
      villageList: [],
      treeData: [], // 存储树形数据
      activeIndex: '1-1', // 默认激活的菜单项
      openeds: ['1'], // 默认展开的菜单项
      input: '',
      queryinfo: {
        currentPage: 1,
        pageSize: 10
      },
      VCList: [],
      total: 0,
    }
  },
  methods: {
    //获取数据列表
    async getVCList() {
      const { data: res } = await this.$http.get('/video-configuration/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize,
          town: this.town,
          village: this.village
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.VCList = res.data.records
      this.total = res.data.total
    },

    // 获取树形数据
    async getTreeData() {
      try {
        const response = await this.$http.get('/video-configuration/tree');
        if (response.data.code === 200) {
          this.treeData = this.convertTreeData(response.data.data);
          console.log(this.treeData);
        } else {
          this.$message.error('获取数据失败');
        }
      } catch (error) {
        console.error('Error fetching tree data:', error);
      }
    },
    // 将后端数据转换为树形数据
    convertTreeData(data) {
      return data.map(item => ({
        ...item,
        children: item.children ? item.children.map(subItem => ({
          ...subItem,
          children: subItem.children ? subItem.children.map(camera => ({
            ...camera,
            children: undefined // 摄像头没有子项
          })) : undefined
        })) : undefined
      }));
    },
    // 处理菜单选择事件
    handleSelect(key, keyPath) {
      // 根据 keyPath 更新 town、village 和 camera
      const [type, index] = key.split('-');
      let selected;
      if (type === 'town') {
        selected = this.treeData[index];
      } else if (type === 'village') {
        const town = this.treeData.find(t => t.children.some(v => v.name === selected.name));
        selected = town.children.find((v, i) => i === parseInt(index));
      } else if (type === 'camera') {
        const village = this.treeData.find(t => t.children.some(v => v.children.some(c => c.id === parseInt(index))));
        selected = village.children.find(v => v.children.some(c => c.id === parseInt(index)));
      }
      this.town = selected.name;
      this.village = selected.name;
      // 执行其他操作，例如获取摄像头列表
      this.getVCList();
    },

    // 处理菜单选择事件
    handleSelect(key, keyPath) {
      // 根据 keyPath 更新 town、village 和 camera
      const [type, index] = key.split('-');
      let selected;
      if (type === 'town') {
        selected = this.treeData[index];
      } else if (type === 'village') {
        const town = this.treeData.find(t => t.children.some(v => v.name === selected.name));
        selected = town.children.find((v, i) => i === parseInt(index));
      } else if (type === 'camera') {
        const village = this.treeData.find(t => t.children.some(v => v.children.some(c => c.id === parseInt(index))));
        selected = village.children.find(v => v.children.some(c => c.id === parseInt(index)));
      }
      this.town = selected.name;
      this.village = selected.name;
      // 执行其他操作，例如获取摄像头列表
      this.getVCList();
    },

    //获取图片
    async getPhotos() {
      const { data: res } = await this.$http.get('/video-configuration/photos', {
        params: {
          code: this.code,
          queryDate: this.queryDate,
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取图片列表失败')
      }
      this.imageList = res.data
    },

    async getCodeList() {
      const { data: res } = await this.$http.get('/dict/LVs', {
        params: {
          name: '图片监测点'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取图片列表失败')
      }
      this.map = res.data;
      // 使用 Object.entries() 将 map 对象转换为数组
      this.codeList = this.map.map(object => ({
        value: object.value,
        label: object.label
      }))
      console.log(this.codeList);
    },

    // 显示放大的图片
    showEnlargedImage(index) {
      this.currentEnlargedIndex = index;
    },

    // 关闭模态框的方法
    closeModal() {
      this.currentEnlargedIndex = -1;
    },

    // 前一张图片
    previousImage() {
      if (this.currentEnlargedIndex > 0) {
        this.currentEnlargedIndex--;
      }
    },
    // 后一张图片
    nextImage() {
      if (this.currentEnlargedIndex < this.imageList.length - 1) {
        this.currentEnlargedIndex++;
      }
    }
  },
  created() {
    this.getVCList();
    this.getTreeData(); // 获取树形数据
    this.getCodeList();
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#bread {
  height: 3%;
  width: 100%;
}

#main {
  height: 94.5%;
  width: 99%;
  display: flex;
  margin: 10px;
  border: 1px solid rgb(212, 212, 212);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background-color: rgb(255, 255, 255);
}

#main-aside {
  height: 100%;
  width: 200px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background-color: rgb(249, 249, 249);
}

#main-body {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
}

#search-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

#photo-frame {
  max-height: 700px;
  /* 根据需要调整最大高度 */
  overflow-y: auto;
  /* 允许垂直方向滚动 */
  width: 100%;
  /* 根据需要调整宽度 */
  padding: 10px;
  background-color: #f9f9f9;
  margin-bottom: 20px;
  /* 根据需要调整 */
}

.photo-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  /* 图片之间的间距 */
}

.photo-display {
  flex: 0 0 auto;
  /* 允许图片宽度自动调整 */
  max-width: calc(25% - 10px);
  /* 根据需要调整每行图片的数量 */
  height: auto;
  border: 1px solid #ddd;
  padding: 3px;
  background-color: #fff;
}

.enlarged-image-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

.enlarged-photo-display {
  max-width: 90%;
  max-height: 90%;
}

.prev-button,
.next-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  padding: 10px;
  color: #fff;
  font-size: 24px;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  padding: 10px;
  font-size: 24px;
  color: #fff;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
}

.prev-button {
  left: 10px;
}

.next-button {
  right: 10px;
}
</style>
