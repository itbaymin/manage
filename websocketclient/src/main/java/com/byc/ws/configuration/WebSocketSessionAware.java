package com.byc.ws.configuration;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by dongt
 * 2019/1/12/012 16:44
 */
public interface WebSocketSessionAware {
    void setWebSocketSession(WebSocketSession webSocketSession);
}
