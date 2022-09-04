// 导入用来创建路由和确定路由模式的两个方法
import {
    createRouter,
    createWebHistory
} from 'vue-router'
import store from '@/store'
import storage from '@/utils/storage'

/**
 * 定义路由信息
 *
 */
const routes = [{
    name: 'login',
    path: '/login',
    component: () => import('@/components/login/login-index'),
}, {
        name: 'main',
        alias: '/',
        path: '/main',
        component: () => import('@/components/main/main-index'),
        children:[
            {
                name: 'user',
                path: '/user',
                component: () => import('@/components/system/user/user-index'),
            }
        ]
    }]

// 创建路由实例并传递 `routes` 配置
// 我们在这里使用 html5 的路由模式，url中不带有#，部署项目的时候需要注意。
const router = createRouter({
    history: createWebHistory(),
    routes,
})


// 全局的路由守卫
router.beforeEach((to) => {
    //访问登陆页面直接放行
    if (to.name==="login"){
        return true;
    }
    //检查是否登陆
    if (!store.getters.ISLOGIN){
        //未登陆跳转登陆页面
        if (!storage.getSessionObject("loginUser")) {
            router.push({name: "login"})
        }else {
            store.dispatch("RECOVERY_USER")
        }
    }
    return true
})

// 讲路由实例导出
export default router