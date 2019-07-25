<template>
  <el-card style="width: 400px; height: 400px; display: inline-block;" v-loading="loading">
    <div slot="header">
      文件（点击文件名下载）
    </div>
    <el-table v-if="response == null" :data="files" size="mini" :show-header=false>
      <el-table-column>
        <template slot-scope="file">
          <el-link :href="'/api/files/' + $route.params.shareId + '/' + file.row.name" style="margin-left: 10px">{{ file.row.name }}</el-link>
        </template>
      </el-table-column>
    </el-table>
    <div v-else>
      <p>{{ response.status }} {{ response.statusText }}</p>
      <p>未找到文件，提取码错误</p>
    </div>
  </el-card>
</template>
<script>
import axios from 'axios'
export default {
  data() {
    return {
      files: [],
      response: null,
      loading: true
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
        _this.files = data.data
      }, function (err) {
        console.log(err.response)
        _this.response = err.response
        // _this.$message({
        //   type: 'error',
        //   dangerouslyUseHTMLString: true,
        //   message: `<p>${err.response.data.message}</p><p style="font-size: 12px">${err.response.status} ${err.response.statusText}</p>`
        //   })
      }
    ).finally(function() {
        _this.loading = false;
    })
  }
}
</script>
