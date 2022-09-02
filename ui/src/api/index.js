/**
 * axios的基本api
 * // 发送 POST 请求
 * axios({
 *  method: 'post',
 *  url: '/user/12345',
 *  data: {
 *    firstName: 'Fred',
 *    lastName: 'Flintstone'
 *  }
 *});
 * 
 */

 import axios from 'axios'

 // 创建axios实例
 const request = axios.create({
     // axios中请求配置有baseURL选项，表示请求URL公共部分
     baseURL: 'http://localhost:80/ssm/',
     // 超时
     timeout: 10000,
     // 设置Content-Type，规定了前后端的交互使用json
     headers: {'Content-Type': 'application/json;charset=utf-8'}
 })
 export default request