import {login,logout,getInfo} from "@/api/user";
import  storage from '@/utils/storage'
const user = {
    state: {
        username:"",
        nickname:"",
        token:"",
        roles:[],
        permissions:[]
    },
    getters:{
        ISLOGIN(state){
            return state.username!==''&&state.token!=='';
        },
        permissions(state){
            return state.permissions
        },
        roles(state){
            return state.roles
        }
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
        SAVA_ROLES(state,roles){
            state.roles=roles;
        },
        SAVA_PERMISSIONS(state,permissions){
            state.permissions=permissions;
        }
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
                logout().then(res=>{
                    //将获取的数据保存起来
                    commit("SAVE_USERNAME",'');
                    commit("SAVE_NICKNAME",'');
                    commit("SAVE_TOKEN",'');
                    commit("SAVA_PERMISSIONS",[]);
                    commit("SAVA_ROLES",[])
                    storage.remove("loginUser")
                    resolve(res)
                })
            })
        },
        RECOVERY_USER({commit}){
            let loginUser = storage.getSessionObject("loginUser");
            // console.log(loginUser)
            if (loginUser){
                commit("SAVE_USERNAME",loginUser.user.userName);
                commit("SAVE_NICKNAME",loginUser.user.nickName);
                commit("SAVE_TOKEN",loginUser.token);
            }
        },
        GET_INFO({commit}){
            return new  Promise(function (resolve){
                getInfo().then(res=>{
                    // console.log(res)
                    commit("SAVA_ROLES",res.data.roles)
                    commit("SAVA_PERMISSIONS",res.data.perms)
                    resolve()
                })
            })

        }

    }
}

export default user