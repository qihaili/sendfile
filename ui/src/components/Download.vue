<template>
  <div>
    <div class="mycard2-download">
      <!-- <div style="min-height: 344px;"> -->
        <div v-if="share" class="download-content">
          <span>{{ $t('msg.download.title') }}</span>
          <el-divider></el-divider>
          <files :share="share" :on-removed="shareRemoved" class="files"></files>
        </div>
        <div v-else-if="needPassword" class="download-content">
          <el-row style="height: 70px;"></el-row>
          <el-row>
            <span>{{ $t('msg.download.needPassword') }}</span>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-input class="password-input" v-model="password" @keyup.enter.native="authorize" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
            <el-button type="primary" @click="authorize">{{ $t('msg.download.unlockButton') }}</el-button>
          </el-row>
        </div>
        <div v-else class="download-content" style="margin: auto; display: table; text-align: center;">
          <p style="font-size: large; vertical-align: middle; display: table-cell;">{{ errorMsg }}</p>
        </div>
        <div style="margin: 20px auto;">
          <el-link type="primary" @click="gotoUpload">{{ $t('msg.download.gotoUploadLink') }}</el-link>
        </div>
      </div>
    <!-- </div> -->
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
      share: null,
      errorMsg: null,
      needPassword: false,
      password: null,
      showPassword: false
    }
  },
  created() {
    this.getShare()
  },
  methods: {
    gotoUpload() {
      this.$router.push('/')
    },
    authorize() {
      axios.post(
        '/api/shares/authorize',
        {
          id: this.$route.params.shareId,
          password: this.password
        }
      ).then(() => {
        this.getShare()
      }).catch((error) => {
        if(error.response.data.status == 401) {
          this.$message.error(this.$t('msg.download.wrongPassword'))
        } else {
          this.$message.error(error.response.data.message ? error.response.data.message : error)
        }
      })
    },
    getShare() {
      let loading = this.$loading()
      axios.get(
        `/api/shares/${this.$route.params.shareId}`, {
          // headers: {
          //   password: this.password
          // }
        }
      ).then((data) => {
          this.share = data.data
          this.needPassword = false
      }).catch((error) => {
        if (error.response.data.status == 401) {
          this.needPassword = true
        } else {
          this.errorMsg = error.response.data.message || error
        }
      }).finally(() => {
          loading.close()
      })
    },
    shareRemoved() {
      this.errorMsg = this.$t('msg.download.fileExpired')
      this.needPassword = false
      this.share = null
    }
  }
}
</script>
<style>
@media (min-width: 1301px) {
  .mycard2-download {
    max-width: 1000px;
    /* width: 100%; */
    min-width: 280px;
    /* min-height: 400px; */
    /* display: inline-block; */
    margin: auto;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .files {
    margin: 0px 30px;
  }
  .mycard2-download .password-input {
    width: 400px;
    margin-right: 10px;
  }
  .download-content {
    height: 380px;
  }
}
@media (min-width: 501px) and (max-width: 1300px) {
  .mycard2-download {
    max-width: 650px;
    /* width: 100%; */
    min-width: 280px;
    /* min-height: 400px; */
    /* display: inline-block; */
    margin: auto;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .files {
    margin: 0px 20px;
  }
  .mycard2-download .password-input {
    width: 300px;
    margin-right: 10px;
  }
  .download-content {
    height: 310px;
  }
}
@media (max-width: 500px) {
  .mycard2-download {
    max-width: 500px;
    min-width: 280px;
    /* min-height: 400px; */
    /* display: inline-block; */
    /* margin: 10px 0; */
    margin: auto;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
    padding: 10px;
  }
  .files {
    margin: 0px 10px;
  }
  .mycard2-download .password-input {
    width: 200px;
    margin-right: 10px;
  }
  .download-content {
    height: 260px;
  }
}
</style>
