package com.byc.ws.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by dongt
 * 2019/1/14/014 10:06
 */
@Data
public class NotificationClientReceive extends Notification {

    @JsonProperty("from")
    private String fromUser;

    public static NotificationClientReceive of(NotificationClientSend notificationClientSend) {
        NotificationClientReceive notificationClientReceive = new NotificationClientReceive();
        notificationClientReceive.setContent(filter(notificationClientSend.getContent()));
        notificationClientReceive.setMessageType(notificationClientSend.getMessageType());
        notificationClientReceive.setSubtype(notificationClientSend.getSubtype());
        notificationClientReceive.setMsgId(notificationClientSend.getMsgId());
        notificationClientReceive.setParam(notificationClientSend.getParam());
        notificationClientReceive.setTimestamp(notificationClientSend.getTimestamp());
        return notificationClientReceive;
    }

    private static String filter(String content) {
        try {
            return URLDecoder.decode(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
