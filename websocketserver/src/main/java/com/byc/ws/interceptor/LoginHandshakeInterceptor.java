package com.byc.ws.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

/**
 * Created by baiyc
 * 2019/5/23/023 20:22
 * Description：
 */
@Slf4j
@AllArgsConstructor
public class LoginHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String utoken = getToken(request.getURI().toString());
        Principal sysuser = () -> "user"+utoken;
        attributes.put("user", sysuser);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }

    private String getToken(String uri) {
        int index = uri.indexOf("login_token=");
        int urlLength = uri.length();
        return uri.substring(index + 12, urlLength);
    }
}
