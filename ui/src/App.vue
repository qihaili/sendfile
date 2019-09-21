<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Upload/> -->
    <el-row>
      <div>
        <i class="el-icon-s-promotion" style="margin-right: 1rem; font-size: 4rem; color: #409EFF; vertical-align: middle;"></i><span style="font-size: 2.5rem; font-weight: bold; color: #606266; vertical-align: middle;">Send File</span>
      </div>
      <div style="margin: 1rem;">
        <el-dropdown @command="changeLang">
          <span style="cursor: pointer; font-size: medium;">
          {{ $t('msg.languageName') }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="(value, key) in $i18n.messages" :command="key" :key="key" :disabled="$i18n.locale == key">{{ value.msg.languageName }}</el-dropdown-item>
            <!-- <el-dropdown-item command="en-US">English</el-dropdown-item> -->
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-row>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app',
  methods: {
    changeLang(command) {
      this.$i18n.locale = command
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
@media (min-width: 1300px) {
  html {
    font-size: 20px;
  }
}
@media (min-width: 650px) and (max-width: 1300px) {
  html {
    font-size: 16px;
  }
}
@media (max-width: 650px) {
  html {
    font-size: 14px;
  }
}
</style>
