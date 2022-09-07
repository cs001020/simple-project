<template>
  <el-form :inline="true" :model="queryParam" class="demo-form-inline">
    <el-form-item label="用户名">
      <el-input v-model="queryParam.userName" placeholder="用户名"/>
    </el-form-item>
    <el-form-item label="昵称">
      <el-input v-model="queryParam.nickName" placeholder="昵称"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
  </el-form>
  <el-row>
    <el-button :icon="Search" circle/>
    <el-button v-hasRole="['admin']" type="primary" :icon="Edit" circle/>
    <el-button type="success" :icon="Plus" @click="create" circle/>
    <el-button type="info" :icon="Message" circle/>
    <el-button type="warning" :icon="Star" circle/>
    <el-button type="danger" :icon="Delete" circle/>
  </el-row>
  <el-table ref="tableRef" row-key="userName" :data="tableData" style="width: 100%;">
    <el-table-column prop="userName" label="用户名" width="180"/>
    <el-table-column prop="nickName" label="昵称" width="180"/>
    <el-table-column prop="email" label="邮箱"/>
    <el-table-column label="Operations">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
        >编辑</el-button
        >
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
        >删除</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <el-dialog
      v-model="dialogVisible"
      :title="title"
      width="30%"
      :before-close="handleClose"
  >
    <el-form :model="userForm" label-width="120px">
      <el-form-item label="用户名">
        <el-input v-model="userForm.userName" />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="userForm.nickName" />
      </el-form-item>
      <el-form-item label="密码" v-if="userForm.userID===null">
        <el-input v-model="userForm.password" />
      </el-form-item>
      <el-form-item label="确认密码" v-if="userForm.userID===null">
        <el-input v-model="userForm.confirmPassword" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm"
        >确认</el-button
        >
      </span>
    </template>
  </el-dialog>
  <el-pagination background layout="prev,pager,next" :total="total" :page-size="queryParam.size"
                 @current-change="changePage">
  </el-pagination>
</template>

<script setup>
import {ref} from "vue";
import {listUser,addUser,getUserById,editUser,delUser} from "@/api/user";
import {onMounted} from "vue";
import { ElMessage, ElMessageBox } from 'element-plus'

import {
  Plus,
  Delete,
  Edit,
  Message,
  Search,
  Star,
} from '@element-plus/icons-vue'

const queryParam = ref({
  size: 2,
  page: 0,
  userName: "",
  nickName: ""
});

let userForm=ref({
  userID:null,
  userName:'',
  nickNAme:'',
  password:'',
  confirmPassword:""
})

let dialogVisible=ref(false);
let title=ref("")

const tableData = ref([])
const total = ref(0)

const getList = function () {
  listUser(queryParam.value).then(res => {
    // console.log(res)
    tableData.value = res.data.content
    total.value = res.data.totalElements
    queryParam.value.size = res.data.size
  })
}
onMounted(() => {
  getList()
})

const changePage = async function (current) {
  queryParam.value.page = current - 1
  getList()
}
const onSubmit = function () {
  getList()
}

const  submitForm=function (){
  delete userForm.value.confirmPassword;
  if (userForm.value.userID===null){
      addUser(userForm.value).then(res=>{
      // console.log(res)
        if (res.status===200){
          dialogVisible.value=false
        }
        getList()
    })
  }else {
    editUser(userForm.value).then(res=>{
      // console.log(res)
      if (res.status===200){
        dialogVisible.value=false
      }
      getList()
    })
  }
}

const handleEdit=function (index,row){
  getUserById(row.userId).then(res=>{
    userForm.value=res.data
    title.value="修改用户"
    dialogVisible.value=true
  })
}
let create=function (){
  userForm.value={
    userID:null,
    userName:'',
    nickNAme:'',
    password:'',
    confirmPassword:""
  }
  title.value="新增用户"
  dialogVisible.value=true
}

const handleDelete=function (index,row){
  ElMessageBox.alert('确认删除？', '温馨提示', {
    // if you want to disable its autofocus
    // autofocus: false,
    confirmButtonText: '确定',
    callback: (action) => {
      // console.log(action)
      if (action==='confirm'){
        delUser(row.userId).then(res=>{
          if (res.status===200){
            getList();
            ElMessage({
              type: 'info',
              message: "删除成功",
            })
          }
        })
      }else {
        ElMessage({
          type: 'info',
          message: "已取消",
        })
      }
    },
  })

}
</script>


<style>

</style>
