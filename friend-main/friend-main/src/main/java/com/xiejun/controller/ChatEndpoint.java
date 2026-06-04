package com.xiejun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiejun.model.domain.Message;
import com.xiejun.model.domain.User;
import com.xiejun.config.GetHttpSessionConfigurator;
import com.xiejun.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.xiejun.constant.UserConstant.USER_LOGIN_STATE;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class ChatEndpoint {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> staticRedisTemplate;

    @PostConstruct
    public void init() {
        staticRedisTemplate = this.redisTemplate;
    }

    /**
     * 在线用户: userId -> ChatEndpoint
     */
    private static Map<Long, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    private Session session;
    private Long userId;
    private String username;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;

        // 从URL参数获取userId
        String queryString = session.getQueryString();
        Long uid = null;
        if (queryString != null) {
            for (String param : queryString.split("&")) {
                String[] kv = param.split("=");
                if (kv.length == 2 && "userId".equals(kv[0])) {
                    try {
                        uid = Long.parseLong(kv[1]);
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
            }
        }

        if (uid == null) {
            log.warn("WebSocket连接缺少userId参数");
            try { session.close(); } catch (Exception ignored) {}
            return;
        }

        // 从Redis查找用户
        String redisKey = USER_LOGIN_STATE + uid;
        ValueOperations<String, Object> ops = staticRedisTemplate.opsForValue();
        User user = (User) ops.get(redisKey);

        if (user == null) {
            log.warn("WebSocket连接: userId={} 未登录或session过期", uid);
            try { session.close(); } catch (Exception ignored) {}
            return;
        }

        this.userId = uid;
        this.username = user.getUsername();
        onlineUsers.put(uid, this);
        log.info("WebSocket连接建立: userId={}, username={}, 当前在线人数={}", uid, username, onlineUsers.size());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("收到消息: {}", message);
        if (StringUtils.isBlank(message)) {
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Message msg = mapper.readValue(message, Message.class);
            Long toUserId = msg.getToUserId();
            String data = msg.getMessage();

            if (toUserId == null || StringUtils.isBlank(data)) {
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String resultMessage = MessageUtils.getMessage(false, username, date, data);

            ChatEndpoint target = onlineUsers.get(toUserId);
            if (target != null) {
                target.session.getBasicRemote().sendText(resultMessage);
                log.info("消息已发送: from={} -> to={}", userId, toUserId);
            } else {
                // 对方不在线，通知发送方
                String offlineMsg = MessageUtils.getMessage(true, "系统", date, "对方不在线");
                this.session.getBasicRemote().sendText(offlineMsg);
            }
        } catch (Exception e) {
            log.error("消息处理失败", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (userId != null) {
            onlineUsers.remove(userId);
            log.info("WebSocket连接关闭: userId={}, username={}, 当前在线人数={}", userId, username, onlineUsers.size());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket错误: userId={}", userId, error);
    }
}
