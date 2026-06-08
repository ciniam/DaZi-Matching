# 搭子小纵队 - 后端

SpringBoot 2.6 + MyBatis Plus + Redis + WebSocket + Elasticsearch

## 技术要点

### 混合推荐算法
用户匹配综合三种算法加权评分：
- **编辑距离**（权重 0.5）
- **Jaccard 相似度**（权重 0.3）
- **余弦相似度**（权重 0.2）
- **IK 分词 + Jaccard**（中文标签，权重 0.3）

### API 文档
启动后访问 `http://localhost:8080/api/doc.html` 查看 Swagger 接口文档。

### 配置说明
1. 复制 `src/main/resources/application.yml.example` 为 `application.yml`
2. 填入你自己的 MySQL、Redis、邮箱 SMTP、七牛云 OSS 配置

## 快速启动

```bash
mvn spring-boot:run
```
