<template>
  <div>
    <div class="mycard2-upload">
      <div v-if="errorMsg">
        <el-row style="height: 450px; width: 100%; display: table; text-align: center;">
          <p style="display: table-cell; vertical-align: middle; color: #F56C6C; font-size: large;"><i class="el-icon-warning" style="margin-right: 5px;"></i><span v-if="errorMsg.msg">{{ $t(errorMsg.msg) }}</span><span v-else>{{ errorMsg }}</span></p>
        </el-row>
        <el-row style="text-align: center; width: 100%;">
          <el-link type="primary" icon="el-icon-s-home" @click="backToHome">{{$t('msg.uploaded.backToHomeLink')}}</el-link>
        </el-row>
      </div>
      <div v-else-if="!isChooseUpload" class="upload-panel">
        <!-- <div style="text-align: left; max-width: 358px; margin: auto;"> -->
        <el-upload
          drag
          ref="upload"
          action=""
          :show-file-list="true"
          :auto-upload="false"
          :multiple="true"
          :on-change="onChange"
          class="upload-component"
          >
          <!-- <el-button slot="trigger" type="primary" icon="el-icon-circle-plus">添加文件</el-button> -->
          <i class="el-icon-circle-plus" style="font-size: 3.7rem; color: #C0C4CC; margin: 2rem 0 1.5rem; line-height: 50px;"></i>
          <div class="el-upload__text" style="font-size: 1rem;">{{$t('msg.upload.dragFile.1')}}<em>{{$t('msg.upload.dragFile.2')}}</em></div>
          <!-- <el-button type="success" icon="el-icon-upload" @click="submitUpload" :disabled="fileList === null" style="margin-left: 50px">上传</el-button> -->
          <div slot="tip" class="el-upload__tip" style="text-align: center;" v-if="config"><span v-if="!config.share.maxFileSize.startsWith('-1')">{{$t('msg.upload.tip.normal',{size:this.config.share.maxFileSize})}}</span><span v-else>{{$t('msg.upload.tip.anySize')}}</span></div>
        </el-upload>
        <el-row style="margin-top: 5px; line-height: 42px;">
          <span v-if="$i18n.locale == 'en-US'" style="color: #606266; font-size: medium;">{{$t('msg.upload.expire')}}</span>
          <el-select v-if="config" v-model="ttl" style="width: 150px; margin: 0px 10px 0px 10px;">
            <el-option
              v-for="(item, index) in config.share.ttlOptions"
              :key="index"
              :label="item.value.slice(0, item.value.length-1) + $t('msg.durationUnit.' + item.value.charAt(item.value.length-1))"
              :value="item.value">
            </el-option>
          </el-select>
          <span v-if="$i18n.locale == 'zh-CN'" style="color: #606266; font-size: medium;">{{$t('msg.upload.expire')}}</span>
        </el-row>
        <el-row style="margin-top: 5px; line-height: 42px;">
          <el-checkbox v-model="passwordEnabled">{{$t('msg.upload.enablePassword')}}</el-checkbox>
          <el-input style="width: 200px; margin-left: 10px;" v-if="passwordEnabled" v-model="password" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
          <!-- <el-input v-model="password" style="width: 300px; margin-right: 10px;" @keyup.enter.native="authorize" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input> -->
        </el-row>
        <el-button type="primary" icon="el-icon-upload" @click="submitUpload" :disabled="fileList === null || fileList.length == 0" style="width: 100%; margin-top: 10px;">{{$t('msg.upload.uploadButton')}}</el-button>
        <!-- </div> -->
      </div>
      <div v-else>
        <el-progress type="circle" :stroke-width="18" :percentage="uploadPercentage" :status="uploadStatus" style="margin-top: 20px;"/>
        <div style="height: 20px; line-height: 20px">
          <span style="font-size: small">{{ speed }}</span>
        </div>
        <div v-if="share" style="text-align: left; position: relative; height: 280px;">
          <span style="font-size: small" v-if="config"><humanReadableDuration :ttl="this.share.ttl"/></span>
          <div style="margin: 20px 5px">
            <el-row>{{$t('msg.uploaded.downloadLink')}}</el-row>
            <el-input :value="address">
              <el-button slot="append" v-clipboard:copy="address" v-clipboard:success="onCopySuccess" type="primary" size="mini">{{$t('msg.uploaded.copyLinkButton')}}</el-button>
            </el-input>
            <el-row style="font-size: small"><i class="el-icon-warning" style="margin-right: 5px"/>{{$t('msg.uploaded.tip')}}</el-row>
          </div>
          <!-- <div style="text-align: center; margin-top: 100px;"> -->
          <div style="position: absolute; bottom: 0px; text-align: center; width: 100%;">
            <el-link type="primary" icon="el-icon-s-home" @click="backToHome">{{$t('msg.uploaded.backToHomeLink')}}</el-link>
          </div>
        </div>
        <div v-else style="margin: 50px 5px">
          <span>{{$t('msg.message.uploading')}}</span>
          <el-row>
            <el-button @click="cancelUpload" style="margin-top: 20px;">{{$t('msg.upload.cancel')}}</el-button>
          </el-row>
        </div>
      </div>
    </div>
    <transition>
      <div class="mycard2" v-if="uploadedList && uploadedList.length > 0">
        <transition-group name="list">
          <el-card v-for="uploadedShare in uploadedList" :key="uploadedShare.id" style="margin: 10px 0px; box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);">
            <files :share="uploadedShare" :on-removed="shareRemoved"></files>
          </el-card>
        </transition-group>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from 'axios'
