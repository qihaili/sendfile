import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueClipboard from 'vue-clipboard2'
import VueI18n from 'vue-i18n'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import util from './util'
import './assets/icon/iconfont.css'
 
Vue.use(VueI18n);
Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueClipboard)
Vue.prototype.util = util

const i18n = new VueI18n({
  // locale: 'en-US',    // 语言标识
  locale: navigator.language || navigator.userLanguage,
  //this.$i18n.locale // 通过切换locale的值来实现语言切换
  messages: {
    'zh-CN': require('./lang/zh'),   // 中文语言包
    'en-US': require('./lang/en')    // 英文语言包
  }
})

new Vue({
  router,
  render: h => h(App),
  i18n
}).$mount('#app')
