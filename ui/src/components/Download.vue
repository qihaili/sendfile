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
    <!-- <el-table v-if="response == null" :data="files" size="mini" :show-header=false>
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
    </el-table> -->
    <!-- <div v-else>
      <p>{{ response.status }} {{ response.statusText }}</p>
      <p>未找到文件，提取码错误</p>
    </div> -->
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
        console.log(data.data)
        _this.response = data.data
      }, function (err) {
        console.log(err.response)
        _this.errorMsg = err.response.data.message
      }
    ).finally(function() {
        _this.loading = false;
    })
  }
}
</script>
