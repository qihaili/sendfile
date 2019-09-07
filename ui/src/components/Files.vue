<template>
  <div>
    <el-row v-for="(file, index) in share.files" :key="index">
      <div style="display: inline-block; float: left;">
        <!-- <el-link :href="'/api/files/' + share.id + '/' + file.name" :underline="false" style="vertical-align: middle; font-size: 16px;" icon="el-icon-document"><p style="width: 280px; margin: 0px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; font-size: 14px;">{{ file.name }}</p></el-link> -->
        <el-tooltip :content="file.name" placement="top"><el-link :href="'/api/shares/' + share.id + '/' + file.name" style="vertical-align: middle; font-size: large; margin: 0px;" icon="el-icon-document"><div style="max-width: 340px; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ file.name }}</div></el-link></el-tooltip>
      </div>
      <div style="display: inline-block; float: right;">
        <span style="vertical-align: middle; font-size: small;">{{ humanReadableDataSize(file.size) }}</span>
      </div>
    </el-row>
    <div style="margin: 10px 0px" class="el-divider el-divider--horizontal"></div>
    <el-row>
      <div style="display: inline-block; float: left; text-align: left;">
        <!-- <el-row><span style="vertical-align: middle; font-size: xx-small;" v-if="share.lastModified">上传于 {{ new Date(share.lastModified).toLocaleString() }}</span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;"><span v-if="share.ttl">{{$t('msg.expireTip.normal', {ttl: this.util.humanreadableDuration(share.ttl)})}}</span><span v-else>{{$t('msg.expireTip.neverExpire')}}</span></span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;">{{$t('msg.humanReadableDuration', {day: this.util.getDuration(share.ttl).day, hour: this.util.getDuration(share.ttl).hour, minute: this.util.getDuration(share.ttl).minute})}}</span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;"><i18n path="msg.humanReadableDuration" tag="span"><div slot="duration">{{duration.day}}</div></i18n></span></el-row> -->
        <el-row><span style="vertical-align: middle; font-size: small;"><humanReadableDuration :ttl="this.share.ttl"/></span></el-row>
      </div>
      <div style="display: inline-block; float: right;">
        <el-tooltip :content="$t('msg.files.copyLink')" placement="top"><el-button size="mini" v-clipboard:copy="address" v-clipboard:success="onCopySuccess" icon="el-icon-document-copy"></el-button></el-tooltip>
        <el-tooltip v-if="share.password" :content="$t('msg.files.copyPassword')" placement="top"><el-button size="mini" v-clipboard:copy="share.password" v-clipboard:success="onCopyPasswordSuccess" icon="el-icon-key"></el-button></el-tooltip>
        <el-tooltip v-if="share.token" :content="$t('msg.files.deleteFile')" placement="top"><el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteShare" :loading="deleteLoading"></el-button></el-tooltip>
      </div>
    </el-row>
  </div>
</template>
<script>
import axios from 'axios'
import humanReadableDuration from './HumanReadableDuration'
import { clearInterval, setInterval } from 'timers';
export default {
  props: {
    share: {
      type: Object,
    },
    onRemoved: {
      type: Function,
      required: true
    }
  },
  components: {
    humanReadableDuration
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
      this.$message.success(this.$t('msg.files.linkCopied'))
    },
    onCopyPasswordSuccess() {
      this.$message.success(this.$t('msg.files.passwordCopied'))
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
        }).catch ((error) => {
          if (error.response.data.status == 404) {
            this.onRemoved(this.share)
          } else {
            // this.$message.error({message: '<p>' + err.response.status + '-' + err.response.statusText + '</p><p>' + err.response.data + '</p>', dangerouslyUseHTMLString: true})
            this.$message.error(error.response.data.message ? error.response.data.message : error.toString())
          }
        })
    },
    refreshTtl() {
      this.share.ttl -= 10000
    },
    deleteShare() {
      this.deleteLoading = true
      this.$confirm(this.$t('msg.files.confirmDelete'), {
        type: 'warning',
        confirmButtonText: this.$t('msg.files.okButton'),
        cancelButtonText: this.$t('msg.files.cancelButton')
      }).then(() => {
        axios.delete(
          `/api/shares/${this.share.id}`, {
            headers: {
              token: this.share.token
            }
          }
          ).then(() => {
            this.onRemoved(this.share)
          }).catch((error) => {
            this.$message.error(error.response.data.message ? error.response.data.message : error.toString())
          }).finally(()=> {
            this.deleteLoading = false
          })
      }).catch(() => {
        this.deleteLoading = false
      })
    },
    humanReadableDataSize(size) {
      let humanReadable
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
