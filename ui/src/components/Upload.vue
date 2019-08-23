<template>
  <div>
    <el-card class="mycard" style="width: 400px;">
      <!-- <div slot="header">
        上传文件
      </div> -->
      <div v-if="!config" style="min-height: 300px; width: 100%; display: table; text-align: center;">
        <p style="display: table-cell; vertical-align: middle; color: #F56C6C; font-size: large;"><i class="el-icon-warning" style="margin-right: 5px;"></i>{{ errorMsg }}</p>
      </div>
      <div v-else-if="!isChooseUpload" style="text-align: left;">
        <el-upload
          ref="upload"
          drag
          action=""
          :show-file-list="true"
          :auto-upload="false"
          :multiple="true"
          :on-change="onChange"
          style="text-align: left; min-height: 310px;"
          >
          <!-- <el-button slot="trigger" type="primary" icon="el-icon-circle-plus">添加文件</el-button> -->
          <i class="el-icon-circle-plus" style="font-size: 67px; color: #C0C4CC; margin: 40px 0 16px; line-height: 50px;"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击添加文件</em></div>
          <!-- <el-button type="success" icon="el-icon-upload" @click="submitUpload" :disabled="fileList === null" style="margin-left: 50px">上传</el-button> -->
          <div class="el-upload__tip" slot="tip" style="text-align: center;" v-if="config">可上传<span v-if="config.share.maxFileSize > 0">{{this.config.share.maxFileSize}}MB</span><span v-else>任意大小</span>的文件</div>
        </el-upload>
        <el-row style="margin-top: 10px;">
          <el-select v-if="config" v-model="ttl" placeholder="请选择" size="mini" style="width: 100px;">
            <el-option
              v-for="(item, index) in config.share.ttlOptions"
              :key="index"
              :label="item.name"
              :value="item.value">
            </el-option>
          </el-select>
          <span style="margin-left: 10px; font-size: small; color: #606266;">后过期</span>
        </el-row>
        <el-row style="margin-top: 10px; line-height: 28px;">
          <el-checkbox v-model="passwordEnabled">密码保护</el-checkbox>
          <el-input style="width: 200px; margin-left: 10px;" v-if="passwordEnabled" v-model="password" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
          <!-- <el-input v-model="password" style="width: 300px; margin-right: 10px;" @keyup.enter.native="authorize" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input> -->
        </el-row>
        <el-button type="primary" icon="el-icon-upload" @click="submitUpload" :disabled="fileList === null || fileList.length == 0" style="width: 100%; margin-top: 10px;">上传</el-button>
      </div>
      <div v-else>
        <el-progress type="circle" :stroke-width="18" :percentage="uploadPercentage" :status="uploadStatus" style="margin-top: 20px;"/>
        <div style="height: 20px; line-height: 20px">
          <span style="font-size: small">{{ speed }}</span>
        </div>
        <div v-if="share" style="text-align: left; position: relative; height: 260px;">
          <span style="font-size: small" v-if="config">文件<span v-if="share.ttl">将于 {{ this.util.humanreadableDuration(share.ttl) }} </span><span v-else>永不</span>过期</span>
          <div style="margin: 20px 5px">
            <el-row>下载链接：</el-row>
            <el-input :value="address">
              <el-button slot="append" v-clipboard:copy="address" v-clipboard:success="onCopySuccess" type="primary" size="mini">复制链接</el-button>
            </el-input>
            <el-row style="font-size: small"><i class="el-icon-warning" style="margin-right: 5px"/>复制链接地址，粘贴到浏览器地址栏中，并打开页面</el-row>
          </div>
          <!-- <div style="text-align: center; margin-top: 100px;"> -->
          <div style="position: absolute; bottom: 0px; text-align: center; width: 100%;">
            <el-link type="primary" icon="el-icon-s-home" @click="backToHome">再次上传</el-link>
          </div>
        </div>
        <div v-else style="margin: 50px 5px">
          <span>正在上传，成功后生成下载链接。。。</span>
        </div>
      </div>
    </el-card>
    <transition>
      <el-card class="mycard" v-if="uploadedList.length > 0" style="width: 502px;">
        <transition-group name="list">
          <el-card v-for="uploadedShare in uploadedList" :key="uploadedShare.id" style="margin: 10px 0px;">
            <files :share="uploadedShare" :on-removed="shareRemoved"></files>
          </el-card>
        </transition-group>
      </el-card>
    </transition>
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
      isChooseUpload: false,
      share: null,
      uploadPercentage: 0,
      uploadStatus: null,
      speed: null,
      lastLoaded: 0,
      lastLoadTime: null,
      config: null,
      ttl: null,
      uploadedList: localStorage.getItem('uploaded') == null ? [] : JSON.parse(localStorage.getItem('uploaded')),
      fileList: null,
      passwordEnabled: false,
      password: null,
      showPassword: false,
      errorMsg: null
    }
  },
  computed: {
    address: function() {
      return window.location.href + this.share.id
    }
  },
  created() {
    var loading = this.$loading()
    axios.get('/api/config')
    .then((data) => {
      this.config = data.data
      for(var option of this.config.share.ttlOptions) {
        if(option.defaultOption) {
          this.ttl = option.value
        }
      }
      if(!this.ttl) {
        this.ttl = this.config.share.ttlOptions[0].value
      }
    }).catch((error) => {
      // this.$message.error(error.response.data.message ? error.response.data.message : error.toString())
      this.errorMsg = error.response.data.message ? error.response.data.message : error.toString()
    }).finally(() => {
      loading.close()
    })
    // setInterval(this.syncUploadedShareList, 3000);
  },
  methods: {
    submitUpload() {
      var fd = new FormData()
      fd.append('ttl', this.ttl)
      if(this.passwordEnabled && this.password) {
        fd.append('password', this.password)
      }
      for(var file of this.fileList) {
        fd.append('file', file.raw)
      }
      axios.post(
        '/api/shares',
        fd,
        {
          onUploadProgress: this.showProgress
        }
      ).then((response) => {
        this.handleSuccess(response.data)
      }).catch((error) => {
        this.handleError(error)
      })
      this.isChooseUpload = true
    },
    onChange(file, fileList) {
      this.fileList = fileList
    },
    handleSuccess(response) {
      this.$message.success('上传成功')
      this.isSuccess = true
      this.share = response
      this.uploadStatus = 'success'
      this.speed = null
      // this.share.ttl = null
      this.uploadedList.unshift(this.share)
      this.save()
      // localStorage.setItem('uploaded', JSON.stringify(this.uploadedList))
    },
    handleError(err) {
      // console.log(err)
      this.$message.error('上传失败。' + err)
      // var response = JSON.parse(err.message)
      // this.$message.error('上传失败。' + response.message)
      this.uploadStatus = 'exception'
    },
    showProgress(event) {
      // this.uploadPercentage = Number(event.percent.toFixed(0))
      this.uploadPercentage = parseInt(event.loaded*100 / event.total)

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
      if (this.maxFileSize > 0 && file.size > this.maxFileSize * 1024 * 1024) {
        this.$message.error('文件过大，不能超过' + this.maxFileSize + 'MB')
        return false
      }
      this.isChooseUpload = true
      this.isChooseDownload = false
      this.lastLoadTime = new Date().getTime()
    },
    onCopySuccess() {
      this.$message.success('地址已复制')
    },
    backToHome() {
      this.isChooseUpload = false
      this.uploadStatus = null
      this.uploadPercentage = 0
      this.share = null
      this.fileList = null
      this.passwordEnabled = false
      this.password = null
      // this.isChooseDownload = true
    },
    shareRemoved(share) {
      // console.log('shareRemoved')
      // var index = this.uploadedList.indexOf(share)
      var index = -1
      for(var i=0; i<this.uploadedList.length; i++) {
        var uploadedShare = this.uploadedList[i]
        if(uploadedShare.id == share.id) {
          index = i
          break
        }
      }
      if (index > -1) {
        this.uploadedList.splice(index, 1)
        this.save()
      }
    },
    save() {
      var saveList = JSON.parse(JSON.stringify(this.uploadedList))
      for(var share of saveList) {
        share.ttl = null
      }
      localStorage.setItem('uploaded', JSON.stringify(saveList))
    }
  }
}
</script>
<style>
.mycard {
  /* max-width: 440px; */
  min-height: 480px;
  /* width: auto; */
  /* height: 450px; */
  margin: 10px;
  display: inline-block;
  vertical-align: top;
}
.list-enter, .list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>
