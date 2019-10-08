<template>
  <div>
    <div class="mycard2">
      <div v-if="!this.$root.adminLoggedIn">
        <el-row style="height: 70px;"></el-row>
        <el-row>
          {{ $t('msg.admin.inputPasswordPrompt') }}
        </el-row>
        <el-row>
          <el-input class="password-input" v-model="password" @keyup.enter.native="authenticate" autofocus :type="showPassword ? 'text' : 'password'"><i slot="suffix" :class="'el-input__icon iconfont ' + (showPassword ? 'icon-eye-open' : 'icon-eye-close')" style="cursor: pointer;" @click="showPassword = !showPassword"></i></el-input>
          <el-button type="primary" @click="authenticate">{{ $t('msg.admin.authenticateButton') }}</el-button>
        </el-row>
      </div>
      <transition-group v-else-if="uploadedList" name="list">
        <el-card v-for="uploadedShare in uploadedList" :key="uploadedShare.id" style="margin: 10px 0px; box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);">
          <files :share="uploadedShare" :on-removed="shareRemoved"></files>
        </el-card>
      </transition-group>
    </div>
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
      authenticated: false,
      errorMsg: null,
      uploadedList: null,
      showPassword: false,
      password: null
    }
  },
  async created() {
    await axios.get('/api/check_authentication')
    .then(() => {
      this.authenticated = true
      this.getShares()
      this.$root.adminLoggedIn = true
    })
    .catch((error) => {
      this.authenticated = false
      if(error.response.data.status != 401) {
        this.errorMsg = error.response.data.message || error
      }
    })
  },
  methods: {
    getShares() {
      axios.get('/api/shares')
      .then((data) => {
        this.uploadedList = data.data
      })
    },
    authenticate() {
      axios.post('/api/login', {password: this.password})
      .then(() => {
        this.authenticated = true
        this.getShares()
        this.$root.adminLoggedIn = true
        this.password = null
      })
      .catch(error => {
        if(error.response.data.status == 401) {
          this.$message.error('密码错误')
        } else {
          this.$message.error(error.response.data.message || error)
        }
      })
    },
    shareRemoved(share) {
      let index = -1
      for(let i=0; i<this.uploadedList.length; i++) {
        let uploadedShare = this.uploadedList[i]
        if(uploadedShare.id == share.id) {
          index = i
          break
        }
      }
      if (index > -1) {
        this.uploadedList.splice(index, 1)
        // this.save()
      }
    }
  }
}
</script>
<style>
.list-enter, .list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
@media (min-width: 1301px) {
  .mycard2 {
    /* max-width: 440px; */
    min-height: 500px;
    width: 580px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .mycard2 .password-input {
    width: 400px;
    margin-right: 10px;
  }
}
@media (min-width: 501px) and (max-width: 1300px) {
  .mycard2 {
    /* max-width: 440px; */
    min-height: 500px;
    width: 430px;
    /* height: 450px; */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  .mycard2 .password-input {
    width: 300px;
    margin-right: 10px;
  }
}
@media (max-width: 500px) {
  .mycard2 {
    /* max-width: 440px; */
    /* min-height: 500px; */
    min-width: 302px;
    /* max-width: 442px; */
    /* width: 100%; */
    /* width: 500px; */
    /* height: 450px; */
    /* margin: 10px; */
    /* display: inline-block; */
    /* vertical-align: top; */
  }
  .mycard2 .password-input {
    width: 200px;
    margin-right: 10px;
  }
}
</style>