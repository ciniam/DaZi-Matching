<template>
  <van-card style="display: block;width: 370px;text-align: left;"
            v-for="user in userList"
            :title="`姓名：${user.username}`"
            desc=""
            :thumb="user.avatarUrl || '/default-avatar.svg'"
  >
    <template #tags>
      <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px;margin-top: 8px">
        {{ tag }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini" @click="ToToken(user)">联系我</van-button>
    </template>
  </van-card>
  <van-empty v-if="!userList || userList.length < 1" description="结果为空"/>

</template>

<script setup lang="ts">
import {useRoute} from "vue-router";
import {useRouter} from "vue-router";
const router = useRouter();
const route = useRoute();
console.log(route.query)
const userList = JSON.parse(route.query.users);
userList.forEach((user: { tags: string; }) => {
  if (user.tags) {
    user.tags = JSON.parse(user.tags);
  }
})
const ToToken = (user: any) => {
  router.push({
    path: "/user/Token",
    query: {
      userId: String(user.id),
      avatarUrl: user.avatarUrl || '',
      username: user.username || '',
    }
  })
}
</script>

<style scoped>

</style>