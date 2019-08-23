<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <Upload/> -->
    <el-row>
      <i class="el-icon-s-promotion" style="margin-right: 20px; font-size: 60px; color: #409EFF; vertical-align: middle;"></i><span style="font-size: 40px; font-weight: bold; color: #606266; vertical-align: middle;">Send File</span>
    </el-row>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
