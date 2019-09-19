<template>
  <div>
    <el-card style="max-width: 700px; min-width: 400px; min-height: 400px; display: inline-block; margin: 10px;">
      <div style="min-height: 344px;">
        <div v-if="share" style="height: 310px;">
          <span>{{ $t('msg.download.title') }}</span>
          <el-divider></el-divider>
          <files :share="share" :on-removed="shareRemoved" style="margin: 0px 20px 0px 20px;"></files>
        </div>
        <div v-else-if="needPassword" style="height: 310px;">
          <el-row style="height: 70px;"></el-row>
          <el-row>
            <span>{{ $t('msg.download.needPassword') }}</span>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-input v-model="password" style="width: 300px; margin-right: 10px;" @keyup.enter.native="authorize" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
            <el-button type="primary" @click="authorize">{{ $t('msg.download.unlockButton') }}</el-button>
          </el-row>
        </div>
        <div v-else style="height: 310px; width: 100%; display: table; text-align: center;">
          <p style="font-size: large; vertical-align: middle; display: table-cell;">{{ errorMsg }}</p>
        </div>
        <div style="margin-top: 20px; width: 100%;">
          <el-link type="primary" @click="gotoUpload">{{ $t('msg.download.gotoUploadLink') }}</el-link>
        </div>
      </div>
    </el-card>
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
