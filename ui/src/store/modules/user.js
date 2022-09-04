import {login,loginout} from "@/api/user";
import  storage from '@/utils/storage'
const user = {
    state: {
        username:"",
        nickname:"",
        token:""
    },
    getters:{   
    },
    mutations: {
        SAVE_USERNAME(state,username){
            state.username=username;
        },
        SAVE_NICKNAME(state,nickname){
            state.nickname=nickname;
        },
        SAVE_TOKEN(state,token){
            state.token=token;
        },
    },
    actions: {
        LOGIN({commit},user){
            return new  Promise(function (resolve){
                login(user).then(res=>{
                    //将获取的数据保存起来
                    commit("SAVE_USERNAME",res.data.user.userName);
                    commit("SAVE_NICKNAME",res.data.user.nickName);
                    commit("SAVE_TOKEN",res.data.token);
                    storage.saveSessionObject("loginUser",res.data)
                    resolve(res)
                })
            })
        },
        LOGOUT({commit}){
            return new  Promise(function (resolve){
                loginout().then(res=>{
                    //将获取的数据保存起来
                    commit("SAVE_USERNAME",'');
                    commit("SAVE_NICKNAME",'');
                    commit("SAVE_TOKEN",'');
                    storage.remove("loginUser")
                    resolve(res)
                })
            })
        }
    }
}

export default user