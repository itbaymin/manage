package com.byc.ws.handler;

import com.byc.ws.configuration.WebsocketSessionWrapper;
import com.byc.ws.message.NotificationClientReceive;
import com.byc.ws.message.NotificationClientSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by baiyc
 * 2019/5/23/023 19:48
 * Description：
 */
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    /** 用户id => 多终端会话 */
    private final static Map<String, Set<WebsocketSessionWrapper>> SESSIONS = new ConcurrentHashMap<>(256);

    protected ObjectMapper objectMapper = new ObjectMapper();//序列化使用

    //接收文本消息，并发送出去
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("处理消息为：{}",message.getPayload());
        String userId = session.getPrincipal().getName();

        NotificationClientSend notificationClientSend = objectMapper.readValue(message.getPayload(), NotificationClientSend.class);
        notificationClientSend.initialize();

        //消息应答
        if(notificationClientSend.ack()) {
            log.info("消息：{}，应答成功",notificationClientSend.getMsgId());
            return;
        }

        // 发送消息模型转接收消息模型
        String toUserId = notificationClientSend.getToUser();
        NotificationClientReceive notificationClientReceive = NotificationClientReceive.of(notificationClientSend);
        notificationClientReceive.setFromUser(userId);


        // 发送消息给目标用户
        send(notificationClientReceive, toUserId);
    }

    //连接建立后处理
    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userid = session.getPrincipal().getName();
        log.info("建链成功,{} -> {}", userid, session.getId());
        Set<WebsocketSessionWrapper> websocketSessionWrappers = SESSIONS.get(userid);
        if(websocketSessionWrappers == null) {
            Set<WebsocketSessionWrapper> websocketSessionWrapperContainer = new HashSet<>();
            Set<WebsocketSessionWrapper> preNode = SESSIONS.putIfAbsent(userid, websocketSessionWrapperContainer);
            websocketSessionWrappers = ObjectUtils.defaultIfNull(preNode, websocketSessionWrapperContainer);
        }
        websocketSessionWrappers.add(new WebsocketSessionWrapper(session));

    }

    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("处理异常断开连接");
        if(session.isOpen()){
            session.close();
        }
        removeSession(session);
    }

    //连接关闭后处理
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("会话关闭 {}", closeStatus);
        removeSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    protected void send(NotificationClientReceive notificationClientReceive, String toUserId) {
        log.info("{}接收方",toUserId);
        Set<WebsocketSessionWrapper> websocketSessionWrappers = SetUtils.emptyIfNull(SESSIONS.get(toUserId));
        websocketSessionWrappers.forEach(websocketSessionWrapper -> {
            WebSocketSession realSession = websocketSessionWrapper.getSession();
            if(realSession != null && realSession.isOpen()) {
                try {
                    String msg = objectMapper.writeValueAsString(notificationClientReceive);
                    realSession.sendMessage(new TextMessage(msg));
                } catch (Exception e) {
                    log.error(String.format("发送消息失败: %s", notificationClientReceive.toString()), e);
                }
            }
        });
    }


    private void removeSession(WebSocketSession session) {
        String userId = session.getPrincipal().getName();
        Set<WebsocketSessionWrapper> websocketSessionWrappers = SetUtils.emptyIfNull(SESSIONS.get(userId));
        Iterator<WebsocketSessionWrapper> iterator = websocketSessionWrappers.iterator();
        while(iterator.hasNext()) {
            WebsocketSessionWrapper websocketSessionWrapper = iterator.next();
            if(websocketSessionWrapper.getSession().getId().equals(session.getId())) {
                iterator.remove();
            }
        }
        if(websocketSessionWrappers.isEmpty()) {
            SESSIONS.remove(userId);
        }
    }

}
