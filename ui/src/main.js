import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import store from '@/store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/style/common.css'
import directives from '@/directive'

let app=createApp(App);
//全局安装路由组件
app.use(router).use(store).use(ElementPlus);
// 安装所有的自定义指令
for(let key in directives ){
    // console.log(key)
    app.directive(key,directives[key]);
}
app.mount('#app')