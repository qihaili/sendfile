<template>
  <div>
    <el-card class="mycard" v-if="isChooseUpload" >
      <div slot="header">
        上传文件
      </div>
      <div v-if="isChooseUpload && !isChooseDownload">
        <el-progress type="circle" :percentage="uploadPercentage" :status="uploadStatus"/>
        <el-divider/>
        <div v-if="shareId != null" style="text-align: left;">
          <div style="margin: 20px 5px">
            <span>提取码</span>
            <el-input :value="shareId"></el-input>
          </div>
          <div style="margin: 20px 5px">
            <span>提取地址：</span>
            <el-input :value="address"></el-input>
          </div>
        </div>
      </div>
      <div v-else>
        <el-upload
          drag
          action="/api/files/upload"
          :show-file-list="true"
          :before-upload="chooseUpload"
          :before-remove="handleRemove"
          :on-success="handleSuccess"
          :on-error="handleError"
          :on-progress="showProgress">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">可上传任何文件，任何大小</div>
        </el-upload>
      </div>
    </el-card>
    <el-card class="mycard" v-if="isChooseDownload">
      <div slot="header">
        下载文件
      </div>
      <el-input v-model="shareId" placeholder="提取码" style="margin-top: 70px"><el-button slot="append" @click="showFiles(shareId)">提取文件</el-button></el-input>
    </el-card>
  </div>
</template>

<script>
// import { constants } from 'crypto';
export default {
  data() {
    return {
      isSuccess: false,
      isChooseUpload: true,
      isChooseDownload: true,
      shareId: null,
      uploadPercentage: 0,
      uploadStatus: null
    }
  },
  computed: {
    address: function() {
      return window.location.href + this.shareId
    }
  },
  methods: {
    handleSuccess(response) {
      this.$message.success('上传成功')
      this.isSuccess = true
      this.shareId = response
      // this.address = 
      this.uploadStatus = 'success'
    },
    handleError() {
      this.$message.error('上传失败')
    },
    showProgress(event) {
      this.uploadPercentage = Number(event.percent.toFixed(2))
    },
    handleRemove() {
      return false;
    },
    chooseUpload() {
      this.isChooseUpload = true;
      this.isChooseDownload = false;
    },
    showFiles(shareId) {
      this.$router.push(`/${shareId}`)
    }
  }
}
</script>
<style>
.mycard {
  width: 400px;
  height: 450px;
  margin: 10px;
  display: inline-block;
}
</style>
