<template>
  <el-card style="width: 400px; height: 400px; display: inline-block;" v-loading="loading">
    <div slot="header">
      文件（点击文件名下载）
    </div>
    <div v-if="loading">
      <p>正在读取文件...</p>
    </div>
    <div v-else>
      <files :share="response" v-if="response" :on-removed="shareRemoved"></files>
      <div v-else>
        <p>{{ errorMsg }}</p>
      </div>
    </div>
  </el-card>
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
      response: null,
      loading: true,
      errorMsg: null
    }
  },
  methods: {
    shareRemoved(share) {
      this.errorMsg = '文件已过期'
      this.response = null
    }
  },
  created() {
    console.log(this.$route)
    const _this = this
    axios.get(
      `/api/files/${this.$route.params.shareId}`
    ).then(
      function (data) {
        _this.response = data.data
      }, function (err) {
        _this.errorMsg = err.response.data.message
      }
    ).finally(function() {
        _this.loading = false;
    })
  }
}
</script>
