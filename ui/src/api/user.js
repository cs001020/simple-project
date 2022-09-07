import request from "@/api";

// // 新增用户
// // 查询用户列表
// export function listUser(query) {
//     return request({
//         url: '/user',
//         method: 'get',
//         params: query
//     })
// }
//
// // 新增用户
// export function addUser(data) {
//     return request({
//         url: '/user',
//         method: 'post',
//         data: data
//     })
// }
export function login(data) {
    return request({
        url: '/login',
        method: 'post',
        data:data
    })
}
export function logout() {
    return request({
        url: '/logout',
        method: 'post',
    })
}
export function listUser(data) {
    return request({
        url: '/user',
        method: 'get',
        params:data
    })
}
export function getInfo() {
    return request({
        url: '/user/getInfo',
        method: 'get',
    })
}
export function addUser(data) {
    return request({
        url: '/user',
        method: 'post',
        data:data
    })
}
export function getUserById(id) {
    return request({
        url: '/user/'+id,
        method: 'get',
    })
}
export function editUser(data) {
    return request({
        url: '/user',
        method: 'put',
        data:data
    })
}
export function delUser(userId) {
    return request({
        url: '/user/'+userId,
        method: 'delete',
    })
}