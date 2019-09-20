<template>
  <div>
    <table v-if="share.files" style="width: 100%;">
      <tr v-for="(file, index) in share.files" :key="index">
        <td width="20">
          <i class="el-icon-document" style="vertical-align: middle;"/>
        </td>
        <td min-width="200" align="left">
          <el-tooltip :content="file.name" placement="top"><el-link :href="'/api/shares/' + share.id + '/' + file.name" style="vertical-align: middle; margin: 0px; font-size: 1rem"><div style="text-align: left; word-break: break-all;">{{ file.name }}</div></el-link></el-tooltip>
        </td>
        <td width="55" align="right">
          <span style="vertical-align: middle; font-size: 0.8rem;">{{ humanReadableDataSize(file.size) }}</span>
        </td>
      </tr>
    </table>
    <!-- <el-table :data="share.files" v-if="share.files" style="width: 100%;" :show-header="false" size="mini">
      <el-table-column width="35">
        <i class="el-icon-document" style="vertical-align: middle;"/>
      </el-table-column>
      <el-table-column>
        <template min-width="200" slot-scope="file">
          <el-tooltip :content="file.row.name" placement="top"><el-link :href="'/api/shares/' + share.id + '/' + file.row.name" style="vertical-align: middle; margin: 0px;"><div style="text-align: left; word-break: break-all;">{{ file.row.name }}</div></el-link></el-tooltip>
        </template>
      </el-table-column>
      <el-table-column width="80" align="right">
        <template slot-scope="file">
          <span style="vertical-align: middle; font-size: small;">{{ humanReadableDataSize(file.row.size) }}</span>
        </template>
      </el-table-column>
    </el-table> -->
    <div style="margin: 10px 0px" class="el-divider el-divider--horizontal"></div>
    <el-row>
      <div style="display: inline-block; float: left; text-align: left;">
        <!-- <el-row><span style="vertical-align: middle; font-size: xx-small;" v-if="share.lastModified">上传于 {{ new Date(share.lastModified).toLocaleString() }}</span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;"><span v-if="share.ttl">{{$t('msg.expireTip.normal', {ttl: this.util.humanreadableDuration(share.ttl)})}}</span><span v-else>{{$t('msg.expireTip.neverExpire')}}</span></span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;">{{$t('msg.humanReadableDuration', {day: this.util.getDuration(share.ttl).day, hour: this.util.getDuration(share.ttl).hour, minute: this.util.getDuration(share.ttl).minute})}}</span></el-row> -->
        <!-- <el-row><span style="vertical-align: middle; font-size: small;"><i18n path="msg.humanReadableDuration" tag="span"><div slot="duration">{{duration.day}}</div></i18n></span></el-row> -->
        <el-row><span style="vertical-align: middle; font-size: 0.8rem;"><humanReadableDuration :ttl="this.share.ttl"/></span></el-row>
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
