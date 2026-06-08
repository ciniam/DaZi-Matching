# 搭子小纵队 🎯

> 全栈伙伴匹配系统 — 基于标签 + 智能算法的兴趣搭子匹配平台

## 📸 项目简介

搭子小纵队是一个面向年轻人群体的伙伴匹配系统，用户可以根据兴趣标签找到志同道合的活动搭子（饭搭子、运动搭子、学习搭子等），同时支持创建和管理活动队伍、在线聊天等功能。

## ✨ 核心功能

| 模块 | 功能 |
|------|------|
| 🏠 主页 | 推荐用户列表、匹配模式切换、滚动加载更多 |
| 🔍 搜索 | 根据标签搜索用户，支持多标签组合搜索 |
| 🎯 智能匹配 | 基于编辑距离 + Jaccard 相似度 + 余弦相似度 + IK分词的混合推荐算法 |
| 👥 队伍管理 | 创建/加入/退出/解散队伍，支持公开和加密两种类型 |
| 💬 在线聊天 | 基于 WebSocket 的实时聊天室 |
| 👤 用户中心 | 注册/登录/忘记密码、个人资料编辑、标签编辑 |
| 📧 邮箱验证 | 发送QQ邮箱验证码完成注册和密码重置 |
| 📷 图片上传 | 七牛云 OSS 图片存储 |

## 🛠 技术栈

### 后端
- **SpringBoot 2.6** — 主框架
- **MyBatis Plus** — ORM
- **MySQL** — 关系型数据库
- **Redis** — 缓存 + Session 管理 + 分布式锁（Redisson）
- **WebSocket** — 实时通信
- **Elasticsearch** — 全文搜索（保留扩展）
- **HanLP** — 中文分词
- **Knife4j (Swagger)** — API 文档
- **七牛云 OSS** — 对象存储

### 前端
- **Vue 3** — 渐进式框架
- **TypeScript** — 类型安全
- **Vite** — 构建工具
- **Vant UI** — 移动端组件库
- **Vue Router** — 路由管理
- **Axios** — HTTP 请求

## 📁 项目结构

```
├── friend-main/          # 后端 SpringBoot 项目
│   └── friend-main/
│       ├── src/main/java/com/xiejun/
│       │   ├── common/       # 通用响应/错误码
│       │   ├── config/       # 配置类（Redis、Swagger、WebSocket等）
│       │   ├── constant/     # 常量
│       │   ├── controller/   # 控制器
│       │   ├── exception/    # 全局异常处理
│       │   ├── job/          # 定时任务
│       │   ├── mapper/       # MyBatis Mapper
│       │   ├── model/        # 实体/DTO/VO
│       │   ├── service/      # 业务逻辑
│       │   └── utils/        # 工具类
│       ├── src/main/resources/
│       │   ├── mapper/       # MyBatis XML
│       │   └── application.yml.example  # 配置文件模板
│       └── sql/              # 建表与测试数据 SQL
│
├── friend-front-main/    # 前端 Vue3 项目
│   └── friend-front-main/
│       └── src/
│           ├── components/   # 通用组件
│           ├── config/       # 路由配置
│           ├── layouts/      # 布局组件
│           ├── models/       # TypeScript 类型定义
│           ├── pages/        # 页面视图
│           ├── plugins/      # Axios 封装
│           ├── services/     # API 调用
│           └── states/       # 全局状态
│
└── sql/                  # 数据库脚本
```

## 🚀 快速启动

### 1. 环境准备

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+

### 2. 数据库

```bash
# 在 MySQL 中执行建表脚本
mysql> source friend-main/friend-main/sql/create_table.sql
# 可选：插入测试数据
mysql> source friend-main/friend-main/sql/test_data.sql
```

### 3. 后端启动

```bash
cd friend-main/friend-main

# 复制配置文件模板
cp src/main/resources/application.yml.example src/main/resources/application.yml

# 编辑 application.yml 填入你自己的数据库、Redis、邮箱、OSS配置
# vim src/main/resources/application.yml

# 启动
mvn spring-boot:run
```

### 4. 前端启动

```bash
cd friend-front-main/friend-front-main

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

访问 `http://localhost:3000` 即可看到应用。

## 🎓 技术亮点

### 混合推荐算法
用户匹配采用多维度相似度计算，综合以下算法加权评分：

- **编辑距离**（权重 0.5）— 衡量标签的字符串相似度
- **Jaccard 相似度**（权重 0.3）— 衡量标签集合的重叠度
- **余弦相似度**（权重 0.2）— 衡量标签的向量相似度
- **IK 分词 + Jaccard**（权重 0.3）— 对中文标签进行分词后再计算

### 其他亮点
- Redis Session 管理，替代传统 HTTP Session
- Redisson 分布式锁，保障定时任务单节点执行
- WebSocket 实现在线聊天室
- 七牛云 OSS 签名 URL，保护图片资源

## 📬 联系方式

- GitHub: [@ciniam](https://github.com/ciniam)
- 作者: xiejun

