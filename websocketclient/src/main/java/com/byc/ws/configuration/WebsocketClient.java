package com.byc.ws.configuration;

import com.byc.ws.handler.ClientMessageHandler;
import com.byc.ws.message.NotificationClientSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by baiyc
 * 2019/5/23/023 20:59
 * Description：
 */
@Slf4j
public class WebsocketClient implements WebSocketSessionAware {
    public static final PingMessage PING = new PingMessage();
    private String url;
    private String loginToken;

    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    WebSocketSession webSocketSession;
    WebSocketConnectionManager manager;

    int retryCount;

    public WebsocketClient(String url, String loginToken) {
        this.url = url;
        this.loginToken = loginToken;
    }

    public void start() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        ClientMessageHandler messageHandler = new ClientMessageHandler(this);
        manager = new WebSocketConnectionManager(client, messageHandler, url+"?login_token="+loginToken);
        manager.start();

        startPing();
    }

    public static void main(String[] args) {
        WebsocketClient websocketClient = new WebsocketClient("ws://127.0.0.1:8084/chat", "1234");
        websocketClient.start();
        Map<String, String> param = new HashMap<>();
        param.put("trade_no", "qwe");
        websocketClient.send(NotificationClientSend.tradeStartNotification("user123", "游戏开始", param));
    }

    public void send(NotificationClientSend notificationClientSend) {
        if(webSocketSession.isOpen()) {
            try {
                synchronized (webSocketSession) {
                    webSocketSession.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(notificationClientSend)));
                }
            } catch (Exception e) {
                log.error("发送消息失败.", e);
            }
        }
    }

    public void startPing() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                if(webSocketSession == null || !webSocketSession.isOpen()) {
                    reconnect();
                } else {
                    webSocketSession.sendMessage(PING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 3, 5, TimeUnit.SECONDS);
    }

    public void reconnect() {
        System.out.println(String.format("第 %d 次尝试重连...", ++retryCount));
        manager.stop();
        manager.start();
    }

    @Override
    public void setWebSocketSession(WebSocketSession webSocketSession) {
        System.out.println("连接成功!");
        retryCount = 0;
        this.webSocketSession = webSocketSession;
    }

    public void disconnect() {
        scheduledExecutorService.shutdown();
        manager.stop();
    }
}
