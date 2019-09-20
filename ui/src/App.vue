<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Upload/> -->
    <el-row style="margin-bottom: 30px;">
      <div>
        <i class="el-icon-s-promotion" style="margin-right: 20px; font-size: 60px; color: #409EFF; vertical-align: middle;"></i><span style="font-size: 40px; font-weight: bold; color: #606266; vertical-align: middle;">Send File</span>
      </div>
      <div style="position: absolute; right: 100px; bottom: 0px;">
        <div>
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
  margin-top: 30px;
}
@media (min-width: 1300px) {
  html {
    font-size: 18px;
  }
}
@media (min-width: 580px) and (max-width: 1300px) {
  html {
    font-size: 18px;
  }
}
@media (max-width: 580px) {
  html {
    font-size: 14px;
  }
}
</style>
