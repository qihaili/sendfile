<template>
  <div>
    <el-row v-for="(file, index) in share.files" :key="index">
      <div style="display: inline-block; float: left;">
        <!-- <el-link :href="'/api/files/' + share.id + '/' + file.name" :underline="false" style="vertical-align: middle; font-size: 16px;" icon="el-icon-document"><p style="width: 280px; margin: 0px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; font-size: 14px;">{{ file.name }}</p></el-link> -->
        <el-tooltip :content="file.name" placement="top"><el-link :href="'/api/shares/' + share.id + '/' + file.name" style="vertical-align: middle; font-size: medium; margin: 0px;" icon="el-icon-document"><div style="max-width: 280px; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ file.name }}</div></el-link></el-tooltip>
      </div>
      <div style="display: inline-block; float: right;">
        <span style="vertical-align: middle; font-size: x-small;">{{ humanReadableDataSize(file.size) }}</span>
      </div>
    </el-row>
    <div style="margin: 10px 0px" class="el-divider el-divider--horizontal"></div>
    <el-row>
      <div style="display: inline-block; float: left; text-align: left;">
        <!-- <el-row><span style="vertical-align: middle; font-size: xx-small;" v-if="share.lastModified">上传于 {{ new Date(share.lastModified).toLocaleString() }}</span></el-row> -->
        <el-row><span style="vertical-align: middle; font-size: xx-small;">文件<span v-if="share.ttl">将于 {{ this.util.humanreadableDuration(share.ttl) }} </span><span v-else>永不</span>过期</span></el-row>
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
    onCopySuccess() {
      this.$message.success('地址已复制')
    },
    syncShare() {
      if (this.countdown) {
        clearInterval(this.countdown)
      }
      axios.get(
          `/api/shares/${this.share.id}`
        ).then((response) => {
          this.share.ttl = response.data.ttl
          this.share.files = response.data.files
          // _this.share = response.data
          if (this.share.ttl) {
            this.countdown = setInterval(this.refreshTtl, 10000)
          }
        }).catch ((err) => {
          if (err.response.status == 404) {
            this.onRemoved(this.share)
          } else {
            this.$message.error({message: '<p>' + err.response.status + '-' + err.response.statusText + '</p><p>' + err.response.data + '</p>', dangerouslyUseHTMLString: true})
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
          `/api/shares/${this.share.id}`, {
            headers: {
              token: this.share.token
            }
          }
          ).then(() => {
            this.onRemoved(this.share)
          }).finally(()=> {
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
    }
  }
}
</script>
<style>

</style>
