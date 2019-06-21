package com.byc.ws.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by baiyc
 * 2019/5/23/023 19:50
 * Description：
 */
@AllArgsConstructor
@Data
public class WebsocketSessionWrapper {

    private WebSocketSession session;

    @Override
    public int hashCode() {
        return session.getPrincipal().getName().hashCode();
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(this.getClass() != obj.getClass()) {
            return false;
        }
        WebsocketSessionWrapper websocketSessionWrapper = (WebsocketSessionWrapper) obj;
        if(!this.session.getId().equalsIgnoreCase(websocketSessionWrapper.getSession().getId())) {
            return false;
        }
        return true;
    }
}
