import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueClipboard from 'vue-clipboard2'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import util from './util'
 
Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueClipboard)
Vue.prototype.util = util

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
