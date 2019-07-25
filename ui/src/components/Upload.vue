<template>
  <div>
    <el-card class="mycard" v-if="isChooseUpload" >
      <div slot="header">
        上传文件
      </div>
      <div v-if="isChooseUpload && !isChooseDownload">
        <!-- <upload-progress :percentage="uploadPercentage" :speed="speed"/> -->
        <el-progress type="circle" :percentage="uploadPercentage" :status="uploadStatus"/>
        <div style="height: 20px; line-height: 20px">
          <span style="font-size: 14px">{{ speed }}</span>
        </div>
        <!-- <el-divider/> -->
        <div v-if="shareId != null" style="text-align: left;">
          <div style="margin: 20px 5px">
            <span>提取码</span>
            <el-input :value="shareId"></el-input>
            <span style="font-size: 12px"><i class="el-icon-warning" style="margin-right: 5px"/>拷贝提取码，在首页中输入，并点击“提取文件”</span>
          </div>
          <div style="margin: 20px 5px">
            <span>提取地址：</span>
            <el-input :value="address"></el-input>
            <span style="font-size: 12px"><i class="el-icon-warning" style="margin-right: 5px"/>拷贝地址，粘贴到浏览器地址栏中，并打开页面</span>
          </div>
        </div>
        <div v-else style="margin: 50px 5px">
          <span>等待上传成功，生成提取码。。。</span>
        </div>
      </div>
      <div v-else>
        <el-upload
          drag
          action="/api/files/upload"
          :show-file-list="true"
          :before-upload="chooseUpload"
          :before-remove="handleRemove"
          :on-success="handleSuccess"
          :on-error="handleError"
          :on-progress="showProgress">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">可上传任何文件，任何大小</div>
        </el-upload>
      </div>
    </el-card>
    <el-card class="mycard" v-if="isChooseDownload">
      <div slot="header">
        下载文件
      </div>
      <div style="text-align: left; margin-top: 70px">
        <span>输入提取码下载文件</span>
        <el-input v-model="shareId" placeholder="提取码"><el-button slot="append" @click="showFiles(shareId)">提取文件</el-button></el-input>
      </div>
    </el-card>
  </div>
</template>

<script>
// import { constants } from 'crypto';
import UploadProgress from './UploadProgress'
export default {
  components: {
    'upload-progress': UploadProgress
  },
  data() {
    return {
      isSuccess: false,
      isChooseUpload: true,
      isChooseDownload: true,
      shareId: null,
      uploadPercentage: 0,
      uploadStatus: null,
      speed: null,
      lastLoaded: 0,
      lastLoadTime: null
    }
  },
  computed: {
    address: function() {
      return window.location.href + this.shareId
    }
  },
  methods: {
    handleSuccess(response) {
      this.$message.success('上传成功')
      this.isSuccess = true
      this.shareId = response
      this.uploadStatus = 'success'
    },
    handleError() {
      this.$message.error('上传失败')
      this.uploadStatus = 'exception'
    },
    showProgress(event) {
      this.uploadPercentage = Number(event.percent.toFixed(0))

      // 计算速度
      var now = new Date().getTime()
      var perTime = (now - this.lastLoadTime) / 1000  // 秒
      if(perTime > 1) {
        var perLoad = event.loaded - this.lastLoaded
        this.lastLoaded = event.loaded
        this.lastLoadTime = now
        var speed = perLoad / perTime

        if(speed > 1024*1024*1024) { // GB
          this.speed = (speed / (1024*1024*1024)).toFixed(1) + ' GB/s'
        } else if(speed > 1024*1024) {
          this.speed = (speed / (1024*1024)).toFixed(1) + ' MB/s'
        } else if(speed > 1024) {
          this.speed = (speed / 1024).toFixed(1) + ' KB/s'
        } else {
          this.speed = speed.toFixed(0) + ' B/s'
        }
      }
    },
    handleRemove() {
      return false;
    },
    chooseUpload() {
      this.isChooseUpload = true
      this.isChooseDownload = false
      this.lastLoadTime = new Date().getTime()
    },
    showFiles(shareId) {
      this.$router.push(`/${shareId}`)
    }
  }
}
</script>
<style>
.mycard {
  width: 400px;
  height: 480px;
  margin: 10px;
  display: inline-block;
}
</style>
