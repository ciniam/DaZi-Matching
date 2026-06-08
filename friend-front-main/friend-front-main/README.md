# 搭子小纵队 - 前端

Vue 3 + TypeScript + Vite + Vant UI

## 页面结构

| 路由 | 页面 | 说明 |
|------|------|------|
| `/` | 主页 | 推荐用户、匹配模式切换 |
| `/team` | 队伍列表 | 公开/加密队伍切换 |
| `/user` | 个人中心 | 个人信息入口 |
| `/search` | 搜索 | 标签搜索用户 |
| `/user/login` | 登录 | 用户登录 |
| `/user/register` | 注册 | 用户注册 |
| `/user/edit` | 编辑 | 个人信息编辑 |
| `/user/edit/tag` | 标签 | 标签编辑 |
| `/user/team/create` | 我创建的 | 创建的队伍 |
| `/user/team/join` | 我加入的 | 加入的队伍 |
| `/user/Token` | 聊天室 | WebSocket 在线聊天 |

## 快速启动

```bash
npm install
npm run dev
```

默认访问 `http://localhost:3000`，后端 API 地址在 `src/plugins/myAxios.ts` 中配置。
