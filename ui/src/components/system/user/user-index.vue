<template>
  <el-form :inline="true" :model="queryParam" class="demo-form-inline">
    <el-form-item label="用户名">
      <el-input v-model="queryParam.userName" placeholder="用户名" />
    </el-form-item>
    <el-form-item label="昵称">
      <el-input v-model="queryParam.nickName" placeholder="昵称" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
  </el-form>

  <el-table ref="tableRef" row-key="userName" :data="tableData" style="width: 100%;">
    <el-table-column prop="userName" label="用户名" width="180"/>
    <el-table-column prop="nickName" label="昵称" width="180"/>
    <el-table-column prop="email" label="邮箱"/>
  </el-table>
  <el-pagination background layout="prev,pager,next" :total="total" :page-size="queryParam.size" @current-change="changePage">
  </el-pagination>
</template>

<script setup>
import {ref} from "vue";
import {listUser} from "@/api/user";
import {onMounted} from "vue";

const queryParam=ref({
    size :2,
    page:0,
    userName:"",
    nickName:""
})

const tableData = ref([])
const total = ref(0)

const getList=function (){
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
  queryParam.value.page=current-1
  getList()
}
const onSubmit=function (){
  getList()
}
</script>


<style>

</style>
