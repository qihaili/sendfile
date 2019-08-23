<template>
  <div>
    <el-card style="width: 560px; min-height: 400px; display: inline-block; margin: 10px;">
      <div style="min-height: 344px;">
        <div v-if="share" style="min-height: 310px;">
          <span>文件（点击文件名下载）</span>
          <el-divider></el-divider>
          <files :share="share" :on-removed="shareRemoved" style="margin: 0px 50px 0px 50px;"></files>
        </div>
        <div v-else-if="needPassword" style="min-height: 310px;">
          <el-row style="height: 70px;"></el-row>
          <el-row>
            <span>文件已加密，请输入密码</span>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-input v-model="password" style="width: 300px; margin-right: 10px;" @keyup.enter.native="getShare" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
            <el-button type="primary" @click="getShare">解锁</el-button>
          </el-row>
        </div>
        <div v-else style="min-height: 310px; width: 100%; display: table; text-align: center;">
          <p style="font-size: large; vertical-align: middle; display: table-cell;">{{ errorMsg }}</p>
        </div>
        <div style="margin-top: 20px; width: 100%;">
          <el-link type="primary" @click="gotoUpload">使用SendFile共享文件</el-link>
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
    getShare() {
      var loading = this.$loading()
      axios.get(
        `/api/shares/${this.$route.params.shareId}`, {
          headers: {
            password: this.password
          }
        }
      ).then((data) => {
          this.share = data.data
          this.share.password = this.password
      }).catch((error) => {
        if (error.response.status == 401) {
          this.needPassword = true
          if(this.password) {
            this.$message.error('密码错误')
          }
        } else {
          this.errorMsg = error.response.data.message ? error.response.data.message : error
        }
      }).finally(() => {
          loading.close()
      })
    },
    shareRemoved(share) {
      this.errorMsg = "文件已过期"
      this.needPassword = false
      this.share = null
    }
  }
}
</script>
