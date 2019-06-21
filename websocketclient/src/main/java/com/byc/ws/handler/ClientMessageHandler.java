package com.byc.ws.handler;

import com.byc.ws.configuration.WebSocketSessionAware;
import com.byc.ws.message.NotificationClientReceive;
import com.byc.ws.message.NotificationClientSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by baiyc
 * 2019/5/23/023 21:02
 * Description：
 */
public class ClientMessageHandler extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private WebSocketSessionAware webSocketSessionAware;

    public ClientMessageHandler(WebSocketSessionAware webSocketSessionAware) {
        this.webSocketSessionAware = webSocketSessionAware;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connected...........");
        webSocketSessionAware.setWebSocketSession(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        String msg = message.getPayload();
        NotificationClientReceive notificationClientReceive = objectMapper.readValue(msg, NotificationClientReceive.class);
        System.out.println("收到("+ notificationClientReceive.getFromUser() +"): " + notificationClientReceive.getContent());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(NotificationClientSend.ackNotification(notificationClientReceive.getMsgId()))));
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }
}
