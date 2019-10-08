<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Upload/> -->
    <el-row>
      <div>
        <i class="el-icon-s-promotion" style="margin-right: 1rem; font-size: 4rem; color: #409EFF; vertical-align: middle;"></i><span style="font-size: 2.5rem; font-weight: bold; color: #606266; vertical-align: middle;">Send File</span>
      </div>
      <div style="line-height: 40px; margin: 0.5rem;">
        <el-dropdown @command="changeLang" class="header-element">
          <span style="cursor: pointer;">
          {{ $t('msg.languageName') }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="(value, key) in $i18n.messages" :command="key" :key="key" :disabled="$i18n.locale == key">{{ value.msg.languageName }}</el-dropdown-item>
            <!-- <el-dropdown-item command="en-US">English</el-dropdown-item> -->
          </el-dropdown-menu>
        </el-dropdown>
        <el-button v-if="this.$root.adminLoggedIn" @click="logout" type="text" class="header-element">
          {{ $t('msg.logout') }}
        </el-button>
      </div>
    </el-row>
    <router-view/>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'app',
  methods: {
    changeLang(command) {
      this.$i18n.locale = command
    },
    logout() {
      axios.post('/api/logout')
      .then(() => {
        this.$root.adminLoggedIn = false
      })
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 1rem;
  min-width: 300px;
}
.header-element {
  margin: 0px 20px;
  font-size: 1rem;
}
@media (min-width: 1301px) {
  html {
    font-size: 18px;
  }
}
@media (min-width: 501px) and (max-width: 1300px) {
  html {
    font-size: 16px;
  }
}
@media (max-width: 500px) {
  html {
    font-size: 14px;
  }
}
</style>