import files from './Files'
import humanReadableDuration from './HumanReadableDuration'
export default {
  components: {
    files,
    humanReadableDuration
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
      uploadedList: null,
      fileList: null,
      passwordEnabled: false,
      password: null,
      showPassword: false,
      errorMsg: null,
      cancelToken: null
    }
  },
  computed: {
    address: function() {
      return window.location.href + this.share.id
    }
  },
  async created() {
    let loading = this.$loading()

    try {
      // 获取后端配置信息
      await axios.get('/api/config')
      .then((data) => {
        this.config = data.data
        for(let option of this.config.share.ttlOptions) {
          if(option.defaultOption) {
            this.ttl = option.value
          }
        }
        if(!this.ttl) {
          this.ttl = this.config.share.ttlOptions[0].value
        }
      }).catch((error) => {
        this.errorMsg = error.response.data.message || error
      })
      if (this.errorMsg) {
        return
      }

      // 获取owner权限
      let storedList = localStorage.getItem('uploaded') == null ? [] : JSON.parse(localStorage.getItem('uploaded'))
      let authorizedShares = []
      for (let storedShare of storedList) {
        await axios.post('/api/shares/authorize', storedShare)
        .then(() => {
          authorizedShares.push(storedShare)
        })
      }
      this.uploadedList = authorizedShares;
      this.save()
      if (this.errorMsg) {
        return
      }
    } finally {
      loading.close()
    }
  },
  methods: {
    submitUpload() {
      let fd = new FormData()
      fd.append('ttl', this.ttl)
      if(this.passwordEnabled && this.password) {
        fd.append('password', this.password)
      }
      let totalSize = 0
      for(let file of this.fileList) {
        totalSize += file.size
        fd.append('file', file.raw)
      }
      let maxSize = this.util.parseDataSize(this.config.share.maxFileSize)
      if (totalSize < maxSize) {
        this.cancelToken = axios.CancelToken.source();
        axios.post(
          '/api/shares',
          fd,
          {
            onUploadProgress: this.showProgress,
            cancelToken: this.cancelToken.token
          }
        ).then((response) => {
          this.handleSuccess(response.data)
        }).catch((error) => {
          this.handleError(error)
        })
        this.isChooseUpload = true
      } else {
        this.$message.error(this.$t('msg.message.fileTooBig', {size: this.config.share.maxFileSize}))
      }
    },
    cancelUpload() {
      if (this.cancelToken) {
        this.cancelToken.cancel()
      }
    },
    onChange(file, fileList) {
      this.fileList = fileList
    },
    handleSuccess(response) {
      this.$message.success(this.$t('msg.message.uploadSuccess'))
      this.isSuccess = true
      this.share = response
      this.uploadStatus = 'success'
      this.speed = null
      this.uploadedList.unshift(this.share)
      this.save()
    },
    handleError(error) {
      if (axios.isCancel(error)) {
        this.backToHome()
      } else {
        this.$message.error(this.$t('msg.message.uploadFail', {errMsg: error.response.data.message || error}))
        this.errorMsg = error.response.data.message || error
        this.uploadStatus = 'exception'
      }
    },
    showProgress(event) {
      this.uploadPercentage = parseInt(event.loaded*100 / event.total)

      // 计算速度
      let now = new Date().getTime()
      let perTime = (now - this.lastLoadTime) / 1000  // 秒
      if(perTime > 1) {
        let perLoad = event.loaded - this.lastLoaded
        this.lastLoaded = event.loaded
        this.lastLoadTime = now
        let speed = perLoad / perTime

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
    onCopySuccess() {
      this.$message.success(this.$t('msg.message.linkCopied'))
    },
    backToHome() {
      this.isChooseUpload = false
      this.uploadStatus = null
      this.uploadPercentage = 0
      this.share = null
      this.fileList = null
      this.passwordEnabled = false
      this.password = null
    },
    shareRemoved(share) {
      let index = -1
      for(let i=0; i<this.uploadedList.length; i++) {
        let uploadedShare = this.uploadedList[i]
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
      let saveList = JSON.parse(JSON.stringify(this.uploadedList))
      for(let share of saveList) {
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
  min-height: 500px;
  width: 500px;
  /* height: 450px; */
  margin: 10px;
  display: inline-block;
  vertical-align: top;
}
.list-enter, .list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
@media (min-width: 1301px) {
  .mycard2-upload {
    /* max-width: 440px; */
    min-height: 500px;
    width: 580px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .upload-panel {
    text-align: left;
    max-width: 358px;
    margin: auto;
  }
  .upload-component {
    min-height: 340px;
  }
  .mycard2 {
    /* max-width: 440px; */
    min-height: 500px;
    width: 580px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
}
@media (min-width: 501px) and (max-width: 1300px) {
  .mycard2-upload {
    /* max-width: 440px; */
    min-height: 500px;
    width: 430px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .upload-panel {
    text-align: left;
    max-width: 358px;
    margin: auto;
  }
  .upload-component {
    min-height: 340px;
  }
  .mycard2 {
    /* max-width: 440px; */
    min-height: 500px;
    width: 430px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
}
@media (max-width: 500px) {
  .hello {
    background-color: red
  }
  .mycard2-upload {
    /* max-width: 440px; */
    min-height: 410px;
    min-width: 280px;
    /* max-width: 442px; */
    /* width: 100%; */
    /* height: 450px; */
    margin-bottom: 10px;
    /* display: inline-block; */
    /* vertical-align: top; */
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
    padding: 10px;
  }
  .mycard2-upload .el-upload-dragger {
    width:280px;
    height:140px;
  }
  .upload-panel {
    text-align: left;
    max-width: 280px;
    /* width: 100%; */
    margin: auto;
  }
  .upload-component {
    min-height: 250px;
  }
  .mycard2 {
    /* max-width: 440px; */
    /* min-height: 500px; */
    min-width: 302px;
    /* max-width: 442px; */
    /* width: 100%; */
    /* width: 500px; */
    /* height: 450px; */
    /* margin: 10px; */
    /* display: inline-block; */
    /* vertical-align: top; */
  }
}
</style>
