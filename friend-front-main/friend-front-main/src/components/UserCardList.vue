<template>
  <van-skeleton title avatar :row="3" :loading="props.loading" v-for="user in props.userList">
    <van-card
        :desc="user.profile"
        :title="`${user.username}`"
        :thumb="user.avatarUrl || '/default-avatar.svg'"
    >
      <template #tags>
        <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">
          {{ tag }}
        </van-tag>
      </template>
      <template #footer >
        <van-button size="mini" @click="ToToken(user)">联系我</van-button>
      </template>
    </van-card>
  </van-skeleton>
</template>

<script setup lang="ts">
import {UserType} from "../models/user";
import {useRouter} from "vue-router";

interface UserCardListProps {
  loading: boolean;
  userList: UserType[];
}

const router = useRouter();
const props = withDefaults(defineProps<UserCardListProps>(), {
  loading: true,
  // @ts-ignore
  userList: [] as UserType[],
});

const ToToken = (user: UserType) => {
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
