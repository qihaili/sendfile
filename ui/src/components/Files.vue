<template>
  <div>
    <div v-for="(file, index) in share.files" :key="index">
      <div style="width: 300px; display: inline-block; text-align: left">
        <i class="el-icon-document" style="font-size: 20px; vertical-align: middle;"></i><el-link :href="'/api/files/' + share.id + '/' + file.name" style="margin-left: 10px; vertical-align: middle; font-size: 16px; "><p style="width: 270px; margin: 0px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ file.name }}</p></el-link>
      </div>
      <div style="width: 48px; display: inline-block; text-align: right">
        <span style="vertical-align: middle; font-size: 12px;">{{ humanReadableDataSize(file.size) }}</span>
      </div>
    </div>
    <div style="margin: 10px 0px" class="el-divider el-divider--horizontal"></div>
    <div>
      <div style="width: 248px; display: inline-block; text-align: left;">
        <span style="vertical-align: middle; font-size: 12px;">文件将于 {{ humanreadableDuration(share.ttl) }} 过期</span>
      </div>
      <div style="width: 100px; display: inline-block; text-align: right;">
        <el-button type="text" style="padding: 0px" v-clipboard:copy="address" v-clipboard:success="onCopySuccess">复制链接</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  props: {
    share: Object,
    onRemoved: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      address: window.location.href + (this.$route.path == '/' ? this.share.id : ''),
      scheduledJob: null
    }
  },
  created() {
    this.scheduledJob = setInterval(this.syncShare, 3000);
  },
  destroyed() {
    clearInterval(this.scheduledJob)
  },
  methods: {
    onCopySuccess(e) {
      this.$message.success('地址已复制')
    },
    syncShare() {
      var _this = this
      console.log(this.share)
      axios.get(
          `/api/files/${this.share.id}`
        ).then(function(response) {
          _this.share.ttl = response.data.ttl
        }).catch (function (err) {
          if (err.response.status == 404) {
            _this.onRemoved(_this.share)
          }
        })
    },
    humanReadableDataSize(size) {
      var humanReadable
      if(size > 1024*1024*1024*1024) {
        humanReadable = (size / (1024*1024*1024*1024)).toFixed(1) + 'TB'
      } else if(size > 1024*1024*1024) { // GB
        humanReadable = (size / (1024*1024*1024)).toFixed(1) + 'GB'
      } else if(size > 1024*1024) {
        humanReadable = (size / (1024*1024)).toFixed(1) + 'MB'
      } else if(size > 1024) {
        humanReadable = (size / 1024).toFixed(1) + 'KB'
      } else {
        humanReadable = size.toFixed(0) + 'Bytes'
      }
      return humanReadable
    },
    humanreadableDuration(duration) {
      var humanReadable = ''
      if (duration > (1000 * 60 * 60 * 24)) {
        humanReadable += parseInt(duration / (1000 * 60 * 60 * 24)) + '天'
        duration = duration % (1000 * 60 * 60 * 24)
      }
      if (duration > (1000 * 60 * 60)) {
        humanReadable += parseInt(duration / (1000 * 60 * 60)) + '小时'
        duration = duration % (1000 * 60 * 60)
      }
      if (duration > (1000 * 60)) {
        humanReadable += parseInt(duration / (1000 * 60)) + '分钟'
        duration = duration % (1000 * 60)
      }
      if (duration > 1000) {
        humanReadable += parseInt(duration / 1000) + '秒'
        duration = duration % 1000
      }
      if (humanReadable == '') {
        humanReadable = '1秒内'
      } else {
        humanReadable += '后'
      }
      return humanReadable
    }
  }
}
</script>
<style>

</style>
