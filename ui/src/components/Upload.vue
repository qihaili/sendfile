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
        <div v-if="share != null" style="text-align: left;">
          <span style="font-size: 14px" v-if="shareTTL">文件将在{{shareTTL}}天后过期</span>
          <!-- <div style="margin: 20px 5px">
            <span>提取码</span>
            <el-input :value="shareId"></el-input>
            <span style="font-size: 12px"><i class="el-icon-warning" style="margin-right: 5px"/>拷贝提取码，在首页中输入，并点击“提取文件”</span>
          </div> -->
          <div style="margin: 20px 5px">
            <span>提取地址：</span>
            <el-input :value="address">
              <el-button slot="append" v-clipboard:copy="address" v-clipboard:success="onCopySuccess" type="primary" size="mini">复制地址</el-button>
            </el-input>
            <span style="font-size: 12px"><i class="el-icon-warning" style="margin-right: 5px"/>拷贝地址，粘贴到浏览器地址栏中，并打开页面</span>
          </div>
          <div style="text-align: center;">
            <el-link type="primary" @click="backToHome">再次上传</el-link>
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
          :show-file-list="false"
          :before-upload="chooseUpload"
          :before-remove="handleRemove"
          :on-success="handleSuccess"
          :on-error="handleError"
          :on-progress="showProgress"
          >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">可上传<span v-if="maxFileSize > 0">{{this.maxFileSize}}MB</span><span v-else>任意大小</span>的文件。<span v-if="shareTTL > 0">有效期{{this.shareTTL}}天</span><span v-else>永久有效</span></div>
        </el-upload>
      </div>
    </el-card>
    <el-card class="mycard" v-if="uploadedList.length > 0">
      <el-card v-for="(uploadedShare, index) in uploadedList" :key="index" style="margin: 10px">
        <files :share="uploadedShare" :on-removed="shareRemoved"></files>
      </el-card>
      <!-- <el-card v-for="(uploadedFile, index) in uploadedList" :key="index" style="margin: 10px">
        <el-table :data="uploadedFile.files" size="mini" :show-header=false>
          <el-table-column>
            <template slot-scope="file">
              <i class="el-icon-document" style="font-size: 20px; vertical-align: middle;"></i><el-link :href="'/api/files/' + $route.params.shareId + '/' + file.row.name" style="margin-left: 10px; vertical-align: middle;">{{ file.row.name }}</el-link>
            </template>
          </el-table-column>
          <el-table-column min-width="20px">
            <template slot-scope="file">
              <span style="vertical-align: middle;">{{ humanReadableDataSize(file.row.size) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-card> -->
    </el-card>
    <!-- <el-card class="mycard" v-if="isChooseDownload">
      <div slot="header">
        下载文件
      </div>
      <div style="text-align: left; margin-top: 70px">
        <span>输入提取码下载文件</span>
        <el-input v-model="shareId" placeholder="提取码"><el-button slot="append" @click="showFiles(shareId)">提取文件</el-button></el-input>
      </div>
    </el-card> -->
  </div>
</template>

<script>
import axios from 'axios'
import files from './Files'
export default {
  components: {
    files
  },
  data() {
    return {
      isSuccess: false,
      isChooseUpload: true,
      isChooseDownload: true,
      share: null,
      uploadPercentage: 0,
      uploadStatus: null,
      speed: null,
      lastLoaded: 0,
      lastLoadTime: null,
      fileList: [],
      shareTTL: null,
      maxFileSize: null,
      uploadedList: JSON.parse(localStorage.getItem('uploaded')) == null ? [] : JSON.parse(localStorage.getItem('uploaded'))
    }
  },
  computed: {
    address: function() {
      return window.location.href + this.share.id
    }
  },
  created() {
    const _this = this
    axios.get('/api/config')
    .then(function(data) {
      _this.shareTTL = data.data.shareTTL
      _this.maxFileSize = data.data.maxFileSize
    }, function(err) {
      console.log('bbb')
    })
    .catch(function(err) {
      console.log('aaa')
    })
    // setInterval(this.syncUploadedShareList, 3000);
  },
  methods: {
    handleSuccess(response) {
      this.$message.success('上传成功')
      this.isSuccess = true
      this.share = response
      this.uploadStatus = 'success'
      this.speed = null
      this.uploadedList.push(this.share)
      localStorage.setItem('uploaded', JSON.stringify(this.uploadedList))
    },
    handleError(err) {
      var response = JSON.parse(err.message)
      console.log(response.message)

      this.$message.error('上传失败。' + response.message)
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
    chooseUpload(file) {
      console.log('最大文件大小：' + this.maxFileSize + 'MB')
      console.log(file.size)
      if (this.maxFileSize > 0 && file.size > this.maxFileSize * 1024 * 1024) {
        this.$message.error('文件过大，不能超过' + this.maxFileSize + 'MB')
        return false
      }
      this.isChooseUpload = true
      this.isChooseDownload = false
      this.lastLoadTime = new Date().getTime()
    },
    showFiles(share) {
      this.$router.push(`/${share}`)
    },
    changeFileList(file, fileList) {
      console.log(file, file.name)
      console.log(fileList)
      console.log(this.fileList)
    },
    onCopySuccess(e) {
      this.$message.success('地址已复制')
    },
    backToHome() {
      this.isChooseUpload = true
      this.isChooseDownload = true
    },
    async syncUploadedShareList() {
      var _this = this
      for (var index in this.uploadedList) {
        var uploadedShare = this.uploadedList[index]
        await axios.get(
          `/api/files/${uploadedShare.id}`
        ).then(function(response) {
          _this.uploadedList[index] = response.data
        }).catch (function (err) {
          console.log(err.response)
          if (err.response.status == 404) {
            _this.uploadedList.splice(index, 1)
          }
        }).finally(function(response) {
          localStorage.setItem('uploaded', JSON.stringify(_this.uploadedList))
          _this.uploadedList = JSON.parse(localStorage.getItem('uploaded')) == null ? [] : JSON.parse(localStorage.getItem('uploaded'))
        })
      }
      localStorage.setItem('uploaded', JSON.stringify(this.uploadedList))
    },
    shareRemoved(share) {
      var index = this.uploadedList.indexOf(share)
      if (index > -1) {
        this.uploadedList.splice(index, 1)
        localStorage.setItem('uploaded', JSON.stringify(this.uploadedList))
      }
    }
  }
}
</script>
<style>
.mycard {
  width: 450px;
  height: 450px;
  margin: 10px;
  display: inline-block;
}
</style>
