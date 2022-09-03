<template>
<router-view/>
</template>

<script>
export default {
  name: 'HelloWorld',
  mounted() {
    this.$router.push({name:'login'})
  }
}
</script>

<style>

</style>
