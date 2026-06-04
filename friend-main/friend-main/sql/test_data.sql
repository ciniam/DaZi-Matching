-- ============================================
-- 测试数据：用户、队伍、用户-队伍关系
-- 密码均为 12345678 (MD5: f941635aa2f0875d6b6d01907fdc843a)
-- ============================================

USE partnermatching;

-- ====== 用户数据 (20条) ======
-- 密码: MD5("my" + "12345678") = f941635aa2f0875d6b6d01907fdc843a
INSERT INTO user (username, userAccount, avatarUrl, gender, userPassword, phone, email, userStatus, userRole, planetCode, tags, profile) VALUES
('篮球少年', 'user02', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002001', 'user02@test.com', 0, 0, 'P002', '["男","篮球","NBA","运动"]', '热爱篮球，寻找球友一起打球'),
('峡谷召唤师', 'user03', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002002', 'user03@test.com', 0, 0, 'P003', '["男","王者荣耀","游戏","电竞"]', '王者荣耀重度玩家，求组队上分'),
('编程达人', 'user04', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002003', 'user04@test.com', 0, 0, 'P004', '["男","编程","Java","Python"]', '全栈开发工程师，寻找技术伙伴'),
('健身辣妹', 'user05', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002004', 'user05@test.com', 0, 0, 'P005', '["女","健身","瑜伽","跑步"]', '健身爱好者，每周5练，一起自律'),
('音乐猎人', 'user06', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002005', 'user06@test.com', 0, 0, 'P006', '["男","音乐","吉他","摇滚"]', '独立音乐人，寻找乐队成员'),
('旅行青蛙', 'user07', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002006', 'user07@test.com', 0, 0, 'P007', '["女","旅行","摄影","美食"]', '背包客一枚，下一个目的地是川西'),
('动漫宅', 'user08', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002007', 'user08@test.com', 0, 0, 'P008', '["男","动漫","二次元","绘画"]', '追番达人，CP展常客'),
('读书人', 'user09', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002008', 'user09@test.com', 0, 0, 'P009', '["女","读书","写作","咖啡"]', '每月读5本书，寻找读书会伙伴'),
('美食探险家', 'user10', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002009', 'user10@test.com', 0, 0, 'P010', '["男","美食","探店","做饭"]', '周末必去打卡新餐厅，约饭友'),
('滑板青年', 'user11', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002010', 'user11@test.com', 0, 0, 'P011', '["男","滑板","街舞","潮流"]', '滑板新手，求大佬带飞'),
('摄影老法师', 'user12', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002011', 'user12@test.com', 0, 0, 'P012', '["男","摄影","旅行","无人机"]', '索尼党，周末约拍'),
('铲屎官', 'user13', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002012', 'user13@test.com', 0, 0, 'P013', '["女","宠物","猫","狗狗"]', '家有两只英短，寻找宠物群'),
('算法侠', 'user14', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002013', 'user14@test.com', 0, 0, 'P014', '["男","算法","刷题","LeetCode"]', '刷题800道，求组队打周赛'),
('电影迷', 'user15', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002014', 'user15@test.com', 0, 0, 'P015', '["女","电影","剧集","影评"]', '豆瓣标记1000+，寻找观影搭子'),
('桌游爱好者', 'user16', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002015', 'user16@test.com', 0, 0, 'P016', '["男","桌游","三国杀","狼人杀"]', '周末桌游局组起来'),
('越野跑者', 'user17', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002016', 'user17@test.com', 0, 0, 'P017', '["男","越野跑","马拉松","户外"]', '全马PB330，寻找越野跑搭子'),
('花艺师', 'user18', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002017', 'user18@test.com', 0, 0, 'P018', '["女","花艺","手工","茶道"]', '开花店的自由职业者'),
('日语同好', 'user19', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002018', 'user19@test.com', 0, 0, 'P019', '["女","日语","日本","动漫"]', 'JLPT N1，一起学日语看日剧'),
('投资新手', 'user20', '', 1, 'f941635aa2f0875d6b6d01907fdc843a', '13800002019', 'user20@test.com', 0, 0, 'P020', '["男","投资","股票","理财"]', '理财小白，寻找投资交流群'),
('电竞少女', 'user21', '', 0, 'f941635aa2f0875d6b6d01907fdc843a', '13800002020', 'user21@test.com', 0, 0, 'P021', '["女","游戏","LOL","电竞","二次元"]', 'LOL钻石段位，求开黑队友');

-- ====== 队伍数据 (6条) ======
-- 注意：表中已有1个队伍(id=1)，新队伍id从2开始
INSERT INTO team (name, description, maxNum, expireTime, userId, status, password, avatarUrl) VALUES
('周六篮球局', '本周六下午2点，奥体中心篮球馆，4v4半场比赛', 8, '2026-06-30 23:59:59', 1, 0, '', ''),
('峡谷开黑小队', '每天晚上8点到11点稳定在线，王者30星以上来', 5, '2026-07-15 00:00:00', 3, 0, '', ''),
('LeetCode刷题打卡', '每天至少刷3道题，互相监督，分享解题思路', 10, '2026-12-31 23:59:59', 14, 1, '', ''),
('周末登山徒步', '每周末组织一次周边登山活动，难度初级到中级', 15, '2026-09-01 00:00:00', 7, 1, '', ''),
('加密讨论组', '私密小组，仅限邀请', 5, '2026-08-01 00:00:00', 1, 2, 'abc123', ''),
('摄影采风团', '每月一次户外采风，一起拍大片', 12, '2026-10-31 23:59:59', 12, 2, 'photo888', '');

-- ====== 用户-队伍关系 ======
-- 周六篮球局 (id=2): 队长id=1 + id=2,id=3,id=5加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (1, 2, '2026-05-20 10:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (2, 2, '2026-05-20 11:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (3, 2, '2026-05-21 09:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (5, 2, '2026-05-21 10:00:00');

-- 峡谷开黑小队 (id=3): 队长id=3 + id=4,id=21加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (3, 3, '2026-05-21 10:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (4, 3, '2026-05-21 11:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (21, 3, '2026-05-21 12:00:00');

-- LeetCode刷题打卡 (id=4): 队长id=14 + id=4加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (14, 4, '2026-05-22 10:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (4, 4, '2026-05-22 10:30:00');

-- 周末登山徒步 (id=5): 队长id=7 + id=5,id=17加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (7, 5, '2026-05-23 08:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (5, 5, '2026-05-23 09:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (17, 5, '2026-05-23 09:30:00');

-- 加密讨论组 (id=6): 队长id=1 + id=2加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (1, 6, '2026-05-24 10:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (2, 6, '2026-05-24 10:30:00');

-- 摄影采风团 (id=7): 队长id=12 + id=7,id=18加入
INSERT INTO user_team (userId, teamId, joinTime) VALUES (12, 7, '2026-05-25 10:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (7, 7, '2026-05-25 11:00:00');
INSERT INTO user_team (userId, teamId, joinTime) VALUES (18, 7, '2026-05-25 12:00:00');
