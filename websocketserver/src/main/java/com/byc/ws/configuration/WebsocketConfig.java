package com.byc.ws.configuration;

import com.byc.ws.handler.ChatWebSocketHandler;
import com.byc.ws.handler.LoginHandshakeHandler;
import com.byc.ws.interceptor.LoginHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by baiyc
 * 2019/5/23/023 19:44
 * Description：
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        ChatWebSocketHandler chatWebSocketHandler = new ChatWebSocketHandler();
        registry.addHandler(chatWebSocketHandler, "/chat").
                addInterceptors(new LoginHandshakeInterceptor()).
                setHandshakeHandler(new LoginHandshakeHandler()).setAllowedOrigins("*");
    }
}
