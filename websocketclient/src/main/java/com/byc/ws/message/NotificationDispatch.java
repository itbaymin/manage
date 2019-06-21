package com.byc.ws.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by baiyc
 * 2019/5/23/023 20:20
 * Description：
 */
@Data
public class NotificationDispatch extends NotificationClientSend {
    @JsonProperty("from")
    private String fromUser;

    public static NotificationDispatch of(NotificationClientSend notificationClientSend) {
        NotificationDispatch notificationDispatch = new NotificationDispatch();
        notificationDispatch.setContent(notificationClientSend.getContent());
        notificationDispatch.setMessageType(notificationClientSend.getMessageType());
        notificationDispatch.setMsgId(notificationClientSend.getMsgId());
        notificationDispatch.setParam(notificationClientSend.getParam());
        notificationDispatch.setSubtype(notificationClientSend.getSubtype());
        notificationDispatch.setTimestamp(notificationClientSend.getTimestamp());
        notificationDispatch.setToUser(notificationClientSend.getToUser());
        return notificationDispatch;
    }

    public NotificationClientReceive convert() {
        NotificationClientReceive notificationClientReceive = NotificationClientReceive.of(this);
        notificationClientReceive.setFromUser(fromUser);
        return notificationClientReceive;
    }
}
