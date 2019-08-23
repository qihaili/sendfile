<template>
  <div>
    <el-card style="width: 560px; height: 400px; display: inline-block; margin: 10px;">
      <div slot="header">
        文件（点击文件名下载）
      </div>
      <div style="height: 300px; position: relative">
          <div v-if="loading">
            <p>正在读取文件...</p>
          </div>
          <div v-else>
            <files :share="share" v-if="share" :on-removed="shareRemoved" style="margin: 0px 50px 0px 50px;"></files>
            <div v-else-if="needPassword" style="margin-top: 40px;">
              <span>文件已加密，请输入密码</span>
              <el-row style="margin-top: 10px;">
                <el-input size="small" v-model="password" style="width: 200px; margin-right: 10px;">
                </el-input>
                <el-button size="small" type="primary" @click="getShare">解锁</el-button>
              </el-row>
            </div>
            <div v-else>
              <p>{{ errorMsg }}</p>
            </div>
          </div>
          <div style="position: absolute; bottom: 0px; width: 100%;">
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
      // files: [],
      share: null,
      loading: true,
      errorMsg: null,
      needPassword: false,
      password: null
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
        } else {
          this.errorMsg = error.response.data.message
        }
      }).finally(() => {
          this.loading = false;
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
