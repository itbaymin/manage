package com.byc.ws;

import com.byc.ws.configuration.WebsocketClient;
import com.byc.ws.message.NotificationClientSend;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class WebsocketclientApplicationTests {

	@Test
	public void contextLoads() throws InterruptedException {
        WebsocketClient websocketClient = new WebsocketClient("ws://127.0.0.1:8084/chat", "bai");
        websocketClient.start();
        Map<String, String> param = new HashMap<>();
        param.put("trade_no", "qwe");
        websocketClient.send(NotificationClientSend.tradeStartNotification("useryong", "游戏开始", param));
    }

    public static void main(String[] args) {
        new WebsocketClient("ws://127.0.0.1:8084/chat","yong").start();
    }
}
