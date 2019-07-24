import Vue from 'vue'
import Router from 'vue-router'
import Upload from './components/Upload'
import Files from './components/Files'

Vue.use(Router)

export default new Router({
  routes: [
    // {
    //   path: '/',
    //   name: 'HelloWorld',
    //   component: HelloWorld
    // },
    {
      path: '/',
      // redirect: '/upload', // 默认页面
      component: Upload
    },
    {
      path: '/:shareId',
      // redirect: '/upload', // 默认页面
      component: Files
    }
  ]
})