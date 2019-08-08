<template>
  <div>
    <el-row v-for="(file, index) in share.files" :key="index">
      <div style="display: inline-block; float: left;">
        <!-- <el-link :href="'/api/files/' + share.id + '/' + file.name" :underline="false" style="vertical-align: middle; font-size: 16px;" icon="el-icon-document"><p style="width: 280px; margin: 0px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; font-size: 14px;">{{ file.name }}</p></el-link> -->
        <el-link :href="'/api/files/' + share.id + '/' + file.name" style="vertical-align: middle; font-size: 14px; margin: 0px;" icon="el-icon-document"><div style="max-width: 290px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ file.name }}</div></el-link>
      </div>
      <div style="display: inline-block; float: right;">
        <span style="vertical-align: middle; font-size: 12px;">{{ humanReadableDataSize(file.size) }}</span>
      </div>
    </el-row>
    <div style="margin: 10px 0px" class="el-divider el-divider--horizontal"></div>
    <el-row>
      <div style="display: inline-block; float: left;">
        <span style="vertical-align: middle; font-size: 12px;" v-if="share.ttl">文件将于 {{ humanreadableDuration(share.ttl) }} 过期</span>
      </div>
      <div style="display: inline-block; float: right;">
        <el-tooltip content="复制链接" placement="top"><el-button size="mini" v-clipboard:copy="address" v-clipboard:success="onCopySuccess" icon="el-icon-document-copy"></el-button></el-tooltip>
        <el-tooltip content="删除文件" placement="top" v-if="deletable"><el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteShare" :loading="deleteLoading"></el-button></el-tooltip>
      </div>
    </el-row>
  </div>
</template>
<script>
import axios from 'axios'
import { clearInterval, setInterval } from 'timers';
export default {
  props: {
    share: {
      type: Object,
    },
    deletable: {
      type: Boolean,
      default: false
    },
    onRemoved: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      address: window.location.href + (this.$route.path == '/' ? this.share.id : ''),
      syncSharePolling: null,
      countdown: null,
      deleteLoading: false
    }
  },
  created() {
    this.syncShare()
    this.syncSharePolling = setInterval(this.syncShare, 120000);  // 两分钟
  },
  destroyed() {
    clearInterval(this.syncSharePolling)
  },
  methods: {
    onCopySuccess(e) {
      this.$message.success('地址已复制')
    },
    syncShare() {
      var _this = this
      clearInterval(this.countdown)
      axios.get(
          `/api/files/${this.share.id}`
        ).then(function(response) {
          _this.share.ttl = response.data.ttl
          _this.share.files = response.data.files
          // _this.share = response.data
          _this.countdown = setInterval(_this.refreshTtl, 10000)
        }).catch (function (err) {
          if (err.response.status == 404) {
            _this.onRemoved(_this.share)
          } else {
            _this.$message.error({message: '<p>' + err.response.status + '-' + err.response.statusText + '</p><p>' + err.response.data + '</p>', dangerouslyUseHTMLString: true})
          }
        })
    },
    refreshTtl() {
      this.share.ttl -= 10000
    },
    deleteShare() {
      this.deleteLoading = true
      this.$confirm('确定删除文件共享？', {
        type: 'warning'
      }).then(() => {
        axios.delete(
          `/api/files/${this.share.id}`, {
            headers: {
              token: this.share.token
            }
          }
          ).then((response) => {
            this.onRemoved(this.share)
          }).finally((response)=> {
            this.deleteLoading = false
          })
      }).catch(() => {

      }).finally(() => {
        this.deleteLoading = false
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
        // duration = duration % (1000 * 60)
      }
      // if (duration > 1000) {
      //   humanReadable += parseInt(duration / 1000) + '秒'
      //   duration = duration % 1000
      // }
      if (humanReadable == '') {
        humanReadable = '1分钟内'
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
