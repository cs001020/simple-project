<template>
  <el-container>
    <el-aside width="790px"
    ><el-image
        fit="fill"
        :src="require('@/assets/image/background.png')"
    ></el-image
    ></el-aside>
    <el-main>
      <el-card class="box-card login-card">
        <span class="login-title">后台管理系统</span>
        <span class="login-tip">welcome 欢迎登陆</span>
        <el-form
            ref="user"
            :model="user"
            label-width="80px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input
                v-model="user.username"
                placeholder="请输入用户"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="user.password"
                type="password"
                placeholder="请输入密码"
                show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login">登陆</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "login-index",
  data() {
    return {
      // 用户信息
      url: "../../assets/image/login_left.png",
      user: {
        username: "admin",
        password: "123456",
      },
    };
  },
  methods:{
    login(){
      this.$router.push({name:'main'})
    }
  }
};
</script>

<style scope>
.el-image {
  height: 885px;
  width: 750px;
}
.el-main {
  position: relative;
}
.login-card {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 480px;
  height: 400px;
  padding: 50px;
}
.login-title {
  width: 459px;
  height: 70px;
  font-size: 40px;
  font-family: AlibabaPuHuiTiB;
  color: #333333;
  line-height: 90px;
  letter-spacing: 1px;
  font-weight: 800;
  display: block;
  text-align: left;
}
.login-tip {
  width: 319px;
  height: 70px;
  font-size: 30px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
  line-height: 90px;
  letter-spacing: 1px;
  display: block;
  text-align: left;
  margin-bottom: 30px;
}
</style>